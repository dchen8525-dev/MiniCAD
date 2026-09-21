package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedCertification assigned certification
 */
public final class StepCertificationAssignment extends AbstractStepEntity {
    private final StepCertification assignedCertification;

    public StepCertificationAssignment(int id, StepCertification assignedCertification) {
        super(id, "");
        this.assignedCertification = assignedCertification;
    }

    public StepCertification getAssignedCertification() {
        return assignedCertification;
    }

    // Record-style accessor - no name field, return empty string
    public String name() {
        return "";
    }

    public StepCertification assignedCertification() {
        return assignedCertification;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedCertification", assignedCertification);
        return state;
    }
}
