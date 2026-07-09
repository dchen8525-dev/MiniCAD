package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRANSFORMATION_INSTANCE.
 * A transformation instance entity.
 *
 * @param id STEP instance id
 * @param name transformation instance name
 * @param transformationDefinition transformation variance definition reference
 * @param transformationState transformation variance state
 * @param transformationInputData transformation variance input data
 * @param transformationOutputData transformation variance output data
 * @param transformationStatus transformation variance status
 */
/**
 * Resolved TRANSFORMATION_INSTANCE.
 * A transformation instance entity.
 *
 * @param id STEP instance id
 * @param name transformation instance name
 * @param transformationDefinition transformation variance definition reference
 * @param transformationState transformation variance state
 * @param transformationInputData transformation variance input data
 * @param transformationOutputData transformation variance output data
 * @param transformationStatus transformation variance status
 */
public final class StepTransformationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity transformationDefinition;
    private final String transformationState;
    private final List<String> transformationInputData;
    private final List<String> transformationOutputData;
    private final String transformationStatus;

    public StepTransformationInstance(int id, String name, StepEntity transformationDefinition, String transformationState, List<String> transformationInputData, List<String> transformationOutputData, String transformationStatus) {
        this.id = id;
        this.name = name;
        this.transformationDefinition = transformationDefinition;
        this.transformationState = transformationState;
        this.transformationInputData = transformationInputData == null ? null : java.util.List.copyOf(transformationInputData);
        this.transformationOutputData = transformationOutputData == null ? null : java.util.List.copyOf(transformationOutputData);
        this.transformationStatus = transformationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTransformationDefinition() {
        return transformationDefinition;
    }

    public String getTransformationState() {
        return transformationState;
    }

    public List<String> getTransformationInputData() {
        return transformationInputData;
    }

    public List<String> getTransformationOutputData() {
        return transformationOutputData;
    }

    public String getTransformationStatus() {
        return transformationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransformationInstance that = (StepTransformationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transformationDefinition, that.transformationDefinition) && Objects.equals(transformationState, that.transformationState) && Objects.equals(transformationInputData, that.transformationInputData) && Objects.equals(transformationOutputData, that.transformationOutputData) && Objects.equals(transformationStatus, that.transformationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transformationDefinition, transformationState, transformationInputData, transformationOutputData, transformationStatus);
    }

    @Override
    public String toString() {
        return "StepTransformationInstance{" + "id=" + id + "name=" + name + "transformationDefinition=" + transformationDefinition + "transformationState=" + transformationState + "transformationInputData=" + transformationInputData + "transformationOutputData=" + transformationOutputData + "transformationStatus=" + transformationStatus + "}";
    }
}