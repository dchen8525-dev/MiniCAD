package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTeamDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String teamType;
    private final String teamPurpose;
    private final List<String> teamResponsibilities;
    private final List<StepEntity> teamMembers;
    private final String teamStatus;

    public StepTeamDefinition(int id, String name, String teamType, String teamPurpose, List<String> teamResponsibilities, List<StepEntity> teamMembers, String teamStatus) {
        this.id = id;
        this.name = name;
        this.teamType = teamType;
        this.teamPurpose = teamPurpose;
        this.teamResponsibilities = teamResponsibilities == null ? null : java.util.List.copyOf(teamResponsibilities);
        this.teamMembers = teamMembers == null ? null : java.util.List.copyOf(teamMembers);
        this.teamStatus = teamStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTeamDefinition that = (StepTeamDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(teamType, that.teamType) && Objects.equals(teamPurpose, that.teamPurpose) && Objects.equals(teamResponsibilities, that.teamResponsibilities) && Objects.equals(teamMembers, that.teamMembers) && Objects.equals(teamStatus, that.teamStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teamType, teamPurpose, teamResponsibilities, teamMembers, teamStatus);
    }

    @Override
    public String toString() {
        return "StepTeamDefinition{" + "id=" + id + "name=" + name + "teamType=" + teamType + "teamPurpose=" + teamPurpose + "teamResponsibilities=" + teamResponsibilities + "teamMembers=" + teamMembers + "teamStatus=" + teamStatus + "}";
    }
}