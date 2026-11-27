package nadiendev.ThaumaturgeTechCompats.materials;

import nadiendev.ThaumaturgeTechCompats.registry.TierOrder;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandCap;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandRod;
import com.leclowndu93150.thaumaturge.content.wands.WandVisHelper;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Ingot, nugget, block, native cluster and the four wand parts (rod, cap, inert rod, inert cap) of each extra metal. */
public final class ExtraMaterialItems {
    public static final Identifier RESEARCH = ThaumaturgeTechCompats.id(ExtraMaterial.RESEARCH);

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgeTechCompats.MOD_ID);
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ThaumaturgeTechCompats.MOD_ID);
    private static final DeferredRegister<WandRod> RODS = DeferredRegister.create(WandRod.REGISTRY_KEY, ThaumaturgeTechCompats.MOD_ID);
    private static final DeferredRegister<WandCap> CAPS = DeferredRegister.create(WandCap.REGISTRY_KEY, ThaumaturgeTechCompats.MOD_ID);

    private static final Map<ExtraMaterial, DeferredItem<Item>> INGOTS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredItem<Item>> NUGGETS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredItem<Item>> CLUSTERS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredBlock<Block>> BLOCK_MAP = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredItem<BlockItem>> BLOCK_ITEMS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredHolder<WandRod, WandRod>> ROD_PARTS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredHolder<WandCap, WandCap>> CAP_PARTS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredItem<ItemWandRod>> ROD_ITEMS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredItem<ItemWandCap>> CAP_ITEMS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredItem<Item>> INERT_RODS = new EnumMap<>(ExtraMaterial.class);
    private static final Map<ExtraMaterial, DeferredItem<Item>> INERT_CAPS = new EnumMap<>(ExtraMaterial.class);

    static {
        for (ExtraMaterial m : ExtraMaterial.values()) {
            Rarity rarity = m.epic() ? Rarity.EPIC : Rarity.RARE;
            INGOTS.put(m, ITEMS.registerItem(m.ingotName(), Item::new, () -> new Item.Properties().rarity(rarity)));
            NUGGETS.put(m, ITEMS.registerItem(m.nuggetName(), Item::new, () -> new Item.Properties().rarity(rarity)));
            CLUSTERS.put(m, ITEMS.registerItem(m.clusterName(), Item::new, () -> new Item.Properties().rarity(rarity)));

            DeferredBlock<Block> block = BLOCKS.registerBlock(m.blockName(), Block::new, () -> BlockBehaviour.Properties.of()
                    .mapColor(mapColor(m))
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops());
            BLOCK_MAP.put(m, block);
            BLOCK_ITEMS.put(m, ITEMS.registerSimpleBlockItem(block, () -> new Item.Properties().rarity(rarity)));

            DeferredHolder<WandRod, WandRod> rod = RODS.register(m.id(), () -> new WandRod(
                    m.rodCapacity(),
                    m.rodCraftCost(),
                    ThaumaturgeTechCompats.id("textures/models/wand_rod_" + m.id() + ".png"),
                    null,
                    m.glow(),
                    false,
                    false,
                    RESEARCH));
            ROD_PARTS.put(m, rod);
            ROD_ITEMS.put(m, ITEMS.registerItem(m.rodName(), properties -> new ItemWandRod(properties, rod),
                    () -> new Item.Properties().rarity(rarity)));

            DeferredHolder<WandCap, WandCap> cap = CAPS.register(m.id(), () -> new WandCap(
                    m.capCost(),
                    aspects(m),
                    m.capSpecialCost(),
                    m.capCraftCost(),
                    ThaumaturgeTechCompats.id("textures/models/wand_cap_" + m.id() + ".png")));
            CAP_PARTS.put(m, cap);
            CAP_ITEMS.put(m, ITEMS.registerItem(m.capName(), properties -> new ItemWandCap(properties, cap),
                    () -> new Item.Properties().rarity(rarity)));

            INERT_RODS.put(m, ITEMS.registerItem(m.inertRodName(), Item::new, () -> new Item.Properties().rarity(rarity)));
            INERT_CAPS.put(m, ITEMS.registerItem(m.inertCapName(), Item::new, () -> new Item.Properties().rarity(rarity)));
        }
    }

    private ExtraMaterialItems() {
    }

    private static MapColor mapColor(ExtraMaterial m) {
        return switch (m) {
            case RIFTING -> MapColor.COLOR_PURPLE;
            case CRIMSON -> MapColor.COLOR_RED;
            case FIBRIC -> MapColor.COLOR_MAGENTA;
            case SALIS -> MapColor.GOLD;
        };
    }

    private static List<ResourceKey<IAspect>> aspects(ExtraMaterial m) {
        List<ResourceKey<IAspect>> keys = new ArrayList<>();
        for (String id : m.capAspects()) {
            keys.add(switch (id) {
                case "vacuos" -> TTAspects.VACUOS;
                case "alienis" -> TTAspects.ALIENIS;
                case "perditio" -> TTAspects.PERDITIO;
                case "humanus" -> TTAspects.HUMANUS;
                case "fabrico" -> TTAspects.FABRICO;
                case "vitium" -> TTAspects.VITIUM;
                case "praecantatio" -> TTAspects.PRAECANTATIO;
                case "potentia" -> TTAspects.POTENTIA;
                default -> throw new IllegalArgumentException("Unknown aspect " + id);
            });
        }
        return keys;
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        RODS.register(modBus);
        CAPS.register(modBus);
    }

    public static DeferredItem<Item> ingot(ExtraMaterial material) {
        return INGOTS.get(material);
    }

    public static DeferredBlock<Block> block(ExtraMaterial material) {
        return BLOCK_MAP.get(material);
    }

    public static List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        for (DeferredBlock<Block> block : BLOCK_MAP.values()) {
            blocks.add(block.get());
        }
        return blocks;
    }

    public static List<Identifier> blockIds() {
        List<Identifier> ids = new ArrayList<>();
        for (ExtraMaterial m : ExtraMaterial.values()) {
            ids.add(ThaumaturgeTechCompats.id(m.blockName()));
        }
        return ids;
    }

    /** By kind of thing, and inside each kind by metal (Rifting, Fibric, Salis, Crimson). */
    public static void fillCreativeTab(CreativeModeTab.Output output) {
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(INGOTS.get(m).get());
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(BLOCK_ITEMS.get(m).get());
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(NUGGETS.get(m).get());
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(CAP_ITEMS.get(m).get());
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(ROD_ITEMS.get(m).get());
        }
        // Hephaestus Tools parts: sword, axe, pickaxe, hammer, shovel, hoe, bow
        for (int group = 0; group < nadiendev.ThaumaturgeTechCompats.materials.modifier.ToolPartCatalog.groupCount(); group++) {
            for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
                for (ItemStack part : nadiendev.ThaumaturgeTechCompats.materials.modifier.ToolPartCatalog.parts(group, m)) {
                    output.accept(part);
                }
            }
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(CLUSTERS.get(m).get());
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(INERT_RODS.get(m).get());
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            output.accept(INERT_CAPS.get(m).get());
        }
        for (ExtraMaterial m : TierOrder.display(ExtraMaterial.values())) {
            ItemStack wand = ItemWand.create(TTItems.WAND.get(), CAP_PARTS.get(m).get(), ROD_PARTS.get(m).get(), false);
            WandVisHelper.fill(wand);
            output.accept(wand);
        }
    }
}
