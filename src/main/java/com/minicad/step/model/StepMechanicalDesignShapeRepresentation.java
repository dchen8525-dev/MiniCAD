package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepMechanicalDesignShapeRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity context;

    public StepMechanicalDesignShapeRepresentation(int id, String name, StepEntity context) {
        this.id = id;
        this.name = name;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getContext() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMechanicalDesignShapeRepresentation that = (StepMechanicalDesignShapeRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, context);
    }

    @Override
    public String toString() {
        return "StepMechanicalDesignShapeRepresentation{" + "id=" + id + "name=" + name + "context=" + context + "}";
    }
}