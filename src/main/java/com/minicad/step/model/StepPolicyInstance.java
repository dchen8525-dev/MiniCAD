package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved POLICY_INSTANCE.
 * A policy instance entity.
 *
 * @param id STEP instance id
 * @param name policy instance name
 * @param policyDefinition policy variance definition reference
 * @param policyState policy variance state
 * @param policyViolations policy variance violation count
 * @param policyExceptions policy variance exception count
 * @param policyStatus policy variance status
 */
/**
 * Resolved POLICY_INSTANCE.
 * A policy instance entity.
 *
 * @param id STEP instance id
 * @param name policy instance name
 * @param policyDefinition policy variance definition reference
 * @param policyState policy variance state
 * @param policyViolations policy variance violation count
 * @param policyExceptions policy variance exception count
 * @param policyStatus policy variance status
 */
public final class StepPolicyInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity policyDefinition;
    private final String policyState;
    private final int policyViolations;
    private final int policyExceptions;
    private final String policyStatus;

    public StepPolicyInstance(int id, String name, StepEntity policyDefinition, String policyState, int policyViolations, int policyExceptions, String policyStatus) {
        this.id = id;
        this.name = name;
        this.policyDefinition = policyDefinition;
        this.policyState = policyState;
        this.policyViolations = policyViolations;
        this.policyExceptions = policyExceptions;
        this.policyStatus = policyStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPolicyDefinition() {
        return policyDefinition;
    }

    public String getPolicyState() {
        return policyState;
    }

    public int getPolicyViolations() {
        return policyViolations;
    }

    public int getPolicyExceptions() {
        return policyExceptions;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPolicyInstance that = (StepPolicyInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(policyDefinition, that.policyDefinition) && Objects.equals(policyState, that.policyState) && policyViolations == that.policyViolations && policyExceptions == that.policyExceptions && Objects.equals(policyStatus, that.policyStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, policyDefinition, policyState, policyViolations, policyExceptions, policyStatus);
    }

    @Override
    public String toString() {
        return "StepPolicyInstance{" + "id=" + id + "name=" + name + "policyDefinition=" + policyDefinition + "policyState=" + policyState + "policyViolations=" + policyViolations + "policyExceptions=" + policyExceptions + "policyStatus=" + policyStatus + "}";
    }
}