package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved WORKPIECE.
 * A workpiece entity.
 *
 * @param id STEP instance id
 * @param name workpiece name
 * @param workpieceGeometry workpiece geometry representation
 * @param rawMaterial raw material specification
 * @param stockDimensions stock dimensions (raw stock size)
 * @param features machining features on workpiece
 * @param setupReference setup reference coordinate system
 * @param workpieceType workpiece type (raw, in-process, finished)
 */
/**
 * Resolved WORKPIECE.
 * A workpiece entity.
 *
 * @param id STEP instance id
 * @param name workpiece name
 * @param workpieceGeometry workpiece geometry representation
 * @param rawMaterial raw material specification
 * @param stockDimensions stock dimensions (raw stock size)
 * @param features machining features on workpiece
 * @param setupReference setup reference coordinate system
 * @param workpieceType workpiece type (raw, in-process, finished)
 */
public final class StepWorkpiece implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity workpieceGeometry;
    private final StepEntity rawMaterial;
    private final List<Double> stockDimensions;
    private final List<StepEntity> features;
    private final StepEntity setupReference;
    private final String workpieceType;

    public StepWorkpiece(int id, String name, StepEntity workpieceGeometry, StepEntity rawMaterial, List<Double> stockDimensions, List<StepEntity> features, StepEntity setupReference, String workpieceType) {
        this.id = id;
        this.name = name;
        this.workpieceGeometry = workpieceGeometry;
        this.rawMaterial = rawMaterial;
        this.stockDimensions = stockDimensions == null ? null : java.util.List.copyOf(stockDimensions);
        this.features = features == null ? null : java.util.List.copyOf(features);
        this.setupReference = setupReference;
        this.workpieceType = workpieceType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getWorkpieceGeometry() {
        return workpieceGeometry;
    }

    public StepEntity getRawMaterial() {
        return rawMaterial;
    }

    public List<Double> getStockDimensions() {
        return stockDimensions;
    }

    public List<StepEntity> getFeatures() {
        return features;
    }

    public StepEntity getSetupReference() {
        return setupReference;
    }

    public String getWorkpieceType() {
        return workpieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWorkpiece that = (StepWorkpiece) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(workpieceGeometry, that.workpieceGeometry) && Objects.equals(rawMaterial, that.rawMaterial) && Objects.equals(stockDimensions, that.stockDimensions) && Objects.equals(features, that.features) && Objects.equals(setupReference, that.setupReference) && Objects.equals(workpieceType, that.workpieceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, workpieceGeometry, rawMaterial, stockDimensions, features, setupReference, workpieceType);
    }

    @Override
    public String toString() {
        return "StepWorkpiece{" + "id=" + id + "name=" + name + "workpieceGeometry=" + workpieceGeometry + "rawMaterial=" + rawMaterial + "stockDimensions=" + stockDimensions + "features=" + features + "setupReference=" + setupReference + "workpieceType=" + workpieceType + "}";
    }
}