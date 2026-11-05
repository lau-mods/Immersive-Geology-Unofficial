/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.builder;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import com.igteam.immersivegeology.client.helper.IGRecipeBuilder;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.GeothermalExchangerRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class GeothermalExchangerRecipeBuilder extends IGRecipeBuilder<GeothermalExchangerRecipeBuilder>
{
	protected GeothermalExchangerRecipeBuilder()
	{
		super(GeothermalExchangerRecipe.SERIALIZER.get());
	}

	public static GeothermalExchangerRecipeBuilder builder(FluidStack fluid_out)
	{
		return new GeothermalExchangerRecipeBuilder().addFluid("fluidResult", fluid_out);
	}

	public GeothermalExchangerRecipeBuilder addInput(SizedFluidIngredient fluidTag)
	{
		return addFluidTag(generateSafeInputKey(), fluidTag);
	}

	public GeothermalExchangerRecipeBuilder addInput(TagKey<Fluid> fluidTag, int amount)
	{
		return addFluidTag(generateSafeInputKey(), fluidTag, amount);
	}

}
