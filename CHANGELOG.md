# ⚡ Thaumaturge Tech Compats — Changelog

## 🚀 [1.0.6-26.1.2] 27/11/2026 17:32

### Additions
- Support for the Farmer's Delight Neoforged port (jexxme), next to the Farmer's Delight 26 Neo.ver port.

### Fixes
- Fixed a startup crash with Farmer's Delight Neoforged 1.3.4 while registering the Thaumium, Alchemical Brass, Void Metal and Flux knives.

### Changes
- The config options tainted_knives, tainted_flux_knife and tainted_food_farmersdelight now explain what each one turns off. Setting tainted_knives or tainted_flux_knife to false removes those knives completely.

### Lineas de codigo cambiadas
+155 −12

## 🚀 [1.0.5-26.1.2] 26/11/2026 23:41

### Additions
- New metals: Salis, Fibric, Crimson and Rifting, each with ingot, nugget, block and native cluster. Rifting ingots are made by dropping a Void Metal ingot in a crucible next to a jar with a captured rift.
- Wand caps and rods (normal and inert) for the four new metals.
- Hephaestus Tools support: tool parts and molten fluids for the new metals and for Thaumium, Brass and Void Metal, each part shows its material, with new modifiers (repair, vision, teleport up to level 12, unbreakable and more).
- In-game guide with Oracle Index: a chapter for every block, machine and item of the mod, with their recipes, plus a chapter about Thaumaturge itself.
- The Thaumonomicon now splits the research into the same categories as the creative tabs, with tabs in the left column showing the real items.
- The crucible holds 1024 essentia per aspect by default, configurable up to 62500.
- A Flux Jar next to a crucible catches the flux it releases; with no jar around, the flux vanishes and gives nearby players 4 seconds of temporary warp. Flux can be taken out of the Flux Jar with a bucket and put back in.
- New configs, on by default: rifts don't break blocks when they appear, and don't break blocks when they die from the Causality Collapser.
- Mekanism support through Mekanism Version Locked.
- Ender IO support: conduits and capacitor banks, like in 1.21.1.
- Jade support.

### Fixes
-

### Changes
- Everything from Avaritia is now in a single creative tab and Thaumonomicon category.

### Lineas de codigo cambiadas
+398313 −1964

## 🚀 [1.0.4-26.1.2] 26/11/2026 23:41

### Additions
-

### Fixes
- The Essentia Smelter can now dissolve modded items with very high aspect values (this mod, Avaritia, Void Miners and other compat mods).
- Essentia tubes connect to the jars of this mod, and the jars no longer crash the game.
- Fixed missing translations that showed raw keys.
- The capacitor banks show their textures again instead of the magenta and black placeholder.
- The Thaummotor shows its rotation speed (RPM) again.
- Void Uranium irradiates whoever carries it, unless they wear a hazmat suit.

### Changes
-

### Lineas de codigo cambiadas
-

## 🚀 [1.0.3-26.1.2] 24/11/2026 21:10

### Additions
- Hephaestus Forge smeltery support: Crystal Matrix, Neutronium and Infinity can be melted and cast (ingots, nuggets, plates and blocks), with their own molten fluids and buckets. A new research entry explains it.
- Edible Goggles of Revealing: a version of the Goggles of Revealing you can eat, made with sugar.
- Tooltip with an enlarged picture of the item on every item, vanilla and modded. It can be turned off or resized in the client config.
- A Flux Jar next to a crucible now absorbs the flux the crucible leaks or overflows, instead of polluting the aura.

### Fixes
- Fixed the data pack loading error when creating or opening a world without Void Miners (or other optional compat mods). The pickaxe mining tag no longer breaks, which also fixes the Aether II pickaxe reinforcements crash.
- Fixed the Oritech resource node world generation failing to load (amber, cinnabar and quartz nodes).
- Fixed the missing Sol aspect that broke the Essences research entry.
- Fixed a startup crash ("Block id not set") when Re-Avaritia is installed. The Neutron Collectors and Compressors of higher tiers no longer break the registry.

### Changes
- The Star Crucifix can now bind any creature, not only hostile ones.

### Lineas de codigo cambiadas
+5379 −190

## 🚀 [1.0.2-26.1.2] 22/11/2026 16:50

### Additions
- Create integration is back, now through Create: Adapted. The original Create 1.21.1 jar is still needed as its asset source (see the Create: Adapted page).

### Fixes
- Fixed the "requires Ponder" error when Ponder is not installed.

### Changes
- Ponder is now optional. Create: Adapted already includes it, so it no longer has to be installed separately.
- The Ponder scenes only load when both Create and Ponder are installed.
- The creative tab icon is now the Creative Planetary Endest Pearl.

### Lineas de codigo cambiadas
+248 −103

## 🚀 [1.0.1-26.1.2] 15/11/2026 14:46

### Additions
-

### Fixes
-

### Changes
- Fractal is now bundled inside the mod, so it no longer has to be installed separately.
- Requires Oritech 2.0.0-exp12 or newer and ForceCraft 7.0.6 or newer.

### Lineas de codigo cambiadas
-

## 🚀 [1.0.0-26.1.2] 13/11/2026 14:44

### Additions
- Initial release.

### Fixes
-

### Changes
-

### Lineas de codigo cambiadas
-
