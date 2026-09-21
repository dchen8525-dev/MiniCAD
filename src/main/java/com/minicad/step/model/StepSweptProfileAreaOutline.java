package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SWEPT_PROFILE_AREA_OUTLINE.
 */
public final class StepSweptProfileAreaOutline extends AbstractStepEntity {
    private final StepEntity profileDef;

    public StepSweptProfileAreaOutline(int id, String name, StepEntity profileDef) {
        super(id, name);
        this.profileDef = profileDef;
    }

    public StepEntity getProfileDef() {
        return profileDef;
    }

    // Record-style accessor
    public StepEntity profileDef() { return profileDef; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("profileDef", profileDef);
        return state;
    }
}
