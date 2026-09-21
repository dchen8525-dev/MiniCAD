package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal draughting callout relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingCallout source callout
 * @param relatedCallout target callout
 */
public final class StepDraughtingCalloutRelationship extends AbstractStepEntity {
    private final String description;
    private final StepDraughtingCallout relatingCallout;
    private final StepDraughtingCallout relatedCallout;

    public StepDraughtingCalloutRelationship(int id, String name, String description, StepDraughtingCallout relatingCallout, StepDraughtingCallout relatedCallout) {
        super(id, name);
        this.description = description;
        this.relatingCallout = relatingCallout;
        this.relatedCallout = relatedCallout;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingCallout", relatingCallout);
        state.put("relatedCallout", relatedCallout);
        return state;
    }
}
