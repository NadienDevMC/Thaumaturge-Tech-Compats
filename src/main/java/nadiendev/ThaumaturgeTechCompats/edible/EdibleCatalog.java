package nadiendev.ThaumaturgeTechCompats.edible;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/** Every edible item. Adding one is one more line here; models, recipes and names follow from it. */
public final class EdibleCatalog {
    public static final String THAUMATURGE = "thaumaturge";

    private static final String MC = "minecraft";
    private static final List<EdibleEntry> ENTRIES = new ArrayList<>();

    static {
        // Vanilla tool sets: material prefix and tier
        String[] prefixes = {"wooden", "golden", "stone", "iron", "diamond", "netherite"};
        int[] tiers = {0, 0, 0, 1, 2, 3};
        for (int i = 0; i < prefixes.length; i++) {
            toolSet(MC, prefixes[i] + "_", tiers[i]);
        }
        add(MC, "mace", EdibleKind.WEAPON, 2);

        // Thaumaturge tool sets (the elemental set is diamond level)
        toolSet(THAUMATURGE, "thaumium_", 2);
        toolSet(THAUMATURGE, "void_", 3);
        toolSet(THAUMATURGE, "elemental_", 2);
        add(THAUMATURGE, "goggles_revealing", EdibleKind.GOGGLES, 0);

        // Ingots
        add(MC, "iron_ingot", EdibleKind.INGOT, 1);
        add(MC, "gold_ingot", EdibleKind.INGOT, 0);
        add(MC, "copper_ingot", EdibleKind.INGOT, 1);
        add(MC, "netherite_ingot", EdibleKind.INGOT, 3);
        add(THAUMATURGE, "ingot_thaumium", EdibleKind.INGOT, 2);
        add(THAUMATURGE, "ingot_void", EdibleKind.INGOT, 3);
        add(THAUMATURGE, "ingot_brass", EdibleKind.INGOT, 1);

        // Gems
        for (String gem : new String[]{"diamond", "emerald", "amethyst_shard", "lapis_lazuli", "quartz"}) {
            add(MC, gem, EdibleKind.GEM, 0);
        }

        // Every vanilla door
        for (String wood : new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped"}) {
            add(MC, wood + "_door", EdibleKind.DOOR, 0);
        }
        add(MC, "iron_door", EdibleKind.DOOR, 0);
        for (String copper : new String[]{"", "exposed_", "weathered_", "oxidized_", "waxed_", "waxed_exposed_", "waxed_weathered_", "waxed_oxidized_"}) {
            add(MC, copper + "copper_door", EdibleKind.DOOR, 0);
        }
    }

    private EdibleCatalog() {
    }

    public static List<EdibleEntry> entries() {
        return ENTRIES;
    }

    private static void toolSet(String namespace, String prefix, int tier) {
        add(namespace, prefix + "pickaxe", EdibleKind.PICKAXE, tier);
        add(namespace, prefix + "sword", EdibleKind.WEAPON, tier);
        add(namespace, prefix + "axe", EdibleKind.AXE, tier);
        add(namespace, prefix + "hoe", EdibleKind.HOE, tier);
        add(namespace, prefix + "shovel", EdibleKind.SHOVEL, tier);
    }

    private static void add(String namespace, String path, EdibleKind kind, int tier) {
        ENTRIES.add(new EdibleEntry(ResourceLocation.fromNamespaceAndPath(namespace, path), kind, tier));
    }
}
