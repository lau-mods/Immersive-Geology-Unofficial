/*
 * Muddykat
 * Copyright (c) 2024
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.core.material.data.enums;

import com.igteam.immersivegeology.core.lib.IGLib;
import com.igteam.immersivegeology.core.material.data.stone.compat.adastra.*;
import com.igteam.immersivegeology.core.material.data.stone.vanilla.*;
import com.igteam.immersivegeology.core.material.data.types.MaterialStone;
import com.igteam.immersivegeology.core.material.helper.flags.ModFlags;
import com.igteam.immersivegeology.core.material.helper.material.IStoneType;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum StoneEnum implements IStoneType {
    //===== Minecraft Stones =====\\
    MCStone(new MaterialVanilla()),
    MCDeepslate(new MaterialMCDeepslate()),
    MCAndesite(new MaterialMCAndesite()),
    MCDiorite(new MaterialMCDiorite()),
    MCGranite(new MaterialMCGranite()),
    MCBasalt(new MaterialMCBasalt()),
    MCEndStone(new MaterialMCEndStone()),
    MCNetherrack(new MaterialMCNetherrack()),
    MCDripstone(new MaterialMCDripstone()),
    MCSandstone(new MaterialMCSandstone());

    //===== Minecraft Sands =====\\
    //Sand(new MaterialSand()),

    //===== Beyond Earth / Ad Astra =====\\
    //MoonStone(new MaterialMoonStone()),
    //MarsStone(new MaterialMarsStone()),
    //MercuryStone(new MaterialMercuryStone()),
    //VenusStone(new MaterialVenusStone()),
    //GlacioStone(new MaterialGlacioStone());

    public static final Codec<StoneEnum> CODEC = Codec.STRING.xmap(StoneEnum::valueOf, Enum::name);
    private static final Map<String, StoneEnum> stoneEnumMap = new HashMap<>();

    static {
        for (StoneEnum stoneEnum : values()) {
            stoneEnumMap.put(stoneEnum.name().toLowerCase(Locale.ROOT), stoneEnum);
        }
    }
    private final MaterialStone material;
    StoneEnum(MaterialStone m){
        this.material = m;
    }
    /**
     * Resolved stone type per block, so the name matching below runs once for each block the world contains
     * rather than once for every block world generation samples.
     * <p>
     * Blocks are registry singletons and the match depends only on the block's description id, so a result is
     * good for the lifetime of the game. Empty means the block is not a stone type Immersive Geology knows.
     */
    private static final Map<Block, Optional<StoneEnum>> worldStateCache = new ConcurrentHashMap<>();

    public static StoneEnum selectWorldState(BlockState stoneState) {
        Block block = stoneState.getBlock();
        Optional<StoneEnum> cached = worldStateCache.get(block);
        if(cached==null)
        {
            cached = worldStateCache.computeIfAbsent(block, b -> Optional.ofNullable(resolveWorldState(b)));
        }
        return cached.orElse(null);
    }

    private static StoneEnum resolveWorldState(Block block) {
        try
        {
            String name = block.getDescriptionId().toLowerCase(Locale.ROOT);
            String stoneName = capitalizeFirstLetter(name.substring(name.lastIndexOf('.')+1));

            // Check for Minecraft stones first
            if(name.contains(ModFlags.MINECRAFT.getName()))
            {
                return stoneEnumMap.get("mc"+stoneName.toLowerCase(Locale.ROOT));
            }

            // Try direct lookup of the stone name
            StoneEnum result = stoneEnumMap.get(stoneName.toLowerCase(Locale.ROOT));
            if(result!=null)
            {
                return result;
            }
        } catch(Exception ex) {
            IGLib.IG_LOGGER.warn("Unable to find matching stone type for ore {}", ex.getMessage());
        }

        return null;
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return input as is if it's null or empty
        }
        // Convert first character to uppercase and concatenate with the rest of the string
        return input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1);
    }

    @Override
    public MaterialStone instance() {
        return material;
    }

    @Override
    public int index() {
        return ordinal();
    }

    public List<TargetBlockState> getTargets(MineralEnum mineral)
	{
        return instance().getTargets(mineral);
	}

    public boolean isWorldState(BlockState stoneState)
    {
        return stoneState.getBlock().getName().getString().equalsIgnoreCase(material.getName());
    }



    public boolean isStoneTypeValid()
    {
        List<ModFlags> list = getFlags().stream().filter(f -> f instanceof ModFlags).map(flag -> (ModFlags) flag).toList();
        if(list.isEmpty()) return true;

        boolean pass = false;
        for(ModFlags flag : list)
        {
            pass = flag.isStrictlyLoaded();
        }

        return pass;
    }

    public boolean isVanilla()
    {
        return getFlags().contains(ModFlags.MINECRAFT);
    }

    /**
     * Derived from the mod flags, exactly as the registry key builder used to derive it inline. Last flag wins,
     * which is the behaviour the existing block ids were generated with.
     */
    @Override
    public String getRegistryPrefix()
    {
        String prefix = "";
        for(ModFlags modflag : ModFlags.values())
        {
            if(hasFlag(modflag)) prefix = modflag.name().toLowerCase(Locale.ROOT)+"_";
        }
        return prefix;
    }

    @Override
    public Set<ResourceLocation> getDimensions()
    {
        return instance().getDimensions();
    }

    /** Whether this is a built-in Minecraft host rock. */
    @Override
    public boolean declaresPresence()
    {
        return isVanilla();
    }
}
