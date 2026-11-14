package com.friend.vulkbatch.batch;

import com.friend.vulkbatch.config.Config;
import net.minecraft.core.SectionPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("all") // 终极武器：压制所有警告
public class BatchManager {
    private static final AtomicInteger INTERCEPTED_BUILDS = new AtomicInteger(0);
    private static final AtomicInteger REUSED_BUFFERS = new AtomicInteger(0);
    private static final AtomicInteger SKIPPED_CHUNKS = new AtomicInteger(0);
    private static final AtomicInteger CULLED_ENTITIES = new AtomicInteger(0);
    private static final AtomicInteger CULLED_BE = new AtomicInteger(0);
    private static final AtomicInteger CULLED_PARTICLES = new AtomicInteger(0);

    private static final ThreadLocal<Boolean> DEFERRED = ThreadLocal.withInitial(() -> false);
    private static final Map<Long, Long> RECENT_CHUNKS = new ConcurrentHashMap<>();
    private static final int CHUNK_CACHE_LIMIT = 5000;

    public static boolean shouldCoalesce() {
        return Config.optimizeBuffer && !DEFERRED.get();
    }

    public static void defer() {
        DEFERRED.set(true);
        INTERCEPTED_BUILDS.incrementAndGet();
    }

    public static void release() {
        if (DEFERRED.get()) {
            DEFERRED.set(false);
            REUSED_BUFFERS.incrementAndGet();
        }
    }

    public static boolean shouldSkipChunk(SectionPos pos, long currentTick) {
        if (!Config.optimizeChunk) return false;
        long packed = pos.asLong();
        Long last = RECENT_CHUNKS.get(packed);

        if (last != null && (currentTick - last) < Config.CHUNK_DEBOUNCE_TICKS) {
            SKIPPED_CHUNKS.incrementAndGet();
            return true;
        }

        if (RECENT_CHUNKS.size() > CHUNK_CACHE_LIMIT) {
            long oldestTick = currentTick - 100;
            RECENT_CHUNKS.entrySet().removeIf(entry -> entry.getValue() < oldestTick);
        }

        RECENT_CHUNKS.put(packed, currentTick);
        return false;
    }

    public static void recordEntityCull() {
        CULLED_ENTITIES.incrementAndGet();
    }

    public static void recordBlockEntityCull() {
        CULLED_BE.incrementAndGet();
    }

    public static void recordParticleCull() {
        CULLED_PARTICLES.incrementAndGet();
    }

    public static String getAndResetStats() {
        int builds = INTERCEPTED_BUILDS.getAndSet(0);
        int reused = REUSED_BUFFERS.getAndSet(0);
        int chunks = SKIPPED_CHUNKS.getAndSet(0);
        int entities = CULLED_ENTITIES.getAndSet(0);
        int bes = CULLED_BE.getAndSet(0);
        int particles = CULLED_PARTICLES.getAndSet(0);

        if (builds == 0 && reused == 0 && chunks == 0 && entities == 0 && bes == 0 && particles == 0) {
            return null;
        }

        return String.format("Builds: %d | Reused: %d | Culled E: %d | BE: %d | Part: %d | Chunks: %d",
                builds / 5, reused / 5, entities / 5, bes / 5, particles / 5, chunks / 5);
    }
}