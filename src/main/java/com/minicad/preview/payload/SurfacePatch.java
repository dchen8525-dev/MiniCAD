package com.minicad.preview.payload;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import com.minicad.common.Epsilon;

import java.util.List;

/**
 * Surface patch payload for STEP preview export.
 */
public final class SurfacePatch {
    private final List<CartesianPoint> bottom;
    private final List<CartesianPoint> top;
    private final List<CartesianPoint> left;
    private final List<CartesianPoint> right;

    public SurfacePatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> top,
            List<CartesianPoint> left,
            List<CartesianPoint> right
    ) {
        this.bottom = PreviewPayloadCopies.copy(bottom);
        this.top = PreviewPayloadCopies.copy(top);
        this.left = PreviewPayloadCopies.copy(left);
        this.right = PreviewPayloadCopies.copy(right);
    }

    public List<CartesianPoint> getBottom() { return bottom; }
    public List<CartesianPoint> getTop() { return top; }
    public List<CartesianPoint> getLeft() { return left; }
    public List<CartesianPoint> getRight() { return right; }

    public int uSegments() { return bottom.size() - 1; }
    public int vSegments() { return left.size() - 1; }

    public CartesianPoint pointAt(double u, double v) {
        CartesianPoint c0 = sample(bottom, u);
        CartesianPoint c1 = sample(top, u);
        CartesianPoint d0 = sample(left, v);
        CartesianPoint d1 = sample(right, v);
        CartesianPoint p00 = bottom.get(0);
        CartesianPoint p10 = bottom.get(bottom.size() - 1);
        CartesianPoint p01 = top.get(0);
        CartesianPoint p11 = top.get(top.size() - 1);
        return bilinearBlend(c0, c1, d0, d1, p00, p10, p01, p11, u, v);
    }

    public Vector3 normalAt(double u, double v) {
        double du = Math.max(1.0 / Math.max(uSegments(), 1), 1.0e-3);
        double dv = Math.max(1.0 / Math.max(vSegments(), 1), 1.0e-3);
        CartesianPoint p = pointAt(u, v);
        CartesianPoint pu = pointAt(Math.min(1.0, u + du), v);
        CartesianPoint pv = pointAt(u, Math.min(1.0, v + dv));
        Vector3 normal = pu.subtract(p).cross(pv.subtract(p));
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        return normal.normalize().asVector();
    }

    private static CartesianPoint sample(List<CartesianPoint> polyline, double t) {
        double clamped = Math.max(0.0, Math.min(1.0, t));
        double scaled = clamped * (polyline.size() - 1);
        int low = Math.min((int) Math.floor(scaled), polyline.size() - 1);
        int high = Math.min(low + 1, polyline.size() - 1);
        double alpha = scaled - low;
        return interpolate(polyline.get(low), polyline.get(high), alpha);
    }

    private static CartesianPoint bilinearBlend(
            CartesianPoint c0,
            CartesianPoint c1,
            CartesianPoint d0,
            CartesianPoint d1,
            CartesianPoint p00,
            CartesianPoint p10,
            CartesianPoint p01,
            CartesianPoint p11,
            double u,
            double v
    ) {
        double x = (1.0 - v) * c0.x() + v * c1.x() + (1.0 - u) * d0.x() + u * d1.x()
                - ((1.0 - u) * (1.0 - v) * p00.x() + u * (1.0 - v) * p10.x()
                + (1.0 - u) * v * p01.x() + u * v * p11.x());
        double y = (1.0 - v) * c0.y() + v * c1.y() + (1.0 - u) * d0.y() + u * d1.y()
                - ((1.0 - u) * (1.0 - v) * p00.y() + u * (1.0 - v) * p10.y()
                + (1.0 - u) * v * p01.y() + u * v * p11.y());
        double z = (1.0 - v) * c0.z() + v * c1.z() + (1.0 - u) * d0.z() + u * d1.z()
                - ((1.0 - u) * (1.0 - v) * p00.z() + u * (1.0 - v) * p10.z()
                + (1.0 - u) * v * p01.z() + u * v * p11.z());
        return new CartesianPoint(x, y, z);
    }

    private static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() + (b.x() - a.x()) * alpha,
                a.y() + (b.y() - a.y()) * alpha,
                a.z() + (b.z() - a.z()) * alpha
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurfacePatch)) return false;
        SurfacePatch that = (SurfacePatch) o;
        return java.util.Objects.equals(bottom, that.bottom)
                && java.util.Objects.equals(top, that.top)
                && java.util.Objects.equals(left, that.left)
                && java.util.Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(bottom, top, left, right);
    }

    @Override
    public String toString() {
        return "SurfacePatch{bottom=" + bottom + ", top=" + top + ", left=" + left + ", right=" + right + "}";
    }
}