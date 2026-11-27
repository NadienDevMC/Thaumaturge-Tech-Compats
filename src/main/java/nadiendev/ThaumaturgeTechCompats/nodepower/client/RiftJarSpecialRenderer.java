package nadiendev.ThaumaturgeTechCompats.nodepower.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * The rift of the Rift Jar item (26.1 port of the 1.21.1 RiftJarItemRenderer). The item definition is a composite of
 * this renderer and the full jar model, so the jar itself is drawn by its normal block model.
 */
public final class RiftJarSpecialRenderer implements NoDataSpecialModelRenderer {
    public static final Identifier TYPE = ThaumaturgeTechCompats.id("rift_jar");

    private static final long TIME_WRAP = 1_000_000L;
    private static final float MILLIS_PER_TICK = 50.0F;

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, boolean glint, int outline) {
        if (!RiftJarClientConfig.enabled()) {
            return;
        }
        float time = (Util.getMillis() % TIME_WRAP) / MILLIS_PER_TICK;
        RiftJarVisual.submitItem(pose, collector, RiftJarVisual.sizeFor(RiftJarClientConfig.itemFill()), time);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        // The rift always stays inside the glass of the jar
        output.accept(new Vector3f(0.25F, 0.1F, 0.25F));
        output.accept(new Vector3f(0.75F, 0.75F, 0.75F));
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
            return new RiftJarSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
