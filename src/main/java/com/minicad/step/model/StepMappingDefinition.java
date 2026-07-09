package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MAPPING_DEFINITION.
 * A mapping definition entity.
 *
 * @param id STEP instance id
 * @param name mapping name
 * @param mappingType mapping variance type
 * @param mappingSource mapping variance source domain
 * @param mappingTarget mapping variance target domain
 * @param mappingRules mapping variance mapping rules
 * @param mappingStatus mapping variance status
 */
/**
 * Resolved MAPPING_DEFINITION.
 * A mapping definition entity.
 *
 * @param id STEP instance id
 * @param name mapping name
 * @param mappingType mapping variance type
 * @param mappingSource mapping variance source domain
 * @param mappingTarget mapping variance target domain
 * @param mappingRules mapping variance mapping rules
 * @param mappingStatus mapping variance status
 */
public final class StepMappingDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String mappingType;
    private final String mappingSource;
    private final String mappingTarget;
    private final List<String> mappingRules;
    private final String mappingStatus;

    public StepMappingDefinition(int id, String name, String mappingType, String mappingSource, String mappingTarget, List<String> mappingRules, String mappingStatus) {
        this.id = id;
        this.name = name;
        this.mappingType = mappingType;
        this.mappingSource = mappingSource;
        this.mappingTarget = mappingTarget;
        this.mappingRules = mappingRules == null ? null : java.util.List.copyOf(mappingRules);
        this.mappingStatus = mappingStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMappingType() {
        return mappingType;
    }

    public String getMappingSource() {
        return mappingSource;
    }

    public String getMappingTarget() {
        return mappingTarget;
    }

    public List<String> getMappingRules() {
        return mappingRules;
    }

    public String getMappingStatus() {
        return mappingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMappingDefinition that = (StepMappingDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(mappingType, that.mappingType) && Objects.equals(mappingSource, that.mappingSource) && Objects.equals(mappingTarget, that.mappingTarget) && Objects.equals(mappingRules, that.mappingRules) && Objects.equals(mappingStatus, that.mappingStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mappingType, mappingSource, mappingTarget, mappingRules, mappingStatus);
    }

    @Override
    public String toString() {
        return "StepMappingDefinition{" + "id=" + id + "name=" + name + "mappingType=" + mappingType + "mappingSource=" + mappingSource + "mappingTarget=" + mappingTarget + "mappingRules=" + mappingRules + "mappingStatus=" + mappingStatus + "}";
    }
}