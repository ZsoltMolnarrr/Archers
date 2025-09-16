package net.archers.component;

import com.mojang.serialization.Codec;
import net.archers.ArchersMod;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ArcherComponents {
    public static final ComponentType<Boolean> AUTO_FIRE = register(
            Identifier.of(ArchersMod.ID, "afh"), builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL)
    );

    private static <T> ComponentType<T> register(Identifier id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }
}
