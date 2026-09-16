package net.ntrdeal.echoedremnants.component.echolocation;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.echoedremnants.component.echoed.EchoedComponent;
import net.ntrdeal.realapi.compat.cardinal.CardinalUtil;
import net.ntrdeal.realapi.compat.cardinal.EntityComponent;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("UnstableApiUsage")
public class EcholocationComponent implements EntityComponent<EcholocationComponent>, ServerTickingComponent, AutoSyncedComponent, VibrationSystem {
    private static final StreamCodec<ByteBuf, Set<Integer>> ID_SET = ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.collection(HashSet::new));

    public static final Echolocation NO_ECHOLOCATION = new Echolocation(0, 0, false);

    private final Player player;
    private final VibrationSystem.User user;
    private final DynamicGameEventListener<VibrationSystem.Listener> listener;
    private final Map<Entity, Integer> tracking_entities = new HashMap<>();
    private Set<Integer> glowing_entities = new HashSet<>();

    private Data data = new Data();
    private Echolocation echolocation = NO_ECHOLOCATION;

    public EcholocationComponent(Player player) {
        this.player = player;
        this.user = new User();
        this.listener = new DynamicGameEventListener<>(new Listener(this));
    }

    public boolean isGlowing(Entity entity) {
        return this.active() && (this.glowing_entities.contains(entity.getId()) || (
                EchoedComponent.isEchoed(entity) && entity.distanceToSqr(this.entity()) < this.echolocation.rangeSqr()
        ));
    }

    public boolean active() {
        return this.echolocation.isActive();
    }

    public void addEntity(Entity entity) {
        this.tracking_entities.put(entity, entity.tickCount);
        if (this.glowing_entities.add(entity.getId())) this.sync();
    }

    public void setEcholocation(@Nullable Echolocation echolocation) {
        this.echolocation = echolocation == null ? NO_ECHOLOCATION : echolocation;
        if (!this.isClient()) this.sync();
    }

    public void updateListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> consumer) {
        if (this.level() instanceof ServerLevel level) consumer.accept(this.listener, level);
    }

    @Override
    public void serverTick() {
        EcholocationUser.Ticker.tick(this.level(), this.getVibrationData(), this.getVibrationUser());
        if (this.glowing_entities.isEmpty()) return;

        Iterator<Map.Entry<Entity, Integer>> iterator = this.tracking_entities.entrySet().iterator();
        boolean changed = false;

        while (iterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = iterator.next();
            if ((entry.getKey().tickCount - entry.getValue()) > this.echolocation.duration()) {
                changed |= this.glowing_entities.remove(entry.getKey().getId());
                iterator.remove();
            }
        }

        if (changed) this.sync();
    }

    @Override public Data getVibrationData() {return this.data;}
    @Override public VibrationSystem.User getVibrationUser() {return this.user;}

    @Override
    public void writeData(ValueOutput output) {
        output.store("listener", Data.CODEC, this.getVibrationData());
    }

    @Override
    public void readData(ValueInput input) {
        this.data = input.read("listener", Data.CODEC).orElseGet(Data::new);
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        Echolocation.STREAM_CODEC.encode(buf, this.echolocation);
        ID_SET.encode(buf, this.glowing_entities);
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        this.echolocation = Echolocation.STREAM_CODEC.decode(buf);
        this.glowing_entities = ID_SET.decode(buf);
    }

    @Override public Player entity() {return this.player;}
    @Override public ComponentKey<EcholocationComponent> key() {return ModComponents.ECHOLOCATION;}

    public class User implements EcholocationUser {
        private final PositionSource positionSource = new EntityPositionSource(
                EcholocationComponent.this.entity(), EcholocationComponent.this.entity().getEyeHeight()
        );

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean isValidVibration(Holder<GameEvent> event, GameEvent.Context context) {
            return EcholocationUser.super.isValidVibration(event, context) && context.sourceEntity() != EcholocationComponent.this.entity();
        }

        @Override
        public void onReceiveVibration(
                ServerLevel level, BlockPos pos, Holder<GameEvent> event, @Nullable Entity sourceEntity,
                @Nullable Entity projectileOwner, float receivingDistance
        ) {
            if (sourceEntity != null) EcholocationComponent.this.addEntity(sourceEntity);
        }

        @Override
        public Echolocation echolocation() {
            return EcholocationComponent.this.echolocation;
        }
    }

    public static boolean isActive(Object object) {
        return CardinalUtil.returnOr(ModComponents.ECHOLOCATION, object, EcholocationComponent::active, false);
    }

    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, _) -> CardinalUtil.ifPresent(ModComponents.ECHOLOCATION, entity,
                component -> component.updateListener(DynamicGameEventListener::add)
        ));

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, _) -> CardinalUtil.ifPresent(ModComponents.ECHOLOCATION, entity,
                component -> component.updateListener(DynamicGameEventListener::remove)
        ));
    }
}
