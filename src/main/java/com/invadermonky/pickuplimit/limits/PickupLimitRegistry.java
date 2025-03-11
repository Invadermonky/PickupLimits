package com.invadermonky.pickuplimit.limits;

import com.invadermonky.pickuplimit.util.LogHelper;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PickupLimitRegistry {
    private static final THashMap<String,PickupLimitGroup> pickupLimitGroups = new THashMap<>();

    public static void addPickupLimitGroup(PickupLimitGroup pickupLimitGroup) {
        if(pickupLimitGroup != null) {
            pickupLimitGroups.put(pickupLimitGroup.getGroupName(), pickupLimitGroup);
        } else {
            LogHelper.error("Failed to register PickupLimitGroup. Value cannot be null.");
        }
    }

    public static List<PickupLimitGroup> getPickupLimitGroups(EntityPlayer player, ItemStack stack) {
        List<PickupLimitGroup> limitGroups = new ArrayList<>();
        for(PickupLimitGroup limitGroup : pickupLimitGroups.values()) {
            if(limitGroup.matches(player, stack)) {
                limitGroups.add(limitGroup);
            }
        }
        return limitGroups;
    }

    public static void removePickupLimitGroup(String groupName) {
        pickupLimitGroups.remove(groupName);
    }

    public static void removeAllGroups() {
        pickupLimitGroups.clear();
    }

    public static THashMap<String,PickupLimitGroup> getAllPickupLimitGroups() {
        return pickupLimitGroups;
    }
}
