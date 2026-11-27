package nadiendev.ThaumaturgeTechCompats.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "com.leclowndu93150.thaumaturge.content.entity.RiftSegmentSweep")
public abstract class RiftSegmentSweepMixin {
    /** A flux rift keeps its look and its damage to entities, but does not erase blocks while the option is on (default). */
    @WrapOperation(method = "eraseBlockAlong",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"),
            require = 0)
    private boolean ttc$keepBlocks(ServerLevel level, BlockPos pos, boolean drop, Operation<Boolean> original) {
        if (CompatToggles.enabled(CompatToggles.RIFT_APPEAR_SAFE)) {
            return false;
        }
        return original.call(level, pos, drop);
    }
}
