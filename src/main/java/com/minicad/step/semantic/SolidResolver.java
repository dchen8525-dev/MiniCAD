package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Solid resolver - handles solid model, CSG, and swept solid entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains manifold/non-manifold solids, CSG primitives and boolean results,
 * half-space solids, and extruded/revolved/swept solids.
 */
final class SolidResolver {

  private final StepEntityResolver resolver;

  SolidResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Solid Model Entities ===

  StepSolidModel resolveSolidModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SOLID_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepSolidModel(instance.id(), resolver.inheritedRepresentationItemName(instance));
  }

  StepSolidReplica resolveSolidReplica(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SOLID_REPLICA");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity parentSolid = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!resolver.isBooleanOperandEntity(parentSolid) && !(parentSolid instanceof StepSolidModel)) {
      throw new StepResolutionException(
          "SOLID_REPLICA parent_solid must reference a supported solid model");
    }
    return new StepSolidReplica(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        parentSolid,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepCartesianTransformationOperator.class,
            "SOLID_REPLICA transformation must reference CARTESIAN_TRANSFORMATION_OPERATOR"));
  }

  // === CSG Entities ===

  StepBooleanResult resolveBooleanResult(StepEntityInstance instance) {
    return resolveBooleanResult(instance, "BOOLEAN_RESULT");
  }

  StepBooleanResult resolveBooleanResult(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity firstOperand = resolver.resolve(resolver.referenceId(instance, definition, 1));
    StepEntity secondOperand = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isBooleanOperandEntity(firstOperand) || !resolver.isBooleanOperandEntity(secondOperand)) {
      throw new StepResolutionException(
          entityName
              + " operands must reference a supported solid or CSG operand");
    }
    return new StepBooleanResult(
        instance.id(),
        resolver.inheritedRepresentationItemName(instance),
        resolver.enumValue(instance, definition, 0),
        firstOperand,
        secondOperand);
  }

  StepHalfSpaceSolid resolveBoxedHalfSpace(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BOXED_HALF_SPACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepHalfSpaceSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireSurfaceReference(instance, definition, 1, "BOXED_HALF_SPACE base_surface"),
        resolver.booleanValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        "BOXED_HALF_SPACE");
  }

  StepCsgPrimitive resolveCsgPrimitive(StepEntityInstance instance,
      String entityName,
      Class<? extends StepEntity> positionType,
      String positionTypeName,
      int dimensionCount) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, dimensionCount + 2);
    StepEntity position = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!positionType.isInstance(position)) {
      throw new StepResolutionException(
          entityName + " position must reference " + positionTypeName);
    }
    List<Double> dimensions = new ArrayList<>(dimensionCount);
    for (int index = 0; index < dimensionCount; index++) {
      dimensions.add(resolver.numberValue(instance, definition, index + 2));
    }
    return new StepCsgPrimitive(
        instance.id(), resolver.stringValue(instance, definition, 0), position, dimensions, entityName);
  }

  StepCsgPrimitive3D resolveCsgPrimitive3D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CSG_PRIMITIVE_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCsgPrimitive3D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepCsgSolid resolveCsgSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CSG_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity treeRootExpression = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!resolver.isBooleanOperandEntity(treeRootExpression)) {
      throw new StepResolutionException(
          "CSG_SOLID tree_root_expression must reference a supported CSG operand");
    }
    return new StepCsgSolid(instance.id(), resolver.stringValue(instance, definition, 0), treeRootExpression);
  }

  StepCsgVolume resolveCsgVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CSG_VOLUME");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCsgVolume(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepHalfSpaceSolid resolveHalfSpaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "HALF_SPACE_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepHalfSpaceSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireSurfaceReference(instance, definition, 1, "HALF_SPACE_SOLID base_surface"),
        resolver.booleanValue(instance, definition, 2),
        null,
        "HALF_SPACE_SOLID");
  }

  // === Swept Solid Entities ===

  StepSweptAreaSolid resolveExtrudedAreaSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXTRUDED_AREA_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepSweptAreaSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepProfileDef.class,
            "EXTRUDED_AREA_SOLID swept_area must reference a profile definition"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "EXTRUDED_AREA_SOLID position must reference AXIS2_PLACEMENT_3D"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepDirection.class,
            "EXTRUDED_AREA_SOLID extruded_direction must reference DIRECTION"),
        resolver.numberValue(instance, definition, 4),
        "EXTRUDED_AREA_SOLID");
  }

  StepExtrudedAreaSolidTapered resolveExtrudedAreaSolidTapered(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXTRUDED_AREA_SOLID_TAPERED");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepExtrudedAreaSolidTapered(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDirection.class,
            "EXTRUDED_AREA_SOLID_TAPERED direction must reference DIRECTION"),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepExtrudedFaceSolid resolveExtrudedFaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXTRUDED_FACE_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepExtrudedFaceSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4));
  }

  StepSweptAreaSolid resolveRevolvedAreaSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REVOLVED_AREA_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepSweptAreaSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepProfileDef.class,
            "REVOLVED_AREA_SOLID swept_area must reference a profile definition"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "REVOLVED_AREA_SOLID position must reference AXIS2_PLACEMENT_3D"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepAxis1Placement.class,
            "REVOLVED_AREA_SOLID axis must reference AXIS1_PLACEMENT"),
        resolver.numberValue(instance, definition, 4),
        "REVOLVED_AREA_SOLID");
  }

  StepRevolvedAreaSolidTapered resolveRevolvedAreaSolidTapered(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REVOLVED_AREA_SOLID_TAPERED");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepRevolvedAreaSolidTapered(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis1Placement.class,
            "REVOLVED_AREA_SOLID_TAPERED axis must reference AXIS1_PLACEMENT"),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepRevolvedFaceSolid resolveRevolvedFaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REVOLVED_FACE_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepRevolvedFaceSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4));
  }

  StepSurfaceCurveSweptAreaSolid resolveSurfaceCurveSweptAreaSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_CURVE_SWEPT_AREA_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepSurfaceCurveSweptAreaSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        resolver.numberValue(instance, definition, 5));
  }

  StepSweptAreaSolid resolveSweptAreaSolid(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    int sweepRefId = resolver.referenceId(instance, definition, 3);
    return new StepSweptAreaSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepProfileDef.class,
            entityName + " swept area must reference a profile definition"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            entityName + " position must reference AXIS2_PLACEMENT_3D"),
        resolver.resolve(sweepRefId),
        resolver.numberValue(instance, definition, 4),
        entityName);
  }

  StepSweptDiskSolid resolveSweptDiskSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SWEPT_DISK_SOLID");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity sweptCurve = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!resolver.isSupportedCurveReference(sweptCurve)) {
      throw new UnsupportedStepEntityException(
          "SWEPT_DISK_SOLID swept_curve must reference a supported curve");
    }
    Double innerRadius = resolver.optionalNumberValue(instance, definition, 3);
    return new StepSweptDiskSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        sweptCurve,
        resolver.numberValue(instance, definition, 2),
        innerRadius);
  }

  StepSweptFaceSolid resolveSweptFaceSolid(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepSweptFaceSolid(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        entityName);
  }
  // === Volume Entities ===

  StepBlockVolume resolveBlockVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BLOCK_VOLUME");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepBlockVolume(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepCylinderVolume resolveCylinderVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CYLINDER_VOLUME");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepCylinderVolume(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CYLINDER_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepPrismVolume resolvePrismVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRISM_VOLUME");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepPrismVolume(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "PRISM_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepRightCircularConeVolume resolveRightCircularConeVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RIGHT_CIRCULAR_CONE_VOLUME");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepRightCircularConeVolume(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "RIGHT_CIRCULAR_CONE_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepSphereVolume resolveSphereVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SPHERE_VOLUME");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepSphereVolume(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "SPHERE_VOLUME center must reference CARTESIAN_POINT"),
        resolver.numberValue(instance, definition, 2));
  }

  StepTorusVolume resolveTorusVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TORUS_VOLUME");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepTorusVolume(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TORUS_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }
}
