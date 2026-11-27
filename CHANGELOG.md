# ⚡ Thaumaturge Tech Compats — Changelog

## 🚀 [1.0.5-1.21.1] 26/11/2026 23:40

### Additions
- New metals: Salis, Fibric, Crimson and Rifting, each with ingot, nugget, block and native cluster. Rifting ingots are made by dropping a Void Metal ingot in a crucible next to a jar with a captured rift.
- Wand caps and rods (normal and inert) for the four new metals.
- Hephaestus Tools support: tool parts and molten fluids for the new metals and for Thaumium, Brass and Void Metal, each part shows its material, with new modifiers (repair, vision, teleport up to level 12, unbreakable and more).
- In-game guide with Oracle Index: a chapter for every block, machine and item of the mod, with their recipes, plus a chapter about Thaumaturge itself.
- The Thaumonomicon now splits the research into the same categories as the creative tabs, with tabs in the left column showing the real items.
- The crucible holds 1024 essentia per aspect by default, configurable up to 62500.
- A Flux Jar next to a crucible catches the flux it releases; with no jar around, the flux vanishes and gives nearby players 4 seconds of temporary warp. Flux can be taken out of the Flux Jar with a bucket and put back in.
- New configs, on by default: rifts don't break blocks when they appear, and don't break blocks when they die from the Causality Collapser.

### Fixes
- The Essentia Smelter can now dissolve modded items with very high aspect values (this mod, Avaritia, Void Miners and other compat mods).
- Essentia tubes connect to the jars of this mod, and the jars no longer crash the game.
- Fixed missing translations that showed raw keys.
- Fixed broken « » characters in some texts.

### Changes
- Thaumaturge updated to 1.1.1.
- Everything from Avaritia is now in a single creative tab and Thaumonomicon category.

### Lineas de codigo cambiadas
+239599 −768

## 🚀 [1.0.4-1.21.1] 25/11/2026 14:50

### Additions
- The mod can now be used as a dependency by other mods through JitPack.

### Fixes
-

### Changes
- The README has a new section for developers that explains how to depend on the mod.

### Lineas de codigo cambiadas
+1 −1

## 🚀 [1.0.3-1.21.1] 24/11/2026 20:15

### Additions
- Hephaestus Forge smeltery support: Crystal Matrix, Neutronium and Infinity can be melted and cast (ingots, nuggets, plates and blocks), with their own molten fluids and buckets.
- Edible Goggles of Revealing: a version of the Goggles of Revealing you can eat, made with sugar.
- Tooltip with an enlarged picture of the item on every item, vanilla and modded. It can be turned off or resized in the client config.
- New Solar Flux Reborn panels: Alchemical Cobalt (needs Hephaestus Forge), Brass Andesite (needs Create), Thaumium Zinc (needs Create or AllTheOres) and Void Uranium (needs AllTheOres or Mekanism, and irradiates whoever carries it when Mekanism is installed).
- A Flux Jar next to a crucible now absorbs the flux the crucible leaks or overflows, instead of polluting the aura.

### Fixes
-

### Changes
- The Alchemical, Thaumium and Void Metal solar panels have new stats. If you already played with the mod, delete their entries in config/solarflux/panels.cfg to get the new values.
- The Alchemical Brass Solar Panel is now the Brass Solar Panel; panels already in a world are converted automatically.
- The Star Crucifix can now bind any creature, not only hostile ones.

### Lineas de codigo cambiadas
+5239 −58

## 🚀 [1.0.2-1.21.1] 22/11/2026 16:50

### Additions
-

### Fixes
- The game no longer crashes on startup when Create is not installed.
- Fixed the "requires Ponder" error when Ponder is not installed.
- Fixed the "requires Fractal 1.8.2" error when another mod (for example Botania Revived) already ships Fractal 1.7.0.

### Changes
- Ponder is now optional: the Ponder scenes only load when Ponder is installed.
- The Fractal requirement is lowered to 1.7.0 or newer.
- The creative tab icon is now the Creative Planetary Endest Pearl.

### Lineas de codigo cambiadas
+47 −34

## 🚀 [1.0.1-1.21.1] 15/11/2026 14:47

### Additions
-

### Fixes
-

### Changes
- Fractal is now bundled inside the mod, so it no longer has to be installed separately.

### Lineas de codigo cambiadas
-

## 🚀 [1.0.0-1.21.1] 13/11/2026 00:36

### Additions
- Initial release.

### Fixes
-

### Changes
-

### Lineas de codigo cambiadas
-
