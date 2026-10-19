package com.fiftyfifty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import com.fiftyfifty.network.SetForceModePayload;

public class FiftyFifty implements ModInitializer {
	public static final String MOD_ID = "fiftyfifty";
	public static final Logger LOGGER = LoggerFactory.getLogger("50/50");

	/**
	 * The current rig. Lives on the server (in singleplayer that's your own world),
	 * and every dropper in the world obeys it.
	 */
	public static volatile ForceMode mode = ForceMode.RANDOM;

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.serverboundPlay().register(SetForceModePayload.TYPE, SetForceModePayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(SetForceModePayload.TYPE, (payload, context) -> {
			mode = ForceMode.byId(payload.mode());

			String text = switch (mode) {
				case WATER -> "§b50/50 rigged: droppers will pull the WATER bucket";
				case LAVA -> "§650/50 rigged: droppers will pull the LAVA bucket";
				case RANDOM -> "§a50/50 is fair again: water or lava, coin flip";
			};
			context.player().sendSystemMessage(Component.literal(text));
		});

		LOGGER.info("50/50 loaded");
	}
}
