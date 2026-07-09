package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepActionRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity relatingAction;
    private final StepEntity relatedAction;

    public StepActionRelationship(int id, String name, String description, StepEntity relatingAction, StepEntity relatedAction) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingAction = relatingAction;
        this.relatedAction = relatedAction;
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

    public StepEntity getRelatingAction() {
        return relatingAction;
    }

    public StepEntity getRelatedAction() {
        return relatedAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActionRelationship that = (StepActionRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingAction, that.relatingAction) && Objects.equals(relatedAction, that.relatedAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingAction, relatedAction);
    }

    @Override
    public String toString() {
        return "StepActionRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingAction=" + relatingAction + "relatedAction=" + relatedAction + "}";
    }
}