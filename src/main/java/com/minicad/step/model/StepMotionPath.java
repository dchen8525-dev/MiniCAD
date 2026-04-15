package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MOTION_PATH.
 * A motion path entity.
 *
 * @param id STEP instance id
 * @param name path name
 * @param pathGeometry path geometry curve
 * @param motionType motion type (linear, circular, spline)
 * @param motionSpeed motion speed profile
 * @param motionAcceleration motion acceleration profile
 * @param startPosition start position point
 * @param endPosition end position point
 */
public record StepMotionPath(
    int id,
    String name,
    StepEntity pathGeometry,
    String motionType,
    StepEntity motionSpeed,
    StepEntity motionAcceleration,
    StepEntity startPosition,
    StepEntity endPosition) implements StepEntity {
}