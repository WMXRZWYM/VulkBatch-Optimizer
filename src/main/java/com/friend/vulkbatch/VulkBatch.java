package com.friend.vulkbatch;

import com.friend.vulkbatch.batch.BatchManager;
import com.friend.vulkbatch.batch.VulkanHints;
import com.friend.vulkbatch.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class VulkBatch implements ClientModInitializer {
    public static final String MOD_ID = "vulkbatch";
    // 将 LOGGER 设为 private，IDEA 不再提示可见性警告
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        Config.load();

        boolean vulkan = VulkanHints.isVulkanBackend();
        LOGGER.info("VulkBatch Loaded. Backend={}, TargetBatchSize={}",
                vulkan ? "Vulkan" : "OpenGL", VulkanHints.preferredBatchSize());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.level != null) {
                tickCounter++;
                if (tickCounter >= 100) {
                    tickCounter = 0;
                    String stats = BatchManager.getAndResetStats();
                    if (stats != null) {
                        LOGGER.info("[VulkBatch Panel] {}", stats);
                    }
                }
            }
        });
    }
}