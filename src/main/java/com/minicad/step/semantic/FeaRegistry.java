package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for fea entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class FeaRegistry {

  private FeaRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: ANALYSIS_MODEL
      registry.put(
          "ANALYSIS_MODEL",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "ANALYSIS_MODEL", false));

// Entity: COMPONENT_FEATURE
      registry.put(
          "COMPONENT_FEATURE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMPONENT_FEATURE"));

// Entity: INSTANCED_FEATURE
      registry.put(
          "INSTANCED_FEATURE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "INSTANCED_FEATURE"));

// Entity: FEATURE_COMPONENT_RELATIONSHIP
      registry.put(
          "FEATURE_COMPONENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "FEATURE_COMPONENT_RELATIONSHIP"));

// Entity: MAKE_FROM_FEATURE_RELATIONSHIP
      registry.put(
          "MAKE_FROM_FEATURE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "MAKE_FROM_FEATURE_RELATIONSHIP"));

// Entity: SHAPE_FEATURE_FIT_RELATIONSHIP
      registry.put(
          "SHAPE_FEATURE_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_FEATURE_FIT_RELATIONSHIP"));

// Entity: A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS"));

// Entity: A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS
      registry.put(
          "A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MS_EQUIVALENCE_CRITERION_WITH_SPECIFIED_ELEMENTS"));

// Entity: ANALYSIS_ASSIGNMENT
      registry.put(
          "ANALYSIS_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANALYSIS_ASSIGNMENT"));

// Entity: BREAKDOWN_ELEMENT_GROUP_ASSIGNMENT
      registry.put(
          "BREAKDOWN_ELEMENT_GROUP_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BREAKDOWN_ELEMENT_GROUP_ASSIGNMENT"));

// Entity: COMPONENT_FEATURE_RELATIONSHIP
      registry.put(
          "COMPONENT_FEATURE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COMPONENT_FEATURE_RELATIONSHIP"));

// Entity: CONCEPT_FEATURE_RELATIONSHIP
      registry.put(
          "CONCEPT_FEATURE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONCEPT_FEATURE_RELATIONSHIP"));

// Entity: CONCEPT_FEATURE_RELATIONSHIP_WITH_CONDITION
      registry.put(
          "CONCEPT_FEATURE_RELATIONSHIP_WITH_CONDITION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONCEPT_FEATURE_RELATIONSHIP_WITH_CONDITION"));

// Entity: CONTACT_FEATURE_DEFINITION_FIT_RELATIONSHIP
      registry.put(
          "CONTACT_FEATURE_DEFINITION_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONTACT_FEATURE_DEFINITION_FIT_RELATIONSHIP"));

// Entity: CURRENT_CHANGE_ELEMENT_ASSIGNMENT
      registry.put(
          "CURRENT_CHANGE_ELEMENT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURRENT_CHANGE_ELEMENT_ASSIGNMENT"));

// Entity: GEOMETRIC_MODEL_ELEMENT_RELATIONSHIP
      registry.put(
          "GEOMETRIC_MODEL_ELEMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "GEOMETRIC_MODEL_ELEMENT_RELATIONSHIP"));

// Entity: PREVIOUS_CHANGE_ELEMENT_ASSIGNMENT
      registry.put(
          "PREVIOUS_CHANGE_ELEMENT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PREVIOUS_CHANGE_ELEMENT_ASSIGNMENT"));

// Entity: SHAPE_FEATURE_DEFINITION_FIT_RELATIONSHIP
      registry.put(
          "SHAPE_FEATURE_DEFINITION_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SHAPE_FEATURE_DEFINITION_FIT_RELATIONSHIP"));

// Entity: SHAPE_FEATURE_DEFINITION_RELATIONSHIP
      registry.put(
          "SHAPE_FEATURE_DEFINITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "SHAPE_FEATURE_DEFINITION_RELATIONSHIP"));

// Entity: ADD_ELEMENT
      registry.put(
          "ADD_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ADD_ELEMENT"));

// Entity: ANALYSIS_ITEM
      registry.put(
          "ANALYSIS_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ANALYSIS_ITEM"));

// Entity: BREAKDOWN_ELEMENT_REALIZATION
      registry.put(
          "BREAKDOWN_ELEMENT_REALIZATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "BREAKDOWN_ELEMENT_REALIZATION"));

// Entity: CHANGE_ELEMENT
      registry.put(
          "CHANGE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CHANGE_ELEMENT"));

// Entity: CHANGE_ELEMENT_SEQUENCE
      registry.put(
          "CHANGE_ELEMENT_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CHANGE_ELEMENT_SEQUENCE"));

// Entity: CROSS_SECTIONAL_ALTERNATIVE_SHAPE_ELEMENT
      registry.put(
          "CROSS_SECTIONAL_ALTERNATIVE_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_ALTERNATIVE_SHAPE_ELEMENT"));

// Entity: CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT
      registry.put(
          "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT"));

// Entity: CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_LACING
      registry.put(
          "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_LACING",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_LACING"));

// Entity: CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_TUBULAR_COVER
      registry.put(
          "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_TUBULAR_COVER",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT_WITH_TUBULAR_COVER"));

// Entity: CROSS_SECTIONAL_OCCURRENCE_SHAPE_ELEMENT
      registry.put(
          "CROSS_SECTIONAL_OCCURRENCE_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CROSS_SECTIONAL_OCCURRENCE_SHAPE_ELEMENT"));

// Entity: CROSS_SECTIONAL_PART_SHAPE_ELEMENT
      registry.put(
          "CROSS_SECTIONAL_PART_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CROSS_SECTIONAL_PART_SHAPE_ELEMENT"));

// Entity: DELETE_ELEMENT
      registry.put(
          "DELETE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DELETE_ELEMENT"));

// Entity: DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS
      registry.put(
          "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS"));

// Entity: DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS_WIREFRAME_MODEL
      registry.put(
          "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS_WIREFRAME_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_TOPOLOGICAL_ELEMENTS_WIREFRAME_MODEL"));

// Entity: FUNCTIONAL_ELEMENT_USAGE
      registry.put(
          "FUNCTIONAL_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FUNCTIONAL_ELEMENT_USAGE"));

// Entity: INAPPROPRIATE_ELEMENT_VISIBILITY
      registry.put(
          "INAPPROPRIATE_ELEMENT_VISIBILITY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INAPPROPRIATE_ELEMENT_VISIBILITY"));

// Entity: INCONSISTENT_ELEMENT_REFERENCE
      registry.put(
          "INCONSISTENT_ELEMENT_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCONSISTENT_ELEMENT_REFERENCE"));

// Entity: INDIRECTLY_SELECTED_ELEMENTS
      registry.put(
          "INDIRECTLY_SELECTED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDIRECTLY_SELECTED_ELEMENTS"));

// Entity: INDIRECTLY_SELECTED_SHAPE_ELEMENTS
      registry.put(
          "INDIRECTLY_SELECTED_SHAPE_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INDIRECTLY_SELECTED_SHAPE_ELEMENTS"));

// Entity: MODIFY_ELEMENT
      registry.put(
          "MODIFY_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODIFY_ELEMENT"));

// Entity: PHYSICAL_ELEMENT_USAGE
      registry.put(
          "PHYSICAL_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PHYSICAL_ELEMENT_USAGE"));

// Entity: SYSTEM_ELEMENT_USAGE
      registry.put(
          "SYSTEM_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SYSTEM_ELEMENT_USAGE"));

// Entity: TWISTED_CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT
      registry.put(
          "TWISTED_CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TWISTED_CROSS_SECTIONAL_GROUP_SHAPE_ELEMENT"));

// Entity: UNUSED_SHAPE_ELEMENT
      registry.put(
          "UNUSED_SHAPE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "UNUSED_SHAPE_ELEMENT"));

// Entity: USER_SELECTED_ELEMENTS
      registry.put(
          "USER_SELECTED_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "USER_SELECTED_ELEMENTS"));

// Entity: USER_SELECTED_SHAPE_ELEMENTS
      registry.put(
          "USER_SELECTED_SHAPE_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "USER_SELECTED_SHAPE_ELEMENTS"));

// Entity: WRONG_ELEMENT_NAME
      registry.put(
          "WRONG_ELEMENT_NAME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "WRONG_ELEMENT_NAME"));

// Entity: CHARACTERIZED_PRODUCT_CONCEPT_FEATURE
      registry.put(
          "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE"));

// Entity: CHARACTERIZED_PRODUCT_CONCEPT_FEATURE_CATEGORY
      registry.put(
          "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE_CATEGORY",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHARACTERIZED_PRODUCT_CONCEPT_FEATURE_CATEGORY"));

// Entity: CONCEPT_FEATURE_OPERATOR
      registry.put(
          "CONCEPT_FEATURE_OPERATOR",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONCEPT_FEATURE_OPERATOR"));

// Entity: CONDITIONAL_CONCEPT_FEATURE
      registry.put(
          "CONDITIONAL_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONDITIONAL_CONCEPT_FEATURE"));

// Entity: CONTACT_FEATURE
      registry.put(
          "CONTACT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONTACT_FEATURE"));

// Entity: ELEMENTARY_FUNCTION
      registry.put(
          "ELEMENTARY_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ELEMENTARY_FUNCTION"));

// Entity: ELEMENTARY_SPACE
      registry.put(
          "ELEMENTARY_SPACE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ELEMENTARY_SPACE"));

// Entity: EXCLUSIVE_PRODUCT_CONCEPT_FEATURE_CATEGORY
      registry.put(
          "EXCLUSIVE_PRODUCT_CONCEPT_FEATURE_CATEGORY",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXCLUSIVE_PRODUCT_CONCEPT_FEATURE_CATEGORY"));

// Entity: INCLUSION_PRODUCT_CONCEPT_FEATURE
      registry.put(
          "INCLUSION_PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "INCLUSION_PRODUCT_CONCEPT_FEATURE"));

// Entity: PACKAGE_PRODUCT_CONCEPT_FEATURE
      registry.put(
          "PACKAGE_PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PACKAGE_PRODUCT_CONCEPT_FEATURE"));

// Entity: PLACED_FEATURE
      registry.put(
          "PLACED_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PLACED_FEATURE"));

// Entity: TRANSITION_FEATURE
      registry.put(
          "TRANSITION_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TRANSITION_FEATURE"));

// Entity: TRANSPORT_FEATURE
      registry.put(
          "TRANSPORT_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TRANSPORT_FEATURE"));

// Entity: DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS
      registry.put(
          "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS"));

// Entity: DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS_WIREFRAME_MODEL
      registry.put(
          "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS_WIREFRAME_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_NUMBER_OF_GEOMETRIC_ELEMENTS_WIREFRAME_MODEL"));

// Entity: FIXED_ELEMENT_GEOMETRIC_CONSTRAINT
      registry.put(
          "FIXED_ELEMENT_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FIXED_ELEMENT_GEOMETRIC_CONSTRAINT"));

// Entity: ASSIGNED_ANALYSIS
      registry.put(
          "ASSIGNED_ANALYSIS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSIGNED_ANALYSIS"));

// Entity: MAKE_FROM_FEATURE
      registry.put("MAKE_FROM_FEATURE", StepEntityResolver::resolveMakeFromFeature);

// Entity: ANALYSIS_RESULT
      registry.put("ANALYSIS_RESULT", StepEntityResolver::resolveAnalysisResult);

// Entity: ANALYSIS_INSTANCE
      registry.put("ANALYSIS_INSTANCE", StepEntityResolver::resolveAnalysisInstance);

// Entity: FINITE_ELEMENT_MESH
      registry.put("FINITE_ELEMENT_MESH", StepEntityResolver::resolveFiniteElementMesh);

// Entity: NODE
      registry.put("NODE", StepEntityResolver::resolveFeaNode);

// Entity: ELEMENT
      registry.put("ELEMENT", StepEntityResolver::resolveFeaElement);

// Entity: LOAD
      registry.put("LOAD", StepEntityResolver::resolveFeaLoad);

// Entity: FEA_MASS_DENSITY
      registry.put("FEA_MASS_DENSITY", StepEntityResolver::resolveFeaMassDensity);

// Entity: FEA_YIELD_STRESS
      registry.put("FEA_YIELD_STRESS", StepEntityResolver::resolveFeaYieldStress);

// Entity: FEA_ULTIMATE_STRESS
      registry.put("FEA_ULTIMATE_STRESS", StepEntityResolver::resolveFeaUltimateStress);

// Entity: STRESS_ANALYSIS
      registry.put("STRESS_ANALYSIS", StepEntityResolver::resolveStressAnalysis);

// Entity: BUCKLING_ANALYSIS
      registry.put("BUCKLING_ANALYSIS", StepEntityResolver::resolveBucklingAnalysis);

// Entity: MODAL_ANALYSIS
      registry.put("MODAL_ANALYSIS", StepEntityResolver::resolveModalAnalysis);

// Entity: THERMAL_ANALYSIS
      registry.put("THERMAL_ANALYSIS", StepEntityResolver::resolveThermalAnalysis);

// Entity: STRUCTURAL_ANALYSIS_MODEL
      registry.put("STRUCTURAL_ANALYSIS_MODEL", StepEntityResolver::resolveStructuralAnalysisModel);

// Entity: MASS_ELEMENT
      registry.put("MASS_ELEMENT", StepEntityResolver::resolveMassElement);

// Entity: CONNECTIVITY_ELEMENT
      registry.put("CONNECTIVITY_ELEMENT", StepEntityResolver::resolveConnectivityElement);

// Entity: ELEMENT_GEOMETRIC_DESCRIPTION
      registry.put("ELEMENT_GEOMETRIC_DESCRIPTION", StepEntityResolver::resolveElementGeometricDescription);

// Entity: NODE_SET
      registry.put("NODE_SET", StepEntityResolver::resolveNodeSet);

// Entity: ELEMENT_SET
      registry.put("ELEMENT_SET", StepEntityResolver::resolveElementSet);

// Entity: FEA_SECURED_VARIABLE
      registry.put("FEA_SECURED_VARIABLE", StepEntityResolver::resolveFeaSecuredVariable);

// Entity: FEA_CONSTANT_FUNCTION_3D
      registry.put("FEA_CONSTANT_FUNCTION_3D", StepEntityResolver::resolveFeaConstantFunction3d);

// Entity: FEATURE_COMPONENT_DEFINITION
      registry.put(
          "FEATURE_COMPONENT_DEFINITION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "FEATURE_COMPONENT_DEFINITION"));

// Entity: PRODUCT_CONCEPT_FEATURE
      registry.put(
          "PRODUCT_CONCEPT_FEATURE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PRODUCT_CONCEPT_FEATURE"));

// Entity: PRODUCT_CONCEPT_FEATURE_ASSOCIATION
      registry.put(
          "PRODUCT_CONCEPT_FEATURE_ASSOCIATION",
          (resolver, instance) -> resolver.resolveShapeAspectRelationship(instance, "PRODUCT_CONCEPT_FEATURE_ASSOCIATION"));

// Entity: PRODUCT_CONCEPT_FEATURE_CATEGORY
      registry.put(
          "PRODUCT_CONCEPT_FEATURE_CATEGORY",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: PRODUCT_CONCEPT_FEATURE_CATEGORY_USAGE
      registry.put(
          "PRODUCT_CONCEPT_FEATURE_CATEGORY_USAGE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: GEOMETRIC_MODEL_ELEMENT
      registry.put(
          "GEOMETRIC_MODEL_ELEMENT",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: FEA_MODEL
      registry.put(
          "FEA_MODEL",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL", false));

// Entity: FEA_MODEL_DEFINITION
      registry.put(
          "FEA_MODEL_DEFINITION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_DEFINITION", false));

// Entity: FEA_MODEL_3D
      registry.put(
          "FEA_MODEL_3D",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_3D", true));

// Entity: FEA_MODEL_2D
      registry.put(
          "FEA_MODEL_2D",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_2D", true));

// Entity: FEA_BEAM_ELEMENT_PROPERTY
      registry.put("FEA_BEAM_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaBeamElementProperty);

// Entity: FEA_2D_ELEMENT_PROPERTY
      registry.put("FEA_2D_ELEMENT_PROPERTY", StepEntityResolver::resolveFea2DElementProperty);

// Entity: FEA_3D_ELEMENT_PROPERTY
      registry.put("FEA_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveFea3DElementProperty);

// Entity: FEA_TRUSS_ELEMENT_PROPERTY
      registry.put("FEA_TRUSS_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaTrussElementProperty);

// Entity: FEA_SPRING_ELEMENT_PROPERTY
      registry.put("FEA_SPRING_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaSpringElementProperty);

// Entity: MARKING_FEATURE
      registry.put("MARKING_FEATURE", StepEntityResolver::resolveMarkingFeature);

// Entity: FEATURE_ELEMENT_DEFINITION
      registry.put("FEATURE_ELEMENT_DEFINITION", StepEntityResolver::resolveFeatureElementDefinition);

// Entity: LOAD_CASE
      registry.put("LOAD_CASE", StepEntityResolver::resolveLoadCase);

// Entity: STRUCTURAL_FEATURE
      registry.put("STRUCTURAL_FEATURE", StepEntityResolver::resolveStructuralFeature);


  }
}
