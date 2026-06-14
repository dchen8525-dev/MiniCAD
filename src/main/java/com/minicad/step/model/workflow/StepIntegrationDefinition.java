package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INTEGRATION_DEFINITION.
 * An integration definition entity.
 *
 * @param id STEP instance id
 * @param name integration name
 * @param integrationType integration variance type
 * @param integrationSource integration variance source system
 * @param integrationTarget integration variance target system
 * @param integrationProtocol integration variance protocol
 * @param integrationParameters integration variance parameters
 * @param integrationStatus integration variance status
 */
/**
 * Resolved INTEGRATION_DEFINITION.
 * An integration definition entity.
 *
 * @param id STEP instance id
 * @param name integration name
 * @param integrationType integration variance type
 * @param integrationSource integration variance source system
 * @param integrationTarget integration variance target system
 * @param integrationProtocol integration variance protocol
 * @param integrationParameters integration variance parameters
 * @param integrationStatus integration variance status
 */
public final class StepIntegrationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String integrationType;
    private final String integrationSource;
    private final String integrationTarget;
    private final String integrationProtocol;
    private final List<String> integrationParameters;
    private final String integrationStatus;

    public StepIntegrationDefinition(int id, String name, String integrationType, String integrationSource, String integrationTarget, String integrationProtocol, List<String> integrationParameters, String integrationStatus) {
        this.id = id;
        this.name = name;
        this.integrationType = integrationType;
        this.integrationSource = integrationSource;
        this.integrationTarget = integrationTarget;
        this.integrationProtocol = integrationProtocol;
        this.integrationParameters = integrationParameters == null ? null : java.util.List.copyOf(integrationParameters);
        this.integrationStatus = integrationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    public String getIntegrationSource() {
        return integrationSource;
    }

    public String getIntegrationTarget() {
        return integrationTarget;
    }

    public String getIntegrationProtocol() {
        return integrationProtocol;
    }

    public List<String> getIntegrationParameters() {
        return integrationParameters;
    }

    public String getIntegrationStatus() {
        return integrationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIntegrationDefinition that = (StepIntegrationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(integrationType, that.integrationType) && Objects.equals(integrationSource, that.integrationSource) && Objects.equals(integrationTarget, that.integrationTarget) && Objects.equals(integrationProtocol, that.integrationProtocol) && Objects.equals(integrationParameters, that.integrationParameters) && Objects.equals(integrationStatus, that.integrationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, integrationType, integrationSource, integrationTarget, integrationProtocol, integrationParameters, integrationStatus);
    }

    @Override
    public String toString() {
        return "StepIntegrationDefinition{" + "id=" + id + "name=" + name + "integrationType=" + integrationType + "integrationSource=" + integrationSource + "integrationTarget=" + integrationTarget + "integrationProtocol=" + integrationProtocol + "integrationParameters=" + integrationParameters + "integrationStatus=" + integrationStatus + "}";
    }
}