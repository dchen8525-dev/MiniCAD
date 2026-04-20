package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COUNTERSINK_HOLE_DEFINITION.
 * A countersink hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param countersinkDiameter diameter of the countersink
 * @param countersinkAngle angle of the countersink
 */
public record StepCountersinkHoleDefinition(
    int id,
    String name,
    StepEntity throughHoleReference,
    Double countersinkDiameter,
    Double countersinkAngle) implements StepEntity {
}