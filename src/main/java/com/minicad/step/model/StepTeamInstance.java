package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTeamInstance extends AbstractStepEntity {
    private final StepEntity teamDefinition;
    private final StepEntity teamLead;
    private final List<StepEntity> teamMembers;
    private final List<StepEntity> teamProjects;
    private final String teamStatus;

    public StepTeamInstance(int id, String name, StepEntity teamDefinition, StepEntity teamLead, List<StepEntity> teamMembers, List<StepEntity> teamProjects, String teamStatus) {
        super(id, name);
        this.teamDefinition = teamDefinition;
        this.teamLead = teamLead;
        this.teamMembers = teamMembers == null ? null : java.util.List.copyOf(teamMembers);
        this.teamProjects = teamProjects == null ? null : java.util.List.copyOf(teamProjects);
        this.teamStatus = teamStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("teamDefinition", teamDefinition);
        state.put("teamLead", teamLead);
        state.put("teamMembers", teamMembers);
        state.put("teamProjects", teamProjects);
        state.put("teamStatus", teamStatus);
        return state;
    }
}
