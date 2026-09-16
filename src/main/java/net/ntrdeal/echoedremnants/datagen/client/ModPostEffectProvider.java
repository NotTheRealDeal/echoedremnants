package net.ntrdeal.echoedremnants.datagen.client;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.client.renderer.PostChainConfig;
import net.minecraft.client.renderer.UniformValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModPostEffectProvider extends FabricCodecDataProvider<PostChainConfig> {
    private static final Identifier MAIN_TARGET = Identifier.withDefaultNamespace("main");
    private static final Identifier PING_TARGET = EchoedRemnants.id("ping");
    private static final Identifier PONG_TARGET = EchoedRemnants.id("pong");
    private static final List<PostChainConfig.Input> MAIN_INPUT = List.of(new PostChainConfig.TargetInput(
            "In", MAIN_TARGET, false, true
    ));
    private static final List<PostChainConfig.Input> PING_INPUT = List.of(new PostChainConfig.TargetInput(
            "In", PING_TARGET, false, true
    ));
    private static final List<PostChainConfig.Input> PONG_INPUT = List.of(new PostChainConfig.TargetInput(
            "In", PONG_TARGET, false, true
    ));

    private static final Identifier QUAD_CORE = Identifier.withDefaultNamespace("core/screenquad");
    private static final Identifier BLUR_POST = Identifier.withDefaultNamespace("post/box_blur");
    private static final Identifier COLOR_POST = Identifier.withDefaultNamespace("post/color_convolve");

    public ModPostEffectProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, PackOutput.Target.RESOURCE_PACK, "post_effect", PostChainConfig.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, PostChainConfig> provider, HolderLookup.Provider lookup) {
        PostChainConfig.Pass pong_blur = new PostChainConfig.Pass(QUAD_CORE, BLUR_POST, PING_INPUT, PONG_TARGET, Map.of(
                "BlurConfig", List.of(new UniformValue.Vec2Uniform(new Vector2f(0f, 1f)), new UniformValue.FloatUniform(0f))
        ));

        PostChainConfig.Pass ping_blur = new PostChainConfig.Pass(QUAD_CORE, BLUR_POST, PONG_INPUT, PING_TARGET, Map.of(
                "BlurConfig", List.of(new UniformValue.Vec2Uniform(new Vector2f(1f, 0f)), new UniformValue.FloatUniform(0f))
        ));


        provider.accept(EchoedRemnants.id("echoed"), new PostChainConfig(Map.of(
                PING_TARGET, new PostChainConfig.InternalTarget(Optional.empty(), Optional.empty(), false, 0),
                PONG_TARGET, new PostChainConfig.InternalTarget(Optional.empty(), Optional.empty(), false, 0)
        ), List.of(
                new PostChainConfig.Pass(QUAD_CORE, BLUR_POST, MAIN_INPUT, PING_TARGET, Map.of(
                        "BlurConfig", List.of(new UniformValue.Vec2Uniform(new Vector2f(1f, 0f)), new UniformValue.FloatUniform(0f))
                )), pong_blur, ping_blur, pong_blur, ping_blur, pong_blur,
                new PostChainConfig.Pass(QUAD_CORE, COLOR_POST, PONG_INPUT, MAIN_TARGET, Map.of(
                        "ColorConfig", List.of(
                                new UniformValue.Vec3Uniform(new Vector3f(0.5f, 0f, 0.3f)),
                                new UniformValue.Vec3Uniform(new Vector3f(0.5f, 0.25f, 0.3f)),
                                new UniformValue.Vec3Uniform(new Vector3f(0.5f, 0.15f, 0.4f))
                        )
                ))
        )));
    }

    @Override public String getName() {return "post_effect";}
}
