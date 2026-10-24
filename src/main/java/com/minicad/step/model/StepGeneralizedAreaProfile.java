package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved GENERALIZED_AREA_PROFILE.
 */
public final class StepGeneralizedAreaProfile extends AbstractStepEntity {
    private final StepEntity profileDef;

    public StepGeneralizedAreaProfile(int id, String name, StepEntity profileDef) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "profileDef", profileDef);
    }
}
