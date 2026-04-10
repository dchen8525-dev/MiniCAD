package com.minicad.step.model;

import java.util.List;

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
public record StepChainBasedGeometricItemSpecificUsage(
        int id,
        String name,
        String description,
        StepEntity usage,
        List<StepRepresentation> nodes,
        List<StepRepresentationRelationship> undirectedLinks,
        StepEntity identifiedItem
) implements StepEntity {

    public StepChainBasedGeometricItemSpecificUsage {
        nodes = List.copyOf(nodes);
        undirectedLinks = List.copyOf(undirectedLinks);
    }

    public StepRepresentation root() {
        return nodes.get(0);
    }

    public StepRepresentation leaf() {
        return nodes.get(nodes.size() - 1);
    }
}
