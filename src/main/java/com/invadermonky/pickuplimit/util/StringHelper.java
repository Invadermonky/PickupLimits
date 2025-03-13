package com.invadermonky.pickuplimit.util;

import com.invadermonky.pickuplimit.PickupLimits;

public class StringHelper {
    public static String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String lowercaseFirstLetter(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    public static String getTranslationKey(String unloc, String type, String... params) {
        StringBuilder str = new StringBuilder(type + "." + PickupLimits.MOD_ID + ":" + unloc);
        for(String param : params) {
            str.append(".").append(param);
        }
        return str.toString();
    }
}
