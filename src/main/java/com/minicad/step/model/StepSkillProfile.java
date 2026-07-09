package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSkillProfile implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity variancePerson;
    private final List<String> varianceSkills;
    private final List<Integer> varianceLevels;
    private final List<Double> varianceExperience;
    private final String varianceStatus;

    public StepSkillProfile(int id, String name, StepEntity variancePerson, List<String> varianceSkills, List<Integer> varianceLevels, List<Double> varianceExperience, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.variancePerson = variancePerson;
        this.varianceSkills = varianceSkills == null ? null : java.util.List.copyOf(varianceSkills);
        this.varianceLevels = varianceLevels == null ? null : java.util.List.copyOf(varianceLevels);
        this.varianceExperience = varianceExperience == null ? null : java.util.List.copyOf(varianceExperience);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSkillProfile that = (StepSkillProfile) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variancePerson, that.variancePerson) && Objects.equals(varianceSkills, that.varianceSkills) && Objects.equals(varianceLevels, that.varianceLevels) && Objects.equals(varianceExperience, that.varianceExperience) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variancePerson, varianceSkills, varianceLevels, varianceExperience, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepSkillProfile{" + "id=" + id + "name=" + name + "variancePerson=" + variancePerson + "varianceSkills=" + varianceSkills + "varianceLevels=" + varianceLevels + "varianceExperience=" + varianceExperience + "varianceStatus=" + varianceStatus + "}";
    }
}