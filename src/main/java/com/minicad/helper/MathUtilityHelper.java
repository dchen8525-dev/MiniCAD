package com.minicad.helper.geometry;

import com.minicad.geometry.CartesianPoint;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.VectorPayload;

/**
 * Helper class for mathematical utility methods extracted from StepPreviewJsonExporter.
 */
public final class MathUtilityHelper {

    public static double unwrapPeriodic(double value, Double previous, double period) {
        if (previous == null) {
            return value;
        }
        while (value - previous > period * 0.5) {
            value -= period;
        }
        while (value - previous < -period * 0.5) {
            value += period;
        }
        return value;
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double[] inverseUniformScaleTransform(double[] matrix) {
        double sx = Math.sqrt(matrix[0] * matrix[0] + matrix[4] * matrix[4] + matrix[8] * matrix[8]);
        double sy = Math.sqrt(matrix[1] * matrix[1] + matrix[5] * matrix[5] + matrix[9] * matrix[9]);
        double sz = Math.sqrt(matrix[2] * matrix[2] + matrix[6] * matrix[6] + matrix[10] * matrix[10]);
        if (sx <= 1.0e-12 || sy <= 1.0e-12 || sz <= 1.0e-12) {
            return null;
        }
        double maxScale = Math.max(sx, Math.max(sy, sz));
        double tolerance = maxScale * 1.0e-6;
        if (Math.abs(sx - sy) > tolerance || Math.abs(sx - sz) > tolerance || Math.abs(sy - sz) > tolerance) {
            return null;
        }
        double n01 = ((matrix[0] / sx) * (matrix[1] / sy)) + ((matrix[4] / sx) * (matrix[5] / sy)) + ((matrix[8] / sx) * (matrix[9] / sy));
        double n02 = ((matrix[0] / sx) * (matrix[2] / sz)) + ((matrix[4] / sx) * (matrix[6] / sz)) + ((matrix[8] / sx) * (matrix[10] / sz));
        double n12 = ((matrix[1] / sy) * (matrix[2] / sz)) + ((matrix[5] / sy) * (matrix[6] / sz)) + ((matrix[9] / sy) * (matrix[10] / sz));
        if (Math.abs(n01) > 1.0e-6 || Math.abs(n02) > 1.0e-6 || Math.abs(n12) > 1.0e-6) {
            return null;
        }
        double scale = (sx + sy + sz) / 3.0;
        double scaleSquared = scale * scale;
        if (scaleSquared <= 1.0e-18) {
            return null;
        }
        double tx = matrix[3];
        double ty = matrix[7];
        double tz = matrix[11];
        return new double[]{
                matrix[0] / scaleSquared, matrix[4] / scaleSquared, matrix[8] / scaleSquared,
                -((matrix[0] * tx) + (matrix[4] * ty) + (matrix[8] * tz)) / scaleSquared,
                matrix[1] / scaleSquared, matrix[5] / scaleSquared, matrix[9] / scaleSquared,
                -((matrix[1] * tx) + (matrix[5] * ty) + (matrix[9] * tz)) / scaleSquared,
                matrix[2] / scaleSquared, matrix[6] / scaleSquared, matrix[10] / scaleSquared,
                -((matrix[2] * tx) + (matrix[6] * ty) + (matrix[10] * tz)) / scaleSquared,
                0.0, 0.0, 0.0, 1.0
        };
    }

    public static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new CartesianPoint(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }

    public static VectorPayload transform(VectorPayload vector, double[] matrix) {
        double x = matrix[0] * vector.x() + matrix[1] * vector.y() + matrix[2] * vector.z();
        double y = matrix[4] * vector.x() + matrix[5] * vector.y() + matrix[6] * vector.z();
        double z = matrix[8] * vector.x() + matrix[9] * vector.y() + matrix[10] * vector.z();
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length <= 1.0e-12) {
            return vector;
        }
        return new VectorPayload(x / length, y / length, z / length);
    }

    public static PointPayload transform(PointPayload point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new PointPayload(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }
}