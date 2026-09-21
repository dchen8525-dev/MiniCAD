package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal shape representation relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 */
public final class StepShapeRepresentationRelationship extends AbstractStepEntity {
    private final String description;
    private final StepRepresentation rep1;
    private final StepRepresentation rep2;

    public StepShapeRepresentationRelationship(int id, String name, String description, StepRepresentation rep1, StepRepresentation rep2) {
        super(id, name);
        this.description = description;
        this.rep1 = rep1;
        this.rep2 = rep2;
    }

    public String getDescription() {
        return description;
    }

    public StepRepresentation getRep1() {
        return rep1;
    }

    public StepRepresentation getRep2() {
        return rep2;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String description() { return description; }
    public StepRepresentation rep1() { return rep1; }
    public StepRepresentation rep2() { return rep2; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("rep1", rep1);
        state.put("rep2", rep2);
        return state;
    }
}
