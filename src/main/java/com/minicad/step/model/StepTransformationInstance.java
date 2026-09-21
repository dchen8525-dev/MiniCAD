package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTransformationInstance extends AbstractStepEntity {
    private final StepEntity transformationDefinition;
    private final String transformationState;
    private final List<String> transformationInputData;
    private final List<String> transformationOutputData;
    private final String transformationStatus;

    public StepTransformationInstance(int id, String name, StepEntity transformationDefinition, String transformationState, List<String> transformationInputData, List<String> transformationOutputData, String transformationStatus) {
        super(id, name);
        this.transformationDefinition = transformationDefinition;
        this.transformationState = transformationState;
        this.transformationInputData = transformationInputData == null ? null : java.util.List.copyOf(transformationInputData);
        this.transformationOutputData = transformationOutputData == null ? null : java.util.List.copyOf(transformationOutputData);
        this.transformationStatus = transformationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transformationDefinition", transformationDefinition);
        state.put("transformationState", transformationState);
        state.put("transformationInputData", transformationInputData);
        state.put("transformationOutputData", transformationOutputData);
        state.put("transformationStatus", transformationStatus);
        return state;
    }
}
