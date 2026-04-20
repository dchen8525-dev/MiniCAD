package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.profile.StepProfileDef;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
/**
 * Minimal parse-only swept area solid.
 *
 * @param id step id
 * @param name step label
 * @param sweptArea profile definition to sweep
 * @param position solid placement
 * @param sweepReference extrusion direction or revolution axis
 * @param parameter depth or angle in STEP order
 * @param entityName concrete STEP entity name
 */
public record StepSweptAreaSolid(
    int id,
    String name,
    StepProfileDef sweptArea,
    StepAxis2Placement3D position,
    StepEntity sweepReference,
    double parameter,
    String entityName)
    implements StepEntity {}
