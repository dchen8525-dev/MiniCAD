package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TOOLPATH_SPEED_PROFILE.
 * A toolpath speed profile representation entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param speedValues speed values along the toolpath
 * @param feedValues feed values along the toolpath
 * @param positionPoints position points for profile values
 */
/**
 * Resolved TOOLPATH_SPEED_PROFILE.
 * A toolpath speed profile representation entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param speedValues speed values along the toolpath
 * @param feedValues feed values along the toolpath
 * @param positionPoints position points for profile values
 */
public final class StepToolpathSpeedProfile implements StepEntity {
    private final int id;
    private final String name;
    private final List<Double> speedValues;
    private final List<Double> feedValues;
    private final List<StepEntity> positionPoints;

    public StepToolpathSpeedProfile(int id, String name, List<Double> speedValues, List<Double> feedValues, List<StepEntity> positionPoints) {
        this.id = id;
        this.name = name;
        this.speedValues = speedValues == null ? null : java.util.List.copyOf(speedValues);
        this.feedValues = feedValues == null ? null : java.util.List.copyOf(feedValues);
        this.positionPoints = positionPoints == null ? null : java.util.List.copyOf(positionPoints);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Double> getSpeedValues() {
        return speedValues;
    }

    public List<Double> getFeedValues() {
        return feedValues;
    }

    public List<StepEntity> getPositionPoints() {
        return positionPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToolpathSpeedProfile that = (StepToolpathSpeedProfile) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(speedValues, that.speedValues) && Objects.equals(feedValues, that.feedValues) && Objects.equals(positionPoints, that.positionPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, speedValues, feedValues, positionPoints);
    }

    @Override
    public String toString() {
        return "StepToolpathSpeedProfile{" + "id=" + id + "name=" + name + "speedValues=" + speedValues + "feedValues=" + feedValues + "positionPoints=" + positionPoints + "}";
    }
}