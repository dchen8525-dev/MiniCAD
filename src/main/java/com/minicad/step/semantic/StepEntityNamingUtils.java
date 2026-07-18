package com.minicad.step.semantic;

import com.minicad.geometry.Curve3;
import com.minicad.geometry2d.Curve2;
import com.minicad.step.model.*;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Loop;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.VertexLoop;

/**
 * Utility class for naming STEP entities.
 * Provides methods to convert STEP entity types to standardized string representations.
 */
final class StepEntityNamingUtils {

    private StepEntityNamingUtils() {
        // Utility class
    }

    /**
     * Returns the STEP entity type name for the given entity.
     *
     * @param entity the STEP entity
     * @return the entity type name
     */
    static String stepEntityTypeName(StepEntity entity) {
        if (entity instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
            return replica.entityName();
        }
        if (entity instanceof StepCsgPrimitive) {
            StepCsgPrimitive primitive = (StepCsgPrimitive) entity;
            return primitive.entityName();
        }
        if (entity instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) entity;
            return sweptAreaSolid.entityName();
        }
        if (entity instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) entity;
            return conic.entityName();
        }
        if (entity instanceof StepFaceBound) {
            StepFaceBound faceBound = (StepFaceBound) entity;
            return faceBound.isOuter() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }
        if (entity instanceof StepProfileDef) {
            StepProfileDef profile = (StepProfileDef) entity;
            return profile.entityName();
        }
        if (entity instanceof StepBooleanClippingResult) {
            return "BOOLEAN_CLIPPING_RESULT";
        }
        if (entity instanceof StepBooleanResult) {
            return "BOOLEAN_RESULT";
        }
        if (entity instanceof StepManifoldSolidBrep) {
            return "MANIFOLD_SOLID_BREP";
        }
        if (entity instanceof StepBrepWithVoids) {
            return "BREP_WITH_VOIDS";
        }
        if (entity instanceof StepCsgSolid) {
            return "CSG_SOLID";
        }
        if (entity instanceof StepSolidReplica) {
            return "SOLID_REPLICA";
        }
        if (entity instanceof StepLine) {
            return "LINE";
        }
        if (entity instanceof StepCircle) {
            return "CIRCLE";
        }
        if (entity instanceof StepEllipse) {
            return "ELLIPSE";
        }
        if (entity instanceof StepPolyline) {
            return "POLYLINE";
        }
        if (entity instanceof StepBSplineCurveWithKnots) {
            return "B_SPLINE_CURVE_WITH_KNOTS";
        }
        if (entity instanceof StepRationalBSplineCurve) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (entity instanceof StepTrimmedCurve) {
            return "TRIMMED_CURVE";
        }
        if (entity instanceof StepSurfaceCurve) {
            return "SURFACE_CURVE";
        }
        if (entity instanceof StepSeamCurve) {
            return "SEAM_CURVE";
        }
        if (entity instanceof StepCompositeCurve) {
            return "COMPOSITE_CURVE";
        }
        if (entity instanceof StepCompositeCurveOnSurface) {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }
        if (entity instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (entity instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (entity instanceof StepOrientedCurve) {
            return "ORIENTED_CURVE";
        }
        String simpleName = entity.getClass().getSimpleName();
        if (simpleName.startsWith("Step")) {
            simpleName = simpleName.substring(4);
        }
        return camelToUpperSnake(simpleName);
    }

    /**
     * Converts a camelCase string to UPPER_SNAKE_CASE.
     *
     * @param value the camelCase string
     * @return the UPPER_SNAKE_CASE string
     */
    static String camelToUpperSnake(String value) {
        if (value.isEmpty()) {
            return value;
        }
        String normalized = value
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        return normalized.toUpperCase(java.util.Locale.ROOT);
    }

    /**
     * Returns the loop type name for the given loop.
     *
     * @param loop the loop
     * @return the loop type name
     */
    static String loopTypeName(Loop loop) {
        if (loop instanceof EdgeLoop) {
            return "EDGE_LOOP";
        }
        if (loop instanceof VertexLoop) {
            return "VERTEX_LOOP";
        }
        if (loop instanceof PolyLoop) {
            return "POLY_LOOP";
        }
        return loop.getClass().getSimpleName();
    }

    /**
     * Returns the curve type name for the given 3D curve.
     *
     * @param curve the 3D curve
     * @return the curve type name
     */
    static String curveTypeName(Curve3 curve) {
        return StepCadGeometryOps.curveTypeName(curve);
    }

    /**
     * Returns the curve type name for the given 2D curve.
     *
     * @param curve the 2D curve
     * @return the curve type name
     */
    static String curveTypeName(Curve2 curve) {
        return StepCadGeometryOps.curveTypeName(curve);
    }
}