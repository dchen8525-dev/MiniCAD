package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import com.minicad.geometry2d.Curve2;

import java.util.List;
import java.util.Objects;

/**
 * Minimal surface-curve wrapper over a supported 3D curve.
 *
 * @param curve3d supported 3D curve
 * @param parametricCurves optional parameter-space curves associated with supporting surfaces
 */
/**
 * Minimal surface-curve wrapper over a supported 3D curve.
 *
 * @param curve3d supported 3D curve
 * @param parametricCurves optional parameter-space curves associated with supporting surfaces
 */
public final class SurfaceCurve3 implements Curve3 {
    private final Curve3 curve3d;
    private final List<ParametricCurve> parametricCurves;

    public static final class ParametricCurve {
        private final SurfaceGeometry surface;
        private final Curve2 curve2;

        public ParametricCurve(SurfaceGeometry surface, Curve2 curve2) {
            Preconditions.requireNonNull(surface, "surface");
            Preconditions.requireNonNull(curve2, "curve2");
            this.surface = surface;
            this.curve2 = curve2;
        }

        public SurfaceGeometry surface() { return surface; }
        public Curve2 curve2() { return curve2; }
        public SurfaceGeometry getSurface() { return surface; }
        public Curve2 getCurve2() { return curve2; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParametricCurve that = (ParametricCurve) o;
            return Objects.equals(surface, that.surface) && Objects.equals(curve2, that.curve2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(surface, curve2);
        }

        @Override
        public String toString() {
            return "ParametricCurve{surface=" + surface + ", curve2=" + curve2 + "}";
        }
    }

    public SurfaceCurve3(Curve3 curve3d, List<ParametricCurve> parametricCurves) {
        Preconditions.requireNonNull(curve3d, "curve3d");
        this.curve3d = curve3d;
        this.parametricCurves = parametricCurves == null ? null : java.util.List.copyOf(parametricCurves);
    }

    public SurfaceCurve3(Curve3 curve3d) {
        this(curve3d, List.of());
    }

    public Curve3 curve3d() {
        return curve3d;
    }

    public List<ParametricCurve> parametricCurves() {
        return parametricCurves;
    }

    // Java Bean getters
    public Curve3 getCurve3d() {
        return curve3d;
    }

    public List<ParametricCurve> getParametricCurves() {
        return parametricCurves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceCurve3 that = (SurfaceCurve3) o;
        return Objects.equals(curve3d, that.curve3d) && Objects.equals(parametricCurves, that.parametricCurves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curve3d, parametricCurves);
    }

    @Override
    public String toString() {
        return "SurfaceCurve3{" + "curve3d=" + curve3d + "parametricCurves=" + parametricCurves + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        return curve3d.pointAt(parameter);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return curve3d.contains(point);
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return curve3d.closestPointTo(point);
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            points.add(pointAt((double) i / segments));
        }
        return java.util.List.copyOf(points);
    }
}
