package com.minicad.step.semantic;

import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement3D;

import java.util.Map;

/**
 * Geometry registry part 1.
 */
public final class GeometryRegistry1 {

  private GeometryRegistry1() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION
      registry.put(
          "CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION", true));

// Entity: DIRECTION_SHAPE_REPRESENTATION
      registry.put(
          "DIRECTION_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DIRECTION_SHAPE_REPRESENTATION", true));

// Entity: CYLINDER_VOLUME
      registry.put("CYLINDER_VOLUME", StepEntityResolver::resolveCylinderVolume);

// Entity: TORUS_VOLUME
      registry.put("TORUS_VOLUME", StepEntityResolver::resolveTorusVolume);

// Entity: RIGHT_CIRCULAR_CYLINDER_VOLUME
      registry.put("RIGHT_CIRCULAR_CYLINDER_VOLUME", StepEntityResolver::resolveCylinderVolume);

// Entity: RIGHT_CIRCULAR_CYLINDER
      registry.put(
          "RIGHT_CIRCULAR_CYLINDER",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance,
                  "RIGHT_CIRCULAR_CYLINDER",
                  StepAxis1Placement.class,
                  "AXIS1_PLACEMENT",
                  2));

// Entity: TORUS
      registry.put(
          "TORUS",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "TORUS", StepAxis1Placement.class, "AXIS1_PLACEMENT", 2));

// Entity: CIRCLE_PROFILE_DEF
      registry.put("CIRCLE_PROFILE_DEF", StepEntityResolver::resolveCircleProfileDef);

// Entity: POINT_REPLICA
      registry.put("POINT_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "POINT_REPLICA"));

// Entity: CURVE_REPLICA
      registry.put("CURVE_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "CURVE_REPLICA"));

// Entity: SURFACE_REPLICA
      registry.put("SURFACE_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "SURFACE_REPLICA"));

// Entity: ELLIPSE_PROFILE_DEF
      registry.put(
          "ELLIPSE_PROFILE_DEF",
          (resolver, instance) -> resolver.resolveParameterizedProfileDef(
              instance, "ELLIPSE_PROFILE_DEF", 2));

// Entity: CENTRE_LINE_ARC_PROFILE_DEF
      registry.put("CENTRE_LINE_ARC_PROFILE_DEF", StepEntityResolver::resolveCentreLineArcProfileDef);

// Entity: CENTERED_CIRCLE_PROFILE_DEF
      registry.put("CENTERED_CIRCLE_PROFILE_DEF", StepEntityResolver::resolveCenteredCircleProfileDef);

// Entity: SURFACE_CURVE_SWEPT_FACE_SOLID
      registry.put("SURFACE_CURVE_SWEPT_FACE_SOLID", (resolver, instance) ->
          resolver.resolveSweptFaceSolid(instance, "SURFACE_CURVE_SWEPT_FACE_SOLID"));

// Entity: SURFACE_CURVE_SWEPT_AREA_SOLID
      registry.put("SURFACE_CURVE_SWEPT_AREA_SOLID", StepEntityResolver::resolveSurfaceCurveSweptAreaSolid);

// Entity: MANIFOLD_SURFACE_SHAPE_REPRESENTATION
      registry.put(
          "MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));

// Entity: MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION
      registry.put(
          "MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION", true));

// Entity: SURFACE_SHAPE_REPRESENTATION
      registry.put(
          "SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SURFACE_SHAPE_REPRESENTATION", true));

// Entity: GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION
      registry.put(
          "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION", true));

// Entity: POINT_PLACEMENT_SHAPE_REPRESENTATION
      registry.put(
          "POINT_PLACEMENT_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "POINT_PLACEMENT_SHAPE_REPRESENTATION", true));

// Entity: NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION
      registry.put(
          "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));

// Entity: CLOSED_CURVE_STYLE_PARAMETERS
      registry.put(
          "CLOSED_CURVE_STYLE_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CLOSED_CURVE_STYLE_PARAMETERS", false));

// Entity: CURVE_STYLE_PARAMETERS_REPRESENTATION
      registry.put(
          "CURVE_STYLE_PARAMETERS_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CURVE_STYLE_PARAMETERS_REPRESENTATION", false));

// Entity: CURVE_STYLE_PARAMETERS_WITH_ENDS
      registry.put(
          "CURVE_STYLE_PARAMETERS_WITH_ENDS",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CURVE_STYLE_PARAMETERS_WITH_ENDS", false));

// Entity: LINEAR_FLEXIBLE_LINK_REPRESENTATION
      registry.put(
          "LINEAR_FLEXIBLE_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "LINEAR_FLEXIBLE_LINK_REPRESENTATION", false));

// Entity: MACHINING_TOOL_DIRECTION_REPRESENTATION
      registry.put(
          "MACHINING_TOOL_DIRECTION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOL_DIRECTION_REPRESENTATION", false));

// Entity: MACHINING_OFFSET_VECTOR_REPRESENTATION
      registry.put(
          "MACHINING_OFFSET_VECTOR_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_OFFSET_VECTOR_REPRESENTATION", false));

// Entity: CHARACTER_GLYPH_SYMBOL_OUTLINE
      registry.put(
          "CHARACTER_GLYPH_SYMBOL_OUTLINE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL_OUTLINE", false));

// Entity: SURFACE_TEXTURE_REPRESENTATION
      registry.put(
          "SURFACE_TEXTURE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SURFACE_TEXTURE_REPRESENTATION", false));

// Entity: USER_DEFINED_CURVE_FONT
      registry.put("USER_DEFINED_CURVE_FONT", StepEntityResolver::resolveUserDefinedCurveFont);

// Entity: SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION
      registry.put(
          "SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));

// Entity: PLANE_ANGLE_MEASURE_WITH_UNIT (moved to UnitRegistry for consistent ordering)
// Removed from GeometryRegistry1 to ensure proper registry order with CONVERSION_BASED_UNIT

// Entity: PLANE_ANGLE_UNIT (moved to UnitRegistry for consistent ordering)
// Removed from GeometryRegistry1 to ensure proper registry order with CONVERSION_BASED_UNIT

// Entity: NEAR_POINT_RELATIONSHIP
      registry.put(
          "NEAR_POINT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "NEAR_POINT_RELATIONSHIP"));

// Entity: ASSEMBLY_GROUP_COMPONENT_DEFINITION_PLACEMENT_LINK
      registry.put(
          "ASSEMBLY_GROUP_COMPONENT_DEFINITION_PLACEMENT_LINK",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_GROUP_COMPONENT_DEFINITION_PLACEMENT_LINK"));

// Entity: CURVE_DISTANCE_GEOMETRIC_CONSTRAINT
      registry.put(
          "CURVE_DISTANCE_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_DISTANCE_GEOMETRIC_CONSTRAINT"));

// Entity: CURVE_LENGTH_GEOMETRIC_CONSTRAINT
      registry.put(
          "CURVE_LENGTH_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_LENGTH_GEOMETRIC_CONSTRAINT"));

// Entity: CURVE_SMOOTHNESS_GEOMETRIC_CONSTRAINT
      registry.put(
          "CURVE_SMOOTHNESS_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_SMOOTHNESS_GEOMETRIC_CONSTRAINT"));

// Entity: CONICAL_STEPPED_HOLE_TRANSITION
      registry.put(
          "CONICAL_STEPPED_HOLE_TRANSITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONICAL_STEPPED_HOLE_TRANSITION"));

// Entity: CURVE_STYLE_FONT_AND_SCALING
      registry.put(
          "CURVE_STYLE_FONT_AND_SCALING",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURVE_STYLE_FONT_AND_SCALING"));

// Entity: CURVE_STYLE_FONT_PATTERN
      registry.put(
          "CURVE_STYLE_FONT_PATTERN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURVE_STYLE_FONT_PATTERN"));

// Entity: CYLINDRICAL_POINT
      registry.put(
          "CYLINDRICAL_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_POINT"));

// Entity: ECCENTRIC_CONICAL_VOLUME
      registry.put(
          "ECCENTRIC_CONICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ECCENTRIC_CONICAL_VOLUME"));

// Entity: HIGH_DEGREE_PLANAR_SURFACE
      registry.put(
          "HIGH_DEGREE_PLANAR_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "HIGH_DEGREE_PLANAR_SURFACE"));

// Entity: IMPLICIT_PLANAR_CURVE
      registry.put(
          "IMPLICIT_PLANAR_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "IMPLICIT_PLANAR_CURVE"));

// Entity: IMPLICIT_PLANAR_INTERSECTION_POINT
      registry.put(
          "IMPLICIT_PLANAR_INTERSECTION_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_PLANAR_INTERSECTION_POINT"));

// Entity: IMPLICIT_PLANAR_PROJECTION_POINT
      registry.put(
          "IMPLICIT_PLANAR_PROJECTION_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_PLANAR_PROJECTION_POINT"));

// Entity: LINEAR_FLEXIBLE_AND_PLANAR_CURVE_PAIR
      registry.put(
          "LINEAR_FLEXIBLE_AND_PLANAR_CURVE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LINEAR_FLEXIBLE_AND_PLANAR_CURVE_PAIR"));

// Entity: PLANAR_CURVE_PAIR
      registry.put(
          "PLANAR_CURVE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PLANAR_CURVE_PAIR"));

// Entity: PLANAR_CURVE_PAIR_RANGE
      registry.put(
          "PLANAR_CURVE_PAIR_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PLANAR_CURVE_PAIR_RANGE"));

// Entity: POINT_ON_PLANAR_CURVE_PAIR
      registry.put(
          "POINT_ON_PLANAR_CURVE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "POINT_ON_PLANAR_CURVE_PAIR"));

// Entity: POINT_ON_PLANAR_CURVE_PAIR_VALUE
      registry.put(
          "POINT_ON_PLANAR_CURVE_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "POINT_ON_PLANAR_CURVE_PAIR_VALUE"));

// Entity: POINT_ON_PLANAR_CURVE_PAIR_WITH_RANGE
      registry.put(
          "POINT_ON_PLANAR_CURVE_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "POINT_ON_PLANAR_CURVE_PAIR_WITH_RANGE"));

// Entity: SOLID_WITH_CONICAL_BOTTOM_ROUND_HOLE
      registry.put(
          "SOLID_WITH_CONICAL_BOTTOM_ROUND_HOLE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_CONICAL_BOTTOM_ROUND_HOLE"));

// Entity: SOLID_WITH_SPHERICAL_BOTTOM_ROUND_HOLE
      registry.put(
          "SOLID_WITH_SPHERICAL_BOTTOM_ROUND_HOLE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_SPHERICAL_BOTTOM_ROUND_HOLE"));

// Entity: SOLID_WITH_STEPPED_ROUND_HOLE_AND_CONICAL_TRANSITIONS
      registry.put(
          "SOLID_WITH_STEPPED_ROUND_HOLE_AND_CONICAL_TRANSITIONS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_STEPPED_ROUND_HOLE_AND_CONICAL_TRANSITIONS"));

// Entity: SPHERICAL_PAIR_VALUE
      registry.put(
          "SPHERICAL_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_PAIR_VALUE"));

// Entity: SPHERICAL_PAIR_WITH_PIN
      registry.put(
          "SPHERICAL_PAIR_WITH_PIN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SPHERICAL_PAIR_WITH_PIN"));

// Entity: SPHERICAL_PAIR_WITH_PIN_AND_RANGE
      registry.put(
          "SPHERICAL_PAIR_WITH_PIN_AND_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SPHERICAL_PAIR_WITH_PIN_AND_RANGE"));

// Entity: SPHERICAL_PAIR_WITH_RANGE
      registry.put(
          "SPHERICAL_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_PAIR_WITH_RANGE"));

// Entity: SPHERICAL_POINT
      registry.put(
          "SPHERICAL_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_POINT"));

// Entity: SPHERICAL_VOLUME
      registry.put(
          "SPHERICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_VOLUME"));

// Entity: ARRAY_PLACEMENT_GROUP
      registry.put(
          "ARRAY_PLACEMENT_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ARRAY_PLACEMENT_GROUP"));

// Entity: BEZIER_VOLUME
      registry.put(
          "BEZIER_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BEZIER_VOLUME"));

// Entity: B_SPLINE_VOLUME
      registry.put(
          "B_SPLINE_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "B_SPLINE_VOLUME"));

// Entity: B_SPLINE_VOLUME_WITH_KNOTS
      registry.put(
          "B_SPLINE_VOLUME_WITH_KNOTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "B_SPLINE_VOLUME_WITH_KNOTS"));

// Entity: DIFFERENT_PLACEMENT_OF_COMPONENT
      registry.put(
          "DIFFERENT_PLACEMENT_OF_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DIFFERENT_PLACEMENT_OF_COMPONENT"));

// Entity: LINEAR_ARRAY_PLACEMENT_GROUP_COMPONENT
      registry.put(
          "LINEAR_ARRAY_PLACEMENT_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "LINEAR_ARRAY_PLACEMENT_GROUP_COMPONENT"));

// Entity: LINEAR_FLEXIBLE_AND_PINION_PAIR
      registry.put(
          "LINEAR_FLEXIBLE_AND_PINION_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LINEAR_FLEXIBLE_AND_PINION_PAIR"));

// Entity: LINEAR_PROFILE
      registry.put(
          "LINEAR_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEAR_PROFILE"));

// Entity: LOCALLY_REFINED_SPLINE_VOLUME
      registry.put(
          "LOCALLY_REFINED_SPLINE_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOCALLY_REFINED_SPLINE_VOLUME"));

// Entity: MULTIPLY_DEFINED_PLACEMENTS
      registry.put(
          "MULTIPLY_DEFINED_PLACEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "MULTIPLY_DEFINED_PLACEMENTS"));

// Entity: PLACEMENT
      registry.put(
          "PLACEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PLACEMENT"));

// Entity: RECTANGULAR_ARRAY_PLACEMENT_GROUP_COMPONENT
      registry.put(
          "RECTANGULAR_ARRAY_PLACEMENT_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RECTANGULAR_ARRAY_PLACEMENT_GROUP_COMPONENT"));

// Entity: RUNOUT_ZONE_ORIENTATION_REFERENCE_DIRECTION
      registry.put(
          "RUNOUT_ZONE_ORIENTATION_REFERENCE_DIRECTION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "RUNOUT_ZONE_ORIENTATION_REFERENCE_DIRECTION"));

// Entity: ABRUPT_CHANGE_OF_SURFACE_NORMAL
      registry.put(
          "ABRUPT_CHANGE_OF_SURFACE_NORMAL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ABRUPT_CHANGE_OF_SURFACE_NORMAL"));

// Entity: BOUNDED_PCURVE
      registry.put(
          "BOUNDED_PCURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BOUNDED_PCURVE"));

// Entity: BOUNDED_SURFACE_CURVE
      registry.put(
          "BOUNDED_SURFACE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BOUNDED_SURFACE_CURVE"));

// Entity: COMPOSITE_TEXT_WITH_ASSOCIATED_CURVES
      registry.put(
          "COMPOSITE_TEXT_WITH_ASSOCIATED_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_TEXT_WITH_ASSOCIATED_CURVES"));

// Entity: CURVE_11
      registry.put(
          "CURVE_11",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_11"));

// Entity: CURVE_BASED_PATH
      registry.put(
          "CURVE_BASED_PATH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_BASED_PATH"));

// Entity: CURVE_BASED_PATH_WITH_ORIENTATION
      registry.put(
          "CURVE_BASED_PATH_WITH_ORIENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_BASED_PATH_WITH_ORIENTATION"));

// Entity: CURVE_BASED_PATH_WITH_ORIENTATION_AND_PARAMETERS
      registry.put(
          "CURVE_BASED_PATH_WITH_ORIENTATION_AND_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_BASED_PATH_WITH_ORIENTATION_AND_PARAMETERS"));

// Entity: CURVE_DIMENSION
      registry.put(
          "CURVE_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_DIMENSION"));

// Entity: CURVE_SEGMENT_SET
      registry.put(
          "CURVE_SEGMENT_SET",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURVE_SEGMENT_SET"));

// Entity: CURVE_WITH_EXCESSIVE_SEGMENTS
      registry.put(
          "CURVE_WITH_EXCESSIVE_SEGMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_WITH_EXCESSIVE_SEGMENTS"));

// Entity: CURVE_WITH_SMALL_CURVATURE_RADIUS
      registry.put(
          "CURVE_WITH_SMALL_CURVATURE_RADIUS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_WITH_SMALL_CURVATURE_RADIUS"));

// Entity: DIFFERENT_CURVE_LENGTH
      registry.put(
          "DIFFERENT_CURVE_LENGTH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_CURVE_LENGTH"));

// Entity: DIFFERENT_SURFACE_AREA
      registry.put(
          "DIFFERENT_SURFACE_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DIFFERENT_SURFACE_AREA"));

// Entity: DIFFERENT_SURFACE_NORMAL
      registry.put(
          "DIFFERENT_SURFACE_NORMAL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_SURFACE_NORMAL"));

// Entity: DIMENSION_CURVE_TERMINATOR
      registry.put(
          "DIMENSION_CURVE_TERMINATOR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIMENSION_CURVE_TERMINATOR"));

// Entity: DUPIN_CYCLIDE_SURFACE
      registry.put(
          "DUPIN_CYCLIDE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DUPIN_CYCLIDE_SURFACE"));

// Entity: ELEMENTARY_SURFACE
      registry.put(
          "ELEMENTARY_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ELEMENTARY_SURFACE"));

// Entity: ENTIRELY_NARROW_SURFACE
      registry.put(
          "ENTIRELY_NARROW_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ENTIRELY_NARROW_SURFACE"));

// Entity: EVALUATED_DEGENERATE_PCURVE
      registry.put(
          "EVALUATED_DEGENERATE_PCURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EVALUATED_DEGENERATE_PCURVE"));

// Entity: EXCESSIVELY_HIGH_DEGREE_CURVE
      registry.put(
          "EXCESSIVELY_HIGH_DEGREE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXCESSIVELY_HIGH_DEGREE_CURVE"));

// Entity: EXCESSIVELY_HIGH_DEGREE_SURFACE
      registry.put(
          "EXCESSIVELY_HIGH_DEGREE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXCESSIVELY_HIGH_DEGREE_SURFACE"));

// Entity: FACE_SURFACE_WITH_EXCESSIVE_PATCHES_IN_ONE_DIRECTION
      registry.put(
          "FACE_SURFACE_WITH_EXCESSIVE_PATCHES_IN_ONE_DIRECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FACE_SURFACE_WITH_EXCESSIVE_PATCHES_IN_ONE_DIRECTION"));

// Entity: FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE
      registry.put(
          "FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE"));

// Entity: ANGLE_DIRECTION_REFERENCE
      registry.put(
          "ANGLE_DIRECTION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGLE_DIRECTION_REFERENCE"));

// Entity: IMPLICIT_INTERSECTION_CURVE
      registry.put(
          "IMPLICIT_INTERSECTION_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IMPLICIT_INTERSECTION_CURVE"));

// Entity: IMPLICIT_MODEL_INTERSECTION_CURVE
      registry.put(
          "IMPLICIT_MODEL_INTERSECTION_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IMPLICIT_MODEL_INTERSECTION_CURVE"));

// Entity: LINE_PROFILE_TOLERANCE
      registry.put(
          "LINE_PROFILE_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "LINE_PROFILE_TOLERANCE"));

// Entity: RULED_SURFACE_SWEPT_AREA_SOLID
      registry.put(
          "RULED_SURFACE_SWEPT_AREA_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "RULED_SURFACE_SWEPT_AREA_SOLID"));

// Entity: SMALL_AREA_SURFACE
      registry.put(
          "SMALL_AREA_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SMALL_AREA_SURFACE"));

// Entity: SMALL_AREA_SURFACE_PATCH
      registry.put(
          "SMALL_AREA_SURFACE_PATCH",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SMALL_AREA_SURFACE_PATCH"));

// Entity: SURFACE_PROFILE_TOLERANCE
      registry.put(
          "SURFACE_PROFILE_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SURFACE_PROFILE_TOLERANCE"));

// Entity: FIXED_REFERENCE_SWEPT_SURFACE
      registry.put(
          "FIXED_REFERENCE_SWEPT_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FIXED_REFERENCE_SWEPT_SURFACE"));

// Entity: G1_DISCONTINUOUS_CURVE
      registry.put(
          "G1_DISCONTINUOUS_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUOUS_CURVE"));

// Entity: G1_DISCONTINUOUS_SURFACE
      registry.put(
          "G1_DISCONTINUOUS_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUOUS_SURFACE"));

// Entity: G2_DISCONTINUOUS_CURVE
      registry.put(
          "G2_DISCONTINUOUS_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUOUS_CURVE"));

// Entity: G2_DISCONTINUOUS_SURFACE
      registry.put(
          "G2_DISCONTINUOUS_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUOUS_SURFACE"));

// Entity: GAP_BETWEEN_EDGE_AND_BASE_SURFACE
      registry.put(
          "GAP_BETWEEN_EDGE_AND_BASE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_EDGE_AND_BASE_SURFACE"));

// Entity: GAP_BETWEEN_PCURVES_RELATED_TO_AN_EDGE
      registry.put(
          "GAP_BETWEEN_PCURVES_RELATED_TO_AN_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_PCURVES_RELATED_TO_AN_EDGE"));

// Entity: GAP_BETWEEN_VERTEX_AND_BASE_SURFACE
      registry.put(
          "GAP_BETWEEN_VERTEX_AND_BASE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_VERTEX_AND_BASE_SURFACE"));

// Entity: HIGH_DEGREE_AXI_SYMMETRIC_SURFACE
      registry.put(
          "HIGH_DEGREE_AXI_SYMMETRIC_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIGH_DEGREE_AXI_SYMMETRIC_SURFACE"));

// Entity: HIGH_DEGREE_LINEAR_CURVE
      registry.put(
          "HIGH_DEGREE_LINEAR_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIGH_DEGREE_LINEAR_CURVE"));

// Entity: IMPLICIT_PROJECTED_CURVE
      registry.put(
          "IMPLICIT_PROJECTED_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_PROJECTED_CURVE"));

// Entity: IMPLICIT_SILHOUETTE_CURVE
      registry.put(
          "IMPLICIT_SILHOUETTE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_SILHOUETTE_CURVE"));

// Entity: IMPORTED_CURVE_FUNCTION
      registry.put(
          "IMPORTED_CURVE_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_CURVE_FUNCTION"));

// Entity: IMPORTED_SURFACE_FUNCTION
      registry.put(
          "IMPORTED_SURFACE_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_SURFACE_FUNCTION"));

// Entity: INCONSISTENT_CURVE_TRANSITION_CODE
      registry.put(
          "INCONSISTENT_CURVE_TRANSITION_CODE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_CURVE_TRANSITION_CODE"));

// Entity: INCONSISTENT_EDGE_AND_CURVE_DIRECTIONS
      registry.put(
          "INCONSISTENT_EDGE_AND_CURVE_DIRECTIONS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_EDGE_AND_CURVE_DIRECTIONS"));

// Entity: INCONSISTENT_FACE_AND_SURFACE_NORMALS
      registry.put(
          "INCONSISTENT_FACE_AND_SURFACE_NORMALS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_FACE_AND_SURFACE_NORMALS"));

// Entity: INCONSISTENT_SURFACE_TRANSITION_CODE
      registry.put(
          "INCONSISTENT_SURFACE_TRANSITION_CODE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_SURFACE_TRANSITION_CODE"));

// Entity: INDISTINCT_CURVE_KNOTS
      registry.put(
          "INDISTINCT_CURVE_KNOTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDISTINCT_CURVE_KNOTS"));

// Entity: INDISTINCT_SURFACE_KNOTS
      registry.put(
          "INDISTINCT_SURFACE_KNOTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDISTINCT_SURFACE_KNOTS"));

// Entity: KINEMATIC_PATH_DEFINED_BY_CURVES
      registry.put(
          "KINEMATIC_PATH_DEFINED_BY_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "KINEMATIC_PATH_DEFINED_BY_CURVES"));

// Entity: LOCALLY_REFINED_SPLINE_CURVE
      registry.put(
          "LOCALLY_REFINED_SPLINE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOCALLY_REFINED_SPLINE_CURVE"));

// Entity: LOCALLY_REFINED_SPLINE_SURFACE
      registry.put(
          "LOCALLY_REFINED_SPLINE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOCALLY_REFINED_SPLINE_SURFACE"));

// Entity: MISMATCH_OF_ARCWISE_CONNECTED_CURVES
      registry.put(
          "MISMATCH_OF_ARCWISE_CONNECTED_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_ARCWISE_CONNECTED_CURVES"));

// Entity: MISMATCH_OF_ARCWISE_CONNECTED_SURFACES
      registry.put(
          "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES"));

// Entity: MISMATCH_OF_ARCWISE_CONNECTED_SURFACES_BOUNDARY
      registry.put(
          "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES_BOUNDARY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES_BOUNDARY"));

// Entity: MULTIPLY_DEFINED_CURVES
      registry.put(
          "MULTIPLY_DEFINED_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_CURVES"));

// Entity: MULTIPLY_DEFINED_SURFACES
      registry.put(
          "MULTIPLY_DEFINED_SURFACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_SURFACES"));

// Entity: LINEAR_ARRAY_COMPONENT_DEFINITION_LINK
      registry.put(
          "LINEAR_ARRAY_COMPONENT_DEFINITION_LINK",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LINEAR_ARRAY_COMPONENT_DEFINITION_LINK"));

// Entity: AXIS1_PLACEMENT
      registry.put("AXIS1_PLACEMENT", StepEntityResolver::resolveAxis1Placement);

// Entity: AXIS2_PLACEMENT_2D
      registry.put("AXIS2_PLACEMENT_2D", StepEntityResolver::resolveAxis2Placement2D);

// Entity: AXIS2_PLACEMENT_3D
      registry.put("AXIS2_PLACEMENT_3D", StepEntityResolver::resolveAxis2Placement3D);

// Entity: COMPOSITE_CURVE_TRANSITION_LOCATOR
      registry.put(
          "COMPOSITE_CURVE_TRANSITION_LOCATOR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_CURVE_TRANSITION_LOCATOR"));
  }
}
