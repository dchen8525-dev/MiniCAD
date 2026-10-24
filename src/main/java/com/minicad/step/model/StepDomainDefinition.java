package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepDomainDefinition extends AbstractStepEntity {
    private final String domainType;
    private final String domainDescription;
    private final String domainScope;
    private final String domainAuthority;
    private final String domainStatus;

    public StepDomainDefinition(int id, String name, String domainType, String domainDescription, String domainScope, String domainAuthority, String domainStatus) {
        super(id, name);
        this.domainType = domainType;
        this.domainDescription = domainDescription;
        this.domainScope = domainScope;
        this.domainAuthority = domainAuthority;
        this.domainStatus = domainStatus;
    }

    public String getDomainType() {
        return domainType;
    }

    public String getDomainDescription() {
        return domainDescription;
    }

    public String getDomainScope() {
        return domainScope;
    }

    public String getDomainAuthority() {
        return domainAuthority;
    }

    public String getDomainStatus() {
        return domainStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "domainType", domainType,
                "domainDescription", domainDescription, "domainScope", domainScope,
                "domainAuthority", domainAuthority, "domainStatus", domainStatus);
    }
}
