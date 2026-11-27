package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import edivad.solargeneration.tools.SolarGenerationDataComponents;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Registers the panels under this mod. The block entity and menu are our own (Solar Generation
 * ties its own to a fixed enum of tiers), but they reuse its battery, sun curve, texture and
 * energy data component.
 */
final class SolarGenerationRegistry {
    private static final String ENTITY_ID = "generation_solar_panel";

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ThaumaturgeTechCompats.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgeTechCompats.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ThaumaturgeTechCompats.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, ThaumaturgeTechCompats.MOD_ID);

    private static final Map<SolarPanelType, DeferredBlock<ThaumSolarPanelBlock>> PANELS = new EnumMap<>(SolarPanelType.class);
    private static final List<DeferredItem<? extends Item>> ITEM_LIST = new ArrayList<>();

    private static DeferredHolder<BlockEntityType<?>, BlockEntityType<ThaumSolarPanelBlockEntity>> blockEntity;
    private static DeferredHolder<MenuType<?>, MenuType<ThaumSolarPanelMenu>> menu;

    private SolarGenerationRegistry() {
    }

    static void register(IEventBus modBus) {
        for (SolarPanelType type : SolarPanelType.values()) {
            if (!type.enabled()) {
                continue;
            }
            DeferredBlock<ThaumSolarPanelBlock> block = BLOCKS.registerBlock(type.id(),
                    properties -> new ThaumSolarPanelBlock(type, properties), SolarGenerationRegistry::panelProperties);
            PANELS.put(type, block);
            ITEM_LIST.add(ITEMS.registerItem(type.id(), properties -> new ThaumSolarPanelItem(block.get(), type,
                    properties.useBlockDescriptionPrefix().component(SolarGenerationDataComponents.ENERGY_COMPONENT, 0))));
        }
        blockEntity = BLOCK_ENTITIES.register(ENTITY_ID, () -> new BlockEntityType<>(ThaumSolarPanelBlockEntity::new, blocks()));
        menu = MENUS.register(ENTITY_ID, () -> new MenuType<>((IContainerFactory<ThaumSolarPanelMenu>) (id, inventory, buffer) -> {
            var pos = buffer.readBlockPos();
            if (inventory.player.level().getBlockEntity(pos) instanceof ThaumSolarPanelBlockEntity panel) {
                return new ThaumSolarPanelMenu(id, panel, new net.minecraft.world.inventory.SimpleContainerData(ThaumSolarPanelMenu.DATA_COUNT));
            }
            return null;
        }, FeatureFlags.DEFAULT_FLAGS));
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
        MENUS.register(modBus);
        modBus.addListener(SolarGenerationRegistry::registerCapabilities);
    }

    private static BlockBehaviour.Properties panelProperties() {
        return BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Energy.BLOCK, blockEntity.get(), ThaumSolarPanelBlockEntity::getBattery);
    }

    static Block[] blocks() {
        return PANELS.values().stream().map(DeferredBlock::get).toArray(Block[]::new);
    }

    static List<Item> items() {
        List<Item> items = new ArrayList<>();
        for (DeferredItem<? extends Item> item : ITEM_LIST) {
            items.add(item.get());
        }
        return items;
    }

    static BlockEntityType<ThaumSolarPanelBlockEntity> blockEntityType() {
        return blockEntity.get();
    }

    static MenuType<ThaumSolarPanelMenu> menuType() {
        return menu.get();
    }
}
