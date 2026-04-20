package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DATUM_REFERENCE.
 * A datum reference used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param precedence datum precedence
 * @param referencedDatum referenced shape aspect
 */
public record StepDatumReference(
    int id,
    String name,
    int precedence,
    StepEntity referencedDatum) implements StepEntity {
}
