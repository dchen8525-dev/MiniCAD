package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EDGE_WIRE.
 * A wire formed by a sequence of edges.
 *
 * @param id STEP instance id
 * @param name wire name
 * @param edges ordered list of edges forming the wire
 */
public record StepEdgeWire(int id, String name, List<StepEntity> edges) implements StepEntity {

  public StepEdgeWire {
    edges = List.copyOf(edges);
  }
}
