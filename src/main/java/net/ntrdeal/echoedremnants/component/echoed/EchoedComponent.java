package net.ntrdeal.echoedremnants.component.echoed;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.echoedremnants.component.protection.ProtectionComponent;
import net.ntrdeal.realapi.compat.cardinal.CardinalUtil;
import net.ntrdeal.realapi.compat.cardinal.EntityComponent;
import net.ntrdeal.realapi.entity.event.RealLivingEntityEvents;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

@SuppressWarnings("UnstableApiUsage")
public class EchoedComponent implements EntityComponent<EchoedComponent>, ServerTickingComponent, AutoSyncedComponent {
    private final Player player;

    private EchoedFood food = null;
    private boolean echoed = false;

    public EchoedComponent(Player player) {
        this.player = player;
    }

    public boolean echoed() {return this.echoed;}

    private void setEchoed(boolean echoed) {
        if (this.echoed == echoed) return;
        this.echoed = echoed;
        if (!this.isClient()) this.sync();
    }

    public void addEchoedFood(EchoedFood food) {
        if (this.food == null || this.food.level() < food.level()) this.food = food;
    }

    @Override
    public void serverTick() {
        this.setEchoed(this.entity().hasEffect(EchoedEffect.HOLDER) && !CardinalUtil.returnOr(
                ModComponents.PROTECTION, this.entity(), ProtectionComponent::protectsEffect, false
        ));

        if (this.food == null) return;
        if (this.food.delay() > 0) this.food = new EchoedFood(this.food.level(), this.food.duration(), this.food.delay() -1);
        else {
            this.entity().addEffect(new MobEffectInstance(EchoedEffect.HOLDER, this.food.duration(), this.food.level(), false, false));
            this.food = null;
        }
    }

    @Override
    public void readData(ValueInput input) {
        this.food = input.read("food", EchoedFood.CODEC).orElse(null);
    }

    @Override
    public void writeData(ValueOutput output) {
        output.storeNullable("food", EchoedFood.CODEC, this.food);
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeBoolean(this.echoed);
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        this.setEchoed(buf.readBoolean());
    }

    @Override public LivingEntity entity() {return this.player;}
    @Override public ComponentKey<EchoedComponent> key() {return ModComponents.ECHOED;}

    public static boolean isEchoed(Object object) {
        return CardinalUtil.returnOr(ModComponents.ECHOED, object, EchoedComponent::echoed, false);
    }

    public static void register() {
        EchoedEffect.register();
        EchoedFood.register();

        RealLivingEntityEvents.INVULNERABLE_TO.register((entity, source) ->
                source.is(DamageTypes.SONIC_BOOM) && CardinalUtil.returnOr(ModComponents.PROTECTION, entity, ProtectionComponent::protectsEffect, false)
        );
    }
}
