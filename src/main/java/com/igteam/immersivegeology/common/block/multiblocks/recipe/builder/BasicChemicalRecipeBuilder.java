/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.builder;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import com.igteam.immersivegeology.client.helper.IGRecipeBuilder;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.BasicChemicalRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class BasicChemicalRecipeBuilder extends IGRecipeBuilder<BasicChemicalRecipeBuilder>
{
	protected BasicChemicalRecipeBuilder()
	{
		super(BasicChemicalRecipe.SERIALIZER.get());
	}

	public static BasicChemicalRecipeBuilder builder(ItemStack result, FluidStack fluidStack, IngredientWithSize itemInput, SizedFluidIngredient fluidInputA, SizedFluidIngredient fluidInputB, int damage_per_second)
	{
		BasicChemicalRecipeBuilder builder = new BasicChemicalRecipeBuilder().addFluid("fluidResult", fluidStack)
				.addIngredient("itemInput",itemInput);

		if(!result.isEmpty())
		{
			builder.addResult(result);
		}
		if(fluidInputA != null)
		{
			builder.addFluidTag("fluidInputA", fluidInputA);
		}
		if(fluidInputB != null)
		{
			builder.addFluidTag("fluidInputB", fluidInputB);
		}

		builder.addWriter((jsonObject) -> {
			jsonObject.addProperty("damage_per_second", damage_per_second);
		});

		return builder;
	}

	public static BasicChemicalRecipeBuilder builder(IngredientWithSize result, FluidStack fluidStack, IngredientWithSize itemInput, SizedFluidIngredient fluidInputA, SizedFluidIngredient fluidInputB, int damage_per_second)
	{
		BasicChemicalRecipeBuilder builder = new BasicChemicalRecipeBuilder().addFluid("fluidResult", fluidStack)
				.addIngredient("itemInput",itemInput);

		builder.addResult(result);

		if(fluidInputA != null)
		{
			builder.addFluidTag("fluidInputA", fluidInputA);
		}
		if(fluidInputB != null)
		{
			builder.addFluidTag("fluidInputB", fluidInputB);
		}

		builder.addWriter((jsonObject) -> {
			jsonObject.addProperty("damage_per_second", damage_per_second);
		});

		return builder;
	}

	public static BasicChemicalRecipeBuilder builder(ItemStack result, FluidStack fluidStack, IngredientWithSize itemInput, SizedFluidIngredient fluidInputA, SizedFluidIngredient fluidInputB)
	{
		BasicChemicalRecipeBuilder builder = new BasicChemicalRecipeBuilder().addFluid("fluidResult", fluidStack)
				.addIngredient("itemInput",itemInput);

		if(!result.isEmpty())
		{
			builder.addResult(result);
		}
		if(fluidInputA != null)
		{
			builder.addFluidTag("fluidInputA", fluidInputA);
		}
		if(fluidInputB != null)
		{
			builder.addFluidTag("fluidInputB", fluidInputB);
		}
		return builder;
	}
}
