package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import net.neoforged.neoforge.common.data.LanguageProvider;

public final class HephaestusSmelteryTranslations {
    private static final String MOD = "thaumaturgetechcompats.";

    private HephaestusSmelteryTranslations() {
    }

    public static void english(LanguageProvider lang) {
        for (MoltenMetal metal : MoltenMetal.values()) {
            add(lang, metal, "Molten " + metal.english(), "Molten " + metal.english() + " Bucket");
        }
    }

    public static void spanish(LanguageProvider lang) {
        for (MoltenMetal metal : MoltenMetal.values()) {
            // Matriz is feminine
            String molten = metal == MoltenMetal.CRYSTAL_MATRIX ? " fundida" : " fundido";
            add(lang, metal, metal.spanish() + molten, "Cubo de " + metal.spanish() + molten);
        }
    }

    private static void add(LanguageProvider lang, MoltenMetal metal, String fluid, String bucket) {
        lang.add("fluid_type." + MOD + metal.fluidName(), fluid);
        lang.add("block." + MOD + metal.fluidName(), fluid);
        lang.add("item." + MOD + metal.bucketName(), bucket);
    }
}
