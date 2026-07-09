package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDrawingReference implements StepEntity {
    private final int id;
    private final String name;
    private final String drawingId;
    private final String drawingType;
    private final String drawingRevision;
    private final double drawingScale;
    private final String drawingStatus;
    private final StepEntity drawingAuthor;

    public StepDrawingReference(int id, String name, String drawingId, String drawingType, String drawingRevision, double drawingScale, String drawingStatus, StepEntity drawingAuthor) {
        this.id = id;
        this.name = name;
        this.drawingId = drawingId;
        this.drawingType = drawingType;
        this.drawingRevision = drawingRevision;
        this.drawingScale = drawingScale;
        this.drawingStatus = drawingStatus;
        this.drawingAuthor = drawingAuthor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDrawingReference that = (StepDrawingReference) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(drawingId, that.drawingId) && Objects.equals(drawingType, that.drawingType) && Objects.equals(drawingRevision, that.drawingRevision) && drawingScale == that.drawingScale && Objects.equals(drawingStatus, that.drawingStatus) && Objects.equals(drawingAuthor, that.drawingAuthor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, drawingId, drawingType, drawingRevision, drawingScale, drawingStatus, drawingAuthor);
    }

    @Override
    public String toString() {
        return "StepDrawingReference{" + "id=" + id + "name=" + name + "drawingId=" + drawingId + "drawingType=" + drawingType + "drawingRevision=" + drawingRevision + "drawingScale=" + drawingScale + "drawingStatus=" + drawingStatus + "drawingAuthor=" + drawingAuthor + "}";
    }
}