package com.minicad.step.model.technical.kinematic;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SCREW_JOINT.
 * A screw joint between two links.
 */
/**
 * Resolved SCREW_JOINT.
 * A screw joint between two links.
 */
public final class StepScrewJoint implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity link1;
    private final StepEntity link2;
    private final double pitch;

    public StepScrewJoint(int id, String name, String description, StepEntity link1, StepEntity link2, double pitch) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.pitch = pitch;
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

    public double getPitch() {
        return pitch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepScrewJoint that = (StepScrewJoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2) && pitch == that.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, link1, link2, pitch);
    }

    @Override
    public String toString() {
        return "StepScrewJoint{" + "id=" + id + "name=" + name + "description=" + description + "link1=" + link1 + "link2=" + link2 + "pitch=" + pitch + "}";
    }
}
