package net.ntrdeal.echoedremnants.reference;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.realapi.util.registry.ResourceCreator;

public class ModMobEffectIds {
    private static final ResourceCreator<MobEffect> CREATOR = ResourceCreator.of(Registries.MOB_EFFECT, EchoedRemnants::id);

    public static final ResourceKey<MobEffect> ECHOED = CREATOR.create("echoed");
}
