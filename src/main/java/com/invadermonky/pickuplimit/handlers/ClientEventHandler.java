package com.invadermonky.pickuplimit.handlers;

import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.limits.groups.AbstractLimitGroup;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.StringHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = PickupLimits.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler {
    //TODO: Check for no server crash.
    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        if (!ConfigHandlerPL.pickup_limits.enableTooltipLimitMessage)
            return;

        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();
        if (player != null && !stack.isEmpty()) {
            List<AbstractLimitGroup<?>> groups = LimitRegistry.getLimitGroups(event.getEntityPlayer(), event.getItemStack()).stream()
                    .filter(AbstractLimitGroup::hasLimitTooltip)
                    .collect(Collectors.toList());

            if (groups.isEmpty())
                return;

            if (!GuiScreen.isShiftKeyDown() && ConfigHandlerPL.pickup_limits.tooltipMessageRequiresShift) {
                event.getToolTip().add(I18n.format(StringHelper.getTranslationKey("limits", "tooltip")));
            } else {
                groups.forEach(group -> {
                    String tooltip = I18n.format(group.getLimitTooltip(), group.getLimit(player));
                    if (tooltip.matches("Format error: .*")) {
                        tooltip = I18n.format(group.getLimitTooltip());
                    }
                    event.getToolTip().add(tooltip);
                });
            }
        }
    }
}
