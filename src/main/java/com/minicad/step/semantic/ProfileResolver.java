package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile resolver - handles profile definition entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains arbitrary/parameterized profile definitions, circle and rectangle
 * profiles, hollow profiles, and profile outlines used by swept solids.
 */
final class ProfileResolver {

  private final StepEntityResolver resolver;

  ProfileResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Profile Definition Entities ===

  StepProfileDef resolveArbitraryClosedProfileDef(StepEntityInstance instance) {
    return resolveArbitraryProfileDef(instance, "ARBITRARY_CLOSED_PROFILE_DEF");
  }

  StepProfileDef resolveArbitraryProfileDef(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity curve = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedArbitraryProfileCurve(curve)) {
      throw new StepResolutionException(
          entityName + " outer_curve must reference a curve entity");
    }
    return new StepProfileDef(
        instance.id(),
        resolver.enumValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        null,
        List.of(curve),
        List.of(),
        entityName);
  }

  StepProfileDef resolveArbitraryProfileDefWithVoids(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ARBITRARY_PROFILE_DEF_WITH_VOIDS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity outerCurve = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedArbitraryProfileCurve(outerCurve)) {
      throw new StepResolutionException(
          "ARBITRARY_PROFILE_DEF_WITH_VOIDS outer_curve must reference a curve entity");
    }
    List<StepEntity> innerCurves =
        resolver.entityReferenceList(
            instance,
            definition,
            3,
            "ARBITRARY_PROFILE_DEF_WITH_VOIDS inner_curves must contain curve references");
    for (StepEntity innerCurve : innerCurves) {
      if (!resolver.isSupportedArbitraryProfileCurve(innerCurve)) {
        throw new StepResolutionException(
            "ARBITRARY_PROFILE_DEF_WITH_VOIDS inner_curves must reference curve entities");
      }
    }
    List<StepEntity> curves = new ArrayList<>(1 + innerCurves.size());
    curves.add(outerCurve);
    curves.addAll(innerCurves);
    return new StepProfileDef(
        instance.id(),
        resolver.enumValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        null,
        curves,
        List.of(),
        "ARBITRARY_PROFILE_DEF_WITH_VOIDS");
  }

  StepProfileDef resolveParameterizedProfileDef(StepEntityInstance instance, String entityName, int parameterCount) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, parameterCount + 3);
    List<Double> parameters = new ArrayList<>(parameterCount);
    for (int index = 0; index < parameterCount; index++) {
      parameters.add(resolver.numberValue(instance, definition, index + 3));
    }
    return new StepProfileDef(
        instance.id(),
        resolver.enumValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement2D.class,
            entityName + " position must reference AXIS2_PLACEMENT_2D"),
        List.of(),
        parameters,
        entityName);
  }

  StepProfileDef resolveProfileDef(StepEntityInstance instance) {
    // PROFILE_DEF is an abstract base type. Check for concrete subtypes at the same ID.
    StepEntityDefinition concrete = instance.definitions().stream()
        .filter(d -> !d.name().equals("PROFILE_DEF"))
        .filter(d -> d.name().endsWith("_PROFILE_DEF"))
        .findFirst()
        .orElse(null);
    if (concrete != null) {
      return resolveProfileDefSubtype(instance, concrete);
    }
    throw new UnsupportedStepEntityException("PROFILE_DEF is an abstract base type with no concrete subtype");
  }

  StepProfileDef resolveProfileDefSubtype(StepEntityInstance instance, StepEntityDefinition concrete) {
    String name = concrete.name();
    if ("CIRCLE_PROFILE_DEF".equals(name)) {
      return resolveCircleProfileDef(instance);
    }
    if ("RECTANGLE_PROFILE_DEF".equals(name)) {
      return resolveRectangleProfileDef(instance);
    }
    if ("ARBITRARY_CLOSED_PROFILE_DEF".equals(name)) {
      return resolveArbitraryClosedProfileDef(instance);
    }
    if ("ARBITRARY_PROFILE_DEF".equals(name)) {
      return resolveArbitraryProfileDef(instance, name);
    }
    if ("ARBITRARY_PROFILE_DEF_WITH_VOIDS".equals(name)) {
      return resolveArbitraryProfileDefWithVoids(instance);
    }
    if ("PARAMETERIZED_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, name, 3);
    }
    if ("CENTERED_CIRCLE_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "CENTERED_CIRCLE_PROFILE_DEF", 2);
    }
    if ("CENTRE_LINE_ARC_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "CENTRE_LINE_ARC_PROFILE_DEF", 2);
    }
    if ("ELLIPSE_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "ELLIPSE_PROFILE_DEF", 2);
    }
    if ("L_SHAPE_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "L_SHAPE_PROFILE_DEF", 4);
    }
    if ("U_SHAPE_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "U_SHAPE_PROFILE_DEF", 5);
    }
    if ("Z_SHAPE_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "Z_SHAPE_PROFILE_DEF", 5);
    }
    if ("CHANNEL_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "CHANNEL_PROFILE_DEF", 5);
    }
    if ("T_SHAPE_PROFILE_DEF".equals(name)) {
      return resolveParameterizedProfileDef(instance, "T_SHAPE_PROFILE_DEF", 5);
    }
    throw new UnsupportedStepEntityException("PROFILE_DEF subtype " + name + " is not a StepProfileDef");
  }

  // === Primitive Profile Entities ===

  StepCenteredCircleProfileDef resolveCenteredCircleProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CENTERED_CIRCLE_PROFILE_DEF");
    StepEntityResolver.requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepCenteredCircleProfileDef(
        instance.id(),
        hasName ? resolver.stringValue(instance, definition, 1) : "",
        resolver.requireEntity(
            resolver.referenceId(instance, definition, hasName ? 2 : 1),
            StepAxis2Placement2D.class,
            "CENTERED_CIRCLE_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        resolver.numberValue(instance, definition, hasName ? 3 : 2),
        resolver.numberValue(instance, definition, hasName ? 4 : 3));
  }

  StepCentreLineArcProfileDef resolveCentreLineArcProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CENTRE_LINE_ARC_PROFILE_DEF");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepCentreLineArcProfileDef(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "CENTRE_LINE_ARC_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepProfileDef resolveCircleProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CIRCLE_PROFILE_DEF");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepProfileDef(
        instance.id(),
        resolver.enumValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement2D.class,
            "CIRCLE_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        List.of(),
        List.of(resolver.numberValue(instance, definition, 3)),
        "CIRCLE_PROFILE_DEF");
  }

  StepRectangleHollowProfileDef resolveRectangleHollowProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RECTANGLE_HOLLOW_PROFILE_DEF");
    StepEntityResolver.requireParameterCountIn(instance, definition, 6, 7);
    boolean hasName = definition.parameters().size() == 7;
    return new StepRectangleHollowProfileDef(
        instance.id(),
        hasName ? resolver.stringValue(instance, definition, 1) : "",
        resolver.requireEntity(
            resolver.referenceId(instance, definition, hasName ? 2 : 1),
            StepAxis2Placement2D.class,
            "RECTANGLE_HOLLOW_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        resolver.numberValue(instance, definition, hasName ? 3 : 2),
        resolver.numberValue(instance, definition, hasName ? 4 : 3),
        resolver.numberValue(instance, definition, hasName ? 5 : 4),
        resolver.numberValue(instance, definition, hasName ? 6 : 5));
  }

  StepProfileDef resolveRectangleProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RECTANGLE_PROFILE_DEF");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepProfileDef(
        instance.id(),
        resolver.enumValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement2D.class,
            "RECTANGLE_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        List.of(),
        List.of(resolver.numberValue(instance, definition, 3), resolver.numberValue(instance, definition, 4)),
        "RECTANGLE_PROFILE_DEF");
  }

  // === Profile Outline Entities ===

  StepSweptProfileAreaOutline resolveSweptProfileAreaOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SWEPT_PROFILE_AREA_OUTLINE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepSweptProfileAreaOutline(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }
}
