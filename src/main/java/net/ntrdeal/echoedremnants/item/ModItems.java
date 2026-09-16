package net.ntrdeal.echoedremnants.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorType;
import net.ntrdeal.echoedremnants.block.ModBlocks;
import net.ntrdeal.echoedremnants.component.echolocation.Echolocation;
import net.ntrdeal.echoedremnants.component.protection.EchoProtection;
import net.ntrdeal.echoedremnants.item.component.ModDataComponents;
import net.ntrdeal.echoedremnants.item.equipment.ModArmorMaterials;
import net.ntrdeal.echoedremnants.reference.ModBlockItemIds;
import net.ntrdeal.echoedremnants.reference.ModItemIds;

public class ModItems {
    public static final Item ROSE_GOLD_NUGGET = Items.registerItem(ModItemIds.ROSE_GOLD_NUGGET);
    public static final Item ROSE_GOLD_INGOT = Items.registerItem(ModItemIds.ROSE_GOLD_INGOT);

    public static final Item ROSE_GOLD_PENDANT = Items.registerItem(
            ModItemIds.ROSE_GOLD_PENDANT, properties -> new Item(properties
                    .humanoidArmor(ModArmorMaterials.ROSE_GOLD, ArmorType.CHESTPLATE)
                    .component(ModDataComponents.ECHO_PROTECTION, EchoProtection.EMPTY)
            )
    );

    public static final Item ROSE_GOLD_MONOCLE = Items.registerItem(
            ModItemIds.ROSE_GOLD_MONOCLE, properties -> new Item(properties
                    .humanoidArmor(ModArmorMaterials.ROSE_GOLD, ArmorType.HELMET)
                    .component(ModDataComponents.ECHOLOCATION, Echolocation.EMPTY)
            )
    );

    public static final Item ROSE_GOLD_BLOCK = Items.registerBlock(ModBlockItemIds.ROSE_GOLD_BLOCK, ModBlocks.ROSE_GOLD_BLOCK);

    public static void register() {
        ModDataComponents.register();

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output ->
                output.insertAfter(Items.NETHERITE_INGOT, ROSE_GOLD_NUGGET, ROSE_GOLD_INGOT)
        );
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output ->
                output.insertAfter(Items.TURTLE_HELMET, ROSE_GOLD_MONOCLE, ROSE_GOLD_PENDANT)
        );
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(output ->
                output.insertAfter(Items.NETHERITE_BLOCK, ModBlocks.ROSE_GOLD_BLOCK)
        );
    }
}
