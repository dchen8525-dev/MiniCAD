package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

public final class StepActionMethod implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final String method;

    public StepActionMethod(int id, String name, String description, String method) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.method = method;
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

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActionMethod that = (StepActionMethod) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, method);
    }

    @Override
    public String toString() {
        return "StepActionMethod{" + "id=" + id + "name=" + name + "description=" + description + "method=" + method + "}";
    }
}