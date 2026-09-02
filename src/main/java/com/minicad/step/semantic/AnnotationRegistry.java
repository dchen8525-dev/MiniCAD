package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for annotation entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class AnnotationRegistry {

  private AnnotationRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA
      registry.put(
          "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA", false));

// Entity: MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA
      registry.put(
          "MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA", false));

// Entity: PRESENTATION_AREA
      registry.put(
          "PRESENTATION_AREA",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_AREA", false));

// Entity: PRESENTATION_VIEW
      registry.put(
          "PRESENTATION_VIEW",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_VIEW", false));

// Entity: PRESENTATION_SIZE
      registry.put(
          "PRESENTATION_SIZE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_SIZE", false));

// Entity: DRAUGHTING_MODEL
      registry.put(
          "DRAUGHTING_MODEL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAUGHTING_MODEL", false));

// Entity: CHARACTER_GLYPH_SYMBOL
      registry.put(
          "CHARACTER_GLYPH_SYMBOL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL", false));

// Entity: GENERIC_CHARACTER_GLYPH_SYMBOL
      registry.put(
          "GENERIC_CHARACTER_GLYPH_SYMBOL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "GENERIC_CHARACTER_GLYPH_SYMBOL", false));

// Entity: CHARACTER_GLYPH_SYMBOL_STROKE
      registry.put(
          "CHARACTER_GLYPH_SYMBOL_STROKE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL_STROKE", false));

// Entity: USER_DEFINED_TERMINATOR_SYMBOL
      registry.put(
          "USER_DEFINED_TERMINATOR_SYMBOL",
          (resolver, instance) -> resolver.draughtingResolver.resolveUserDefinedTerminatorSymbol(instance));

// Entity: MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP
      registry.put(
          "MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP"));

// Entity: APPLIED_DESCRIPTION_TEXT_ASSIGNMENT
      registry.put(
          "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT"));

// Entity: APPLIED_DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP"));

// Entity: DESCRIPTION_TEXT_ASSIGNMENT
      registry.put(
          "DESCRIPTION_TEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DESCRIPTION_TEXT_ASSIGNMENT"));

// Entity: DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP"));

// Entity: DIMENSION_CALLOUT_COMPONENT_RELATIONSHIP
      registry.put(
          "DIMENSION_CALLOUT_COMPONENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSION_CALLOUT_COMPONENT_RELATIONSHIP"));

// Entity: DIMENSION_CALLOUT_RELATIONSHIP
      registry.put(
          "DIMENSION_CALLOUT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSION_CALLOUT_RELATIONSHIP"));

// Entity: CHARACTER_GLYPH_FONT_USAGE
      registry.put(
          "CHARACTER_GLYPH_FONT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTER_GLYPH_FONT_USAGE"));

// Entity: HIDDEN_ELEMENT_OVER_RIDING_STYLED_ITEM
      registry.put(
          "HIDDEN_ELEMENT_OVER_RIDING_STYLED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIDDEN_ELEMENT_OVER_RIDING_STYLED_ITEM"));

// Entity: BACKGROUND_COLOUR
      registry.put(
          "BACKGROUND_COLOUR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BACKGROUND_COLOUR"));

// Entity: EXTERNALLY_DEFINED_COLOUR
      registry.put(
          "EXTERNALLY_DEFINED_COLOUR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_COLOUR"));

// Entity: FILL_AREA_STYLE_TILES
      registry.put(
          "FILL_AREA_STYLE_TILES",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FILL_AREA_STYLE_TILES"));

// Entity: FILL_AREA_STYLE_TILE_COLOURED_REGION
      registry.put(
          "FILL_AREA_STYLE_TILE_COLOURED_REGION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FILL_AREA_STYLE_TILE_COLOURED_REGION"));

// Entity: FILL_AREA_STYLE_TILE_SYMBOL_WITH_STYLE
      registry.put(
          "FILL_AREA_STYLE_TILE_SYMBOL_WITH_STYLE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "FILL_AREA_STYLE_TILE_SYMBOL_WITH_STYLE"));

// Entity: STRUCTURED_TEXT_COMPOSITION
      registry.put(
          "STRUCTURED_TEXT_COMPOSITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STRUCTURED_TEXT_COMPOSITION"));

// Entity: TAGGED_TEXT_ITEM
      registry.put(
          "TAGGED_TEXT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TAGGED_TEXT_ITEM"));

// Entity: COMPOSITE_TEXT_WITH_BLANKING_BOX
      registry.put(
          "COMPOSITE_TEXT_WITH_BLANKING_BOX",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_TEXT_WITH_BLANKING_BOX"));

// Entity: COMPOSITE_TEXT_WITH_EXTENT
      registry.put(
          "COMPOSITE_TEXT_WITH_EXTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_TEXT_WITH_EXTENT"));

// Entity: COMPOSITE_TEXT
      registry.put("COMPOSITE_TEXT", StepEntityResolver::resolveCompositeText);

// Entity: TEXT_LITERAL
      registry.put("TEXT_LITERAL", StepEntityResolver::resolveTextLiteral);

// Entity: COMPOSED_TEXT
      registry.put("COMPOSED_TEXT", StepEntityResolver::resolveComposedText);

// Entity: PRESENTATION_LAYER_USAGE
      registry.put("PRESENTATION_LAYER_USAGE", (resolver, instance) -> resolver.materialResolver.resolvePresentationLayerUsage(instance));

// Entity: ANNOTATION_FILL_AREA
      registry.put("ANNOTATION_FILL_AREA", (resolver, instance) -> resolver.annotationResolver.resolveAnnotationFillArea(instance));

// Entity: ANNOTATION_FILL_AREA_OCCURRENCE
      registry.put(
          "ANNOTATION_FILL_AREA_OCCURRENCE",
          (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationFillAreaOccurrence(instance));

// Entity: ANNOTATION_PLACEHOLDER_OCCURRENCE
      registry.put(
          "ANNOTATION_PLACEHOLDER_OCCURRENCE",
          (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationPlaceholderOccurrence(instance));

// Entity: ANNOTATION_SUBFIGURE_OCCURRENCE
      registry.put(
          "ANNOTATION_SUBFIGURE_OCCURRENCE",
          (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationSubfigureOccurrence(instance));

// Entity: DRAUGHTING_ANNOTATION_OCCURRENCE
      registry.put(
          "DRAUGHTING_ANNOTATION_OCCURRENCE",
          (resolver, instance) -> resolver.draughtingResolver.resolveDraughtingAnnotationOccurrence(instance));

// Entity: COLOUR_RGB
      registry.put("COLOUR_RGB", (resolver, instance) -> resolver.materialResolver.resolveColourRgb(instance));

// Entity: PRE_DEFINED_DIMENSION_SYMBOL
      registry.put(
          "PRE_DEFINED_DIMENSION_SYMBOL",
          StepEntityResolver::resolvePreDefinedDimensionSymbol);

// Entity: PRE_DEFINED_TERMINATOR_SYMBOL
      registry.put(
          "PRE_DEFINED_TERMINATOR_SYMBOL",
          (resolver, instance) -> resolver.draughtingResolver.resolvePreDefinedTerminatorSymbol(instance));

// Entity: DRAUGHTING_PRE_DEFINED_TEXT_FONT
      registry.put(
          "DRAUGHTING_PRE_DEFINED_TEXT_FONT",
          (resolver, instance) -> resolver.draughtingResolver.resolveDraughtingPreDefinedTextFont(instance));

// Entity: PRE_DEFINED_TEXT_FONT
      registry.put("PRE_DEFINED_TEXT_FONT", (resolver, instance) -> resolver.materialResolver.resolvePreDefinedTextFont(instance));

// Entity: PRE_DEFINED_SYMBOL
      registry.put("PRE_DEFINED_SYMBOL", StepEntityResolver::resolvePreDefinedSymbol);

// Entity: DRAUGHTING_PRE_DEFINED_COLOUR
      registry.put(
          "DRAUGHTING_PRE_DEFINED_COLOUR", (resolver, instance) -> resolver.draughtingResolver.resolveDraughtingPreDefinedColour(instance));

// Entity: PRE_DEFINED_COLOUR
      registry.put("PRE_DEFINED_COLOUR", (resolver, instance) -> resolver.materialResolver.resolvePreDefinedColour(instance));

// Entity: COLOUR_SPECIFICATION
      registry.put("COLOUR_SPECIFICATION", (resolver, instance) -> resolver.materialResolver.resolveColourSpecification(instance));

// Entity: COLOUR
      registry.put("COLOUR", (resolver, instance) -> resolver.materialResolver.resolveColour(instance));

// Entity: CHARACTER_GLYPH_STYLE_STROKE
      registry.put(
          "CHARACTER_GLYPH_STYLE_STROKE",
          (resolver, instance) -> resolver.draughtingResolver.resolveCharacterGlyphStyleStroke(instance));

// Entity: TEXT_STYLE_FOR_DEFINED_FONT
      registry.put("TEXT_STYLE_FOR_DEFINED_FONT", (resolver, instance) -> resolver.materialResolver.resolveTextStyleForDefinedFont(instance));

// Entity: TEXT_STYLE_WITH_SPACING
      registry.put("TEXT_STYLE_WITH_SPACING", (resolver, instance) -> resolver.materialResolver.resolveTextStyleWithSpacing(instance));

// Entity: TEXT_STYLE_WITH_JUSTIFICATION
      registry.put(
          "TEXT_STYLE_WITH_JUSTIFICATION",
          StepEntityResolver::resolveTextStyleWithJustification);

// Entity: TEXT_STYLE_WITH_MIRROR
      registry.put("TEXT_STYLE_WITH_MIRROR", (resolver, instance) -> resolver.materialResolver.resolveTextStyleWithMirror(instance));

// Entity: TEXT_STYLE_WITH_BOX_CHARACTERISTICS
      registry.put(
          "TEXT_STYLE_WITH_BOX_CHARACTERISTICS",
          StepEntityResolver::resolveTextStyleWithBoxCharacteristics);

// Entity: TEXT_STYLE
      registry.put("TEXT_STYLE", (resolver, instance) -> resolver.materialResolver.resolveTextStyle(instance));

// Entity: SYMBOL_COLOUR
      registry.put("SYMBOL_COLOUR", (resolver, instance) -> resolver.materialResolver.resolveSymbolColour(instance));

// Entity: SYMBOL_STYLE
      registry.put("SYMBOL_STYLE", (resolver, instance) -> resolver.materialResolver.resolveSymbolStyle(instance));

// Entity: FILL_AREA_STYLE_COLOUR
      registry.put("FILL_AREA_STYLE_COLOUR", (resolver, instance) -> resolver.materialResolver.resolveFillAreaStyleColour(instance));

// Entity: FILL_AREA_STYLE
      registry.put("FILL_AREA_STYLE", (resolver, instance) -> resolver.materialResolver.resolveFillAreaStyle(instance));

// Entity: PRESENTATION_STYLE_ASSIGNMENT
      registry.put("PRESENTATION_STYLE_ASSIGNMENT", StepEntityResolver::resolvePresentationStyleAssignment);


// Entity: STYLED_ITEM
      registry.put("STYLED_ITEM", (resolver, instance) -> resolver.materialResolver.resolveStyledItem(instance));

// Entity: OVER_RIDING_STYLED_ITEM
      registry.put("OVER_RIDING_STYLED_ITEM", (resolver, instance) -> resolver.materialResolver.resolveOverRidingStyledItem(instance));

// Entity: PRESENTATION_LAYER_ASSIGNMENT
      registry.put("PRESENTATION_LAYER_ASSIGNMENT", StepEntityResolver::resolvePresentationLayerAssignment);

// Entity: ANNOTATION_TEXT
      registry.put("ANNOTATION_TEXT", (resolver, instance) -> resolver.annotationResolver.resolveAnnotationText(instance));

// Entity: ANNOTATION_TEXT_CHARACTER
      registry.put("ANNOTATION_TEXT_CHARACTER", (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationTextCharacter(instance));

// Entity: ANNOTATION_SYMBOL
      registry.put("ANNOTATION_SYMBOL", (resolver, instance) -> resolver.annotationResolver.resolveAnnotationSymbol(instance));

// Entity: ANNOTATION_SYMBOL_OCCURRENCE
      registry.put("ANNOTATION_SYMBOL_OCCURRENCE", (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationSymbolOccurrence(instance));

// Entity: TERMINATOR_SYMBOL
      registry.put("TERMINATOR_SYMBOL", (resolver, instance) -> resolver.draughtingResolver.resolveTerminatorSymbol(instance));

// Entity: ANNOTATION_OCCURRENCE_RELATIONSHIP
      registry.put(
          "ANNOTATION_OCCURRENCE_RELATIONSHIP",
          StepEntityResolver::resolveAnnotationOccurrenceRelationship);

// Entity: ANNOTATION_OCCURRENCE_ASSOCIATIVITY
      registry.put(
          "ANNOTATION_OCCURRENCE_ASSOCIATIVITY",
          (resolver, instance) ->
              resolver.resolveAnnotationOccurrenceRelationship(instance, "ANNOTATION_OCCURRENCE_ASSOCIATIVITY"));

// Entity: ANNOTATION_TEXT_OCCURRENCE
      registry.put("ANNOTATION_TEXT_OCCURRENCE", (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationTextOccurrence(instance));

// Entity: LEADER_DIRECTED_CALLOUT
      registry.put("LEADER_DIRECTED_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "LEADER_DIRECTED_CALLOUT"));

// Entity: PROJECTION_DIRECTED_CALLOUT
      registry.put(
          "PROJECTION_DIRECTED_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "PROJECTION_DIRECTED_CALLOUT"));

// Entity: DIMENSION_CALLOUT
      registry.put(
          "DIMENSION_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DIMENSION_CALLOUT"));

// Entity: ROUGHNESS_CALLOUT
      registry.put(
          "ROUGHNESS_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "ROUGHNESS_CALLOUT"));

// Entity: STRUCTURED_DIMENSION_CALLOUT
      registry.put(
          "STRUCTURED_DIMENSION_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "STRUCTURED_DIMENSION_CALLOUT"));

// Entity: DRAUGHTING_CALLOUT
      registry.put("DRAUGHTING_CALLOUT", StepEntityResolver::resolveDraughtingCallout);

// Entity: DRAUGHTING_CALLOUT_RELATIONSHIP
      registry.put(
          "DRAUGHTING_CALLOUT_RELATIONSHIP",
          (resolver, instance) -> resolver.annotationResolver.resolveDraughtingCalloutRelationship(instance));

// Entity: DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER
      registry.put(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER",
          (resolver, instance) -> resolver.associationResolver.resolveDraughtingModelItemAssociationWithPlaceholder(instance));

// Entity: DRAUGHTING_MODEL_ITEM_ASSOCIATION
      registry.put(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION",
          (resolver, instance) -> resolver.associationResolver.resolveDraughtingModelItemAssociation(instance));

// Entity: EXTERNALLY_DEFINED_HATCH_STYLE
      registry.put("EXTERNALLY_DEFINED_HATCH_STYLE", (resolver, instance) -> resolver.materialResolver.resolveExternallyDefinedHatchStyle(instance));

// Entity: EXTERNALLY_DEFINED_SYMBOL
      registry.put(
          "EXTERNALLY_DEFINED_SYMBOL",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_SYMBOL"));

// Entity: EXTERNALLY_DEFINED_TEXT_FONT
      registry.put(
          "EXTERNALLY_DEFINED_TEXT_FONT",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TEXT_FONT"));

// Entity: EXTERNALLY_DEFINED_TILE_STYLE
      registry.put("EXTERNALLY_DEFINED_TILE_STYLE", (resolver, instance) -> resolver.materialResolver.resolveExternallyDefinedTileStyle(instance));

// Entity: INSET_CALLOUT
      registry.put(
          "INSET_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "INSET_CALLOUT"));

// Entity: DRAUGHTING_PRE_DEFINED_DIMENSION_SYMBOL
      registry.put(
          "DRAUGHTING_PRE_DEFINED_DIMENSION_SYMBOL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ANNOTATION_FILL_AREA_REGION
      registry.put("ANNOTATION_FILL_AREA_REGION", (resolver, instance) -> resolver.annotationResolver.resolveAnnotationFillAreaRegion(instance));

// Entity: ANNOTATION_RECORD
      registry.put("ANNOTATION_RECORD", (resolver, instance) -> resolver.annotationResolver.resolveAnnotationRecord(instance));

// Entity: TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT
      registry.put(
          "TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT",
          (resolver, instance) -> resolver.annotationResolver.resolveTextLiteralWithDraughtingCallout(instance));

// Entity: COMPOSED_TEXT_LITERAL
      registry.put("COMPOSED_TEXT_LITERAL", StepEntityResolver::resolveComposedTextLiteral);

// Entity: TEXT_FONT
      registry.put("TEXT_FONT", (resolver, instance) -> resolver.materialResolver.resolveTextFont(instance));

// Entity: FILL_AREA_STYLE_TRANSPARENT
      registry.put("FILL_AREA_STYLE_TRANSPARENT", (resolver, instance) -> resolver.materialResolver.resolveFillAreaStyleTransparent(instance));

// Entity: FILL_AREA_STYLE_HATCHING
      registry.put("FILL_AREA_STYLE_HATCHING", (resolver, instance) -> resolver.materialResolver.resolveFillAreaStyleHatching(instance));

// Entity: FILL_AREA_STYLE_TILING
      registry.put("FILL_AREA_STYLE_TILING", (resolver, instance) -> resolver.materialResolver.resolveFillAreaStyleTiling(instance));

// Entity: DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL
      registry.put(
          "DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL",
          (resolver, instance) -> resolver.annotationResolver.resolveDraughtingPreDefinedTerminatorSymbol(instance));

// Entity: APPLIED_AREA
      registry.put(
          "APPLIED_AREA",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "APPLIED_AREA"));

// Entity: EXTERNALLY_DEFINED_MARKER
      registry.put(
          "EXTERNALLY_DEFINED_MARKER",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_MARKER"));

// Entity: EXTERNALLY_DEFINED_CHARACTER_GLYPH
      registry.put(
          "EXTERNALLY_DEFINED_CHARACTER_GLYPH",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_CHARACTER_GLYPH"));

// Entity: EXTERNALLY_DEFINED_DIMENSION_DEFINITION
      registry.put(
          "EXTERNALLY_DEFINED_DIMENSION_DEFINITION",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_DIMENSION_DEFINITION"));

// Entity: EXTERNALLY_DEFINED_TERMINATOR_SYMBOL
      registry.put(
          "EXTERNALLY_DEFINED_TERMINATOR_SYMBOL",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TERMINATOR_SYMBOL"));

// Entity: EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM
      registry.put(
          "EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM"));

// Entity: EXTERNALLY_DEFINED_TEXT_STYLE
      registry.put(
          "EXTERNALLY_DEFINED_TEXT_STYLE",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TEXT_STYLE"));

// Entity: EXTERNALLY_DEFINED_TILE
      registry.put(
          "EXTERNALLY_DEFINED_TILE",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TILE"));

// Entity: EXTERNALLY_DEFINED_STYLE
      registry.put(
          "EXTERNALLY_DEFINED_STYLE",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_STYLE"));

// Entity: ANNOTATION_PLANE (moved from GeometryRegistry for test organization)
      registry.put("ANNOTATION_PLANE", (resolver, instance) -> resolver.annotationResolver.resolveAnnotationPlane(instance));

// Entity: ANNOTATION_CURVE_OCCURRENCE (moved from GeometryRegistry for test organization)
      registry.put("ANNOTATION_CURVE_OCCURRENCE", (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationCurveOccurrence(instance));

// Entity: CURVE_STYLE (moved from GeometryRegistry2 for test organization)
      registry.put("CURVE_STYLE", (resolver, instance) -> resolver.materialResolver.resolveCurveStyle(instance));

// Entity: CURVE_STYLE_FONT (moved from GeometryRegistry2 for test organization)
      registry.put("CURVE_STYLE_FONT", (resolver, instance) -> resolver.materialResolver.resolveCurveStyleFont(instance));

// Entity: CURVE_STYLE_RENDERING (moved from GeometryRegistry2 for test organization)
      registry.put("CURVE_STYLE_RENDERING", (resolver, instance) -> resolver.materialResolver.resolveCurveStyleRendering(instance));

// Entity: PRE_DEFINED_CURVE_FONT (moved from GeometryRegistry2 for test organization)
      registry.put("PRE_DEFINED_CURVE_FONT", (resolver, instance) -> resolver.materialResolver.resolvePreDefinedCurveFont(instance));

// Entity: SURFACE_STYLE_FILL_AREA (moved from GeometryRegistry2 for test organization)
      registry.put("SURFACE_STYLE_FILL_AREA", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleFillArea(instance));

// Entity: SURFACE_SIDE_STYLE (moved from GeometryRegistry2 for test organization)
      registry.put("SURFACE_SIDE_STYLE", (resolver, instance) -> resolver.materialResolver.resolveSurfaceSideStyle(instance));

// Entity: SURFACE_STYLE_USAGE (moved from GeometryRegistry2 for test organization)
      registry.put("SURFACE_STYLE_USAGE", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleUsage(instance));

  }
}
