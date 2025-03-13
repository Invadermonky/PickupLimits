package com.invadermonky.pickuplimit.compat.crafttweaker.api;

import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(LibZenClasses.ILimitGroup)
@ZenRegister
public interface ILimitGroupCT {
    @ZenGetter("name")
    String getName();

    @ZenGetter("message")
    String getMessage();

    @ZenGetter("limit")
    int getLimit();

    @ZenMethod
    int getStackLimitValue(IItemStack stack);
}
