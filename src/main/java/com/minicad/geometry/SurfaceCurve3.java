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

    public SurfaceCurve3(Curve3 curve3d, List<ParametricCurve> parametricCurves) {
        this.curve3d = curve3d;
        this.parametricCurves = parametricCurves == null ? null : java.util.List.copyOf(parametricCurves);
    }

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
}
