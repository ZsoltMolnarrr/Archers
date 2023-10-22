package net.archers.config;

import org.jetbrains.annotations.Nullable;

public record RangedConfig(int pull_time, float damage, @Nullable Float velocity) { }
