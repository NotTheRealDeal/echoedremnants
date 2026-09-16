package net.ntrdeal.echoedremnants.component;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.echoedremnants.component.echoed.EchoedComponent;
import net.ntrdeal.echoedremnants.component.echolocation.EcholocationComponent;
import net.ntrdeal.echoedremnants.component.protection.ProtectionComponent;
import net.ntrdeal.echoedremnants.item.component.ModDataComponents;
import net.ntrdeal.realapi.compat.cardinal.CardinalUtil;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {
    public static final ComponentKey<EcholocationComponent> ECHOLOCATION = ComponentRegistry.getOrCreate(EchoedRemnants.id("echolocation"), EcholocationComponent.class);
    public static final ComponentKey<ProtectionComponent> PROTECTION = ComponentRegistry.getOrCreate(EchoedRemnants.id("protection"), ProtectionComponent.class);
    public static final ComponentKey<EchoedComponent> ECHOED = ComponentRegistry.getOrCreate(EchoedRemnants.id("echoed"), EchoedComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ECHOLOCATION, EcholocationComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerFor(LivingEntity.class, PROTECTION, ProtectionComponent::new);
        registry.registerForPlayers(ECHOED, EchoedComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
    }

    public static void register() {
        EcholocationComponent.register();
        EchoedComponent.register();

        ServerEntityEvents.EQUIPMENT_CHANGE.register((entity, slot, _, newStack) -> {
            if (slot.equals(EquipmentSlot.HEAD)) CardinalUtil.ifPresent(ModComponents.ECHOLOCATION, entity, component -> component.setEcholocation(newStack.get(ModDataComponents.ECHOLOCATION)));
            if (slot.equals(EquipmentSlot.CHEST)) CardinalUtil.ifPresent(ModComponents.PROTECTION, entity, component -> component.setProtection(newStack.get(ModDataComponents.ECHO_PROTECTION)));
        });
    }
}
