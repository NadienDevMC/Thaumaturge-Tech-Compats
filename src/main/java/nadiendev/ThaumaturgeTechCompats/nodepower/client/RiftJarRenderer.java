package nadiendev.ThaumaturgeTechCompats.nodepower.client;

import com.mojang.blaze3d.vertex.PoseStack;
import nadiendev.ThaumaturgeTechCompats.nodepower.RiftJarBlock;
import nadiendev.ThaumaturgeTechCompats.nodepower.RiftJarBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.WeakHashMap;

/** Draws the trapped flux rift inside a Rift Jar, growing smoothly with the fill of the jar (26.1 port of the 1.21.1 renderer). */
public final class RiftJarRenderer implements BlockEntityRenderer<RiftJarBlockEntity, RiftJarRenderer.State> {
    private static final long TIME_WRAP = 100_000L;
    private static final float MAX_FRAME_SECONDS = 0.25F;
    private static final float MILLIS_PER_SECOND = 1000.0F;

    private final Map<RiftJarBlockEntity, Animation> animations = new WeakHashMap<>();

    public RiftJarRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(RiftJarBlockEntity jar, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderer.super.extractRenderState(jar, state, partialTick, cameraPos, crumbling);
        BlockState block = jar.getBlockState();
        int fill = block.hasProperty(RiftJarBlock.FILL) ? block.getValue(RiftJarBlock.FILL) : 0;
        state.size = RiftJarVisual.sizeFor(animate(jar, fill / (float) RiftJarBlock.MAX_FILL));
        Level level = jar.getLevel();
        state.time = (level == null ? 0L : level.getGameTime() % TIME_WRAP) + partialTick;
        state.seed = Mth.getSeed(jar.getBlockPos());
    }

    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!RiftJarClientConfig.enabled()) {
            return;
        }
        RiftJarVisual.submitWorld(pose, collector, state.size, state.time, state.seed);
    }

    private float animate(RiftJarBlockEntity jar, float target) {
        long now = Util.getMillis();
        Animation animation = animations.get(jar);
        if (animation == null) {
            animations.put(jar, new Animation(target, now));
            return target;
        }
        float seconds = Mth.clamp((now - animation.millis) / MILLIS_PER_SECOND, 0.0F, MAX_FRAME_SECONDS);
        float blend = 1.0F - (float) Math.exp(-seconds * RiftJarClientConfig.growSpeed());
        animation.value += (target - animation.value) * blend;
        animation.millis = now;
        return animation.value;
    }

    public static final class State extends BlockEntityRenderState {
        float size;
        float time;
        long seed;
    }

    private static final class Animation {
        private float value;
        private long millis;

        private Animation(float value, long millis) {
            this.value = value;
            this.millis = millis;
        }
    }
}
