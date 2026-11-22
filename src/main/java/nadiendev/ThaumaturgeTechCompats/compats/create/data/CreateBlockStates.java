package nadiendev.ThaumaturgeTechCompats.compats.create.data;

import nadiendev.ThaumaturgeTechCompats.compats.create.casing.CasingTier;
import nadiendev.ThaumaturgeTechCompats.compats.create.casing.CreateCasings;
import nadiendev.ThaumaturgeTechCompats.datagen.model.ModelAssets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

public class CreateBlockStates extends ModelAssets {
    public CreateBlockStates(PackOutput output) {
        super(output);
    }

    @Override
    public String getName() {
        return "Create Block States: " + MOD;
    }

    @Override
    protected void register() {
        for (CasingTier tier : CasingTier.values()) {
            Block block = CreateCasings.block(tier).get();
            String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
            blockModel(name, model("minecraft:block/cube_all", textures("all", mod("block/" + name))));
            simpleState(block, mod("block/" + name));
            blockItem(name, mod("block/" + name));
        }
    }
}
