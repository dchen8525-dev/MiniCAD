package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MONITOR_INSTANCE.
 * A monitor instance entity.
 *
 * @param id STEP instance id
 * @param name monitor instance name
 * @param monitorDefinition monitor variance definition reference
 * @param monitorState monitor variance state
 * @param monitorLastCheck monitor variance last check time
 * @param monitorAlerts monitor variance alert count
 * @param monitorStatus monitor variance status
 */
/**
 * Resolved MONITOR_INSTANCE.
 * A monitor instance entity.
 *
 * @param id STEP instance id
 * @param name monitor instance name
 * @param monitorDefinition monitor variance definition reference
 * @param monitorState monitor variance state
 * @param monitorLastCheck monitor variance last check time
 * @param monitorAlerts monitor variance alert count
 * @param monitorStatus monitor variance status
 */
public final class StepMonitorInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity monitorDefinition;
    private final String monitorState;
    private final StepEntity monitorLastCheck;
    private final int monitorAlerts;
    private final String monitorStatus;

    public StepMonitorInstance(int id, String name, StepEntity monitorDefinition, String monitorState, StepEntity monitorLastCheck, int monitorAlerts, String monitorStatus) {
        this.id = id;
        this.name = name;
        this.monitorDefinition = monitorDefinition;
        this.monitorState = monitorState;
        this.monitorLastCheck = monitorLastCheck;
        this.monitorAlerts = monitorAlerts;
        this.monitorStatus = monitorStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getMonitorDefinition() {
        return monitorDefinition;
    }

    public String getMonitorState() {
        return monitorState;
    }

    public StepEntity getMonitorLastCheck() {
        return monitorLastCheck;
    }

    public int getMonitorAlerts() {
        return monitorAlerts;
    }

    public String getMonitorStatus() {
        return monitorStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMonitorInstance that = (StepMonitorInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(monitorDefinition, that.monitorDefinition) && Objects.equals(monitorState, that.monitorState) && Objects.equals(monitorLastCheck, that.monitorLastCheck) && monitorAlerts == that.monitorAlerts && Objects.equals(monitorStatus, that.monitorStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, monitorDefinition, monitorState, monitorLastCheck, monitorAlerts, monitorStatus);
    }

    @Override
    public String toString() {
        return "StepMonitorInstance{" + "id=" + id + "name=" + name + "monitorDefinition=" + monitorDefinition + "monitorState=" + monitorState + "monitorLastCheck=" + monitorLastCheck + "monitorAlerts=" + monitorAlerts + "monitorStatus=" + monitorStatus + "}";
    }
}