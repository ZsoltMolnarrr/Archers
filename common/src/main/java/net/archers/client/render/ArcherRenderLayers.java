package net.archers.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.archers.ArchersMod;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.spell_engine.api.render.CustomLayers;

import java.util.function.Function;

/// Render layers owned by this mod.
///
/// Since 1.21.11 a render layer is a [RenderSetup] (textures, lightmap/overlay usage, sorting hints)
/// over a [RenderPipeline] (shaders, blend, depth, cull, colour/depth write); the old
/// `RenderPhase`/`MultiPhaseParameters` assembly is gone, and so is the reason this class used to
/// extend [RenderLayer]. `RenderLayer.of` is still package-private, so the layer is built through
/// Spell Engine's [CustomLayers#create] escape hatch; the pipeline is derived from vanilla's
/// emissive snippet (widened in `archers.accesswidener`).
public final class ArcherRenderLayers {
    private ArcherRenderLayers() { }

    /// Vanilla `ENTITY_TRANSLUCENT_EMISSIVE` with exactly one state changed: this one writes depth.
    ///
    /// The vanilla pipeline sets `withDepthWrite(false)`, so an entity drawn on it leaves the depth
    /// buffer untouched. Everything drawn afterwards then depth-tests against whatever is *behind* the
    /// entity and wins, which is why a spirit rendered on it sinks behind water (the translucent
    /// terrain pass runs after entities) and behind its own drop shadow (`entity_shadow` is
    /// `LEQUAL`-tested and also unwritten, so it paints straight over the legs). Writing depth puts the
    /// silhouette into the depth buffer and both rejections happen on their own.
    ///
    /// Depth writing is only safe here because the emissive program discards at `alpha < 0.1`
    /// (`ALPHA_CUTOUT`), and it tests the *raw texture* sample - before `vertexColor` is multiplied in.
    /// So the transparent regions of the sheet write no depth and punch no invisible holes in the
    /// world, while partially transparent texels still blend. Note the corollary: fading a spirit out
    /// through the vertex color alpha would *not* start discarding it, so a fully faded model would
    /// still occlude. Fade by swapping the texture, or by depth-testing a fade-specific layer
    /// differently.
    ///
    /// Culling stays disabled, matching vanilla. It is tempting to enable it now that depth is written
    /// (it would spare the back faces a second blend), but the model's mane planes - `cube_r1` /
    /// `cube_r2`, zero-thickness cuboids - rely on it: a zero-depth box emits its NORTH and SOUTH quads
    /// coplanar with opposite winding, and only SOUTH carries art. Cull, and the mane disappears from
    /// one side.
    private static final RenderPipeline SPIRIT_PIPELINE = RenderPipeline.builder(RenderPipelines.ENTITY_EMISSIVE_SNIPPET)
            .withLocation(Identifier.of(ArchersMod.ID, "pipeline/spirit"))
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withShaderDefine("PER_FACE_LIGHTING")
            .withSampler("Sampler1")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(true)
            .build();

    private static final Function<Identifier, RenderLayer> SPIRIT = Util.memoize(texture ->
            CustomLayers.create("archers_spirit", RenderSetup.builder(SPIRIT_PIPELINE)
                    .texture("Sampler0", texture)
                    .useOverlay()
                    .crumbling()
                    .translucent()
                    .expectedBufferSize(1536)
                    .outlineMode(RenderSetup.OutlineMode.AFFECTS_OUTLINE)
                    .build()));

    /// Translucent, self-lit and depth-correct: the layer for summoned spirit bodies.
    /// Memoized per texture, mirroring vanilla's layer factories.
    public static RenderLayer spirit(Identifier texture) {
        return SPIRIT.apply(texture);
    }
}
