package com.minicad.step.semantic;

import java.util.Map;

/**
 * Representation registry part 2.
 */
public final class RepresentationRegistry2 {

  private RepresentationRegistry2() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP
      registry.put(
          "VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP"));

// Entity: SHAPE_REPRESENTATION_RELATIONSHIP
      registry.put(
          "SHAPE_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) -> resolver.representationResolver.resolveShapeRepresentationRelationship(instance));

// Entity: CONTEXT_DEPENDENT_SHAPE_REPRESENTATION
      registry.put("CONTEXT_DEPENDENT_SHAPE_REPRESENTATION", StepEntityResolver::resolveContextDependentShapeRepresentation);

// Entity: GLOBAL_UNIT_ASSIGNED_CONTEXT
      registry.put("GLOBAL_UNIT_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUnitAssignedContext);

// Entity: GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT
      registry.put("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUncertaintyAssignedContext);

// Entity: CONTEXT_DEPENDENT_UNIT
      registry.put("CONTEXT_DEPENDENT_UNIT", (resolver, instance) -> resolver.unitResolver.resolveContextDependentUnit(instance));

// Entity: A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR
      registry.put(
          "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_LENGTH_MEASURE_AND_CONTEXT_DEPENDENT_MEASURE_PAIR"));

// Entity: ANALYSIS_REPRESENTATION_CONTEXT
      registry.put(
          "ANALYSIS_REPRESENTATION_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ANALYSIS_REPRESENTATION_CONTEXT"));

// Entity: APPLIED_LOCATION_REPRESENTATION_ASSIGNMENT
      registry.put(
          "APPLIED_LOCATION_REPRESENTATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_LOCATION_REPRESENTATION_ASSIGNMENT"));

// Entity: CLASS_USAGE_EFFECTIVITY_CONTEXT_ASSIGNMENT
      registry.put(
          "CLASS_USAGE_EFFECTIVITY_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CLASS_USAGE_EFFECTIVITY_CONTEXT_ASSIGNMENT"));

// Entity: CONFIGURED_EFFECTIVITY_CONTEXT_ASSIGNMENT
      registry.put(
          "CONFIGURED_EFFECTIVITY_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONFIGURED_EFFECTIVITY_CONTEXT_ASSIGNMENT"));

// Entity: DOCUMENT_REPRESENTATION_TYPE
      registry.put(
          "DOCUMENT_REPRESENTATION_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "DOCUMENT_REPRESENTATION_TYPE"));

// Entity: EFFECTIVITY_CONTEXT_ASSIGNMENT
      registry.put(
          "EFFECTIVITY_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EFFECTIVITY_CONTEXT_ASSIGNMENT"));

// Entity: EFFECTIVITY_CONTEXT_ROLE
      registry.put(
          "EFFECTIVITY_CONTEXT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "EFFECTIVITY_CONTEXT_ROLE"));

// Entity: EXPLICIT_PROCEDURAL_GEOMETRIC_REPRESENTATION_ITEM_RELATIONSHIP
      registry.put(
          "EXPLICIT_PROCEDURAL_GEOMETRIC_REPRESENTATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXPLICIT_PROCEDURAL_GEOMETRIC_REPRESENTATION_ITEM_RELATIONSHIP"));

// Entity: EXPLICIT_PROCEDURAL_REPRESENTATION_ITEM_RELATIONSHIP
      registry.put(
          "EXPLICIT_PROCEDURAL_REPRESENTATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXPLICIT_PROCEDURAL_REPRESENTATION_ITEM_RELATIONSHIP"));

// Entity: GEOMETRIC_REPRESENTATION_CONTEXT_WITH_PARAMETER
      registry.put(
          "GEOMETRIC_REPRESENTATION_CONTEXT_WITH_PARAMETER",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "GEOMETRIC_REPRESENTATION_CONTEXT_WITH_PARAMETER"));

// Entity: INSTANCE_USAGE_CONTEXT_ASSIGNMENT
      registry.put(
          "INSTANCE_USAGE_CONTEXT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "INSTANCE_USAGE_CONTEXT_ASSIGNMENT"));

// Entity: LOCATION_REPRESENTATION_ASSIGNMENT
      registry.put(
          "LOCATION_REPRESENTATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOCATION_REPRESENTATION_ASSIGNMENT"));

// Entity: LOCATION_REPRESENTATION_ROLE
      registry.put(
          "LOCATION_REPRESENTATION_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "LOCATION_REPRESENTATION_ROLE"));

// Entity: PARAMETRIC_REPRESENTATION_CONTEXT
      registry.put(
          "PARAMETRIC_REPRESENTATION_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PARAMETRIC_REPRESENTATION_CONTEXT"));

// Entity: PARTIAL_DOCUMENT_WITH_STRUCTURED_TEXT_REPRESENTATION_ASSIGNMENT
      registry.put(
          "PARTIAL_DOCUMENT_WITH_STRUCTURED_TEXT_REPRESENTATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PARTIAL_DOCUMENT_WITH_STRUCTURED_TEXT_REPRESENTATION_ASSIGNMENT"));

// Entity: REPRESENTATION_ITEM_RELATIONSHIP
      registry.put(
          "REPRESENTATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "REPRESENTATION_ITEM_RELATIONSHIP"));

// Entity: SHAPE_ASPECT_RELATIONSHIP_REPRESENTATION_ASSOCIATION
      registry.put(
          "SHAPE_ASPECT_RELATIONSHIP_REPRESENTATION_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SHAPE_ASPECT_RELATIONSHIP_REPRESENTATION_ASSOCIATION"));

// Entity: APPLICATION_CONTEXT_ELEMENT
      registry.put(
          "APPLICATION_CONTEXT_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLICATION_CONTEXT_ELEMENT"));

// Entity: AUXILIARY_GEOMETRIC_REPRESENTATION_ITEM
      registry.put(
          "AUXILIARY_GEOMETRIC_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AUXILIARY_GEOMETRIC_REPRESENTATION_ITEM"));

// Entity: BINARY_REPRESENTATION_ITEM
      registry.put(
          "BINARY_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BINARY_REPRESENTATION_ITEM"));

// Entity: BYTES_REPRESENTATION_ITEM
      registry.put(
          "BYTES_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BYTES_REPRESENTATION_ITEM"));

// Entity: FUNCTIONAL_BREAKDOWN_CONTEXT
      registry.put(
          "FUNCTIONAL_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "FUNCTIONAL_BREAKDOWN_CONTEXT"));

// Entity: PHYSICAL_BREAKDOWN_CONTEXT
      registry.put(
          "PHYSICAL_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PHYSICAL_BREAKDOWN_CONTEXT"));

// Entity: SYSTEM_BREAKDOWN_CONTEXT
      registry.put(
          "SYSTEM_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SYSTEM_BREAKDOWN_CONTEXT"));

// Entity: ZONE_BREAKDOWN_CONTEXT
      registry.put(
          "ZONE_BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ZONE_BREAKDOWN_CONTEXT"));

// Entity: CONSTRAINED_KINEMATIC_MOTION_REPRESENTATION
      registry.put(
          "CONSTRAINED_KINEMATIC_MOTION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONSTRAINED_KINEMATIC_MOTION_REPRESENTATION"));

// Entity: CONTEXT_DEPENDENT_INVISIBILITY
      registry.put(
          "CONTEXT_DEPENDENT_INVISIBILITY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTEXT_DEPENDENT_INVISIBILITY"));

// Entity: CONTEXT_DEPENDENT_KINEMATIC_LINK_REPRESENTATION
      registry.put(
          "CONTEXT_DEPENDENT_KINEMATIC_LINK_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTEXT_DEPENDENT_KINEMATIC_LINK_REPRESENTATION"));

// Entity: CONTEXT_DEPENDENT_OVER_RIDING_STYLED_ITEM
      registry.put(
          "CONTEXT_DEPENDENT_OVER_RIDING_STYLED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTEXT_DEPENDENT_OVER_RIDING_STYLED_ITEM"));

// Entity: DATE_REPRESENTATION_ITEM
      registry.put(
          "DATE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATE_REPRESENTATION_ITEM"));

// Entity: DATE_TIME_REPRESENTATION_ITEM
      registry.put(
          "DATE_TIME_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATE_TIME_REPRESENTATION_ITEM"));

// Entity: EXTERNALLY_DEFINED_CONTEXT_DEPENDENT_UNIT
      registry.put(
          "EXTERNALLY_DEFINED_CONTEXT_DEPENDENT_UNIT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_CONTEXT_DEPENDENT_UNIT"));

// Entity: EXTERNALLY_DEFINED_REPRESENTATION_ITEM
      registry.put(
          "EXTERNALLY_DEFINED_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNALLY_DEFINED_REPRESENTATION_ITEM"));

// Entity: FREE_KINEMATIC_MOTION_REPRESENTATION
      registry.put(
          "FREE_KINEMATIC_MOTION_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FREE_KINEMATIC_MOTION_REPRESENTATION"));

// Entity: INTEGER_REPRESENTATION_ITEM
      registry.put(
          "INTEGER_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "INTEGER_REPRESENTATION_ITEM"));

// Entity: KINEMATIC_LINK_REPRESENTATION_ASSOCIATION
      registry.put(
          "KINEMATIC_LINK_REPRESENTATION_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "KINEMATIC_LINK_REPRESENTATION_ASSOCIATION"));

// Entity: LOCATION_IN_AGGREGATE_REPRESENTATION_ITEM
      registry.put(
          "LOCATION_IN_AGGREGATE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOCATION_IN_AGGREGATE_REPRESENTATION_ITEM"));

// Entity: LOGICAL_REPRESENTATION_ITEM
      registry.put(
          "LOGICAL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOGICAL_REPRESENTATION_ITEM"));

// Entity: NULL_REPRESENTATION_ITEM
      registry.put(
          "NULL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "NULL_REPRESENTATION_ITEM"));

// Entity: PICTURE_REPRESENTATION_ITEM
      registry.put(
          "PICTURE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PICTURE_REPRESENTATION_ITEM"));

// Entity: PREDEFINED_PICTURE_REPRESENTATION_ITEM
      registry.put(
          "PREDEFINED_PICTURE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PREDEFINED_PICTURE_REPRESENTATION_ITEM"));

// Entity: PRESENTATION_STYLE_BY_CONTEXT
      registry.put(
          "PRESENTATION_STYLE_BY_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRESENTATION_STYLE_BY_CONTEXT"));

// Entity: PRESENTED_ITEM_REPRESENTATION
      registry.put(
          "PRESENTED_ITEM_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRESENTED_ITEM_REPRESENTATION"));

// Entity: PROCEDURAL_REPRESENTATION_SEQUENCE
      registry.put(
          "PROCEDURAL_REPRESENTATION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCEDURAL_REPRESENTATION_SEQUENCE"));

// Entity: PROCEDURAL_SHAPE_REPRESENTATION_SEQUENCE
      registry.put(
          "PROCEDURAL_SHAPE_REPRESENTATION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCEDURAL_SHAPE_REPRESENTATION_SEQUENCE"));

// Entity: PRODUCT_CONCEPT_CONTEXT
      registry.put(
          "PRODUCT_CONCEPT_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_CONCEPT_CONTEXT"));

// Entity: PRODUCT_GROUP_CONTEXT
      registry.put(
          "PRODUCT_GROUP_CONTEXT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_GROUP_CONTEXT"));

// Entity: REAL_REPRESENTATION_ITEM
      registry.put(
          "REAL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REAL_REPRESENTATION_ITEM"));

// Entity: REPRESENTATION_CONTEXT_REFERENCE
      registry.put(
          "REPRESENTATION_CONTEXT_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPRESENTATION_CONTEXT_REFERENCE"));

// Entity: REPRESENTATION_PROXY_ITEM
      registry.put(
          "REPRESENTATION_PROXY_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPRESENTATION_PROXY_ITEM"));

// Entity: REPRESENTATION_REFERENCE
      registry.put(
          "REPRESENTATION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REPRESENTATION_REFERENCE"));

// Entity: ROW_REPRESENTATION_ITEM
      registry.put(
          "ROW_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ROW_REPRESENTATION_ITEM"));

// Entity: SHAPE_REPRESENTATION_REFERENCE
      registry.put(
          "SHAPE_REPRESENTATION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SHAPE_REPRESENTATION_REFERENCE"));

// Entity: TABLE_REPRESENTATION_ITEM
      registry.put(
          "TABLE_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TABLE_REPRESENTATION_ITEM"));

// Entity: VARIATIONAL_REPRESENTATION_ITEM
      registry.put(
          "VARIATIONAL_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "VARIATIONAL_REPRESENTATION_ITEM"));

// Entity: MATERIAL_PROPERTY_REPRESENTATION
      registry.put(
          "MATERIAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: EFFECTIVITY_CONTEXT
      registry.put(
          "EFFECTIVITY_CONTEXT",
          (resolver, instance) -> resolver.resolveEffectivity(instance));

// Entity: WITH_DESCRIPTIVE_REPRESENTATION_ITEM
      registry.put("WITH_DESCRIPTIVE_REPRESENTATION_ITEM",
          (resolver, instance) -> resolver.representationResolver.resolveWithDescriptiveRepresentationItem(instance));

// Entity: QUALIFIED_REPRESENTATION_ITEM
      registry.put(
          "QUALIFIED_REPRESENTATION_ITEM",
          (resolver, instance) -> resolver.representationResolver.resolveQualifiedRepresentationItem(instance));

// Entity: MEASURE_REPRESENTATION_ITEM_WITH_UNIT
      registry.put(
          "MEASURE_REPRESENTATION_ITEM_WITH_UNIT",
          (resolver, instance) -> resolver.unitResolver.resolveMeasureRepresentationItemWithUnit(instance));

// Entity: SHAPE_ASPECT_SHAPE_REPRESENTATION
      registry.put(
          "SHAPE_ASPECT_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.representationResolver.resolveShapeAspectShapeRepresentation(instance));

// Entity: MECHANICAL_DESIGN_SHAPE_REPRESENTATION
      registry.put(
          "MECHANICAL_DESIGN_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.productResolver.resolveMechanicalDesignShapeRepresentation(instance));

// Entity: SHAPE_REPRESENTATION_TRANSFORMATION
      registry.put(
          "SHAPE_REPRESENTATION_TRANSFORMATION",
          (resolver, instance) -> resolver.representationResolver.resolveShapeRepresentationTransformation(instance));

// Entity: REPRESENTATION_CONTEXT_3D
      registry.put("REPRESENTATION_CONTEXT_3D", (resolver, instance) -> resolver.representationResolver.resolveRepresentationContext3d(instance));

// Entity: STRUCTURAL_ANALYSIS_REPRESENTATION
      registry.put(
          "STRUCTURAL_ANALYSIS_REPRESENTATION",
          (resolver, instance) -> resolver.analysisResolver.resolveStructuralAnalysisRepresentation(instance));

// Entity: STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS
      registry.put(
          "STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS",
          (resolver, instance) -> resolver.analysisResolver.resolveStructuralAnalysisRepresentationParameters(instance));

// Entity: GEOMETRIC_REPRESENTATION_ITEM
      registry.put("GEOMETRIC_REPRESENTATION_ITEM", StepEntityResolver::resolveGeometricRepresentationItem);

// Entity: TOPOLOGICAL_REPRESENTATION_ITEM
      registry.put(
          "TOPOLOGICAL_REPRESENTATION_ITEM",
          (resolver, instance) -> resolver.representationResolver.resolveTopologicalRepresentationItem(instance));

// Entity: REPRESENTATION_ITEM
      registry.put("REPRESENTATION_ITEM", StepEntityResolver::resolveRepresentationItem);

// Entity: REPRESENTATION_CONTEXT
      registry.put("REPRESENTATION_CONTEXT", (resolver, instance) -> resolver.representationResolver.resolveRepresentationContext(instance));

// Entity: DEFINITIONAL_REPRESENTATION
      registry.put(
          "DEFINITIONAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DEFINITIONAL_REPRESENTATION", false));

// Entity: CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE
      registry.put(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE",
          (resolver, instance) -> resolver.associationResolver.resolveChainBasedItemIdentifiedRepresentationUsage(instance));

// Entity: ITEM_IDENTIFIED_REPRESENTATION_USAGE
      registry.put(
          "ITEM_IDENTIFIED_REPRESENTATION_USAGE",
          (resolver, instance) -> resolver.representationResolver.resolveItemIdentifiedRepresentationUsage(instance));

// Entity: MEASURE_REPRESENTATION_ITEM
      registry.put("MEASURE_REPRESENTATION_ITEM", (resolver, instance) -> resolver.unitResolver.resolveMeasureRepresentationItem(instance));

// Entity: DESCRIPTIVE_REPRESENTATION_ITEM
      registry.put(
          "DESCRIPTIVE_REPRESENTATION_ITEM",
          (resolver, instance) -> resolver.representationResolver.resolveDescriptiveRepresentationItem(instance));

// Entity: VALUE_REPRESENTATION_ITEM
      registry.put("VALUE_REPRESENTATION_ITEM", (resolver, instance) -> resolver.representationResolver.resolveValueRepresentationItem(instance));

// Entity: FEA_MATERIAL_PROPERTY_REPRESENTATION
      registry.put("FEA_MATERIAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.representationResolver.resolveFeaMaterialPropertyRepresentation(instance));

// Entity: NODE_REPRESENTATION
      registry.put("NODE_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveNodeRepresentation(instance));

// Entity: FEA_GROUP_REPRESENTATION
      registry.put("FEA_GROUP_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveFeaGroupRepresentation(instance));

// Entity: DIMENSIONAL_CHARACTERISTIC_REPRESENTATION
      registry.put(
          "DIMENSIONAL_CHARACTERISTIC_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: GEOMETRIC_REPRESENTATION_ITEM_WITH_GEOMETRY
      registry.put(
          "GEOMETRIC_REPRESENTATION_ITEM_WITH_GEOMETRY",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: REPRESENTATION_WITH_PARAMETERS
      registry.put(
          "REPRESENTATION_WITH_PARAMETERS",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "REPRESENTATION_WITH_PARAMETERS", false));

// Entity: MIXED_SHAPE_REPRESENTATION
      registry.put(
          "MIXED_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "MIXED_SHAPE_REPRESENTATION", true));

// Entity: MATERIAL_PROPERTY_DEFINITION_REPRESENTATION
      registry.put(
          "MATERIAL_PROPERTY_DEFINITION_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: MECHANICAL_PROPERTY_REPRESENTATION
      registry.put(
          "MECHANICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: THERMAL_PROPERTY_REPRESENTATION
      registry.put(
          "THERMAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: ELECTRICAL_PROPERTY_REPRESENTATION
      registry.put(
          "ELECTRICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: OPTICAL_PROPERTY_REPRESENTATION
      registry.put(
          "OPTICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: MAGNETIC_PROPERTY_REPRESENTATION
      registry.put(
          "MAGNETIC_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: ACOUSTIC_PROPERTY_REPRESENTATION
      registry.put(
          "ACOUSTIC_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: RADIATION_PROPERTY_REPRESENTATION
      registry.put(
          "RADIATION_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: CHEMICAL_PROPERTY_REPRESENTATION
      registry.put(
          "CHEMICAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: ENVIRONMENTAL_PROPERTY_REPRESENTATION
      registry.put(
          "ENVIRONMENTAL_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));

// Entity: SHAPE_REPRESENTATION_MAP
      registry.put(
          "SHAPE_REPRESENTATION_MAP",
          (resolver, instance) -> resolver.resolveRepresentationMap(instance));

// Entity: GEOMETRIC_REPRESENTATION_MAP
      registry.put(
          "GEOMETRIC_REPRESENTATION_MAP",
          (resolver, instance) -> resolver.resolveRepresentationMap(instance));

// Entity: ANALYSIS_REPRESENTATION
      registry.put(
          "ANALYSIS_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ANALYSIS_REPRESENTATION", false));

// Entity: KINEMATIC_REPRESENTATION
      registry.put(
          "KINEMATIC_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "KINEMATIC_REPRESENTATION", false));

// Entity: KINEMATIC_REPRESENTATION_CONTEXT
      registry.put(
          "KINEMATIC_REPRESENTATION_CONTEXT",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "KINEMATIC_REPRESENTATION_CONTEXT", false));

// Entity: KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP
      registry.put(
          "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationRelationship(instance, "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP"));

// Entity: VALIDATION_PROPERTY_REPRESENTATION
      registry.put("VALIDATION_PROPERTY_REPRESENTATION",
          (resolver, instance) -> resolver.representationResolver.resolveValidationPropertyRepresentation(instance));

// Entity: CALCULATED_GEOMETRIC_REPRESENTATION_ITEM
      registry.put("CALCULATED_GEOMETRIC_REPRESENTATION_ITEM",
          (resolver, instance) -> resolver.representationResolver.resolveCalculatedGeometricRepresentationItem(instance));

// Entity: HYBRID_SHAPE_REPRESENTATION
      registry.put("HYBRID_SHAPE_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveHybridShapeRepresentation(instance));

// Entity: DRAWING_REPRESENTATION
      registry.put("DRAWING_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveDrawingRepresentation(instance));

// Entity: SCHEMATIC_REPRESENTATION
      registry.put("SCHEMATIC_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveSchematicRepresentation(instance));

// Entity: SKETCH_REPRESENTATION
      registry.put("SKETCH_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveSketchRepresentation(instance));

// Entity: SECTION_REPRESENTATION
      registry.put("SECTION_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveSectionRepresentation(instance));

// Entity: TABULATION_REPRESENTATION
      registry.put("TABULATION_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveTabulationRepresentation(instance));

// Entity: ZONE_REPRESENTATION
      registry.put("ZONE_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveZoneRepresentation(instance));

// Entity: COMPOUND_REPRESENTATION_ITEM
      registry.put("COMPOUND_REPRESENTATION_ITEM", (resolver, instance) -> resolver.resolveCompoundRepresentationItem(instance, "COMPOUND_REPRESENTATION_ITEM"));

// Entity: CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION
      registry.put(
          "CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.representationResolver.resolveContextDependentGeometricShapeRepresentation(instance));

// Entity: ANGULAR_DIMENSION_REPRESENTATION
      registry.put(
          "ANGULAR_DIMENSION_REPRESENTATION",
          (resolver, instance) -> resolver.annotationResolver.resolveAngularDimensionRepresentation(instance));

// Entity: CHAIN_DIMENSION_REPRESENTATION
      registry.put(
          "CHAIN_DIMENSION_REPRESENTATION",
          (resolver, instance) -> resolver.annotationResolver.resolveChainDimensionRepresentation(instance));

// Entity: ORDINATE_DIMENSION_REPRESENTATION
      registry.put(
          "ORDINATE_DIMENSION_REPRESENTATION",
          (resolver, instance) -> resolver.annotationResolver.resolveOrdinateDimensionRepresentation(instance));

// Entity: SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE
      registry.put(
          "SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE",
          (resolver, instance) -> resolver.representationResolver.resolveShapeDimensionRepresentationWithTolerance(instance));

// Entity: TEXT_FILE_REPRESENTATION
      registry.put("TEXT_FILE_REPRESENTATION", (resolver, instance) -> resolver.representationResolver.resolveTextFileRepresentation(instance));

// Entity: GEOMETRIC_SET_SHAPE_REPRESENTATION (shape representation)
      registry.put(
          "GEOMETRIC_SET_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "GEOMETRIC_SET_SHAPE_REPRESENTATION", true));

// Entity: REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT
      registry.put(
          "REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT"));

// Entity: BREAKDOWN_CONTEXT
      registry.put(
          "BREAKDOWN_CONTEXT",
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationship(instance, "BREAKDOWN_CONTEXT"));

// Entity: BREAKDOWN_ELEMENT_USAGE
      registry.put(
          "BREAKDOWN_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationship(instance, "BREAKDOWN_ELEMENT_USAGE"));

// Entity: SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION (shape representation)
      registry.put(
          "SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION", true));

// Entity: SURFACE_MODEL_SHAPE_REPRESENTATION (shape representation)
      registry.put(
          "SURFACE_MODEL_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "SURFACE_MODEL_SHAPE_REPRESENTATION", true));

  }
}
