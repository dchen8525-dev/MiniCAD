package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal RECTANGULAR_TRIMMED_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface surface being trimmed
 * @param u1 lower u parameter
 * @param u2 upper u parameter
 * @param v1 lower v parameter
 * @param v2 upper v parameter
 * @param usense u direction sense
 * @param vsense v direction sense
 */
public final class StepRectangularTrimmedSurface extends AbstractStepEntity {
    private final StepEntity basisSurface;
    private final double u1;
    private final double u2;
    private final double v1;
    private final double v2;
    private final boolean usense;
    private final boolean vsense;

    public StepRectangularTrimmedSurface(int id, String name, StepEntity basisSurface, double u1, double u2, double v1, double v2, boolean usense, boolean vsense) {
        super(id, name);
        this.basisSurface = basisSurface;
        this.u1 = u1;
        this.u2 = u2;
        this.v1 = v1;
        this.v2 = v2;
        this.usense = usense;
        this.vsense = vsense;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public double getU1() {
        return u1;
    }

    public double getU2() {
        return u2;
    }

    public double getV1() {
        return v1;
    }

    public double getV2() {
        return v2;
    }

    public boolean isUsense() {
        return usense;
    }

    public boolean isVsense() {
        return vsense;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public double u1() { return getU1(); }
    public double u2() { return getU2(); }
    public double v1() { return getV1(); }
    public double v2() { return getV2(); }
    public boolean usense() { return isUsense(); }
    public boolean vsense() { return isVsense(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "basisSurface", basisSurface, "u1", u1, "u2", u2, "v1", v1,
                "v2", v2, "usense", usense, "vsense", vsense);
    }
}
