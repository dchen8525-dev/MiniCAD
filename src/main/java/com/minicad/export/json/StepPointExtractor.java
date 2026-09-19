package com.minicad.export.json;

import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.TransformationOperatorBasis;
import com.minicad.geometry.CartesianPoint;
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

    /**
     * Applies a CARTESIAN_TRANSFORMATION_OPERATOR to an already-converted point.
     *
     * <p>A one-line delegate to {@code TransformationOperatorBasis.transformPoint},
     * which is where the frame is resolved, scaled and translated. The name stays
     * because the callers sit in the json payload builders' frozen dispatch rows.
     * </p>
     *
     * @param point the point to transform
     * @param transformation the transformation operator
     * @param builder the CAD builder the frame resolves through
     * @return the transformed point
     */
    static CartesianPoint transformPoint(
            CartesianPoint point,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return TransformationOperatorBasis.transformPoint(point, transformation, builder);
    }

}
