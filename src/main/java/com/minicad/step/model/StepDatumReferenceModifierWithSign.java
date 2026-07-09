package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_SIGN.
 */
/**
 * Resolved DATUM_REFERENCE_MODIFIER_WITH_SIGN.
 */
public final class StepDatumReferenceModifierWithSign implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity modifier;
    private final String sign;

    public StepDatumReferenceModifierWithSign(int id, String name, StepEntity modifier, String sign) {
        this.id = id;
        this.name = name;
        this.modifier = modifier;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getModifier() {
        return modifier;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumReferenceModifierWithSign that = (StepDatumReferenceModifierWithSign) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modifier, that.modifier) && Objects.equals(sign, that.sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modifier, sign);
    }

    @Override
    public String toString() {
        return "StepDatumReferenceModifierWithSign{" + "id=" + id + "name=" + name + "modifier=" + modifier + "sign=" + sign + "}";
    }
}
