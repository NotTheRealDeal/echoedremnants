package net.ntrdeal.echoedremnants.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.realapi.util.registry.TagCreator;

public class ModItemTags {
    private static final TagCreator<Item> CREATOR = TagCreator.of(Registries.ITEM, EchoedRemnants::id);

    public static final TagKey<Item> REPAIRS_ROSE_GOLD_ARMOR = CREATOR.create("repairs_rose_gold_armor");
}
