package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved PROJECTED_ZONE_DEFINITION.
 * A projected tolerance zone definition entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param description zone description
 * @param projectedZone the projected zone entity reference
 * @param applied whether the projected zone is applied
 */
/**
 * Resolved PROJECTED_ZONE_DEFINITION.
 * A projected tolerance zone definition entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param description zone description
 * @param projectedZone the projected zone entity reference
 * @param applied whether the projected zone is applied
 */
public final class StepProjectedZoneDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity projectedZone;
    private final boolean applied;

    public StepProjectedZoneDefinition(int id, String name, String description, StepEntity projectedZone, boolean applied) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectedZone = projectedZone;
        this.applied = applied;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getProjectedZone() {
        return projectedZone;
    }

    public boolean isApplied() {
        return applied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProjectedZoneDefinition that = (StepProjectedZoneDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(projectedZone, that.projectedZone) && applied == that.applied;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, projectedZone, applied);
    }

    @Override
    public String toString() {
        return "StepProjectedZoneDefinition{" + "id=" + id + "name=" + name + "description=" + description + "projectedZone=" + projectedZone + "applied=" + applied + "}";
    }
}
