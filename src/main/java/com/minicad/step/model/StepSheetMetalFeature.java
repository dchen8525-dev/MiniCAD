package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SHEET_METAL_FEATURE.
 * A sheet metal feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType sheet metal feature type (flange, bend, cutout, hole)
 * @param sheetThickness sheet thickness
 * @param bendRadius bend radius for bends
 * @param bendAngle bend angle for bends
 * @param featureGeometry feature geometry representation
 * @param flatPattern flat pattern geometry reference
 */
public final class StepSheetMetalFeature extends AbstractStepEntity {
    private final String featureType;
    private final double sheetThickness;
    private final double bendRadius;
    private final double bendAngle;
    private final StepEntity featureGeometry;
    private final StepEntity flatPattern;

    public StepSheetMetalFeature(int id, String name, String featureType, double sheetThickness, double bendRadius, double bendAngle, StepEntity featureGeometry, StepEntity flatPattern) {
        super(id, name);
        this.featureType = featureType;
        this.sheetThickness = sheetThickness;
        this.bendRadius = bendRadius;
        this.bendAngle = bendAngle;
        this.featureGeometry = featureGeometry;
        this.flatPattern = flatPattern;
    }

    public String getFeatureType() {
        return featureType;
    }

    public double getSheetThickness() {
        return sheetThickness;
    }

    public double getBendRadius() {
        return bendRadius;
    }

    public double getBendAngle() {
        return bendAngle;
    }

    public StepEntity getFeatureGeometry() {
        return featureGeometry;
    }

    public StepEntity getFlatPattern() {
        return flatPattern;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("featureType", featureType);
        state.put("sheetThickness", sheetThickness);
        state.put("bendRadius", bendRadius);
        state.put("bendAngle", bendAngle);
        state.put("featureGeometry", featureGeometry);
        state.put("flatPattern", flatPattern);
        return state;
    }
}
