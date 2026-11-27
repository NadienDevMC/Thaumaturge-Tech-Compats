package nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools;

import nadiendev.ThaumaturgeTechCompats.datagen.model.ModelAssets;
import net.minecraft.data.PackOutput;

public class MekanismToolItemModels extends ModelAssets {
    public MekanismToolItemModels(PackOutput output) {
        super(output);
    }

    @Override
    protected void register() {
        for (MekanismToolTier tier : MekanismToolTier.values()) {
            for (MekanismToolKind kind : MekanismToolKind.values()) {
                String name = MekanismToolRegistry.itemId(tier, kind);
                item(name, "minecraft:item/handheld", textures("layer0", mod("item/" + name)));
            }
        }
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats Mekanism energy tools item models";
    }
}
