package net.ntrdeal.echoedremnants.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.realapi.compat.cardinal.CardinalUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.level.ServerLevel$EntityCallbacks")
public class ServerLevelEntityCallbacksMixin {
    @Inject(method = "onSectionChange(Lnet/minecraft/world/entity/Entity;)V", at = @At("TAIL"))
    private void ntrdeal$updateListener(Entity entity, CallbackInfo ci) {
        CardinalUtil.ifPresent(ModComponents.ECHOLOCATION, entity, component -> component.updateListener(DynamicGameEventListener::move));
    }
}
