package com.invadermonky.pickuplimit.compat.groovy;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import com.invadermonky.pickuplimit.compat.groovy.limits.EquipmentLimit;
import com.invadermonky.pickuplimit.compat.groovy.limits.PickupLimit;

public class GSContainer extends GroovyPropertyContainer {
    public final EquipmentLimit EquipmentLimit = new EquipmentLimit();
    public final PickupLimit PickupLimit = new PickupLimit();

    public GSContainer() {
        this.addProperty(EquipmentLimit);
        this.addProperty(PickupLimit);
    }
}
