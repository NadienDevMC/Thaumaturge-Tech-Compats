package nadiendev.ThaumaturgeTechCompats.planetary.pearl;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.planetary.PlanetaryTier;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PearlAssets implements DataProvider {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final int HALO_COLOR = -872415232;
    private static final int HALO_SIZE = 4;
    private static final int INFINITY_HALO_SIZE = 6;

    private final PackOutput output;

    public PearlAssets(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (PlanetaryTier tier : PlanetaryTier.values()) {
            String name = PearlRegistry.name(tier);
            tasks.add(ContentJson.haloItem(output, cache, name, HALO_COLOR, tier == PlanetaryTier.INFINITY ? INFINITY_HALO_SIZE : HALO_SIZE, false));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats planetary pearl assets";
    }
}
