package com.minicad.step.model.profile;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved AREA_PROFILE.
 */
/**
 * Resolved AREA_PROFILE.
 */
public final class StepAreaProfile implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profileDef;

    public StepAreaProfile(int id, String name, StepEntity profileDef) {
        this.id = id;
        this.name = name;
        this.profileDef = profileDef;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getProfileDef() {
        return profileDef;
    }

    // Record-style accessor
    public StepEntity profileDef() { return profileDef; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAreaProfile that = (StepAreaProfile) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profileDef, that.profileDef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profileDef);
    }

    @Override
    public String toString() {
        return "StepAreaProfile{" + "id=" + id + "name=" + name + "profileDef=" + profileDef + "}";
    }
}
