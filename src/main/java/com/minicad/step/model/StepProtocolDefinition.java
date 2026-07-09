package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PROTOCOL_DEFINITION.
 * A protocol definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceProtocol defined variance protocol
 * @varianceType protocol variance type (communication, data, control)
 * @varianceFormat protocol variance format specification
 * @varianceSequence protocol variance sequence/steps
 * @varianceStandard protocol variance standard reference
 * @varianceStatus definition variance status
 */
/**
 * Resolved PROTOCOL_DEFINITION.
 * A protocol definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceProtocol defined variance protocol
 * @varianceType protocol variance type (communication, data, control)
 * @varianceFormat protocol variance format specification
 * @varianceSequence protocol variance sequence/steps
 * @varianceStandard protocol variance standard reference
 * @varianceStatus definition variance status
 */
public final class StepProtocolDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceProtocol;
    private final String varianceType;
    private final String varianceFormat;
    private final List<String> varianceSequence;
    private final String varianceStandard;
    private final String varianceStatus;

    public StepProtocolDefinition(int id, String name, String varianceProtocol, String varianceType, String varianceFormat, List<String> varianceSequence, String varianceStandard, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceProtocol = varianceProtocol;
        this.varianceType = varianceType;
        this.varianceFormat = varianceFormat;
        this.varianceSequence = varianceSequence == null ? null : java.util.List.copyOf(varianceSequence);
        this.varianceStandard = varianceStandard;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceProtocol() {
        return varianceProtocol;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceFormat() {
        return varianceFormat;
    }

    public List<String> getVarianceSequence() {
        return varianceSequence;
    }

    public String getVarianceStandard() {
        return varianceStandard;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProtocolDefinition that = (StepProtocolDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceProtocol, that.varianceProtocol) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceFormat, that.varianceFormat) && Objects.equals(varianceSequence, that.varianceSequence) && Objects.equals(varianceStandard, that.varianceStandard) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceProtocol, varianceType, varianceFormat, varianceSequence, varianceStandard, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepProtocolDefinition{" + "id=" + id + "name=" + name + "varianceProtocol=" + varianceProtocol + "varianceType=" + varianceType + "varianceFormat=" + varianceFormat + "varianceSequence=" + varianceSequence + "varianceStandard=" + varianceStandard + "varianceStatus=" + varianceStatus + "}";
    }
}