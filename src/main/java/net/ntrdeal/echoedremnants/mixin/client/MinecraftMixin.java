package net.ntrdeal.echoedremnants.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.realapi.compat.cardinal.CardinalUtil;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public abstract @Nullable Entity getCameraEntity();

    @WrapMethod(method = "shouldEntityAppearGlowing")
    private boolean ntrdeal$vibrating(Entity entity, Operation<Boolean> original) {
        return original.call(entity) || CardinalUtil.returnOr(ModComponents.ECHOLOCATION, this.getCameraEntity(),
                component -> component.isGlowing(entity), false
        );
    }
}
