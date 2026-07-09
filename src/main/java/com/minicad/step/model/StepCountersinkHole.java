package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COUNTERSINK_HOLE.
 * Represents a countersink hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name countersink name
 * @param throughHole through hole reference
 * @param countersinkDiameter countersink diameter
 * @param countersinkAngle countersink angle
 */
/**
 * Resolved COUNTERSINK_HOLE.
 * Represents a countersink hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name countersink name
 * @param throughHole through hole reference
 * @param countersinkDiameter countersink diameter
 * @param countersinkAngle countersink angle
 */
public final class StepCountersinkHole implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity throughHole;
    private final Double countersinkDiameter;
    private final Double countersinkAngle;

    public StepCountersinkHole(int id, String name, StepEntity throughHole, Double countersinkDiameter, Double countersinkAngle) {
        this.id = id;
        this.name = name;
        this.throughHole = throughHole;
        this.countersinkDiameter = countersinkDiameter;
        this.countersinkAngle = countersinkAngle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getThroughHole() {
        return throughHole;
    }

    public Double getCountersinkDiameter() {
        return countersinkDiameter;
    }

    public Double getCountersinkAngle() {
        return countersinkAngle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCountersinkHole that = (StepCountersinkHole) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(throughHole, that.throughHole) && Objects.equals(countersinkDiameter, that.countersinkDiameter) && Objects.equals(countersinkAngle, that.countersinkAngle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, throughHole, countersinkDiameter, countersinkAngle);
    }

    @Override
    public String toString() {
        return "StepCountersinkHole{" + "id=" + id + "name=" + name + "throughHole=" + throughHole + "countersinkDiameter=" + countersinkDiameter + "countersinkAngle=" + countersinkAngle + "}";
    }
}