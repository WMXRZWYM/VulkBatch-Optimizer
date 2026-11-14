package com.friend.vulkbatch.config;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger("VulkBatch-Config");
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("vulkbatch.properties");
    private static final Properties PROPS = new Properties();

    public static boolean optimizeBuffer = true;
    public static boolean optimizeChunk = true;
    public static boolean optimizeEntity = true;
    public static boolean optimizeBlockEntity = true;
    public static boolean optimizeParticle = true;

    public static int ITEM_CULL_DISTANCE = 64;
    public static int XP_ORB_CULL_DISTANCE = 32;
    public static int BLOCK_ENTITY_CULL_DISTANCE = 64;
    public static int PARTICLE_CULL_DISTANCE = 32;

    public static int CHUNK_DEBOUNCE_TICKS = 5;

    public static void load() {
        boolean shouldSave = false;
        if (!Files.exists(FILE)) {
            shouldSave = true;
        } else {
            try (InputStream in = Files.newInputStream(FILE)) {
                PROPS.load(in);
            } catch (IOException e) {
                LOGGER.error("Failed to load config.", e);
                shouldSave = true;
            }
        }

        optimizeBuffer = getBoolean("optimizeBuffer");
        optimizeChunk = getBoolean("optimizeChunk");
        optimizeEntity = getBoolean("optimizeEntity");
        optimizeBlockEntity = getBoolean("optimizeBlockEntity");
        optimizeParticle = getBoolean("optimizeParticle");

        ITEM_CULL_DISTANCE = getInt("itemCullDistance", 64, 16, 256);
        XP_ORB_CULL_DISTANCE = getInt("xpOrbCullDistance", 32, 8, 128);
        BLOCK_ENTITY_CULL_DISTANCE = getInt("blockEntityCullDistance", 64, 16, 256);
        PARTICLE_CULL_DISTANCE = getInt("particleCullDistance", 32, 8, 128);

        CHUNK_DEBOUNCE_TICKS = getInt("chunkDebounceTicks", 5, 1, 20);

        if (shouldSave) save();
    }

    public static void save() {
        PROPS.setProperty("optimizeBuffer", String.valueOf(optimizeBuffer));
        PROPS.setProperty("optimizeChunk", String.valueOf(optimizeChunk));
        PROPS.setProperty("optimizeEntity", String.valueOf(optimizeEntity));
        PROPS.setProperty("optimizeBlockEntity", String.valueOf(optimizeBlockEntity));
        PROPS.setProperty("optimizeParticle", String.valueOf(optimizeParticle));

        PROPS.setProperty("itemCullDistance", String.valueOf(ITEM_CULL_DISTANCE));
        PROPS.setProperty("xpOrbCullDistance", String.valueOf(XP_ORB_CULL_DISTANCE));
        PROPS.setProperty("blockEntityCullDistance", String.valueOf(BLOCK_ENTITY_CULL_DISTANCE));
        PROPS.setProperty("particleCullDistance", String.valueOf(PARTICLE_CULL_DISTANCE));

        PROPS.setProperty("chunkDebounceTicks", String.valueOf(CHUNK_DEBOUNCE_TICKS));

        try (OutputStream out = Files.newOutputStream(FILE)) {
            PROPS.store(out, "VulkBatch Configuration");
        } catch (IOException e) {
            LOGGER.error("Failed to save config.", e);
        }
    }

    private static boolean getBoolean(String key) {
        return Boolean.parseBoolean(PROPS.getProperty(key, "true"));
    }

    private static int getInt(String key, int def, int min, int max) {
        try {
            int val = Integer.parseInt(PROPS.getProperty(key, String.valueOf(def)));
            return Math.clamp(val, min, max);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}