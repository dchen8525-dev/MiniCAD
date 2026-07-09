package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LOGGING_SPECIFICATION.
 * A logging specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceEvents logged variance events
 * @varianceFormat log variance format
 * @varianceLevel log variance level (info, warning, error)
 * @varianceDestination log variance destination
 * @varianceRetention retention variance period
 * @varianceStatus specification variance status
 */
/**
 * Resolved LOGGING_SPECIFICATION.
 * A logging specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceEvents logged variance events
 * @varianceFormat log variance format
 * @varianceLevel log variance level (info, warning, error)
 * @varianceDestination log variance destination
 * @varianceRetention retention variance period
 * @varianceStatus specification variance status
 */
public final class StepLoggingSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final List<String> varianceEvents;
    private final String varianceFormat;
    private final String varianceLevel;
    private final String varianceDestination;
    private final double varianceRetention;
    private final String varianceStatus;

    public StepLoggingSpecification(int id, String name, List<String> varianceEvents, String varianceFormat, String varianceLevel, String varianceDestination, double varianceRetention, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.varianceFormat = varianceFormat;
        this.varianceLevel = varianceLevel;
        this.varianceDestination = varianceDestination;
        this.varianceRetention = varianceRetention;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getVarianceEvents() {
        return varianceEvents;
    }

    public String getVarianceFormat() {
        return varianceFormat;
    }

    public String getVarianceLevel() {
        return varianceLevel;
    }

    public String getVarianceDestination() {
        return varianceDestination;
    }

    public double getVarianceRetention() {
        return varianceRetention;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLoggingSpecification that = (StepLoggingSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEvents, that.varianceEvents) && Objects.equals(varianceFormat, that.varianceFormat) && Objects.equals(varianceLevel, that.varianceLevel) && Objects.equals(varianceDestination, that.varianceDestination) && varianceRetention == that.varianceRetention && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEvents, varianceFormat, varianceLevel, varianceDestination, varianceRetention, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepLoggingSpecification{" + "id=" + id + "name=" + name + "varianceEvents=" + varianceEvents + "varianceFormat=" + varianceFormat + "varianceLevel=" + varianceLevel + "varianceDestination=" + varianceDestination + "varianceRetention=" + varianceRetention + "varianceStatus=" + varianceStatus + "}";
    }
}