package com.invadermonky.pickuplimit.util.libs;

import baubles.common.Baubles;
import com.cleanroommc.groovyscript.GroovyScript;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraftforge.fml.common.Loader;

public enum ModIds {
    baubles(ConstIds.baubles),
    crafttweaker(ConstIds.craftweaker),
    gamestages(ConstIds.gamestages),
    groovyscript(ConstIds.groovyscript)
    ;

    public final String modId;

    ModIds(String modId) {
        this.modId = modId;
    }

    public boolean isLoaded() {
        return Loader.isModLoaded(this.modId);
    }

    public static class ConstIds {
        public static final String baubles = Baubles.MODID;
        public static final String craftweaker = CraftTweaker.MODID;
        public static final String gamestages = "gamestages";
        public static final String groovyscript = GroovyScript.ID;
    }
}
