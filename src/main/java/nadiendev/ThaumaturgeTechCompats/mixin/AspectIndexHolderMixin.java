package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.content.aspect.AspectIndex;
import com.leclowndu93150.thaumaturge.content.aspect.AspectIndexHolder;
import nadiendev.ThaumaturgeTechCompats.smelter.SmelterAspectCap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Every aspect index, built on the server or received by the client, passes here before it becomes the active one. */
@Mixin(AspectIndexHolder.class)
public abstract class AspectIndexHolderMixin {
    @ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true, require = 1)
    private static AspectIndex ttc$fitModdedItemsInTheSmelter(AspectIndex index) {
        return SmelterAspectCap.apply(index);
    }
}
