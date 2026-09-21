package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CHAMFER_DEFINITION.
 * A chamfer definition entity.
 *
 * @param id STEP instance id
 * @param name chamfer name
 * @param edges edges being chamfered
 * @param angle chamfer angle
 * @param width chamfer width
 */
public final class StepChamferDefinition extends AbstractStepEntity {
    private final List<StepEntity> edges;
    private final Double angle;
    private final Double width;

    public StepChamferDefinition(int id, String name, List<StepEntity> edges, Double angle, Double width) {
        super(id, name);
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
        this.angle = angle;
        this.width = width;
    }

    public List<StepEntity> getEdges() {
        return edges;
    }

    public Double getAngle() {
        return angle;
    }

    public Double getWidth() {
        return width;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("edges", edges);
        state.put("angle", angle);
        state.put("width", width);
        return state;
    }
}
