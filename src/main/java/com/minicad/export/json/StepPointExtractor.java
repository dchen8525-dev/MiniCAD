package com.minicad.export.json;

import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.*;

/**
 * Converts STEP {@code CARTESIAN_POINT} entities into {@link CartesianPoint} and
 * applies a CARTESIAN_TRANSFORMATION_OPERATOR to an already-converted point.
 *
 * <p>{@link #pointFromStep} pads a short coordinate list with zeroes instead of
 * indexing blindly, so a 2D point becomes a point on z = 0 rather than an
 * exception; it is the single home of that conversion on the export side.</p>
 *
 * <p>This class used to mirror the annotation-point stack of
 * {@code StepPmiPayloadBuilder} as well: its own {@code pointFromPlacement},
 * {@code pointFromAnnotationSymbol}, {@code pointFromAnnotationFillArea},
 * {@code pointFromReplica}, {@code pointFromAnnotationOccurrence} and
 * {@code pointFromAnnotationPoint} all had zero callers, and two of them were
 * null stubs, so no capability went with them; the live stack is the rule table
 * on {@code StepPmiPayloadBuilder}. Its {@code basisDirectionForNormal} was dead
 * too, and had already drifted from the live one on
 * {@code com.minicad.export.glb.PreviewMeshExporter} — a cross product here, a
 * Gram-Schmidt projection there. See StepPointExtractorConvergenceTest.</p>
 */
public final class StepPointExtractor {

    private StepPointExtractor() {
        // Utility class - prevent instantiation
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
