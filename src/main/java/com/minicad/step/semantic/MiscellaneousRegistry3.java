package com.minicad.step.semantic;

import java.util.Map;

/**
 * Miscellaneous registry part 3.
 */
public final class MiscellaneousRegistry3 {

  private MiscellaneousRegistry3() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: SHAPE_INSPECTION_RESULT_ACCURACY_ASSOCIATION
      registry.put(
          "SHAPE_INSPECTION_RESULT_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SHAPE_INSPECTION_RESULT_ACCURACY_ASSOCIATION"));

// Entity: SMEARED_MATERIAL_DEFINITION
      registry.put(
          "SMEARED_MATERIAL_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SMEARED_MATERIAL_DEFINITION"));

// Entity: SPECIFICATION_DEFINITION
      registry.put(
          "SPECIFICATION_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SPECIFICATION_DEFINITION"));

// Entity: ANGLE_GEOMETRIC_CONSTRAINT
      registry.put(
          "ANGLE_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ANGLE_GEOMETRIC_CONSTRAINT"));

// Entity: COAXIAL_GEOMETRIC_CONSTRAINT
      registry.put(
          "COAXIAL_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COAXIAL_GEOMETRIC_CONSTRAINT"));

// Entity: EXPLICIT_GEOMETRIC_CONSTRAINT
      registry.put(
          "EXPLICIT_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXPLICIT_GEOMETRIC_CONSTRAINT"));

// Entity: CYLINDRICAL_11
      registry.put(
          "CYLINDRICAL_11",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CYLINDRICAL_11"));

// Entity: PARALLEL_OFFSET_GEOMETRIC_CONSTRAINT
      registry.put(
          "PARALLEL_OFFSET_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PARALLEL_OFFSET_GEOMETRIC_CONSTRAINT"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));

// Entity: CIRCULAR_CLOSED_PROFILE
      registry.put(
          "CIRCULAR_CLOSED_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CIRCULAR_CLOSED_PROFILE"));

// Entity: DRAPED_DEFINED_TRANSFORMATION
      registry.put(
          "DRAPED_DEFINED_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DRAPED_DEFINED_TRANSFORMATION"));

// Entity: FUNCTIONALLY_DEFINED_TRANSFORMATION
      registry.put(
          "FUNCTIONALLY_DEFINED_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "FUNCTIONALLY_DEFINED_TRANSFORMATION"));

// Entity: LAID_DEFINED_TRANSFORMATION
      registry.put(
          "LAID_DEFINED_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "LAID_DEFINED_TRANSFORMATION"));

// Entity: NGON_CLOSED_PROFILE
      registry.put(
          "NGON_CLOSED_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "NGON_CLOSED_PROFILE"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: CAMERA_MODEL
      registry.put(
          "CAMERA_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_UNION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION"));

// Entity: CAMERA_MODEL_D3_WITH_HLHSR
      registry.put(
          "CAMERA_MODEL_D3_WITH_HLHSR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_WITH_HLHSR"));

// Entity: CAMERA_MODEL_WITH_LIGHT_SOURCES
      registry.put(
          "CAMERA_MODEL_WITH_LIGHT_SOURCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_WITH_LIGHT_SOURCES"));

// Entity: CARTESIAN_COMPLEX_NUMBER_REGION
      registry.put(
          "CARTESIAN_COMPLEX_NUMBER_REGION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CARTESIAN_COMPLEX_NUMBER_REGION"));

// Entity: CHARACTERIZED_CLASS
      registry.put(
          "CHARACTERIZED_CLASS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CHARACTERIZED_CLASS"));

// Entity: CIRCULAR_AREA
      registry.put(
          "CIRCULAR_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_AREA"));

// Entity: CLASS_BY_EXTENSION
      registry.put(
          "CLASS_BY_EXTENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CLASS_BY_EXTENSION"));

// Entity: CLASS_BY_INTENSION
      registry.put(
          "CLASS_BY_INTENSION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CLASS_BY_INTENSION"));

// Entity: COMPLEX_AREA
      registry.put(
          "COMPLEX_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_AREA"));

// Entity: ELLIPTIC_AREA
      registry.put(
          "ELLIPTIC_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ELLIPTIC_AREA"));

// Entity: EXTERNAL_CLASS_LIBRARY
      registry.put(
          "EXTERNAL_CLASS_LIBRARY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNAL_CLASS_LIBRARY"));

// Entity: LISTED_COMPLEX_NUMBER_DATA
      registry.put(
          "LISTED_COMPLEX_NUMBER_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_COMPLEX_NUMBER_DATA"));

// Entity: LISTED_DATA
      registry.put(
          "LISTED_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_DATA"));

// Entity: LISTED_INTEGER_DATA
      registry.put(
          "LISTED_INTEGER_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_INTEGER_DATA"));

// Entity: LISTED_LOGICAL_DATA
      registry.put(
          "LISTED_LOGICAL_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_LOGICAL_DATA"));

// Entity: LISTED_PRODUCT_SPACE
      registry.put(
          "LISTED_PRODUCT_SPACE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_PRODUCT_SPACE"));

// Entity: LISTED_REAL_DATA
      registry.put(
          "LISTED_REAL_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_REAL_DATA"));

// Entity: LISTED_STRING_DATA
      registry.put(
          "LISTED_STRING_DATA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LISTED_STRING_DATA"));

// Entity: MACHINING_PROCESS_EXECUTABLE
      registry.put(
          "MACHINING_PROCESS_EXECUTABLE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "MACHINING_PROCESS_EXECUTABLE"));

// Entity: PARTIAL_CIRCULAR_PROFILE
      registry.put(
          "PARTIAL_CIRCULAR_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PARTIAL_CIRCULAR_PROFILE"));

// Entity: POLAR_COMPLEX_NUMBER_REGION
      registry.put(
          "POLAR_COMPLEX_NUMBER_REGION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "POLAR_COMPLEX_NUMBER_REGION"));

// Entity: POLYGONAL_AREA
      registry.put(
          "POLYGONAL_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "POLYGONAL_AREA"));

// Entity: PROCESS_OPERATION
      registry.put(
          "PROCESS_OPERATION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PROCESS_OPERATION"));

// Entity: PROCESS_PLAN
      registry.put(
          "PROCESS_PLAN",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PROCESS_PLAN"));

// Entity: PRODUCT_CLASS
      registry.put(
          "PRODUCT_CLASS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_CLASS"));

// Entity: PRODUCT_PROCESS_PLAN
      registry.put(
          "PRODUCT_PROCESS_PLAN",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_PROCESS_PLAN"));

// Entity: PROFILE_FLOOR
      registry.put(
          "PROFILE_FLOOR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PROFILE_FLOOR"));

// Entity: PROPERTY_PROCESS
      registry.put(
          "PROPERTY_PROCESS",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PROPERTY_PROCESS"));

// Entity: RECTANGULAR_AREA
      registry.put(
          "RECTANGULAR_AREA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RECTANGULAR_AREA"));

// Entity: RECTANGULAR_CLOSED_PROFILE
      registry.put(
          "RECTANGULAR_CLOSED_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RECTANGULAR_CLOSED_PROFILE"));

// Entity: REQUIREMENT_ASSIGNED_OBJECT
      registry.put(
          "REQUIREMENT_ASSIGNED_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "REQUIREMENT_ASSIGNED_OBJECT"));

// Entity: REQUIREMENT_FOR_ACTION_RESOURCE
      registry.put(
          "REQUIREMENT_FOR_ACTION_RESOURCE",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "REQUIREMENT_FOR_ACTION_RESOURCE"));

// Entity: REQUIREMENT_SOURCE
      registry.put(
          "REQUIREMENT_SOURCE",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "REQUIREMENT_SOURCE"));

// Entity: ROUNDED_U_PROFILE
      registry.put(
          "ROUNDED_U_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ROUNDED_U_PROFILE"));

// Entity: SCAN_3D_MODEL
      registry.put(
          "SCAN_3D_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCAN_3D_MODEL"));

// Entity: SQUARE_U_PROFILE
      registry.put(
          "SQUARE_U_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SQUARE_U_PROFILE"));

// Entity: STRUCTURED_MESSAGE
      registry.put(
          "STRUCTURED_MESSAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STRUCTURED_MESSAGE"));

// Entity: TEE_PROFILE
      registry.put(
          "TEE_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "TEE_PROFILE"));

// Entity: TRANSFORMATION_WITH_DERIVED_ANGLE
      registry.put(
          "TRANSFORMATION_WITH_DERIVED_ANGLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "TRANSFORMATION_WITH_DERIVED_ANGLE"));

// Entity: TYPE_QUALIFIER
      registry.put(
          "TYPE_QUALIFIER",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "TYPE_QUALIFIER"));

// Entity: VEE_PROFILE
      registry.put(
          "VEE_PROFILE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "VEE_PROFILE"));

// Entity: ABSTRACTED_EXPRESSION_FUNCTION
      registry.put(
          "ABSTRACTED_EXPRESSION_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ABSTRACTED_EXPRESSION_FUNCTION"));

// Entity: ACTION_RESOURCE
      registry.put(
          "ACTION_RESOURCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ACTION_RESOURCE"));

// Entity: AGC_WITH_DIMENSION
      registry.put(
          "AGC_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AGC_WITH_DIMENSION"));

// Entity: AGGREGATE_ID_ATTRIBUTE
      registry.put(
          "AGGREGATE_ID_ATTRIBUTE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AGGREGATE_ID_ATTRIBUTE"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));

// Entity: MODEL_GEOMETRIC_VIEW
      registry.put(
          "MODEL_GEOMETRIC_VIEW",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODEL_GEOMETRIC_VIEW"));

// Entity: ERRONEOUS_TOPOLOGY
      registry.put(
          "ERRONEOUS_TOPOLOGY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ERRONEOUS_TOPOLOGY"));

// Entity: GEOMETRIC_GAP_IN_TOPOLOGY
      registry.put(
          "GEOMETRIC_GAP_IN_TOPOLOGY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GEOMETRIC_GAP_IN_TOPOLOGY"));

// Entity: INAPT_TOPOLOGY
      registry.put(
          "INAPT_TOPOLOGY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INAPT_TOPOLOGY"));

// Entity: INCIDENCE_GEOMETRIC_CONSTRAINT
      registry.put(
          "INCIDENCE_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INCIDENCE_GEOMETRIC_CONSTRAINT"));

// Entity: ANGULAR_DIMENSION
      registry.put(
          "ANGULAR_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGULAR_DIMENSION"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: APPLICATION_DEFINED_FUNCTION
      registry.put(
          "APPLICATION_DEFINED_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLICATION_DEFINED_FUNCTION"));

// Entity: ASCRIBABLE_STATE
      registry.put(
          "ASCRIBABLE_STATE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASCRIBABLE_STATE"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE"));

// Entity: A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));

// Entity: A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));

// Entity: A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));

// Entity: A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: CAMERA_MODEL_D2
      registry.put(
          "CAMERA_MODEL_D2",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D2"));

// Entity: CAMERA_MODEL_D3
      registry.put(
          "CAMERA_MODEL_D3",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_INTERSECTION"));

// Entity: CAMERA_MODEL_D3_MULTI_CLIPPING_UNION
      registry.put(
          "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_MULTI_CLIPPING_UNION"));

// Entity: CAMERA_MODEL_D3_WITH_HLHSR
      registry.put(
          "CAMERA_MODEL_D3_WITH_HLHSR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CAMERA_MODEL_D3_WITH_HLHSR"));

// Entity: CARTESIAN_TRANSFORMATION_OPERATOR_2D
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_2D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_2D"));

// Entity: CARTESIAN_TRANSFORMATION_OPERATOR_3D
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_3D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_3D"));

// Entity: SCAN_3D_MODEL
      registry.put(
          "SCAN_3D_MODEL",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCAN_3D_MODEL"));

// Entity: CYLINDRICAL_11
      registry.put(
          "CYLINDRICAL_11",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CYLINDRICAL_11"));

// Entity: A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));

// Entity: A3MA_EQUIVALENCE_INSPECTION_RESULT
      registry.put(
          "A3MA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_INSPECTION_RESULT"));

// Entity: A3MS_EQUIVALENCE_INSPECTION_RESULT
      registry.put(
          "A3MS_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MS_EQUIVALENCE_INSPECTION_RESULT"));

// Entity: A3M_EQUIVALENCE_ACCURACY_ASSOCIATION
      registry.put(
          "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION"));

// Entity: A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST"));

// Entity: A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST"));

// Entity: A3M_EQUIVALENCE_CRITERION
      registry.put(
          "A3M_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION"));

// Entity: A3M_EQUIVALENCE_CRITERION_FOR_SHAPE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE"));

// Entity: A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));

// Entity: A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));

// Entity: A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));

// Entity: A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE
      registry.put(
          "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE"));

// Entity: A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: ATOMIC_FORMULA
      registry.put(
          "ATOMIC_FORMULA",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATOMIC_FORMULA"));

// Entity: ATOM_BASED_LITERAL
      registry.put(
          "ATOM_BASED_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATOM_BASED_LITERAL"));

// Entity: A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));

// Entity: A3MA_EQUIVALENCE_INSPECTION_RESULT
      registry.put(
          "A3MA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_EQUIVALENCE_INSPECTION_RESULT"));

// Entity: A3MS_EQUIVALENCE_INSPECTION_RESULT
      registry.put(
          "A3MS_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MS_EQUIVALENCE_INSPECTION_RESULT"));

// Entity: A3M_EQUIVALENCE_ACCURACY_ASSOCIATION
      registry.put(
          "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION"));

// Entity: A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST"));

// Entity: A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST"));

// Entity: A3M_EQUIVALENCE_CRITERION
      registry.put(
          "A3M_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION"));

// Entity: A3M_EQUIVALENCE_CRITERION_FOR_SHAPE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE"));

// Entity: A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));

// Entity: A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));

// Entity: A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));

// Entity: A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE
      registry.put(
          "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE"));

// Entity: A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: BACK_CHAINING_RULE
      registry.put(
          "BACK_CHAINING_RULE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BACK_CHAINING_RULE"));

// Entity: BANDED_MATRIX
      registry.put(
          "BANDED_MATRIX",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BANDED_MATRIX"));

// Entity: BASIC_SPARSE_MATRIX
      registry.put(
          "BASIC_SPARSE_MATRIX",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BASIC_SPARSE_MATRIX"));

// Entity: BINARY_LITERAL
      registry.put(
          "BINARY_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BINARY_LITERAL"));

// Entity: BACK_CHAINING_RULE
      registry.put(
          "BACK_CHAINING_RULE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BACK_CHAINING_RULE"));

// Entity: CAMERA_IMAGE_2D_WITH_SCALE
      registry.put(
          "CAMERA_IMAGE_2D_WITH_SCALE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_IMAGE_2D_WITH_SCALE"));

// Entity: CAMERA_IMAGE_3D_WITH_SCALE
      registry.put(
          "CAMERA_IMAGE_3D_WITH_SCALE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CAMERA_IMAGE_3D_WITH_SCALE"));

// Entity: CARTESIAN_11
      registry.put(
          "CARTESIAN_11",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CARTESIAN_11"));

// Entity: CDGC_WITH_DIMENSION
      registry.put(
          "CDGC_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CDGC_WITH_DIMENSION"));

// Entity: CHARACTERISTIC_DATA_COLUMN_HEADER
      registry.put(
          "CHARACTERISTIC_DATA_COLUMN_HEADER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_COLUMN_HEADER"));

// Entity: CHARACTERISTIC_DATA_TABLE_HEADER
      registry.put(
          "CHARACTERISTIC_DATA_TABLE_HEADER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_TABLE_HEADER"));

// Entity: CHARACTERISTIC_DATA_TABLE_HEADER_DECOMPOSITION
      registry.put(
          "CHARACTERISTIC_DATA_TABLE_HEADER_DECOMPOSITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_TABLE_HEADER_DECOMPOSITION"));

// Entity: CHARACTERIZED_LOCATION_OBJECT
      registry.put(
          "CHARACTERIZED_LOCATION_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERIZED_LOCATION_OBJECT"));

// Entity: CIRCULAR_INVOLUTE
      registry.put(
          "CIRCULAR_INVOLUTE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_INVOLUTE"));

// Entity: CLGC_WITH_DIMENSION
      registry.put(
          "CLGC_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CLGC_WITH_DIMENSION"));

// Entity: COLLECTION
      registry.put(
          "COLLECTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION"));

// Entity: COLLECTION_MEMBERSHIP
      registry.put(
          "COLLECTION_MEMBERSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION_MEMBERSHIP"));

// Entity: COMPARISON_EQUAL
      registry.put(
          "COMPARISON_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_EQUAL"));

// Entity: COMPARISON_GREATER
      registry.put(
          "COMPARISON_GREATER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_GREATER"));

// Entity: COMPARISON_GREATER_EQUAL
      registry.put(
          "COMPARISON_GREATER_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_GREATER_EQUAL"));

// Entity: COMPARISON_LESS
      registry.put(
          "COMPARISON_LESS",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_LESS"));

// Entity: COMPARISON_LESS_EQUAL
      registry.put(
          "COMPARISON_LESS_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_LESS_EQUAL"));

// Entity: COMPARISON_NOT_EQUAL
      registry.put(
          "COMPARISON_NOT_EQUAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPARISON_NOT_EQUAL"));

  }
}
