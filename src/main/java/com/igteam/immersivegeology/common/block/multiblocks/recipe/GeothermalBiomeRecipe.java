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
import blusunrize.immersiveengineering.api.utils.FastEither;
import com.igteam.immersivegeology.core.registration.IGRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeothermalBiomeRecipe extends IESerializableRecipe
{
	public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<GeothermalBiomeRecipe>> SERIALIZER;
	public static final CachedRecipeList<GeothermalBiomeRecipe> RECIPES;
	public final FastEither<net.minecraft.resources.ResourceKey<Biome>, List<TagKey<Biome>>> biomes;
	private final int min_heat;
	private final int max_heat;

	public GeothermalBiomeRecipe(net.minecraft.resources.ResourceKey<Biome> biome, int min_heat, int max_heat) {
		super(TagOutput.EMPTY, IGRecipeTypes.GEOTHERMAL_EXCHANGER_BIOME);
		this.biomes = FastEither.left(biome);
		this.min_heat = min_heat;
		this.max_heat = max_heat;
	}

	public GeothermalBiomeRecipe(List<TagKey<Biome>> biomes, int min_heat, int max_heat) {
		super(TagOutput.EMPTY, IGRecipeTypes.GEOTHERMAL_EXCHANGER_BIOME);
		this.biomes = FastEither.right(biomes);
		this.min_heat = min_heat;
		this.max_heat = max_heat;
	}

	public List<TagKey<Biome>> getBiomes(Level level) {
		return this.biomes.map(
				biome -> {
					var holderOpt = level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BIOME).getHolder(biome);
					return holderOpt.map(biomeHolder -> biomeHolder.tags()
							.collect(Collectors.toList())).orElse(Collections.emptyList());
				},
				Function.identity()
		);
	}

	public static GeothermalBiomeRecipe findRecipe(Level level, TagKey<Biome> biome)
	{
		for(GeothermalBiomeRecipe recipe : RECIPES.getRecipes(level).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList())
			if(recipe.getBiomes(level).contains(biome))
				return recipe;
		return null;
	}

	public int getMinHeat() {
		return min_heat;
	}

	public int getMaxHeat() {
		return max_heat;
	}

	protected IERecipeSerializer<?> getIESerializer() {
		return SERIALIZER.get();
	}

	@Nonnull
	public ItemStack getResultItem(@NotNull HolderLookup.Provider access) {
		return ItemStack.EMPTY;
	}

	static {
		RECIPES = new CachedRecipeList<>(IGRecipeTypes.GEOTHERMAL_EXCHANGER_BIOME);
	}
}
