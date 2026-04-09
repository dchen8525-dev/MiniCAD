package com.minicad.step.model;

/**
 * Minimal context-dependent unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
public record StepContextDependentUnit(int id, String name, String unitKind) implements StepEntity {
}
