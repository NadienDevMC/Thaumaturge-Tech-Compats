package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import net.neoforged.neoforge.common.data.LanguageProvider;

public final class HephaestusSmelteryTranslations {
    private static final String MOD = "thaumaturgetechcompats.";
    private static final String ENTRY = "research.thaumaturgetechcompats." + HephaestusSmelteryContent.ENTRY;

    private HephaestusSmelteryTranslations() {
    }

    public static void english(LanguageProvider lang) {
        lang.add(ENTRY + ".title", "Smeltery Metals");
        lang.add(ENTRY + ".stage_0",
                "The Hephaestus Forge melts every metal it knows, yet the three great metals of Avaritia have always refused its heat. If the smeltery could be taught their nature, they should flow like any other ore.");
        lang.add(ENTRY + ".stage_1",
                "The Crystal Matrix, Neutronium and Infinity can now be melted in the Hephaestus smeltery into their own molten fluids, from ingots, plates and blocks (and nuggets for Neutronium and Infinity). The fluids can be cast again with the ingot and plate casts on a casting table, with the nugget cast for Neutronium and Infinity, and as a block in a casting basin.<BR>The stronger the metal, the hotter the smeltery must be: Crystal Matrix needs a heat of 1100, Neutronium 1500 and Infinity 2000.<BR>Void metal and alchemical brass need nothing new, as Hephaestus Tools already knows them and can make tools from them.");
        for (MoltenMetal metal : MoltenMetal.values()) {
            add(lang, metal, "Molten " + metal.english(), "Molten " + metal.english() + " Bucket");
        }
    }

    public static void spanish(LanguageProvider lang) {
        lang.add(ENTRY + ".title", "Metales de la fundición");
        lang.add(ENTRY + ".stage_0",
                "La Forja de Hefesto funde todos los metales que conoce, pero los tres grandes metales de Avaritia siempre han rechazado su calor. Si la fundición pudiera aprender su naturaleza, deberían fluir como cualquier otro mineral.");
        lang.add(ENTRY + ".stage_1",
                "La Matriz de Cristal, el Neutronio y el Infinito ahora se pueden fundir en la fundición de Hefesto en sus propios fluidos fundidos, desde lingotes, placas y bloques (y pepitas para el Neutronio y el Infinito). Los fluidos se pueden moldear de nuevo con los moldes de lingote y de placa en una mesa de moldeo, con el molde de pepita para el Neutronio y el Infinito, y como bloque en una cubeta de moldeo.<BR>Cuanto más fuerte es el metal, más caliente debe estar la fundición: la Matriz de Cristal necesita un calor de 1100, el Neutronio 1500 y el Infinito 2000.<BR>El metal del vacío y el latón alquímico no necesitan nada nuevo, porque Hephaestus Tools ya los conoce y puede hacer herramientas con ellos.");
        for (MoltenMetal metal : MoltenMetal.values()) {
            // Matriz is feminine
            String molten = metal == MoltenMetal.CRYSTAL_MATRIX ? " fundida" : " fundido";
            String name = Character.toUpperCase(metal.spanish().charAt(0)) + metal.spanish().substring(1);
            add(lang, metal, name + molten, "Cubo de " + metal.spanish() + molten);
        }
    }

    private static void add(LanguageProvider lang, MoltenMetal metal, String fluid, String bucket) {
        lang.add("fluid_type." + MOD + metal.fluidName(), fluid);
        lang.add("block." + MOD + metal.fluidName(), fluid);
        lang.add("item." + MOD + metal.bucketName(), bucket);
    }
}
