package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DRAWING_REFERENCE.
 * A drawing reference entity.
 *
 * @param id STEP instance id
 * @param name reference name
 * @param drawingId drawing identifier/number
 * @param drawingType drawing type (assembly, detail, schematic)
 * @param drawingRevision drawing revision
 * @param drawingScale drawing scale factor
 * @param drawingStatus drawing status
 * @param drawingAuthor drawing author reference
 */
public final class StepDrawingReference extends AbstractStepEntity {
    private final String drawingId;
    private final String drawingType;
    private final String drawingRevision;
    private final double drawingScale;
    private final String drawingStatus;
    private final StepEntity drawingAuthor;

    public StepDrawingReference(int id, String name, String drawingId, String drawingType, String drawingRevision, double drawingScale, String drawingStatus, StepEntity drawingAuthor) {
        super(id, name);
        this.drawingId = drawingId;
        this.drawingType = drawingType;
        this.drawingRevision = drawingRevision;
        this.drawingScale = drawingScale;
        this.drawingStatus = drawingStatus;
        this.drawingAuthor = drawingAuthor;
    }

    public String getDrawingId() {
        return drawingId;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public String getDrawingRevision() {
        return drawingRevision;
    }

    public double getDrawingScale() {
        return drawingScale;
    }

    public String getDrawingStatus() {
        return drawingStatus;
    }

    public StepEntity getDrawingAuthor() {
        return drawingAuthor;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("drawingId", drawingId);
        state.put("drawingType", drawingType);
        state.put("drawingRevision", drawingRevision);
        state.put("drawingScale", drawingScale);
        state.put("drawingStatus", drawingStatus);
        state.put("drawingAuthor", drawingAuthor);
        return state;
    }
}
