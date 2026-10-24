package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal context-dependent unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
public final class StepContextDependentUnit extends AbstractStepEntity {
    private final String unitKind;

    public StepContextDependentUnit(int id, String name, String unitKind) {
        super(id, name);
        this.unitKind = unitKind;
    }

    public String getUnitKind() {
        return unitKind;
    }

    // Record-style accessor
    public String unitKind() { return unitKind; }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "unitKind", unitKind);
    }
}
