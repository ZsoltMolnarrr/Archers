package net.archers.config;

import org.jetbrains.annotations.Nullable;

public record RangedConfig(int pullTime, float damage, @Nullable Float velocity) { }
