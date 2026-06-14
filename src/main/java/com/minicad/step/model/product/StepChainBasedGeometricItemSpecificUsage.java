package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;

/**
 * Minimal chain-based geometric item specific usage.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param usage source PMI item
 * @param nodes representation chain nodes
 * @param undirectedLinks chain links
 * @param identifiedItem referenced geometric item
 */
/**
 * Minimal chain-based geometric item specific usage.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param usage source PMI item
 * @param nodes representation chain nodes
 * @param undirectedLinks chain links
 * @param identifiedItem referenced geometric item
 */
public final class StepChainBasedGeometricItemSpecificUsage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity usage;
    private final List<StepRepresentation> nodes;
    private final List<StepRepresentationRelationship> undirectedLinks;
    private final StepEntity identifiedItem;

    public StepChainBasedGeometricItemSpecificUsage(int id, String name, String description, StepEntity usage, List<StepRepresentation> nodes, List<StepRepresentationRelationship> undirectedLinks, StepEntity identifiedItem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.undirectedLinks = undirectedLinks == null ? null : java.util.List.copyOf(undirectedLinks);
        this.identifiedItem = identifiedItem;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getUsage() {
        return usage;
    }

    public List<StepRepresentation> getNodes() {
        return nodes;
    }

    public List<StepRepresentationRelationship> getUndirectedLinks() {
        return undirectedLinks;
    }

    public StepEntity getIdentifiedItem() {
        return identifiedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChainBasedGeometricItemSpecificUsage that = (StepChainBasedGeometricItemSpecificUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(usage, that.usage) && Objects.equals(nodes, that.nodes) && Objects.equals(undirectedLinks, that.undirectedLinks) && Objects.equals(identifiedItem, that.identifiedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, usage, nodes, undirectedLinks, identifiedItem);
    }

    @Override
    public String toString() {
        return "StepChainBasedGeometricItemSpecificUsage{" + "id=" + id + "name=" + name + "description=" + description + "usage=" + usage + "nodes=" + nodes + "undirectedLinks=" + undirectedLinks + "identifiedItem=" + identifiedItem + "}";
    }
}
