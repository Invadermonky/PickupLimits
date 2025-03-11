package com.invadermonky.pickuplimit.compat.groovy;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import com.invadermonky.pickuplimit.PickupLimit;

public class LinkGeneratorGS extends BasicLinkGenerator {
    @Override
    public String id() {
        return PickupLimit.MOD_NAME;
    }

    @Override
    protected String domain() {
        //TODO: Add github link
        return super.domain();
    }

}
