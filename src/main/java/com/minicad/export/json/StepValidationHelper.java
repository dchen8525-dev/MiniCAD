package com.minicad.export.json;

import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.*;

import java.util.List;

/**
 * Utility class for STEP entity type validation and classification.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides boolean validation methods to classify
 * STEP entities into different categories for processing.
 */
public final class StepValidationHelper {

    private StepValidationHelper() {
        // Utility class - prevent instantiation
    }

    /**
     * Determines if the entity is a source for standalone edges.
     * Standalone edges are wireframe geometry, annotations, and curve sets
     * that should be rendered independently of shell/solid geometry.
     *
     * @param item the STEP entity to check
     * @return true if the entity is a standalone edge source
     */
    public static boolean isStandaloneEdgeSource(StepEntity item) {
        return item instanceof StepPolyline
                || item instanceof StepGeometricCurveSet
                || item instanceof StepGeometricSet
                || item instanceof StepShellBasedWireframeModel
                || item instanceof StepEdgeBasedWireframeModel
                || item instanceof StepConnectedEdgeSet
                || item instanceof StepEdgeWire
                || item instanceof StepPath
                || item instanceof StepOpenPath
                || item instanceof StepSubpath
                || item instanceof StepOrientedPath
                || item instanceof StepWireShell
                || item instanceof StepAnnotationCurveOccurrence
                || item instanceof StepAnnotationFillArea
                || item instanceof StepAnnotationFillAreaOccurrence
                || item instanceof StepAnnotationSymbol
                || item instanceof StepAnnotationSymbolOccurrence
                || item instanceof StepAnnotationSubfigureOccurrence
                || item instanceof StepFilletEdge
                || item instanceof StepChamferEdge
                || item instanceof StepSubedge
                || item instanceof StepAnnotationText
                || item instanceof StepAnnotationTextCharacter
                || item instanceof StepDimensionCurve
                || item instanceof StepLeaderCurve
                || item instanceof StepProjectionCurve
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepTerminatorSymbol
                || item instanceof StepGeometricSurfaceSet;
    }

    /**
     * Determines if the entity is a source for sampled curves.
     * Sampled curves are curve geometry that should be discretized
     * into polylines for rendering.
     *
     * @param item the STEP entity to check
     * @return true if the entity should be sampled as a curve
     */
    public static boolean isSampledCurveSource(StepEntity item) {
        return item instanceof StepLine
                || item instanceof StepCircle
                || item instanceof StepEllipse
                || item instanceof StepConicCurve
                || item instanceof StepBezierCurve
                || item instanceof StepUniformCurve
                || item instanceof StepQuasiUniformCurve
                || item instanceof StepPiecewiseBezierCurve
                || item instanceof StepBSplineCurveWithKnots
                || item instanceof StepBSplineCurve
                || item instanceof StepRationalBSplineCurve
                || item instanceof StepSurfaceCurve
                || item instanceof StepSeamCurve
                || item instanceof StepTrimmedCurve
                || item instanceof StepPolyline
                || item instanceof StepCompositeCurve
                || item instanceof StepCompositeCurveOnSurface
                || item instanceof StepCompositeCurveOnSurface3D
                || item instanceof StepOffsetCurve2D
                || item instanceof StepOffsetCurve3D
                || item instanceof StepPcurve
                || item instanceof StepDegeneratePcurve
                || item instanceof StepOrientedCurve
                || item instanceof StepAnnotationCurveOccurrence
                || item instanceof StepDimensionCurve
                || item instanceof StepLeaderCurve
                || item instanceof StepProjectionCurve
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepTerminatorSymbol
                || item instanceof StepClothoid
                || item instanceof StepIndexedPolyCurve
                || item instanceof StepDegenerateCurve
                || item instanceof StepBSplineCurveWithKnotsAndBreakpoints
                || item instanceof StepLineSegment
                || item instanceof StepEdgeCurve
                || item instanceof StepSurfacedEdgeCurve
                || item instanceof StepPath
                || item instanceof StepOpenPath
                || item instanceof StepSubpath
                || item instanceof StepOrientedPath
                || item instanceof StepCurve
                || item instanceof StepBoundedCurve
                || item instanceof StepCircle2D
                || item instanceof StepEllipse2D
                || item instanceof StepPolyline2D
                || item instanceof StepTrimmedCurve2D
                || item instanceof StepCompositeCurve2D
                || item instanceof StepBezierCurve2D
                || item instanceof StepQuasiUniformCurve2D
                || item instanceof StepUniformCurve2D
                || item instanceof StepPiecewiseBezierCurve2D
                || item instanceof StepIndexedPolyCurve2D
                || item instanceof StepDegenerateCurve2D
                || item instanceof StepBSplineCurve2D
                || item instanceof StepRationalBSplineCurve2D
                || item instanceof StepHyperbola2D
                || item instanceof StepParabola2D
                || item instanceof StepLine2D
                || item instanceof StepCurve2D
                || item instanceof StepBoundedCurve2D
                || (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName()));
    }

    /**
     * Determines if the entity is a supported PMI usage carrier.
     * PMI (Product Manufacturing Information) carriers are entities
     * that can carry semantic annotations.
     *
     * @param entity the STEP entity to check
     * @return true if the entity is a supported PMI carrier
     */
    public static boolean isSupportedPmiUsageCarrier(StepEntity entity) {
        return entity instanceof StepAnnotationCurveOccurrence
                || entity instanceof StepAnnotationFillArea
                || entity instanceof StepAnnotationFillAreaOccurrence
                || entity instanceof StepAnnotationSymbol
                || entity instanceof StepAnnotationSymbolOccurrence
                || entity instanceof StepAnnotationSubfigureOccurrence
                || entity instanceof StepAnnotationText
                || entity instanceof StepAnnotationTextCharacter
                || entity instanceof StepAnnotationTextOccurrence
                || entity instanceof StepAnnotationPlane
                || entity instanceof StepAnnotationPlaceholderOccurrence
                || entity instanceof StepAnnotationPointOccurrence
                || entity instanceof StepDimensionCurve
                || entity instanceof StepLeaderCurve
                || entity instanceof StepProjectionCurve
                || entity instanceof StepDraughtingAnnotationOccurrence
                || entity instanceof StepFilletEdge
                || entity instanceof StepChamferEdge
                || entity instanceof StepSubedge
                || entity instanceof StepGeometricSurfaceSet;
    }

    /**
     * Checks if a face entity has the same sense as its surface.
     *
     * @param stepFace the face entity
     * @return true if the face has same sense, false otherwise
     */
    public static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.sameSense();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.sameSense();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            boolean base = faceSameSense(orientedFace.faceElement());
            return orientedFace.orientation() ? base : !base;
        }
        return true;
    }

    /**
     * Checks if an entity is a representation solid item.
     * These are entities that should be processed as solid geometry.
     *
     * @param entity the STEP entity to check
     * @return true if the entity is a representation solid item
     */
    public static boolean isRepresentationSolidItem(StepEntity entity) {
        return entity instanceof StepManifoldSolidBrep
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
                || entity instanceof StepSolidModel;
    }

    /**
     * Checks if corners of two geometry elements match.
     * Used for validation of closed loops.
     *
     * @param corners1 first set of corner points
     * @param corners2 second set of corner points
     * @return true if all corners match within tolerance
     */
    public static boolean cornersMatch(List<CartesianPoint> corners1, List<CartesianPoint> corners2) {
        if (corners1.size() != corners2.size()) {
            return false;
        }
        for (int i = 0; i < corners1.size(); i++) {
            if (!close(corners1.get(i), corners2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two points are close (within tolerance).
     *
     * @param left first point
     * @param right second point
     * @return true if points are within tolerance
     */
    private static boolean close(CartesianPoint left, CartesianPoint right) {
        double tolerance = 1e-6;
        return Math.abs(left.x() - right.x()) < tolerance
                && Math.abs(left.y() - right.y()) < tolerance
                && Math.abs(left.z() - right.z()) < tolerance;
    }

    /**
     * Computes a mapped payload ID from source IDs.
     * Used to generate unique IDs for mapped geometry.
     *
     * @param mappedItemId the mapped item ID
     * @param sourceId the source entity ID
     * @param salt a salt value to ensure uniqueness
     * @return the computed payload ID
     */
    public static int mappedPayloadId(int mappedItemId, int sourceId, int salt) {
        return mappedItemId * 1000000 + sourceId * 10 + salt;
    }
}
