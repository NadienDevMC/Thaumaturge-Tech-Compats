package nadiendev.ThaumaturgeTechCompats.metals;

import net.neoforged.neoforge.common.data.LanguageProvider;

public final class ExtraMetalTranslations {
    private static final String ITEM = "item.thaumaturgetechcompats.";
    private static final String ENTRY = "research.thaumaturgetechcompats." + ExtraMetal.RESEARCH;
    public static final String RIFTING_NEEDS_RIFT_JAR = "message.thaumaturgetechcompats.rifting_needs_rift_jar";

    private ExtraMetalTranslations() {
    }

    public static void english(LanguageProvider lang) {
        for (ExtraMetal metal : ExtraMetal.values()) {
            String name = metal.english();
            lang.add(ITEM + metal.ingotName(), name + " Ingot");
            lang.add(ITEM + metal.nuggetName(), name + " Nugget");
            lang.add("block.thaumaturgetechcompats." + metal.blockName(), name + " Block");
            lang.add(ITEM + metal.clusterName(), name + " Native Cluster");
            lang.add(ITEM + metal.capName(), name + " Cap");
            lang.add(ITEM + metal.inertCapName(), "Inert " + name + " Cap");
            lang.add(ITEM + metal.rodName(), name + " Rod");
            lang.add(ITEM + metal.inertRodName(), "Inert " + name + " Rod");
            lang.add("wand.thaumaturge.cap." + metal.id(), name + " Capped");
            lang.add("wand.thaumaturge.rod." + metal.id(), name);
            lang.add("material.thaumaturgetechcompats." + metal.id(), name);
        }
        lang.add("tool.thaumaturgetechcompats.material_name", "%1$s %2$s");
        lang.add(RIFTING_NEEDS_RIFT_JAR, "The void metal needs a Rift Jar with a trapped rift next to the crucible");
        lang.add(ENTRY + ".title", "Rifting, Fibric, Salis and Crimson");
        lang.add(ENTRY + ".stage_0",
                "There are metals the old alchemists never named. One is born when void metal is made to drink from a tear in the world, and others wait in cloth, in salt and in blood. Each should carry vis better than the metals I know, and make hard tools besides.");
        lang.add(ENTRY + ".stage_1",
                "Rifting Ingot: drop a Void Metal Ingot into a crucible with water and 20 praecantatio and 20 sol, with a Rift Jar next to the crucible that holds a trapped rift. Without the trapped rift nothing happens.<BR>Rifting, Fibric, Salis and Crimson each have an ingot, a nugget, a block and a native cluster, and they can be melted in the Hephaestus smeltery and made into Hephaestus tools, from best to worst: Salis, Fibric, Crimson and Rifting.<BR>Each metal also makes a wand rod and a wand cap. First shape the inert part at an arcane workbench: the rod from three ingots and the cap from five nuggets. Then wake it with an infusion, with the inert part as catalyst, four salis mundus and the aspects of the metal. The stronger the metal, the more vis the rod holds and the cheaper every spell is with the cap.");
        nadiendev.ThaumaturgeTechCompats.metals.upgrades.ToolUpgradeIntegration.lang(lang::add, false);
    }

    public static void spanish(LanguageProvider lang) {
        for (ExtraMetal metal : ExtraMetal.values()) {
            String name = metal.spanish();
            lang.add(ITEM + metal.ingotName(), "Lingote de " + name);
            lang.add(ITEM + metal.nuggetName(), "Pepita de " + name);
            lang.add("block.thaumaturgetechcompats." + metal.blockName(), "Bloque de " + name);
            lang.add(ITEM + metal.clusterName(), "Racimo nativo de " + name);
            lang.add(ITEM + metal.capName(), "Pomo de " + name);
            lang.add(ITEM + metal.inertCapName(), "Pomo inerte de " + name);
            lang.add(ITEM + metal.rodName(), "Vara de " + name);
            lang.add(ITEM + metal.inertRodName(), "Vara inerte de " + name);
            lang.add("wand.thaumaturge.cap." + metal.id(), capitalize(name));
            lang.add("wand.thaumaturge.rod." + metal.id(), "de " + name);
            lang.add("material.thaumaturgetechcompats." + metal.id(), capitalize(name));
        }
        lang.add("tool.thaumaturgetechcompats.material_name", "%2$s de %1$s");
        lang.add(RIFTING_NEEDS_RIFT_JAR, "El metal del vacío necesita una Jarra de grieta con una grieta atrapada junto al crisol");
        lang.add(ENTRY + ".title", "Rifting, Fibric, Salis y carmesí");
        lang.add(ENTRY + ".stage_0",
                "Hay metales que los viejos alquimistas nunca nombraron. Uno nace cuando se obliga al metal del vacío a beber de un desgarro del mundo, y otros esperan en la tela, en la sal y en la sangre. Cada uno debería llevar el vis mejor que los metales que conozco, y además dar herramientas duras.");
        lang.add(ENTRY + ".stage_1",
                "Lingote de Rifting: echa un Lingote de metal del vacío en un crisol con agua y 20 de praecantatio y 20 de sol, con una Jarra de grieta junto al crisol que tenga una grieta atrapada. Sin la grieta atrapada no pasa nada.<BR>Rifting, Fibric, Salis y carmesí tienen cada uno lingote, pepita, bloque y racimo nativo, y se pueden fundir en la fundición de Hefesto y hacer herramientas de Hephaestus, de mejor a peor: Salis, Fibric, carmesí y Rifting.<BR>Cada metal también da una vara y un pomo de varita. Primero se forma la pieza inerte en una mesa de trabajo arcana: la vara con tres lingotes y el pomo con cinco pepitas. Luego se despierta con una infusión, con la pieza inerte como catalizador, cuatro salis mundus y los aspectos del metal. Cuanto más fuerte es el metal, más vis guarda la vara y más barato es cada hechizo con el pomo.");
        nadiendev.ThaumaturgeTechCompats.metals.upgrades.ToolUpgradeIntegration.lang(lang::add, true);
    }

    private static String capitalize(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
