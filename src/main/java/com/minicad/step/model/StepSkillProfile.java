package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SKILL_PROFILE.
 * A skill profile entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @variancePerson person variance reference
 * @varianceSkills skill variance list
 * @varianceLevels skill variance levels
 * @varianceExperience experience variance years
 * @varianceStatus profile variance status
 */
public final class StepSkillProfile extends AbstractStepEntity {
    private final StepEntity variancePerson;
    private final List<String> varianceSkills;
    private final List<Integer> varianceLevels;
    private final List<Double> varianceExperience;
    private final String varianceStatus;

    public StepSkillProfile(int id, String name, StepEntity variancePerson, List<String> varianceSkills, List<Integer> varianceLevels, List<Double> varianceExperience, String varianceStatus) {
        super(id, name);
        this.variancePerson = variancePerson;
        this.varianceSkills = varianceSkills == null ? null : java.util.List.copyOf(varianceSkills);
        this.varianceLevels = varianceLevels == null ? null : java.util.List.copyOf(varianceLevels);
        this.varianceExperience = varianceExperience == null ? null : java.util.List.copyOf(varianceExperience);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVariancePerson() {
        return variancePerson;
    }

    public List<String> getVarianceSkills() {
        return varianceSkills;
    }

    public List<Integer> getVarianceLevels() {
        return varianceLevels;
    }

    public List<Double> getVarianceExperience() {
        return varianceExperience;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("variancePerson", variancePerson);
        state.put("varianceSkills", varianceSkills);
        state.put("varianceLevels", varianceLevels);
        state.put("varianceExperience", varianceExperience);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
