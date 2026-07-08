package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HEAT_TREATMENT_REPRESENTATION_ITEM.
 * A heat treatment representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param treatmentType treatment variance type
 * @param treatmentTemperature treatment variance temperature
 * @param treatmentDuration treatment variance duration
 * @param treatmentStatus treatment variance status
 */
/**
 * Resolved HEAT_TREATMENT_REPRESENTATION_ITEM.
 * A heat treatment representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param treatmentType treatment variance type
 * @param treatmentTemperature treatment variance temperature
 * @param treatmentDuration treatment variance duration
 * @param treatmentStatus treatment variance status
 */
public final class StepHeatTreatmentRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final String treatmentType;
    private final double treatmentTemperature;
    private final int treatmentDuration;
    private final String treatmentStatus;

    public StepHeatTreatmentRepresentationItem(int id, String name, String treatmentType, double treatmentTemperature, int treatmentDuration, String treatmentStatus) {
        this.id = id;
        this.name = name;
        this.treatmentType = treatmentType;
        this.treatmentTemperature = treatmentTemperature;
        this.treatmentDuration = treatmentDuration;
        this.treatmentStatus = treatmentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public double getTreatmentTemperature() {
        return treatmentTemperature;
    }

    public int getTreatmentDuration() {
        return treatmentDuration;
    }

    public String getTreatmentStatus() {
        return treatmentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHeatTreatmentRepresentationItem that = (StepHeatTreatmentRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(treatmentType, that.treatmentType) && treatmentTemperature == that.treatmentTemperature && treatmentDuration == that.treatmentDuration && Objects.equals(treatmentStatus, that.treatmentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, treatmentType, treatmentTemperature, treatmentDuration, treatmentStatus);
    }

    @Override
    public String toString() {
        return "StepHeatTreatmentRepresentationItem{" + "id=" + id + "name=" + name + "treatmentType=" + treatmentType + "treatmentTemperature=" + treatmentTemperature + "treatmentDuration=" + treatmentDuration + "treatmentStatus=" + treatmentStatus + "}";
    }
}