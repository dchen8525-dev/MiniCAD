package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MOLD_FEATURE.
 * A mold feature entity.
 *
 * @param id STEP instance id
 * @param name mold name
 * @param moldType mold type classification (injection, compression, blow)
 * @param cavityGeometry cavity geometry representation
 * @param coreGeometry core geometry representation
 * @param partingLine parting line geometry
 * @param gatingSystem gating system features
 * @param coolingChannels cooling channel features
 */
public record StepMoldFeature(
    int id,
    String name,
    String moldType,
    StepEntity cavityGeometry,
    StepEntity coreGeometry,
    StepEntity partingLine,
    List<StepEntity> gatingSystem,
    List<StepEntity> coolingChannels) implements StepEntity {
}