package com.minicad.step.semantic;

import com.minicad.step.model.StepAxis2Placement3D;

import java.util.Map;

/**
 * Registry for topology entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class TopologyRegistry {

  private TopologyRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: FACETED_BREP_SHAPE_REPRESENTATION
      registry.put(
          "FACETED_BREP_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "FACETED_BREP_SHAPE_REPRESENTATION", true));

// Entity: SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION
      registry.put(
          "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION", true));

// Entity: RIGHT_ANGULAR_WEDGE
      registry.put(
          "RIGHT_ANGULAR_WEDGE",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance,
                  "RIGHT_ANGULAR_WEDGE",
                  StepAxis2Placement3D.class,
                  "AXIS2_PLACEMENT_3D",
                  4));

// Entity: EXTRUDED_FACE_SOLID
      registry.put("EXTRUDED_FACE_SOLID", (resolver, instance) -> resolver.solidResolver.resolveExtrudedFaceSolid(instance));

// Entity: REVOLVED_FACE_SOLID
      registry.put("REVOLVED_FACE_SOLID", (resolver, instance) -> resolver.solidResolver.resolveRevolvedFaceSolid(instance));

// Entity: SWEPT_FACE_SOLID
      registry.put("SWEPT_FACE_SOLID", (resolver, instance) ->
          resolver.resolveSweptFaceSolid(instance, "SWEPT_FACE_SOLID"));

// Entity: POLYGONAL_BOUNDED_HALF_SPACE
      registry.put("POLYGONAL_BOUNDED_HALF_SPACE", (resolver, instance) -> resolver.tessellationResolver.resolvePolygonalBoundedHalfSpace(instance));

// Entity: FACETED_BREP
      registry.put(
          "FACETED_BREP",
          (resolver, instance) -> resolver.resolveManifoldSolidBrep(instance, "FACETED_BREP"));

// Entity: EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION
      registry.put(
          "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));

// Entity: GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION
      registry.put(
          "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION", true));

// Entity: GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION
      registry.put(
          "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION", true));

// Entity: SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION
      registry.put(
          "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));

// Entity: TESSELLATED_SHAPE_REPRESENTATION
      registry.put(
          "TESSELLATED_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "TESSELLATED_SHAPE_REPRESENTATION", true));

// Entity: TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS
      registry.put(
          "TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS", true));

// Entity: PATH_SHAPE_REPRESENTATION
      registry.put(
          "PATH_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PATH_SHAPE_REPRESENTATION", true));

// Entity: FACE_SHAPE_REPRESENTATION
      registry.put(
          "FACE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "FACE_SHAPE_REPRESENTATION", true));

// Entity: LINK_MOTION_REPRESENTATION_ALONG_PATH
      registry.put(
          "LINK_MOTION_REPRESENTATION_ALONG_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "LINK_MOTION_REPRESENTATION_ALONG_PATH", false));

// Entity: CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION
      registry.put(
          "CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION", false));

// Entity: EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT
      registry.put(
          "EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT",
                  false));

// Entity: FOUNDED_KINEMATIC_PATH
      registry.put(
          "FOUNDED_KINEMATIC_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "FOUNDED_KINEMATIC_PATH", false));

// Entity: MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION
      registry.put(
          "MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION", false));

// Entity: PATH_PARAMETER_REPRESENTATION
      registry.put(
          "PATH_PARAMETER_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PATH_PARAMETER_REPRESENTATION", false));

// Entity: PRESCRIBED_PATH
      registry.put(
          "PRESCRIBED_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESCRIBED_PATH", false));

// Entity: RESULTING_PATH
      registry.put(
          "RESULTING_PATH",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "RESULTING_PATH", false));

// Entity: EDGE_ROUND
      registry.put(
          "EDGE_ROUND",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "EDGE_ROUND"));

// Entity: FACE_SHAPE_REPRESENTATION_RELATIONSHIP
      registry.put(
          "FACE_SHAPE_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "FACE_SHAPE_REPRESENTATION_RELATIONSHIP"));

// Entity: PATH_PARAMETER_REPRESENTATION_CONTEXT
      registry.put(
          "PATH_PARAMETER_REPRESENTATION_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PATH_PARAMETER_REPRESENTATION_CONTEXT"));

// Entity: REPOSITIONED_TESSELLATED_ITEM
      registry.put(
          "REPOSITIONED_TESSELLATED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPOSITIONED_TESSELLATED_ITEM"));

// Entity: STYLED_TESSELLATED_ITEM_WITH_COLOURS
      registry.put(
          "STYLED_TESSELLATED_ITEM_WITH_COLOURS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "STYLED_TESSELLATED_ITEM_WITH_COLOURS"));

// Entity: TESSELLATED_ITEM
      registry.put(
          "TESSELLATED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TESSELLATED_ITEM"));

// Entity: TESSELLATED_STRUCTURED_ITEM
      registry.put(
          "TESSELLATED_STRUCTURED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TESSELLATED_STRUCTURED_ITEM"));

// Entity: DOUBLE_OFFSET_SHELLED_SOLID
      registry.put(
          "DOUBLE_OFFSET_SHELLED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DOUBLE_OFFSET_SHELLED_SOLID"));

// Entity: CLOSED_PATH_PROFILE
      registry.put(
          "CLOSED_PATH_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CLOSED_PATH_PROFILE"));

// Entity: DIFFERENT_BOUNDING_BOX
      registry.put(
          "DIFFERENT_BOUNDING_BOX",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_BOUNDING_BOX"));

// Entity: OPEN_PATH_PROFILE
      registry.put(
          "OPEN_PATH_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "OPEN_PATH_PROFILE"));

// Entity: PATH_AREA_WITH_PARAMETERS
      registry.put(
          "PATH_AREA_WITH_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PATH_AREA_WITH_PARAMETERS"));

// Entity: AREA_WITH_OUTER_BOUNDARY
      registry.put(
          "AREA_WITH_OUTER_BOUNDARY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AREA_WITH_OUTER_BOUNDARY"));

// Entity: SMALL_AREA_FACE
      registry.put(
          "SMALL_AREA_FACE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SMALL_AREA_FACE"));

// Entity: COMPLEX_SHELLED_SOLID
      registry.put(
          "COMPLEX_SHELLED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPLEX_SHELLED_SOLID"));

// Entity: DIFFERENT_NUMBER_OF_CLOSED_SHELL
      registry.put(
          "DIFFERENT_NUMBER_OF_CLOSED_SHELL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_CLOSED_SHELL"));

// Entity: DISCONNECTED_FACE_SET
      registry.put(
          "DISCONNECTED_FACE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DISCONNECTED_FACE_SET"));

// Entity: EDGE_BLENDED_SOLID
      registry.put(
          "EDGE_BLENDED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EDGE_BLENDED_SOLID"));

// Entity: EDGE_WITH_EXCESSIVE_SEGMENTS
      registry.put(
          "EDGE_WITH_EXCESSIVE_SEGMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EDGE_WITH_EXCESSIVE_SEGMENTS"));

// Entity: EDGE_WITH_LENGTH
      registry.put(
          "EDGE_WITH_LENGTH",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EDGE_WITH_LENGTH"));

// Entity: ENTIRELY_NARROW_FACE
      registry.put(
          "ENTIRELY_NARROW_FACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ENTIRELY_NARROW_FACE"));

// Entity: EXTRUDED_FACE_SOLID_WITH_DRAFT_ANGLE
      registry.put(
          "EXTRUDED_FACE_SOLID_WITH_DRAFT_ANGLE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTRUDED_FACE_SOLID_WITH_DRAFT_ANGLE"));

// Entity: EXTRUDED_FACE_SOLID_WITH_MULTIPLE_DRAFT_ANGLES
      registry.put(
          "EXTRUDED_FACE_SOLID_WITH_MULTIPLE_DRAFT_ANGLES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTRUDED_FACE_SOLID_WITH_MULTIPLE_DRAFT_ANGLES"));

// Entity: EXTRUDED_FACE_SOLID_WITH_TRIM_CONDITIONS
      registry.put(
          "EXTRUDED_FACE_SOLID_WITH_TRIM_CONDITIONS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTRUDED_FACE_SOLID_WITH_TRIM_CONDITIONS"));

// Entity: FACETED_PRIMITIVE
      registry.put(
          "FACETED_PRIMITIVE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FACETED_PRIMITIVE"));

// Entity: FREE_EDGE
      registry.put(
          "FREE_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FREE_EDGE"));

// Entity: G1_DISCONTINUITY_BETWEEN_ADJACENT_FACES
      registry.put(
          "G1_DISCONTINUITY_BETWEEN_ADJACENT_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G1_DISCONTINUITY_BETWEEN_ADJACENT_FACES"));

// Entity: G2_DISCONTINUITY_BETWEEN_ADJACENT_FACES
      registry.put(
          "G2_DISCONTINUITY_BETWEEN_ADJACENT_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "G2_DISCONTINUITY_BETWEEN_ADJACENT_FACES"));

// Entity: GAP_BETWEEN_ADJACENT_EDGES_IN_LOOP
      registry.put(
          "GAP_BETWEEN_ADJACENT_EDGES_IN_LOOP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_ADJACENT_EDGES_IN_LOOP"));

// Entity: GAP_BETWEEN_FACES_RELATED_TO_AN_EDGE
      registry.put(
          "GAP_BETWEEN_FACES_RELATED_TO_AN_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_FACES_RELATED_TO_AN_EDGE"));

// Entity: GAP_BETWEEN_VERTEX_AND_EDGE
      registry.put(
          "GAP_BETWEEN_VERTEX_AND_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GAP_BETWEEN_VERTEX_AND_EDGE"));

// Entity: HIERARCHICAL_INTERFACE_CONNECTION
      registry.put(
          "HIERARCHICAL_INTERFACE_CONNECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HIERARCHICAL_INTERFACE_CONNECTION"));

// Entity: INCONSISTENT_ADJACENT_FACE_NORMALS
      registry.put(
          "INCONSISTENT_ADJACENT_FACE_NORMALS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_ADJACENT_FACE_NORMALS"));

// Entity: INCONSISTENT_FACE_AND_CLOSED_SHELL_NORMALS
      registry.put(
          "INCONSISTENT_FACE_AND_CLOSED_SHELL_NORMALS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_FACE_AND_CLOSED_SHELL_NORMALS"));

// Entity: INTERFACED_GROUP_COMPONENT
      registry.put(
          "INTERFACED_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACED_GROUP_COMPONENT"));

// Entity: INTERFACE_COMPONENT
      registry.put(
          "INTERFACE_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_COMPONENT"));

// Entity: INTERFACE_CONNECTION
      registry.put(
          "INTERFACE_CONNECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTION"));

// Entity: INTERFACE_CONNECTOR_AS_PLANNED
      registry.put(
          "INTERFACE_CONNECTOR_AS_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_AS_PLANNED"));

// Entity: INTERFACE_CONNECTOR_AS_REALIZED
      registry.put(
          "INTERFACE_CONNECTOR_AS_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_AS_REALIZED"));

// Entity: INTERFACE_CONNECTOR_DEFINITION
      registry.put(
          "INTERFACE_CONNECTOR_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DEFINITION"));

// Entity: INTERFACE_CONNECTOR_DESIGN
      registry.put(
          "INTERFACE_CONNECTOR_DESIGN",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DESIGN"));

// Entity: INTERFACE_CONNECTOR_DESIGN_TO_PLANNED
      registry.put(
          "INTERFACE_CONNECTOR_DESIGN_TO_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DESIGN_TO_PLANNED"));

// Entity: INTERFACE_CONNECTOR_DESIGN_TO_REALIZED
      registry.put(
          "INTERFACE_CONNECTOR_DESIGN_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_DESIGN_TO_REALIZED"));

// Entity: INTERFACE_CONNECTOR_OCCURRENCE
      registry.put(
          "INTERFACE_CONNECTOR_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_OCCURRENCE"));

// Entity: INTERFACE_CONNECTOR_PLANNED_TO_REALIZED
      registry.put(
          "INTERFACE_CONNECTOR_PLANNED_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_PLANNED_TO_REALIZED"));

// Entity: INTERFACE_CONNECTOR_VERSION
      registry.put(
          "INTERFACE_CONNECTOR_VERSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_CONNECTOR_VERSION"));

// Entity: INTERFACE_DEFINITION_CONNECTION
      registry.put(
          "INTERFACE_DEFINITION_CONNECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_DEFINITION_CONNECTION"));

// Entity: INTERFACE_DEFINITION_FOR
      registry.put(
          "INTERFACE_DEFINITION_FOR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_DEFINITION_FOR"));

// Entity: INTERFACE_SPECIFICATION_DEFINITION
      registry.put(
          "INTERFACE_SPECIFICATION_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_SPECIFICATION_DEFINITION"));

// Entity: INTERFACE_SPECIFICATION_VERSION
      registry.put(
          "INTERFACE_SPECIFICATION_VERSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERFACE_SPECIFICATION_VERSION"));

// Entity: INTERSECTING_CONNECTED_FACE_SETS
      registry.put(
          "INTERSECTING_CONNECTED_FACE_SETS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERSECTING_CONNECTED_FACE_SETS"));

// Entity: INTERSECTING_LOOPS_IN_FACE
      registry.put(
          "INTERSECTING_LOOPS_IN_FACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERSECTING_LOOPS_IN_FACE"));

// Entity: INTERSECTING_SHELLS_IN_SOLID
      registry.put(
          "INTERSECTING_SHELLS_IN_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INTERSECTING_SHELLS_IN_SOLID"));

// Entity: KINEMATIC_LOOP
      registry.put(
          "KINEMATIC_LOOP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "KINEMATIC_LOOP"));

// Entity: LOOP
      registry.put(
          "LOOP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOOP"));

// Entity: MISMATCH_OF_EDGES
      registry.put(
          "MISMATCH_OF_EDGES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_EDGES"));

// Entity: MISMATCH_OF_FACES
      registry.put(
          "MISMATCH_OF_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_FACES"));

// Entity: MISMATCH_OF_UNDERLYING_EDGE_GEOMETRY
      registry.put(
          "MISMATCH_OF_UNDERLYING_EDGE_GEOMETRY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_UNDERLYING_EDGE_GEOMETRY"));

// Entity: MISMATCH_OF_UNDERLYING_FACE_GEOMETRY
      registry.put(
          "MISMATCH_OF_UNDERLYING_FACE_GEOMETRY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISMATCH_OF_UNDERLYING_FACE_GEOMETRY"));

// Entity: MISSING_EDGE
      registry.put(
          "MISSING_EDGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISSING_EDGE"));

// Entity: MISSING_FACE
      registry.put(
          "MISSING_FACE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MISSING_FACE"));

// Entity: MULTIPLY_DEFINED_EDGES
      registry.put(
          "MULTIPLY_DEFINED_EDGES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_EDGES"));

// Entity: MULTIPLY_DEFINED_FACES
      registry.put(
          "MULTIPLY_DEFINED_FACES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_FACES"));

// Entity: SIMPLIFIED_SPOTFACE_HOLE_DEFINITION
      registry.put(
          "SIMPLIFIED_SPOTFACE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SIMPLIFIED_SPOTFACE_HOLE_DEFINITION"));

// Entity: BOUND_PARAMETER_ENVIRONMENT
      registry.put(
          "BOUND_PARAMETER_ENVIRONMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOUND_PARAMETER_ENVIRONMENT"));

// Entity: BOUND_VARIABLE_SEMANTICS
      registry.put(
          "BOUND_VARIABLE_SEMANTICS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOUND_VARIABLE_SEMANTICS"));

// Entity: BOUND_VARIATIONAL_PARAMETER
      registry.put(
          "BOUND_VARIATIONAL_PARAMETER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOUND_VARIATIONAL_PARAMETER"));

// Entity: CIRCULAR_PATH
      registry.put(
          "CIRCULAR_PATH",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_PATH"));

// Entity: COMPONENT_PATH_SHAPE_ASPECT
      registry.put(
          "COMPONENT_PATH_SHAPE_ASPECT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPONENT_PATH_SHAPE_ASPECT"));

// Entity: VERTEX
      registry.put("VERTEX", StepEntityResolver::resolveVertex);

// Entity: EDGE_BASED_WIREFRAME_MODEL
      registry.put("EDGE_BASED_WIREFRAME_MODEL", (resolver, instance) -> resolver.topologyResolver.resolveEdgeBasedWireframeModel(instance));

// Entity: CONNECTED_EDGE_SET
      registry.put("CONNECTED_EDGE_SET", (resolver, instance) -> resolver.topologyResolver.resolveConnectedEdgeSet(instance));

// Entity: SUBEDGE
      registry.put("SUBEDGE", (resolver, instance) -> resolver.topologyResolver.resolveSubedge(instance));

// Entity: EDGE
      registry.put("EDGE", StepEntityResolver::resolveEdge);

// Entity: FACE
      registry.put("FACE", StepEntityResolver::resolveFace);

// Entity: FACETTED_BREP
      registry.put("FACETTED_BREP", (resolver, instance) -> resolver.topologyResolver.resolveFacettedBrep(instance));

// Entity: KINEMATIC_PATH
      registry.put(
          "KINEMATIC_PATH",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: MOTION_PATH
      registry.put("MOTION_PATH", (resolver, instance) -> resolver.kinematicResolver.resolveMotionPath(instance));

// Entity: POLYGONAL_FACE_SET
      registry.put(
          "POLYGONAL_FACE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: CHAMFER_EDGE
      registry.put("CHAMFER_EDGE", (resolver, instance) -> resolver.geometricFeatureResolver.resolveChamferEdge(instance));

// Entity: FILLET_EDGE
      registry.put("FILLET_EDGE", (resolver, instance) -> resolver.geometricFeatureResolver.resolveFilletEdge(instance));

// Entity: ORIENTED_EDGE
      registry.put("ORIENTED_EDGE", (resolver, instance) -> resolver.topologyResolver.resolveOrientedEdge(instance));

// Entity: VERTEX_LOOP
      registry.put("VERTEX_LOOP", (resolver, instance) -> resolver.topologyResolver.resolveVertexLoop(instance));

// Entity: POLY_LOOP
      registry.put("POLY_LOOP", (resolver, instance) -> resolver.topologyResolver.resolvePolyLoop(instance));

// Entity: OPEN_PATH
      registry.put("OPEN_PATH", (resolver, instance) -> resolver.topologyResolver.resolveOpenPath(instance));

// Entity: SUBPATH
      registry.put("SUBPATH", (resolver, instance) -> resolver.topologyResolver.resolveSubpath(instance));

// Entity: ORIENTED_PATH
      registry.put("ORIENTED_PATH", (resolver, instance) -> resolver.topologyResolver.resolveOrientedPath(instance));

// Entity: PATH
      registry.put("PATH", (resolver, instance) -> resolver.topologyResolver.resolvePath(instance));

// Entity: EDGE_LOOP
      registry.put("EDGE_LOOP", (resolver, instance) -> resolver.topologyResolver.resolveEdgeLoop(instance));

// Entity: EDGE_WIRE
      registry.put("EDGE_WIRE", StepEntityResolver::resolveEdgeWire);

// Entity: FACE_OUTER_BOUND
      registry.put(
          "FACE_OUTER_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, true));

// Entity: FACE_BOUND
      registry.put("FACE_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, false));

// Entity: ADVANCED_FACE
      registry.put("ADVANCED_FACE", (resolver, instance) -> resolver.topologyResolver.resolveAdvancedFace(instance));

// Entity: ORIENTED_FACE
      registry.put("ORIENTED_FACE", (resolver, instance) -> resolver.topologyResolver.resolveOrientedFace(instance));

// Entity: VERTEX_SHELL
      registry.put("VERTEX_SHELL", (resolver, instance) -> resolver.topologyResolver.resolveVertexShell(instance));

// Entity: WIRE_SHELL
      registry.put("WIRE_SHELL", (resolver, instance) -> resolver.topologyResolver.resolveWireShell(instance));

// Entity: CONNECTED_FACE_SUB_SET
      registry.put("CONNECTED_FACE_SUB_SET", (resolver, instance) -> resolver.topologyResolver.resolveConnectedFaceSubSet(instance));

// Entity: CONNECTED_FACE_SET
      registry.put("CONNECTED_FACE_SET", (resolver, instance) -> resolver.topologyResolver.resolveConnectedFaceSet(instance));

// Entity: TESSELLATED_FACE_SET
      registry.put("TESSELLATED_FACE_SET", StepEntityResolver::resolveTessellatedFaceSet);

// Entity: SEAM_EDGE
      registry.put("SEAM_EDGE", StepEntityResolver::resolveSeamEdge);

// Entity: TESSELLATED_FACE
      registry.put("TESSELLATED_FACE", (resolver, instance) -> resolver.tessellationResolver.resolveTessellatedFace(instance));

// Entity: TESSELLATED_TRIANGLE
      registry.put("TESSELLATED_TRIANGLE", (resolver, instance) -> resolver.tessellationResolver.resolveTessellatedTriangle(instance));

// Entity: TRIANGULATED_FACE
      registry.put(
          "TRIANGULATED_FACE",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: COMPLEX_TRIANGULATED_FACE
      registry.put("COMPLEX_TRIANGULATED_FACE", (resolver, instance) -> resolver.tessellationResolver.resolveComplexTriangulatedFace(instance));

// Entity: VELOCITY_BOUNDARY_CONDITION
      registry.put(
          "VELOCITY_BOUNDARY_CONDITION",
          (resolver, instance) -> resolver.boundaryConditionResolver.resolveVelocityBoundaryCondition(instance));

// Entity: ACCELERATION_BOUNDARY_CONDITION
      registry.put(
          "ACCELERATION_BOUNDARY_CONDITION",
          (resolver, instance) -> resolver.boundaryConditionResolver.resolveAccelerationBoundaryCondition(instance));

// Entity: FORCE_BOUNDARY_CONDITION
      registry.put("FORCE_BOUNDARY_CONDITION", (resolver, instance) -> resolver.boundaryConditionResolver.resolveForceBoundaryCondition(instance));

// Entity: PRESSURE_BOUNDARY_CONDITION
      registry.put(
          "PRESSURE_BOUNDARY_CONDITION",
          (resolver, instance) -> resolver.boundaryConditionResolver.resolvePressureBoundaryCondition(instance));

// Entity: THERMAL_BOUNDARY_CONDITION
      registry.put(
          "THERMAL_BOUNDARY_CONDITION",
          (resolver, instance) -> resolver.boundaryConditionResolver.resolveThermalBoundaryCondition(instance));

// Entity: TRIANGULATED_FACE_SET
      registry.put(
          "TRIANGULATED_FACE_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance)); // Same as TESSELLATED_FACE_SET

// Entity: SUBFACE
      registry.put("SUBFACE", StepEntityResolver::resolveSubface);

// Entity: ORIENTED_SUBFACE
      registry.put("ORIENTED_SUBFACE", StepEntityResolver::resolveOrientedSubface);

// Entity: ORIENTED_OPEN_SHELL
      registry.put("ORIENTED_OPEN_SHELL", (resolver, instance) -> resolver.topologyResolver.resolveOrientedOpenShell(instance));

// Entity: ORIENTED_CLOSED_SHELL
      registry.put("ORIENTED_CLOSED_SHELL", (resolver, instance) -> resolver.topologyResolver.resolveOrientedClosedShell(instance));

// Entity: SHELL_BASED_WIREFRAME_MODEL
      registry.put("SHELL_BASED_WIREFRAME_MODEL", StepEntityResolver::resolveShellBasedWireframeModel);

// Entity: OPEN_SHELL
      registry.put("OPEN_SHELL", (resolver, instance) -> resolver.topologyResolver.resolveOpenShell(instance));

// Entity: CLOSED_SHELL
      registry.put("CLOSED_SHELL", (resolver, instance) -> resolver.topologyResolver.resolveClosedShell(instance));

// Entity: POLYGONAL_FACE
      registry.put(
          "POLYGONAL_FACE",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: TESSELLATED_SHELL
      registry.put(
          "TESSELLATED_SHELL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_SOLID
      registry.put(
          "TESSELLATED_SOLID",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: SHELL
      registry.put(
          "SHELL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ADVANCED_FACE_REPRESENTATION
      registry.put(
          "ADVANCED_FACE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ADVANCED_FACE_REPRESENTATION", true));

// Entity: FACE_REPRESENTATION
      registry.put(
          "FACE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FACE_REPRESENTATION", true));

// Entity: EDGE_REPRESENTATION
      registry.put(
          "EDGE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "EDGE_REPRESENTATION", true));

// Entity: VERTEX_REPRESENTATION
      registry.put(
          "VERTEX_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "VERTEX_REPRESENTATION", true));

// Entity: LOOP_REPRESENTATION
      registry.put(
          "LOOP_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "LOOP_REPRESENTATION", true));

// Entity: SHELL_REPRESENTATION
      registry.put(
          "SHELL_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "SHELL_REPRESENTATION", true));

// Entity: TESSELLATED_GEOMETRIC_SET
      registry.put(
          "TESSELLATED_GEOMETRIC_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: TESSELLATED_STRUCTURED_MESH
      registry.put(
          "TESSELLATED_STRUCTURED_MESH",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_MESH
      registry.put(
          "TESSELLATED_MESH",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_MESH_ELEMENTS
      registry.put(
          "TESSELLATED_MESH_ELEMENTS",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_MESH_ELEMENT_SET
      registry.put(
          "TESSELLATED_MESH_ELEMENT_SET",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_MESH_STRUCTURE
      registry.put(
          "TESSELLATED_MESH_STRUCTURE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_CELL
      registry.put(
          "TESSELLATED_CELL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_CELL_SET
      registry.put(
          "TESSELLATED_CELL_SET",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_EDGE
      registry.put(
          "TESSELLATED_EDGE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_EDGE_SET
      registry.put(
          "TESSELLATED_EDGE_SET",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_VERTEX
      registry.put(
          "TESSELLATED_VERTEX",
          (resolver, instance) -> resolver.resolveCartesianPoint(instance));

// Entity: TESSELLATED_VERTEX_SET
      registry.put(
          "TESSELLATED_VERTEX_SET",
          (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));

// Entity: TESSELLATED_WIREFRAME
      registry.put(
          "TESSELLATED_WIREFRAME",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_ANNOTATION
      registry.put(
          "TESSELLATED_ANNOTATION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_TEXT
      registry.put(
          "TESSELLATED_TEXT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_DIMENSION
      registry.put(
          "TESSELLATED_DIMENSION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TESSELLATED_SYMBOL
      registry.put(
          "TESSELLATED_SYMBOL",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: SHELL_SET
      registry.put(
          "SHELL_SET",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: FEA_SHELL_ELEMENT_PROPERTY
      registry.put("FEA_SHELL_ELEMENT_PROPERTY", (resolver, instance) -> resolver.analysisResolver.resolveFeaShellElementProperty(instance));

// Entity: BOUNDARY_CONDITION
      registry.put("BOUNDARY_CONDITION", StepEntityResolver::resolveBoundaryCondition);


  }
}
