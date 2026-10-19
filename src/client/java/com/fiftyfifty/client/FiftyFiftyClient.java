package com.fiftyfifty.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.fiftyfifty.FiftyFifty;
import com.fiftyfifty.ForceMode;
import com.fiftyfifty.network.SetForceModePayload;

public class FiftyFiftyClient implements ClientModInitializer {
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(FiftyFifty.id("main"));

	private static final KeyMapping FORCE_WATER = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.fiftyfifty.force_water", InputConstants.Type.KEYSYM, InputConstants.KEY_J, CATEGORY));

	private static final KeyMapping FORCE_LAVA = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.fiftyfifty.force_lava", InputConstants.Type.KEYSYM, InputConstants.KEY_K, CATEGORY));

	private static final KeyMapping MAKE_FAIR = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.fiftyfifty.make_fair", InputConstants.Type.KEYSYM, InputConstants.KEY_H, CATEGORY));

	private static final KeyMapping OPEN_MENU = KeyMappingHelper.registerKeyMapping(new KeyMapping(
			"key.fiftyfifty.open_menu", InputConstants.Type.KEYSYM, InputConstants.KEY_O, CATEGORY));

	/** What this player last picked, shown in the panel. */
	static ForceMode currentMode = ForceMode.RANDOM;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (FORCE_WATER.consumeClick()) send(client, ForceMode.WATER);
			while (FORCE_LAVA.consumeClick()) send(client, ForceMode.LAVA);
			while (MAKE_FAIR.consumeClick()) send(client, ForceMode.RANDOM);
			while (OPEN_MENU.consumeClick()) {
				if (client.player != null) client.gui.setScreen(new FiftyFiftyScreen());
			}
		});
	}

	static void send(Minecraft client, ForceMode mode) {
		if (client.player == null) return;

		if (!ClientPlayNetworking.canSend(SetForceModePayload.TYPE)) {
			client.player.sendSystemMessage(Component.literal(
					"§c50/50 isn't installed on this server, so it can't rig the droppers here."));
			return;
		}

		ClientPlayNetworking.send(new SetForceModePayload(mode.ordinal()));
		currentMode = mode;
	}
}
