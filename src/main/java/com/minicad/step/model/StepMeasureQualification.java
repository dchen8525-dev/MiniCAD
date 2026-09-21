package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MEASURE_QUALIFICATION.
 * Qualification of a measure value.
 *
 * @param id STEP instance id
 * @param name qualification name
 * @param qualifiedMeasure qualified measure reference
 * @param qualifiers list of qualifiers
 */
public final class StepMeasureQualification extends AbstractStepEntity {
    private final StepEntity qualifiedMeasure;
    private final List<StepEntity> qualifiers;

    public StepMeasureQualification(int id, String name, StepEntity qualifiedMeasure, List<StepEntity> qualifiers) {
        super(id, name);
        this.qualifiedMeasure = qualifiedMeasure;
        this.qualifiers = qualifiers == null ? null : java.util.List.copyOf(qualifiers);
    }

    public StepEntity getQualifiedMeasure() {
        return qualifiedMeasure;
    }

    public List<StepEntity> getQualifiers() {
        return qualifiers;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("qualifiedMeasure", qualifiedMeasure);
        state.put("qualifiers", qualifiers);
        return state;
    }
}
