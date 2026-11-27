package nadiendev.ThaumaturgeTechCompats.research;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.registry.CreativeSection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Thaumonomicon tabs of this mod: the item drawn on each tab (instead of the flat texture of the category JSON,
 * which stays as fallback) and their place in the tab columns. Used by the client mixin on the book screen.
 */
public final class ResearchTabIcons {
    /** Creative section id -> item of this mod drawn on its tab. */
    private static final Map<String, String> ITEMS = Map.ofEntries(
            Map.entry("avaritia", "extremely_primordial_pearl"),
            Map.entry("avaritia_pearls", "infinity_planetary_endest_pearl"),
            Map.entry("solar_panels", "thaumium_solar_panel"),
            Map.entry("create", "thaummotor"),
            Map.entry("oritech", "oritech_thaumium_speed_addon"),
            Map.entry("mekanism", "energized_thaumium_pickaxe"),
            Map.entry("botania", "thaumium_spreader"),
            Map.entry("void_miners", "thaumium_miner"),
            Map.entry("nodes", "flux_jar"),
            Map.entry("flux_network", "thaumium_flux_storage_core"),
            Map.entry("flux_gear", "thaumium_shield_generator"),
            Map.entry("food_tainted", "tainted_apple"),
            Map.entry("food_farmers", "thaumium_knife"),
            Map.entry("purity", "purification_potion"),
            Map.entry("enderio", "thaumium_capacitor_bank"),
            Map.entry("extra_metals", "crimson_ingot"),
            Map.entry("extras", "thaumium_aura_converter"));

    private static final Map<Identifier, ItemStack> CACHE = new ConcurrentHashMap<>();

    private ResearchTabIcons() {
    }

    public static boolean isOurs(Identifier category) {
        return category != null && ThaumaturgeTechCompats.MOD_ID.equals(category.getNamespace());
    }

    /** Item for the tab of one of our categories; empty when the category is not ours or the item is not registered. */
    public static ItemStack forCategory(Identifier category) {
        if (!isOurs(category)) {
            return ItemStack.EMPTY;
        }
        return CACHE.computeIfAbsent(category, ResearchTabIcons::resolve);
    }

    private static ItemStack resolve(Identifier category) {
        String item = ITEMS.get(section(category));
        if (item == null) {
            return ItemStack.EMPTY;
        }
        return BuiltInRegistries.ITEM.getOptional(ThaumaturgeTechCompats.id(item))
                .map(found -> new ItemStack(found))
                .orElse(ItemStack.EMPTY);
    }

    /** Tab ids carry the two-digit order of their tab ("01_avaritia"); the section id is what follows it. */
    private static String section(Identifier category) {
        String path = category.getPath();
        return path.matches("\\d\\d_.+") ? path.substring(3) : path;
    }

    private static int order(Identifier category) {
        String id = section(category);
        for (CreativeSection section : CreativeSection.values()) {
            if (section.id().equals(id)) {
                return section.ordinal();
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Moves our tabs into the free slots of the left column (under Thaumaturge's own tabs, which keep their place)
     * and leaves the rest at the start of the right column, all in creative-tab order.
     *
     * @param left     tabs of the left column; ours are added at its end
     * @param right    tabs of the right (add-on) column
     * @param capacity how many tabs fit in one column on this screen
     */
    public static <T> void arrange(List<T> left, List<T> right, Function<T, Identifier> id, int capacity) {
        List<T> ours = new ArrayList<>();
        for (T tab : right) {
            if (isOurs(id.apply(tab))) {
                ours.add(tab);
            }
        }
        if (ours.isEmpty()) {
            return;
        }
        ours.sort(Comparator.comparingInt(tab -> order(id.apply(tab))));
        right.removeAll(ours);
        int moved = Math.min(ours.size(), Math.max(0, capacity - left.size()));
        left.addAll(ours.subList(0, moved));
        right.addAll(0, ours.subList(moved, ours.size()));
    }
}
