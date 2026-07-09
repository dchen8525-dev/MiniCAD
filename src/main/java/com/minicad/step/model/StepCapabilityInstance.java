package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CAPABILITY_INSTANCE.
 * A capability instance entity.
 *
 * @param id STEP instance id
 * @param name capability instance name
 * @param capabilityDefinition capability variance definition reference
 * @param capabilityState capability variance state
 * @param capabilityScore capability variance score
 * @param capabilityHistory capability variance history records
 * @param capabilityStatus capability variance status
 */
/**
 * Resolved CAPABILITY_INSTANCE.
 * A capability instance entity.
 *
 * @param id STEP instance id
 * @param name capability instance name
 * @param capabilityDefinition capability variance definition reference
 * @param capabilityState capability variance state
 * @param capabilityScore capability variance score
 * @param capabilityHistory capability variance history records
 * @param capabilityStatus capability variance status
 */
public final class StepCapabilityInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity capabilityDefinition;
    private final String capabilityState;
    private final double capabilityScore;
    private final List<String> capabilityHistory;
    private final String capabilityStatus;

    public StepCapabilityInstance(int id, String name, StepEntity capabilityDefinition, String capabilityState, double capabilityScore, List<String> capabilityHistory, String capabilityStatus) {
        this.id = id;
        this.name = name;
        this.capabilityDefinition = capabilityDefinition;
        this.capabilityState = capabilityState;
        this.capabilityScore = capabilityScore;
        this.capabilityHistory = capabilityHistory == null ? null : java.util.List.copyOf(capabilityHistory);
        this.capabilityStatus = capabilityStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCapabilityDefinition() {
        return capabilityDefinition;
    }

    public String getCapabilityState() {
        return capabilityState;
    }

    public double getCapabilityScore() {
        return capabilityScore;
    }

    public List<String> getCapabilityHistory() {
        return capabilityHistory;
    }

    public String getCapabilityStatus() {
        return capabilityStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCapabilityInstance that = (StepCapabilityInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(capabilityDefinition, that.capabilityDefinition) && Objects.equals(capabilityState, that.capabilityState) && capabilityScore == that.capabilityScore && Objects.equals(capabilityHistory, that.capabilityHistory) && Objects.equals(capabilityStatus, that.capabilityStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, capabilityDefinition, capabilityState, capabilityScore, capabilityHistory, capabilityStatus);
    }

    @Override
    public String toString() {
        return "StepCapabilityInstance{" + "id=" + id + "name=" + name + "capabilityDefinition=" + capabilityDefinition + "capabilityState=" + capabilityState + "capabilityScore=" + capabilityScore + "capabilityHistory=" + capabilityHistory + "capabilityStatus=" + capabilityStatus + "}";
    }
}