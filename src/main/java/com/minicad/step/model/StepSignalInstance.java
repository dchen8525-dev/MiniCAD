package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SIGNAL_INSTANCE.
 * A signal instance entity.
 *
 * @param id STEP instance id
 * @param name signal instance name
 * @param signalDefinition signal variance definition reference
 * @param signalSource signal variance source reference
 * @param signalValue signal variance current value
 * @param signalHistory signal variance history samples
 * @param signalStatus signal variance status
 */
/**
 * Resolved SIGNAL_INSTANCE.
 * A signal instance entity.
 *
 * @param id STEP instance id
 * @param name signal instance name
 * @param signalDefinition signal variance definition reference
 * @param signalSource signal variance source reference
 * @param signalValue signal variance current value
 * @param signalHistory signal variance history samples
 * @param signalStatus signal variance status
 */
public final class StepSignalInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity signalDefinition;
    private final StepEntity signalSource;
    private final double signalValue;
    private final List<Double> signalHistory;
    private final String signalStatus;

    public StepSignalInstance(int id, String name, StepEntity signalDefinition, StepEntity signalSource, double signalValue, List<Double> signalHistory, String signalStatus) {
        this.id = id;
        this.name = name;
        this.signalDefinition = signalDefinition;
        this.signalSource = signalSource;
        this.signalValue = signalValue;
        this.signalHistory = signalHistory == null ? null : java.util.List.copyOf(signalHistory);
        this.signalStatus = signalStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSignalDefinition() {
        return signalDefinition;
    }

    public StepEntity getSignalSource() {
        return signalSource;
    }

    public double getSignalValue() {
        return signalValue;
    }

    public List<Double> getSignalHistory() {
        return signalHistory;
    }

    public String getSignalStatus() {
        return signalStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSignalInstance that = (StepSignalInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(signalDefinition, that.signalDefinition) && Objects.equals(signalSource, that.signalSource) && signalValue == that.signalValue && Objects.equals(signalHistory, that.signalHistory) && Objects.equals(signalStatus, that.signalStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, signalDefinition, signalSource, signalValue, signalHistory, signalStatus);
    }

    @Override
    public String toString() {
        return "StepSignalInstance{" + "id=" + id + "name=" + name + "signalDefinition=" + signalDefinition + "signalSource=" + signalSource + "signalValue=" + signalValue + "signalHistory=" + signalHistory + "signalStatus=" + signalStatus + "}";
    }
}