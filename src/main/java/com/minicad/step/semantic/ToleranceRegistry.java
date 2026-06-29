package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for tolerance entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class ToleranceRegistry {

  private ToleranceRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: DEFAULT_TOLERANCE_TABLE
      registry.put(
          "DEFAULT_TOLERANCE_TABLE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "DEFAULT_TOLERANCE_TABLE", false));

// Entity: DATUM
      registry.put("DATUM", StepEntityResolver::resolveDatum);

// Entity: DATUM_FEATURE
      registry.put("DATUM_FEATURE", StepEntityResolver::resolveDatumFeature);

// Entity: DATUM_TARGET
      registry.put("DATUM_TARGET", StepEntityResolver::resolveDatumTarget);

// Entity: DIMENSIONAL_LOCATION
      registry.put("DIMENSIONAL_LOCATION", StepEntityResolver::resolveDimensionalLocation);

// Entity: DIMENSIONAL_SIZE
      registry.put("DIMENSIONAL_SIZE", StepEntityResolver::resolveDimensionalSize);

// Entity: DIRECTED_DIMENSIONAL_LOCATION
      registry.put(
          "DIRECTED_DIMENSIONAL_LOCATION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "DIRECTED_DIMENSIONAL_LOCATION"));

// Entity: FEATURE_FOR_DATUM_TARGET_RELATIONSHIP
      registry.put(
          "FEATURE_FOR_DATUM_TARGET_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "FEATURE_FOR_DATUM_TARGET_RELATIONSHIP"));

// Entity: PLACED_DATUM_TARGET_FEATURE
      registry.put(
          "PLACED_DATUM_TARGET_FEATURE",
          StepEntityResolver::resolvePlacedDatumTargetFeature);

// Entity: ANGULARITY_TOLERANCE_WITH_MODIFIERS
      registry.put(
          "ANGULARITY_TOLERANCE_WITH_MODIFIERS",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "ANGULARITY_TOLERANCE_WITH_MODIFIERS"));

// Entity: DIMENSIONAL_EXPONENTS
      registry.put("DIMENSIONAL_EXPONENTS", StepEntityResolver::resolveDimensionalExponents);

// Entity: GEOMETRIC_TOLERANCE_RELATIONSHIP
      registry.put(
          "GEOMETRIC_TOLERANCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "GEOMETRIC_TOLERANCE_RELATIONSHIP"));

// Entity: PROJECTED_ZONE_DEFINITION_WITH_OFFSET
      registry.put(
          "PROJECTED_ZONE_DEFINITION_WITH_OFFSET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROJECTED_ZONE_DEFINITION_WITH_OFFSET"));

// Entity: ZONE_ELEMENT_USAGE
      registry.put(
          "ZONE_ELEMENT_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ZONE_ELEMENT_USAGE"));

// Entity: IN_ZONE
      registry.put(
          "IN_ZONE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IN_ZONE"));

// Entity: RUNOUT_ZONE_ORIENTATION
      registry.put(
          "RUNOUT_ZONE_ORIENTATION",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "RUNOUT_ZONE_ORIENTATION"));

// Entity: ZONE_STRUCTURAL_MAKEUP
      registry.put(
          "ZONE_STRUCTURAL_MAKEUP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ZONE_STRUCTURAL_MAKEUP"));

// Entity: DIMENSIONAL_SIZE_WITH_DATUM_FEATURE
      registry.put(
          "DIMENSIONAL_SIZE_WITH_DATUM_FEATURE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSIONAL_SIZE_WITH_DATUM_FEATURE"));

// Entity: DIMENSION_RELATED_TOLERANCE_ZONE_ELEMENT
      registry.put(
          "DIMENSION_RELATED_TOLERANCE_ZONE_ELEMENT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIMENSION_RELATED_TOLERANCE_ZONE_ELEMENT"));

// Entity: DIRECTED_TOLERANCE_ZONE
      registry.put(
          "DIRECTED_TOLERANCE_ZONE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DIRECTED_TOLERANCE_ZONE"));

// Entity: ANGULARITY_TOLERANCE
      registry.put(
          "ANGULARITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ANGULARITY_TOLERANCE"));

// Entity: GEOMETRIC_TOLERANCE_AUXILIARY_CLASSIFICATION
      registry.put(
          "GEOMETRIC_TOLERANCE_AUXILIARY_CLASSIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GEOMETRIC_TOLERANCE_AUXILIARY_CLASSIFICATION"));

// Entity: ORIENTED_TOLERANCE_ZONE
      registry.put(
          "ORIENTED_TOLERANCE_ZONE",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ORIENTED_TOLERANCE_ZONE"));

// Entity: TOLERANCE_ZONE_WITH_DATUM
      registry.put(
          "TOLERANCE_ZONE_WITH_DATUM",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TOLERANCE_ZONE_WITH_DATUM"));

// Entity: MODIFIED_GEOMETRIC_TOLERANCE
      registry.put(
          "MODIFIED_GEOMETRIC_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "MODIFIED_GEOMETRIC_TOLERANCE"));

// Entity: CIRCULAR_RUNOUT_TOLERANCE
      registry.put(
          "CIRCULAR_RUNOUT_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CIRCULAR_RUNOUT_TOLERANCE"));

// Entity: COAXIALITY_TOLERANCE
      registry.put(
          "COAXIALITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COAXIALITY_TOLERANCE"));

// Entity: CONCENTRICITY_TOLERANCE
      registry.put(
          "CONCENTRICITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONCENTRICITY_TOLERANCE"));

// Entity: CYLINDRICITY_TOLERANCE
      registry.put(
          "CYLINDRICITY_TOLERANCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CYLINDRICITY_TOLERANCE"));

// Entity: GEOMETRIC_TOLERANCE
      registry.put("GEOMETRIC_TOLERANCE",
          (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE"));

// Entity: GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT
      registry.put(
          "GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT",
          (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT"));

// Entity: DATUM_REFERENCE_COMPARTMENT
      registry.put("DATUM_REFERENCE_COMPARTMENT", StepEntityResolver::resolveDatumReferenceCompartment);

// Entity: DATUM_REFERENCE_ELEMENT
      registry.put(
          "DATUM_REFERENCE_ELEMENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_REFERENCE_ELEMENT"));

// Entity: COMMON_DATUM
      registry.put(
          "COMMON_DATUM",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMMON_DATUM"));

// Entity: TOLERANCE_ZONE_FORM
      registry.put("TOLERANCE_ZONE_FORM", StepEntityResolver::resolveToleranceZoneForm);

// Entity: TOLERANCE_ZONE
      registry.put("TOLERANCE_ZONE", StepEntityResolver::resolveToleranceZone);

// Entity: FEATURE_CONTROL_FRAME
      registry.put("FEATURE_CONTROL_FRAME", StepEntityResolver::resolveFeatureControlFrame);

// Entity: RUNOUT_TOLERANCE_ZONE
      registry.put("RUNOUT_TOLERANCE_ZONE", StepEntityResolver::resolveRunoutToleranceZone);

// Entity: GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE
      registry.put(
          "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE",
          (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE"));

// Entity: RADIAL_TOLERANCE_ZONE
      registry.put("RADIAL_TOLERANCE_ZONE", StepEntityResolver::resolveRadialToleranceZone);

// Entity: PROJECTED_ZONE_DEFINITION
      registry.put("PROJECTED_ZONE_DEFINITION", StepEntityResolver::resolveProjectedZoneDefinition);

// Entity: PLUS_MINUS_TOLERANCE_WITH_MODIFIERS
      registry.put("PLUS_MINUS_TOLERANCE_WITH_MODIFIERS", StepEntityResolver::resolvePlusMinusToleranceWithModifiers);

// Entity: DIRECTED_DIMENSIONAL_SIZE
      registry.put("DIRECTED_DIMENSIONAL_SIZE", StepEntityResolver::resolveDirectedDimensionalSize);

// Entity: DATUM_REFERENCE
      registry.put("DATUM_REFERENCE", StepEntityResolver::resolveDatumReference);

// Entity: GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT
      registry.put("GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT",
          StepEntityResolver::resolveGeometricToleranceWithDefinedAreaUnit);

// Entity: GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE
      registry.put("GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE",
          StepEntityResolver::resolveGeometricToleranceWithMaximumTolerance);

// Entity: NON_UNIFORM_ZONE_DEFINITION
      registry.put("NON_UNIFORM_ZONE_DEFINITION", StepEntityResolver::resolveNonUniformZoneDefinition);

// Entity: DATUM_REFERENCE_MODIFIER_WITH_VALUE
      registry.put("DATUM_REFERENCE_MODIFIER_WITH_VALUE",
          StepEntityResolver::resolveDatumReferenceModifierWithValue);

// Entity: RUNOUT_ZONE_DEFINITION_ORIENTATION
      registry.put("RUNOUT_ZONE_DEFINITION_ORIENTATION",
          StepEntityResolver::resolveRunoutZoneDefinitionOrientation);

// Entity: DATUM_REFERENCE_MODIFIER
      registry.put("DATUM_REFERENCE_MODIFIER", StepEntityResolver::resolveDatumReferenceModifier);

// Entity: DATUM_SYSTEM_REFERENCE
      registry.put("DATUM_SYSTEM_REFERENCE", StepEntityResolver::resolveDatumSystemReference);

// Entity: COMPOSITE_GROUP_TOLERANCE
      registry.put("COMPOSITE_GROUP_TOLERANCE", StepEntityResolver::resolveCompositeGroupTolerance);

// Entity: GEOMETRIC_TOLERANCE_TARGET
      registry.put("GEOMETRIC_TOLERANCE_TARGET", StepEntityResolver::resolveGeometricToleranceTarget);

// Entity: MODIFIER
      registry.put("MODIFIER", StepEntityResolver::resolveModifier);

// Entity: DATUM_REFERENCE_MODIFIER_WITH_SIGN
      registry.put("DATUM_REFERENCE_MODIFIER_WITH_SIGN", StepEntityResolver::resolveDatumReferenceModifierWithSign);

// Entity: RUNOUT_ZONE_DEFINITION
      registry.put("RUNOUT_ZONE_DEFINITION", StepEntityResolver::resolveRunoutZoneDefinition);

// Entity: TOLERANCE_PAIR
      registry.put("TOLERANCE_PAIR", StepEntityResolver::resolveTolerancePair);

// Entity: TOLERANCE_SET
      registry.put("TOLERANCE_SET", StepEntityResolver::resolveToleranceSet);

// Entity: COMPOSITE_DATUM_REFERENCE
      registry.put("COMPOSITE_DATUM_REFERENCE", StepEntityResolver::resolveCompositeDatumReference);

// Entity: DIMENSIONAL_MEASUREMENT
      registry.put("DIMENSIONAL_MEASUREMENT", StepEntityResolver::resolveDimensionalMeasurement);

// Entity: PLUS_MINUS_TOLERANCE
      registry.put("PLUS_MINUS_TOLERANCE", StepEntityResolver::resolvePlusMinusTolerance);

// Entity: TOLERANCE_VALUE
      registry.put("TOLERANCE_VALUE", StepEntityResolver::resolveToleranceValue);

// Entity: MEASURE_QUALIFICATION
      registry.put("MEASURE_QUALIFICATION", StepEntityResolver::resolveMeasureQualification);

// Entity: RANGE_DIMENSIONAL_SIZE
      registry.put("RANGE_DIMENSIONAL_SIZE", StepEntityResolver::resolveRangeDimensionalSize);

// Entity: GENERALIZED_DATUM
      registry.put("GENERALIZED_DATUM", StepEntityResolver::resolveGeneralizedDatum);

// Entity: PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL
      registry.put(
          "PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL",
          StepEntityResolver::resolvePreDefinedGeometricalToleranceSymbol);

// Entity: DATUM_FEATURE_CALLOUT
      registry.put(
          "DATUM_FEATURE_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DATUM_FEATURE_CALLOUT"));

// Entity: DATUM_TARGET_CALLOUT
      registry.put(
          "DATUM_TARGET_CALLOUT",
          (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DATUM_TARGET_CALLOUT"));

// Entity: GEOMETRICAL_TOLERANCE_CALLOUT
      registry.put(
          "GEOMETRICAL_TOLERANCE_CALLOUT",
          (resolver, instance) ->
              resolver.resolveDraughtingCallout(instance, "GEOMETRICAL_TOLERANCE_CALLOUT"));

// Entity: PROJECTED_TOLERANCE_ZONE
      registry.put(
          "PROJECTED_TOLERANCE_ZONE",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "PROJECTED_TOLERANCE_ZONE"));

// Entity: DATUM_SYSTEM
      registry.put("DATUM_SYSTEM", StepEntityResolver::resolveDatumSystem);

// Entity: RECTANGULAR_TOLERANCE_ZONE
      registry.put("RECTANGULAR_TOLERANCE_ZONE", StepEntityResolver::resolveRectangularToleranceZone);

// Entity: TOLERANCE_MODIFIER
      registry.put("TOLERANCE_MODIFIER", StepEntityResolver::resolveToleranceModifier);

    // Additional geometric tolerance aliases from MiscRegistry
    RegistryHelpers.registerGeometricToleranceAliases(
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
        "LINE_PROFILE_TOLERANCE",
        "SURFACE_PROFILE_TOLERANCE");

    RegistryHelpers.registerGeometricToleranceAliases(
        registry,
        "POSITION_TOLERANCE_WITH_DATUM_REFERENCE",
        "FLATNESS_TOLERANCE_WITH_DATUM_REFERENCE",
        "STRAIGHTNESS_TOLERANCE_WITH_DATUM_REFERENCE",
        "CIRCULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "PERPENDICULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "PARALLELISM_TOLERANCE_WITH_DATUM_REFERENCE",
        "ANGULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "CYLINDRICITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "CONCENTRICITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "SYMMETRY_TOLERANCE_WITH_DATUM_REFERENCE",
        "CIRCULAR_RUNOUT_TOLERANCE_WITH_DATUM_REFERENCE",
        "TOTAL_RUNOUT_TOLERANCE_WITH_DATUM_REFERENCE",
        "LINE_PROFILE_TOLERANCE_WITH_DATUM_REFERENCE",
        "SURFACE_PROFILE_TOLERANCE_WITH_DATUM_REFERENCE");

  }
}
