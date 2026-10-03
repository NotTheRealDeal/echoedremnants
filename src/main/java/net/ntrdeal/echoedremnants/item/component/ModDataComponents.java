package net.ntrdeal.echoedremnants.item.component;

import net.minecraft.core.component.DataComponentType;
import net.ntrdeal.echoedremnants.component.echoed.EchoedFood;
import net.ntrdeal.echoedremnants.component.echolocation.Echolocation;
import net.ntrdeal.echoedremnants.component.protection.EchoProtection;
import net.ntrdeal.echoedremnants.reference.ModDataComponentIds;
import net.ntrdeal.realapi.util.registry.DataComponentRegistry;

public class ModDataComponents {
    public static final DataComponentType<Echolocation> ECHOLOCATION = DataComponentRegistry.register(
            ModDataComponentIds.ECHOLOCATION, builder -> builder.persistent(Echolocation.CODEC)
                    .networkSynchronized(Echolocation.STREAM_CODEC).cacheEncoding()
    );

    public static final DataComponentType<EchoProtection> ECHO_PROTECTION = DataComponentRegistry.register(
            ModDataComponentIds.ECHO_PROTECTION, builder -> builder.persistent(EchoProtection.CODEC)
                    .networkSynchronized(EchoProtection.STREAM_CODEC).cacheEncoding()
    );

    public static final DataComponentType<EchoedFood> ECHOED_FOOD = DataComponentRegistry.register(
            ModDataComponentIds.ECHOED_FOOD, builder -> builder.persistent(EchoedFood.CODEC)
                    .networkSynchronized(EchoedFood.STREAM_CODEC).cacheEncoding()
    );

    public static void register() {}
}
