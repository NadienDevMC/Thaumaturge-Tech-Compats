package nadiendev.ThaumaturgeTechCompats.device.client;

import com.leclowndu93150.thaumaturge.client.model.AdvancedAlchemicalFurnaceItemSpecialRenderer;
import com.leclowndu93150.thaumaturge.client.model.NodeStabilizerItemSpecialRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.joml.Vector3fc;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Item renderer of the tiered furnaces, stabilizers and transducers: the Thaumaturge item renderer of the base
 * device, drawn with the tier textures (26.1 port of the 1.21.1 Tiered*ItemRenderer). Item definition:
 * {@code {"type": "thaumaturgetechcompats:tiered_device", "block": "<block id>"}}.
 */
public final class TieredDeviceSpecialRenderer implements NoDataSpecialModelRenderer {
    public static final Identifier TYPE = ThaumaturgeTechCompats.id("tiered_device");

    private final NoDataSpecialModelRenderer delegate;
    private final Map<RenderType, RenderType> swaps;

    private TieredDeviceSpecialRenderer(NoDataSpecialModelRenderer delegate, Map<RenderType, RenderType> swaps) {
        this.delegate = delegate;
        this.swaps = swaps;
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, boolean glint, int outline) {
        delegate.submit(pose, TextureSwapCollector.wrap(collector, swaps), light, overlay, glint, outline);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        delegate.getExtents(output);
    }

    public record Unbaked(Identifier block) implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("block").forGetter(Unbaked::block)).apply(instance, Unbaked::new));

        @Override
        public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
            Block device = BuiltInRegistries.BLOCK.getValue(block);
            String path = block.getPath();
            if (path.endsWith("node_transducer")) {
                return new TieredDeviceSpecialRenderer(new NodeStabilizerItemSpecialRenderer(false, true), TieredDeviceSwaps.transducer(device));
            }
            if (path.endsWith("node_stabilizer")) {
                return new TieredDeviceSpecialRenderer(new NodeStabilizerItemSpecialRenderer(false, false), TieredDeviceSwaps.stabilizer(device));
            }
            return new TieredDeviceSpecialRenderer(new AdvancedAlchemicalFurnaceItemSpecialRenderer(), TieredDeviceSwaps.furnace(device));
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
