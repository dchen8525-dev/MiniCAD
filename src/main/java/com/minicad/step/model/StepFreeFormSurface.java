package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FREE_FORM_SURFACE.
 * A free-form surface entity.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param surfaceType free-form surface type classification
 * @param controlPoints control points matrix
 * @param degreeU degree in U direction
 * @param degreeV degree in V direction
 * @param knotVectors knot vectors for U and V
 * @param weights weight values for rational surfaces
 */
public final class StepFreeFormSurface extends AbstractStepEntity {
    private final String surfaceType;
    private final List<List<StepEntity>> controlPoints;
    private final int degreeU;
    private final int degreeV;
    private final List<Double> knotVectors;
    private final List<Double> weights;

    public StepFreeFormSurface(int id, String name, String surfaceType, List<List<StepEntity>> controlPoints, int degreeU, int degreeV, List<Double> knotVectors, List<Double> weights) {
        super(id, name);
        this.surfaceType = surfaceType;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.degreeU = degreeU;
        this.degreeV = degreeV;
        this.knotVectors = knotVectors == null ? null : java.util.List.copyOf(knotVectors);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
    }

    public String getSurfaceType() {
        return surfaceType;
    }

    public List<List<StepEntity>> getControlPoints() {
        return controlPoints;
    }

    public int getDegreeU() {
        return degreeU;
    }

    public int getDegreeV() {
        return degreeV;
    }

    public List<Double> getKnotVectors() {
        return knotVectors;
    }

    public List<Double> getWeights() {
        return weights;
    }

    // Record-style accessors
    public int degreeU() { return getDegreeU(); }
    public int degreeV() { return getDegreeV(); }
    public List<List<StepEntity>> controlPoints() { return getControlPoints(); }
    public List<Double> knotVectors() { return getKnotVectors(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("surfaceType", surfaceType);
        state.put("controlPoints", controlPoints);
        state.put("degreeU", degreeU);
        state.put("degreeV", degreeV);
        state.put("knotVectors", knotVectors);
        state.put("weights", weights);
        return state;
    }
}
