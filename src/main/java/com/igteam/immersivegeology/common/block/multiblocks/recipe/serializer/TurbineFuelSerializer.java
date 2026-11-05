/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.multiblocks.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.*;
import com.igteam.immersivegeology.common.block.multiblocks.recipe.*;
import com.igteam.immersivegeology.core.registration.IGMultiblockProvider;
import malte0811.dualcodecs.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public class TurbineFuelSerializer extends IERecipeSerializer<TurbineFuel>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, TurbineFuel> CODECS = DualCompositeMapCodecs.composite(
        DualCodecs.RESOURCE_LOCATION.map(id -> net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.FLUID, id), net.minecraft.tags.TagKey::location).fieldOf("fluidTag"), r -> r.fluids,
        DualCodecs.FLOAT.fieldOf("outputRatio"), r -> r.getOutputRatio(),
        DualCodecs.INT.fieldOf("consume_amount"), r -> r.getConsumed(),
        DualCodecs.INT.fieldOf("burnTime"), r -> r.getBurnTime(),
        TurbineFuel::new
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, TurbineFuel> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IGMultiblockProvider.STEAM_TURBINE.iconStack();
    }
}
