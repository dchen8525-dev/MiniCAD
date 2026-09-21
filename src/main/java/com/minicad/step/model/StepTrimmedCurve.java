package com.minicad.step.model;

import com.minicad.step.syntax.StepValue;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTrimmedCurve extends AbstractStepEntity {
    private final StepEntity basisCurve;
    private final List<StepValue> trim1;
    private final List<StepValue> trim2;
    private final boolean senseAgreement;
    private final String masterRepresentation;

    public StepTrimmedCurve(int id, String name, StepEntity basisCurve, List<StepValue> trim1, List<StepValue> trim2, boolean senseAgreement, String masterRepresentation) {
        super(id, name);
        this.basisCurve = basisCurve;
        this.trim1 = trim1 == null ? null : java.util.List.copyOf(trim1);
        this.trim2 = trim2 == null ? null : java.util.List.copyOf(trim2);
        this.senseAgreement = senseAgreement;
        this.masterRepresentation = masterRepresentation;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisCurve", basisCurve);
        state.put("trim1", trim1);
        state.put("trim2", trim2);
        state.put("senseAgreement", senseAgreement);
        state.put("masterRepresentation", masterRepresentation);
        return state;
    }
}
