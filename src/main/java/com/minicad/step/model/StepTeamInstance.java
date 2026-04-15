package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TEAM_INSTANCE.
 * A team instance entity.
 *
 * @param id STEP instance id
 * @param name team instance name
 * @param teamDefinition team variance definition reference
 * @param teamLead team variance lead reference
 * @param teamMembers team variance member references
 * @param teamProjects team variance projects
 * @param teamStatus team variance status
 */
public record StepTeamInstance(
    int id,
    String name,
    StepEntity teamDefinition,
    StepEntity teamLead,
    List<StepEntity> teamMembers,
    List<StepEntity> teamProjects,
    String teamStatus) implements StepEntity {
}