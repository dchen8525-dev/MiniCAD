package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_SIGN.
 */
public record StepDatumReferenceModifierWithSign(
    int id,
    String name,
    StepEntity modifier,
    String sign) implements StepEntity {
}
