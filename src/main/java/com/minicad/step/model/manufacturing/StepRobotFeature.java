package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ROBOT_FEATURE.
 * A robot feature entity.
 *
 * @param id STEP instance id
 * @param name robot name
 * @param robotType robot type (articulated, SCARA, cartesian)
 * @param robotGeometry robot geometry representation
 * @param numberOfAxes number of robot axes
 * @param reachRange robot reach range specification
 * @param payloadCapacity robot payload capacity
 * @varianceSpeed robot variance speed specification
 */
public record StepRobotFeature(
    int id,
    String name,
    String robotType,
    StepEntity robotGeometry,
    int numberOfAxes,
    List<Double> reachRange,
    double payloadCapacity,
    double varianceSpeed) implements StepEntity {
}