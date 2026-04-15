package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FILTER_DEFINITION.
 * A filter definition entity.
 *
 * @param id STEP instance id
 * @param name filter name
 * @param filterType filter variance type
 * @param filterExpression filter variance expression
 * @param filterConditions filter variance conditions
 * @param filterPriority filter variance priority
 * @param filterStatus filter variance status
 */
public record StepFilterDefinition(
    int id,
    String name,
    String filterType,
    String filterExpression,
    List<String> filterConditions,
    int filterPriority,
    String filterStatus) implements StepEntity {
}