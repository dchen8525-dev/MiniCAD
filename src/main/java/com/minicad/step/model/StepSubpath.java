package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SUBPATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 * @param parentPath parent path entity
 */
public record StepSubpath(int id, String name, List<StepOrientedEdge> edges, StepEntity parentPath)
    implements StepEntity {

  public StepSubpath {
    edges = List.copyOf(edges);
  }
}
