package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepDomainInstance extends AbstractStepEntity {
    private final StepEntity domainDefinition;
    private final String domainState;
    private final int domainMembers;
    private final List<StepEntity> domainResources;
    private final String domainStatus;

    public StepDomainInstance(int id, String name, StepEntity domainDefinition, String domainState, int domainMembers, List<StepEntity> domainResources, String domainStatus) {
        super(id, name);
        this.domainDefinition = domainDefinition;
        this.domainState = domainState;
        this.domainMembers = domainMembers;
        this.domainResources = domainResources == null ? null : java.util.List.copyOf(domainResources);
        this.domainStatus = domainStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("domainDefinition", domainDefinition);
        state.put("domainState", domainState);
        state.put("domainMembers", domainMembers);
        state.put("domainResources", domainResources);
        state.put("domainStatus", domainStatus);
        return state;
    }
}
