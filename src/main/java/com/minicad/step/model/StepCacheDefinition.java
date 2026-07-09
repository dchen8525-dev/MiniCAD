package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCacheDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String cacheType;
    private final int cacheCapacity;
    private final String cachePolicy;
    private final int cacheTtl;
    private final String cacheStatus;

    public StepCacheDefinition(int id, String name, String cacheType, int cacheCapacity, String cachePolicy, int cacheTtl, String cacheStatus) {
        this.id = id;
        this.name = name;
        this.cacheType = cacheType;
        this.cacheCapacity = cacheCapacity;
        this.cachePolicy = cachePolicy;
        this.cacheTtl = cacheTtl;
        this.cacheStatus = cacheStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCacheDefinition that = (StepCacheDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(cacheType, that.cacheType) && cacheCapacity == that.cacheCapacity && Objects.equals(cachePolicy, that.cachePolicy) && cacheTtl == that.cacheTtl && Objects.equals(cacheStatus, that.cacheStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cacheType, cacheCapacity, cachePolicy, cacheTtl, cacheStatus);
    }

    @Override
    public String toString() {
        return "StepCacheDefinition{" + "id=" + id + "name=" + name + "cacheType=" + cacheType + "cacheCapacity=" + cacheCapacity + "cachePolicy=" + cachePolicy + "cacheTtl=" + cacheTtl + "cacheStatus=" + cacheStatus + "}";
    }
}