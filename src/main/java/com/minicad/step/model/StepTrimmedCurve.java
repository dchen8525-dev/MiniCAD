package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.syntax.StepValue;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRIMMED_CURVE for supported basis curves.
 * Trim values can be entity references (Cartesian points) or parameter values (numeric literals).
 *
 * @param id step id
 * @param name step label
 * @param basisCurve basis curve
 * @param trim1 first trim list (entity references or numeric parameter values)
 * @param trim2 second trim list (entity references or numeric parameter values)
 * @param senseAgreement orientation agreement
 * @param masterRepresentation trimming preference enum
 */
/**
 * Resolved TRIMMED_CURVE for supported basis curves.
 * Trim values can be entity references (Cartesian points) or parameter values (numeric literals).
 *
 * @param id step id
 * @param name step label
 * @param basisCurve basis curve
 * @param trim1 first trim list (entity references or numeric parameter values)
 * @param trim2 second trim list (entity references or numeric parameter values)
 * @param senseAgreement orientation agreement
 * @param masterRepresentation trimming preference enum
 */
public final class StepTrimmedCurve implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisCurve;
    private final List<StepValue> trim1;
    private final List<StepValue> trim2;
    private final boolean senseAgreement;
    private final String masterRepresentation;

    public StepTrimmedCurve(int id, String name, StepEntity basisCurve, List<StepValue> trim1, List<StepValue> trim2, boolean senseAgreement, String masterRepresentation) {
        this.id = id;
        this.name = name;
        this.basisCurve = basisCurve;
        this.trim1 = trim1 == null ? null : java.util.List.copyOf(trim1);
        this.trim2 = trim2 == null ? null : java.util.List.copyOf(trim2);
        this.senseAgreement = senseAgreement;
        this.masterRepresentation = masterRepresentation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBasisCurve() {
        return basisCurve;
    }

    public List<StepValue> getTrim1() {
        return trim1;
    }

    public List<StepValue> getTrim2() {
        return trim2;
    }

    public boolean isSenseAgreement() {
        return senseAgreement;
    }

    public String getMasterRepresentation() {
        return masterRepresentation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisCurve() { return getBasisCurve(); }
    public List<StepValue> trim1() { return getTrim1(); }
    public List<StepValue> trim2() { return getTrim2(); }
    public boolean senseAgreement() { return isSenseAgreement(); }
    public String masterRepresentation() { return getMasterRepresentation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTrimmedCurve that = (StepTrimmedCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisCurve, that.basisCurve) && Objects.equals(trim1, that.trim1) && Objects.equals(trim2, that.trim2) && senseAgreement == that.senseAgreement && Objects.equals(masterRepresentation, that.masterRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisCurve, trim1, trim2, senseAgreement, masterRepresentation);
    }

    @Override
    public String toString() {
        return "StepTrimmedCurve{" + "id=" + id + "name=" + name + "basisCurve=" + basisCurve + "trim1=" + trim1 + "trim2=" + trim2 + "senseAgreement=" + senseAgreement + "masterRepresentation=" + masterRepresentation + "}";
    }
}
