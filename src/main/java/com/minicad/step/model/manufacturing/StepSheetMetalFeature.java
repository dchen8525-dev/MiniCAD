package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSheetMetalFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String featureType;
    private final double sheetThickness;
    private final double bendRadius;
    private final double bendAngle;
    private final StepEntity featureGeometry;
    private final StepEntity flatPattern;

    public StepSheetMetalFeature(int id, String name, String featureType, double sheetThickness, double bendRadius, double bendAngle, StepEntity featureGeometry, StepEntity flatPattern) {
        this.id = id;
        this.name = name;
        this.featureType = featureType;
        this.sheetThickness = sheetThickness;
        this.bendRadius = bendRadius;
        this.bendAngle = bendAngle;
        this.featureGeometry = featureGeometry;
        this.flatPattern = flatPattern;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSheetMetalFeature that = (StepSheetMetalFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(featureType, that.featureType) && sheetThickness == that.sheetThickness && bendRadius == that.bendRadius && bendAngle == that.bendAngle && Objects.equals(featureGeometry, that.featureGeometry) && Objects.equals(flatPattern, that.flatPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, featureType, sheetThickness, bendRadius, bendAngle, featureGeometry, flatPattern);
    }

    @Override
    public String toString() {
        return "StepSheetMetalFeature{" + "id=" + id + "name=" + name + "featureType=" + featureType + "sheetThickness=" + sheetThickness + "bendRadius=" + bendRadius + "bendAngle=" + bendAngle + "featureGeometry=" + featureGeometry + "flatPattern=" + flatPattern + "}";
    }
}