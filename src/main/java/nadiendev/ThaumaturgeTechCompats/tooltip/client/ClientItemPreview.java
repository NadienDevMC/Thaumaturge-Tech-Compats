package nadiendev.ThaumaturgeTechCompats.tooltip.client;

import nadiendev.ThaumaturgeTechCompats.tooltip.ItemPreview;
import nadiendev.ThaumaturgeTechCompats.tooltip.ItemPreviewConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class ClientItemPreview implements ClientTooltipComponent {
    private static final int ICON = 16;
    private static final int PADDING = 2;

    private final ItemPreview preview;

    public ClientItemPreview(ItemPreview preview) {
        this.preview = preview;
    }

    private static int size() {
        return ICON * ItemPreviewConfig.scale();
    }

    @Override
    public int getHeight() {
        return size() + PADDING * 2;
    }

    @Override
    public int getWidth(Font font) {
        return size();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        int scale = ItemPreviewConfig.scale();
        graphics.pose().pushPose();
        graphics.pose().translate(x, y + PADDING, 0.0F);
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.renderFakeItem(preview.stack(), 0, 0);
        graphics.pose().popPose();
    }
}
