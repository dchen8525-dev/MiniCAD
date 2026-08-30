package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for unit entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class UnitRegistry {

  private UnitRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: COMPOSITE_UNIT_SHAPE_ASPECT
      registry.put(
          "COMPOSITE_UNIT_SHAPE_ASPECT",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "COMPOSITE_UNIT_SHAPE_ASPECT"));

// Entity: UNCERTAINTY_MEASURE_WITH_UNIT
      registry.put("UNCERTAINTY_MEASURE_WITH_UNIT", (resolver, instance) -> resolver.unitResolver.resolveUncertaintyMeasureWithUnit(instance));

// Entity: LENGTH_MEASURE_WITH_UNIT
      registry.put(
          "LENGTH_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "LENGTH_MEASURE_WITH_UNIT", "LENGTH_UNIT"));

// Entity: MASS_MEASURE_WITH_UNIT
      registry.put(
          "MASS_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "MASS_MEASURE_WITH_UNIT", "MASS_UNIT"));

// Entity: TIME_MEASURE_WITH_UNIT
      registry.put(
          "TIME_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "TIME_MEASURE_WITH_UNIT", "TIME_UNIT"));

// Entity: AREA_MEASURE_WITH_UNIT
      registry.put(
          "AREA_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "AREA_MEASURE_WITH_UNIT", "AREA_UNIT"));

// Entity: RATIO_MEASURE_WITH_UNIT
      registry.put(
          "RATIO_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "RATIO_MEASURE_WITH_UNIT", "RATIO_UNIT"));

// Entity: THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT
      registry.put(
          "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
                  "THERMODYNAMIC_TEMPERATURE_UNIT"));

// Entity: ELECTRIC_CURRENT_MEASURE_WITH_UNIT
      registry.put(
          "ELECTRIC_CURRENT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ELECTRIC_CURRENT_MEASURE_WITH_UNIT", "ELECTRIC_CURRENT_UNIT"));

// Entity: AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT
      registry.put(
          "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
                  "AMOUNT_OF_SUBSTANCE_UNIT"));

// Entity: FREQUENCY_MEASURE_WITH_UNIT
      registry.put(
          "FREQUENCY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "FREQUENCY_MEASURE_WITH_UNIT", "FREQUENCY_UNIT"));

// Entity: FORCE_MEASURE_WITH_UNIT
      registry.put(
          "FORCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "FORCE_MEASURE_WITH_UNIT", "FORCE_UNIT"));

// Entity: PRESSURE_MEASURE_WITH_UNIT
      registry.put(
          "PRESSURE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "PRESSURE_MEASURE_WITH_UNIT", "PRESSURE_UNIT"));

// Entity: ENERGY_MEASURE_WITH_UNIT
      registry.put(
          "ENERGY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ENERGY_MEASURE_WITH_UNIT", "ENERGY_UNIT"));

// Entity: POWER_MEASURE_WITH_UNIT
      registry.put(
          "POWER_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(instance, "POWER_MEASURE_WITH_UNIT", "POWER_UNIT"));

// Entity: ELECTRIC_CHARGE_MEASURE_WITH_UNIT
      registry.put(
          "ELECTRIC_CHARGE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ELECTRIC_CHARGE_MEASURE_WITH_UNIT", "ELECTRIC_CHARGE_UNIT"));

// Entity: ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT
      registry.put(
          "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
                  "ELECTRIC_POTENTIAL_UNIT"));

// Entity: CAPACITANCE_MEASURE_WITH_UNIT
      registry.put(
          "CAPACITANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CAPACITANCE_MEASURE_WITH_UNIT", "CAPACITANCE_UNIT"));

// Entity: RESISTANCE_MEASURE_WITH_UNIT
      registry.put(
          "RESISTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "RESISTANCE_MEASURE_WITH_UNIT", "RESISTANCE_UNIT"));

// Entity: CONDUCTANCE_MEASURE_WITH_UNIT
      registry.put(
          "CONDUCTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CONDUCTANCE_MEASURE_WITH_UNIT", "CONDUCTANCE_UNIT"));

// Entity: MAGNETIC_FLUX_MEASURE_WITH_UNIT
      registry.put(
          "MAGNETIC_FLUX_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "MAGNETIC_FLUX_MEASURE_WITH_UNIT", "MAGNETIC_FLUX_UNIT"));

// Entity: MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT
      registry.put(
          "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
                  "MAGNETIC_FLUX_DENSITY_UNIT"));

// Entity: INDUCTANCE_MEASURE_WITH_UNIT
      registry.put(
          "INDUCTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "INDUCTANCE_MEASURE_WITH_UNIT", "INDUCTANCE_UNIT"));

// Entity: ILLUMINANCE_MEASURE_WITH_UNIT
      registry.put(
          "ILLUMINANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ILLUMINANCE_MEASURE_WITH_UNIT", "ILLUMINANCE_UNIT"));

// Entity: LUMINOUS_FLUX_MEASURE_WITH_UNIT
      registry.put(
          "LUMINOUS_FLUX_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "LUMINOUS_FLUX_MEASURE_WITH_UNIT", "LUMINOUS_FLUX_UNIT"));

// Entity: LUMINOUS_INTENSITY_MEASURE_WITH_UNIT
      registry.put(
          "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
                  "LUMINOUS_INTENSITY_UNIT"));

// Entity: RADIOACTIVITY_MEASURE_WITH_UNIT
      registry.put(
          "RADIOACTIVITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "RADIOACTIVITY_MEASURE_WITH_UNIT", "RADIOACTIVITY_UNIT"));

// Entity: ABSORBED_DOSE_MEASURE_WITH_UNIT
      registry.put(
          "ABSORBED_DOSE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ABSORBED_DOSE_MEASURE_WITH_UNIT", "ABSORBED_DOSE_UNIT"));

// Entity: DOSE_EQUIVALENT_MEASURE_WITH_UNIT
      registry.put(
          "DOSE_EQUIVALENT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "DOSE_EQUIVALENT_MEASURE_WITH_UNIT", "DOSE_EQUIVALENT_UNIT"));

// Entity: ACCELERATION_MEASURE_WITH_UNIT
      registry.put(
          "ACCELERATION_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ACCELERATION_MEASURE_WITH_UNIT", "ACCELERATION_UNIT"));

// Entity: VELOCITY_MEASURE_WITH_UNIT
      registry.put(
          "VELOCITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "VELOCITY_MEASURE_WITH_UNIT", "VELOCITY_UNIT"));

// Entity: THERMAL_RESISTANCE_MEASURE_WITH_UNIT
      registry.put(
          "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance,
                  "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
                  "THERMAL_RESISTANCE_UNIT"));

// Entity: MEASURE_WITH_UNIT
      registry.put("MEASURE_WITH_UNIT", (resolver, instance) -> resolver.unitResolver.resolveMeasureWithUnit(instance));

// Entity: DERIVED_UNIT_ELEMENT
      registry.put("DERIVED_UNIT_ELEMENT", (resolver, instance) -> resolver.unitResolver.resolveDerivedUnitElement(instance));

// Entity: DERIVED_UNIT
      registry.put("DERIVED_UNIT", (resolver, instance) -> resolver.unitResolver.resolveDerivedUnit(instance));

// Entity: SI_UNIT
      registry.put("SI_UNIT", (resolver, instance) -> resolver.unitResolver.resolveSiUnit(instance));

// Entity: SOLID_ANGLE_UNIT (moved from ProductRegistry, must be AFTER SI_UNIT for proper complex entity resolution)
      registry.put(
          "SOLID_ANGLE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "SOLID_ANGLE_UNIT"));

// Entity: VOLUME_UNIT (moved from ProductRegistry)
      registry.put(
          "VOLUME_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "VOLUME_UNIT"));

// Entity: CONVERSION_BASED_UNIT_WITH_OFFSET
      registry.put("CONVERSION_BASED_UNIT_WITH_OFFSET", (resolver, instance) -> resolver.unitResolver.resolveConversionBasedUnitWithOffset(instance));

// Entity: CONVERSION_BASED_UNIT (must be before NAMED_UNIT for complex entity priority)
      registry.put("CONVERSION_BASED_UNIT", (resolver, instance) -> resolver.resolveConversionBasedUnit(instance, "CONVERSION_BASED_UNIT"));

// Entity: NAMED_UNIT
      registry.put("NAMED_UNIT", (resolver, instance) -> resolver.unitResolver.resolveNamedUnit(instance));

// Entity: PLANE_ANGLE_UNIT (moved from GeometryRegistry1 to ensure consistent ordering)
      registry.put(
          "PLANE_ANGLE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "PLANE_ANGLE_UNIT"));

// Entity: PLANE_ANGLE_MEASURE_WITH_UNIT (moved from GeometryRegistry1 to ensure consistent ordering)
      registry.put(
          "PLANE_ANGLE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "PLANE_ANGLE_MEASURE_WITH_UNIT", "PLANE_ANGLE_UNIT"));

// Entity: PLANE_ANGLE_UNIT_WITH_UNIT (moved from GeometryRegistry2 to ensure consistent ordering)
      registry.put("PLANE_ANGLE_UNIT_WITH_UNIT", (resolver, instance) -> resolver.unitResolver.resolvePlaneAngleUnitWithUnit(instance));

// Entity: LENGTH_UNIT
      registry.put(
          "LENGTH_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LENGTH_UNIT"));

// Entity: MASS_UNIT
      registry.put(
          "MASS_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "MASS_UNIT"));

// Entity: RATIO_UNIT
      registry.put(
          "RATIO_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "RATIO_UNIT"));

// Entity: AREA_UNIT
      registry.put(
          "AREA_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "AREA_UNIT"));

// Entity: TIME_UNIT
      registry.put(
          "TIME_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "TIME_UNIT"));

// Entity: THERMODYNAMIC_TEMPERATURE_UNIT
      registry.put(
          "THERMODYNAMIC_TEMPERATURE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "THERMODYNAMIC_TEMPERATURE_UNIT"));

// Entity: ELECTRIC_CURRENT_UNIT
      registry.put(
          "ELECTRIC_CURRENT_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "ELECTRIC_CURRENT_UNIT"));

// Entity: AMOUNT_OF_SUBSTANCE_UNIT
      registry.put(
          "AMOUNT_OF_SUBSTANCE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "AMOUNT_OF_SUBSTANCE_UNIT"));

// Entity: LUMINOUS_FLUX_UNIT
      registry.put(
          "LUMINOUS_FLUX_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_FLUX_UNIT"));

// Entity: LUMINOUS_INTENSITY_UNIT
      registry.put(
          "LUMINOUS_INTENSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_INTENSITY_UNIT"));

// Entity: ACCELERATION_UNIT
      registry.put(
          "ACCELERATION_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ACCELERATION_UNIT"));

// Entity: VELOCITY_UNIT
      registry.put(
          "VELOCITY_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "VELOCITY_UNIT"));

// Entity: THERMAL_RESISTANCE_UNIT
      registry.put(
          "THERMAL_RESISTANCE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "THERMAL_RESISTANCE_UNIT"));

// Entity: FREQUENCY_UNIT
      registry.put(
          "FREQUENCY_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FREQUENCY_UNIT"));

// Entity: FORCE_UNIT
      registry.put(
          "FORCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FORCE_UNIT"));

// Entity: PRESSURE_UNIT
      registry.put(
          "PRESSURE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "PRESSURE_UNIT"));

// Entity: ENERGY_UNIT
      registry.put(
          "ENERGY_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ENERGY_UNIT"));

// Entity: POWER_UNIT
      registry.put(
          "POWER_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "POWER_UNIT"));

// Entity: ELECTRIC_CHARGE_UNIT
      registry.put(
          "ELECTRIC_CHARGE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_CHARGE_UNIT"));

// Entity: ELECTRIC_POTENTIAL_UNIT
      registry.put(
          "ELECTRIC_POTENTIAL_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_POTENTIAL_UNIT"));

// Entity: CAPACITANCE_UNIT
      registry.put(
          "CAPACITANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CAPACITANCE_UNIT"));

// Entity: RESISTANCE_UNIT
      registry.put(
          "RESISTANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "RESISTANCE_UNIT"));

// Entity: CONDUCTANCE_UNIT
      registry.put(
          "CONDUCTANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CONDUCTANCE_UNIT"));

// Entity: MAGNETIC_FLUX_UNIT
      registry.put(
          "MAGNETIC_FLUX_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_UNIT"));

// Entity: MAGNETIC_FLUX_DENSITY_UNIT
      registry.put(
          "MAGNETIC_FLUX_DENSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_DENSITY_UNIT"));

// Entity: INDUCTANCE_UNIT
      registry.put(
          "INDUCTANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "INDUCTANCE_UNIT"));

// Entity: ILLUMINANCE_UNIT
      registry.put(
          "ILLUMINANCE_UNIT",
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ILLUMINANCE_UNIT"));

// Entity: RADIOACTIVITY_UNIT
      registry.put(
          "RADIOACTIVITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "RADIOACTIVITY_UNIT"));

// Entity: ABSORBED_DOSE_UNIT
      registry.put(
          "ABSORBED_DOSE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ABSORBED_DOSE_UNIT"));

// Entity: DOSE_EQUIVALENT_UNIT
      registry.put(
          "DOSE_EQUIVALENT_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "DOSE_EQUIVALENT_UNIT"));

// Entity: CELSIUS_TEMPERATURE_MEASURE_WITH_UNIT
      registry.put(
          "CELSIUS_TEMPERATURE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CELSIUS_TEMPERATURE_MEASURE_WITH_UNIT", "CELSIUS_TEMPERATURE_UNIT"));

// Entity: CURRENCY_MEASURE_WITH_UNIT
      registry.put(
          "CURRENCY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "CURRENCY_MEASURE_WITH_UNIT", "CURRENCY_UNIT"));

// Entity: DIELECTRIC_CONSTANT_MEASURE_WITH_UNIT
      registry.put(
          "DIELECTRIC_CONSTANT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "DIELECTRIC_CONSTANT_MEASURE_WITH_UNIT", "DIELECTRIC_CONSTANT_UNIT"));

// Entity: LOSS_TANGENT_MEASURE_WITH_UNIT
      registry.put(
          "LOSS_TANGENT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "LOSS_TANGENT_MEASURE_WITH_UNIT", "LOSS_TANGENT_UNIT"));

// Entity: POSITIVE_LENGTH_MEASURE_WITH_UNIT
      registry.put(
          "POSITIVE_LENGTH_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "POSITIVE_LENGTH_MEASURE_WITH_UNIT", "LENGTH_UNIT"));

// Entity: EXPRESSION_CONVERSION_BASED_UNIT
      registry.put(
          "EXPRESSION_CONVERSION_BASED_UNIT",
          (resolver, instance) ->
              resolver.resolveConversionBasedUnit(instance, "EXPRESSION_CONVERSION_BASED_UNIT"));

// Entity: EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT
      registry.put(
          "EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT",
          (resolver, instance) ->
              resolver.resolveExternallyDefinedConversionBasedUnit(instance));

// Entity: NON_AGREED_UNIT_USAGE
      registry.put(
          "NON_AGREED_UNIT_USAGE",
          (resolver, instance) ->
              resolver.resolveNonAgreedUnitUsage(instance));

// Entity: SI_BASE_UNIT
      registry.put(
          "SI_BASE_UNIT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: SI_DERIVED_UNIT
      registry.put(
          "SI_DERIVED_UNIT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: SI_DERIVED_UNIT_ELEMENT
      registry.put(
          "SI_DERIVED_UNIT_ELEMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: CONVERSION_BASED_UNIT_AND_RATIO_UNIT
      registry.put(
          "CONVERSION_BASED_UNIT_AND_RATIO_UNIT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: TYPED_MEASURE_WITH_UNIT
      registry.put("TYPED_MEASURE_WITH_UNIT", (resolver, instance) ->
          resolver.resolveTypedMeasureWithUnit(instance, "TYPED_MEASURE_WITH_UNIT"));

// Entity: LENGTH_UNIT_WITH_UNIT
      registry.put("LENGTH_UNIT_WITH_UNIT", (resolver, instance) -> resolver.unitResolver.resolveLengthUnitWithUnit(instance));

// Entity: AREA_UNIT_WITH_UNIT
      registry.put("AREA_UNIT_WITH_UNIT", (resolver, instance) -> resolver.unitResolver.resolveAreaUnitWithUnit(instance));

// Entity: MASS_UNIT_WITH_UNIT
      registry.put("MASS_UNIT_WITH_UNIT", (resolver, instance) -> resolver.unitResolver.resolveMassUnitWithUnit(instance));

// Entity: CONVERSION_BASED_UNIT_AND_UNIT
      registry.put("CONVERSION_BASED_UNIT_AND_UNIT", (resolver, instance) -> resolver.unitResolver.resolveConversionBasedUnitAndUnit(instance));

// Entity: MASS_DENSITY_MEASURE_WITH_UNIT
      registry.put(
          "MASS_DENSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "MASS_DENSITY_MEASURE_WITH_UNIT", "MASS_DENSITY_UNIT"));

// Entity: MASS_DENSITY_UNIT
      registry.put(
          "MASS_DENSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "MASS_DENSITY_UNIT"));

// Entity: DYNAMIC_VISCOSITY_MEASURE_WITH_UNIT
      registry.put(
          "DYNAMIC_VISCOSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "DYNAMIC_VISCOSITY_MEASURE_WITH_UNIT", "DYNAMIC_VISCOSITY_UNIT"));

// Entity: DYNAMIC_VISCOSITY_UNIT
      registry.put(
          "DYNAMIC_VISCOSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "DYNAMIC_VISCOSITY_UNIT"));

// Entity: KINEMATIC_VISCOSITY_MEASURE_WITH_UNIT
      registry.put(
          "KINEMATIC_VISCOSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "KINEMATIC_VISCOSITY_MEASURE_WITH_UNIT", "KINEMATIC_VISCOSITY_UNIT"));

// Entity: KINEMATIC_VISCOSITY_UNIT
      registry.put(
          "KINEMATIC_VISCOSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "KINEMATIC_VISCOSITY_UNIT"));

// Entity: MOMENT_OF_INERTIA_MEASURE_WITH_UNIT
      registry.put(
          "MOMENT_OF_INERTIA_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "MOMENT_OF_INERTIA_MEASURE_WITH_UNIT", "MOMENT_OF_INERTIA_UNIT"));

// Entity: MOMENT_OF_INERTIA_UNIT
      registry.put(
          "MOMENT_OF_INERTIA_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "MOMENT_OF_INERTIA_UNIT"));

// Entity: THERMAL_CONDUCTIVITY_MEASURE_WITH_UNIT
      registry.put(
          "THERMAL_CONDUCTIVITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "THERMAL_CONDUCTIVITY_MEASURE_WITH_UNIT", "THERMAL_CONDUCTIVITY_UNIT"));

// Entity: THERMAL_CONDUCTIVITY_UNIT
      registry.put(
          "THERMAL_CONDUCTIVITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "THERMAL_CONDUCTIVITY_UNIT"));

// Entity: HEAT_FLUX_DENSITY_MEASURE_WITH_UNIT
      registry.put(
          "HEAT_FLUX_DENSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "HEAT_FLUX_DENSITY_MEASURE_WITH_UNIT", "HEAT_FLUX_DENSITY_UNIT"));

// Entity: HEAT_FLUX_DENSITY_UNIT
      registry.put(
          "HEAT_FLUX_DENSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "HEAT_FLUX_DENSITY_UNIT"));

// Entity: TORQUE_UNIT
      registry.put(
          "TORQUE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "TORQUE_UNIT"));

// Entity: LINEAR_FORCE_UNIT
      registry.put(
          "LINEAR_FORCE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "LINEAR_FORCE_UNIT"));

// Entity: LINEAR_STIFFNESS_UNIT
      registry.put(
          "LINEAR_STIFFNESS_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "LINEAR_STIFFNESS_UNIT"));

// Entity: ROTATIONAL_STIFFNESS_UNIT
      registry.put(
          "ROTATIONAL_STIFFNESS_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ROTATIONAL_STIFFNESS_UNIT"));

// Entity: LINEAR_MOMENT_UNIT
      registry.put(
          "LINEAR_MOMENT_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "LINEAR_MOMENT_UNIT"));

// Entity: SPECIFIC_HEAT_CAPACITY_MEASURE_WITH_UNIT
      registry.put(
          "SPECIFIC_HEAT_CAPACITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "SPECIFIC_HEAT_CAPACITY_MEASURE_WITH_UNIT", "SPECIFIC_HEAT_CAPACITY_UNIT"));

// Entity: SPECIFIC_HEAT_CAPACITY_UNIT
      registry.put(
          "SPECIFIC_HEAT_CAPACITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "SPECIFIC_HEAT_CAPACITY_UNIT"));

// Entity: AREA_DENSITY_MEASURE_WITH_UNIT
      registry.put(
          "AREA_DENSITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "AREA_DENSITY_MEASURE_WITH_UNIT", "AREA_DENSITY_UNIT"));

// Entity: AREA_DENSITY_UNIT
      registry.put(
          "AREA_DENSITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "AREA_DENSITY_UNIT"));

// Entity: VOLUMETRIC_FLOW_RATE_MEASURE_WITH_UNIT
      registry.put(
          "VOLUMETRIC_FLOW_RATE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "VOLUMETRIC_FLOW_RATE_MEASURE_WITH_UNIT", "VOLUMETRIC_FLOW_RATE_UNIT"));

// Entity: VOLUMETRIC_FLOW_RATE_UNIT
      registry.put(
          "VOLUMETRIC_FLOW_RATE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "VOLUMETRIC_FLOW_RATE_UNIT"));

// Entity: MASS_FLOW_RATE_MEASURE_WITH_UNIT
      registry.put(
          "MASS_FLOW_RATE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "MASS_FLOW_RATE_MEASURE_WITH_UNIT", "MASS_FLOW_RATE_UNIT"));

// Entity: MASS_FLOW_RATE_UNIT
      registry.put(
          "MASS_FLOW_RATE_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "MASS_FLOW_RATE_UNIT"));

// Entity: ROTATIONAL_FREQUENCY_UNIT
      registry.put(
          "ROTATIONAL_FREQUENCY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ROTATIONAL_FREQUENCY_UNIT"));

// Entity: ROTATIONAL_FREQUENCY_MEASURE_WITH_UNIT
      registry.put(
          "ROTATIONAL_FREQUENCY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ROTATIONAL_FREQUENCY_MEASURE_WITH_UNIT", "ROTATIONAL_FREQUENCY_UNIT"));

// Entity: ANGULAR_VELOCITY_UNIT
      registry.put(
          "ANGULAR_VELOCITY_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ANGULAR_VELOCITY_UNIT"));

// Entity: ANGULAR_VELOCITY_MEASURE_WITH_UNIT
      registry.put(
          "ANGULAR_VELOCITY_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ANGULAR_VELOCITY_MEASURE_WITH_UNIT", "ANGULAR_VELOCITY_UNIT"));

// Entity: ANGULAR_ACCELERATION_UNIT
      registry.put(
          "ANGULAR_ACCELERATION_UNIT",
          (resolver, instance) ->
              resolver.resolveStandaloneDerivedUnitKind(instance, "ANGULAR_ACCELERATION_UNIT"));

// Entity: ANGULAR_ACCELERATION_MEASURE_WITH_UNIT
      registry.put(
          "ANGULAR_ACCELERATION_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ANGULAR_ACCELERATION_MEASURE_WITH_UNIT", "ANGULAR_ACCELERATION_UNIT"));

// Entity: TORQUE_MEASURE_WITH_UNIT
      registry.put(
          "TORQUE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "TORQUE_MEASURE_WITH_UNIT", "TORQUE_UNIT"));

// Entity: LINEAR_FORCE_MEASURE_WITH_UNIT
      registry.put(
          "LINEAR_FORCE_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "LINEAR_FORCE_MEASURE_WITH_UNIT", "LINEAR_FORCE_UNIT"));

// Entity: LINEAR_STIFFNESS_MEASURE_WITH_UNIT
      registry.put(
          "LINEAR_STIFFNESS_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "LINEAR_STIFFNESS_MEASURE_WITH_UNIT", "LINEAR_STIFFNESS_UNIT"));

// Entity: ROTATIONAL_STIFFNESS_MEASURE_WITH_UNIT
      registry.put(
          "ROTATIONAL_STIFFNESS_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "ROTATIONAL_STIFFNESS_MEASURE_WITH_UNIT", "ROTATIONAL_STIFFNESS_UNIT"));

// Entity: LINEAR_MOMENT_MEASURE_WITH_UNIT
      registry.put(
          "LINEAR_MOMENT_MEASURE_WITH_UNIT",
          (resolver, instance) ->
              resolver.resolveTypedMeasureWithUnit(
                  instance, "LINEAR_MOMENT_MEASURE_WITH_UNIT", "LINEAR_MOMENT_UNIT"));


  }
}
