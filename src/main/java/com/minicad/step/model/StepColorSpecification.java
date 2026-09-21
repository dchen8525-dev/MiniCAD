package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved COLOR_SPECIFICATION.
 * A color specification with RGB or named values.
 *
 * @param id STEP instance id
 * @param name color name
 * @param red red component (0-1)
 * @param green green component (0-1)
 * @param blue blue component (0-1)
 */
public final class StepColorSpecification extends AbstractStepEntity {
    private final double red;
    private final double green;
    private final double blue;

    public StepColorSpecification(int id, String name, double red, double green, double blue) {
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
