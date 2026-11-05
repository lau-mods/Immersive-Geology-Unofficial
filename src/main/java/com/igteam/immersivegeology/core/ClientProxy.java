/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.core;


import com.igteam.immersivegeology.client.menu.*;
import com.igteam.immersivegeology.client.menu.multiblock.*;
import com.igteam.immersivegeology.core.registration.IGMenuTypes;
import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import com.igteam.immersivegeology.common.block.entity.cable.EnergyPipeCallback;
import com.igteam.immersivegeology.core.lib.IGLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(value = Dist.CLIENT, modid = IGLib.MODID, bus = Bus.MOD)
public class ClientProxy extends CommonProxy
{

    @net.neoforged.bus.api.SubscribeEvent
    public static void registerScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event)
    {
        event.register(IGMenuTypes.BLOOMERY.getType(), BloomeryScreen::new);
        event.register(IGMenuTypes.REVERBERATION_FURNACE.getType(), ReverberationScreen::new);
        event.register(IGMenuTypes.GEOTHERMAL_EXCHANGER.getType(), GeothermalExchangerScreen::new);
        event.register(IGMenuTypes.CRYSTALLIZER.getType(), CrystallizerScreen::new);
        event.register(IGMenuTypes.CHEMICAL_REACTOR.getType(), ChemicalReactorScreen::new);
        event.register(IGMenuTypes.SMALL_CHEMICAL_REACTOR.getType(), SmallChemicalReactorScreen::new);
        event.register(IGMenuTypes.ROTARY_KILN.getType(), RotaryKilnScreen::new);
        event.register(IGMenuTypes.BULK_BLAST_FURNACE.getType(), BulkBlastFurnaceScreen::new);
        event.register(IGMenuTypes.FOUNDRY.getType(), FoundryScreen::new);
        event.register(IGMenuTypes.CRATE.get(), IGCrateScreen.StandardIGCrate::new);
        event.register(IGMenuTypes.METAL_DETECTOR.get(), IGMetalDetectorScreen::new);
    }


	@Override
	public void reinitializeGUI()
	{
		Screen currentScreen = Minecraft.getInstance().screen;
		if(currentScreen instanceof IEContainerScreen)
			currentScreen.init(Minecraft.getInstance(), currentScreen.width, currentScreen.height);
	}

	@Override
	public void modConstruction(net.neoforged.bus.api.IEventBus bus)
	{
		super.modConstruction(bus);
		IEOBJCallbacks.register(ResourceLocation.fromNamespaceAndPath(IGLib.MODID, "energy_pipe"), EnergyPipeCallback.INSTANCE);
	}

	@Override
	public Level getClientWorld()
	{
		return Minecraft.getInstance().level;
	}

	@Override
	public Player getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}
}
