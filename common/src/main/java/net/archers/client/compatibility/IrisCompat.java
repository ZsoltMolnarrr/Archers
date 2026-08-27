package net.archers.client.compatibility;

import net.archers.client.render.ArcherRenderLayers;
import net.spell_engine.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// Declares this mod's own render pipelines to Iris, mirroring Spell Engine's `IrisCompatibility`
/// and ArmorModelAPI's `ShaderCompat`.
///
/// Iris is a compile-only dependency: every `IrisApi` reference sits behind the `isModLoaded` gate
/// and inside a nested class, so its absence at runtime never loads the API classes.
///
/// [#assignPipelines] is called once from the shared client init ([net.archers.client.ArchersClientMod#init]).
public final class IrisCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("Archers/IrisCompat");

    private IrisCompat() { }

    public static void assignPipelines() {
        if (!Platform.util().isModLoaded("iris")) {
            return;
        }
        try {
            Iris.assign();
        } catch (Throwable e) {
            LOGGER.warn("Failed to register Archers pipelines with Iris: {}", e.toString());
        }
    }

    /// Nested so the Iris API classes are only loaded when Iris is actually present.
    private static final class Iris {

        /// Since 1.21.11 Iris resolves the shader program to run from the `RenderPipeline`, not from
        /// the render layer: it looks the pipeline up in its own map and, when it isn't there, leaves
        /// the *vanilla* GLSL program in place while the pack's gbuffers are bound
        /// (`MixinShaderManager_Overrides`, plus a "Missing program ... in override list" error per
        /// drawn pipeline). A vanilla entity program writes colortex0 only, so the pack's deferred pass
        /// has no normals/lightmap/specular for those fragments and composites the draw away - the
        /// spirit body simply vanishes under every shader pack while looking correct without one.
        ///
        /// `EMISSIVE_ENTITIES` maps to Iris' `gbuffers_spidereyes` program (fullbright, no diffuse
        /// lighting), the same program vanilla mob eyes and `ENTITY_TRANSLUCENT_EMISSIVE` resolve to -
        /// which is what the spirit layer is built from
        /// ([ArcherRenderLayers] widens and reuses `ENTITY_EMISSIVE_SNIPPET`).
        ///
        /// The shadow-pass variant (`assignPipelineShadow`, new in Iris 1.11) is deliberately not
        /// called: it does not exist in 1.10.x and would hard-fail there, and a translucent emissive
        /// overdraw contributes nothing to a shadow map.
        static void assign() {
            var api = net.irisshaders.iris.api.v0.IrisApi.getInstance();
            var pipelines = ArcherRenderLayers.customPipelines();
            for (var pipeline : pipelines) {
                api.assignPipeline(pipeline, net.irisshaders.iris.api.v0.IrisProgram.EMISSIVE_ENTITIES);
            }
            LOGGER.info("Registered {} custom pipelines with Iris", pipelines.size());
        }
    }
}
