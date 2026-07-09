package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COUNTERSINK_HOLE_DEFINITION.
 * A countersink hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param countersinkDiameter diameter of the countersink
 * @param countersinkAngle angle of the countersink
 */
/**
 * Resolved COUNTERSINK_HOLE_DEFINITION.
 * A countersink hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param countersinkDiameter diameter of the countersink
 * @param countersinkAngle angle of the countersink
 */
public final class StepCountersinkHoleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity throughHoleReference;
    private final Double countersinkDiameter;
    private final Double countersinkAngle;

    public StepCountersinkHoleDefinition(int id, String name, StepEntity throughHoleReference, Double countersinkDiameter, Double countersinkAngle) {
        this.id = id;
        this.name = name;
        this.throughHoleReference = throughHoleReference;
        this.countersinkDiameter = countersinkDiameter;
        this.countersinkAngle = countersinkAngle;
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
        StepCountersinkHoleDefinition that = (StepCountersinkHoleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(throughHoleReference, that.throughHoleReference) && Objects.equals(countersinkDiameter, that.countersinkDiameter) && Objects.equals(countersinkAngle, that.countersinkAngle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, throughHoleReference, countersinkDiameter, countersinkAngle);
    }

    @Override
    public String toString() {
        return "StepCountersinkHoleDefinition{" + "id=" + id + "name=" + name + "throughHoleReference=" + throughHoleReference + "countersinkDiameter=" + countersinkDiameter + "countersinkAngle=" + countersinkAngle + "}";
    }
}