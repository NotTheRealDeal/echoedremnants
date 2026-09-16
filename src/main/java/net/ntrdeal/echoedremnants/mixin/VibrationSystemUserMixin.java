package net.ntrdeal.echoedremnants.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.echoedremnants.component.echolocation.EcholocationUser;
import net.ntrdeal.realapi.compat.cardinal.CardinalUtil;
import net.ntrdeal.realapi.data.mixin.RealMixin;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VibrationSystem.User.class)
public interface VibrationSystemUserMixin extends RealMixin<VibrationSystem.User> {
    @WrapMethod(method = "isValidVibration")
    private boolean ntrdeal$protect(Holder<GameEvent> event, GameEvent.Context context, Operation<Boolean> original) {
        return CardinalUtil.returnOr(ModComponents.PROTECTION, context.sourceEntity(),
                component -> !component.protects(event, this instanceof EcholocationUser user ? user.echolocation() : null), true
        ) && original.call(event, context);
    }
}
