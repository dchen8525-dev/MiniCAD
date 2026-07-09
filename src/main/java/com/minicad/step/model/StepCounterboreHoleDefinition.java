package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COUNTERBORE_HOLE_DEFINITION.
 * A counterbore hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param counterboreDiameter diameter of the counterbore
 * @param counterboreDepth depth of the counterbore
 */
/**
 * Resolved COUNTERBORE_HOLE_DEFINITION.
 * A counterbore hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param counterboreDiameter diameter of the counterbore
 * @param counterboreDepth depth of the counterbore
 */
public final class StepCounterboreHoleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity throughHoleReference;
    private final Double counterboreDiameter;
    private final Double counterboreDepth;

    public StepCounterboreHoleDefinition(int id, String name, StepEntity throughHoleReference, Double counterboreDiameter, Double counterboreDepth) {
        this.id = id;
        this.name = name;
        this.throughHoleReference = throughHoleReference;
        this.counterboreDiameter = counterboreDiameter;
        this.counterboreDepth = counterboreDepth;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getThroughHoleReference() {
        return throughHoleReference;
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
        StepCounterboreHoleDefinition that = (StepCounterboreHoleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(throughHoleReference, that.throughHoleReference) && Objects.equals(counterboreDiameter, that.counterboreDiameter) && Objects.equals(counterboreDepth, that.counterboreDepth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, throughHoleReference, counterboreDiameter, counterboreDepth);
    }

    @Override
    public String toString() {
        return "StepCounterboreHoleDefinition{" + "id=" + id + "name=" + name + "throughHoleReference=" + throughHoleReference + "counterboreDiameter=" + counterboreDiameter + "counterboreDepth=" + counterboreDepth + "}";
    }
}