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
import com.igteam.immersivegeology.common.block.multiblocks.recipe.*;
import com.igteam.immersivegeology.core.registration.IGMultiblockProvider;
import malte0811.dualcodecs.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public class BasicChemicalRecipeSerializer extends IERecipeSerializer<BasicChemicalRecipe>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, BasicChemicalRecipe> CODECS = DualCompositeMapCodecs.composite(
        IngredientWithSize.CODECS.fieldOf("itemInput"), r -> r.itemInput,
        IEDualCodecs.SIZED_FLUID_INGREDIENT.optionalFieldOf("fluidInputA"), r -> r.fluidIn.stream().skip(0).findFirst(),
        IEDualCodecs.SIZED_FLUID_INGREDIENT.optionalFieldOf("fluidInputB"), r -> r.fluidIn.stream().skip(1).findFirst(),
        TagOutput.CODECS.optionalFieldOf("result", TagOutput.EMPTY), r -> r.itemOutput,
        IEDualCodecs.FLUID_STACK.fieldOf("fluidResult"), r -> r.fluidOutput,
        DualCodecs.INT.fieldOf("damage_per_second"), r -> r.getDamagePerTick(),
        DualCodecs.INT.fieldOf("energy"), r -> r.getTotalProcessEnergy(),
        DualCodecs.INT.fieldOf("time"), r -> r.getTotalProcessTime(),
        (item, a, b, out, fluid, damage, energy, time) -> new BasicChemicalRecipe(item, new LinkedHashSet<>(java.util.stream.Stream.of(a, b).flatMap(Optional::stream).toList()), out, fluid, damage, energy, time)
    );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, BasicChemicalRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return IGMultiblockProvider.SMALL_CHEMICAL_REACTOR.iconStack();
    }
}
