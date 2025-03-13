package com.invadermonky.pickuplimit.compat.crafttweaker.api;

import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass(LibZenClasses.ILimitFunction)
@ZenRegister
public interface ILimitFunctionCT {
    int process(IPlayer player, IItemStack stack, ILimitGroupCT group);
}
