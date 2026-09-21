package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MAPPING_INSTANCE.
 * A mapping instance entity.
 *
 * @param id STEP instance id
 * @param name mapping instance name
 * @param mappingDefinition mapping variance definition reference
 * @param mappingInput mapping variance input data
 * @param mappingOutput mapping variance output data
 * @param mappingStatus mapping variance status
 */
public final class StepMappingInstance extends AbstractStepEntity {
    private final StepEntity mappingDefinition;
    private final List<String> mappingInput;
    private final List<String> mappingOutput;
    private final String mappingStatus;

    public StepMappingInstance(int id, String name, StepEntity mappingDefinition, List<String> mappingInput, List<String> mappingOutput, String mappingStatus) {
        super(id, name);
        this.mappingDefinition = mappingDefinition;
        this.mappingInput = mappingInput == null ? null : java.util.List.copyOf(mappingInput);
        this.mappingOutput = mappingOutput == null ? null : java.util.List.copyOf(mappingOutput);
        this.mappingStatus = mappingStatus;
    }

    public StepEntity getMappingDefinition() {
        return mappingDefinition;
    }

    public List<String> getMappingInput() {
        return mappingInput;
    }

    public List<String> getMappingOutput() {
        return mappingOutput;
    }

    public String getMappingStatus() {
        return mappingStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("mappingDefinition", mappingDefinition);
        state.put("mappingInput", mappingInput);
        state.put("mappingOutput", mappingOutput);
        state.put("mappingStatus", mappingStatus);
        return state;
    }
}
