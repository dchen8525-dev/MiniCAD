package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DATUM_SYSTEM.
 * A datum system entity with multiple datum references.
 *
 * @param id STEP instance id
 * @param name system name
 * @param datums ordered list of datums in the system
 * @param systemType datum system type classification
 * @param tolerance tolerance that uses this datum system
 */
/**
 * Resolved DATUM_SYSTEM.
 * A datum system entity with multiple datum references.
 *
 * @param id STEP instance id
 * @param name system name
 * @param datums ordered list of datums in the system
 * @param systemType datum system type classification
 * @param tolerance tolerance that uses this datum system
 */
public final class StepDatumSystem implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> datums;
    private final String systemType;
    private final StepEntity tolerance;

    public StepDatumSystem(int id, String name, List<StepEntity> datums, String systemType, StepEntity tolerance) {
        this.id = id;
        this.name = name;
        this.datums = datums == null ? null : java.util.List.copyOf(datums);
        this.systemType = systemType;
        this.tolerance = tolerance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getDatums() {
        return datums;
    }

    public String getSystemType() {
        return systemType;
    }

    public StepEntity getTolerance() {
        return tolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumSystem that = (StepDatumSystem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(datums, that.datums) && Objects.equals(systemType, that.systemType) && Objects.equals(tolerance, that.tolerance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, datums, systemType, tolerance);
    }

    @Override
    public String toString() {
        return "StepDatumSystem{" + "id=" + id + "name=" + name + "datums=" + datums + "systemType=" + systemType + "tolerance=" + tolerance + "}";
    }
}