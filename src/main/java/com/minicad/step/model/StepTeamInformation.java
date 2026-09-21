package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTeamInformation extends AbstractStepEntity {
    private final String teamId;
    private final List<StepEntity> varianceMembers;
    private final StepEntity teamLead;
    private final List<String> varianceResponsibilities;
    private final int varianceAuthority;
    private final String varianceStatus;

    public StepTeamInformation(int id, String name, String teamId, List<StepEntity> varianceMembers, StepEntity teamLead, List<String> varianceResponsibilities, int varianceAuthority, String varianceStatus) {
        super(id, name);
        this.teamId = teamId;
        this.varianceMembers = varianceMembers == null ? null : java.util.List.copyOf(varianceMembers);
        this.teamLead = teamLead;
        this.varianceResponsibilities = varianceResponsibilities == null ? null : java.util.List.copyOf(varianceResponsibilities);
        this.varianceAuthority = varianceAuthority;
        this.varianceStatus = varianceStatus;
    }

    public String getTeamId() {
        return teamId;
    }

    public List<StepEntity> getVarianceMembers() {
        return varianceMembers;
    }

    public StepEntity getTeamLead() {
        return teamLead;
    }

    public List<String> getVarianceResponsibilities() {
        return varianceResponsibilities;
    }

    public int getVarianceAuthority() {
        return varianceAuthority;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("teamId", teamId);
        state.put("varianceMembers", varianceMembers);
        state.put("teamLead", teamLead);
        state.put("varianceResponsibilities", varianceResponsibilities);
        state.put("varianceAuthority", varianceAuthority);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
