package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved NON_MANIFOLD_SOLID_BREP.
 * A B-rep solid whose boundary may be a non-manifold shell.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param outer the surface (open or closed shell) forming the boundary
 */
public record StepNonManifoldSolidBrep(
    int id,
    String name,
    StepEntity outer) implements StepEntity {
}
