package net.archers.component;

import com.mojang.serialization.Codec;
import net.archers.ArchersMod;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import java.util.function.UnaryOperator;

public class ArcherComponents {
    public static final DataComponentType<Boolean> AUTO_FIRE = register(
            Identifier.fromNamespaceAndPath(ArchersMod.ID, "afh"), builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    private static <T> DataComponentType<T> register(Identifier id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, ((DataComponentType.Builder)builderOperator.apply(DataComponentType.builder())).build());
    }
}
