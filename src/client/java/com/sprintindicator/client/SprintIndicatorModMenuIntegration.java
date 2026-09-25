package com.sprintindicator.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

/**
 * Registers this mod's config screen with Mod Menu. Requires both
 * "modmenu" and "cloth-config" jars in your mods folder to actually show
 * up (they're compile-time/runtime dependencies, not bundled).
 *
 * Register this class under a "modmenu" entrypoint in fabric.mod.json, e.g.:
 *   "modmenu": ["com.example.sprintindicator.client.SprintIndicatorModMenuIntegration"]
 */
public class SprintIndicatorModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            SprintIndicatorConfig cfg = SprintIndicatorConfig.get();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("Sprint Indicator"));

            builder.setSavingRunnable(cfg::save);

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

            general.addEntry(entryBuilder.startFloatField(Component.literal("Scale"), cfg.scale)
                    .setDefaultValue(1.0f)
                    .setMin(0.25f)
                    .setMax(4.0f)
                    .setSaveConsumer(val -> cfg.scale = val)
                    .build());

            general.addEntry(entryBuilder.startIntField(Component.literal("X Position"), cfg.x)
                    .setDefaultValue(10)
                    .setMin(-2000)
                    .setMax(2000)
                    .setSaveConsumer(val -> cfg.x = val)
                    .build());

            general.addEntry(entryBuilder.startIntField(Component.literal("Y Position"), cfg.y)
                    .setDefaultValue(10)
                    .setMin(-2000)
                    .setMax(2000)
                    .setSaveConsumer(val -> cfg.y = val)
                    .build());

            general.addEntry(entryBuilder.startColorField(Component.literal("Sprinting Color"), cfg.sprintingColor)
                    .setDefaultValue(0x55FF55)
                    .setSaveConsumer(val -> cfg.sprintingColor = val)
                    .build());

            general.addEntry(entryBuilder.startColorField(Component.literal("Walking Color"), cfg.walkingColor)
                    .setDefaultValue(0xFF5555)
                    .setSaveConsumer(val -> cfg.walkingColor = val)
                    .build());

            general.addEntry(entryBuilder.startIntSlider(Component.literal("Background Opacity"), cfg.backgroundOpacity, 0, 255)
                    .setDefaultValue(128)
                    .setSaveConsumer(val -> cfg.backgroundOpacity = val)
                    .build());

            return builder.build();
        };
    }
}