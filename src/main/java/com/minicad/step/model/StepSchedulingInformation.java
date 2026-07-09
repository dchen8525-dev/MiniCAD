package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSchedulingInformation implements StepEntity {
    private final int id;
    private final String name;
    private final double plannedStart;
    private final double plannedEnd;
    private final double actualStart;
    private final double actualEnd;
    private final String schedulingStatus;
    private final List<StepEntity> schedulingDependencies;

    public StepSchedulingInformation(int id, String name, double plannedStart, double plannedEnd, double actualStart, double actualEnd, String schedulingStatus, List<StepEntity> schedulingDependencies) {
        this.id = id;
        this.name = name;
        this.plannedStart = plannedStart;
        this.plannedEnd = plannedEnd;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.schedulingStatus = schedulingStatus;
        this.schedulingDependencies = schedulingDependencies == null ? null : java.util.List.copyOf(schedulingDependencies);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSchedulingInformation that = (StepSchedulingInformation) o;
        return id == that.id && Objects.equals(name, that.name) && plannedStart == that.plannedStart && plannedEnd == that.plannedEnd && actualStart == that.actualStart && actualEnd == that.actualEnd && Objects.equals(schedulingStatus, that.schedulingStatus) && Objects.equals(schedulingDependencies, that.schedulingDependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, plannedStart, plannedEnd, actualStart, actualEnd, schedulingStatus, schedulingDependencies);
    }

    @Override
    public String toString() {
        return "StepSchedulingInformation{" + "id=" + id + "name=" + name + "plannedStart=" + plannedStart + "plannedEnd=" + plannedEnd + "actualStart=" + actualStart + "actualEnd=" + actualEnd + "schedulingStatus=" + schedulingStatus + "schedulingDependencies=" + schedulingDependencies + "}";
    }
}