package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepMonitorInstance extends AbstractStepEntity {
    private final StepEntity monitorDefinition;
    private final String monitorState;
    private final StepEntity monitorLastCheck;
    private final int monitorAlerts;
    private final String monitorStatus;

    public StepMonitorInstance(int id, String name, StepEntity monitorDefinition, String monitorState, StepEntity monitorLastCheck, int monitorAlerts, String monitorStatus) {
        super(id, name);
        this.monitorDefinition = monitorDefinition;
        this.monitorState = monitorState;
        this.monitorLastCheck = monitorLastCheck;
        this.monitorAlerts = monitorAlerts;
        this.monitorStatus = monitorStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("monitorDefinition", monitorDefinition);
        state.put("monitorState", monitorState);
        state.put("monitorLastCheck", monitorLastCheck);
        state.put("monitorAlerts", monitorAlerts);
        state.put("monitorStatus", monitorStatus);
        return state;
    }
}
