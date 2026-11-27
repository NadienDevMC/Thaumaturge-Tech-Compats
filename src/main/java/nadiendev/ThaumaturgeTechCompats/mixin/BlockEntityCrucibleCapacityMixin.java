package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.content.crucible.BlockEntityCrucible;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import nadiendev.ThaumaturgeTechCompats.nodepower.NodePowerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The crucible counts itself as overfilled (it leaks flux, froths and fills its surface) when the TOTAL of its aspects
 * passes 100. Those checks read AspectList.totalAmount(); here that value is replaced by a virtual one based on the
 * fullest single aspect and the configured capacity per aspect: 101 when any aspect passes it, otherwise 0..100 in
 * proportion. The real amounts, the saving, the sync and the spilling of the contents are not touched.
 */
@Mixin(BlockEntityCrucible.class)
public abstract class BlockEntityCrucibleCapacityMixin {
    @WrapOperation(method = {"isOverfilled", "surfaceLevel"},
            at = @At(value = "INVOKE",
                    target = "Lcom/leclowndu93150/thaumaturge/api/aspect/AspectList;totalAmount()I"),
            require = 0)
    private int ttc$perAspectFill(AspectList aspects, Operation<Integer> original) {
        int total = original.call(aspects);
        if (!CompatToggles.enabled(CompatToggles.CRUCIBLE_CAPACITY)) {
            return total;
        }
        long capacity = Math.max(1, NodePowerConfig.crucibleAspectCapacity());
        long fullest = 0L;
        for (AspectInstance entry : aspects.entries()) {
            fullest = Math.max(fullest, entry.amount());
        }
        return fullest > capacity ? 101 : (int) (100L * fullest / capacity);
    }
}
