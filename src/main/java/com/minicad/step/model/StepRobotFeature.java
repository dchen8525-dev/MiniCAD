package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepRobotFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String robotType;
    private final StepEntity robotGeometry;
    private final int numberOfAxes;
    private final List<Double> reachRange;
    private final double payloadCapacity;
    private final double varianceSpeed;

    public StepRobotFeature(int id, String name, String robotType, StepEntity robotGeometry, int numberOfAxes, List<Double> reachRange, double payloadCapacity, double varianceSpeed) {
        this.id = id;
        this.name = name;
        this.robotType = robotType;
        this.robotGeometry = robotGeometry;
        this.numberOfAxes = numberOfAxes;
        this.reachRange = reachRange == null ? null : java.util.List.copyOf(reachRange);
        this.payloadCapacity = payloadCapacity;
        this.varianceSpeed = varianceSpeed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRobotType() {
        return robotType;
    }

    public StepEntity getRobotGeometry() {
        return robotGeometry;
    }

    public int getNumberOfAxes() {
        return numberOfAxes;
    }

    public List<Double> getReachRange() {
        return reachRange;
    }

    public double getPayloadCapacity() {
        return payloadCapacity;
    }

    public double getVarianceSpeed() {
        return varianceSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRobotFeature that = (StepRobotFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(robotType, that.robotType) && Objects.equals(robotGeometry, that.robotGeometry) && numberOfAxes == that.numberOfAxes && Objects.equals(reachRange, that.reachRange) && payloadCapacity == that.payloadCapacity && varianceSpeed == that.varianceSpeed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, robotType, robotGeometry, numberOfAxes, reachRange, payloadCapacity, varianceSpeed);
    }

    @Override
    public String toString() {
        return "StepRobotFeature{" + "id=" + id + "name=" + name + "robotType=" + robotType + "robotGeometry=" + robotGeometry + "numberOfAxes=" + numberOfAxes + "reachRange=" + reachRange + "payloadCapacity=" + payloadCapacity + "varianceSpeed=" + varianceSpeed + "}";
    }
}