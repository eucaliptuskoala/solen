package org.solen.business.habitcases.creationstrategy;

public class NameUtils {

    public static String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }
        name = name.trim().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private NameUtils() {
    }
}
