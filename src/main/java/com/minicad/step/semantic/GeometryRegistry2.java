package com.minicad.step.semantic;

import java.util.Map;

/**
 * Geometry registry part 2.
 */
public final class GeometryRegistry2 {

  private GeometryRegistry2() {}

  public static void register(Map<String, EntityFactory> registry) {

// Entity: COMPOSITE_TEXT_WITH_DELINEATION
      registry.put(
          "COMPOSITE_TEXT_WITH_DELINEATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_TEXT_WITH_DELINEATION"));

// Entity: DRAUGHTING_TEXT_LITERAL_WITH_DELINEATION
      registry.put(
          "DRAUGHTING_TEXT_LITERAL_WITH_DELINEATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DRAUGHTING_TEXT_LITERAL_WITH_DELINEATION"));

// Entity: GENERAL_LINEAR_FUNCTION
      registry.put(
          "GENERAL_LINEAR_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERAL_LINEAR_FUNCTION"));

// Entity: HOMOGENEOUS_LINEAR_FUNCTION
      registry.put(
          "HOMOGENEOUS_LINEAR_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HOMOGENEOUS_LINEAR_FUNCTION"));

// Entity: IMPLICIT_POINT_ON_PLANE
      registry.put(
          "IMPLICIT_POINT_ON_PLANE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_POINT_ON_PLANE"));

// Entity: IMPORTED_POINT_FUNCTION
      registry.put(
          "IMPORTED_POINT_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_POINT_FUNCTION"));

// Entity: LINEARIZED_TABLE_FUNCTION
      registry.put(
          "LINEARIZED_TABLE_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEARIZED_TABLE_FUNCTION"));

// Entity: LINEAR_DIMENSION
      registry.put(
          "LINEAR_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEAR_DIMENSION"));

// Entity: LINEAR_PATH
      registry.put(
          "LINEAR_PATH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEAR_PATH"));

// Entity: MISMATCH_OF_POINTS
      registry.put(
          "MISMATCH_OF_POINTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_POINTS"));

// Entity: MISMATCH_OF_POINT_CLOUD_AND_RELATED_GEOMETRY
      registry.put(
          "MISMATCH_OF_POINT_CLOUD_AND_RELATED_GEOMETRY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_POINT_CLOUD_AND_RELATED_GEOMETRY"));

// Entity: MULTIPLY_DEFINED_CARTESIAN_POINTS
      registry.put(
          "MULTIPLY_DEFINED_CARTESIAN_POINTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_CARTESIAN_POINTS"));

// Entity: POINT
      registry.put("POINT", StepEntityResolver::resolvePoint);

// Entity: RATIONAL_B_SPLINE_CURVE
      registry.put("RATIONAL_B_SPLINE_CURVE", StepEntityResolver::resolveRationalBSplineCurve);

// Entity: RATIONAL_B_SPLINE_SURFACE
      registry.put("RATIONAL_B_SPLINE_SURFACE", StepEntityResolver::resolveRationalBSplineSurface);

// Entity: B_SPLINE_CURVE_WITH_KNOTS
      registry.put("B_SPLINE_CURVE_WITH_KNOTS", StepEntityResolver::resolveBSplineCurveWithKnots);

// Entity: B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS
      registry.put("B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineCurveWithKnotsAndBreakpoints);

// Entity: B_SPLINE_SURFACE_WITH_KNOTS
      registry.put("B_SPLINE_SURFACE_WITH_KNOTS", StepEntityResolver::resolveBSplineSurfaceWithKnots);

// Entity: B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS
      registry.put("B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineSurfaceWithKnotsAndBreakpoints);

// Entity: PIECEWISE_BEZIER_CURVE
      registry.put("PIECEWISE_BEZIER_CURVE", StepEntityResolver::resolvePiecewiseBezierCurve);

// Entity: PIECEWISE_BEZIER_SURFACE
      registry.put("PIECEWISE_BEZIER_SURFACE", StepEntityResolver::resolvePiecewiseBezierSurface);

// Entity: BEZIER_CURVE
      registry.put("BEZIER_CURVE", StepEntityResolver::resolveBezierCurve);

// Entity: BEZIER_SURFACE
      registry.put("BEZIER_SURFACE", StepEntityResolver::resolveBezierSurface);

// Entity: UNIFORM_CURVE
      registry.put("UNIFORM_CURVE", StepEntityResolver::resolveUniformCurve);

// Entity: UNIFORM_SURFACE
      registry.put("UNIFORM_SURFACE", StepEntityResolver::resolveUniformSurface);

// Entity: QUASI_UNIFORM_CURVE
      registry.put("QUASI_UNIFORM_CURVE", StepEntityResolver::resolveQuasiUniformCurve);

// Entity: QUASI_UNIFORM_SURFACE
      registry.put("QUASI_UNIFORM_SURFACE", StepEntityResolver::resolveQuasiUniformSurface);

// Entity: B_SPLINE_CURVE
      registry.put("B_SPLINE_CURVE", StepEntityResolver::resolveBSplineCurve);

// Entity: B_SPLINE_SURFACE
      registry.put("B_SPLINE_SURFACE", StepEntityResolver::resolveBSplineSurface);

// Entity: FACE_BASED_SURFACE_MODEL
      registry.put("FACE_BASED_SURFACE_MODEL", StepEntityResolver::resolveFaceBasedSurfaceModel);

// Entity: SHELL_BASED_SURFACE_MODEL
      registry.put("SHELL_BASED_SURFACE_MODEL", StepEntityResolver::resolveShellBasedSurfaceModel);

// Entity: SURFACE_MODEL
      registry.put("SURFACE_MODEL", StepEntityResolver::resolveSurfaceModel);

// Entity: COMPOSITE_CURVE_SEGMENT
      registry.put("COMPOSITE_CURVE_SEGMENT", StepEntityResolver::resolveCompositeCurveSegment);

// Entity: COMPOSITE_CURVE_ON_SURFACE
      registry.put("COMPOSITE_CURVE_ON_SURFACE", StepEntityResolver::resolveCompositeCurveOnSurface);

// Entity: BOUNDARY_CURVE
      registry.put(
          "BOUNDARY_CURVE",
          (resolver, instance) ->
              resolver.resolveCompositeCurveOnSurface(instance, "BOUNDARY_CURVE"));

// Entity: OUTER_BOUNDARY_CURVE
      registry.put(
          "OUTER_BOUNDARY_CURVE",
          (resolver, instance) ->
              resolver.resolveCompositeCurveOnSurface(instance, "OUTER_BOUNDARY_CURVE"));

// Entity: COMPOSITE_CURVE
      registry.put("COMPOSITE_CURVE", StepEntityResolver::resolveCompositeCurve);

// Entity: POLYLINE
      registry.put("POLYLINE", StepEntityResolver::resolvePolyline);

// Entity: INDEXED_POLY_CURVE
      registry.put("INDEXED_POLY_CURVE", StepEntityResolver::resolveIndexedPolyCurve);

// Entity: BOUNDED_CURVE
      registry.put("BOUNDED_CURVE", StepEntityResolver::resolveBoundedCurve);

// Entity: BOUNDED_SURFACE
      registry.put("BOUNDED_SURFACE", StepEntityResolver::resolveBoundedSurface);

// Entity: CURVE
      registry.put("CURVE", StepEntityResolver::resolveCurve);

// Entity: SURFACE
      registry.put("SURFACE", StepEntityResolver::resolveSurface);

// Entity: OFFSET_CURVE_2D
      registry.put("OFFSET_CURVE_2D", StepEntityResolver::resolveOffsetCurve2D);

// Entity: OFFSET_CURVE_3D
      registry.put("OFFSET_CURVE_3D", StepEntityResolver::resolveOffsetCurve3D);

// Entity: ORIENTED_CURVE
      registry.put("ORIENTED_CURVE", StepEntityResolver::resolveOrientedCurve);

// Entity: OFFSET_SURFACE
      registry.put("OFFSET_SURFACE", StepEntityResolver::resolveOffsetSurface);

// Entity: OFFSET_SURFACE_2
      registry.put("OFFSET_SURFACE_2", StepEntityResolver::resolveOffsetSurface2);

// Entity: MANIFOLD_SURFACE_MODEL
      registry.put("MANIFOLD_SURFACE_MODEL", StepEntityResolver::resolveManifoldSurfaceModel);

// Entity: SURFACED_EDGE_CURVE
      registry.put("SURFACED_EDGE_CURVE", StepEntityResolver::resolveSurfacedEdgeCurve);

// Entity: LINEAR_TOLERANCE_ZONE
      registry.put("LINEAR_TOLERANCE_ZONE", StepEntityResolver::resolveLinearToleranceZone);

// Entity: SURFACE_STYLE_RENDERING
      registry.put("SURFACE_STYLE_RENDERING", StepEntityResolver::resolveSurfaceStyleRendering);

// Entity: SURFACE_STYLE_RENDERING_WITH_PROPERTIES
      registry.put("SURFACE_STYLE_RENDERING_WITH_PROPERTIES", StepEntityResolver::resolveSurfaceStyleRenderingWithProperties);

// Entity: LIGHT_SOURCE_DIRECTIONAL
      registry.put("LIGHT_SOURCE_DIRECTIONAL", StepEntityResolver::resolveLightSourceDirectional);

// Entity: SPHERICAL_PAIR
      registry.put("SPHERICAL_PAIR", StepEntityResolver::resolveSphericalPair);

// Entity: SPHERICAL_JOINT
      registry.put("SPHERICAL_JOINT", StepEntityResolver::resolveSphericalJoint);

// Entity: DIRECTION_SENSE
      registry.put("DIRECTION_SENSE", StepEntityResolver::resolveDirectionSense);

// Entity: ANNOTATION_PLANE
      registry.put("ANNOTATION_PLANE", StepEntityResolver::resolveAnnotationPlane);

// Entity: ANNOTATION_POINT_OCCURRENCE
      registry.put("ANNOTATION_POINT_OCCURRENCE", StepEntityResolver::resolveAnnotationPointOccurrence);

// Entity: LEADER_CURVE
      registry.put("LEADER_CURVE", StepEntityResolver::resolveLeaderCurve);

// Entity: PROJECTION_CURVE
      registry.put("PROJECTION_CURVE", StepEntityResolver::resolveProjectionCurve);

// Entity: DIMENSION_CURVE
      registry.put("DIMENSION_CURVE", StepEntityResolver::resolveDimensionCurve);

// Entity: ANNOTATION_CURVE_OCCURRENCE
      registry.put("ANNOTATION_CURVE_OCCURRENCE", StepEntityResolver::resolveAnnotationCurveOccurrence);

// Entity: DRAUGHTING_PRE_DEFINED_CURVE_FONT
      registry.put(
          "DRAUGHTING_PRE_DEFINED_CURVE_FONT",
          StepEntityResolver::resolveDraughtingPreDefinedCurveFont);

// Entity: PRE_DEFINED_POINT_MARKER_SYMBOL
      registry.put(
          "PRE_DEFINED_POINT_MARKER_SYMBOL",
          StepEntityResolver::resolvePreDefinedPointMarkerSymbol);

// Entity: PRE_DEFINED_SURFACE_SIDE_STYLE
      registry.put(
          "PRE_DEFINED_SURFACE_SIDE_STYLE",
          StepEntityResolver::resolvePreDefinedSurfaceSideStyle);

// Entity: PRE_DEFINED_CURVE_FONT
      registry.put("PRE_DEFINED_CURVE_FONT", StepEntityResolver::resolvePreDefinedCurveFont);

// Entity: CURVE_STYLE
      registry.put("CURVE_STYLE", StepEntityResolver::resolveCurveStyle);

// Entity: POINT_STYLE
      registry.put("POINT_STYLE", StepEntityResolver::resolvePointStyle);

// Entity: CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS
      registry.put(
          "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS",
          StepEntityResolver::resolveCharacterGlyphStyleOutlineWithCharacteristics);

// Entity: CHARACTER_GLYPH_STYLE_OUTLINE
      registry.put(
          "CHARACTER_GLYPH_STYLE_OUTLINE",
          StepEntityResolver::resolveCharacterGlyphStyleOutline);

// Entity: SURFACE_STYLE_FILL_AREA
      registry.put("SURFACE_STYLE_FILL_AREA", StepEntityResolver::resolveSurfaceStyleFillArea);

// Entity: SURFACE_STYLE_BOUNDARY
      registry.put("SURFACE_STYLE_BOUNDARY", StepEntityResolver::resolveSurfaceStyleBoundary);

// Entity: SURFACE_STYLE_CONTROL_GRID
      registry.put("SURFACE_STYLE_CONTROL_GRID", StepEntityResolver::resolveSurfaceStyleControlGrid);

// Entity: SURFACE_STYLE_SEGMENTATION_CURVE
      registry.put("SURFACE_STYLE_SEGMENTATION_CURVE", StepEntityResolver::resolveSurfaceStyleSegmentationCurve);

// Entity: SURFACE_STYLE_SILHOUETTE
      registry.put("SURFACE_STYLE_SILHOUETTE", StepEntityResolver::resolveSurfaceStyleSilhouette);

// Entity: SURFACE_STYLE_TRANSPARENT
      registry.put("SURFACE_STYLE_TRANSPARENT", StepEntityResolver::resolveSurfaceStyleTransparent);

// Entity: SURFACE_STYLE_REFLECTANCE_AMBIENT
      registry.put("SURFACE_STYLE_REFLECTANCE_AMBIENT", StepEntityResolver::resolveSurfaceStyleReflectanceAmbient);

// Entity: SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE",
          StepEntityResolver::resolveSurfaceStyleReflectanceAmbientDiffuse);

// Entity: SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR",
          StepEntityResolver::resolveSurfaceStyleReflectanceAmbientDiffuseSpecular);

// Entity: SURFACE_STYLE_PARAMETER_LINE
      registry.put("SURFACE_STYLE_PARAMETER_LINE", StepEntityResolver::resolveSurfaceStyleParameterLine);

// Entity: SURFACE_SIDE_STYLE
      registry.put("SURFACE_SIDE_STYLE", StepEntityResolver::resolveSurfaceSideStyle);

// Entity: SURFACE_STYLE_USAGE
      registry.put("SURFACE_STYLE_USAGE", StepEntityResolver::resolveSurfaceStyleUsage);

// Entity: DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY
      registry.put(
          "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY",
          (resolver, instance) ->
              resolver.resolveAnnotationOccurrenceRelationship(
                  instance, "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY"));

// Entity: GEOMETRIC_CURVE_SET
      registry.put("GEOMETRIC_CURVE_SET", StepEntityResolver::resolveGeometricCurveSet);

// Entity: GEOMETRIC_SURFACE_SET
      registry.put("GEOMETRIC_SURFACE_SET", StepEntityResolver::resolveGeometricSurfaceSet);

// Entity: POINT_SET
      registry.put("POINT_SET", StepEntityResolver::resolvePointSet);

// Entity: TRIANGULATED_SURFACE_SET
      registry.put(
          "TRIANGULATED_SURFACE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: DIMENSION_CURVE_DIRECTED_CALLOUT
      registry.put(
          "DIMENSION_CURVE_DIRECTED_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "DIMENSION_CURVE_DIRECTED_CALLOUT"));

// Entity: SURFACE_CONDITION_CALLOUT
      registry.put(
          "SURFACE_CONDITION_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "SURFACE_CONDITION_CALLOUT"));

// Entity: CARTESIAN_POINT
      registry.put("CARTESIAN_POINT", StepEntityResolver::resolveCartesianPoint);

// Entity: DIRECTION
      registry.put("DIRECTION", StepEntityResolver::resolveDirection);

// Entity: VECTOR
      registry.put("VECTOR", StepEntityResolver::resolveVector);

// Entity: LINE
      registry.put("LINE", StepEntityResolver::resolveLine);

// Entity: PLANE
      registry.put("PLANE", StepEntityResolver::resolvePlane);

// Entity: CIRCLE
      registry.put("CIRCLE", StepEntityResolver::resolveCircle);

// Entity: ELLIPSE
      registry.put("ELLIPSE", StepEntityResolver::resolveEllipse);

// Entity: PARABOLA
      registry.put(
          "PARABOLA",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "PARABOLA", 1));

// Entity: HYPERBOLA
      registry.put(
          "HYPERBOLA",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "HYPERBOLA", 2));

// Entity: CONIC_CURVE
      registry.put(
          "CONIC_CURVE",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "CONIC_CURVE", 2));

// Entity: CLOTHOID
      registry.put("CLOTHOID", StepEntityResolver::resolveClothoid);

// Entity: SURFACE_CURVE
      registry.put("SURFACE_CURVE", StepEntityResolver::resolveSurfaceCurve);

// Entity: INTERSECTION_CURVE
      registry.put(
          "INTERSECTION_CURVE",
          (resolver, instance) -> resolver.resolveSurfaceCurve(instance, "INTERSECTION_CURVE"));

// Entity: SEAM_CURVE
      registry.put("SEAM_CURVE", StepEntityResolver::resolveSeamCurve);

// Entity: DEGENERATE_CURVE
      registry.put("DEGENERATE_CURVE", StepEntityResolver::resolveDegenerateCurve);

// Entity: DEGENERATE_PCURVE
      registry.put("DEGENERATE_PCURVE", StepEntityResolver::resolveDegeneratePcurve);

// Entity: PCURVE
      registry.put("PCURVE", StepEntityResolver::resolvePcurve);

// Entity: CYLINDRICAL_SURFACE
      registry.put("CYLINDRICAL_SURFACE", StepEntityResolver::resolveCylindricalSurface);

// Entity: CONICAL_SURFACE
      registry.put("CONICAL_SURFACE", StepEntityResolver::resolveConicalSurface);

// Entity: TOROIDAL_SURFACE
      registry.put("TOROIDAL_SURFACE", StepEntityResolver::resolveToroidalSurface);

// Entity: DEGENERATE_TOROIDAL_SURFACE
      registry.put("DEGENERATE_TOROIDAL_SURFACE", StepEntityResolver::resolveDegenerateToroidalSurface);

// Entity: SPHERICAL_SURFACE
      registry.put("SPHERICAL_SURFACE", StepEntityResolver::resolveSphericalSurface);

// Entity: SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS
      registry.put("SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveSphericalSurfaceWithEllipticalAxis);

// Entity: CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS
      registry.put("CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveCylindricalSurfaceWithEllipticalAxis);

// Entity: CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS
      registry.put("CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveConicalSurfaceWithEllipticalAxis);

// Entity: TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS
      registry.put("TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveToroidalSurfaceWithEllipticalAxis);

// Entity: TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS
      registry.put("TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS", StepEntityResolver::resolveToroidalSurfaceWithCylindricalAxis);

// Entity: TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS
      registry.put("TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS", StepEntityResolver::resolveToroidalSurfaceWithSpecifiedBends);

// Entity: BLENDED_SURFACE
      registry.put("BLENDED_SURFACE", StepEntityResolver::resolveBlendedSurface);

// Entity: FREE_FORM_SURFACE
      registry.put("FREE_FORM_SURFACE", StepEntityResolver::resolveFreeFormSurface);

// Entity: CURVED_TOLERANCE_ZONE
      registry.put("CURVED_TOLERANCE_ZONE", StepEntityResolver::resolveCurvedToleranceZone);

// Entity: SURFACE_QUALITY
      registry.put("SURFACE_QUALITY", StepEntityResolver::resolveSurfaceQuality);

// Entity: MEASUREMENT_POINT
      registry.put("MEASUREMENT_POINT", StepEntityResolver::resolveMeasurementPoint);

// Entity: SURFACE_MEASUREMENT
      registry.put("SURFACE_MEASUREMENT", StepEntityResolver::resolveSurfaceMeasurement);

// Entity: SURFACE_TEXTURE_REPRESENTATION_ITEM
      registry.put("SURFACE_TEXTURE_REPRESENTATION_ITEM", StepEntityResolver::resolveSurfaceTextureRepresentationItem);

// Entity: RULED_SURFACE
      registry.put("RULED_SURFACE", StepEntityResolver::resolveRuledSurface);

// Entity: SURFACE_PATCH
      registry.put("SURFACE_PATCH", StepEntityResolver::resolveSurfacePatch);

// Entity: RECTANGULAR_TRIMMED_SURFACE
      registry.put("RECTANGULAR_TRIMMED_SURFACE", StepEntityResolver::resolveRectangularTrimmedSurface);

// Entity: CURVE_BOUNDED_SURFACE
      registry.put("CURVE_BOUNDED_SURFACE", StepEntityResolver::resolveCurveBoundedSurface);

// Entity: ORIENTED_SURFACE
      registry.put("ORIENTED_SURFACE", StepEntityResolver::resolveOrientedSurface);

// Entity: SURFACE_OF_LINEAR_EXTRUSION
      registry.put("SURFACE_OF_LINEAR_EXTRUSION", StepEntityResolver::resolveSurfaceOfLinearExtrusion);

// Entity: SURFACE_OF_REVOLUTION
      registry.put("SURFACE_OF_REVOLUTION", StepEntityResolver::resolveSurfaceOfRevolution);

// Entity: SURFACE_OF_CONSTANT_RADIUS
      registry.put("SURFACE_OF_CONSTANT_RADIUS", StepEntityResolver::resolveSurfaceOfConstantRadius);

// Entity: TRIMMED_CURVE
      registry.put("TRIMMED_CURVE", StepEntityResolver::resolveTrimmedCurve);

// Entity: VERTEX_POINT
      registry.put("VERTEX_POINT", StepEntityResolver::resolveVertexPoint);

// Entity: EDGE_CURVE
      registry.put("EDGE_CURVE", StepEntityResolver::resolveEdgeCurve);

// Entity: LINE_SEGMENT
      registry.put("LINE_SEGMENT", StepEntityResolver::resolveLineSegment);

// Entity: RECTANGULAR_COMPOSITE_SURFACE
      registry.put("RECTANGULAR_COMPOSITE_SURFACE", StepEntityResolver::resolveRectangularCompositeSurface);

// Entity: COMPOSITE_CURVE_ON_SURFACE_3D
      registry.put("COMPOSITE_CURVE_ON_SURFACE_3D", StepEntityResolver::resolveCompositeCurveOnSurface3D);

// Entity: FACE_SURFACE
      registry.put("FACE_SURFACE", StepEntityResolver::resolveFaceSurface);

// Entity: CUBIC_BEZIER_TRIANGULATED_FACE
      registry.put("CUBIC_BEZIER_TRIANGULATED_FACE", StepEntityResolver::resolveCubicBezierTriangulatedFace);

// Entity: CURVE_3D_ELEMENT_PROPERTY
      registry.put("CURVE_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveCurve3dElementProperty);

// Entity: SURFACE_3D_ELEMENT_PROPERTY
      registry.put("SURFACE_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveSurface3dElementProperty);

// Entity: FEA_LINEAR_MATERIAL
      registry.put("FEA_LINEAR_MATERIAL", StepEntityResolver::resolveFeaLinearMaterial);

// Entity: FEA_NON_LINEAR_MATERIAL
      registry.put("FEA_NON_LINEAR_MATERIAL", StepEntityResolver::resolveFeaNonLinearMaterial);

// Entity: DISPLACEMENT_BOUNDARY_CONDITION
      registry.put("DISPLACEMENT_BOUNDARY_CONDITION", StepEntityResolver::resolveDisplacementBoundaryCondition);

// Entity: SURFACE_ELEMENT
      registry.put("SURFACE_ELEMENT", StepEntityResolver::resolveSurfaceElement);

// Entity: LINE_ELEMENT
      registry.put("LINE_ELEMENT", StepEntityResolver::resolveLineElement);

// Entity: UNIFORM_SURFACE_ELEMENT
      registry.put("UNIFORM_SURFACE_ELEMENT", StepEntityResolver::resolveUniformSurfaceElement);

// Entity: FEA_LINEAR_ALGEBRAIC_MATRIX
      registry.put("FEA_LINEAR_ALGEBRAIC_MATRIX", StepEntityResolver::resolveFeaLinearAlgebraicMatrix);

// Entity: FEA_LINEAR_ALGEBRAIC_VECTOR
      registry.put("FEA_LINEAR_ALGEBRAIC_VECTOR", StepEntityResolver::resolveFeaLinearAlgebraicVector);

// Entity: FEA_AXIS_2_PLACEMENT_3D
      registry.put("FEA_AXIS_2_PLACEMENT_3D", StepEntityResolver::resolveFeaAxis2Placement3d);

// Entity: SURFACED_OPEN_SHELL
      registry.put("SURFACED_OPEN_SHELL", StepEntityResolver::resolveSurfacedOpenShell);

// Entity: TESSELLATED_CURVE
      registry.put(
          "TESSELLATED_CURVE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_POINT_SET
      registry.put(
          "TESSELLATED_POINT_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: REPARAMETRISED_COMPOSITE_CURVE_SEGMENT
      registry.put(
          "REPARAMETRISED_COMPOSITE_CURVE_SEGMENT",
          (resolver, instance) -> resolver.resolveCompositeCurveSegment(instance));

// Entity: B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS_CURVE
      registry.put(
          "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS_CURVE",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));

// Entity: EXTERNALLY_DEFINED_CURVE_FONT
      registry.put(
          "EXTERNALLY_DEFINED_CURVE_FONT",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_CURVE_FONT"));

// Entity: TESSELLATED_CURVE_SET
      registry.put(
          "TESSELLATED_CURVE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: B_SPLINE_CURVE_UNIFORM
      registry.put(
          "B_SPLINE_CURVE_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));

// Entity: B_SPLINE_CURVE_QUASI_UNIFORM
      registry.put(
          "B_SPLINE_CURVE_QUASI_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));

// Entity: B_SPLINE_CURVE_BEZIER
      registry.put(
          "B_SPLINE_CURVE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));

// Entity: B_SPLINE_CURVE_PIECEWISE_BEZIER
      registry.put(
          "B_SPLINE_CURVE_PIECEWISE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));

// Entity: B_SPLINE_SURFACE_UNIFORM
      registry.put(
          "B_SPLINE_SURFACE_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));

// Entity: B_SPLINE_SURFACE_QUASI_UNIFORM
      registry.put(
          "B_SPLINE_SURFACE_QUASI_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));

// Entity: B_SPLINE_SURFACE_BEZIER
      registry.put(
          "B_SPLINE_SURFACE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));

// Entity: B_SPLINE_SURFACE_PIECEWISE_BEZIER
      registry.put(
          "B_SPLINE_SURFACE_PIECEWISE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));

// Entity: POINT_SET_2D
      registry.put(
          "POINT_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: POINT_SET_3D
      registry.put(
          "POINT_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: CURVE_SET_2D
      registry.put(
          "CURVE_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: CURVE_SET_3D
      registry.put(
          "CURVE_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: SURFACE_SET
      registry.put(
          "SURFACE_SET",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: DRAUGHTING_PRE_DEFINED_POINT_SYMBOL
      registry.put(
          "DRAUGHTING_PRE_DEFINED_POINT_SYMBOL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: AXIS_PLACEMENT
      registry.put(
          "AXIS_PLACEMENT",
          (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));

// Entity: AXIS_PLACEMENT_2D
      registry.put(
          "AXIS_PLACEMENT_2D",
          (resolver, instance) -> resolver.resolveAxis2Placement2D(instance));

// Entity: AXIS_PLACEMENT_3D
      registry.put(
          "AXIS_PLACEMENT_3D",
          (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));

// Entity: PLACEMENT_1D
      registry.put(
          "PLACEMENT_1D",
          (resolver, instance) -> resolver.resolveAxis1Placement(instance));

// Entity: PLACEMENT_2D
      registry.put(
          "PLACEMENT_2D",
          (resolver, instance) -> resolver.resolveAxis2Placement2D(instance));

// Entity: PLACEMENT_3D
      registry.put(
          "PLACEMENT_3D",
          (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));

// Entity: FEA_AXIS2_PLACEMENT_3D
      registry.put("FEA_AXIS2_PLACEMENT_3D", StepEntityResolver::resolveFeaAxis2Placement3d);

// Entity: FEA_LINEAR_ALGEBRA_MATRIX
      registry.put(
          "FEA_LINEAR_ALGEBRA_MATRIX",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: FEA_LINEAR_ALGEBRA_MATRIX_3D
      registry.put(
          "FEA_LINEAR_ALGEBRA_MATRIX_3D",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: CURVE_ELEMENT_FREEDOM
      registry.put(
          "CURVE_ELEMENT_FREEDOM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: CURVE_ELEMENT_FREEDOM_VALUE
      registry.put(
          "CURVE_ELEMENT_FREEDOM_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: SURFACE_ELEMENT_FREEDOM
      registry.put(
          "SURFACE_ELEMENT_FREEDOM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: SURFACE_ELEMENT_FREEDOM_VALUE
      registry.put(
          "SURFACE_ELEMENT_FREEDOM_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: CIRCLE_2D
      registry.put("CIRCLE_2D", StepEntityResolver::resolveCircle2D);

// Entity: ELLIPSE_2D
      registry.put("ELLIPSE_2D", StepEntityResolver::resolveEllipse2D);

// Entity: HYPERBOLA_2D
      registry.put("HYPERBOLA_2D", StepEntityResolver::resolveHyperbola2D);

// Entity: PARABOLA_2D
      registry.put("PARABOLA_2D", StepEntityResolver::resolveParabola2D);

// Entity: LINE_2D
      registry.put("LINE_2D", StepEntityResolver::resolveLine2D);

// Entity: POLYLINE_2D
      registry.put("POLYLINE_2D", StepEntityResolver::resolvePolyline2D);

// Entity: TRIMMED_CURVE_2D
      registry.put("TRIMMED_CURVE_2D", StepEntityResolver::resolveTrimmedCurve2D);

// Entity: COMPOSITE_CURVE_2D
      registry.put("COMPOSITE_CURVE_2D", StepEntityResolver::resolveCompositeCurve2D);

// Entity: B_SPLINE_CURVE_2D
      registry.put("B_SPLINE_CURVE_2D", StepEntityResolver::resolveBSplineCurve2D);

// Entity: RATIONAL_B_SPLINE_CURVE_2D
      registry.put("RATIONAL_B_SPLINE_CURVE_2D", StepEntityResolver::resolveRationalBSplineCurve2D);

// Entity: BEZIER_CURVE_2D
      registry.put("BEZIER_CURVE_2D", StepEntityResolver::resolveBezierCurve2D);

// Entity: QUASI_UNIFORM_CURVE_2D
      registry.put("QUASI_UNIFORM_CURVE_2D", StepEntityResolver::resolveQuasiUniformCurve2D);

// Entity: UNIFORM_CURVE_2D
      registry.put("UNIFORM_CURVE_2D", StepEntityResolver::resolveUniformCurve2D);

// Entity: PIECEWISE_BEZIER_CURVE_2D
      registry.put("PIECEWISE_BEZIER_CURVE_2D", StepEntityResolver::resolvePiecewiseBezierCurve2D);

// Entity: INDEXED_POLY_CURVE_2D
      registry.put("INDEXED_POLY_CURVE_2D", StepEntityResolver::resolveIndexedPolyCurve2D);

// Entity: DEGENERATE_CURVE_2D
      registry.put("DEGENERATE_CURVE_2D", StepEntityResolver::resolveDegenerateCurve2D);

// Entity: SURFACE_OF_TRANSLATION
      registry.put("SURFACE_OF_TRANSLATION", StepEntityResolver::resolveSurfaceOfTranslation);

// Entity: SURFACE_OF_PROJECTION
      registry.put("SURFACE_OF_PROJECTION", StepEntityResolver::resolveSurfaceOfProjection);

// Entity: PARABOLOID_SURFACE
      registry.put("PARABOLOID_SURFACE", StepEntityResolver::resolveParaboloidSurface);

// Entity: HYPERBOLOID_SURFACE
      registry.put("HYPERBOLOID_SURFACE", StepEntityResolver::resolveHyperboloidSurface);

// Entity: BOUNDED_CURVE_2D
      registry.put("BOUNDED_CURVE_2D", StepEntityResolver::resolveBoundedCurve2D);

// Entity: CURVE_2D
      registry.put("CURVE_2D", StepEntityResolver::resolveCurve2D);

// Entity: MACHINED_SURFACE
      registry.put("MACHINED_SURFACE", StepEntityResolver::resolveMachinedSurface);

// Entity: PLANE_ANGLE_UNIT_WITH_UNIT
      registry.put("PLANE_ANGLE_UNIT_WITH_UNIT", StepEntityResolver::resolvePlaneAngleUnitWithUnit);

// Entity: SWEPT_PROFILE_AREA_OUTLINE
      registry.put("SWEPT_PROFILE_AREA_OUTLINE", StepEntityResolver::resolveSweptProfileAreaOutline);

// Entity: INDEXED_POLYCURVE
      registry.put("INDEXED_POLYCURVE", StepEntityResolver::resolveIndexedPolycurve);

// Entity: POLYLINE_3D
      registry.put("POLYLINE_3D", StepEntityResolver::resolvePolyline3D);

// Entity: FILL_AREA_WITH_OUTLINE
      registry.put("FILL_AREA_WITH_OUTLINE", StepEntityResolver::resolveFillAreaWithOutline);

// Entity: CHARACTER_GLYPH_OUTLINE
      registry.put("CHARACTER_GLYPH_OUTLINE", StepEntityResolver::resolveCharacterGlyphOutline);

// Entity: CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS
      registry.put("CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS", StepEntityResolver::resolveCharacterGlyphOutlineWithCharacteristics);

// Entity: PRE_DEFINED_SURFACE_STYLE
      registry.put("PRE_DEFINED_SURFACE_STYLE", StepEntityResolver::resolvePreDefinedSurfaceStyle);

// Entity: SURFACE_STYLE_PARAMETER_LINES
      registry.put("SURFACE_STYLE_PARAMETER_LINES", StepEntityResolver::resolveSurfaceStyleParameterLines);

// Entity: FILL_AREA_STYLE_OUTLINE
      registry.put("FILL_AREA_STYLE_OUTLINE", StepEntityResolver::resolveFillAreaStyleOutline);

// Entity: CURVE_STYLE_FONT
      registry.put("CURVE_STYLE_FONT", StepEntityResolver::resolveCurveStyleFont);

// Entity: CURVE_STYLE_RENDERING
      registry.put("CURVE_STYLE_RENDERING", StepEntityResolver::resolveCurveStyleRendering);

// Entity: CURVE_STYLE_WITH_FONT
      registry.put("CURVE_STYLE_WITH_FONT", StepEntityResolver::resolveCurveStyleWithFont);

// Entity: LINEAR_DIMENSION_REPRESENTATION
      registry.put("LINEAR_DIMENSION_REPRESENTATION", StepEntityResolver::resolveLinearDimensionRepresentation);

// Entity: BSPLINE_CURVE_2D
      registry.put("BSPLINE_CURVE_2D", StepEntityResolver::resolveBSplineCurve2D);

// Entity: RATIONAL_BSPLINE_CURVE_2D
      registry.put("RATIONAL_BSPLINE_CURVE_2D", StepEntityResolver::resolveRationalBSplineCurve2D);


  }
}
