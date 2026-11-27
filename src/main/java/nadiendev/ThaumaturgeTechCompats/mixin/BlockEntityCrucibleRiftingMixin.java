package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.content.crucible.BlockEntityCrucible;
import com.leclowndu93150.thaumaturge.content.recipe.crucible.CrucibleRecipe;
import com.leclowndu93150.thaumaturge.content.recipe.crucible.CrucibleRecipeInput;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nadiendev.ThaumaturgeTechCompats.metals.ExtraMetal;
import nadiendev.ThaumaturgeTechCompats.metals.ExtraMetalRegistry;
import nadiendev.ThaumaturgeTechCompats.metals.ExtraMetalTranslations;
import nadiendev.ThaumaturgeTechCompats.nodepower.RiftJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The Rifting Ingot recipe is a normal crucible recipe, but it only works while a Rift Jar holding a trapped rift
 * touches the crucible. A recipe cannot express that, so the match is rejected here when the jar is missing or empty.
 */
@Mixin(BlockEntityCrucible.class)
public abstract class BlockEntityCrucibleRiftingMixin {
    @Unique
    private long ttc$lastRiftingHint = Long.MIN_VALUE;

    @WrapOperation(
            method = "attemptSmelt(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/function/Consumer;Z)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE",
                    target = "Lcom/leclowndu93150/thaumaturge/content/recipe/ThaumaturgeCraftingManager;findMatchingCrucibleRecipe(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/player/Player;Lcom/leclowndu93150/thaumaturge/api/aspect/AspectList;Lnet/minecraft/world/item/ItemStack;)Lcom/leclowndu93150/thaumaturge/content/recipe/crucible/CrucibleRecipe;"),
            require = 0)
    private CrucibleRecipe ttc$requireRiftJar(ServerLevel level, Player player, AspectList aspects, ItemStack stack,
                                              Operation<CrucibleRecipe> original) {
        CrucibleRecipe recipe = original.call(level, player, aspects, stack);
        if (recipe == null) {
            return null;
        }
        ItemStack result = recipe.assemble(new CrucibleRecipeInput(stack, aspects), level.registryAccess());
        if (!result.is(ExtraMetalRegistry.ingot(ExtraMetal.RIFTING))) {
            return recipe;
        }
        BlockPos pos = ((BlockEntityCrucible) (Object) this).getBlockPos();
        for (Direction direction : Direction.values()) {
            if (level.getBlockEntity(pos.relative(direction)) instanceof RiftJarBlockEntity jar && jar.power() > 0.0F) {
                return recipe;
            }
        }
        // No trapped rift next to the crucible: tell the player (action bar, at most every 3 seconds)
        long now = level.getGameTime();
        if (player != null && now - ttc$lastRiftingHint >= 60L) {
            ttc$lastRiftingHint = now;
            player.displayClientMessage(Component.translatable(ExtraMetalTranslations.RIFTING_NEEDS_RIFT_JAR), true);
        }
        return null;
    }
}
