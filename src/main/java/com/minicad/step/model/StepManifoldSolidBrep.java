package com.minicad.step.model;

/**
 * Resolved MANIFOLD_SOLID_BREP.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 */
public record StepManifoldSolidBrep(int id, String name, StepClosedShell outer) implements StepEntity {
}
