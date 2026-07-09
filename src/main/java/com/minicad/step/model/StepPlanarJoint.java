package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PLANAR_JOINT.
 * A planar joint between two links.
 */
/**
 * Resolved PLANAR_JOINT.
 * A planar joint between two links.
 */
public final class StepPlanarJoint implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity link1;
    private final StepEntity link2;
    private final StepEntity plane;

    public StepPlanarJoint(int id, String name, String description, StepEntity link1, StepEntity link2, StepEntity plane) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.plane = plane;
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

    public StepEntity getPlane() {
        return plane;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlanarJoint that = (StepPlanarJoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2) && Objects.equals(plane, that.plane);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, link1, link2, plane);
    }

    @Override
    public String toString() {
        return "StepPlanarJoint{" + "id=" + id + "name=" + name + "description=" + description + "link1=" + link1 + "link2=" + link2 + "plane=" + plane + "}";
    }
}
