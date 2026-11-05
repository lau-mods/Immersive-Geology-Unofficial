/*
 * Muddykat
 * Copyright (c) 2024
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GeothermalExchangerRecipe extends MultiblockRecipe
{
	public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<GeothermalExchangerRecipe>> SERIALIZER;
	public static final CachedRecipeList<GeothermalExchangerRecipe> RECIPES = new CachedRecipeList<>(IGRecipeTypes.GEOTHERMAL_EXCHANGER);
	public final Lazy<FluidStack> fluidOutput;
	public final SizedFluidIngredient fluidIn;
	Lazy<Integer> totalProcessEnergy;
	Lazy<Integer> totalProcessTime;

	public <T extends Recipe<?>> GeothermalExchangerRecipe(SizedFluidIngredient fluidInput, Lazy<FluidStack> fluid_output, int energy, int time)
	{
		super(TagOutput.EMPTY, IGRecipeTypes.GEOTHERMAL_EXCHANGER, 1, 0, () -> new RecipeMultiplier(() -> 1, () -> 1));
		this.fluidIn = fluidInput;
		totalProcessEnergy = Lazy.of(() -> energy);
		totalProcessTime = Lazy.of(() -> time);
		this.fluidOutput = fluid_output;
	}

	public static List<GeothermalExchangerRecipe> findAllValidRecipes(Level level, FluidStack input)
	{
		List<GeothermalExchangerRecipe> valid = new ArrayList<>();
		for(GeothermalExchangerRecipe recipe : RECIPES.getRecipes(level).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList())
			if(recipe.fluidIn.test(input)) valid.add(recipe);
		return valid;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER.get();
	}

	@Override
	public int getTotalProcessEnergy()
	{
		return totalProcessEnergy.get();
	}

	@Override
	public int getTotalProcessTime()
	{
		return totalProcessTime.get();
	}

	public static RecipeHolder<GeothermalExchangerRecipe> findRecipe(Level level, FluidStack input)
	{
		for(RecipeHolder<GeothermalExchangerRecipe> recipe : RECIPES.getRecipes(level))
			if(recipe.value().fluidIn.test(input))
				return recipe;
		return null;
	}

	public static RecipeHolder<GeothermalExchangerRecipe> findRecipe(Level level, FluidStack input, @Nullable RecipeHolder<GeothermalExchangerRecipe> hint)
	{
		if (input.isEmpty())
			return null;
		if (hint != null && hint.value().matches(input))
			return hint;
		for(RecipeHolder<GeothermalExchangerRecipe> recipe : RECIPES.getRecipes(level))
			if(recipe.value().fluidIn.test(input))
				return recipe;
		return null;
	}

	private boolean matches(FluidStack input)
	{
		return this.fluidIn.test(input);
	}

	@Override
	protected IERecipeSerializer<?> getIESerializer()
	{
		return SERIALIZER.get();
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 1;
	}

	public boolean isCooling()
	{
		return java.util.Arrays.stream(fluidIn.getFluids()).findFirst().orElse(FluidStack.EMPTY).getFluid().getFluidType().getTemperature() > fluidOutput.get().getFluid().getFluidType().getTemperature();
	}
}
