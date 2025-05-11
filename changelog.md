# Changelog
<br><br>
## [1.12.2-1.2.0]
### Changed
- Renamed `addStacks()` builder method to `addItems()`. This method now supports IIngredients.
- Renamed Pickup Limit Builder `addStagedStackRemovals()` to `addStagedItemRemovals()`. This method now supports IIngredients.
### Fixed
- Fixed over-limit items being picked up and dropped constantly
- Fixed script error when Baubles is not loaded
- Fixed script error when Game Stages is not loaded
### Removed
- Removed Pickup Limit Builder `addOreDict()` method. This is functionality is included in `addItems()`.
- Removed Pickup Limit Builder `addStagedOreRemovals()` method. This functionality is included in `addStagedItemRemovals()`.

---

## [1.12.2-1.1.0]
### Added
- added `getInvCount()` method to ILimitGroup, exposing it for use in limit functions
- added `setInvCount(int)` method to ILimitGroup, exposing it for use in limit functions
- added `setMessage(String)` method to ILimitGroup, exposing it for use in limit functions
- added new builder method `setAllowOverLimit()`
  - this method disables the limit group item dropping/unequipping, useful for complex ILimitFunctions
- added new builder method `addEnchantmentLimitAdjustment(Enchant,int,int)`
  - this method allows adjusting group limits based on currently equipped enchants
- added new builder method `addPotionLimitAdjustment(Enchant,int,int)`
  - this method allows adjusting group limits based on active potion effects
- added new builder method `addEncumberedEffect(Potion,int)`
  - this method is a simple way to allow players to exceed group limits, but will inflict them with potion effects (such as slowness or weakness)
- added `setItemLimitValueFunction(ILimitFunction)` to CrT and GS Pickup Limit builders
- added partial GroovyScript wiki support

### Changed
- pickup builder and equipment builder for CrT and GS can use either `build()` or `register()` to register limit groups
- merged identical scripting builder methods into abstract parent class

### Fixed
- fixed a circular logic error when using `ILimitGroup#getStackLimitValue(ItemStack)`
  - `ILimitGroup#getStackLimitValue(ItemStack)` has been renamed to `ILimitGroup#getDefaultLimitValue(ItemStack)`
- fixed an Item Lifetime parsing error when setting values to -32768

---

## [1.12.2-1.0.0]
- Initial Release
