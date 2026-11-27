package nadiendev.ThaumaturgeTechCompats.compats.mekanism;

import nadiendev.ThaumaturgeTechCompats.compats.common.ClusterMaterial;
import nadiendev.ThaumaturgeTechCompats.datagen.model.ModelAssets;
import net.minecraft.data.PackOutput;

public class MekanismClusterItemModels extends ModelAssets {
    public MekanismClusterItemModels(PackOutput output) {
        super(output);
    }

    @Override
    protected void register() {
        for (ClusterMaterial material : ClusterMaterial.ownMaterials()) {
            basicItem(material.itemName());
        }
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats Mekanism cluster item models";
    }
}
