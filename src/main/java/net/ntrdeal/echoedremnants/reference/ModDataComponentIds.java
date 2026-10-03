package net.ntrdeal.echoedremnants.reference;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.realapi.util.registry.ResourceCreator;

public class ModDataComponentIds {
    private static final ResourceCreator<DataComponentType<?>> CREATOR = ResourceCreator.of(Registries.DATA_COMPONENT_TYPE, EchoedRemnants::id);

    public static final ResourceKey<DataComponentType<?>> ECHOLOCATION = CREATOR.create("echolocation");
    public static final ResourceKey<DataComponentType<?>> ECHO_PROTECTION = CREATOR.create("echo_protection");
    public static final ResourceKey<DataComponentType<?>> ECHOED_FOOD = CREATOR.create("echoed_food");
}
