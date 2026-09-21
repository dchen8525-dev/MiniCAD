package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CSG_VOLUME.
 * A CSG solid represented as a volume.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param treeRoot root of the CSG tree
 */
public final class StepCsgVolume extends AbstractStepEntity {
    private final StepEntity treeRoot;

    public StepCsgVolume(int id, String name, StepEntity treeRoot) {
        super(id, name);
        this.treeRoot = treeRoot;
    }

    public StepEntity getTreeRoot() {
        return treeRoot;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity treeRoot() { return getTreeRoot(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("treeRoot", treeRoot);
        return state;
    }
}
