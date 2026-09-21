package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved REGULATION_DEFINITION.
 * A regulation definition entity.
 *
 * @param id STEP instance id
 * @param name regulation name
 * @param regulationType regulation variance type
 * @param regulationAuthority regulation variance authority
 * @param regulationRequirements regulation variance requirements
 * @param regulationPenalties regulation variance penalties
 * @param regulationStatus regulation variance status
 */
public final class StepRegulationDefinition extends AbstractStepEntity {
    private final String regulationType;
    private final String regulationAuthority;
    private final List<String> regulationRequirements;
    private final String regulationPenalties;
    private final String regulationStatus;

    public StepRegulationDefinition(int id, String name, String regulationType, String regulationAuthority, List<String> regulationRequirements, String regulationPenalties, String regulationStatus) {
        super(id, name);
        this.regulationType = regulationType;
        this.regulationAuthority = regulationAuthority;
        this.regulationRequirements = regulationRequirements == null ? null : java.util.List.copyOf(regulationRequirements);
        this.regulationPenalties = regulationPenalties;
        this.regulationStatus = regulationStatus;
    }

    public String getRegulationType() {
        return regulationType;
    }

    public String getRegulationAuthority() {
        return regulationAuthority;
    }

    public List<String> getRegulationRequirements() {
        return regulationRequirements;
    }

    public String getRegulationPenalties() {
        return regulationPenalties;
    }

    public String getRegulationStatus() {
        return regulationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("regulationType", regulationType);
        state.put("regulationAuthority", regulationAuthority);
        state.put("regulationRequirements", regulationRequirements);
        state.put("regulationPenalties", regulationPenalties);
        state.put("regulationStatus", regulationStatus);
        return state;
    }
}
