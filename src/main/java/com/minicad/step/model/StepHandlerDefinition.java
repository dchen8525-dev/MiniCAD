package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HANDLER_DEFINITION.
 * A handler definition entity.
 *
 * @param id STEP instance id
 * @param name handler name
 * @param handlerType handler variance type
 * @param handlerCondition handler variance trigger condition
 * @param handlerAction handler variance action reference
 * @param handlerPriority handler variance priority
 * @param handlerStatus handler variance status
 */
public final class StepHandlerDefinition extends AbstractStepEntity {
    private final String handlerType;
    private final String handlerCondition;
    private final StepEntity handlerAction;
    private final int handlerPriority;
    private final String handlerStatus;

    public StepHandlerDefinition(int id, String name, String handlerType, String handlerCondition, StepEntity handlerAction, int handlerPriority, String handlerStatus) {
        super(id, name);
        this.handlerType = handlerType;
        this.handlerCondition = handlerCondition;
        this.handlerAction = handlerAction;
        this.handlerPriority = handlerPriority;
        this.handlerStatus = handlerStatus;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public String getHandlerCondition() {
        return handlerCondition;
    }

    public StepEntity getHandlerAction() {
        return handlerAction;
    }

    public int getHandlerPriority() {
        return handlerPriority;
    }

    public String getHandlerStatus() {
        return handlerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("handlerType", handlerType);
        state.put("handlerCondition", handlerCondition);
        state.put("handlerAction", handlerAction);
        state.put("handlerPriority", handlerPriority);
        state.put("handlerStatus", handlerStatus);
        return state;
    }
}
