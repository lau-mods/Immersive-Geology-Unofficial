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
import net.neoforged.neoforge.fluids.FluidStack;
import java.util.*;

public class BulkBlastFurnaceRecipeSerializer extends IERecipeSerializer<BulkBlastFurnaceRecipe>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, BulkBlastFurnaceRecipe> CODECS = DualCompositeMapCodecs.composite(
        IngredientWithSize.CODECS.optionalFieldOf("ore"), r -> Optional.ofNullable(r.oreInput),
        IEDualCodecs.SIZED_FLUID_INGREDIENT.optionalFieldOf("melt_input"), r -> Optional.ofNullable(r.meltInput),
        IEDualCodecs.FLUID_STACK.fieldOf("melt"), r -> r.meltPerUnit,
        IEDualCodecs.FLUID_STACK.optionalFieldOf("rich_melt", FluidStack.EMPTY), r -> r.richMelt,
        DualCodecs.FLOAT.fieldOf("coke_ratio"), r -> r.cokeRatio,
        DualCodecs.FLOAT.optionalFieldOf("rich_coke_ratio", 0f), r -> r.richCokeRatio,
        DualCodecs.FLOAT.fieldOf("flux_ratio"), r -> r.fluxRatio,
        DualCodecs.FLOAT.fieldOf("min_yield"), r -> r.minYield,
        DualCodecs.FLOAT.fieldOf("max_yield"), r -> r.maxYield,
        DualCodecs.INT.fieldOf("heat"), r -> r.getHeatRequired(),
        DualCodecs.INT.fieldOf("time"), r -> r.getTotalProcessTime(),
        (ore, input, melt, richMelt, coke, richCoke, flux, min, max, heat, time) -> new BulkBlastFurnaceRecipe(ore.orElse(null), input.orElse(null), melt, richMelt, coke, richCoke, flux, min, max, heat, time)
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, BulkBlastFurnaceRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IGMultiblockProvider.BULK_BLAST_FURNACE.iconStack();
    }
}
