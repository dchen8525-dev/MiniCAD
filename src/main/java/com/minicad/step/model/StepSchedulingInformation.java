package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SCHEDULING_INFORMATION.
 * A scheduling information entity.
 *
 * @param id STEP instance id
 * @param name scheduling name
 * @param plannedStart planned start time
 * @param plannedEnd planned end time
 * @param actualStart actual start time
 * @param actualEnd actual end time
 * @param schedulingStatus scheduling status (planned, started, completed)
 * @param schedulingDependencies scheduling dependencies
 */
public final class StepSchedulingInformation extends AbstractStepEntity {
    private final double plannedStart;
    private final double plannedEnd;
    private final double actualStart;
    private final double actualEnd;
    private final String schedulingStatus;
    private final List<StepEntity> schedulingDependencies;

    public StepSchedulingInformation(int id, String name, double plannedStart, double plannedEnd, double actualStart, double actualEnd, String schedulingStatus, List<StepEntity> schedulingDependencies) {
        super(id, name);
        this.plannedStart = plannedStart;
        this.plannedEnd = plannedEnd;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.schedulingStatus = schedulingStatus;
        this.schedulingDependencies = schedulingDependencies == null ? null : java.util.List.copyOf(schedulingDependencies);
    }

    public double getPlannedStart() {
        return plannedStart;
    }

    public double getPlannedEnd() {
        return plannedEnd;
    }

    public double getActualStart() {
        return actualStart;
    }

    public double getActualEnd() {
        return actualEnd;
    }

    public String getSchedulingStatus() {
        return schedulingStatus;
    }

    public List<StepEntity> getSchedulingDependencies() {
        return schedulingDependencies;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("plannedStart", plannedStart);
        state.put("plannedEnd", plannedEnd);
        state.put("actualStart", actualStart);
        state.put("actualEnd", actualEnd);
        state.put("schedulingStatus", schedulingStatus);
        state.put("schedulingDependencies", schedulingDependencies);
        return state;
    }
}
