package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MODULE_INSTANCE.
 * A module instance entity.
 *
 * @param id STEP instance id
 * @param name module instance name
 * @param moduleDefinition module variance definition reference
 * @param moduleState module variance state
 * @param moduleVersion module variance version
 * @param moduleConfig module variance configuration
 * @param moduleStatus module variance status
 */
public record StepModuleInstance(
    int id,
    String name,
    StepEntity moduleDefinition,
    String moduleState,
    String moduleVersion,
    List<String> moduleConfig,
    String moduleStatus) implements StepEntity {
}