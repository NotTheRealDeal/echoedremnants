package net.ntrdeal.echoedremnants.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.ntrdeal.echoedremnants.EchoedRemnants;
import net.ntrdeal.echoedremnants.block.ModBlocks;
import net.ntrdeal.echoedremnants.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookup, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                this.shapeless(RecipeCategory.MISC, ModItems.ROSE_GOLD_INGOT, 2)
                        .requires(ConventionalItemTags.GOLD_INGOTS)
                        .requires(ConventionalItemTags.COPPER_INGOTS)
                        .unlockedBy(RecipeProvider.getHasName(Items.GOLD_INGOT), this.has(Items.GOLD_INGOT))
                        .save(this.output, ResourceKey.create(Registries.RECIPE, EchoedRemnants.id("alloy_rose_gold_ingot")));

                this.nineBlockStorageRecipesWithCustomPacking(
                        RecipeCategory.MISC, ModItems.ROSE_GOLD_NUGGET,
                        RecipeCategory.MISC, ModItems.ROSE_GOLD_INGOT,
                        "rose_gold_ingot_from_nuggets", "rose_gold_ingot"
                );

                this.nineBlockStorageRecipesWithCustomPacking(
                        RecipeCategory.MISC, ModItems.ROSE_GOLD_INGOT,
                        RecipeCategory.MISC, ModBlocks.ROSE_GOLD_BLOCK,
                        "rose_gold_block_from_ingot", "rose_gold_ingot"
                );

                String echoShardName = RecipeProvider.getHasName(Items.ECHO_SHARD);
                Criterion<InventoryChangeTrigger.TriggerInstance> echoShardCriterion = this.has(Items.ECHO_SHARD);

                this.shaped(RecipeCategory.COMBAT, ModItems.ROSE_GOLD_PENDANT)
                        .pattern("RER")
                        .pattern(" R ")
                        .define('R', ModItems.ROSE_GOLD_INGOT)
                        .define('E', Items.ECHO_SHARD)
                        .unlockedBy(echoShardName, echoShardCriterion)
                        .save(output);

                this.shaped(RecipeCategory.COMBAT, ModItems.ROSE_GOLD_MONOCLE)
                        .pattern(" R ")
                        .pattern(" ER")
                        .pattern(" R ")
                        .define('R', ModItems.ROSE_GOLD_INGOT)
                        .define('E', Items.ECHO_SHARD)
                        .unlockedBy(echoShardName, echoShardCriterion)
                        .save(output);
            }
        };
    }

    @Override public String getName() {return "recipes";}
}
