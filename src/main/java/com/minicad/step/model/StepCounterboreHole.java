package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COUNTERBORE_HOLE.
 * Represents a counterbore hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name counterbore name
 * @param throughHole through hole reference
 * @param counterboreDiameter counterbore diameter
 * @param counterboreDepth counterbore depth
 */
/**
 * Resolved COUNTERBORE_HOLE.
 * Represents a counterbore hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name counterbore name
 * @param throughHole through hole reference
 * @param counterboreDiameter counterbore diameter
 * @param counterboreDepth counterbore depth
 */
public final class StepCounterboreHole implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity throughHole;
    private final Double counterboreDiameter;
    private final Double counterboreDepth;

    public StepCounterboreHole(int id, String name, StepEntity throughHole, Double counterboreDiameter, Double counterboreDepth) {
        this.id = id;
        this.name = name;
        this.throughHole = throughHole;
        this.counterboreDiameter = counterboreDiameter;
        this.counterboreDepth = counterboreDepth;
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

    public Double getCounterboreDiameter() {
        return counterboreDiameter;
    }

    public Double getCounterboreDepth() {
        return counterboreDepth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCounterboreHole that = (StepCounterboreHole) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(throughHole, that.throughHole) && Objects.equals(counterboreDiameter, that.counterboreDiameter) && Objects.equals(counterboreDepth, that.counterboreDepth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, throughHole, counterboreDiameter, counterboreDepth);
    }

    @Override
    public String toString() {
        return "StepCounterboreHole{" + "id=" + id + "name=" + name + "throughHole=" + throughHole + "counterboreDiameter=" + counterboreDiameter + "counterboreDepth=" + counterboreDepth + "}";
    }
}