package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal draughting callout relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingCallout source callout
 * @param relatedCallout target callout
 */
/**
 * Minimal draughting callout relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingCallout source callout
 * @param relatedCallout target callout
 */
public final class StepDraughtingCalloutRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepDraughtingCallout relatingCallout;
    private final StepDraughtingCallout relatedCallout;

    public StepDraughtingCalloutRelationship(int id, String name, String description, StepDraughtingCallout relatingCallout, StepDraughtingCallout relatedCallout) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingCallout = relatingCallout;
        this.relatedCallout = relatedCallout;
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

    public StepDraughtingCallout getRelatingCallout() {
        return relatingCallout;
    }

    public StepDraughtingCallout getRelatedCallout() {
        return relatedCallout;
    }

    // Record-style accessors
    public StepDraughtingCallout relatingCallout() {
        return relatingCallout;
    }

    public StepDraughtingCallout relatedCallout() {
        return relatedCallout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDraughtingCalloutRelationship that = (StepDraughtingCalloutRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingCallout, that.relatingCallout) && Objects.equals(relatedCallout, that.relatedCallout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingCallout, relatedCallout);
    }

    @Override
    public String toString() {
        return "StepDraughtingCalloutRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingCallout=" + relatingCallout + "relatedCallout=" + relatedCallout + "}";
    }
}
