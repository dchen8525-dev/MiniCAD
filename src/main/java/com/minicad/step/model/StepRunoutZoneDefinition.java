package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved RUNOUT_ZONE_DEFINITION.
 * Defines the orientation and form of a runout tolerance zone.
 */
/**
 * Resolved RUNOUT_ZONE_DEFINITION.
 * Defines the orientation and form of a runout tolerance zone.
 */
public final class StepRunoutZoneDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity zoneForm;

    public StepRunoutZoneDefinition(int id, String name, StepEntity zoneForm) {
        this.id = id;
        this.name = name;
        this.zoneForm = zoneForm;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getZoneForm() {
        return zoneForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRunoutZoneDefinition that = (StepRunoutZoneDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(zoneForm, that.zoneForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, zoneForm);
    }

    @Override
    public String toString() {
        return "StepRunoutZoneDefinition{" + "id=" + id + "name=" + name + "zoneForm=" + zoneForm + "}";
    }
}
