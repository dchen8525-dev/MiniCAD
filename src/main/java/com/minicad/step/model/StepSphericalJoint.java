package com.minicad.step.model.technical.kinematic;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SPHERICAL_JOINT.
 * A spherical (ball) joint between two links.
 */
/**
 * Resolved SPHERICAL_JOINT.
 * A spherical (ball) joint between two links.
 */
public final class StepSphericalJoint implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity link1;
    private final StepEntity link2;
    private final StepEntity center;

    public StepSphericalJoint(int id, String name, String description, StepEntity link1, StepEntity link2, StepEntity center) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.center = center;
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

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    public StepEntity getCenter() {
        return center;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSphericalJoint that = (StepSphericalJoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2) && Objects.equals(center, that.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, link1, link2, center);
    }

    @Override
    public String toString() {
        return "StepSphericalJoint{" + "id=" + id + "name=" + name + "description=" + description + "link1=" + link1 + "link2=" + link2 + "center=" + center + "}";
    }
}
