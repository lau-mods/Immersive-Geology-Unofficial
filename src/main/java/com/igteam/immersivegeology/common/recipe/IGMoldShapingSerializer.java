/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class IGMoldShapingSerializer implements RecipeSerializer<IGMoldShapingRecipe>
{
	@Override
	public MapCodec<IGMoldShapingRecipe> codec()
	{
		return new ShapelessRecipe.Serializer().codec().xmap(IGMoldShapingSerializer::wrap, recipe -> recipe);
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, IGMoldShapingRecipe> streamCodec()
	{
		return ShapelessRecipe.Serializer.STREAM_CODEC.map(IGMoldShapingSerializer::wrap, recipe -> recipe);
	}

	private static IGMoldShapingRecipe wrap(ShapelessRecipe base)
	{
		return new IGMoldShapingRecipe(
				base.getGroup(), base.category(),
				base.getResultItem(RegistryAccess.EMPTY), base.getIngredients()
		);
	}
}
