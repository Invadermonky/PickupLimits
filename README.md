# Pickup Limits #

## Description ##

Pickup Limits is a utility mod that allows modpack makers to define custom "limit groups" that specify how much of any item, or items, players can hold or equip at a given time. These groups can be mixed and matched to create highly customized and interweaving interactions. Currently, there two types of limit groups, Pickup Limits and Equipment Limits.

[Pickup Limits](https://github.com/Invadermonky/PickupLimits/wiki/Pickup-Limits) are limit groups focused on how many items are stored in a player's inventory. These limits can be used to control the maximum number of items players can hold at a time and prevent players from pickup items that exceed this value.

[Equipment Limits](https://github.com/Invadermonky/PickupLimits/wiki/Equipment-Limits) are limit groups focused on currently equipped items. These limits can be used to control the relative power of players by restricting enchants or armor types.

This mod also includes a configuration-based [Item Lifetime Tweaks](https://github.com/Invadermonky/PickupLimits/wiki/Item-Lifetime-Tweaks) module, allowing modpack makers to specify the lifetime, or duration, before items dropped into the world despawn.

## What can I do with this? ##

Pickup limits features a modular limit group system, allowing the creation of complex interactions between interwoven groups. This can be as simple as restricting the number of items a player can carry, to as complex as only allowing the player to wear certain enchants when specific items are worn. Limit groups also include Game Stage support, creating a system that can grow, adapt, or even disable itself over the course of a modpack.

## Integrations ##

### [Baubles](https://www.curseforge.com/minecraft/mc-mods/baubles) ###

Pickup Limits includes native Baubles support, allowing equipped baubles to modify or add to pickup group limits.

### [Craftweaker](https://www.curseforge.com/minecraft/mc-mods/crafttweaker) ###

Pickup Limits features a robust Crafttweaker integration, allowing the creation of highly customized limit groups to fit your modpack. You can read more about the different creation methods on the wiki.

### [Game Stages](https://www.curseforge.com/minecraft/mc-mods/game-stages) ###

Pickup Limits includes Game Stages support, allowing you to customize limit group behavior depending on player stage.

### [GroovyScript](https://www.curseforge.com/minecraft/mc-mods/groovyscript) ###

Pickup Limits includes native GroovyScript integration. You can read more about the different limit creation methods on the wiki.

### [Zen Utils](https://www.curseforge.com/minecraft/mc-mods/zenutil) ###

Pickup Limits supports Crafttweaker script reloading. Be sure to add the ```#reloadable``` preprosessor to your script files!