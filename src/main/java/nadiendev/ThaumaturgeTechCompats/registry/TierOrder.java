package nadiendev.ThaumaturgeTechCompats.registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Display order of our materials in creative tabs and listings: Alchemical Brass, Thaumium, Void Metal, Salis, Fibric,
 * Crimson, Rifting. Enum ordinals drive crafting progression (previous tier lookups), so they are never reordered:
 * only the output order goes through here.
 */
public final class TierOrder {
    private TierOrder() {
    }

    /** Rank of a material by its id or enum name; -1 when it is none of ours. */
    public static int rank(String name) {
        String n = name.toLowerCase(java.util.Locale.ROOT);
        if (n.contains("brass")) {
            return 0;
        }
        if (n.contains("thaumium")) {
            return 1;
        }
        if (n.contains("void")) {
            return 2;
        }
        if (n.contains("salis")) {
            return 3;
        }
        if (n.contains("fibric")) {
            return 4;
        }
        if (n.contains("crimson")) {
            return 5;
        }
        if (n.contains("rifting")) {
            return 6;
        }
        return -1;
    }

    /** The constants in display order; constants that are not ours keep their slot. */
    public static <T extends Enum<T>> List<T> display(T[] values) {
        List<T> result = new ArrayList<>(List.of(values));
        List<Integer> slots = new ArrayList<>();
        List<T> ours = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (rank(result.get(i).name()) >= 0) {
                slots.add(i);
                ours.add(result.get(i));
            }
        }
        ours.sort(Comparator.comparingInt(value -> rank(value.name())));
        for (int i = 0; i < slots.size(); i++) {
            result.set(slots.get(i), ours.get(i));
        }
        return result;
    }
}
