package com.invadermonky.pickuplimit.limits;

import baubles.api.IBauble;
import com.invadermonky.pickuplimit.util.LogHelper;
import com.invadermonky.pickuplimit.util.ModIds;
import com.invadermonky.pickuplimit.util.StringHelper;
import gnu.trove.map.hash.THashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PickupLimitBuilder {
    private final String groupName;
    private final int pickupLimit;
    private final NonNullList<ItemStack> groupStacks;
    private String pickupLimitMessage;
    private final Map<ItemStack, Integer> armorLimits = new HashMap<>();
    private final Map<ItemStack, Integer> baubleLimits = new HashMap<>();
    private final LinkedHashMap<String,Integer> stageLimitOverride = new LinkedHashMap<>();
    private final THashMap<String, NonNullList<ItemStack>> stagedGroupStackRemovals = new THashMap<>();

    /**
     * Builder constructor for {@link PickupLimitGroup} objects.
     *
     * @param groupName The group name. This must be unique. Used internally for some logic.
     * @param defaultLimit The default pickup limit amount. Set to -1 to disable the limit.
     */
    public PickupLimitBuilder(String groupName, int defaultLimit) {
        this.groupName = groupName;
        this.pickupLimit = defaultLimit;
        this.groupStacks = NonNullList.create();
        this.pickupLimitMessage = StringHelper.getTranslationKey("no_pickup", "chat");
    }

    /**
     * Returns the builder pickup limit group name
     *
     * @return The builder pickup limit group name
     */
    public String getGroupName() {
        return this.groupName;
    }

    /**
     * Returns the default group pickup limit
     *
     * @return The default group pickup limit
     */
    public int getPickupLimit() {
        return this.pickupLimit;
    }

    /**
     * Returns all ItemStacks that have a limited pickup defined by this pickup limit
     *
     * @return All ItemStacks that have a limited pickup defined by this pickup limit
     */
    public NonNullList<ItemStack> getGroupStacks() {
        return this.groupStacks;
    }

    /**
     * Adds all passed ItemStacks to the group ItemStack list.
     *
     * @param stacks ItemStacks to add
     * @return this
     */
    public PickupLimitBuilder addStacksToGroup(ItemStack... stacks) {
        this.groupStacks.addAll(Arrays.asList(stacks));
        return this;
    }

    /**
     * Adds all ItemStacks associated with the passed ore dictionary string to the group ItemStack list.
     *
     * @param oreDict The ore dictionary string
     * @return this
     */
    public PickupLimitBuilder addOreDictToGroup(String oreDict) {
        this.groupStacks.addAll(OreDictionary.getOres(oreDict));
        return this;
    }

    /**
     * Sets a new failed pickup message for this group. This can be plain text or a language key. Including a %s will
     * cause the value to include the display name of the ItemStack.
     *
     * @param pickupLimitMessage The new pickup message text or language key
     * @return this
     */
    public PickupLimitBuilder setPickupLimitMessage(String pickupLimitMessage) {
        this.pickupLimitMessage = pickupLimitMessage;
        return this;
    }

    /**
     * Returns the defined pickup limit message string that will be displayed in the pickup fails. This can be a localization
     * key or plain text.
     *
     * @return The pickup limit message string that will be displayed if the pickup fails.
     */
    public String getPickupLimitMessage() {
        return this.pickupLimitMessage;
    }

    /**
     * Returns any defined equipped armor pickup limit adjustments.
     *
     * @return Defined equipped armor pickup limit adjustments.
     */
    public Map<ItemStack, Integer> getArmorLimits() {
        return this.armorLimits;
    }

    /**
     * Adds a limit adjustment when the player has the passed armor equipped. This adjustment value can be positive or
     * negative.
     *
     * @param stack The armor ItemStack
     * @param adjustment The limit adjustment when the armor is equipped
     * @return
     */
    public PickupLimitBuilder addArmorLimitAdjustment(ItemStack stack, int adjustment) {
        if(!stack.isEmpty()) {
            this.armorLimits.put(stack, adjustment);
        } else {
            LogHelper.error("Error adding armor limit for " + stack.getItem().getRegistryName());
        }
        return this;
    }

    /**
     * Returns any defined bauble pickup limit adjustments.
     *
     * @return Defined bauble pickup limit adjustments.
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public Map<ItemStack, Integer> getBaubleLimits() {
        return this.baubleLimits;
    }

    /**
     * Adds a limit adjustment when the player has the passed bauble equipped. This adjustment value can be positive
     * or negative.
     *
     * @param stack The bauble ItemStack
     * @param adjustment The limit adjustment when the bauble is equipped
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilder addBaubleLimitAdjustment(ItemStack stack, int adjustment) {
        if(!stack.isEmpty() && stack.getItem() instanceof IBauble) {
            baubleLimits.put(stack, adjustment);
        } else {
            LogHelper.error("Error adding bauble limit for " + stack.getItem().getRegistryName());
        }
        return this;
    }

    /**
     * Returns any defined GameStage group pickup limit overrides.
     *
     * @return Any defined GameStage group pickup limit overrides.
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public LinkedHashMap<String,Integer> getStageLimitOverride() {
        return this.stageLimitOverride;
    }

    /**
     * Creates a game stage pickup limit override. This value will override the default pickup limit. Stages <b>must</b>
     * be added in order. The last stage added will override any values before it provided the player has the required
     * stage.
     *
     * @param stageName The GameStage stage name
     * @param limitOverride The pickup limit override for this stage
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilder addStageLimitOverride(String stageName, int limitOverride) {
        this.stageLimitOverride.put(stageName, limitOverride);
        return this;
    }

    /**
     * Returns any defined GameStage group ItemStack removals.
     *
     * @return Any defined GameStage group ItemStack removals
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public THashMap<String, NonNullList<ItemStack>> getStagedGroupStackRemovals() {
        return this.stagedGroupStackRemovals;
    }

    /**
     * Specifies any ItemStacks that will be removed from this group once the player obtains the specified GameStage.
     *
     * @param stageName The name of the GameStage
     * @param stacks Any stacks that will be removed from this group limit when the player obtains the specified GameStage
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilder addStagedStackGroupRemoval(String stageName, ItemStack... stacks) {
        NonNullList<ItemStack> stackList = NonNullList.from(ItemStack.EMPTY, stacks);
        stackList.removeIf(ItemStack::isEmpty);
        if(this.stagedGroupStackRemovals.containsKey(stageName)) {
            this.stagedGroupStackRemovals.get(stageName).addAll(stackList);
        } else {
            this.stagedGroupStackRemovals.put(stageName, stackList);
        }
        return this;
    }

    /**
     * Uses the settings in this builder to generate a new {@link PickupLimitGroup}. This is just here for ease of use.
     */
    public PickupLimitGroup build() {
        return new PickupLimitGroup(this);
    }
}
