package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LABEL_FEATURE.
 * A label feature entity.
 *
 * @param id STEP instance id
 * @param name label name
 * @param labelType label type (barcode, QR, RFID, text)
 * @param labelGeometry label geometry representation
 * @param labelPosition label position placement
 * @param labelContent label content text/data
 * @param labelStandard label standard reference
 */
/**
 * Resolved LABEL_FEATURE.
 * A label feature entity.
 *
 * @param id STEP instance id
 * @param name label name
 * @param labelType label type (barcode, QR, RFID, text)
 * @param labelGeometry label geometry representation
 * @param labelPosition label position placement
 * @param labelContent label content text/data
 * @param labelStandard label standard reference
 */
public final class StepLabelFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String labelType;
    private final StepEntity labelGeometry;
    private final StepEntity labelPosition;
    private final String labelContent;
    private final String labelStandard;

    public StepLabelFeature(int id, String name, String labelType, StepEntity labelGeometry, StepEntity labelPosition, String labelContent, String labelStandard) {
        this.id = id;
        this.name = name;
        this.labelType = labelType;
        this.labelGeometry = labelGeometry;
        this.labelPosition = labelPosition;
        this.labelContent = labelContent;
        this.labelStandard = labelStandard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLabelType() {
        return labelType;
    }

    public StepEntity getLabelGeometry() {
        return labelGeometry;
    }

    public StepEntity getLabelPosition() {
        return labelPosition;
    }

    public String getLabelContent() {
        return labelContent;
    }

    public String getLabelStandard() {
        return labelStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLabelFeature that = (StepLabelFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(labelType, that.labelType) && Objects.equals(labelGeometry, that.labelGeometry) && Objects.equals(labelPosition, that.labelPosition) && Objects.equals(labelContent, that.labelContent) && Objects.equals(labelStandard, that.labelStandard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, labelType, labelGeometry, labelPosition, labelContent, labelStandard);
    }

    @Override
    public String toString() {
        return "StepLabelFeature{" + "id=" + id + "name=" + name + "labelType=" + labelType + "labelGeometry=" + labelGeometry + "labelPosition=" + labelPosition + "labelContent=" + labelContent + "labelStandard=" + labelStandard + "}";
    }
}