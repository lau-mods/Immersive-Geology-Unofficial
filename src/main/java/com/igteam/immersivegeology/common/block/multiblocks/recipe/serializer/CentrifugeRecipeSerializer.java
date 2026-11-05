/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.CentrifugeRecipe;
import com.igteam.immersivegeology.core.registration.IGMultiblockProvider;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.Lazy;

public class CentrifugeRecipeSerializer extends IERecipeSerializer<CentrifugeRecipe>
{
	public static final DualMapCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> CODECS = DualCompositeMapCodecs.composite(
			IEDualCodecs.SIZED_FLUID_INGREDIENT.fieldOf("fluid_input"), r -> r.fluidIn,
			TagOutput.CODECS.fieldOf("item_output"), r -> r.itemOutput,
			IEDualCodecs.FLUID_STACK.map(f -> Lazy.of(() -> f), Lazy::get).fieldOf("primary_fluid_out"), r -> r.primaryFluidOutput,
			IEDualCodecs.FLUID_STACK.map(f -> Lazy.of(() -> f), Lazy::get).fieldOf("secondary_fluid_out"), r -> r.secondaryFluidOutput,
			DualCodecs.INT.fieldOf("energy"), r -> r.getTotalProcessEnergy(),
			DualCodecs.INT.fieldOf("time"), r -> r.getTotalProcessTime(),
			CentrifugeRecipe::new
	);

	@Override
	protected DualMapCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> codecs()
	{
		return CODECS;
	}

	@Override
	public ItemStack getIcon()
	{
		return IGMultiblockProvider.CENTRIFUGE.iconStack();
	}
}
