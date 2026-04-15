package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOGGING_SPECIFICATION.
 * A logging specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceEvents logged variance events
 * @varianceFormat log variance format
 * @varianceLevel log variance level (info, warning, error)
 * @varianceDestination log variance destination
 * @varianceRetention retention variance period
 * @varianceStatus specification variance status
 */
public record StepLoggingSpecification(
    int id,
    String name,
    List<String> varianceEvents,
    String varianceFormat,
    String varianceLevel,
    String varianceDestination,
    double varianceRetention,
    String varianceStatus) implements StepEntity {
}