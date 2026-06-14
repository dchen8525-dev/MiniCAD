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
    private final String transition;
    private final boolean sameSense;
    private final StepEntity parentCurve;

    public StepCompositeCurveSegment(int id, String transition, boolean sameSense, StepEntity parentCurve) {
        this.id = id;
        this.transition = transition;
        this.sameSense = sameSense;
        this.parentCurve = parentCurve;
    }

    public int getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompositeCurveSegment that = (StepCompositeCurveSegment) o;
        return id == that.id && Objects.equals(transition, that.transition) && sameSense == that.sameSense && Objects.equals(parentCurve, that.parentCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transition, sameSense, parentCurve);
    }

    @Override
    public String toString() {
        return "StepCompositeCurveSegment{" + "id=" + id + "transition=" + transition + "sameSense=" + sameSense + "parentCurve=" + parentCurve + "}";
    }
}
