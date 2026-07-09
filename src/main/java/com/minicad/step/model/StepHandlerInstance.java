package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepHandlerInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity handlerDefinition;
    private final String handlerState;
    private final boolean handlerTriggered;
    private final StepEntity handlerExecutionTime;
    private final String handlerStatus;

    public StepHandlerInstance(int id, String name, StepEntity handlerDefinition, String handlerState, boolean handlerTriggered, StepEntity handlerExecutionTime, String handlerStatus) {
        this.id = id;
        this.name = name;
        this.handlerDefinition = handlerDefinition;
        this.handlerState = handlerState;
        this.handlerTriggered = handlerTriggered;
        this.handlerExecutionTime = handlerExecutionTime;
        this.handlerStatus = handlerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHandlerInstance that = (StepHandlerInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(handlerDefinition, that.handlerDefinition) && Objects.equals(handlerState, that.handlerState) && handlerTriggered == that.handlerTriggered && Objects.equals(handlerExecutionTime, that.handlerExecutionTime) && Objects.equals(handlerStatus, that.handlerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, handlerDefinition, handlerState, handlerTriggered, handlerExecutionTime, handlerStatus);
    }

    @Override
    public String toString() {
        return "StepHandlerInstance{" + "id=" + id + "name=" + name + "handlerDefinition=" + handlerDefinition + "handlerState=" + handlerState + "handlerTriggered=" + handlerTriggered + "handlerExecutionTime=" + handlerExecutionTime + "handlerStatus=" + handlerStatus + "}";
    }
}