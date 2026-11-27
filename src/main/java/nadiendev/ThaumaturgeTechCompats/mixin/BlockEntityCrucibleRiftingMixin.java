package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.content.crucible.BlockEntityCrucible;
import nadiendev.ThaumaturgeTechCompats.materials.RiftingRules;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityCrucible.class)
public abstract class BlockEntityCrucibleRiftingMixin {
    @Unique
    private long ttc$lastRiftingHint = Long.MIN_VALUE;

    /**
     * The Rifting Ingot recipe is a normal crucible recipe, but the crucible must have a Rift Jar with a trapped rift
     * next to it. Without it the item is left alone (not dissolved) and the player gets a hint.
     */
    @Inject(method = "smeltOne", at = @At("HEAD"), cancellable = true, require = 0)
    private void ttc$requireTrappedRift(ServerLevel level, ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        BlockEntityCrucible crucible = (BlockEntityCrucible) (Object) this;
        if (!RiftingRules.blocked(level, crucible.getBlockPos(), crucible.getAspects(), player, stack)) {
            return;
        }
        long now = level.getGameTime();
        if (now - ttc$lastRiftingHint >= 60L) {
            ttc$lastRiftingHint = now;
            player.sendOverlayMessage(Component.translatable("message.thaumaturgetechcompats.rifting_needs_rift_jar"));
        }
        cir.setReturnValue(false);
    }
}
