package com.minicad.step.model;

import java.util.List;

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
public record StepCacheInstance(
    int id,
    String name,
    StepEntity cacheDefinition,
    String cacheState,
    int cacheEntries,
    double cacheHitRate,
    double cacheMissRate,
    String cacheStatus) implements StepEntity {
}