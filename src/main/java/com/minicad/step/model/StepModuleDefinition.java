package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MODULE_DEFINITION.
 * A module definition entity.
 *
 * @param id STEP instance id
 * @param name module name
 * @param moduleType module variance type
 * @param moduleDescription module variance description
 * @param moduleComponents module variance components
 * @param moduleInterfaces module variance interfaces
 * @param moduleStatus module variance status
 */
public record StepModuleDefinition(
    int id,
    String name,
    String moduleType,
    String moduleDescription,
    List<StepEntity> moduleComponents,
    List<StepEntity> moduleInterfaces,
    String moduleStatus) implements StepEntity {
}