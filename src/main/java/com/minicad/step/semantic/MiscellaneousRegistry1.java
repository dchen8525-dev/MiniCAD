package com.minicad.step.semantic;

import com.minicad.step.model.StepAxis2Placement3D;

import java.util.Map;

/**
 * Miscellaneous registry part 1.
 */
public final class MiscellaneousRegistry1 {

  private MiscellaneousRegistry1() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: SPHERE
      registry.put(
          "SPHERE",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "SPHERE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 1));

// Entity: ELLIPSOID
      registry.put(
          "ELLIPSOID",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "ELLIPSOID", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));

// NOTE: RIGHT_CIRCULAR_CONE moved to ProductRegistry (before CSG_PRIMITIVE for complex entity priority)

// Entity: BOX_DOMAIN
      registry.put("BOX_DOMAIN", StepEntityResolver::resolveBoxDomain);

// Entity: COMPLEX_CLIPPING_RESULT
      registry.put("COMPLEX_CLIPPING_RESULT", StepEntityResolver::resolveComplexClippingResult);

// Entity: RANGE_CHARACTERISTIC
      registry.put(
          "RANGE_CHARACTERISTIC",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "RANGE_CHARACTERISTIC", false));

// Entity: REINFORCEMENT_ORIENTATION_BASIS
      registry.put(
          "REINFORCEMENT_ORIENTATION_BASIS",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "REINFORCEMENT_ORIENTATION_BASIS", false));

// Entity: MESSAGE_CONTENTS_ASSIGNMENT
      registry.put(
          "MESSAGE_CONTENTS_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "MESSAGE_CONTENTS_ASSIGNMENT", false));

// Entity: EVALUATED_CHARACTERISTIC
      registry.put(
          "EVALUATED_CHARACTERISTIC",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "EVALUATED_CHARACTERISTIC", false));

// Entity: EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT
      registry.put(
          "EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance,
                  "EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT",
                  false));

// Entity: DRAWING_SHEET_LAYOUT
      registry.put(
          "DRAWING_SHEET_LAYOUT",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAWING_SHEET_LAYOUT", false));

// Entity: DRAWING_SHEET_REVISION
      registry.put(
          "DRAWING_SHEET_REVISION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DRAWING_SHEET_REVISION", false));

// Entity: APPLICATION_PROTOCOL_DEFINITION
      registry.put(
          "APPLICATION_PROTOCOL_DEFINITION",
          StepEntityResolver::resolveApplicationProtocolDefinition);

// Entity: PRODUCT
      registry.put("PRODUCT", (resolver, instance) -> resolver.productResolver.resolveProduct(instance));

// Entity: PRODUCT_CATEGORY
      registry.put("PRODUCT_CATEGORY", (resolver, instance) -> resolver.productResolver.resolveProductCategory(instance));

// Entity: PRODUCT_CATEGORY_RELATIONSHIP
      registry.put(
          "PRODUCT_CATEGORY_RELATIONSHIP",
          StepEntityResolver::resolveProductCategoryRelationship);

// Entity: PRODUCT_RELATED_PRODUCT_CATEGORY
      registry.put(
          "PRODUCT_RELATED_PRODUCT_CATEGORY",
          StepEntityResolver::resolveProductRelatedProductCategory);

// Entity: PRODUCT_RELATIONSHIP
      registry.put("PRODUCT_RELATIONSHIP", (resolver, instance) -> resolver.productResolver.resolveProductRelationship(instance));

// Entity: PROPERTY_DEFINITION
      registry.put("PROPERTY_DEFINITION", StepEntityResolver::resolvePropertyDefinition);

// Entity: PROPERTY_DEFINITION_RELATIONSHIP
      registry.put(
          "PROPERTY_DEFINITION_RELATIONSHIP",
          (resolver, instance) -> resolver.propertyResolver.resolvePropertyDefinitionRelationship(instance));

// Entity: GENERAL_PROPERTY
      registry.put("GENERAL_PROPERTY", (resolver, instance) -> resolver.propertyResolver.resolveGeneralProperty(instance));

// Entity: GENERAL_PROPERTY_RELATIONSHIP
      registry.put(
          "GENERAL_PROPERTY_RELATIONSHIP",
          StepEntityResolver::resolveGeneralPropertyRelationship);

// Entity: GROUP
      registry.put("GROUP", StepEntityResolver::resolveGroup);

// Entity: CLASS
      registry.put("CLASS", (resolver, instance) -> resolver.resolveGroup(instance, "CLASS"));

// Entity: CLASS_SYSTEM
      registry.put(
          "CLASS_SYSTEM", (resolver, instance) -> resolver.resolveGroup(instance, "CLASS_SYSTEM"));

// Entity: GROUP_RELATIONSHIP
      registry.put("GROUP_RELATIONSHIP", (resolver, instance) -> resolver.propertyResolver.resolveGroupRelationship(instance));

// Entity: GROUP_ASSIGNMENT
      registry.put("GROUP_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveGroupAssignment(instance));

// Entity: APPLIED_GROUP_ASSIGNMENT
      registry.put("APPLIED_GROUP_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveAppliedGroupAssignment(instance));

// Entity: CC_DESIGN_SPECIFICATION_REFERENCE
      registry.put(
          "CC_DESIGN_SPECIFICATION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveAppliedDocumentReference(instance, "CC_DESIGN_SPECIFICATION_REFERENCE"));

// Entity: LANGUAGE
      registry.put("LANGUAGE", StepEntityResolver::resolveLanguage);

// Entity: LANGUAGE_ASSIGNMENT
      registry.put("LANGUAGE_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveLanguageAssignment(instance));

// Entity: APPLIED_LANGUAGE_ASSIGNMENT
      registry.put(
          "APPLIED_LANGUAGE_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveAppliedLanguageAssignment(instance));

// Entity: CLASSIFICATION_ROLE
      registry.put("CLASSIFICATION_ROLE", (resolver, instance) -> resolver.assignmentResolver.resolveClassificationRole(instance));

// Entity: CLASSIFICATION_ASSIGNMENT
      registry.put(
          "CLASSIFICATION_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveClassificationAssignment(instance));

// Entity: APPLIED_CLASSIFICATION_ASSIGNMENT
      registry.put(
          "APPLIED_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) -> resolver.assignmentResolver.resolveAppliedClassificationAssignment(instance));

// Entity: IDENTIFICATION_ROLE
      registry.put("IDENTIFICATION_ROLE", (resolver, instance) -> resolver.assignmentResolver.resolveIdentificationRole(instance));

// Entity: IDENTIFICATION_ASSIGNMENT
      registry.put(
          "IDENTIFICATION_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveIdentificationAssignment(instance));

// Entity: APPLIED_IDENTIFICATION_ASSIGNMENT
      registry.put(
          "APPLIED_IDENTIFICATION_ASSIGNMENT",
          (resolver, instance) -> resolver.assignmentResolver.resolveAppliedIdentificationAssignment(instance));

// Entity: EXTERNAL_IDENTIFICATION_ASSIGNMENT
      registry.put(
          "EXTERNAL_IDENTIFICATION_ASSIGNMENT",
          (resolver, instance) -> resolver.assignmentResolver.resolveExternalIdentificationAssignment(instance));

// Entity: APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT
      registry.put(
          "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT",
          (resolver, instance) -> resolver.assignmentResolver.resolveAppliedExternalIdentificationAssignment(instance));

// Entity: NAME_ASSIGNMENT
      registry.put("NAME_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveNameAssignment(instance));

// Entity: APPLIED_NAME_ASSIGNMENT
      registry.put("APPLIED_NAME_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveAppliedNameAssignment(instance));

// Entity: DESCRIPTION_ATTRIBUTE
      registry.put("DESCRIPTION_ATTRIBUTE", StepEntityResolver::resolveDescriptionAttribute);

// Entity: NAME_ATTRIBUTE
      registry.put("NAME_ATTRIBUTE", StepEntityResolver::resolveNameAttribute);

// Entity: ID_ATTRIBUTE
      registry.put("ID_ATTRIBUTE", StepEntityResolver::resolveIdAttribute);

// Entity: EXTERNALLY_DEFINED_ITEM
      registry.put("EXTERNALLY_DEFINED_ITEM",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_ITEM"));

// Entity: EXTERNAL_SOURCE_RELATIONSHIP
      registry.put(
          "EXTERNAL_SOURCE_RELATIONSHIP",
          StepEntityResolver::resolveExternalSourceRelationship);

// Entity: EXTERNALLY_DEFINED_ITEM
      registry.put(
          "EXTERNALLY_DEFINED_ITEM",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_ITEM"));

// Entity: EXTERNALLY_DEFINED_CLASS
      registry.put(
          "EXTERNALLY_DEFINED_CLASS",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_CLASS"));

// Entity: EXTERNALLY_DEFINED_GENERAL_PROPERTY
      registry.put(
          "EXTERNALLY_DEFINED_GENERAL_PROPERTY",
          (resolver, instance) ->
              resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_GENERAL_PROPERTY"));

// Entity: CHARACTERIZED_OBJECT
      registry.put("CHARACTERIZED_OBJECT", StepEntityResolver::resolveCharacterizedObject);

// Entity: APEX
      registry.put(
          "APEX",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "APEX"));

// Entity: ALL_AROUND_SHAPE_ASPECT
      registry.put(
          "ALL_AROUND_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "ALL_AROUND_SHAPE_ASPECT"));

// Entity: BETWEEN_SHAPE_ASPECT
      registry.put(
          "BETWEEN_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "BETWEEN_SHAPE_ASPECT"));

// Entity: CENTRE_OF_SYMMETRY
      registry.put(
          "CENTRE_OF_SYMMETRY",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CENTRE_OF_SYMMETRY"));

// Entity: COMPOSITE_GROUP_SHAPE_ASPECT
      registry.put(
          "COMPOSITE_GROUP_SHAPE_ASPECT",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "COMPOSITE_GROUP_SHAPE_ASPECT"));

// Entity: COMPOSITE_SHAPE_ASPECT
      registry.put(
          "COMPOSITE_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMPOSITE_SHAPE_ASPECT"));

// Entity: CONTINUOUS_SHAPE_ASPECT
      registry.put(
          "CONTINUOUS_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CONTINUOUS_SHAPE_ASPECT"));

// Entity: GEOMETRIC_ALIGNMENT
      registry.put(
          "GEOMETRIC_ALIGNMENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_ALIGNMENT"));

// Entity: GEOMETRIC_CONTACT
      registry.put(
          "GEOMETRIC_CONTACT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_CONTACT"));

// Entity: GEOMETRIC_INTERSECTION
      registry.put(
          "GEOMETRIC_INTERSECTION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_INTERSECTION"));

// Entity: GROUP_SHAPE_ASPECT
      registry.put(
          "GROUP_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "GROUP_SHAPE_ASPECT"));

// Entity: EXTENSION
      registry.put(
          "EXTENSION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "EXTENSION"));

// Entity: PARALLEL_OFFSET
      registry.put(
          "PARALLEL_OFFSET",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PARALLEL_OFFSET"));

// Entity: PERPENDICULAR_TO
      registry.put(
          "PERPENDICULAR_TO",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PERPENDICULAR_TO"));

// Entity: INSTANCED_SHAPE_ASPECT
      registry.put(
          "INSTANCED_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "INSTANCED_SHAPE_ASPECT"));

// Entity: SINGULAR_SHAPE_ASPECT
      registry.put(
          "SINGULAR_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "SINGULAR_SHAPE_ASPECT"));

// Entity: SYMMETRIC_SHAPE_ASPECT
      registry.put(
          "SYMMETRIC_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "SYMMETRIC_SHAPE_ASPECT"));

// Entity: TANGENT
      registry.put(
          "TANGENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "TANGENT"));

// Entity: SHAPE_ASPECT
      registry.put("SHAPE_ASPECT", StepEntityResolver::resolveShapeAspect);

// Entity: SHAPE_ASPECT_OCCURRENCE
      registry.put("SHAPE_ASPECT_OCCURRENCE",
          (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, "SHAPE_ASPECT_OCCURRENCE"));

// Entity: SHAPE_ASPECT_OCCURRENCE
      registry.put(
          "SHAPE_ASPECT_OCCURRENCE",
          (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, "SHAPE_ASPECT_OCCURRENCE"));

// Entity: ANGULAR_LOCATION
      registry.put(
          "ANGULAR_LOCATION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "ANGULAR_LOCATION"));

// Entity: COMPOSITE_SHAPE_ASPECT_RELATIONSHIP
      registry.put(
          "COMPOSITE_SHAPE_ASPECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(
                  instance, "COMPOSITE_SHAPE_ASPECT_RELATIONSHIP"));

// Entity: GEOMETRIC_ALIGNMENT_RELATIONSHIP
      registry.put(
          "GEOMETRIC_ALIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "GEOMETRIC_ALIGNMENT_RELATIONSHIP"));

// Entity: GEOMETRIC_CONTACT_RELATIONSHIP
      registry.put(
          "GEOMETRIC_CONTACT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "GEOMETRIC_CONTACT_RELATIONSHIP"));

// Entity: SHAPE_ASPECT_ASSOCIATIVITY
      registry.put(
          "SHAPE_ASPECT_ASSOCIATIVITY",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_ASSOCIATIVITY"));

// Entity: SHAPE_ASPECT_DERIVING_RELATIONSHIP
      registry.put(
          "SHAPE_ASPECT_DERIVING_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_DERIVING_RELATIONSHIP"));

// Entity: SHAPE_ASPECT_TRANSITION
      registry.put(
          "SHAPE_ASPECT_TRANSITION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_TRANSITION"));

// Entity: SHAPE_DEFINING_RELATIONSHIP
      registry.put(
          "SHAPE_DEFINING_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_DEFINING_RELATIONSHIP"));

// Entity: SHAPE_ASPECT_RELATIONSHIP
      registry.put(
          "SHAPE_ASPECT_RELATIONSHIP",
          StepEntityResolver::resolveShapeAspectRelationship);

// Entity: ROW_VARIABLE
      registry.put("ROW_VARIABLE", StepEntityResolver::resolveRowVariable);

// Entity: SCALAR_VARIABLE
      registry.put("SCALAR_VARIABLE", StepEntityResolver::resolveScalarVariable);

// Entity: ABSTRACT_VARIABLE
      registry.put("ABSTRACT_VARIABLE", StepEntityResolver::resolveAbstractVariable);

// Entity: ATTRIBUTE_ASSERTION
      registry.put("ATTRIBUTE_ASSERTION", StepEntityResolver::resolveAttributeAssertion);

// Entity: BACK_CHAINING_RULE_BODY
      registry.put("BACK_CHAINING_RULE_BODY", StepEntityResolver::resolveBackChainingRuleBody);

// Entity: FORWARD_CHAINING_RULE_PREMISE
      registry.put(
          "FORWARD_CHAINING_RULE_PREMISE",
          StepEntityResolver::resolveForwardChainingRulePremise);

// Entity: MAPPED_ITEM
      registry.put("MAPPED_ITEM", StepEntityResolver::resolveMappedItem);

// Entity: CARTESIAN_TRANSFORMATION_OPERATOR_2D
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_2D",
          (resolver, instance) -> resolver.transformationResolver.resolveCartesianTransformationOperator2D(instance));

// Entity: CARTESIAN_TRANSFORMATION_OPERATOR_3D
      registry.put(
          "CARTESIAN_TRANSFORMATION_OPERATOR_3D",
          (resolver, instance) -> resolver.transformationResolver.resolveCartesianTransformationOperator3D(instance));

// Entity: USER_DEFINED_MARKER
      registry.put("USER_DEFINED_MARKER", (resolver, instance) -> resolver.materialResolver.resolveUserDefinedMarker(instance));

// Entity: ITEM_DEFINED_TRANSFORMATION
      registry.put(
          "ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);

// Entity: DRAWING_SHEET_REVISION_SEQUENCE
      registry.put(
          "DRAWING_SHEET_REVISION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "DRAWING_SHEET_REVISION_SEQUENCE"));

// Entity: SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP"));

// Entity: TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION
      registry.put(
          "TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION"));

// Entity: GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION
      registry.put(
          "GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION"));

// Entity: A3M_EQUIVALENCE_ACCURACY_ASSOCIATION
      registry.put(
          "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceAccuracyAssociation(instance));

// Entity: A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP
      registry.put(
          "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveA3mInspectedModelAndInspectionResultRelationship(instance));

// Entity: A3MA_EQUIVALENCE_INSPECTION_RESULT
      registry.put(
          "A3MA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveA3maEquivalenceInspectionResult(instance));

// Entity: A3MS_EQUIVALENCE_INSPECTION_RESULT
      registry.put(
          "A3MS_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveA3msEquivalenceInspectionResult(instance));

// Entity: A3M_EQUIVALENCE_CRITERION
      registry.put(
          "A3M_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_COMPONENT_PROPERTY_DIFFERENCE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));

// Entity: A3M_EQUIVALENCE_CRITERION_FOR_SHAPE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_SHAPE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_SHAPE_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_SHAPE_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_SHAPE_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveA3mEquivalenceCriterion(instance));

// Entity: A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceAssessmentSpecification(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_LOGICAL_TEST"));

// Entity: A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST
      registry.put(
          "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceAssessmentSpecification(instance, "A3M_EQUIVALENCE_ASSESSMENT_BY_NUMERICAL_TEST"));

// Entity: A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceInspectionCriterionReportItem(instance, "A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE"));

// Entity: A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceInspectionInstanceReportItem(instance, "A3M_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));

// Entity: A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceInspectionRequirement(instance, "A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES"));

// Entity: A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE
      registry.put(
          "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE",
          (resolver, instance) ->
              resolver.resolveDataEquivalenceReportRequest(instance, "A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE"));

// Entity: A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP
      registry.put(
          "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationItemRelationship(instance, "A3MA_EQUIVALENCE_CRITERION_ASSESSMENT_THRESHOLD_RELATIONSHIP"));

// Entity: ABS_FUNCTION
      registry.put(
          "ABS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ABS_FUNCTION"));

// Entity: MINUS_FUNCTION
      registry.put(
          "MINUS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "MINUS_FUNCTION"));

// Entity: SIN_FUNCTION
      registry.put(
          "SIN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "SIN_FUNCTION"));

// Entity: COS_FUNCTION
      registry.put(
          "COS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "COS_FUNCTION"));

// Entity: TAN_FUNCTION
      registry.put(
          "TAN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "TAN_FUNCTION"));

// Entity: ASIN_FUNCTION
      registry.put(
          "ASIN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ASIN_FUNCTION"));

// Entity: ACOS_FUNCTION
      registry.put(
          "ACOS_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ACOS_FUNCTION"));

// Entity: ATAN_FUNCTION
      registry.put(
          "ATAN_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ATAN_FUNCTION"));

// Entity: EXP_FUNCTION
      registry.put(
          "EXP_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "EXP_FUNCTION"));

// Entity: LOG_FUNCTION
      registry.put(
          "LOG_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "LOG_FUNCTION"));

// Entity: LOG2_FUNCTION
      registry.put(
          "LOG2_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "LOG2_FUNCTION"));

// Entity: LOG10_FUNCTION
      registry.put(
          "LOG10_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "LOG10_FUNCTION"));

// Entity: SQUARE_ROOT_FUNCTION
      registry.put(
          "SQUARE_ROOT_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "SQUARE_ROOT_FUNCTION"));

// Entity: ODD_FUNCTION
      registry.put(
          "ODD_FUNCTION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "ODD_FUNCTION"));

// Entity: UNARY_FUNCTION_CALL
      registry.put(
          "UNARY_FUNCTION_CALL",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_FUNCTION_CALL"));

// Entity: UNARY_GENERIC_EXPRESSION
      registry.put(
          "UNARY_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_GENERIC_EXPRESSION"));

// Entity: UNARY_NUMERIC_EXPRESSION
      registry.put(
          "UNARY_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_NUMERIC_EXPRESSION"));

// Entity: NOT_EXPRESSION
      registry.put(
          "NOT_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "NOT_EXPRESSION"));

// Entity: BINARY_GENERIC_EXPRESSION
      registry.put(
          "BINARY_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_GENERIC_EXPRESSION"));

// Entity: BINARY_FUNCTION_CALL
      registry.put(
          "BINARY_FUNCTION_CALL",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_FUNCTION_CALL"));

// Entity: BINARY_NUMERIC_EXPRESSION
      registry.put(
          "BINARY_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_NUMERIC_EXPRESSION"));

// Entity: AND_EXPRESSION
      registry.put(
          "AND_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "AND_EXPRESSION"));

// Entity: OR_EXPRESSION
      registry.put(
          "OR_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "OR_EXPRESSION"));

// Entity: XOR_EXPRESSION
      registry.put(
          "XOR_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "XOR_EXPRESSION"));

// Entity: PLUS_EXPRESSION
      registry.put(
          "PLUS_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "PLUS_EXPRESSION"));

// Entity: MINUS_EXPRESSION
      registry.put(
          "MINUS_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "MINUS_EXPRESSION"));

// Entity: MULT_EXPRESSION
      registry.put(
          "MULT_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "MULT_EXPRESSION"));

// Entity: DIV_EXPRESSION
      registry.put(
          "DIV_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "DIV_EXPRESSION"));

// Entity: MOD_EXPRESSION
      registry.put(
          "MOD_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "MOD_EXPRESSION"));

// Entity: SLASH_EXPRESSION
      registry.put(
          "SLASH_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "SLASH_EXPRESSION"));

// Entity: POWER_EXPRESSION
      registry.put(
          "POWER_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "POWER_EXPRESSION"));

// Entity: COMPARISON_EXPRESSION
      registry.put(
          "COMPARISON_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "COMPARISON_EXPRESSION"));

// Entity: EQUALS_EXPRESSION
      registry.put(
          "EQUALS_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "EQUALS_EXPRESSION"));

// Entity: LIKE_EXPRESSION
      registry.put(
          "LIKE_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "LIKE_EXPRESSION"));

// Entity: CONCAT_EXPRESSION
      registry.put(
          "CONCAT_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "CONCAT_EXPRESSION"));

// Entity: MULTIPLE_ARITY_GENERIC_EXPRESSION
      registry.put(
          "MULTIPLE_ARITY_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_GENERIC_EXPRESSION"));

// Entity: MULTIPLE_ARITY_FUNCTION_CALL
      registry.put(
          "MULTIPLE_ARITY_FUNCTION_CALL",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_FUNCTION_CALL"));

// Entity: MULTIPLE_ARITY_NUMERIC_EXPRESSION
      registry.put(
          "MULTIPLE_ARITY_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_NUMERIC_EXPRESSION"));

// Entity: SIMPLE_GENERIC_EXPRESSION
      registry.put(
          "SIMPLE_GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_GENERIC_EXPRESSION"));

// Entity: SIMPLE_NUMERIC_EXPRESSION
      registry.put(
          "SIMPLE_NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_NUMERIC_EXPRESSION"));

// Entity: SIMPLE_STRING_EXPRESSION
      registry.put(
          "SIMPLE_STRING_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_STRING_EXPRESSION"));

// Entity: GENERIC_EXPRESSION
      registry.put(
          "GENERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "GENERIC_EXPRESSION"));

// Entity: NUMERIC_EXPRESSION
      registry.put(
          "NUMERIC_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "NUMERIC_EXPRESSION"));

// Entity: STRING_EXPRESSION
      registry.put(
          "STRING_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "STRING_EXPRESSION"));

// Entity: INDEX_EXPRESSION
      registry.put(
          "INDEX_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "INDEX_EXPRESSION"));

// Entity: SUBSTRING_EXPRESSION
      registry.put(
          "SUBSTRING_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "SUBSTRING_EXPRESSION"));

// Entity: INTERVAL_EXPRESSION
      registry.put(
          "INTERVAL_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "INTERVAL_EXPRESSION"));

  }
}
