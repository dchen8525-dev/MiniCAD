package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HEAT_AFFECTED_ZONE.
 * A heat affected zone entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param zoneGeometry zone geometry representation
 * @param affectedMaterial affected material properties
 * @param zoneWidth zone width specification
 * @param hardnessChange hardness change in HAZ
 * @param microstructureChange microstructure change description
 */
/**
 * Resolved HEAT_AFFECTED_ZONE.
 * A heat affected zone entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param zoneGeometry zone geometry representation
 * @param affectedMaterial affected material properties
 * @param zoneWidth zone width specification
 * @param hardnessChange hardness change in HAZ
 * @param microstructureChange microstructure change description
 */
public final class StepHeatAffectedZone implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity zoneGeometry;
    private final StepEntity affectedMaterial;
    private final double zoneWidth;
    private final double hardnessChange;
    private final String microstructureChange;

    public StepHeatAffectedZone(int id, String name, StepEntity zoneGeometry, StepEntity affectedMaterial, double zoneWidth, double hardnessChange, String microstructureChange) {
        this.id = id;
        this.name = name;
        this.zoneGeometry = zoneGeometry;
        this.affectedMaterial = affectedMaterial;
        this.zoneWidth = zoneWidth;
        this.hardnessChange = hardnessChange;
        this.microstructureChange = microstructureChange;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getZoneGeometry() {
        return zoneGeometry;
    }

    public StepEntity getAffectedMaterial() {
        return affectedMaterial;
    }

    public double getZoneWidth() {
        return zoneWidth;
    }

    public double getHardnessChange() {
        return hardnessChange;
    }

    public String getMicrostructureChange() {
        return microstructureChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHeatAffectedZone that = (StepHeatAffectedZone) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(zoneGeometry, that.zoneGeometry) && Objects.equals(affectedMaterial, that.affectedMaterial) && zoneWidth == that.zoneWidth && hardnessChange == that.hardnessChange && Objects.equals(microstructureChange, that.microstructureChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, zoneGeometry, affectedMaterial, zoneWidth, hardnessChange, microstructureChange);
    }

    @Override
    public String toString() {
        return "StepHeatAffectedZone{" + "id=" + id + "name=" + name + "zoneGeometry=" + zoneGeometry + "affectedMaterial=" + affectedMaterial + "zoneWidth=" + zoneWidth + "hardnessChange=" + hardnessChange + "microstructureChange=" + microstructureChange + "}";
    }
}