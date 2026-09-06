package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.HyperboloidSurface;
import com.minicad.geometry.Line3;
import com.minicad.geometry.OffsetSurface3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.RuledSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfConstantRadius3;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfProjection3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.SurfaceOfTranslation3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.TrimmedCurve3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for reversing geometry sense (orientation) for STEP geometry objects.
 * Used when ORIENTED_EDGE or ORIENTED_SURFACE has orientation=false.
 *
 * This is the single home for curve/surface sense reversal: StepCadBuilder and
 * StepCadCurveBuilder each carried a private copy of the same two chains (29
 * branches in total, duplicated three times), and this class -- which already
 * held a third copy -- was never referenced at all. Both call sites now
 * delegate here.
 *
 * Every branch of both chains returns, so each is first-match-wins dispatch
 * with a terminal `return curve;` / `return surface;` fall-through for
 * unsupported geometry. They are now ordered (type, handler) rule lists; see
 * the frozen order files under src/test/resources for the branch order captured
 * from the original chains.
 */
public final class StepGeometryReverser {

    private StepGeometryReverser() {
        // Utility class - no instantiation
    }

    // ─── Curve3 sense reversal ───────────────────────────────────────────

    private interface Curve3Handler {
        Curve3 reverse(Curve3 curve);
    }

    private record Curve3Rule(Class<? extends Curve3> type, Curve3Handler handler) {}

    private static Curve3Rule curve3Rule(
            Class<? extends Curve3> type, Curve3Handler handler) {
        return new Curve3Rule(type, handler);
    }

    private static final List<Curve3Rule> REVERSE_CURVE3_RULES = List.of(
            curve3Rule(Line3.class, (curve) -> {
                Line3 line = (Line3) curve;
                return new Line3(line.getOrigin(), line.getDirection().reverse(), line.getParameterScale());
            }),
            curve3Rule(Polyline3.class, (curve) -> {
                Polyline3 polyline = (Polyline3) curve;
                List<CartesianPoint> reversedPoints = new ArrayList<>(polyline.getPoints());
                Collections.reverse(reversedPoints);
                return new Polyline3(reversedPoints);
            }),
            curve3Rule(CompositeCurve3.class, (curve) -> {
                CompositeCurve3 composite = (CompositeCurve3) curve;
                return reverseCompositeCurve(composite);
            }),
            curve3Rule(Circle.class, (curve) -> {
                Circle circle = (Circle) curve;
                Axis2Placement3D p = circle.getPosition();
                return new Circle(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        circle.getRadius());
            }),
            curve3Rule(Ellipse3.class, (curve) -> {
                Ellipse3 ellipse = (Ellipse3) curve;
                Axis2Placement3D p = ellipse.getPosition();
                return new Ellipse3(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        ellipse.getSemiAxis1(), ellipse.getSemiAxis2());
            }),
            curve3Rule(Parabola3.class, (curve) -> {
                Parabola3 parabola = (Parabola3) curve;
                Axis2Placement3D p = parabola.getPosition();
                return new Parabola3(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        parabola.getFocalLength());
            }),
            curve3Rule(Hyperbola3.class, (curve) -> {
                Hyperbola3 hyperbola = (Hyperbola3) curve;
                Axis2Placement3D p = hyperbola.getPosition();
                return new Hyperbola3(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        hyperbola.getSemiAxisA(), hyperbola.getSemiAxisB());
            }),
            curve3Rule(Clothoid3.class, (curve) -> {
                Clothoid3 clothoid = (Clothoid3) curve;
                Axis2Placement3D p = clothoid.getPosition();
                return new Clothoid3(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        clothoid.xAxisIntercept(), clothoid.curvature());
            }),
            curve3Rule(DegenerateCurve3.class, (curve) -> {
                DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
                return new DegenerateCurve3(degenerate.point());
            }),
            curve3Rule(TrimmedCurve3.class, (curve) -> {
                TrimmedCurve3 trimmed = (TrimmedCurve3) curve;
                // Swap trim parameters and flip sense to reverse the curve
                return new TrimmedCurve3(
                        reverseCurve3(trimmed.getBasisCurve()),
                        trimmed.getTrimParamEnd(),
                        trimmed.getTrimParamStart(),
                        !trimmed.isSenseAgreement());
            }),
            curve3Rule(SurfaceCurve3.class, (curve) -> {
                SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
                return new SurfaceCurve3(
                        reverseCurve3(surfaceCurve.getCurve3d()),
                        surfaceCurve.getParametricCurves());
            }),
            curve3Rule(BSplineCurve3.class, (curve) -> {
                BSplineCurve3 bspline = (BSplineCurve3) curve;
                return new BSplineCurve3(
                        bspline.getDegree(),
                        reverseList(bspline.getControlPoints()),
                        bspline.getKnotMultiplicities(),
                        bspline.getKnots());
            }),
            curve3Rule(RationalBSplineCurve3.class, (curve) -> {
                RationalBSplineCurve3 rational = (RationalBSplineCurve3) curve;
                return new RationalBSplineCurve3(
                        rational.getDegree(),
                        reverseList(rational.getControlPoints()),
                        rational.getWeights(),
                        rational.getKnotMultiplicities(),
                        rational.getKnots());
            }));

    // ─── SurfaceGeometry sense reversal ──────────────────────────────────

    private interface SurfaceHandler {
        SurfaceGeometry reverse(SurfaceGeometry surface);
    }

    private record SurfaceRule(Class<? extends SurfaceGeometry> type, SurfaceHandler handler) {}

    private static SurfaceRule surfaceRule(
            Class<? extends SurfaceGeometry> type, SurfaceHandler handler) {
        return new SurfaceRule(type, handler);
    }

    private static final List<SurfaceRule> REVERSE_SURFACE_RULES = List.of(
            surfaceRule(Plane.class, (surface) -> {
                Plane plane = (Plane) surface;
                return new Plane(plane.getOrigin(), plane.getNormal().reverse());
            }),
            surfaceRule(CylindricalSurface.class, (surface) -> {
                CylindricalSurface cyl = (CylindricalSurface) surface;
                Axis2Placement3D p = cyl.getPosition();
                return new CylindricalSurface(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        cyl.getRadius());
            }),
            surfaceRule(ConicalSurface.class, (surface) -> {
                ConicalSurface conic = (ConicalSurface) surface;
                Axis2Placement3D p = conic.getPosition();
                return new ConicalSurface(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        conic.getRadius(), conic.getSemiAngle());
            }),
            surfaceRule(SphericalSurface.class, (surface) -> {
                SphericalSurface sphere = (SphericalSurface) surface;
                Axis2Placement3D p = sphere.getPosition();
                return new SphericalSurface(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        sphere.getRadius());
            }),
            surfaceRule(ToroidalSurface.class, (surface) -> {
                ToroidalSurface torus = (ToroidalSurface) surface;
                Axis2Placement3D p = torus.getPosition();
                return new ToroidalSurface(
                        new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                        torus.getMajorRadius(), torus.getMinorRadius());
            }),
            surfaceRule(SurfaceOfLinearExtrusion3.class, (surface) -> {
                SurfaceOfLinearExtrusion3 extrusion = (SurfaceOfLinearExtrusion3) surface;
                return new SurfaceOfLinearExtrusion3(extrusion.getSweptCurve(), extrusion.getExtrusionVector().negate());
            }),
            surfaceRule(SurfaceOfRevolution3.class, (surface) -> {
                SurfaceOfRevolution3 revolution = (SurfaceOfRevolution3) surface;
                return new SurfaceOfRevolution3(
                        revolution.getSweptCurve(),
                        revolution.getAxisOrigin(),
                        revolution.getAxisDirection().reverse());
            }),
            surfaceRule(RuledSurface3.class, (surface) -> {
                RuledSurface3 ruled = (RuledSurface3) surface;
                return new RuledSurface3(
                        reverseCurve3(ruled.getDirectrix1()),
                        reverseCurve3(ruled.getDirectrix2()));
            }),
            surfaceRule(SurfaceOfConstantRadius3.class, (surface) -> {
                SurfaceOfConstantRadius3 constant = (SurfaceOfConstantRadius3) surface;
                return new SurfaceOfConstantRadius3(
                        reverseSurfaceSense(constant.getSweptSurface()),
                        constant.getRadius());
            }),
            surfaceRule(OffsetSurface3.class, (surface) -> {
                OffsetSurface3 offset = (OffsetSurface3) surface;
                return new OffsetSurface3(
                        reverseSurfaceSense(offset.getBasisSurface()),
                        offset.getDistance());
            }),
            surfaceRule(BSplineSurface3.class, (surface) -> {
                BSplineSurface3 bspline = (BSplineSurface3) surface;
                return new BSplineSurface3(
                        bspline.getUDegree(),
                        bspline.getVDegree(),
                        reverseBSplineControlGrid(bspline.getControlPoints()),
                        bspline.getUMultiplicities(),
                        bspline.getVMultiplicities(),
                        bspline.getUKnots(),
                        bspline.getVKnots());
            }),
            surfaceRule(RationalBSplineSurface3.class, (surface) -> {
                RationalBSplineSurface3 rational = (RationalBSplineSurface3) surface;
                return new RationalBSplineSurface3(
                        rational.getUDegree(),
                        rational.getVDegree(),
                        reverseBSplineControlGrid(rational.getControlPoints()),
                        rational.getWeightsData(),
                        rational.getUMultiplicities(),
                        rational.getVMultiplicities(),
                        rational.getUKnots(),
                        rational.getVKnots());
            }),
            surfaceRule(ParaboloidSurface.class, (surface) -> {
                ParaboloidSurface paraboloid = (ParaboloidSurface) surface;
                Axis2Placement3D pp = paraboloid.getPosition();
                return new ParaboloidSurface(
                        new Axis2Placement3D(pp.getLocation(), pp.getAxis(), pp.xDirection().reverse()),
                        paraboloid.getFocalLength());
            }),
            surfaceRule(HyperboloidSurface.class, (surface) -> {
                HyperboloidSurface hyperboloid = (HyperboloidSurface) surface;
                Axis2Placement3D hp = hyperboloid.getPosition();
                return new HyperboloidSurface(
                        new Axis2Placement3D(hp.getLocation(), hp.getAxis(), hp.xDirection().reverse()),
                        hyperboloid.getRadius(), hyperboloid.getSemiAxis());
            }),
            surfaceRule(SurfaceOfTranslation3.class, (surface) -> {
                SurfaceOfTranslation3 translation = (SurfaceOfTranslation3) surface;
                return new SurfaceOfTranslation3(
                        reverseCurve3(translation.getProfile()),
                        translation.getDirection());
            }),
            surfaceRule(SurfaceOfProjection3.class, (surface) -> {
                SurfaceOfProjection3 projection = (SurfaceOfProjection3) surface;
                return new SurfaceOfProjection3(
                        reverseCurve3(projection.getProfile()),
                        projection.getProjectionDirection());
            }));

    /**
     * Reverses a composite curve by reversing segment order and reversing each segment.
     */
    public static CompositeCurve3 reverseCompositeCurve(CompositeCurve3 original) {
        List<Curve3> reversedSegments = new ArrayList<>(original.getSegments());
        Collections.reverse(reversedSegments);
        for (int i = 0; i < reversedSegments.size(); i++) {
            reversedSegments.set(i, reverseCurve3(reversedSegments.get(i)));
        }
        return new CompositeCurve3(List.copyOf(reversedSegments));
    }

    /**
     * Reverses a single curve segment. For supported types, returns a geometrically reversed curve.
     * For unsupported types, returns the original curve (reversal not implemented).
     */
    public static Curve3 reverseCurve3(Curve3 curve) {
        for (Curve3Rule rule : REVERSE_CURVE3_RULES) {
            if (rule.type().isInstance(curve)) {
                return rule.handler().reverse(curve);
            }
        }
        return curve;
    }

    /**
     * Reverses the surface sense (normal direction) for oriented surfaces.
     * When an ORIENTED_SURFACE has orientation=false, the surface normal should be flipped.
     */
    public static SurfaceGeometry reverseSurfaceSense(SurfaceGeometry surface) {
        for (SurfaceRule rule : REVERSE_SURFACE_RULES) {
            if (rule.type().isInstance(surface)) {
                return rule.handler().reverse(surface);
            }
        }
        return surface;
    }

    /**
     * Reverses each row of a B-spline control grid (for surface sense reversal).
     */
    private static List<List<CartesianPoint>> reverseBSplineControlGrid(
            List<List<CartesianPoint>> grid) {
        List<List<CartesianPoint>> result = new ArrayList<>(grid.size());
        for (List<CartesianPoint> row : grid) {
            result.add(reverseList(row));
        }
        return List.copyOf(result);
    }

    /**
     * Reverses a list (helper for control point reversal).
     */
    private static <T> List<T> reverseList(List<T> list) {
        List<T> result = new ArrayList<>(list);
        Collections.reverse(result);
        return List.copyOf(result);
    }
}
