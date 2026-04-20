package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved JOINT_CONFIGURATION.
 * A joint configuration entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param joint reference kinematic joint
 * @param jointType joint type (revolute, prismatic, spherical)
 * @param jointPosition joint position/angle value
 * @param jointVelocity joint velocity
 * @param jointLimits joint limit values
 * @param jointMotion joint motion direction
 */
public record StepJointConfiguration(
    int id,
    String name,
    StepEntity joint,
    String jointType,
    double jointPosition,
    double jointVelocity,
    List<Double> jointLimits,
    String jointMotion) implements StepEntity {
}