package net.ntrdeal.echoedremnants;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import net.ntrdeal.echoedremnants.block.ModBlocks;
import net.ntrdeal.echoedremnants.component.ModComponents;
import net.ntrdeal.echoedremnants.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoedRemnants implements ModInitializer {
	public static final String MOD_ID = "echoedremnants";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModItems.register();
		ModComponents.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}