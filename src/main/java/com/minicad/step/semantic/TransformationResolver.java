package com.minicad.step.semantic;

import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;


/**
 * Transformation resolver - handles transformation operator entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains cartesian transformation operators (2D/3D), item-defined
 * transformations, and representation relationships with transformations.
 */
final class TransformationResolver {

  private final StepEntityResolver resolver;

  TransformationResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Cartesian Transformation Entities ===

  StepCartesianTransformationOperator resolveCartesianTransformationOperator(StepEntityInstance instance) {
    // Base type: check for concrete 2D/3D subtype at the same ID
    StepEntityDefinition concrete = instance.definitions().stream()
        .filter(d -> !d.name().equals("CARTESIAN_TRANSFORMATION_OPERATOR"))
        .filter(d -> d.name().startsWith("CARTESIAN_TRANSFORMATION_OPERATOR"))
        .findFirst()
        .orElse(null);
    if (concrete != null) {
      return resolveCartesianTransformationOperator(instance, concrete.name());
    }
    throw new UnsupportedStepEntityException("CARTESIAN_TRANSFORMATION_OPERATOR is an abstract base type with no concrete subtype");
  }

  StepCartesianTransformationOperator resolveCartesianTransformationOperator(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    // 2D has 4-5 params, 3D has 6-7 params
    int paramCount = definition.parameters().size();
    return new StepCartesianTransformationOperator(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        paramCount >= 4 ? resolver.optionalResolveDirection(resolver.referenceId(instance, definition, 1)) : null,
        paramCount >= 5 ? resolver.optionalResolveDirection(resolver.referenceId(instance, definition, 2)) : null,
        resolver.optionalResolveCartesianPoint(resolver.referenceId(instance, definition, paramCount >= 7 ? 3 : paramCount == 6 ? 2 : 2)),
        paramCount >= 6 ? resolver.optionalNumberValue(instance, definition, paramCount >= 7 ? 4 : 3) : null,
        paramCount >= 7 ? resolver.optionalResolveDirection(resolver.referenceId(instance, definition, 5)) : null,
        entityName);
  }

  StepCartesianTransformationOperator resolveCartesianTransformationOperator2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepCartesianTransformationOperator(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalDirectionReference(
            instance,
            definition,
            1,
            "CARTESIAN_TRANSFORMATION_OPERATOR_2D axis1 must reference DIRECTION"),
        resolver.optionalDirectionReference(
            instance,
            definition,
            2,
            "CARTESIAN_TRANSFORMATION_OPERATOR_2D axis2 must reference DIRECTION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepCartesianPoint.class,
            "CARTESIAN_TRANSFORMATION_OPERATOR_2D local_origin must reference CARTESIAN_POINT"),
        resolver.optionalNumberValue(instance, definition, 4),
        null,
        "CARTESIAN_TRANSFORMATION_OPERATOR_2D");
  }

  StepCartesianTransformationOperator resolveCartesianTransformationOperator3D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepCartesianTransformationOperator(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalDirectionReference(
            instance,
            definition,
            1,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D axis1 must reference DIRECTION"),
        resolver.optionalDirectionReference(
            instance,
            definition,
            2,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D axis2 must reference DIRECTION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepCartesianPoint.class,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D local_origin must reference CARTESIAN_POINT"),
        resolver.optionalNumberValue(instance, definition, 4),
        resolver.optionalDirectionReference(
            instance,
            definition,
            5,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D axis3 must reference DIRECTION"),
        "CARTESIAN_TRANSFORMATION_OPERATOR_3D");
  }

  // === Item Defined Transformation Entities ===

  StepItemDefinedTransformation resolveItemDefinedTransformation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ITEM_DEFINED_TRANSFORMATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepItemDefinedTransformation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "ITEM_DEFINED_TRANSFORMATION transform_item_1 must reference AXIS2_PLACEMENT_3D"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepAxis2Placement3D.class,
            "ITEM_DEFINED_TRANSFORMATION transform_item_2 must reference AXIS2_PLACEMENT_3D"));
  }

  StepRepresentationRelationshipWithTransformation
      resolveRepresentationRelationshipWithTransformation(StepEntityInstance instance) {
    StepEntityDefinition relationship = resolver.definition(instance, "REPRESENTATION_RELATIONSHIP");
    StepEntityDefinition transformation =
        resolver.definition(instance, "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION");
    StepEntityResolver.requireParameterCount(instance, relationship, 4);
    StepEntityResolver.requireParameterCount(instance, transformation, 1);
    return new StepRepresentationRelationshipWithTransformation(
        instance.id(),
        resolver.stringValue(instance, relationship, 0),
        resolver.stringValue(instance, relationship, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, relationship, 2),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, relationship, 3),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, transformation, 0),
            StepItemDefinedTransformation.class,
            "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION transformation_operator must reference ITEM_DEFINED_TRANSFORMATION"));
  }
}
