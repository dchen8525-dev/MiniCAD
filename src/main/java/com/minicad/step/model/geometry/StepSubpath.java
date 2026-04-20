package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepOrientedEdge;

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
