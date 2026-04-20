package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONNECTION_DEFINITION.
 * A connection definition entity.
 *
 * @param id STEP instance id
 * @param name connection name
 * @param connectionType connection variance type
 * @param connectionProtocol connection variance protocol
 * @param connectionParameters connection variance parameters
 * @param connectionQuality connection variance quality requirements
 * @param connectionStatus connection variance status
 */
public record StepConnectionDefinition2(
    int id,
    String name,
    String connectionType,
    String connectionProtocol,
    List<String> connectionParameters,
    String connectionQuality,
    String connectionStatus) implements StepEntity {
}