package com.minicad.step.model.technical.kinematic;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved UNIVERSAL_PAIR.
 * A universal (Hooke's joint) kinematic pair allowing rotation about two intersecting axes.
 */
/**
 * Resolved UNIVERSAL_PAIR.
 * A universal (Hooke's joint) kinematic pair allowing rotation about two intersecting axes.
 */
public final class StepUniversalPair implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity position;
    private final StepEntity axis1;
    private final StepEntity axis2;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepUniversalPair(int id, String name, String description, StepEntity position, StepEntity axis1, StepEntity axis2, StepEntity link1, StepEntity link2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.link1 = link1;
        this.link2 = link2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getAxis1() {
        return axis1;
    }

    public StepEntity getAxis2() {
        return axis2;
    }

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepUniversalPair that = (StepUniversalPair) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(position, that.position) && Objects.equals(axis1, that.axis1) && Objects.equals(axis2, that.axis2) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, position, axis1, axis2, link1, link2);
    }

    @Override
    public String toString() {
        return "StepUniversalPair{" + "id=" + id + "name=" + name + "description=" + description + "position=" + position + "axis1=" + axis1 + "axis2=" + axis2 + "link1=" + link1 + "link2=" + link2 + "}";
    }
}
