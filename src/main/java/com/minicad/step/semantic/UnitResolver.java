package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;

import java.util.List;

/**
 * Unit resolver - handles unit, measure, and measurement entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains named/SI/derived/conversion-based units, measures with units,
 * measure qualifications, and measurement entities.
 */
final class UnitResolver {

  private final StepEntityResolver resolver;

  UnitResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Unit Entities ===

  StepAreaUnitWithUnit resolveAreaUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "AREA_UNIT_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepAreaUnitWithUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepContextDependentUnit resolveContextDependentUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONTEXT_DEPENDENT_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    resolver.validateNamedUnitDimensions(instance);
    return new StepContextDependentUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.deriveUnitKind(instance));
  }

  StepConversionBasedUnit resolveConversionBasedUnit(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    resolver.validateNamedUnitDimensions(instance);
    StepEntity conversionFactor = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(conversionFactor instanceof StepMeasureWithUnit)) {
      throw new StepResolutionException(
          entityName + " conversion_factor must reference MEASURE_WITH_UNIT");
    }
    StepMeasureWithUnit measureWithUnit = (StepMeasureWithUnit) conversionFactor;
    return new StepConversionBasedUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.deriveUnitKind(instance),
        measureWithUnit,
        entityName);
  }

  StepConversionBasedUnitAndUnit resolveConversionBasedUnitAndUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONVERSION_BASED_UNIT_AND_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepConversionBasedUnitAndUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepConversionBasedUnitWithOffset resolveConversionBasedUnitWithOffset(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONVERSION_BASED_UNIT_WITH_OFFSET");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    StepConversionBasedUnit base = resolveConversionBasedUnit(instance, "CONVERSION_BASED_UNIT");
    return new StepConversionBasedUnitWithOffset(
        instance.id(),
        base.name(),
        base.unitKind(),
        base.conversionFactor(),
        resolver.numberValue(instance, definition, 0));
  }

  StepDerivedUnit resolveDerivedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DERIVED_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepDerivedUnit(
        instance.id(),
        resolver.referenceList(
            instance,
            definition,
            0,
            StepDerivedUnitElement.class,
            "DERIVED_UNIT elements must contain DERIVED_UNIT_ELEMENT references"),
        "DERIVED_UNIT");
  }

  StepDerivedUnitElement resolveDerivedUnitElement(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DERIVED_UNIT_ELEMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepDerivedUnitElement(
        instance.id(),
        resolver.resolve(resolver.referenceId(instance, definition, 0)),
        resolver.numberValue(instance, definition, 1));
  }

  StepExternallyDefinedConversionBasedUnit resolveExternallyDefinedConversionBasedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    resolver.validateNamedUnitDimensions(instance);
    StepExternallyDefinedItem item = resolver.requireEntity(
        resolver.referenceId(instance, definition, 1),
        StepExternallyDefinedItem.class,
        "EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT item must reference EXTERNALLY_DEFINED_ITEM");
    return new StepExternallyDefinedConversionBasedUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.deriveUnitKind(instance),
        item);
  }

  StepLengthUnitWithUnit resolveLengthUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LENGTH_UNIT_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepLengthUnitWithUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepMassUnitWithUnit resolveMassUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MASS_UNIT_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepMassUnitWithUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepNamedUnit resolveNamedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "NAMED_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    resolver.validateNamedUnitDimensions(instance);
    return new StepNamedUnit(instance.id(), resolver.deriveUnitKind(instance));
  }

  StepNonAgreedUnitUsage resolveNonAgreedUnitUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "NON_AGREED_UNIT_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity unit = resolver.resolve(resolver.referenceId(instance, definition, 1));
    String unitName = resolver.stringValue(instance, definition, 0);
    return new StepNonAgreedUnitUsage(
        instance.id(),
        unitName,
        unit);
  }

  StepPlaneAngleUnitWithUnit resolvePlaneAngleUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PLANE_ANGLE_UNIT_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepPlaneAngleUnitWithUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepSiUnit resolveSiUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SI_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    resolver.validateNamedUnitDimensions(instance);
    String prefix = null;
    if (!resolver.isUnset(definition.parameters().get(0))) {
      prefix = resolver.enumValue(instance, definition, 0);
    }
    return new StepSiUnit(
        instance.id(), resolver.deriveUnitKind(instance), prefix, resolver.enumValue(instance, definition, 1));
  }

  StepDerivedUnit resolveStandaloneDerivedUnitKind(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepDerivedUnit(instance.id(), List.of(), entityName);
  }

  StepNamedUnit resolveStandaloneUnitKind(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepNamedUnit(instance.id(), entityName);
  }

  StepVolumeUnitWithUnit resolveVolumeUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VOLUME_UNIT_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepVolumeUnitWithUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  // === Measure Entities ===

  StepDimensionalExponents resolveDimensionalExponents(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIMENSIONAL_EXPONENTS");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepDimensionalExponents(
        instance.id(),
        resolver.numberValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4),
        resolver.numberValue(instance, definition, 5),
        resolver.numberValue(instance, definition, 6));
  }

  StepMeasureQualification resolveMeasureQualification(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MEASURE_QUALIFICATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> qualifiers =
        resolver.entityReferenceList(
            instance, definition, 2,
            "MEASURE_QUALIFICATION qualifiers must contain entity references");
    return new StepMeasureQualification(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        qualifiers);
  }

  StepMeasureRepresentationItem resolveMeasureRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MEASURE_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 3);

    // Use enhanced typedSelection with validation
    StepParameterReader.TypedSelection selection = resolver.typedSelection(instance, definition, 1);
    resolver.validateSelectTypeName(instance, definition, 1, selection,
        SelectTypeRegistry.MEASURE_SELECT_TYPES);

    StepValue unwrapped = selection.value();
    if (!(unwrapped instanceof StepValue.NumberValue)) {
      throw new StepResolutionException(
          "entity #" + instance.id() + " MEASURE_REPRESENTATION_ITEM" +
          " parameter 1 typed measure must wrap a number, actual: " +
          StepParameterReader.valueType(unwrapped));
    }
    StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
    return new StepMeasureRepresentationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        selection.typeName(),
        numberValue.value(),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepMeasureRepresentationItemWithUnit resolveMeasureRepresentationItemWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MEASURE_REPRESENTATION_ITEM_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepMeasureRepresentationItemWithUnit(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepMeasureWithUnit resolveMeasureWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MEASURE_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepMeasureWithUnit(
        instance.id(),
        resolver.numberValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepTypedMeasureWithUnit(
        instance.id(),
        entityName,
        resolver.numberValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName, String expectedUnitKind) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity unitComponent = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!resolver.matchesUnitKind(unitComponent, expectedUnitKind)) {
      throw new StepResolutionException(
          entityName + " unit_component must reference " + expectedUnitKind);
    }
    return new StepTypedMeasureWithUnit(
        instance.id(),
        entityName,
        resolver.numberValue(instance, definition, 0),
        unitComponent);
  }

  StepUncertaintyMeasure resolveUncertaintyMeasure(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "UNCERTAINTY_MEASURE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepUncertaintyMeasure(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.stringValue(instance, definition, 2));
  }

  StepUncertaintyMeasureWithUnit resolveUncertaintyMeasureWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "UNCERTAINTY_MEASURE_WITH_UNIT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepUncertaintyMeasureWithUnit(
        instance.id(),
        resolver.numberValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        resolver.stringValue(instance, definition, 3));
  }

  // === Measurement Entities ===

  StepGeometricMeasurement resolveGeometricMeasurement(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEOMETRIC_MEASUREMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    List<StepEntity> measurementPoints =
        resolver.entityReferenceList(
            instance, definition, 6,
            "GEOMETRIC_MEASUREMENT measurement_points must contain entity references");
    return new StepGeometricMeasurement(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        measurementPoints,
        resolver.stringValue(instance, definition, 5));
  }

  StepMeasurementPoint resolveMeasurementPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MEASUREMENT_POINT");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepMeasurementPoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.numberValue(instance, definition, 5),
        (int) resolver.numberValue(instance, definition, 6));
  }

  StepSurfaceMeasurement resolveSurfaceMeasurement(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_MEASUREMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    List<String> roughnessParameters = resolver.literalList(instance, definition, 3);
    List<Double> measuredValues = resolver.numberList(instance, definition, 4);
    return new StepSurfaceMeasurement(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        roughnessParameters,
        measuredValues,
        resolver.stringValue(instance, definition, 5),
        resolver.resolve(resolver.referenceId(instance, definition, 6)),
        resolver.stringValue(instance, definition, 7));
  }
}
