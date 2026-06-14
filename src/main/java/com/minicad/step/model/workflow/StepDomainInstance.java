package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DOMAIN_INSTANCE.
 * A domain instance entity.
 *
 * @param id STEP instance id
 * @param name domain instance name
 * @param domainDefinition domain variance definition reference
 * @param domainState domain variance state
 * @param domainMembers domain variance member count
 * @param domainResources domain variance resources within
 * @param domainStatus domain variance status
 */
/**
 * Resolved DOMAIN_INSTANCE.
 * A domain instance entity.
 *
 * @param id STEP instance id
 * @param name domain instance name
 * @param domainDefinition domain variance definition reference
 * @param domainState domain variance state
 * @param domainMembers domain variance member count
 * @param domainResources domain variance resources within
 * @param domainStatus domain variance status
 */
public final class StepDomainInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity domainDefinition;
    private final String domainState;
    private final int domainMembers;
    private final List<StepEntity> domainResources;
    private final String domainStatus;

    public StepDomainInstance(int id, String name, StepEntity domainDefinition, String domainState, int domainMembers, List<StepEntity> domainResources, String domainStatus) {
        this.id = id;
        this.name = name;
        this.domainDefinition = domainDefinition;
        this.domainState = domainState;
        this.domainMembers = domainMembers;
        this.domainResources = domainResources == null ? null : java.util.List.copyOf(domainResources);
        this.domainStatus = domainStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getDomainDefinition() {
        return domainDefinition;
    }

    public String getDomainState() {
        return domainState;
    }

    public int getDomainMembers() {
        return domainMembers;
    }

    public List<StepEntity> getDomainResources() {
        return domainResources;
    }

    public String getDomainStatus() {
        return domainStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDomainInstance that = (StepDomainInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(domainDefinition, that.domainDefinition) && Objects.equals(domainState, that.domainState) && domainMembers == that.domainMembers && Objects.equals(domainResources, that.domainResources) && Objects.equals(domainStatus, that.domainStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, domainDefinition, domainState, domainMembers, domainResources, domainStatus);
    }

    @Override
    public String toString() {
        return "StepDomainInstance{" + "id=" + id + "name=" + name + "domainDefinition=" + domainDefinition + "domainState=" + domainState + "domainMembers=" + domainMembers + "domainResources=" + domainResources + "domainStatus=" + domainStatus + "}";
    }
}