package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DIE_FEATURE.
 * A die feature entity.
 *
 * @param id STEP instance id
 * @param name die name
 * @param dieType die type classification (stamping, forging, extrusion)
 * @param dieGeometry die geometry representation
 * @param dieSurface die working surface
 * @param dieClearance die clearance specification
 * @param dieMaterial die material specification
 */
public record StepDieFeature(
    int id,
    String name,
    String dieType,
    StepEntity dieGeometry,
    StepEntity dieSurface,
    double dieClearance,
    StepEntity dieMaterial) implements StepEntity {
}