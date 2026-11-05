/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.api.crafting.IJEIRecipe;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.igteam.immersivegeology.core.material.GeologyMaterial;
import com.igteam.immersivegeology.core.registration.IGRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class IGGeoRecipe extends IESerializableRecipe implements IJEIRecipe
{
	public final GeologyMaterial material;
	public static DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<IGGeoRecipe>> SERIALIZER;
	public static final CachedRecipeList<IGGeoRecipe> RECIPES = new CachedRecipeList<>(IGRecipeTypes.GEOHINT);
	protected <T extends Recipe<?>> IGGeoRecipe(GeologyMaterial material)
	{
		super(TagOutput.EMPTY, IGRecipeTypes.GEOHINT);
		this.material = material;
	}

	@Override
	protected IERecipeSerializer<?> getIESerializer()
	{
		return SERIALIZER.get();
	}

	@Override
	public @NotNull ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registryAccess)
	{
		return material.getOreIcon();
	}
}
