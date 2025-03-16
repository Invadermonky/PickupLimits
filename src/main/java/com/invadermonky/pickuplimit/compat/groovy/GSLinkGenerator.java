package com.invadermonky.pickuplimit.compat.groovy;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import com.invadermonky.pickuplimit.PickupLimits;

public class GSLinkGenerator extends BasicLinkGenerator {
    @Override
    public String id() {
        return PickupLimits.MOD_NAME;
    }

    @Override
    protected String version() {
        return PickupLimits.VERSION;
    }

    @Override
    protected String domain() {
        return "https://github.com/Invadermonky/PickupLimit/";
    }

}
