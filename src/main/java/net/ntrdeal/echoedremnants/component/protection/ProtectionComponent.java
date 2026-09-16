package net.ntrdeal.echoedremnants.component.protection;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.echoedremnants.component.echolocation.Echolocation;
import net.ntrdeal.realapi.compat.cardinal.EntityComponent;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.TransientComponent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

@SuppressWarnings("UnstableApiUsage")
public class ProtectionComponent implements EntityComponent<ProtectionComponent>, TransientComponent, AutoSyncedComponent {
    public static final EchoProtection NO_PROTECTION = EchoProtection.of(false, false);

    private final LivingEntity entity;

    private EchoProtection protection = NO_PROTECTION;

    public ProtectionComponent(LivingEntity entity) {
        this.entity = entity;
    }

    public boolean protects(Holder<GameEvent> holder, @Nullable Echolocation echolocation) {
        if (echolocation != null && echolocation.isActive()) return this.protection.protectsEcholocation() && !echolocation.bypassProtection();
        else return this.protection.isProtected(holder);
    }

    public boolean protectsEffect() {return this.protection.protectsEffect();}

    public void setProtection(@Nullable EchoProtection protection) {
        this.protection = protection != null ? protection : NO_PROTECTION;
        if (!this.isClient()) this.sync();
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        EchoProtection.STREAM_CODEC.encode(buf, this.protection);
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        this.setProtection(EchoProtection.STREAM_CODEC.decode(buf));
    }

    @Override public LivingEntity entity() {return this.entity;}
    @Override public ComponentKey<ProtectionComponent> key() {return ModComponents.PROTECTION;}
}
