package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.api.warp.WarpHelper;
import com.leclowndu93150.thaumaturge.api.warp.WarpType;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.leclowndu93150.thaumaturge.content.crucible.BlockEntityCrucible;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import nadiendev.ThaumaturgeTechCompats.nodepower.FluxJarBlockEntity;
import nadiendev.ThaumaturgeTechCompats.nodepower.NodePowerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntityCrucible.class)
public abstract class BlockEntityCrucibleMixin {
    @Unique
    private long ttc$lastWarpTick = Long.MIN_VALUE;

    /**
     * Flux that leaks or overflows from the crucible goes straight into the Flux Jars touching it. Whatever does not fit
     * does not pollute the aura: it vanishes and gives temporary warp to the players next to the crucible.
     */
    @WrapOperation(method = {"spillOverflow", "spillRandom", "spillRemnants"},
            at = @At(value = "INVOKE",
                    target = "Lcom/leclowndu93150/thaumaturge/api/aura/AuraHelper;polluteAura(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FZ)V"),
            require = 0)
    private void ttc$feedAdjacentFluxJars(Level level, BlockPos pos, float amount, boolean showEffect, Operation<Void> original) {
        if (!CompatToggles.enabled(CompatToggles.NODE_POWER)) {
            original.call(level, pos, amount, showEffect);
            return;
        }
        if (level.isClientSide()) {
            return;
        }
        float remaining = amount;
        for (Direction direction : Direction.values()) {
            if (remaining <= 0.0F) {
                break;
            }
            if (level.getBlockEntity(pos.relative(direction)) instanceof FluxJarBlockEntity jar) {
                remaining -= jar.absorb(remaining);
            }
        }
        if (remaining > 0.0F) {
            long now = level.getGameTime();
            if (now - ttc$lastWarpTick >= 80L) {
                ttc$lastWarpTick = now;
                int warp = NodePowerConfig.crucibleWarpAmount();
                double radius = NodePowerConfig.crucibleWarpRadius();
                if (warp > 0) {
                    for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(radius))) {
                        WarpHelper.addWarp(player, warp, WarpType.TEMPORARY);
                    }
                }
            }
        }
    }
}
