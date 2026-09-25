package com.sprintindicator.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plain-data config for the sprint indicator, persisted as JSON in the
 * game's config folder (.minecraft/config/sprintindicator.json).
 */
public class SprintIndicatorConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("sprintindicator.json");

    // --- Editable fields (Cloth Config binds directly to these via the setSaveConsumer calls) ---
    public float scale = 1.0f;
    public int x = 10;
    public int y = 10;
    public int sprintingColor = 0x55FF55; // green, plain RGB (no alpha)
    public int walkingColor = 0xFF5555;   // red, plain RGB (no alpha)
    public int backgroundOpacity = 128;   // 0 (invisible) - 255 (fully opaque black)

    private static SprintIndicatorConfig instance;

    public static SprintIndicatorConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static SprintIndicatorConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                SprintIndicatorConfig loaded = GSON.fromJson(reader, SprintIndicatorConfig.class);
                if (loaded != null) {
                    return loaded;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new SprintIndicatorConfig();
    }

    /** Call this after any field changes (Cloth Config's save button does this automatically). */
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}