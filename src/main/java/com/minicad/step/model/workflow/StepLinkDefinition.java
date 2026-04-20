package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LINK_DEFINITION.
 * A link definition entity.
 *
 * @param id STEP instance id
 * @param name link name
 * @param linkType link variance type
 * @param linkSource link variance source reference
 * @param linkTarget link variance target reference
 * @param linkBandwidth link variance bandwidth
 * @param linkStatus link variance status
 */
public record StepLinkDefinition(
    int id,
    String name,
    String linkType,
    StepEntity linkSource,
    StepEntity linkTarget,
    double linkBandwidth,
    String linkStatus) implements StepEntity {
}