package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HANDLER_INSTANCE.
 * A handler instance entity.
 *
 * @param id STEP instance id
 * @param name handler instance name
 * @param handlerDefinition handler variance definition reference
 * @param handlerState handler variance state
 * @param handlerTriggered handler variance triggered flag
 * @param handlerExecutionTime handler variance execution time
 * @param handlerStatus handler variance status
 */
public final class StepHandlerInstance extends AbstractStepEntity {
    private final StepEntity handlerDefinition;
    private final String handlerState;
    private final boolean handlerTriggered;
    private final StepEntity handlerExecutionTime;
    private final String handlerStatus;

    public StepHandlerInstance(int id, String name, StepEntity handlerDefinition, String handlerState, boolean handlerTriggered, StepEntity handlerExecutionTime, String handlerStatus) {
        super(id, name);
        this.handlerDefinition = handlerDefinition;
        this.handlerState = handlerState;
        this.handlerTriggered = handlerTriggered;
        this.handlerExecutionTime = handlerExecutionTime;
        this.handlerStatus = handlerStatus;
    }

    public StepEntity getHandlerDefinition() {
        return handlerDefinition;
    }

    public String getHandlerState() {
        return handlerState;
    }

    public boolean isHandlerTriggered() {
        return handlerTriggered;
    }

    public StepEntity getHandlerExecutionTime() {
        return handlerExecutionTime;
    }

    public String getHandlerStatus() {
        return handlerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("handlerDefinition", handlerDefinition);
        state.put("handlerState", handlerState);
        state.put("handlerTriggered", handlerTriggered);
        state.put("handlerExecutionTime", handlerExecutionTime);
        state.put("handlerStatus", handlerStatus);
        return state;
    }
}
