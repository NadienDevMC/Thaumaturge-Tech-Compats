package nadiendev.ThaumaturgeTechCompats.materials.modifier.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Hephaestus Tools doubles the cost of every upgrade level and asks for extra items from level 6 on when the max level
 * is 10 or more. Our upgrades cost N ingots for level N and nothing else.
 */
@Mixin(targets = "com.titammods.hephaestus_tools.table.ToolUpgrade", remap = false)
public abstract class ToolUpgradeCostMixin {
    @Shadow
    @Final
    private Identifier id;

    @Inject(method = "costFor", at = @At("HEAD"), cancellable = true, remap = false)
    private void thaumaturgetechcompats$linearCost(int level, CallbackInfoReturnable<Integer> cir) {
        if (id.getNamespace().equals("thaumaturgetechcompats")) {
            cir.setReturnValue(Math.max(1, level));
        }
    }

    @Inject(method = "extraCosts", at = @At("HEAD"), cancellable = true, remap = false)
    private void thaumaturgetechcompats$noExtras(int level, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (id.getNamespace().equals("thaumaturgetechcompats")) {
            cir.setReturnValue(List.of());
        }
    }
}
