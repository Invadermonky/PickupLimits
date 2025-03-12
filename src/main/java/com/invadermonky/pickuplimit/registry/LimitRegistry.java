package com.invadermonky.pickuplimit.registry;

import com.invadermonky.pickuplimit.limits.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.limits.PickupLimitGroup;
import com.invadermonky.pickuplimit.util.LogHelper;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LimitRegistry {
    private static final THashMap<String, EquipmentLimitGroup> equipmentLimitGroups = new THashMap<>();
    private static final THashMap<String, PickupLimitGroup> pickupLimitGroups = new THashMap<>();

    public static void addEquipmentLimitGroup(EquipmentLimitGroup pickupLimitGroup) {
        if(pickupLimitGroup != null) {
            equipmentLimitGroups.put(pickupLimitGroup.getGroupName(), pickupLimitGroup);
        } else {
            LogHelper.error("Failed to register PickupLimitGroup. Value cannot be null.");
        }
    }

    public static void addPickupLimitGroup(PickupLimitGroup pickupLimitGroup) {
        if(pickupLimitGroup != null) {
            pickupLimitGroups.put(pickupLimitGroup.getGroupName(), pickupLimitGroup);
        } else {
            LogHelper.error("Failed to register PickupLimitGroup. Value cannot be null.");
        }
    }

    public static List<EquipmentLimitGroup> getEquipmentLimitGroups(EntityPlayer player, ItemStack stack) {
        List<EquipmentLimitGroup> pickupLimitGroups = new ArrayList<>();
        for(EquipmentLimitGroup pickupLimitGroup : equipmentLimitGroups.values()) {
            if(pickupLimitGroup.matches(player, stack)) {
                pickupLimitGroups.add(pickupLimitGroup);
            }
        }
        return pickupLimitGroups;
    }

    public static List<PickupLimitGroup> getPickupLimitGroups(EntityPlayer player, ItemStack stack) {
        List<PickupLimitGroup> pickupLimitGroups = new ArrayList<>();
        for(PickupLimitGroup pickupLimitGroup : LimitRegistry.pickupLimitGroups.values()) {
            if(pickupLimitGroup.matches(player, stack)) {
                pickupLimitGroups.add(pickupLimitGroup);
            }
        }
        return pickupLimitGroups;
    }

    public static THashMap<String, EquipmentLimitGroup> getAllEquipmentLimitGroups() {
        return equipmentLimitGroups;
    }

    public static THashMap<String, PickupLimitGroup> getAllPickupLimitGroups() {
        return pickupLimitGroups;
    }

    public static void removeEquipmentLimitGroup(String groupName) {
        equipmentLimitGroups.remove(groupName);
    }

    public static void removePickupLimitGroup(String groupName) {
        pickupLimitGroups.remove(groupName);
    }

    public static void removeAllEquipmentLimitGroups() {
        equipmentLimitGroups.clear();
    }

    public static void removeAllPickupLimitGroups() {
        pickupLimitGroups.clear();
    }
}
