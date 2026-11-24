package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class HephaestusSmelteryIntegration {
    public static final String MOD_ID = "hephaestus";
    public static final String TOGGLE = "hephaestus_smeltery";

    private static boolean active;

    private HephaestusSmelteryIntegration() {
    }

    public static boolean enabled() {
        return CompatToggles.enabled(TOGGLE);
    }

    public static void register(IEventBus modBus) {
        if (!enabled() || !ModList.get().isLoaded(MOD_ID)) {
            return;
        }
        active = true;
        HephaestusSmelteryRegistry.register(modBus);
        if (FMLEnvironment.dist.isClient()) {
            HephaestusSmelteryClient.register(modBus);
        }
    }

    public static void fillCreativeTab(CreativeModeTab.Output output) {
        if (!active) {
            return;
        }
        for (Item item : HephaestusSmelteryRegistry.buckets()) {
            output.accept(item);
        }
    }

    public static void addProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeClient(), new HephaestusSmelteryAssets(output));
        generator.addProvider(event.includeServer(), new HephaestusSmelteryContent(output));
    }
}
