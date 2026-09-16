package net.ntrdeal.echoedremnants.component.echoed;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.ntrdeal.echoedremnants.reference.ModDamageTypeIds;
import net.ntrdeal.echoedremnants.reference.ModMobEffectIds;
import net.ntrdeal.echoedremnants.reference.ModParticleIds;
import net.ntrdeal.realapi.entity.DamageSourcesKey;

public class EchoedEffect extends MobEffect {
    public static final SimpleParticleType PARTICLE = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE, ModParticleIds.ECHOED,
            FabricParticleTypes.simple(false)
    );

    public static final Holder<MobEffect> HOLDER = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT, ModMobEffectIds.ECHOED,
            new EchoedEffect(MobEffectCategory.NEUTRAL, 0x034150)
    );

    public static final DamageSourcesKey<DamageSource> DAMAGE = DamageSourcesKey.register(
            (_, sources) -> sources.source(ModDamageTypeIds.ECHOED)
    );

    public EchoedEffect(MobEffectCategory category, int color) {
        super(category, color, PARTICLE);
    }

    public static void register() {
    }
}
