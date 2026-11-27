package nadiendev.ThaumaturgeTechCompats.metals.upgrades.mixin;

import com.titammods.hephaestus_tools.materials.MaterialId;
import com.titammods.hephaestus_tools.tools.item.ModifiableItem;
import com.titammods.hephaestus_tools.tools.nbt.ToolStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Hephaestus Tools names every tool just "Sword" or "Pickaxe"; this puts the material of the head in front, like "Rifting Sword". */
@Mixin(value = ModifiableItem.class, remap = false)
public abstract class ModifiableItemNameMixin {
    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void thaumaturgetechcompats$materialName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        if (!ToolStack.isInitialized(stack)) {
            return;
        }
        MaterialId material = ToolStack.getMaterial(stack, 0);
        if (material == null || material.isEmpty()) {
            return;
        }
        cir.setReturnValue(Component.translatable("tool.thaumaturgetechcompats.material_name",
                Component.translatable(material.translationKey()), cir.getReturnValue()));
    }
}
