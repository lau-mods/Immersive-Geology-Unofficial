/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.entity.device;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.HashMap;
import java.util.List;

public final class IGDepositMapMarks
{
	private IGDepositMapMarks() {}

	public static final String PREFIX = "ig_deposit_";
	private static final String NAMES = "IGNames";

	public record Mark(String id, Holder<MapDecorationType> type, double x, double z, Component name) {}

	public static String prefixFor(long detector)
	{
		return PREFIX+detector+"_";
	}

	public static void write(ItemStack stack, String prefix, List<Mark> marks, HolderLookup.Provider provider)
	{
		var decorations = new HashMap<>(stack.getOrDefault(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY).decorations());
		decorations.keySet().removeIf(id -> id.startsWith(prefix));
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
			CompoundTag names = tag.getCompound(NAMES);
			names.getAllKeys().removeIf(id -> id.startsWith(prefix));
			for(Mark mark : marks)
			{
				decorations.put(mark.id(), new MapDecorations.Entry(mark.type(), mark.x(), mark.z(), 180));
				names.putString(mark.id(), Component.Serializer.toJson(mark.name(), provider));
			}
			tag.put(NAMES, names);
		});
		stack.set(DataComponents.MAP_DECORATIONS, new MapDecorations(decorations));
	}

	public static boolean hasMarks(ItemStack stack)
	{
		return stack.getOrDefault(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY)
				.decorations().keySet().stream().anyMatch(id -> id.startsWith(PREFIX));
	}

	public static boolean restore(ItemStack stack, MapItemSavedData data, Level level)
	{
		CompoundTag names = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(NAMES);
		boolean found = false;
		for(var mark : stack.getOrDefault(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY).decorations().entrySet())
		{
			String id = mark.getKey();
			if(!id.startsWith(PREFIX)) continue;
			found = true;
			if(!names.contains(id)) continue;
			Component name = Component.Serializer.fromJson(names.getString(id), level.registryAccess());
			var entry = mark.getValue();
			if(name!=null) data.addDecoration(entry.type(), level, id, entry.x(), entry.z(), entry.rotation(), name);
		}
		return found;
	}
}
