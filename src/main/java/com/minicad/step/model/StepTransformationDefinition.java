package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepTransformationDefinition extends AbstractStepEntity {
    private final String transformationType;
    private final String transformationInput;
    private final String transformationOutput;
    private final List<String> transformationParameters;
    private final String transformationStatus;

    public StepTransformationDefinition(int id, String name, String transformationType, String transformationInput, String transformationOutput, List<String> transformationParameters, String transformationStatus) {
        super(id, name);
        this.transformationType = transformationType;
        this.transformationInput = transformationInput;
        this.transformationOutput = transformationOutput;
        this.transformationParameters = transformationParameters == null ? null : java.util.List.copyOf(transformationParameters);
        this.transformationStatus = transformationStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "transformationType", transformationType,
                "transformationInput", transformationInput, "transformationOutput", transformationOutput,
                "transformationParameters", transformationParameters,
                "transformationStatus", transformationStatus);
    }
}
