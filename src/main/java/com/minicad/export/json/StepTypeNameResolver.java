package com.minicad.export.json;

import com.minicad.geometry.*;
import com.minicad.step.model.*;

import java.util.List;

/**
 * Utility class for resolving STEP entity type names.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides human-readable type names for STEP entities,
 * used in JSON export for debugging and display purposes.
 */
public final class StepTypeNameResolver {

    private StepTypeNameResolver() {
        // Utility class - prevent instantiation
    }

    /**
     * Returns the STEP entity type name for the given geometry entity.
     * Used for display and debugging in the preview JSON output.
     *
     * @param geometry the STEP entity, may be any geometry type
     * @return the STEP entity type name as a string
     */
    public static String surfaceTypeName(StepEntity geometry) {
        for (SurfaceTypeNameRule rule : SURFACE_TYPE_NAME_RULES) {
            if (rule.type().isInstance(geometry)) {
                return rule.handler().name(geometry);
            }
        }

        return geometry.getClass().getSimpleName();
    }

    /**
     * Returns the geometry type name for error reporting, distinguishing
     * between open shells (SHELL) and closed shells/solids (SOLID).
     *
     * @param entity the STEP entity, may be null
     * @return "SHELL" for open shells, "SOLID" for closed shells/solids,
     *         or surfaceTypeName for other entities
     */
    // surfaceTypeName dispatch table (first-match-return, mirrors the original sequential ifs).
    private record SurfaceTypeNameRule(Class<? extends StepEntity> type, SurfaceTypeNameHandler handler) {}

    private interface SurfaceTypeNameHandler {
        String name(StepEntity geometry);
    }

    private static SurfaceTypeNameRule surfaceTypeNameRule(
            Class<? extends StepEntity> type, SurfaceTypeNameHandler handler) {
        return new SurfaceTypeNameRule(type, handler);
    }

    private static final List<SurfaceTypeNameRule> SURFACE_TYPE_NAME_RULES = List.of(
        surfaceTypeNameRule(StepLine.class, (geometry) -> "LINE"),
        surfaceTypeNameRule(StepCircle.class, (geometry) -> "CIRCLE"),
        surfaceTypeNameRule(StepEllipse.class, (geometry) -> "ELLIPSE"),
        surfaceTypeNameRule(StepPolyline.class, (geometry) -> "POLYLINE"),
        surfaceTypeNameRule(StepBSplineCurve.class, (geometry) -> "B_SPLINE_CURVE"),
        surfaceTypeNameRule(StepBSplineCurveWithKnots.class, (geometry) -> "B_SPLINE_CURVE_WITH_KNOTS"),
        surfaceTypeNameRule(StepBezierCurve.class, (geometry) -> "BEZIER_CURVE"),
        surfaceTypeNameRule(StepUniformCurve.class, (geometry) -> "UNIFORM_CURVE"),
        surfaceTypeNameRule(StepQuasiUniformCurve.class, (geometry) -> "QUASI_UNIFORM_CURVE"),
        surfaceTypeNameRule(StepPiecewiseBezierCurve.class, (geometry) -> "PIECEWISE_BEZIER_CURVE"),
        surfaceTypeNameRule(StepRationalBSplineCurve.class, (geometry) -> "RATIONAL_B_SPLINE_CURVE"),
        surfaceTypeNameRule(StepOffsetCurve2D.class, (geometry) -> "OFFSET_CURVE_2D"),
        surfaceTypeNameRule(StepOffsetCurve3D.class, (geometry) -> "OFFSET_CURVE_3D"),
        surfaceTypeNameRule(StepTrimmedCurve.class, (geometry) -> "TRIMMED_CURVE"),
        surfaceTypeNameRule(StepSurfaceCurve.class, (geometry) -> "SURFACE_CURVE"),
        surfaceTypeNameRule(StepSeamCurve.class, (geometry) -> "SEAM_CURVE"),
        surfaceTypeNameRule(StepPcurve.class, (geometry) -> "PCURVE"),
        surfaceTypeNameRule(StepCompositeCurve.class, (geometry) -> "COMPOSITE_CURVE"),
        surfaceTypeNameRule(StepCompositeCurveOnSurface.class, (geometry) -> "COMPOSITE_CURVE_ON_SURFACE"),
        surfaceTypeNameRule(StepConicCurve.class, (geometry) -> {
            StepConicCurve conic = (StepConicCurve) geometry;
            return conic.entityName();
        }),
        surfaceTypeNameRule(StepOrientedCurve.class, (geometry) -> "ORIENTED_CURVE"),
        surfaceTypeNameRule(StepPath.class, (geometry) -> "PATH"),
        surfaceTypeNameRule(StepOpenPath.class, (geometry) -> "OPEN_PATH"),
        surfaceTypeNameRule(StepSubpath.class, (geometry) -> "SUBPATH"),
        surfaceTypeNameRule(StepOrientedPath.class, (geometry) -> "ORIENTED_PATH"),
        surfaceTypeNameRule(StepVertex.class, (geometry) -> "VERTEX"),
        surfaceTypeNameRule(StepVertexPoint.class, (geometry) -> "VERTEX_POINT"),
        surfaceTypeNameRule(StepEdgeCurve.class, (geometry) -> "EDGE_CURVE"),
        surfaceTypeNameRule(StepSubedge.class, (geometry) -> "SUBEDGE"),
        surfaceTypeNameRule(StepEdge.class, (geometry) -> "EDGE"),
        surfaceTypeNameRule(StepLoop.class, (geometry) -> "LOOP"),
        surfaceTypeNameRule(StepPolyLoop.class, (geometry) -> "POLY_LOOP"),
        surfaceTypeNameRule(StepEdgeLoop.class, (geometry) -> "EDGE_LOOP"),
        surfaceTypeNameRule(StepVertexLoop.class, (geometry) -> "VERTEX_LOOP"),
        surfaceTypeNameRule(StepFaceBound.class, (geometry) -> {
            StepFaceBound faceBound = (StepFaceBound) geometry;
            return faceBound.outer() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }),
        surfaceTypeNameRule(StepOrientedEdge.class, (geometry) -> "ORIENTED_EDGE"),
        surfaceTypeNameRule(StepOrientedFace.class, (geometry) -> "ORIENTED_FACE"),
        surfaceTypeNameRule(StepConnectedEdgeSet.class, (geometry) -> "CONNECTED_EDGE_SET"),
        surfaceTypeNameRule(StepConnectedFaceSubSet.class, (geometry) -> "CONNECTED_FACE_SUB_SET"),
        surfaceTypeNameRule(StepConnectedFaceSet.class, (geometry) -> "CONNECTED_FACE_SET"),
        surfaceTypeNameRule(StepOpenShell.class, (geometry) -> "OPEN_SHELL"),
        surfaceTypeNameRule(StepSurfacedOpenShell.class, (geometry) -> "SURFACED_OPEN_SHELL"),
        surfaceTypeNameRule(StepOrientedOpenShell.class, (geometry) -> "ORIENTED_OPEN_SHELL"),
        surfaceTypeNameRule(StepClosedShell.class, (geometry) -> "CLOSED_SHELL"),
        surfaceTypeNameRule(StepOrientedClosedShell.class, (geometry) -> "ORIENTED_CLOSED_SHELL"),
        surfaceTypeNameRule(StepWireShell.class, (geometry) -> "WIRE_SHELL"),
        surfaceTypeNameRule(StepVertexShell.class, (geometry) -> "VERTEX_SHELL"),
        surfaceTypeNameRule(StepShellBasedSurfaceModel.class, (geometry) -> "SHELL_BASED_SURFACE_MODEL"),
        surfaceTypeNameRule(StepFaceBasedSurfaceModel.class, (geometry) -> "FACE_BASED_SURFACE_MODEL"),
        surfaceTypeNameRule(StepEdgeBasedWireframeModel.class, (geometry) -> "EDGE_BASED_WIREFRAME_MODEL"),
        surfaceTypeNameRule(StepShellBasedWireframeModel.class, (geometry) -> "SHELL_BASED_WIREFRAME_MODEL"),
        surfaceTypeNameRule(StepGeometricCurveSet.class, (geometry) -> "GEOMETRIC_CURVE_SET"),
        surfaceTypeNameRule(StepGeometricSet.class, (geometry) -> "GEOMETRIC_SET"),
        surfaceTypeNameRule(StepRepresentation.class, (geometry) -> "REPRESENTATION"),
        surfaceTypeNameRule(StepRepresentationMap.class, (geometry) -> "REPRESENTATION_MAP"),
        surfaceTypeNameRule(StepRepresentationRelationshipWithTransformation.class, (geometry) -> "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION"),
        surfaceTypeNameRule(StepRepresentationRelationship.class, (geometry) -> "REPRESENTATION_RELATIONSHIP"),
        surfaceTypeNameRule(StepMappedItem.class, (geometry) -> "MAPPED_ITEM"),
        surfaceTypeNameRule(StepStyledItem.class, (geometry) -> "STYLED_ITEM"),
        surfaceTypeNameRule(StepOverRidingStyledItem.class, (geometry) -> "OVER_RIDING_STYLED_ITEM"),
        surfaceTypeNameRule(StepSurface.class, (geometry) -> "SURFACE"),
        surfaceTypeNameRule(StepBoundedSurface.class, (geometry) -> "BOUNDED_SURFACE"),
        surfaceTypeNameRule(StepBSplineSurface.class, (geometry) -> "B_SPLINE_SURFACE"),
        surfaceTypeNameRule(StepBezierSurface.class, (geometry) -> "BEZIER_SURFACE"),
        surfaceTypeNameRule(StepUniformSurface.class, (geometry) -> "UNIFORM_SURFACE"),
        surfaceTypeNameRule(StepQuasiUniformSurface.class, (geometry) -> "QUASI_UNIFORM_SURFACE"),
        surfaceTypeNameRule(StepPiecewiseBezierSurface.class, (geometry) -> "PIECEWISE_BEZIER_SURFACE"),
        surfaceTypeNameRule(StepPlane.class, (geometry) -> "PLANE"),
        surfaceTypeNameRule(StepCylindricalSurface.class, (geometry) -> "CYLINDRICAL_SURFACE"),
        surfaceTypeNameRule(StepConicalSurface.class, (geometry) -> "CONICAL_SURFACE"),
        surfaceTypeNameRule(StepToroidalSurface.class, (geometry) -> "TOROIDAL_SURFACE"),
        surfaceTypeNameRule(StepSphericalSurface.class, (geometry) -> "SPHERICAL_SURFACE"),
        surfaceTypeNameRule(StepDegenerateToroidalSurface.class, (geometry) -> "DEGENERATE_TOROIDAL_SURFACE"),
        surfaceTypeNameRule(StepSurfaceOfLinearExtrusion.class, (geometry) -> "SURFACE_OF_LINEAR_EXTRUSION"),
        surfaceTypeNameRule(StepSurfaceOfRevolution.class, (geometry) -> "SURFACE_OF_REVOLUTION"),
        surfaceTypeNameRule(StepRationalBSplineSurface.class, (geometry) -> "RATIONAL_B_SPLINE_SURFACE"),
        surfaceTypeNameRule(StepBSplineSurfaceWithKnots.class, (geometry) -> "B_SPLINE_SURFACE_WITH_KNOTS"),
        surfaceTypeNameRule(StepRectangularTrimmedSurface.class, (geometry) -> "RECTANGULAR_TRIMMED_SURFACE"),
        surfaceTypeNameRule(StepCurveBoundedSurface.class, (geometry) -> "CURVE_BOUNDED_SURFACE"),
        surfaceTypeNameRule(StepOrientedSurface.class, (geometry) -> "ORIENTED_SURFACE"),
        surfaceTypeNameRule(StepOffsetSurface.class, (geometry) -> "OFFSET_SURFACE"),
        surfaceTypeNameRule(StepSweptAreaSolid.class, (geometry) -> {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) geometry;
            return sweptAreaSolid.entityName();
        }),
        surfaceTypeNameRule(StepSolidReplica.class, (geometry) -> "SOLID_REPLICA"),
        surfaceTypeNameRule(StepManifoldSolidBrep.class, (geometry) -> "MANIFOLD_SOLID_BREP"),
        surfaceTypeNameRule(StepBrepWithVoids.class, (geometry) -> "BREP_WITH_VOIDS"),
        surfaceTypeNameRule(StepCsgSolid.class, (geometry) -> "CSG_SOLID"),
        surfaceTypeNameRule(StepCsgPrimitive.class, (geometry) -> {
            StepCsgPrimitive primitive = (StepCsgPrimitive) geometry;
            return primitive.entityName();
        }),
        surfaceTypeNameRule(StepBooleanClippingResult.class, (geometry) -> "BOOLEAN_CLIPPING_RESULT"),
        surfaceTypeNameRule(StepBooleanResult.class, (geometry) -> "BOOLEAN_RESULT"),
        surfaceTypeNameRule(StepSweptDiskSolid.class, (geometry) -> "SWEPT_DISK_SOLID"),
        surfaceTypeNameRule(StepComplexClippingResult.class, (geometry) -> "COMPLEX_CLIPPING_RESULT"),
        surfaceTypeNameRule(StepGeometricReplica.class, (geometry) -> {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            return replica.entityName();
        })
    );

    public static String geometryTypeName(StepEntity entity) {
        if (entity == null) {
            return "SOLID"; // Default for unknown entities in solid context
        }
        // Open shell types -> SHELL (surface, not a closed volume)
        if (entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell) {
            return "SHELL";
        }
        // Closed shell types and solid-like entities -> SOLID
        if (entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell
                || entity instanceof StepManifoldSolidBrep
                || entity instanceof StepFacettedBrep
                || entity instanceof StepNonManifoldSolidBrep
                || entity instanceof StepAdvancedBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepBooleanResult
                || entity instanceof StepSweptDiskSolid
                || entity instanceof StepExtrudedAreaSolidTapered
                || entity instanceof StepRevolvedAreaSolidTapered
                || entity instanceof StepSurfaceCurveSweptAreaSolid
                || entity instanceof StepPolygonalBoundedHalfSpace
                || entity instanceof StepComplexClippingResult
                || entity instanceof StepHalfSpaceSolid
                || entity instanceof StepCsgVolume
                || entity instanceof StepBlockVolume
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepMappedItem
                || entity instanceof StepSolidModel
                || entity instanceof StepSurfacePatch
                || entity instanceof StepExtrudedFaceSolid
                || entity instanceof StepRevolvedFaceSolid
                || entity instanceof StepSweptFaceSolid
                || entity instanceof StepCylinderVolume
                || entity instanceof StepSphereVolume
                || entity instanceof StepTorusVolume
                || entity instanceof StepPrismVolume
                || entity instanceof StepRightCircularConeVolume) {
            return "SOLID";
        }
        // For other entities, use the detailed type name from surfaceTypeName
        return surfaceTypeName(entity);
    }

    /**
     * Returns the type name for a SurfaceGeometry instance.
     * Used internally for face payload creation.
     *
     * @param surface the surface geometry
     * @return the surface type name
     */
    public static String surfaceTypeNameForGeometry(SurfaceGeometry surface) {
        if (surface instanceof Plane) return "PLANE";
        else if (surface instanceof CylindricalSurface) return "CYLINDRICAL_SURFACE";
        else if (surface instanceof ConicalSurface) return "CONICAL_SURFACE";
        else if (surface instanceof SphericalSurface) return "SPHERICAL_SURFACE";
        else if (surface instanceof ToroidalSurface) return "TOROIDAL_SURFACE";
        else if (surface instanceof BSplineSurface3) return "BSPLINE_SURFACE";
        else if (surface instanceof RationalBSplineSurface3) return "RATIONAL_BSPLINE_SURFACE";
        else if (surface instanceof RuledSurface3) return "RULED_SURFACE";
        else if (surface instanceof SurfaceOfRevolution3) return "SURFACE_OF_REVOLUTION";
        else if (surface instanceof OffsetSurface3) return "OFFSET_SURFACE";
        else if (surface instanceof SurfaceOfLinearExtrusion3) return "SURFACE_OF_LINEAR_EXTRUSION";
        else if (surface instanceof SurfaceOfConstantRadius3) return "SURFACE_OF_CONSTANT_RADIUS";
        else if (surface instanceof ParaboloidSurface) return "PARABOLOID_SURFACE";
        else if (surface instanceof HyperboloidSurface) return "HYPERBOLOID_SURFACE";
        else if (surface instanceof SurfaceOfTranslation3) return "SURFACE_OF_TRANSLATION";
        else if (surface instanceof SurfaceOfProjection3) return "SURFACE_OF_PROJECTION";
        else throw new IllegalArgumentException("Unknown surface type: " + surface.getClass().getSimpleName());
    }

    /**
     * Returns the STEP definition entity type name for display purposes.
     *
     * @param definition the STEP definition entity
     * @return the definition type name
     */
    public static String definitionTypeName(StepEntity definition) {
        String entityName = tryEntityName(definition);
        if (entityName != null) {
            return entityName;
        }
        if (definition instanceof StepAxis1Placement) {
            return "AXIS1_PLACEMENT";
        }
        if (definition instanceof StepAxis2Placement2D) {
            return "AXIS2_PLACEMENT_2D";
        }
        if (definition instanceof StepAxis2Placement3D) {
            return "AXIS2_PLACEMENT_3D";
        }
        return definition.getClass().getSimpleName().startsWith("Step")
                ? camelToStepLike(definition.getClass().getSimpleName().substring(4))
                : definition.getClass().getSimpleName();
    }

    /**
     * Returns the relationship type name (uses same logic as definitionTypeName).
     *
     * @param relationship the STEP relationship entity
     * @return the relationship type name
     */
    public static String relationshipTypeName(StepEntity relationship) {
        return definitionTypeName(relationship);
    }

    /**
     * Attempts to get entityName from a StepEntity using reflection.
     * Not every semantic record exposes entityName, so this may return null.
     *
     * @param definition the STEP entity
     * @return the entity name if available, null otherwise
     */
    private static String tryEntityName(StepEntity definition) {
        try {
            Object value = definition.getClass().getMethod("entityName").invoke(definition);
            if (value instanceof String) {
                String name = (String) value;
                return name;
            }
        } catch (ReflectiveOperationException ignored) {
            // Not every semantic record exposes entityName; fall back to explicit naming below.
        }
        return null;
    }

    /**
     * Converts a camelCase name to STEP-LIKE name format.
     * For example: "ProductDefinition" -> "PRODUCT_DEFINITION"
     *
     * @param value the camelCase name
     * @return the STEP-like name
     */
    private static String camelToStepLike(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (i > 0 && Character.isUpperCase(current)
                    && (Character.isLowerCase(value.charAt(i - 1))
                    || (i + 1 < value.length() && Character.isLowerCase(value.charAt(i + 1))))) {
                builder.append('_');
            } else if (i > 0 && Character.isDigit(current) && Character.isLetter(value.charAt(i - 1))) {
                builder.append('_');
            }
            builder.append(Character.toUpperCase(current));
        }
        return builder.toString();
    }
}
