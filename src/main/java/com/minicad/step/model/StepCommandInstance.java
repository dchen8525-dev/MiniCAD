package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMMAND_INSTANCE.
 * A command instance entity.
 *
 * @param id STEP instance id
 * @param name command instance name
 * @param commandDefinition command variance definition reference
 * @param commandState command variance state
 * @param commandParameters command variance parameter values
 * @param commandResult command variance result
 * @param commandStatus command variance status
 */
public record StepCommandInstance(
    int id,
    String name,
    StepEntity commandDefinition,
    String commandState,
    List<String> commandParameters,
    String commandResult,
    String commandStatus) implements StepEntity {
}