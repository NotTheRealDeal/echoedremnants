package net.ntrdeal.echoedremnants.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.world.level.block.state.BlockState;
import net.ntrdeal.echoedremnants.component.echoed.EchoedComponent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {
    @WrapMethod(method = "getViewBlockingState")
    private static BlockState ntrdeal$echoedRendering(LocalPlayer player, Frustum frustum, Operation<BlockState> original) {
        return EchoedComponent.isEchoed(player) ? null : original.call(player, frustum);
    }
}
