package net.archers.client.entity;

import net.archers.ArchersMod;
import net.archers.client.render.ArcherRenderLayers;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationDefinition;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
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
				? RenderLayers.entityTranslucent(texture)
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
		this.spawnAnimation = DirewolfEntityAnimations.spawn.createAnimation(root);
		this.biteAnimation = DirewolfEntityAnimations.bite.createAnimation(root);
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 16.0F, 0.0F));

		ModelPartData right_back_leg = root.addChild("right_back_leg", ModelPartBuilder.create().uv(12, 42).mirrored().cuboid(-1.0F, -3.0F, -2.95F, 3.0F, 7.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
		.uv(0, 45).mirrored().cuboid(-1.0F, 1.0F, 3.05F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.origin(-4.0F, -2.0F, 6.0F));

		ModelPartData left_back_leg = root.addChild("left_back_leg", ModelPartBuilder.create().uv(12, 42).cuboid(-2.0F, -3.0F, -2.95F, 3.0F, 7.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 45).cuboid(-2.0F, 1.0F, 3.05F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(4.0F, -2.0F, 6.0F));

		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(28, 24).cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.0F, -6.0F, -16.0F, 10.0F, 11.0F, 10.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -4.0F, 5.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 21).cuboid(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 21).cuboid(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 34).cuboid(-2.0F, -1.0F, -10.0F, 4.0F, 3.0F, 5.0F, new Dilation(0.0F))
		.uv(11, 58).cuboid(-2.0F, 2.0F, -10.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -2.0F, -16.0F));

		ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(47, 50).mirrored().cuboid(-5.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.0F, -4.0F, -1.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(47, 50).cuboid(0.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -4.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData mouith = head.addChild("mouith", ModelPartBuilder.create().uv(30, 42).cuboid(-1.5F, 0.0F, -4.0F, 3.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(30, 48).cuboid(-1.5F, -1.0F, -4.0F, 3.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 2.0F, -5.0F));

		ModelPartData right_ear = head.addChild("right_ear", ModelPartBuilder.create().uv(21, 22).mirrored().cuboid(-1.25F, -3.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.origin(-2.75F, -4.0F, -0.5F));

		ModelPartData left_ear = head.addChild("left_ear", ModelPartBuilder.create().uv(21, 22).cuboid(-0.75F, -3.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(2.75F, -4.0F, -0.5F));

		ModelPartData right_front_leg = body.addChild("right_front_leg", ModelPartBuilder.create().uv(50, 3).mirrored().cuboid(-0.25F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.origin(-5.0F, 0.0F, -12.0F));

		ModelPartData left_front_leg = body.addChild("left_front_leg", ModelPartBuilder.create().uv(50, 3).cuboid(-2.75F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(5.0F, 0.0F, -12.0F));

		ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(28, 9).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -2.5F, 4.0F));
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

	// Animations

	private static final Vector3f TEMP = new Vector3f();

	/// Since 1.21.2 a keyframe clip is bound to the model's parts once ([AnimationDefinition#createAnimation])
	/// instead of being resolved per frame.
	private final Animation spawnAnimation;
	private final Animation biteAnimation;

	@Override
	public void setAngles(SpiritWolfRenderState state) {
		super.setAngles(state); // resets every part's transform
		this.setHeadAngles(state.relativeHeadYaw, state.pitch);
		// Same time/amplitude mapping as Animation#applyWalking (limbSwing → time, limbSwingAmount → scale),
		// but routed through ModelAnimations.seamlessLoop so the run clip's loop seam is interpolated with
		// wrapped catmull-rom neighbours instead of vanilla's clamped ones — removing the per-cycle hitch.
		long runTime = (long) (state.limbSwingAnimationProgress * 50F);
		float runAmount = Math.min(state.limbSwingAmplitude, 1F);
		ModelAnimations.seamlessLoop(this, DirewolfEntityAnimations.run, runTime, runAmount, TEMP);
		// No dedicated spawn animation yet — idle stands in (despawn plays it reversed)
		// double playback speed due to long animation
		this.spawnAnimation.apply(state.spawnAnimationState,   state.age, 2F);
		this.spawnAnimation.apply(state.despawnAnimationState, state.age, -2F);

		var anyAction = false;
		// Attack animation
		if (state.attackAnimationState.isRunning()) {
			animationForAttackVariant(state.attackVariant).apply(state.attackAnimationState, state.age, state.attackAnimationSpeed);
			anyAction = true;
		}
		// Idle animation: crossfaded against movement. The run animation already scales with
		// limbSwingAmplitude, so idle fades out over the same signal instead of cutting off —
		// full at standstill, gone by limbSwingAmplitude 0.33. The state clock always advances
		// so fading back in resumes the loop in phase instead of snapping.
		float idleWeight = anyAction ? 0F : MathHelper.clamp(1F - state.limbSwingAmplitude * 3F, 0F, 1F);
		if (idleWeight > 0F) {
			float weight = idleWeight;
			state.idleAnimationState.run(running ->
					ModelAnimations.seamlessLoop(this, DirewolfEntityAnimations.idle, running.getTimeInMilliseconds(state.age), weight, TEMP));
		}
	}

	private Animation animationForAttackVariant(int variant) {
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
