package nadiendev.ThaumaturgeTechCompats.tooltip.client;

import com.mojang.datafixers.util.Either;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.tooltip.ItemPreview;
import nadiendev.ThaumaturgeTechCompats.tooltip.ItemPreviewConfig;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.util.List;

public final class ItemPreviewClientEvents {
    private ItemPreviewClientEvents() {
    }

    @EventBusSubscriber(modid = ThaumaturgeTechCompats.MOD_ID, value = Dist.CLIENT)
    public static final class ModBus {
        private ModBus() {
        }

        @SubscribeEvent
        public static void onRegisterFactories(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(ItemPreview.class, ClientItemPreview::new);
        }
    }

    @EventBusSubscriber(modid = ThaumaturgeTechCompats.MOD_ID, value = Dist.CLIENT)
    public static final class GameBus {
        private GameBus() {
        }

        @SubscribeEvent
        public static void onGather(RenderTooltipEvent.GatherComponents event) {
            if (!ItemPreviewConfig.enabled() || event.getItemStack().isEmpty()) {
                return;
            }
            List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
            // Items that already bring their own picture keep it.
            for (Either<FormattedText, TooltipComponent> element : elements) {
                if (element.right().isPresent()) {
                    return;
                }
            }
            elements.add(Math.min(1, elements.size()), Either.right(new ItemPreview(event.getItemStack().copyWithCount(1))));
        }
    }
}
