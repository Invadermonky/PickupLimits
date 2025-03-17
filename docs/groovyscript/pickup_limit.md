---
title: "Pickup Limits"
titleTemplate: "Pickup Limits | CleanroomMC"
description: "Pickup Limits are limit groups that restrict the number of items a player is allowed to carry. PickupLimits scan the player's inventory and calculate a total based on number of items that match each limit group. Should the player exceed the maximum allowable limit, excess items will be dropped into the world. For more information and examples, you can check out the Pickup Limits GitHub Wiki https://github.com/Invadermonky/PickupLimits/wiki."
source_code_link: "https://github.com/CleanroomMC/GroovyScript/blob/master/src/main/java/com/invadermonky/pickuplimit/compat/groovy/limits/PickupLimit.java"
---

# Pickup Limits (Pickup Limits)

## Description

Pickup Limits are limit groups that restrict the number of items a player is allowed to carry. PickupLimits scan the player's inventory and calculate a total based on number of items that match each limit group. Should the player exceed the maximum allowable limit, excess items will be dropped into the world. For more information and examples, you can check out the Pickup Limits GitHub Wiki https://github.com/Invadermonky/PickupLimits/wiki.

## Identifier

Refer to this via any of the following:

```groovy:no-line-numbers {1}
mods.pickuplimits.pickup_limit/* Used as page default */ // [!code focus]
mods.pickuplimits.pickuplimit
mods.pickuplimits.pickupLimit
mods.pickuplimits.PickupLimit
```


## Adding Recipes

- Creates a simple ItemStack inventory carry limit group:

    ```groovy:no-line-numbers
    mods.pickuplimits.pickup_limit.simplePickupLimit(String, int, ItemStack...)
    ```

- Creates a simple ItemStack inventory carry limit group with limit message:

    ```groovy:no-line-numbers
    mods.pickuplimits.pickup_limit.simplePickupLimit(String, int, String, ItemStack...)
    ```

- Creates a simple Ore Dictionary inventory carry limit group:

    ```groovy:no-line-numbers
    mods.pickuplimits.pickup_limit.simplePickupLimit(String, int, OreDictIngredient...)
    ```

- Creates a simple Ore Dictionary inventory carry limit group with limit message:

    ```groovy:no-line-numbers
    mods.pickuplimits.pickup_limit.simplePickupLimit(String, int, String, OreDictIngredient...)
    ```

:::::::::: details Example {open id="example"}
```groovy:no-line-numbers
mods.pickuplimits.pickup_limit.simplePickupLimit('stone', 256, item('minecraft:stone'), item('minecraft:cobblestone'))
mods.pickuplimits.pickup_limit.simplePickupLimit('stone', 256, 'your.translation.key', item('minecraft:stone'), item('minecraft:cobblestone'))
mods.pickuplimits.pickup_limit.simplePickupLimit('stone', 256, 'You cannot carry any more %s', item('minecraft:stone'), item('minecraft:cobblestone'))
mods.pickuplimits.pickup_limit.simplePickupLimit('stone', 256, 'You cannot carry any more stone', item('minecraft:stone'), item('minecraft:cobblestone'))
mods.pickuplimits.pickup_limit.simplePickupLimit('gems', 32, ore('gemDiamond'), ore('gemEmerald'))
mods.pickuplimits.pickup_limit.simplePickupLimit('gems', 32, 'your.translation.key', ore('gemDiamond'), ore('gemEmerald'))
mods.pickuplimits.pickup_limit.simplePickupLimit('gems', 32, '%s are falling out of your pockets', ore('gemDiamond'), ore('gemEmerald'))
mods.pickuplimits.pickup_limit.simplePickupLimit('gems', 32, 'Gems are falling out of your pockets', ore('gemDiamond'), ore('gemEmerald'))
```

::::::::::

### Recipe Builder

Just like other recipe types, the Pickup Limits also uses a recipe builder.

Don't know what a builder is? Check [the builder info page](../../getting_started/builder.md) out.

:::::::::: details mods.pickuplimits.pickup_limit.pickupLimitBuilder() {open id="abstract"}
- `List<ItemStack>`. <p>You can add ItemStacks to any Limit Group. Additional stacks can be included by separating them with a comma or using repeating `addStacks()` method calls.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 1.

    ```groovy:no-line-numbers
    addStacks(ItemStack...)
    ```

- `List<OreDictIngredient>`. <p>In addition to ItemStacks, Pickup Limits support adding items via [OreDictIngredient](https://cleanroommc.com/groovy-script/minecraft/vanilla_object_mappers#ore). Any ItemStack matching the passed ore dictionary will be registered with the limit group.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addOreDict(OreDictIngredient)
    ```

- `ILimitFunction`. <p>Limit value functions allow users to have complete control over the calculated limit value of any item that matches the limit group.<p>The `ILimitFunction` is a function provider that grants access to the player (EntityPlayer), the queried ItemStack, and the limit group (ILimitGroup).</p><p>This method overwrites all other limit calculation behavior. Should you want to use default group behavior, be sure to return the ```getDefaultLimitValue``` pulled from the group parameter.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1.

    ```groovy:no-line-numbers
    setStackLimitFunction(ILimitFunction)
    ```

- `boolean`. <p>When using stack limit value functions, sometimes it is necessary to allow limit groups to exceed their max limit values. The `setAllowOverLimit()` method disables default limit group behavior such as dropping or unequipping items.</p>This method can only be called once per builder. Requires greater than or equal to 0 and exactly 1. (Default `false`).

    ```groovy:no-line-numbers
    setAllowOverLimit()
    ```

- `Map<Potion, Integer>`. <p>Encumbered Effects are potion effects that applied when a player exceeds a limit group. These are performed in lue of dropping or unequipping the items.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

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

- `Map<ItemStack, Integer>`. <p>You can define limit adjustments based on the player's currently equipped armors. A positive adjustment value will increase the group item limit and negative adjustment value will decrease the group item limit.You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addArmorLimitAdjustment(ItemStack, int)
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

- `Map<String, List<OreDictIngredient>>`. <p>You can define any [OreDictIngredient](https://cleanroommc.com/groovy-script/minecraft/vanilla_object_mappers#ore) value you wish to remove from this group based on player stage. When the player reaches the specified stage, any defined item will be removed from the limit group.</p>You can call this method as many times as needed in any order. Requires greater than or equal to 0.

    ```groovy:no-line-numbers
    addStagedOreRemovals(String, OreDictIngredient...)
    ```

- First validates the builder, returning `null` and outputting errors to the log file if the validation failed, then registers the builder and returns the registered object. (returns `null` or `com.invadermonky.pickuplimit.limits.groups.PickupLimitGroup`).

    ```groovy:no-line-numbers
    register()
    ```



::::::::::
