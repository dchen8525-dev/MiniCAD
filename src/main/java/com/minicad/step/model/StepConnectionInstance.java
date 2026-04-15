package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CONNECTION_INSTANCE.
 * A connection instance entity.
 *
 * @param id STEP instance id
 * @param name connection instance name
 * @param connectionDefinition connection variance definition reference
 * @param connectionState connection variance state
 * @param connectionLatency connection variance latency
 * @param connectionThroughput connection variance throughput
 * @param connectionStatus connection variance status
 */
public record StepConnectionInstance(
    int id,
    String name,
    StepEntity connectionDefinition,
    String connectionState,
    double connectionLatency,
    double connectionThroughput,
    String connectionStatus) implements StepEntity {
}