package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ELLIPSE_2D.
 * An ellipse in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param semiAxis1 semi-major axis length
 * @param semiAxis2 semi-minor axis length
 */
public final class StepEllipse2D extends AbstractStepEntity {
    private final StepAxis2Placement2D position;
    private final double semiAxis1;
    private final double semiAxis2;

    public StepEllipse2D(int id, String name, StepAxis2Placement2D position, double semiAxis1, double semiAxis2) {
        super(id, name);
        this.position = position;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getSemiAxis1() {
        return semiAxis1;
    }

    public double getSemiAxis2() {
        return semiAxis2;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement2D position() { return getPosition(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("semiAxis1", semiAxis1);
        state.put("semiAxis2", semiAxis2);
        return state;
    }
}
