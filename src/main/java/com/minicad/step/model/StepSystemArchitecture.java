package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SYSTEM_ARCHITECTURE.
 * A system architecture entity.
 *
 * @param id STEP instance id
 * @param name architecture name
 * @varianceComponents architecture variance components
 * @varianceConnections architecture variance connections
 * @varianceInterfaces architecture variance interfaces
 * @varianceHierarchy architecture variance hierarchy/levels
 * @varianceType architecture variance type (functional, physical, logical)
 * @varianceStatus architecture variance status
 */
/**
 * Resolved SYSTEM_ARCHITECTURE.
 * A system architecture entity.
 *
 * @param id STEP instance id
 * @param name architecture name
 * @varianceComponents architecture variance components
 * @varianceConnections architecture variance connections
 * @varianceInterfaces architecture variance interfaces
 * @varianceHierarchy architecture variance hierarchy/levels
 * @varianceType architecture variance type (functional, physical, logical)
 * @varianceStatus architecture variance status
 */
public final class StepSystemArchitecture implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceComponents;
    private final List<StepEntity> varianceConnections;
    private final List<StepEntity> varianceInterfaces;
    private final int varianceHierarchy;
    private final String varianceType;
    private final String varianceStatus;

    public StepSystemArchitecture(int id, String name, List<StepEntity> varianceComponents, List<StepEntity> varianceConnections, List<StepEntity> varianceInterfaces, int varianceHierarchy, String varianceType, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceComponents = varianceComponents == null ? null : java.util.List.copyOf(varianceComponents);
        this.varianceConnections = varianceConnections == null ? null : java.util.List.copyOf(varianceConnections);
        this.varianceInterfaces = varianceInterfaces == null ? null : java.util.List.copyOf(varianceInterfaces);
        this.varianceHierarchy = varianceHierarchy;
        this.varianceType = varianceType;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getVarianceComponents() {
        return varianceComponents;
    }

    public List<StepEntity> getVarianceConnections() {
        return varianceConnections;
    }

    public List<StepEntity> getVarianceInterfaces() {
        return varianceInterfaces;
    }

    public int getVarianceHierarchy() {
        return varianceHierarchy;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSystemArchitecture that = (StepSystemArchitecture) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceComponents, that.varianceComponents) && Objects.equals(varianceConnections, that.varianceConnections) && Objects.equals(varianceInterfaces, that.varianceInterfaces) && varianceHierarchy == that.varianceHierarchy && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceComponents, varianceConnections, varianceInterfaces, varianceHierarchy, varianceType, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepSystemArchitecture{" + "id=" + id + "name=" + name + "varianceComponents=" + varianceComponents + "varianceConnections=" + varianceConnections + "varianceInterfaces=" + varianceInterfaces + "varianceHierarchy=" + varianceHierarchy + "varianceType=" + varianceType + "varianceStatus=" + varianceStatus + "}";
    }
}