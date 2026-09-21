package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepHeatTreatmentRepresentationItem extends AbstractStepEntity {
    private final String treatmentType;
    private final double treatmentTemperature;
    private final int treatmentDuration;
    private final String treatmentStatus;

    public StepHeatTreatmentRepresentationItem(int id, String name, String treatmentType, double treatmentTemperature, int treatmentDuration, String treatmentStatus) {
        super(id, name);
        this.treatmentType = treatmentType;
        this.treatmentTemperature = treatmentTemperature;
        this.treatmentDuration = treatmentDuration;
        this.treatmentStatus = treatmentStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("treatmentType", treatmentType);
        state.put("treatmentTemperature", treatmentTemperature);
        state.put("treatmentDuration", treatmentDuration);
        state.put("treatmentStatus", treatmentStatus);
        return state;
    }
}
