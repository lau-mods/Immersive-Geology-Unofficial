/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.builder;

import com.igteam.immersivegeology.client.helper.IGRecipeBuilder;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.BloomeryFuel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class BloomeryFuelBuilder extends IGRecipeBuilder<BloomeryFuelBuilder> {
	private BloomeryFuelBuilder() {
		super(BloomeryFuel.SERIALIZER.get());
		this.maxResultCount = 0;
	}

	public static BloomeryFuelBuilder builder(ItemLike input) {
		return new BloomeryFuelBuilder().addInput(new ItemLike[]{input});
	}

	public static BloomeryFuelBuilder builder(ItemStack input) {
		return new BloomeryFuelBuilder().addInput(new ItemStack[]{input});
	}

	public static BloomeryFuelBuilder builder(TagKey<Item> input) {
		return new BloomeryFuelBuilder().addInput(Ingredient.of(input));
	}
}
