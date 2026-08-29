package com.minicad.export.json;

import com.minicad.geometry.*;
import com.minicad.step.model.*;

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
        if (geometry instanceof StepLine) {
            return "LINE";
        }
        if (geometry instanceof StepCircle) {
            return "CIRCLE";
        }
        if (geometry instanceof StepEllipse) {
            return "ELLIPSE";
        }
        if (geometry instanceof StepPolyline) {
            return "POLYLINE";
        }
        if (geometry instanceof StepBSplineCurve) {
            return "B_SPLINE_CURVE";
        }
        if (geometry instanceof StepBSplineCurveWithKnots) {
            return "B_SPLINE_CURVE_WITH_KNOTS";
        }
        if (geometry instanceof StepBezierCurve) {
            return "BEZIER_CURVE";
        }
        if (geometry instanceof StepUniformCurve) {
            return "UNIFORM_CURVE";
        }
        if (geometry instanceof StepQuasiUniformCurve) {
            return "QUASI_UNIFORM_CURVE";
        }
        if (geometry instanceof StepPiecewiseBezierCurve) {
            return "PIECEWISE_BEZIER_CURVE";
        }
        if (geometry instanceof StepRationalBSplineCurve) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (geometry instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (geometry instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (geometry instanceof StepTrimmedCurve) {
            return "TRIMMED_CURVE";
        }
        if (geometry instanceof StepSurfaceCurve) {
            return "SURFACE_CURVE";
        }
        if (geometry instanceof StepSeamCurve) {
            return "SEAM_CURVE";
        }
        if (geometry instanceof StepPcurve) {
            return "PCURVE";
        }
        if (geometry instanceof StepCompositeCurve) {
            return "COMPOSITE_CURVE";
        }
        if (geometry instanceof StepCompositeCurveOnSurface) {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }
        if (geometry instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) geometry;
            return conic.entityName();
        }
        if (geometry instanceof StepOrientedCurve) {
            return "ORIENTED_CURVE";
        }
        if (geometry instanceof StepPath) {
            return "PATH";
        }
        if (geometry instanceof StepOpenPath) {
            return "OPEN_PATH";
        }
        if (geometry instanceof StepSubpath) {
            return "SUBPATH";
        }
        if (geometry instanceof StepOrientedPath) {
            return "ORIENTED_PATH";
        }
        if (geometry instanceof StepVertex) {
            return "VERTEX";
        }
        if (geometry instanceof StepVertexPoint) {
            return "VERTEX_POINT";
        }
        if (geometry instanceof StepEdgeCurve) {
            return "EDGE_CURVE";
        }
        if (geometry instanceof StepSubedge) {
            return "SUBEDGE";
        }
        if (geometry instanceof StepEdge) {
            return "EDGE";
        }
        if (geometry instanceof StepLoop) {
            return "LOOP";
        }
        if (geometry instanceof StepPolyLoop) {
            return "POLY_LOOP";
        }
        if (geometry instanceof StepEdgeLoop) {
            return "EDGE_LOOP";
        }
        if (geometry instanceof StepVertexLoop) {
            return "VERTEX_LOOP";
        }
        if (geometry instanceof StepFaceBound) {
            StepFaceBound faceBound = (StepFaceBound) geometry;
            return faceBound.outer() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }
        if (geometry instanceof StepOrientedEdge) {
            return "ORIENTED_EDGE";
        }
        if (geometry instanceof StepOrientedFace) {
            return "ORIENTED_FACE";
        }
        if (geometry instanceof StepConnectedEdgeSet) {
            return "CONNECTED_EDGE_SET";
        }
        if (geometry instanceof StepConnectedFaceSubSet) {
            return "CONNECTED_FACE_SUB_SET";
        }
        if (geometry instanceof StepConnectedFaceSet) {
            return "CONNECTED_FACE_SET";
        }
        if (geometry instanceof StepOpenShell) {
            return "OPEN_SHELL";
        }
        if (geometry instanceof StepSurfacedOpenShell) {
            return "SURFACED_OPEN_SHELL";
        }
        if (geometry instanceof StepOrientedOpenShell) {
            return "ORIENTED_OPEN_SHELL";
        }
        if (geometry instanceof StepClosedShell) {
            return "CLOSED_SHELL";
        }
        if (geometry instanceof StepOrientedClosedShell) {
            return "ORIENTED_CLOSED_SHELL";
        }
        if (geometry instanceof StepWireShell) {
            return "WIRE_SHELL";
        }
        if (geometry instanceof StepVertexShell) {
            return "VERTEX_SHELL";
        }
        if (geometry instanceof StepShellBasedSurfaceModel) {
            return "SHELL_BASED_SURFACE_MODEL";
        }
        if (geometry instanceof StepFaceBasedSurfaceModel) {
            return "FACE_BASED_SURFACE_MODEL";
        }
        if (geometry instanceof StepEdgeBasedWireframeModel) {
            return "EDGE_BASED_WIREFRAME_MODEL";
        }
        if (geometry instanceof StepShellBasedWireframeModel) {
            return "SHELL_BASED_WIREFRAME_MODEL";
        }
        if (geometry instanceof StepGeometricCurveSet) {
            return "GEOMETRIC_CURVE_SET";
        }
        if (geometry instanceof StepGeometricSet) {
            return "GEOMETRIC_SET";
        }
        if (geometry instanceof StepRepresentation) {
            return "REPRESENTATION";
        }
        if (geometry instanceof StepRepresentationMap) {
            return "REPRESENTATION_MAP";
        }
        if (geometry instanceof StepRepresentationRelationshipWithTransformation) {
            return "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION";
        }
        if (geometry instanceof StepRepresentationRelationship) {
            return "REPRESENTATION_RELATIONSHIP";
        }
        if (geometry instanceof StepMappedItem) {
            return "MAPPED_ITEM";
        }
        if (geometry instanceof StepStyledItem) {
            return "STYLED_ITEM";
        }
        if (geometry instanceof StepOverRidingStyledItem) {
            return "OVER_RIDING_STYLED_ITEM";
        }
        if (geometry instanceof StepSurface) {
            return "SURFACE";
        }
        if (geometry instanceof StepBoundedSurface) {
            return "BOUNDED_SURFACE";
        }
        if (geometry instanceof StepBSplineSurface) {
            return "B_SPLINE_SURFACE";
        }
        if (geometry instanceof StepBezierSurface) {
            return "BEZIER_SURFACE";
        }
        if (geometry instanceof StepUniformSurface) {
            return "UNIFORM_SURFACE";
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            return "QUASI_UNIFORM_SURFACE";
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            return "PIECEWISE_BEZIER_SURFACE";
        }
        if (geometry instanceof StepPlane) {
            return "PLANE";
        }
        if (geometry instanceof StepCylindricalSurface) {
            return "CYLINDRICAL_SURFACE";
        }
        if (geometry instanceof StepConicalSurface) {
            return "CONICAL_SURFACE";
        }
        if (geometry instanceof StepToroidalSurface) {
            return "TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepSphericalSurface) {
            return "SPHERICAL_SURFACE";
        }
        if (geometry instanceof StepDegenerateToroidalSurface) {
            return "DEGENERATE_TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion) {
            return "SURFACE_OF_LINEAR_EXTRUSION";
        }
        if (geometry instanceof StepSurfaceOfRevolution) {
            return "SURFACE_OF_REVOLUTION";
        }
        if (geometry instanceof StepRationalBSplineSurface) {
            return "RATIONAL_B_SPLINE_SURFACE";
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            return "B_SPLINE_SURFACE_WITH_KNOTS";
        }
        if (geometry instanceof StepRectangularTrimmedSurface) {
            return "RECTANGULAR_TRIMMED_SURFACE";
        }
        if (geometry instanceof StepCurveBoundedSurface) {
            return "CURVE_BOUNDED_SURFACE";
        }
        if (geometry instanceof StepOrientedSurface) {
            return "ORIENTED_SURFACE";
        }
        if (geometry instanceof StepOffsetSurface) {
            return "OFFSET_SURFACE";
        }
        if (geometry instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) geometry;
            return sweptAreaSolid.entityName();
        }
        if (geometry instanceof StepSolidReplica) {
            return "SOLID_REPLICA";
        }
        if (geometry instanceof StepManifoldSolidBrep) {
            return "MANIFOLD_SOLID_BREP";
        }
        if (geometry instanceof StepBrepWithVoids) {
            return "BREP_WITH_VOIDS";
        }
        if (geometry instanceof StepCsgSolid) {
            return "CSG_SOLID";
        }
        if (geometry instanceof StepCsgPrimitive) {
            StepCsgPrimitive primitive = (StepCsgPrimitive) geometry;
            return primitive.entityName();
        }
        if (geometry instanceof StepBooleanClippingResult) {
            return "BOOLEAN_CLIPPING_RESULT";
        }
        if (geometry instanceof StepBooleanResult) {
            return "BOOLEAN_RESULT";
        }
        if (geometry instanceof StepSweptDiskSolid) {
            return "SWEPT_DISK_SOLID";
        }
        if (geometry instanceof StepComplexClippingResult) {
            return "COMPLEX_CLIPPING_RESULT";
        }
        if (geometry instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            return replica.entityName();
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
