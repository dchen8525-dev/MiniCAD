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
      registry.put("CONVERSION_BASED_UNIT", StepEntityResolver::resolveConversionBasedUnit);
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
      registry.put("COMPOUND_REPRESENTATION_ITEM", StepEntityResolver::resolveCompoundRepresentationItem);
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
      registry.put("FEA_MATERIAL_PROPERTY_REPRESENTATION", StepEntityResolver::resolveFeaMaterialPropertyRepresentation);
      registry.put("VOLUME_3D_ELEMENT_REPRESENTATION", StepEntityResolver::resolveVolume3dElementRepresentation);
  
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
