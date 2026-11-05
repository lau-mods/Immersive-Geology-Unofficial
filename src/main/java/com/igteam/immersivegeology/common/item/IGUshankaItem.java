/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.item;

import com.igteam.immersivegeology.core.lib.IGLib;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class IGUshankaItem extends ArmorItem
{
	private static final net.minecraft.resources.ResourceLocation TEXTURE = IGLib.rl("textures/models/armor_russian.png");

	public IGUshankaItem()
	{
		super(com.igteam.immersivegeology.core.registration.IGRegistrationHolder.USHANKA_MATERIAL, Type.HELMET, new Properties().durability(77));
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip,
								@NotNull TooltipFlag flag)
	{
		super.appendHoverText(stack, context, tooltip, flag);
		tooltip.add(Component.translatable("item.immersivegeology.armor_russian_helmet.lore")
				.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
	}

	@Nullable
	@Override
	public net.minecraft.resources.ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel)
	{
		return TEXTURE;
	}

}
