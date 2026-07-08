package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HARDNESS_REPRESENTATION_ITEM.
 * A hardness representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param hardnessValue hardness variance value
 * @param hardnessUnit hardness variance unit reference
 * @param hardnessMethod hardness variance measurement method
 * @param hardnessStatus hardness variance status
 */
/**
 * Resolved HARDNESS_REPRESENTATION_ITEM.
 * A hardness representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param hardnessValue hardness variance value
 * @param hardnessUnit hardness variance unit reference
 * @param hardnessMethod hardness variance measurement method
 * @param hardnessStatus hardness variance status
 */
public final class StepHardnessRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final double hardnessValue;
    private final StepEntity hardnessUnit;
    private final String hardnessMethod;
    private final String hardnessStatus;

    public StepHardnessRepresentationItem(int id, String name, double hardnessValue, StepEntity hardnessUnit, String hardnessMethod, String hardnessStatus) {
        this.id = id;
        this.name = name;
        this.hardnessValue = hardnessValue;
        this.hardnessUnit = hardnessUnit;
        this.hardnessMethod = hardnessMethod;
        this.hardnessStatus = hardnessStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHardnessValue() {
        return hardnessValue;
    }

    public StepEntity getHardnessUnit() {
        return hardnessUnit;
    }

    public String getHardnessMethod() {
        return hardnessMethod;
    }

    public String getHardnessStatus() {
        return hardnessStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHardnessRepresentationItem that = (StepHardnessRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && hardnessValue == that.hardnessValue && Objects.equals(hardnessUnit, that.hardnessUnit) && Objects.equals(hardnessMethod, that.hardnessMethod) && Objects.equals(hardnessStatus, that.hardnessStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hardnessValue, hardnessUnit, hardnessMethod, hardnessStatus);
    }

    @Override
    public String toString() {
        return "StepHardnessRepresentationItem{" + "id=" + id + "name=" + name + "hardnessValue=" + hardnessValue + "hardnessUnit=" + hardnessUnit + "hardnessMethod=" + hardnessMethod + "hardnessStatus=" + hardnessStatus + "}";
    }
}