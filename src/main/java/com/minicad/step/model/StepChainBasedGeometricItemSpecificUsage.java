package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepChainBasedGeometricItemSpecificUsage extends AbstractStepEntity {
    private final String description;
    private final StepEntity usage;
    private final List<StepRepresentation> nodes;
    private final List<StepRepresentationRelationship> undirectedLinks;
    private final StepEntity identifiedItem;

    public StepChainBasedGeometricItemSpecificUsage(int id, String name, String description, StepEntity usage, List<StepRepresentation> nodes, List<StepRepresentationRelationship> undirectedLinks, StepEntity identifiedItem) {
        super(id, name);
        this.description = description;
        this.usage = usage;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.undirectedLinks = undirectedLinks == null ? null : java.util.List.copyOf(undirectedLinks);
        this.identifiedItem = identifiedItem;
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

    // Record-style accessors
    public String name() {
        return getName();
    }

    public String description() {
        return description;
    }

    public StepEntity usage() {
        return usage;
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

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("usage", usage);
        state.put("nodes", nodes);
        state.put("undirectedLinks", undirectedLinks);
        state.put("identifiedItem", identifiedItem);
        return state;
    }
}
