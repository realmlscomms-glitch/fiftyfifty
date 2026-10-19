package com.fiftyfifty.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import com.fiftyfifty.FiftyFifty;

/** Sent from the client when a 50/50 key is pressed. */
public record SetForceModePayload(int mode) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SetForceModePayload> TYPE =
			new CustomPacketPayload.Type<>(FiftyFifty.id("set_force_mode"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SetForceModePayload> CODEC =
			StreamCodec.composite(ByteBufCodecs.INT, SetForceModePayload::mode, SetForceModePayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
