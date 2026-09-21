package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedSecurityClassification assigned security classification
 */
public final class StepSecurityClassificationAssignment extends AbstractStepEntity {
    private final StepSecurityClassification assignedSecurityClassification;

    public StepSecurityClassificationAssignment(int id, StepSecurityClassification assignedSecurityClassification) {
        super(id, "");
        this.assignedSecurityClassification = assignedSecurityClassification;
    }

    public StepSecurityClassification getAssignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    // Record-style accessor
    public StepSecurityClassification assignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedSecurityClassification", assignedSecurityClassification);
        return state;
    }
}
