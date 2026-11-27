package nadiendev.ThaumaturgeTechCompats.device.client;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.device.DeviceTier;
import nadiendev.ThaumaturgeTechCompats.furnace.FurnaceIntegration;
import nadiendev.ThaumaturgeTechCompats.stabilizer.StabilizerIntegration;
import nadiendev.ThaumaturgeTechCompats.stabilizer.StabilizerTier;
import nadiendev.ThaumaturgeTechCompats.transducer.TransducerIntegration;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/** Thaumaturge texture to tier texture, per block, for the furnaces, stabilizers and transducers of this mod (same pairs as 1.21.1). */
public final class TieredDeviceSwaps {
    private static final Map<DeviceTier, Map<RenderType, RenderType>> FURNACES = new EnumMap<>(DeviceTier.class);
    private static final Map<StabilizerTier, Map<RenderType, RenderType>> STABILIZERS = new EnumMap<>(StabilizerTier.class);
    private static final Map<DeviceTier, Map<RenderType, RenderType>> TRANSDUCERS = new EnumMap<>(DeviceTier.class);

    private TieredDeviceSwaps() {
    }

    public static Map<RenderType, RenderType> furnace(Block block) {
        DeviceTier tier = FurnaceIntegration.tierOf(block);
        return tier == null ? null : FURNACES.computeIfAbsent(tier, TieredDeviceSwaps::buildFurnace);
    }

    public static Map<RenderType, RenderType> stabilizer(Block block) {
        StabilizerTier tier = StabilizerIntegration.tierOf(block);
        return tier == null ? null : STABILIZERS.computeIfAbsent(tier, TieredDeviceSwaps::buildStabilizer);
    }

    public static Map<RenderType, RenderType> transducer(Block block) {
        DeviceTier tier = TransducerIntegration.tierOf(block);
        return tier == null ? null : TRANSDUCERS.computeIfAbsent(tier, TieredDeviceSwaps::buildTransducer);
    }

    private static Map<RenderType, RenderType> buildFurnace(DeviceTier tier) {
        String name = FurnaceIntegration.blockName(tier);
        Map<RenderType, RenderType> swaps = new HashMap<>();
        cutout(swaps, "advanced_alchemical_furnace", name);
        cutout(swaps, "advanced_alchemical_furnace_on", name + "_on");
        cutout(swaps, "advanced_alchemical_furnace_tank", name + "_tank");
        cutout(swaps, "advanced_alchemical_furnace_tank_on", name + "_tank_on");
        cutout(swaps, "metal_thaumium", name + "_trim");
        return swaps;
    }

    private static Map<RenderType, RenderType> buildStabilizer(StabilizerTier tier) {
        Map<RenderType, RenderType> swaps = new HashMap<>();
        swaps.put(RenderTypes.entityCutout(thaumaturge("node_stabilizer")), RenderTypes.entityCutout(tier.baseTexture()));
        swaps.put(RenderTypes.entityTranslucent(thaumaturge("node_stabilizer_over")), RenderTypes.entityTranslucent(tier.overlayTexture()));
        return swaps;
    }

    private static Map<RenderType, RenderType> buildTransducer(DeviceTier tier) {
        Map<RenderType, RenderType> swaps = new HashMap<>();
        cutout(swaps, "node_converter", TransducerIntegration.blockName(tier));
        return swaps;
    }

    private static void cutout(Map<RenderType, RenderType> swaps, String original, String replacement) {
        swaps.put(RenderTypes.entityCutout(thaumaturge(original)),
                RenderTypes.entityCutout(ThaumaturgeTechCompats.id("textures/block/" + replacement + ".png")));
    }

    private static Identifier thaumaturge(String texture) {
        return Identifier.fromNamespaceAndPath("thaumaturge", "textures/block/" + texture + ".png");
    }
}
