/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.common.pack;

import com.igteam.immersivegeology.core.lib.IGLib;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import com.igteam.immersivegeology.core.material.data.stone.IGStoneTypes;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Puts {@link IGDeclaredStonePack} in front of the resource and data pack stacks.
 */
@EventBusSubscriber(modid = IGLib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class IGPackFinder
{
	@SubscribeEvent
	public static void addPackFinders(AddPackFindersEvent event)
	{
		// Decided from the configuration alone. Whether any ore blocks were registered for a declared rock type is
		// not knowable yet at this point in start-up, but we can tell if anything was declared.
		boolean anyDeclared = IGStoneTypes.all().stream().anyMatch(stone -> stone.getHostBlockId()!=null);
		if(!anyDeclared) return;

		PackType type = event.getPackType();
		event.addRepositorySource(consumer -> {
            Pack pack = Pack.readMetaAndCreate(IGDeclaredStonePack.LOCATION, new Pack.ResourcesSupplier()
            {
                @Override
                public net.minecraft.server.packs.PackResources openPrimary(net.minecraft.server.packs.PackLocationInfo location)
                {
                    return new IGDeclaredStonePack(type);
                }

                @Override
                public net.minecraft.server.packs.PackResources openFull(net.minecraft.server.packs.PackLocationInfo location, Pack.Metadata metadata)
                {
                    return new IGDeclaredStonePack(type);
                }
            }, type, new net.minecraft.server.packs.PackSelectionConfig(true, Pack.Position.BOTTOM, false));
			if(pack!=null) consumer.accept(pack);
		});
	}
}
