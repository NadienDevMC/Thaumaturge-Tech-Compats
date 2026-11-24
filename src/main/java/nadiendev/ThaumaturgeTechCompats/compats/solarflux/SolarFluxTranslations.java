package nadiendev.ThaumaturgeTechCompats.compats.solarflux;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class SolarFluxTranslations {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;

    private SolarFluxTranslations() {
    }

    public static void english(LanguageProvider lang) {
        lang.add("aspect." + MOD + ".sol", "Sol");
        lang.add("aspect." + MOD + ".sol.desc", "Sun, Solar Energy");
        lang.add("aspect." + MOD + ".sol.help", "the power of the sun");
        panel(lang, "alchemical", "Alchemical Solar Panel");
        panel(lang, "brass_solar_panel", "Brass Solar Panel");
        panel(lang, "brass_andesite_solar_panel", "Brass Andesite Solar Panel");
        panel(lang, "thaumium_zinc_solar_panel", "Thaumium Zinc Solar Panel");
        panel(lang, "alchemical_cobalt_solar_panel", "Alchemical Cobalt Solar Panel");
        panel(lang, "void_uranium_solar_panel", "Void Uranium Solar Panel");
        panel(lang, "thaumium", "Thaumium Solar Panel");
        panel(lang, "void_metal", "Void Metal Solar Panel");
        entry(lang, SolarFluxContent.ROOT, "Alchemical Solar Panel",
                "Sunlight is only light, and light is only a thing to be shaped. If I bind the aspect of the sun into a plain solar panel inside the crucible, it should keep drinking the light long after the sun has set.",
                "The crucible has dissolved the sun into the panel. The Sol it holds is tame, and the panel now generates far more energy than it ever did.<PAGE>Alchemical Solar Panel<DIV>Dropping a tier 8 solar panel into a crucible filled with Sol and Machina turns it into an alchemical solar panel. If Twilight Forest is installed the catalyst is its tier 2 panel (fiery), and if Draconic Evolution is installed it is the chaotic panel.");
        entry(lang, SolarFluxContent.BRASS, "Brass Solar Panel",
                "Brass conducts the sun's light better than anything I have set beside it. Wrapping an alchemical panel in brass plating and photovoltaic cells should wring far more energy out of every ray.",
                "The brass plating channels the light into the panel without losing it.<PAGE>Brass Solar Panel<DIV>It is crafted at the arcane workbench from photovoltaic cells, brass plates and an alchemical solar panel.");
        entry(lang, SolarFluxContent.THAUMIUM, "Thaumium Solar Panel",
                "Thaumium takes magic as easily as brass takes light. A matrix of thaumium plates and refined photovoltaic cells should let a panel shine with an energy that no craftsman could ever give it.",
                "The infusion altar has fused the plates and cells into a single, steady source of power.<PAGE>Thaumium Solar Panel<DIV>It is infused over a brass solar panel using thaumium plates, advanced photovoltaic cells and a simple mechanism.");
        entry(lang, SolarFluxContent.VOID, "Void Metal Solar Panel",
                "Void metal swallows light the way the void swallows everything else. If I give it the purest photovoltaic cells and a complex mechanism, it should turn the faintest ray into a torrent of energy.",
                "The panel drinks light that would otherwise vanish into nothing.<PAGE>Void Metal Solar Panel<DIV>It is infused over a thaumium solar panel using void metal plates, the finest photovoltaic cells and a complex mechanism.");
        entry(lang, SolarFluxContent.BRASS_ANDESITE, "Brass Andesite Solar Panel",
                "The craftsmen of Create pair andesite alloy with brass for anything that has to keep turning. Set around a plain panel, the pair should give a humble cell that asks for little and gives steadily.",
                "The alloy and the brass have settled into a plain, hardy panel.<PAGE>Brass Andesite Solar Panel<DIV>It is crafted at the arcane workbench from photovoltaic cells, andesite alloy, brass ingots and an alchemical solar panel. It needs Create.");
        entry(lang, SolarFluxContent.THAUMIUM_ZINC, "Thaumium Zinc Solar Panel",
                "Zinc is a quiet metal, but thaumium wakes it. Plated together over refined photovoltaic cells, they should make a panel with a steady and cheap hum.",
                "Zinc and thaumium have been drawn into a single, even plating.<PAGE>Thaumium Zinc Solar Panel<DIV>It is crafted at the arcane workbench from advanced photovoltaic cells, thaumium plates, zinc ingots and an alchemical solar panel. It needs Create or All The Ores.");
        entry(lang, SolarFluxContent.ALCHEMICAL_COBALT, "Alchemical Cobalt Solar Panel",
                "Cobalt keeps its blue fire through any heat the smeltery can offer. Wrapped around an alchemical panel, it should hold the power of the sun a little longer.",
                "The cobalt casing has sealed the alchemy inside.<PAGE>Alchemical Cobalt Solar Panel<DIV>It is crafted at the arcane workbench from photovoltaic cells, cobalt ingots and an alchemical solar panel. It needs Hephaestus Forge.");
        entry(lang, SolarFluxContent.VOID_URANIUM, "Void Uranium Solar Panel",
                "Uranium burns forever in the void, where nothing can put it out. Fed to a void metal panel with the purest cells, it should shine with a power no mortal sun can match, and I doubt it will be kind to whoever holds it.",
                "The panel glows with a light that is not the sun's.<PAGE>Void Uranium Solar Panel<DIV>It is infused over a void metal solar panel using void metal plates, uranium ingots, the finest photovoltaic cells and a complex mechanism. It needs All The Ores, and with Mekanism installed it irradiates whoever carries it.");
    }

    public static void spanish(LanguageProvider lang) {
        lang.add("aspect." + MOD + ".sol", "Sol");
        lang.add("aspect." + MOD + ".sol.desc", "Sol, energía solar");
        lang.add("aspect." + MOD + ".sol.help", "el poder del sol");
        panel(lang, "alchemical", "Panel solar alquímico");
        panel(lang, "brass_solar_panel", "Panel solar de latón");
        panel(lang, "brass_andesite_solar_panel", "Panel solar de latón y andesita");
        panel(lang, "thaumium_zinc_solar_panel", "Panel solar de taumio y zinc");
        panel(lang, "alchemical_cobalt_solar_panel", "Panel solar alquímico de cobalto");
        panel(lang, "void_uranium_solar_panel", "Panel solar de uranio del vacío");
        panel(lang, "thaumium", "Panel solar de taumio");
        panel(lang, "void_metal", "Panel solar de metal del vacío");
        entry(lang, SolarFluxContent.ROOT, "Panel solar alquímico",
                "La luz del sol es solo luz, y la luz es algo que se puede moldear. Si ato el aspecto del sol a un panel solar sencillo dentro del crisol, seguirá bebiendo luz mucho después de que el sol se ponga.",
                "El crisol ha disuelto el sol dentro del panel. El Sol que contiene está domado, y el panel genera ahora mucha más energía que antes.<PAGE>Panel solar alquímico<DIV>Echar un panel solar de nivel 8 en un crisol lleno de Sol y Machina lo convierte en un panel solar alquímico. Si Twilight Forest está instalado, el catalizador es su panel de nivel 2 (ígneo), y si lo está Draconic Evolution, es el panel caótico.");
        entry(lang, SolarFluxContent.BRASS, "Panel solar de latón",
                "El latón conduce la luz del sol mejor que cualquier otra cosa que haya probado. Envolver un panel alquímico en placas de latón y células fotovoltaicas debería sacar mucha más energía de cada rayo.",
                "El chapado de latón guía la luz hasta el panel sin perderla.<PAGE>Panel solar de latón<DIV>Se fabrica en la mesa de trabajo arcana con células fotovoltaicas, placas de latón y un panel solar alquímico.");
        entry(lang, SolarFluxContent.THAUMIUM, "Panel solar de taumio",
                "El taumio acepta la magia igual que el latón acepta la luz. Una matriz de placas de taumio y células fotovoltaicas refinadas debería hacer brillar un panel con una energía que ningún artesano podría darle.",
                "El altar de infusión ha fundido placas y células en una fuente de energía única y estable.<PAGE>Panel solar de taumio<DIV>Se infunde sobre un panel solar de latón con placas de taumio, células fotovoltaicas avanzadas y un mecanismo simple.");
        entry(lang, SolarFluxContent.VOID, "Panel solar de metal del vacío",
                "El metal del vacío se traga la luz como el vacío se traga todo lo demás. Si le doy las células fotovoltaicas más puras y un mecanismo complejo, convertirá el rayo más débil en un torrente de energía.",
                "El panel bebe luz que de otro modo se desvanecería en la nada.<PAGE>Panel solar de metal del vacío<DIV>Se infunde sobre un panel solar de taumio con placas de metal del vacío, las mejores células fotovoltaicas y un mecanismo complejo.");
        entry(lang, SolarFluxContent.BRASS_ANDESITE, "Panel solar de latón y andesita",
                "Los artesanos de Create unen aleación de andesita y latón para todo lo que tiene que girar sin parar. Alrededor de un panel sencillo, el par debería dar una célula humilde que pide poco y rinde con constancia.",
                "La aleación y el latón se han asentado en un panel sencillo y resistente.<PAGE>Panel solar de latón y andesita<DIV>Se fabrica en la mesa de trabajo arcana con células fotovoltaicas, aleación de andesita, lingotes de latón y un panel solar alquímico. Necesita Create.");
        entry(lang, SolarFluxContent.THAUMIUM_ZINC, "Panel solar de taumio y zinc",
                "El zinc es un metal callado, pero el taumio lo despierta. Chapados juntos sobre células fotovoltaicas refinadas, deberían dar un panel con un zumbido constante y barato.",
                "El zinc y el taumio se han fundido en un único chapado uniforme.<PAGE>Panel solar de taumio y zinc<DIV>Se fabrica en la mesa de trabajo arcana con células fotovoltaicas avanzadas, placas de taumio, lingotes de zinc y un panel solar alquímico. Necesita Create o All The Ores.");
        entry(lang, SolarFluxContent.ALCHEMICAL_COBALT, "Panel solar alquímico de cobalto",
                "El cobalto conserva su fuego azul con cualquier calor que ofrezca la fundición. Envolviendo un panel alquímico, debería retener el poder del sol un poco más.",
                "La carcasa de cobalto ha sellado la alquimia en su interior.<PAGE>Panel solar alquímico de cobalto<DIV>Se fabrica en la mesa de trabajo arcana con células fotovoltaicas, lingotes de cobalto y un panel solar alquímico. Necesita Hephaestus Forge.");
        entry(lang, SolarFluxContent.VOID_URANIUM, "Panel solar de uranio del vacío",
                "El uranio arde para siempre en el vacío, donde nada puede apagarlo. Dado a un panel de metal del vacío con las células más puras, debería brillar con un poder que ningún sol mortal iguala, y dudo que sea amable con quien lo lleve.",
                "El panel brilla con una luz que no es la del sol.<PAGE>Panel solar de uranio del vacío<DIV>Se infunde sobre un panel solar de metal del vacío con placas de metal del vacío, lingotes de uranio, las mejores células fotovoltaicas y un mecanismo complejo. Necesita All The Ores y, con Mekanism instalado, irradia a quien lo lleve.");
    }

    private static void panel(LanguageProvider lang, String name, String text) {
        lang.add("block." + SolarFluxIntegration.MOD_ID + ".sp_" + name, text);
    }

    private static void entry(LanguageProvider lang, String name, String title, String stage0, String stage1) {
        String base = "research." + MOD + "." + name;
        lang.add(base + ".title", title);
        lang.add(base + ".stage_0", stage0);
        lang.add(base + ".stage_1", stage1);
    }
}
