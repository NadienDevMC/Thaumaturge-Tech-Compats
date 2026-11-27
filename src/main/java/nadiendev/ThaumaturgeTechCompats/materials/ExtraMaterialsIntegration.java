package nadiendev.ThaumaturgeTechCompats.materials;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

public final class ExtraMaterialsIntegration {
    private ExtraMaterialsIntegration() {
    }

    public static void register(IEventBus modBus) {
        ExtraMaterialItems.register(modBus);
        nadiendev.ThaumaturgeTechCompats.materials.modifier.ToolModifierEvents.register(modBus);
    }

    public static void fillCreativeTab(CreativeModeTab.Output output) {
        ExtraMaterialItems.fillCreativeTab(output);
    }

    public static List<Block> blocks() {
        return ExtraMaterialItems.blocks();
    }

    public static List<Identifier> blockIds() {
        return ExtraMaterialItems.blockIds();
    }

    public static void addProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event instanceof GatherDataEvent.Client, new ExtraMaterialFiles(output, true));
        generator.addProvider(event instanceof GatherDataEvent.Server, new ExtraMaterialFiles(output, false));
    }
}
