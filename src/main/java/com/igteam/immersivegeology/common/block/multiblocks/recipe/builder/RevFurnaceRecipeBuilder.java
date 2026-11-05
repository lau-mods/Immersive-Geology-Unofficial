/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.builder;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import com.igteam.immersivegeology.client.helper.IGRecipeBuilder;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.RevFurnaceRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RevFurnaceRecipeBuilder extends IGRecipeBuilder<RevFurnaceRecipeBuilder>
{
	protected RevFurnaceRecipeBuilder()
	{
		super(RevFurnaceRecipe.SERIALIZER.get());
	}

	public static RevFurnaceRecipeBuilder builder(Item result)
	{
		return new RevFurnaceRecipeBuilder().addResult(result);
	}

	public static RevFurnaceRecipeBuilder builder(ItemStack result)
	{
		return new RevFurnaceRecipeBuilder().addResult(result);
	}

	public static RevFurnaceRecipeBuilder builder(TagKey<Item> result, int count)
	{
		return new RevFurnaceRecipeBuilder().addResult(new IngredientWithSize(result, count));
	}

	public RevFurnaceRecipeBuilder setWasteAmount(int amount)
	{
		return this.addWriter((jsonObject) -> {
			jsonObject.addProperty("waste", amount);
		});
	}
}
