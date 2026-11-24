package nadiendev.ThaumaturgeTechCompats.edible;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EdibleItem extends Item {
    public static final String NAME_KEY = "item." + ThaumaturgeTechCompats.MOD_ID + ".edible";

    private final EdibleEntry entry;

    public EdibleItem(EdibleEntry entry, Properties properties) {
        super(properties);
        this.entry = entry;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component original = BuiltInRegistries.ITEM.getOptional(entry.original())
                .map(item -> new ItemStack(item).getHoverName())
                .orElseGet(() -> Component.translatable(entry.original().toLanguageKey("item")));
        return Component.translatable(NAME_KEY, original);
    }
}
