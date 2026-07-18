package com.minicad.export.json;

import com.minicad.step.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for extracting curve metadata from STEP entities.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides methods to extract curve properties like
 * orientation, sense agreement, offset distance, and associated surface info.
 */
public final class StepCurveMetadataHelper {

    private StepCurveMetadataHelper() {
        // Utility class - prevent instantiation
    }

    /**
     * Extracts the orientation of an oriented curve.
     *
     * @param item the curve entity
     * @return the orientation, or null if not an oriented curve
     */
    public static Boolean previewCurveOrientation(StepEntity item) {
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return orientedCurve.orientation();
        }
        return null;
    }

    /**
     * Extracts the sense agreement for trimmed curves.
     *
     * @param item the curve entity
     * @return the sense agreement, or null if not a trimmed curve
     */
    public static Boolean previewCurveSenseAgreement(StepEntity item) {
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return trimmedCurve.senseAgreement();
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return trimmedCurve2D.senseAgreement();
        }
        return null;
    }

    /**
     * Extracts the offset distance for offset curves.
     *
     * @param item the curve entity
     * @return the offset distance, or null if not an offset curve
     */
    public static Double previewCurveOffsetDistance(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.distance();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.distance();
        }
        return null;
    }

    /**
     * Extracts the self-intersect flag for offset curves and composite curves.
     *
     * @param item the curve entity
     * @return the self-intersect flag, or null if not applicable
     */
    public static Boolean previewCurveSelfIntersect(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.selfIntersect();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.selfIntersect();
        }
        if (item instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeOnSurface = (StepCompositeCurveOnSurface) item;
            return compositeOnSurface.selfIntersect();
        }
        return null;
    }

    /**
     * Extracts the reference direction for 3D offset curves.
     *
     * @param item the curve entity
     * @return the reference direction ratios, or null if not available
     */
    public static List<Double> previewCurveRefDirection(StepEntity item) {
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return List.copyOf(offsetCurve3D.refDirection().directionRatios());
        }
        return null;
    }

    /**
     * Extracts the transform scale for curve replicas.
     *
     * @param item the curve entity
     * @return the scale factor, or null if not a curve replica
     */
    public static Double previewCurveTransformScale(StepEntity item) {
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return replica.transformation().scale();
        }
        return null;
    }

    /**
     * Extracts the master representation for surface curves.
     *
     * @param item the curve entity
     * @param semanticCurve the semantic curve item
     * @return the master representation string, or null if not available
     */
    public static String previewCurveMasterRepresentation(StepEntity item, StepEntity semanticCurve) {
        if (semanticCurve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) semanticCurve;
            return surfaceCurve.masterRepresentation();
        }
        if (semanticCurve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) semanticCurve;
            return seamCurve.masterRepresentation();
        }
        return null;
    }

    /**
     * Extracts associated surface types from PCURVE entities.
     *
     * @param associatedGeometry the associated geometry list
     * @return list of surface type names, or null if empty
     */
    public static List<String> previewCurveAssociatedSurfaceTypes(List<StepEntity> associatedGeometry) {
        if (associatedGeometry == null || associatedGeometry.isEmpty()) {
            return null;
        }
        List<String> surfaceTypes = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve) {
                StepPcurve pcurve = (StepPcurve) associated;
                surfaceTypes.add(StepTypeNameResolver.surfaceTypeName(pcurve.basisSurface()));
            } else if (associated instanceof StepDegeneratePcurve) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                surfaceTypes.add(StepTypeNameResolver.surfaceTypeName(pcurve.basisSurface()));
            }
        }
        return surfaceTypes.isEmpty() ? null : List.copyOf(surfaceTypes);
    }

    /**
     * Extracts associated surface STEP IDs from PCURVE entities.
     *
     * @param associatedGeometry the associated geometry list
     * @return list of surface IDs, or null if empty
     */
    public static List<Integer> previewCurveAssociatedSurfaceStepIds(List<StepEntity> associatedGeometry) {
        if (associatedGeometry == null || associatedGeometry.isEmpty()) {
            return null;
        }
        List<Integer> surfaceIds = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve) {
                StepPcurve pcurve = (StepPcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            } else if (associated instanceof StepDegeneratePcurve) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            }
        }
        return surfaceIds.isEmpty() ? null : List.copyOf(surfaceIds);
    }
}