package nadiendev.ThaumaturgeTechCompats.device;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.auraconverter.AuraConverterIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.create.CreateIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.enderio.EnderIOIntegration;
import nadiendev.ThaumaturgeTechCompats.flux.charger.ChargerIntegration;
import nadiendev.ThaumaturgeTechCompats.flux.core.CoreIntegration;
import nadiendev.ThaumaturgeTechCompats.flux.field.FieldIntegration;
import nadiendev.ThaumaturgeTechCompats.flux.link.LinkIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.botania.BotaniaIntegration;
import nadiendev.ThaumaturgeTechCompats.infusion.InfusionIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.voidminers.VoidMinersMagicalIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.avaritia.machines.AvaritiaMachinesIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.oritech.OritechIntegration;
import nadiendev.ThaumaturgeTechCompats.furnace.FurnaceIntegration;
import nadiendev.ThaumaturgeTechCompats.smelter.SmelterIntegration;
import nadiendev.ThaumaturgeTechCompats.stabilizer.StabilizerIntegration;
import nadiendev.ThaumaturgeTechCompats.materials.ExtraMaterialsIntegration;
import nadiendev.ThaumaturgeTechCompats.transducer.TransducerIntegration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeviceBlockTags extends BlockTagsProvider {
    public DeviceBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, ThaumaturgeTechCompats.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Every entry is optional: most of these blocks only exist when their compat mod is installed,
        // and a missing required entry would break the whole minecraft:mineable/pickaxe tag.
        optionalBlocks(SmelterIntegration.blocks());
        optionalBlocks(StabilizerIntegration.blocks());
        optionalBlocks(TransducerIntegration.blocks());
        optionalBlocks(FurnaceIntegration.blocks());
        optionalBlocks(BotaniaIntegration.blocks());
        optionalBlocks(InfusionIntegration.blocks());
        optionalBlocks(VoidMinersMagicalIntegration.blocks());
        optionalBlocks(AvaritiaMachinesIntegration.blocks());
        optionalBlocks(AuraConverterIntegration.blocks());
        optionalBlocks(CoreIntegration.blocks());
        optionalBlocks(LinkIntegration.blocks());
        optionalBlocks(ChargerIntegration.blocks());
        optionalBlocks(FieldIntegration.blocks());
        optionalBlocks(CreateIntegration.blocks());
        optionalBlocks(ThaummotorIntegration.blocks());
        ExtraMaterialsIntegration.blockIds().forEach(id -> {
            getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE).addOptionalElement(id);
            getOrCreateRawBuilder(BlockTags.NEEDS_IRON_TOOL).addOptionalElement(id);
        });
        nadiendev.ThaumaturgeTechCompats.compats.solargeneration.SolarGenerationIntegration.blockIds().forEach(id -> {
            getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE).addOptionalElement(id);
            getOrCreateRawBuilder(BlockTags.NEEDS_IRON_TOOL).addOptionalElement(id);
        });
        EnderIOIntegration.blockIds().forEach(id -> getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE).addOptionalElement(id));
        OritechIntegration.blockIds().forEach(id -> getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE).addOptionalElement(id));
    }

    private void optionalBlocks(List<Block> blocks) {
        blocks.forEach(block -> getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .addOptionalElement(BuiltInRegistries.BLOCK.getKey(block)));
    }
}
