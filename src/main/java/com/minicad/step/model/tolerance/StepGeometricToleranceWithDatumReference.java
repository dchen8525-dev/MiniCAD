package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE.
 * A geometric tolerance with datum reference entity.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param toleranceType tolerance type
 * @param magnitude tolerance magnitude
 * @param magnitudeUnit tolerance unit
 * @param tolerancedFeature the feature being toleranced
 * * @param datumReference datum reference entity
 */
public record StepGeometricToleranceWithDatumReference(
    int id,
    String name,
    String toleranceType,
    Double magnitude,
    StepEntity magnitudeUnit,
    StepEntity tolerancedFeature,
    StepEntity datumReference) implements StepEntity {
}