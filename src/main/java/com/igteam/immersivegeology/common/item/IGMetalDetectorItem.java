/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.item;

import com.igteam.immersivegeology.common.block.entity.device.IGMetalDetectorEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IGMetalDetectorItem extends BlockItem
{
	public static final String ENERGY_TAG = "energy";
	

	public IGMetalDetectorItem(Block block)
	{
		super(block, new Properties().stacksTo(1));
	}

	public static int getEnergyStored(ItemStack stack)
	{
		CompoundTag tag = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
		return Math.min(IGMetalDetectorEntity.ENERGY_CAPACITY, tag.getInt(ENERGY_TAG));
	}

	private static void setEnergyStored(ItemStack stack, int energy)
	{
		CustomData.update(DataComponents.BLOCK_ENTITY_DATA, stack, tag -> {
			tag.putString("id", "immersivegeology:metal_detector_type");
			tag.putInt(ENERGY_TAG, energy);
		});
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext level, @NotNull List<Component> tooltip,
								@NotNull TooltipFlag flag)
	{
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable("gui.immersivegeology.metal_detector.energy",
				getEnergyStored(stack), IGMetalDetectorEntity.ENERGY_CAPACITY));
	}

	@Override
	public boolean isBarVisible(@NotNull ItemStack stack)
	{
		return getEnergyStored(stack) > 0;
	}

	@Override
	public int getBarWidth(@NotNull ItemStack stack)
	{
		return Math.round(13f*getEnergyStored(stack)/IGMetalDetectorEntity.ENERGY_CAPACITY);
	}

	@Override
	public int getBarColor(@NotNull ItemStack stack)
	{
		return 0xD54B18;
	}


	public static class EnergyProvider implements IEnergyStorage
	{
		private final ItemStack stack;

		public EnergyProvider(ItemStack stack)
		{
			this.stack = stack;
		}


		@Override
		public int receiveEnergy(int maxReceive, boolean simulate)
		{
			int stored = getEnergyStored();
			int accepted = Math.min(getMaxEnergyStored()-stored,
					Math.min(IGMetalDetectorEntity.ENERGY_MAX_INPUT, maxReceive));
			if(accepted <= 0) return 0;
			if(!simulate) setEnergyStored(stack, stored+accepted);
			return accepted;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate)
		{
			return 0;
		}

		@Override
		public int getEnergyStored()
		{
			return IGMetalDetectorItem.getEnergyStored(stack);
		}

		@Override
		public int getMaxEnergyStored()
		{
			return IGMetalDetectorEntity.ENERGY_CAPACITY;
		}

		@Override
		public boolean canExtract()
		{
			return false;
		}

		@Override
		public boolean canReceive()
		{
			return true;
		}
	}
}
