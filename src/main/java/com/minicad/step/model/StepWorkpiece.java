package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepWorkpiece extends AbstractStepEntity {
    private final StepEntity workpieceGeometry;
    private final StepEntity rawMaterial;
    private final List<Double> stockDimensions;
    private final List<StepEntity> features;
    private final StepEntity setupReference;
    private final String workpieceType;

    public StepWorkpiece(int id, String name, StepEntity workpieceGeometry, StepEntity rawMaterial, List<Double> stockDimensions, List<StepEntity> features, StepEntity setupReference, String workpieceType) {
        super(id, name);
        this.workpieceGeometry = workpieceGeometry;
        this.rawMaterial = rawMaterial;
        this.stockDimensions = stockDimensions == null ? null : java.util.List.copyOf(stockDimensions);
        this.features = features == null ? null : java.util.List.copyOf(features);
        this.setupReference = setupReference;
        this.workpieceType = workpieceType;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("workpieceGeometry", workpieceGeometry);
        state.put("rawMaterial", rawMaterial);
        state.put("stockDimensions", stockDimensions);
        state.put("features", features);
        state.put("setupReference", setupReference);
        state.put("workpieceType", workpieceType);
        return state;
    }
}
