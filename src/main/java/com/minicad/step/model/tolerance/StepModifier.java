package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MODIFIER.
 */
/**
 * Resolved MODIFIER.
 */
public final class StepModifier implements StepEntity {
    private final int id;
    private final String name;
    private final String modifierValue;

    public StepModifier(int id, String name, String modifierValue) {
        this.id = id;
        this.name = name;
        this.modifierValue = modifierValue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModifierValue() {
        return modifierValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepModifier that = (StepModifier) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modifierValue, that.modifierValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modifierValue);
    }

    @Override
    public String toString() {
        return "StepModifier{" + "id=" + id + "name=" + name + "modifierValue=" + modifierValue + "}";
    }
}
