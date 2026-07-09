package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
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
public final class StepTrimmedCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepCurve basisCurve;
    private final double trim1;
    private final double trim2;
    private final boolean senseAgreement;

    public StepTrimmedCurve2D(int id, String name, StepCurve basisCurve, double trim1, double trim2, boolean senseAgreement) {
        this.id = id;
        this.name = name;
        this.basisCurve = basisCurve;
        this.trim1 = trim1;
        this.trim2 = trim2;
        this.senseAgreement = senseAgreement;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTrimmedCurve2D that = (StepTrimmedCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisCurve, that.basisCurve) && trim1 == that.trim1 && trim2 == that.trim2 && senseAgreement == that.senseAgreement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisCurve, trim1, trim2, senseAgreement);
    }

    @Override
    public String toString() {
        return "StepTrimmedCurve2D{" + "id=" + id + "name=" + name + "basisCurve=" + basisCurve + "trim1=" + trim1 + "trim2=" + trim2 + "senseAgreement=" + senseAgreement + "}";
    }
}
