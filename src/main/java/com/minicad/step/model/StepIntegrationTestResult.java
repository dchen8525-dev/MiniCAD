package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INTEGRATION_TEST_RESULT.
 * An integration test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceComponents integrated variance components
 * @varianceInterfaces tested variance interfaces
 * @varianceIssues integration variance issues
 * @varianceResolved resolved variance issues count
 * @varianceStatus result variance status
 */
/**
 * Resolved INTEGRATION_TEST_RESULT.
 * An integration test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceComponents integrated variance components
 * @varianceInterfaces tested variance interfaces
 * @varianceIssues integration variance issues
 * @varianceResolved resolved variance issues count
 * @varianceStatus result variance status
 */
public final class StepIntegrationTestResult implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceComponents;
    private final List<StepEntity> varianceInterfaces;
    private final List<String> varianceIssues;
    private final int varianceResolved;
    private final String varianceStatus;

    public StepIntegrationTestResult(int id, String name, List<StepEntity> varianceComponents, List<StepEntity> varianceInterfaces, List<String> varianceIssues, int varianceResolved, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceComponents = varianceComponents == null ? null : java.util.List.copyOf(varianceComponents);
        this.varianceInterfaces = varianceInterfaces == null ? null : java.util.List.copyOf(varianceInterfaces);
        this.varianceIssues = varianceIssues == null ? null : java.util.List.copyOf(varianceIssues);
        this.varianceResolved = varianceResolved;
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

    public List<StepEntity> getVarianceInterfaces() {
        return varianceInterfaces;
    }

    public List<String> getVarianceIssues() {
        return varianceIssues;
    }

    public int getVarianceResolved() {
        return varianceResolved;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIntegrationTestResult that = (StepIntegrationTestResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceComponents, that.varianceComponents) && Objects.equals(varianceInterfaces, that.varianceInterfaces) && Objects.equals(varianceIssues, that.varianceIssues) && varianceResolved == that.varianceResolved && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceComponents, varianceInterfaces, varianceIssues, varianceResolved, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepIntegrationTestResult{" + "id=" + id + "name=" + name + "varianceComponents=" + varianceComponents + "varianceInterfaces=" + varianceInterfaces + "varianceIssues=" + varianceIssues + "varianceResolved=" + varianceResolved + "varianceStatus=" + varianceStatus + "}";
    }
}