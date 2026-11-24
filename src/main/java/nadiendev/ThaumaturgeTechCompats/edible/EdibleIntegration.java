package nadiendev.ThaumaturgeTechCompats.edible;

import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class EdibleIntegration {
    public static final String TOGGLE = "edible_items";

    private static boolean registered;

    private EdibleIntegration() {
    }

    public static void register(IEventBus modBus) {
        if (!CompatToggles.enabled(TOGGLE)) {
            return;
        }
        EdibleItems.register(modBus);
        registered = true;
    }

    public static void fillCreativeTab(CreativeModeTab.Output output) {
        if (!registered) {
            return;
        }
        for (Item item : EdibleItems.items()) {
            output.accept(item);
        }
    }

    public static void addProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeClient(), new EdibleAssets(output));
        generator.addProvider(event.includeServer(), new EdibleContent(output));
    }
}
