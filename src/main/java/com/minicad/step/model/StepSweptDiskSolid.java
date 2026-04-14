package com.minicad.step.model;

/**
 * Resolved SWEPT_DISK_SOLID.
 * A solid formed by sweeping a circular disk along a curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptCurve the curve along which to sweep
 * @param radius disk radius
 * @param innerRadius inner disk radius (0 for solid disk)
 */
public record StepSweptDiskSolid(
    int id,
    String name,
    StepEntity sweptCurve,
    double radius,
    Double innerRadius) implements StepEntity {
}
