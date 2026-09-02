package com.minicad.step.semantic;

import java.util.Map;

/**
 * Representation registry part 1.
 */
public final class RepresentationRegistry1 {

  private RepresentationRegistry1() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: GEOMETRIC_REPRESENTATION_CONTEXT
      registry.put(
          "GEOMETRIC_REPRESENTATION_CONTEXT",
          (resolver, instance) -> resolver.representationResolver.resolveGeometricRepresentationContext(instance));

// Entity: SHAPE_REPRESENTATION
      registry.put(
          "SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, true));

// Entity: BEVELED_SHEET_REPRESENTATION
      registry.put(
          "BEVELED_SHEET_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "BEVELED_SHEET_REPRESENTATION", true));

// Entity: COMPOSITE_SHEET_REPRESENTATION
      registry.put(
          "COMPOSITE_SHEET_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "COMPOSITE_SHEET_REPRESENTATION", true));

// Entity: CYLINDRICAL_SHAPE_REPRESENTATION
      registry.put(
          "CYLINDRICAL_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CYLINDRICAL_SHAPE_REPRESENTATION", true));

// Entity: COMPOUND_SHAPE_REPRESENTATION
      registry.put(
          "COMPOUND_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "COMPOUND_SHAPE_REPRESENTATION", true));

// Entity: PLANAR_SHAPE_REPRESENTATION
      registry.put(
          "PLANAR_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PLANAR_SHAPE_REPRESENTATION", true));

// Entity: SHAPE_DIMENSION_REPRESENTATION
      registry.put("SHAPE_DIMENSION_REPRESENTATION", (resolver, instance) ->
          resolver.resolveRepresentation(instance, "SHAPE_DIMENSION_REPRESENTATION", true));

// Entity: SHAPE_REPRESENTATION_WITH_PARAMETERS
      registry.put(
          "SHAPE_REPRESENTATION_WITH_PARAMETERS",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "SHAPE_REPRESENTATION_WITH_PARAMETERS", true));

// Entity: LOCATION_SHAPE_REPRESENTATION
      registry.put(
          "LOCATION_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "LOCATION_SHAPE_REPRESENTATION", true));

// Entity: REPRESENTATIVE_SHAPE_REPRESENTATION
      registry.put(
          "REPRESENTATIVE_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "REPRESENTATIVE_SHAPE_REPRESENTATION", true));

// Entity: NEUTRAL_SKETCH_REPRESENTATION
      registry.put(
          "NEUTRAL_SKETCH_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "NEUTRAL_SKETCH_REPRESENTATION", true));

// Entity: PROCEDURAL_SHAPE_REPRESENTATION
      registry.put(
          "PROCEDURAL_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PROCEDURAL_SHAPE_REPRESENTATION", true));

// Entity: NGON_SHAPE_REPRESENTATION
      registry.put(
          "NGON_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "NGON_SHAPE_REPRESENTATION", true));

// Entity: SCAN_DATA_SHAPE_REPRESENTATION
      registry.put(
          "SCAN_DATA_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SCAN_DATA_SHAPE_REPRESENTATION", true));

// Entity: WIREFRAME_SHAPE_REPRESENTATION
      registry.put(
          "WIREFRAME_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "WIREFRAME_SHAPE_REPRESENTATION", true));

// Entity: MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION
      registry.put(
          "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION", false));

// Entity: MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION
      registry.put(
          "MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION", false));

// Entity: MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING
      registry.put(
          "MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING", false));

// Entity: VISUAL_APPEARANCE_REPRESENTATION
      registry.put(
          "VISUAL_APPEARANCE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "VISUAL_APPEARANCE_REPRESENTATION", false));

// Entity: SYMBOL_REPRESENTATION
      registry.put(
          "SYMBOL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SYMBOL_REPRESENTATION", false));

// Entity: PRESENTATION_REPRESENTATION
      registry.put(
          "PRESENTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PRESENTATION_REPRESENTATION", false));

// Entity: PICTURE_REPRESENTATION
      registry.put(
          "PICTURE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PICTURE_REPRESENTATION", false));

// Entity: TEXT_STRING_REPRESENTATION
      registry.put(
          "TEXT_STRING_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "TEXT_STRING_REPRESENTATION", false));

// Entity: STRUCTURED_TEXT_REPRESENTATION
      registry.put(
          "STRUCTURED_TEXT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "STRUCTURED_TEXT_REPRESENTATION", false));

// Entity: PROCEDURAL_REPRESENTATION
      registry.put(
          "PROCEDURAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PROCEDURAL_REPRESENTATION", false));

// Entity: CONSTRUCTIVE_GEOMETRY_REPRESENTATION
      registry.put(
          "CONSTRUCTIVE_GEOMETRY_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CONSTRUCTIVE_GEOMETRY_REPRESENTATION", false));

// Entity: AREA_DEPENDENT_ANNOTATION_REPRESENTATION
      registry.put(
          "AREA_DEPENDENT_ANNOTATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "AREA_DEPENDENT_ANNOTATION_REPRESENTATION", false));

// Entity: VARIATIONAL_REPRESENTATION
      registry.put(
          "VARIATIONAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "VARIATIONAL_REPRESENTATION", false));

// Entity: PLY_ANGLE_REPRESENTATION
      registry.put(
          "PLY_ANGLE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "PLY_ANGLE_REPRESENTATION", false));

// Entity: MOMENTS_OF_INERTIA_REPRESENTATION
      registry.put(
          "MOMENTS_OF_INERTIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MOMENTS_OF_INERTIA_REPRESENTATION", false));

// Entity: UNCERTAINTY_ASSIGNED_REPRESENTATION
      registry.put(
          "UNCERTAINTY_ASSIGNED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "UNCERTAINTY_ASSIGNED_REPRESENTATION", false));

// Entity: INTERPOLATED_CONFIGURATION_REPRESENTATION
      registry.put(
          "INTERPOLATED_CONFIGURATION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "INTERPOLATED_CONFIGURATION_REPRESENTATION", false));

// Entity: KINEMATIC_FRAME_BACKGROUND_REPRESENTATION
      registry.put(
          "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION", false));

// Entity: KINEMATIC_GROUND_REPRESENTATION
      registry.put(
          "KINEMATIC_GROUND_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_GROUND_REPRESENTATION", false));

// Entity: KINEMATIC_LINK_REPRESENTATION
      registry.put(
          "KINEMATIC_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_LINK_REPRESENTATION", false));

// Entity: RIGID_LINK_REPRESENTATION
      registry.put(
          "RIGID_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "RIGID_LINK_REPRESENTATION", false));

// Entity: MECHANISM_REPRESENTATION
      registry.put(
          "MECHANISM_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MECHANISM_REPRESENTATION", false));

// Entity: MECHANISM_STATE_REPRESENTATION
      registry.put(
          "MECHANISM_STATE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MECHANISM_STATE_REPRESENTATION", false));

// Entity: DATA_EQUIVALENCE_CRITERIA_REPRESENTATION
      registry.put(
          "DATA_EQUIVALENCE_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_EQUIVALENCE_CRITERIA_REPRESENTATION", false));

// Entity: DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION", false));

// Entity: DATA_QUALITY_CRITERIA_REPRESENTATION
      registry.put(
          "DATA_QUALITY_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_QUALITY_CRITERIA_REPRESENTATION", false));

// Entity: DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION
      registry.put(
          "DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION", false));

// Entity: EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION",
                  false));

// Entity: EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
                  false));

// Entity: A3M_EQUIVALENCE_CRITERIA_REPRESENTATION
      registry.put(
          "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION"));

// Entity: A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION"));

// Entity: A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE"));

// Entity: SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION
      registry.put(
          "SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION", false));

// Entity: SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION", false));

// Entity: EXTERNALLY_DEFINED_REPRESENTATION
      registry.put(
          "EXTERNALLY_DEFINED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "EXTERNALLY_DEFINED_REPRESENTATION", false));

// Entity: EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS
      registry.put(
          "EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS", false));

// Entity: SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY
      registry.put(
          "SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY", false));

// Entity: SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY
      registry.put(
          "SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY", false));

// Entity: MACHINING_CUTTING_CORNER_REPRESENTATION
      registry.put(
          "MACHINING_CUTTING_CORNER_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_CUTTING_CORNER_REPRESENTATION", false));

// Entity: MACHINING_DWELL_TIME_REPRESENTATION
      registry.put(
          "MACHINING_DWELL_TIME_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_DWELL_TIME_REPRESENTATION", false));

// Entity: MACHINING_FEED_SPEED_REPRESENTATION
      registry.put(
          "MACHINING_FEED_SPEED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_FEED_SPEED_REPRESENTATION", false));

// Entity: MACHINING_SPINDLE_SPEED_REPRESENTATION
      registry.put(
          "MACHINING_SPINDLE_SPEED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_SPINDLE_SPEED_REPRESENTATION", false));

// Entity: MACHINING_TOOL_BODY_REPRESENTATION
      registry.put(
          "MACHINING_TOOL_BODY_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOL_BODY_REPRESENTATION", false));

// Entity: MACHINING_TOOL_DIMENSION_REPRESENTATION
      registry.put(
          "MACHINING_TOOL_DIMENSION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "MACHINING_TOOL_DIMENSION_REPRESENTATION", false));

// Entity: FREEFORM_MILLING_TOLERANCE_REPRESENTATION
      registry.put(
          "FREEFORM_MILLING_TOLERANCE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "FREEFORM_MILLING_TOLERANCE_REPRESENTATION", false));

// Entity: HARDNESS_REPRESENTATION
      registry.put(
          "HARDNESS_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "HARDNESS_REPRESENTATION", false));

// Entity: OTHER_LIST_TABLE_REPRESENTATION
      registry.put(
          "OTHER_LIST_TABLE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "OTHER_LIST_TABLE_REPRESENTATION", false));

// Entity: CHARACTERIZED_REPRESENTATION
      registry.put(
          "CHARACTERIZED_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CHARACTERIZED_REPRESENTATION", false));

// Entity: CHARACTERIZED_ITEM_WITHIN_REPRESENTATION
      registry.put(
          "CHARACTERIZED_ITEM_WITHIN_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CHARACTERIZED_ITEM_WITHIN_REPRESENTATION", false));

// Entity: CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION
      registry.put(
          "CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION", false));

// Entity: DRAUGHTING_SUBFIGURE_REPRESENTATION
      registry.put(
          "DRAUGHTING_SUBFIGURE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAUGHTING_SUBFIGURE_REPRESENTATION", false));

// Entity: DRAUGHTING_SYMBOL_REPRESENTATION
      registry.put(
          "DRAUGHTING_SYMBOL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAUGHTING_SYMBOL_REPRESENTATION", false));

// Entity: REPRESENTATION
      registry.put("REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, "REPRESENTATION", false));

// Entity: TACTILE_APPEARANCE_REPRESENTATION
      registry.put(
          "TACTILE_APPEARANCE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "TACTILE_APPEARANCE_REPRESENTATION", false));

// Entity: APPLICATION_CONTEXT
      registry.put("APPLICATION_CONTEXT", StepEntityResolver::resolveApplicationContext);

// Entity: PRODUCT_CONTEXT
      registry.put("PRODUCT_CONTEXT", StepEntityResolver::resolveProductContext);

// Entity: MECHANICAL_CONTEXT
      registry.put(
          "MECHANICAL_CONTEXT",
          (resolver, instance) -> resolver.resolveProductContext(instance, "MECHANICAL_CONTEXT"));

// Entity: DESIGN_CONTEXT
      registry.put(
          "DESIGN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveProductDefinitionContext(instance, "DESIGN_CONTEXT"));

// Entity: SHAPE_DEFINITION_REPRESENTATION
      registry.put("SHAPE_DEFINITION_REPRESENTATION", StepEntityResolver::resolveShapeDefinitionRepresentation);

// Entity: ACTION_PROPERTY_REPRESENTATION
      registry.put(
          "ACTION_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.representationResolver.resolveActionPropertyRepresentation(instance));

// Entity: CONTACT_RATIO_REPRESENTATION
      registry.put(
          "CONTACT_RATIO_REPRESENTATION",
          (resolver, instance) -> resolver.representationResolver.resolveContactRatioRepresentation(instance));

// Entity: KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION
      registry.put(
          "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION",
          (resolver, instance) -> resolver.kinematicResolver.resolveKinematicPropertyDefinitionRepresentation(instance));

// Entity: KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION
      registry.put(
          "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION",
          (resolver, instance) -> resolver.kinematicResolver.resolveKinematicPropertyMechanismRepresentation(instance));

// Entity: KINEMATIC_PROPERTY_REPRESENTATION_RELATION
      registry.put(
          "KINEMATIC_PROPERTY_REPRESENTATION_RELATION",
          (resolver, instance) -> resolver.kinematicResolver.resolveKinematicPropertyRepresentationRelation(instance));

// Entity: KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION
      registry.put(
          "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION",
          (resolver, instance) -> resolver.kinematicResolver.resolveKinematicPropertyTopologyRepresentation(instance));

// Entity: RESOURCE_PROPERTY_REPRESENTATION
      registry.put(
          "RESOURCE_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.representationResolver.resolveResourcePropertyRepresentation(instance));

// Entity: PROPERTY_DEFINITION_REPRESENTATION
      registry.put(
          "PROPERTY_DEFINITION_REPRESENTATION",
          StepEntityResolver::resolvePropertyDefinitionRepresentation);

// Entity: REPRESENTATION_MAP
      registry.put(
          "REPRESENTATION_MAP",
          (resolver, instance) -> resolver.resolveRepresentationMap(instance));

// Entity: SYMBOL_REPRESENTATION_MAP
      registry.put("SYMBOL_REPRESENTATION_MAP", StepEntityResolver::resolveSymbolRepresentationMap);

// Entity: REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION
      registry.put(
          "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
          (resolver, instance) -> resolver.transformationResolver.resolveRepresentationRelationshipWithTransformation(instance));

// Entity: REPRESENTATION_RELATIONSHIP
      registry.put("REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveRepresentationRelationship);


// Entity: CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP
      registry.put(
          "CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP"));

// Entity: DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP
      registry.put(
          "DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP"));

// Entity: DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP
      registry.put(
          "DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP"));

// Entity: DEFINITIONAL_REPRESENTATION_RELATIONSHIP
      registry.put(
          "DEFINITIONAL_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DEFINITIONAL_REPRESENTATION_RELATIONSHIP"));

// Entity: DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT
      registry.put(
          "DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT"));

// Entity: EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP
      registry.put(
          "EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP"));

// Entity: EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP
      registry.put(
          "EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP"));

// Entity: FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP
      registry.put(
          "FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP"));

// Entity: PAIR_REPRESENTATION_RELATIONSHIP
      registry.put(
          "PAIR_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PAIR_REPRESENTATION_RELATIONSHIP"));

// Entity: REPRESENTATION_RELATIONSHIP_WITH_CLASS
      registry.put(
          "REPRESENTATION_RELATIONSHIP_WITH_CLASS",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "REPRESENTATION_RELATIONSHIP_WITH_CLASS"));

// Entity: SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION
      registry.put(
          "SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION"));

  }
}
