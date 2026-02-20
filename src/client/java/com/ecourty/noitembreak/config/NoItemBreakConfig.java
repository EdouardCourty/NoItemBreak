package com.ecourty.noitembreak.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoItemBreakConfig {

    public static final List<String> DEFAULT_PROTECTED_ITEMS = Arrays.asList(
        "minecraft:diamond_sword",
        "minecraft:diamond_pickaxe",
        "minecraft:diamond_axe",
        "minecraft:diamond_shovel",
        "minecraft:diamond_hoe",
        "minecraft:netherite_sword",
        "minecraft:netherite_pickaxe",
        "minecraft:netherite_axe",
        "minecraft:netherite_shovel",
        "minecraft:netherite_hoe"
    );

    public boolean enabled = true;
    public List<String> protectedItems = new ArrayList<>(DEFAULT_PROTECTED_ITEMS);

    public NoItemBreakConfig copy() {
        NoItemBreakConfig copy = new NoItemBreakConfig();
        copy.enabled = this.enabled;
        copy.protectedItems = new ArrayList<>(this.protectedItems);
        return copy;
    }
}
