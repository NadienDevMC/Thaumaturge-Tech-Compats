package nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools;

import com.mojang.serialization.Codec;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class MekanismToolRegistry {
    public static final String ENERGY_NAME = "mekanism_tool_energy";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgeTechCompats.MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ThaumaturgeTechCompats.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY = COMPONENTS.register(ENERGY_NAME,
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());

    private static final Map<MekanismToolTier, Map<MekanismToolKind, DeferredItem<Item>>> TOOLS = new EnumMap<>(MekanismToolTier.class);

    private MekanismToolRegistry() {
    }

    public static String itemId(MekanismToolTier tier, MekanismToolKind kind) {
        return "energized_" + tier.id() + "_" + kind.id();
    }

    public static void register(IEventBus modBus) {
        for (MekanismToolTier tier : MekanismToolTier.values()) {
            if (!MekanismToolIntegration.tierEnabled(tier)) {
                continue;
            }
            Map<MekanismToolKind, DeferredItem<Item>> kinds = new EnumMap<>(MekanismToolKind.class);
            for (MekanismToolKind kind : MekanismToolKind.values()) {
                kinds.put(kind, ITEMS.registerItem(itemId(tier, kind), properties -> create(tier, kind, properties), () -> new Item.Properties().rarity(tier.rarity())));
            }
            TOOLS.put(tier, kinds);
        }
        COMPONENTS.register(modBus);
        ITEMS.register(modBus);
        modBus.addListener(MekanismToolRegistry::onModifyDefaults);
        modBus.addListener(MekanismToolRegistry::onRegisterCapabilities);
    }

    public static List<Item> items() {
        List<Item> items = new ArrayList<>();
        for (Map<MekanismToolKind, DeferredItem<Item>> kinds : TOOLS.values()) {
            for (DeferredItem<Item> holder : kinds.values()) {
                items.add(holder.get());
            }
        }
        return items;
    }

    private static Item create(MekanismToolTier tier, MekanismToolKind kind, Item.Properties properties) {
        return switch (kind) {
            case PICKAXE -> new MekanismPickaxeItem(tier, properties);
            case AXE -> new MekanismAxeItem(tier, properties);
            case SHOVEL -> new MekanismShovelItem(tier, properties);
            case HOE -> new MekanismHoeItem(tier, properties);
            case SWORD -> new MekanismSwordItem(tier, properties);
        };
    }

    private static void onModifyDefaults(ModifyDefaultComponentsEvent event) {
        for (Map<MekanismToolKind, DeferredItem<Item>> kinds : TOOLS.values()) {
            for (DeferredItem<Item> holder : kinds.values()) {
                event.modify(holder.get(), (builder, context, item) -> builder
                        .set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                        .set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.UNBREAKABLE, true)));
            }
        }
    }

    private static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : items()) {
            event.registerItem(Capabilities.Energy.ITEM, (stack, access) -> new MekanismToolHandler(stack, access), item);
        }
    }
}
