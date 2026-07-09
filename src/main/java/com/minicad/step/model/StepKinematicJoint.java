package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

public final class StepKinematicJoint implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity jointGeometry;

    public StepKinematicJoint(int id, String name, String description, StepEntity jointGeometry) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.jointGeometry = jointGeometry;
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

    public StepEntity getJointGeometry() {
        return jointGeometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicJoint that = (StepKinematicJoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(jointGeometry, that.jointGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, jointGeometry);
    }

    @Override
    public String toString() {
        return "StepKinematicJoint{" + "id=" + id + "name=" + name + "description=" + description + "jointGeometry=" + jointGeometry + "}";
    }
}