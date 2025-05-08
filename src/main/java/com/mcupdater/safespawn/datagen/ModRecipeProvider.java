package com.mcupdater.safespawn.datagen;

import com.mcupdater.safespawn.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
	public ModRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.BEACONBLOCK.get())
				.define('g', Ingredient.of(Tags.Items.GLASS_BLOCKS))
				.define('l', Ingredient.of(Items.LANTERN))
				.define('o', Ingredient.of(Blocks.OBSIDIAN))
				.pattern("ggg")
				.pattern("glg")
				.pattern("ooo")
				.unlockedBy("has_obsidian", has(Items.OBSIDIAN))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blocks.BEACON)
				.requires(Registration.BEACONBLOCK_ITEM)
				.requires(Items.NETHER_STAR)
				.unlockedBy("has_star", has(Items.NETHER_STAR))
				.save(recipeOutput, "upgrade_beacon");

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Registration.SPAWNLANTERNBLOCK, 8)
				.define('s', Ingredient.of(Blocks.STONE_BRICKS))
				.define('l', Ingredient.of(Blocks.REDSTONE_LAMP))
				.pattern("sss")
				.pattern("sls")
				.pattern("sss")
				.unlockedBy("has_lamp", has(Items.REDSTONE_LAMP))
				.save(recipeOutput);
	}
}
