package com.sprintindicator.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;

public class SprintIndicatorClient implements ClientModInitializer {

	private static final String MOD_ID = "sprint-indicator";
	private static boolean toggleState = false;

	@Override
	public void onInitializeClient() {
		SprintIndicatorConfig.get();

		HudElementRegistry.attachElementBefore(
				VanillaHudElements.HOTBAR,
				Identifier.fromNamespaceAndPath(MOD_ID, "sprint_status"),
				SprintIndicatorClient::render
		);
	}

	private static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
		Minecraft client = Minecraft.getInstance();
		LocalPlayer player = client.player;

		if (player == null) {
			return;
		}

		// Intercepts every click reliably for instant toggle response
		while (client.options.keySprint.consumeClick()) {
			toggleState = !toggleState;
		}

		SprintIndicatorConfig cfg = SprintIndicatorConfig.get();

		boolean sprinting = toggleState;
		String text = sprinting ? "Sprint toggle: ON" : "Sprint toggle: OFF";
		int rgb = sprinting ? cfg.sprintingColor : cfg.walkingColor;
		int textColor = 0xFF000000 | (rgb & 0x00FFFFFF);

		int textWidth = client.font.width(text);
		int textHeight = client.font.lineHeight;
		int padding = 3;

		Matrix3x2fStack matrices = graphics.pose();

		matrices.pushMatrix();
		matrices.translate(cfg.x, cfg.y);
		matrices.scale(cfg.scale, cfg.scale);

		int bgAlpha = Math.clamp(cfg.backgroundOpacity, 0, 255);
		if (bgAlpha > 0) {
			int bgColor = (bgAlpha << 24);
			graphics.fill(-padding, -padding, textWidth + padding, textHeight + padding, bgColor);
		}

		graphics.text(client.font, text, 0, 0, textColor);

		matrices.popMatrix();
	}}
