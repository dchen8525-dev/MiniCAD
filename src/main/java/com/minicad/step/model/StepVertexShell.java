package com.minicad.step.model;

/**
 * Resolved VERTEX_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param extent defining vertex loop
 */
public record StepVertexShell(int id, String name, StepVertexLoop extent) implements StepEntity {
}
