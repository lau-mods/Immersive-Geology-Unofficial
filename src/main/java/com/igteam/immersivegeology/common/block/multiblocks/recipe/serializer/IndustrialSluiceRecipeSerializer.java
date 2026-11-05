/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.*;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.*;
import malte0811.dualcodecs.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.NonNullList;
import java.util.*;

public class IndustrialSluiceRecipeSerializer extends IERecipeSerializer<IndustrialSluiceRecipe>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, IndustrialSluiceRecipe> CODECS = DualCompositeMapCodecs.composite(
        DualCodecs.INGREDIENT.fieldOf("input"), r -> r.itemIn,
        TagOutput.CODECS.fieldOf("result"), r -> r.itemOutput,
        StackWithChance.CODECS.listOf().map(list -> NonNullList.of(null, list.toArray(StackWithChance[]::new)), list -> List.copyOf(list)).fieldOf("byproducts"), r -> r.getByproducts(),
        DualCodecs.INT.fieldOf("water"), r -> r.getTotalProcessWater(),
        DualCodecs.INT.fieldOf("time"), r -> r.getTotalProcessTime(),
        DualCodecs.INT.fieldOf("energy"), r -> r.getTotalProcessEnergy(),
        IndustrialSluiceRecipe::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, IndustrialSluiceRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Items.APPLE);
    }
}
