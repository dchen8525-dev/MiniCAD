package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INDEXED_POLY_CURVE.
 * A polygonal curve defined by indices into a point list.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param points ordered list of cartesian points
 * @param indices indices into the points list defining the curve path
 * @param closed whether the curve is closed
 */
public record StepIndexedPolyCurve(
    int id,
    String name,
    List<StepCartesianPoint> points,
    List<Integer> indices,
    boolean closed) implements StepEntity {

  public StepIndexedPolyCurve {
    points = List.copyOf(points);
    indices = List.copyOf(indices);
  }
}
