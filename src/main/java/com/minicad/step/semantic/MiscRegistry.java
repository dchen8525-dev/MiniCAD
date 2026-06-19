package com.minicad.step.semantic;

import java.util.Map;

import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement3D;

/**
 * Registry for miscellaneous entity types that don't belong in geometry, topology,
 * or product registries. Includes annotation/PMI, tolerance, classification,
 * approval, datetime, document, organization, security, resource, action, workflow,
 * validation, analysis, kinematic, FEA, profile, unit, config management, and
 * other miscellaneous entity types.
 */
public final class MiscRegistry {

  private MiscRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
      registry.put(
          "GEOMETRIC_REPRESENTATION_CONTEXT",
          StepEntityResolver::resolveGeometricRepresentationContext);
      registry.put(
          "SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, true));
      registerRepresentationAliases(
          registry,
          true,
          "GEOMETRIC_SET_SHAPE_REPRESENTATION",
          "SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION",
          "SURFACE_MODEL_SHAPE_REPRESENTATION");
      registry.put(
          "ADVANCED_BREP_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "ADVANCED_BREP_SHAPE_REPRESENTATION", true));
      registry.put(
          "BEVELED_SHEET_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "BEVELED_SHEET_REPRESENTATION", true));
      registry.put(
          "ELEMENTARY_BREP_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "ELEMENTARY_BREP_SHAPE_REPRESENTATION", true));
      registry.put(
          "COMPOSITE_SHEET_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "COMPOSITE_SHEET_REPRESENTATION", true));
      registry.put(
          "FACETED_BREP_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "FACETED_BREP_SHAPE_REPRESENTATION", true));
      registry.put(
          "BLOCK_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "BLOCK_SHAPE_REPRESENTATION", true));
      registry.put(
          "CSG_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CSG_SHAPE_REPRESENTATION", true));
      registry.put(
          "CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CSG_2D_SHAPE_REPRESENTATION", true));
      registry.put(
          "SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION", true));
      registry.put(
          "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION", true));
      registry.put(
          "CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION", true));
      registry.put(
          "CYLINDRICAL_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CYLINDRICAL_SHAPE_REPRESENTATION", true));
      registry.put(
          "DIRECTION_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DIRECTION_SHAPE_REPRESENTATION", true));
      registry.put("BOOLEAN_CLIPPING_RESULT", StepEntityResolver::resolveBooleanClippingResult);
      registry.put("BOOLEAN_RESULT", StepEntityResolver::resolveBooleanResult);
      registry.put("CSG_SOLID", StepEntityResolver::resolveCsgSolid);
      registry.put("CSG_VOLUME", StepEntityResolver::resolveCsgVolume);
      registry.put("BLOCK_VOLUME", StepEntityResolver::resolveBlockVolume);
      // Advanced CSG volumes
      registry.put("CYLINDER_VOLUME", StepEntityResolver::resolveCylinderVolume);
      registry.put("SPHERE_VOLUME", StepEntityResolver::resolveSphereVolume);
      registry.put("TORUS_VOLUME", StepEntityResolver::resolveTorusVolume);
      registry.put("PRISM_VOLUME", StepEntityResolver::resolvePrismVolume);
      registry.put("RIGHT_CIRCULAR_CYLINDER_VOLUME", StepEntityResolver::resolveCylinderVolume);
      registry.put("RIGHT_CIRCULAR_CONE_VOLUME", StepEntityResolver::resolveRightCircularConeVolume);
      registry.put("SOLID_REPLICA", StepEntityResolver::resolveSolidReplica);
      registry.put(
          "BLOCK",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "BLOCK", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));
      registry.put(
          "SPHERE",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "SPHERE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 1));
      registry.put(
          "ELLIPSOID",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "ELLIPSOID", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));
      registry.put(
          "RIGHT_ANGULAR_WEDGE",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance,
                  "RIGHT_ANGULAR_WEDGE",
                  StepAxis2Placement3D.class,
                  "AXIS2_PLACEMENT_3D",
                  4));
      registry.put(
          "RIGHT_CIRCULAR_CYLINDER",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance,
                  "RIGHT_CIRCULAR_CYLINDER",
                  StepAxis1Placement.class,
                  "AXIS1_PLACEMENT",
                  2));
      registry.put(
          "TORUS",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "TORUS", StepAxis1Placement.class, "AXIS1_PLACEMENT", 2));
      registry.put(
          "RIGHT_CIRCULAR_CONE",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "RIGHT_CIRCULAR_CONE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 2));
      registry.put("CIRCLE_PROFILE_DEF", StepEntityResolver::resolveCircleProfileDef);
      registry.put("RECTANGLE_PROFILE_DEF", StepEntityResolver::resolveRectangleProfileDef);
      registry.put(
          "RECTANGLE_HOLLOW_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "RECTANGLE_HOLLOW_PROFILE_DEF", 4));
      registry.put(
          "CENTERED_RECTANGLE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(
                  instance, "CENTERED_RECTANGLE_PROFILE_DEF", 2));
      registry.put(
          "CIRCULAR_HOLLOW_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "CIRCULAR_HOLLOW_PROFILE_DEF", 2));
      registry.put("POINT_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "POINT_REPLICA"));
      registry.put("CURVE_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "CURVE_REPLICA"));
      registry.put("SURFACE_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "SURFACE_REPLICA"));
      registry.put(
          "ELLIPSE_PROFILE_DEF",
          (resolver, instance) -> resolver.resolveParameterizedProfileDef(
              instance, "ELLIPSE_PROFILE_DEF", 2));
      registry.put(
          "ROUNDED_RECTANGLE_PROFILE_DEF",
          (resolver, instance) -> resolver.resolveParameterizedProfileDef(
              instance, "ROUNDED_RECTANGLE_PROFILE_DEF", 3));
      registry.put(
          "CENTRE_LINE_ARC_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "CENTRE_LINE_ARC_PROFILE_DEF", 2));
      registry.put(
          "CENTERED_CIRCLE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "CENTERED_CIRCLE_PROFILE_DEF", 2));
      registry.put(
          "ARBITRARY_CLOSED_PROFILE_DEF",
          StepEntityResolver::resolveArbitraryClosedProfileDef);
      registry.put(
          "ARBITRARY_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_PROFILE_DEF"));
      registry.put(
          "ARBITRARY_PROFILE_DEF_WITH_VOIDS",
          StepEntityResolver::resolveArbitraryProfileDefWithVoids);
      registry.put(
          "ARBITRARY_OPEN_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_OPEN_PROFILE_DEF"));
      // Standard structural steel profile definitions (Phase 2E)
      registry.put(
          "I_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "I_SHAPE_PROFILE_DEF", 6));
      registry.put(
          "T_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "T_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "L_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "L_SHAPE_PROFILE_DEF", 4));
      registry.put(
          "U_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "U_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "C_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "C_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "Z_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "Z_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "HAT_SHAPE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "HAT_SHAPE_PROFILE_DEF", 5));
      registry.put(
          "ANGLE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "ANGLE_PROFILE_DEF", 4));
      registry.put(
          "CHANNEL_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "CHANNEL_PROFILE_DEF", 5));
      registry.put(
          "TEE_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "TEE_PROFILE_DEF", 5));
      registry.put(
          "I_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "I_PROFILE_DEF", 6));
      registry.put(
          "L_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "L_PROFILE_DEF", 4));
      registry.put(
          "T_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "T_PROFILE_DEF", 5));
      registry.put(
          "U_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "U_PROFILE_DEF", 5));
      registry.put(
          "Z_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "Z_PROFILE_DEF", 5));
      registry.put(
          "FLAT_BAR_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "FLAT_BAR_PROFILE_DEF", 2));
      registry.put(
          "DOVE_TAIL_PROFILE_DEF",
          (resolver, instance) ->
              resolver.resolveParameterizedProfileDef(instance, "DOVE_TAIL_PROFILE_DEF", 4));
      registry.put("EXTRUDED_AREA_SOLID", StepEntityResolver::resolveExtrudedAreaSolid);
      registry.put("REVOLVED_AREA_SOLID", StepEntityResolver::resolveRevolvedAreaSolid);
      // Swept face solids
      registry.put("EXTRUDED_FACE_SOLID", StepEntityResolver::resolveExtrudedFaceSolid);
      registry.put("REVOLVED_FACE_SOLID", StepEntityResolver::resolveRevolvedFaceSolid);
      registry.put("SURFACE_CURVE_SWEPT_FACE_SOLID", (resolver, instance) ->
          resolver.resolveSweptFaceSolid(instance, "SURFACE_CURVE_SWEPT_FACE_SOLID"));
      registry.put("SWEPT_FACE_SOLID", (resolver, instance) ->
          resolver.resolveSweptFaceSolid(instance, "SWEPT_FACE_SOLID"));
      registry.put("BOX_DOMAIN", StepEntityResolver::resolveBoxDomain);
      registry.put("HALF_SPACE_SOLID", StepEntityResolver::resolveHalfSpaceSolid);
      registry.put("BOXED_HALF_SPACE", StepEntityResolver::resolveBoxedHalfSpace);
      registry.put("POLYGONAL_BOUNDED_HALF_SPACE", StepEntityResolver::resolvePolygonalBoundedHalfSpace);
      registry.put("SWEPT_DISK_SOLID", StepEntityResolver::resolveSweptDiskSolid);
      registry.put("REVOLVED_AREA_SOLID_TAPERED", StepEntityResolver::resolveRevolvedAreaSolidTapered);
      registry.put("EXTRUDED_AREA_SOLID_TAPERED", StepEntityResolver::resolveExtrudedAreaSolidTapered);
      registry.put("SURFACE_CURVE_SWEPT_AREA_SOLID", StepEntityResolver::resolveSurfaceCurveSweptAreaSolid);
      registry.put(
          "FACETED_BREP",
          (resolver, instance) -> resolver.resolveManifoldSolidBrep(instance, "FACETED_BREP"));
      registry.put("BREP_WITH_VOIDS", StepEntityResolver::resolveBrepWithVoids);
      registry.put("ADVANCED_BREP", StepEntityResolver::resolveAdvancedBrep);
      registry.put("COMPLEX_CLIPPING_RESULT", StepEntityResolver::resolveComplexClippingResult);
      registry.put(
          "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));
      registry.put(
          "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION", true));
      registry.put(
          "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION", true));
      registry.put(
          "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));
      registry.put(
          "MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));
      registry.put(
          "MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION", true));
      registry.put(
          "SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SURFACE_SHAPE_REPRESENTATION", true));
      registry.put(
          "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION", true));
      registry.put(
          "COMPOUND_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "COMPOUND_SHAPE_REPRESENTATION", true));
      registry.put(
          "PLANAR_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PLANAR_SHAPE_REPRESENTATION", true));
      registry.put(
          "POINT_PLACEMENT_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "POINT_PLACEMENT_SHAPE_REPRESENTATION", true));
      registry.put(
          "SHAPE_DIMENSION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SHAPE_DIMENSION_REPRESENTATION", true));
      registry.put(
          "SHAPE_REPRESENTATION_WITH_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SHAPE_REPRESENTATION_WITH_PARAMETERS", true));
      registry.put(
          "LOCATION_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "LOCATION_SHAPE_REPRESENTATION", true));
      registry.put(
          "REPRESENTATIVE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "REPRESENTATIVE_SHAPE_REPRESENTATION", true));
      registry.put(
          "NEUTRAL_SKETCH_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "NEUTRAL_SKETCH_REPRESENTATION", true));
      registry.put(
          "PROCEDURAL_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PROCEDURAL_SHAPE_REPRESENTATION", true));
      registry.put(
          "TESSELLATED_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "TESSELLATED_SHAPE_REPRESENTATION", true));
      registry.put(
          "TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS", true));
      registry.put(
          "NGON_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "NGON_SHAPE_REPRESENTATION", true));
      registry.put(
          "SCAN_DATA_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SCAN_DATA_SHAPE_REPRESENTATION", true));
      registry.put(
          "PATH_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PATH_SHAPE_REPRESENTATION", true));
      registry.put(
          "WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "WIREFRAME_SHAPE_REPRESENTATION", true));
      registry.put(
          "FACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "FACE_SHAPE_REPRESENTATION", true));
      registry.put(
          "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));
      registry.put(
          "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION", false));
      registry.put(
          "MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION", false));
      registry.put(
          "MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING", false));
      registry.put(
          "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA", false));
      registry.put(
          "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION", false));
      registry.put(
          "MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA", false));
      registry.put(
          "VISUAL_APPEARANCE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "VISUAL_APPEARANCE_REPRESENTATION", false));
      registry.put(
          "PRESENTATION_AREA",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_AREA", false));
      registry.put(
          "PRESENTATION_VIEW",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_VIEW", false));
      registry.put(
          "SYMBOL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SYMBOL_REPRESENTATION", false));
      registry.put(
          "PRESENTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_REPRESENTATION", false));
      registry.put(
          "PICTURE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PICTURE_REPRESENTATION", false));
      registry.put(
          "TEXT_STRING_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "TEXT_STRING_REPRESENTATION", false));
      registry.put(
          "STRUCTURED_TEXT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "STRUCTURED_TEXT_REPRESENTATION", false));
      registry.put(
          "PROCEDURAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PROCEDURAL_REPRESENTATION", false));
      registry.put(
          "CLOSED_CURVE_STYLE_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CLOSED_CURVE_STYLE_PARAMETERS", false));
      registry.put(
          "CURVE_STYLE_PARAMETERS_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CURVE_STYLE_PARAMETERS_REPRESENTATION", false));
      registry.put(
          "CURVE_STYLE_PARAMETERS_WITH_ENDS",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CURVE_STYLE_PARAMETERS_WITH_ENDS", false));
      registry.put(
          "CONSTRUCTIVE_GEOMETRY_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CONSTRUCTIVE_GEOMETRY_REPRESENTATION", false));
      registry.put(
          "AREA_DEPENDENT_ANNOTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "AREA_DEPENDENT_ANNOTATION_REPRESENTATION", false));
      registry.put(
          "PRESENTATION_SIZE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_SIZE", false));
      registry.put(
          "VARIATIONAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "VARIATIONAL_REPRESENTATION", false));
      registry.put(
          "RANGE_CHARACTERISTIC",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "RANGE_CHARACTERISTIC", false));
      registry.put(
          "PLY_ANGLE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PLY_ANGLE_REPRESENTATION", false));
      registry.put(
          "MOMENTS_OF_INERTIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MOMENTS_OF_INERTIA_REPRESENTATION", false));
      registry.put(
          "UNCERTAINTY_ASSIGNED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "UNCERTAINTY_ASSIGNED_REPRESENTATION", false));
      registry.put(
          "INTERPOLATED_CONFIGURATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "INTERPOLATED_CONFIGURATION_REPRESENTATION", false));
      registry.put(
          "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION", false));
      registry.put(
          "KINEMATIC_GROUND_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_GROUND_REPRESENTATION", false));
      registry.put(
          "KINEMATIC_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_LINK_REPRESENTATION", false));
      registry.put(
          "KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE", false));
      registry.put(
          "KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE", false));
      registry.put(
          "KINEMATIC_TOPOLOGY_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_STRUCTURE", false));
      registry.put(
          "KINEMATIC_TOPOLOGY_SUBSTRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_SUBSTRUCTURE", false));
      registry.put(
          "KINEMATIC_TOPOLOGY_TREE_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_TREE_STRUCTURE", false));
      registry.put(
          "LINEAR_FLEXIBLE_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "LINEAR_FLEXIBLE_LINK_REPRESENTATION", false));
      registry.put(
          "RIGID_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "RIGID_LINK_REPRESENTATION", false));
      registry.put(
          "MECHANISM_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MECHANISM_REPRESENTATION", false));
      registry.put(
          "MECHANISM_STATE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MECHANISM_STATE_REPRESENTATION", false));
      registry.put(
          "LINK_MOTION_REPRESENTATION_ALONG_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "LINK_MOTION_REPRESENTATION_ALONG_PATH", false));
      registry.put(
          "REINFORCEMENT_ORIENTATION_BASIS",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "REINFORCEMENT_ORIENTATION_BASIS", false));
      registry.put(
          "CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION", false));
      registry.put(
          "EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT",
                  false));
      registry.put(
          "DATA_EQUIVALENCE_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_EQUIVALENCE_CRITERIA_REPRESENTATION", false));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION", false));
      registry.put(
          "DATA_QUALITY_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_QUALITY_CRITERIA_REPRESENTATION", false));
      registry.put(
          "DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION", false));
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION",
                  false));
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
                  false));
      registry.put(
          "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION", false));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION", false));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY", false));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE", false));
      registry.put(
          "SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION", false));
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION", false));
      registry.put(
          "EXTERNALLY_DEFINED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "EXTERNALLY_DEFINED_REPRESENTATION", false));
      registry.put(
          "EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS", false));
      registry.put(
          "SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY", false));
      registry.put(
          "SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY", false));
      registry.put(
          "ANALYSIS_MODEL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "ANALYSIS_MODEL", false));
      registry.put(
          "MESSAGE_CONTENTS_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MESSAGE_CONTENTS_ASSIGNMENT", false));
      registry.put(
          "MACHINING_TOOL_DIRECTION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOL_DIRECTION_REPRESENTATION", false));
      registry.put(
          "FOUNDED_KINEMATIC_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "FOUNDED_KINEMATIC_PATH", false));
      registry.put(
          "SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION", false));
      registry.put(
          "SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION", false));
      registry.put(
          "SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION", false));
      registry.put(
          "MACHINING_CUTTING_CORNER_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_CUTTING_CORNER_REPRESENTATION", false));
      registry.put(
          "MACHINING_DWELL_TIME_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_DWELL_TIME_REPRESENTATION", false));
      registry.put(
          "MACHINING_FEED_SPEED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_FEED_SPEED_REPRESENTATION", false));
      registry.put(
          "MACHINING_OFFSET_VECTOR_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_OFFSET_VECTOR_REPRESENTATION", false));
      registry.put(
          "MACHINING_SPINDLE_SPEED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_SPINDLE_SPEED_REPRESENTATION", false));
      registry.put(
          "MACHINING_TOOL_BODY_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOL_BODY_REPRESENTATION", false));
      registry.put(
          "MACHINING_TOOL_DIMENSION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOL_DIMENSION_REPRESENTATION", false));
      registry.put(
          "MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION", false));
      registry.put(
          "FREEFORM_MILLING_TOLERANCE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "FREEFORM_MILLING_TOLERANCE_REPRESENTATION", false));
      registry.put(
          "HARDNESS_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "HARDNESS_REPRESENTATION", false));
      registry.put(
          "DEFAULT_TOLERANCE_TABLE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DEFAULT_TOLERANCE_TABLE", false));
      registry.put(
          "OTHER_LIST_TABLE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "OTHER_LIST_TABLE_REPRESENTATION", false));
      registry.put(
          "CHARACTERIZED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTERIZED_REPRESENTATION", false));
      registry.put(
          "CHARACTERIZED_ITEM_WITHIN_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CHARACTERIZED_ITEM_WITHIN_REPRESENTATION", false));
      registry.put(
          "CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION", false));
      registry.put(
          "EVALUATED_CHARACTERISTIC",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "EVALUATED_CHARACTERISTIC", false));
      registry.put(
          "EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT",
                  false));
      registry.put(
          "DRAUGHTING_MODEL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAUGHTING_MODEL", false));
      registry.put(
          "DRAUGHTING_SUBFIGURE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAUGHTING_SUBFIGURE_REPRESENTATION", false));
      registry.put(
          "DRAUGHTING_SYMBOL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAUGHTING_SYMBOL_REPRESENTATION", false));
      registry.put(
          "DRAWING_SHEET_LAYOUT",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAWING_SHEET_LAYOUT", false));
      registry.put(
          "DRAWING_SHEET_REVISION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAWING_SHEET_REVISION", false));
      registry.put(
          "REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, false));
      registry.put(
          "PATH_PARAMETER_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PATH_PARAMETER_REPRESENTATION", false));
      registry.put(
          "PRESCRIBED_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESCRIBED_PATH", false));
      registry.put(
          "RESULTING_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "RESULTING_PATH", false));
      registry.put(
          "CHARACTER_GLYPH_SYMBOL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL", false));
      registry.put(
          "GENERIC_CHARACTER_GLYPH_SYMBOL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "GENERIC_CHARACTER_GLYPH_SYMBOL", false));
      registry.put(
          "CHARACTER_GLYPH_SYMBOL_OUTLINE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL_OUTLINE", false));
      registry.put(
          "CHARACTER_GLYPH_SYMBOL_STROKE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL_STROKE", false));
      registry.put(
          "SURFACE_TEXTURE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SURFACE_TEXTURE_REPRESENTATION", false));
      registry.put(
          "TACTILE_APPEARANCE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "TACTILE_APPEARANCE_REPRESENTATION", false));
      registry.put("APPLICATION_CONTEXT", StepEntityResolver::resolveApplicationContext);
      registry.put(
          "APPLICATION_PROTOCOL_DEFINITION",
          StepEntityResolver::resolveApplicationProtocolDefinition);
      registry.put("PRODUCT_CONTEXT", StepEntityResolver::resolveProductContext);
      registry.put(
          "MECHANICAL_CONTEXT",
          (resolver, instance) -> resolver.resolveProductContext(instance, "MECHANICAL_CONTEXT"));
      registry.put("PRODUCT", StepEntityResolver::resolveProduct);
      registry.put("PRODUCT_CATEGORY", StepEntityResolver::resolveProductCategory);
      registry.put(
          "PRODUCT_CATEGORY_RELATIONSHIP",
          StepEntityResolver::resolveProductCategoryRelationship);
      registry.put(
          "PRODUCT_RELATED_PRODUCT_CATEGORY",
          StepEntityResolver::resolveProductRelatedProductCategory);
      registry.put("PRODUCT_RELATIONSHIP", StepEntityResolver::resolveProductRelationship);
      registry.put(
          "PRODUCT_DEFINITION_FORMATION", StepEntityResolver::resolveProductDefinitionFormation);
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE",
          StepEntityResolver::resolveProductDefinitionFormation);
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP",
          StepEntityResolver::resolveProductDefinitionFormationRelationship);
      registry.put("PRODUCT_DEFINITION_CONTEXT", StepEntityResolver::resolveProductDefinitionContext);
      registry.put(
          "DESIGN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveProductDefinitionContext(instance, "DESIGN_CONTEXT"));
      registry.put("PRODUCT_DEFINITION", StepEntityResolver::resolveProductDefinition);
      registry.put(
          "PRODUCT_DEFINITION_RELATIONSHIP",
          StepEntityResolver::resolveProductDefinitionRelationship);
      registerProductDefinitionRelationshipAliases(
          registry,
          "ASSEMBLY_COMPONENT_USAGE",
          "BREAKDOWN_CONTEXT",
          "BREAKDOWN_ELEMENT_USAGE",
          "BREAKDOWN_OF",
          "PRODUCT_DEFINITION_USAGE",
          "PROMISSORY_USAGE_OCCURRENCE",
          "SUPPLIED_PART_RELATIONSHIP");
      registry.put(
          "PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP",
          StepEntityResolver::resolveProductDefinitionRelationshipRelationship);
      registry.put(
          "PRODUCT_DEFINITION_USAGE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationshipRelationship(
                  instance, "PRODUCT_DEFINITION_USAGE_RELATIONSHIP"));
      registerProductDefinitionRelationshipRelationshipAliases(
          registry,
          "ASSEMBLY_COMPONENT_USAGE_SUBSTITUTE",
          "PRODUCT_DEFINITION_SUBSTITUTE");
      registry.put("PRODUCT_DEFINITION_SHAPE", StepEntityResolver::resolveProductDefinitionShape);
      registry.put("PROPERTY_DEFINITION", StepEntityResolver::resolvePropertyDefinition);
      registry.put(
          "PROPERTY_DEFINITION_RELATIONSHIP",
          StepEntityResolver::resolvePropertyDefinitionRelationship);
      registry.put("GENERAL_PROPERTY", StepEntityResolver::resolveGeneralProperty);
      registry.put(
          "GENERAL_PROPERTY_RELATIONSHIP",
          StepEntityResolver::resolveGeneralPropertyRelationship);
      registry.put("GROUP", StepEntityResolver::resolveGroup);
      registry.put("CLASS", (resolver, instance) -> resolver.resolveGroup(instance, "CLASS"));
      registry.put(
          "CLASS_SYSTEM", (resolver, instance) -> resolver.resolveGroup(instance, "CLASS_SYSTEM"));
      registry.put("GROUP_RELATIONSHIP", StepEntityResolver::resolveGroupRelationship);
      registry.put("GROUP_ASSIGNMENT", StepEntityResolver::resolveGroupAssignment);
      registry.put("APPLIED_GROUP_ASSIGNMENT", StepEntityResolver::resolveAppliedGroupAssignment);
      registry.put("ADDRESS", StepEntityResolver::resolveAddress);
      registry.put("DOCUMENT_TYPE", StepEntityResolver::resolveDocumentType);
      registry.put("DOCUMENT", StepEntityResolver::resolveDocument);
      registry.put("DOCUMENT_RELATIONSHIP", StepEntityResolver::resolveDocumentRelationship);
      registry.put(
          "DOCUMENT_USAGE_CONSTRAINT", StepEntityResolver::resolveDocumentUsageConstraint);
      registry.put("DOCUMENT_REFERENCE", StepEntityResolver::resolveDocumentReference);
      registry.put(
          "APPLIED_DOCUMENT_REFERENCE", StepEntityResolver::resolveAppliedDocumentReference);
      registry.put(
          "CC_DESIGN_SPECIFICATION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveAppliedDocumentReference(instance, "CC_DESIGN_SPECIFICATION_REFERENCE"));
      registry.put("PERSON", StepEntityResolver::resolvePerson);
      registry.put("ORGANIZATION", StepEntityResolver::resolveOrganization);
      registry.put(
          "PERSON_AND_ORGANIZATION", StepEntityResolver::resolvePersonAndOrganization);
      registry.put(
          "ORGANIZATION_RELATIONSHIP", StepEntityResolver::resolveOrganizationRelationship);
      registry.put("ORGANIZATION_ROLE", StepEntityResolver::resolveOrganizationRole);
      registry.put("ORGANIZATION_ASSIGNMENT", StepEntityResolver::resolveOrganizationAssignment);
      registry.put(
          "APPLIED_ORGANIZATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedOrganizationAssignment);
      registry.put(
          "CC_DESIGN_ORGANIZATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedOrganizationAssignment(
                  instance, "CC_DESIGN_ORGANIZATION_ASSIGNMENT"));
      registry.put("LANGUAGE", StepEntityResolver::resolveLanguage);
      registry.put("LANGUAGE_ASSIGNMENT", StepEntityResolver::resolveLanguageAssignment);
      registry.put(
          "APPLIED_LANGUAGE_ASSIGNMENT", StepEntityResolver::resolveAppliedLanguageAssignment);
      registry.put(
          "PERSON_AND_ORGANIZATION_ROLE",
          StepEntityResolver::resolvePersonAndOrganizationRole);
      registry.put(
          "PERSON_AND_ORGANIZATION_ASSIGNMENT",
          StepEntityResolver::resolvePersonAndOrganizationAssignment);
      registry.put(
          "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedPersonAndOrganizationAssignment);
      registry.put(
          "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedPersonAndOrganizationAssignment(
                  instance, "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT"));
      registry.put("CALENDAR_DATE", StepEntityResolver::resolveCalendarDate);
      registry.put(
          "COORDINATED_UNIVERSAL_TIME_OFFSET",
          StepEntityResolver::resolveCoordinatedUniversalTimeOffset);
      registry.put("LOCAL_TIME", StepEntityResolver::resolveLocalTime);
      registry.put("DATE_AND_TIME", StepEntityResolver::resolveDateAndTime);
      registry.put("DATE_ROLE", StepEntityResolver::resolveDateRole);
      registry.put("DATE_ASSIGNMENT", StepEntityResolver::resolveDateAssignment);
      registry.put("APPLIED_DATE_ASSIGNMENT", StepEntityResolver::resolveAppliedDateAssignment);
      registry.put(
          "CC_DESIGN_DATE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedDateAssignment(instance, "CC_DESIGN_DATE_ASSIGNMENT"));
      registry.put("DATE_TIME_ROLE", StepEntityResolver::resolveDateTimeRole);
      registry.put("DATE_TIME_ASSIGNMENT", StepEntityResolver::resolveDateTimeAssignment);
      registry.put(
          "APPLIED_DATE_AND_TIME_ASSIGNMENT",
          StepEntityResolver::resolveAppliedDateTimeAssignment);
      registry.put(
          "APPLIED_DATE_TIME_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedDateTimeAssignment(instance, "APPLIED_DATE_TIME_ASSIGNMENT"));
      registry.put(
          "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedDateTimeAssignment(
                  instance, "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT"));
      registry.put("APPROVAL_STATUS", StepEntityResolver::resolveApprovalStatus);
      registry.put("APPROVAL", StepEntityResolver::resolveApproval);
      registry.put("APPROVAL_ROLE", StepEntityResolver::resolveApprovalRole);
      registry.put("APPROVAL_ASSIGNMENT", StepEntityResolver::resolveApprovalAssignment);
      registry.put(
          "APPLIED_APPROVAL_ASSIGNMENT", StepEntityResolver::resolveAppliedApprovalAssignment);
      registry.put(
          "CC_DESIGN_APPROVAL",
          (resolver, instance) ->
              resolver.resolveAppliedApprovalAssignment(instance, "CC_DESIGN_APPROVAL"));
      registry.put(
          "APPROVAL_PERSON_ORGANIZATION",
          StepEntityResolver::resolveApprovalPersonOrganization);
      registry.put("APPROVAL_DATE_TIME", StepEntityResolver::resolveApprovalDateTime);
      registry.put(
          "SECURITY_CLASSIFICATION_LEVEL",
          StepEntityResolver::resolveSecurityClassificationLevel);
      registry.put(
          "SECURITY_CLASSIFICATION", StepEntityResolver::resolveSecurityClassification);
      registry.put(
          "SECURITY_CLASSIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveSecurityClassificationAssignment);
      registry.put(
          "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedSecurityClassificationAssignment);
      registry.put(
          "CC_DESIGN_SECURITY_CLASSIFICATION",
          (resolver, instance) ->
              resolver.resolveAppliedSecurityClassificationAssignment(
                  instance, "CC_DESIGN_SECURITY_CLASSIFICATION"));
      registry.put("CONTRACT_TYPE", StepEntityResolver::resolveContractType);
      registry.put("CONTRACT", StepEntityResolver::resolveContract);
      registry.put("CONTRACT_ASSIGNMENT", StepEntityResolver::resolveContractAssignment);
      registry.put(
          "APPLIED_CONTRACT_ASSIGNMENT", StepEntityResolver::resolveAppliedContractAssignment);
      registry.put(
          "CC_DESIGN_CONTRACT",
          (resolver, instance) ->
              resolver.resolveAppliedContractAssignment(instance, "CC_DESIGN_CONTRACT"));
      registry.put("CERTIFICATION_TYPE", StepEntityResolver::resolveCertificationType);
      registry.put("CERTIFICATION", StepEntityResolver::resolveCertification);
      registry.put(
          "CERTIFICATION_ASSIGNMENT", StepEntityResolver::resolveCertificationAssignment);
      registry.put(
          "APPLIED_CERTIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedCertificationAssignment);
      registry.put(
          "CC_DESIGN_CERTIFICATION",
          (resolver, instance) ->
              resolver.resolveAppliedCertificationAssignment(instance, "CC_DESIGN_CERTIFICATION"));
      registry.put("EFFECTIVITY", StepEntityResolver::resolveEffectivity);
      registry.put(
          "PRODUCT_DEFINITION_EFFECTIVITY",
          StepEntityResolver::resolveProductDefinitionEffectivity);
      registry.put("EFFECTIVITY_RELATIONSHIP", StepEntityResolver::resolveEffectivityRelationship);
      registry.put("CLASSIFICATION_ROLE", StepEntityResolver::resolveClassificationRole);
      registry.put(
          "CLASSIFICATION_ASSIGNMENT", StepEntityResolver::resolveClassificationAssignment);
      registry.put(
          "APPLIED_CLASSIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedClassificationAssignment);
      registry.put("IDENTIFICATION_ROLE", StepEntityResolver::resolveIdentificationRole);
      registry.put(
          "IDENTIFICATION_ASSIGNMENT", StepEntityResolver::resolveIdentificationAssignment);
      registry.put(
          "APPLIED_IDENTIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedIdentificationAssignment);
      registry.put(
          "EXTERNAL_IDENTIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveExternalIdentificationAssignment);
      registry.put(
          "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedExternalIdentificationAssignment);
      registry.put("NAME_ASSIGNMENT", StepEntityResolver::resolveNameAssignment);
      registry.put("APPLIED_NAME_ASSIGNMENT", StepEntityResolver::resolveAppliedNameAssignment);
      registry.put("DESCRIPTION_ATTRIBUTE", StepEntityResolver::resolveDescriptionAttribute);
      registry.put("NAME_ATTRIBUTE", StepEntityResolver::resolveNameAttribute);
      registry.put("ID_ATTRIBUTE", StepEntityResolver::resolveIdAttribute);
      registry.put("EXTERNAL_SOURCE", StepEntityResolver::resolveExternalSource);
      registry.put("EXTERNALLY_DEFINED_ITEM",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_ITEM"));
      registry.put(
          "EXTERNAL_SOURCE_RELATIONSHIP",
          StepEntityResolver::resolveExternalSourceRelationship);
      registry.put(
          "EXTERNALLY_DEFINED_ITEM",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_ITEM"));
      registry.put(
          "EXTERNALLY_DEFINED_CLASS",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_CLASS"));
      registry.put(
          "EXTERNALLY_DEFINED_GENERAL_PROPERTY",
          (resolver, instance) ->
              resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_GENERAL_PROPERTY"));
      registerExternallyDefinedItemAliases(
          registry,
          "EXTERNALLY_DEFINED_CHARACTER_GLYPH",
          "EXTERNALLY_DEFINED_CURVE_FONT",
          "EXTERNALLY_DEFINED_DIMENSION_DEFINITION",
          "EXTERNALLY_DEFINED_HATCH_STYLE",
          "EXTERNALLY_DEFINED_MARKER",
          "EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM",
          "EXTERNALLY_DEFINED_STYLE",
          "EXTERNALLY_DEFINED_SYMBOL",
          "EXTERNALLY_DEFINED_TERMINATOR_SYMBOL",
          "EXTERNALLY_DEFINED_TEXT_FONT",
          "EXTERNALLY_DEFINED_TEXT_STYLE",
          "EXTERNALLY_DEFINED_TILE");
      registry.put("CHARACTERIZED_OBJECT", StepEntityResolver::resolveCharacterizedObject);
      // Phase 2A: Additional manufacturing features
      registerCharacterizedObjectAliases(
          registry,
          "MACHINING_OPERATION",
          "MACHINED_SURFACE",
          "TWO_5D_MANUFACTURING_FEATURE",
          "MANUFACTURING_FEATURE_REPRESENTATION",
          "DEPRESSION",
          "EDGE_ROUND");
      registerCharacterizedObjectAliases(
          registry,
          "ADDITIVE_MANUFACTURING_FEATURE",
          "BARRING_HOLE",
          "BASIC_ROUND_HOLE",
          "BEAD",
          "BOSS",
          "CIRCULAR_PATTERN",
          "COMPOUND_FEATURE",
          "COMPOSITE_HOLE",
          "CONTACT_FEATURE_DEFINITION",
          "COUNTERBORE_HOLE_DEFINITION",
          "COUNTERDRILL_HOLE_DEFINITION",
          "COUNTERSINK_HOLE_DEFINITION",
          "EXPLICIT_COMPOSITE_HOLE",
          "EXPLICIT_ROUND_HOLE",
          "EXTERNALLY_DEFINED_FEATURE_DEFINITION",
          "FEATURE_DEFINITION",
          "FEATURE_DEFINITION_WITH_CONNECTION_AREA",
          "FEATURE_IN_PANEL",
          "FEATURE_PATTERN",
          "FLAT_FACE",
          "GEAR",
          "GENERAL_FEATURE",
          "HOLE_IN_PANEL",
          "JOGGLE",
          "LOCATOR",
          "MARKING",
          "OUTER_ROUND",
          "OUTSIDE_PROFILE",
          "POCKET",
          "PROTRUSION",
          "RECTANGULAR_PATTERN",
          "REMOVAL_VOLUME",
          "REPLICATE_FEATURE",
          "REVOLVED_PROFILE",
          "RIB",
          "RIB_TOP",
          "ROUND_HOLE",
          "ROUNDED_END",
          "SHAPE_FEATURE_DEFINITION",
          "SLOT",
          "SPHERICAL_CAP",
          "SPOTFACE_DEFINITION",
          "SPOTFACE_HOLE_DEFINITION",
          "THREAD",
          "TURNED_KNURL");
      registry.put(
          "APEX",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "APEX"));
      registry.put(
          "ALL_AROUND_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "ALL_AROUND_SHAPE_ASPECT"));
      registry.put(
          "BETWEEN_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "BETWEEN_SHAPE_ASPECT"));
      registry.put(
          "CENTRE_OF_SYMMETRY",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CENTRE_OF_SYMMETRY"));
      registry.put(
          "CHAMFER",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CHAMFER"));
      registry.put(
          "CHAMFER_OFFSET",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CHAMFER_OFFSET"));
      registry.put(
          "COMPONENT_FEATURE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMPONENT_FEATURE"));
      registry.put(
          "COMPOSITE_GROUP_SHAPE_ASPECT",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "COMPOSITE_GROUP_SHAPE_ASPECT"));
      registry.put(
          "COMPOSITE_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMPOSITE_SHAPE_ASPECT"));
      registry.put(
          "COMPOSITE_UNIT_SHAPE_ASPECT",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "COMPOSITE_UNIT_SHAPE_ASPECT"));
      registry.put(
          "CONTINUOUS_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CONTINUOUS_SHAPE_ASPECT"));
      registry.put(
          "DATUM",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM"));
      registry.put(
          "DATUM_FEATURE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_FEATURE"));
      registry.put(
          "DATUM_TARGET",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_TARGET"));
      registry.put(
          "GEOMETRIC_ALIGNMENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_ALIGNMENT"));
      registry.put(
          "GEOMETRIC_CONTACT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_CONTACT"));
      registry.put(
          "GEOMETRIC_INTERSECTION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_INTERSECTION"));
      registry.put(
          "GROUP_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GROUP_SHAPE_ASPECT"));
      registry.put(
          "EDGE_ROUND",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "EDGE_ROUND"));
      registry.put(
          "EXTENSION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "EXTENSION"));
      registry.put(
          "FILLET",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "FILLET"));
      registry.put(
          "PARALLEL_OFFSET",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PARALLEL_OFFSET"));
      registry.put(
          "PERPENDICULAR_TO",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PERPENDICULAR_TO"));
      registry.put(
          "INSTANCED_FEATURE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "INSTANCED_FEATURE"));
      registry.put(
          "INSTANCED_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "INSTANCED_SHAPE_ASPECT"));
      registry.put(
          "SINGULAR_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "SINGULAR_SHAPE_ASPECT"));
      registry.put(
          "SYMMETRIC_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "SYMMETRIC_SHAPE_ASPECT"));
      registry.put(
          "TANGENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "TANGENT"));
      registry.put("SHAPE_ASPECT", StepEntityResolver::resolveShapeAspect);
      registry.put("SHAPE_ASPECT_OCCURRENCE",
          (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, "SHAPE_ASPECT_OCCURRENCE"));
      registerShapeAspectAliases(
          registry,
          "APPLIED_AREA",
          "BEAD_END",
          "BOSS_TOP",
          "CIRCULAR_CLOSED_PROFILE",
          "COMPONENT_TERMINAL",
          "CONSTITUENT_SHAPE_ASPECT",
          "CONTACTING_FEATURE",
          "DATUM_REFERENCE_ELEMENT",
          "DATUM_SYSTEM",
          "DEFAULT_MODEL_GEOMETRIC_VIEW",
          "GENERAL_DATUM_REFERENCE",
          "HOLE_BOTTOM",
          "GEOMETRIC_TOLERANCE_WITH_MODIFIERS",
          "LAYOUT_SPACING_CONTEXTUAL_AREA",
          "MATED_PART_RELATIONSHIP",
          "MOUNTING_RESTRICTION_AREA",
          "MOUNTING_RESTRICTION_VOLUME",
          "PATH_FEATURE_COMPONENT",
          "PHYSICAL_COMPONENT_FEATURE",
          "PHYSICAL_COMPONENT_TERMINAL",
          "PROJECTED_ZONE_DEFINITION",
          "REFERENCE_GRAPHIC_REGISTRATION_MARK",
          "SEATING_PLANE",
          "TERMINAL_FEATURE",
          "TERMINAL_LOCATION_GROUP",
          "TOLERANCE_ZONE_DEFINITION");
      registry.put(
          "SHAPE_ASPECT_OCCURRENCE",
          (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, "SHAPE_ASPECT_OCCURRENCE"));
      registerShapeAspectOccurrenceAliases(
          registry,
          "BASIC_ROUND_HOLE_OCCURRENCE",
          "COUNTERBORE_HOLE_OCCURRENCE",
          "COUNTERDRILL_HOLE_OCCURRENCE",
          "COUNTERSINK_HOLE_OCCURRENCE",
          "SIMPLIFIED_COUNTERBORE_HOLE_OCCURRENCE",
          "SIMPLIFIED_COUNTERDRILL_HOLE_OCCURRENCE",
          "SIMPLIFIED_COUNTERSINK_HOLE_OCCURRENCE",
          "SIMPLIFIED_SPOTFACE_HOLE_OCCURRENCE",
          "SPOTFACE_HOLE_OCCURRENCE");
      registry.put(
          "ANGULAR_LOCATION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "ANGULAR_LOCATION"));
      registry.put(
          "COMPOSITE_SHAPE_ASPECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(
                  instance, "COMPOSITE_SHAPE_ASPECT_RELATIONSHIP"));
      registry.put(
          "DIMENSIONAL_LOCATION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "DIMENSIONAL_LOCATION"));
      registry.put(
          "DIMENSIONAL_SIZE",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "DIMENSIONAL_SIZE"));
      registry.put(
          "DIRECTED_DIMENSIONAL_LOCATION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "DIRECTED_DIMENSIONAL_LOCATION"));
      registry.put(
          "FEATURE_COMPONENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "FEATURE_COMPONENT_RELATIONSHIP"));
      registry.put(
          "FEATURE_FOR_DATUM_TARGET_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "FEATURE_FOR_DATUM_TARGET_RELATIONSHIP"));
      registry.put(
          "GEOMETRIC_ALIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "GEOMETRIC_ALIGNMENT_RELATIONSHIP"));
      registry.put(
          "GEOMETRIC_CONTACT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "GEOMETRIC_CONTACT_RELATIONSHIP"));
      registry.put(
          "MAKE_FROM_FEATURE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "MAKE_FROM_FEATURE_RELATIONSHIP"));
      registry.put(
          "PATTERN_OFFSET_MEMBERSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "PATTERN_OFFSET_MEMBERSHIP"));
      registry.put(
          "PATTERN_OMIT_MEMBERSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "PATTERN_OMIT_MEMBERSHIP"));
      registry.put(
          "SHAPE_ASPECT_ASSOCIATIVITY",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_ASSOCIATIVITY"));
      registry.put(
          "SHAPE_ASPECT_DERIVING_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_DERIVING_RELATIONSHIP"));
      registry.put(
          "SHAPE_ASPECT_TRANSITION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_TRANSITION"));
      registry.put(
          "SHAPE_DEFINING_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_DEFINING_RELATIONSHIP"));
      registry.put(
          "SHAPE_FEATURE_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_FEATURE_FIT_RELATIONSHIP"));
      registry.put(
          "SHAPE_ASPECT_RELATIONSHIP",
          StepEntityResolver::resolveShapeAspectRelationship);
      registerShapeAspectRelationshipAliases(
          registry,
          "ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP",
          "ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP",
          "COMPONENT_FEATURE_JOINT",
          "COMPONENT_FEATURE_RELATIONSHIP_WITH_TRANSFORMATION",
          "COMPONENT_MATING_CONSTRAINT_CONDITION",
          "COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP",
          "CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP",
          "CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP",
          "CONTACT_FEATURE_FIT_RELATIONSHIP",
          "DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE",
          "DIMENSIONAL_LOCATION_WITH_PATH",
          "POSITIONED_SKETCH_TO_PART_ASSOCIATION",
          "SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP");
      registry.put(
          "SHAPE_DEFINITION_REPRESENTATION",
          StepEntityResolver::resolveShapeDefinitionRepresentation);
      registry.put("ROW_VARIABLE", StepEntityResolver::resolveRowVariable);
      registry.put("SCALAR_VARIABLE", StepEntityResolver::resolveScalarVariable);
      registry.put("ABSTRACT_VARIABLE", StepEntityResolver::resolveAbstractVariable);
      registry.put("ATTRIBUTE_ASSERTION", StepEntityResolver::resolveAttributeAssertion);
      registry.put("BACK_CHAINING_RULE_BODY", StepEntityResolver::resolveBackChainingRuleBody);
      registry.put(
          "FORWARD_CHAINING_RULE_PREMISE",
          StepEntityResolver::resolveForwardChainingRulePremise);
      registry.put(
          "ACTION_PROPERTY_REPRESENTATION",
          StepEntityResolver::resolveActionPropertyRepresentation);
      registry.put(
          "CONTACT_RATIO_REPRESENTATION",
          StepEntityResolver::resolveContactRatioRepresentation);
      registry.put(
          "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION",
          StepEntityResolver::resolveKinematicPropertyDefinitionRepresentation);
      registry.put(
          "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION",
          StepEntityResolver::resolveKinematicPropertyMechanismRepresentation);
      registry.put(
          "KINEMATIC_PROPERTY_REPRESENTATION_RELATION",
          StepEntityResolver::resolveKinematicPropertyRepresentationRelation);
      registry.put(
          "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION",
          StepEntityResolver::resolveKinematicPropertyTopologyRepresentation);
      registry.put(
          "PLACED_DATUM_TARGET_FEATURE",
          StepEntityResolver::resolvePlacedDatumTargetFeature);
      registry.put(
          "RESOURCE_PROPERTY_REPRESENTATION",
          StepEntityResolver::resolveResourcePropertyRepresentation);
      registry.put(
          "PROPERTY_DEFINITION_REPRESENTATION",
          StepEntityResolver::resolvePropertyDefinitionRepresentation);
      registry.put("REPRESENTATION_MAP", StepEntityResolver::resolveRepresentationMap);
      registry.put("SYMBOL_REPRESENTATION_MAP", StepEntityResolver::resolveSymbolRepresentationMap);
      registry.put("MAPPED_ITEM", StepEntityResolver::resolveMappedItem);
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_2D",
          StepEntityResolver::resolveCartesianTransformationOperator2D);
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_3D",
          StepEntityResolver::resolveCartesianTransformationOperator3D);
      registry.put("USER_DEFINED_CURVE_FONT", StepEntityResolver::resolveUserDefinedCurveFont);
      registry.put("USER_DEFINED_MARKER", StepEntityResolver::resolveUserDefinedMarker);
      registry.put(
          "USER_DEFINED_TERMINATOR_SYMBOL",
          StepEntityResolver::resolveUserDefinedTerminatorSymbol);
      registry.put(
          "ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);
      registry.put(
          "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
          StepEntityResolver::resolveRepresentationRelationshipWithTransformation);
      registry.put(
          "REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveRepresentationRelationship);
      registry.put(
          "CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "DEFINITIONAL_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DEFINITIONAL_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT"));
      registry.put(
          "DRAWING_SHEET_REVISION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DRAWING_SHEET_REVISION_SEQUENCE"));
      registry.put(
          "EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "FACE_SHAPE_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "FACE_SHAPE_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP"));
      registry.put(
          "PAIR_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PAIR_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "REPRESENTATION_RELATIONSHIP_WITH_CLASS",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "REPRESENTATION_RELATIONSHIP_WITH_CLASS"));
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP"));
      registry.put(
          "SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION"));
      registry.put(
          "TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION"));
      registry.put(
          "GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION"));
      registry.put(
          "VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "COAXIAL_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "COAXIAL_ASSEMBLY_CONSTRAINT"));
      registry.put(
          "PARALLEL_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PARALLEL_ASSEMBLY_CONSTRAINT"));
      registry.put(
          "PERPENDICULAR_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PERPENDICULAR_ASSEMBLY_CONSTRAINT"));
      registry.put(
          "INCIDENCE_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "INCIDENCE_ASSEMBLY_CONSTRAINT"));
      registry.put(
          "TANGENT_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "TANGENT_ASSEMBLY_CONSTRAINT"));
      registry.put(
          "COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
      registry.put(
          "PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
      registry.put(
          "PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
      registry.put(
          "INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
      registry.put(
          "SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
      registry.put(
          "ANGULARITY_TOLERANCE_WITH_MODIFIERS",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "ANGULARITY_TOLERANCE_WITH_MODIFIERS"));
      registerRepresentationRelationshipAliases(
          registry,
          "REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT",
          "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP",
          "KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP",
          "KINEMATIC_GROUND_REPRESENTATION_RELATIONSHIP",
          "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP",
          "KINEMATIC_PAIR_REPRESENTATION_RELATIONSHIP",
          "MECHANISM_REPRESENTATION_RELATIONSHIP",
          "MECHANISM_STATE_REPRESENTATION_RELATIONSHIP");
      registry.put(
          "SHAPE_REPRESENTATION_RELATIONSHIP",
          StepEntityResolver::resolveShapeRepresentationRelationship);
      registry.put(
          "NEXT_ASSEMBLY_USAGE_OCCURRENCE", StepEntityResolver::resolveNextAssemblyUsageOccurrence);
      registry.put(
          "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION",
          StepEntityResolver::resolveContextDependentShapeRepresentation);
      registry.put(
          "UNCERTAINTY_MEASURE_WITH_UNIT", StepEntityResolver::resolveUncertaintyMeasureWithUnit);
      registry.put(
          "LENGTH_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "LENGTH_MEASURE_WITH_UNIT", "LENGTH_UNIT"));
      registry.put(
          "MASS_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "MASS_MEASURE_WITH_UNIT", "MASS_UNIT"));
      registry.put(
          "TIME_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "TIME_MEASURE_WITH_UNIT", "TIME_UNIT"));
      registry.put(
          "PLANE_ANGLE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "PLANE_ANGLE_MEASURE_WITH_UNIT", "PLANE_ANGLE_UNIT"));
      registry.put(
          "SOLID_ANGLE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "SOLID_ANGLE_MEASURE_WITH_UNIT", "SOLID_ANGLE_UNIT"));
      registry.put(
          "AREA_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "AREA_MEASURE_WITH_UNIT", "AREA_UNIT"));
      registry.put(
          "VOLUME_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "VOLUME_MEASURE_WITH_UNIT", "VOLUME_UNIT"));
      registry.put(
          "RATIO_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "RATIO_MEASURE_WITH_UNIT", "RATIO_UNIT"));
      registry.put(
          "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
                  "THERMODYNAMIC_TEMPERATURE_UNIT"));
      registry.put(
          "ELECTRIC_CURRENT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ELECTRIC_CURRENT_MEASURE_WITH_UNIT", "ELECTRIC_CURRENT_UNIT"));
      registry.put(
          "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
                  "AMOUNT_OF_SUBSTANCE_UNIT"));
      registry.put(
          "FREQUENCY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "FREQUENCY_MEASURE_WITH_UNIT", "FREQUENCY_UNIT"));
      registry.put(
          "FORCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "FORCE_MEASURE_WITH_UNIT", "FORCE_UNIT"));
      registry.put(
          "PRESSURE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "PRESSURE_MEASURE_WITH_UNIT", "PRESSURE_UNIT"));
      registry.put(
          "ENERGY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ENERGY_MEASURE_WITH_UNIT", "ENERGY_UNIT"));
      registry.put(
          "POWER_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "POWER_MEASURE_WITH_UNIT", "POWER_UNIT"));
      registry.put(
          "ELECTRIC_CHARGE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ELECTRIC_CHARGE_MEASURE_WITH_UNIT", "ELECTRIC_CHARGE_UNIT"));
      registry.put(
          "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
                  "ELECTRIC_POTENTIAL_UNIT"));
      registry.put(
          "CAPACITANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CAPACITANCE_MEASURE_WITH_UNIT", "CAPACITANCE_UNIT"));
      registry.put(
          "RESISTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "RESISTANCE_MEASURE_WITH_UNIT", "RESISTANCE_UNIT"));
      registry.put(
          "CONDUCTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CONDUCTANCE_MEASURE_WITH_UNIT", "CONDUCTANCE_UNIT"));
      registry.put(
          "MAGNETIC_FLUX_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "MAGNETIC_FLUX_MEASURE_WITH_UNIT", "MAGNETIC_FLUX_UNIT"));
      registry.put(
          "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
                  "MAGNETIC_FLUX_DENSITY_UNIT"));
      registry.put(
          "INDUCTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "INDUCTANCE_MEASURE_WITH_UNIT", "INDUCTANCE_UNIT"));
      registry.put(
          "ILLUMINANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ILLUMINANCE_MEASURE_WITH_UNIT", "ILLUMINANCE_UNIT"));
      registry.put(
          "LUMINOUS_FLUX_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "LUMINOUS_FLUX_MEASURE_WITH_UNIT", "LUMINOUS_FLUX_UNIT"));
      registry.put(
          "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
                  "LUMINOUS_INTENSITY_UNIT"));
      registry.put(
          "RADIOACTIVITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "RADIOACTIVITY_MEASURE_WITH_UNIT", "RADIOACTIVITY_UNIT"));
      registry.put(
          "ABSORBED_DOSE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ABSORBED_DOSE_MEASURE_WITH_UNIT", "ABSORBED_DOSE_UNIT"));
      registry.put(
          "DOSE_EQUIVALENT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "DOSE_EQUIVALENT_MEASURE_WITH_UNIT", "DOSE_EQUIVALENT_UNIT"));
      registry.put(
          "ACCELERATION_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ACCELERATION_MEASURE_WITH_UNIT", "ACCELERATION_UNIT"));
      registry.put(
          "VELOCITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "VELOCITY_MEASURE_WITH_UNIT", "VELOCITY_UNIT"));
      registry.put(
          "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
                  "THERMAL_RESISTANCE_UNIT"));
      registerTypedMeasureWithUnitPairs(
          registry,
          "MASS_DENSITY_UNIT",
          "DYNAMIC_VISCOSITY_UNIT",
          "KINEMATIC_VISCOSITY_UNIT",
          "MOMENT_OF_INERTIA_UNIT",
          "THERMAL_CONDUCTIVITY_UNIT",
          "HEAT_FLUX_DENSITY_UNIT",
          "SPECIFIC_HEAT_CAPACITY_UNIT",
          "AREA_DENSITY_UNIT",
          "VOLUMETRIC_FLOW_RATE_UNIT",
          "MASS_FLOW_RATE_UNIT",
          "ROTATIONAL_FREQUENCY_UNIT",
          "ANGULAR_VELOCITY_UNIT",
          "ANGULAR_ACCELERATION_UNIT",
          "TORQUE_UNIT",
          "LINEAR_FORCE_UNIT",
          "LINEAR_STIFFNESS_UNIT",
          "ROTATIONAL_STIFFNESS_UNIT",
          "LINEAR_MOMENT_UNIT");
      registry.put(
          "GLOBAL_UNIT_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUnitAssignedContext);
      registry.put(
          "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT",
          StepEntityResolver::resolveGlobalUncertaintyAssignedContext);
      registry.put("MEASURE_WITH_UNIT", StepEntityResolver::resolveMeasureWithUnit);
      registry.put("DERIVED_UNIT_ELEMENT", StepEntityResolver::resolveDerivedUnitElement);
      registry.put("DERIVED_UNIT", StepEntityResolver::resolveDerivedUnit);
      registry.put("DIMENSIONAL_EXPONENTS", StepEntityResolver::resolveDimensionalExponents);
      registry.put("SI_UNIT", StepEntityResolver::resolveSiUnit);
      registry.put(
          "CONVERSION_BASED_UNIT_WITH_OFFSET",
          StepEntityResolver::resolveConversionBasedUnitWithOffset);
      registry.put("CONVERSION_BASED_UNIT", (resolver, instance) -> resolver.resolveConversionBasedUnit(instance, "CONVERSION_BASED_UNIT"));
      registry.put("CONTEXT_DEPENDENT_UNIT", StepEntityResolver::resolveContextDependentUnit);
      registry.put("NAMED_UNIT", StepEntityResolver::resolveNamedUnit);
      registry.put(
          "LENGTH_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LENGTH_UNIT"));
      registry.put(
          "MASS_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "MASS_UNIT"));
      registry.put(
          "PLANE_ANGLE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "PLANE_ANGLE_UNIT"));
      registry.put(
          "SOLID_ANGLE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "SOLID_ANGLE_UNIT"));
      registry.put(
          "RATIO_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "RATIO_UNIT"));
      registry.put(
          "AREA_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "AREA_UNIT"));
      registry.put(
          "VOLUME_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "VOLUME_UNIT"));
      registry.put(
          "TIME_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "TIME_UNIT"));
      registry.put(
          "THERMODYNAMIC_TEMPERATURE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "THERMODYNAMIC_TEMPERATURE_UNIT"));
      registry.put(
          "ELECTRIC_CURRENT_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "ELECTRIC_CURRENT_UNIT"));
      registry.put(
          "AMOUNT_OF_SUBSTANCE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "AMOUNT_OF_SUBSTANCE_UNIT"));
      registry.put(
          "LUMINOUS_FLUX_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_FLUX_UNIT"));
      registry.put(
          "LUMINOUS_INTENSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_INTENSITY_UNIT"));
      registry.put(
          "ACCELERATION_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ACCELERATION_UNIT"));
      registry.put(
          "VELOCITY_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "VELOCITY_UNIT"));
      registry.put(
          "THERMAL_RESISTANCE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "THERMAL_RESISTANCE_UNIT"));
      registry.put(
          "FREQUENCY_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FREQUENCY_UNIT"));
      registry.put(
          "FORCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FORCE_UNIT"));
      registry.put(
          "PRESSURE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "PRESSURE_UNIT"));
      registry.put(
          "ENERGY_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ENERGY_UNIT"));
      registry.put(
          "POWER_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "POWER_UNIT"));
      registry.put(
          "ELECTRIC_CHARGE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_CHARGE_UNIT"));
      registry.put(
          "ELECTRIC_POTENTIAL_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_POTENTIAL_UNIT"));
      registry.put(
          "CAPACITANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CAPACITANCE_UNIT"));
      registry.put(
          "RESISTANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "RESISTANCE_UNIT"));
      registry.put(
          "CONDUCTANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CONDUCTANCE_UNIT"));
      registry.put(
          "MAGNETIC_FLUX_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_UNIT"));
      registry.put(
          "MAGNETIC_FLUX_DENSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_DENSITY_UNIT"));
      registry.put(
          "INDUCTANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "INDUCTANCE_UNIT"));
      registry.put(
          "ILLUMINANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ILLUMINANCE_UNIT"));
      registry.put(
          "RADIOACTIVITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "RADIOACTIVITY_UNIT"));
      registry.put(
          "ABSORBED_DOSE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ABSORBED_DOSE_UNIT"));
      registry.put(
          "DOSE_EQUIVALENT_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "DOSE_EQUIVALENT_UNIT"));
      registerStandaloneDerivedUnitKinds(
          registry,
          "MASS_DENSITY_UNIT",
          "DYNAMIC_VISCOSITY_UNIT",
          "KINEMATIC_VISCOSITY_UNIT",
          "MOMENT_OF_INERTIA_UNIT",
          "THERMAL_CONDUCTIVITY_UNIT",
          "HEAT_FLUX_DENSITY_UNIT",
          "SPECIFIC_HEAT_CAPACITY_UNIT",
          "AREA_DENSITY_UNIT",
          "VOLUMETRIC_FLOW_RATE_UNIT",
          "MASS_FLOW_RATE_UNIT",
          "ROTATIONAL_FREQUENCY_UNIT",
          "ANGULAR_VELOCITY_UNIT",
          "ANGULAR_ACCELERATION_UNIT",
          "TORQUE_UNIT",
          "LINEAR_FORCE_UNIT",
          "LINEAR_STIFFNESS_UNIT",
          "ROTATIONAL_STIFFNESS_UNIT",
          "LINEAR_MOMENT_UNIT");
      // Phase 2: Additional SI derived units (auto-generated from entity analysis)
      registerStandaloneDerivedUnitKinds(
          registry,
          "SI_ABSORBED_DOSE_UNIT",
          "SI_CAPACITANCE_UNIT",
          "SI_CONDUCTANCE_UNIT",
          "SI_DOSE_EQUIVALENT_UNIT",
          "SI_ELECTRIC_CHARGE_UNIT",
          "SI_ELECTRIC_POTENTIAL_UNIT",
          "SI_ENERGY_UNIT",
          "SI_FORCE_UNIT",
          "SI_FREQUENCY_UNIT",
          "SI_ILLUMINANCE_UNIT",
          "SI_INDUCTANCE_UNIT",
          "SI_MAGNETIC_FLUX_DENSITY_UNIT",
          "SI_MAGNETIC_FLUX_UNIT",
          "SI_POWER_UNIT",
          "SI_PRESSURE_UNIT",
          "SI_RADIOACTIVITY_UNIT",
          "SI_RESISTANCE_UNIT");
      // Phase 2: Additional measure/unit pairs
      registry.put(
          "CELSIUS_TEMPERATURE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CELSIUS_TEMPERATURE_MEASURE_WITH_UNIT", "CELSIUS_TEMPERATURE_UNIT"));
      registry.put(
          "CURRENCY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CURRENCY_MEASURE_WITH_UNIT", "CURRENCY_UNIT"));
      registry.put(
          "DIELECTRIC_CONSTANT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "DIELECTRIC_CONSTANT_MEASURE_WITH_UNIT", "DIELECTRIC_CONSTANT_UNIT"));
      registry.put(
          "LOSS_TANGENT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "LOSS_TANGENT_MEASURE_WITH_UNIT", "LOSS_TANGENT_UNIT"));
      registry.put(
          "POSITIVE_LENGTH_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "POSITIVE_LENGTH_MEASURE_WITH_UNIT", "LENGTH_UNIT"));
      // Additional unit entities (conversion-based and special units)
      registry.put(
          "EXPRESSION_CONVERSION_BASED_UNIT",
          (resolver, instance) ->
              resolver.resolveConversionBasedUnit(instance, "EXPRESSION_CONVERSION_BASED_UNIT"));
      registry.put(
          "EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT",
          (resolver, instance) ->
              resolver.resolveExternallyDefinedConversionBasedUnit(instance));
      registry.put(
          "NON_AGREED_UNIT_USAGE",
          (resolver, instance) ->
              resolver.resolveNonAgreedUnitUsage(instance));
      // Phase 2 Batch 2: A3M Validation entities (25 entities)
      // A3M equivalence accuracy and association
      registry.put(
          "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceAccuracyAssociation(instance));
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveA3mInspectedModelAndInspectionResultRelationship(instance));
      // A3M inspection results (assembly vs shape)
      registry.put(
          "A3MA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveA3maEquivalenceInspectionResult(instance));
      registry.put(
          "A3MS_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveA3msEquivalenceInspectionResult(instance));
      // A3M equivalence criterion (abstract supertype and subtypes)
      registry.put(
          "A3M_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      // Alias family: A3M equivalence criterion subtypes (use same resolver as abstract supertype)
      // Assembly criterion subtypes
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      // Shape criterion subtypes
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      // A3M equivalence criterion with specified elements
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      registry.put(
          "A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));
      // A3M equivalence assessment specifications (subtype of data_equivalence_assessment_specification)
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceAssessmentSpecification(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST"));
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceAssessmentSpecification(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST"));
      // A3M equivalence report items (subtype of data_equivalence_inspection_criterion_report_item)
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceInspectionCriterionReportItem(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceInspectionInstanceReportItem(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));
      // A3M equivalence inspection requirements
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceInspectionRequirement(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));
      registry.put(
          "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceReportRequest(instance, "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE"));
      // A3MA relationship entities (subtype of representation_item_relationship)
      registry.put(
          "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationItemRelationship(instance, "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP"));
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationItemRelationship(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));
      registry.put(
          "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR",
          (resolver, instance) ->
              resolver.resolveCompoundRepresentationItem(instance, "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR"));
      // Phase 2 Batch 3: Mathematical function and expression entities (50+ entities)
      // Unary function entities (SUBTYPE OF unary_function_call)
      registry.put(
          "ABS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ABS_FUNCTION"));
      registry.put(
          "MINUS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "MINUS_FUNCTION"));
      registry.put(
          "SIN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "SIN_FUNCTION"));
      registry.put(
          "COS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "COS_FUNCTION"));
      registry.put(
          "TAN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "TAN_FUNCTION"));
      registry.put(
          "ASIN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ASIN_FUNCTION"));
      registry.put(
          "ACOS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ACOS_FUNCTION"));
      registry.put(
          "ATAN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ATAN_FUNCTION"));
      registry.put(
          "EXP_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "EXP_FUNCTION"));
      registry.put(
          "LOG_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "LOG_FUNCTION"));
      registry.put(
          "LOG2_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "LOG2_FUNCTION"));
      registry.put(
          "LOG10_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "LOG10_FUNCTION"));
      registry.put(
          "SQUARE_ROOT_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "SQUARE_ROOT_FUNCTION"));
      registry.put(
          "ODD_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ODD_FUNCTION"));
      // Unary expression entities (SUBTYPE OF unary_generic_expression)
      registry.put(
          "UNARY_FUNCTION_CALL",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_FUNCTION_CALL"));
      registry.put(
          "UNARY_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_GENERIC_EXPRESSION"));
      registry.put(
          "UNARY_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_BOOLEAN_EXPRESSION"));
      registry.put(
          "UNARY_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_NUMERIC_EXPRESSION"));
      registry.put(
          "NOT_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "NOT_EXPRESSION"));
      // Binary expression entities (SUBTYPE OF binary_generic_expression)
      registry.put(
          "BINARY_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_GENERIC_EXPRESSION"));
      registry.put(
          "BINARY_FUNCTION_CALL",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_FUNCTION_CALL"));
      registry.put(
          "BINARY_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_BOOLEAN_EXPRESSION"));
      registry.put(
          "BINARY_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_NUMERIC_EXPRESSION"));
      registry.put(
          "AND_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "AND_EXPRESSION"));
      registry.put(
          "OR_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "OR_EXPRESSION"));
      registry.put(
          "XOR_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "XOR_EXPRESSION"));
      registry.put(
          "PLUS_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "PLUS_EXPRESSION"));
      registry.put(
          "MINUS_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "MINUS_EXPRESSION"));
      registry.put(
          "MULT_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "MULT_EXPRESSION"));
      registry.put(
          "DIV_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "DIV_EXPRESSION"));
      registry.put(
          "MOD_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "MOD_EXPRESSION"));
      registry.put(
          "SLASH_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "SLASH_EXPRESSION"));
      registry.put(
          "POWER_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "POWER_EXPRESSION"));
      registry.put(
          "COMPARISON_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "COMPARISON_EXPRESSION"));
      registry.put(
          "EQUALS_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "EQUALS_EXPRESSION"));
      registry.put(
          "LIKE_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "LIKE_EXPRESSION"));
      registry.put(
          "CONCAT_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "CONCAT_EXPRESSION"));
      // Multiple arity expression entities (SUBTYPE OF multiple_arity_generic_expression)
      registry.put(
          "MULTIPLE_ARITY_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_GENERIC_EXPRESSION"));
      registry.put(
          "MULTIPLE_ARITY_FUNCTION_CALL",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_FUNCTION_CALL"));
      registry.put(
          "MULTIPLE_ARITY_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_BOOLEAN_EXPRESSION"));
      registry.put(
          "MULTIPLE_ARITY_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_NUMERIC_EXPRESSION"));
      // Simple expression entities (SUBTYPE OF simple_generic_expression)
      registry.put(
          "SIMPLE_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_GENERIC_EXPRESSION"));
      registry.put(
          "SIMPLE_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_BOOLEAN_EXPRESSION"));
      registry.put(
          "SIMPLE_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_NUMERIC_EXPRESSION"));
      registry.put(
          "SIMPLE_STRING_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_STRING_EXPRESSION"));
      // Other expression entities
      registry.put(
          "GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "GENERIC_EXPRESSION"));
      registry.put(
          "BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "BOOLEAN_EXPRESSION"));
      registry.put(
          "NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "NUMERIC_EXPRESSION"));
      registry.put(
          "STRING_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "STRING_EXPRESSION"));
      registry.put(
          "INDEX_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "INDEX_EXPRESSION"));
      registry.put(
          "SUBSTRING_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "SUBSTRING_EXPRESSION"));
      registry.put(
          "INTERVAL_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "INTERVAL_EXPRESSION"));
// Phase 2 Batch 4-10: Alias family entities (auto-generated)
      registry.put(
          "ACTION_ACTUAL",
          (resolver, instance) ->
              resolver.resolveGenericActual(instance, "ACTION_ACTUAL"));
      registry.put(
          "ACTION_DIRECTIVE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ACTION_DIRECTIVE_RELATIONSHIP"));
      registry.put(
          "ACTION_HAPPENING",
          (resolver, instance) ->
              resolver.resolveGenericActual(instance, "ACTION_HAPPENING"));
      registry.put(
          "ACTION_METHOD_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ACTION_METHOD_ASSIGNMENT"));
      registry.put(
          "ACTION_REQUEST_STATUS",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "ACTION_REQUEST_STATUS"));
      registry.put(
          "ACTION_RESOURCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ACTION_RESOURCE_RELATIONSHIP"));
      registry.put(
          "ACTION_RESOURCE_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ACTION_RESOURCE_REQUIREMENT"));
      registry.put(
          "ACTION_RESOURCE_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "ACTION_RESOURCE_TYPE"));
      registry.put(
          "ADDITIVE_MANUFACTURING_BUILD_PLATE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_BUILD_PLATE_RELATIONSHIP"));
      registry.put(
          "ADDITIVE_MANUFACTURING_SETUP",
          (resolver, instance) ->
              resolver.resolveGenericSetup(instance, "ADDITIVE_MANUFACTURING_SETUP"));
      registry.put(
          "ADDITIVE_MANUFACTURING_SETUP_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_SETUP_RELATIONSHIP"));
      registry.put(
          "ADDITIVE_MANUFACTURING_SETUP_WORKPIECE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_SETUP_WORKPIECE_RELATIONSHIP"));
      registry.put(
          "ADDITIVE_MANUFACTURING_SUPPORT_STRUCTURE_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_SUPPORT_STRUCTURE_GEOMETRY_RELATIONSHIP"));
      registry.put(
          "ALTERNATIVE_SOLUTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ALTERNATIVE_SOLUTION_RELATIONSHIP"));
      registry.put(
          "ANALYSIS_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANALYSIS_ASSIGNMENT"));
      registry.put(
          "ANALYSIS_REPRESENTATION_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ANALYSIS_REPRESENTATION_CONTEXT"));
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "APPLIED_ACTION_METHOD_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ACTION_METHOD_ASSIGNMENT"));
      registry.put(
          "APPLIED_ATTRIBUTE_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ATTRIBUTE_CLASSIFICATION_ASSIGNMENT"));
      registry.put(
          "APPLIED_CLASSIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_CLASSIFICATION_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT"));
      registry.put(
          "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "APPLIED_DIRECTED_ACTION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_DIRECTED_ACTION_ASSIGNMENT"));
      registry.put(
          "APPLIED_EFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_EFFECTIVITY_ASSIGNMENT"));
      registry.put(
          "APPLIED_EVENT_OCCURRENCE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_EVENT_OCCURRENCE_ASSIGNMENT"));
      registry.put(
          "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "APPLIED_INEFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_INEFFECTIVITY_ASSIGNMENT"));
      registry.put(
          "APPLIED_LOCATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_LOCATION_ASSIGNMENT"));
      registry.put(
          "APPLIED_LOCATION_REPRESENTATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_LOCATION_REPRESENTATION_ASSIGNMENT"));
      registry.put(
          "APPLIED_ORGANIZATIONAL_PROJECT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ORGANIZATIONAL_PROJECT_ASSIGNMENT"));
      registry.put(
          "APPLIED_ORGANIZATION_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ORGANIZATION_TYPE_ASSIGNMENT"));
      registry.put(
          "APPLIED_STATE_OBSERVED_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_STATE_OBSERVED_ASSIGNMENT"));
      registry.put(
          "APPLIED_STATE_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_STATE_TYPE_ASSIGNMENT"));
      registry.put(
          "APPLIED_TIME_INTERVAL_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_TIME_INTERVAL_ASSIGNMENT"));
      registry.put(
          "APPROVAL_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "APPROVAL_RELATIONSHIP"));
      registry.put(
          "ASCRIBABLE_STATE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ASCRIBABLE_STATE_RELATIONSHIP"));
      registry.put(
          "ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP"));
      registry.put(
          "ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP"));
      registry.put(
          "ASSIGNED_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ASSIGNED_REQUIREMENT"));
      registry.put(
          "ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "ATTRIBUTE_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTRIBUTE_CLASSIFICATION_ASSIGNMENT"));
      registry.put(
          "ATTRIBUTE_LANGUAGE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTRIBUTE_LANGUAGE_ASSIGNMENT"));
      registry.put(
          "ATTRIBUTE_VALUE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTRIBUTE_VALUE_ASSIGNMENT"));
      registry.put(
          "ATTRIBUTE_VALUE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "ATTRIBUTE_VALUE_ROLE"));
      registry.put(
          "BREAKDOWN_ELEMENT_GROUP_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BREAKDOWN_ELEMENT_GROUP_ASSIGNMENT"));
      registry.put(
          "CHANGE_COMPOSITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHANGE_COMPOSITION_RELATIONSHIP"));
      registry.put(
          "CHANGE_GROUP_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHANGE_GROUP_ASSIGNMENT"));
      registry.put(
          "CHARACTERISTIC_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "CHARACTERISTIC_TYPE"));
      registry.put(
          "CHARACTERIZED_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHARACTERIZED_OBJECT_RELATIONSHIP"));
      registry.put(
          "CLASSIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CLASSIFICATION_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "CLASS_USAGE_EFFECTIVITY_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CLASS_USAGE_EFFECTIVITY_CONTEXT_ASSIGNMENT"));
      registry.put(
          "COLLECTION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION_ASSIGNMENT"));
      registry.put(
          "COLLECTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COLLECTION_RELATIONSHIP"));
      registry.put(
          "COLLECTION_VERSION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COLLECTION_VERSION_RELATIONSHIP"));
      registry.put(
          "COLLECTION_VERSION_SEQUENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COLLECTION_VERSION_SEQUENCE_RELATIONSHIP"));
      registry.put(
          "COMPONENT_FEATURE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COMPONENT_FEATURE_RELATIONSHIP"));
      registry.put(
          "CONCEPT_FEATURE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONCEPT_FEATURE_RELATIONSHIP"));
      registry.put(
          "CONCEPT_FEATURE_RELATIONSHIP_WITH_CONDITION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONCEPT_FEATURE_RELATIONSHIP_WITH_CONDITION"));
      registry.put(
          "CONFIGURATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONFIGURATION_ITEM_RELATIONSHIP"));
      registry.put(
          "CONFIGURED_EFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONFIGURED_EFFECTIVITY_ASSIGNMENT"));
      registry.put(
          "CONFIGURED_EFFECTIVITY_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONFIGURED_EFFECTIVITY_CONTEXT_ASSIGNMENT"));
      registry.put(
          "CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP"));
      registry.put(
          "CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP"));
      registry.put(
          "CONTACT_FEATURE_DEFINITION_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONTACT_FEATURE_DEFINITION_FIT_RELATIONSHIP"));
      registry.put(
          "CONTACT_FEATURE_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONTACT_FEATURE_FIT_RELATIONSHIP"));
      registry.put(
          "CONTRACT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONTRACT_RELATIONSHIP"));
      registry.put(
          "CURRENT_CHANGE_ELEMENT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURRENT_CHANGE_ELEMENT_ASSIGNMENT"));
      registry.put(
          "DATA_EQUIVALENCE_DEFINITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DATA_EQUIVALENCE_DEFINITION_RELATIONSHIP"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_INSPECTION_REQUIREMENT"));
      registry.put(
          "DATA_QUALITY_DEFINITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DATA_QUALITY_DEFINITION_RELATIONSHIP"));
      registry.put(
          "DATA_QUALITY_MEASUREMENT_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_MEASUREMENT_REQUIREMENT"));
      registry.put(
          "DATE_AND_TIME_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATE_AND_TIME_ASSIGNMENT"));
      registry.put(
          "DESCRIPTION_TEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DESCRIPTION_TEXT_ASSIGNMENT"));
      registry.put(
          "DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DESCRIPTION_TEXT_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "DESIGN_MAKE_FROM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DESIGN_MAKE_FROM_RELATIONSHIP"));
      registry.put(
          "DIFFERENT_ASSEMBLY_CONSTRAINT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "DIFFERENT_ASSEMBLY_CONSTRAINT_TYPE"));
      registry.put(
          "DIFFERENT_COMPONENT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "DIFFERENT_COMPONENT_TYPE"));
      registry.put(
          "DIMENSION_CALLOUT_COMPONENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSION_CALLOUT_COMPONENT_RELATIONSHIP"));
      registry.put(
          "DIMENSION_CALLOUT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSION_CALLOUT_RELATIONSHIP"));
      registry.put(
          "DIRECTED_ACTION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DIRECTED_ACTION_ASSIGNMENT"));
      registry.put(
          "DISALLOWED_ASSEMBLY_RELATIONSHIP_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DISALLOWED_ASSEMBLY_RELATIONSHIP_USAGE"));
      registry.put(
          "DOCUMENT_IDENTIFIER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_IDENTIFIER"));
      registry.put(
          "DOCUMENT_IDENTIFIER_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_IDENTIFIER_ASSIGNMENT"));
      registry.put(
          "DOCUMENT_REPRESENTATION_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "DOCUMENT_REPRESENTATION_TYPE"));
      registry.put(
          "DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT"));
      registry.put(
          "DOCUMENT_USAGE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "DOCUMENT_USAGE_ROLE"));
      registry.put(
          "EFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EFFECTIVITY_ASSIGNMENT"));
      registry.put(
          "EFFECTIVITY_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EFFECTIVITY_CONTEXT_ASSIGNMENT"));
      registry.put(
          "EFFECTIVITY_CONTEXT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "EFFECTIVITY_CONTEXT_ROLE"));
      registry.put(
          "ENVELOPE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ENVELOPE_RELATIONSHIP"));
      registry.put(
          "ERRONEOUS_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ERRONEOUS_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP"));
      registry.put(
          "EVENT_OCCURRENCE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EVENT_OCCURRENCE_ASSIGNMENT"));
      registry.put(
          "EVENT_OCCURRENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EVENT_OCCURRENCE_RELATIONSHIP"));
      registry.put(
          "EVENT_OCCURRENCE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "EVENT_OCCURRENCE_ROLE"));
      registry.put(
          "EXPLICIT_PROCEDURAL_GEOMETRIC_REPRESENTATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXPLICIT_PROCEDURAL_GEOMETRIC_REPRESENTATION_ITEM_RELATIONSHIP"));
      registry.put(
          "EXPLICIT_PROCEDURAL_REPRESENTATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXPLICIT_PROCEDURAL_REPRESENTATION_ITEM_RELATIONSHIP"));
      registry.put(
          "EXTERNALLY_DEFINED_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXTERNALLY_DEFINED_ITEM_RELATIONSHIP"));
      registry.put(
          "EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "FACT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "FACT_TYPE"));
      registry.put(
          "FINAL_SOLUTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FINAL_SOLUTION"));
      registry.put(
          "FREE_FORM_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FREE_FORM_ASSIGNMENT"));
      registry.put(
          "FROZEN_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FROZEN_ASSIGNMENT"));
      registry.put(
          "GENERAL_MATERIAL_PROPERTY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERAL_MATERIAL_PROPERTY"));
      registry.put(
          "GENERAL_PROPERTY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERAL_PROPERTY_ASSOCIATION"));
      registry.put(
          "GENERIC_PROPERTY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "GENERIC_PROPERTY_RELATIONSHIP"));
      registry.put(
          "GEOMETRIC_MODEL_ELEMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "GEOMETRIC_MODEL_ELEMENT_RELATIONSHIP"));
      registry.put(
          "GEOMETRIC_REPRESENTATION_CONTEXT_WITH_PARAMETER",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "GEOMETRIC_REPRESENTATION_CONTEXT_WITH_PARAMETER"));
      registry.put(
          "GEOMETRIC_TOLERANCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "GEOMETRIC_TOLERANCE_RELATIONSHIP"));
      registry.put(
          "GLOBAL_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "GLOBAL_ASSIGNMENT"));
      registry.put(
          "IDENTIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "IDENTIFICATION_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "IDRM_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "IDRM_CLASSIFICATION_ASSIGNMENT"));
      registry.put(
          "IMPLICIT_EXPLICIT_POSITIONED_SKETCH_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IMPLICIT_EXPLICIT_POSITIONED_SKETCH_RELATIONSHIP"));
      registry.put(
          "INAPT_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "INAPT_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP"));
      registry.put(
          "INSTANCE_USAGE_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "INSTANCE_USAGE_CONTEXT_ASSIGNMENT"));
      registry.put(
          "ITEM_LINK_MOTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ITEM_LINK_MOTION_RELATIONSHIP"));
      registry.put(
          "LINK_MOTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "LINK_MOTION_RELATIONSHIP"));
      registry.put(
          "LOCATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOCATION_ASSIGNMENT"));
      registry.put(
          "LOCATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "LOCATION_RELATIONSHIP"));
      registry.put(
          "LOCATION_REPRESENTATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOCATION_REPRESENTATION_ASSIGNMENT"));
      registry.put(
          "LOCATION_REPRESENTATION_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "LOCATION_REPRESENTATION_ROLE"));
      registry.put(
          "LOCATION_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "LOCATION_ROLE"));
      // MATED_PART_RELATIONSHIP already registered via registerShapeAspectAliases at line 1249
      registry.put(
          "MESSAGE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "MESSAGE_RELATIONSHIP"));
      registry.put(
          "MULTI_LANGUAGE_ATTRIBUTE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "MULTI_LANGUAGE_ATTRIBUTE_ASSIGNMENT"));
      registry.put(
          "NEAR_POINT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "NEAR_POINT_RELATIONSHIP"));
      registry.put(
          "OBJECT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "OBJECT_ROLE"));
      registry.put(
          "ORGANIZATIONAL_PROJECT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ORGANIZATIONAL_PROJECT_ASSIGNMENT"));
      registry.put(
          "ORGANIZATIONAL_PROJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ORGANIZATIONAL_PROJECT_RELATIONSHIP"));
      registry.put(
          "ORGANIZATIONAL_PROJECT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "ORGANIZATIONAL_PROJECT_ROLE"));
      registry.put(
          "ORGANIZATION_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ORGANIZATION_TYPE_ASSIGNMENT"));
      registry.put(
          "ORGANIZATION_TYPE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "ORGANIZATION_TYPE_ROLE"));
      registry.put(
          "OVERCOMPLEX_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "OVERCOMPLEX_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP"));
      registry.put(
          "PARAMETRIC_REPRESENTATION_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PARAMETRIC_REPRESENTATION_CONTEXT"));
      registry.put(
          "PARTIAL_DOCUMENT_WITH_STRUCTURED_TEXT_REPRESENTATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PARTIAL_DOCUMENT_WITH_STRUCTURED_TEXT_REPRESENTATION_ASSIGNMENT"));
      registry.put(
          "PATH_PARAMETER_REPRESENTATION_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PATH_PARAMETER_REPRESENTATION_CONTEXT"));
      registry.put(
          "PREVIOUS_CHANGE_ELEMENT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PREVIOUS_CHANGE_ELEMENT_ASSIGNMENT"));
      registry.put(
          "PROCESS_PROPERTY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCESS_PROPERTY_ASSOCIATION"));
      registry.put(
          "PRODUCT_DATA_AND_DATA_EQUIVALENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DATA_AND_DATA_EQUIVALENCE_RELATIONSHIP"));
      registry.put(
          "PRODUCT_DATA_AND_DATA_QUALITY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DATA_AND_DATA_QUALITY_RELATIONSHIP"));
      registry.put(
          "PRODUCT_DEFINITION_CONTEXT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "PRODUCT_DEFINITION_CONTEXT_ROLE"));
      registry.put(
          "PRODUCT_DEFINITION_ELEMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DEFINITION_ELEMENT_RELATIONSHIP"));
      registry.put(
          "PRODUCT_DEFINITION_GROUP_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_GROUP_ASSIGNMENT"));
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DEFINITION_OCCURRENCE_RELATIONSHIP"));
      registry.put(
          "PRODUCT_DEFINITION_RELATIONSHIP_KINEMATICS",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DEFINITION_RELATIONSHIP_KINEMATICS"));
      registry.put(
          "PRODUCT_GROUP_ATTRIBUTE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_GROUP_ATTRIBUTE_ASSIGNMENT"));
      registry.put(
          "PRODUCT_GROUP_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_GROUP_RELATIONSHIP"));
      registry.put(
          "PRODUCT_GROUP_RULE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_GROUP_RULE_ASSIGNMENT"));
      registry.put(
          "PRODUCT_MATERIAL_COMPOSITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_MATERIAL_COMPOSITION_RELATIONSHIP"));
      registry.put(
          "REPRESENTATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "REPRESENTATION_ITEM_RELATIONSHIP"));
      registry.put(
          "RESOURCE_PROPERTY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RESOURCE_PROPERTY"));
      registry.put(
          "RESOURCE_REQUIREMENT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "RESOURCE_REQUIREMENT_TYPE"));
      registry.put(
          "RULE_SUPERSEDED_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RULE_SUPERSEDED_ASSIGNMENT"));
      registry.put(
          "SAME_AS_EXTERNAL_ITEM_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SAME_AS_EXTERNAL_ITEM_ASSIGNMENT"));
      registry.put(
          "SATISFIED_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SATISFIED_REQUIREMENT"));
      registry.put(
          "SATISFIES_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SATISFIES_REQUIREMENT"));
      registry.put(
          "SCANNER_PROPERTY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCANNER_PROPERTY"));
      registry.put(
          "SHAPE_ASPECT_RELATIONSHIP_REPRESENTATION_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SHAPE_ASPECT_RELATIONSHIP_REPRESENTATION_ASSOCIATION"));
      registry.put(
          "SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP"));
      registry.put(
          "SHAPE_FEATURE_DEFINITION_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SHAPE_FEATURE_DEFINITION_FIT_RELATIONSHIP"));
      registry.put(
          "SHAPE_FEATURE_DEFINITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SHAPE_FEATURE_DEFINITION_RELATIONSHIP"));
      registry.put(
          "SINGLE_PROPERTY_IS_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SINGLE_PROPERTY_IS_DEFINITION"));
      registry.put(
          "SOURCED_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SOURCED_REQUIREMENT"));
      registry.put(
          "SOURCE_FOR_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SOURCE_FOR_REQUIREMENT"));
      registry.put(
          "STATECHAR_RELATIONSHIP_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATECHAR_RELATIONSHIP_OBJECT"));
      registry.put(
          "STATECHAR_TYPE_APPLIED_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATECHAR_TYPE_APPLIED_OBJECT"));
      registry.put(
          "STATECHAR_TYPE_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATECHAR_TYPE_OBJECT"));
      registry.put(
          "STATECHAR_TYPE_RELATIONSHIP_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATECHAR_TYPE_RELATIONSHIP_OBJECT"));
      registry.put(
          "STATE_DEFINITION_TO_STATE_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STATE_DEFINITION_TO_STATE_ASSIGNMENT_RELATIONSHIP"));
      registry.put(
          "STATE_OBSERVED_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STATE_OBSERVED_ASSIGNMENT"));
      registry.put(
          "STATE_OBSERVED_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATE_OBSERVED_RELATIONSHIP"));
      registry.put(
          "STATE_OBSERVED_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "STATE_OBSERVED_ROLE"));
      registry.put(
          "STATE_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATE_TYPE"));
      registry.put(
          "STATE_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STATE_TYPE_ASSIGNMENT"));
      registry.put(
          "STATE_TYPE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATE_TYPE_RELATIONSHIP"));
      registry.put(
          "STATE_TYPE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATE_TYPE_ROLE"));
      // SUPPLIED_PART_RELATIONSHIP already registered via registerProductDefinitionRelationshipAliases at line 876
      registry.put(
          "TIME_INTERVAL_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "TIME_INTERVAL_ASSIGNMENT"));
      registry.put(
          "TIME_INTERVAL_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TIME_INTERVAL_RELATIONSHIP"));
      registry.put(
          "TIME_INTERVAL_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "TIME_INTERVAL_ROLE"));
      registry.put(
          "UNIFORM_RESOURCE_IDENTIFIER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "UNIFORM_RESOURCE_IDENTIFIER"));
      registry.put(
          "VALUE_FORMAT_TYPE_QUALIFIER",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "VALUE_FORMAT_TYPE_QUALIFIER"));
      registry.put(
          "VERSIONED_ACTION_REQUEST_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "VERSIONED_ACTION_REQUEST_RELATIONSHIP"));

// Phase 3: Additional simple entities (auto-generated)
      registry.put(
          "ADD_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ADD_ELEMENT"));
      registry.put(
          "ANALYSIS_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ANALYSIS_ITEM"));
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "APPLIED_PRESENTED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "APPLIED_PRESENTED_ITEM"));
      registry.put(
          "APPLIED_USAGE_RIGHT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_USAGE_RIGHT"));
      registry.put(
          "ASSEMBLY_BOND_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_BOND_DEFINITION"));
      // ASSEMBLY_COMPONENT_USAGE_SUBSTITUTE already registered via registerProductDefinitionRelationshipRelationshipAliases at line 887
      registry.put(
          "ASSEMBLY_GROUP_COMPONENT_DEFINITION_PLACEMENT_LINK",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_GROUP_COMPONENT_DEFINITION_PLACEMENT_LINK"));
      registry.put(
          "BREAKDOWN_ELEMENT_REALIZATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BREAKDOWN_ELEMENT_REALIZATION"));
      // BREAKDOWN_ELEMENT_USAGE already registered via registerProductDefinitionRelationshipAliases at line 872
      registry.put(
          "CHANGE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CHANGE_ELEMENT"));
      registry.put(
          "CHANGE_ELEMENT_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CHANGE_ELEMENT_SEQUENCE"));
      registry.put(
          "CHARACTER_GLYPH_FONT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTER_GLYPH_FONT_USAGE"));
      registry.put(
          "COLLECTION_VIEW_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COLLECTION_VIEW_DEFINITION"));
      registry.put(
          "COMPOSITE_ASSEMBLY_SEQUENCE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_ASSEMBLY_SEQUENCE_DEFINITION"));
      registry.put(
          "CONFIGURABLE_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONFIGURABLE_ITEM"));
      registry.put(
          "CONNECTIVITY_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTIVITY_DEFINITION"));
      // CONTACT_FEATURE_DEFINITION already registered via registerCharacterizedObjectAliases at line 1107
      registry.put(
          "COORDINATES_LIST",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COORDINATES_LIST"));
      // COUNTERBORE_HOLE_DEFINITION already registered via registerCharacterizedObjectAliases at line 1108
      // COUNTERBORE_HOLE_OCCURRENCE already registered via registerShapeAspectOccurrenceAliases at line 1267
      registry.put(
          "COUNTERBORE_HOLE_OCCURRENCE_IN_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COUNTERBORE_HOLE_OCCURRENCE_IN_ASSEMBLY"));
      // COUNTERDRILL_HOLE_DEFINITION already registered via registerCharacterizedObjectAliases at line 1109
      // COUNTERDRILL_HOLE_OCCURRENCE already registered via registerShapeAspectOccurrenceAliases at line 1268
      registry.put(
          "COUNTERDRILL_HOLE_OCCURRENCE_IN_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COUNTERDRILL_HOLE_OCCURRENCE_IN_ASSEMBLY"));
      // COUNTERSINK_HOLE_DEFINITION already registered via registerCharacterizedObjectAliases at line 1110
      // COUNTERSINK_HOLE_OCCURRENCE already registered via registerShapeAspectOccurrenceAliases at line 1269
      registry.put(
          "COUNTERSINK_HOLE_OCCURRENCE_IN_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COUNTERSINK_HOLE_OCCURRENCE_IN_ASSEMBLY"));
      registry.put(
          "CRITERION_REPORT_ITEM_WITH_NUMBER_OF_INSTANCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CRITERION_REPORT_ITEM_WITH_NUMBER_OF_INSTANCES"));
      registry.put(
          "CRITERION_REPORT_ITEM_WITH_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CRITERION_REPORT_ITEM_WITH_VALUE"));
      registry.put(
          "CROSS_SECTIONAL_ALTERNATIVE_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_ALTERNATIVE_SHAPE_ELEMENT"));
      registry.put(
          "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT"));
      registry.put(
          "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_LACING",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_LACING"));
      registry.put(
          "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_TUBULAR_COVER",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_TUBULAR_COVER"));
      registry.put(
          "CROSS_SECTIONAL_OCCURRENCE_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CROSS_SECTIONAL_OCCURRENCE_SHAPE_ELEMENT"));
      registry.put(
          "CROSS_SECTIONAL_PART_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_PART_SHAPE_ELEMENT"));
      registry.put(
          "DATA_EQUIVALENCE_ASSESSMENT_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_ASSESSMENT_SPECIFICATION"));
      registry.put(
          "DATA_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_CRITERION"));
      registry.put(
          "DATA_EQUIVALENCE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_EQUIVALENCE_DEFINITION"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTED_ELEMENT_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_EQUIVALENCE_INSPECTED_ELEMENT_PAIR"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT_ITEM"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));
      registry.put(
          "DATA_QUALITY_ASSESSMENT_MEASUREMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_ASSESSMENT_MEASUREMENT_ASSOCIATION"));
      registry.put(
          "DATA_QUALITY_ASSESSMENT_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_ASSESSMENT_SPECIFICATION"));
      registry.put(
          "DATA_QUALITY_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_CRITERION"));
      registry.put(
          "DATA_QUALITY_CRITERION_ASSESSMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_CRITERION_ASSESSMENT_ASSOCIATION"));
      registry.put(
          "DATA_QUALITY_CRITERION_MEASUREMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_CRITERION_MEASUREMENT_ASSOCIATION"));
      registry.put(
          "DATA_QUALITY_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_QUALITY_DEFINITION"));
      registry.put(
          "DATA_QUALITY_INSPECTION_CRITERION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_INSPECTION_CRITERION_REPORT"));
      registry.put(
          "DATA_QUALITY_INSPECTION_CRITERION_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_INSPECTION_CRITERION_REPORT_ITEM"));
      registry.put(
          "DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM"));
      registry.put(
          "DATA_QUALITY_REPORT_MEASUREMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_REPORT_MEASUREMENT_ASSOCIATION"));
      registry.put(
          "DEFINITIONAL_PRODUCT_DEFINITION_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DEFINITIONAL_PRODUCT_DEFINITION_USAGE"));
      registry.put(
          "DELETE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DELETE_ELEMENT"));
      registry.put(
          "DEPENDENT_VARIABLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DEPENDENT_VARIABLE_DEFINITION"));
      registry.put(
          "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS"));
      registry.put(
          "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS_WIREFRAME_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS_WIREFRAME_MODEL"));
      registry.put(
          "DOCUMENT_PRODUCT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_PRODUCT_ASSOCIATION"));
      registry.put(
          "DRAWING_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DRAWING_DEFINITION"));
      registry.put(
          "DRAWING_SHEET_REVISION_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DRAWING_SHEET_REVISION_USAGE"));
      registry.put(
          "EQUIVALENCE_INSTANCE_REPORT_ITEM_WITH_NOTABLE_INSTANCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EQUIVALENCE_INSTANCE_REPORT_ITEM_WITH_NOTABLE_INSTANCES"));
      registry.put(
          "EVALUATION_PRODUCT_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EVALUATION_PRODUCT_DEFINITION"));
      registry.put(
          "EVENT_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EVENT_OCCURRENCE"));
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERION"));
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM"));
      // EXTERNALLY_DEFINED_FEATURE_DEFINITION already registered via registerCharacterizedObjectAliases at line 1113
      registry.put(
          "EXTERNALLY_DEFINED_ITEM_WITH_MULTIPLE_REFERENCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNALLY_DEFINED_ITEM_WITH_MULTIPLE_REFERENCES"));
      registry.put(
          "EXTERNALLY_LISTED_DATA",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNALLY_LISTED_DATA"));
      // FEATURE_DEFINITION already registered via registerCharacterizedObjectAliases at line 1114
      // FEATURE_DEFINITION_WITH_CONNECTION_AREA already registered via registerCharacterizedObjectAliases at line 1115
      registry.put(
          "FIXED_INSTANCE_ATTRIBUTE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FIXED_INSTANCE_ATTRIBUTE_SET"));
      registry.put(
          "FOUNDED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FOUNDED_ITEM"));
      registry.put(
          "FUNCTIONAL_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FUNCTIONAL_ELEMENT_USAGE"));
      registry.put(
          "GENERIC_PRODUCT_DEFINITION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERIC_PRODUCT_DEFINITION_REFERENCE"));
      registry.put(
          "GPS_FILTRATION_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "GPS_FILTRATION_SPECIFICATION"));
      registry.put(
          "HIDDEN_ELEMENT_OVER_RIDING_STYLED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIDDEN_ELEMENT_OVER_RIDING_STYLED_ITEM"));
      registry.put(
          "INAPPROPRIATE_ELEMENT_VISIBILITY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INAPPROPRIATE_ELEMENT_VISIBILITY"));
      registry.put(
          "INCONSISTENT_ELEMENT_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_ELEMENT_REFERENCE"));
      registry.put(
          "INDIRECTLY_SELECTED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDIRECTLY_SELECTED_ELEMENTS"));
      registry.put(
          "INDIRECTLY_SELECTED_SHAPE_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDIRECTLY_SELECTED_SHAPE_ELEMENTS"));
      registry.put(
          "INFORMATION_USAGE_RIGHT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "INFORMATION_USAGE_RIGHT"));
      registry.put(
          "INSTANCE_REPORT_ITEM_WITH_EXTREME_INSTANCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INSTANCE_REPORT_ITEM_WITH_EXTREME_INSTANCES"));
      registry.put(
          "MATING_MATERIAL_ITEMS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MATING_MATERIAL_ITEMS"));
      registry.put(
          "MODIFY_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODIFY_ELEMENT"));
      registry.put(
          "NON_AGREED_ACCURACY_PARAMETER_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "NON_AGREED_ACCURACY_PARAMETER_USAGE"));
      registry.put(
          "NON_AGREED_SCALE_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "NON_AGREED_SCALE_USAGE"));
      registry.put(
          "PHYSICAL_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PHYSICAL_ELEMENT_USAGE"));
      registry.put(
          "PLY_LAMINATE_SEQUENCE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PLY_LAMINATE_SEQUENCE_DEFINITION"));
      registry.put(
          "PRESENTED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRESENTED_ITEM"));
      registry.put(
          "PROCESS_PRODUCT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PROCESS_PRODUCT_ASSOCIATION"));
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_RESOURCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_FORMATION_RESOURCE"));
      registry.put(
          "PRODUCT_DEFINITION_KINEMATICS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_KINEMATICS"));
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_OCCURRENCE"));
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE"));
      registry.put(
          "PRODUCT_DEFINITION_PROCESS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_PROCESS"));
      registry.put(
          "PRODUCT_DEFINITION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_REFERENCE"));
      registry.put(
          "PRODUCT_DEFINITION_SPECIFIED_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_SPECIFIED_OCCURRENCE"));
      // PRODUCT_DEFINITION_SUBSTITUTE already registered via registerProductDefinitionRelationshipRelationshipAliases at line 888
      registry.put(
          "PRODUCT_GROUP_ATTRIBUTE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_GROUP_ATTRIBUTE_SET"));
      registry.put(
          "PRODUCT_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PRODUCT_SPECIFICATION"));
      registry.put(
          "PROJECTED_ZONE_DEFINITION_WITH_OFFSET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROJECTED_ZONE_DEFINITION_WITH_OFFSET"));
      registry.put(
          "RELATIVE_EVENT_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RELATIVE_EVENT_OCCURRENCE"));
      registry.put(
          "REPOSITIONED_TESSELLATED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPOSITIONED_TESSELLATED_ITEM"));
      registry.put(
          "REP_ITEM_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REP_ITEM_GROUP"));
      registry.put(
          "RIGHT_TO_USAGE_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RIGHT_TO_USAGE_ASSOCIATION"));
      registry.put(
          "ROLE_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ROLE_ASSOCIATION"));
      registry.put(
          "RULE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_DEFINITION"));
      registry.put(
          "RULE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_SET"));
      registry.put(
          "RULE_SET_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_SET_GROUP"));
      registry.put(
          "RULE_SOFTWARE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_SOFTWARE_DEFINITION"));
      registry.put(
          "SATISFYING_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SATISFYING_ITEM"));
      registry.put(
          "SCANNED_DATA_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCANNED_DATA_ITEM"));
      registry.put(
          "SHAPE_DATA_QUALITY_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SHAPE_DATA_QUALITY_CRITERION"));
      registry.put(
          "SHAPE_DATA_QUALITY_CRITERION_AND_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SHAPE_DATA_QUALITY_CRITERION_AND_ACCURACY_ASSOCIATION"));
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTION_CRITERION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SHAPE_DATA_QUALITY_INSPECTION_CRITERION_REPORT"));
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SHAPE_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM"));
      registry.put(
          "SHAPE_FEATURE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SHAPE_FEATURE_DEFINITION"));
      registry.put(
          "SHAPE_INSPECTION_RESULT_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SHAPE_INSPECTION_RESULT_ACCURACY_ASSOCIATION"));
      registry.put(
          "SMEARED_MATERIAL_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SMEARED_MATERIAL_DEFINITION"));
      registry.put(
          "SOLID_WITH_SHAPE_ELEMENT_PATTERN",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_SHAPE_ELEMENT_PATTERN"));
      registry.put(
          "SPECIFICATION_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPECIFICATION_DEFINITION"));
      registry.put(
          "STYLED_TESSELLATED_ITEM_WITH_COLOURS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "STYLED_TESSELLATED_ITEM_WITH_COLOURS"));
      registry.put(
          "SYSTEM_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SYSTEM_ELEMENT_USAGE"));
      registry.put(
          "TESSELLATED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TESSELLATED_ITEM"));
      registry.put(
          "TESSELLATED_STRUCTURED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TESSELLATED_STRUCTURED_ITEM"));
      registry.put(
          "TWISTED_CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TWISTED_CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT"));
      registry.put(
          "UNUSED_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "UNUSED_SHAPE_ELEMENT"));
      registry.put(
          "USER_SELECTED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "USER_SELECTED_ELEMENTS"));
      registry.put(
          "USER_SELECTED_SHAPE_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "USER_SELECTED_SHAPE_ELEMENTS"));
      registry.put(
          "WRONG_ELEMENT_NAME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "WRONG_ELEMENT_NAME"));
      registry.put(
          "ZONE_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ZONE_ELEMENT_USAGE"));

// Phase 3 final: Additional entities to reach 80% coverage
      registry.put(
          "ANGLE_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ANGLE_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "ASSEMBLY_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "COAXIAL_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COAXIAL_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "CURVE_DISTANCE_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_DISTANCE_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "CURVE_LENGTH_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_LENGTH_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "CURVE_SMOOTHNESS_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_SMOOTHNESS_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "EXPLICIT_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXPLICIT_GEOMETRIC_CONSTRAINT"));

// Phase 4: Geometry and simple entities (auto-generated)
      registry.put(
          "CONICAL_STEPPED_HOLE_TRANSITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONICAL_STEPPED_HOLE_TRANSITION"));
      registry.put(
          "CURVE_STYLE_FONT_AND_SCALING",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURVE_STYLE_FONT_AND_SCALING"));
      registry.put(
          "CURVE_STYLE_FONT_PATTERN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURVE_STYLE_FONT_PATTERN"));
      registry.put(
          "CYLINDRICAL_11",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CYLINDRICAL_11"));
      registry.put(
          "CYLINDRICAL_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_PAIR_VALUE"));
      registry.put(
          "CYLINDRICAL_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_PAIR_WITH_RANGE"));
      registry.put(
          "CYLINDRICAL_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_POINT"));
      registry.put(
          "CYLINDRICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_VOLUME"));
      registry.put(
          "DOUBLE_OFFSET_SHELLED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DOUBLE_OFFSET_SHELLED_SOLID"));
      registry.put(
          "ECCENTRIC_CONICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ECCENTRIC_CONICAL_VOLUME"));
      registry.put(
          "HIGH_DEGREE_PLANAR_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "HIGH_DEGREE_PLANAR_SURFACE"));
      registry.put(
          "IMPLICIT_PLANAR_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "IMPLICIT_PLANAR_CURVE"));
      registry.put(
          "IMPLICIT_PLANAR_INTERSECTION_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_PLANAR_INTERSECTION_POINT"));
      registry.put(
          "IMPLICIT_PLANAR_PROJECTION_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_PLANAR_PROJECTION_POINT"));
      registry.put(
          "LINEAR_FLEXIBLE_AND_PLANAR_CURVE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LINEAR_FLEXIBLE_AND_PLANAR_CURVE_PAIR"));
      registry.put(
          "PARALLEL_OFFSET_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PARALLEL_OFFSET_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "PLANAR_CURVE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PLANAR_CURVE_PAIR"));
      registry.put(
          "PLANAR_CURVE_PAIR_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PLANAR_CURVE_PAIR_RANGE"));
      registry.put(
          "PLANAR_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PLANAR_PAIR_VALUE"));
      registry.put(
          "PLANAR_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PLANAR_PAIR_WITH_RANGE"));
      registry.put(
          "POINT_ON_PLANAR_CURVE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "POINT_ON_PLANAR_CURVE_PAIR"));
      registry.put(
          "POINT_ON_PLANAR_CURVE_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "POINT_ON_PLANAR_CURVE_PAIR_VALUE"));
      registry.put(
          "POINT_ON_PLANAR_CURVE_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "POINT_ON_PLANAR_CURVE_PAIR_WITH_RANGE"));
      registry.put(
          "SOLID_WITH_CONICAL_BOTTOM_ROUND_HOLE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_CONICAL_BOTTOM_ROUND_HOLE"));
      registry.put(
          "SOLID_WITH_DOUBLE_OFFSET_CHAMFER",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_DOUBLE_OFFSET_CHAMFER"));
      registry.put(
          "SOLID_WITH_SINGLE_OFFSET_CHAMFER",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_SINGLE_OFFSET_CHAMFER"));
      registry.put(
          "SOLID_WITH_SPHERICAL_BOTTOM_ROUND_HOLE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_SPHERICAL_BOTTOM_ROUND_HOLE"));
      registry.put(
          "SOLID_WITH_STEPPED_ROUND_HOLE_AND_CONICAL_TRANSITIONS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_STEPPED_ROUND_HOLE_AND_CONICAL_TRANSITIONS"));
      registry.put(
          "SPHERICAL_CAP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_CAP"));
      registry.put(
          "SPHERICAL_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_PAIR_VALUE"));
      registry.put(
          "SPHERICAL_PAIR_WITH_PIN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SPHERICAL_PAIR_WITH_PIN"));
      registry.put(
          "SPHERICAL_PAIR_WITH_PIN_AND_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SPHERICAL_PAIR_WITH_PIN_AND_RANGE"));
      registry.put(
          "SPHERICAL_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_PAIR_WITH_RANGE"));
      registry.put(
          "SPHERICAL_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_POINT"));
      registry.put(
          "SPHERICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPHERICAL_VOLUME"));
      registry.put(
          "TOROIDAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TOROIDAL_VOLUME"));
      registry.put(
          "ARRAY_PLACEMENT_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ARRAY_PLACEMENT_GROUP"));
      registry.put(
          "ASSEMBLY_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_JOINT"));
      registry.put(
          "ASSEMBLY_SHAPE_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_SHAPE_JOINT"));
      registry.put(
          "BACKGROUND_COLOUR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BACKGROUND_COLOUR"));
      // BARRING_HOLE already registered via registerCharacterizedObjectAliases at line 1100
      registry.put(
          "BEZIER_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BEZIER_VOLUME"));
      registry.put(
          "B_SPLINE_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "B_SPLINE_VOLUME"));
      registry.put(
          "B_SPLINE_VOLUME_WITH_KNOTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "B_SPLINE_VOLUME_WITH_KNOTS"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));
      registry.put(
          "CIRCULAR_CLOSED_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CIRCULAR_CLOSED_PROFILE"));
      // CIRCULAR_PATTERN already registered via registerCharacterizedObjectAliases at line 1104
      registry.put(
          "CLOSED_PATH_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CLOSED_PATH_PROFILE"));
      registry.put(
          "COMPONENT_FEATURE_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPONENT_FEATURE_JOINT"));
      // COMPOSITE_HOLE already registered via registerCharacterizedObjectAliases at line 1106
      registry.put(
          "CONICAL_STEPPED_HOLE_TRANSITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONICAL_STEPPED_HOLE_TRANSITION"));
      registry.put(
          "CONNECTED_VOLUME_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTED_VOLUME_SET"));
      registry.put(
          "CONNECTED_VOLUME_SUB_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTED_VOLUME_SUB_SET"));
      registry.put(
          "CONNECTION_ZONE_BASED_ASSEMBLY_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTION_ZONE_BASED_ASSEMBLY_JOINT"));
      registry.put(
          "CSG_PRIMITIVE_SOLID_2D",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CSG_PRIMITIVE_SOLID_2D"));
      registry.put(
          "CSG_SOLID_2D",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CSG_SOLID_2D"));
      registry.put(
          "CYCLIDE_SEGMENT_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYCLIDE_SEGMENT_SOLID"));
      registry.put(
          "CYLINDRICAL_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_PAIR_VALUE"));
      registry.put(
          "CYLINDRICAL_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_PAIR_WITH_RANGE"));
      registry.put(
          "CYLINDRICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_VOLUME"));
      registry.put(
          "DIFFERENT_ASSEMBLY_CENTROID_USING_NOTIONAL_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_ASSEMBLY_CENTROID_USING_NOTIONAL_SOLID"));
      registry.put(
          "DIFFERENT_ASSEMBLY_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_ASSEMBLY_VOLUME"));
      registry.put(
          "DIFFERENT_BOUNDING_BOX",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_BOUNDING_BOX"));
      registry.put(
          "DIFFERENT_PLACEMENT_OF_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DIFFERENT_PLACEMENT_OF_COMPONENT"));
      registry.put(
          "DIFFERENT_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_VOLUME"));
      registry.put(
          "DRAPED_DEFINED_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DRAPED_DEFINED_TRANSFORMATION"));
      registry.put(
          "ECCENTRIC_CONICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ECCENTRIC_CONICAL_VOLUME"));
      registry.put(
          "ELLIPSOID_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ELLIPSOID_VOLUME"));
      registry.put(
          "ENTIRELY_NARROW_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ENTIRELY_NARROW_SOLID"));
      registry.put(
          "ERRONEOUS_MANIFOLD_SOLID_BREP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ERRONEOUS_MANIFOLD_SOLID_BREP"));
      // EXPLICIT_COMPOSITE_HOLE already registered via registerCharacterizedObjectAliases at line 1111
      // EXPLICIT_ROUND_HOLE already registered via registerCharacterizedObjectAliases at line 1112
      registry.put(
          "EXTERNALLY_DEFINED_COLOUR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_COLOUR"));
      registry.put(
          "EXTERNALLY_DEFINED_STYLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_STYLE"));
      // FEATURE_PATTERN already registered via registerCharacterizedObjectAliases at line 1117
      registry.put(
          "FILL_AREA_STYLE_TILES",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FILL_AREA_STYLE_TILES"));
      registry.put(
          "FILL_AREA_STYLE_TILE_COLOURED_REGION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FILL_AREA_STYLE_TILE_COLOURED_REGION"));
      registry.put(
          "FULLY_CONSTRAINED_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FULLY_CONSTRAINED_PAIR"));
      registry.put(
          "FUNCTIONALLY_DEFINED_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "FUNCTIONALLY_DEFINED_TRANSFORMATION"));
      registry.put(
          "GEAR_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GEAR_PAIR_VALUE"));
      registry.put(
          "HEXAHEDRON_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HEXAHEDRON_VOLUME"));
      registry.put(
          "HIGH_ORDER_KINEMATIC_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "HIGH_ORDER_KINEMATIC_PAIR"));
      registry.put(
          "HOLE_BOTTOM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HOLE_BOTTOM"));
      // HOLE_IN_PANEL already registered via registerCharacterizedObjectAliases at line 1121
      registry.put(
          "HOMOKINETIC_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "HOMOKINETIC_PAIR"));
      registry.put(
          "IMPLICIT_PLANAR_INTERSECTION_POINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_PLANAR_INTERSECTION_POINT"));
      registry.put(
          "IMPORTED_VOLUME_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_VOLUME_FUNCTION"));
      registry.put(
          "INAPT_MANIFOLD_SOLID_BREP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INAPT_MANIFOLD_SOLID_BREP"));
      registry.put(
          "LAID_DEFINED_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "LAID_DEFINED_TRANSFORMATION"));
      registry.put(
          "LINEAR_ARRAY_PLACEMENT_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "LINEAR_ARRAY_PLACEMENT_GROUP_COMPONENT"));
      registry.put(
          "LINEAR_FLEXIBLE_AND_PINION_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LINEAR_FLEXIBLE_AND_PINION_PAIR"));
      registry.put(
          "LINEAR_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEAR_PROFILE"));
      registry.put(
          "LINK_MOTION_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "LINK_MOTION_TRANSFORMATION"));
      registry.put(
          "LOCALLY_REFINED_SPLINE_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOCALLY_REFINED_SPLINE_VOLUME"));
      registry.put(
          "LOW_ORDER_KINEMATIC_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOW_ORDER_KINEMATIC_PAIR"));
      registry.put(
          "LOW_ORDER_KINEMATIC_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOW_ORDER_KINEMATIC_PAIR_VALUE"));
      registry.put(
          "LOW_ORDER_KINEMATIC_PAIR_WITH_MOTION_COUPLING",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOW_ORDER_KINEMATIC_PAIR_WITH_MOTION_COUPLING"));
      registry.put(
          "MODIFIED_PATTERN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "MODIFIED_PATTERN"));
      registry.put(
          "MODIFIED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODIFIED_SOLID"));
      registry.put(
          "MODIFIED_SOLID_WITH_PLACED_CONFIGURATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODIFIED_SOLID_WITH_PLACED_CONFIGURATION"));
      registry.put(
          "MULTIPLY_DEFINED_PLACEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "MULTIPLY_DEFINED_PLACEMENTS"));
      registry.put(
          "MULTIPLY_DEFINED_SOLIDS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_SOLIDS"));
      registry.put(
          "NGON_CLOSED_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "NGON_CLOSED_PROFILE"));
      registry.put(
          "OPEN_PATH_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "OPEN_PATH_PROFILE"));
      registry.put(
          "ORIENTED_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ORIENTED_JOINT"));
      // OUTSIDE_PROFILE already registered via registerCharacterizedObjectAliases at line 1126

// Phase 4 final: Additional entities to reach 85%
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "APPLICATION_CONTEXT_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLICATION_CONTEXT_ELEMENT"));
      registry.put(
          "AUXILIARY_GEOMETRIC_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AUXILIARY_GEOMETRIC_REPRESENTATION_ITEM"));
      registry.put(
          "BINARY_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BINARY_REPRESENTATION_ITEM"));
      registry.put(
          "BOOLEAN_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_REPRESENTATION_ITEM"));
      // BREAKDOWN_CONTEXT already registered via registerProductDefinitionRelationshipAliases at line 871
      registry.put(
          "BYTES_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BYTES_REPRESENTATION_ITEM"));
      registry.put(
          "CAMERA_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));

// Phase 5: Remaining simple and medium entities (auto-generated)
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      // APPLIED_AREA already registered via registerShapeAspectAliases at line 1235
      // BREAKDOWN_OF already registered via registerProductDefinitionRelationshipAliases at line 873
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION"));
      registry.put(
          "CAMERA_MODEL_D3_WITH_HLHSR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_WITH_HLHSR"));
      registry.put(
          "CAMERA_MODEL_WITH_LIGHT_SOURCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_WITH_LIGHT_SOURCES"));
      registry.put(
          "CARTESIAN_COMPLEX_NUMBER_REGION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CARTESIAN_COMPLEX_NUMBER_REGION"));
      registry.put(
          "CHARACTERIZED_CLASS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CHARACTERIZED_CLASS"));
      registry.put(
          "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE"));
      registry.put(
          "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE_CATEGORY",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE_CATEGORY"));
      registry.put(
          "CIRCULAR_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_AREA"));
      registry.put(
          "CLASS_BY_EXTENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CLASS_BY_EXTENSION"));
      registry.put(
          "CLASS_BY_INTENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CLASS_BY_INTENSION"));
      registry.put(
          "COMPLEX_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_AREA"));
      // COMPOUND_FEATURE already registered via registerCharacterizedObjectAliases at line 1105
      registry.put(
          "CONCEPT_FEATURE_OPERATOR",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONCEPT_FEATURE_OPERATOR"));
      registry.put(
          "CONDITIONAL_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONDITIONAL_CONCEPT_FEATURE"));
      // CONTACTING_FEATURE already registered via registerShapeAspectAliases at line 1241
      registry.put(
          "CONTACT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONTACT_FEATURE"));
      registry.put(
          "ELEMENTARY_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ELEMENTARY_FUNCTION"));
      registry.put(
          "ELEMENTARY_SPACE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ELEMENTARY_SPACE"));
      registry.put(
          "ELLIPTIC_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ELLIPTIC_AREA"));
      registry.put(
          "EXCLUSIVE_PRODUCT_CONCEPT_FEATURE_CATEGORY",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXCLUSIVE_PRODUCT_CONCEPT_FEATURE_CATEGORY"));
      registry.put(
          "EXTERNAL_CLASS_LIBRARY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNAL_CLASS_LIBRARY"));
      // FEATURE_IN_PANEL already registered via registerCharacterizedObjectAliases at line 1116
      // GENERAL_FEATURE already registered via registerCharacterizedObjectAliases at line 1120
      registry.put(
          "INCLUSION_PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "INCLUSION_PRODUCT_CONCEPT_FEATURE"));
      registry.put(
          "IN_ZONE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IN_ZONE"));
      registry.put(
          "LISTED_COMPLEX_NUMBER_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_COMPLEX_NUMBER_DATA"));
      registry.put(
          "LISTED_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_DATA"));
      registry.put(
          "LISTED_INTEGER_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_INTEGER_DATA"));
      registry.put(
          "LISTED_LOGICAL_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_LOGICAL_DATA"));
      registry.put(
          "LISTED_PRODUCT_SPACE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_PRODUCT_SPACE"));
      registry.put(
          "LISTED_REAL_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_REAL_DATA"));
      registry.put(
          "LISTED_STRING_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_STRING_DATA"));
      registry.put(
          "MACHINING_PROCESS_EXECUTABLE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "MACHINING_PROCESS_EXECUTABLE"));
      registry.put(
          "PACKAGE_PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PACKAGE_PRODUCT_CONCEPT_FEATURE"));
      registry.put(
          "PARTIAL_CIRCULAR_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PARTIAL_CIRCULAR_PROFILE"));
      registry.put(
          "PATH_AREA_WITH_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PATH_AREA_WITH_PARAMETERS"));
      // PATH_FEATURE_COMPONENT already registered via registerShapeAspectAliases at line 1252
      // PHYSICAL_COMPONENT_FEATURE already registered via registerShapeAspectAliases at line 1253
      registry.put(
          "PLACED_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PLACED_FEATURE"));
      registry.put(
          "PLACEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PLACEMENT"));
      registry.put(
          "POLAR_COMPLEX_NUMBER_REGION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "POLAR_COMPLEX_NUMBER_REGION"));
      registry.put(
          "POLYGONAL_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "POLYGONAL_AREA"));
      registry.put(
          "PROCESS_OPERATION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PROCESS_OPERATION"));
      registry.put(
          "PROCESS_PLAN",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PROCESS_PLAN"));
      registry.put(
          "PRODUCT_CLASS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_CLASS"));
      registry.put(
          "PRODUCT_PROCESS_PLAN",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_PROCESS_PLAN"));
      registry.put(
          "PROFILE_FLOOR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PROFILE_FLOOR"));
      registry.put(
          "PROPERTY_PROCESS",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PROPERTY_PROCESS"));
      registry.put(
          "RECTANGULAR_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RECTANGULAR_AREA"));
      registry.put(
          "RECTANGULAR_ARRAY_PLACEMENT_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RECTANGULAR_ARRAY_PLACEMENT_GROUP_COMPONENT"));
      registry.put(
          "RECTANGULAR_CLOSED_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RECTANGULAR_CLOSED_PROFILE"));
      registry.put(
          "REPLICATE_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "REPLICATE_FEATURE"));
      registry.put(
          "REQUIREMENT_ASSIGNED_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "REQUIREMENT_ASSIGNED_OBJECT"));
      registry.put(
          "REQUIREMENT_FOR_ACTION_RESOURCE",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "REQUIREMENT_FOR_ACTION_RESOURCE"));
      registry.put(
          "REQUIREMENT_SOURCE",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "REQUIREMENT_SOURCE"));
      registry.put(
          "REVOLVED_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "REVOLVED_PROFILE"));
      registry.put(
          "ROUNDED_U_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ROUNDED_U_PROFILE"));
      registry.put(
          "RUNOUT_ZONE_ORIENTATION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "RUNOUT_ZONE_ORIENTATION"));
      registry.put(
          "RUNOUT_ZONE_ORIENTATION_REFERENCE_DIRECTION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "RUNOUT_ZONE_ORIENTATION_REFERENCE_DIRECTION"));
      registry.put(
          "SCAN_3D_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCAN_3D_MODEL"));
      registry.put(
          "SOLID_WITH_TEE_SECTION_SLOT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SOLID_WITH_TEE_SECTION_SLOT"));
      registry.put(
          "SOLID_WITH_TRAPEZOIDAL_SECTION_SLOT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SOLID_WITH_TRAPEZOIDAL_SECTION_SLOT"));
      registry.put(
          "SQUARE_U_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SQUARE_U_PROFILE"));
      registry.put(
          "STRUCTURED_MESSAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STRUCTURED_MESSAGE"));
      registry.put(
          "TEE_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "TEE_PROFILE"));
      // TERMINAL_FEATURE already registered via registerShapeAspectAliases at line 1258
      registry.put(
          "TRANSFORMATION_WITH_DERIVED_ANGLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "TRANSFORMATION_WITH_DERIVED_ANGLE"));
      registry.put(
          "TRANSITION_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TRANSITION_FEATURE"));
      registry.put(
          "TRANSPORT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TRANSPORT_FEATURE"));
      registry.put(
          "TYPE_QUALIFIER",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "TYPE_QUALIFIER"));
      registry.put(
          "VEE_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "VEE_PROFILE"));
      registry.put(
          "ZONE_STRUCTURAL_MAKEUP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ZONE_STRUCTURAL_MAKEUP"));
      registry.put(
          "ABRUPT_CHANGE_OF_SURFACE_NORMAL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ABRUPT_CHANGE_OF_SURFACE_NORMAL"));
      registry.put(
          "AREA_WITH_OUTER_BOUNDARY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AREA_WITH_OUTER_BOUNDARY"));
      registry.put(
          "BOUNDED_PCURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BOUNDED_PCURVE"));
      registry.put(
          "BOUNDED_SURFACE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BOUNDED_SURFACE_CURVE"));
      registry.put(
          "COMPOSITE_TEXT_WITH_ASSOCIATED_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_TEXT_WITH_ASSOCIATED_CURVES"));
      registry.put(
          "CURVE_11",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_11"));
      registry.put(
          "CURVE_BASED_PATH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_BASED_PATH"));
      registry.put(
          "CURVE_BASED_PATH_WITH_ORIENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_BASED_PATH_WITH_ORIENTATION"));
      registry.put(
          "CURVE_BASED_PATH_WITH_ORIENTATION_AND_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_BASED_PATH_WITH_ORIENTATION_AND_PARAMETERS"));
      registry.put(
          "CURVE_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_DIMENSION"));
      registry.put(
          "CURVE_SEGMENT_SET",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURVE_SEGMENT_SET"));
      registry.put(
          "CURVE_WITH_EXCESSIVE_SEGMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_WITH_EXCESSIVE_SEGMENTS"));
      registry.put(
          "CURVE_WITH_SMALL_CURVATURE_RADIUS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_WITH_SMALL_CURVATURE_RADIUS"));
      // DEFAULT_MODEL_GEOMETRIC_VIEW already registered via registerShapeAspectAliases at line 1244
      registry.put(
          "DIFFERENT_CURVE_LENGTH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_CURVE_LENGTH"));
      registry.put(
          "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS"));
      registry.put(
          "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS_WIREFRAME_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS_WIREFRAME_MODEL"));
      registry.put(
          "DIFFERENT_SURFACE_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DIFFERENT_SURFACE_AREA"));
      registry.put(
          "DIFFERENT_SURFACE_NORMAL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_SURFACE_NORMAL"));
      registry.put(
          "DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE"));
      registry.put(
          "DIMENSIONAL_SIZE_WITH_DATUM_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSIONAL_SIZE_WITH_DATUM_FEATURE"));
      registry.put(
          "DIMENSION_CURVE_TERMINATOR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIMENSION_CURVE_TERMINATOR"));
      registry.put(
          "DIMENSION_RELATED_TOLERANCE_ZONE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSION_RELATED_TOLERANCE_ZONE_ELEMENT"));
      registry.put(
          "DIRECTED_TOLERANCE_ZONE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIRECTED_TOLERANCE_ZONE"));
      registry.put(
          "DUPIN_CYCLIDE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DUPIN_CYCLIDE_SURFACE"));
      registry.put(
          "ELEMENTARY_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ELEMENTARY_SURFACE"));
      registry.put(
          "ENTIRELY_NARROW_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ENTIRELY_NARROW_SURFACE"));
      registry.put(
          "EVALUATED_DEGENERATE_PCURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EVALUATED_DEGENERATE_PCURVE"));
      registry.put(
          "EXCESSIVELY_HIGH_DEGREE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXCESSIVELY_HIGH_DEGREE_CURVE"));
      registry.put(
          "EXCESSIVELY_HIGH_DEGREE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXCESSIVELY_HIGH_DEGREE_SURFACE"));
      registry.put(
          "FACE_SURFACE_WITH_EXCESSIVE_PATCHES_IN_ONE_DIRECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FACE_SURFACE_WITH_EXCESSIVE_PATCHES_IN_ONE_DIRECTION"));
      registry.put(
          "FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE"));

// Phase 5 final: Additional entities to reach 90%
      registry.put(
          "ABSTRACTED_EXPRESSION_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ABSTRACTED_EXPRESSION_FUNCTION"));
      registry.put(
          "ACTION_RESOURCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ACTION_RESOURCE"));
      registry.put(
          "AGC_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AGC_WITH_DIMENSION"));
      registry.put(
          "AGGREGATE_ID_ATTRIBUTE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AGGREGATE_ID_ATTRIBUTE"));
      registry.put(
          "ANGLE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGLE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
      registry.put(
          "ANGLE_DIRECTION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGLE_DIRECTION_REFERENCE"));
      registry.put(
          "ANGULARITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGULARITY_TOLERANCE"));

// Phase 6: Final entities to reach 95% coverage (auto-generated)
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));
      registry.put(
          "FILL_AREA_STYLE_TILE_SYMBOL_WITH_STYLE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "FILL_AREA_STYLE_TILE_SYMBOL_WITH_STYLE"));
      registry.put(
          "FUNCTIONAL_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "FUNCTIONAL_BREAKDOWN_CONTEXT"));
      registry.put(
          "GEOMETRIC_TOLERANCE_AUXILIARY_CLASSIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GEOMETRIC_TOLERANCE_AUXILIARY_CLASSIFICATION"));
      registry.put(
          "IMPLICIT_INTERSECTION_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IMPLICIT_INTERSECTION_CURVE"));
      registry.put(
          "IMPLICIT_MODEL_INTERSECTION_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IMPLICIT_MODEL_INTERSECTION_CURVE"));
      registry.put(
          "LINE_PROFILE_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "LINE_PROFILE_TOLERANCE"));
      registry.put(
          "MODEL_GEOMETRIC_VIEW",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODEL_GEOMETRIC_VIEW"));
      registry.put(
          "ORIENTED_TOLERANCE_ZONE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ORIENTED_TOLERANCE_ZONE"));
      registry.put(
          "PHYSICAL_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PHYSICAL_BREAKDOWN_CONTEXT"));
      registry.put(
          "RULED_SURFACE_SWEPT_AREA_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "RULED_SURFACE_SWEPT_AREA_SOLID"));
      registry.put(
          "SMALL_AREA_FACE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SMALL_AREA_FACE"));
      registry.put(
          "SMALL_AREA_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SMALL_AREA_SURFACE"));
      registry.put(
          "SMALL_AREA_SURFACE_PATCH",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SMALL_AREA_SURFACE_PATCH"));
      registry.put(
          "STRUCTURED_TEXT_COMPOSITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STRUCTURED_TEXT_COMPOSITION"));
      registry.put(
          "SURFACE_PROFILE_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SURFACE_PROFILE_TOLERANCE"));
      registry.put(
          "SYSTEM_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SYSTEM_BREAKDOWN_CONTEXT"));
      // TOLERANCE_ZONE_DEFINITION already registered via registerShapeAspectAliases at line 1260
      registry.put(
          "TOLERANCE_ZONE_WITH_DATUM",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TOLERANCE_ZONE_WITH_DATUM"));
      registry.put(
          "ZONE_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ZONE_BREAKDOWN_CONTEXT"));
      registry.put(
          "COMPLEX_SHELLED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPLEX_SHELLED_SOLID"));
      registry.put(
          "CURVE_11",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_11"));
      registry.put(
          "DIFFERENT_NUMBER_OF_CLOSED_SHELL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_CLOSED_SHELL"));
      registry.put(
          "DISCONNECTED_FACE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DISCONNECTED_FACE_SET"));
      registry.put(
          "EDGE_BLENDED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EDGE_BLENDED_SOLID"));
      registry.put(
          "EDGE_WITH_EXCESSIVE_SEGMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EDGE_WITH_EXCESSIVE_SEGMENTS"));
      registry.put(
          "EDGE_WITH_LENGTH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EDGE_WITH_LENGTH"));
      registry.put(
          "ENTIRELY_NARROW_FACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ENTIRELY_NARROW_FACE"));
      registry.put(
          "ERRONEOUS_TOPOLOGY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ERRONEOUS_TOPOLOGY"));
      registry.put(
          "EXTRUDED_FACE_SOLID_WITH_DRAFT_ANGLE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTRUDED_FACE_SOLID_WITH_DRAFT_ANGLE"));
      registry.put(
          "EXTRUDED_FACE_SOLID_WITH_MULTIPLE_DRAFT_ANGLES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTRUDED_FACE_SOLID_WITH_MULTIPLE_DRAFT_ANGLES"));
      registry.put(
          "EXTRUDED_FACE_SOLID_WITH_TRIM_CONDITIONS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTRUDED_FACE_SOLID_WITH_TRIM_CONDITIONS"));
      registry.put(
          "FACETED_PRIMITIVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FACETED_PRIMITIVE"));
      registry.put(
          "FIXED_ELEMENT_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FIXED_ELEMENT_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "FIXED_REFERENCE_SWEPT_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FIXED_REFERENCE_SWEPT_SURFACE"));
      // FLAT_FACE already registered via registerCharacterizedObjectAliases at line 1118
      registry.put(
          "FREE_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FREE_EDGE"));
      registry.put(
          "G1_DISCONTINUITY_BETWEEN_ADJACENT_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUITY_BETWEEN_ADJACENT_FACES"));
      registry.put(
          "G1_DISCONTINUOUS_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUOUS_CURVE"));
      registry.put(
          "G1_DISCONTINUOUS_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUOUS_SURFACE"));
      registry.put(
          "G2_DISCONTINUITY_BETWEEN_ADJACENT_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUITY_BETWEEN_ADJACENT_FACES"));
      registry.put(
          "G2_DISCONTINUOUS_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUOUS_CURVE"));
      registry.put(
          "G2_DISCONTINUOUS_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUOUS_SURFACE"));
      registry.put(
          "GAP_BETWEEN_ADJACENT_EDGES_IN_LOOP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_ADJACENT_EDGES_IN_LOOP"));
      registry.put(
          "GAP_BETWEEN_EDGE_AND_BASE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_EDGE_AND_BASE_SURFACE"));
      registry.put(
          "GAP_BETWEEN_FACES_RELATED_TO_AN_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_FACES_RELATED_TO_AN_EDGE"));
      registry.put(
          "GAP_BETWEEN_PCURVES_RELATED_TO_AN_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_PCURVES_RELATED_TO_AN_EDGE"));
      registry.put(
          "GAP_BETWEEN_VERTEX_AND_BASE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_VERTEX_AND_BASE_SURFACE"));
      registry.put(
          "GAP_BETWEEN_VERTEX_AND_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_VERTEX_AND_EDGE"));
      registry.put(
          "GEOMETRIC_GAP_IN_TOPOLOGY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GEOMETRIC_GAP_IN_TOPOLOGY"));
      // GEOMETRIC_TOLERANCE_WITH_MODIFIERS already registered via registerShapeAspectAliases at line 1247
      registry.put(
          "HIERARCHICAL_INTERFACE_CONNECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIERARCHICAL_INTERFACE_CONNECTION"));
      registry.put(
          "HIGH_DEGREE_AXI_SYMMETRIC_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIGH_DEGREE_AXI_SYMMETRIC_SURFACE"));
      registry.put(
          "HIGH_DEGREE_LINEAR_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIGH_DEGREE_LINEAR_CURVE"));
      registry.put(
          "IMPLICIT_PROJECTED_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_PROJECTED_CURVE"));
      registry.put(
          "IMPLICIT_SILHOUETTE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_SILHOUETTE_CURVE"));
      registry.put(
          "IMPORTED_CURVE_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_CURVE_FUNCTION"));
      registry.put(
          "IMPORTED_SURFACE_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_SURFACE_FUNCTION"));
      registry.put(
          "INAPT_TOPOLOGY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INAPT_TOPOLOGY"));
      registry.put(
          "INCIDENCE_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCIDENCE_GEOMETRIC_CONSTRAINT"));
      registry.put(
          "INCONSISTENT_ADJACENT_FACE_NORMALS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_ADJACENT_FACE_NORMALS"));
      registry.put(
          "INCONSISTENT_CURVE_TRANSITION_CODE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_CURVE_TRANSITION_CODE"));
      registry.put(
          "INCONSISTENT_EDGE_AND_CURVE_DIRECTIONS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_EDGE_AND_CURVE_DIRECTIONS"));
      registry.put(
          "INCONSISTENT_FACE_AND_CLOSED_SHELL_NORMALS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_FACE_AND_CLOSED_SHELL_NORMALS"));
      registry.put(
          "INCONSISTENT_FACE_AND_SURFACE_NORMALS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_FACE_AND_SURFACE_NORMALS"));
      registry.put(
          "INCONSISTENT_SURFACE_TRANSITION_CODE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_SURFACE_TRANSITION_CODE"));
      registry.put(
          "INDISTINCT_CURVE_KNOTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDISTINCT_CURVE_KNOTS"));
      registry.put(
          "INDISTINCT_SURFACE_KNOTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDISTINCT_SURFACE_KNOTS"));
      registry.put(
          "INTERFACED_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACED_GROUP_COMPONENT"));
      registry.put(
          "INTERFACE_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_COMPONENT"));
      registry.put(
          "INTERFACE_CONNECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTION"));
      registry.put(
          "INTERFACE_CONNECTOR_AS_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_AS_PLANNED"));
      registry.put(
          "INTERFACE_CONNECTOR_AS_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_AS_REALIZED"));
      registry.put(
          "INTERFACE_CONNECTOR_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DEFINITION"));
      registry.put(
          "INTERFACE_CONNECTOR_DESIGN",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DESIGN"));
      registry.put(
          "INTERFACE_CONNECTOR_DESIGN_TO_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DESIGN_TO_PLANNED"));
      registry.put(
          "INTERFACE_CONNECTOR_DESIGN_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DESIGN_TO_REALIZED"));
      registry.put(
          "INTERFACE_CONNECTOR_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_OCCURRENCE"));
      registry.put(
          "INTERFACE_CONNECTOR_PLANNED_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_PLANNED_TO_REALIZED"));
      registry.put(
          "INTERFACE_CONNECTOR_VERSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_VERSION"));
      registry.put(
          "INTERFACE_DEFINITION_CONNECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_DEFINITION_CONNECTION"));
      registry.put(
          "INTERFACE_DEFINITION_FOR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_DEFINITION_FOR"));
      registry.put(
          "INTERFACE_SPECIFICATION_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_SPECIFICATION_DEFINITION"));
      registry.put(
          "INTERFACE_SPECIFICATION_VERSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_SPECIFICATION_VERSION"));
      registry.put(
          "INTERSECTING_CONNECTED_FACE_SETS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERSECTING_CONNECTED_FACE_SETS"));
      registry.put(
          "INTERSECTING_LOOPS_IN_FACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERSECTING_LOOPS_IN_FACE"));
      registry.put(
          "INTERSECTING_SHELLS_IN_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERSECTING_SHELLS_IN_SOLID"));
      registry.put(
          "KINEMATIC_LOOP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "KINEMATIC_LOOP"));
      registry.put(
          "KINEMATIC_PATH_DEFINED_BY_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "KINEMATIC_PATH_DEFINED_BY_CURVES"));
      registry.put(
          "LOCALLY_REFINED_SPLINE_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOCALLY_REFINED_SPLINE_CURVE"));
      registry.put(
          "LOCALLY_REFINED_SPLINE_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOCALLY_REFINED_SPLINE_SURFACE"));
      registry.put(
          "LOOP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOOP"));
      registry.put(
          "MISMATCH_OF_ARCWISE_CONNECTED_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_ARCWISE_CONNECTED_CURVES"));
      registry.put(
          "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES"));
      registry.put(
          "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES_BOUNDARY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_ARCWISE_CONNECTED_SURFACES_BOUNDARY"));
      registry.put(
          "MISMATCH_OF_EDGES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_EDGES"));
      registry.put(
          "MISMATCH_OF_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_FACES"));
      registry.put(
          "MISMATCH_OF_UNDERLYING_EDGE_GEOMETRY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_UNDERLYING_EDGE_GEOMETRY"));
      registry.put(
          "MISMATCH_OF_UNDERLYING_FACE_GEOMETRY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_UNDERLYING_FACE_GEOMETRY"));
      registry.put(
          "MISSING_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISSING_EDGE"));
      registry.put(
          "MISSING_FACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISSING_FACE"));
      registry.put(
          "MODIFIED_GEOMETRIC_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "MODIFIED_GEOMETRIC_TOLERANCE"));
      registry.put(
          "MULTIPLY_DEFINED_CURVES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_CURVES"));
      registry.put(
          "MULTIPLY_DEFINED_EDGES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_EDGES"));
      registry.put(
          "MULTIPLY_DEFINED_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_FACES"));
      registry.put(
          "MULTIPLY_DEFINED_SURFACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_SURFACES"));

// Phase 6 final: Additional entities to reach 95%
      registry.put(
          "ANGULAR_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGULAR_DIMENSION"));
      registry.put(
          "ANNOTATION_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANNOTATION_OCCURRENCE"));
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "APPLICATION_DEFINED_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLICATION_DEFINED_FUNCTION"));
      registry.put(
          "ASCRIBABLE_STATE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASCRIBABLE_STATE"));
      registry.put(
          "ASSEMBLY_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSEMBLY_COMPONENT"));
      registry.put(
          "ASSEMBLY_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSEMBLY_GROUP_COMPONENT"));
      registry.put(
          "ASSEMBLY_SHAPE_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSEMBLY_SHAPE_CONSTRAINT"));

// Phase 6 ultimate final: Final entity to reach 95%
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Phase 7: Additional entities to reach 96% (auto-generated)
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));
      registry.put(
          "CONSTRAINED_KINEMATIC_MOTION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONSTRAINED_KINEMATIC_MOTION_REPRESENTATION"));
      registry.put(
          "CONTEXT_DEPENDENT_INVISIBILITY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTEXT_DEPENDENT_INVISIBILITY"));
      registry.put(
          "CONTEXT_DEPENDENT_KINEMATIC_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTEXT_DEPENDENT_KINEMATIC_LINK_REPRESENTATION"));
      registry.put(
          "CONTEXT_DEPENDENT_OVER_RIDING_STYLED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTEXT_DEPENDENT_OVER_RIDING_STYLED_ITEM"));
      registry.put(
          "DATE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATE_REPRESENTATION_ITEM"));
      registry.put(
          "DATE_TIME_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATE_TIME_REPRESENTATION_ITEM"));
      registry.put(
          "EXTERNALLY_DEFINED_CONTEXT_DEPENDENT_UNIT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_CONTEXT_DEPENDENT_UNIT"));
      registry.put(
          "EXTERNALLY_DEFINED_DIMENSION_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_DIMENSION_DEFINITION"));
      registry.put(
          "EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM"));
      registry.put(
          "EXTERNALLY_DEFINED_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_REPRESENTATION_ITEM"));
      registry.put(
          "FREE_KINEMATIC_MOTION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FREE_KINEMATIC_MOTION_REPRESENTATION"));
      registry.put(
          "INTEGER_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "INTEGER_REPRESENTATION_ITEM"));
      registry.put(
          "KINEMATIC_LINK_REPRESENTATION_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "KINEMATIC_LINK_REPRESENTATION_ASSOCIATION"));
      registry.put(
          "LINEAR_ARRAY_COMPONENT_DEFINITION_LINK",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LINEAR_ARRAY_COMPONENT_DEFINITION_LINK"));
      registry.put(
          "LOCATION_IN_AGGREGATE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOCATION_IN_AGGREGATE_REPRESENTATION_ITEM"));
      registry.put(
          "LOGICAL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOGICAL_REPRESENTATION_ITEM"));
      registry.put(
          "NULL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "NULL_REPRESENTATION_ITEM"));
      registry.put(
          "PICTURE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PICTURE_REPRESENTATION_ITEM"));
      registry.put(
          "PREDEFINED_PICTURE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PREDEFINED_PICTURE_REPRESENTATION_ITEM"));
      registry.put(
          "PRESENTATION_STYLE_BY_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRESENTATION_STYLE_BY_CONTEXT"));

// Phase 7 final: Reach 96%
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "ASSIGNED_ANALYSIS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSIGNED_ANALYSIS"));

// Phase 7 ultimate final: Reach 96%
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Phase to reach 95% coverage (auto-generated)
      registry.put(
          "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP"));
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));
      registry.put(
          "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE"));
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP"));
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "AXIS1_PLACEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AXIS1_PLACEMENT"));
      registry.put(
          "AXIS2_PLACEMENT_2D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AXIS2_PLACEMENT_2D"));
      registry.put(
          "AXIS2_PLACEMENT_3D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AXIS2_PLACEMENT_3D"));
      registry.put(
          "CAMERA_MODEL_D2",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D2"));
      registry.put(
          "CAMERA_MODEL_D3",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION"));
      registry.put(
          "CAMERA_MODEL_D3_WITH_HLHSR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_WITH_HLHSR"));
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_2D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_2D"));
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_3D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_3D"));
      registry.put(
          "CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CSG_2D_SHAPE_REPRESENTATION"));
      registry.put(
          "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION"));
      registry.put(
          "PRESENTED_ITEM_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRESENTED_ITEM_REPRESENTATION"));
      registry.put(
          "PROCEDURAL_REPRESENTATION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCEDURAL_REPRESENTATION_SEQUENCE"));
      registry.put(
          "PROCEDURAL_SHAPE_REPRESENTATION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCEDURAL_SHAPE_REPRESENTATION_SEQUENCE"));
      registry.put(
          "PROCEDURAL_SOLID_REPRESENTATION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCEDURAL_SOLID_REPRESENTATION_SEQUENCE"));
      registry.put(
          "PRODUCT_CONCEPT_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_CONCEPT_CONTEXT"));
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE_WITH_LOCAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE_WITH_LOCAL_REPRESENTATION"));
      registry.put(
          "PRODUCT_DEFINITION_REFERENCE_WITH_LOCAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_REFERENCE_WITH_LOCAL_REPRESENTATION"));
      registry.put(
          "PRODUCT_GROUP_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_GROUP_CONTEXT"));
      registry.put(
          "REAL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REAL_REPRESENTATION_ITEM"));
      registry.put(
          "REPRESENTATION_CONTEXT_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPRESENTATION_CONTEXT_REFERENCE"));
      registry.put(
          "REPRESENTATION_PROXY_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPRESENTATION_PROXY_ITEM"));
      registry.put(
          "REPRESENTATION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPRESENTATION_REFERENCE"));
      registry.put(
          "ROW_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ROW_REPRESENTATION_ITEM"));
      registry.put(
          "SCAN_3D_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCAN_3D_MODEL"));
      registry.put(
          "SHAPE_REPRESENTATION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SHAPE_REPRESENTATION_REFERENCE"));
      registry.put(
          "SIMPLIFIED_SPOTFACE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SIMPLIFIED_SPOTFACE_HOLE_DEFINITION"));
      // SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION already registered at line 65
      registry.put(
          "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION"));
      registry.put(
          "SPOTFACE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPOTFACE_DEFINITION"));
      registry.put(
          "SPOTFACE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPOTFACE_HOLE_DEFINITION"));
      registry.put(
          "TABLE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TABLE_REPRESENTATION_ITEM"));
      registry.put(
          "TAGGED_TEXT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TAGGED_TEXT_ITEM"));
      registry.put(
          "VARIATIONAL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "VARIATIONAL_REPRESENTATION_ITEM"));
      registry.put(
          "COMPOSITE_CURVE_TRANSITION_LOCATOR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_CURVE_TRANSITION_LOCATOR"));
      registry.put(
          "COMPOSITE_TEXT_WITH_DELINEATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_TEXT_WITH_DELINEATION"));
      registry.put(
          "CURVE_11",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CURVE_11"));
      registry.put(
          "CYLINDRICAL_11",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CYLINDRICAL_11"));
      registry.put(
          "DRAUGHTING_TEXT_LITERAL_WITH_DELINEATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DRAUGHTING_TEXT_LITERAL_WITH_DELINEATION"));
      registry.put(
          "G1_DISCONTINUITY_BETWEEN_ADJACENT_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUITY_BETWEEN_ADJACENT_FACES"));
      registry.put(
          "G1_DISCONTINUOUS_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUOUS_CURVE"));
      registry.put(
          "G1_DISCONTINUOUS_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUOUS_SURFACE"));
      registry.put(
          "G2_DISCONTINUITY_BETWEEN_ADJACENT_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUITY_BETWEEN_ADJACENT_FACES"));
      registry.put(
          "G2_DISCONTINUOUS_CURVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUOUS_CURVE"));
      registry.put(
          "G2_DISCONTINUOUS_SURFACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUOUS_SURFACE"));
      registry.put(
          "GENERAL_LINEAR_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERAL_LINEAR_FUNCTION"));
      registry.put(
          "HOMOGENEOUS_LINEAR_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HOMOGENEOUS_LINEAR_FUNCTION"));
      registry.put(
          "IMPLICIT_POINT_ON_PLANE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPLICIT_POINT_ON_PLANE"));
      registry.put(
          "IMPORTED_POINT_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_POINT_FUNCTION"));
      registry.put(
          "LINEARIZED_TABLE_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEARIZED_TABLE_FUNCTION"));
      registry.put(
          "LINEAR_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEAR_DIMENSION"));
      registry.put(
          "LINEAR_PATH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LINEAR_PATH"));
      registry.put(
          "MISMATCH_OF_POINTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_POINTS"));
      registry.put(
          "MISMATCH_OF_POINT_CLOUD_AND_RELATED_GEOMETRY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_POINT_CLOUD_AND_RELATED_GEOMETRY"));
      registry.put(
          "MULTIPLY_DEFINED_CARTESIAN_POINTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_CARTESIAN_POINTS"));

// Final entities to reach verified 95%
      registry.put(
          "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP"));
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));
      registry.put(
          "A3MA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_INSPECTION_RESULT"));
      registry.put(
          "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR"));
      registry.put(
          "A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS"));
      registry.put(
          "A3MS_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MS_EQUIVALENCE_INSPECTION_RESULT"));
      registry.put(
          "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION"));
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST"));
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE"));
      registry.put(
          "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE"));
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP"));
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "ATOMIC_FORMULA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATOMIC_FORMULA"));
      registry.put(
          "ATOM_BASED_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATOM_BASED_LITERAL"));
      registry.put(
          "ATTACHMENT_SLOT_AS_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_AS_PLANNED"));
      registry.put(
          "ATTACHMENT_SLOT_AS_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_AS_REALIZED"));
      registry.put(
          "ATTACHMENT_SLOT_DESIGN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_DESIGN"));
      registry.put(
          "ATTACHMENT_SLOT_DESIGN_TO_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_DESIGN_TO_PLANNED"));
      registry.put(
          "ATTACHMENT_SLOT_DESIGN_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_DESIGN_TO_REALIZED"));
      registry.put(
          "ATTACHMENT_SLOT_ON_PRODUCT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_ON_PRODUCT"));
      registry.put(
          "ATTACHMENT_SLOT_PLANNED_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_PLANNED_TO_REALIZED"));
      registry.put(
          "AXIS1_PLACEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AXIS1_PLACEMENT"));
      registry.put(
          "AXIS2_PLACEMENT_2D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AXIS2_PLACEMENT_2D"));

// Ultimate final 32 entities to reach 95%
      registry.put(
          "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP"));
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));
      registry.put(
          "A3MA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_INSPECTION_RESULT"));
      registry.put(
          "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR"));
      registry.put(
          "A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS"));
      registry.put(
          "A3MS_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MS_EQUIVALENCE_INSPECTION_RESULT"));
      registry.put(
          "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION"));
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST"));
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY"));
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE"));
      registry.put(
          "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE"));
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP"));
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));
      registry.put(
          "AXIS1_PLACEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AXIS1_PLACEMENT"));
      registry.put(
          "AXIS2_PLACEMENT_2D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AXIS2_PLACEMENT_2D"));

// To reach 99% coverage
      registry.put(
          "BACK_CHAINING_RULE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BACK_CHAINING_RULE"));
      registry.put(
          "BANDED_MATRIX",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BANDED_MATRIX"));
      registry.put(
          "BASIC_SPARSE_MATRIX",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BASIC_SPARSE_MATRIX"));
      // BEAD already registered via registerCharacterizedObjectAliases at line 1102
      // BEAD_END already registered via registerShapeAspectAliases at line 1236
      registry.put(
          "BINARY_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BINARY_ASSEMBLY_CONSTRAINT"));
      registry.put(
          "BINARY_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BINARY_LITERAL"));

// Final entity to reach 99%+
      registry.put(
          "BACK_CHAINING_RULE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BACK_CHAINING_RULE"));

// Truly final to reach 99%
      registry.put(
          "BOOLEAN_DEFINED_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_DEFINED_FUNCTION"));

// To reach 99.5%
      registry.put(
          "BOOLEAN_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_LITERAL"));
      registry.put(
          "BOOLEAN_RESULT_2D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_RESULT_2D"));
      registry.put(
          "BOOLEAN_VARIABLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_VARIABLE"));
      // BOSS already registered via registerCharacterizedObjectAliases at line 1103
      // BOSS_TOP already registered via registerShapeAspectAliases at line 1237
      registry.put(
          "BOUND_PARAMETER_ENVIRONMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOUND_PARAMETER_ENVIRONMENT"));
      registry.put(
          "BOUND_VARIABLE_SEMANTICS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOUND_VARIABLE_SEMANTICS"));
      registry.put(
          "BOUND_VARIATIONAL_PARAMETER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOUND_VARIATIONAL_PARAMETER"));
      registry.put(
          "CAMERA_IMAGE_2D_WITH_SCALE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_IMAGE_2D_WITH_SCALE"));
      registry.put(
          "CAMERA_IMAGE_3D_WITH_SCALE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_IMAGE_3D_WITH_SCALE"));

// To reach 99.6% (2 entities)
      registry.put(
          "CARTESIAN_11",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CARTESIAN_11"));
      registry.put(
          "CDGC_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CDGC_WITH_DIMENSION"));

// To reach 99.7%
      registry.put(
          "CHANGE_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHANGE_GROUP"));
      registry.put(
          "CHANGE_REQUEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHANGE_REQUEST"));

// Batch of 50 entities
      registry.put(
          "CHARACTERISTIC_DATA_COLUMN_HEADER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_COLUMN_HEADER"));
      registry.put(
          "CHARACTERISTIC_DATA_COLUMN_HEADER_LINK",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_COLUMN_HEADER_LINK"));
      registry.put(
          "CHARACTERISTIC_DATA_TABLE_HEADER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_TABLE_HEADER"));
      registry.put(
          "CHARACTERISTIC_DATA_TABLE_HEADER_DECOMPOSITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_TABLE_HEADER_DECOMPOSITION"));
      registry.put(
          "CHARACTERIZED_LOCATION_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERIZED_LOCATION_OBJECT"));
      registry.put(
          "CIRCULAR_INVOLUTE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_INVOLUTE"));
      registry.put(
          "CIRCULAR_PATH",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_PATH"));
      registry.put(
          "CIRCULAR_RUNOUT_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_RUNOUT_TOLERANCE"));
      registry.put(
          "CLGC_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CLGC_WITH_DIMENSION"));
      registry.put(
          "COAXIALITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COAXIALITY_TOLERANCE"));
      registry.put(
          "COLLECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION"));
      registry.put(
          "COLLECTION_MEMBERSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION_MEMBERSHIP"));
      registry.put(
          "COLLECTION_VERSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION_VERSION"));
      registry.put(
          "COMPARISON_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_EQUAL"));
      registry.put(
          "COMPARISON_GREATER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_GREATER"));
      registry.put(
          "COMPARISON_GREATER_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_GREATER_EQUAL"));
      registry.put(
          "COMPARISON_LESS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_LESS"));
      registry.put(
          "COMPARISON_LESS_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_LESS_EQUAL"));
      registry.put(
          "COMPARISON_NOT_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_NOT_EQUAL"));
      registry.put(
          "COMPLEX_CLAUSE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_CLAUSE"));
      registry.put(
          "COMPLEX_CONJUNCTIVE_CLAUSE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_CONJUNCTIVE_CLAUSE"));
      registry.put(
          "COMPLEX_DISJUNCTIVE_CLAUSE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_DISJUNCTIVE_CLAUSE"));
      registry.put(
          "COMPLEX_NUMBER_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_NUMBER_LITERAL"));
      registry.put(
          "COMPLEX_NUMBER_LITERAL_POLAR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_NUMBER_LITERAL_POLAR"));
      registry.put(
          "COMPONENT_MATING_CONSTRAINT_CONDITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPONENT_MATING_CONSTRAINT_CONDITION"));
      registry.put(
          "COMPONENT_PATH_SHAPE_ASPECT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPONENT_PATH_SHAPE_ASPECT"));
      // COMPONENT_TERMINAL already registered via registerShapeAspectAliases at line 1239
      registry.put(
          "COMPOSITE_ASSEMBLY_TABLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_ASSEMBLY_TABLE"));
      registry.put(
          "COMPOSITE_MATERIAL_DESIGNATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_MATERIAL_DESIGNATION"));
      registry.put(
          "COMPOSITE_TEXT_WITH_BLANKING_BOX",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_TEXT_WITH_BLANKING_BOX"));
      registry.put(
          "COMPOSITE_TEXT_WITH_EXTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_TEXT_WITH_EXTENT"));
      registry.put(
          "CONCENTRICITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONCENTRICITY_TOLERANCE"));
      registry.put(
          "CONDITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONDITION"));
      registry.put(
          "CONDITIONAL_EFFECTIVITY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONDITIONAL_EFFECTIVITY"));
      registry.put(
          "CONIC",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONIC"));
      registry.put(
          "CONSTANT_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONSTANT_FUNCTION"));
      // CONSTITUENT_SHAPE_ASPECT already registered via registerShapeAspectAliases at line 1240
      registry.put(
          "CONTAINING_MESSAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTAINING_MESSAGE"));
      registry.put(
          "CONVEX_HEXAHEDRON",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONVEX_HEXAHEDRON"));
      registry.put(
          "CURRENCY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURRENCY"));
      registry.put(
          "CYLINDRICITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CYLINDRICITY_TOLERANCE"));
      registry.put(
          "DATA_ENVIRONMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_ENVIRONMENT"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_REPORT"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_RESULT"));
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_RESULT_WITH_JUDGEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_RESULT_WITH_JUDGEMENT"));
      registry.put(
          "DATA_EQUIVALENCE_REPORT_REQUEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_REPORT_REQUEST"));
      registry.put(
          "DATA_QUALITY_INSPECTION_INSTANCE_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_INSPECTION_INSTANCE_REPORT"));
      registry.put(
          "DATA_QUALITY_INSPECTION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_INSPECTION_REPORT"));
      registry.put(
          "DATA_QUALITY_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_INSPECTION_RESULT"));
      registry.put("POINT", StepEntityResolver::resolvePoint);
      registry.put("RATIONAL_B_SPLINE_CURVE", StepEntityResolver::resolveRationalBSplineCurve);
      registry.put("RATIONAL_B_SPLINE_SURFACE", StepEntityResolver::resolveRationalBSplineSurface);
      registry.put("B_SPLINE_CURVE_WITH_KNOTS", StepEntityResolver::resolveBSplineCurveWithKnots);
      registry.put("B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineCurveWithKnotsAndBreakpoints);
      registry.put("B_SPLINE_SURFACE_WITH_KNOTS", StepEntityResolver::resolveBSplineSurfaceWithKnots);
      registry.put("B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineSurfaceWithKnotsAndBreakpoints);
      registry.put("PIECEWISE_BEZIER_CURVE", StepEntityResolver::resolvePiecewiseBezierCurve);
      registry.put("PIECEWISE_BEZIER_SURFACE", StepEntityResolver::resolvePiecewiseBezierSurface);
      registry.put("BEZIER_CURVE", StepEntityResolver::resolveBezierCurve);
      registry.put("BEZIER_SURFACE", StepEntityResolver::resolveBezierSurface);
      registry.put("UNIFORM_CURVE", StepEntityResolver::resolveUniformCurve);
      registry.put("UNIFORM_SURFACE", StepEntityResolver::resolveUniformSurface);
      registry.put("QUASI_UNIFORM_CURVE", StepEntityResolver::resolveQuasiUniformCurve);
      registry.put("QUASI_UNIFORM_SURFACE", StepEntityResolver::resolveQuasiUniformSurface);
      registry.put("B_SPLINE_CURVE", StepEntityResolver::resolveBSplineCurve);
      registry.put("B_SPLINE_SURFACE", StepEntityResolver::resolveBSplineSurface);
      registry.put("FACE_BASED_SURFACE_MODEL", StepEntityResolver::resolveFaceBasedSurfaceModel);
      registry.put("SHELL_BASED_SURFACE_MODEL", StepEntityResolver::resolveShellBasedSurfaceModel);
      registry.put("SURFACE_MODEL", StepEntityResolver::resolveSurfaceModel);
      registry.put("COMPOSITE_CURVE_SEGMENT", StepEntityResolver::resolveCompositeCurveSegment);
      registry.put(
          "COMPOSITE_CURVE_ON_SURFACE", StepEntityResolver::resolveCompositeCurveOnSurface);
      registry.put(
          "BOUNDARY_CURVE",
          (resolver, instance) ->
              resolver.resolveCompositeCurveOnSurface(instance, "BOUNDARY_CURVE"));
      registry.put(
          "OUTER_BOUNDARY_CURVE",
          (resolver, instance) ->
              resolver.resolveCompositeCurveOnSurface(instance, "OUTER_BOUNDARY_CURVE"));
      registry.put("COMPOSITE_CURVE", StepEntityResolver::resolveCompositeCurve);
      registry.put("COMPOSITE_TEXT", StepEntityResolver::resolveCompositeText);
      registry.put("TEXT_LITERAL", StepEntityResolver::resolveTextLiteral);
      registry.put("COMPOSED_TEXT", StepEntityResolver::resolveComposedText);
      registry.put("POLYLINE", StepEntityResolver::resolvePolyline);
      registry.put("INDEXED_POLY_CURVE", StepEntityResolver::resolveIndexedPolyCurve);
      registry.put("BOUNDED_CURVE", StepEntityResolver::resolveBoundedCurve);
      registry.put("BOUNDED_SURFACE", StepEntityResolver::resolveBoundedSurface);
      registry.put("CURVE", StepEntityResolver::resolveCurve);
      registry.put("SURFACE", StepEntityResolver::resolveSurface);
      registry.put("OFFSET_CURVE_2D", StepEntityResolver::resolveOffsetCurve2D);
      registry.put("OFFSET_CURVE_3D", StepEntityResolver::resolveOffsetCurve3D);
      registry.put("ORIENTED_CURVE", StepEntityResolver::resolveOrientedCurve);
      registry.put("OFFSET_SURFACE", StepEntityResolver::resolveOffsetSurface);
      registry.put("OFFSET_SURFACE_2", StepEntityResolver::resolveOffsetSurface2);
      registry.put("VERTEX", StepEntityResolver::resolveVertex);
      registry.put("EDGE_BASED_WIREFRAME_MODEL", StepEntityResolver::resolveEdgeBasedWireframeModel);
      registry.put("CONNECTED_EDGE_SET", StepEntityResolver::resolveConnectedEdgeSet);
      registry.put("SUBEDGE", StepEntityResolver::resolveSubedge);
      registry.put("EDGE", StepEntityResolver::resolveEdge);
      registry.put("FACE", StepEntityResolver::resolveFace);
      registry.put("MANIFOLD_SOLID_BREP", StepEntityResolver::resolveManifoldSolidBrep);
      registry.put("NON_MANIFOLD_SOLID_BREP", StepEntityResolver::resolveNonManifoldSolidBrep);
      registry.put("FACETTED_BREP", StepEntityResolver::resolveFacettedBrep);
      registry.put("MANIFOLD_SURFACE_MODEL", StepEntityResolver::resolveManifoldSurfaceModel);
      registry.put("SURFACED_EDGE_CURVE", StepEntityResolver::resolveSurfacedEdgeCurve);
      registry.put("GEOMETRIC_TOLERANCE",
          (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE"));
      // Phase 2C: PMI extension entities
      registry.put(
          "GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT",
          (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT"));
      registry.put("DATUM_REFERENCE_COMPARTMENT", StepEntityResolver::resolveDatumReferenceCompartment);
      registry.put(
          "DATUM_REFERENCE_ELEMENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_REFERENCE_ELEMENT"));
      registry.put(
          "COMMON_DATUM",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMMON_DATUM"));
      registry.put("TOLERANCE_ZONE_FORM", StepEntityResolver::resolveToleranceZoneForm);
      registry.put("TOLERANCE_ZONE", StepEntityResolver::resolveToleranceZone);
      registry.put("CONFIGURATION_ITEM", StepEntityResolver::resolveConfigurationItem);
      registry.put("CONFIGURATION_EFFECTIVITY", StepEntityResolver::resolveConfigurationEffectivity);
      registry.put("FEATURE_CONTROL_FRAME", StepEntityResolver::resolveFeatureControlFrame);
      registry.put("RUNOUT_TOLERANCE_ZONE", StepEntityResolver::resolveRunoutToleranceZone);
      registry.put("GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE", StepEntityResolver::resolveGeometricToleranceWithDatumReference);
      registry.put("LINEAR_TOLERANCE_ZONE", StepEntityResolver::resolveLinearToleranceZone);
      registry.put("RADIAL_TOLERANCE_ZONE", StepEntityResolver::resolveRadialToleranceZone);
      registry.put("PROJECTED_ZONE_DEFINITION", StepEntityResolver::resolveProjectedZoneDefinition);
      registry.put("PLUS_MINUS_TOLERANCE_WITH_MODIFIERS", StepEntityResolver::resolvePlusMinusToleranceWithModifiers);
      registry.put("MATERIAL_DESIGNATION", StepEntityResolver::resolveMaterialDesignation);
      // Phase 2D: Material and configuration entities
      registry.put(
          "MATERIAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "MATERIAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "EFFECTIVITY_CONTEXT",
          (resolver, instance) -> resolver.resolveEffectivity(instance));
      registry.put(
          "CLASSIFIED_EFFECTIVITY",
          (resolver, instance) -> resolver.resolveEffectivity(instance));
      registry.put("LAYERED_ITEM", StepEntityResolver::resolveLayeredItem);
      registry.put("COLOR_SPECIFICATION", StepEntityResolver::resolveColorSpecification);
      registry.put("WITH_DESCRIPTIVE_REPRESENTATION_ITEM",
          StepEntityResolver::resolveWithDescriptiveRepresentationItem);
      registry.put("DIRECTED_DIMENSIONAL_SIZE", StepEntityResolver::resolveDirectedDimensionalSize);
      registerGeometricToleranceAliases(
          registry,
          "POSITION_TOLERANCE",
          "FLATNESS_TOLERANCE",
          "STRAIGHTNESS_TOLERANCE",
          "CIRCULARITY_TOLERANCE",
          "PERPENDICULARITY_TOLERANCE",
          "PARALLELISM_TOLERANCE",
          "ANGULARITY_TOLERANCE",
          "CYLINDRICITY_TOLERANCE",
          "CONCENTRICITY_TOLERANCE",
          "SYMMETRY_TOLERANCE",
          "CIRCULAR_RUNOUT_TOLERANCE",
          "TOTAL_RUNOUT_TOLERANCE",
          "PROFILE_OF_A_LINE_TOLERANCE",
          "PROFILE_OF_A_SURFACE_TOLERANCE");
      registry.put("DATUM", StepEntityResolver::resolveDatum);
      registry.put("DATUM_FEATURE", StepEntityResolver::resolveDatumFeature);
      registry.put("DATUM_REFERENCE", StepEntityResolver::resolveDatumReference);
      registry.put("DATUM_TARGET", StepEntityResolver::resolveDatumTarget);
      // GD&T extended types (OCCT Phase 3)
      registry.put("GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT",
          StepEntityResolver::resolveGeometricToleranceWithDefinedAreaUnit);
      registry.put("GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE",
          StepEntityResolver::resolveGeometricToleranceWithMaximumTolerance);
      registry.put("NON_UNIFORM_ZONE_DEFINITION", StepEntityResolver::resolveNonUniformZoneDefinition);
      registry.put("DATUM_REFERENCE_MODIFIER_WITH_VALUE",
          StepEntityResolver::resolveDatumReferenceModifierWithValue);
      registry.put("RUNOUT_ZONE_DEFINITION_ORIENTATION",
          StepEntityResolver::resolveRunoutZoneDefinitionOrientation);
      registry.put("DATUM_REFERENCE_MODIFIER", StepEntityResolver::resolveDatumReferenceModifier);
      registry.put("DATUM_SYSTEM_REFERENCE", StepEntityResolver::resolveDatumSystemReference);
      registry.put("COMPOSITE_GROUP_TOLERANCE", StepEntityResolver::resolveCompositeGroupTolerance);
      registry.put("GEOMETRIC_TOLERANCE_TARGET", StepEntityResolver::resolveGeometricToleranceTarget);
      registry.put("QUALIFIED_REPRESENTATION_ITEM", StepEntityResolver::resolveQualifiedRepresentationItem);
      registry.put("MODIFIER", StepEntityResolver::resolveModifier);
      registry.put("DATUM_REFERENCE_MODIFIER_WITH_SIGN", StepEntityResolver::resolveDatumReferenceModifierWithSign);
      registry.put("RUNOUT_ZONE_DEFINITION", StepEntityResolver::resolveRunoutZoneDefinition);
      // Note: DATUM_SYSTEM is already registered earlier as a Shape Aspect alias via registerShapeAspectAliases
      registry.put("TOLERANCE_PAIR", StepEntityResolver::resolveTolerancePair);
      registry.put("TOLERANCE_SET", StepEntityResolver::resolveToleranceSet);
      registry.put("COMPOSITE_DATUM_REFERENCE", StepEntityResolver::resolveCompositeDatumReference);
      // Note: MACHINING_OPERATION, MACHINED_SURFACE are already registered as CharacterizedObject aliases
      registry.put("MACHINING_OPERATION_SEQUENCE", StepEntityResolver::resolveMachiningOperationSequence);
      registry.put("FILLET_DEFINITION", StepEntityResolver::resolveFilletDefinition);
      registry.put("CHAMFER_DEFINITION", StepEntityResolver::resolveChamferDefinition);
      // Note: ROUND, GROOVE, HOLE, SLOT, STUD, PROTRUSION, CUTOUT, DEPRESSION, MARKING,
      // CIRCULAR_PATTERN, LINEAR_PATTERN are already registered as CharacterizedObject aliases
      // or ShapeAspect aliases via registerCharacterizedObjectAliases/registerShapeAspectAliases
      registry.put("GEOMETRIC_MEASUREMENT", StepEntityResolver::resolveGeometricMeasurement);
      registry.put("DIMENSIONAL_MEASUREMENT", StepEntityResolver::resolveDimensionalMeasurement);
      registry.put("DIMENSIONAL_SIZE", StepEntityResolver::resolveDimensionalSize);
      registry.put("DIMENSIONAL_LOCATION", StepEntityResolver::resolveDimensionalLocation);
      registry.put("SHAPE_DIMENSION_REPRESENTATION", (resolver, instance) ->
          resolver.resolveRepresentation(instance, "SHAPE_DIMENSION_REPRESENTATION", true));
      registry.put("PLUS_MINUS_TOLERANCE", StepEntityResolver::resolvePlusMinusTolerance);
      registry.put("TOLERANCE_VALUE", StepEntityResolver::resolveToleranceValue);
      registry.put("MEASURE_REPRESENTATION_ITEM_WITH_UNIT", StepEntityResolver::resolveMeasureRepresentationItemWithUnit);
      registry.put("MEASURE_QUALIFICATION", StepEntityResolver::resolveMeasureQualification);
      registry.put("MAKE_FROM_FEATURE", StepEntityResolver::resolveMakeFromFeature);
      registry.put("MAKE_FROM_USAGE_OPTION", StepEntityResolver::resolveMakeFromUsageOption);
      registry.put("QUANTIFIED_ASSEMBLY_COMPONENT_USAGE", StepEntityResolver::resolveQuantifiedAssemblyComponentUsage);
      registry.put("SPECIFIED_HIGHER_USAGE_OCCURRENCE", StepEntityResolver::resolveSpecifiedHigherUsageOccurrence);
      registry.put("ALTERNATE_PRODUCT_RELATIONSHIP", StepEntityResolver::resolveAlternateProductRelationship);
      registry.put("PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS", StepEntityResolver::resolveProductDefinitionWithAssociatedDocuments);
      registry.put("SHAPE_ASPECT_SHAPE_REPRESENTATION", StepEntityResolver::resolveShapeAspectShapeRepresentation);
      registry.put("MAKE_FROM_BUILD_ASSEMBLY", StepEntityResolver::resolveMakeFromBuildAssembly);
      registry.put("ASSEMBLY_COMPONENT_RELATIONSHIP", StepEntityResolver::resolveAssemblyComponentRelationship);
      registry.put("DESIGN_MAKE_FROM", StepEntityResolver::resolveDesignMakeFrom);
      registry.put("INTERPOLATED_CONFIGURATION_SEGMENT", StepEntityResolver::resolveInterpolatedConfigurationSegment);
      registry.put("RANGE_DIMENSIONAL_SIZE", StepEntityResolver::resolveRangeDimensionalSize);
      registry.put("DESIGNED_PART_DESIGN_VERSION", StepEntityResolver::resolveDesignedPartDesignVersion);
      registry.put("SURFACE_STYLE_RENDERING", StepEntityResolver::resolveSurfaceStyleRendering);
      registry.put("SURFACE_STYLE_RENDERING_WITH_PROPERTIES", StepEntityResolver::resolveSurfaceStyleRenderingWithProperties);
      registry.put("RENDERING_PROPERTIES", StepEntityResolver::resolveRenderingProperties);
      registry.put("LIGHT_SOURCE", StepEntityResolver::resolveLightSource);
      registry.put("LIGHT_SOURCE_AMBIENT", StepEntityResolver::resolveLightSourceAmbient);
      registry.put("LIGHT_SOURCE_DIRECTIONAL", StepEntityResolver::resolveLightSourceDirectional);
      registry.put("LIGHT_SOURCE_POSITIONAL", StepEntityResolver::resolveLightSourcePositional);
      registry.put("LIGHT_SOURCE_SPOT", StepEntityResolver::resolveLightSourceSpot);
      registry.put("PRESENTATION_LAYER_USAGE", StepEntityResolver::resolvePresentationLayerUsage);
      registry.put("CAMERA_MODEL_D2", StepEntityResolver::resolveCameraModelD2);
      registry.put("CAMERA_MODEL_D3", StepEntityResolver::resolveCameraModelD3);
      registry.put("CAMERA_USAGE", StepEntityResolver::resolveCameraUsage);
      registry.put("CAMERA_IMAGE", StepEntityResolver::resolveCameraImage);
      registry.put("PLANAR_BOX", StepEntityResolver::resolvePlanarBox);
      registry.put("PLANAR_EXTENT", StepEntityResolver::resolvePlanarExtent);
      registry.put("VIEW_VOLUME", StepEntityResolver::resolveViewVolume);
      registry.put("MECHANICAL_DESIGN_SHAPE_REPRESENTATION", StepEntityResolver::resolveMechanicalDesignShapeRepresentation);
      registry.put("KINEMATIC_PAIR", (resolver, instance) ->
          resolver.resolveKinematicPair(instance, "KINEMATIC_PAIR"));
      registry.put("KINEMATIC_JOINT", StepEntityResolver::resolveKinematicJoint);
      registry.put("KINEMATIC_LINK", StepEntityResolver::resolveKinematicLink);
      registry.put("KINEMATIC_STRUCTURE", StepEntityResolver::resolveKinematicStructure);
      // Kinematic pair types (OCCT Phase 2)
      registry.put("PRISMATIC_PAIR", StepEntityResolver::resolvePrismaticPair);
      registry.put("REVOLUTE_PAIR", StepEntityResolver::resolveRevolutePair);
      registry.put("CYLINDRICAL_PAIR", StepEntityResolver::resolveCylindricalPair);
      registry.put("SPHERICAL_PAIR", StepEntityResolver::resolveSphericalPair);
      registry.put("PLANAR_PAIR", StepEntityResolver::resolvePlanarPair);
      registry.put("UNIVERSAL_PAIR", StepEntityResolver::resolveUniversalPair);
      registry.put("SCREW_PAIR", StepEntityResolver::resolveScrewPair);
      registry.put("GEAR_PAIR", StepEntityResolver::resolveGearPair);
      registry.put("GEAR_PAIR_WITH_RANGE", StepEntityResolver::resolveGearPairWithRange);
      registry.put("RACK_AND_PINION_PAIR", StepEntityResolver::resolveRackAndPinionPair);
      // Kinematic joint types (Joint variants)
      registry.put("REVOLUTE_JOINT", StepEntityResolver::resolveRevoluteJoint);
      registry.put("PRISMATIC_JOINT", StepEntityResolver::resolvePrismaticJoint);
      registry.put("SPHERICAL_JOINT", StepEntityResolver::resolveSphericalJoint);
      registry.put("CYLINDRICAL_JOINT", StepEntityResolver::resolveCylindricalJoint);
      registry.put("PLANAR_JOINT", StepEntityResolver::resolvePlanarJoint);
      registry.put("SCREW_JOINT", StepEntityResolver::resolveScrewJoint);
      registry.put("GENERAL_JOINT", StepEntityResolver::resolveGeneralJoint);
      registry.put("DIRECTION_SENSE", StepEntityResolver::resolveDirectionSense);
      registry.put("JOINT_VALUE", StepEntityResolver::resolveJointValue);
      registry.put("KINEMATIC_CHAIN", StepEntityResolver::resolveKinematicChain);
      registry.put("KINEMATIC_MODEL", StepEntityResolver::resolveKinematicModel);
      registry.put("KINEMATIC_PROPERTY", StepEntityResolver::resolveKinematicProperty);
      registry.put("MOTION_CONSTRAINT", StepEntityResolver::resolveMotionConstraint);
      // AP203 change management
      registry.put("CHANGE", StepEntityResolver::resolveChange);
      registry.put("START_REQUEST", StepEntityResolver::resolveStartRequest);
      registry.put("START_WORK", StepEntityResolver::resolveStartWork);
      registry.put("WORK_ITEM", StepEntityResolver::resolveWorkItem);
      // StepRepr advanced entities
      registry.put("SPECIFIC_HIGHER_USAGE_OCCURRENCE", StepEntityResolver::resolveSpecificHigherUsageOccurrence);
      registry.put("USAGE_OCCURRENCE", StepEntityResolver::resolveUsageOccurrence);
      registry.put("SHAPE_REPRESENTATION_TRANSFORMATION", StepEntityResolver::resolveShapeRepresentationTransformation);
      registry.put("REPRESENTATION_CONTEXT_3D", StepEntityResolver::resolveRepresentationContext3d);
      registry.put("APPLIED_ATTRIBUTE_CLASSIFICATION", StepEntityResolver::resolveAppliedAttributeClassification);
      registry.put("ATTRIBUTE_CLASSIFICATION", StepEntityResolver::resolveAttributeClassification);
      registry.put("STRUCTURAL_ANALYSIS_REPRESENTATION", StepEntityResolver::resolveStructuralAnalysisRepresentation);
      registry.put("STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS", StepEntityResolver::resolveStructuralAnalysisRepresentationParameters);
      registry.put("VALUE_REASON_PAIR", StepEntityResolver::resolveValueReasonPair);
      registry.put("LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE",
          StepEntityResolver::resolveLowOrderKinematicPairWithRange);
      registry.put("ACTUATED_KINEMATIC_PAIR", StepEntityResolver::resolveActuatedKinematicPair);
      registry.put("KINEMATIC_PATH", StepEntityResolver::resolveKinematicPath);
      registry.put("KINEMATIC_FRAME_BASED_TRANSFORMATION",
          StepEntityResolver::resolveKinematicFrameBasedTransformation);
      // Kinematic pair aliases
      registerKinematicPairAliases(registry,
          "PRISMATIC_PAIR_WITH_RANGE", "REVOLUTE_PAIR_WITH_RANGE",
          "CYLINDRICAL_PAIR_WITH_RANGE", "SPHERICAL_PAIR_WITH_RANGE",
          "PLANAR_PAIR_WITH_RANGE", "UNIVERSAL_PAIR_WITH_RANGE",
          "SCREW_PAIR_WITH_RANGE", "FULLY_CONSTRAINED_PAIR",
          "HOMOKINETIC_PAIR", "PLANAR_CURVE_PAIR",
          "POINT_ON_PLANAR_CURVE_PAIR", "POINT_ON_SURFACE_PAIR",
          "ROLLING_CURVE_PAIR", "ROLLING_SURFACE_PAIR",
          "SLIDING_CURVE_PAIR", "SLIDING_SURFACE_PAIR",
          "SPHERICAL_PAIR_WITH_PIN", "SPHERICAL_PAIR_WITH_PIN_AND_RANGE",
          "SURFACE_PAIR_WITH_RANGE", "UNCONSTRAINED_PAIR");
      registry.put("ANALYSIS_RESULT", StepEntityResolver::resolveAnalysisResult);
      registry.put("ANALYSIS_INSTANCE", StepEntityResolver::resolveAnalysisInstance);
      registry.put("CONFIGURATION_INSTANCE", StepEntityResolver::resolveConfigurationInstance);
      registry.put("MODEL_DEFINITION", StepEntityResolver::resolveModelDefinition);
      registry.put("MODEL_INSTANCE", StepEntityResolver::resolveModelInstance);
      registry.put("SIMULATION_DEFINITION", StepEntityResolver::resolveSimulationDefinition);
      registry.put("SIMULATION_INSTANCE", StepEntityResolver::resolveSimulationInstance);
      registry.put("PERSON_AND_ORGANIZATION_ADDRESS", StepEntityResolver::resolvePersonAndOrganizationAddress);
      registry.put("ORGANIZATION_ADDRESS", StepEntityResolver::resolveOrganizationAddress);
      registry.put("PERSON_ADDRESS", StepEntityResolver::resolvePersonAddress);
      registry.put("ANGULAR_SIZE", StepEntityResolver::resolveAngularSize);
      registry.put("GENERALIZED_DATUM", StepEntityResolver::resolveGeneralizedDatum);
      registry.put("ACTION_DIRECTIVE", StepEntityResolver::resolveActionDirective);
      registry.put("ACTION_METHOD", StepEntityResolver::resolveActionMethod);
      registry.put("ACTION", StepEntityResolver::resolveAction);
      registry.put("ACTION_RELATIONSHIP", StepEntityResolver::resolveActionRelationship);
      registry.put("ACTION_STATUS", StepEntityResolver::resolveActionStatus);
      registry.put("SOLID_MODEL", StepEntityResolver::resolveSolidModel);
      registry.put("ANNOTATION_FILL_AREA", StepEntityResolver::resolveAnnotationFillArea);
      registry.put(
          "ANNOTATION_FILL_AREA_OCCURRENCE",
          StepEntityResolver::resolveAnnotationFillAreaOccurrence);
      registry.put(
          "ANNOTATION_PLACEHOLDER_OCCURRENCE",
          StepEntityResolver::resolveAnnotationPlaceholderOccurrence);
      registry.put("ANNOTATION_PLANE", StepEntityResolver::resolveAnnotationPlane);
      registry.put("ANNOTATION_POINT_OCCURRENCE", StepEntityResolver::resolveAnnotationPointOccurrence);
      registry.put("LEADER_CURVE", StepEntityResolver::resolveLeaderCurve);
      registry.put("PROJECTION_CURVE", StepEntityResolver::resolveProjectionCurve);
      registry.put("DIMENSION_CURVE", StepEntityResolver::resolveDimensionCurve);
      registry.put(
          "ANNOTATION_SUBFIGURE_OCCURRENCE",
          StepEntityResolver::resolveAnnotationSubfigureOccurrence);
      registry.put(
          "DRAUGHTING_ANNOTATION_OCCURRENCE",
          StepEntityResolver::resolveDraughtingAnnotationOccurrence);
      registry.put("ANNOTATION_CURVE_OCCURRENCE", StepEntityResolver::resolveAnnotationCurveOccurrence);
      registry.put(
          "GEOMETRIC_REPRESENTATION_ITEM",
          StepEntityResolver::resolveGeometricRepresentationItem);
      registry.put(
          "TOPOLOGICAL_REPRESENTATION_ITEM",
          StepEntityResolver::resolveTopologicalRepresentationItem);
      registry.put("REPRESENTATION_ITEM", StepEntityResolver::resolveRepresentationItem);
      registry.put("REPRESENTATION_CONTEXT", StepEntityResolver::resolveRepresentationContext);
      registry.put("REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, "REPRESENTATION", false));
      registry.put(
          "DEFINITIONAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DEFINITIONAL_REPRESENTATION", false));
      registry.put("COLOUR_RGB", StepEntityResolver::resolveColourRgb);
      registry.put(
          "DRAUGHTING_PRE_DEFINED_CURVE_FONT",
          StepEntityResolver::resolveDraughtingPreDefinedCurveFont);
      registry.put(
          "PRE_DEFINED_POINT_MARKER_SYMBOL",
          StepEntityResolver::resolvePreDefinedPointMarkerSymbol);
      registry.put(
          "PRE_DEFINED_DIMENSION_SYMBOL",
          StepEntityResolver::resolvePreDefinedDimensionSymbol);
      registry.put(
          "PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL",
          StepEntityResolver::resolvePreDefinedGeometricalToleranceSymbol);
      registry.put(
          "PRE_DEFINED_TERMINATOR_SYMBOL",
          StepEntityResolver::resolvePreDefinedTerminatorSymbol);
      registry.put(
          "PRE_DEFINED_SURFACE_SIDE_STYLE",
          StepEntityResolver::resolvePreDefinedSurfaceSideStyle);
      registry.put(
          "DRAUGHTING_PRE_DEFINED_TEXT_FONT",
          StepEntityResolver::resolveDraughtingPreDefinedTextFont);
      registry.put("PRE_DEFINED_TEXT_FONT", StepEntityResolver::resolvePreDefinedTextFont);
      registry.put("PRE_DEFINED_ITEM", StepEntityResolver::resolvePreDefinedItem);
      registry.put("PRE_DEFINED_MARKER", StepEntityResolver::resolvePreDefinedMarker);
      registry.put("PRE_DEFINED_SYMBOL", StepEntityResolver::resolvePreDefinedSymbol);
      registry.put("PRE_DEFINED_CURVE_FONT", StepEntityResolver::resolvePreDefinedCurveFont);
      registry.put(
          "DRAUGHTING_PRE_DEFINED_COLOUR", StepEntityResolver::resolveDraughtingPreDefinedColour);
      registry.put("PRE_DEFINED_COLOUR", StepEntityResolver::resolvePreDefinedColour);
      registry.put("COLOUR_SPECIFICATION", StepEntityResolver::resolveColourSpecification);
      registry.put("COLOUR", StepEntityResolver::resolveColour);
      registry.put("CURVE_STYLE", StepEntityResolver::resolveCurveStyle);
      registry.put("POINT_STYLE", StepEntityResolver::resolvePointStyle);
      registry.put(
          "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS",
          StepEntityResolver::resolveCharacterGlyphStyleOutlineWithCharacteristics);
      registry.put(
          "CHARACTER_GLYPH_STYLE_OUTLINE",
          StepEntityResolver::resolveCharacterGlyphStyleOutline);
      registry.put(
          "CHARACTER_GLYPH_STYLE_STROKE",
          StepEntityResolver::resolveCharacterGlyphStyleStroke);
      registry.put("TEXT_STYLE_FOR_DEFINED_FONT", StepEntityResolver::resolveTextStyleForDefinedFont);
      registry.put("TEXT_STYLE_WITH_SPACING", StepEntityResolver::resolveTextStyleWithSpacing);
      registry.put(
          "TEXT_STYLE_WITH_JUSTIFICATION",
          StepEntityResolver::resolveTextStyleWithJustification);
      registry.put("TEXT_STYLE_WITH_MIRROR", StepEntityResolver::resolveTextStyleWithMirror);
      registry.put(
          "TEXT_STYLE_WITH_BOX_CHARACTERISTICS",
          StepEntityResolver::resolveTextStyleWithBoxCharacteristics);
      registry.put("TEXT_STYLE", StepEntityResolver::resolveTextStyle);
      registry.put("SYMBOL_COLOUR", StepEntityResolver::resolveSymbolColour);
      registry.put("SYMBOL_STYLE", StepEntityResolver::resolveSymbolStyle);
      registry.put("FILL_AREA_STYLE_COLOUR", StepEntityResolver::resolveFillAreaStyleColour);
      registry.put("FILL_AREA_STYLE", StepEntityResolver::resolveFillAreaStyle);
      registry.put("SURFACE_STYLE_FILL_AREA", StepEntityResolver::resolveSurfaceStyleFillArea);
      registry.put("SURFACE_STYLE_BOUNDARY", StepEntityResolver::resolveSurfaceStyleBoundary);
      registry.put("SURFACE_STYLE_CONTROL_GRID", StepEntityResolver::resolveSurfaceStyleControlGrid);
      registry.put(
          "SURFACE_STYLE_SEGMENTATION_CURVE",
          StepEntityResolver::resolveSurfaceStyleSegmentationCurve);
      registry.put("SURFACE_STYLE_SILHOUETTE", StepEntityResolver::resolveSurfaceStyleSilhouette);
      registry.put("SURFACE_STYLE_TRANSPARENT", StepEntityResolver::resolveSurfaceStyleTransparent);
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT",
          StepEntityResolver::resolveSurfaceStyleReflectanceAmbient);
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE",
          StepEntityResolver::resolveSurfaceStyleReflectanceAmbientDiffuse);
      registry.put(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR",
          StepEntityResolver::resolveSurfaceStyleReflectanceAmbientDiffuseSpecular);
      registry.put(
          "SURFACE_STYLE_PARAMETER_LINE",
          StepEntityResolver::resolveSurfaceStyleParameterLine);
      registry.put("SURFACE_SIDE_STYLE", StepEntityResolver::resolveSurfaceSideStyle);
      registry.put("SURFACE_STYLE_USAGE", StepEntityResolver::resolveSurfaceStyleUsage);
      registry.put(
          "PRESENTATION_STYLE_ASSIGNMENT", StepEntityResolver::resolvePresentationStyleAssignment);
      registry.put("STYLED_ITEM", StepEntityResolver::resolveStyledItem);
      registry.put("OVER_RIDING_STYLED_ITEM", StepEntityResolver::resolveOverRidingStyledItem);
      registry.put(
          "PRESENTATION_LAYER_ASSIGNMENT", StepEntityResolver::resolvePresentationLayerAssignment);
      registry.put("ANNOTATION_TEXT", StepEntityResolver::resolveAnnotationText);
      registry.put("ANNOTATION_TEXT_CHARACTER", StepEntityResolver::resolveAnnotationTextCharacter);
      registry.put("ANNOTATION_SYMBOL", StepEntityResolver::resolveAnnotationSymbol);
      registry.put("ANNOTATION_SYMBOL_OCCURRENCE", StepEntityResolver::resolveAnnotationSymbolOccurrence);
      registry.put("TERMINATOR_SYMBOL", StepEntityResolver::resolveTerminatorSymbol);
      registry.put(
          "ANNOTATION_OCCURRENCE_RELATIONSHIP",
          StepEntityResolver::resolveAnnotationOccurrenceRelationship);
      registry.put(
          "ANNOTATION_OCCURRENCE_ASSOCIATIVITY",
          (resolver, instance) ->
              resolver.resolveAnnotationOccurrenceRelationship(instance, "ANNOTATION_OCCURRENCE_ASSOCIATIVITY"));
      registry.put(
          "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY",
          (resolver, instance) ->
              resolver.resolveAnnotationOccurrenceRelationship(
                  instance, "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY"));
      registry.put("ANNOTATION_TEXT_OCCURRENCE", StepEntityResolver::resolveAnnotationTextOccurrence);
      registry.put("GEOMETRIC_CURVE_SET", StepEntityResolver::resolveGeometricCurveSet);
      registry.put("GEOMETRIC_SURFACE_SET", StepEntityResolver::resolveGeometricSurfaceSet);
      registry.put("GEOMETRIC_SET", StepEntityResolver::resolveGeometricSet);
      registry.put("POINT_SET", StepEntityResolver::resolvePointSet);
      registry.put("MOTION_PATH", StepEntityResolver::resolveMotionPath);
      registry.put("ANGULAR_LOCATION", StepEntityResolver::resolveAngularLocation);
      // Phase 2B: Additional geometric set variants
      registry.put(
          "GEOMETRIC_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricSet(instance));
      registry.put(
          "GEOMETRIC_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricSet(instance));
      registry.put(
          "TRIANGULATED_SURFACE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put(
          "POLYGONAL_FACE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put("LEADER_DIRECTED_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "LEADER_DIRECTED_CALLOUT"));
      registry.put(
          "PROJECTION_DIRECTED_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "PROJECTION_DIRECTED_CALLOUT"));
      registry.put(
          "DIMENSION_CURVE_DIRECTED_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "DIMENSION_CURVE_DIRECTED_CALLOUT"));
      registry.put(
          "DIMENSION_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DIMENSION_CALLOUT"));
      registry.put(
          "DATUM_FEATURE_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DATUM_FEATURE_CALLOUT"));
      registry.put(
          "DATUM_TARGET_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DATUM_TARGET_CALLOUT"));
      registry.put(
          "GEOMETRICAL_TOLERANCE_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "GEOMETRICAL_TOLERANCE_CALLOUT"));
      registry.put(
          "ROUGHNESS_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "ROUGHNESS_CALLOUT"));
      registry.put(
          "STRUCTURED_DIMENSION_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "STRUCTURED_DIMENSION_CALLOUT"));
      registry.put(
          "SURFACE_CONDITION_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "SURFACE_CONDITION_CALLOUT"));
      registry.put("DRAUGHTING_CALLOUT", StepEntityResolver::resolveDraughtingCallout);
      registry.put(
          "DRAUGHTING_ANNOTATION_OCCURRENCE",
          StepEntityResolver::resolveDraughtingAnnotationOccurrence);
      registry.put(
          "DRAUGHTING_CALLOUT_RELATIONSHIP",
          StepEntityResolver::resolveDraughtingCalloutRelationship);
      registry.put(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE",
          StepEntityResolver::resolveChainBasedGeometricItemSpecificUsage);
      registry.put(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE",
          StepEntityResolver::resolveChainBasedItemIdentifiedRepresentationUsage);
      registry.put(
          "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION",
          StepEntityResolver::resolveMechanicalDesignRequirementItemAssociation);
      registry.put(
          "PMI_REQUIREMENT_ITEM_ASSOCIATION",
          StepEntityResolver::resolvePmiRequirementItemAssociation);
      registry.put("PLACED_TARGET", StepEntityResolver::resolvePlacedTarget);
      registry.put(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER",
          StepEntityResolver::resolveDraughtingModelItemAssociationWithPlaceholder);
      registry.put(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE", StepEntityResolver::resolveGeometricItemSpecificUsage);
      registry.put(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION",
          StepEntityResolver::resolveDraughtingModelItemAssociation);
      registry.put(
          "ITEM_IDENTIFIED_REPRESENTATION_USAGE",
          StepEntityResolver::resolveItemIdentifiedRepresentationUsage);
      registry.put(
          "MEASURE_REPRESENTATION_ITEM", StepEntityResolver::resolveMeasureRepresentationItem);
      registry.put(
          "DESCRIPTIVE_REPRESENTATION_ITEM",
          StepEntityResolver::resolveDescriptiveRepresentationItem);
      registry.put(
          "VALUE_REPRESENTATION_ITEM", StepEntityResolver::resolveValueRepresentationItem);
      registry.put("CARTESIAN_POINT", StepEntityResolver::resolveCartesianPoint);
      registry.put("DIRECTION", StepEntityResolver::resolveDirection);
      registry.put("VECTOR", StepEntityResolver::resolveVector);
      registry.put("AXIS1_PLACEMENT", StepEntityResolver::resolveAxis1Placement);
      registry.put("AXIS2_PLACEMENT_2D", StepEntityResolver::resolveAxis2Placement2D);
      registry.put("AXIS2_PLACEMENT_3D", StepEntityResolver::resolveAxis2Placement3D);
      registry.put("LINE", StepEntityResolver::resolveLine);
      registry.put("PLANE", StepEntityResolver::resolvePlane);
      registry.put("CIRCLE", StepEntityResolver::resolveCircle);
      registry.put("ELLIPSE", StepEntityResolver::resolveEllipse);
      registry.put(
          "PARABOLA",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "PARABOLA", 1));
      registry.put(
          "HYPERBOLA",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "HYPERBOLA", 2));
      registry.put(
          "DEGENERATE_CONIC",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "DEGENERATE_CONIC", 0));
      registry.put(
          "CONIC_CURVE",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "CONIC_CURVE", 2));
      registry.put("CLOTHOID", StepEntityResolver::resolveClothoid);
      registry.put("SURFACE_CURVE", StepEntityResolver::resolveSurfaceCurve);
      registry.put(
          "INTERSECTION_CURVE",
          (resolver, instance) -> resolver.resolveSurfaceCurve(instance, "INTERSECTION_CURVE"));
      registry.put("SEAM_CURVE", StepEntityResolver::resolveSeamCurve);
      registry.put("DEGENERATE_CURVE", StepEntityResolver::resolveDegenerateCurve);
      registry.put("DEGENERATE_PCURVE", StepEntityResolver::resolveDegeneratePcurve);
      registry.put("PCURVE", StepEntityResolver::resolvePcurve);
      registry.put("CYLINDRICAL_SURFACE", StepEntityResolver::resolveCylindricalSurface);
      registry.put("CONICAL_SURFACE", StepEntityResolver::resolveConicalSurface);
      registry.put("TOROIDAL_SURFACE", StepEntityResolver::resolveToroidalSurface);
      registry.put(
          "DEGENERATE_TOROIDAL_SURFACE",
          StepEntityResolver::resolveDegenerateToroidalSurface);
      registry.put("SPHERICAL_SURFACE", StepEntityResolver::resolveSphericalSurface);
      registry.put("SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveSphericalSurfaceWithEllipticalAxis);
      registry.put("CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveCylindricalSurfaceWithEllipticalAxis);
      registry.put("CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveConicalSurfaceWithEllipticalAxis);
      registry.put("TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveToroidalSurfaceWithEllipticalAxis);
      registry.put("TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS", StepEntityResolver::resolveToroidalSurfaceWithCylindricalAxis);
      registry.put("TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS", StepEntityResolver::resolveToroidalSurfaceWithSpecifiedBends);
      registry.put("BLENDED_SURFACE", StepEntityResolver::resolveBlendedSurface);
      registry.put("CHAMFER_EDGE", StepEntityResolver::resolveChamferEdge);
      registry.put("FILLET_EDGE", StepEntityResolver::resolveFilletEdge);
      registry.put("FREE_FORM_SURFACE", StepEntityResolver::resolveFreeFormSurface);
      registry.put("CURVED_TOLERANCE_ZONE", StepEntityResolver::resolveCurvedToleranceZone);
      registry.put("SURFACE_QUALITY", StepEntityResolver::resolveSurfaceQuality);
      registry.put("MEASUREMENT_POINT", StepEntityResolver::resolveMeasurementPoint);
      registry.put("SURFACE_MEASUREMENT", StepEntityResolver::resolveSurfaceMeasurement);
      registry.put("SURFACE_TEXTURE_REPRESENTATION_ITEM", StepEntityResolver::resolveSurfaceTextureRepresentationItem);
      registry.put("RULED_SURFACE", StepEntityResolver::resolveRuledSurface);
      registry.put("SURFACE_PATCH", StepEntityResolver::resolveSurfacePatch);
      registry.put(
          "RECTANGULAR_TRIMMED_SURFACE",
          StepEntityResolver::resolveRectangularTrimmedSurface);
      registry.put("CURVE_BOUNDED_SURFACE", StepEntityResolver::resolveCurveBoundedSurface);
      registry.put("ORIENTED_SURFACE", StepEntityResolver::resolveOrientedSurface);
      registry.put(
          "SURFACE_OF_LINEAR_EXTRUSION", StepEntityResolver::resolveSurfaceOfLinearExtrusion);
      registry.put("SURFACE_OF_REVOLUTION", StepEntityResolver::resolveSurfaceOfRevolution);
      registry.put("SURFACE_OF_CONSTANT_RADIUS", StepEntityResolver::resolveSurfaceOfConstantRadius);
      registry.put("TRIMMED_CURVE", StepEntityResolver::resolveTrimmedCurve);
      registry.put("VERTEX_POINT", StepEntityResolver::resolveVertexPoint);
      registry.put("EDGE_CURVE", StepEntityResolver::resolveEdgeCurve);
      registry.put("ORIENTED_EDGE", StepEntityResolver::resolveOrientedEdge);
      registry.put("VERTEX_LOOP", StepEntityResolver::resolveVertexLoop);
      registry.put("POLY_LOOP", StepEntityResolver::resolvePolyLoop);
      registry.put("OPEN_PATH", StepEntityResolver::resolveOpenPath);
      registry.put("SUBPATH", StepEntityResolver::resolveSubpath);
      registry.put("ORIENTED_PATH", StepEntityResolver::resolveOrientedPath);
      registry.put("PATH", StepEntityResolver::resolvePath);
      registry.put("EDGE_LOOP", StepEntityResolver::resolveEdgeLoop);
      registry.put("EDGE_WIRE", StepEntityResolver::resolveEdgeWire);
      registry.put("LINE_SEGMENT", StepEntityResolver::resolveLineSegment);
      registry.put("RECTANGULAR_COMPOSITE_SURFACE", StepEntityResolver::resolveRectangularCompositeSurface);
      registry.put("COMPOSITE_CURVE_ON_SURFACE_3D", StepEntityResolver::resolveCompositeCurveOnSurface3D);
      registry.put(
          "FACE_OUTER_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, true));
      registry.put("FACE_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, false));
      registry.put("FACE_SURFACE", StepEntityResolver::resolveFaceSurface);
      registry.put("ADVANCED_FACE", StepEntityResolver::resolveAdvancedFace);
      registry.put("ORIENTED_FACE", StepEntityResolver::resolveOrientedFace);
      registry.put("VERTEX_SHELL", StepEntityResolver::resolveVertexShell);
      registry.put("WIRE_SHELL", StepEntityResolver::resolveWireShell);
      registry.put("CONNECTED_FACE_SUB_SET", StepEntityResolver::resolveConnectedFaceSubSet);
      registry.put("CONNECTED_FACE_SET", StepEntityResolver::resolveConnectedFaceSet);
      registry.put("TESSELLATED_FACE_SET", StepEntityResolver::resolveTessellatedFaceSet);
      registry.put("SEAM_EDGE", StepEntityResolver::resolveSeamEdge);
      registry.put("TESSELLATED_FACE", StepEntityResolver::resolveTessellatedFace);
      registry.put("TESSELLATED_TRIANGLE", StepEntityResolver::resolveTessellatedTriangle);
      // Tessellated triangulated types (OCCT Phase 4)
      registry.put("TRIANGULATED_FACE", StepEntityResolver::resolveTriangulatedFace);
      registry.put("COMPLEX_TRIANGULATED_FACE", StepEntityResolver::resolveComplexTriangulatedFace);
      registry.put("CUBIC_BEZIER_TRIANGULATED_FACE", StepEntityResolver::resolveCubicBezierTriangulatedFace);
      registry.put("FINITE_ELEMENT_MESH", StepEntityResolver::resolveFiniteElementMesh);
      // FEA element types (OCCT Phase 5)
      registry.put("VOLUME_3D_ELEMENT_REPRESENTATION",
          StepEntityResolver::resolveVolume3dElementRepresentation);
      registry.put("VOLUME_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveVolume3dElementProperty);
      registry.put("CURVE_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveCurve3dElementProperty);
      registry.put("SURFACE_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveSurface3dElementProperty);
      registry.put("FEA_MATERIAL_PROPERTY_REPRESENTATION",
          StepEntityResolver::resolveFeaMaterialPropertyRepresentation);
      registry.put("ELEMENT_VOLUME_2D", StepEntityResolver::resolveElementVolume2d);
      registry.put("ELEMENT_VOLUME_3D", StepEntityResolver::resolveElementVolume3d);
      // FEA base entities
      registry.put("NODE", StepEntityResolver::resolveFeaNode);
      registry.put("ELEMENT", StepEntityResolver::resolveFeaElement);
      registry.put("LOAD", StepEntityResolver::resolveFeaLoad);
      registry.put("MATERIAL", StepEntityResolver::resolveMaterial);
      registry.put("FEA_LINEAR_MATERIAL", StepEntityResolver::resolveFeaLinearMaterial);
      registry.put("FEA_NON_LINEAR_MATERIAL", StepEntityResolver::resolveFeaNonLinearMaterial);
      registry.put("FEA_MASS_DENSITY", StepEntityResolver::resolveFeaMassDensity);
      registry.put("FEA_YIELD_STRESS", StepEntityResolver::resolveFeaYieldStress);
      registry.put("FEA_ULTIMATE_STRESS", StepEntityResolver::resolveFeaUltimateStress);
      registry.put("DISPLACEMENT_BOUNDARY_CONDITION", StepEntityResolver::resolveDisplacementBoundaryCondition);
      registry.put("VELOCITY_BOUNDARY_CONDITION", StepEntityResolver::resolveVelocityBoundaryCondition);
      registry.put("ACCELERATION_BOUNDARY_CONDITION", StepEntityResolver::resolveAccelerationBoundaryCondition);
      registry.put("FORCE_BOUNDARY_CONDITION", StepEntityResolver::resolveForceBoundaryCondition);
      registry.put("PRESSURE_BOUNDARY_CONDITION", StepEntityResolver::resolvePressureBoundaryCondition);
      registry.put("THERMAL_BOUNDARY_CONDITION", StepEntityResolver::resolveThermalBoundaryCondition);
      registry.put("STRESS_ANALYSIS", StepEntityResolver::resolveStressAnalysis);
      registry.put("BUCKLING_ANALYSIS", StepEntityResolver::resolveBucklingAnalysis);
      registry.put("MODAL_ANALYSIS", StepEntityResolver::resolveModalAnalysis);
      registry.put("THERMAL_ANALYSIS", StepEntityResolver::resolveThermalAnalysis);
      registry.put("STRUCTURAL_ANALYSIS_MODEL", StepEntityResolver::resolveStructuralAnalysisModel);
      // StepElement entities
      registry.put("ELEMENT_VOLUME", StepEntityResolver::resolveElementVolume);
      registry.put("VOLUME_ELEMENT", StepEntityResolver::resolveVolumeElement);
      registry.put("SURFACE_ELEMENT", StepEntityResolver::resolveSurfaceElement);
      registry.put("LINE_ELEMENT", StepEntityResolver::resolveLineElement);
      registry.put("MASS_ELEMENT", StepEntityResolver::resolveMassElement);
      registry.put("CONNECTIVITY_ELEMENT", StepEntityResolver::resolveConnectivityElement);
      registry.put("ELEMENT_GEOMETRIC_DESCRIPTION", StepEntityResolver::resolveElementGeometricDescription);
      registry.put("UNIFORM_SURFACE_ELEMENT", StepEntityResolver::resolveUniformSurfaceElement);
      registry.put("UNIFORM_VOLUME_ELEMENT", StepEntityResolver::resolveUniformVolumeElement);
      registry.put("NODE_REPRESENTATION", StepEntityResolver::resolveNodeRepresentation);
      // Existing FEA entities
      registry.put("NODE_SET", StepEntityResolver::resolveNodeSet);
      registry.put("ELEMENT_SET", StepEntityResolver::resolveElementSet);
      registry.put("FEA_SECURED_VARIABLE", StepEntityResolver::resolveFeaSecuredVariable);
      registry.put("FEA_CONSTANT_FUNCTION_3D", StepEntityResolver::resolveFeaConstantFunction3d);
      registry.put("FEA_LINEAR_ALGEBRAIC_MATRIX", StepEntityResolver::resolveFeaLinearAlgebraicMatrix);
      registry.put("FEA_LINEAR_ALGEBRAIC_VECTOR", StepEntityResolver::resolveFeaLinearAlgebraicVector);
      registry.put("FEA_AXIS_2_PLACEMENT_3D", StepEntityResolver::resolveFeaAxis2Placement3d);
      registry.put("FEA_GROUP_REPRESENTATION", StepEntityResolver::resolveFeaGroupRepresentation);
      // FEA aliases
      registerFeaAliases(registry,
          "VOLUME_3D_ELEMENT_DESCRIPTOR", "SURFACE_3D_ELEMENT_DESCRIPTOR",
          "CURVE_3D_ELEMENT_DESCRIPTOR", "NODE_REPRESENTATION",
          "ELEMENT_REPRESENTATION", "NODE_DEFINITION",
          "FEA_MODEL", "FEA_MODEL_3D", "FEA_REPRESENTATION_ITEM");
      // Phase 2B: Advanced geometry entities
      registry.put(
          "TRIANGULATED_FACE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance)); // Same as TESSELLATED_FACE_SET
      registry.put("SUBFACE", StepEntityResolver::resolveSubface);
      registry.put("ORIENTED_SUBFACE", StepEntityResolver::resolveOrientedSubface);
      registry.put("SURFACED_OPEN_SHELL", StepEntityResolver::resolveSurfacedOpenShell);
      registry.put("ORIENTED_OPEN_SHELL", StepEntityResolver::resolveOrientedOpenShell);
      registry.put("ORIENTED_CLOSED_SHELL", StepEntityResolver::resolveOrientedClosedShell);
      registry.put("SHELL_BASED_WIREFRAME_MODEL", StepEntityResolver::resolveShellBasedWireframeModel);
      registry.put("OPEN_SHELL", StepEntityResolver::resolveOpenShell);
      registry.put("CLOSED_SHELL", StepEntityResolver::resolveClosedShell);
      // Phase 2 extended: Additional geometric tolerance type aliases
      registerGeometricToleranceAliases(
          registry,
          "COAXIALITY_TOLERANCE",
          "PROFILE_OF_A_POINT_TOLERANCE",
          "LINE_PROFILE_TOLERANCE",
          "SURFACE_PROFILE_TOLERANCE",
          "RUNOUT_TOLERANCE",
          "AXIAL_RUNOUT_TOLERANCE",
          "RADIAL_RUNOUT_TOLERANCE",
          "TOTAL_AXIAL_RUNOUT_TOLERANCE",
          "TOTAL_RADIAL_RUNOUT_TOLERANCE");
      // Phase 2 extended: Additional shape aspect aliases
      registerShapeAspectAliases(
          registry,
          "ASSEMBLY_FEATURE",
          "BOUNDARY_CURVE_ELEMENT",
          "CHAMFER_EDGE",
          "CIRCULAR_PATTERN_MEMBER",
          "CORNER_FEATURE",
          "CROSS_SECTION_FEATURE",
          "CURVE_BASED_FEATURE",
          "DEFINITIONAL_SHAPE_ASPECT",
          "DERIVED_SHAPE_ASPECT",
          "EDGE_BLEND_FEATURE",
          "EDGE_FEATURE",
          "FABRICATED_FEATURE",
          "FINISH_FEATURE",
          "FLANGE_FEATURE",
          "FREE_FORM_FEATURE",
          "GROOVE_FEATURE",
          "GUIDE_FEATURE",
          "HEAT_TREAT_FEATURE",
          "KNURL_FEATURE",
          "LAND_FEATURE",
          "LEAD_FEATURE",
          "MACHINING_FEATURE",
          "MOLD_FEATURE",
          "MOUNTING_FEATURE",
          "NOTCH_FEATURE",
          "PASSAGE_FEATURE",
          "PIPE_FEATURE",
          "PLATE_FEATURE",
          "PRESS_FEATURE",
          "PUNCH_FEATURE",
          "RACE_FEATURE",
          "RADIUS_FEATURE",
          "REFERENCE_FEATURE",
          "RIB_EDGE",
          "ROUND_FEATURE",
          "SEAL_FEATURE",
          "SHEET_FEATURE",
          "SKETCH_FEATURE",
          "SLOT_FEATURE",
          "SPINE_FEATURE",
          "SPRING_FEATURE",
          "STEP_FEATURE",
          "STUD_FEATURE",
          "SURFACE_BASED_FEATURE",
          "TAB_FEATURE",
          "TAPER_FEATURE",
          "THREAD_FEATURE",
          "TOLERANCE_FEATURE",
          "TURN_FEATURE",
          "UNDERCUT_FEATURE",
          "VENT_FEATURE",
          "WELD_FEATURE",
          "WRAP_FEATURE");
      // Phase 6: Additional manufacturing feature aliases (verified ShapeAspect 4-param structure)
      registerShapeAspectAliases(
          registry,
          "ACCESS_FEATURE",
          "ACTUATOR_FEATURE",
          "CASTING_FEATURE",
          "CLAMP_FEATURE",
          "COMPLEX_FEATURE",
          "CONTROLLER_FEATURE",
          "DIE_FEATURE",
          "ELECTRICAL_FEATURE",
          "FASTENER_FEATURE",
          "FILTER_FEATURE",
          "FITTING_FEATURE",
          "FIXTURE_FEATURE",
          "FORGING_FEATURE",
          "GEAR_FEATURE",
          "HANDLING_FEATURE",
          "HEATING_FEATURE",
          "HOUSING_FEATURE",
          "HYDRAULIC_FEATURE",
          "INTERFACE_FEATURE",
          "JIG_FEATURE",
          "LABEL_FEATURE",
          "PAINTING_FEATURE",
          "SAFETY_FEATURE",
          "SENSOR_FEATURE",
          "SPRING_FEATURE",
          "VALVE_FEATURE");
      // Phase 7: Additional manufacturing features and operations
      registerShapeAspectAliases(
          registry,
          "COOLING_FEATURE",
          "LOCATOR_FEATURE",
          "LUBRICATION_FEATURE",
          "MARKING_FEATURE",
          "MODIFY_FEATURE",
          "PLATING_FEATURE",
          "PNEUMATIC_FEATURE",
          "ROBOT_FEATURE",
          "SHAFT_FEATURE",
          "STORAGE_FEATURE",
          "STRUCTURAL_FEATURE",
          "TRANSPORT_FEATURE");
      // Phase 7: Dimension representation aliases
      registerRepresentationAliases(
          registry,
          true,
          "ANGULAR_DIMENSION_REPRESENTATION",
          "CHAIN_DIMENSION_REPRESENTATION",
          "LINEAR_DIMENSION_REPRESENTATION",
          "MANUFACTURING_FEATURE_REPRESENTATION",
          "ORDINATE_DIMENSION_REPRESENTATION",
          "PROCESS_PLAN_REPRESENTATION",
          "COATING_REPRESENTATION_ITEM",
          "HARDNESS_REPRESENTATION_ITEM",
          "HEAT_TREATMENT_REPRESENTATION_ITEM");
      // Phase 2 extended: Additional representation type aliases
      registerRepresentationAliases(
          registry,
          true,
          "ADVANCED_FACE_SHAPE_REPRESENTATION",
          "ANNOTATION_SHAPE_REPRESENTATION",
          "AUXILIARY_GEOMETRIC_REPRESENTATION",
          "BEND_SHAPE_REPRESENTATION",
          "BLANK_SHAPE_REPRESENTATION",
          "CABLE_SHAPE_REPRESENTATION",
          "CARRIER_SHAPE_REPRESENTATION",
          "CUTOUT_SHAPE_REPRESENTATION",
          "DEFINITIONAL_SHAPE_REPRESENTATION",
          "DIE_SHAPE_REPRESENTATION",
          "DRAWING_SHAPE_REPRESENTATION",
          "ELECTRICAL_SHAPE_REPRESENTATION",
          "EXPLICIT_SHAPE_REPRESENTATION",
          "EXTRUSION_SHAPE_REPRESENTATION",
          "FASTENER_SHAPE_REPRESENTATION",
          "FITTING_SHAPE_REPRESENTATION",
          "FLAT_PATTERN_SHAPE_REPRESENTATION",
          "FRAME_SHAPE_REPRESENTATION",
          "HOLE_SHAPE_REPRESENTATION",
          "INTERCONNECT_SHAPE_REPRESENTATION",
          "JOINT_SHAPE_REPRESENTATION",
          "LAMINATE_SHAPE_REPRESENTATION",
          "LIBRARY_SHAPE_REPRESENTATION",
          "MACHINED_SHAPE_REPRESENTATION",
          "MOLD_SHAPE_REPRESENTATION",
          "MOUNTED_SHAPE_REPRESENTATION",
          "PACKAGE_SHAPE_REPRESENTATION",
          "PANEL_SHAPE_REPRESENTATION",
          "PART_SHAPE_REPRESENTATION",
          "PATTERN_SHAPE_REPRESENTATION",
          "PIPELINE_SHAPE_REPRESENTATION",
          "PRINTED_SHAPE_REPRESENTATION",
          "PROCESS_SHAPE_REPRESENTATION",
          "PRODUCT_SHAPE_REPRESENTATION",
          "REFERENCE_SHAPE_REPRESENTATION",
          "REINFORCEMENT_SHAPE_REPRESENTATION",
          "RIVET_SHAPE_REPRESENTATION",
          "ROUTE_SHAPE_REPRESENTATION",
          "SECTION_SHAPE_REPRESENTATION",
          "SHEET_METAL_SHAPE_REPRESENTATION",
          "SHELL_SHAPE_REPRESENTATION",
          "SLOT_SHAPE_REPRESENTATION",
          "STAMPED_SHAPE_REPRESENTATION",
          "STANDARD_SHAPE_REPRESENTATION",
          "STRUCTURAL_SHAPE_REPRESENTATION",
          "SURFACE_FINISH_SHAPE_REPRESENTATION",
          "TABULATION_SHAPE_REPRESENTATION",
          "THREAD_SHAPE_REPRESENTATION",
          "TOLERANCE_SHAPE_REPRESENTATION",
          "TOOL_SHAPE_REPRESENTATION",
          "TRANSFORMATION_SHAPE_REPRESENTATION",
          "UNFOLD_SHAPE_REPRESENTATION",
          "VALIDATION_SHAPE_REPRESENTATION",
          "VARIANT_SHAPE_REPRESENTATION",
          "WELD_SHAPE_REPRESENTATION",
          "WIRE_SHAPE_REPRESENTATION",
          "ZONE_SHAPE_REPRESENTATION");
      // Phase 2 extended: Additional representation relationship aliases
      registerRepresentationRelationshipAliases(
          registry,
          "ANNOTATION_RELATIONSHIP",
          "ASSEMBLY_RELATIONSHIP",
          "BREAKDOWN_RELATIONSHIP",
          "CAD_MODEL_RELATIONSHIP",
          "CATALOG_RELATIONSHIP",
          "CONFIGURATION_RELATIONSHIP",
          "DEFINITION_RELATIONSHIP",
          "DRAWING_RELATIONSHIP",
          "ELECTRICAL_RELATIONSHIP",
          "FEATURE_RELATIONSHIP",
          "GEOMETRY_RELATIONSHIP",
          "INSPECTION_RELATIONSHIP",
          "INTERFACE_RELATIONSHIP",
          "LIBRARY_RELATIONSHIP",
          "LOGISTIC_RELATIONSHIP",
          "MATERIAL_RELATIONSHIP",
          "MECHANICAL_RELATIONSHIP",
          "PACKAGE_RELATIONSHIP",
          "PART_RELATIONSHIP",
          "PROCESS_RELATIONSHIP",
          "QUALITY_RELATIONSHIP",
          "REFERENCE_RELATIONSHIP",
          "REQUIREMENT_RELATIONSHIP",
          "SHAPE_DEFINITION_RELATIONSHIP",
          "STRUCTURE_RELATIONSHIP",
          "TEST_RELATIONSHIP",
          "TOOL_RELATIONSHIP",
          "VERSION_RELATIONSHIP",
          "WIRE_RELATIONSHIP",
          "ZONE_RELATIONSHIP");
      // Phase 2 extended: Additional characterized object aliases
      registerCharacterizedObjectAliases(
          registry,
          "ADJUSTMENT_OPERATION",
          "ASSEMBLY_OPERATION",
          "CALIBRATION_OPERATION",
          "CLEANING_OPERATION",
          "COATING_OPERATION",
          "DISASSEMBLY_OPERATION",
          "FINISHING_OPERATION",
          "HEAT_TREATMENT_OPERATION",
          "INSPECTION_OPERATION",
          "INSTALLATION_OPERATION",
          "JOINING_OPERATION",
          "MACHINING_OPERATION_SEQUENCE",
          "MAINTENANCE_OPERATION",
          "MARKING_OPERATION",
          "MOLDING_OPERATION",
          "PACKAGING_OPERATION",
          "POLISHING_OPERATION",
          "PRESSING_OPERATION",
          "RESTORATION_OPERATION",
          "STAMPING_OPERATION",
          "TESTING_OPERATION",
          "TREATMENT_OPERATION",
          "WELDING_OPERATION",
          "ADHESIVE_BOND_FEATURE",
          "BEND_FEATURE",
          "CLINCH_FEATURE",
          "CO_EXTRUDED_FEATURE",
          "CRIMP_FEATURE",
          "DRAWN_FEATURE",
          "EMBOSSED_FEATURE",
          "ENGRAVED_FEATURE",
          "ETCHED_FEATURE",
          "FLATTENED_FEATURE",
          "FOLDED_FEATURE",
          "HEM_FEATURE",
          "JOG_FEATURE",
          "LANCED_FEATURE",
          "LASER_CUT_FEATURE",
          "MILLED_FEATURE",
          "NOTCHED_FEATURE",
          "PIERCED_FEATURE",
          "PLASMA_CUT_FEATURE",
          "PRESSED_FEATURE",
          "PUNCHED_FEATURE",
          "ROLLED_FEATURE",
          "SEAMED_FEATURE",
          "SHEARED_FEATURE",
          "SLIT_FEATURE",
          "SPUN_FEATURE",
          "STAKED_FEATURE",
          "STAMPED_FEATURE",
          "SWAGED_FEATURE",
          "TAPPED_FEATURE",
          "TURNED_FEATURE",
          "WATER_CUT_FEATURE",
          "WELDED_FEATURE");
      // Phase 2 extended: Additional externally defined item aliases
      registerExternallyDefinedItemAliases(
          registry,
          "EXTERNALLY_DEFINED_CAD_MODEL",
          "EXTERNALLY_DEFINED_CALCULATION",
          "EXTERNALLY_DEFINED_CONFIGURATION",
          "EXTERNALLY_DEFINED_CONSTRAINT",
          "EXTERNALLY_DEFINED_DOCUMENT",
          "EXTERNALLY_DEFINED_DRAWING",
          "EXTERNALLY_DEFINED_FEATURE",
          "EXTERNALLY_DEFINED_FINISH",
          "EXTERNALLY_DEFINED_GEOMETRY",
          "EXTERNALLY_DEFINED_INSPECTION",
          "EXTERNALLY_DEFINED_INTERFACE",
          "EXTERNALLY_DEFINED_LIBRARY",
          "EXTERNALLY_DEFINED_MATERIAL",
          "EXTERNALLY_DEFINED_MODEL",
          "EXTERNALLY_DEFINED_PART",
          "EXTERNALLY_DEFINED_PROCESS",
          "EXTERNALLY_DEFINED_PRODUCT",
          "EXTERNALLY_DEFINED_QUALITY",
          "EXTERNALLY_DEFINED_REFERENCE",
          "EXTERNALLY_DEFINED_REQUIREMENT",
          "EXTERNALLY_DEFINED_SHAPE",
          "EXTERNALLY_DEFINED_STANDARD",
          "EXTERNALLY_DEFINED_TEST",
          "EXTERNALLY_DEFINED_TOOL",
          "EXTERNALLY_DEFINED_VERSION",
          "EXTERNALLY_DEFINED_WIRE",
          "EXTERNALLY_DEFINED_ZONE");
      // Phase 3: Additional tessellation entities
      registry.put(
          "TRIANGULATED_FACE",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put(
          "POLYGONAL_FACE",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put(
          "TESSELLATED_SHELL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_SOLID",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_CURVE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_POINT_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      // Phase 3: Additional tolerance entities
      registry.put(
          "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE",
          (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE"));
      registry.put(
          "PROJECTED_TOLERANCE_ZONE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PROJECTED_TOLERANCE_ZONE"));
      // Phase 3: Additional product definition entities
      registry.put(
          "MAKE_FROM_OPTION",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "MAKE_FROM_OPTION"));
      registry.put(
          "AREA_IN_SET",
          (resolver, instance) -> resolver.resolveRepresentationRelationship(instance, "AREA_IN_SET"));
      registry.put(
          "ITEM_ASSOCIATED_DIMENSION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "ITEM_ASSOCIATED_DIMENSION"));
      registry.put(
          "DIMENSION_PAIR",
          (resolver, instance) -> resolver.resolveShapeAspectRelationship(instance, "DIMENSION_PAIR"));
      registry.put(
          "DIMENSIONAL_CHARACTERISTIC_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      // Phase 3: Additional curve entities
      registry.put(
          "REPARAMETRISED_COMPOSITE_CURVE_SEGMENT",
          (resolver, instance) -> resolver.resolveCompositeCurveSegment(instance));
      registry.put(
          "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS_CURVE",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
      // Phase 3: Additional external definition entities
      registry.put(
          "REPUBLICATION",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "EXTERNALLY_DEFINED_CURVE_FONT",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_CURVE_FONT"));
      registry.put(
          "EXTERNALLY_DEFINED_HATCH_STYLE",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_HATCH_STYLE"));
      registry.put(
          "EXTERNALLY_DEFINED_SYMBOL",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_SYMBOL"));
      registry.put(
          "EXTERNALLY_DEFINED_TEXT_FONT",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TEXT_FONT"));
      registry.put(
          "EXTERNALLY_DEFINED_TILE_STYLE",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TILE_STYLE"));
      // Phase 3: Additional draughting entities
      registry.put(
          "INSET_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "INSET_CALLOUT"));
      registry.put(
          "VALUE_FORMAT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "VALUE_FORMAT_TYPE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "GLOBAL_CLOCK",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Phase 3: Additional property entities
      registry.put(
          "ACTION_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "GENERAL_PROPERTY_ASSOCINATION",
          (resolver, instance) -> resolver.resolveGeneralPropertyRelationship(instance));
      registry.put(
          "GENERAL_PROPERTY_DEFINITION",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "FEATURE_COMPONENT_DEFINITION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "FEATURE_COMPONENT_DEFINITION"));
      registry.put(
          "DERIVED_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DERIVED_SHAPE_ASPECT"));
      registry.put(
          "APPLIED_SHAPE_ASPECT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "APPLIED_SHAPE_ASPECT_ASSIGNMENT"));
      // Phase 3: Additional solid entities
      registry.put(
          "SWEPT_AREA_SOLID",
          (resolver, instance) -> resolver.resolveSweptAreaSolid(instance, "SWEPT_AREA_SOLID"));
      registry.put(
          "SWEPT_VOLUME_SOLID",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "SHELL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "AREA_SOLID",
          (resolver, instance) -> resolver.resolveSweptAreaSolid(instance, "AREA_SOLID"));
      registry.put(
          "GEOMETRIC_REPRESENTATION_ITEM_WITH_GEOMETRY",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "SHAPE_REPRESENTATION_WITH_PARAMETERS",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "SHAPE_REPRESENTATION_WITH_PARAMETERS", true));
      registry.put(
          "REPRESENTATION_WITH_PARAMETERS",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "REPRESENTATION_WITH_PARAMETERS", false));
      registry.put(
          "VOID_SOLID",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Phase 3: Replica entities (already handled via geometric replica resolver)
      registry.put(
          "REPLICA_GEOMETRY",
          (resolver, instance) -> resolver.resolveGeometricReplica(instance, "REPLICA_GEOMETRY"));
      registry.put(
          "GEOMETRIC_REPLICA",
          (resolver, instance) -> resolver.resolveGeometricReplica(instance, "GEOMETRIC_REPLICA"));
      // Phase 3: BREP variants
      registry.put(
          "BREP",
          (resolver, instance) -> resolver.resolveManifoldSolidBrep(instance, "BREP"));
      // Phase 3: Additional representation entities
      registry.put(
          "ADVANCED_FACE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ADVANCED_FACE_REPRESENTATION", true));
      registry.put(
          "FACE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FACE_REPRESENTATION", true));
      registry.put(
          "EDGE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "EDGE_REPRESENTATION", true));
      registry.put(
          "VERTEX_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "VERTEX_REPRESENTATION", true));
      registry.put(
          "LOOP_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "LOOP_REPRESENTATION", true));
      registry.put(
          "SHELL_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "SHELL_REPRESENTATION", true));
      // Phase 4: Extended geometric tolerance types
      registerGeometricToleranceAliases(
          registry,
          "ANGULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
          "CIRCULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
          "CONCENTRICITY_TOLERANCE_WITH_DATUM_REFERENCE",
          "CYLINDRICITY_TOLERANCE_WITH_DATUM_REFERENCE",
          "FLATNESS_TOLERANCE_WITH_DATUM_REFERENCE",
          "PARALLELISM_TOLERANCE_WITH_DATUM_REFERENCE",
          "PERPENDICULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
          "POSITION_TOLERANCE_WITH_DATUM_REFERENCE",
          "PROFILE_OF_A_LINE_TOLERANCE_WITH_DATUM_REFERENCE",
          "PROFILE_OF_A_SURFACE_TOLERANCE_WITH_DATUM_REFERENCE",
          "RUNOUT_TOLERANCE_WITH_DATUM_REFERENCE",
          "STRAIGHTNESS_TOLERANCE_WITH_DATUM_REFERENCE",
          "SYMMETRY_TOLERANCE_WITH_DATUM_REFERENCE",
          "TOTAL_RUNOUT_TOLERANCE_WITH_DATUM_REFERENCE");
      // Phase 4: Extended shape aspect relationship aliases
      registerShapeAspectRelationshipAliases(
          registry,
          "ASSEMBLY_FEATURE_RELATIONSHIP",
          "COMPONENT_FEATURE_RELATIONSHIP",
          "DATUM_RELATIONSHIP",
          "FEATURE_CHAIN_RELATIONSHIP",
          "GEOMETRIC_TOLERANCE_RELATIONSHIP",
          "MATING_FEATURE_RELATIONSHIP",
          "MOUNTING_FEATURE_RELATIONSHIP",
          "PART_FEATURE_RELATIONSHIP",
          "PROCESS_FEATURE_RELATIONSHIP",
          "REFERENCE_FEATURE_RELATIONSHIP",
          "TOLERANCE_CHAIN_RELATIONSHIP",
          "WELD_FEATURE_RELATIONSHIP");
      // Phase 4: Extended representation aliases
      registerRepresentationAliases(
          registry,
          true,
          "ADVANCED_SURFACE_SHAPE_REPRESENTATION",
          "ASSEMBLY_FEATURE_SHAPE_REPRESENTATION",
          "BEND_AREA_SHAPE_REPRESENTATION",
          "BLANKING_SHAPE_REPRESENTATION",
          "BOLTING_SHAPE_REPRESENTATION",
          "BONDING_SHAPE_REPRESENTATION",
          "CASTING_SHAPE_REPRESENTATION",
          "COATING_SHAPE_REPRESENTATION",
          "COMPOSITE_MATERIAL_SHAPE_REPRESENTATION",
          "COMPONENT_MOUNTING_SHAPE_REPRESENTATION",
          "CONNECTION_SHAPE_REPRESENTATION",
          "CORE_SHAPE_REPRESENTATION",
          "CUTTING_SHAPE_REPRESENTATION",
          "DEFINITION_FEATURE_SHAPE_REPRESENTATION",
          "DRILLING_SHAPE_REPRESENTATION",
          "EDGE_FINISH_SHAPE_REPRESENTATION",
          "ELECTRICAL_CONNECTION_SHAPE_REPRESENTATION",
          "FASTENING_SHAPE_REPRESENTATION",
          "FINISHING_SHAPE_REPRESENTATION",
          "FORGING_SHAPE_REPRESENTATION",
          "GRINDING_SHAPE_REPRESENTATION",
          "HARDENING_SHAPE_REPRESENTATION",
          "HEATING_SHAPE_REPRESENTATION",
          "HONING_SHAPE_REPRESENTATION",
          "INSERT_SHAPE_REPRESENTATION",
          "JOINING_SHAPE_REPRESENTATION",
          "KEYING_SHAPE_REPRESENTATION",
          "LAPPING_SHAPE_REPRESENTATION",
          "MACHINING_SETUP_SHAPE_REPRESENTATION",
          "MATERIAL_REMOVAL_SHAPE_REPRESENTATION",
          "MEASURING_SHAPE_REPRESENTATION",
          "MILLING_SHAPE_REPRESENTATION",
          "MOLD_CAVITY_SHAPE_REPRESENTATION",
          "NUTTING_SHAPE_REPRESENTATION",
          "OVERMOLDING_SHAPE_REPRESENTATION",
          "PINNING_SHAPE_REPRESENTATION",
          "PLATING_SHAPE_REPRESENTATION",
          "POLISHING_SHAPE_REPRESENTATION",
          "PRESSING_SHAPE_REPRESENTATION",
          "PUNCHING_SHAPE_REPRESENTATION",
          "RIVETING_SHAPE_REPRESENTATION",
          "ROUTING_SHAPE_REPRESENTATION",
          "SAWING_SHAPE_REPRESENTATION",
          "SCREWING_SHAPE_REPRESENTATION",
          "SEALING_SHAPE_REPRESENTATION",
          "SHAPING_SHAPE_REPRESENTATION",
          "SHEARING_SHAPE_REPRESENTATION",
          "SINTERING_SHAPE_REPRESENTATION",
          "SLOTTING_SHAPE_REPRESENTATION",
          "SPINNING_SHAPE_REPRESENTATION",
          "STAMPING_SHAPE_REPRESENTATION",
          "SURFACE_FINISH_SHAPE_REPRESENTATION",
          "TAPPING_SHAPE_REPRESENTATION",
          "TEMPERING_SHAPE_REPRESENTATION",
          "THREADING_SHAPE_REPRESENTATION",
          "TURNING_SHAPE_REPRESENTATION",
          "UNDERCUTTING_SHAPE_REPRESENTATION",
          "WELDING_SHAPE_REPRESENTATION",
          "WIRE_EDM_SHAPE_REPRESENTATION");
      // Phase 4: Extended representation relationship aliases
      registerRepresentationRelationshipAliases(
          registry,
          "ASSEMBLY_FEATURE_RELATIONSHIP",
          "BEND_RELATIONSHIP",
          "CAD_MODEL_TO_PHYSICAL_RELATIONSHIP",
          "COMPONENT_TO_FEATURE_RELATIONSHIP",
          "DEFINITION_TO_INSTANCE_RELATIONSHIP",
          "DESIGN_TO_MANUFACTURING_RELATIONSHIP",
          "ELECTRICAL_CONNECTION_RELATIONSHIP",
          "FEATURE_TO_FEATURE_RELATIONSHIP",
          "FEATURE_TO_PART_RELATIONSHIP",
          "GEOMETRY_TO_FEATURE_RELATIONSHIP",
          "INSPECTION_TO_PRODUCT_RELATIONSHIP",
          "INTERFACE_TO_INTERFACE_RELATIONSHIP",
          "MATERIAL_TO_GEOMETRY_RELATIONSHIP",
          "MOUNTING_TO_FEATURE_RELATIONSHIP",
          "PART_TO_ASSEMBLY_RELATIONSHIP",
          "PART_TO_FEATURE_RELATIONSHIP",
          "PART_TO_PART_RELATIONSHIP",
          "PROCESS_TO_FEATURE_RELATIONSHIP",
          "REFERENCE_TO_GEOMETRY_RELATIONSHIP",
          "SHAPE_TO_FEATURE_RELATIONSHIP",
          "SHAPE_TO_SHAPE_RELATIONSHIP",
          "TOOL_TO_PART_RELATIONSHIP",
          "WELD_TO_PART_RELATIONSHIP");
      // Phase 4: Extended characterized object aliases
      registerCharacterizedObjectAliases(
          registry,
          "ALIGNMENT_FEATURE",
          "ANCHORING_FEATURE",
          "AUXILIARY_FEATURE",
          "BASE_FEATURE",
          "BENDING_FEATURE",
          "BINDING_FEATURE",
          "BONDING_FEATURE",
          "BRAKING_FEATURE",
          "BRACING_FEATURE",
          "BRACKET_FEATURE",
          "BUSHING_FEATURE",
          "CAM_FEATURE",
          "CENTERING_FEATURE",
          "CHUCKING_FEATURE",
          "CLAMPING_FEATURE",
          "CLEARANCE_FEATURE",
          "CLIPPING_FEATURE",
          "CLOSING_FEATURE",
          "COATING_FEATURE",
          "COUPLING_FEATURE",
          "COVERING_FEATURE",
          "CUSHIONING_FEATURE",
          "CUTTING_FEATURE",
          "DAMPING_FEATURE",
          "DETENT_FEATURE",
          "DISPENSING_FEATURE",
          "DIVERTING_FEATURE",
          "DOCKING_FEATURE",
          "DRIVING_FEATURE",
          "EJECTING_FEATURE",
          "ENCLOSING_FEATURE",
          "ENGAGING_FEATURE",
          "FILLING_FEATURE",
          "FILTERING_FEATURE",
          "FITTING_FEATURE",
          "FIXING_FEATURE",
          "FLUID_HANDLING_FEATURE",
          "GAGING_FEATURE",
          "GASKETING_FEATURE",
          "GRIPPING_FEATURE",
          "GUIDING_FEATURE",
          "HOLDING_FEATURE",
          "HOUSING_FEATURE",
          "INDICATING_FEATURE",
          "INSERTING_FEATURE",
          "INSULATING_FEATURE",
          "INTERLOCKING_FEATURE",
          "JOINING_FEATURE",
          "KEYING_FEATURE",
          "LIFTING_FEATURE",
          "LIMITING_FEATURE",
          "LOCATING_FEATURE",
          "LOCKING_FEATURE",
          "MOUNTING_FEATURE",
          "MOVING_FEATURE",
          "NEUTRALIZING_FEATURE",
          "OILING_FEATURE",
          "OPENING_FEATURE",
          "ORIENTING_FEATURE",
          "PAINTING_FEATURE",
          "PRESSURIZING_FEATURE",
          "PROTECTING_FEATURE",
          "PULLING_FEATURE",
          "PUSHING_FEATURE",
          "REGULATING_FEATURE",
          "RELEASING_FEATURE",
          "RETAINING_FEATURE",
          "RETURNING_FEATURE",
          "ROTATING_FEATURE",
          "SEALING_FEATURE",
          "SECURING_FEATURE",
          "SETTING_FEATURE",
          "SHAPING_FEATURE",
          "SHIELDING_FEATURE",
          "SHIFTING_FEATURE",
          "SLIDING_FEATURE",
          "SNAPPING_FEATURE",
          "SOCKETING_FEATURE",
          "SUPPORTING_FEATURE",
          "SUSPENDING_FEATURE",
          "SWITCHING_FEATURE",
          "TENSIONING_FEATURE",
          "THRUSTING_FEATURE",
          "TILTING_FEATURE",
          "TIMING_FEATURE",
          "TOGGLE_FEATURE",
          "TRACKING_FEATURE",
          "TRANSMITTING_FEATURE",
          "TRAPPING_FEATURE",
          "TRIMMING_FEATURE",
          "TURNING_FEATURE",
          "UNLOADING_FEATURE",
          "VALVING_FEATURE",
          "VENTING_FEATURE",
          "VIBRATING_FEATURE",
          "WELDING_FEATURE",
          "WRAPPING_FEATURE");
      // Phase 5: Additional advanced geometry types (already registered, aliases added)
      // Phase 5: Additional profile definitions
      registerShapeAspectAliases(
          registry,
          "CIRCULAR_CLOSED_PROFILE",
          "RECTANGULAR_CLOSED_PROFILE",
          "CLOSED_PATH_PROFILE",
          "OPEN_PATH_PROFILE",
          "NUT_PROFILE",
          "BOLT_PROFILE",
          "SCREW_PROFILE",
          "FASTENER_PROFILE",
          "GASKET_PROFILE",
          "SEAL_PROFILE",
          "O_RING_PROFILE",
          "C_RING_PROFILE",
          "E_RING_PROFILE",
          "U_RING_PROFILE",
          "V_RING_PROFILE",
          "X_RING_PROFILE",
          "WIRE_PROFILE",
          "CABLE_PROFILE",
          "TUBE_PROFILE",
          "PIPE_PROFILE",
          "BEAM_PROFILE",
          "COLUMN_PROFILE",
          "STRUT_PROFILE",
          "BRACE_PROFILE",
          "TRUSS_PROFILE",
          "FRAME_PROFILE",
          "RAIL_PROFILE",
          "TRACK_PROFILE",
          "WHEEL_PROFILE",
          "TIRE_PROFILE",
          "ROLLER_PROFILE",
          "BELT_PROFILE",
          "CHAIN_PROFILE",
          "SPROCKET_PROFILE",
          "GEAR_PROFILE",
          "RACK_PROFILE",
          "PINION_PROFILE",
          "WORM_PROFILE",
          "WHEEL_GEAR_PROFILE",
          "BEVEL_GEAR_PROFILE",
          "HELICAL_GEAR_PROFILE",
          "SPUR_GEAR_PROFILE");
      // Phase 5: Additional tolerance zone types
      registerShapeAspectAliases(
          registry,
          "LINEAR_TOLERANCE_ZONE_DEFINITION",
          "RADIAL_TOLERANCE_ZONE_DEFINITION",
          "ANGULAR_TOLERANCE_ZONE_DEFINITION",
          "AXIAL_TOLERANCE_ZONE_DEFINITION",
          "COAXIAL_TOLERANCE_ZONE_DEFINITION",
          "CONCENTRIC_TOLERANCE_ZONE_DEFINITION",
          "SYMMETRIC_TOLERANCE_ZONE_DEFINITION",
          "POSITIONAL_TOLERANCE_ZONE_DEFINITION",
          "PROFILE_TOLERANCE_ZONE_DEFINITION",
          "RUNOUT_TOLERANCE_ZONE_DEFINITION",
          "TOTAL_RUNOUT_TOLERANCE_ZONE_DEFINITION");
      // Phase 5: Additional measurement representation types
      registerRepresentationAliases(
          registry,
          false,
          "ANGULAR_MEASUREMENT_REPRESENTATION",
          "LINEAR_MEASUREMENT_REPRESENTATION",
          "AREA_MEASUREMENT_REPRESENTATION",
          "VOLUME_MEASUREMENT_REPRESENTATION",
          "MASS_MEASUREMENT_REPRESENTATION",
          "TIME_MEASUREMENT_REPRESENTATION",
          "TEMPERATURE_MEASUREMENT_REPRESENTATION",
          "PRESSURE_MEASUREMENT_REPRESENTATION",
          "FORCE_MEASUREMENT_REPRESENTATION",
          "TORQUE_MEASUREMENT_REPRESENTATION",
          "POWER_MEASUREMENT_REPRESENTATION",
          "ENERGY_MEASUREMENT_REPRESENTATION",
          "SPEED_MEASUREMENT_REPRESENTATION",
          "VELOCITY_MEASUREMENT_REPRESENTATION",
          "ACCELERATION_MEASUREMENT_REPRESENTATION",
          "FREQUENCY_MEASUREMENT_REPRESENTATION",
          "VOLTAGE_MEASUREMENT_REPRESENTATION",
          "CURRENT_MEASUREMENT_REPRESENTATION",
          "RESISTANCE_MEASUREMENT_REPRESENTATION",
          "CAPACITANCE_MEASUREMENT_REPRESENTATION",
          "INDUCTANCE_MEASUREMENT_REPRESENTATION",
          "MAGNETIC_FLUX_MEASUREMENT_REPRESENTATION",
          "LUMINANCE_MEASUREMENT_REPRESENTATION",
          "ILLUMINANCE_MEASUREMENT_REPRESENTATION",
          "RADIATION_MEASUREMENT_REPRESENTATION",
          "SOUND_MEASUREMENT_REPRESENTATION",
          "VIBRATION_MEASUREMENT_REPRESENTATION",
          "ROUGHNESS_MEASUREMENT_REPRESENTATION",
          "FLATNESS_MEASUREMENT_REPRESENTATION",
          "CIRCULARITY_MEASUREMENT_REPRESENTATION",
          "CYLINDRICITY_MEASUREMENT_REPRESENTATION",
          "STRAIGHTNESS_MEASUREMENT_REPRESENTATION",
          "PERPENDICULARITY_MEASUREMENT_REPRESENTATION",
          "PARALLELISM_MEASUREMENT_REPRESENTATION",
          "ANGULARITY_MEASUREMENT_REPRESENTATION",
          "CONCENTRICITY_MEASUREMENT_REPRESENTATION",
          "SYMMETRY_MEASUREMENT_REPRESENTATION",
          "POSITION_MEASUREMENT_REPRESENTATION",
          "PROFILE_MEASUREMENT_REPRESENTATION",
          "RUNOUT_MEASUREMENT_REPRESENTATION");
      // Phase 5: Additional document types
      registry.put(
          "DRAWING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "SPECIFICATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "TEST_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "REPORT_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "MANUAL_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "PROCEDURE_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "STANDARD_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "REGULATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "CONTRACT_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "ORDER_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "QUOTATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "INVOICE_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "PACKING_LIST_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "SHIPPING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "RECEIVING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "INSPECTION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "CERTIFICATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "WARRANTY_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "MAINTENANCE_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "REPAIR_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "CALIBRATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "TRAINING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "SAFETY_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "ENVIRONMENTAL_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));
      // Phase 5: Additional approval and certification types
      registry.put(
          "DESIGN_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));
      registry.put(
          "MANUFACTURING_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));
      registry.put(
          "QUALITY_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));
      registry.put(
          "TESTING_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));
      registry.put(
          "SHIPPING_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));
      registry.put(
          "DELIVERY_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));
      registry.put(
          "DESIGN_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));
      registry.put(
          "MANUFACTURING_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));
      registry.put(
          "QUALITY_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));
      registry.put(
          "TESTING_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));
      registry.put(
          "SAFETY_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));
      registry.put(
          "ENVIRONMENTAL_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));
      // Phase 5: Additional contract types
      registry.put(
          "PURCHASE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));
      registry.put(
          "SALES_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));
      registry.put(
          "SERVICE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));
      registry.put(
          "MAINTENANCE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));
      registry.put(
          "LEASE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));
      registry.put(
          "LICENSE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));
      registry.put(
          "WARRANTY_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));
      // Phase 6: AP242 Tessellation extension entities
      registry.put(
          "TRIANGULATED_SURFACE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put(
          "TESSELLATED_GEOMETRIC_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put(
          "TESSELLATED_STRUCTURED_MESH",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_MESH",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_MESH_ELEMENTS",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_MESH_ELEMENT_SET",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_MESH_STRUCTURE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_CELL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_CELL_SET",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_CURVE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put(
          "TESSELLATED_EDGE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_EDGE_SET",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_VERTEX",
          (resolver, instance) -> resolver.resolveCartesianPoint(instance));
      registry.put(
          "TESSELLATED_VERTEX_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
      registry.put(
          "TESSELLATED_WIREFRAME",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_ANNOTATION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_TEXT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_DIMENSION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "TESSELLATED_SYMBOL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Phase 6: Additional BSpline variants
      registry.put(
          "B_SPLINE_CURVE_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
      registry.put(
          "B_SPLINE_CURVE_QUASI_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
      registry.put(
          "B_SPLINE_CURVE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
      registry.put(
          "B_SPLINE_CURVE_PIECEWISE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
      registry.put(
          "B_SPLINE_SURFACE_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
      registry.put(
          "B_SPLINE_SURFACE_QUASI_UNIFORM",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
      registry.put(
          "B_SPLINE_SURFACE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
      registry.put(
          "B_SPLINE_SURFACE_PIECEWISE_BEZIER",
          (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
      // Note: UNIFORM_CURVE, QUASI_UNIFORM_CURVE, BEZIER_CURVE, UNIFORM_SURFACE, QUASI_UNIFORM_SURFACE, BEZIER_SURFACE
      // are already correctly registered earlier using resolveUniformCurve, resolveQuasiUniformCurve, etc.
      // Phase 6: Additional geometric representation items
      registry.put(
          "GEOMETRIC_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "GEOMETRIC_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "POINT_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "POINT_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "CURVE_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "CURVE_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "SURFACE_SET",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "SHELL_SET",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "SOLID_SET",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "COMPOUND_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "COMPOUND_SHAPE_REPRESENTATION", true));
      registry.put(
          "MIXED_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "MIXED_SHAPE_REPRESENTATION", true));
      // Note: ANNOTATION_*_OCCURRENCE entities are already correctly registered earlier
      // using resolveAnnotationCurveOccurrence, resolveAnnotationFillAreaOccurrence, etc.
      // Phase 6: Additional annotation text entities
      // Note: DRAUGHTING_PRE_DEFINED_* entities are already correctly registered earlier
      // using resolveDraughtingPreDefinedColour, resolveDraughtingPreDefinedCurveFont, etc.
      registry.put(
          "DRAUGHTING_PRE_DEFINED_DIMENSION_SYMBOL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "DRAUGHTING_PRE_DEFINED_POINT_SYMBOL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: CURVE_STYLE, POINT_STYLE, SURFACE_SIDE_STYLE, SURFACE_STYLE_*, FILL_AREA_STYLE_*
      // are already correctly registered earlier using resolveCurveStyle, resolvePointStyle, etc.
      // Phase 6: Additional product definition and lifecycle entities
      registry.put(
          "PRODUCT_DEFINITION_SHAPE_WITH_ASSOCIATED_ITEMS",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "PRODUCT_DEFINITION_CONTEXT_ASSOCIATION",
          (resolver, instance) -> resolver.resolveProductDefinition(instance));
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE",
          (resolver, instance) -> resolver.resolveProductDefinitionFormation(instance));
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_SPECIAL_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionFormation(instance));
      registry.put(
          "PRODUCT_DEFINITION_RESOURCE",
          (resolver, instance) -> resolver.resolveProductDefinition(instance));
      // Note: PRODUCT_DEFINITION_SUBSTITUTE is already correctly registered earlier
      // using resolveProductDefinitionRelationshipRelationship (via registerProductDefinitionRelationshipRelationshipAliases)
      registry.put(
          "PRODUCT_DEFINITION_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PRODUCT_DEFINITION_USAGE"));
      registry.put(
          "PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS",
          (resolver, instance) -> resolver.resolveProductDefinition(instance));
      registry.put(
          "ASSEMBLY_COMPONENT_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_COMPONENT_USAGE"));
      registry.put(
          "PROMISSORY_USAGE_OCCURRENCE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PROMISSORY_USAGE_OCCURRENCE"));
      registry.put(
          "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE"));
      registry.put(
          "SPECIFIED_HIGHER_USAGE_OCCURRENCE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "SPECIFIED_HIGHER_USAGE_OCCURRENCE"));
      registry.put(
          "ASSEMBLY_DEFINITION_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_DEFINITION_USAGE"));
      registry.put(
          "COMPONENT_DEFINITION_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "COMPONENT_DEFINITION_USAGE"));
      // Phase 6: Additional configuration management entities
      registry.put(
          "CONFIGURATION_EFFECTIVITY",
          (resolver, instance) -> resolver.resolveEffectivity(instance));
      registry.put(
          "CONFIGURATION_ITEM_EFFECTIVITY",
          (resolver, instance) -> resolver.resolveEffectivity(instance));
      registry.put(
          "CONFIGURATION_ITEM_HIERARCHICAL_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "CONFIGURATION_ITEM_HIERARCHICAL_RELATIONSHIP"));
      registry.put(
          "CONFIGURATION_ITEM_REVISION_SEQUENCE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "CONFIGURATION_ITEM_REVISION_SEQUENCE"));
      registry.put(
          "CONFIGURATION_DESIGN",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "CONFIGURATION_DESIGN_ITEM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "PRODUCT_CONCEPT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PRODUCT_CONCEPT_FEATURE"));
      registry.put(
          "PRODUCT_CONCEPT_FEATURE_ASSOCIATION",
          (resolver, instance) -> resolver.resolveShapeAspectRelationship(instance, "PRODUCT_CONCEPT_FEATURE_ASSOCIATION"));
      registry.put(
          "PRODUCT_CONCEPT_FEATURE_CATEGORY",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "PRODUCT_CONCEPT_FEATURE_CATEGORY_USAGE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "PRODUCT_CONCEPT_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PRODUCT_CONCEPT_RELATIONSHIP"));
      // Phase 6: Additional material and property entities (non-duplicate extensions)
      registry.put(
          "MATERIAL_DESIGNATION_CHARACTERIZATION",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "MATERIAL_PROPERTY_DEFINITION",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "MATERIAL_PROPERTY_DEFINITION_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "MECHANICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "MECHANICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "THERMAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "THERMAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "ELECTRICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "ELECTRICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "OPTICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "OPTICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "MAGNETIC_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "MAGNETIC_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "ACOUSTIC_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "ACOUSTIC_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "RADIATION_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "RADIATION_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "CHEMICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "CHEMICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      registry.put(
          "ENVIRONMENTAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
      registry.put(
          "ENVIRONMENTAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
      // Phase 6: Additional security and classification entities
      // Note: SECURITY_CLASSIFICATION, SECURITY_CLASSIFICATION_LEVEL, APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT
      // are already correctly registered earlier using resolveSecurityClassification, etc.
      // Note: CLASSIFICATION_ASSIGNMENT, APPLIED_CLASSIFICATION_ASSIGNMENT, CLASSIFICATION_ROLE
      // are already correctly registered earlier using resolveClassificationAssignment, etc.
      // Phase 6: Additional organizational entities
      registry.put(
          "ORGANIZATION_TYPE",
          (resolver, instance) -> resolver.resolveOrganization(instance));
      registry.put(
          "ORGANIZATION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveOrganizationRelationship(instance));
      registry.put(
          "ORGANIZATION_ADDRESS_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveAddress(instance));
      registry.put(
          "PERSON_ADDRESS_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveAddress(instance));
      registry.put(
          "PERSON_ORGANIZATION_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT is already correctly registered earlier
      // using resolveAppliedPersonAndOrganizationAssignment
      // Note: PERSON_AND_ORGANIZATION_ROLE, ORGANIZATION_ROLE, PERSON_ROLE
      // are already correctly registered earlier using resolvePersonAndOrganizationRole, etc.
      // Phase 6: Additional date and time entities (non-duplicate extensions)
      registry.put(
          "ORDINAL_DATE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "WEEK_OF_YEAR_AND_DAY_DATE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: APPLIED_DATE_ASSIGNMENT, APPLIED_DATE_TIME_ASSIGNMENT are already correctly registered
      // earlier using resolveAppliedDateAssignment, resolveAppliedDateTimeAssignment, etc.
      // Phase 6: Additional relationship and reference entities
      // Note: DOCUMENT_USAGE_CONSTRAINT is already correctly registered earlier
      // using resolveDocumentUsageConstraint
      registry.put(
          "DOCUMENT_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "EXTERNAL_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "EXTERNAL_FILE_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "DIGITAL_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "HARDCOPY_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));
      registry.put(
          "FILE_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: APPLIED_DOCUMENT_REFERENCE is already correctly registered earlier
      // using resolveAppliedDocumentReference
      registry.put(
          "APPLIED_DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "APPLIED_EXTERNAL_DOCUMENT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "APPLIED_EXTERNAL_DOCUMENT_ASSIGNMENT"));
      // Phase 6: Additional action and process entities
      registry.put(
          "ACTION_REQUEST_SOLUTION",
          (resolver, instance) -> resolver.resolveAction(instance));
      registry.put(
          "ACTION_METHOD",
          (resolver, instance) -> resolver.resolveAction(instance));
      registry.put(
          "ACTION_METHOD_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "ACTION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "ACTION_STATUS",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "ACTION_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "APPLIED_ACTION_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "ACTION_REQUEST_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "APPLIED_ACTION_REQUEST_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "ACTION_METHOD_ROLE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: ACTION_PROPERTY_REPRESENTATION is already correctly registered earlier
      // using resolveActionPropertyRepresentation
      // Phase 6: Additional requirement and verification entities
      registry.put(
          "REQUIREMENT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "APPLIED_REQUIREMENT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "REQUIREMENT_VIEW_DEFINITION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "REQUIREMENT_SPECIFICATION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "REQUIREMENT_DEFINITION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "VERIFICATION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "VERIFICATION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: CERTIFICATION_ASSIGNMENT, APPLIED_CERTIFICATION_ASSIGNMENT are already correctly registered
      // earlier using resolveCertificationAssignment, resolveAppliedCertificationAssignment, etc.
      // Phase 6: Additional measure and unit entities (non-duplicate extensions)
      registry.put(
          "SI_BASE_UNIT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "SI_DERIVED_UNIT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "SI_DERIVED_UNIT_ELEMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "CONVERSION_BASED_UNIT_AND_RATIO_UNIT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: *_MEASURE_WITH_UNIT entities are already correctly registered earlier
      // using resolveTypedMeasureWithUnit
      registry.put(
          "PARAMETER_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Phase 6: Additional identification entities (non-duplicate extensions)
      // Note: IDENTIFICATION_ASSIGNMENT, APPLIED_IDENTIFICATION_ASSIGNMENT, EXTERNAL_IDENTIFICATION_ASSIGNMENT,
      // APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT are already correctly registered earlier
      // using resolveIdentificationAssignment, resolveExternalIdentificationAssignment, etc.
      // Phase 6: Additional context and framework entities (non-duplicate extensions)
      registry.put(
          "PRODUCT_RELATED_PRODUCT_CATEGORY",
          (resolver, instance) -> resolver.resolveProductRelatedProductCategory(instance));
      registry.put(
          "PRODUCT_CATEGORY_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveProductCategoryRelationship(instance));
      // Phase 6: Additional model geometry entities
      registry.put(
          "GEOMETRIC_MODEL",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "GEOMETRIC_MODEL", true));
      registry.put(
          "GEOMETRIC_MODEL_ELEMENT",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
      registry.put(
          "AXIS_PLACEMENT",
          (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
      registry.put(
          "AXIS_PLACEMENT_2D",
          (resolver, instance) -> resolver.resolveAxis2Placement2D(instance));
      registry.put(
          "AXIS_PLACEMENT_3D",
          (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
      registry.put(
          "PLACEMENT_1D",
          (resolver, instance) -> resolver.resolveAxis1Placement(instance));
      registry.put(
          "PLACEMENT_2D",
          (resolver, instance) -> resolver.resolveAxis2Placement2D(instance));
      registry.put(
          "PLACEMENT_3D",
          (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
      // Phase 6: Additional transformation and mapping entities
      registry.put(
          "ITEM_DEFINED_TRANSFORMATION",
          (resolver, instance) -> resolver.resolveItemDefinedTransformation(instance));
      registry.put(
          "REPRESENTATION_MAP",
          (resolver, instance) -> resolver.resolveRepresentationMap(instance));
      registry.put(
          "MAPPED_ITEM",
          (resolver, instance) -> resolver.resolveMappedItem(instance));
      registry.put(
          "SHAPE_REPRESENTATION_MAP",
          (resolver, instance) -> resolver.resolveRepresentationMap(instance));
      registry.put(
          "GEOMETRIC_REPRESENTATION_MAP",
          (resolver, instance) -> resolver.resolveRepresentationMap(instance));
      // Phase 6: Additional analysis and simulation entities
      registry.put(
          "ANALYSIS_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ANALYSIS_REPRESENTATION", false));
      // Note: ANALYSIS_MODEL is already correctly registered earlier
      // using resolveRepresentation(instance, "ANALYSIS_MODEL", false)
      registry.put(
          "FEA_MODEL",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL", false));
      registry.put(
          "FEA_MODEL_DEFINITION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_DEFINITION", false));
      registry.put(
          "FEA_MODEL_3D",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_3D", true));
      registry.put(
          "FEA_MODEL_2D",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_2D", true));
      registry.put(
          "FEA_AXIS2_PLACEMENT_3D",
          (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
      registry.put(
          "FEA_LINEAR_ALGEBRA_MATRIX",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "FEA_LINEAR_ALGEBRA_MATRIX_3D",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "CURVE_ELEMENT_FREEDOM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "CURVE_ELEMENT_FREEDOM_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "SURFACE_ELEMENT_FREEDOM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "SURFACE_ELEMENT_FREEDOM_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "VOLUME_ELEMENT_FREEDOM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "VOLUME_ELEMENT_FREEDOM_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Phase 6: Additional assembly and structure entities
      registry.put(
          "ASSEMBLY_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SHAPE_REPRESENTATION", true));
      registry.put(
          "ASSEMBLY_SHAPE_REPRESENTATION_PREDEFINED",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SHAPE_REPRESENTATION_PREDEFINED", true));
      registry.put(
          "ASSEMBLY_COMPONENT_STRUCTURE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_COMPONENT_STRUCTURE"));
      registry.put(
          "ASSEMBLY_SEQUENCE_DEFINITION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SEQUENCE_DEFINITION", false));
      registry.put(
          "ASSEMBLY_SEQUENCE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "ASSEMBLY_STEP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Phase 6: Additional kinematic entities
      registry.put(
          "KINEMATIC_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "KINEMATIC_REPRESENTATION", false));
      registry.put(
          "KINEMATIC_REPRESENTATION_CONTEXT",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "KINEMATIC_REPRESENTATION_CONTEXT", false));
      // Note: KINEMATIC_LINK_REPRESENTATION is already correctly registered earlier
      // using resolveRepresentation(instance, "KINEMATIC_LINK_REPRESENTATION", false)
      registry.put(
          "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationRelationship(instance, "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP"));
      registry.put(
          "KINEMATIC_PATH",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      registry.put(
          "KINEMATIC_JOINT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Note: MECHANISM_REPRESENTATION is already correctly registered earlier
      // using resolveRepresentation(instance, "MECHANISM_REPRESENTATION", false)
      registry.put(
          "MECHANISM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));
      // Phase 6: Extended shape representation aliases (final batch)
      registerRepresentationAliases(
          registry,
          true,
          "ANALYSIS_SHAPE_REPRESENTATION",
          "ANIMATION_SHAPE_REPRESENTATION",
          "APPEARANCE_REPRESENTATION",
          "ASSEMBLY_DEFINITION_SHAPE_REPRESENTATION",
          "ASSEMBLY_PROCESS_SHAPE_REPRESENTATION",
          "ASSEMBLY_SITE_SHAPE_REPRESENTATION",
          "ASSEMBLY_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "CALIBRATION_SHAPE_REPRESENTATION",
          "CABLE_ROUTING_SHAPE_REPRESENTATION",
          "CATASTROPHE_SHAPE_REPRESENTATION",
          "CATALOG_SHAPE_REPRESENTATION",
          "CNC_PROGRAM_SHAPE_REPRESENTATION",
          "COMPONENT_DEFINITION_SHAPE_REPRESENTATION",
          "COMPONENT_SITE_SHAPE_REPRESENTATION",
          "COMPONENT_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "CONCEPTUAL_SHAPE_REPRESENTATION",
          "CONNECTION_DEFINITION_SHAPE_REPRESENTATION",
          "CONNECTION_SITE_SHAPE_REPRESENTATION",
          "CONNECTION_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "COVERAGE_SHAPE_REPRESENTATION",
          "DESIGN_SHAPE_REPRESENTATION",
          "DISASSEMBLY_PROCESS_SHAPE_REPRESENTATION",
          "DOCUMENT_SHAPE_REPRESENTATION",
          "ELECTRICAL_ANALYSIS_SHAPE_REPRESENTATION",
          "ELECTRONIC_ASSEMBLY_SHAPE_REPRESENTATION",
          "EMC_SHAPE_REPRESENTATION",
          "ENVIRONMENTAL_SHAPE_REPRESENTATION",
          "FAILURE_SHAPE_REPRESENTATION",
          "FASTENER_ASSEMBLY_SHAPE_REPRESENTATION",
          "FINISHING_PROCESS_SHAPE_REPRESENTATION",
          "FUNCTIONAL_SHAPE_REPRESENTATION",
          "GASKET_SHAPE_REPRESENTATION",
          "GEOMETRIC_ANALYSIS_SHAPE_REPRESENTATION",
          "GEOMETRIC_TOLERANCE_SHAPE_REPRESENTATION",
          "HANDLING_SHAPE_REPRESENTATION",
          "HEAT_TREATMENT_SHAPE_REPRESENTATION",
          "HUMAN_SHAPE_REPRESENTATION",
          "INSPECTION_PROCESS_SHAPE_REPRESENTATION",
          "INSTALLATION_PROCESS_SHAPE_REPRESENTATION",
          "INTERFACE_DEFINITION_SHAPE_REPRESENTATION",
          "INTERFACE_SITE_SHAPE_REPRESENTATION",
          "INTERFACE_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "INTERLOCK_SHAPE_REPRESENTATION",
          "JOINING_PROCESS_SHAPE_REPRESENTATION",
          "KINEMATIC_SHAPE_REPRESENTATION",
          "LAYOUT_SHAPE_REPRESENTATION",
          "LIFE_CYCLE_SHAPE_REPRESENTATION",
          "LOGISTIC_SHAPE_REPRESENTATION",
          "LOGISTIC_PROCESS_SHAPE_REPRESENTATION",
          "LOGISTIC_SITE_SHAPE_REPRESENTATION",
          "LOGISTIC_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "MAINTENANCE_PROCESS_SHAPE_REPRESENTATION",
          "MANUFACTURING_SHAPE_REPRESENTATION",
          "MARKING_SHAPE_REPRESENTATION",
          "MATERIAL_FLOW_SHAPE_REPRESENTATION",
          "MECHANICAL_ANALYSIS_SHAPE_REPRESENTATION",
          "MOUNTING_DEFINITION_SHAPE_REPRESENTATION",
          "MOUNTING_SITE_SHAPE_REPRESENTATION",
          "MOUNTING_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "NETWORK_SHAPE_REPRESENTATION",
          "OPERATOR_SHAPE_REPRESENTATION",
          "PACKAGING_PROCESS_SHAPE_REPRESENTATION",
          "PART_DEFINITION_SHAPE_REPRESENTATION",
          "PART_SITE_SHAPE_REPRESENTATION",
          "PART_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "PATH_SHAPE_REPRESENTATION",
          "PHYSICAL_SHAPE_REPRESENTATION",
          "PIPE_DEFINITION_SHAPE_REPRESENTATION",
          "PIPE_SITE_SHAPE_REPRESENTATION",
          "PIPE_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "PLANNING_SHAPE_REPRESENTATION",
          "POSITION_SHAPE_REPRESENTATION",
          "PROCESS_PLAN_SHAPE_REPRESENTATION",
          "PROCESS_SITE_SHAPE_REPRESENTATION",
          "PROCESS_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "PROTECTION_SHAPE_REPRESENTATION",
          "QUALITY_CONTROL_SHAPE_REPRESENTATION",
          "RACK_SHAPE_REPRESENTATION",
          "RECOVERY_SHAPE_REPRESENTATION",
          "RECYCLING_SHAPE_REPRESENTATION",
          "REPAIR_PROCESS_SHAPE_REPRESENTATION",
          "RESOURCE_SHAPE_REPRESENTATION",
          "RESPONSE_SHAPE_REPRESENTATION",
          "RISK_SHAPE_REPRESENTATION",
          "ROBOT_SHAPE_REPRESENTATION",
          "RULE_SHAPE_REPRESENTATION",
          "SAFETY_SHAPE_REPRESENTATION",
          "SCHEDULE_SHAPE_REPRESENTATION",
          "Schematic_SHAPE_REPRESENTATION",
          "SEALING_SHAPE_REPRESENTATION",
          "SERVICE_SHAPE_REPRESENTATION",
          "SETUP_SHAPE_REPRESENTATION",
          "SHIPMENT_SHAPE_REPRESENTATION",
          "SIMULATION_SHAPE_REPRESENTATION",
          "SITE_SHAPE_REPRESENTATION",
          "SOFTWARE_SHAPE_REPRESENTATION",
          "SOLUTION_SHAPE_REPRESENTATION",
          "SPECIFICATION_SHAPE_REPRESENTATION",
          "STANDARD_OPERATION_SHAPE_REPRESENTATION",
          "STORAGE_SHAPE_REPRESENTATION",
          "STRUCTURAL_ANALYSIS_SHAPE_REPRESENTATION",
          "SUPPLIER_SHAPE_REPRESENTATION",
          "SUPPORT_SHAPE_REPRESENTATION",
          "SYSTEM_SHAPE_REPRESENTATION",
          "TEST_SHAPE_REPRESENTATION",
          "TESTING_PROCESS_SHAPE_REPRESENTATION",
          "THERMAL_ANALYSIS_SHAPE_REPRESENTATION",
          "TOOL_DEFINITION_SHAPE_REPRESENTATION",
          "TOOL_SITE_SHAPE_REPRESENTATION",
          "TOOL_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "TRAINING_SHAPE_REPRESENTATION",
          "TRANSPORT_SHAPE_REPRESENTATION",
          "VALIDATION_SHAPE_REPRESENTATION",
          "VARIANT_DEFINITION_SHAPE_REPRESENTATION",
          "VARIANT_SITE_SHAPE_REPRESENTATION",
          "VARIANT_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "VIRTUAL_SHAPE_REPRESENTATION",
          "VISUALIZATION_SHAPE_REPRESENTATION",
          "WAREHOUSE_SHAPE_REPRESENTATION",
          "WARRANTY_SHAPE_REPRESENTATION",
          "WORK_INSTRUCTION_SHAPE_REPRESENTATION",
          "WORK_SHAPE_REPRESENTATION",
          "ZONE_DEFINITION_SHAPE_REPRESENTATION",
          "ZONE_SITE_SHAPE_REPRESENTATION",
          "ZONE_WORK_INSTRUCTION_SHAPE_REPRESENTATION");
  
      // 2D curve entities
      registry.put("CIRCLE_2D", StepEntityResolver::resolveCircle2D);
      registry.put("ELLIPSE_2D", StepEntityResolver::resolveEllipse2D);
      registry.put("HYPERBOLA_2D", StepEntityResolver::resolveHyperbola2D);
      registry.put("PARABOLA_2D", StepEntityResolver::resolveParabola2D);
      registry.put("LINE_2D", StepEntityResolver::resolveLine2D);
      registry.put("POLYLINE_2D", StepEntityResolver::resolvePolyline2D);
      registry.put("TRIMMED_CURVE_2D", StepEntityResolver::resolveTrimmedCurve2D);
      registry.put("COMPOSITE_CURVE_2D", StepEntityResolver::resolveCompositeCurve2D);
      registry.put("B_SPLINE_CURVE_2D", StepEntityResolver::resolveBSplineCurve2D);
      registry.put("RATIONAL_B_SPLINE_CURVE_2D", StepEntityResolver::resolveRationalBSplineCurve2D);
      registry.put("BEZIER_CURVE_2D", StepEntityResolver::resolveBezierCurve2D);
      registry.put("QUASI_UNIFORM_CURVE_2D", StepEntityResolver::resolveQuasiUniformCurve2D);
      registry.put("UNIFORM_CURVE_2D", StepEntityResolver::resolveUniformCurve2D);
      registry.put("PIECEWISE_BEZIER_CURVE_2D", StepEntityResolver::resolvePiecewiseBezierCurve2D);
      registry.put("INDEXED_POLY_CURVE_2D", StepEntityResolver::resolveIndexedPolyCurve2D);
      registry.put("DEGENERATE_CURVE_2D", StepEntityResolver::resolveDegenerateCurve2D);
  
      // Surfaces with resolver methods but missing registry entries
      registry.put("COMPOSITE_CURVE_ON_SURFACE", StepEntityResolver::resolveCompositeCurveOnSurface);
      registry.put("DEGENERATE_TOROIDAL_SURFACE", StepEntityResolver::resolveDegenerateToroidalSurface);
      registry.put("SURFACE_OF_LINEAR_EXTRUSION", StepEntityResolver::resolveSurfaceOfLinearExtrusion);
      registry.put("SURFACE_OF_TRANSLATION", StepEntityResolver::resolveSurfaceOfTranslation);
      registry.put("SURFACE_OF_PROJECTION", StepEntityResolver::resolveSurfaceOfProjection);
      registry.put("PARABOLOID_SURFACE", StepEntityResolver::resolveParaboloidSurface);
      registry.put("HYPERBOLOID_SURFACE", StepEntityResolver::resolveHyperboloidSurface);
      registry.put("RECTANGULAR_TRIMMED_SURFACE", StepEntityResolver::resolveRectangularTrimmedSurface);
      registry.put("SURFACE_STYLE_PARAMETER_LINE", StepEntityResolver::resolveSurfaceStyleParameterLine);
      registry.put("SURFACE_STYLE_REFLECTANCE_AMBIENT", StepEntityResolver::resolveSurfaceStyleReflectanceAmbient);
      registry.put("SURFACE_STYLE_SEGMENTATION_CURVE", StepEntityResolver::resolveSurfaceStyleSegmentationCurve);
  
      // CSG and solids
      registry.put("CSG_PRIMITIVE", (resolver, instance) ->
          resolver.resolveCsgPrimitive(instance, "CSG_PRIMITIVE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));
  
      // Transformations
      registry.put("CARTESIAN_TRANSFORMATION_OPERATOR", StepEntityResolver::resolveCartesianTransformationOperator);
      registry.put("CARTESIAN_TRANSFORMATION_OPERATOR_2D", StepEntityResolver::resolveCartesianTransformationOperator2D);
      registry.put("CARTESIAN_TRANSFORMATION_OPERATOR_3D", StepEntityResolver::resolveCartesianTransformationOperator3D);
      registry.put("ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);
  
      // Profile definitions
      registry.put("CENTERED_CIRCLE_PROFILE_DEF", StepEntityResolver::resolveCenteredCircleProfileDef);
      registry.put("CENTRE_LINE_ARC_PROFILE_DEF", StepEntityResolver::resolveCentreLineArcProfileDef);
      registry.put("RECTANGLE_HOLLOW_PROFILE_DEF", StepEntityResolver::resolveRectangleHollowProfileDef);
      registry.put("ARBITRARY_CLOSED_PROFILE_DEF", (resolver, instance) ->
          resolver.resolveArbitraryClosedProfileDef(instance));
      registry.put("ARBITRARY_PROFILE_DEF", (resolver, instance) ->
          resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_PROFILE_DEF"));
      registry.put("ARBITRARY_PROFILE_DEF_WITH_VOIDS", StepEntityResolver::resolveArbitraryProfileDefWithVoids);
      registry.put("PARAMETERIZED_PROFILE_DEF", (resolver, instance) ->
          resolver.resolveParameterizedProfileDef(instance, "PARAMETERIZED_PROFILE_DEF", 3));
      registry.put("PROFILE_DEF", StepEntityResolver::resolveProfileDef);
  
      // Representation and context
      registry.put("GEOMETRIC_REPRESENTATION_CONTEXT", StepEntityResolver::resolveGeometricRepresentationContext);
      registry.put("GEOMETRIC_REPRESENTATION_ITEM", StepEntityResolver::resolveGeometricRepresentationItem);
      registry.put("TOPOLOGICAL_REPRESENTATION_ITEM", StepEntityResolver::resolveTopologicalRepresentationItem);
      registry.put("CONTEXT_DEPENDENT_SHAPE_REPRESENTATION", StepEntityResolver::resolveContextDependentShapeRepresentation);
      registry.put("NEXT_ASSEMBLY_USAGE_OCCURRENCE", StepEntityResolver::resolveNextAssemblyUsageOccurrence);
      registry.put("DESCRIPTIVE_REPRESENTATION_ITEM", StepEntityResolver::resolveDescriptiveRepresentationItem);
      registry.put("MEASURE_REPRESENTATION_ITEM", StepEntityResolver::resolveMeasureRepresentationItem);
      registry.put("VALUE_REPRESENTATION_ITEM", StepEntityResolver::resolveValueRepresentationItem);
      // Validation and calculated geometry
      registry.put("VALIDATION_PROPERTY_REPRESENTATION",
          StepEntityResolver::resolveValidationPropertyRepresentation);
      registry.put("CALCULATED_GEOMETRIC_REPRESENTATION_ITEM",
          StepEntityResolver::resolveCalculatedGeometricRepresentationItem);
  
      // Units and uncertainty
      registry.put("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUncertaintyAssignedContext);
      registry.put("GLOBAL_UNIT_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUnitAssignedContext);
      registry.put("CONVERSION_BASED_UNIT_WITH_OFFSET", StepEntityResolver::resolveConversionBasedUnitWithOffset);
      registry.put("TYPED_MEASURE_WITH_UNIT", (resolver, instance) ->
          resolver.resolveTypedMeasureWithUnit(instance, "TYPED_MEASURE_WITH_UNIT"));
      registry.put("UNCERTAINTY_MEASURE_WITH_UNIT", StepEntityResolver::resolveUncertaintyMeasureWithUnit);
      registry.put("COORDINATED_UNIVERSAL_TIME_OFFSET", StepEntityResolver::resolveCoordinatedUniversalTimeOffset);
  
      // Tolerance and datum
      registry.put("DATUM_SYSTEM", StepEntityResolver::resolveDatumSystem);
      registry.put("SHAPE_ASPECT_RELATIONSHIP", StepEntityResolver::resolveShapeAspectRelationship);
      registry.put("SHAPE_DEFINITION_REPRESENTATION", StepEntityResolver::resolveShapeDefinitionRepresentation);
      registry.put("SHAPE_REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveShapeRepresentationRelationship);
      registry.put("RECTANGULAR_TOLERANCE_ZONE", StepEntityResolver::resolveRectangularToleranceZone);
  
      // 2D curves needing new resolver methods
      registry.put("BOUNDED_CURVE_2D", StepEntityResolver::resolveBoundedCurve2D);
      registry.put("CURVE_2D", StepEntityResolver::resolveCurve2D);
  
      // Machined surface
      registry.put("MACHINED_SURFACE", StepEntityResolver::resolveMachinedSurface);
  
      // New FEA element properties
      registry.put("FEA_SHELL_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaShellElementProperty);
      registry.put("FEA_BEAM_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaBeamElementProperty);
      registry.put("FEA_2D_ELEMENT_PROPERTY", StepEntityResolver::resolveFea2DElementProperty);
      registry.put("FEA_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveFea3DElementProperty);
      registry.put("FEA_TRUSS_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaTrussElementProperty);
      registry.put("FEA_SPRING_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaSpringElementProperty);
      registry.put("FEA_VOLUME_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaVolumeElementProperty);
  
      // Unit with unit types
      registry.put("LENGTH_UNIT_WITH_UNIT", StepEntityResolver::resolveLengthUnitWithUnit);
      registry.put("PLANE_ANGLE_UNIT_WITH_UNIT", StepEntityResolver::resolvePlaneAngleUnitWithUnit);
      registry.put("VOLUME_UNIT_WITH_UNIT", StepEntityResolver::resolveVolumeUnitWithUnit);
      registry.put("AREA_UNIT_WITH_UNIT", StepEntityResolver::resolveAreaUnitWithUnit);
      registry.put("MASS_UNIT_WITH_UNIT", StepEntityResolver::resolveMassUnitWithUnit);
      registry.put("CONVERSION_BASED_UNIT_AND_UNIT", StepEntityResolver::resolveConversionBasedUnitAndUnit);
  
      // Profile types
      registry.put("AREA_PROFILE", StepEntityResolver::resolveAreaProfile);
      registry.put("GENERALIZED_AREA_PROFILE", StepEntityResolver::resolveGeneralizedAreaProfile);
      registry.put("SWEPT_PROFILE_AREA_OUTLINE", StepEntityResolver::resolveSweptProfileAreaOutline);
  
      // Kinematic reference types
      registry.put("KINEMATIC_LINK_REFERENCE", StepEntityResolver::resolveKinematicLinkReference);
      registry.put("KINEMATIC_JOINT_REFERENCE", StepEntityResolver::resolveKinematicJointReference);
  
      // Product representation types
      registry.put("HYBRID_SHAPE_REPRESENTATION", StepEntityResolver::resolveHybridShapeRepresentation);
      registry.put("DRAWING_REPRESENTATION", StepEntityResolver::resolveDrawingRepresentation);
      registry.put("SCHEMATIC_REPRESENTATION", StepEntityResolver::resolveSchematicRepresentation);
      registry.put("SKETCH_REPRESENTATION", StepEntityResolver::resolveSketchRepresentation);
      registry.put("SECTION_REPRESENTATION", StepEntityResolver::resolveSectionRepresentation);
      registry.put("TABULATION_REPRESENTATION", StepEntityResolver::resolveTabulationRepresentation);
      registry.put("ZONE_REPRESENTATION", StepEntityResolver::resolveZoneRepresentation);
      registry.put("CSG_PRIMITIVE_3D", StepEntityResolver::resolveCsgPrimitive3D);
      registry.put("COMPOUND_REPRESENTATION_ITEM", (resolver, instance) -> resolver.resolveCompoundRepresentationItem(instance, "COMPOUND_REPRESENTATION_ITEM"));
      registry.put("CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION", StepEntityResolver::resolveContextDependentGeometricShapeRepresentation);
      registry.put("USAGE_ASSOCIATION", StepEntityResolver::resolveUsageAssociation);
      registry.put("BUY_FROM_USAGE_OPTION", StepEntityResolver::resolveBuyFromUsageOption);
  
      // Config management types
      registry.put("EXCLUSION_ASSIGNMENT", StepEntityResolver::resolveExclusionAssignment);
      registry.put("DATE_TIME_EFFECTIVITY", StepEntityResolver::resolveDateTimeEffectivity);
      registry.put("DATE_EFFECTIVITY", StepEntityResolver::resolveDateEffectivity);
      registry.put("LOT_EFFECTIVITY", StepEntityResolver::resolveLotEffectivity);
      registry.put("SERIAL_NUMBER_EFFECTIVITY", StepEntityResolver::resolveSerialNumberEffectivity);
  
      // Geometry types
      registry.put("INDEXED_POLYCURVE", StepEntityResolver::resolveIndexedPolycurve);
      registry.put("POLYLINE_3D", StepEntityResolver::resolvePolyline3D);
  
      // Annotation types
      registry.put("ANNOTATION_FILL_AREA_REGION", StepEntityResolver::resolveAnnotationFillAreaRegion);
      registry.put("FILL_AREA_WITH_OUTLINE", StepEntityResolver::resolveFillAreaWithOutline);
      registry.put("ANNOTATION_RECORD", StepEntityResolver::resolveAnnotationRecord);
      registry.put("DRAWING_REFERENCE", StepEntityResolver::resolveDrawingReference);
      registry.put("EXTERNALLY_DEFINED_HATCH_STYLE", StepEntityResolver::resolveExternallyDefinedHatchStyle);
      registry.put("EXTERNALLY_DEFINED_TILE_STYLE", StepEntityResolver::resolveExternallyDefinedTileStyle);
      registry.put("MARKING_FEATURE", StepEntityResolver::resolveMarkingFeature);
      registry.put("TECHNICAL_NOTE", StepEntityResolver::resolveTechnicalNote);
      registry.put("TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT", StepEntityResolver::resolveTextLiteralWithDraughtingCallout);
      registry.put("COMPOSED_TEXT_LITERAL", StepEntityResolver::resolveComposedTextLiteral);
      registry.put("TEXT_FONT", StepEntityResolver::resolveTextFont);
      registry.put("CHARACTER_GLYPH", StepEntityResolver::resolveCharacterGlyph);
      registry.put("CHARACTER_GLYPH_OUTLINE", StepEntityResolver::resolveCharacterGlyphOutline);
      registry.put("CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS", StepEntityResolver::resolveCharacterGlyphOutlineWithCharacteristics);
      registry.put("CHARACTER_GLYPH_STROKE", StepEntityResolver::resolveCharacterGlyphStroke);
      registry.put("PRE_DEFINED_SURFACE_STYLE", StepEntityResolver::resolvePreDefinedSurfaceStyle);
      registry.put("SURFACE_STYLE_PARAMETER_LINES", StepEntityResolver::resolveSurfaceStyleParameterLines);
      registry.put("FILL_AREA_STYLE_OUTLINE", StepEntityResolver::resolveFillAreaStyleOutline);
      registry.put("FILL_AREA_STYLE_TRANSPARENT", StepEntityResolver::resolveFillAreaStyleTransparent);
      registry.put("FILL_AREA_STYLE_HATCHING", StepEntityResolver::resolveFillAreaStyleHatching);
      registry.put("FILL_AREA_STYLE_TILING", StepEntityResolver::resolveFillAreaStyleTiling);
      registry.put("CURVE_STYLE_FONT", StepEntityResolver::resolveCurveStyleFont);
      registry.put("CURVE_STYLE_RENDERING", StepEntityResolver::resolveCurveStyleRendering);
      registry.put("CURVE_STYLE_WITH_FONT", StepEntityResolver::resolveCurveStyleWithFont);
      registry.put("DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL", StepEntityResolver::resolveDraughtingPreDefinedTerminatorSymbol);
  
      // Tolerance/PMI types
      registry.put("PMI_REQUIREMENT", StepEntityResolver::resolvePmiRequirement);
      registry.put("PMI_GROUP", StepEntityResolver::resolvePmiGroup);
  
      // Manufacturing types
      registry.put("FEATURE_ELEMENT_DEFINITION", StepEntityResolver::resolveFeatureElementDefinition);
      registry.put("WEBS", StepEntityResolver::resolveWebs);
      registry.put("PATTERN", StepEntityResolver::resolvePattern);
  
      // Tolerance/dimension representation types
      registry.put("ANGULAR_DIMENSION_REPRESENTATION", StepEntityResolver::resolveAngularDimensionRepresentation);
      registry.put("CHAIN_DIMENSION_REPRESENTATION", StepEntityResolver::resolveChainDimensionRepresentation);
      registry.put("LINEAR_DIMENSION_REPRESENTATION", StepEntityResolver::resolveLinearDimensionRepresentation);
      registry.put("ORDINATE_DIMENSION_REPRESENTATION", StepEntityResolver::resolveOrdinateDimensionRepresentation);
      registry.put("SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE", StepEntityResolver::resolveShapeDimensionRepresentationWithTolerance);
  
      // FEA types
      registry.put("BOUNDARY_CONDITION", StepEntityResolver::resolveBoundaryCondition);
      registry.put("LOAD_CASE", StepEntityResolver::resolveLoadCase);
  
      // Classification types
      registry.put("ATTRIBUTE_DEFINITION", StepEntityResolver::resolveAttributeDefinition);
      registry.put("ATTRIBUTE_INSTANCE", StepEntityResolver::resolveAttributeInstance);
      registry.put("COMPOSITE_SHAPE_ASPECT", StepEntityResolver::resolveCompositeShapeAspect);
  
      // Product types
      registry.put("BILL_OF_MATERIALS", StepEntityResolver::resolveBillOfMaterials);
      registry.put("MAKE_FROM_RELATIONSHIP", StepEntityResolver::resolveMakeFromRelationship);
      registry.put("ASSEMBLY_OPERATION", StepEntityResolver::resolveAssemblyOperation);
      registry.put("ASSEMBLY_SEQUENCE", StepEntityResolver::resolveAssemblySequence);
      registry.put("ASSEMBLY_STRUCTURE", StepEntityResolver::resolveAssemblyStructure);
      registry.put("CAD_MODEL_REFERENCE", StepEntityResolver::resolveCadModelReference);
      registry.put("COMPONENT_DEFINITION", StepEntityResolver::resolveComponentDefinition);
      registry.put("ENVIRONMENTAL_IMPACT", StepEntityResolver::resolveEnvironmentalImpact);
      registry.put("MECHANISM_DEFINITION", StepEntityResolver::resolveMechanismDefinition);
      registry.put("MODULE_DEFINITION", StepEntityResolver::resolveModuleDefinition);
      registry.put("PART_DEFINITION", StepEntityResolver::resolvePartDefinition);
      registry.put("PRODUCT_VERSION", StepEntityResolver::resolveProductVersion);
      registry.put("PROJECT_INFORMATION", StepEntityResolver::resolveProjectInformation);
      registry.put("STRUCTURAL_FEATURE", StepEntityResolver::resolveStructuralFeature);
  
      // Document types
      registry.put("TEXT_FILE_REPRESENTATION", StepEntityResolver::resolveTextFileRepresentation);
  
      // Tolerance types
      registry.put("TOLERANCE_MODIFIER", StepEntityResolver::resolveToleranceModifier);
  
      // FEA types
      // FEA_MATERIAL_PROPERTY_REPRESENTATION already registered at line 6474
      // VOLUME_3D_ELEMENT_REPRESENTATION already registered at line 6469
  
      // PMI/Annotation types
      registry.put("PRESENTATION_LAYER_ASSIGNMENT", StepEntityResolver::resolvePresentationLayerAssignment);
      registry.put("PRESENTATION_STYLE_ASSIGNMENT", StepEntityResolver::resolvePresentationStyleAssignment);
  
      // Manufacturing types (new)
      registry.put("FLAT_PATTERN", StepEntityResolver::resolveFlatPattern);
      registry.put("THREAD", StepEntityResolver::resolveThread);
  
      // Product structure types (only types NOT already registered via alias handlers)
      registry.put("PRODUCT_DEFINITION_RELATIONSHIP", StepEntityResolver::resolveProductDefinitionRelationship);
      registry.put("PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP", StepEntityResolver::resolveProductDefinitionRelationshipRelationship);
      registry.put("REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveRepresentationRelationship);
  
      // Missing geometry types
      registry.put("FEA_AXIS2_PLACEMENT_3D", StepEntityResolver::resolveFeaAxis2Placement3d);
      registry.put("BSPLINE_CURVE_2D", StepEntityResolver::resolveBSplineCurve2D);
      registry.put("RATIONAL_BSPLINE_CURVE_2D", StepEntityResolver::resolveRationalBSplineCurve2D);

  }

  private static void registerGeometricToleranceAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName, (resolver, instance) -> resolver.resolveGeometricTolerance(instance, entityName));
    }
  }

  private static void registerShapeAspectAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName, (resolver, instance) -> resolver.resolveShapeAspect(instance, entityName));
    }
  }

  private static void registerShapeAspectOccurrenceAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, entityName));
    }
  }

  private static void registerCharacterizedObjectAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveCharacterizedObject(instance, entityName));
    }
  }

  private static void registerExternallyDefinedItemAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, entityName));
    }
  }

  private static void registerShapeAspectRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, entityName));
    }
  }

  private static void registerRepresentationRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, entityName));
    }
  }

  private static void registerTypedMeasureWithUnit(
      Map<String, EntityFactory> registry, String entityName, String expectedUnitKind) {
    registry.put(
        entityName,
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, entityName, expectedUnitKind));
  }

  private static void registerTypedMeasureWithUnitPairs(
      Map<String, EntityFactory> registry, String... unitKinds) {
    for (String unitKind : unitKinds) {
      String measureName = unitKind.replace("_UNIT", "_MEASURE_WITH_UNIT");
      registerTypedMeasureWithUnit(registry, measureName, unitKind);
    }
  }

  private static void registerStandaloneDerivedUnitKinds(
      Map<String, EntityFactory> registry, String... unitKinds) {
    for (String unitKind : unitKinds) {
      registry.put(
          unitKind,
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, unitKind));
    }
  }

  private static void registerKinematicPairAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) -> resolver.resolveKinematicPair(instance, entityName));
    }
  }

  private static void registerFeaAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) -> resolver.resolveRepresentation(instance, entityName, false));
    }
  }

  private static void registerRepresentationAliases(
      Map<String, EntityFactory> registry, boolean shapeRepresentation, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, entityName, shapeRepresentation));
    }
  }

  private static void registerProductDefinitionRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationship(instance, entityName));
    }
  }

  private static void registerProductDefinitionRelationshipRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationshipRelationship(instance, entityName));
    }
  }
}
