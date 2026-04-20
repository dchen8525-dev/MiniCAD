package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepSheetMetalBend(
    int id,
    String name,
    StepEntity bendLine,
    double bendAngle,
    double bendRadius,
    String bendDirection,
    double bendAllowance,
    double kFactor) implements StepEntity {
}