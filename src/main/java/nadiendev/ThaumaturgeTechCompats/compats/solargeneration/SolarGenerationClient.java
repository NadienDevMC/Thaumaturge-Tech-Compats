package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

final class SolarGenerationClient {
    private SolarGenerationClient() {
    }

    static void register(IEventBus modBus) {
        modBus.addListener(SolarGenerationClient::registerScreens);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(SolarGenerationRegistry.menuType(), ThaumSolarPanelScreen::new);
    }
}
