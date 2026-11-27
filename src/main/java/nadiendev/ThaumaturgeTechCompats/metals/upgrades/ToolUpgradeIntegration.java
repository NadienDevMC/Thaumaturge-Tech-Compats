package nadiendev.ThaumaturgeTechCompats.metals.upgrades;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.BiConsumer;

/** Entry point of the Hephaestus Tools upgrades. Nothing here loads Hephaestus classes unless the mod is present. */
public final class ToolUpgradeIntegration {
    public static final String TOOLS_MOD_ID = "hephaestus_tools";

    private ToolUpgradeIntegration() {
    }

    public static void register(IEventBus modBus) {
        if (!ModList.get().isLoaded(TOOLS_MOD_ID)) {
            return;
        }
        modBus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(ToolUpgradeRegistration::register));
        NeoForge.EVENT_BUS.register(ToolUpgradeEvents.class);
    }

    public static void lang(BiConsumer<String, String> out, boolean spanish) {
        for (ToolUpgradeKind kind : ToolUpgradeKind.values()) {
            String name = spanish ? kind.spanish() : kind.english();
            String desc = spanish ? kind.spanishDescription() : kind.englishDescription();
            out.accept(kind.nameKey(), name);
            out.accept(kind.nameKey() + ".desc", desc);
            out.accept("gui.hephaestus_tools.upgrade." + kind.id().getPath() + ".desc", desc);
        }
    }
}
