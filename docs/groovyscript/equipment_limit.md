---
title: "Equipment Limits"
titleTemplate: "Pickup Limits | CleanroomMC"
description: "<p>Equipment Limits are a specialized type of Pickup Limit. Where Pickup Limits scan a player's inventory for items and calculate a total based on count, Equipment Limits focus on currently equipped items. The focus on equipped items comes with two major differences from Pickup Limits.</p><p>First, any item found that exceeds the equipment group limit will be unequipped and placed in the player's inventory instead of being dropped on the ground. The only exception to this are items held in the player's mainhand.</p><p>Second, in the place of ore dictionary entries, equipment limit groups allow you to specify limits based on enchantments. This means that limit values per item are calculated slightly differently than normal pickup limits.</p>For more information and examples, you can check out the Pickup Limits GitHub Wiki https://github.com/Invadermonky/PickupLimits/wiki."
source_code_link: "https://github.com/CleanroomMC/GroovyScript/blob/master/src/main/java/com/invadermonky/pickuplimit/compat/groovy/limits/EquipmentLimit.java"
---

# Equipment Limits (Pickup Limits)

## Description

<p>Equipment Limits are a specialized type of Pickup Limit. Where Pickup Limits scan a player's inventory for items and calculate a total based on count, Equipment Limits focus on currently equipped items. The focus on equipped items comes with two major differences from Pickup Limits.</p><p>First, any item found that exceeds the equipment group limit will be unequipped and placed in the player's inventory instead of being dropped on the ground. The only exception to this are items held in the player's mainhand.</p><p>Second, in the place of ore dictionary entries, equipment limit groups allow you to specify limits based on enchantments. This means that limit values per item are calculated slightly differently than normal pickup limits.</p>For more information and examples, you can check out the Pickup Limits GitHub Wiki https://github.com/Invadermonky/PickupLimits/wiki.

## Identifier

Refer to this via any of the following:

```groovy:no-line-numbers {1}
mods.pickuplimits.equipment_limit/* Used as page default */ // [!code focus]
mods.pickuplimits.equipmentlimit
mods.pickuplimits.equipmentLimit
mods.pickuplimits.EquipmentLimit
```


## Adding Recipes

- Creates a simple equipped item limit Group:

    ```groovy:no-line-numbers
    mods.pickuplimits.equipment_limit.simpleEquipmentLimit(String, int, ItemStack...)
    ```

- Creates a equipped item limit group with limit message:

    ```groovy:no-line-numbers
    mods.pickuplimits.equipment_limit.simpleEquipmentLimit(String, int, String, ItemStack...)
    ```

- Creates a simple equipped item enchantment limit group:

    ```groovy:no-line-numbers
    mods.pickuplimits.equipment_limit.simpleEnchantmentLimit(String, int, Enchantment...)
    ```

- Creates a simple equipped item enchantment limit group with limit message:

    ```groovy:no-line-numbers
    mods.pickuplimits.equipment_limit.simpleEnchantmentLimit(String, int, String, Enchantment...)
    ```

- Creates an equipment limit group restricting the number of enchanted items a player can equip and use at once:

    ```groovy:no-line-numbers
    mods.pickuplimits.equipment_limit.equippedEnchantedItemLimit(int, boolean)
    ```

- Creates an equipment limit group restricting the number of enchanted items a player can equip and use at once with limit message:

    ```groovy:no-line-numbers
    mods.pickuplimits.equipment_limit.equippedEnchantedItemLimit(int, String, boolean)
    ```

:::::::::: details Example {open id="example"}
```groovy:no-line-numbers
mods.pickuplimits.equipment_limit.simpleEquipmentLimit('diamond_armor', 2, item('minecraft:diamond_helmet'), item('minecraft:diamond_chestplate'), item('minecraft:diamond_leggings'), item('minecraft:diamond_boots'))
mods.pickuplimits.equipment_limit.simpleEquipmentLimit('diamond_armor', 2, 'your.translation.key', item('minecraft:diamond_helmet'), item('minecraft:diamond_chestplate'), item('minecraft:diamond_leggings'), item('minecraft:diamond_boots'))
mods.pickuplimits.equipment_limit.simpleEquipmentLimit('diamond_armor', 2, 'You can only equip two pieces of diamond armor', item('minecraft:diamond_helmet'), item('minecraft:diamond_chestplate'), item('minecraft:diamond_leggings'), item('minecraft:diamond_boots'))
mods.pickuplimits.equipment_limit.simpleEnchantmentLimit('protection', 8, enchantment('minecraft:protection'))
mods.pickuplimits.equipment_limit.simpleEnchantmentLimit('protection', 8, 'your.translation.key', enchantment('minecraft:protection'))
mods.pickuplimits.equipment_limit.simpleEnchantmentLimit('protection', 8, 'You can only equip up to 8 levels of Protection', enchantment('minecraft:protection'))
mods.pickuplimits.equipment_limit.equippedEnchantedItemLimit(2, false)
mods.pickuplimits.equipment_limit.equippedEnchantedItemLimit(2, 'your.translation.key', false)
mods.pickuplimits.equipment_limit.equippedEnchantedItemLimit(2, 'You can only equip two enchanted items', false)
```

::::::::::

### Recipe Builder

Just like other recipe types, the Equipment Limits also uses a recipe builder.

Don't know what a builder is? Check [the builder info page](../../getting_started/builder.md) out.

:::::::::: details mods.pickuplimits.equipment_limit.equipmentLimitBuilder() {open id="abstract"}
- `List<ItemStack>`. <p>You can add ItemStacks to any Limit Group. Additional stacks can be included by separating them with a comma or using repeating `addStacks()` method calls.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addStacks(ItemStack...)
    ```

- `List<Enchantment>`. <p>You can add Enchantment values to Equipment Limit groups. Using the `setMatchAnyEnchant()` will allow the limit group to match any enchantment.</p><p>You can call the `addEnchantments()` method as many times as needed in any order.</p>. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addEnchantments(Enchantment...)
    ```

- `boolean`. <p>Using the `setMatchAnyEnchant()` will allow the limit group to match any enchantment.</p><p>Calling the `setMatchAnyEnchant()` will override all other group enchantment behavior. This includes Game Stage enchantment removals added with `addStagedEnchantRemovals()`.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1. (Default `false`).

    ```groovy:no-line-numbers
    setMatchAnyEnchant()
    ```

- `boolean`. <p>The ```setIgnoreItemEnchantmentCount()``` method allows you to bypass the checks for both enchantment level and total enchants on the item. An Iron Chestplate with Protection III and Thorns II will have an enchantment limit value of 1.</p><p>Due to the nature of the ```setIgnoreItemEnchantmentCount()``` method, it has priority over, and is mutually exclusive to ```setIgnoreEnchantmentLevel()```.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1. (Default `false`).

    ```groovy:no-line-numbers
    setIgnoreItemEnchantmentCount()
    ```

- `boolean`. <p>The ```setIgnoreEnchantmentLevel()``` method will set this specific group to ignore enchantment levels and instead tally the number of matching enchants. An Iron Chestplate with Protection III and Thorns II will have an enchantment limit value of 1 + 1 = 2, 1 limit value for protection and 1 limit value for thorns.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1. (Default `false`).

    ```groovy:no-line-numbers
    setIgnoreEnchantmentLevel()
    ```

- `boolean`. <p>The ```setCheckMainhand()``` method will also include whatever item is currently being held by the player. If the equipment limit is exceeded, the main hand item will be **DROPPED** into the world.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1. (Default `false`).

    ```groovy:no-line-numbers
    setCheckMainhand()
    ```

- `boolean`. <p>The ```setCheckOffhand()``` method includes the player's offhand slot when calculating the equipment limit. If the equipment limit is exceeded, the item will be inserted into the player's inventory or dropped into the world if the inventory is full.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1. (Default `false`).

    ```groovy:no-line-numbers
    setCheckOffhand()
    ```

- `ILimitFunction`. <p>Limit value functions allow users to have complete control over the calculated limit value of any item that matches the limit group.<p>The `ILimitFunction` is a function provider that grants access to the player (EntityPlayer), the queried ItemStack, and the limit group (ILimitGroup).</p><p>This method overwrites all other limit calculation behavior. Should you want to use default group behavior, be sure to return the ```getDefaultLimitValue``` pulled from the group parameter.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1.

    ```groovy:no-line-numbers
    setStackLimitFunction(ILimitFunction)
    ```

- `boolean`. <p>When using stack limit value functions, sometimes it is necessary to allow limit groups to exceed their max limit values. The `setAllowOverLimit()` method disables default limit group behavior such as dropping or unequipping items.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1. (Default `false`).

    ```groovy:no-line-numbers
    setAllowOverLimit()
    ```

- `List<Potion>`. <p>Encumbered Effects are potion effects that applied when a player exceeds a limit group. These are performed in lue of dropping or unequipping the items.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addEncumberedEffect(Potion, int)
    ```

- `String`. Format error: <p>You can define the message sent to the player whenever an item exceeds the limit group max limit. This method supports localization language keys or plain text. Including a `%s` in the text or translated string will also display the ItemStack display name. This method can only be called once. Subsequent calls will overwrite previous settings.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1.

    ```groovy:no-line-numbers
    setLimitMessage(String)
    ```

- `String`. <p>You can define item tooltips that will display on any item that belongs or is matched with this limit group. If this method is not called, no tooltip will be displayed.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1.

    ```groovy:no-line-numbers
    setLimitTooltip(String)
    ```

- `Map<ItemStack, Integer>`. <p>You can define limit adjustments based on the player's currently equipped baubles. A positive adjustment value will increase the group item limit and negative adjustment value will decrease the group item limit.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addBaubleLimitAdjustment(ItemStack, int)
    ```

- `Map<Enchantment, Integer>`. <p>You can define limit adjustments based on enchantments on the player's currently equipped items. This search includes both equipment and bauble slots. A positive adjustment value will increase the group item limit and negative adjustment value will decrease the group item limit.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addEnchantmentLimitAdjustment(Enchantment, int, int)
    ```

- `Map<Potion, Integer>`. <p>You can define limit adjustments based on the player's currently equipped potion effects. A positive adjustment value will increase the group item limit and negative adjustment value will decrease the group item limit.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addPotionLimitAdjustment(Potion, int, int)
    ```

- `Map<String, Integer>`. <p>You can define group limit overrides depending on player game stage. This value will override the default limit if the player has the specified game stage.</p>You can use this method as many times as needed, but stages must registered in order of priority, lowest priority being registered first and highest priority registered last. The last registered stage will override any that precede it.</p><p>Using this method to define a limit override value of -1 will disable this group. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addStagedLimitOverride(String, int)
    ```

- `Map<String, List<ItemStack>>`. <p>You can define any ItemStack value you wish to remove from this group based on player stage. When the player reaches the specified stage, any defined item will be removed from the limit group.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addStagedStackRemovals(String, ItemStack...)
    ```

- `Map<String, List<OreDictIngredient>>`. <p>You can define Enchantment values you wish to remove from this group based on player stage. When the player reaches the specified stage, any defined item will be removed from the limit group.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addStagedEnchantmentRemovals(String, Enchantment...)
    ```

- `Map<ItemStack, Integer>`. <p>You can define limit adjustments based on the player's currently equipped armors. A positive adjustment value will increase the group item limit and negative adjustment value will decrease the group item limit.You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addArmorLimitAdjustment(ItemStack, int)
    ```

- First validates the builder, returning `null` and outputting errors to the log file if the validation failed, then registers the builder and returns the registered object. (returns `null` or `com.invadermonky.pickuplimit.limits.groups.EquipmentLimitGroup`).

    ```groovy:no-line-numbers
    register()
    ```



::::::::::
