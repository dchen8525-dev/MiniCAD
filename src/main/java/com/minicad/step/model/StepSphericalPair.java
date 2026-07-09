package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SPHERICAL_PAIR.
 * A spherical (ball-and-socket) kinematic pair allowing rotation about three axes.
 */
/**
 * Resolved SPHERICAL_PAIR.
 * A spherical (ball-and-socket) kinematic pair allowing rotation about three axes.
 */
public final class StepSphericalPair implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity position;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepSphericalPair(int id, String name, String description, StepEntity position, StepEntity link1, StepEntity link2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
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
        StepSphericalPair that = (StepSphericalPair) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(position, that.position) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, position, link1, link2);
    }

    @Override
    public String toString() {
        return "StepSphericalPair{" + "id=" + id + "name=" + name + "description=" + description + "position=" + position + "link1=" + link1 + "link2=" + link2 + "}";
    }
}
