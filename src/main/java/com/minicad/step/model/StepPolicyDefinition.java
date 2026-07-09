package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved POLICY_DEFINITION.
 * A policy definition entity.
 *
 * @param id STEP instance id
 * @param name policy name
 * @param policyType policy variance type
 * @param policyScope policy variance scope
 * @param policyRules policy variance rules
 * @param policyEnforcement policy variance enforcement level
 * @param policyStatus policy variance status
 */
/**
 * Resolved POLICY_DEFINITION.
 * A policy definition entity.
 *
 * @param id STEP instance id
 * @param name policy name
 * @param policyType policy variance type
 * @param policyScope policy variance scope
 * @param policyRules policy variance rules
 * @param policyEnforcement policy variance enforcement level
 * @param policyStatus policy variance status
 */
public final class StepPolicyDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String policyType;
    private final String policyScope;
    private final List<String> policyRules;
    private final String policyEnforcement;
    private final String policyStatus;

    public StepPolicyDefinition(int id, String name, String policyType, String policyScope, List<String> policyRules, String policyEnforcement, String policyStatus) {
        this.id = id;
        this.name = name;
        this.policyType = policyType;
        this.policyScope = policyScope;
        this.policyRules = policyRules == null ? null : java.util.List.copyOf(policyRules);
        this.policyEnforcement = policyEnforcement;
        this.policyStatus = policyStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPolicyType() {
        return policyType;
    }

    public String getPolicyScope() {
        return policyScope;
    }

    public List<String> getPolicyRules() {
        return policyRules;
    }

    public String getPolicyEnforcement() {
        return policyEnforcement;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPolicyDefinition that = (StepPolicyDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(policyType, that.policyType) && Objects.equals(policyScope, that.policyScope) && Objects.equals(policyRules, that.policyRules) && Objects.equals(policyEnforcement, that.policyEnforcement) && Objects.equals(policyStatus, that.policyStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, policyType, policyScope, policyRules, policyEnforcement, policyStatus);
    }

    @Override
    public String toString() {
        return "StepPolicyDefinition{" + "id=" + id + "name=" + name + "policyType=" + policyType + "policyScope=" + policyScope + "policyRules=" + policyRules + "policyEnforcement=" + policyEnforcement + "policyStatus=" + policyStatus + "}";
    }
}