package nadiendev.ThaumaturgeTechCompats.jei.catalysts;

import com.leclowndu93150.thaumaturge.compat.jei.category.AspectFromStacksCategory;
import com.leclowndu93150.thaumaturge.compat.jei.category.CrucibleCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import nadiendev.ThaumaturgeTechCompats.device.DeviceTier;
import nadiendev.ThaumaturgeTechCompats.furnace.FurnaceIntegration;
import nadiendev.ThaumaturgeTechCompats.smelter.SmelterTier;

final class ThaumaturgeCatalysts {
    static final String MOD_ID = "thaumaturge";

    private ThaumaturgeCatalysts() {
    }

    static void register(IRecipeCatalystRegistration registration) {
        // The Rift Jar and the Flux Jar work beside a crucible: the Rifting Ingot needs a Rift Jar next to it, and a Flux Jar takes its leaks
        CatalystSupport.add(registration, "rift_jar", CrucibleCategory.RECIPE_TYPE);
        CatalystSupport.add(registration, "flux_jar", CrucibleCategory.RECIPE_TYPE);
        for (SmelterTier tier : SmelterTier.values()) {
            CatalystSupport.add(registration, tier.blockName(), AspectFromStacksCategory.RECIPE_TYPE);
        }
        for (DeviceTier tier : DeviceTier.values()) {
            CatalystSupport.add(registration, FurnaceIntegration.blockName(tier), AspectFromStacksCategory.RECIPE_TYPE);
        }
    }
}
