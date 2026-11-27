package nadiendev.ThaumaturgeTechCompats.materials;

import nadiendev.ThaumaturgeTechCompats.materials.modifier.ToolModifier;

import java.util.function.BiConsumer;

/** Names of the extra metals. Plain BiConsumer so a language provider (lang::add) or a standalone run can use it. */
public final class ExtraMaterialTranslations {
    private static final String MOD = ExtraMaterial.MOD;
    private static final String ITEM = "item." + MOD + ".";
    private static final String BLOCK = "block." + MOD + ".";
    private static final String ENTRY = "research." + MOD + "." + ExtraMaterial.RESEARCH;

    private ExtraMaterialTranslations() {
    }

    public static void english(BiConsumer<String, String> lang) {
        for (ExtraMaterial m : ExtraMaterial.values()) {
            String n = m.english();
            lang.accept(ITEM + m.ingotName(), n + " Ingot");
            lang.accept(ITEM + m.nuggetName(), n + " Nugget");
            lang.accept(BLOCK + m.blockName(), n + " Block");
            lang.accept(ITEM + m.clusterName(), "Native " + n + " Cluster");
            lang.accept(ITEM + m.rodName(), n + " Rod");
            lang.accept(ITEM + m.capName(), n + " Cap");
            lang.accept(ITEM + m.inertRodName(), "Inert " + n + " Rod");
            lang.accept(ITEM + m.inertCapName(), "Inert " + n + " Cap");
            lang.accept("wand.thaumaturge.rod." + m.id(), n);
            lang.accept("wand.thaumaturge.cap." + m.id(), n + " Capped");
            lang.accept("material.hephaestus_tools." + m.id(), n);
        }
        for (ToolModifier t : ToolModifier.values()) {
            lang.accept(t.nameKey(), t.english());
            lang.accept(t.descKey(), t.englishDesc());
            lang.accept("gui.hephaestus_tools.upgrade." + t.path() + ".desc", t.englishDesc());
        }
        lang.accept(ITEM + "hephaestus_tool_name", "%1$s %2$s");
        lang.accept("message." + MOD + ".rifting_needs_rift_jar",
                "The void metal needs a Rift Jar with a trapped rift next to the crucible");
        lang.accept(ENTRY + ".title", "Rifting, Fibric, Salis and Crimson");
        lang.accept(ENTRY + ".stage_0",
                "There are metals the old alchemists never named. One is born when void metal is made to drink from a tear in the world, and others wait in cloth, in salt and in blood. Each should carry vis better than the metals I know, and make hard tools besides.");
        lang.accept(ENTRY + ".stage_1",
                "Rifting Ingot: drop a Void Metal Ingot into a crucible with water and 20 praecantatio and 20 sol, with a Rift Jar next to the crucible that holds a trapped rift. Without the trapped rift nothing happens.<BR>Rifting, Fibric, Salis and Crimson each have an ingot, a nugget, a block and a native cluster, and they can be melted in the Hephaestus smeltery and made into Hephaestus tools, from best to worst: Salis, Fibric, Crimson and Rifting.<BR>Each metal also makes a wand rod and a wand cap. First shape the inert part at an arcane workbench: the rod from three ingots and the cap from five nuggets. Then wake it with an infusion, with the inert part as catalyst, four salis mundus and the aspects of the metal. The stronger the metal, the more vis the rod holds and the cheaper every spell is with the cap.");
    }

    public static void spanish(BiConsumer<String, String> lang) {
        for (ExtraMaterial m : ExtraMaterial.values()) {
            String n = m.spanish();
            lang.accept(ITEM + m.ingotName(), "Lingote de " + n);
            lang.accept(ITEM + m.nuggetName(), "Pepita de " + n);
            lang.accept(BLOCK + m.blockName(), "Bloque de " + n);
            lang.accept(ITEM + m.clusterName(), "Racimo nativo de " + n);
            lang.accept(ITEM + m.rodName(), "Vara de " + n);
            lang.accept(ITEM + m.capName(), "Pomo de " + n);
            lang.accept(ITEM + m.inertRodName(), "Vara inerte de " + n);
            lang.accept(ITEM + m.inertCapName(), "Pomo inerte de " + n);
            lang.accept("wand.thaumaturge.rod." + m.id(), "de " + n);
            lang.accept("wand.thaumaturge.cap." + m.id(), n);
            lang.accept("material.hephaestus_tools." + m.id(), n);
        }
        for (ToolModifier t : ToolModifier.values()) {
            lang.accept(t.nameKey(), t.spanish());
            lang.accept(t.descKey(), t.spanishDesc());
            lang.accept("gui.hephaestus_tools.upgrade." + t.path() + ".desc", t.spanishDesc());
        }
        lang.accept(ITEM + "hephaestus_tool_name", "%2$s de %1$s");
        lang.accept("message." + MOD + ".rifting_needs_rift_jar",
                "El metal del vacío necesita un Tarro de Grietas con una grieta atrapada junto al caldero");
        lang.accept(ENTRY + ".title", "Rifting, Fibric, Salis y Crimson");
        lang.accept(ENTRY + ".stage_0",
                "Hay metales que los viejos alquimistas nunca nombraron. Uno nace cuando se obliga al metal del vacío a beber de un desgarro del mundo, y otros esperan en la tela, en la sal y en la sangre. Cada uno debería llevar el vis mejor que los metales que conozco, y además dar herramientas duras.");
        lang.accept(ENTRY + ".stage_1",
                "Lingote de Rifting: echa un Lingote de metal del vacío en un caldero con agua y 20 de praecantatio y 20 de sol, con un Tarro de Grietas junto al caldero que tenga una grieta atrapada. Sin la grieta atrapada no pasa nada.<BR>Rifting, Fibric, Salis y Crimson tienen cada uno lingote, pepita, bloque y racimo nativo, y se pueden fundir en la fundición de Hefesto y hacer herramientas de Hephaestus, de mejor a peor: Salis, Fibric, Crimson y Rifting.<BR>Cada metal también da una vara y un pomo de varita. Primero se forma la pieza inerte en una mesa de trabajo arcana: la vara con tres lingotes y el pomo con cinco pepitas. Luego se despierta con una infusión, con la pieza inerte como catalizador, cuatro salis mundus y los aspectos del metal. Cuanto más fuerte es el metal, más vis guarda la vara y más barato es cada hechizo con el pomo.");
    }
}
