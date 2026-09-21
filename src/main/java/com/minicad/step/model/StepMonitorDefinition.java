package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MONITOR_DEFINITION.
 * A monitor definition entity.
 *
 * @param id STEP instance id
 * @param name monitor name
 * @param monitorType monitor variance type
 * @param monitorTarget monitor variance target reference
 * @param monitorMetrics monitor variance metrics
 * @param monitorInterval monitor variance check interval
 * @param monitorThresholds monitor variance thresholds
 * @param monitorStatus monitor variance status
 */
public final class StepMonitorDefinition extends AbstractStepEntity {
    private final String monitorType;
    private final StepEntity monitorTarget;
    private final List<StepEntity> monitorMetrics;
    private final int monitorInterval;
    private final List<StepEntity> monitorThresholds;
    private final String monitorStatus;

    public StepMonitorDefinition(int id, String name, String monitorType, StepEntity monitorTarget, List<StepEntity> monitorMetrics, int monitorInterval, List<StepEntity> monitorThresholds, String monitorStatus) {
        super(id, name);
        this.monitorType = monitorType;
        this.monitorTarget = monitorTarget;
        this.monitorMetrics = monitorMetrics == null ? null : java.util.List.copyOf(monitorMetrics);
        this.monitorInterval = monitorInterval;
        this.monitorThresholds = monitorThresholds == null ? null : java.util.List.copyOf(monitorThresholds);
        this.monitorStatus = monitorStatus;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public StepEntity getMonitorTarget() {
        return monitorTarget;
    }

    public List<StepEntity> getMonitorMetrics() {
        return monitorMetrics;
    }

    public int getMonitorInterval() {
        return monitorInterval;
    }

    public List<StepEntity> getMonitorThresholds() {
        return monitorThresholds;
    }

    public String getMonitorStatus() {
        return monitorStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("monitorType", monitorType);
        state.put("monitorTarget", monitorTarget);
        state.put("monitorMetrics", monitorMetrics);
        state.put("monitorInterval", monitorInterval);
        state.put("monitorThresholds", monitorThresholds);
        state.put("monitorStatus", monitorStatus);
        return state;
    }
}
