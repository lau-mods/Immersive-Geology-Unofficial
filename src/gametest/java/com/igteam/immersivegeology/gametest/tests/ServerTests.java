/*
 * Muddykat
 * Copyright (c) 2025
 *
 * This code is licensed under "GNU LESSER GENERAL PUBLIC LICENSE"
 * Details can be found in the license file in the root folder of this project
 */

package com.igteam.immersivegeology.gametest.tests;

import com.igteam.immersivegeology.core.lib.IGLib;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufUtil;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;

public class ServerTests
{
	private static final String TEST_AREA = IGLib.MODID + ":test_area";
	public static List<TestFunction> all()
	{
		List<TestFunction> all = new ArrayList<>();
		all.addAll(basics());
		return all;
	}

	private static List<TestFunction> basics()
	{
		List<TestFunction> tests = new ArrayList<>();
		tests.add(new TestFunction(
				"server", "startup", TEST_AREA,
				200, 0, true, ServerTests::testServerStartup
		));

		tests.add(new TestFunction(
				"server", "connection", TEST_AREA,
				200, 0, true, ServerTests::testNetworkConnectivity
		));

		tests.add(new TestFunction("server", "recipes_load_and_sync", TEST_AREA,
				200, 0, true, ServerTests::testRecipesLoadAndSync));
		return tests;
	}

	private static void testRecipesLoadAndSync(GameTestHelper helper) {
		var level = helper.getLevel();
		var recipes = level.getRecipeManager();
		var files = new FileToIdConverter("recipe", ".json");
		for(var entry : files.listMatchingResources(level.getServer().getResourceManager()).entrySet()) {
			if(!entry.getKey().getNamespace().equals(IGLib.MODID)) continue;
			try(var reader = entry.getValue().openAsReader()) {
				var json = JsonParser.parseReader(reader).getAsJsonObject();
				if(!json.has("neoforge:conditions"))
					helper.assertTrue(recipes.byKey(files.fileToId(entry.getKey())).isPresent(),
							"Recipe failed to load: " + entry.getKey());
			} catch(java.io.IOException e) {
				throw new RuntimeException(e);
			}
		}
		int checked = 0;
		for(var holder : recipes.getRecipes()) {
			if(!BuiltInRegistries.RECIPE_SERIALIZER.getKey(holder.value().getSerializer()).getNamespace().equals(IGLib.MODID)) continue;
			var encoded = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
			var roundTrip = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
			try {
				Recipe.STREAM_CODEC.encode(encoded, holder.value());
				var decoded = Recipe.STREAM_CODEC.decode(encoded);
				Recipe.STREAM_CODEC.encode(roundTrip, decoded);
				encoded.readerIndex(0);
				helper.assertTrue(ByteBufUtil.equals(encoded, roundTrip), "Recipe sync changed: " + holder.id());
				checked++;
			} finally {
				encoded.release();
				roundTrip.release();
			}
		}
		helper.assertTrue(checked > 0, "No Immersive Geology recipes loaded");
		helper.succeed();
	}

	private static void testServerStartup(GameTestHelper helper) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		helper.assertTrue(server != null, "Server failed to initialize");
		helper.assertTrue(server.isRunning(), "Server is not running");
		helper.succeed();
	}

	private static void testNetworkConnectivity(GameTestHelper helper) {
		helper.runAfterDelay(40, () -> {
			try {
				MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
				helper.assertTrue(server.getPlayerList() != null,
						"Server player list not initialized");
				helper.succeed();
			} catch (Exception e) {
				helper.fail("Network test failed: " + e.getMessage());
			}
		});
	}
}
