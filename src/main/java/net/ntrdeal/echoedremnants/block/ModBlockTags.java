package net.ntrdeal.echoedremnants.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.realapi.util.registry.TagCreator;

public class ModBlockTags {
    private static final TagCreator<Block> CREATOR = TagCreator.of(Registries.BLOCK, EchoedRemnants::id);

    public static final TagKey<Block> CANNOT_ECHO = CREATOR.create("cannot_echo");
}
