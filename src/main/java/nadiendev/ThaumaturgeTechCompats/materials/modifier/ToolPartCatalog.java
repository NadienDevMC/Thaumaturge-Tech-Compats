package nadiendev.ThaumaturgeTechCompats.materials.modifier;

import nadiendev.ThaumaturgeTechCompats.materials.ExtraMaterial;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hephaestus Tools tool parts made of our metals, for the creative tab. Reflection only, so it is empty without
 * Hephaestus Tools. Parts come from what each tool is built from (ToolBuildHandler.getToolParts), grouped by kind of
 * tool; a part shared by several kinds (handles, bindings) is listed once, in the first group that uses it.
 */
public final class ToolPartCatalog {
    private static final String BASE = "com.titammods.hephaestus_tools.";

    /** Group names in tab order, with the Hephaestus tools that decide the parts of each group. */
    private static final String[][] GROUPS = {
            {"sword", "sword", "dagger", "cleaver", "spear"},
            {"axe", "hand_axe", "broad_axe"},
            {"pickaxe", "pickaxe", "paxel"},
            {"hammer", "sledge_hammer", "vein_hammer", "mace"},
            {"shovel", "excavator", "mattock"},
            {"hoe", "kama", "scythe"},
            {"bow", "bow"}
    };

    private ToolPartCatalog() {
    }

    /** The part stacks of one group (0 sword .. 6 bow) made of the given material; empty when Hephaestus Tools is absent. */
    public static List<ItemStack> parts(int group, ExtraMaterial material) {
        List<ItemStack> out = new ArrayList<>();
        if (!ModList.get().isLoaded(ToolModifierEvents.HEPHAESTUS_TOOLS)) {
            return out;
        }
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Method getToolParts = Class.forName(BASE + "tools.helper.ToolBuildHandler", true, loader)
                    .getMethod("getToolParts", Item.class);
            Class<?> materialId = Class.forName(BASE + "materials.MaterialId", true, loader);
            Object id = materialId.getMethod("of", String.class, String.class)
                    .invoke(null, ToolModifierEvents.HEPHAESTUS_TOOLS, material.id());
            Class<?> partItem = Class.forName(BASE + "tools.part.ToolPartItem", true, loader);
            Method withMaterial = partItem.getMethod("withMaterial", materialId);

            Set<Item> seen = new HashSet<>();
            for (int g = 0; g <= group; g++) {
                List<Item> items = new ArrayList<>();
                String[] names = GROUPS[g];
                for (int i = 1; i < names.length; i++) {
                    Identifier toolId = Identifier.fromNamespaceAndPath(ToolModifierEvents.HEPHAESTUS_TOOLS, names[i]);
                    if (!BuiltInRegistries.ITEM.containsKey(toolId)) {
                        continue;
                    }
                    Item tool = BuiltInRegistries.ITEM.getValue(toolId);
                    for (Object part : (List<?>) getToolParts.invoke(null, tool)) {
                        if (partItem.isInstance(part) && seen.add((Item) part)) {
                            items.add((Item) part);
                        }
                    }
                }
                if (g == group) {
                    for (Item item : items) {
                        out.add((ItemStack) withMaterial.invoke(item, id));
                    }
                }
            }
        } catch (Throwable t) {
            out.clear();
        }
        return out;
    }

    public static int groupCount() {
        return GROUPS.length;
    }
}
