package net.ntrdeal.echoedremnants.component.echoed;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.ntrdeal.echoedremnants.block.ModBlocks;
import net.ntrdeal.echoedremnants.block.ShriekerTriggerer;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.echoedremnants.component.protection.ProtectionComponent;
import net.ntrdeal.echoedremnants.item.component.ModDataComponents;
import net.ntrdeal.realapi.compat.cardinal.CardinalUtil;

import java.util.function.Consumer;

public record EchoedFood(int level, int duration, int delay) implements ConsumableListener, TooltipProvider {
    public static final EchoedFood EMPTY = new EchoedFood(0, 600, 0);
    public static final Codec<EchoedFood> CODEC = RecordCodecBuilder.create(food -> food.group(
            Codec.INT.optionalFieldOf("level", EMPTY.level()).forGetter(EchoedFood::level),
            Codec.INT.optionalFieldOf("duration", EMPTY.duration()).forGetter(EchoedFood::duration),
            Codec.INT.optionalFieldOf("delay", EMPTY.delay()).forGetter(EchoedFood::delay)
    ).apply(food, EchoedFood::new));
    public static final StreamCodec<FriendlyByteBuf, EchoedFood> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, EchoedFood::level,
            ByteBufCodecs.VAR_INT, EchoedFood::duration,
            ByteBufCodecs.VAR_INT, EchoedFood::delay,
            EchoedFood::new
    );

    @Override
    public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
        if (level.isClientSide() || CardinalUtil.returnOr(ModComponents.PROTECTION, user, ProtectionComponent::protectsEffect, false)) return;
        CardinalUtil.ifPresent(ModComponents.ECHOED, user, component -> component.addEchoedFood(this));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(Component.literal("Duration: %s | Delay: %s".formatted(
                StringUtil.formatTickDuration(this.duration, context.tickRate()),
                ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(this.delay / 20d)+"s")).withStyle(ChatFormatting.BLUE)
        );
    }

    public static EchoedFood fromLevel(int level) {
        return level == 0 ? EMPTY : new EchoedFood(level,
                (int) Math.round(Math.pow(2, level) * 600),
                (int) Math.round(Math.pow(2, level - 1) * 100)
        );
    }

    public static void register() {
        BlockEvents.USE_ITEM_ON.register((
                stack, state, level, pos,
                player, _, _
        ) -> {
            if (!state.is(Blocks.SCULK_SHRIEKER) || !state.getValue(SculkShriekerBlock.CAN_SUMMON)) return null;

            if (stack.is(Items.ECHO_SHARD) && state.getValue(ModBlocks.SHARDS_PROPERTY) < 4) {
                stack.consume(1, player);
                level.setBlock(
                        pos,
                        state.setValue(ModBlocks.SHARDS_PROPERTY, Math.clamp(state.getValue(ModBlocks.SHARDS_PROPERTY) + 1, 0, 4)),
                        Block.UPDATE_ALL
                );
                return InteractionResult.SUCCESS;
            } else if (!state.getValue(SculkShriekerBlock.SHRIEKING) && stack.has(DataComponents.CONSUMABLE)) {
                EchoedFood food = stack.get(ModDataComponents.ECHOED_FOOD);
                if (food != null && state.getValue(ModBlocks.SHARDS_PROPERTY) <= food.level()) return InteractionResult.FAIL;
                ItemStack newStack = stack.split(1);
                food = fromLevel(food == null ? 0 : food.level() + 1);
                newStack.set(ModDataComponents.ECHOED_FOOD, food);
                Block.popResourceFromFace(level, pos, player.getDirection().getOpposite(), newStack);

                level.playSound(
                        null, pos, SoundEvents.SCULK_BLOCK_SPREAD,
                        SoundSource.BLOCKS, 1f, 1f
                );

                if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof ShriekerTriggerer triggerer) {
                    triggerer.ntrdeal$tryShriek(serverLevel, serverPlayer);
                    state = level.getBlockState(pos);

                    if (level.getRandom().nextFloat() < food.level() * 0.125f) level.setBlock(
                            pos,
                            state.setValue(ModBlocks.SHARDS_PROPERTY, Math.clamp(state.getValue(ModBlocks.SHARDS_PROPERTY) - 1, 0, 4)),
                            Block.UPDATE_ALL
                    );
                }

                return InteractionResult.SUCCESS;
            } else return InteractionResult.FAIL;
        });
    }
}
