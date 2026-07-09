package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFreeFormSurface implements StepEntity {
    private final int id;
    private final String name;
    private final String surfaceType;
    private final List<List<StepEntity>> controlPoints;
    private final int degreeU;
    private final int degreeV;
    private final List<Double> knotVectors;
    private final List<Double> weights;

    public StepFreeFormSurface(int id, String name, String surfaceType, List<List<StepEntity>> controlPoints, int degreeU, int degreeV, List<Double> knotVectors, List<Double> weights) {
        this.id = id;
        this.name = name;
        this.surfaceType = surfaceType;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.degreeU = degreeU;
        this.degreeV = degreeV;
        this.knotVectors = knotVectors == null ? null : java.util.List.copyOf(knotVectors);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFreeFormSurface that = (StepFreeFormSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(surfaceType, that.surfaceType) && Objects.equals(controlPoints, that.controlPoints) && degreeU == that.degreeU && degreeV == that.degreeV && Objects.equals(knotVectors, that.knotVectors) && Objects.equals(weights, that.weights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surfaceType, controlPoints, degreeU, degreeV, knotVectors, weights);
    }

    @Override
    public String toString() {
        return "StepFreeFormSurface{" + "id=" + id + "name=" + name + "surfaceType=" + surfaceType + "controlPoints=" + controlPoints + "degreeU=" + degreeU + "degreeV=" + degreeV + "knotVectors=" + knotVectors + "weights=" + weights + "}";
    }
}