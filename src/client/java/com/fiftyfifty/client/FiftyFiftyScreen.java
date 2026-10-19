package com.fiftyfifty.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import com.fiftyfifty.ForceMode;

/** The 50/50 control panel, opened with O. */
public class FiftyFiftyScreen extends Screen {
	private Button status;

	public FiftyFiftyScreen() {
		super(Component.literal("50/50"));
	}

	@Override
	protected void init() {
		int w = 200;
		int x = (this.width - w) / 2;
		int y = this.height / 2 - 70;

		// Shows which item droppers will pull right now.
		this.status = Button.builder(statusText(), b -> {}).bounds(x, y, w, 20).build();
		this.status.active = false;
		this.addRenderableWidget(this.status);

		this.addRenderableWidget(Button.builder(Component.literal("§bForce Water Bucket"),
				b -> choose(ForceMode.WATER)).bounds(x, y + 35, w, 20).build());

		this.addRenderableWidget(Button.builder(Component.literal("§6Force Lava Bucket"),
				b -> choose(ForceMode.LAVA)).bounds(x, y + 60, w, 20).build());

		this.addRenderableWidget(Button.builder(Component.literal("§aFair 50/50"),
				b -> choose(ForceMode.RANDOM)).bounds(x, y + 85, w, 20).build());

		this.addRenderableWidget(Button.builder(Component.literal("Done"),
				b -> this.onClose()).bounds(x, y + 120, w, 20).build());
	}

	private void choose(ForceMode mode) {
		FiftyFiftyClient.send(this.minecraft, mode);
		this.status.setMessage(statusText());
	}

	private static Component statusText() {
		return Component.literal(switch (FiftyFiftyClient.currentMode) {
			case WATER -> "Droppers pull: §bWATER";
			case LAVA -> "Droppers pull: §6LAVA";
			case RANDOM -> "Droppers pull: §aFAIR 50/50";
		});
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
