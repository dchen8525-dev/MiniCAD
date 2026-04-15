package com.minicad.step.model;

import java.util.List;

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
public record StepCacheDefinition(
    int id,
    String name,
    String cacheType,
    int cacheCapacity,
    String cachePolicy,
    int cacheTtl,
    String cacheStatus) implements StepEntity {
}