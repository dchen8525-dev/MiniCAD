package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepMappingDefinition extends AbstractStepEntity {
    private final String mappingType;
    private final String mappingSource;
    private final String mappingTarget;
    private final List<String> mappingRules;
    private final String mappingStatus;

    public StepMappingDefinition(int id, String name, String mappingType, String mappingSource, String mappingTarget, List<String> mappingRules, String mappingStatus) {
        super(id, name);
        this.mappingType = mappingType;
        this.mappingSource = mappingSource;
        this.mappingTarget = mappingTarget;
        this.mappingRules = mappingRules == null ? null : java.util.List.copyOf(mappingRules);
        this.mappingStatus = mappingStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("mappingType", mappingType);
        state.put("mappingSource", mappingSource);
        state.put("mappingTarget", mappingTarget);
        state.put("mappingRules", mappingRules);
        state.put("mappingStatus", mappingStatus);
        return state;
    }
}
