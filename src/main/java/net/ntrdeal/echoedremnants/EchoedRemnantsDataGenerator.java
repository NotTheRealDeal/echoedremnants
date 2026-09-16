package net.ntrdeal.echoedremnants;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.ntrdeal.echoedremnants.datagen.ModDamageTypeProvider;
import net.ntrdeal.echoedremnants.datagen.ModRecipeProvider;
import net.ntrdeal.echoedremnants.datagen.client.ModEquipmentProvider;
import net.ntrdeal.echoedremnants.datagen.client.ModModelProvider;
import net.ntrdeal.echoedremnants.datagen.client.ModParticleProvider;
import net.ntrdeal.echoedremnants.datagen.client.ModPostEffectProvider;
import net.ntrdeal.echoedremnants.datagen.client.language.ModEnglishProvider;
import net.ntrdeal.echoedremnants.datagen.tag.ModBlockTagProvider;
import net.ntrdeal.echoedremnants.datagen.tag.ModDamageTypeTagProvider;
import net.ntrdeal.echoedremnants.datagen.tag.ModItemTagProvider;
import net.ntrdeal.echoedremnants.datagen.tag.ModMobEffectTagProvider;

public class EchoedRemnantsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModDamageTypeTagProvider::new);
		pack.addProvider(ModMobEffectTagProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModDamageTypeProvider::new);

		pack.addProvider(ModEnglishProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModEquipmentProvider::new);
		pack.addProvider(ModPostEffectProvider::new);
		pack.addProvider(ModParticleProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder builder) {
		builder.add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap);
	}
}
