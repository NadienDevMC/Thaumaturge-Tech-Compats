package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.registry.TTBlockEntities;
import nadiendev.ThaumaturgeTechCompats.jar.EssentiaJarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Our Matrix, Neutronium and Infinity jars reuse the Thaumaturge jar block entity, whose type only lists the blocks of
 * Thaumaturge. NeoForge does not let a block of another class be added to that list, so the type is told here that our jars
 * are valid for it.
 */
@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeJarMixin {
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true, require = 1)
    private void ttc$acceptEssentiaJars(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof EssentiaJarBlock && TTBlockEntities.JAR.isBound() && (Object) this == TTBlockEntities.JAR.get()) {
            cir.setReturnValue(true);
        }
    }
}
