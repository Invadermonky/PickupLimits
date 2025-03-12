package com.invadermonky.pickuplimit.limits.builders;

import com.invadermonky.pickuplimit.limits.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.limits.util.AbstractLimitBuilder;
import com.invadermonky.pickuplimit.util.StringHelper;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.Optional;

import java.util.*;

public class EquipmentLimitBuilder extends AbstractLimitBuilder<EquipmentLimitBuilder, EquipmentLimitGroup> {
    protected boolean matchAnyEnchant;
    protected boolean ignoreEnchantLevel;
    protected boolean checkMainhand;
    protected boolean checkOffhand;
    protected final Set<Enchantment> groupEnchants = new THashSet<>();
    protected final Map<String, Set<Enchantment>> stagedEnchantRemovals = new THashMap<>();

    public EquipmentLimitBuilder(String groupName, int defaultLimit) {
        super(groupName, defaultLimit);
        this.setLimitMessage(StringHelper.getTranslationKey("no_equip", "chat"));
        this.ignoreEnchantLevel = false;
        this.checkMainhand = false;
        this.checkOffhand = false;
    }

    @Override
    protected EquipmentLimitBuilder getThis() {
        return this;
    }

    public EquipmentLimitBuilder addEnchantsToGroup(Enchantment... enchantments) {
        this.groupEnchants.addAll(Arrays.asList(enchantments));
        return this;
    }

    public Set<Enchantment> getGroupEnchants() {
        return this.groupEnchants;
    }

    public EquipmentLimitBuilder setMatchAnyEnchant() {
        this.matchAnyEnchant = true;
        return this;
    }

    public boolean getMatchAnyEnchant() {
        return this.matchAnyEnchant;
    }

    public EquipmentLimitBuilder setIgnoreEnchantmentLevels() {
        this.ignoreEnchantLevel = true;
        return this;
    }

    public boolean getIgnoreEnchantmentLevels() {
        return this.ignoreEnchantLevel;
    }

    public EquipmentLimitBuilder setCheckMainhand() {
        this.checkMainhand = true;
        return this;
    }

    public boolean getCheckMainhand() {
        return this.checkMainhand;
    }

    public EquipmentLimitBuilder setCheckOffhand() {
        this.checkOffhand = true;
        return this;
    }

    public boolean getCheckOffhand() {
        return this.checkOffhand;
    }

    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public EquipmentLimitBuilder addStagedEnchantRemoval(String stageName, Enchantment... enchantments) {
        Set<Enchantment> enchants = new HashSet<>(Arrays.asList(enchantments));
        enchants.removeIf(Objects::isNull);
        if(this.stagedEnchantRemovals.containsKey(stageName)) {
            this.stagedEnchantRemovals.get(stageName).addAll(enchants);
        } else {
            this.stagedEnchantRemovals.put(stageName, enchants);
        }
        return this;
    }

    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public Map<String, Set<Enchantment>> getStagedEnchantRemovals() {
        return this.stagedEnchantRemovals;
    }

    @Override
    public EquipmentLimitGroup build() {
        return new EquipmentLimitGroup(this);
    }
}
