package nadiendev.ThaumaturgeTechCompats.tooltip.client;

import nadiendev.ThaumaturgeTechCompats.tooltip.ItemPreview;
import nadiendev.ThaumaturgeTechCompats.tooltip.ItemPreviewConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
    public int getHeight(Font font) {
        return size() + PADDING * 2;
    }

    @Override
    public int getWidth(Font font) {
        return size();
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        int scale = ItemPreviewConfig.scale();
        graphics.pose().pushMatrix();
        graphics.pose().translate((float) x, (float) (y + PADDING));
        graphics.pose().scale((float) scale, (float) scale);
        graphics.fakeItem(preview.stack(), 0, 0);
        graphics.pose().popMatrix();
    }
}
