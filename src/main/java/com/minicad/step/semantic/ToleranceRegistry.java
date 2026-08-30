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
      registry.put("DATUM", (resolver, instance) -> resolver.annotationResolver.resolveDatum(instance));

// Entity: DATUM_FEATURE
      registry.put("DATUM_FEATURE", (resolver, instance) -> resolver.annotationResolver.resolveDatumFeature(instance));

// Entity: DATUM_TARGET
      registry.put("DATUM_TARGET", (resolver, instance) -> resolver.annotationResolver.resolveDatumTarget(instance));

// Entity: DIMENSIONAL_LOCATION
      registry.put("DIMENSIONAL_LOCATION", (resolver, instance) -> resolver.annotationResolver.resolveDimensionalLocation(instance));

// Entity: DIMENSIONAL_SIZE
      registry.put("DIMENSIONAL_SIZE", (resolver, instance) -> resolver.annotationResolver.resolveDimensionalSize(instance));

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
          (resolver, instance) -> resolver.geometricFeatureResolver.resolvePlacedDatumTargetFeature(instance));

// Entity: ANGULARITY_TOLERANCE_WITH_MODIFIERS
      registry.put(
          "ANGULARITY_TOLERANCE_WITH_MODIFIERS",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(
                  instance, "ANGULARITY_TOLERANCE_WITH_MODIFIERS"));

// Entity: DIMENSIONAL_EXPONENTS
      registry.put("DIMENSIONAL_EXPONENTS", (resolver, instance) -> resolver.unitResolver.resolveDimensionalExponents(instance));

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
      registry.put("DATUM_REFERENCE_COMPARTMENT", (resolver, instance) -> resolver.annotationResolver.resolveDatumReferenceCompartment(instance));

// Entity: DATUM_REFERENCE_ELEMENT
      registry.put(
          "DATUM_REFERENCE_ELEMENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_REFERENCE_ELEMENT"));

// Entity: COMMON_DATUM
      registry.put(
          "COMMON_DATUM",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMMON_DATUM"));

// Entity: TOLERANCE_ZONE_FORM
      registry.put("TOLERANCE_ZONE_FORM", (resolver, instance) -> resolver.annotationResolver.resolveToleranceZoneForm(instance));

// Entity: TOLERANCE_ZONE
      registry.put("TOLERANCE_ZONE", (resolver, instance) -> resolver.annotationResolver.resolveToleranceZone(instance));

// Entity: FEATURE_CONTROL_FRAME
      registry.put("FEATURE_CONTROL_FRAME", (resolver, instance) -> resolver.annotationResolver.resolveFeatureControlFrame(instance));

// Entity: RUNOUT_TOLERANCE_ZONE
      registry.put("RUNOUT_TOLERANCE_ZONE", (resolver, instance) -> resolver.annotationResolver.resolveRunoutToleranceZone(instance));

// Entity: GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE
      registry.put(
          "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE",
          (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE"));

// Entity: RADIAL_TOLERANCE_ZONE
      registry.put("RADIAL_TOLERANCE_ZONE", (resolver, instance) -> resolver.annotationResolver.resolveRadialToleranceZone(instance));

// Entity: PROJECTED_ZONE_DEFINITION
      registry.put("PROJECTED_ZONE_DEFINITION", StepEntityResolver::resolveProjectedZoneDefinition);

// Entity: PLUS_MINUS_TOLERANCE_WITH_MODIFIERS
      registry.put("PLUS_MINUS_TOLERANCE_WITH_MODIFIERS", StepEntityResolver::resolvePlusMinusToleranceWithModifiers);

// Entity: DIRECTED_DIMENSIONAL_SIZE
      registry.put("DIRECTED_DIMENSIONAL_SIZE", (resolver, instance) -> resolver.annotationResolver.resolveDirectedDimensionalSize(instance));

// Entity: DATUM_REFERENCE
      registry.put("DATUM_REFERENCE", (resolver, instance) -> resolver.annotationResolver.resolveDatumReference(instance));

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
          (resolver, instance) -> resolver.annotationResolver.resolveDatumReferenceModifierWithValue(instance));

// Entity: RUNOUT_ZONE_DEFINITION_ORIENTATION
      registry.put("RUNOUT_ZONE_DEFINITION_ORIENTATION",
          StepEntityResolver::resolveRunoutZoneDefinitionOrientation);

// Entity: DATUM_REFERENCE_MODIFIER
      registry.put("DATUM_REFERENCE_MODIFIER", (resolver, instance) -> resolver.annotationResolver.resolveDatumReferenceModifier(instance));

// Entity: DATUM_SYSTEM_REFERENCE
      registry.put("DATUM_SYSTEM_REFERENCE", (resolver, instance) -> resolver.annotationResolver.resolveDatumSystemReference(instance));

// Entity: COMPOSITE_GROUP_TOLERANCE
      registry.put("COMPOSITE_GROUP_TOLERANCE", (resolver, instance) -> resolver.annotationResolver.resolveCompositeGroupTolerance(instance));

// Entity: GEOMETRIC_TOLERANCE_TARGET
      registry.put("GEOMETRIC_TOLERANCE_TARGET", (resolver, instance) -> resolver.annotationResolver.resolveGeometricToleranceTarget(instance));

// Entity: MODIFIER
      registry.put("MODIFIER", StepEntityResolver::resolveModifier);

// Entity: DATUM_REFERENCE_MODIFIER_WITH_SIGN
      registry.put(
          "DATUM_REFERENCE_MODIFIER_WITH_SIGN",
          (resolver, instance) -> resolver.annotationResolver.resolveDatumReferenceModifierWithSign(instance));

// Entity: RUNOUT_ZONE_DEFINITION
      registry.put("RUNOUT_ZONE_DEFINITION", StepEntityResolver::resolveRunoutZoneDefinition);

// Entity: TOLERANCE_PAIR
      registry.put("TOLERANCE_PAIR", (resolver, instance) -> resolver.annotationResolver.resolveTolerancePair(instance));

// Entity: TOLERANCE_SET
      registry.put("TOLERANCE_SET", (resolver, instance) -> resolver.annotationResolver.resolveToleranceSet(instance));

// Entity: COMPOSITE_DATUM_REFERENCE
      registry.put("COMPOSITE_DATUM_REFERENCE", (resolver, instance) -> resolver.annotationResolver.resolveCompositeDatumReference(instance));

// Entity: DIMENSIONAL_MEASUREMENT
      registry.put("DIMENSIONAL_MEASUREMENT", (resolver, instance) -> resolver.annotationResolver.resolveDimensionalMeasurement(instance));

// Entity: PLUS_MINUS_TOLERANCE
      registry.put("PLUS_MINUS_TOLERANCE", (resolver, instance) -> resolver.annotationResolver.resolvePlusMinusTolerance(instance));

// Entity: TOLERANCE_VALUE
      registry.put("TOLERANCE_VALUE", (resolver, instance) -> resolver.annotationResolver.resolveToleranceValue(instance));

// Entity: MEASURE_QUALIFICATION
      registry.put("MEASURE_QUALIFICATION", (resolver, instance) -> resolver.unitResolver.resolveMeasureQualification(instance));

// Entity: RANGE_DIMENSIONAL_SIZE
      registry.put("RANGE_DIMENSIONAL_SIZE", (resolver, instance) -> resolver.annotationResolver.resolveRangeDimensionalSize(instance));

// Entity: GENERALIZED_DATUM
      registry.put("GENERALIZED_DATUM", (resolver, instance) -> resolver.annotationResolver.resolveGeneralizedDatum(instance));

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
      registry.put("DATUM_SYSTEM", (resolver, instance) -> resolver.annotationResolver.resolveDatumSystem(instance));

// Entity: RECTANGULAR_TOLERANCE_ZONE
      registry.put("RECTANGULAR_TOLERANCE_ZONE", (resolver, instance) -> resolver.annotationResolver.resolveRectangularToleranceZone(instance));

// Entity: TOLERANCE_MODIFIER
      registry.put("TOLERANCE_MODIFIER", (resolver, instance) -> resolver.annotationResolver.resolveToleranceModifier(instance));

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