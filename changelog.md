# Changelog

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
- added GroovyScript wiki support
### Changed
- pickup builder and equipment builder for CrT and GS can use either `build()` or `register()` to register the limit group
- merged identical scripting builder methods into parent class
### Fixed
- fixed a circular logic error when using `ILimitGroup#getStackLimitValue(ItemStack)`
  - `ILimitGroup#getStackLimitValue(ItemStack)` has been renamed to `ILimitGroup#getDefaultLimitValue(ItemStack)`
- fixed an Item Lifetime parsing error when setting values to -32768
<br><br>


## [1.12.2-1.0.0]
- Initial Release
