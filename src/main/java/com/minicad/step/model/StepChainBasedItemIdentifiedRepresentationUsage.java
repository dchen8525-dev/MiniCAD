package com.minicad.step.model;

import java.util.List;

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
public record StepChainBasedItemIdentifiedRepresentationUsage(
        int id,
        String name,
        String description,
        StepEntity definition,
        List<StepRepresentation> nodes,
        List<StepRepresentationRelationship> undirectedLinks,
        StepEntity identifiedItem
) implements StepEntity {

    public StepChainBasedItemIdentifiedRepresentationUsage {
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
