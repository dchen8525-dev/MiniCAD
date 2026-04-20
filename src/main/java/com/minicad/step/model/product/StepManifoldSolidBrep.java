package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MANIFOLD_SOLID_BREP.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 */
public record StepManifoldSolidBrep(int id, String name, StepEntity outer) implements StepEntity {
}
