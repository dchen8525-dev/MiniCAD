package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved WAIT_DEFINITION.
 * A wait definition entity.
 *
 * @param id STEP instance id
 * @param name wait name
 * @param waitType wait variance type
 * @param waitCondition wait variance condition
 * @param waitTimeout wait variance timeout
 * @param waitAction wait variance action on timeout
 * @param waitStatus wait variance status
 */
public final class StepWaitDefinition extends AbstractStepEntity {
    private final String waitType;
    private final String waitCondition;
    private final int waitTimeout;
    private final StepEntity waitAction;
    private final String waitStatus;

    public StepWaitDefinition(int id, String name, String waitType, String waitCondition, int waitTimeout, StepEntity waitAction, String waitStatus) {
        super(id, name);
        this.waitType = waitType;
        this.waitCondition = waitCondition;
        this.waitTimeout = waitTimeout;
        this.waitAction = waitAction;
        this.waitStatus = waitStatus;
    }

    public String getWaitType() {
        return waitType;
    }

    public String getWaitCondition() {
        return waitCondition;
    }

    public int getWaitTimeout() {
        return waitTimeout;
    }

    public StepEntity getWaitAction() {
        return waitAction;
    }

    public String getWaitStatus() {
        return waitStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("waitType", waitType);
        state.put("waitCondition", waitCondition);
        state.put("waitTimeout", waitTimeout);
        state.put("waitAction", waitAction);
        state.put("waitStatus", waitStatus);
        return state;
    }
}
