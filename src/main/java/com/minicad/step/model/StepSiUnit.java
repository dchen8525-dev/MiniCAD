package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SI unit definition.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param prefix optional SI prefix enum name
 * @param unitName SI base unit enum name
 */
public final class StepSiUnit extends AbstractStepEntity {
    private final String unitKind;
    private final String prefix;
    private final String unitName;

    public StepSiUnit(int id, String unitKind, String prefix, String unitName) {
        super(id, "");
        this.unitKind = unitKind;
        this.prefix = prefix;
        this.unitName = unitName;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getUnitName() {
        return unitName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String unitKind() { return unitKind; }
    public String prefix() { return prefix; }
    public String unitName() { return unitName; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("unitKind", unitKind);
        state.put("prefix", prefix);
        state.put("unitName", unitName);
        return state;
    }
}
