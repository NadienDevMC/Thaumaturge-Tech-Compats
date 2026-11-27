package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import edivad.solargeneration.tools.Translations;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

/** Same layout and texture as the Solar Generation panel screen, fed by the menu data. */
public class ThaumSolarPanelScreen extends AbstractContainerScreen<ThaumSolarPanelMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(SolarGenerationIntegration.MOD_ID, "textures/gui/solar_panel.png");

    public ThaumSolarPanelScreen(ThaumSolarPanelMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (mouseX > leftPos + 7 && mouseX < leftPos + 29 && mouseY > topPos + 10 && mouseY < topPos + 77) {
            var component = Component.translatable(Translations.ENERGY, percent());
            var tooltip = ClientTooltipComponent.create(component.getVisualOrderText());
            graphics.tooltip(font, List.of(tooltip), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        SolarPanelType type = menu.panel().type();
        label(graphics, Component.translatable(Translations.STORED_ENERGY, format(menu.energy())), 20);
        label(graphics, Component.translatable(Translations.CAPACITY, format(type.capacity())), 30);
        label(graphics, Component.translatable(Translations.GENERATION, menu.production()), 40);
    }

    private void label(GuiGraphicsExtractor graphics, Component text, int y) {
        graphics.text(font, text, (imageWidth / 2 - font.width(text) / 2) + 14, y, 0xFF333333, false);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        int y = 60 - (60 * percent() / 100);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 10, topPos + 12 + y, imageWidth, 0, 16, 60 - y, 256, 256);
    }

    private static String format(int energy) {
        return energy >= 1_000_000 ? (energy / 1000) + " kFE" : energy + " FE";
    }

    private int percent() {
        return (int) ((long) menu.energy() * 100L / menu.panel().type().capacity());
    }
}
