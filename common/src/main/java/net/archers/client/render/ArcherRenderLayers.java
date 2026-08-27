package net.archers.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.archers.ArchersMod;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.spell_engine.api.render.CustomLayers;

import java.util.List;
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
    /// The vanilla pipeline sets `DepthStencilState(LEQUAL, false)`, so an entity drawn on it leaves the depth
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
            .withLocation(Identifier.fromNamespaceAndPath(ArchersMod.ID, "pipeline/spirit"))
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withShaderDefine("PER_FACE_LIGHTING")
            .withSampler("Sampler1")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            // 26.1: `withDepthWrite`/`withDepthTestFunction` collapsed into one `DepthStencilState`.
            // This is `DepthStencilState.DEFAULT` spelled out: LEQUAL test *and* depth write.
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .build();

    private static final Function<Identifier, RenderType> SPIRIT = Util.memoize(texture ->
            CustomLayers.create("archers_spirit", RenderSetup.builder(SPIRIT_PIPELINE)
                    .withTexture("Sampler0", texture)
                    .useOverlay()
                    .affectsCrumbling()
                    .sortOnUpload()
                    .bufferSize(1536)
                    .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                    .createRenderSetup()));

    /// Translucent, self-lit and depth-correct: the layer for summoned spirit bodies.
    /// Memoized per texture, mirroring vanilla's layer factories.
    public static RenderType spirit(Identifier texture) {
        return SPIRIT.apply(texture);
    }

    /// Every [RenderPipeline] this class builds itself, i.e. the ones a shader mod cannot know about
    /// from vanilla's registry. Since 1.21.11 Iris maps *pipelines* (not render layers) to its shader
    /// programs, so each of these has to be declared to it once at client init - see
    /// [net.archers.client.compatibility.IrisCompat#assignPipelines].
    public static List<RenderPipeline> customPipelines() {
        return List.of(SPIRIT_PIPELINE);
    }
}
