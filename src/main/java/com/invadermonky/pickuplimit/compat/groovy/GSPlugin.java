package com.invadermonky.pickuplimit.compat.groovy;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import com.cleanroommc.groovyscript.documentation.linkgenerator.LinkGeneratorHooks;
import com.invadermonky.pickuplimit.PickupLimits;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GSPlugin implements GroovyPlugin {
    @GroovyBlacklist
    public static GSContainer instance;

    @Override
    public @NotNull String getModId() {
        return PickupLimits.MOD_ID;
    }

    @Override
    public @NotNull String getContainerName() {
        return PickupLimits.MOD_NAME;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        LinkGeneratorHooks.registerLinkGenerator(new GSLinkGenerator());
    }

    @Override
    public @Nullable GroovyPropertyContainer createGroovyPropertyContainer() {
        return instance == null ? instance = new GSContainer() : instance;
    }
}
