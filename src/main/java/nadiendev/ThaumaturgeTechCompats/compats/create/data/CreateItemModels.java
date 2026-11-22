package nadiendev.ThaumaturgeTechCompats.compats.create.data;

import nadiendev.ThaumaturgeTechCompats.compats.create.seal.CreateSeals;
import nadiendev.ThaumaturgeTechCompats.datagen.model.ModelAssets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;

public class CreateItemModels extends ModelAssets {
    public CreateItemModels(PackOutput output) {
        super(output);
    }

    @Override
    public String getName() {
        return "Create Item Models: " + MOD;
    }

    @Override
    protected void register() {
        basicItem(BuiltInRegistries.ITEM.getKey(CreateSeals.ITEM.get()).getPath());
    }
}
