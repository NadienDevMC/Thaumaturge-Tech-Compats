package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Solar panels built on Solar Generation. Every reference to that mod lives in classes that are
 * only touched from here after checking it is loaded.
 */
public final class SolarGenerationIntegration {
    public static final String MOD_ID = "solargeneration";

    private SolarGenerationIntegration() {
    }

    public static boolean isLoaded() {
        return ModList.get() != null && ModList.get().isLoaded(MOD_ID);
    }

    public static boolean active() {
        return isLoaded() && CompatToggles.enabled(CompatToggles.SOLAR_GENERATION);
    }

    private static boolean anyEnabled() {
        if (!active()) {
            return false;
        }
        for (SolarPanelType type : SolarPanelType.values()) {
            if (type.enabled()) {
                return true;
            }
        }
        return false;
    }

    public static void register(IEventBus modBus) {
        if (!anyEnabled()) {
            return;
        }
        SolarGenerationRegistry.register(modBus);
        VoidUraniumRadiation.register();
        if (FMLEnvironment.getDist().isClient()) {
            SolarGenerationClient.register(modBus);
        }
    }

    public static void fillCreativeTab(CreativeModeTab.Output output) {
        if (!active()) {
            return;
        }
        // the list only holds the panels registered in this run
        for (Item item : SolarGenerationRegistry.items()) {
            output.accept(item);
        }
    }

    /** Ids of every panel, whether or not it is registered in this run (used by the tags). */
    public static List<Identifier> blockIds() {
        List<Identifier> ids = new ArrayList<>();
        for (SolarPanelType type : SolarPanelType.values()) {
            ids.add(ThaumaturgeTechCompats.id(type.id()));
        }
        return ids;
    }

    public static void addProviders(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event instanceof GatherDataEvent.Client, new SolarGenerationAssets(output));
        generator.addProvider(event instanceof GatherDataEvent.Server, new SolarGenerationContent(output));
    }
}
