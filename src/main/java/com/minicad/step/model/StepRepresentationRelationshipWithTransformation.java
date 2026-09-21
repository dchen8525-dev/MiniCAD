package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal representation relationship with transformation.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 * @param transformationOperator linked item-defined transformation
 */
public final class StepRepresentationRelationshipWithTransformation extends AbstractStepEntity {
    private final String description;
    private final StepRepresentation rep1;
    private final StepRepresentation rep2;
    private final StepItemDefinedTransformation transformationOperator;

    public StepRepresentationRelationshipWithTransformation(int id, String name, String description, StepRepresentation rep1, StepRepresentation rep2, StepItemDefinedTransformation transformationOperator) {
        super(id, name);
        this.description = description;
        this.rep1 = rep1;
        this.rep2 = rep2;
        this.transformationOperator = transformationOperator;
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

    public StepItemDefinedTransformation getTransformationOperator() {
        return transformationOperator;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String description() { return description; }
    public StepRepresentation rep1() { return rep1; }
    public StepRepresentation rep2() { return rep2; }
    public StepItemDefinedTransformation transformationOperator() { return transformationOperator; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("rep1", rep1);
        state.put("rep2", rep2);
        state.put("transformationOperator", transformationOperator);
        return state;
    }
}
