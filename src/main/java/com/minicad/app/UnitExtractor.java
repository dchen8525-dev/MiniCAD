package com.minicad.app;

import com.minicad.step.model.*;

import java.util.Map;

/**
 * Extracts unit information from resolved STEP entities.
 */
final class UnitExtractor {

    private UnitExtractor() {
    }

    record UnitInfo(
            String lengthUnit,
            Double scaleToMeters,
            String angleUnit
    ) {
        boolean isEmpty() {
            return lengthUnit == null && angleUnit == null;
        }
    }

    // SI prefixes mapped to their multipliers relative to the base unit
    private static final Map<String, Double> SI_PREFIXES = Map.ofEntries(
            Map.entry("EXA", 1e18),
            Map.entry("PETA", 1e15),
            Map.entry("TERA", 1e12),
            Map.entry("GIGA", 1e9),
            Map.entry("MEGA", 1e6),
            Map.entry("MYRIA", 1e4),
            Map.entry("KILO", 1e3),
            Map.entry("HECTO", 1e2),
            Map.entry("DECA", 1e1),
            Map.entry("DECI", 1e-1),
            Map.entry("CENTI", 1e-2),
            Map.entry("MILLI", 1e-3),
            Map.entry("MICRO", 1e-6),
            Map.entry("NANO", 1e-9),
            Map.entry("PICO", 1e-12),
            Map.entry("FEMTO", 1e-15),
            Map.entry("ATTO", 1e-18)
    );

    // Base unit names to their meter equivalents
    private static final Map<String, Double> BASE_UNITS_TO_METERS = Map.ofEntries(
            Map.entry("METRE", 1.0),
            Map.entry("INCH", 0.0254),
            Map.entry("FOOT", 0.3048),
            Map.entry("YARD", 0.9144),
            Map.entry("MILE", 1609.344),
            Map.entry("MILE_STATUTE", 1609.344)
    );

    static UnitInfo extract(Map<Integer, StepEntity> resolved) {
        String lengthUnit = null;
        String angleUnit = null;
        Double scaleToMeters = null;

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepGlobalUnitAssignedContext ctx) {
                for (StepEntity unit : ctx.units()) {
                    if (unit instanceof StepSiUnit si) {
                        if ("LENGTH_UNIT".equals(si.unitKind())) {
                            lengthUnit = formatSiUnit(si);
                            scaleToMeters = computeScaleToMeters(si);
                        } else if ("PLANE_ANGLE_UNIT".equals(si.unitKind())) {
                            angleUnit = formatSiUnit(si);
                        }
                    } else if (unit instanceof StepNamedUnit named) {
                        // NamedUnit has no unitName, just unitKind
                        if ("LENGTH_UNIT".equals(named.unitKind())) {
                            lengthUnit = "named_length_unit";
                        } else if ("PLANE_ANGLE_UNIT".equals(named.unitKind())) {
                            angleUnit = "named_angle_unit";
                        }
                    } else if (unit instanceof StepConversionBasedUnit conv) {
                        if ("LENGTH_UNIT".equals(conv.unitKind())) {
                            lengthUnit = conv.name() != null ? conv.name() : "conversion_unit";
                        } else if ("PLANE_ANGLE_UNIT".equals(conv.unitKind())) {
                            angleUnit = conv.name() != null ? conv.name() : "conversion_unit";
                        }
                    }
                }
            }
            // Also scan standalone units not referenced by GLOBAL_UNIT_ASSIGNED_CONTEXT
            if (entity instanceof StepSiUnit si) {
                if (lengthUnit == null && "LENGTH_UNIT".equals(si.unitKind())) {
                    lengthUnit = formatSiUnit(si);
                    scaleToMeters = computeScaleToMeters(si);
                }
                if (angleUnit == null && "PLANE_ANGLE_UNIT".equals(si.unitKind())) {
                    angleUnit = formatSiUnit(si);
                }
            }
        }

        return new UnitInfo(lengthUnit, scaleToMeters, angleUnit);
    }

    private static String formatSiUnit(StepSiUnit si) {
        if (si.prefix() != null) {
            return si.prefix().toLowerCase() + si.unitName().toLowerCase();
        }
        return si.unitName().toLowerCase();
    }

    private static Double computeScaleToMeters(StepSiUnit si) {
        if (!"METRE".equals(si.unitName()) && !"MILLIMETRE".equals(si.unitName())) {
            Double base = BASE_UNITS_TO_METERS.get(si.unitName());
            if (base != null) {
                double prefixMult = si.prefix() != null
                        ? SI_PREFIXES.getOrDefault(si.prefix(), 1.0)
                        : 1.0;
                return base * prefixMult;
            }
            return null;
        }
        // METRE with prefix
        double prefixMult = si.prefix() != null
                ? SI_PREFIXES.getOrDefault(si.prefix(), 1.0)
                : 1.0;
        return prefixMult;
    }
}
