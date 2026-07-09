package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved SWEPT_PROFILE_AREA_OUTLINE.
 */
/**
 * Resolved SWEPT_PROFILE_AREA_OUTLINE.
 */
public final class StepSweptProfileAreaOutline implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profileDef;

    public StepSweptProfileAreaOutline(int id, String name, StepEntity profileDef) {
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
        StepSweptProfileAreaOutline that = (StepSweptProfileAreaOutline) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profileDef, that.profileDef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profileDef);
    }

    @Override
    public String toString() {
        return "StepSweptProfileAreaOutline{" + "id=" + id + "name=" + name + "profileDef=" + profileDef + "}";
    }
}
