package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PROCESS_STEP_REPRESENTATION.
 * A process step representation entity.
 *
 * @param id STEP instance id
 * @param name step name
 * @param stepType step type
 * @param stepParameters step parameters
 * @param operations operations in this step
 */
/**
 * Resolved PROCESS_STEP_REPRESENTATION.
 * A process step representation entity.
 *
 * @param id STEP instance id
 * @param name step name
 * @param stepType step type
 * @param stepParameters step parameters
 * @param operations operations in this step
 */
public final class StepProcessStepRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final String stepType;
    private final List<StepEntity> stepParameters;
    private final List<StepEntity> operations;

    public StepProcessStepRepresentation(int id, String name, String stepType, List<StepEntity> stepParameters, List<StepEntity> operations) {
        this.id = id;
        this.name = name;
        this.stepType = stepType;
        this.stepParameters = stepParameters == null ? null : java.util.List.copyOf(stepParameters);
        this.operations = operations == null ? null : java.util.List.copyOf(operations);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStepType() {
        return stepType;
    }

    public List<StepEntity> getStepParameters() {
        return stepParameters;
    }

    public List<StepEntity> getOperations() {
        return operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProcessStepRepresentation that = (StepProcessStepRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(stepType, that.stepType) && Objects.equals(stepParameters, that.stepParameters) && Objects.equals(operations, that.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stepType, stepParameters, operations);
    }

    @Override
    public String toString() {
        return "StepProcessStepRepresentation{" + "id=" + id + "name=" + name + "stepType=" + stepType + "stepParameters=" + stepParameters + "operations=" + operations + "}";
    }
}