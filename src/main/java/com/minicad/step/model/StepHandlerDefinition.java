package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepHandlerDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String handlerType;
    private final String handlerCondition;
    private final StepEntity handlerAction;
    private final int handlerPriority;
    private final String handlerStatus;

    public StepHandlerDefinition(int id, String name, String handlerType, String handlerCondition, StepEntity handlerAction, int handlerPriority, String handlerStatus) {
        this.id = id;
        this.name = name;
        this.handlerType = handlerType;
        this.handlerCondition = handlerCondition;
        this.handlerAction = handlerAction;
        this.handlerPriority = handlerPriority;
        this.handlerStatus = handlerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHandlerDefinition that = (StepHandlerDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(handlerType, that.handlerType) && Objects.equals(handlerCondition, that.handlerCondition) && Objects.equals(handlerAction, that.handlerAction) && handlerPriority == that.handlerPriority && Objects.equals(handlerStatus, that.handlerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, handlerType, handlerCondition, handlerAction, handlerPriority, handlerStatus);
    }

    @Override
    public String toString() {
        return "StepHandlerDefinition{" + "id=" + id + "name=" + name + "handlerType=" + handlerType + "handlerCondition=" + handlerCondition + "handlerAction=" + handlerAction + "handlerPriority=" + handlerPriority + "handlerStatus=" + handlerStatus + "}";
    }
}