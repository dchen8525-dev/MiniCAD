package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepDomainInstance(
    int id,
    String name,
    StepEntity domainDefinition,
    String domainState,
    int domainMembers,
    List<StepEntity> domainResources,
    String domainStatus) implements StepEntity {
}