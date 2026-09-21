package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PAUSE_DEFINITION.
 * A pause definition entity.
 *
 * @param id STEP instance id
 * @param name pause name
 * @param pauseType pause variance type
 * @param pauseCondition pause variance condition
 * @param pauseResumeCondition pause variance resume condition
 * @param pauseTimeout pause variance max pause time
 * @param pauseStatus pause variance status
 */
public final class StepPauseDefinition extends AbstractStepEntity {
    private final String pauseType;
    private final String pauseCondition;
    private final String pauseResumeCondition;
    private final int pauseTimeout;
    private final String pauseStatus;

    public StepPauseDefinition(int id, String name, String pauseType, String pauseCondition, String pauseResumeCondition, int pauseTimeout, String pauseStatus) {
        super(id, name);
        this.pauseType = pauseType;
        this.pauseCondition = pauseCondition;
        this.pauseResumeCondition = pauseResumeCondition;
        this.pauseTimeout = pauseTimeout;
        this.pauseStatus = pauseStatus;
    }

    public String getPauseType() {
        return pauseType;
    }

    public String getPauseCondition() {
        return pauseCondition;
    }

    public String getPauseResumeCondition() {
        return pauseResumeCondition;
    }

    public int getPauseTimeout() {
        return pauseTimeout;
    }

    public String getPauseStatus() {
        return pauseStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pauseType", pauseType);
        state.put("pauseCondition", pauseCondition);
        state.put("pauseResumeCondition", pauseResumeCondition);
        state.put("pauseTimeout", pauseTimeout);
        state.put("pauseStatus", pauseStatus);
        return state;
    }
}
