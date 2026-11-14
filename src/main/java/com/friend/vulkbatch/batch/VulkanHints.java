package com.friend.vulkbatch.batch;

@SuppressWarnings({"unused", "SameReturnValue"})
public class VulkanHints {
    public static boolean isVulkanBackend() {
        return false;
    }

    public static int preferredBatchSize() {
        return 4096;
    }
}