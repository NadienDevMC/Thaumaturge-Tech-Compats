package nadiendev.ThaumaturgeTechCompats.compats.solarflux;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Optional;

/** Irradiates players carrying the void uranium panel. Does nothing without Mekanism. */
public final class VoidUraniumRadiation {
    // Solar Flux registers every panel as solarflux:sp_{name}
    private static final ResourceLocation PANEL = ResourceLocation.fromNamespaceAndPath(SolarFluxIntegration.MOD_ID, "sp_" + SolarFluxPanels.VOID_URANIUM);
    // Radiation dose (Sv) added per panel on every check
    private static final double MAGNITUDE_PER_STACK = 0.0002D;
    private static final int INTERVAL = 20;

    private VoidUraniumRadiation() {
    }

    public static void register(IEventBus gameBus) {
        if (ModList.get().isLoaded("mekanism")) {
            gameBus.addListener(VoidUraniumRadiation::onPlayerTick);
        }
    }

    private static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % INTERVAL != 0
                || player.isCreative() || player.isSpectator()) {
            return;
        }
        Optional<Item> panel = BuiltInRegistries.ITEM.getOptional(PANEL);
        if (panel.isEmpty()) {
            return;
        }
        int stacks = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(panel.get())) {
                stacks += stack.getCount();
            }
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.is(panel.get())) {
                stacks += stack.getCount();
            }
        }
        if (stacks > 0) {
            Mekanism.radiate(player, MAGNITUDE_PER_STACK * stacks);
        }
    }

    /** Holder so Mekanism classes load only when this is first used. */
    private static final class Mekanism {
        static void radiate(ServerPlayer player, double magnitude) {
            mekanism.api.radiation.IRadiationManager.INSTANCE.radiate(player, magnitude);
        }
    }
}
