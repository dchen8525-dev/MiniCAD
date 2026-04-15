package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SIGNAL_INSTANCE.
 * A signal instance entity.
 *
 * @param id STEP instance id
 * @param name signal instance name
 * @param signalDefinition signal variance definition reference
 * @param signalSource signal variance source reference
 * @param signalValue signal variance current value
 * @param signalHistory signal variance history samples
 * @param signalStatus signal variance status
 */
public record StepSignalInstance(
    int id,
    String name,
    StepEntity signalDefinition,
    StepEntity signalSource,
    double signalValue,
    List<Double> signalHistory,
    String signalStatus) implements StepEntity {
}