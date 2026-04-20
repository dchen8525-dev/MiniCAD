package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TOOLPATH_SPEED_PROFILE.
 * A toolpath speed profile representation entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param speedValues speed values along the toolpath
 * @param feedValues feed values along the toolpath
 * @param positionPoints position points for profile values
 */
public record StepToolpathSpeedProfile(
    int id,
    String name,
    List<Double> speedValues,
    List<Double> feedValues,
    List<StepEntity> positionPoints) implements StepEntity {
}