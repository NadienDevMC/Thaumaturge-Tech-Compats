package nadiendev.ThaumaturgeTechCompats.research.mixin;

import com.leclowndu93150.thaumaturge.api.capability.PlayerResearch;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.client.render.GuiBlend;
import com.leclowndu93150.thaumaturge.client.screen.research.ThaumonomiconBrowserScreen;
import nadiendev.ThaumaturgeTechCompats.research.ResearchTabIcons;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Thaumonomicon tabs of this mod: the real item instead of the flat texture, and the free slots of the left
 * column (under Thaumaturge's own tabs) before the paged right column. Tabs of other mods are untouched.
 */
@Mixin(ThaumonomiconBrowserScreen.class)
public abstract class ThaumonomiconTabsMixin {
    /** Same values as Thaumaturge: tabs start 10 + 24 px down, the column ends 32 + 28 px above the bottom. */
    @Unique
    private static final int TTC_STRIDE = 24;
    @Unique
    private static final int TTC_RESERVED_HEIGHT = 32 + 28;

    @Shadow
    @Final
    private List<Holder.Reference<IResearchCategory>> categoriesTT;

    @Shadow
    @Final
    private List<Holder.Reference<IResearchCategory>> categoriesOther;

    @Unique
    private Holder.Reference<IResearchCategory> ttc$drawingTab;

    @Inject(method = "loadRegistryData", at = @At("TAIL"), require = 0)
    private void ttcLeftColumn(CallbackInfo ci) {
        int height = ((Screen) (Object) this).height;
        ResearchTabIcons.arrange(categoriesTT, categoriesOther, tab -> tab.key().location(),
                Math.max(0, (height - TTC_RESERVED_HEIGHT) / TTC_STRIDE));
    }

    @Inject(method = "drawCategoryButton", at = @At("HEAD"), require = 0)
    private void ttcRememberTab(GuiGraphics graphics, PlayerResearch research, Holder.Reference<IResearchCategory> category,
                                int x, int y, int mouseX, int mouseY, boolean addOn, int shift, CallbackInfo ci) {
        ttc$drawingTab = category;
    }

    /** The second tinted blit of the button is the 16x16 category icon (the first one is the tab frame). */
    @Redirect(method = "drawCategoryButton",
            at = @At(value = "INVOKE",
                    target = "Lcom/leclowndu93150/thaumaturge/client/render/GuiBlend;blitTinted(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIFFIIIII)V",
                    ordinal = 1),
            require = 0)
    private void ttcTabIcon(GuiGraphics graphics, ResourceLocation texture, int x, int y, float u, float v,
                            int width, int height, int textureWidth, int textureHeight, int tint) {
        Holder.Reference<IResearchCategory> tab = ttc$drawingTab;
        ItemStack icon = tab == null ? ItemStack.EMPTY : ResearchTabIcons.forCategory(tab.key().location());
        if (icon.isEmpty()) {
            GuiBlend.blitTinted(graphics, texture, x, y, u, v, width, height, textureWidth, textureHeight, tint);
            return;
        }
        graphics.renderFakeItem(icon, x, y);
    }
}
