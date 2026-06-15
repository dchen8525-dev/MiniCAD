package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal derived unit definition.
 *
 * @param id STEP instance id
 * @param elements unit elements
 * @param unitKind derived unit kind such as FORCE_UNIT
 */
/**
 * Minimal derived unit definition.
 *
 * @param id STEP instance id
 * @param elements unit elements
 * @param unitKind derived unit kind such as FORCE_UNIT
 */
public final class StepDerivedUnit implements StepEntity {
    private final int id;
    private final List<StepDerivedUnitElement> elements;
    private final String unitKind;

    public StepDerivedUnit(int id, List<StepDerivedUnitElement> elements, String unitKind) {
        this.id = id;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.unitKind = unitKind;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public List<StepDerivedUnitElement> getElements() {
        return elements;
    }

    public String getUnitKind() {
        return unitKind;
    }

    // Record-style accessor
    public List<StepDerivedUnitElement> elements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDerivedUnit that = (StepDerivedUnit) o;
        return id == that.id && Objects.equals(elements, that.elements) && Objects.equals(unitKind, that.unitKind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, elements, unitKind);
    }

    @Override
    public String toString() {
        return "StepDerivedUnit{" + "id=" + id + "elements=" + elements + "unitKind=" + unitKind + "}";
    }
}
