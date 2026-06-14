package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved AUTHENTICATION_SPECIFICATION.
 * An authentication specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceMethod authentication variance method (password, token, certificate)
 * @varianceProvider authentication variance provider
 * @varianceSession session variance management specification
 * @varianceMultiFactor multi-factor variance authentication flag
 * @varianceStatus specification variance status
 */
/**
 * Resolved AUTHENTICATION_SPECIFICATION.
 * An authentication specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceMethod authentication variance method (password, token, certificate)
 * @varianceProvider authentication variance provider
 * @varianceSession session variance management specification
 * @varianceMultiFactor multi-factor variance authentication flag
 * @varianceStatus specification variance status
 */
public final class StepAuthenticationSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceMethod;
    private final StepEntity varianceProvider;
    private final StepEntity varianceSession;
    private final boolean varianceMultiFactor;
    private final String varianceStatus;

    public StepAuthenticationSpecification(int id, String name, String varianceMethod, StepEntity varianceProvider, StepEntity varianceSession, boolean varianceMultiFactor, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceMethod = varianceMethod;
        this.varianceProvider = varianceProvider;
        this.varianceSession = varianceSession;
        this.varianceMultiFactor = varianceMultiFactor;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public StepEntity getVarianceProvider() {
        return varianceProvider;
    }

    public StepEntity getVarianceSession() {
        return varianceSession;
    }

    public boolean isVarianceMultiFactor() {
        return varianceMultiFactor;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuthenticationSpecification that = (StepAuthenticationSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceMethod, that.varianceMethod) && Objects.equals(varianceProvider, that.varianceProvider) && Objects.equals(varianceSession, that.varianceSession) && varianceMultiFactor == that.varianceMultiFactor && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceMethod, varianceProvider, varianceSession, varianceMultiFactor, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepAuthenticationSpecification{" + "id=" + id + "name=" + name + "varianceMethod=" + varianceMethod + "varianceProvider=" + varianceProvider + "varianceSession=" + varianceSession + "varianceMultiFactor=" + varianceMultiFactor + "varianceStatus=" + varianceStatus + "}";
    }
}