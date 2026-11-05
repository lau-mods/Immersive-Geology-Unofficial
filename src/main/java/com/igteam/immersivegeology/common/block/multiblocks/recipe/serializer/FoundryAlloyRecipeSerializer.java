/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.*;
import com.igteam.immersivegeology.core.registration.IGMultiblockProvider;
import malte0811.dualcodecs.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public class FoundryAlloyRecipeSerializer extends IERecipeSerializer<FoundryAlloyRecipe>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, FoundryAlloyRecipe> CODECS = DualCompositeMapCodecs.composite(
        IEDualCodecs.SIZED_FLUID_INGREDIENT.listOf().fieldOf("inputs"), r -> r.alloyInputs,
        IEDualCodecs.FLUID_STACK.fieldOf("result"), r -> r.alloyOutput,
        DualCodecs.INT.fieldOf("energy"), r -> r.getTotalProcessEnergy(),
        DualCodecs.INT.fieldOf("time"), r -> r.getTotalProcessTime(),
        FoundryAlloyRecipe::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, FoundryAlloyRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IGMultiblockProvider.FOUNDRY.iconStack();
    }
}
