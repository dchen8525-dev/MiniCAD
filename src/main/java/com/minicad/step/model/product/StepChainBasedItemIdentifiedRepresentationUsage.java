package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;

/**
 * Minimal chain-based item identified representation usage.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param nodes representation chain nodes
 * @param undirectedLinks chain links
 * @param identifiedItem identified item reference
 */
/**
 * Minimal chain-based item identified representation usage.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param nodes representation chain nodes
 * @param undirectedLinks chain links
 * @param identifiedItem identified item reference
 */
public final class StepChainBasedItemIdentifiedRepresentationUsage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity definition;
    private final List<StepRepresentation> nodes;
    private final List<StepRepresentationRelationship> undirectedLinks;
    private final StepEntity identifiedItem;

    public StepChainBasedItemIdentifiedRepresentationUsage(int id, String name, String description, StepEntity definition, List<StepRepresentation> nodes, List<StepRepresentationRelationship> undirectedLinks, StepEntity identifiedItem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.definition = definition;
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

    public StepEntity getDefinition() {
        return definition;
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

    // Record-style accessors
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public StepEntity definition() {
        return definition;
    }

    public List<StepRepresentation> nodes() {
        return nodes;
    }

    public List<StepRepresentationRelationship> undirectedLinks() {
        return undirectedLinks;
    }

    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    /**
     * Returns the leaf representation from the chain (last node).
     */
    public StepRepresentation leaf() {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        return nodes.get(nodes.size() - 1);
    }

    /**
     * Returns the root representation from the chain (first node).
     */
    public StepRepresentation root() {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        return nodes.get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChainBasedItemIdentifiedRepresentationUsage that = (StepChainBasedItemIdentifiedRepresentationUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(definition, that.definition) && Objects.equals(nodes, that.nodes) && Objects.equals(undirectedLinks, that.undirectedLinks) && Objects.equals(identifiedItem, that.identifiedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, definition, nodes, undirectedLinks, identifiedItem);
    }

    @Override
    public String toString() {
        return "StepChainBasedItemIdentifiedRepresentationUsage{" + "id=" + id + "name=" + name + "description=" + description + "definition=" + definition + "nodes=" + nodes + "undirectedLinks=" + undirectedLinks + "identifiedItem=" + identifiedItem + "}";
    }
}
