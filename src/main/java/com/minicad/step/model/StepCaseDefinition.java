package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CASE_DEFINITION.
 * A case definition entity.
 *
 * @param id STEP instance id
 * @param name case name
 * @param caseType case variance type
 * @param caseScenario case variance scenario
 * @param caseInputs case variance inputs
 * @param caseExpectedOutputs case variance expected outputs
 * @param caseStatus case variance status
 */
public final class StepCaseDefinition extends AbstractStepEntity {
    private final String caseType;
    private final StepEntity caseScenario;
    private final List<String> caseInputs;
    private final List<String> caseExpectedOutputs;
    private final String caseStatus;

    public StepCaseDefinition(int id, String name, String caseType, StepEntity caseScenario, List<String> caseInputs, List<String> caseExpectedOutputs, String caseStatus) {
        super(id, name);
        this.caseType = caseType;
        this.caseScenario = caseScenario;
        this.caseInputs = caseInputs == null ? null : java.util.List.copyOf(caseInputs);
        this.caseExpectedOutputs = caseExpectedOutputs == null ? null : java.util.List.copyOf(caseExpectedOutputs);
        this.caseStatus = caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public StepEntity getCaseScenario() {
        return caseScenario;
    }

    public List<String> getCaseInputs() {
        return caseInputs;
    }

    public List<String> getCaseExpectedOutputs() {
        return caseExpectedOutputs;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("caseType", caseType);
        state.put("caseScenario", caseScenario);
        state.put("caseInputs", caseInputs);
        state.put("caseExpectedOutputs", caseExpectedOutputs);
        state.put("caseStatus", caseStatus);
        return state;
    }
}
