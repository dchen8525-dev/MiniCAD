package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_SIGN.
 */
public final class StepDatumReferenceModifierWithSign extends AbstractStepEntity {
    private final StepEntity modifier;
    private final String sign;

    public StepDatumReferenceModifierWithSign(int id, String name, StepEntity modifier, String sign) {
        super(id, name);
        this.modifier = modifier;
        this.sign = sign;
    }

    public StepEntity getModifier() {
        return modifier;
    }

    public String getSign() {
        return sign;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("modifier", modifier);
        state.put("sign", sign);
        return state;
    }
}
