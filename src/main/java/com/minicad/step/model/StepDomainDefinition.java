package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DOMAIN_DEFINITION.
 * A domain definition entity.
 *
 * @param id STEP instance id
 * @param name domain name
 * @param domainType domain variance type
 * @param domainDescription domain variance description
 * @param domainScope domain variance scope
 * @param domainAuthority domain variance authority
 * @param domainStatus domain variance status
 */
public record StepDomainDefinition(
    int id,
    String name,
    String domainType,
    String domainDescription,
    String domainScope,
    String domainAuthority,
    String domainStatus) implements StepEntity {
}