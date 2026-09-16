package net.ntrdeal.echoedremnants.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.ntrdeal.echoedremnants.reference.ModDamageTypeIds;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModDamageTypeProvider extends FabricCodecDataProvider<DamageType> {
    public ModDamageTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, DamageType> provider, HolderLookup.Provider lookup) {
        provider.accept(ModDamageTypeIds.ECHOED.identifier(), new DamageType("echoed", DamageScaling.NEVER, 0f));
    }

    @Override public String getName() {return "damage_types";}

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(ModDamageTypeIds.ECHOED, new DamageType("echoed", DamageScaling.NEVER, 0f));
    }
}
