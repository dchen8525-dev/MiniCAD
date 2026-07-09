package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepGeneralizedDatum implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity datumTarget;

    public StepGeneralizedDatum(int id, String name, String description, StepEntity datumTarget) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.datumTarget = datumTarget;
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

    public StepEntity getDatumTarget() {
        return datumTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeneralizedDatum that = (StepGeneralizedDatum) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(datumTarget, that.datumTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, datumTarget);
    }

    @Override
    public String toString() {
        return "StepGeneralizedDatum{" + "id=" + id + "name=" + name + "description=" + description + "datumTarget=" + datumTarget + "}";
    }
}