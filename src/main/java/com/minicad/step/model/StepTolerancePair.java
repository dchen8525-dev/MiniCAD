package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TOLERANCE_PAIR.
 * A tolerance pair entity (limits and fits).
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperTolerance upper tolerance value
 * @param lowerTolerance lower tolerance value
 * * @param toleranceUnit tolerance unit
 * @param fitType fit type classification
 */
/**
 * Resolved TOLERANCE_PAIR.
 * A tolerance pair entity (limits and fits).
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperTolerance upper tolerance value
 * @param lowerTolerance lower tolerance value
 * * @param toleranceUnit tolerance unit
 * @param fitType fit type classification
 */
public final class StepTolerancePair implements StepEntity {
    private final int id;
    private final String name;
    private final Double upperTolerance;
    private final Double lowerTolerance;
    private final StepEntity toleranceUnit;
    private final String fitType;

    public StepTolerancePair(int id, String name, Double upperTolerance, Double lowerTolerance, StepEntity toleranceUnit, String fitType) {
        this.id = id;
        this.name = name;
        this.upperTolerance = upperTolerance;
        this.lowerTolerance = lowerTolerance;
        this.toleranceUnit = toleranceUnit;
        this.fitType = fitType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getUpperTolerance() {
        return upperTolerance;
    }

    public Double getLowerTolerance() {
        return lowerTolerance;
    }

    public StepEntity getToleranceUnit() {
        return toleranceUnit;
    }

    public String getFitType() {
        return fitType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTolerancePair that = (StepTolerancePair) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(upperTolerance, that.upperTolerance) && Objects.equals(lowerTolerance, that.lowerTolerance) && Objects.equals(toleranceUnit, that.toleranceUnit) && Objects.equals(fitType, that.fitType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, upperTolerance, lowerTolerance, toleranceUnit, fitType);
    }

    @Override
    public String toString() {
        return "StepTolerancePair{" + "id=" + id + "name=" + name + "upperTolerance=" + upperTolerance + "lowerTolerance=" + lowerTolerance + "toleranceUnit=" + toleranceUnit + "fitType=" + fitType + "}";
    }
}