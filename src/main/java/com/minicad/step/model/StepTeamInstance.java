package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTeamInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity teamDefinition;
    private final StepEntity teamLead;
    private final List<StepEntity> teamMembers;
    private final List<StepEntity> teamProjects;
    private final String teamStatus;

    public StepTeamInstance(int id, String name, StepEntity teamDefinition, StepEntity teamLead, List<StepEntity> teamMembers, List<StepEntity> teamProjects, String teamStatus) {
        this.id = id;
        this.name = name;
        this.teamDefinition = teamDefinition;
        this.teamLead = teamLead;
        this.teamMembers = teamMembers == null ? null : java.util.List.copyOf(teamMembers);
        this.teamProjects = teamProjects == null ? null : java.util.List.copyOf(teamProjects);
        this.teamStatus = teamStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTeamDefinition() {
        return teamDefinition;
    }

    public StepEntity getTeamLead() {
        return teamLead;
    }

    public List<StepEntity> getTeamMembers() {
        return teamMembers;
    }

    public List<StepEntity> getTeamProjects() {
        return teamProjects;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTeamInstance that = (StepTeamInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(teamDefinition, that.teamDefinition) && Objects.equals(teamLead, that.teamLead) && Objects.equals(teamMembers, that.teamMembers) && Objects.equals(teamProjects, that.teamProjects) && Objects.equals(teamStatus, that.teamStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teamDefinition, teamLead, teamMembers, teamProjects, teamStatus);
    }

    @Override
    public String toString() {
        return "StepTeamInstance{" + "id=" + id + "name=" + name + "teamDefinition=" + teamDefinition + "teamLead=" + teamLead + "teamMembers=" + teamMembers + "teamProjects=" + teamProjects + "teamStatus=" + teamStatus + "}";
    }
}