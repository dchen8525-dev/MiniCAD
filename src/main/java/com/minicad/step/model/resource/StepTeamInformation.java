package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TEAM_INFORMATION.
 * A team information entity.
 *
 * @param id STEP instance id
 * @param name team name
 * @param teamId team identifier
 * @varianceMembers team variance members
 * @param teamLead team lead reference
 * @varianceResponsibilities team variance responsibilities
 * @varianceAuthority team variance authority level
 * @varianceStatus team variance status
 */
public record StepTeamInformation(
    int id,
    String name,
    String teamId,
    List<StepEntity> varianceMembers,
    StepEntity teamLead,
    List<String> varianceResponsibilities,
    int varianceAuthority,
    String varianceStatus) implements StepEntity {
}