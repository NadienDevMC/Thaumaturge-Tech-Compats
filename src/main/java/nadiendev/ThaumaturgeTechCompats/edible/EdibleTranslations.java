package nadiendev.ThaumaturgeTechCompats.edible;

import net.neoforged.neoforge.common.data.LanguageProvider;

public final class EdibleTranslations {
    private EdibleTranslations() {
    }

    public static void english(LanguageProvider lang) {
        lang.add(EdibleItem.NAME_KEY, "Edible %s");
    }

    public static void spanish(LanguageProvider lang) {
        lang.add(EdibleItem.NAME_KEY, "%s comestible");
    }
}
