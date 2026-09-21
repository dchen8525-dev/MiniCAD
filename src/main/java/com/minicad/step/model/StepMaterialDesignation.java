package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MATERIAL_DESIGNATION.
 * A named material specification.
 *
 * @param id STEP instance id
 * @param name material name
 * @param definitions references defining the material properties
 */
public final class StepMaterialDesignation extends AbstractStepEntity {
    private final List<StepEntity> definitions;

    public StepMaterialDesignation(int id, String name, List<StepEntity> definitions) {
        super(id, name);
        this.definitions = definitions == null ? null : java.util.List.copyOf(definitions);
    }

    public List<StepEntity> getDefinitions() {
        return definitions;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("definitions", definitions);
        return state;
    }
}
