# 2.7.0

DISCLAIMER: All spell books and spell scrolls will be reset, due to major API changes. Some (looted) weapons with custom spell containers become non-functional, and need to be re-obtained. Apologies for the inconvenience.

- Update to use Spell Engine 1.9.0
- Archery manual now offers 3 spells only, to match other classes
- Entangling Roots spell is now a spell book choice

# 2.6.8

- Update default armor stats
- Update some translations

# 2.6.7

- Update AzureLib Armor

# 2.6.6

- Add basic armor trim support for all archer armor pieces
- Add Quiver renderers, thanks to @Rulft44

# 2.6.5

- Add vanilla recipe book support (fully datagen recipes)
- Add spell book descriptions
- Add simplified Chinese translation #68 thanks to Star234Des

# 2.6.4

- Entangling roots can now be casted using melee weapons 

# 2.6.3

- Fix rare launch sequence crash

# 2.6.2

- Fix mapping related crashes

# 2.6.1

- Fix dependency declarations

# 2.6.0

- Migrate to Architectury
- Auto Fire Hook can now be removed on Grindstone

# 2.5.2

- Auto Fire Hook now supports all RPG Series ranged weapons

# 2.5.1

- Fix ranged weapon config loading #65

# 2.5.0

- Update to latest Spell Engine

# 2.4.11

- Update to latest Spell Engine

# 2.4.10

- Add armor meta type tags
- Update translations

# 2.4.9

- Fix compat issue with Universal Enchants (by Fuzs) #59
- AzureLibArmor now has version requirement in Fabric mod json

# 2.4.8

- Fix some of the Archery Artisan villager trades

# 2.4.7

- Fix and rebalance some of the Archery Artisan villager trades

# 2.4.6

- Add archer scroll texture

# 2.4.5

- Fix trade advancement
- Add spell casting advancements

# 2.4.4

- Fix some smelting recipes

# 2.4.3

- Add Archery Range trade advancement
- Archery Range chests now may contain Lapis Lazuli
- Change repair material for Ranger Armor pieces
- Add smelting recipes for disassembling archer weapons and armor pieces

# 2.4.2

- Update to Spell Engine 1.6.0

# 2.4.1

- Fix sound related issues, and crashes upon disconnects

# 2.4.0

- Add Quiver (can store 4x64 arrows)
- Add Hunting Quiver (can store 8x64 bolts)
- Add Battle Quiver (can store 12x64 arrows)
- Support Spell Engine 1.5.0
- Add category for all crafting recipes
- Entangling Roots now prevents jumping
- Hunter's Mark stacks can now be applied with Barrage, all at once

# 2.3.0

- Support Spell Engine v1.4.0
- Support AzureLib Armor v3.X
- Fix Hunter's Mark bonus

# 2.2.5

- Support Lithostitched v1.4

# 2.2.4

- Rebalance Quick Charge enchantment for crossbows

# 2.2.3

- Add spell scroll name

# 2.2.2

- Add support for Lithostitched village structure injection
- Update structure files for newer NBT version

# 2.2.1

- Add some Arrow Velocity bonus for Longbows and Heavy Crossbows

# 2.2.0

- Rework Power Shot into an active skill
- Udpdate Russian translation, thanks to @Heimdallr
- Add Brazilian translation, thanks to @demorogabrtz
- Add new weapons, obtainable only as loot from Aether dungeons
  - Holy Spear
  - Silver Bow of the Acropolis
  - Sky Crossbow
  - Valkyrie Ballista

# 2.1.1

- Update Ruby Heavy Crossbow texture

# 2.1.0

- Add new armor set: Netherite Ranger Armor
- Fix Infinity applicable to any enchantable item

# 2.0.5

- Fireproof ranged weapons with netherite or better material

# 2.0.4

- Configurable Power Shot bonus damage

# 2.0.3

- Lowered Fabric API version requirement

# 2.0.2

- Allow running on 1.21

# 2.0.1

- Fix Power enchantment bonus per level

# 2.0.0

- Update to Minecraft 1.21.1

# 1.2.5

- Update translations
- Update some item textures

# 1.2.4

- Remove some enchantment specific API calls

# 1.2.3

- Update Ruby crossbow textures and recipes

# 1.2.2

- Update Spanish translation, thanks to SirColor
- Update Spear textures
- Add Ruby Spear
- Rebalance Spear attributes
- Restructure advancements
- Update Fabric Loader to 15+ for embedded MixinExtras

# 1.2.1

- Using latest Spell Engine, archery skills no longer perform additional random critical strikes
- Migrate to latest Ranged Weapon API
- Archers Armor now provides Draw Speed bonus
- Reduce cooldown of Magic Arrow
- Fix applied Auto-Fire Hook not showing up on item tooltip
- Update Italian translation, thanks to Zano1999

# 1.1.0

- Add new Archer skill: Magic Arrow
- Add BetterNether exclusive weapons
  - Ruby Rapid Crossbow
  - Ruby Heavy Crossbow
- Add BetterEnd exclusive weapons
  - Aeternium Spear
  - Crystal Shortbow
  - Crystal Longbow
- Add equipment tier item tags in `rpg_series` scope
- Add Spanish translation, thanks to SirColor
- Add French translation, thanks to YanisBft
- Fix loot table injections not being configurable (missing `loot.json` file)
- Migrate to RangedWeaponAPI for improved compatibility
- Remove loot config, replaced by `config/rpg_series/loot.json`

# 1.0.7

- Add compatibility for `c:wood_sticks` in recipes
- Update Italian translation, thanks to Zano1999
- Update Chinese translation, thanks to Sillymoon
- Fix Crossbow Infinity can fire tipped arrows without consuming them #16

# 1.0.6

- Add feature: Allow Infinity enchantment for CrossBows (configurable in: `config/archers/tweaks.json`)
- Add item tag: `archers:spears`
- Add effect descriptions
- Update Italian translation, thanks to Zano1999 #10
- Update Russian translation, thanks to skel39eek66 #9

# 1.0.5

- Add basic, craftable, two-handed spears, so Archers can defend themselves in melee
- Slightly increase damage done by Barrage skill
- Disable Spell Power related enchantments for archer equipment
- Remove false range tooltips from skills

# 1.0.4

- Reduce damage done by Barrage skill
- Fix Forge compatibility (via Sinytra Connector), by lowering some mixin requirements
- Fix Z fighting on Archery Artisan Table block model
- Fix Ranger Armor model clipping into some player skins
- Improve Russian translation, thanks to kel39eek66 #7

# 1.0.3

- Texture bug Archery Artisan Table #3

# 1.0.2

- Add missing textures

# 1.0.1

- Add missing assets and mod icon
- Update creative tab icon
- Add Russian translation #1, thanks to skel39eek66 #1
- Add Italian translation #2, thanks to Zano1999 #2

# 1.0.0

- Initial release

#