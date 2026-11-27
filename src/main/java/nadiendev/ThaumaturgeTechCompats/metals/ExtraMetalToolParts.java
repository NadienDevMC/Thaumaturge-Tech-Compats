package nadiendev.ThaumaturgeTechCompats.metals;

import com.titammods.hephaestus_tools.materials.MaterialId;
import com.titammods.hephaestus_tools.tools.helper.ToolBuildHandler;
import com.titammods.hephaestus_tools.tools.part.ToolPartItem;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Hephaestus Tools parts made of the new metals, grouped by tool kind for the creative tab. Only loaded when
 * Hephaestus Tools is present. The parts of each group come from the tool catalog of Hephaestus Tools; a part used by
 * several tools (handles, bindings) is listed once, in the first group that uses it.
 */
final class ExtraMetalToolParts {
    private static final String TOOLS = "hephaestus_tools";
    /** Groups in creative order. Hephaestus Tools has no hoe or bow shaped tools, so those use the closest tools or stay empty. */
    private static final String[][] GROUPS = {
            {"sword", "dagger", "cleaver"},
            {"hand_axe", "broad_axe"},
            {"pickaxe"},
            {"sledge_hammer", "vein_hammer"},
            {"excavator"},
            {"kama", "scythe", "mattock"},
            {"bow"}
    };

    private ExtraMetalToolParts() {
    }

    /** Parts of each group in {@link #GROUPS} order, empty lists included. */
    static List<List<ToolPartItem>> groups() {
        Set<Item> seen = new LinkedHashSet<>();
        List<List<ToolPartItem>> result = new ArrayList<>();
        for (String[] tools : GROUPS) {
            List<ToolPartItem> group = new ArrayList<>();
            for (String tool : tools) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(TOOLS, tool));
                if (item == Items.AIR) {
                    continue;
                }
                for (Item part : ToolBuildHandler.getToolParts(item)) {
                    if (part instanceof ToolPartItem toolPart && seen.add(part)) {
                        group.add(toolPart);
                    }
                }
            }
            result.add(group);
        }
        return result;
    }

    static void fill(CreativeModeTab.Output output, int groupIndex) {
        List<List<ToolPartItem>> groups = groups();
        if (groupIndex >= groups.size()) {
            return;
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            for (ToolPartItem part : groups.get(groupIndex)) {
                ItemStack stack = part.withMaterial(MaterialId.of(ThaumaturgeTechCompats.MOD_ID, metal.id()));
                output.accept(stack);
            }
        }
    }
}
