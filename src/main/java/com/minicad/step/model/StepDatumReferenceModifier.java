package com.minicad.step.model;

/**
 * Resolved DATUM_REFERENCE_MODIFIER.
 * A modifier applied to a datum reference (e.g., MMB, LMB).
 */
public record StepDatumReferenceModifier(
    int id,
    String name,
    String modifierType,
    StepEntity referencedDatum
) implements StepEntity {}
