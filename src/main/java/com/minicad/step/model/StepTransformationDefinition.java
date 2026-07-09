package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRANSFORMATION_DEFINITION.
 * A transformation definition entity.
 *
 * @param id STEP instance id
 * @param name transformation name
 * @param transformationType transformation variance type
 * @param transformationInput transformation variance input type
 * @param transformationOutput transformation variance output type
 * @param transformationParameters transformation variance parameters
 * @param transformationStatus transformation variance status
 */
/**
 * Resolved TRANSFORMATION_DEFINITION.
 * A transformation definition entity.
 *
 * @param id STEP instance id
 * @param name transformation name
 * @param transformationType transformation variance type
 * @param transformationInput transformation variance input type
 * @param transformationOutput transformation variance output type
 * @param transformationParameters transformation variance parameters
 * @param transformationStatus transformation variance status
 */
public final class StepTransformationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String transformationType;
    private final String transformationInput;
    private final String transformationOutput;
    private final List<String> transformationParameters;
    private final String transformationStatus;

    public StepTransformationDefinition(int id, String name, String transformationType, String transformationInput, String transformationOutput, List<String> transformationParameters, String transformationStatus) {
        this.id = id;
        this.name = name;
        this.transformationType = transformationType;
        this.transformationInput = transformationInput;
        this.transformationOutput = transformationOutput;
        this.transformationParameters = transformationParameters == null ? null : java.util.List.copyOf(transformationParameters);
        this.transformationStatus = transformationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransformationType() {
        return transformationType;
    }

    public String getTransformationInput() {
        return transformationInput;
    }

    public String getTransformationOutput() {
        return transformationOutput;
    }

    public List<String> getTransformationParameters() {
        return transformationParameters;
    }

    public String getTransformationStatus() {
        return transformationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransformationDefinition that = (StepTransformationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transformationType, that.transformationType) && Objects.equals(transformationInput, that.transformationInput) && Objects.equals(transformationOutput, that.transformationOutput) && Objects.equals(transformationParameters, that.transformationParameters) && Objects.equals(transformationStatus, that.transformationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transformationType, transformationInput, transformationOutput, transformationParameters, transformationStatus);
    }

    @Override
    public String toString() {
        return "StepTransformationDefinition{" + "id=" + id + "name=" + name + "transformationType=" + transformationType + "transformationInput=" + transformationInput + "transformationOutput=" + transformationOutput + "transformationParameters=" + transformationParameters + "transformationStatus=" + transformationStatus + "}";
    }
}