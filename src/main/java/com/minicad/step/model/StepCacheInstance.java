package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCacheInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity cacheDefinition;
    private final String cacheState;
    private final int cacheEntries;
    private final double cacheHitRate;
    private final double cacheMissRate;
    private final String cacheStatus;

    public StepCacheInstance(int id, String name, StepEntity cacheDefinition, String cacheState, int cacheEntries, double cacheHitRate, double cacheMissRate, String cacheStatus) {
        this.id = id;
        this.name = name;
        this.cacheDefinition = cacheDefinition;
        this.cacheState = cacheState;
        this.cacheEntries = cacheEntries;
        this.cacheHitRate = cacheHitRate;
        this.cacheMissRate = cacheMissRate;
        this.cacheStatus = cacheStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCacheInstance that = (StepCacheInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(cacheDefinition, that.cacheDefinition) && Objects.equals(cacheState, that.cacheState) && cacheEntries == that.cacheEntries && cacheHitRate == that.cacheHitRate && cacheMissRate == that.cacheMissRate && Objects.equals(cacheStatus, that.cacheStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cacheDefinition, cacheState, cacheEntries, cacheHitRate, cacheMissRate, cacheStatus);
    }

    @Override
    public String toString() {
        return "StepCacheInstance{" + "id=" + id + "name=" + name + "cacheDefinition=" + cacheDefinition + "cacheState=" + cacheState + "cacheEntries=" + cacheEntries + "cacheHitRate=" + cacheHitRate + "cacheMissRate=" + cacheMissRate + "cacheStatus=" + cacheStatus + "}";
    }
}