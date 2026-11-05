/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.builder;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import com.igteam.immersivegeology.client.helper.IGRecipeBuilder;
import com.google.gson.JsonArray;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.FoundryAlloyRecipe;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.world.level.material.Fluid;

public class FoundryAlloyRecipeBuilder extends IGRecipeBuilder<FoundryAlloyRecipeBuilder>
{
	private final JsonArray inputs = new JsonArray();

	protected FoundryAlloyRecipeBuilder()
	{
		super(FoundryAlloyRecipe.SERIALIZER.get());
		addWriter(json -> json.add("inputs", inputs));
	}

	public static FoundryAlloyRecipeBuilder builder(FluidStack result)
	{
		return new FoundryAlloyRecipeBuilder().addFluid("result", result);
	}

	public FoundryAlloyRecipeBuilder addPart(TagKey<Fluid> fluidTag, int parts)
	{
		return addPart(SizedFluidIngredient.of(fluidTag, parts));
	}

	public FoundryAlloyRecipeBuilder addPart(SizedFluidIngredient part)
	{
		inputs.add(SizedFluidIngredient.FLAT_CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, part).getOrThrow());
		return this;
	}
}
