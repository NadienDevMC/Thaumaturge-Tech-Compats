package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class HephaestusSmelteryRegistry {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, MOD);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MOD);
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD);

    private static final Map<MoltenMetal, DeferredHolder<FluidType, FluidType>> TYPES = new EnumMap<>(MoltenMetal.class);
    private static final Map<MoltenMetal, DeferredHolder<Fluid, BaseFlowingFluid.Source>> SOURCES = new EnumMap<>(MoltenMetal.class);
    private static final Map<MoltenMetal, DeferredHolder<Fluid, BaseFlowingFluid.Flowing>> FLOWING = new EnumMap<>(MoltenMetal.class);
    private static final Map<MoltenMetal, DeferredBlock<LiquidBlock>> BLOCK_MAP = new EnumMap<>(MoltenMetal.class);
    private static final Map<MoltenMetal, DeferredItem<BucketItem>> BUCKETS = new EnumMap<>(MoltenMetal.class);

    private HephaestusSmelteryRegistry() {
    }

    public static void register(IEventBus modBus) {
        for (MoltenMetal metal : MoltenMetal.values()) {
            TYPES.put(metal, FLUID_TYPES.register(metal.fluidName(), () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid_type." + MOD + "." + metal.fluidName())
                    .lightLevel(12)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(metal.temperature())
                    .rarity(Rarity.RARE)
                    .canSwim(false)
                    .canDrown(false)
                    .canPushEntity(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .supportsBoating(false)
                    .pathType(PathType.LAVA)
                    .adjacentPathType(null)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA))));
            SOURCES.put(metal, FLUIDS.register(metal.fluidName(), () -> new BaseFlowingFluid.Source(properties(metal))));
            FLOWING.put(metal, FLUIDS.register(metal.flowingName(), () -> new BaseFlowingFluid.Flowing(properties(metal))));
            BLOCK_MAP.put(metal, BLOCKS.registerBlock(metal.fluidName(),
                    props -> new LiquidBlock(SOURCES.get(metal).get(), props),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)));
            BUCKETS.put(metal, ITEMS.registerItem(metal.bucketName(),
                    props -> new BucketItem(SOURCES.get(metal).get(), props),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.RARE)));
        }
        FLUID_TYPES.register(modBus);
        FLUIDS.register(modBus);
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }

    private static BaseFlowingFluid.Properties properties(MoltenMetal metal) {
        return new BaseFlowingFluid.Properties(TYPES.get(metal), SOURCES.get(metal), FLOWING.get(metal))
                .block(BLOCK_MAP.get(metal))
                .bucket(BUCKETS.get(metal))
                .slopeFindDistance(2)
                .levelDecreasePerBlock(2)
                .tickRate(30);
    }

    public static Supplier<? extends Fluid> source(MoltenMetal metal) {
        return SOURCES.get(metal);
    }

    public static Supplier<? extends Fluid> flowing(MoltenMetal metal) {
        return FLOWING.get(metal);
    }

    public static FluidType type(MoltenMetal metal) {
        return TYPES.get(metal).get();
    }

    public static Item bucket(MoltenMetal metal) {
        return BUCKETS.get(metal).get();
    }

    public static List<Item> buckets() {
        List<Item> items = new ArrayList<>();
        for (DeferredItem<BucketItem> bucket : BUCKETS.values()) {
            items.add(bucket.get());
        }
        return items;
    }
}
