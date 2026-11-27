package nadiendev.ThaumaturgeTechCompats.datagen.provider;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ModLanguageProvider extends LanguageProvider {
    private static final String BLOCK_PREFIX = "block." + ThaumaturgeTechCompats.MOD_ID + ".";
    private static final String ITEM_PREFIX = "item." + ThaumaturgeTechCompats.MOD_ID + ".";

    private final Consumer<LanguageProvider> translations;
    private final Map<String, String> added = new LinkedHashMap<>();

    public ModLanguageProvider(PackOutput output, String locale, Consumer<LanguageProvider> translations) {
        super(output, ThaumaturgeTechCompats.MOD_ID, locale);
        this.translations = translations;
    }

    @Override
    public void add(String key, String value) {
        added.put(key, value);
        super.add(key, value);
    }

    @Override
    protected void addTranslations() {
        translations.accept(this);
        // Block items registered without useBlockDescriptionPrefix() use item.<mod>.<name> as their name key,
        // so they need the same text as their block.<mod>.<name> key.
        for (Map.Entry<String, String> entry : Map.copyOf(added).entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(BLOCK_PREFIX)) {
                continue;
            }
            String name = key.substring(BLOCK_PREFIX.length());
            String itemKey = ITEM_PREFIX + name;
            if (added.containsKey(itemKey)) {
                continue;
            }
            BuiltInRegistries.ITEM.getOptional(ThaumaturgeTechCompats.id(name))
                    .filter(item -> itemKey.equals(item.getDescriptionId()))
                    .ifPresent(item -> super.add(itemKey, entry.getValue()));
        }
    }
}
