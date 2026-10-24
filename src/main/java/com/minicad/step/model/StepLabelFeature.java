package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepLabelFeature extends AbstractStepEntity {
    private final String labelType;
    private final StepEntity labelGeometry;
    private final StepEntity labelPosition;
    private final String labelContent;
    private final String labelStandard;

    public StepLabelFeature(int id, String name, String labelType, StepEntity labelGeometry, StepEntity labelPosition, String labelContent, String labelStandard) {
        super(id, name);
        this.labelType = labelType;
        this.labelGeometry = labelGeometry;
        this.labelPosition = labelPosition;
        this.labelContent = labelContent;
        this.labelStandard = labelStandard;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "labelType", labelType, "labelGeometry", labelGeometry,
                "labelPosition", labelPosition, "labelContent", labelContent, "labelStandard", labelStandard);
    }
}
