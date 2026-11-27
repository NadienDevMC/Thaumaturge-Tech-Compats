package nadiendev.ThaumaturgeTechCompats.research.mixin;

import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.client.screen.research.ThaumonomiconBrowserScreen;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import nadiendev.ThaumaturgeTechCompats.research.ResearchTabIcons;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

/**
 * Thaumonomicon tabs of this mod: the real item instead of the flat texture, and the free slots of the left
 * column (under Thaumaturge's own tabs) before the paged right column. Tabs of other mods are untouched.
 *
 * <p>Thaumaturge lays out, draws and clicks every tab from the model's built-in and add-on lists, so moving our
 * tabs from one list to the other keeps drawing, clicks, tooltips, paging and the selected tab consistent.</p>
 */
@Mixin(ThaumonomiconBrowserScreen.class)
public abstract class ThaumonomiconTabsMixin {
    /** The model field has a package-private type, so it is read by reflection. */
    @Unique
    private static Field ttc$modelField;

    @Unique
    private IResearchCategory ttc$drawingTab;

    @Shadow
    private int tabsPerPage() {
        throw new AssertionError();
    }

    @Unique
    private BrowserModelAccessor ttc$model() {
        try {
            if (ttc$modelField == null) {
                Field field = ThaumonomiconBrowserScreen.class.getDeclaredField("model");
                field.setAccessible(true);
                ttc$modelField = field;
            }
            Object model = ttc$modelField.get(this);
            return model instanceof BrowserModelAccessor accessor ? accessor : null;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null;
        }
    }

    /** Right after the model is built in init (also on every resize), before the active tab and the scroll are restored. */
    @Inject(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lcom/leclowndu93150/thaumaturge/client/screen/research/ThaumonomiconBrowserScreen;resolveActive()Lcom/leclowndu93150/thaumaturge/client/screen/research/BrowserModel$CategoryRef;"),
            require = 0)
    private void ttcLeftColumn(CallbackInfo ci) {
        BrowserModelAccessor model = ttc$model();
        if (model != null) {
            ResearchTabIcons.arrange(model.ttc$builtIn(), model.ttc$addOn(), tab -> ((CategoryRefAccessor) tab).ttc$id(), tabsPerPage());
        }
    }

    @Redirect(method = "drawTabs",
            at = @At(value = "INVOKE",
                    target = "Lcom/leclowndu93150/thaumaturge/api/research/IResearchCategory;icon()Lnet/minecraft/resources/Identifier;"),
            require = 0)
    private Identifier ttcRememberTab(IResearchCategory category) {
        ttc$drawingTab = category;
        return category.icon();
    }

    /** The only 16x16 textured blit of drawTabs is the category icon (the tab frame goes through sprite()). */
    @Redirect(method = "drawTabs",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V"),
            require = 0)
    private void ttcTabIcon(GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier texture, int x, int y, float u, float v,
                            int width, int height, int textureWidth, int textureHeight, int color) {
        ItemStack icon = ttc$iconOf(ttc$drawingTab);
        if (icon.isEmpty()) {
            graphics.blit(pipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight, color);
            return;
        }
        graphics.fakeItem(icon, x, y);
    }

    @Unique
    private ItemStack ttc$iconOf(IResearchCategory data) {
        BrowserModelAccessor model = data == null ? null : ttc$model();
        if (model == null) {
            return ItemStack.EMPTY;
        }
        for (Object tab : model.ttc$builtIn()) {
            if (((CategoryRefAccessor) tab).ttc$data() == data) {
                return ResearchTabIcons.forCategory(((CategoryRefAccessor) tab).ttc$id());
            }
        }
        for (Object tab : model.ttc$addOn()) {
            if (((CategoryRefAccessor) tab).ttc$data() == data) {
                return ResearchTabIcons.forCategory(((CategoryRefAccessor) tab).ttc$id());
            }
        }
        return ItemStack.EMPTY;
    }
}
