package net.archers.config;

import org.jetbrains.annotations.Nullable;

public record RangedConfig(int pullTime, int damage, @Nullable Float velocity) { }
