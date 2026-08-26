package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.client.render.ArcherRenderLayers;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.spell_engine.client.compatibility.ShaderCompatibility;
import net.spell_engine.entity.ModelAnimations;
import org.joml.Vector3f;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class DirewolfEntityModel extends EntityModel<SpiritWolfRenderState> {
	private final ModelPart animationRoot;
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
		// Emissive looks right with vanilla rendering, but shaderpacks tag fullbright geometry
		// for bloom and blow it out — with shaders active, fall back to the lightmap-respecting
		// translucent layer (visibility in darkness is restored via the renderer's block-light
		// boost). Evaluated per frame, so toggling shaders in-game switches immediately.
		//
		// Both branches must write depth, or the spirit sinks behind water and behind its own
		// drop shadow. `getEntityTranslucent` already does (it leaves the write mask at its
		// `ALL_MASK` default); vanilla's emissive layer does not, hence the local copy — see
		// ArcherRenderLayers#spirit.
		super(root, texture -> ShaderCompatibility.isShaderPackInUse()
				? RenderTypes.entityTranslucent(texture)
				: ArcherRenderLayers.spirit(texture));
		this.animationRoot = root.getChild("root");
		this.right_back_leg = this.animationRoot.getChild("right_back_leg");
		this.left_back_leg = this.animationRoot.getChild("left_back_leg");
		this.body = this.animationRoot.getChild("body");
		this.head = this.body.getChild("head");
		this.mouith = this.head.getChild("mouith");
		this.right_ear = this.head.getChild("right_ear");
		this.left_ear = this.head.getChild("left_ear");
		this.right_front_leg = this.body.getChild("right_front_leg");
		this.left_front_leg = this.body.getChild("left_front_leg");
		this.tail = this.body.getChild("tail");
		this.spawnAnimation = DirewolfEntityAnimations.spawn.bake(root);
		this.biteAnimation = DirewolfEntityAnimations.bite.bake(root);
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition right_back_leg = root.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(12, 42).mirror().addBox(-1.0F, -3.0F, -2.95F, 3.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 45).mirror().addBox(-1.0F, 1.0F, 3.05F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -2.0F, 6.0F));

		PartDefinition left_back_leg = root.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(12, 42).addBox(-2.0F, -3.0F, -2.95F, 3.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 45).addBox(-2.0F, 1.0F, 3.05F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -2.0F, 6.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 24).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -6.0F, -16.0F, 10.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 5.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 21).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-2.0F, -1.0F, -10.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(11, 58).addBox(-2.0F, 2.0F, -10.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -16.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(47, 50).mirror().addBox(-5.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -4.0F, -1.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(47, 50).addBox(0.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition mouith = head.addOrReplaceChild("mouith", CubeListBuilder.create().texOffs(30, 42).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(30, 48).addBox(-1.5F, -1.0F, -4.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -5.0F));

		PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(21, 22).mirror().addBox(-1.25F, -3.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.75F, -4.0F, -0.5F));

		PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(21, 22).addBox(-0.75F, -3.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, -4.0F, -0.5F));

		PartDefinition right_front_leg = body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(50, 3).mirror().addBox(-0.25F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 0.0F, -12.0F));

		PartDefinition left_front_leg = body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(50, 3).addBox(-2.75F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, -12.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(28, 9).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 4.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}

	// HAND-WRITTEN CODE

	// Basic render features

	public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(Identifier.fromNamespaceAndPath(ArchersMod.ID, "direwolf"), "main");

	private void setHeadAngles(float headYaw, float headPitch) {
		headYaw = Mth.clamp(headYaw, -60, 60);
		headPitch = Mth.clamp(headPitch, -60, 60);
		head.yRot = headYaw * 0.017453292F;
		head.xRot = headPitch * 0.017453292F;
	}

	// Animations

	private static final Vector3f TEMP = new Vector3f();

	/// Since 1.21.2 a keyframe clip is bound to the model's parts once ([AnimationDefinition#createAnimation])
	/// instead of being resolved per frame.
	private final KeyframeAnimation spawnAnimation;
	private final KeyframeAnimation biteAnimation;

	@Override
	public void setupAnim(SpiritWolfRenderState state) {
		super.setupAnim(state); // resets every part's transform
		this.setHeadAngles(state.yRot, state.xRot);
		// Same time/amplitude mapping as Animation#applyWalking (limbSwing → time, limbSwingAmount → scale),
		// but routed through ModelAnimations.seamlessLoop so the run clip's loop seam is interpolated with
		// wrapped catmull-rom neighbours instead of vanilla's clamped ones — removing the per-cycle hitch.
		long runTime = (long) (state.walkAnimationPos * 50F);
		float runAmount = Math.min(state.walkAnimationSpeed, 1F);
		ModelAnimations.seamlessLoop(this, DirewolfEntityAnimations.run, runTime, runAmount, TEMP);
		// No dedicated spawn animation yet — idle stands in (despawn plays it reversed)
		// double playback speed due to long animation
		this.spawnAnimation.apply(state.spawnAnimationState,   state.ageInTicks, 2F);
		this.spawnAnimation.apply(state.despawnAnimationState, state.ageInTicks, -2F);

		var anyAction = false;
		// Attack animation
		if (state.attackAnimationState.isStarted()) {
			animationForAttackVariant(state.attackVariant).apply(state.attackAnimationState, state.ageInTicks, state.attackAnimationSpeed);
			anyAction = true;
		}
		// Idle animation: crossfaded against movement. The run animation already scales with
		// limbSwingAmplitude, so idle fades out over the same signal instead of cutting off —
		// full at standstill, gone by limbSwingAmplitude 0.33. The state clock always advances
		// so fading back in resumes the loop in phase instead of snapping.
		float idleWeight = anyAction ? 0F : Mth.clamp(1F - state.walkAnimationSpeed * 3F, 0F, 1F);
		if (idleWeight > 0F) {
			float weight = idleWeight;
			state.idleAnimationState.ifStarted(running ->
					ModelAnimations.seamlessLoop(this, DirewolfEntityAnimations.idle, running.getTimeInMillis(state.ageInTicks), weight, TEMP));
		}
	}

	private KeyframeAnimation animationForAttackVariant(int variant) {
		return switch (variant) {
			default -> this.biteAnimation;
		};
	}

	/// Maps a behaviour-defined attack variant number to one of this model's attack clips.
	/// Unknown variants fall back to the variant-1 default. Used by the renderer to derive the
	/// playback speed from the clip length.
	public static AnimationDefinition attackAnimationFor(int variant) {
		return switch (variant) {
			default -> DirewolfEntityAnimations.bite;
		};
	}
}
