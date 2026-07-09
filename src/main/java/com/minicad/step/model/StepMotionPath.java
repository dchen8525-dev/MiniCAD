package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMotionPath implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity pathGeometry;
    private final String motionType;
    private final StepEntity motionSpeed;
    private final StepEntity motionAcceleration;
    private final StepEntity startPosition;
    private final StepEntity endPosition;

    public StepMotionPath(int id, String name, StepEntity pathGeometry, String motionType, StepEntity motionSpeed, StepEntity motionAcceleration, StepEntity startPosition, StepEntity endPosition) {
        this.id = id;
        this.name = name;
        this.pathGeometry = pathGeometry;
        this.motionType = motionType;
        this.motionSpeed = motionSpeed;
        this.motionAcceleration = motionAcceleration;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPathGeometry() {
        return pathGeometry;
    }

    public String getMotionType() {
        return motionType;
    }

    public StepEntity getMotionSpeed() {
        return motionSpeed;
    }

    public StepEntity getMotionAcceleration() {
        return motionAcceleration;
    }

    public StepEntity getStartPosition() {
        return startPosition;
    }

    public StepEntity getEndPosition() {
        return endPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMotionPath that = (StepMotionPath) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pathGeometry, that.pathGeometry) && Objects.equals(motionType, that.motionType) && Objects.equals(motionSpeed, that.motionSpeed) && Objects.equals(motionAcceleration, that.motionAcceleration) && Objects.equals(startPosition, that.startPosition) && Objects.equals(endPosition, that.endPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pathGeometry, motionType, motionSpeed, motionAcceleration, startPosition, endPosition);
    }

    @Override
    public String toString() {
        return "StepMotionPath{" + "id=" + id + "name=" + name + "pathGeometry=" + pathGeometry + "motionType=" + motionType + "motionSpeed=" + motionSpeed + "motionAcceleration=" + motionAcceleration + "startPosition=" + startPosition + "endPosition=" + endPosition + "}";
    }
}