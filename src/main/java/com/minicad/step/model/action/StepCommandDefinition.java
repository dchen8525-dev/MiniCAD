package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMMAND_DEFINITION.
 * A command definition entity.
 *
 * @param id STEP instance id
 * @param name command name
 * @param commandType command variance type
 * @param commandDescription command variance description
 * @param commandParameters command variance parameters
 * @param commandReturnType command variance return type
 * @param commandStatus command variance status
 */
public record StepCommandDefinition(
    int id,
    String name,
    String commandType,
    String commandDescription,
    List<String> commandParameters,
    String commandReturnType,
    String commandStatus) implements StepEntity {
}