package net.archers.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

/// Render layers owned by this mod.
///
/// Subclasses [RenderLayer] purely to reach the protected namespace a layer is assembled from:
/// `RenderLayer.of` and every `RenderPhase` constant below are `protected static`, so nothing
/// outside the hierarchy can build one.
public class ArcherRenderLayers extends RenderLayer {
    /// Never called - see the class doc for why this type exists at all.
    private ArcherRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode,
                               int expectedBufferSize, boolean hasCrumbling, boolean translucent,
                               Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    /// Vanilla [RenderLayer#getEntityTranslucentEmissive] with exactly one phase changed: this one
    /// writes depth.
    ///
    /// The vanilla layer sets `writeMaskState(COLOR_MASK)`, so an entity drawn on it leaves the depth
    /// buffer untouched. Everything drawn afterwards then depth-tests against whatever is *behind* the
    /// entity and wins, which is why a spirit rendered on it sinks behind water (the translucent
    /// terrain pass runs after entities) and behind its own drop shadow (`entity_shadow` is
    /// `LEQUAL`-tested and also unwritten, so it paints straight over the legs). `ALL_MASK` puts the
    /// silhouette into the depth buffer and both rejections happen on their own.
    ///
    /// Depth writing is only safe here because the emissive program discards at `alpha < 0.1`, and it
    /// tests the *raw texture* sample - before `vertexColor` and `ColorModulator` are multiplied in. So
    /// the transparent regions of the sheet write no depth and punch no invisible holes in the world,
    /// while partially transparent texels still blend. Note the corollary: fading a spirit out through
    /// the vertex color alpha would *not* start discarding it, so a fully faded model would still
    /// occlude. Fade by swapping the texture, or by depth-testing a fade-specific layer differently.
    ///
    /// Culling stays disabled, matching vanilla. It is tempting to enable it now that depth is written
    /// (it would spare the back faces a second blend), but the model's mane planes - `cube_r1` /
    /// `cube_r2`, zero-thickness cuboids - rely on it: a zero-depth box emits its NORTH and SOUTH quads
    /// coplanar with opposite winding, and only SOUTH carries art. Cull, and the mane disappears from
    /// one side.
    private static final Function<Identifier, RenderLayer> SPIRIT = Util.memoize(texture -> {
        var parameters = MultiPhaseParameters.builder()
                .program(ENTITY_TRANSLUCENT_EMISSIVE_PROGRAM)
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .cull(DISABLE_CULLING)
                .writeMaskState(ALL_MASK)
                .overlay(ENABLE_OVERLAY_COLOR)
                .build(true);
        return of("archers_spirit",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                1536,
                true,
                true,
                parameters);
    });

    /// Translucent, self-lit and depth-correct: the layer for summoned spirit bodies.
    /// Memoized per texture, mirroring vanilla's layer factories.
    public static RenderLayer spirit(Identifier texture) {
        return SPIRIT.apply(texture);
    }
}
