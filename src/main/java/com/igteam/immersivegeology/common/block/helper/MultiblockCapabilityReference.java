/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class MultiblockCapabilityReference<T>
{
	private final BlockEntity local;
	private final BlockCapability<T, Direction> capability;
	private final Direction side;

	private MultiblockCapabilityReference(BlockEntity local, BlockCapability<T, Direction> capability, Direction side)
	{
		this.local = local;
		this.capability = capability;
		this.side = side;
	}

	@Nullable
	public T getNullable()
	{
		Level level = local.getLevel();
		BlockPos target = local.getBlockPos().relative(side);
		if(level==null || local.isRemoved() || !level.hasChunkAt(target)) return null;
		return level.getCapability(capability, target, side.getOpposite());
	}

	public boolean isPresent()
	{
		return getNullable()!=null;
	}

	public static <T> Map<Direction, MultiblockCapabilityReference<T>> forAllNeighbors(BlockEntity local, BlockCapability<T, Direction> capability)
	{
		Map<Direction, MultiblockCapabilityReference<T>> neighbors = new EnumMap<>(Direction.class);
		for(Direction side : Direction.values()) neighbors.put(side, new MultiblockCapabilityReference<>(local, capability, side));
		return neighbors;
	}
}
