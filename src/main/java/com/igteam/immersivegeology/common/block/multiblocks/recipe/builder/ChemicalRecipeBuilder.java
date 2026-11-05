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
import com.igteam.immersivegeology.common.block.multiblocks.recipe.ChemicalRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChemicalRecipeBuilder extends IGRecipeBuilder<ChemicalRecipeBuilder>
{
	protected ChemicalRecipeBuilder()
	{
		super(ChemicalRecipe.SERIALIZER.get());
	}

	public static ChemicalRecipeBuilder builder(IngredientWithSize result, FluidStack fluidStack, IngredientWithSize itemInput, SizedFluidIngredient fluidInputA, SizedFluidIngredient fluidInputB, SizedFluidIngredient fluidInputC)
	{
		ChemicalRecipeBuilder builder = new ChemicalRecipeBuilder().addResult(result).addFluid("fluidResult", fluidStack)
				.addIngredient("itemInput",itemInput);

		if(fluidInputA != null)
		{
			builder.addFluidTag("fluidInputA", fluidInputA);
		}
		if(fluidInputB != null)
		{
			builder.addFluidTag("fluidInputB", fluidInputB);
		}
		if(fluidInputC != null)
		{
			builder.addFluidTag("fluidInputC", fluidInputC);
		}
		return builder;
	}

	public static ChemicalRecipeBuilder builder(ItemStack result, FluidStack fluidStack, IngredientWithSize itemInput, SizedFluidIngredient fluidInputA, SizedFluidIngredient fluidInputB, SizedFluidIngredient fluidInputC)
	{
		ChemicalRecipeBuilder builder = new ChemicalRecipeBuilder().addFluid("fluidResult", fluidStack)
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
		if(fluidInputC != null)
		{
			builder.addFluidTag("fluidInputC", fluidInputC);
		}

		return builder;
	}

}
