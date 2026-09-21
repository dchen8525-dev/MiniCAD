package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal parse-only half-space solid.
 *
 * @param id step id
 * @param name step label
 * @param baseSurface boundary surface
 * @param agreementFlag side agreement flag
 * @param enclosure optional enclosure entity for boxed half spaces
 * @param entityName concrete STEP entity name
 */
public final class StepHalfSpaceSolid extends AbstractStepEntity {
    private final StepEntity baseSurface;
    private final boolean agreementFlag;
    private final StepEntity enclosure;
    private final String entityName;

    public StepHalfSpaceSolid(int id, String name, StepEntity baseSurface, boolean agreementFlag, StepEntity enclosure, String entityName) {
        super(id, name);
        this.baseSurface = baseSurface;
        this.agreementFlag = agreementFlag;
        this.enclosure = enclosure;
        this.entityName = entityName;
    }

    public StepEntity getBaseSurface() {
        return baseSurface;
    }

    public boolean isAgreementFlag() {
        return agreementFlag;
    }

    public StepEntity getEnclosure() {
        return enclosure;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity baseSurface() { return getBaseSurface(); }
    public boolean agreementFlag() { return isAgreementFlag(); }
    public StepEntity enclosure() { return getEnclosure(); }
    public String entityName() { return getEntityName(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("baseSurface", baseSurface);
        state.put("agreementFlag", agreementFlag);
        state.put("enclosure", enclosure);
        state.put("entityName", entityName);
        return state;
    }
}
