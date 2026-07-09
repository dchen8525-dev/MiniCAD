package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTeamInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String teamId;
    private final List<StepEntity> varianceMembers;
    private final StepEntity teamLead;
    private final List<String> varianceResponsibilities;
    private final int varianceAuthority;
    private final String varianceStatus;

    public StepTeamInformation(int id, String name, String teamId, List<StepEntity> varianceMembers, StepEntity teamLead, List<String> varianceResponsibilities, int varianceAuthority, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
        this.varianceMembers = varianceMembers == null ? null : java.util.List.copyOf(varianceMembers);
        this.teamLead = teamLead;
        this.varianceResponsibilities = varianceResponsibilities == null ? null : java.util.List.copyOf(varianceResponsibilities);
        this.varianceAuthority = varianceAuthority;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTeamInformation that = (StepTeamInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(teamId, that.teamId) && Objects.equals(varianceMembers, that.varianceMembers) && Objects.equals(teamLead, that.teamLead) && Objects.equals(varianceResponsibilities, that.varianceResponsibilities) && varianceAuthority == that.varianceAuthority && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teamId, varianceMembers, teamLead, varianceResponsibilities, varianceAuthority, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepTeamInformation{" + "id=" + id + "name=" + name + "teamId=" + teamId + "varianceMembers=" + varianceMembers + "teamLead=" + teamLead + "varianceResponsibilities=" + varianceResponsibilities + "varianceAuthority=" + varianceAuthority + "varianceStatus=" + varianceStatus + "}";
    }
}