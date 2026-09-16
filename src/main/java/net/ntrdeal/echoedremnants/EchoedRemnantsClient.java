package net.ntrdeal.echoedremnants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.ntrdeal.echoedremnants.client.EchoedFogEnvironment;
import net.ntrdeal.echoedremnants.client.SculkShriekerRenderer;
import net.ntrdeal.echoedremnants.component.echoed.EchoedComponent;
import net.ntrdeal.echoedremnants.component.echoed.EchoedEffect;
import net.ntrdeal.realapi.client.render.RealFogRegistry;
import net.ntrdeal.realapi.client.render.post.RealPostRegistry;

public class EchoedRemnantsClient implements ClientModInitializer {
    public static final Identifier ECHOED_POST_SHADER = EchoedRemnants.id("echoed");

    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(EchoedEffect.PARTICLE, SpellParticle.Provider::new);
        BlockEntityRenderers.register(BlockEntityTypes.SCULK_SHRIEKER, SculkShriekerRenderer::new);
        RealFogRegistry.registerBeforeClass(new EchoedFogEnvironment(), BlindnessFogEnvironment.class);
        RealPostRegistry.register(entity -> EchoedComponent.isEchoed(entity) ? ECHOED_POST_SHADER : null);
    }
}
