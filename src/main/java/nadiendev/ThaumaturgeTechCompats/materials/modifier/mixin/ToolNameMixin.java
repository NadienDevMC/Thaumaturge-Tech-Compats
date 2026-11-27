package nadiendev.ThaumaturgeTechCompats.materials.modifier.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;

/**
 * Hephaestus Tools names every tool just "Sword", "Pickaxe"... Prefix the name with the material of the first part
 * (the head, blade or limb), so a tool made of Rifting is called "Rifting Sword". Works for every material.
 * Hephaestus Tools is only a runtime dependency, so its API is called by reflection.
 */
@Mixin(targets = "com.titammods.hephaestus_tools.tools.item.ModifiableItem", remap = false)
public abstract class ToolNameMixin {
    private static final String BASE = "com.titammods.hephaestus_tools.";
    private static Method isInitialized;
    private static Method getMaterial;
    private static Method isEmpty;
    private static Method translationKey;
    private static boolean failed;

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true, remap = false)
    private void thaumaturgetechcompats$materialPrefix(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        if (failed) {
            return;
        }
        try {
            if (getMaterial == null) {
                ClassLoader loader = getClass().getClassLoader();
                Class<?> toolStack = Class.forName(BASE + "tools.nbt.ToolStack", true, loader);
                Class<?> materialId = Class.forName(BASE + "materials.MaterialId", true, loader);
                isInitialized = toolStack.getMethod("isInitialized", ItemStack.class);
                isEmpty = materialId.getMethod("isEmpty");
                translationKey = materialId.getMethod("translationKey");
                getMaterial = toolStack.getMethod("getMaterial", ItemStack.class, int.class);
            }
            if (!(Boolean) isInitialized.invoke(null, stack)) {
                return;
            }
            Object head = getMaterial.invoke(null, stack, 0);
            if (head == null || (Boolean) isEmpty.invoke(head)) {
                return;
            }
            cir.setReturnValue(Component.translatable("item.thaumaturgetechcompats.hephaestus_tool_name",
                    Component.translatable((String) translationKey.invoke(head)), cir.getReturnValue()));
        } catch (ReflectiveOperationException | RuntimeException e) {
            failed = true;
        }
    }
}
