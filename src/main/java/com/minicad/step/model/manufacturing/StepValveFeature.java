package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved VALVE_FEATURE.
 * A valve feature entity.
 *
 * @param id STEP instance id
 * @param name valve name
 * @param valveType valve type classification (gate, ball, check, globe)
 * @param portDiameter port/flow diameter
 * @param valveBody valve body geometry
 * @valveActuator valve actuator reference
 * @param valveMaterial valve material specification
 * @param flowDirection flow direction specification
 */
public record StepValveFeature(
    int id,
    String name,
    String valveType,
    double portDiameter,
    StepEntity valveBody,
    StepEntity valveActuator,
    StepEntity valveMaterial,
    String flowDirection) implements StepEntity {
}