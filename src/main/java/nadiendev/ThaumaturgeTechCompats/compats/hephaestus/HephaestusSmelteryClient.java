package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSources;

public final class HephaestusSmelteryClient {
    // Hephaestus molten metal textures (stitched by its own blocks atlas source), reused and tinted per metal
    private static final Material STILL = new Material(Identifier.fromNamespaceAndPath("hephaestus", "fluid/liquid/molten_metal"));
    private static final Material FLOWING = new Material(Identifier.fromNamespaceAndPath("hephaestus", "fluid/liquid/molten_metal_flow"));

    private HephaestusSmelteryClient() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(HephaestusSmelteryClient::registerModels);
    }

    private static void registerModels(RegisterFluidModelsEvent event) {
        for (MoltenMetal metal : MoltenMetal.values()) {
            FluidModel.Unbaked model = new FluidModel.Unbaked(STILL, FLOWING, FLOWING, FluidTintSources.constant(metal.color()));
            event.register(model, HephaestusSmelteryRegistry.source(metal), HephaestusSmelteryRegistry.flowing(metal));
        }
    }
}
