package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DIE_FEATURE.
 * A die feature entity.
 *
 * @param id STEP instance id
 * @param name die name
 * @param dieType die type classification (stamping, forging, extrusion)
 * @param dieGeometry die geometry representation
 * @param dieSurface die working surface
 * @param dieClearance die clearance specification
 * @param dieMaterial die material specification
 */
public final class StepDieFeature extends AbstractStepEntity {
    private final String dieType;
    private final StepEntity dieGeometry;
    private final StepEntity dieSurface;
    private final double dieClearance;
    private final StepEntity dieMaterial;

    public StepDieFeature(int id, String name, String dieType, StepEntity dieGeometry, StepEntity dieSurface, double dieClearance, StepEntity dieMaterial) {
        super(id, name);
        this.dieType = dieType;
        this.dieGeometry = dieGeometry;
        this.dieSurface = dieSurface;
        this.dieClearance = dieClearance;
        this.dieMaterial = dieMaterial;
    }

    public String getDieType() {
        return dieType;
    }

    public StepEntity getDieGeometry() {
        return dieGeometry;
    }

    public StepEntity getDieSurface() {
        return dieSurface;
    }

    public double getDieClearance() {
        return dieClearance;
    }

    public StepEntity getDieMaterial() {
        return dieMaterial;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("dieType", dieType);
        state.put("dieGeometry", dieGeometry);
        state.put("dieSurface", dieSurface);
        state.put("dieClearance", dieClearance);
        state.put("dieMaterial", dieMaterial);
        return state;
    }
}
