package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DATUM_REFERENCE_COMPARTMENT.
 * A compartment of a datum reference in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name compartment name
 * @param description compartment description
 * @param ofShape product definition shape
 * @param precedence datum precedence
 * @param referencedDatum referenced datum
 */
public record StepDatumReferenceCompartment(
    int id,
    String name,
    String description,
    StepEntity ofShape,
    int precedence,
    StepEntity referencedDatum) implements StepEntity {
}
