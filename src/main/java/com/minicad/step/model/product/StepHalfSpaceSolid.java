package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal parse-only half-space solid.
 *
 * @param id step id
 * @param name step label
 * @param baseSurface boundary surface
 * @param agreementFlag side agreement flag
 * @param enclosure optional enclosure entity for boxed half spaces
 * @param entityName concrete STEP entity name
 */
public record StepHalfSpaceSolid(
    int id,
    String name,
    StepEntity baseSurface,
    boolean agreementFlag,
    StepEntity enclosure,
    String entityName)
    implements StepEntity {}
