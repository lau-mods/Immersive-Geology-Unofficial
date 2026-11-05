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
import com.igteam.immersivegeology.core.registration.IGMultiblockProvider;
import malte0811.dualcodecs.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.Lazy;
import java.util.*;

public class GeothermalConversionRecipeSerializer extends IERecipeSerializer<GeothermalConversionRecipe>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, GeothermalConversionRecipe> CODECS = DualCompositeMapCodecs.composite(
        DualCodecs.registryEntry(net.minecraft.core.registries.BuiltInRegistries.BLOCK).map(block -> Lazy.of(() -> block), Lazy::get).fieldOf("transitionBlock"), r -> r.transitionBlock,
        DualCodecs.INT.fieldOf("blockHeat"), r -> r.blockHeat,
        DualCodecs.registryEntry(net.minecraft.core.registries.BuiltInRegistries.BLOCK).optionalFieldOf("upperBoundBlock"), r -> Optional.ofNullable(r.upperTransition),
        DualCodecs.INT.optionalFieldOf("upperHeat"), r -> Optional.ofNullable(r.upperHeat),
        DualCodecs.registryEntry(net.minecraft.core.registries.BuiltInRegistries.BLOCK).optionalFieldOf("lowerBoundBlock"), r -> Optional.ofNullable(r.lowerTransition),
        DualCodecs.INT.optionalFieldOf("lowerHeat"), r -> Optional.ofNullable(r.lowerHeat),
        (block, heat, upper, upperHeat, lower, lowerHeat) -> new GeothermalConversionRecipe(block, heat, upper.map(b -> com.mojang.datafixers.util.Pair.of(b, upperHeat.orElseThrow())).orElse(null), lower.map(b -> com.mojang.datafixers.util.Pair.of(b, lowerHeat.orElseThrow())).orElse(null))
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, GeothermalConversionRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IGMultiblockProvider.GEOTHERMAL_EXCHANGER.iconStack();
    }
}
