/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.recipe;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import com.igteam.immersivegeology.common.block.helper.OreRichness;
import com.igteam.immersivegeology.core.material.data.enums.*;
import com.igteam.immersivegeology.core.material.data.types.MaterialMineral;
import malte0811.dualcodecs.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import java.util.Locale;

public class IGGeoSerializer extends IERecipeSerializer<IGGeoRecipe>
{
    public static final DualMapCodec<RegistryFriendlyByteBuf, IGGeoRecipe> CODECS = DualCompositeMapCodecs.composite(
            DualCodecs.INT.fieldOf("material_type"), r -> r.material instanceof MaterialMineral?0:1,
            DualCodecs.INT.fieldOf("material_index"), IGGeoSerializer::materialIndex,
            (type, index) -> new IGGeoRecipe(switch(type) {
                case 0 -> MineralEnum.values()[index].instance();
                case 1 -> MetalEnum.values()[index].instance();
                default -> throw new IllegalArgumentException("Unknown geology material type: "+type);
            })
    );

    private static int materialIndex(IGGeoRecipe recipe)
    {
        String name = recipe.material.getName();
        name = name.substring(0, 1).toUpperCase(Locale.ROOT)+name.substring(1);
        return recipe.material instanceof MaterialMineral?MineralEnum.valueOf(name).ordinal():MetalEnum.valueOf(name).ordinal();
    }

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, IGGeoRecipe> codecs()
    {
        return CODECS;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(MineralEnum.Unobtania.getOreBlock(StoneEnum.MCStone, OreRichness.NORMAL).asIGBlock());
    }
}
