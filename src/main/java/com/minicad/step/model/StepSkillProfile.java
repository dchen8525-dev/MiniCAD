package com.minicad.step.model;

import java.util.List;

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
public record StepSkillProfile(
    int id,
    String name,
    StepEntity variancePerson,
    List<String> varianceSkills,
    List<Integer> varianceLevels,
    List<Double> varianceExperience,
    String varianceStatus) implements StepEntity {
}