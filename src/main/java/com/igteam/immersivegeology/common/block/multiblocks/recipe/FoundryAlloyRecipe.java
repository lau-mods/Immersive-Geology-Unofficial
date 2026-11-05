/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.igteam.immersivegeology.core.registration.IGRecipeTypes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FoundryAlloyRecipe extends MultiblockRecipe
{
	public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<FoundryAlloyRecipe>> SERIALIZER;
	public static final CachedRecipeList<FoundryAlloyRecipe> RECIPES = new CachedRecipeList<>(IGRecipeTypes.FOUNDRY_ALLOYING);

	public final List<SizedFluidIngredient> alloyInputs;
	public final FluidStack alloyOutput;
	private final int energyPerUnit;
	private final int time;

	public FoundryAlloyRecipe(List<SizedFluidIngredient> inputs, FluidStack output, int energyPerUnit, int time)
	{
		super(TagOutput.EMPTY, IGRecipeTypes.FOUNDRY_ALLOYING, 1, 0, () -> new RecipeMultiplier(() -> 1, () -> 1));
		validate(inputs, output, energyPerUnit, time);
		this.alloyInputs = List.copyOf(inputs);
		this.alloyOutput = output;
		this.energyPerUnit = energyPerUnit;
		this.time = time;
		this.fluidInputList = this.alloyInputs;
		this.fluidOutputList = List.of(output);
	}

	public int getInputVolumePerUnit()
	{
		int volume = 0;
		for(SizedFluidIngredient input : alloyInputs) volume += input.amount();
		return volume;
	}

	public int getOutputVolumePerUnit()
	{
		return alloyOutput.getAmount();
	}

	public int unitsFrom(List<FluidStack> available, int limit)
	{
		int units = limit;
		for(SizedFluidIngredient input : alloyInputs)
		{
			if(input.amount() <= 0) return 0;
			int stored = 0;
			for(FluidStack fluid : available)
				if(!fluid.isEmpty()&&input.ingredient().test(fluid)) stored += fluid.getAmount();
			units = Math.min(units, stored/input.amount());
			if(units <= 0) return 0;
		}
		return units;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER.get();
	}

	@Override
	protected IERecipeSerializer<?> getIESerializer()
	{
		return SERIALIZER.get();
	}

	@Override
	public int getTotalProcessTime()
	{
		return time;
	}

	@Override
	public int getTotalProcessEnergy()
	{
		return energyPerUnit;
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}
	private static void validate(List<SizedFluidIngredient> inputs, FluidStack output, int energy, int time)
	{
		if(inputs.size() < 2)
			throw new IllegalArgumentException("Foundry alloying recipe  needs at least two fluid inputs to merge");
		for(SizedFluidIngredient input : inputs)
			if(input.amount() <= 0)
				throw new IllegalArgumentException("Foundry alloying recipe  has an input part of zero, every part of the ratio must be above zero");
		if(output.isEmpty()||output.getAmount() <= 0)
			throw new IllegalArgumentException("Foundry alloying recipe  produces no alloy");
		if(energy < 0)
			throw new IllegalArgumentException("Foundry alloying recipe  has a negative energy cost");
		if(time <= 0)
			throw new IllegalArgumentException("Foundry alloying recipe  has a time of "+time+", it must be at least 1 tick");
	}

}
