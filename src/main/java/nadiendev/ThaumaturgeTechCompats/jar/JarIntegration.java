package nadiendev.ThaumaturgeTechCompats.jar;

import com.leclowndu93150.thaumaturge.api.aspect.AspectCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.content.essentia.jar.AbstractBlockEntityJar;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class JarIntegration {
    private JarIntegration() {
    }

    public static void register(IEventBus modBus) {
        JarRegistry.BLOCKS.register(modBus);
        JarRegistry.ITEMS.register(modBus);
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, JarConfig.SPEC, "thaumaturgetechcompats-jars.toml");
        modBus.addListener(JarIntegration::registerCapabilities);
    }

    /**
     * Thaumaturge registers the essentia capabilities of its jars with registerBlockEntity, which only binds them to the blocks
     * listed in the block entity type. Our jars are not in that list, so tubes did not see them as connectable. The same
     * providers Thaumaturge uses for its own jars are bound here directly to our blocks.
     */
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        Block[] blocks = new Block[JarTier.values().length];
        for (JarTier tier : JarTier.values()) {
            blocks[tier.ordinal()] = JarRegistry.block(tier).get();
        }
        event.registerBlock(EssentiaCapabilities.TRANSPORT,
                (level, pos, state, be, side) -> be instanceof AbstractBlockEntityJar jar && (side == null || jar.isConnectable(side)) ? jar : null,
                blocks);
        event.registerBlock(EssentiaCapabilities.STORAGE,
                (level, pos, state, be, side) -> be instanceof AbstractBlockEntityJar jar && side != null && jar.isConnectable(side) ? jar.storage(side) : null,
                blocks);
        event.registerBlock(AspectCapabilities.CONTAINER,
                (level, pos, state, be, side) -> be instanceof AbstractBlockEntityJar jar ? jar : null,
                blocks);
    }

    public static void fillCreativeTab(CreativeModeTab.Output output) {
        for (JarTier tier : JarTier.values()) {
            output.accept(JarRegistry.item(tier).get());
        }
    }

    public static void addProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        generator.addProvider(event instanceof GatherDataEvent.Client, new JarBlockStates(output));
        generator.addProvider(event instanceof GatherDataEvent.Server, new JarDataProvider(output));
    }
}
