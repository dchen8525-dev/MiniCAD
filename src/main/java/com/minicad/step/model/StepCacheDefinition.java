package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CACHE_DEFINITION.
 * A cache definition entity.
 *
 * @param id STEP instance id
 * @param name cache name
 * @param cacheType cache variance type
 * @param cacheCapacity cache variance capacity
 * @param cachePolicy cache variance eviction policy
 * @param cacheTtl cache variance TTL in seconds
 * @param cacheStatus cache variance status
 */
public final class StepCacheDefinition extends AbstractStepEntity {
    private final String cacheType;
    private final int cacheCapacity;
    private final String cachePolicy;
    private final int cacheTtl;
    private final String cacheStatus;

    public StepCacheDefinition(int id, String name, String cacheType, int cacheCapacity, String cachePolicy, int cacheTtl, String cacheStatus) {
        super(id, name);
        this.cacheType = cacheType;
        this.cacheCapacity = cacheCapacity;
        this.cachePolicy = cachePolicy;
        this.cacheTtl = cacheTtl;
        this.cacheStatus = cacheStatus;
    }

    public String getCacheType() {
        return cacheType;
    }

    public int getCacheCapacity() {
        return cacheCapacity;
    }

    public String getCachePolicy() {
        return cachePolicy;
    }

    public int getCacheTtl() {
        return cacheTtl;
    }

    public String getCacheStatus() {
        return cacheStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("cacheType", cacheType);
        state.put("cacheCapacity", cacheCapacity);
        state.put("cachePolicy", cachePolicy);
        state.put("cacheTtl", cacheTtl);
        state.put("cacheStatus", cacheStatus);
        return state;
    }
}
