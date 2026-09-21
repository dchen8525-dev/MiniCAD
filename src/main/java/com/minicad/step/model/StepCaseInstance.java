package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CASE_INSTANCE.
 * A case instance entity.
 *
 * @param id STEP instance id
 * @param name case instance name
 * @param caseDefinition case variance definition reference
 * @param caseState case variance state
 * @param caseActualOutputs case variance actual outputs
 * @param caseResult case variance result (pass/fail)
 * @param caseStatus case variance status
 */
public final class StepCaseInstance extends AbstractStepEntity {
    private final StepEntity caseDefinition;
    private final String caseState;
    private final List<String> caseActualOutputs;
    private final boolean caseResult;
    private final String caseStatus;

    public StepCaseInstance(int id, String name, StepEntity caseDefinition, String caseState, List<String> caseActualOutputs, boolean caseResult, String caseStatus) {
        super(id, name);
        this.caseDefinition = caseDefinition;
        this.caseState = caseState;
        this.caseActualOutputs = caseActualOutputs == null ? null : java.util.List.copyOf(caseActualOutputs);
        this.caseResult = caseResult;
        this.caseStatus = caseStatus;
    }

    public StepEntity getCaseDefinition() {
        return caseDefinition;
    }

    public String getCaseState() {
        return caseState;
    }

    public List<String> getCaseActualOutputs() {
        return caseActualOutputs;
    }

    public boolean isCaseResult() {
        return caseResult;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("caseDefinition", caseDefinition);
        state.put("caseState", caseState);
        state.put("caseActualOutputs", caseActualOutputs);
        state.put("caseResult", caseResult);
        state.put("caseStatus", caseStatus);
        return state;
    }
}
