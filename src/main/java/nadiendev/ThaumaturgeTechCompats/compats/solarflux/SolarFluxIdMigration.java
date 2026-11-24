package nadiendev.ThaumaturgeTechCompats.compats.solarflux;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;

/**
 * Aliases the retired panel ids to their replacements, so blocks and items already stored in worlds
 * (and sent by clients) load as the new panel. NeoForge applies registry aliases when loading saves.
 */
public final class SolarFluxIdMigration {
    private static final String NAMESPACE = SolarFluxIntegration.MOD_ID;

    /** Old registry path to new registry path, all in the solarflux namespace. */
    private static final Map<String, String> OLD_TO_NEW = Map.of(
            "sp_alchemical_brass", "sp_brass" + SolarFluxPanels.SUFFIX);

    private SolarFluxIdMigration() {
    }

    public static void register() {
        ModList.get().getModContainerById(ThaumaturgeTechCompats.MOD_ID)
                .ifPresent(container -> {
                    if (container.getEventBus() != null) {
                        container.getEventBus().addListener(SolarFluxIdMigration::onRegister);
                    }
                });
    }

    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            alias(event, Registries.BLOCK);
        } else if (event.getRegistryKey().equals(Registries.ITEM)) {
            alias(event, Registries.ITEM);
        }
    }

    private static <T> void alias(RegisterEvent event, ResourceKey<? extends Registry<T>> key) {
        Registry<T> registry = event.getRegistry(key);
        OLD_TO_NEW.forEach((oldPath, newPath) -> registry.addAlias(
                ResourceLocation.fromNamespaceAndPath(NAMESPACE, oldPath),
                ResourceLocation.fromNamespaceAndPath(NAMESPACE, newPath)));
    }
}
