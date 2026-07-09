package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepWaitDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String waitType;
    private final String waitCondition;
    private final int waitTimeout;
    private final StepEntity waitAction;
    private final String waitStatus;

    public StepWaitDefinition(int id, String name, String waitType, String waitCondition, int waitTimeout, StepEntity waitAction, String waitStatus) {
        this.id = id;
        this.name = name;
        this.waitType = waitType;
        this.waitCondition = waitCondition;
        this.waitTimeout = waitTimeout;
        this.waitAction = waitAction;
        this.waitStatus = waitStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWaitDefinition that = (StepWaitDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(waitType, that.waitType) && Objects.equals(waitCondition, that.waitCondition) && waitTimeout == that.waitTimeout && Objects.equals(waitAction, that.waitAction) && Objects.equals(waitStatus, that.waitStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, waitType, waitCondition, waitTimeout, waitAction, waitStatus);
    }

    @Override
    public String toString() {
        return "StepWaitDefinition{" + "id=" + id + "name=" + name + "waitType=" + waitType + "waitCondition=" + waitCondition + "waitTimeout=" + waitTimeout + "waitAction=" + waitAction + "waitStatus=" + waitStatus + "}";
    }
}