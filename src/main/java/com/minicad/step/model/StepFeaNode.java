package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved NODE.
 * A finite element analysis node (grid point).
 */
public final class StepFeaNode extends AbstractStepEntity {
    private final double x;
    private final double y;
    private final double z;

    public StepFeaNode(int id, String name, double x, double y, double z) {
        super(id, name);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("x", x);
        state.put("y", y);
        state.put("z", z);
        return state;
    }
}
