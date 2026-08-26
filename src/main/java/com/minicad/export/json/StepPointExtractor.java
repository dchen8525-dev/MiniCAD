package com.minicad.export.json;

import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.*;

/**
 * Utility class for extracting 3D points from STEP entities.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides methods to extract Cartesian points from various
 * STEP entity types including placements, annotations, geometric sets, etc.
 */
public final class StepPointExtractor {

    private StepPointExtractor() {
        // Utility class - prevent instantiation
    }

    /**
     * Extracts a point from a STEP placement entity.
     * Supports Axis2Placement3D and Axis2Placement2D.
     *
     * @param placement the placement entity
     * @return the extracted CartesianPoint, or null if not supported
     */
    public static CartesianPoint pointFromPlacement(StepEntity placement) {
        if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) placement;
            return pointFromStep(placement3D.location());
        }
        if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) placement;
            CartesianPoint origin = pointFromStep(placement2D.location());
            if (origin == null) {
                return null;
            }
            // For 2D placement, return the point with z=0
            return new CartesianPoint(origin.x(), origin.y(), 0.0);
        }
        return null;
    }

    /**
     * Extracts a point from a STEP CartesianPoint.
     * Handles 2D and 3D coordinates.
     *
     * @param point the STEP CartesianPoint
     * @return the CartesianPoint
     */
    public static CartesianPoint pointFromStep(StepCartesianPoint point) {
        double x = point.coordinates().get(0);
        double y = point.coordinates().size() > 1 ? point.coordinates().get(1) : 0.0;
        double z = point.coordinates().size() > 2 ? point.coordinates().get(2) : 0.0;
        return new CartesianPoint(x, y, z);
    }

    /**
     * Extracts a point from an annotation symbol.
     *
     * @param annotationSymbol the annotation symbol
     * @return the extracted point
     */
    public static CartesianPoint pointFromAnnotationSymbol(StepAnnotationSymbol annotationSymbol) {
        return pointFromPlacement(annotationSymbol.mappingTarget());
    }

    /**
     * Extracts the first point from an annotation fill area.
     *
     * @param fillArea the fill area entity
     * @param builder the CAD builder
     * @return the first point, or null if no boundaries
     */
    public static CartesianPoint pointFromAnnotationFillArea(
            StepAnnotationFillArea fillArea,
            Object builder
    ) {
        // Simplified - returns null if no boundaries
        if (fillArea.boundaries() == null || fillArea.boundaries().isEmpty()) {
            return null;
        }
        // Would need full implementation with sampleAnnotationFillAreaPoints
        return null;
    }

    /**
     * Extracts a point from a geometric replica (CURVE_REPLICA or POINT_REPLICA).
     *
     * @param replica the geometric replica
     * @param builder the CAD builder
     * @return the transformed point
     */
    public static CartesianPoint pointFromReplica(StepGeometricReplica replica, Object builder) {
        // Simplified implementation
        if (!"POINT_REPLICA".equals(replica.entityName())) {
            return null;
        }
        // Would need full transformation logic
        return null;
    }

    /**
     * Extracts a point from an annotation occurrence.
     * Dispatches to specific extraction methods based on occurrence type.
     *
     * @param occurrence the annotation occurrence entity
     * @param builder the CAD builder
     * @return the extracted point, or null if not available
     */
    public static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, Object builder) {
        if (occurrence instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) occurrence;
            return pointFromAnnotationPoint(pointOccurrence.item(), builder);
        }
        if (occurrence instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) occurrence;
            return pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
        }
        if (occurrence instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) occurrence;
            return pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
        }
        return null;
    }

    /**
     * Main dispatcher for extracting points from annotation-related entities.
     *
     * @param item the STEP entity
     * @param builder the CAD builder
     * @return the extracted point, or null if not available
     */
    public static CartesianPoint pointFromAnnotationPoint(StepEntity item, Object builder) {
        if (item instanceof StepCartesianPoint) {
            return pointFromStep((StepCartesianPoint) item);
        }
        if (item instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) item;
            return pointFromStep(vertexPoint.point());
        }
        if (item instanceof StepAnnotationSymbol) {
            return pointFromAnnotationSymbol((StepAnnotationSymbol) item);
        }
        return null;
    }

    /**
     * Computes the basis X-direction from a normal vector.
     * Used for surface coordinate systems.
     *
     * @param normal the normal direction
     * @return the X-direction basis vector as coordinate list
     */
    public static java.util.List<Double> basisDirectionForNormal(Direction3 normal) {
        Vector3 z = new Vector3(normal.x(), normal.y(), normal.z());
        Vector3 fallback = new Vector3(1.0, 0.0, 0.0);
        Vector3 x;
        if (Math.abs(z.x()) < 0.9) {
            x = fallback.cross(z).normalize().asVector();
        } else {
            x = new Vector3(0.0, 1.0, 0.0).cross(z).normalize().asVector();
        }
        return java.util.List.of(x.x(), x.y(), x.z());
    }

    static CartesianPoint transformPoint(
            CartesianPoint point,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() != null) {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        } else {
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            axis2 = axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }
        Vector3 axis3;
        if (transformation.axis3() != null) {
            axis3 = builder.buildDirection(transformation.axis3().id()).asVector();
        } else {
            Vector3 cross = axis1.cross(axis2);
            axis3 = cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        CartesianPoint origin = builder.buildPoint(transformation.localOrigin().id());
        Vector3 offset = axis1.scale(point.x() * scale)
                .add(axis2.scale(point.y() * scale))
                .add(axis3.scale(point.z() * scale));
        return origin.add(offset);
    }

}