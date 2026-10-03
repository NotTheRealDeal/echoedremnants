package net.ntrdeal.echoedremnants.reference;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.realapi.util.registry.ResourceCreator;

public class ModItemIds {
    private static final ResourceCreator<Item> CREATOR = ResourceCreator.of(Registries.ITEM, EchoedRemnants::id);

    public static final ResourceKey<Item> ROSE_GOLD_NUGGET = CREATOR.create("rose_gold_nugget");
    public static final ResourceKey<Item> ROSE_GOLD_INGOT = CREATOR.create("rose_gold_ingot");
    public static final ResourceKey<Item> ROSE_GOLD_PENDANT = CREATOR.create("rose_gold_pendant");
    public static final ResourceKey<Item> ROSE_GOLD_MONOCLE = CREATOR.create("rose_gold_monocle");
}
