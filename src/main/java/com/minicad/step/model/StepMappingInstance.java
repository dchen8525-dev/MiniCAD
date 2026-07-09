package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMappingInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity mappingDefinition;
    private final List<String> mappingInput;
    private final List<String> mappingOutput;
    private final String mappingStatus;

    public StepMappingInstance(int id, String name, StepEntity mappingDefinition, List<String> mappingInput, List<String> mappingOutput, String mappingStatus) {
        this.id = id;
        this.name = name;
        this.mappingDefinition = mappingDefinition;
        this.mappingInput = mappingInput == null ? null : java.util.List.copyOf(mappingInput);
        this.mappingOutput = mappingOutput == null ? null : java.util.List.copyOf(mappingOutput);
        this.mappingStatus = mappingStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMappingInstance that = (StepMappingInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(mappingDefinition, that.mappingDefinition) && Objects.equals(mappingInput, that.mappingInput) && Objects.equals(mappingOutput, that.mappingOutput) && Objects.equals(mappingStatus, that.mappingStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mappingDefinition, mappingInput, mappingOutput, mappingStatus);
    }

    @Override
    public String toString() {
        return "StepMappingInstance{" + "id=" + id + "name=" + name + "mappingDefinition=" + mappingDefinition + "mappingInput=" + mappingInput + "mappingOutput=" + mappingOutput + "mappingStatus=" + mappingStatus + "}";
    }
}