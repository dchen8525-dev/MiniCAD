package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SHEET_METAL_BEND.
 * A sheet metal bend entity.
 *
 * @param id STEP instance id
 * @param name bend name
 * @param bendLine bend line geometry
 * @param bendAngle bend angle in degrees
 * @param bendRadius bend radius
 * @param bendDirection bend direction (up, down)
 * @param bendAllowance bend allowance factor
 * @param kFactor k-factor for bend calculation
 */
/**
 * Resolved SHEET_METAL_BEND.
 * A sheet metal bend entity.
 *
 * @param id STEP instance id
 * @param name bend name
 * @param bendLine bend line geometry
 * @param bendAngle bend angle in degrees
 * @param bendRadius bend radius
 * @param bendDirection bend direction (up, down)
 * @param bendAllowance bend allowance factor
 * @param kFactor k-factor for bend calculation
 */
public final class StepSheetMetalBend implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity bendLine;
    private final double bendAngle;
    private final double bendRadius;
    private final String bendDirection;
    private final double bendAllowance;
    private final double kFactor;

    public StepSheetMetalBend(int id, String name, StepEntity bendLine, double bendAngle, double bendRadius, String bendDirection, double bendAllowance, double kFactor) {
        this.id = id;
        this.name = name;
        this.bendLine = bendLine;
        this.bendAngle = bendAngle;
        this.bendRadius = bendRadius;
        this.bendDirection = bendDirection;
        this.bendAllowance = bendAllowance;
        this.kFactor = kFactor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBendLine() {
        return bendLine;
    }

    public double getBendAngle() {
        return bendAngle;
    }

    public double getBendRadius() {
        return bendRadius;
    }

    public String getBendDirection() {
        return bendDirection;
    }

    public double getBendAllowance() {
        return bendAllowance;
    }

    public double getKFactor() {
        return kFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSheetMetalBend that = (StepSheetMetalBend) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(bendLine, that.bendLine) && bendAngle == that.bendAngle && bendRadius == that.bendRadius && Objects.equals(bendDirection, that.bendDirection) && bendAllowance == that.bendAllowance && kFactor == that.kFactor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bendLine, bendAngle, bendRadius, bendDirection, bendAllowance, kFactor);
    }

    @Override
    public String toString() {
        return "StepSheetMetalBend{" + "id=" + id + "name=" + name + "bendLine=" + bendLine + "bendAngle=" + bendAngle + "bendRadius=" + bendRadius + "bendDirection=" + bendDirection + "bendAllowance=" + bendAllowance + "kFactor=" + kFactor + "}";
    }
}