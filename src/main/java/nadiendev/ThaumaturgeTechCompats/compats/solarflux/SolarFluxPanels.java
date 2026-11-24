package nadiendev.ThaumaturgeTechCompats.compats.solarflux;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.zeith.solarflux.panels.SolarPanel;

import java.util.ArrayList;
import java.util.List;

public final class SolarFluxPanels {
    /** Solar Flux registers every panel as solarflux:sp_{name}, so the suffix lives in the name. */
    public static final String SUFFIX = "_solar_panel";
    public static final String VOID_URANIUM = "void_uranium" + SUFFIX;

    private static final List<SolarPanel> PANELS = new ArrayList<>();
    private static boolean voidUraniumRegistered;

    private SolarFluxPanels() {
    }

    public static void register() {
        // Registration order is the order shown in the Solar Flux creative tab.
        add(CompatToggles.SOLAR_ALCHEMICAL_COBALT, "alchemical_cobalt" + SUFFIX, 29_500L, 50_500L, 2_000_000L, "Alchemical Cobalt Solar Panel", "Panel solar de cobalto alquímico", "hephaestus");
        add(CompatToggles.SOLAR_ALCHEMICAL, "alchemical", 89_500L, 50_500L, 18_000_000L, "Alchemical Solar Panel", "Panel solar alquímico");
        add(CompatToggles.SOLAR_BRASS_ANDESITE, "brass_andesite" + SUFFIX, 11_000L, 5_095L, 500_000L, "Brass Andesite Solar Panel", "Panel solar de latón y andesita", "create");
        add(CompatToggles.SOLAR_ALCHEMICAL_BRASS, "brass" + SUFFIX, 250_000L, 100_000L, 78_000_000L, "Brass Solar Panel", "Panel solar de latón");
        add(CompatToggles.SOLAR_THAUMIUM_ZINC, "thaumium_zinc" + SUFFIX, 19_500L, 50_500L, 1_000_000L, "Thaumium Zinc Solar Panel", "Panel solar de taumio y zinc", "create", "alltheores");
        add(CompatToggles.SOLAR_THAUMIUM, "thaumium", 791_800L, 7_000_000L, 88_000_000L, "Thaumium Solar Panel", "Panel solar de taumio");
        add(CompatToggles.SOLAR_VOID_URANIUM, VOID_URANIUM, 15_035_000L, 150_350_000L, 95_060_000L,
                "Void Uranium Solar Panel", "Panel solar de uranio del vacío", "alltheores", "mekanism");
        add(CompatToggles.SOLAR_VOID_METAL, "void_metal", 15_500_000L, 155_000_000L, 98_000_000L, "Void Metal Solar Panel", "Panel solar de metal del vacío");
        if (voidUraniumRegistered && ModList.get().isLoaded("mekanism")) {
            VoidUraniumRadiation.register(NeoForge.EVENT_BUS);
        }
    }

    /** True when the void uranium panel was registered (used by the radiation hook). */
    public static boolean isVoidUraniumRegistered() {
        return voidUraniumRegistered;
    }

    public static List<Item> items() {
        List<Item> items = new ArrayList<>();
        for (SolarPanel panel : PANELS) {
            items.add(panel.asItem());
        }
        return items;
    }

    /** Registers the panel when its toggle is on and, if any mod is listed, at least one of them is loaded. */
    private static void add(String feature, String name, long generation, long transfer, long capacity, String english, String spanish, String... anyOf) {
        if (!CompatToggles.enabled(feature) || !anyLoaded(anyOf)) {
            return;
        }
        SolarPanel panel = SolarPanel.builder()
                .name(name)
                .generation(generation)
                .capacity(capacity)
                .transfer(transfer)
                .buildAndRegister()
                .setCompatMod(ThaumaturgeTechCompats.MOD_ID)
                .langBuilder()
                .put("en_us", english)
                .put("es_es", spanish)
                .put("es_ar", spanish)
                .put("es_mx", spanish)
                .build();
        PANELS.add(panel);
        if (name.equals(VOID_URANIUM)) {
            voidUraniumRegistered = true;
        }
    }

    private static boolean anyLoaded(String[] mods) {
        if (mods.length == 0) {
            return true;
        }
        for (String mod : mods) {
            if (ModList.get().isLoaded(mod)) {
                return true;
            }
        }
        return false;
    }
}
