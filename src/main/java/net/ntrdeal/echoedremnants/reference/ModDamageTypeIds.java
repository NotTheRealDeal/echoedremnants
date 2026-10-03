package net.ntrdeal.echoedremnants.reference;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.realapi.util.registry.ResourceCreator;

public class ModDamageTypeIds {
    private static final ResourceCreator<DamageType> CREATOR = ResourceCreator.of(Registries.DAMAGE_TYPE, EchoedRemnants::id);

    public static final ResourceKey<DamageType> ECHOED = CREATOR.create("echoed");
}
