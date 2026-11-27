package nadiendev.ThaumaturgeTechCompats.metals.upgrades.mixin;

import com.titammods.hephaestus_tools.table.ToolUpgrade;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/** Our upgrades cost level * base ingots and need none of the extra items Hephaestus asks from upgrades of level 10+. */
@Mixin(value = ToolUpgrade.class, remap = false)
public abstract class ToolUpgradeMixin {
    @Shadow
    public abstract ResourceLocation id();

    @Shadow
    public abstract int baseCost();

    @Inject(method = "costFor", at = @At("HEAD"), cancellable = true)
    private void thaumaturgetechcompats$linearCost(int level, CallbackInfoReturnable<Integer> cir) {
        if (id().getNamespace().equals(ThaumaturgeTechCompats.MOD_ID)) {
            cir.setReturnValue(Math.max(1, level) * baseCost());
        }
    }

    @Inject(method = "extraCosts", at = @At("HEAD"), cancellable = true)
    private void thaumaturgetechcompats$noExtras(int level, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (id().getNamespace().equals(ThaumaturgeTechCompats.MOD_ID)) {
            cir.setReturnValue(List.of());
        }
    }
}
