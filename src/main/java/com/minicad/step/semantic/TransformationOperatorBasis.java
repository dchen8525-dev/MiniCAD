package com.minicad.step.semantic;

import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepCartesianTransformationOperator;

/**
 * The orthonormal frame and scale a CARTESIAN_TRANSFORMATION_OPERATOR resolves
 * to.
 *
 * <p>Every axis falls back to a derived value when the operator leaves it out:
 * a missing axis1 is +X; a missing axis2 is +Y unless that would be parallel to
 * axis1, in which case +Z; a missing axis3 is axis1 x axis2 unless that
 * degenerates, in which case +Z. A missing scale is 1.0.</p>
 *
 * <p>This resolution was written out four times — inlined on
 * {@code StepPointExtractor.transformPoint} and on
 * {@code StepPlacementTransformer.matrixForTransformationOperator}, and split
 * into a pair of private axis helpers on {@code StepCadGeometryOps} and
 * {@code StepMeshExporter}. The four agreed statement for statement, so folding
 * them is a de-duplication rather than a behaviour change.</p>
 *
 * <p>What genuinely differed stayed with the callers: this record carries no
 * orthogonality check, because {@code StepCadGeometryOps} validates the frame
 * and throws while {@code StepMeshExporter} and the json side transform with
 * whatever they are handed. Folding the check in here would make those two
 * reject operators they currently accept, and it is also why the record carries
 * the scale — the one derived value every caller needs — instead of only the
 * three axes.</p>
 */
public record TransformationOperatorBasis(Vector3 x, Vector3 y, Vector3 z, double scale) {

    /**
     * Resolves the frame of {@code transformation}, building axis directions
     * through {@code builder}.
     *
     * @param transformation the transformation operator
     * @param builder the CAD builder the axis directions and origin resolve through
     * @return the resolved frame and scale
     */
    public static TransformationOperatorBasis resolve(
            StepCartesianTransformationOperator transformation, StepCadBuilder builder) {
        Vector3 axis1 = xAxis(transformation, builder);
        Vector3 axis2 = yAxis(transformation, axis1, builder);
        Vector3 axis3 = zAxis(transformation, axis1, axis2, builder);
        return new TransformationOperatorBasis(axis1, axis2, axis3, scaleOf(transformation));
    }

    /**
     * The operator's scale, defaulting to {@code 1.0} when it is absent. Kept
     * separate from {@link #resolve} so a caller that only needs the scale does
     * not have to build the axis directions first.
     *
     * @param transformation the transformation operator
     * @return the scale, or 1.0 when the operator carries none
     */
    public static double scaleOf(StepCartesianTransformationOperator transformation) {
        return transformation.scale() == null ? 1.0 : transformation.scale();
    }

    private static Vector3 xAxis(
            StepCartesianTransformationOperator transformation, StepCadBuilder builder) {
        return transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
    }

    private static Vector3 yAxis(
            StepCartesianTransformationOperator transformation,
            Vector3 axis1,
            StepCadBuilder builder) {
        if (transformation.axis2() != null) {
            return builder.buildDirection(transformation.axis2().id()).asVector();
        }
        Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
        return axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
    }

    private static Vector3 zAxis(
            StepCartesianTransformationOperator transformation,
            Vector3 axis1,
            Vector3 axis2,
            StepCadBuilder builder) {
        if (transformation.axis3() != null) {
            return builder.buildDirection(transformation.axis3().id()).asVector();
        }
        Vector3 cross = axis1.cross(axis2);
        return cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
    }
}
