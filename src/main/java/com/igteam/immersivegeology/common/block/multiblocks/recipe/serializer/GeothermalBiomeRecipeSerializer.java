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
import java.util.*;

public class GeothermalBiomeRecipeSerializer extends IERecipeSerializer<GeothermalBiomeRecipe>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, GeothermalBiomeRecipe> CODECS = DualCompositeMapCodecs.composite(
        DualCodecs.resourceKey(net.minecraft.core.registries.Registries.BIOME).optionalFieldOf("biome"), r -> r.biomes.isLeft()?Optional.of(r.biomes.leftNonnull()):Optional.empty(),
        DualCodecs.RESOURCE_LOCATION.map(id -> net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BIOME, id), net.minecraft.tags.TagKey::location).listOf().optionalFieldOf("biome_tags", List.of()), r -> r.biomes.isLeft()?List.of():r.biomes.rightNonnull(),
        DualCodecs.INT.fieldOf("min_heat"), r -> r.getMinHeat(),
        DualCodecs.INT.fieldOf("max_heat"), r -> r.getMaxHeat(),
        (biome, tags, min, max) -> biome.map(key -> new GeothermalBiomeRecipe(key, min, max)).orElseGet(() -> new GeothermalBiomeRecipe(tags, min, max))
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, GeothermalBiomeRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IGMultiblockProvider.GEOTHERMAL_EXCHANGER.iconStack();
    }
}
