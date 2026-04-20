package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepWorkpiece(
    int id,
    String name,
    StepEntity workpieceGeometry,
    StepEntity rawMaterial,
    List<Double> stockDimensions,
    List<StepEntity> features,
    StepEntity setupReference,
    String workpieceType) implements StepEntity {
}