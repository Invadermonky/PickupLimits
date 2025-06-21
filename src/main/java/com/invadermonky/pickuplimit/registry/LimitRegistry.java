package com.invadermonky.pickuplimit.registry;

import com.invadermonky.pickuplimit.limits.groups.AbstractLimitGroup;
import com.invadermonky.pickuplimit.limits.groups.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.limits.groups.PickupLimitGroup;
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
        if (pickupLimitGroup != null) {
            equipmentLimitGroups.put(pickupLimitGroup.getGroupName(), pickupLimitGroup);
        } else {
            LogHelper.error("Failed to register PickupLimitGroup. Value cannot be null.");
        }
    }

    public static void addPickupLimitGroup(PickupLimitGroup pickupLimitGroup) {
        if (pickupLimitGroup != null) {
            pickupLimitGroups.put(pickupLimitGroup.getGroupName(), pickupLimitGroup);
        } else {
            LogHelper.error("Failed to register PickupLimitGroup. Value cannot be null.");
        }
    }

    public static List<EquipmentLimitGroup> getEquipmentLimitGroups(EntityPlayer player, ItemStack stack) {
        List<EquipmentLimitGroup> pickupLimitGroups = new ArrayList<>();
        for (EquipmentLimitGroup equipmentLimitGroup : equipmentLimitGroups.values()) {
            if (equipmentLimitGroup.matches(player, stack)) {
                pickupLimitGroups.add(equipmentLimitGroup);
            }
        }
        return pickupLimitGroups;
    }

    public static List<PickupLimitGroup> getPickupLimitGroups(EntityPlayer player, ItemStack stack) {
        List<PickupLimitGroup> pickupLimitGroups = new ArrayList<>();
        for (PickupLimitGroup pickupLimitGroup : LimitRegistry.pickupLimitGroups.values()) {
            if (pickupLimitGroup.matches(player, stack)) {
                pickupLimitGroups.add(pickupLimitGroup);
            }
        }
        return pickupLimitGroups;
    }

    /**
     * Returns all limit groups associated with the passed Player and ItemStack.
     *
     * @param player The player being queried
     * @param stack The itemstack being queried
     * @return All Pickup and Equipment limit groups valid for the passed player and itemstack
     */
    public static List<AbstractLimitGroup<?>> getLimitGroups(EntityPlayer player, ItemStack stack) {
        List<AbstractLimitGroup<?>> groups = new ArrayList<>();
        groups.addAll(getEquipmentLimitGroups(player, stack));
        groups.addAll(getPickupLimitGroups(player, stack));
        return groups;
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
