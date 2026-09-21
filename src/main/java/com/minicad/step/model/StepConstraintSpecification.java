package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONSTRAINT_SPECIFICATION.
 * A constraint specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param constraintType constraint type (geometric, functional, assembly)
 * @varianceSubject constraint variance subject
 * @varianceValue constraint variance value
 * @varianceTolerance tolerance variance if applicable
 * @varianceUnit constraint variance unit
 * @varianceStatus specification variance status
 */
public final class StepConstraintSpecification extends AbstractStepEntity {
    private final String constraintType;
    private final StepEntity varianceSubject;
    private final double varianceValue;
    private final double varianceTolerance;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepConstraintSpecification(int id, String name, String constraintType, StepEntity varianceSubject, double varianceValue, double varianceTolerance, StepEntity varianceUnit, String varianceStatus) {
        super(id, name);
        this.constraintType = constraintType;
        this.varianceSubject = varianceSubject;
        this.varianceValue = varianceValue;
        this.varianceTolerance = varianceTolerance;
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public StepEntity getVarianceSubject() {
        return varianceSubject;
    }

    public double getVarianceValue() {
        return varianceValue;
    }

    public double getVarianceTolerance() {
        return varianceTolerance;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("constraintType", constraintType);
        state.put("varianceSubject", varianceSubject);
        state.put("varianceValue", varianceValue);
        state.put("varianceTolerance", varianceTolerance);
        state.put("varianceUnit", varianceUnit);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
