package com.invadermonky.pickuplimit.config;

import com.invadermonky.pickuplimit.util.LogHelper;
import gnu.trove.map.hash.THashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModTags {
    public static THashMap<String,Integer> simpleItemEntityLifetimes = new THashMap<>();
    public static THashMap<ItemStack,Integer> itemStackItemEntityLifetimes = new THashMap<>();

    public static int getItemLifetime(ItemStack stack) {
        int config = ConfigHandlerPL.item_lifetimes.globalItemLifetime;
        if(stack.isEmpty())
            return config;

        for(ItemStack mapStack : itemStackItemEntityLifetimes.keySet()) {
            if(OreDictionary.itemMatches(mapStack, stack, false)) {
                if(!mapStack.hasTagCompound() || ItemStack.areItemStackTagsEqual(mapStack, stack)) {
                    return itemStackItemEntityLifetimes.get(mapStack);
                }
            }
        }

        String itemId = stack.getItem().getRegistryName().toString();
        if(simpleItemEntityLifetimes.containsKey(itemId)) {
            return simpleItemEntityLifetimes.get(itemId);
        } else if(simpleItemEntityLifetimes.containsKey(itemId + ":" + stack.getMetadata())) {
            return simpleItemEntityLifetimes.get(itemId + ":" + stack.getMetadata());
        }
        return config;
    }

    public static void syncConfig() {
        populateConfigMaps(ConfigHandlerPL.item_lifetimes.itemLifetimeOverrides, simpleItemEntityLifetimes, itemStackItemEntityLifetimes);
    }

    private static void populateConfigMaps(String[] configArray, THashMap<String,Integer> simpleItemMap, THashMap<ItemStack,Integer> itemStackMap) {
        simpleItemMap.clear();
        itemStackMap.clear();
        Pattern pattern = Pattern.compile("^(([^:]+?):([^:]+?)(?::(\\d+))?)(\\{.+\\})?=(\\d+)$");
        /*  Item parsing regex pattern:
                Group 1 - modid
                Group 2 - itemid
                Group 3 - meta
                Group 4 - tag
                Group 5 - limit
            Format:
                modid:itemid:meta{tagdata}=stacklimit   ("meta" and "tagdata" are optional)
                minecraft:stone:0{tag=data}=16
         */

        Matcher matcher;
        for(String configStr : configArray) {
            try {
                matcher = pattern.matcher(configStr);
                if (matcher.find()) {
                    //The setting will always be matcher group 6 for item parsing.
                    int setting = Integer.parseInt(matcher.group(6));
                    if (matcher.group(5) == null) {
                        simpleItemMap.put(matcher.group(1), setting);
                    } else {
                        Item item = Item.getByNameOrId(new ResourceLocation(matcher.group(2), matcher.group(3)).toString());
                        if(item != null) {
                            ItemStack stack = new ItemStack(item, 1, matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : Short.MAX_VALUE);
                            NBTTagCompound tag = JsonToNBT.getTagFromJson(matcher.group(5));
                            stack.setTagCompound(tag);
                            itemStackMap.put(stack, setting);
                        }
                    }
                }
            } catch (Exception e) {
                LogHelper.error("An error occurred while parsing a configuration setting.");
                LogHelper.error(e);
            }

        }
    }

}
