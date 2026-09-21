package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TRIMMED_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param basisCurve the underlying 2D curve
 * @param trim1 first trim parameter
 * @param trim2 second trim parameter
 * @param senseAgreement whether the trimmed curve follows the same sense as the basis curve
 */
public final class StepTrimmedCurve2D extends AbstractStepEntity {
    private final StepCurve basisCurve;
    private final double trim1;
    private final double trim2;
    private final boolean senseAgreement;

    public StepTrimmedCurve2D(int id, String name, StepCurve basisCurve, double trim1, double trim2, boolean senseAgreement) {
        super(id, name);
        this.basisCurve = basisCurve;
        this.trim1 = trim1;
        this.trim2 = trim2;
        this.senseAgreement = senseAgreement;
    }

    public StepCurve getBasisCurve() {
        return basisCurve;
    }

    public double getTrim1() {
        return trim1;
    }

    public double getTrim2() {
        return trim2;
    }

    public boolean isSenseAgreement() {
        return senseAgreement;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCurve basisCurve() { return getBasisCurve(); }
    public double trim1() { return getTrim1(); }
    public double trim2() { return getTrim2(); }
    public boolean senseAgreement() { return isSenseAgreement(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisCurve", basisCurve);
        state.put("trim1", trim1);
        state.put("trim2", trim2);
        state.put("senseAgreement", senseAgreement);
        return state;
    }
}
