package nadiendev.ThaumaturgeTechCompats.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Display order of our materials in every list the player sees (creative tabs, JEI catalysts):
 * Alchemical Brass, Thaumium, Void Metal, then Salis, Fibric, Crimson, Rifting.
 * It only reorders output; enum ordinals (previous/next tier lookups) are left alone.
 */
public final class MaterialOrder {
    private static final Pattern TOKEN = Pattern.compile("(?<![a-z0-9])(alchemical_brass|brass|void_metal|void|thaumium|salis|fibric|crimson|rifting)(?![a-z0-9])");

    private MaterialOrder() {
    }

    public static int rank(String token) {
        return switch (token) {
            case "alchemical_brass", "brass" -> 1;
            case "thaumium" -> 2;
            case "void_metal", "void" -> 3;
            case "salis" -> 4;
            case "fibric" -> 5;
            case "crimson" -> 6;
            case "rifting" -> 7;
            default -> 0;
        };
    }

    /** Rank of the first material found in an id or path, 0 when there is none. */
    public static int rankOf(String path) {
        Matcher matcher = TOKEN.matcher(path);
        return matcher.find() ? rank(matcher.group(1)) : 0;
    }

    /** Stable sort of tier values by the material found in the key (ids, block names). Unranked values keep their place. */
    public static <T> List<T> sorted(T[] values, Function<T, String> key) {
        List<T> list = new ArrayList<>(List.of(values));
        list.sort(Comparator.comparingInt(value -> rankOf(key.apply(value))));
        return list;
    }

    /**
     * Items that only differ by the material in their id form a family (thaumium_x, alchemical_brass_x, void_metal_x).
     * The slots used by a family are kept, but refilled in the material order above.
     */
    public static void reorder(List<Entry> entries) {
        Map<String, List<Integer>> families = new HashMap<>();
        for (int i = 0; i < entries.size(); i++) {
            ResourceKeyed key = ResourceKeyed.of(entries.get(i).stack());
            if (key != null) {
                families.computeIfAbsent(key.family, k -> new ArrayList<>()).add(i);
            }
        }
        for (List<Integer> slots : families.values()) {
            if (slots.size() < 2) {
                continue;
            }
            List<Entry> members = new ArrayList<>();
            for (int slot : slots) {
                members.add(entries.get(slot));
            }
            members.sort(Comparator.comparingInt(entry -> rankOf(BuiltInRegistries.ITEM.getKey(entry.stack().getItem()).getPath())));
            for (int i = 0; i < slots.size(); i++) {
                entries.set(slots.get(i), members.get(i));
            }
        }
    }

    public record Entry(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
    }

    private record ResourceKeyed(String family) {
        static ResourceKeyed of(ItemStack stack) {
            var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            Matcher matcher = TOKEN.matcher(id.getPath());
            if (!matcher.find()) {
                return null;
            }
            return new ResourceKeyed(id.getNamespace() + ":" + matcher.replaceAll("*"));
        }
    }

    /** Collects what a section adds, so it can be reordered before it reaches the tab. */
    public static final class Buffer implements CreativeModeTab.Output {
        private final List<Entry> entries = new ArrayList<>();

        @Override
        public void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
            entries.add(new Entry(stack, visibility));
        }

        public void flushTo(CreativeModeTab.Output output) {
            reorder(entries);
            for (Entry entry : entries) {
                output.accept(entry.stack(), entry.visibility());
            }
        }
    }
}
