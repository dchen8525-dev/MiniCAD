package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MACHINING_TOOLPATH.
 * A machining toolpath entity.
 *
 * @param id STEP instance id
 * @param name toolpath name
 * @param pathGeometry path geometry curve/curve set
 * @param tool tool used for this path
 * @param pathParameters path parameters (speed, feed, etc.)
 * @param approachStrategy approach strategy configuration
 */
public final class StepMachiningToolpath extends AbstractStepEntity {
    private final StepEntity pathGeometry;
    private final StepEntity tool;
    private final List<StepEntity> pathParameters;
    private final StepEntity approachStrategy;

    public StepMachiningToolpath(int id, String name, StepEntity pathGeometry, StepEntity tool, List<StepEntity> pathParameters, StepEntity approachStrategy) {
        super(id, name);
        this.pathGeometry = pathGeometry;
        this.tool = tool;
        this.pathParameters = pathParameters == null ? null : java.util.List.copyOf(pathParameters);
        this.approachStrategy = approachStrategy;
    }

    public StepEntity getPathGeometry() {
        return pathGeometry;
    }

    public StepEntity getTool() {
        return tool;
    }

    public List<StepEntity> getPathParameters() {
        return pathParameters;
    }

    public StepEntity getApproachStrategy() {
        return approachStrategy;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pathGeometry", pathGeometry);
        state.put("tool", tool);
        state.put("pathParameters", pathParameters);
        state.put("approachStrategy", approachStrategy);
        return state;
    }
}
