package com.invadermonky.pickuplimit.compat.crafttweaker.api;

import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenClass(LibZenClasses.ILimitGroup)
@ZenRegister
public interface ILimitGroupCT {
    @ZenGetter("name")
    String getName();

    @ZenGetter("message")
    String getLimitMessage();

    @ZenSetter("message")
    void setLimitMessage(String message);

    @ZenGetter("count")
    int getInvCount();

    @ZenSetter("count")
    void setInvCount(int invCount);

    @ZenGetter("limit")
    int getLimit();

    @ZenMethod
    int getDefaultLimitValue(IItemStack stack);
}
