package nadiendev.ThaumaturgeTechCompats.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.leclowndu93150.thaumaturge.content.crucible.BlockEntityCrucible;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import nadiendev.ThaumaturgeTechCompats.nodepower.FluxJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntityCrucible.class)
public abstract class BlockEntityCrucibleMixin {
    /**
     * A crucible that leaks or overflows pollutes the aura. Flux jars touching it take that flux first,
     * so it stays in the jar instead of polluting; whatever does not fit pollutes as usual.
     */
    @WrapOperation(method = {"spillOverflow", "spillRandom", "spillRemnants"},
            at = @At(value = "INVOKE",
                    target = "Lcom/leclowndu93150/thaumaturge/api/aura/AuraHelper;polluteAura(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FZ)V"),
            require = 0)
    private void ttc$feedAdjacentFluxJars(Level level, BlockPos pos, float amount, boolean showEffect, Operation<Void> original) {
        float remaining = amount;
        if (CompatToggles.enabled(CompatToggles.NODE_POWER) && !level.isClientSide()) {
            for (Direction direction : Direction.values()) {
                if (remaining <= 0.0F) {
                    break;
                }
                if (level.getBlockEntity(pos.relative(direction)) instanceof FluxJarBlockEntity jar) {
                    remaining -= jar.absorb(remaining);
                }
            }
        }
        if (remaining > 0.0F) {
            original.call(level, pos, remaining, showEffect);
        }
    }
}
