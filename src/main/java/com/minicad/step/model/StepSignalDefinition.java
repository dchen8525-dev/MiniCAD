package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SIGNAL_DEFINITION.
 * A signal definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceSignal defined variance signal
 * @varianceType signal variance type (analog, digital, discrete)
 * @varianceRange signal variance range (min/max)
 * @varianceUnit signal variance unit
 * @varianceFrequency signal variance frequency
 * @varianceStatus definition variance status
 */
public record StepSignalDefinition(
    int id,
    String name,
    String varianceSignal,
    String varianceType,
    List<Double> varianceRange,
    StepEntity varianceUnit,
    double varianceFrequency,
    String varianceStatus) implements StepEntity {
}