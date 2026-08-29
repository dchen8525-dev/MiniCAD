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
 */
public final class StepGeometryReverser {

    private StepGeometryReverser() {
        // Utility class - no instantiation
    }

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
        if (curve instanceof Line3) {
            Line3 line = (Line3) curve;
            return new Line3(line.getOrigin(), line.getDirection().reverse(), line.getParameterScale());
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            List<CartesianPoint> reversedPoints = new ArrayList<>(polyline.getPoints());
            Collections.reverse(reversedPoints);
            return new Polyline3(reversedPoints);
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 composite = (CompositeCurve3) curve;
            return reverseCompositeCurve(composite);
        }
        if (curve instanceof Circle) {
            Circle circle = (Circle) curve;
            Axis2Placement3D p = circle.getPosition();
            return new Circle(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    circle.getRadius());
        }
        if (curve instanceof Ellipse3) {
            Ellipse3 ellipse = (Ellipse3) curve;
            Axis2Placement3D p = ellipse.getPosition();
            return new Ellipse3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    ellipse.getSemiAxis1(), ellipse.getSemiAxis2());
        }
        if (curve instanceof Parabola3) {
            Parabola3 parabola = (Parabola3) curve;
            Axis2Placement3D p = parabola.getPosition();
            return new Parabola3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    parabola.getFocalLength());
        }
        if (curve instanceof Hyperbola3) {
            Hyperbola3 hyperbola = (Hyperbola3) curve;
            Axis2Placement3D p = hyperbola.getPosition();
            return new Hyperbola3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    hyperbola.getSemiAxisA(), hyperbola.getSemiAxisB());
        }
        if (curve instanceof Clothoid3) {
            Clothoid3 clothoid = (Clothoid3) curve;
            Axis2Placement3D p = clothoid.getPosition();
            return new Clothoid3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    clothoid.xAxisIntercept(), clothoid.curvature());
        }
        if (curve instanceof DegenerateCurve3) {
            DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
            return new DegenerateCurve3(degenerate.point());
        }
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmed = (TrimmedCurve3) curve;
            // Swap trim parameters and flip sense to reverse the curve
            return new TrimmedCurve3(
                    reverseCurve3(trimmed.getBasisCurve()),
                    trimmed.getTrimParamEnd(),
                    trimmed.getTrimParamStart(),
                    !trimmed.isSenseAgreement());
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return new SurfaceCurve3(
                    reverseCurve3(surfaceCurve.getCurve3d()),
                    surfaceCurve.getParametricCurves());
        }
        if (curve instanceof BSplineCurve3) {
            BSplineCurve3 bspline = (BSplineCurve3) curve;
            return new BSplineCurve3(
                    bspline.getDegree(),
                    reverseList(bspline.getControlPoints()),
                    bspline.getKnotMultiplicities(),
                    bspline.getKnots());
        }
        if (curve instanceof RationalBSplineCurve3) {
            RationalBSplineCurve3 rational = (RationalBSplineCurve3) curve;
            return new RationalBSplineCurve3(
                    rational.getDegree(),
                    reverseList(rational.getControlPoints()),
                    rational.getWeights(),
                    rational.getKnotMultiplicities(),
                    rational.getKnots());
        }
        return curve;
    }

    /**
     * Reverses the surface sense (normal direction) for oriented surfaces.
     * When an ORIENTED_SURFACE has orientation=false, the surface normal should be flipped.
     */
    public static SurfaceGeometry reverseSurfaceSense(SurfaceGeometry surface) {
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            return new Plane(plane.getOrigin(), plane.getNormal().reverse());
        }
        if (surface instanceof CylindricalSurface) {
            CylindricalSurface cyl = (CylindricalSurface) surface;
            Axis2Placement3D p = cyl.getPosition();
            return new CylindricalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    cyl.getRadius());
        }
        if (surface instanceof ConicalSurface) {
            ConicalSurface conic = (ConicalSurface) surface;
            Axis2Placement3D p = conic.getPosition();
            return new ConicalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    conic.getRadius(), conic.getSemiAngle());
        }
        if (surface instanceof SphericalSurface) {
            SphericalSurface sphere = (SphericalSurface) surface;
            Axis2Placement3D p = sphere.getPosition();
            return new SphericalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    sphere.getRadius());
        }
        if (surface instanceof ToroidalSurface) {
            ToroidalSurface torus = (ToroidalSurface) surface;
            Axis2Placement3D p = torus.getPosition();
            return new ToroidalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    torus.getMajorRadius(), torus.getMinorRadius());
        }
        if (surface instanceof SurfaceOfLinearExtrusion3) {
            SurfaceOfLinearExtrusion3 extrusion = (SurfaceOfLinearExtrusion3) surface;
            return new SurfaceOfLinearExtrusion3(extrusion.getSweptCurve(), extrusion.getExtrusionVector().negate());
        }
        if (surface instanceof SurfaceOfRevolution3) {
            SurfaceOfRevolution3 revolution = (SurfaceOfRevolution3) surface;
            return new SurfaceOfRevolution3(
                    revolution.getSweptCurve(),
                    revolution.getAxisOrigin(),
                    revolution.getAxisDirection().reverse());
        }
        if (surface instanceof RuledSurface3) {
            RuledSurface3 ruled = (RuledSurface3) surface;
            return new RuledSurface3(
                    reverseCurve3(ruled.getDirectrix1()),
                    reverseCurve3(ruled.getDirectrix2()));
        }
        if (surface instanceof SurfaceOfConstantRadius3) {
            SurfaceOfConstantRadius3 constant = (SurfaceOfConstantRadius3) surface;
            return new SurfaceOfConstantRadius3(
                    reverseSurfaceSense(constant.getSweptSurface()),
                    constant.getRadius());
        }
        if (surface instanceof OffsetSurface3) {
            OffsetSurface3 offset = (OffsetSurface3) surface;
            return new OffsetSurface3(
                    reverseSurfaceSense(offset.getBasisSurface()),
                    offset.getDistance());
        }
        if (surface instanceof BSplineSurface3) {
            BSplineSurface3 bspline = (BSplineSurface3) surface;
            return new BSplineSurface3(
                    bspline.getUDegree(),
                    bspline.getVDegree(),
                    reverseBSplineControlGrid(bspline.getControlPoints()),
                    bspline.getUMultiplicities(),
                    bspline.getVMultiplicities(),
                    bspline.getUKnots(),
                    bspline.getVKnots());
        }
        if (surface instanceof RationalBSplineSurface3) {
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
        }
        if (surface instanceof ParaboloidSurface) {
            ParaboloidSurface paraboloid = (ParaboloidSurface) surface;
            Axis2Placement3D pp = paraboloid.getPosition();
            return new ParaboloidSurface(
                    new Axis2Placement3D(pp.getLocation(), pp.getAxis(), pp.xDirection().reverse()),
                    paraboloid.getFocalLength());
        }
        if (surface instanceof HyperboloidSurface) {
            HyperboloidSurface hyperboloid = (HyperboloidSurface) surface;
            Axis2Placement3D hp = hyperboloid.getPosition();
            return new HyperboloidSurface(
                    new Axis2Placement3D(hp.getLocation(), hp.getAxis(), hp.xDirection().reverse()),
                    hyperboloid.getRadius(), hyperboloid.getSemiAxis());
        }
        if (surface instanceof SurfaceOfTranslation3) {
            SurfaceOfTranslation3 translation = (SurfaceOfTranslation3) surface;
            return new SurfaceOfTranslation3(
                    reverseCurve3(translation.getProfile()),
                    translation.getDirection());
        }
        if (surface instanceof SurfaceOfProjection3) {
            SurfaceOfProjection3 projection = (SurfaceOfProjection3) surface;
            return new SurfaceOfProjection3(
                    reverseCurve3(projection.getProfile()),
                    projection.getProjectionDirection());
        }
        return surface;
    }

    /**
     * Reverses each row of a B-spline control grid (for surface sense reversal).
     */
    public static List<List<CartesianPoint>> reverseBSplineControlGrid(
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
    public static <T> List<T> reverseList(List<T> list) {
        List<T> result = new ArrayList<>(list);
        Collections.reverse(result);
        return List.copyOf(result);
    }
}
