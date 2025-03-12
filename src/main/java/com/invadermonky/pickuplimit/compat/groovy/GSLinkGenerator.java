package com.invadermonky.pickuplimit.compat.groovy;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import com.invadermonky.pickuplimit.PickupLimit;

public class GSLinkGenerator extends BasicLinkGenerator {
    @Override
    public String id() {
        return PickupLimit.MOD_NAME;
    }

    @Override
    protected String version() {
        return PickupLimit.VERSION;
    }

    @Override
    protected String domain() {
        return "https://github.com/Invadermonky/PickupLimit";
    }

}
