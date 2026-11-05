/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.gametest.tests;

import com.igteam.immersivegeology.common.block.entity.cable.IGEnergyPipeEntity;
import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler.IMultiblock;
import com.igteam.immersivegeology.common.block.multiblocks.IGTemplateMultiblock;
import com.igteam.immersivegeology.core.material.data.enums.ChemicalEnum;
import com.igteam.immersivegeology.core.material.data.enums.MetalEnum;
import com.igteam.immersivegeology.core.material.data.enums.MineralEnum;
import com.igteam.immersivegeology.core.material.data.enums.MiscEnum;
import com.igteam.immersivegeology.core.material.data.enums.StoneEnum;
import com.igteam.immersivegeology.core.material.helper.flags.BlockCategoryFlags;
import com.igteam.immersivegeology.core.material.helper.material.MaterialInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CapabilityLifecycleTests
{

	private static final List<BlockCapability<?, Direction>> WATCHED = List.of(
			Capabilities.FluidHandler.BLOCK, Capabilities.ItemHandler.BLOCK, Capabilities.EnergyStorage.BLOCK
	);

	public static List<TestFunction> all()
	{
		List<TestFunction> tests = new ArrayList<>();
		for(IMultiblock mb : MultiblockHandler.getMultiblocks())
			if(mb instanceof IGTemplateMultiblock ig)
				tests.add(new TestFunction(
						"capability", ig.getUniqueName().getPath()+"_teardown",
						ig.getUniqueName().toString(),
						400, 0, true, helper -> capabilityTeardown(helper, ig)
				));
		return tests;
	}

	private static void capabilityTeardown(GameTestHelper helper, IGTemplateMultiblock multiblock)
	{
		BlockPos trigger = multiblock.getTriggerOffset().above();
		Vec3i size = multiblock.getSize(helper.getLevel());

		CommonTests.formMultiblock(multiblock, helper);
		helper.assertBlockPresent(multiblock.getBlock(), trigger);
		Anchor anchor = attachEnergyPipe(helper, size);

        helper.runAfterDelay(20, () -> {
            if(anchor != null) poll(helper, anchor);
            List<Held> held = captureCapabilities(helper, size);
            helper.setBlock(trigger, Blocks.AIR);
            helper.runAfterDelay(10, () -> {
                if(anchor != null) poll(helper, anchor);
                for(Held h : held)
                    if(h.provider().isRemoved() && h.cache().getCapability() == h.initial())
                        helper.fail(h.describe()+" outlived its block entity in a capability cache");
                helper.succeed();
            });
        });
    }

	private static void poll(GameTestHelper helper, Anchor anchor)
	{
		if(helper.getLevel().getBlockEntity(helper.absolutePos(anchor.pipePos())) instanceof IGEnergyPipeEntity pipe)
			pipe.updateConnectionByte(anchor.towardsReactor());
	}

	@Nullable
	private static Anchor attachEnergyPipe(GameTestHelper helper, Vec3i size)
	{
		Block pipeBlock = findEnergyPipeBlock();
		if(pipeBlock == null)
			return null;

		for(BlockPos pos : BlockPos.betweenClosed(BlockPos.ZERO, new BlockPos(size).offset(1, 1, 1)))
		{
			BlockEntity be = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
			if(be == null)
				continue;

			for(Direction side : Direction.values())
			{
				if(helper.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, be.getBlockPos(), side) == null)
					continue;

				BlockPos pipePos = pos.relative(side);
				if(!helper.getBlockState(pipePos).isAir())
					continue;

				helper.setBlock(pipePos, pipeBlock);
				return new Anchor(pipePos, side.getOpposite());
			}
		}
		return null;
	}

	@Nullable
	private static Block findEnergyPipeBlock()
	{
		List<MaterialInterface<?>> materials = new ArrayList<>();
		Collections.addAll(materials, MiscEnum.values());
		Collections.addAll(materials, MetalEnum.values());
		Collections.addAll(materials, ChemicalEnum.values());
		Collections.addAll(materials, MineralEnum.values());
		Collections.addAll(materials, StoneEnum.values());

		for(MaterialInterface<?> material : materials)
			if(material.hasFlag(BlockCategoryFlags.ENERGY_PIPE))
				return material.getBlock(BlockCategoryFlags.ENERGY_PIPE);
		return null;
	}

	private static List<Held> captureCapabilities(GameTestHelper helper, Vec3i size)
	{
		List<Held> held = new ArrayList<>();
		for(BlockPos pos : BlockPos.betweenClosed(BlockPos.ZERO, new BlockPos(size).offset(1, 1, 1)))
		{
			BlockEntity be = helper.getLevel().getBlockEntity(helper.absolutePos(pos));
			if(be == null||be instanceof IGEnergyPipeEntity)
				continue;

			for(BlockCapability<?, Direction> cap : WATCHED)
				for(Direction side : Direction.values())
				{
					var cache = BlockCapabilityCache.create(cap, helper.getLevel(), be.getBlockPos(), side);
                    Object initial = cache.getCapability();
                    if(initial != null) held.add(new Held(be, cap, side, cache, initial));
				}
		}
		return held;
	}

	private record Anchor(BlockPos pipePos, Direction towardsReactor) {}

	private record Held(BlockEntity provider, BlockCapability<?, Direction> capability, Direction side, BlockCapabilityCache<?, Direction> cache, Object initial)
	{
		String describe()
		{
			return capability.name()+" on "+provider.getClass().getSimpleName()
					+" at "+provider.getBlockPos()+" side "+side;
		}
	}

}
