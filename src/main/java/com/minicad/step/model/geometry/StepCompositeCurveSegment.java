package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved COMPOSITE_CURVE_SEGMENT.
 *
 * @param id STEP id
 * @param transition transition-code enum
 * @param sameSense same-sense flag
 * @param parentCurve parent curve
 */
/**
 * Resolved COMPOSITE_CURVE_SEGMENT.
 *
 * @param id STEP id
 * @param transition transition-code enum
 * @param sameSense same-sense flag
 * @param parentCurve parent curve
 */
public final class StepCompositeCurveSegment implements StepEntity {
    private final int id;
    private final String name;
    private final String transition;
    private final boolean sameSense;
    private final StepEntity parentCurve;

    public StepCompositeCurveSegment(int id, String name, String transition, boolean sameSense, StepEntity parentCurve) {
        this.id = id;
        this.name = name != null ? name : "";
        this.transition = transition;
        this.sameSense = sameSense;
        this.parentCurve = parentCurve;
    }

    public StepCompositeCurveSegment(int id, String transition, boolean sameSense, StepEntity parentCurve) {
        this(id, "", transition, sameSense, parentCurve);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransition() {
        return transition;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    public StepEntity getParentCurve() {
        return parentCurve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String transition() { return getTransition(); }
    public boolean sameSense() { return isSameSense(); }
    public StepEntity parentCurve() { return getParentCurve(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompositeCurveSegment that = (StepCompositeCurveSegment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transition, that.transition) && sameSense == that.sameSense && Objects.equals(parentCurve, that.parentCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transition, sameSense, parentCurve);
    }

    @Override
    public String toString() {
        return "StepCompositeCurveSegment{" + "id=" + id + "name=" + name + "transition=" + transition + "sameSense=" + sameSense + "parentCurve=" + parentCurve + "}";
    }
}
