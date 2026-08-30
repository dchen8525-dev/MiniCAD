package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;


/**
 * Draughting resolver - handles annotation occurrence and draughting entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains annotation text/symbol/subfigure/point/curve/fill-area occurrences,
 * terminator symbols, character glyphs, draughting pre-defined items, and marking.
 */
final class DraughtingResolver {

  private final StepEntityResolver resolver;

  DraughtingResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Annotation Occurrence Entities ===

  StepAnnotationCurveOccurrence resolveAnnotationCurveOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_CURVE_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "ANNOTATION_CURVE_OCCURRENCE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepAnnotationCurveOccurrence(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_CURVE_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  StepAnnotationFillAreaOccurrence resolveAnnotationFillAreaOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_FILL_AREA_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity fillStyleTarget = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!resolver.isSupportedAnnotationPointCarrier(fillStyleTarget)) {
      throw new StepResolutionException(
          "ANNOTATION_FILL_AREA_OCCURRENCE fill_style_target must reference supported point carriers or point-like annotation content/occurrences");
    }
    return new StepAnnotationFillAreaOccurrence(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_FILL_AREA_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAnnotationFillArea.class,
            "ANNOTATION_FILL_AREA_OCCURRENCE item must reference ANNOTATION_FILL_AREA"),
        fillStyleTarget);
  }

  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(StepEntityInstance instance) {
    return resolveAnnotationOccurrenceRelationship(instance, "ANNOTATION_OCCURRENCE_RELATIONSHIP");
  }

  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity relating = resolver.resolve(resolver.referenceId(instance, definition, 2));
    StepEntity related = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!StepResolverValueHelpers.isAnnotationOccurrence(relating) || !StepResolverValueHelpers.isAnnotationOccurrence(related)) {
      throw new UnsupportedStepEntityException(
          entityName + " occurrences must reference supported annotation occurrence entities");
    }
    return new StepAnnotationOccurrenceRelationship(
        instance.id(),
        entityName,
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        relating,
        related);
  }

  StepAnnotationPlaceholderOccurrence resolveAnnotationPlaceholderOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_PLACEHOLDER_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    double lineSpacing = resolver.numberValue(instance, definition, 4);
    if (!(lineSpacing > 0.0)) {
      throw new StepResolutionException(
          "ANNOTATION_PLACEHOLDER_OCCURRENCE line_spacing must be positive");
    }
    return new StepAnnotationPlaceholderOccurrence(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_PLACEHOLDER_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolver.requireSupportedPlaceholderItem(resolver.resolve(resolver.referenceId(instance, definition, 2))),
        resolver.enumValue(instance, definition, 3),
        lineSpacing);
  }

  StepAnnotationPointOccurrence resolveAnnotationPointOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_POINT_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationPointCarrier(item)) {
      throw new StepResolutionException(
          "ANNOTATION_POINT_OCCURRENCE item must reference supported point carriers or point-like annotation content/occurrences");
    }
    return new StepAnnotationPointOccurrence(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_POINT_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  StepAnnotationSubfigureOccurrence resolveAnnotationSubfigureOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_SUBFIGURE_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationWrapperItem(item)) {
      throw new StepResolutionException(
          "ANNOTATION_SUBFIGURE_OCCURRENCE item must reference supported annotation content or occurrence");
    }
    return new StepAnnotationSubfigureOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_SUBFIGURE_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  StepAnnotationSymbolOccurrence resolveAnnotationSymbolOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_SYMBOL_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationWrapperItem(item)) {
      throw new StepResolutionException(
          "ANNOTATION_SYMBOL_OCCURRENCE item must reference supported annotation content or occurrence");
    }
    return new StepAnnotationSymbolOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_SYMBOL_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  StepAnnotationTextCharacter resolveAnnotationTextCharacter(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_TEXT_CHARACTER");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_TEXT_CHARACTER mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepAnnotationTextCharacter(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "ANNOTATION_TEXT_CHARACTER mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  StepAnnotationTextOccurrence resolveAnnotationTextOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_TEXT_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity position = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationPointCarrier(position)) {
      throw new StepResolutionException(
          "ANNOTATION_TEXT_OCCURRENCE position must reference supported point carriers or point-like annotation content/occurrences");
    }
    return new StepAnnotationTextOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        position);
  }

  StepDraughtingAnnotationOccurrence resolveDraughtingAnnotationOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DRAUGHTING_ANNOTATION_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepDraughtingAnnotationOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "DRAUGHTING_ANNOTATION_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  // === Terminator Symbol Entities ===

  StepPreDefinedTerminatorSymbol resolvePreDefinedTerminatorSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRE_DEFINED_TERMINATOR_SYMBOL");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepPreDefinedTerminatorSymbol(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepTerminatorSymbol resolveTerminatorSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TERMINATOR_SYMBOL");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity annotatedCurve = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!(annotatedCurve instanceof StepAnnotationCurveOccurrence)
        && !(annotatedCurve instanceof StepLeaderCurve)
        && !(annotatedCurve instanceof StepProjectionCurve)
        && !(annotatedCurve instanceof StepDimensionCurve)) {
      throw new StepResolutionException(
          "TERMINATOR_SYMBOL annotated_curve must reference supported annotation curve occurrence");
    }
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationWrapperItem(item)) {
      throw new StepResolutionException(
          "TERMINATOR_SYMBOL item must reference supported annotation content or occurrence");
    }
    return new StepTerminatorSymbol(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "TERMINATOR_SYMBOL styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item,
        annotatedCurve);
  }

  StepUserDefinedTerminatorSymbol resolveUserDefinedTerminatorSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "USER_DEFINED_TERMINATOR_SYMBOL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "USER_DEFINED_TERMINATOR_SYMBOL mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepUserDefinedTerminatorSymbol(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "USER_DEFINED_TERMINATOR_SYMBOL mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  // === Character Glyph Entities ===

  StepCharacterGlyph resolveCharacterGlyph(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHARACTER_GLYPH");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCharacterGlyph(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepCharacterGlyphOutline resolveCharacterGlyphOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHARACTER_GLYPH_OUTLINE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepCharacterGlyphOutline(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepCharacterGlyphOutlineWithCharacteristics resolveCharacterGlyphOutlineWithCharacteristics(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepCharacterGlyphOutlineWithCharacteristics(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepCharacterGlyphStroke resolveCharacterGlyphStroke(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHARACTER_GLYPH_STROKE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepCharacterGlyphStroke(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepCharacterGlyphStyleOutline resolveCharacterGlyphStyleOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHARACTER_GLYPH_STYLE_OUTLINE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepCharacterGlyphStyleOutline(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "CHARACTER_GLYPH_STYLE_OUTLINE outline_style must reference CURVE_STYLE"));
  }

  StepCharacterGlyphStyleOutlineWithCharacteristics resolveCharacterGlyphStyleOutlineWithCharacteristics(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepCharacterGlyphStyleOutlineWithCharacteristics(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS outline_style must reference CURVE_STYLE"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepFillAreaStyle.class,
            "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS characteristics must reference FILL_AREA_STYLE"));
  }

  StepCharacterGlyphStyleStroke resolveCharacterGlyphStyleStroke(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHARACTER_GLYPH_STYLE_STROKE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepCharacterGlyphStyleStroke(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "CHARACTER_GLYPH_STYLE_STROKE stroke_style must reference CURVE_STYLE"));
  }

  // === Draughting Pre Defined Entities ===

  StepDraughtingPreDefinedColour resolveDraughtingPreDefinedColour(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DRAUGHTING_PRE_DEFINED_COLOUR");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedColour(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepDraughtingPreDefinedCurveFont resolveDraughtingPreDefinedCurveFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DRAUGHTING_PRE_DEFINED_CURVE_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedCurveFont(
        instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepDraughtingPreDefinedTextFont resolveDraughtingPreDefinedTextFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DRAUGHTING_PRE_DEFINED_TEXT_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedTextFont(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepMarking resolveMarking(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MARKING");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepMarking(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.optionalNumberValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }
}
