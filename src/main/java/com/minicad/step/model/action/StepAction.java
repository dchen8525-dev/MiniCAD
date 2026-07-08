package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepAction implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final String actionMethod;

    public StepAction(int id, String name, String description, String actionMethod) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.actionMethod = actionMethod;
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

    public String getActionMethod() {
        return actionMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAction that = (StepAction) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(actionMethod, that.actionMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, actionMethod);
    }

    @Override
    public String toString() {
        return "StepAction{" + "id=" + id + "name=" + name + "description=" + description + "actionMethod=" + actionMethod + "}";
    }
}