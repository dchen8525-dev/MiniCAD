package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CSG_SOLID parse-only solid model.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param treeRootExpression CSG tree root expression
 */
public final class StepCsgSolid extends AbstractStepEntity {
    private final StepEntity treeRootExpression;

    public StepCsgSolid(int id, String name, StepEntity treeRootExpression) {
        super(id, name);
        this.treeRootExpression = treeRootExpression;
    }

    public StepEntity getTreeRootExpression() {
        return treeRootExpression;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity treeRootExpression() { return getTreeRootExpression(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("treeRootExpression", treeRootExpression);
        return state;
    }
}
