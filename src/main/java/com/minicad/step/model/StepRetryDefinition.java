package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RETRY_DEFINITION.
 * A retry definition entity.
 *
 * @param id STEP instance id
 * @param name retry name
 * @param retryType retry variance type
 * @param retryMaxAttempts retry variance max attempts
 * @param retryDelay retry variance delay between attempts
 * @param retryBackoff retry variance backoff strategy
 * @param retryStatus retry variance status
 */
public record StepRetryDefinition(
    int id,
    String name,
    String retryType,
    int retryMaxAttempts,
    int retryDelay,
    String retryBackoff,
    String retryStatus) implements StepEntity {
}