package com.minicad.step.model;

/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_VALUE.
 * A datum reference modifier with an associated value (e.g., maximum material condition value).
 */
public record StepDatumReferenceModifierWithValue(
    int id,
    String name,
    String modifierType,
    Double modifierValue,
    StepEntity modifierUnit,
    StepEntity referencedDatum
) implements StepEntity {}
