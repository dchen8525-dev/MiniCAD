package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal RGB colour definition.
 *
 * @param id STEP instance id
 * @param name colour name
 * @param red red channel in [0, 1]
 * @param green green channel in [0, 1]
 * @param blue blue channel in [0, 1]
 */
public final class StepColourRgb extends AbstractStepEntity {
    private final double red;
    private final double green;
    private final double blue;

    public StepColourRgb(int id, String name, double red, double green, double blue) {
        super(id, name);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public double red() {
        return red;
    }

    public double green() {
        return green;
    }

    public double blue() {
        return blue;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("red", red);
        state.put("green", green);
        state.put("blue", blue);
        return state;
    }
}
