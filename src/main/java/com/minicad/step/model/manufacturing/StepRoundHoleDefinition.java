package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ROUND_HOLE_DEFINITION.
 * A round hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param diameter hole diameter
 * @param depth hole depth
 * @param bottomType bottom type (through, blind, flat, etc)
 */
/**
 * Resolved ROUND_HOLE_DEFINITION.
 * A round hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param diameter hole diameter
 * @param depth hole depth
 * @param bottomType bottom type (through, blind, flat, etc)
 */
public final class StepRoundHoleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final Double diameter;
    private final Double depth;
    private final String bottomType;

    public StepRoundHoleDefinition(int id, String name, Double diameter, Double depth, String bottomType) {
        this.id = id;
        this.name = name;
        this.diameter = diameter;
        this.depth = depth;
        this.bottomType = bottomType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getDiameter() {
        return diameter;
    }

    public Double getDepth() {
        return depth;
    }

    public String getBottomType() {
        return bottomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRoundHoleDefinition that = (StepRoundHoleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(diameter, that.diameter) && Objects.equals(depth, that.depth) && Objects.equals(bottomType, that.bottomType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, diameter, depth, bottomType);
    }

    @Override
    public String toString() {
        return "StepRoundHoleDefinition{" + "id=" + id + "name=" + name + "diameter=" + diameter + "depth=" + depth + "bottomType=" + bottomType + "}";
    }
}