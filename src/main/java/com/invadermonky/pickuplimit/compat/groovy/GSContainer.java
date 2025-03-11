package com.invadermonky.pickuplimit.compat.groovy;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {
    public final PickupLimitGS PickupLimit = new PickupLimitGS();

    public GSContainer() {
        this.addProperty(PickupLimit);
    }
}
