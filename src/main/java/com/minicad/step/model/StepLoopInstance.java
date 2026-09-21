package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved LOOP_INSTANCE.
 * A loop instance entity.
 *
 * @param id STEP instance id
 * @param name loop instance name
 * @param loopDefinition loop variance definition reference
 * @param loopState loop variance state
 * @param loopIteration loop variance current iteration
 * @param loopCompleted loop variance completed flag
 * @param loopStatus loop variance status
 */
public final class StepLoopInstance extends AbstractStepEntity {
    private final StepEntity loopDefinition;
    private final String loopState;
    private final int loopIteration;
    private final boolean loopCompleted;
    private final String loopStatus;

    public StepLoopInstance(int id, String name, StepEntity loopDefinition, String loopState, int loopIteration, boolean loopCompleted, String loopStatus) {
        super(id, name);
        this.loopDefinition = loopDefinition;
        this.loopState = loopState;
        this.loopIteration = loopIteration;
        this.loopCompleted = loopCompleted;
        this.loopStatus = loopStatus;
    }

    public StepEntity getLoopDefinition() {
        return loopDefinition;
    }

    public String getLoopState() {
        return loopState;
    }

    public int getLoopIteration() {
        return loopIteration;
    }

    public boolean isLoopCompleted() {
        return loopCompleted;
    }

    public String getLoopStatus() {
        return loopStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("loopDefinition", loopDefinition);
        state.put("loopState", loopState);
        state.put("loopIteration", loopIteration);
        state.put("loopCompleted", loopCompleted);
        state.put("loopStatus", loopStatus);
        return state;
    }
}
