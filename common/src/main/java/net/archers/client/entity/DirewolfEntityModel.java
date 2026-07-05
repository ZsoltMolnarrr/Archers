package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.entity.SpiritWolfEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class DirewolfEntityModel extends SinglePartEntityModel<SpiritWolfEntity> {
	private final ModelPart root;
	private final ModelPart right_back_leg;
	private final ModelPart left_back_leg;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart mouith;
	private final ModelPart right_ear;
	private final ModelPart left_ear;
	private final ModelPart right_front_leg;
	private final ModelPart left_front_leg;
	private final ModelPart tail;

	public DirewolfEntityModel(ModelPart root) {
		this.root = root.getChild("root");
		this.right_back_leg = this.root.getChild("right_back_leg");
		this.left_back_leg = this.root.getChild("left_back_leg");
		this.body = this.root.getChild("body");
		this.head = this.body.getChild("head");
		this.mouith = this.head.getChild("mouith");
		this.right_ear = this.head.getChild("right_ear");
		this.left_ear = this.head.getChild("left_ear");
		this.right_front_leg = this.body.getChild("right_front_leg");
		this.left_front_leg = this.body.getChild("left_front_leg");
		this.tail = this.body.getChild("tail");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

		ModelPartData right_back_leg = root.addChild("right_back_leg", ModelPartBuilder.create().uv(12, 42).mirrored().cuboid(-1.0F, -3.0F, -2.95F, 3.0F, 7.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
		.uv(0, 45).mirrored().cuboid(-1.0F, 1.0F, 3.05F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-4.0F, -2.0F, 6.0F));

		ModelPartData left_back_leg = root.addChild("left_back_leg", ModelPartBuilder.create().uv(12, 42).cuboid(-2.0F, -3.0F, -2.95F, 3.0F, 7.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 45).cuboid(-2.0F, 1.0F, 3.05F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -2.0F, 6.0F));

		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(28, 24).cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.0F, -6.0F, -16.0F, 10.0F, 11.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 5.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 21).cuboid(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 21).cuboid(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 34).cuboid(-2.0F, -1.0F, -10.0F, 4.0F, 3.0F, 5.0F, new Dilation(0.0F))
		.uv(11, 58).cuboid(-2.0F, 2.0F, -10.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -16.0F));

		ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(47, 50).mirrored().cuboid(-5.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.0F, -4.0F, -1.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(47, 50).cuboid(0.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -4.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData mouith = head.addChild("mouith", ModelPartBuilder.create().uv(30, 42).cuboid(-1.5F, 0.0F, -4.0F, 3.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(30, 48).cuboid(-1.5F, -1.0F, -4.0F, 3.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, -5.0F));

		ModelPartData right_ear = head.addChild("right_ear", ModelPartBuilder.create().uv(21, 22).mirrored().cuboid(-1.25F, -3.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.75F, -4.0F, -0.5F));

		ModelPartData left_ear = head.addChild("left_ear", ModelPartBuilder.create().uv(21, 22).cuboid(-0.75F, -3.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.75F, -4.0F, -0.5F));

		ModelPartData right_front_leg = body.addChild("right_front_leg", ModelPartBuilder.create().uv(50, 3).mirrored().cuboid(-0.25F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-5.0F, 0.0F, -12.0F));

		ModelPartData left_front_leg = body.addChild("left_front_leg", ModelPartBuilder.create().uv(50, 3).cuboid(-2.75F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 0.0F, -12.0F));

		ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(28, 9).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.5F, 4.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	// HAND-WRITTEN CODE

	// Basic render features

	public static final EntityModelLayer TEXTURE = new EntityModelLayer(Identifier.of(ArchersMod.ID, "direwolf"), "main");

	private void setHeadAngles(float headYaw, float headPitch) {
		headYaw = MathHelper.clamp(headYaw, -60, 60);
		headPitch = MathHelper.clamp(headPitch, -60, 60);
		head.yaw = headYaw * 0.017453292F;
		head.pitch = headPitch * 0.017453292F;
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		root.render(matrices, vertices, light, overlay, color);
	}

	// Animations

	@Override
	public void setAngles(SpiritWolfEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float netHeadPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		this.setHeadAngles(netHeadYaw, netHeadPitch);
		this.animateMovement(DirewolfEntityAnimations.run, limbSwing, limbSwingAmount, 1F, 1F);
		// No dedicated spawn animation yet — idle stands in (despawn plays it reversed)
		this.updateAnimation(entity.spawnAnimationState,   DirewolfEntityAnimations.idle, ageInTicks, 1F);
		this.updateAnimation(entity.despawnAnimationState, DirewolfEntityAnimations.idle, ageInTicks, -1F);

		var anyAction = false;
		// Attack animation
		if (entity.attackAnimationState.isRunning()) {
			Animation attackAnim = attackAnimationFor(entity.getAttackVariant());
			float attackSpeed = entity.getAttackAnimationSpeed(attackAnim.lengthInSeconds() * 20F);
			this.updateAnimation(entity.attackAnimationState, attackAnim, ageInTicks, attackSpeed);
			anyAction = true;
		}
		// Idle animation (only if not doing any other action)
		if (!anyAction) {
			this.updateAnimation(entity.idleAnimationState, DirewolfEntityAnimations.idle, ageInTicks, 1F);
		}
	}

	// Maps a behaviour-defined attack variant number to one of this model's attack animations.
	// Unknown variants fall back to the variant-1 default.
	private static Animation attackAnimationFor(int variant) {
		return switch (variant) {
			default -> DirewolfEntityAnimations.bite;
		};
	}
}
