package nadiendev.ThaumaturgeTechCompats.metals;

import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandCap;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandRod;
import com.leclowndu93150.thaumaturge.content.wands.WandVisHelper;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import nadiendev.ThaumaturgeTechCompats.metals.upgrades.ToolUpgradeIntegration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

public final class ExtraMetalRegistry {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD);
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD);
    private static final DeferredRegister<WandRod> RODS = DeferredRegister.create(WandRod.REGISTRY_KEY, MOD);
    private static final DeferredRegister<WandCap> CAPS = DeferredRegister.create(WandCap.REGISTRY_KEY, MOD);

    private static final Map<ExtraMetal, DeferredItem<Item>> INGOTS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredItem<Item>> NUGGETS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredItem<Item>> CLUSTERS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredBlock<Block>> BLOCK_MAP = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredItem<BlockItem>> BLOCK_ITEMS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredHolder<WandRod, WandRod>> ROD_PARTS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredHolder<WandCap, WandCap>> CAP_PARTS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredItem<ItemWandRod>> ROD_ITEMS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredItem<ItemWandCap>> CAP_ITEMS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredItem<Item>> INERT_RODS = new EnumMap<>(ExtraMetal.class);
    private static final Map<ExtraMetal, DeferredItem<Item>> INERT_CAPS = new EnumMap<>(ExtraMetal.class);

    static {
        for (ExtraMetal metal : ExtraMetal.values()) {
            INGOTS.put(metal, ITEMS.registerSimpleItem(metal.ingotName(), new Item.Properties().rarity(metal.rarity())));
            NUGGETS.put(metal, ITEMS.registerSimpleItem(metal.nuggetName(), new Item.Properties().rarity(metal.rarity())));
            CLUSTERS.put(metal, ITEMS.registerSimpleItem(metal.clusterName(), new Item.Properties().rarity(metal.rarity())));
            DeferredBlock<Block> block = BLOCKS.registerSimpleBlock(metal.blockName(), BlockBehaviour.Properties.of()
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL));
            BLOCK_MAP.put(metal, block);
            BLOCK_ITEMS.put(metal, ITEMS.registerSimpleBlockItem(block, new Item.Properties().rarity(metal.rarity())));

            INERT_CAPS.put(metal, ITEMS.registerSimpleItem(metal.inertCapName(), new Item.Properties().rarity(metal.rarity())));
            INERT_RODS.put(metal, ITEMS.registerSimpleItem(metal.inertRodName(), new Item.Properties().rarity(metal.rarity())));

            DeferredHolder<WandRod, WandRod> rod = RODS.register(metal.id(), () -> new WandRod(
                    metal.rodCapacity(),
                    metal.rodCraftCost(),
                    ThaumaturgeTechCompats.id("textures/models/wand_rod_" + metal.id() + ".png"),
                    null,
                    metal.rodGlow(),
                    false,
                    false,
                    ThaumaturgeTechCompats.id(ExtraMetal.RESEARCH)));
            ROD_PARTS.put(metal, rod);
            ROD_ITEMS.put(metal, ITEMS.registerItem(metal.rodName(), props -> new ItemWandRod(props, rod),
                    new Item.Properties().rarity(metal.rarity())));

            DeferredHolder<WandCap, WandCap> cap = CAPS.register(metal.id(), () -> new WandCap(
                    metal.capBaseCost(),
                    metal.capAspects(),
                    metal.capSpecialCost(),
                    metal.capCraftCost(),
                    ThaumaturgeTechCompats.id("textures/models/wand_cap_" + metal.id() + ".png")));
            CAP_PARTS.put(metal, cap);
            CAP_ITEMS.put(metal, ITEMS.registerItem(metal.capName(), props -> new ItemWandCap(props, cap),
                    new Item.Properties().rarity(metal.rarity())));
        }
    }

    private ExtraMetalRegistry() {
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        RODS.register(modBus);
        CAPS.register(modBus);
    }

    public static Item ingot(ExtraMetal metal) {
        return INGOTS.get(metal).get();
    }

    public static void fillCreativeTab(CreativeModeTab.Output output) {
        for (ExtraMetal metal : ExtraMetal.values()) {
            output.accept(INGOTS.get(metal).get());
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            output.accept(BLOCK_ITEMS.get(metal).get());
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            output.accept(NUGGETS.get(metal).get());
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            output.accept(CAP_ITEMS.get(metal).get());
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            output.accept(ROD_ITEMS.get(metal).get());
        }
        // Hephaestus Tools parts: sword, axe, pickaxe, hammer, shovel, hoe, bow.
        boolean tools = ModList.get().isLoaded(ToolUpgradeIntegration.TOOLS_MOD_ID);
        for (int group = 0; group < 7; group++) {
            if (tools) {
                ExtraMetalToolParts.fill(output, group);
            }
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            output.accept(CLUSTERS.get(metal).get());
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            output.accept(INERT_CAPS.get(metal).get());
            output.accept(INERT_RODS.get(metal).get());
        }
        for (ExtraMetal metal : ExtraMetal.values()) {
            ItemStack wand = ItemWand.create(TTItems.WAND.get(), CAP_PARTS.get(metal).get(), ROD_PARTS.get(metal).get(), false);
            WandVisHelper.fill(wand);
            output.accept(wand);
        }
    }
}
