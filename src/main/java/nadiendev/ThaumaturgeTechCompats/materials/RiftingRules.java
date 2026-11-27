package nadiendev.ThaumaturgeTechCompats.materials;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.content.recipe.ThaumaturgeCraftingManager;
import com.leclowndu93150.thaumaturge.content.recipe.crucible.CrucibleRecipe;
import com.leclowndu93150.thaumaturge.content.recipe.crucible.CrucibleRecipeInput;
import nadiendev.ThaumaturgeTechCompats.nodepower.RiftJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class RiftingRules {
    private RiftingRules() {
    }

    /** True when the crucible would craft a Rifting Ingot from this stack but no adjacent Rift Jar holds a trapped rift. */
    public static boolean blocked(ServerLevel level, BlockPos crucible, AspectList aspects, Player player, ItemStack stack) {
        CrucibleRecipe recipe = ThaumaturgeCraftingManager.findMatchingCrucibleRecipe(level, player, aspects, stack);
        if (recipe == null) {
            return false;
        }
        ItemStack result = recipe.assemble(new CrucibleRecipeInput(stack, aspects));
        if (!result.is(ExtraMaterialItems.ingot(ExtraMaterial.RIFTING).get())) {
            return false;
        }
        return !hasTrappedRift(level, crucible);
    }

    public static boolean hasTrappedRift(ServerLevel level, BlockPos crucible) {
        for (Direction direction : Direction.values()) {
            if (level.getBlockEntity(crucible.relative(direction)) instanceof RiftJarBlockEntity jar && jar.power() > 0.0F) {
                return true;
            }
        }
        return false;
    }
}
