package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOOP_DEFINITION.
 * A loop definition entity.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param loopType loop variance type
 * @param loopCondition loop variance condition
 * @param loopBody loop variance body reference
 * @param loopMaxIterations loop variance max iterations
 * @param loopStatus loop variance status
 */
public final class StepLoopDefinition extends AbstractStepEntity {
    private final String loopType;
    private final String loopCondition;
    private final StepEntity loopBody;
    private final int loopMaxIterations;
    private final String loopStatus;

    public StepLoopDefinition(int id, String name, String loopType, String loopCondition, StepEntity loopBody, int loopMaxIterations, String loopStatus) {
        super(id, name);
        this.loopType = loopType;
        this.loopCondition = loopCondition;
        this.loopBody = loopBody;
        this.loopMaxIterations = loopMaxIterations;
        this.loopStatus = loopStatus;
    }

    public String getLoopType() {
        return loopType;
    }

    public String getLoopCondition() {
        return loopCondition;
    }

    public StepEntity getLoopBody() {
        return loopBody;
    }

    public int getLoopMaxIterations() {
        return loopMaxIterations;
    }

    public String getLoopStatus() {
        return loopStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("loopType", loopType);
        state.put("loopCondition", loopCondition);
        state.put("loopBody", loopBody);
        state.put("loopMaxIterations", loopMaxIterations);
        state.put("loopStatus", loopStatus);
        return state;
    }
}
