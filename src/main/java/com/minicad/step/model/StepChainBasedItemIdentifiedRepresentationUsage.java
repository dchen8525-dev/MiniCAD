package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepChainBasedItemIdentifiedRepresentationUsage extends AbstractStepEntity {
    private final String description;
    private final StepEntity definition;
    private final List<StepRepresentation> nodes;
    private final List<StepRepresentationRelationship> undirectedLinks;
    private final StepEntity identifiedItem;

    public StepChainBasedItemIdentifiedRepresentationUsage(int id, String name, String description, StepEntity definition, List<StepRepresentation> nodes, List<StepRepresentationRelationship> undirectedLinks, StepEntity identifiedItem) {
        super(id, name);
        this.description = description;
        this.definition = definition;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.undirectedLinks = undirectedLinks == null ? null : java.util.List.copyOf(undirectedLinks);
        this.identifiedItem = identifiedItem;
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
        return getName();
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("definition", definition);
        state.put("nodes", nodes);
        state.put("undirectedLinks", undirectedLinks);
        state.put("identifiedItem", identifiedItem);
        return state;
    }
}
