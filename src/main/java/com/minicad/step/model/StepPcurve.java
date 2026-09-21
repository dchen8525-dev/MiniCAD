package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal resolved PCURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param basisSurface basis surface
 * @param referenceToCurve referenced definitional representation
 */
public final class StepPcurve extends AbstractStepEntity {
    private final StepEntity basisSurface;
    private final StepRepresentation referenceToCurve;

    public StepPcurve(int id, String name, StepEntity basisSurface, StepRepresentation referenceToCurve) {
        super(id, name);
        this.basisSurface = basisSurface;
        this.referenceToCurve = referenceToCurve;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public StepRepresentation getReferenceToCurve() {
        return referenceToCurve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public StepRepresentation referenceToCurve() { return getReferenceToCurve(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisSurface", basisSurface);
        state.put("referenceToCurve", referenceToCurve);
        return state;
    }
}
