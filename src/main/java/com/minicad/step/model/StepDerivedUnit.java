package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal derived unit definition.
 *
 * @param id STEP instance id
 * @param elements unit elements
 * @param unitKind derived unit kind such as FORCE_UNIT
 */
public final class StepDerivedUnit extends AbstractStepEntity {
    private final List<StepDerivedUnitElement> elements;
    private final String unitKind;

    public StepDerivedUnit(int id, List<StepDerivedUnitElement> elements, String unitKind) {
        super(id, "");
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.unitKind = unitKind;
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

    public String unitKind() { return unitKind; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("elements", elements);
        state.put("unitKind", unitKind);
        return state;
    }
}
