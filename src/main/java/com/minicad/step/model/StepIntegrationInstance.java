package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INTEGRATION_INSTANCE.
 * An integration instance entity.
 *
 * @param id STEP instance id
 * @param name integration instance name
 * @param integrationDefinition integration variance definition reference
 * @param integrationState integration variance state
 * @param integrationLastSync integration variance last sync time
 * @param integrationErrors integration variance error count
 * @param integrationStatus integration variance status
 */
/**
 * Resolved INTEGRATION_INSTANCE.
 * An integration instance entity.
 *
 * @param id STEP instance id
 * @param name integration instance name
 * @param integrationDefinition integration variance definition reference
 * @param integrationState integration variance state
 * @param integrationLastSync integration variance last sync time
 * @param integrationErrors integration variance error count
 * @param integrationStatus integration variance status
 */
public final class StepIntegrationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity integrationDefinition;
    private final String integrationState;
    private final StepEntity integrationLastSync;
    private final int integrationErrors;
    private final String integrationStatus;

    public StepIntegrationInstance(int id, String name, StepEntity integrationDefinition, String integrationState, StepEntity integrationLastSync, int integrationErrors, String integrationStatus) {
        this.id = id;
        this.name = name;
        this.integrationDefinition = integrationDefinition;
        this.integrationState = integrationState;
        this.integrationLastSync = integrationLastSync;
        this.integrationErrors = integrationErrors;
        this.integrationStatus = integrationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getIntegrationDefinition() {
        return integrationDefinition;
    }

    public String getIntegrationState() {
        return integrationState;
    }

    public StepEntity getIntegrationLastSync() {
        return integrationLastSync;
    }

    public int getIntegrationErrors() {
        return integrationErrors;
    }

    public String getIntegrationStatus() {
        return integrationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIntegrationInstance that = (StepIntegrationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(integrationDefinition, that.integrationDefinition) && Objects.equals(integrationState, that.integrationState) && Objects.equals(integrationLastSync, that.integrationLastSync) && integrationErrors == that.integrationErrors && Objects.equals(integrationStatus, that.integrationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, integrationDefinition, integrationState, integrationLastSync, integrationErrors, integrationStatus);
    }

    @Override
    public String toString() {
        return "StepIntegrationInstance{" + "id=" + id + "name=" + name + "integrationDefinition=" + integrationDefinition + "integrationState=" + integrationState + "integrationLastSync=" + integrationLastSync + "integrationErrors=" + integrationErrors + "integrationStatus=" + integrationStatus + "}";
    }
}