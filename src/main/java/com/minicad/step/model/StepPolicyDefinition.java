package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPolicyDefinition extends AbstractStepEntity {
    private final String policyType;
    private final String policyScope;
    private final List<String> policyRules;
    private final String policyEnforcement;
    private final String policyStatus;

    public StepPolicyDefinition(int id, String name, String policyType, String policyScope, List<String> policyRules, String policyEnforcement, String policyStatus) {
        super(id, name);
        this.policyType = policyType;
        this.policyScope = policyScope;
        this.policyRules = policyRules == null ? null : java.util.List.copyOf(policyRules);
        this.policyEnforcement = policyEnforcement;
        this.policyStatus = policyStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("policyType", policyType);
        state.put("policyScope", policyScope);
        state.put("policyRules", policyRules);
        state.put("policyEnforcement", policyEnforcement);
        state.put("policyStatus", policyStatus);
        return state;
    }
}
