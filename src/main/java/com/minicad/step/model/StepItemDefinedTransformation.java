package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal item-defined transformation between two placement items.
 *
 * @param id STEP instance id
 * @param name transformation name
 * @param description optional description
 * @param transformItem1 source placement item
 * @param transformItem2 target placement item
 */
public final class StepItemDefinedTransformation extends AbstractStepEntity {
    private final String description;
    private final StepAxis2Placement3D transformItem1;
    private final StepAxis2Placement3D transformItem2;

    public StepItemDefinedTransformation(int id, String name, String description, StepAxis2Placement3D transformItem1, StepAxis2Placement3D transformItem2) {
        super(id, name);
        this.description = description;
        this.transformItem1 = transformItem1;
        this.transformItem2 = transformItem2;
    }

    public String getDescription() {
        return description;
    }

    public StepAxis2Placement3D getTransformItem1() {
        return transformItem1;
    }

    public StepAxis2Placement3D getTransformItem2() {
        return transformItem2;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String description() { return getDescription(); }
    public StepAxis2Placement3D transformItem1() { return getTransformItem1(); }
    public StepAxis2Placement3D transformItem2() { return getTransformItem2(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("transformItem1", transformItem1);
        state.put("transformItem2", transformItem2);
        return state;
    }
}
