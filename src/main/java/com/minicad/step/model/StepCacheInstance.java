package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved CACHE_INSTANCE.
 * A cache instance entity.
 *
 * @param id STEP instance id
 * @param name cache instance name
 * @param cacheDefinition cache variance definition reference
 * @param cacheState cache variance state
 * @param cacheEntries cache variance entry count
 * @param cacheHitRate cache variance hit rate
 * @param cacheMissRate cache variance miss rate
 * @param cacheStatus cache variance status
 */
public final class StepCacheInstance extends AbstractStepEntity {
    private final StepEntity cacheDefinition;
    private final String cacheState;
    private final int cacheEntries;
    private final double cacheHitRate;
    private final double cacheMissRate;
    private final String cacheStatus;

    public StepCacheInstance(int id, String name, StepEntity cacheDefinition, String cacheState, int cacheEntries, double cacheHitRate, double cacheMissRate, String cacheStatus) {
        super(id, name);
        this.cacheDefinition = cacheDefinition;
        this.cacheState = cacheState;
        this.cacheEntries = cacheEntries;
        this.cacheHitRate = cacheHitRate;
        this.cacheMissRate = cacheMissRate;
        this.cacheStatus = cacheStatus;
    }

    public StepEntity getCacheDefinition() {
        return cacheDefinition;
    }

    public String getCacheState() {
        return cacheState;
    }

    public int getCacheEntries() {
        return cacheEntries;
    }

    public double getCacheHitRate() {
        return cacheHitRate;
    }

    public double getCacheMissRate() {
        return cacheMissRate;
    }

    public String getCacheStatus() {
        return cacheStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "cacheDefinition", cacheDefinition,
                "cacheState", cacheState, "cacheEntries", cacheEntries, "cacheHitRate", cacheHitRate,
                "cacheMissRate", cacheMissRate, "cacheStatus", cacheStatus);
    }
}
