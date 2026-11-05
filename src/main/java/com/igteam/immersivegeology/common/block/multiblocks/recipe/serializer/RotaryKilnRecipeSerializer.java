/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.*;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.RotaryKilnRecipe;
import com.igteam.immersivegeology.core.registration.IGMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class RotaryKilnRecipeSerializer extends IERecipeSerializer<RotaryKilnRecipe>
{
	public static final DualMapCodec<RegistryFriendlyByteBuf, RotaryKilnRecipe> CODECS = DualCompositeMapCodecs.composite(
			IngredientWithSize.CODECS.fieldOf("input"), r -> r.itemIn,
			TagOutput.CODECS.fieldOf("result"), r -> r.itemOutput,
			DualCodecs.INT.fieldOf("time"), r -> r.getTotalProcessTime(),
			DualCodecs.INT.fieldOf("heat"), r -> r.getHeatRequired(),
			RotaryKilnRecipe::new
	);

	@Override
	protected DualMapCodec<RegistryFriendlyByteBuf, RotaryKilnRecipe> codecs()
	{
		return CODECS;
	}

	@Override
	public ItemStack getIcon()
	{
		return IGMultiblockProvider.ROTARYKILN.iconStack();
	}
}
