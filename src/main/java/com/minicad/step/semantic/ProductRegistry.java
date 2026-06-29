package com.minicad.step.semantic;

import com.minicad.step.model.geometry.StepAxis2Placement3D;

import java.util.Map;

/**
 * Registry for product entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class ProductRegistry {

  private ProductRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: ADVANCED_BREP_SHAPE_REPRESENTATION
      registry.put(
          "ADVANCED_BREP_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "ADVANCED_BREP_SHAPE_REPRESENTATION", true));

// Entity: ELEMENTARY_BREP_SHAPE_REPRESENTATION
      registry.put(
          "ELEMENTARY_BREP_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "ELEMENTARY_BREP_SHAPE_REPRESENTATION", true));

// Entity: BLOCK_SHAPE_REPRESENTATION
      registry.put(
          "BLOCK_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "BLOCK_SHAPE_REPRESENTATION", true));

// Entity: CSG_SHAPE_REPRESENTATION
      registry.put(
          "CSG_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CSG_SHAPE_REPRESENTATION", true));

// Entity: CSG_2D_SHAPE_REPRESENTATION
      registry.put(
          "CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "CSG_2D_SHAPE_REPRESENTATION", true));

// Entity: SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION
      registry.put(
          "SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION", true));

// Entity: BOOLEAN_CLIPPING_RESULT
      registry.put("BOOLEAN_CLIPPING_RESULT", StepEntityResolver::resolveBooleanClippingResult);

// Entity: BOOLEAN_RESULT
      registry.put("BOOLEAN_RESULT", StepEntityResolver::resolveBooleanResult);

// Entity: CSG_SOLID
      registry.put("CSG_SOLID", StepEntityResolver::resolveCsgSolid);

// Entity: CSG_VOLUME
      registry.put("CSG_VOLUME", StepEntityResolver::resolveCsgVolume);

// Entity: BLOCK_VOLUME
      registry.put("BLOCK_VOLUME", StepEntityResolver::resolveBlockVolume);

// Entity: SPHERE_VOLUME
      registry.put("SPHERE_VOLUME", StepEntityResolver::resolveSphereVolume);

// Entity: PRISM_VOLUME
      registry.put("PRISM_VOLUME", StepEntityResolver::resolvePrismVolume);

// Entity: RIGHT_CIRCULAR_CONE_VOLUME
      registry.put("RIGHT_CIRCULAR_CONE_VOLUME", StepEntityResolver::resolveRightCircularConeVolume);

// Entity: SOLID_REPLICA
      registry.put("SOLID_REPLICA", StepEntityResolver::resolveSolidReplica);

// Entity: BLOCK
      registry.put(
          "BLOCK",
          (resolver, instance) ->
              resolver.resolveCsgPrimitive(
                  instance, "BLOCK", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));

// Entity: EXTRUDED_AREA_SOLID
      registry.put("EXTRUDED_AREA_SOLID", StepEntityResolver::resolveExtrudedAreaSolid);

// Entity: REVOLVED_AREA_SOLID
      registry.put("REVOLVED_AREA_SOLID", StepEntityResolver::resolveRevolvedAreaSolid);

// Entity: HALF_SPACE_SOLID
      registry.put("HALF_SPACE_SOLID", StepEntityResolver::resolveHalfSpaceSolid);

// Entity: BOXED_HALF_SPACE
      registry.put("BOXED_HALF_SPACE", StepEntityResolver::resolveBoxedHalfSpace);

// Entity: SWEPT_DISK_SOLID
      registry.put("SWEPT_DISK_SOLID", StepEntityResolver::resolveSweptDiskSolid);

// Entity: REVOLVED_AREA_SOLID_TAPERED
      registry.put("REVOLVED_AREA_SOLID_TAPERED", StepEntityResolver::resolveRevolvedAreaSolidTapered);

// Entity: EXTRUDED_AREA_SOLID_TAPERED
      registry.put("EXTRUDED_AREA_SOLID_TAPERED", StepEntityResolver::resolveExtrudedAreaSolidTapered);

// Entity: BREP_WITH_VOIDS
      registry.put("BREP_WITH_VOIDS", StepEntityResolver::resolveBrepWithVoids);

// Entity: ADVANCED_BREP
      registry.put("ADVANCED_BREP", StepEntityResolver::resolveAdvancedBrep);

// Entity: A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY
      registry.put(
          "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY"));

// Entity: PRODUCT_DEFINITION_FORMATION
      registry.put(
          "PRODUCT_DEFINITION_FORMATION", StepEntityResolver::resolveProductDefinitionFormation);

// Entity: PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE",
          (resolver, instance) -> resolver.resolveProductDefinitionFormation(instance));

// Entity: PRODUCT_DEFINITION_FORMATION_RELATIONSHIP
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP",
          StepEntityResolver::resolveProductDefinitionFormationRelationship);

// Entity: PRODUCT_DEFINITION_CONTEXT
      registry.put("PRODUCT_DEFINITION_CONTEXT", StepEntityResolver::resolveProductDefinitionContext);

// Entity: PRODUCT_DEFINITION
      registry.put("PRODUCT_DEFINITION", StepEntityResolver::resolveProductDefinition);

// Entity: PRODUCT_DEFINITION_RELATIONSHIP
      registry.put("PRODUCT_DEFINITION_RELATIONSHIP", StepEntityResolver::resolveProductDefinitionRelationship);

// Entity: PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP
      registry.put("PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP", StepEntityResolver::resolveProductDefinitionRelationshipRelationship);


// Entity: PRODUCT_DEFINITION_USAGE_RELATIONSHIP
      registry.put(
          "PRODUCT_DEFINITION_USAGE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationshipRelationship(
                  instance, "PRODUCT_DEFINITION_USAGE_RELATIONSHIP"));

// Entity: PRODUCT_DEFINITION_SHAPE
      registry.put("PRODUCT_DEFINITION_SHAPE", StepEntityResolver::resolveProductDefinitionShape);

// Entity: PRODUCT_DEFINITION_EFFECTIVITY
      registry.put(
          "PRODUCT_DEFINITION_EFFECTIVITY",
          StepEntityResolver::resolveProductDefinitionEffectivity);

// Entity: COAXIAL_ASSEMBLY_CONSTRAINT
      registry.put(
          "COAXIAL_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "COAXIAL_ASSEMBLY_CONSTRAINT"));

// Entity: PARALLEL_ASSEMBLY_CONSTRAINT
      registry.put(
          "PARALLEL_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PARALLEL_ASSEMBLY_CONSTRAINT"));

// Entity: PERPENDICULAR_ASSEMBLY_CONSTRAINT
      registry.put(
          "PERPENDICULAR_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PERPENDICULAR_ASSEMBLY_CONSTRAINT"));

// Entity: INCIDENCE_ASSEMBLY_CONSTRAINT
      registry.put(
          "INCIDENCE_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "INCIDENCE_ASSEMBLY_CONSTRAINT"));

// Entity: TANGENT_ASSEMBLY_CONSTRAINT
      registry.put(
          "TANGENT_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "TANGENT_ASSEMBLY_CONSTRAINT"));

// Entity: COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION
      registry.put(
          "COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));

// Entity: PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION
      registry.put(
          "PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));

// Entity: PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION
      registry.put(
          "PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));

// Entity: INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION
      registry.put(
          "INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));

// Entity: NEXT_ASSEMBLY_USAGE_OCCURRENCE
      registry.put("NEXT_ASSEMBLY_USAGE_OCCURRENCE", StepEntityResolver::resolveNextAssemblyUsageOccurrence);

// Entity: SOLID_ANGLE_MEASURE_WITH_UNIT
      registry.put(
          "SOLID_ANGLE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "SOLID_ANGLE_MEASURE_WITH_UNIT", "SOLID_ANGLE_UNIT"));

// Entity: VOLUME_MEASURE_WITH_UNIT
      registry.put(
          "VOLUME_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "VOLUME_MEASURE_WITH_UNIT", "VOLUME_UNIT"));

// Entity: SOLID_ANGLE_UNIT
      registry.put(
          "SOLID_ANGLE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "SOLID_ANGLE_UNIT"));

// Entity: VOLUME_UNIT
      registry.put(
          "VOLUME_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "VOLUME_UNIT"));

// Entity: A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_FOR_ASSEMBLY"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_ASSEMBLY_DATA_STRUCTURE"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_DETAILED_ASSEMBLY_DATA_CONTENT"));

// Entity: A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE
      registry.put(
          "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3M_EQUIVALENCE_CRITERION_OF_REPRESENTATIVE_ASSEMBLY_PROPERTY_VALUE"));

// Entity: A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP
      registry.put(
          "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "A3MA_ASSEMBLY_AND_SHAPE_CRITERIA_RELATIONSHIP"));

// Entity: UNARY_BOOLEAN_EXPRESSION
      registry.put(
          "UNARY_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveUnaryGenericExpression(instance, "UNARY_BOOLEAN_EXPRESSION"));

// Entity: BINARY_BOOLEAN_EXPRESSION
      registry.put(
          "BINARY_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveBinaryGenericExpression(instance, "BINARY_BOOLEAN_EXPRESSION"));

// Entity: MULTIPLE_ARITY_BOOLEAN_EXPRESSION
      registry.put(
          "MULTIPLE_ARITY_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveMultipleArityGenericExpression(instance, "MULTIPLE_ARITY_BOOLEAN_EXPRESSION"));

// Entity: SIMPLE_BOOLEAN_EXPRESSION
      registry.put(
          "SIMPLE_BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "SIMPLE_BOOLEAN_EXPRESSION"));

// Entity: BOOLEAN_EXPRESSION
      registry.put(
          "BOOLEAN_EXPRESSION",
          (resolver, instance) ->
              resolver.resolveSimpleGenericExpression(instance, "BOOLEAN_EXPRESSION"));

// Entity: DIFFERENT_ASSEMBLY_CONSTRAINT_TYPE
      registry.put(
          "DIFFERENT_ASSEMBLY_CONSTRAINT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "DIFFERENT_ASSEMBLY_CONSTRAINT_TYPE"));

// Entity: DISALLOWED_ASSEMBLY_RELATIONSHIP_USAGE
      registry.put(
          "DISALLOWED_ASSEMBLY_RELATIONSHIP_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DISALLOWED_ASSEMBLY_RELATIONSHIP_USAGE"));

// Entity: PRODUCT_DEFINITION_CONTEXT_ROLE
      registry.put(
          "PRODUCT_DEFINITION_CONTEXT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "PRODUCT_DEFINITION_CONTEXT_ROLE"));

// Entity: PRODUCT_DEFINITION_ELEMENT_RELATIONSHIP
      registry.put(
          "PRODUCT_DEFINITION_ELEMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DEFINITION_ELEMENT_RELATIONSHIP"));

// Entity: PRODUCT_DEFINITION_GROUP_ASSIGNMENT
      registry.put(
          "PRODUCT_DEFINITION_GROUP_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_GROUP_ASSIGNMENT"));

// Entity: PRODUCT_DEFINITION_OCCURRENCE_RELATIONSHIP
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DEFINITION_OCCURRENCE_RELATIONSHIP"));

// Entity: PRODUCT_DEFINITION_RELATIONSHIP_KINEMATICS
      registry.put(
          "PRODUCT_DEFINITION_RELATIONSHIP_KINEMATICS",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DEFINITION_RELATIONSHIP_KINEMATICS"));

// Entity: ASSEMBLY_BOND_DEFINITION
      registry.put(
          "ASSEMBLY_BOND_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_BOND_DEFINITION"));

// Entity: COMPOSITE_ASSEMBLY_SEQUENCE_DEFINITION
      registry.put(
          "COMPOSITE_ASSEMBLY_SEQUENCE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COMPOSITE_ASSEMBLY_SEQUENCE_DEFINITION"));

// Entity: COUNTERBORE_HOLE_OCCURRENCE_IN_ASSEMBLY
      registry.put(
          "COUNTERBORE_HOLE_OCCURRENCE_IN_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COUNTERBORE_HOLE_OCCURRENCE_IN_ASSEMBLY"));

// Entity: COUNTERDRILL_HOLE_OCCURRENCE_IN_ASSEMBLY
      registry.put(
          "COUNTERDRILL_HOLE_OCCURRENCE_IN_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COUNTERDRILL_HOLE_OCCURRENCE_IN_ASSEMBLY"));

// Entity: COUNTERSINK_HOLE_OCCURRENCE_IN_ASSEMBLY
      registry.put(
          "COUNTERSINK_HOLE_OCCURRENCE_IN_ASSEMBLY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COUNTERSINK_HOLE_OCCURRENCE_IN_ASSEMBLY"));

// Entity: DEFINITIONAL_PRODUCT_DEFINITION_USAGE
      registry.put(
          "DEFINITIONAL_PRODUCT_DEFINITION_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DEFINITIONAL_PRODUCT_DEFINITION_USAGE"));

// Entity: EVALUATION_PRODUCT_DEFINITION
      registry.put(
          "EVALUATION_PRODUCT_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EVALUATION_PRODUCT_DEFINITION"));

// Entity: GENERIC_PRODUCT_DEFINITION_REFERENCE
      registry.put(
          "GENERIC_PRODUCT_DEFINITION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERIC_PRODUCT_DEFINITION_REFERENCE"));

// Entity: PRODUCT_DEFINITION_FORMATION_RESOURCE
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_RESOURCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_FORMATION_RESOURCE"));

// Entity: PRODUCT_DEFINITION_KINEMATICS
      registry.put(
          "PRODUCT_DEFINITION_KINEMATICS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_KINEMATICS"));

// Entity: PRODUCT_DEFINITION_OCCURRENCE
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_OCCURRENCE"));

// Entity: PRODUCT_DEFINITION_OCCURRENCE_REFERENCE
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE"));

// Entity: PRODUCT_DEFINITION_PROCESS
      registry.put(
          "PRODUCT_DEFINITION_PROCESS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_PROCESS"));

// Entity: PRODUCT_DEFINITION_REFERENCE
      registry.put(
          "PRODUCT_DEFINITION_REFERENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_REFERENCE"));

// Entity: PRODUCT_DEFINITION_SPECIFIED_OCCURRENCE
      registry.put(
          "PRODUCT_DEFINITION_SPECIFIED_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_DEFINITION_SPECIFIED_OCCURRENCE"));

// Entity: SOLID_WITH_SHAPE_ELEMENT_PATTERN
      registry.put(
          "SOLID_WITH_SHAPE_ELEMENT_PATTERN",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_SHAPE_ELEMENT_PATTERN"));

// Entity: ASSEMBLY_GEOMETRIC_CONSTRAINT
      registry.put(
          "ASSEMBLY_GEOMETRIC_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_GEOMETRIC_CONSTRAINT"));

// Entity: CYLINDRICAL_VOLUME
      registry.put(
          "CYLINDRICAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_VOLUME"));

// Entity: SOLID_WITH_DOUBLE_OFFSET_CHAMFER
      registry.put(
          "SOLID_WITH_DOUBLE_OFFSET_CHAMFER",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_DOUBLE_OFFSET_CHAMFER"));

// Entity: SOLID_WITH_SINGLE_OFFSET_CHAMFER
      registry.put(
          "SOLID_WITH_SINGLE_OFFSET_CHAMFER",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SOLID_WITH_SINGLE_OFFSET_CHAMFER"));

// Entity: TOROIDAL_VOLUME
      registry.put(
          "TOROIDAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "TOROIDAL_VOLUME"));

// Entity: ASSEMBLY_JOINT
      registry.put(
          "ASSEMBLY_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_JOINT"));

// Entity: ASSEMBLY_SHAPE_JOINT
      registry.put(
          "ASSEMBLY_SHAPE_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ASSEMBLY_SHAPE_JOINT"));

// Entity: CONNECTED_VOLUME_SET
      registry.put(
          "CONNECTED_VOLUME_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTED_VOLUME_SET"));

// Entity: CONNECTED_VOLUME_SUB_SET
      registry.put(
          "CONNECTED_VOLUME_SUB_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTED_VOLUME_SUB_SET"));

// Entity: CONNECTION_ZONE_BASED_ASSEMBLY_JOINT
      registry.put(
          "CONNECTION_ZONE_BASED_ASSEMBLY_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTION_ZONE_BASED_ASSEMBLY_JOINT"));

// Entity: CSG_PRIMITIVE_SOLID_2D
      registry.put(
          "CSG_PRIMITIVE_SOLID_2D",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CSG_PRIMITIVE_SOLID_2D"));

// Entity: CSG_SOLID_2D
      registry.put(
          "CSG_SOLID_2D",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CSG_SOLID_2D"));

// Entity: CYCLIDE_SEGMENT_SOLID
      registry.put(
          "CYCLIDE_SEGMENT_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYCLIDE_SEGMENT_SOLID"));

// Entity: DIFFERENT_ASSEMBLY_CENTROID_USING_NOTIONAL_SOLID
      registry.put(
          "DIFFERENT_ASSEMBLY_CENTROID_USING_NOTIONAL_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_ASSEMBLY_CENTROID_USING_NOTIONAL_SOLID"));

// Entity: DIFFERENT_ASSEMBLY_VOLUME
      registry.put(
          "DIFFERENT_ASSEMBLY_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_ASSEMBLY_VOLUME"));

// Entity: DIFFERENT_VOLUME
      registry.put(
          "DIFFERENT_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DIFFERENT_VOLUME"));

// Entity: ELLIPSOID_VOLUME
      registry.put(
          "ELLIPSOID_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ELLIPSOID_VOLUME"));

// Entity: ENTIRELY_NARROW_SOLID
      registry.put(
          "ENTIRELY_NARROW_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ENTIRELY_NARROW_SOLID"));

// Entity: ERRONEOUS_MANIFOLD_SOLID_BREP
      registry.put(
          "ERRONEOUS_MANIFOLD_SOLID_BREP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ERRONEOUS_MANIFOLD_SOLID_BREP"));

// Entity: HEXAHEDRON_VOLUME
      registry.put(
          "HEXAHEDRON_VOLUME",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HEXAHEDRON_VOLUME"));

// Entity: IMPORTED_VOLUME_FUNCTION
      registry.put(
          "IMPORTED_VOLUME_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "IMPORTED_VOLUME_FUNCTION"));

// Entity: INAPT_MANIFOLD_SOLID_BREP
      registry.put(
          "INAPT_MANIFOLD_SOLID_BREP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INAPT_MANIFOLD_SOLID_BREP"));

// Entity: MODIFIED_SOLID
      registry.put(
          "MODIFIED_SOLID",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODIFIED_SOLID"));

// Entity: MODIFIED_SOLID_WITH_PLACED_CONFIGURATION
      registry.put(
          "MODIFIED_SOLID_WITH_PLACED_CONFIGURATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MODIFIED_SOLID_WITH_PLACED_CONFIGURATION"));

// Entity: MULTIPLY_DEFINED_SOLIDS
      registry.put(
          "MULTIPLY_DEFINED_SOLIDS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MULTIPLY_DEFINED_SOLIDS"));

// Entity: BOOLEAN_REPRESENTATION_ITEM
      registry.put(
          "BOOLEAN_REPRESENTATION_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_REPRESENTATION_ITEM"));

// Entity: SOLID_WITH_TEE_SECTION_SLOT
      registry.put(
          "SOLID_WITH_TEE_SECTION_SLOT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SOLID_WITH_TEE_SECTION_SLOT"));

// Entity: SOLID_WITH_TRAPEZOIDAL_SECTION_SLOT
      registry.put(
          "SOLID_WITH_TRAPEZOIDAL_SECTION_SLOT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SOLID_WITH_TRAPEZOIDAL_SECTION_SLOT"));

// Entity: ANGLE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION
      registry.put(
          "ANGLE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGLE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));

// Entity: ASSEMBLY_COMPONENT
      registry.put(
          "ASSEMBLY_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSEMBLY_COMPONENT"));

// Entity: ASSEMBLY_GROUP_COMPONENT
      registry.put(
          "ASSEMBLY_GROUP_COMPONENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSEMBLY_GROUP_COMPONENT"));

// Entity: ASSEMBLY_SHAPE_CONSTRAINT
      registry.put(
          "ASSEMBLY_SHAPE_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ASSEMBLY_SHAPE_CONSTRAINT"));

// Entity: PROCEDURAL_SOLID_REPRESENTATION_SEQUENCE
      registry.put(
          "PROCEDURAL_SOLID_REPRESENTATION_SEQUENCE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCEDURAL_SOLID_REPRESENTATION_SEQUENCE"));

// Entity: PRODUCT_DEFINITION_OCCURRENCE_REFERENCE_WITH_LOCAL_REPRESENTATION
      registry.put(
          "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE_WITH_LOCAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_OCCURRENCE_REFERENCE_WITH_LOCAL_REPRESENTATION"));

// Entity: PRODUCT_DEFINITION_REFERENCE_WITH_LOCAL_REPRESENTATION
      registry.put(
          "PRODUCT_DEFINITION_REFERENCE_WITH_LOCAL_REPRESENTATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_DEFINITION_REFERENCE_WITH_LOCAL_REPRESENTATION"));

// Entity: BINARY_ASSEMBLY_CONSTRAINT
      registry.put(
          "BINARY_ASSEMBLY_CONSTRAINT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BINARY_ASSEMBLY_CONSTRAINT"));

// Entity: BOOLEAN_DEFINED_FUNCTION
      registry.put(
          "BOOLEAN_DEFINED_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_DEFINED_FUNCTION"));

// Entity: BOOLEAN_LITERAL
      registry.put(
          "BOOLEAN_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_LITERAL"));

// Entity: BOOLEAN_RESULT_2D
      registry.put(
          "BOOLEAN_RESULT_2D",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_RESULT_2D"));

// Entity: BOOLEAN_VARIABLE
      registry.put(
          "BOOLEAN_VARIABLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "BOOLEAN_VARIABLE"));

// Entity: COMPOSITE_ASSEMBLY_TABLE
      registry.put(
          "COMPOSITE_ASSEMBLY_TABLE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_ASSEMBLY_TABLE"));

// Entity: MANIFOLD_SOLID_BREP
      registry.put("MANIFOLD_SOLID_BREP", StepEntityResolver::resolveManifoldSolidBrep);

// Entity: NON_MANIFOLD_SOLID_BREP
      registry.put("NON_MANIFOLD_SOLID_BREP", StepEntityResolver::resolveNonManifoldSolidBrep);

// Entity: QUANTIFIED_ASSEMBLY_COMPONENT_USAGE
      registry.put(
          "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE"));

// Entity: PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS
      registry.put(
          "PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS",
          (resolver, instance) -> resolver.resolveProductDefinition(instance));

// Entity: MAKE_FROM_BUILD_ASSEMBLY
      registry.put("MAKE_FROM_BUILD_ASSEMBLY", StepEntityResolver::resolveMakeFromBuildAssembly);

// Entity: ASSEMBLY_COMPONENT_RELATIONSHIP
      registry.put("ASSEMBLY_COMPONENT_RELATIONSHIP", StepEntityResolver::resolveAssemblyComponentRelationship);

// Entity: VIEW_VOLUME
      registry.put("VIEW_VOLUME", StepEntityResolver::resolveViewVolume);

// Entity: SOLID_MODEL
      registry.put("SOLID_MODEL", StepEntityResolver::resolveSolidModel);

// Entity: VOLUME_3D_ELEMENT_REPRESENTATION
      registry.put("VOLUME_3D_ELEMENT_REPRESENTATION",
          StepEntityResolver::resolveVolume3dElementRepresentation);

// Entity: VOLUME_3D_ELEMENT_PROPERTY
      registry.put("VOLUME_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveVolume3dElementProperty);

// Entity: ELEMENT_VOLUME_2D
      registry.put("ELEMENT_VOLUME_2D", StepEntityResolver::resolveElementVolume2d);

// Entity: ELEMENT_VOLUME_3D
      registry.put("ELEMENT_VOLUME_3D", StepEntityResolver::resolveElementVolume3d);

// Entity: ELEMENT_VOLUME
      registry.put("ELEMENT_VOLUME", StepEntityResolver::resolveElementVolume);

// Entity: VOLUME_ELEMENT
      registry.put("VOLUME_ELEMENT", StepEntityResolver::resolveVolumeElement);

// Entity: UNIFORM_VOLUME_ELEMENT
      registry.put("UNIFORM_VOLUME_ELEMENT", StepEntityResolver::resolveUniformVolumeElement);

// Entity: SWEPT_AREA_SOLID
      registry.put(
          "SWEPT_AREA_SOLID",
          (resolver, instance) -> resolver.resolveSweptAreaSolid(instance, "SWEPT_AREA_SOLID"));

// Entity: SWEPT_VOLUME_SOLID
      registry.put(
          "SWEPT_VOLUME_SOLID",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: AREA_SOLID
      registry.put(
          "AREA_SOLID",
          (resolver, instance) -> resolver.resolveSweptAreaSolid(instance, "AREA_SOLID"));

// Entity: VOID_SOLID
      registry.put(
          "VOID_SOLID",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: BREP
      registry.put(
          "BREP",
          (resolver, instance) -> resolver.resolveManifoldSolidBrep(instance, "BREP"));

// Entity: SOLID_SET
      registry.put(
          "SOLID_SET",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: PRODUCT_DEFINITION_SHAPE_WITH_ASSOCIATED_ITEMS
      registry.put(
          "PRODUCT_DEFINITION_SHAPE_WITH_ASSOCIATED_ITEMS",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: PRODUCT_DEFINITION_CONTEXT_ASSOCIATION
      registry.put(
          "PRODUCT_DEFINITION_CONTEXT_ASSOCIATION",
          (resolver, instance) -> resolver.resolveProductDefinition(instance));

// Entity: PRODUCT_DEFINITION_FORMATION_SPECIAL_USAGE
      registry.put(
          "PRODUCT_DEFINITION_FORMATION_SPECIAL_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionFormation(instance));

// Entity: PRODUCT_DEFINITION_RESOURCE
      registry.put(
          "PRODUCT_DEFINITION_RESOURCE",
          (resolver, instance) -> resolver.resolveProductDefinition(instance));

// Entity: PRODUCT_DEFINITION_USAGE
      registry.put(
          "PRODUCT_DEFINITION_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PRODUCT_DEFINITION_USAGE"));

// Entity: ASSEMBLY_COMPONENT_USAGE
      registry.put(
          "ASSEMBLY_COMPONENT_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_COMPONENT_USAGE"));

// Entity: ASSEMBLY_DEFINITION_USAGE
      registry.put(
          "ASSEMBLY_DEFINITION_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_DEFINITION_USAGE"));

// Entity: VOLUME_ELEMENT_FREEDOM
      registry.put(
          "VOLUME_ELEMENT_FREEDOM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: VOLUME_ELEMENT_FREEDOM_VALUE
      registry.put(
          "VOLUME_ELEMENT_FREEDOM_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ASSEMBLY_SHAPE_REPRESENTATION
      registry.put(
          "ASSEMBLY_SHAPE_REPRESENTATION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SHAPE_REPRESENTATION", true));

// Entity: ASSEMBLY_SHAPE_REPRESENTATION_PREDEFINED
      registry.put(
          "ASSEMBLY_SHAPE_REPRESENTATION_PREDEFINED",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SHAPE_REPRESENTATION_PREDEFINED", true));

// Entity: ASSEMBLY_COMPONENT_STRUCTURE
      registry.put(
          "ASSEMBLY_COMPONENT_STRUCTURE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_COMPONENT_STRUCTURE"));

// Entity: ASSEMBLY_SEQUENCE_DEFINITION
      registry.put(
          "ASSEMBLY_SEQUENCE_DEFINITION",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SEQUENCE_DEFINITION", false));

// Entity: ASSEMBLY_SEQUENCE
      registry.put("ASSEMBLY_SEQUENCE", StepEntityResolver::resolveAssemblySequence);

// Entity: ASSEMBLY_STEP
      registry.put(
          "ASSEMBLY_STEP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: CSG_PRIMITIVE
      registry.put("CSG_PRIMITIVE", (resolver, instance) ->
          resolver.resolveCsgPrimitive(instance, "CSG_PRIMITIVE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));

// Entity: FEA_VOLUME_ELEMENT_PROPERTY
      registry.put("FEA_VOLUME_ELEMENT_PROPERTY", StepEntityResolver::resolveFeaVolumeElementProperty);

// Entity: VOLUME_UNIT_WITH_UNIT
      registry.put("VOLUME_UNIT_WITH_UNIT", StepEntityResolver::resolveVolumeUnitWithUnit);

// Entity: CSG_PRIMITIVE_3D
      registry.put("CSG_PRIMITIVE_3D", StepEntityResolver::resolveCsgPrimitive3D);

// Entity: ASSEMBLY_OPERATION
      registry.put("ASSEMBLY_OPERATION", StepEntityResolver::resolveAssemblyOperation);

// Entity: ASSEMBLY_STRUCTURE
      registry.put("ASSEMBLY_STRUCTURE", StepEntityResolver::resolveAssemblyStructure);

  }
}
