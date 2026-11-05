/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import com.igteam.immersivegeology.core.registration.IGRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TurbineFuel extends IESerializableRecipe
{
	public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<TurbineFuel>> SERIALIZER;
	public static final CachedRecipeList<TurbineFuel> RECIPES;
	public final TagKey<Fluid> fluids;
	private final int burnTime;
	private final int consume_amount;
	private final float outputRatio;

	public TurbineFuel(TagKey<Fluid> fluids, float outputRatio, int consume_amount, int burnTime) {
		super(TagOutput.EMPTY, IGRecipeTypes.TURBINE_FUEL);
		this.fluids = fluids;
		this.burnTime = burnTime;
		this.outputRatio = outputRatio;
		this.consume_amount = consume_amount;
	}

	public List<Fluid> getFluids() {
        return TagUtils.elementStream(BuiltInRegistries.FLUID, fluids).toList();
    }

	public int getBurnTime() {
		return this.burnTime;
	}

	public int getConsumed()
	{
		return consume_amount;
	}

	public float getOutputRatio()
	{
		return outputRatio;
	}

	protected IERecipeSerializer<?> getIESerializer() {
		return SERIALIZER.get();
	}

	@Nonnull
	public ItemStack getResultItem(@NotNull HolderLookup.Provider access) {
		return ItemStack.EMPTY;
	}

	public boolean matches(Fluid in) {
		return in.is(fluids);
	}

	public static TurbineFuel getRecipeFor(Level level, Fluid in, @Nullable TurbineFuel hint) {
		if (hint != null && hint.matches(in)) {
			return hint;
		} else {
			Iterator<TurbineFuel> var3 = RECIPES.getRecipes(level).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList().iterator();

			TurbineFuel fuel;
			do {
				if (!var3.hasNext()) {
					return null;
				}

				fuel = (TurbineFuel)var3.next();
			} while(!fuel.matches(in));

			return fuel;
		}
	}

	public static SortedMap<Component, Integer> getManualFuelList(Level level) {
		SortedMap<Component, Integer> map = new TreeMap<>(Comparator.comparing(Component::getString, Comparator.naturalOrder()));

		for(TurbineFuel recipe : RECIPES.getRecipes(level).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList())
		{
			for(Fluid f : recipe.getFluids())
			{
				map.put(f.getFluidType().getDescription(), recipe.getBurnTime());
			}
		}

		return map;
	}

	static {
		RECIPES = new CachedRecipeList(IGRecipeTypes.TURBINE_FUEL);
	}
}
