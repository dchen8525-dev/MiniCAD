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
      registry.put("RATIONAL_B_SPLINE_CURVE", (resolver, instance) -> resolver.bSplineResolver.resolveRationalBSplineCurve(instance));

// Entity: RATIONAL_B_SPLINE_SURFACE
      registry.put("RATIONAL_B_SPLINE_SURFACE", (resolver, instance) -> resolver.bSplineResolver.resolveRationalBSplineSurface(instance));

// Entity: B_SPLINE_CURVE_WITH_KNOTS
      registry.put("B_SPLINE_CURVE_WITH_KNOTS", StepEntityResolver::resolveBSplineCurveWithKnots);

// Entity: B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS
      registry.put("B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineCurveWithKnotsAndBreakpoints);

// Entity: B_SPLINE_SURFACE_WITH_KNOTS
      registry.put("B_SPLINE_SURFACE_WITH_KNOTS", StepEntityResolver::resolveBSplineSurfaceWithKnots);

// Entity: B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS
      registry.put("B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineSurfaceWithKnotsAndBreakpoints);

// Entity: PIECEWISE_BEZIER_CURVE
      registry.put("PIECEWISE_BEZIER_CURVE", (resolver, instance) -> resolver.bezierResolver.resolvePiecewiseBezierCurve(instance));

// Entity: PIECEWISE_BEZIER_SURFACE
      registry.put("PIECEWISE_BEZIER_SURFACE", (resolver, instance) -> resolver.bezierResolver.resolvePiecewiseBezierSurface(instance));

// Entity: BEZIER_CURVE
      registry.put("BEZIER_CURVE", (resolver, instance) -> resolver.bezierResolver.resolveBezierCurve(instance));

// Entity: BEZIER_SURFACE
      registry.put("BEZIER_SURFACE", (resolver, instance) -> resolver.bezierResolver.resolveBezierSurface(instance));

// Entity: UNIFORM_CURVE
      registry.put("UNIFORM_CURVE", (resolver, instance) -> resolver.bezierResolver.resolveUniformCurve(instance));

// Entity: UNIFORM_SURFACE
      registry.put("UNIFORM_SURFACE", (resolver, instance) -> resolver.bezierResolver.resolveUniformSurface(instance));

// Entity: QUASI_UNIFORM_CURVE
      registry.put("QUASI_UNIFORM_CURVE", (resolver, instance) -> resolver.bezierResolver.resolveQuasiUniformCurve(instance));

// Entity: QUASI_UNIFORM_SURFACE
      registry.put("QUASI_UNIFORM_SURFACE", (resolver, instance) -> resolver.bezierResolver.resolveQuasiUniformSurface(instance));

// Entity: B_SPLINE_CURVE
      registry.put("B_SPLINE_CURVE", (resolver, instance) -> resolver.bSplineResolver.resolveBSplineCurve(instance));

// Entity: B_SPLINE_SURFACE
      registry.put("B_SPLINE_SURFACE", (resolver, instance) -> resolver.bSplineResolver.resolveBSplineSurface(instance));

// Entity: FACE_BASED_SURFACE_MODEL
      registry.put("FACE_BASED_SURFACE_MODEL", (resolver, instance) -> resolver.topologyResolver.resolveFaceBasedSurfaceModel(instance));

// Entity: SHELL_BASED_SURFACE_MODEL
      registry.put("SHELL_BASED_SURFACE_MODEL", (resolver, instance) -> resolver.topologyResolver.resolveShellBasedSurfaceModel(instance));

// Entity: SURFACE_MODEL
      registry.put("SURFACE_MODEL", (resolver, instance) -> resolver.surfaceResolver.resolveSurfaceModel(instance));

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
      registry.put("COMPOSITE_CURVE", (resolver, instance) -> resolver.bezierResolver.resolveCompositeCurve(instance));

// Entity: POLYLINE
      registry.put("POLYLINE", (resolver, instance) -> resolver.geometryResolver.resolvePolyline(instance));

// Entity: INDEXED_POLY_CURVE
      registry.put("INDEXED_POLY_CURVE", (resolver, instance) -> resolver.curveResolver.resolveIndexedPolyCurve(instance));

// Entity: BOUNDED_CURVE
      registry.put("BOUNDED_CURVE", StepEntityResolver::resolveBoundedCurve);

// Entity: BOUNDED_SURFACE
      registry.put("BOUNDED_SURFACE", (resolver, instance) -> resolver.bezierResolver.resolveBoundedSurface(instance));

// Entity: CURVE
      registry.put("CURVE", StepEntityResolver::resolveCurve);

// Entity: SURFACE
      registry.put("SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveSurface(instance));

// Entity: OFFSET_CURVE_2D
      registry.put("OFFSET_CURVE_2D", (resolver, instance) -> resolver.bezierResolver.resolveOffsetCurve2D(instance));

// Entity: OFFSET_CURVE_3D
      registry.put("OFFSET_CURVE_3D", (resolver, instance) -> resolver.bezierResolver.resolveOffsetCurve3D(instance));

// Entity: ORIENTED_CURVE
      registry.put("ORIENTED_CURVE", (resolver, instance) -> resolver.bezierResolver.resolveOrientedCurve(instance));

// Entity: OFFSET_SURFACE
      registry.put("OFFSET_SURFACE", (resolver, instance) -> resolver.bezierResolver.resolveOffsetSurface(instance));

// Entity: OFFSET_SURFACE_2
      registry.put("OFFSET_SURFACE_2", (resolver, instance) -> resolver.surfaceResolver.resolveOffsetSurface2(instance));

// Entity: MANIFOLD_SURFACE_MODEL
      registry.put("MANIFOLD_SURFACE_MODEL", (resolver, instance) -> resolver.topologyResolver.resolveManifoldSurfaceModel(instance));

// Entity: SURFACED_EDGE_CURVE
      registry.put("SURFACED_EDGE_CURVE", (resolver, instance) -> resolver.topologyResolver.resolveSurfacedEdgeCurve(instance));

// Entity: LINEAR_TOLERANCE_ZONE
      registry.put("LINEAR_TOLERANCE_ZONE", (resolver, instance) -> resolver.annotationResolver.resolveLinearToleranceZone(instance));

// Entity: SURFACE_STYLE_RENDERING
      registry.put("SURFACE_STYLE_RENDERING", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleRendering(instance));

// Entity: SURFACE_STYLE_RENDERING_WITH_PROPERTIES
      registry.put(
          "SURFACE_STYLE_RENDERING_WITH_PROPERTIES",
          (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleRenderingWithProperties(instance));

// Entity: LIGHT_SOURCE_DIRECTIONAL
      registry.put("LIGHT_SOURCE_DIRECTIONAL", (resolver, instance) -> resolver.visualizationResolver.resolveLightSourceDirectional(instance));

// Entity: SPHERICAL_PAIR
      registry.put("SPHERICAL_PAIR", (resolver, instance) -> resolver.kinematicResolver.resolveSphericalPair(instance));

// Entity: SPHERICAL_JOINT
      registry.put("SPHERICAL_JOINT", (resolver, instance) -> resolver.kinematicResolver.resolveSphericalJoint(instance));

// Entity: DIRECTION_SENSE
      registry.put("DIRECTION_SENSE", StepEntityResolver::resolveDirectionSense);

// Entity: ANNOTATION_PLANE
      registry.put("ANNOTATION_PLANE", (resolver, instance) -> resolver.annotationResolver.resolveAnnotationPlane(instance));

// Entity: ANNOTATION_POINT_OCCURRENCE
      registry.put("ANNOTATION_POINT_OCCURRENCE", (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationPointOccurrence(instance));

// Entity: LEADER_CURVE
      registry.put("LEADER_CURVE", (resolver, instance) -> resolver.annotationResolver.resolveLeaderCurve(instance));

// Entity: PROJECTION_CURVE
      registry.put("PROJECTION_CURVE", (resolver, instance) -> resolver.annotationResolver.resolveProjectionCurve(instance));

// Entity: DIMENSION_CURVE
      registry.put("DIMENSION_CURVE", (resolver, instance) -> resolver.annotationResolver.resolveDimensionCurve(instance));

// Entity: ANNOTATION_CURVE_OCCURRENCE
      registry.put("ANNOTATION_CURVE_OCCURRENCE", (resolver, instance) -> resolver.draughtingResolver.resolveAnnotationCurveOccurrence(instance));

// Entity: DRAUGHTING_PRE_DEFINED_CURVE_FONT
      registry.put(
          "DRAUGHTING_PRE_DEFINED_CURVE_FONT",
          (resolver, instance) -> resolver.draughtingResolver.resolveDraughtingPreDefinedCurveFont(instance));

// Entity: PRE_DEFINED_POINT_MARKER_SYMBOL
      registry.put(
          "PRE_DEFINED_POINT_MARKER_SYMBOL",
          StepEntityResolver::resolvePreDefinedPointMarkerSymbol);

// Entity: PRE_DEFINED_SURFACE_SIDE_STYLE
      registry.put(
          "PRE_DEFINED_SURFACE_SIDE_STYLE",
          (resolver, instance) -> resolver.materialResolver.resolvePreDefinedSurfaceSideStyle(instance));

// Entity: POINT_STYLE
      registry.put("POINT_STYLE", (resolver, instance) -> resolver.materialResolver.resolvePointStyle(instance));

// Entity: CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS
      registry.put(
          "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS",
          (resolver, instance) -> resolver.draughtingResolver.resolveCharacterGlyphStyleOutlineWithCharacteristics(instance));

// Entity: CHARACTER_GLYPH_STYLE_OUTLINE
      registry.put(
          "CHARACTER_GLYPH_STYLE_OUTLINE",
          (resolver, instance) -> resolver.draughtingResolver.resolveCharacterGlyphStyleOutline(instance));

// Entity: SURFACE_STYLE_BOUNDARY
      registry.put("SURFACE_STYLE_BOUNDARY", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleBoundary(instance));

// Entity: SURFACE_STYLE_CONTROL_GRID
      registry.put("SURFACE_STYLE_CONTROL_GRID", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleControlGrid(instance));

// Entity: SURFACE_STYLE_SEGMENTATION_CURVE
      registry.put(
          "SURFACE_STYLE_SEGMENTATION_CURVE",
          (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleSegmentationCurve(instance));

// Entity: SURFACE_STYLE_SILHOUETTE
      registry.put("SURFACE_STYLE_SILHOUETTE", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleSilhouette(instance));

// Entity: SURFACE_STYLE_TRANSPARENT
      registry.put("SURFACE_STYLE_TRANSPARENT", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleTransparent(instance));

// Entity: SURFACE_STYLE_REFLECTANCE_AMBIENT
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT",
          (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleReflectanceAmbient(instance));

// Entity: SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE",
          (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleReflectanceAmbientDiffuse(instance));

// Entity: SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR",
          (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleReflectanceAmbientDiffuseSpecular(instance));

// Entity: SURFACE_STYLE_PARAMETER_LINE
      registry.put("SURFACE_STYLE_PARAMETER_LINE", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleParameterLine(instance));

// Entity: DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY
      registry.put(
          "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY",
          (resolver, instance) ->
              resolver.resolveAnnotationOccurrenceRelationship(
                  instance, "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY"));

// Entity: GEOMETRIC_CURVE_SET
      registry.put("GEOMETRIC_CURVE_SET", StepEntityResolver::resolveGeometricCurveSet);

// Entity: GEOMETRIC_SURFACE_SET
      registry.put("GEOMETRIC_SURFACE_SET", (resolver, instance) -> resolver.surfaceResolver.resolveGeometricSurfaceSet(instance));

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
      registry.put("DIRECTION", (resolver, instance) -> resolver.geometryResolver.resolveDirection(instance));

// Entity: VECTOR
      registry.put("VECTOR", (resolver, instance) -> resolver.geometryResolver.resolveVector(instance));

// Entity: LINE
      registry.put("LINE", (resolver, instance) -> resolver.geometryResolver.resolveLine(instance));

// Entity: PLANE
      registry.put("PLANE", (resolver, instance) -> resolver.surfaceResolver.resolvePlane(instance));

// Entity: CIRCLE
      registry.put("CIRCLE", (resolver, instance) -> resolver.geometryResolver.resolveCircle(instance));

// Entity: ELLIPSE
      registry.put("ELLIPSE", (resolver, instance) -> resolver.geometryResolver.resolveEllipse(instance));

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
      registry.put("SEAM_CURVE", (resolver, instance) -> resolver.curveResolver.resolveSeamCurve(instance));

// Entity: DEGENERATE_CURVE
      registry.put("DEGENERATE_CURVE", (resolver, instance) -> resolver.curveResolver.resolveDegenerateCurve(instance));

// Entity: DEGENERATE_PCURVE
      registry.put("DEGENERATE_PCURVE", (resolver, instance) -> resolver.curveResolver.resolveDegeneratePcurve(instance));

// Entity: PCURVE
      registry.put("PCURVE", (resolver, instance) -> resolver.curveResolver.resolvePcurve(instance));

// Entity: CYLINDRICAL_SURFACE
      registry.put("CYLINDRICAL_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveCylindricalSurface(instance));

// Entity: CONICAL_SURFACE
      registry.put("CONICAL_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveConicalSurface(instance));

// Entity: TOROIDAL_SURFACE
      registry.put("TOROIDAL_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveToroidalSurface(instance));

// Entity: DEGENERATE_TOROIDAL_SURFACE
      registry.put("DEGENERATE_TOROIDAL_SURFACE", StepEntityResolver::resolveDegenerateToroidalSurface);

// Entity: SPHERICAL_SURFACE
      registry.put("SPHERICAL_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveSphericalSurface(instance));

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
      registry.put("BLENDED_SURFACE", (resolver, instance) -> resolver.geometricFeatureResolver.resolveBlendedSurface(instance));

// Entity: FREE_FORM_SURFACE
      registry.put("FREE_FORM_SURFACE", (resolver, instance) -> resolver.geometricFeatureResolver.resolveFreeFormSurface(instance));

// Entity: CURVED_TOLERANCE_ZONE
      registry.put("CURVED_TOLERANCE_ZONE", (resolver, instance) -> resolver.geometricFeatureResolver.resolveCurvedToleranceZone(instance));

// Entity: SURFACE_QUALITY
      registry.put("SURFACE_QUALITY", (resolver, instance) -> resolver.geometricFeatureResolver.resolveSurfaceQuality(instance));

// Entity: MEASUREMENT_POINT
      registry.put("MEASUREMENT_POINT", (resolver, instance) -> resolver.unitResolver.resolveMeasurementPoint(instance));

// Entity: SURFACE_MEASUREMENT
      registry.put("SURFACE_MEASUREMENT", (resolver, instance) -> resolver.unitResolver.resolveSurfaceMeasurement(instance));

// Entity: SURFACE_TEXTURE_REPRESENTATION_ITEM
      registry.put(
          "SURFACE_TEXTURE_REPRESENTATION_ITEM",
          (resolver, instance) -> resolver.representationResolver.resolveSurfaceTextureRepresentationItem(instance));

// Entity: RULED_SURFACE
      registry.put("RULED_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveRuledSurface(instance));

// Entity: SURFACE_PATCH
      registry.put("SURFACE_PATCH", (resolver, instance) -> resolver.surfaceResolver.resolveSurfacePatch(instance));

// Entity: RECTANGULAR_TRIMMED_SURFACE
      registry.put("RECTANGULAR_TRIMMED_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveRectangularTrimmedSurface(instance));

// Entity: CURVE_BOUNDED_SURFACE
      registry.put("CURVE_BOUNDED_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveCurveBoundedSurface(instance));

// Entity: ORIENTED_SURFACE
      registry.put("ORIENTED_SURFACE", (resolver, instance) -> resolver.bezierResolver.resolveOrientedSurface(instance));

// Entity: SURFACE_OF_LINEAR_EXTRUSION
      registry.put("SURFACE_OF_LINEAR_EXTRUSION", (resolver, instance) -> resolver.surfaceResolver.resolveSurfaceOfLinearExtrusion(instance));

// Entity: SURFACE_OF_REVOLUTION
      registry.put("SURFACE_OF_REVOLUTION", (resolver, instance) -> resolver.surfaceResolver.resolveSurfaceOfRevolution(instance));

// Entity: SURFACE_OF_CONSTANT_RADIUS
      registry.put("SURFACE_OF_CONSTANT_RADIUS", (resolver, instance) -> resolver.surfaceResolver.resolveSurfaceOfConstantRadius(instance));

// Entity: TRIMMED_CURVE
      registry.put("TRIMMED_CURVE", (resolver, instance) -> resolver.curveResolver.resolveTrimmedCurve(instance));

// Entity: VERTEX_POINT
      registry.put("VERTEX_POINT", (resolver, instance) -> resolver.topologyResolver.resolveVertexPoint(instance));

// Entity: EDGE_CURVE
      registry.put("EDGE_CURVE", (resolver, instance) -> resolver.topologyResolver.resolveEdgeCurve(instance));

// Entity: LINE_SEGMENT
      registry.put("LINE_SEGMENT", StepEntityResolver::resolveLineSegment);

// Entity: RECTANGULAR_COMPOSITE_SURFACE
      registry.put("RECTANGULAR_COMPOSITE_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveRectangularCompositeSurface(instance));

// Entity: COMPOSITE_CURVE_ON_SURFACE_3D
      registry.put("COMPOSITE_CURVE_ON_SURFACE_3D", (resolver, instance) -> resolver.curveResolver.resolveCompositeCurveOnSurface3D(instance));

// Entity: FACE_SURFACE
      registry.put("FACE_SURFACE", (resolver, instance) -> resolver.topologyResolver.resolveFaceSurface(instance));

// Entity: CUBIC_BEZIER_TRIANGULATED_FACE
      registry.put("CUBIC_BEZIER_TRIANGULATED_FACE", StepEntityResolver::resolveCubicBezierTriangulatedFace);

// Entity: CURVE_3D_ELEMENT_PROPERTY
      registry.put("CURVE_3D_ELEMENT_PROPERTY", (resolver, instance) -> resolver.analysisResolver.resolveCurve3dElementProperty(instance));

// Entity: SURFACE_3D_ELEMENT_PROPERTY
      registry.put("SURFACE_3D_ELEMENT_PROPERTY", (resolver, instance) -> resolver.analysisResolver.resolveSurface3dElementProperty(instance));

// Entity: FEA_LINEAR_MATERIAL
      registry.put("FEA_LINEAR_MATERIAL", (resolver, instance) -> resolver.materialResolver.resolveFeaLinearMaterial(instance));

// Entity: FEA_NON_LINEAR_MATERIAL
      registry.put("FEA_NON_LINEAR_MATERIAL", (resolver, instance) -> resolver.materialResolver.resolveFeaNonLinearMaterial(instance));

// Entity: DISPLACEMENT_BOUNDARY_CONDITION
      registry.put(
          "DISPLACEMENT_BOUNDARY_CONDITION",
          (resolver, instance) -> resolver.boundaryConditionResolver.resolveDisplacementBoundaryCondition(instance));

// Entity: SURFACE_ELEMENT
      registry.put("SURFACE_ELEMENT", (resolver, instance) -> resolver.feaElementResolver.resolveSurfaceElement(instance));

// Entity: LINE_ELEMENT
      registry.put("LINE_ELEMENT", (resolver, instance) -> resolver.feaElementResolver.resolveLineElement(instance));

// Entity: UNIFORM_SURFACE_ELEMENT
      registry.put("UNIFORM_SURFACE_ELEMENT", (resolver, instance) -> resolver.feaElementResolver.resolveUniformSurfaceElement(instance));

// Entity: FEA_LINEAR_ALGEBRAIC_MATRIX
      registry.put("FEA_LINEAR_ALGEBRAIC_MATRIX", (resolver, instance) -> resolver.analysisResolver.resolveFeaLinearAlgebraicMatrix(instance));

// Entity: FEA_LINEAR_ALGEBRAIC_VECTOR
      registry.put("FEA_LINEAR_ALGEBRAIC_VECTOR", (resolver, instance) -> resolver.analysisResolver.resolveFeaLinearAlgebraicVector(instance));

// Entity: FEA_AXIS_2_PLACEMENT_3D
      registry.put("FEA_AXIS_2_PLACEMENT_3D", (resolver, instance) -> resolver.analysisResolver.resolveFeaAxis2Placement3d(instance));

// Entity: SURFACED_OPEN_SHELL
      registry.put("SURFACED_OPEN_SHELL", (resolver, instance) -> resolver.topologyResolver.resolveSurfacedOpenShell(instance));

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
      registry.put("FEA_AXIS2_PLACEMENT_3D", (resolver, instance) -> resolver.analysisResolver.resolveFeaAxis2Placement3d(instance));

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
      registry.put("CIRCLE_2D", (resolver, instance) -> resolver.geometryResolver.resolveCircle2D(instance));

// Entity: ELLIPSE_2D
      registry.put("ELLIPSE_2D", (resolver, instance) -> resolver.geometryResolver.resolveEllipse2D(instance));

// Entity: HYPERBOLA_2D
      registry.put("HYPERBOLA_2D", StepEntityResolver::resolveHyperbola2D);

// Entity: PARABOLA_2D
      registry.put("PARABOLA_2D", StepEntityResolver::resolveParabola2D);

// Entity: LINE_2D
      registry.put("LINE_2D", (resolver, instance) -> resolver.geometryResolver.resolveLine2D(instance));

// Entity: POLYLINE_2D
      registry.put("POLYLINE_2D", (resolver, instance) -> resolver.geometryResolver.resolvePolyline2D(instance));

// Entity: TRIMMED_CURVE_2D
      registry.put("TRIMMED_CURVE_2D", StepEntityResolver::resolveTrimmedCurve2D);

// Entity: COMPOSITE_CURVE_2D
      registry.put("COMPOSITE_CURVE_2D", StepEntityResolver::resolveCompositeCurve2D);

// Entity: B_SPLINE_CURVE_2D
      registry.put("B_SPLINE_CURVE_2D", (resolver, instance) -> resolver.geometryResolver.resolveBSplineCurve2D(instance));

// Entity: RATIONAL_B_SPLINE_CURVE_2D
      registry.put("RATIONAL_B_SPLINE_CURVE_2D", (resolver, instance) -> resolver.geometryResolver.resolveRationalBSplineCurve2D(instance));

// Entity: BEZIER_CURVE_2D
      registry.put("BEZIER_CURVE_2D", (resolver, instance) -> resolver.geometryResolver.resolveBezierCurve2D(instance));

// Entity: QUASI_UNIFORM_CURVE_2D
      registry.put("QUASI_UNIFORM_CURVE_2D", (resolver, instance) -> resolver.geometryResolver.resolveQuasiUniformCurve2D(instance));

// Entity: UNIFORM_CURVE_2D
      registry.put("UNIFORM_CURVE_2D", (resolver, instance) -> resolver.geometryResolver.resolveUniformCurve2D(instance));

// Entity: PIECEWISE_BEZIER_CURVE_2D
      registry.put("PIECEWISE_BEZIER_CURVE_2D", StepEntityResolver::resolvePiecewiseBezierCurve2D);

// Entity: INDEXED_POLY_CURVE_2D
      registry.put("INDEXED_POLY_CURVE_2D", (resolver, instance) -> resolver.curveResolver.resolveIndexedPolyCurve2D(instance));

// Entity: DEGENERATE_CURVE_2D
      registry.put("DEGENERATE_CURVE_2D", (resolver, instance) -> resolver.curveResolver.resolveDegenerateCurve2D(instance));

// Entity: SURFACE_OF_TRANSLATION
      registry.put("SURFACE_OF_TRANSLATION", (resolver, instance) -> resolver.surfaceResolver.resolveSurfaceOfTranslation(instance));

// Entity: SURFACE_OF_PROJECTION
      registry.put("SURFACE_OF_PROJECTION", (resolver, instance) -> resolver.surfaceResolver.resolveSurfaceOfProjection(instance));

// Entity: PARABOLOID_SURFACE
      registry.put("PARABOLOID_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveParaboloidSurface(instance));

// Entity: HYPERBOLOID_SURFACE
      registry.put("HYPERBOLOID_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveHyperboloidSurface(instance));

// Entity: BOUNDED_CURVE_2D
      registry.put("BOUNDED_CURVE_2D", StepEntityResolver::resolveBoundedCurve2D);

// Entity: CURVE_2D
      registry.put("CURVE_2D", (resolver, instance) -> resolver.curveResolver.resolveCurve2D(instance));

// Entity: MACHINED_SURFACE
      registry.put("MACHINED_SURFACE", (resolver, instance) -> resolver.surfaceResolver.resolveMachinedSurface(instance));

// Entity: PLANE_ANGLE_UNIT_WITH_UNIT (moved to UnitRegistry for consistent ordering)
// Removed from GeometryRegistry2 to ensure proper registry order with CONVERSION_BASED_UNIT

// Entity: SWEPT_PROFILE_AREA_OUTLINE
      registry.put("SWEPT_PROFILE_AREA_OUTLINE", (resolver, instance) -> resolver.profileResolver.resolveSweptProfileAreaOutline(instance));

// Entity: INDEXED_POLYCURVE
      registry.put("INDEXED_POLYCURVE", StepEntityResolver::resolveIndexedPolycurve);

// Entity: POLYLINE_3D
      registry.put("POLYLINE_3D", StepEntityResolver::resolvePolyline3D);

// Entity: FILL_AREA_WITH_OUTLINE
      registry.put("FILL_AREA_WITH_OUTLINE", (resolver, instance) -> resolver.materialResolver.resolveFillAreaWithOutline(instance));

// Entity: CHARACTER_GLYPH_OUTLINE
      registry.put("CHARACTER_GLYPH_OUTLINE", (resolver, instance) -> resolver.draughtingResolver.resolveCharacterGlyphOutline(instance));

// Entity: CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS
      registry.put(
          "CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS",
          (resolver, instance) -> resolver.draughtingResolver.resolveCharacterGlyphOutlineWithCharacteristics(instance));

// Entity: PRE_DEFINED_SURFACE_STYLE
      registry.put("PRE_DEFINED_SURFACE_STYLE", (resolver, instance) -> resolver.materialResolver.resolvePreDefinedSurfaceStyle(instance));

// Entity: SURFACE_STYLE_PARAMETER_LINES
      registry.put("SURFACE_STYLE_PARAMETER_LINES", (resolver, instance) -> resolver.materialResolver.resolveSurfaceStyleParameterLines(instance));

// Entity: FILL_AREA_STYLE_OUTLINE
      registry.put("FILL_AREA_STYLE_OUTLINE", (resolver, instance) -> resolver.materialResolver.resolveFillAreaStyleOutline(instance));

// Entity: CURVE_STYLE_WITH_FONT
      registry.put("CURVE_STYLE_WITH_FONT", (resolver, instance) -> resolver.materialResolver.resolveCurveStyleWithFont(instance));

// Entity: LINEAR_DIMENSION_REPRESENTATION
      registry.put(
          "LINEAR_DIMENSION_REPRESENTATION",
          (resolver, instance) -> resolver.annotationResolver.resolveLinearDimensionRepresentation(instance));

// Entity: BSPLINE_CURVE_2D
      registry.put("BSPLINE_CURVE_2D", (resolver, instance) -> resolver.geometryResolver.resolveBSplineCurve2D(instance));

// Entity: RATIONAL_BSPLINE_CURVE_2D
      registry.put("RATIONAL_BSPLINE_CURVE_2D", (resolver, instance) -> resolver.geometryResolver.resolveRationalBSplineCurve2D(instance));


  }
}
