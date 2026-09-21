package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTeamDefinition extends AbstractStepEntity {
    private final String teamType;
    private final String teamPurpose;
    private final List<String> teamResponsibilities;
    private final List<StepEntity> teamMembers;
    private final String teamStatus;

    public StepTeamDefinition(int id, String name, String teamType, String teamPurpose, List<String> teamResponsibilities, List<StepEntity> teamMembers, String teamStatus) {
        super(id, name);
        this.teamType = teamType;
        this.teamPurpose = teamPurpose;
        this.teamResponsibilities = teamResponsibilities == null ? null : java.util.List.copyOf(teamResponsibilities);
        this.teamMembers = teamMembers == null ? null : java.util.List.copyOf(teamMembers);
        this.teamStatus = teamStatus;
    }

    public String getTeamType() {
        return teamType;
    }

    public String getTeamPurpose() {
        return teamPurpose;
    }

    public List<String> getTeamResponsibilities() {
        return teamResponsibilities;
    }

    public List<StepEntity> getTeamMembers() {
        return teamMembers;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("teamType", teamType);
        state.put("teamPurpose", teamPurpose);
        state.put("teamResponsibilities", teamResponsibilities);
        state.put("teamMembers", teamMembers);
        state.put("teamStatus", teamStatus);
        return state;
    }
}
