package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDomainDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String domainType;
    private final String domainDescription;
    private final String domainScope;
    private final String domainAuthority;
    private final String domainStatus;

    public StepDomainDefinition(int id, String name, String domainType, String domainDescription, String domainScope, String domainAuthority, String domainStatus) {
        this.id = id;
        this.name = name;
        this.domainType = domainType;
        this.domainDescription = domainDescription;
        this.domainScope = domainScope;
        this.domainAuthority = domainAuthority;
        this.domainStatus = domainStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDomainDefinition that = (StepDomainDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(domainType, that.domainType) && Objects.equals(domainDescription, that.domainDescription) && Objects.equals(domainScope, that.domainScope) && Objects.equals(domainAuthority, that.domainAuthority) && Objects.equals(domainStatus, that.domainStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, domainType, domainDescription, domainScope, domainAuthority, domainStatus);
    }

    @Override
    public String toString() {
        return "StepDomainDefinition{" + "id=" + id + "name=" + name + "domainType=" + domainType + "domainDescription=" + domainDescription + "domainScope=" + domainScope + "domainAuthority=" + domainAuthority + "domainStatus=" + domainStatus + "}";
    }
}