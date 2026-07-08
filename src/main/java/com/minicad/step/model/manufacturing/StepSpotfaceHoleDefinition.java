package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SPOTFACE_HOLE_DEFINITION.
 * A spotface hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param spotfaceDiameter diameter of the spotface
 * @param spotfaceDepth depth of the spotface
 */
/**
 * Resolved SPOTFACE_HOLE_DEFINITION.
 * A spotface hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param spotfaceDiameter diameter of the spotface
 * @param spotfaceDepth depth of the spotface
 */
public final class StepSpotfaceHoleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity throughHoleReference;
    private final Double spotfaceDiameter;
    private final Double spotfaceDepth;

    public StepSpotfaceHoleDefinition(int id, String name, StepEntity throughHoleReference, Double spotfaceDiameter, Double spotfaceDepth) {
        this.id = id;
        this.name = name;
        this.throughHoleReference = throughHoleReference;
        this.spotfaceDiameter = spotfaceDiameter;
        this.spotfaceDepth = spotfaceDepth;
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

    public Double getSpotfaceDiameter() {
        return spotfaceDiameter;
    }

    public Double getSpotfaceDepth() {
        return spotfaceDepth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSpotfaceHoleDefinition that = (StepSpotfaceHoleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(throughHoleReference, that.throughHoleReference) && Objects.equals(spotfaceDiameter, that.spotfaceDiameter) && Objects.equals(spotfaceDepth, that.spotfaceDepth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, throughHoleReference, spotfaceDiameter, spotfaceDepth);
    }

    @Override
    public String toString() {
        return "StepSpotfaceHoleDefinition{" + "id=" + id + "name=" + name + "throughHoleReference=" + throughHoleReference + "spotfaceDiameter=" + spotfaceDiameter + "spotfaceDepth=" + spotfaceDepth + "}";
    }
}