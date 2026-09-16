package net.ntrdeal.echoedremnants.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.ntrdeal.echoedremnants.component.echoed.EchoedComponent;
import net.ntrdeal.echoedremnants.component.echoed.EchoedEffect;
import net.ntrdeal.realapi.data.mixin.RealMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements RealMixin<LivingEntity> {
    @Shadow public abstract EntityDimensions getDimensions(Pose pose);
    @Shadow public abstract boolean isDeadOrDying();
    @Shadow public abstract void setHealth(float health);
    @Shadow public abstract float getHealth();

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapMethod(method = "heal")
    private void ntrdeal$echoed(float heal, Operation<Void> original) {
        if (!EchoedComponent.isEchoed(this)) original.call(heal);
    }

    @WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", ordinal = 0))
    private boolean ntrdeal$echoed(LivingEntity entity, ServerLevel level, DamageSource source, float damage, Operation<Boolean> original) {
        if (!EchoedComponent.isEchoed(entity)) return original.call(entity, level, source, damage);
        float checkWidth = this.getDimensions(this.getPose()).width() * 0.8f;
        boolean insideSculk = BlockPos.betweenClosedStream(AABB.ofSize(this.getEyePosition(), checkWidth, 1.0E-6, checkWidth)).map(level::getBlockState).anyMatch(state -> state.is(Blocks.SCULK));
        if (!insideSculk || this.isDeadOrDying()) return original.call(entity, level, EchoedEffect.DAMAGE.get(entity), 0.5f);
        this.setHealth(this.getHealth() + 0.1f);
        return false;
    }
}
