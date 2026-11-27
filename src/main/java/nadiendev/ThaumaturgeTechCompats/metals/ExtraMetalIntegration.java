package nadiendev.ThaumaturgeTechCompats.metals;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class ExtraMetalIntegration {
    private ExtraMetalIntegration() {
    }

    public static void register(IEventBus modBus) {
        ExtraMetalRegistry.register(modBus);
        nadiendev.ThaumaturgeTechCompats.metals.upgrades.ToolUpgradeIntegration.register(modBus);
    }

    public static void fillCreativeTab(CreativeModeTab.Output output) {
        ExtraMetalRegistry.fillCreativeTab(output);
    }

    public static void addProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeClient(), new ExtraMetalAssets(output));
        generator.addProvider(event.includeServer(), new ExtraMetalContent(output));
        generator.addProvider(event.includeServer(), new ExtraMetalToolContent(output));
    }
}
