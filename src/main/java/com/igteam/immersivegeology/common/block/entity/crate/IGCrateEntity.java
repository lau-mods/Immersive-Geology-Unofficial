/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.block.entity.crate;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import com.igteam.immersivegeology.common.block.helper.IGBlockType;
import com.igteam.immersivegeology.common.menu.IGCrateMenu;
import com.igteam.immersivegeology.core.material.data.enums.MetalEnum;
import com.igteam.immersivegeology.core.material.helper.flags.BlockCategoryFlags;
import com.igteam.immersivegeology.core.material.helper.material.MaterialInterface;
import com.igteam.immersivegeology.core.material.helper.material.MaterialTexture;
import com.igteam.immersivegeology.core.registration.IGMenuTypes;
import com.igteam.immersivegeology.core.registration.IGRegistrationHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class IGCrateEntity extends RandomizableContainerBlockEntity implements IIEInventory, IEBlockInterfaces.IBlockEntityDrop, IEBlockInterfaces.IComparatorOverride
{
	public static final int CONTAINER_SIZE = 36;
	private NonNullList<ItemStack> inventory;
	private ItemEnchantments enchantments = ItemEnchantments.EMPTY;
	public final IItemHandler inventoryCap;

	public IGCrateEntity(BlockPos pos, BlockState state) {
		super(resolveEntityType(state), pos, state);
		this.inventory = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
		this.inventoryCap = new IEInventoryHandler(CONTAINER_SIZE, this);
	}

	private static BlockEntityType<?> resolveEntityType(BlockState state) {
		Block block = state.getBlock();

		if (block instanceof IGBlockType crateBlock) {
			MaterialInterface<?> material = crateBlock.getMaterial(MaterialTexture.base);
			String registryKey = BlockCategoryFlags.CRATE.getRegistryKey(material);
			return IGRegistrationHolder.getTE.apply(registryKey);
		}

		// Fallback to Steel if block is not of the expected type
		String fallbackKey = BlockCategoryFlags.CRATE.getRegistryKey(MetalEnum.Steel);
		return IGRegistrationHolder.getTE.apply(fallbackKey);
	}

	@Override
	protected @NotNull Component getDefaultName()
	{
		Block b = this.getBlockState().getBlock();
		if(b instanceof IGCrateEntityType block)
		{
			return Component.translatable("block.immersivegeology.crate", block.getMaterial(MaterialTexture.base).getTranslationName());
		}
		return Component.literal("Crate");

	}

	@Override
	protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory)
	{
		return new IGCrateMenu(IGMenuTypes.CRATE.get(), pContainerId, pInventory, this);
	}

	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		super.loadAdditional(nbt, provider);
		enchantments = ItemEnchantments.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt.getCompound("enchantments")).result().orElse(ItemEnchantments.EMPTY);
		if(!tryLoadLootTable(nbt)) ContainerHelper.loadAllItems(nbt, inventory, provider);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		super.saveAdditional(nbt, provider);
		nbt.put("enchantments", ItemEnchantments.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), enchantments).getOrThrow());
		if(!trySaveLootTable(nbt)) ContainerHelper.saveAllItems(nbt, inventory, provider);
	}

	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}

	protected void setItems(NonNullList<ItemStack> pItemStacks) {
		this.inventory = pItemStacks;
	}

	@Nonnull
	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}

	public boolean isStackValid(int slot, ItemStack stack) {
		return IEApi.isAllowedInCrate(stack);
	}

	public int getSlotLimit(int slot) {
		return 64;
	}

	public void doGraphicalUpdates() {
		this.setChanged();
	}

	public void getBlockEntityDrop(LootContext context, Consumer<ItemStack> drop) {
		ItemStack stack = new ItemStack(getBlockState().getBlock());
		stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(inventory));
		Component customName = getCustomName();
		if(customName!=null) stack.set(DataComponents.CUSTOM_NAME, customName);
		stack.set(DataComponents.ENCHANTMENTS, enchantments);
		drop.accept(stack);
	}

	public void onBEPlaced(BlockPlaceContext ctx) {
		this.onBEPlaced(ctx.getItemInHand());
	}

	public void onBEPlaced(ItemStack stack) {
		enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
	}





	public boolean canPlaceItem(int index, ItemStack stack) {
		return this.isStackValid(index, stack);
	}

	public int getComparatorInputOverride() {
		return Utils.calcRedstoneFromInventory(this);
	}

	public int getContainerSize() {
		return CONTAINER_SIZE;
	}
}
