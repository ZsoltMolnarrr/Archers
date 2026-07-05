package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.entity.SpiritWolfEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.LightEmission;

import java.util.ArrayList;
import java.util.List;

public class DirewolfEntityRenderer extends MobEntityRenderer<SpiritWolfEntity, DirewolfEntityModel> {
    public static final Identifier TEXTURE =
            Identifier.of(ArchersMod.ID, "textures/entity/direwolf_spell.png");

    // The Spirit Wolf uses a translucent, non-depth-writing render layer. During the normal entity
    // pass (which runs before translucent terrain, particles and clouds) the later passes paint over
    // it, so parts of the model appear behind water/clouds/distant terrain. To fix this we don't draw
    // during the normal pass: we queue the entity and replay the full model render AFTER_TRANSLUCENT,
    // so it rasterizes on top. Mirrors Wizards' FireHydraRenderer.
    private static final List<Deferred> deferredQueue = new ArrayList<>();
    private boolean inDeferredPass = false;

    private record Deferred(SpiritWolfEntity entity, float yaw, float tickDelta, int light) {}

    public static void setup() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (deferredQueue.isEmpty()) {
                return;
            }
            var client = MinecraftClient.getInstance();
            var dispatcher = client.getEntityRenderDispatcher();
            var vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
            var matrices = context.matrixStack();
            Vec3d cam = context.camera().getPos();
            float tickDelta = context.tickCounter().getTickDelta(true);

            matrices.push();
            matrices.translate(-cam.x, -cam.y, -cam.z);
            for (Deferred d : deferredQueue) {
                SpiritWolfEntity entity = d.entity();
                if (!(dispatcher.getRenderer(entity) instanceof DirewolfEntityRenderer renderer)) {
                    continue;
                }
                double x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
                double y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
                double z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
                matrices.push();
                matrices.translate(x, y, z);
                renderer.inDeferredPass = true;
                renderer.render(entity, d.yaw(), d.tickDelta(), matrices, vertexConsumers, d.light());
                renderer.inDeferredPass = false;
                matrices.pop();
            }
            matrices.pop();
            vertexConsumers.draw();
            deferredQueue.clear();
        });
    }

    public DirewolfEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DirewolfEntityModel(context.getPart(DirewolfEntityModel.TEXTURE)), 0.5f);
    }

    @Override
    public void render(SpiritWolfEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!inDeferredPass && entity.isAlive()) {
            // Queue for the AFTER_TRANSLUCENT pass instead of drawing now.
            deferredQueue.add(new Deferred(entity, yaw, tickDelta, light));
            return;
        }
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(SpiritWolfEntity entity) {
        return TEXTURE;
    }

    public static final RenderLayer renderLayer = CustomLayers.spellObject(TEXTURE, LightEmission.GLOW, true);
    @Override
    protected RenderLayer getRenderLayer(SpiritWolfEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (showOutline) {
            return RenderLayer.getOutline(TEXTURE);
        }
        return renderLayer;
    }
}
