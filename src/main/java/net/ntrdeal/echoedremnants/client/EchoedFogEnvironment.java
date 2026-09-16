package net.ntrdeal.echoedremnants.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.ntrdeal.echoedremnants.component.echoed.EchoedComponent;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EchoedFogEnvironment extends FogEnvironment {
    @Override
    public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
        fog.environmentalStart = 11.25f;
        fog.environmentalEnd = 15f;
        fog.skyEnd = 15f;
        fog.cloudEnd = 15f;
    }

    @Override
    public float getModifiedDarkness(LivingEntity entity, float darkness, float partialTickTime) {
        return 1f;
    }

    @Override public boolean providesColor() {return false;}
    @Override public boolean modifiesDarkness() {return true;}

    @Override
    public boolean isApplicable(@Nullable FogType fogType, Entity entity) {
        return EchoedComponent.isEchoed(entity);
    }
}
