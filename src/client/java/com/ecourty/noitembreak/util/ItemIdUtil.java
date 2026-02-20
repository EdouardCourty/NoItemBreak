package com.ecourty.noitembreak.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ItemIdUtil {

    private static final String DEFAULT_NAMESPACE = "minecraft";

    private ItemIdUtil() {}

    /** Adds "minecraft:" prefix if no namespace is provided. */
    public static String normalize(String input) {
        return input.contains(":") ? input : DEFAULT_NAMESPACE + ":" + input;
    }

    /** Returns true if the item ID exists in the item registry. */
    public static boolean exists(String itemId) {
        try {
            return BuiltInRegistries.ITEM.containsKey(Identifier.parse(itemId));
        } catch (Exception e) {
            return false;
        }
    }
}
