package com.ecourty.noitembreak.config;

import com.ecourty.noitembreak.NoItemBreak;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;

public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoItemBreak.MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "noitembreak.json";

    private static NoItemBreakConfig config;

    public static NoItemBreakConfig getConfig() {
        if (config == null) {
            load();
        }
        return config;
    }

    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);
        File configFile = configPath.toFile();

        if (configFile.exists()) {
            try (Reader reader = new FileReader(configFile)) {
                config = GSON.fromJson(reader, NoItemBreakConfig.class);
                if (config == null) {
                    config = new NoItemBreakConfig();
                }
            } catch (IOException | JsonSyntaxException e) {
                LOGGER.error("Failed to load NoItemBreak config, using defaults", e);
                config = new NoItemBreakConfig();
            }
        } else {
            config = new NoItemBreakConfig();
            save();
        }
    }

    public static void save() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);
        try (Writer writer = new FileWriter(configPath.toFile())) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save NoItemBreak config", e);
        }
    }
}
