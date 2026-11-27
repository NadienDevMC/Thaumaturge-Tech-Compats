package nadiendev.ThaumaturgeTechCompats.metals.upgrades;

import com.titammods.hephaestus_tools.table.ToolRole;
import com.titammods.hephaestus_tools.table.ToolUpgrade;
import com.titammods.hephaestus_tools.table.ToolUpgrades;
import com.titammods.hephaestus_tools.tools.modifier.ModifierEffect;
import com.titammods.hephaestus_tools.tools.modifier.ModifierEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Map;

/**
 * Hephaestus Tools has no data format for upgrades: they live in {@link ToolUpgrades#REGISTRY} and their stat effects in
 * a private map of {@link ModifierEffects}. Both are plain mutable collections, so they are filled at common setup.
 */
final class ToolUpgradeRegistration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToolUpgradeRegistration.class);
    private ToolUpgradeRegistration() {
    }

    /** The Arsenal Table draws the Modify tab icon from hephaestus_tools:textures/gui/forge_table/modifier_icon/modifier_<upgrade id path>.png. */
    private static ResourceLocation icon(ToolUpgradeKind kind) {
        return ResourceLocation.fromNamespaceAndPath("hephaestus_tools",
                "textures/gui/forge_table/modifier_icon/modifier_" + kind.id().getPath() + ".png");
    }

    @SuppressWarnings("unchecked")
    static void register() {
        Map<ResourceLocation, ModifierEffect> effects;
        try {
            Field field = ModifierEffects.class.getDeclaredField("EFFECTS");
            field.setAccessible(true);
            effects = (Map<ResourceLocation, ModifierEffect>) field.get(null);
        } catch (ReflectiveOperationException | RuntimeException e) {
            LOGGER.error("Hephaestus Tools modifier effects are not reachable, the extra upgrades are disabled", e);
            return;
        }
        // Builds the built-in upgrade list first, so ours are added after it.
        ToolUpgrades.availableFor(Items.AIR);
        for (ToolUpgradeKind kind : ToolUpgradeKind.values()) {
            Item cost = BuiltInRegistries.ITEM.get(kind.costItemId());
            if (cost == Items.AIR) {
                LOGGER.warn("Upgrade {} skipped: item {} does not exist", kind.id(), kind.costItemId());
                continue;
            }
            effects.put(kind.id(), (context, level) -> {
                if (level >= ToolUpgradeKind.MAX_LEVEL) {
                    context.indestructible = true;
                }
            });
            ToolUpgrades.REGISTRY.add(new ToolUpgrade(kind.id(), kind.nameKey(), EnumSet.allOf(ToolRole.class),
                    ToolUpgradeKind.MAX_LEVEL, cost, 1, icon(kind)));
        }
    }
}
