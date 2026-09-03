package com.minicad.step.semantic;

import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.DegenerateCurve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.step.model.*;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Loop;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.VertexLoop;

import java.util.List;

/**
 * Utility class for naming STEP entities.
 * Provides methods to convert STEP entity types to standardized string representations.
 */
public final class StepEntityNamingUtils {

    private StepEntityNamingUtils() {
        // Utility class
    }

    // stepEntityTypeName dispatch table (first-match-return, mirrors the original
    // sequential ifs). Branches that only return a constant literal and branches
    // that delegate to the entity's own entityName() both live here unchanged.
    private record EntityTypeNameRule(
            Class<? extends StepEntity> type, EntityTypeNameHandler handler) {}

    private interface EntityTypeNameHandler {
        String name(StepEntity entity);
    }

    private static EntityTypeNameRule entityTypeNameRule(
            Class<? extends StepEntity> type, EntityTypeNameHandler handler) {
        return new EntityTypeNameRule(type, handler);
    }

    private static final List<EntityTypeNameRule> ENTITY_TYPE_NAME_RULES = List.of(
        entityTypeNameRule(StepGeometricReplica.class, (entity) -> {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
            return replica.entityName();
        }),
        entityTypeNameRule(StepCsgPrimitive.class, (entity) -> {
            StepCsgPrimitive primitive = (StepCsgPrimitive) entity;
            return primitive.entityName();
        }),
        entityTypeNameRule(StepSweptAreaSolid.class, (entity) -> {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) entity;
            return sweptAreaSolid.entityName();
        }),
        entityTypeNameRule(StepConicCurve.class, (entity) -> {
            StepConicCurve conic = (StepConicCurve) entity;
            return conic.entityName();
        }),
        entityTypeNameRule(StepFaceBound.class, (entity) -> {
            StepFaceBound faceBound = (StepFaceBound) entity;
            return faceBound.isOuter() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }),
        entityTypeNameRule(StepProfileDef.class, (entity) -> {
            StepProfileDef profile = (StepProfileDef) entity;
            return profile.entityName();
        }),
        entityTypeNameRule(StepBooleanClippingResult.class, (entity) -> {
            return "BOOLEAN_CLIPPING_RESULT";
        }),
        entityTypeNameRule(StepBooleanResult.class, (entity) -> {
            return "BOOLEAN_RESULT";
        }),
        entityTypeNameRule(StepManifoldSolidBrep.class, (entity) -> {
            return "MANIFOLD_SOLID_BREP";
        }),
        entityTypeNameRule(StepBrepWithVoids.class, (entity) -> {
            return "BREP_WITH_VOIDS";
        }),
        entityTypeNameRule(StepCsgSolid.class, (entity) -> {
            return "CSG_SOLID";
        }),
        entityTypeNameRule(StepSolidReplica.class, (entity) -> {
            return "SOLID_REPLICA";
        }),
        entityTypeNameRule(StepLine.class, (entity) -> {
            return "LINE";
        }),
        entityTypeNameRule(StepCircle.class, (entity) -> {
            return "CIRCLE";
        }),
        entityTypeNameRule(StepEllipse.class, (entity) -> {
            return "ELLIPSE";
        }),
        entityTypeNameRule(StepPolyline.class, (entity) -> {
            return "POLYLINE";
        }),
        entityTypeNameRule(StepBSplineCurveWithKnots.class, (entity) -> {
            return "B_SPLINE_CURVE_WITH_KNOTS";
        }),
        entityTypeNameRule(StepRationalBSplineCurve.class, (entity) -> {
            return "RATIONAL_B_SPLINE_CURVE";
        }),
        entityTypeNameRule(StepTrimmedCurve.class, (entity) -> {
            return "TRIMMED_CURVE";
        }),
        entityTypeNameRule(StepSurfaceCurve.class, (entity) -> {
            return "SURFACE_CURVE";
        }),
        entityTypeNameRule(StepSeamCurve.class, (entity) -> {
            return "SEAM_CURVE";
        }),
        entityTypeNameRule(StepCompositeCurve.class, (entity) -> {
            return "COMPOSITE_CURVE";
        }),
        entityTypeNameRule(StepCompositeCurveOnSurface.class, (entity) -> {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }),
        entityTypeNameRule(StepOffsetCurve2D.class, (entity) -> {
            return "OFFSET_CURVE_2D";
        }),
        entityTypeNameRule(StepOffsetCurve3D.class, (entity) -> {
            return "OFFSET_CURVE_3D";
        }),
        entityTypeNameRule(StepOrientedCurve.class, (entity) -> {
            return "ORIENTED_CURVE";
        })
    );

    /**
     * Returns the STEP entity type name for the given entity.
     *
     * @param entity the STEP entity
     * @return the entity type name
     */
    static String stepEntityTypeName(StepEntity entity) {
        for (EntityTypeNameRule rule : ENTITY_TYPE_NAME_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().name(entity);
            }
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
     * Returns the curve type name for the given 3D curve. The single shared
     * implementation — every other curveTypeName copy delegates here.
     *
     * @param curve the 3D curve
     * @return the curve type name
     */
    public static String curveTypeName(Curve3 curve) {
        if (curve instanceof Line3) return "LINE";
        if (curve instanceof Circle) return "CIRCLE";
        if (curve instanceof Ellipse3) return "ELLIPSE";
        if (curve instanceof Parabola3) return "PARABOLA";
        if (curve instanceof Hyperbola3) return "HYPERBOLA";
        if (curve instanceof Clothoid3) return "CLOTHOID";
        if (curve instanceof DegenerateCurve3) return "DEGENERATE_CURVE";
        if (curve instanceof BSplineCurve3) return "B_SPLINE_CURVE";
        if (curve instanceof RationalBSplineCurve3) return "RATIONAL_B_SPLINE_CURVE";
        if (curve instanceof TrimmedCurve3) return "TRIMMED_CURVE";
        if (curve instanceof SurfaceCurve3) return "SURFACE_CURVE";
        if (curve instanceof Polyline3) return "POLYLINE";
        if (curve instanceof CompositeCurve3) return "COMPOSITE_CURVE";
        return curve.getClass().getSimpleName();
    }

    /**
     * Returns the curve type name for the given 2D curve. The single shared
     * implementation — every other curveTypeName copy delegates here.
     *
     * @param curve the 2D curve
     * @return the curve type name
     */
    public static String curveTypeName(Curve2 curve) {
        if (curve instanceof Line2) return "LINE";
        if (curve instanceof Circle2) return "CIRCLE";
        if (curve instanceof Ellipse2) return "ELLIPSE";
        if (curve instanceof Parabola2) return "PARABOLA";
        if (curve instanceof Hyperbola2) return "HYPERBOLA";
        if (curve instanceof DegenerateCurve2) return "DEGENERATE_CURVE";
        if (curve instanceof BSplineCurve2) return "B_SPLINE_CURVE";
        if (curve instanceof RationalBSplineCurve2) return "RATIONAL_B_SPLINE_CURVE";
        if (curve instanceof TrimmedCurve2) return "TRIMMED_CURVE";
        if (curve instanceof Polyline2) return "POLYLINE";
        if (curve instanceof CompositeCurve2) return "COMPOSITE_CURVE";
        return curve.getClass().getSimpleName();
    }
}
