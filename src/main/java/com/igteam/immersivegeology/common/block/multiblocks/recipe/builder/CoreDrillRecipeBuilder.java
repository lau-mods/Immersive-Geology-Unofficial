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
import com.igteam.immersivegeology.common.block.multiblocks.recipe.CoreDrillRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class CoreDrillRecipeBuilder extends IGRecipeBuilder<CoreDrillRecipeBuilder>
{
	protected CoreDrillRecipeBuilder()
	{
		super(CoreDrillRecipe.SERIALIZER.get());
	}

	public static CoreDrillRecipeBuilder builder(Fluid fluid_out)
	{
		return new CoreDrillRecipeBuilder().addFluid("fluidResult", new FluidStack(fluid_out, 1));
	}

	public CoreDrillRecipeBuilder addInput(SizedFluidIngredient fluidTag)
	{
		return addFluidTag(generateSafeInputKey(), fluidTag);
	}

	public CoreDrillRecipeBuilder addInput(TagKey<Fluid> fluidTag, int amount)
	{
		return addFluidTag(generateSafeInputKey(), fluidTag, amount);
	}

}
