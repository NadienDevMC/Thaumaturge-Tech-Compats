package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public final class HephaestusSmelteryClient {
    // Hephaestus molten metal textures (stitched by its own blocks atlas source), reused and tinted per metal
    private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath("hephaestus", "fluid/liquid/molten_metal");
    private static final ResourceLocation FLOWING = ResourceLocation.fromNamespaceAndPath("hephaestus", "fluid/liquid/molten_metal_flow");

    private HephaestusSmelteryClient() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(HephaestusSmelteryClient::registerExtensions);
        modBus.addListener(HephaestusSmelteryClient::registerItemColors);
    }

    private static void registerExtensions(RegisterClientExtensionsEvent event) {
        for (MoltenMetal metal : MoltenMetal.values()) {
            int color = metal.color();
            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return STILL;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return FLOWING;
                }

                @Override
                public int getTintColor() {
                    return color;
                }
            }, HephaestusSmelteryRegistry.type(metal));
        }
    }

    // Tints the whole bucket sprite (layer 0)
    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (MoltenMetal metal : MoltenMetal.values()) {
            int color = metal.color();
            event.register((stack, layer) -> layer == 0 ? color : 0xFFFFFFFF, HephaestusSmelteryRegistry.bucket(metal));
        }
    }
}
