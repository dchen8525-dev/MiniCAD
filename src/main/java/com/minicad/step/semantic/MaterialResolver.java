package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Material resolver - handles material, colour, and presentation style entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains materials, colours, curve/text/point styles, surface styles, fill area
 * styles, symbol styles, and presentation layer entities.
 */
final class MaterialResolver {

  private final StepEntityResolver resolver;

  MaterialResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Material Entities ===

  StepBillOfMaterials resolveBillOfMaterials(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BILL_OF_MATERIALS");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepBillOfMaterials(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.entityReferenceList(instance, definition, 2,
            "BILL_OF_MATERIALS items must contain entity references"),
        StepResolverValueHelpers.intList(instance, definition, 3),
        resolver.stringValue(instance, definition, 4),
        (int) resolver.numberValue(instance, definition, 5),
        resolver.stringValue(instance, definition, 6));
  }

  StepFeaLinearMaterial resolveFeaLinearMaterial(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_LINEAR_MATERIAL");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaLinearMaterial(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepFeaNonLinearMaterial resolveFeaNonLinearMaterial(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_NON_LINEAR_MATERIAL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepFeaNonLinearMaterial(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2));
  }

  StepMaterial resolveMaterial(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MATERIAL");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepMaterial(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepMaterialDesignation resolveMaterialDesignation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MATERIAL_DESIGNATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> defs =
        resolver.entityReferenceList(
            instance, definition, 1,
            "MATERIAL_DESIGNATION definitions must contain entity references");
    return new StepMaterialDesignation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        defs);
  }

  // === Colour Entities ===

  StepColorSpecification resolveColorSpecification(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COLOR_SPECIFICATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepColorSpecification(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepColour resolveColour(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COLOUR");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepColour(instance.id());
  }

  StepColourRgb resolveColourRgb(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COLOUR_RGB");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepColourRgb(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepColourSpecification resolveColourSpecification(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COLOUR_SPECIFICATION");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepColourSpecification(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepFillAreaStyleColour resolveFillAreaStyleColour(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_STYLE_COLOUR");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity colour = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "FILL_AREA_STYLE_COLOUR colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepFillAreaStyleColour(
        instance.id(), resolver.optionalStringValue(instance, definition, 0), colour);
  }

  StepPreDefinedColour resolvePreDefinedColour(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRE_DEFINED_COLOUR");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepPreDefinedColour(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepSymbolColour resolveSymbolColour(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SYMBOL_COLOUR");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    StepEntity colour = resolver.resolve(resolver.referenceId(instance, definition, 0));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "SYMBOL_COLOUR colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepSymbolColour(instance.id(), colour);
  }

  // === Curve / Text / Point Style Entities ===

  StepCurveStyle resolveCurveStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVE_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity font = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(font instanceof StepDraughtingPreDefinedCurveFont)
        && !(font instanceof StepPreDefinedCurveFont)
        && !(font instanceof StepUserDefinedCurveFont)) {
      throw new UnsupportedStepEntityException(
          "CURVE_STYLE font must reference PRE_DEFINED_CURVE_FONT, DRAUGHTING_PRE_DEFINED_CURVE_FONT or USER_DEFINED_CURVE_FONT");
    }
    StepEntity colour = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "CURVE_STYLE colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepCurveStyle(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        font,
        resolver.numberValue(instance, definition, 2),
        colour);
  }

  StepCurveStyleFont resolveCurveStyleFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVE_STYLE_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCurveStyleFont(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepCurveStyleRendering resolveCurveStyleRendering(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVE_STYLE_RENDERING");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepCurveStyleRendering(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepCurveStyleWithFont resolveCurveStyleWithFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVE_STYLE_WITH_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepCurveStyleWithFont(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2));
  }

  StepPointStyle resolvePointStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "POINT_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity marker = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(marker instanceof StepPreDefinedPointMarkerSymbol)
        && !(marker instanceof StepPreDefinedMarker)
        && !(marker instanceof StepUserDefinedMarker)) {
      throw new UnsupportedStepEntityException(
          "POINT_STYLE marker must reference PRE_DEFINED_POINT_MARKER_SYMBOL, PRE_DEFINED_MARKER or USER_DEFINED_MARKER");
    }
    StepEntity colour = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "POINT_STYLE colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepPointStyle(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        marker,
        resolver.numberValue(instance, definition, 2),
        colour);
  }

  StepPreDefinedCurveFont resolvePreDefinedCurveFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRE_DEFINED_CURVE_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepPreDefinedCurveFont(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepPreDefinedMarker resolvePreDefinedMarker(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRE_DEFINED_MARKER");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepPreDefinedMarker(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepPreDefinedTextFont resolvePreDefinedTextFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRE_DEFINED_TEXT_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepPreDefinedTextFont(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepTextFont resolveTextFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TEXT_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepTextFont(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepTextStyle resolveTextStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TEXT_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity characterAppearance = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    return new StepTextStyle(
        instance.id(), resolver.stringValue(instance, definition, 0), characterAppearance);
  }

  StepTextStyleForDefinedFont resolveTextStyleForDefinedFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TEXT_STYLE_FOR_DEFINED_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    StepEntity colour = resolver.resolve(resolver.referenceId(instance, definition, 0));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_FOR_DEFINED_FONT colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepTextStyleForDefinedFont(instance.id(), colour);
  }

  StepTextStyleWithMirror resolveTextStyleWithMirror(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TEXT_STYLE_WITH_MIRROR");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity characterAppearance = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_MIRROR character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    StepEntity mirrorPlacement = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(mirrorPlacement instanceof StepAxis2Placement2D)
        && !(mirrorPlacement instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_MIRROR mirror_placement must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepTextStyleWithMirror(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        characterAppearance,
        mirrorPlacement);
  }

  StepTextStyleWithSpacing resolveTextStyleWithSpacing(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TEXT_STYLE_WITH_SPACING");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity characterAppearance = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_SPACING character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    return new StepTextStyleWithSpacing(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        characterAppearance,
        resolver.numberValue(instance, definition, 2));
  }

  StepUserDefinedCurveFont resolveUserDefinedCurveFont(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "USER_DEFINED_CURVE_FONT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "USER_DEFINED_CURVE_FONT mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepUserDefinedCurveFont(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "USER_DEFINED_CURVE_FONT mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  StepUserDefinedMarker resolveUserDefinedMarker(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "USER_DEFINED_MARKER");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "USER_DEFINED_MARKER mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepUserDefinedMarker(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "USER_DEFINED_MARKER mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  // === Surface Style Entities ===

  StepPreDefinedSurfaceStyle resolvePreDefinedSurfaceStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRE_DEFINED_SURFACE_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepPreDefinedSurfaceStyle(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepRenderingProperties resolveRenderingProperties(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RENDERING_PROPERTIES");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepRenderingProperties(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2));
  }

  StepSurfaceSideStyle resolveSurfaceSideStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_SIDE_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepSurfaceSideStyle(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.entityReferenceList(
                instance,
                definition,
                1,
                "SURFACE_SIDE_STYLE styles must contain SURFACE_STYLE_FILL_AREA, SURFACE_STYLE_BOUNDARY, SURFACE_STYLE_CONTROL_GRID, SURFACE_STYLE_SEGMENTATION_CURVE, SURFACE_STYLE_SILHOUETTE, SURFACE_STYLE_TRANSPARENT, SURFACE_STYLE_REFLECTANCE_AMBIENT, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR or SURFACE_STYLE_PARAMETER_LINE references")
                .stream()
                .map(
                        style -> {
                            if (!(style instanceof StepSurfaceStyleFillArea)
                                    && !(style instanceof StepSurfaceStyleBoundary)
                                    && !(style instanceof StepSurfaceStyleControlGrid)
                                    && !(style instanceof StepSurfaceStyleSegmentationCurve)
                                    && !(style instanceof StepSurfaceStyleSilhouette)
                                    && !(style instanceof StepSurfaceStyleTransparent)
                                    && !(style instanceof StepSurfaceStyleReflectanceAmbient)
                                    && !(style instanceof StepSurfaceStyleReflectanceAmbientDiffuse)
                                    && !(style instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular)
                                    && !(style instanceof StepSurfaceStyleRendering)
                                    && !(style instanceof StepSurfaceStyleParameterLine)) {
                                throw new StepResolutionException(
                                        "SURFACE_SIDE_STYLE styles must reference SURFACE_STYLE_FILL_AREA, SURFACE_STYLE_BOUNDARY, SURFACE_STYLE_CONTROL_GRID, SURFACE_STYLE_SEGMENTATION_CURVE, SURFACE_STYLE_SILHOUETTE, SURFACE_STYLE_TRANSPARENT, SURFACE_STYLE_REFLECTANCE_AMBIENT, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR, SURFACE_STYLE_RENDERING or SURFACE_STYLE_PARAMETER_LINE");
                            }
                            return style;
                        })
                .collect(Collectors.toList()));
  }

  StepSurfaceStyleBoundary resolveSurfaceStyleBoundary(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_BOUNDARY");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleBoundary(
        instance.id(), resolver.requireCurveStyleReference(instance, definition, "SURFACE_STYLE_BOUNDARY"));
  }

  StepSurfaceStyleControlGrid resolveSurfaceStyleControlGrid(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_CONTROL_GRID");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleControlGrid(
        instance.id(),
        resolver.requireCurveStyleReference(instance, definition, "SURFACE_STYLE_CONTROL_GRID"));
  }

  StepSurfaceStyleFillArea resolveSurfaceStyleFillArea(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_FILL_AREA");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleFillArea(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepFillAreaStyle.class,
            "SURFACE_STYLE_FILL_AREA fill style must reference FILL_AREA_STYLE"));
  }

  StepSurfaceStyleParameterLines resolveSurfaceStyleParameterLines(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_PARAMETER_LINES");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepSurfaceStyleParameterLines(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepSurfaceStyleRendering resolveSurfaceStyleRendering(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_RENDERING");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepSurfaceStyleRendering(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4));
  }

  StepSurfaceStyleSilhouette resolveSurfaceStyleSilhouette(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_SILHOUETTE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleSilhouette(
        instance.id(), resolver.requireCurveStyleReference(instance, definition, "SURFACE_STYLE_SILHOUETTE"));
  }

  StepSurfaceStyleTransparent resolveSurfaceStyleTransparent(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_TRANSPARENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleTransparent(instance.id(), resolver.numberValue(instance, definition, 0));
  }

  StepSurfaceStyleUsage resolveSurfaceStyleUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepSurfaceStyleUsage(
        instance.id(),
        resolver.enumValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepSurfaceSideStyle.class,
            "SURFACE_STYLE_USAGE style must reference SURFACE_SIDE_STYLE"));
  }

  // === Fill Area Style Entities ===

  StepExternallyDefinedHatchStyle resolveExternallyDefinedHatchStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXTERNALLY_DEFINED_HATCH_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepExternallyDefinedHatchStyle(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepExternallyDefinedTileStyle resolveExternallyDefinedTileStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXTERNALLY_DEFINED_TILE_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepExternallyDefinedTileStyle(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepFillAreaShapeUse resolveFillAreaShapeUse(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_SHAPE_USE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepFillAreaShapeUse(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepFillAreaStyle resolveFillAreaStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepFillAreaStyle(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFillAreaStyleColour.class,
            "FILL_AREA_STYLE styles must contain FILL_AREA_STYLE_COLOUR references"));
  }

  StepFillAreaStyleHatching resolveFillAreaStyleHatching(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_STYLE_HATCHING");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFillAreaStyleHatching(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2));
  }

  StepFillAreaStyleOutline resolveFillAreaStyleOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_STYLE_OUTLINE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepFillAreaStyleOutline(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepFillAreaStyleTiling resolveFillAreaStyleTiling(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_STYLE_TILING");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepFillAreaStyleTiling(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepFillAreaStyleTransparent resolveFillAreaStyleTransparent(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_STYLE_TRANSPARENT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepFillAreaStyleTransparent(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1));
  }

  StepFillAreaWithOutline resolveFillAreaWithOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILL_AREA_WITH_OUTLINE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepFillAreaWithOutline(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "FILL_AREA_WITH_OUTLINE outlines must contain entity references"));
  }

  // === Symbol / Presentation Entities ===

  StepLayeredItem resolveLayeredItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LAYERED_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepLayeredItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepOverRidingStyledItem resolveOverRidingStyledItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "OVER_RIDING_STYLED_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepOverRidingStyledItem(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "OVER_RIDING_STYLED_ITEM styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepStyledItem.class,
            "OVER_RIDING_STYLED_ITEM over_ridden_style must reference STYLED_ITEM"));
  }

  StepPresentationLayerUsage resolvePresentationLayerUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRESENTATION_LAYER_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepPresentationLayerUsage(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepStyledItem resolveStyledItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "STYLED_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepStyledItem(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "STYLED_ITEM styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepSymbolStyle resolveSymbolStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SYMBOL_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity styleOfSymbol = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(styleOfSymbol instanceof StepSymbolColour)) {
      throw new UnsupportedStepEntityException(
          "SYMBOL_STYLE style_of_symbol must reference SYMBOL_COLOUR");
    }
    return new StepSymbolStyle(instance.id(), resolver.stringValue(instance, definition, 0), styleOfSymbol);
  }
  // === Surface Style Entities ===

  StepPreDefinedSurfaceSideStyle resolvePreDefinedSurfaceSideStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRE_DEFINED_SURFACE_SIDE_STYLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepPreDefinedSurfaceSideStyle(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepSurfaceStyleParameterLine resolveSurfaceStyleParameterLine(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_PARAMETER_LINE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleParameterLine(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "SURFACE_STYLE_PARAMETER_LINE style must reference CURVE_STYLE"));
  }

  StepSurfaceStyleReflectanceAmbient resolveSurfaceStyleReflectanceAmbient(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleReflectanceAmbient(instance.id(), resolver.numberValue(instance, definition, 0));
  }

  StepSurfaceStyleReflectanceAmbientDiffuse resolveSurfaceStyleReflectanceAmbientDiffuse(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepSurfaceStyleReflectanceAmbientDiffuse(
        instance.id(),
        resolver.numberValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1));
  }

  StepSurfaceStyleReflectanceAmbientDiffuseSpecular resolveSurfaceStyleReflectanceAmbientDiffuseSpecular(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity specularColour = resolver.resolve(resolver.referenceId(instance, definition, 4));
    if (!(specularColour instanceof StepColour)
        && !(specularColour instanceof StepColourSpecification)
        && !(specularColour instanceof StepColourRgb)
        && !(specularColour instanceof StepDraughtingPreDefinedColour)
        && !(specularColour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR specular_colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepSurfaceStyleReflectanceAmbientDiffuseSpecular(
        instance.id(),
        resolver.numberValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        specularColour);
  }

  StepSurfaceStyleRenderingWithProperties resolveSurfaceStyleRenderingWithProperties(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_RENDERING_WITH_PROPERTIES");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    List<StepEntity> props =
        resolver.entityReferenceList(
            instance, definition, 1,
            "SURFACE_STYLE_RENDERING_WITH_PROPERTIES properties must contain entity references");
    return new StepSurfaceStyleRenderingWithProperties(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        props);
  }

  StepSurfaceStyleSegmentationCurve resolveSurfaceStyleSegmentationCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_STYLE_SEGMENTATION_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleSegmentationCurve(
        instance.id(),
        resolver.requireCurveStyleReference(instance, definition, "SURFACE_STYLE_SEGMENTATION_CURVE"));
  }
}
