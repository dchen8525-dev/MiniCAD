package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TEAM_DEFINITION.
 * A team definition entity.
 *
 * @param id STEP instance id
 * @param name team name
 * @param teamType team variance type
 * @param teamPurpose team variance purpose
 * @param teamResponsibilities team variance responsibilities
 * @param teamMembers team variance member definitions
 * @param teamStatus team variance status
 */
public record StepTeamDefinition(
    int id,
    String name,
    String teamType,
    String teamPurpose,
    List<String> teamResponsibilities,
    List<StepEntity> teamMembers,
    String teamStatus) implements StepEntity {
}