package com.minicad.step.semantic;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Point2;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepProfileDef;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;

/**
 * Builds 2D area profile point loops from STEP profile definitions.
 * Handles standard profiles (rectangle, circle, ellipse) and structural steel shapes
 * (I, T, L, U, Z, hat, etc.) with optional fillet radii.
 */
final class StepProfileBuilder {

    /**
     * Immutable pair of outer and inner 2D profile loops.
     */
    public final class ProfileLoops {
        private final List<Point2> outer;
        private final List<List<Point2>> inner;

        public ProfileLoops(List<Point2> outer, List<List<Point2>> inner) {
            this.outer = List.copyOf(outer);
            this.inner = inner.stream().map(List::copyOf).collect(Collectors.toList());
        }

        public List<Point2> outer() { return outer; }
        public List<List<Point2>> inner() { return inner; }
        public List<Point2> getOuter() { return outer; }
        public List<List<Point2>> getInner() { return inner; }
        public boolean isOuter() { return !outer.isEmpty(); }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProfileLoops that = (ProfileLoops) o;
            return outer.equals(that.outer) && inner.equals(that.inner);
        }

        @Override
        public int hashCode() {
            return outer.hashCode() + 31 * inner.hashCode();
        }

        @Override
        public String toString() {
            return "ProfileLoops{outer=" + outer + ", inner=" + inner + "}";
        }
    }

    private final StepCadGeometryOps geometryOps;
    private final Function<StepEntity, Curve2> buildCurve2;
    private final Map<String, Function<StepProfileDef, ProfileLoops>> areaProfileBuilders;

    StepProfileBuilder(StepCadGeometryOps geometryOps, Function<StepEntity, Curve2> buildCurve2) {
        this.geometryOps = geometryOps;
        this.buildCurve2 = buildCurve2;
        this.areaProfileBuilders = buildAreaProfileBuilders();
    }

    /**
     * AREA profile entity names mapped to their loop builders, replacing the
     * former 19-branch if/else-if chain in buildAreaProfileLoops. Aliased
     * STEP names (I_PROFILE_DEF vs I_SHAPE_PROFILE_DEF, etc.) share one
     * builder. Insertion order is preserved for readability only; dispatch is
     * on exact name equality so lookup order cannot change behavior.
     */
    private Map<String, Function<StepProfileDef, ProfileLoops>> buildAreaProfileBuilders() {
        Map<String, Function<StepProfileDef, ProfileLoops>> builders = new LinkedHashMap<>();
        builders.put("RECTANGLE_PROFILE_DEF", profile -> outerLoopProfile(rectangleProfile(profile)));
        builders.put("CENTERED_RECTANGLE_PROFILE_DEF", profile -> outerLoopProfile(rectangleProfile(profile)));
        builders.put("CIRCLE_PROFILE_DEF", profile -> outerLoopProfile(circleProfile(profile)));
        builders.put("ELLIPSE_PROFILE_DEF", profile -> outerLoopProfile(ellipseProfile(profile)));
        builders.put("ROUNDED_RECTANGLE_PROFILE_DEF", profile -> outerLoopProfile(roundedRectangleProfile(profile)));
        builders.put("CIRCULAR_HOLLOW_PROFILE_DEF", this::circularHollowProfile);
        builders.put("RECTANGLE_HOLLOW_PROFILE_DEF", this::rectangleHollowProfile);
        builders.put("CENTERED_CIRCLE_PROFILE_DEF", profile -> outerLoopProfile(centeredCircleProfile(profile)));
        builders.put("CENTRE_LINE_ARC_PROFILE_DEF", profile -> outerLoopProfile(centreLineArcProfile(profile)));
        builders.put("ARBITRARY_CLOSED_PROFILE_DEF", profile -> outerLoopProfile(arbitraryClosedProfile(profile)));
        builders.put("ARBITRARY_PROFILE_DEF", profile -> outerLoopProfile(arbitraryClosedProfile(profile)));
        builders.put("ARBITRARY_PROFILE_DEF_WITH_VOIDS", this::arbitraryProfileWithVoids);
        builders.put("I_SHAPE_PROFILE_DEF", profile -> outerLoopProfile(iShapeProfile(profile)));
        builders.put("I_PROFILE_DEF", profile -> outerLoopProfile(iShapeProfile(profile)));
        builders.put("T_SHAPE_PROFILE_DEF", profile -> outerLoopProfile(tShapeProfile(profile)));
        builders.put("T_PROFILE_DEF", profile -> outerLoopProfile(tShapeProfile(profile)));
        builders.put("TEE_PROFILE_DEF", profile -> outerLoopProfile(tShapeProfile(profile)));
        builders.put("L_SHAPE_PROFILE_DEF", profile -> outerLoopProfile(lShapeProfile(profile)));
        builders.put("L_PROFILE_DEF", profile -> outerLoopProfile(lShapeProfile(profile)));
        builders.put("ANGLE_PROFILE_DEF", profile -> outerLoopProfile(lShapeProfile(profile)));
        builders.put("U_SHAPE_PROFILE_DEF", profile -> outerLoopProfile(uShapeProfile(profile)));
        builders.put("U_PROFILE_DEF", profile -> outerLoopProfile(uShapeProfile(profile)));
        builders.put("CHANNEL_PROFILE_DEF", profile -> outerLoopProfile(uShapeProfile(profile)));
        builders.put("C_SHAPE_PROFILE_DEF", profile -> outerLoopProfile(uShapeProfile(profile)));
        builders.put("Z_SHAPE_PROFILE_DEF", profile -> outerLoopProfile(zShapeProfile(profile)));
        builders.put("Z_PROFILE_DEF", profile -> outerLoopProfile(zShapeProfile(profile)));
        builders.put("HAT_SHAPE_PROFILE_DEF", profile -> outerLoopProfile(hatShapeProfile(profile)));
        builders.put("FLAT_BAR_PROFILE_DEF", profile -> outerLoopProfile(flatBarProfile(profile)));
        builders.put("DOVE_TAIL_PROFILE_DEF", profile -> outerLoopProfile(doveTailProfile(profile)));
        builders.put("ARBITRARY_OPEN_PROFILE_DEF", this::buildArbitraryOpenProfile);
        builders.put("PARAMETERIZED_PROFILE_DEF", this::buildParameterizedProfile);
        return builders;
    }

    private ProfileLoops outerLoopProfile(List<Point2> outerLoop) {
        return new ProfileLoops(normalizeOuterLoop(outerLoop), List.of());
    }

    ProfileLoops buildAreaProfileLoops(StepProfileDef profile) {
        if (!"AREA".equals(profile.profileType())) {
            throw new UnsupportedGeometryException(profile.entityName() + " must be an AREA profile");
        }
        Function<StepProfileDef, ProfileLoops> builder = areaProfileBuilders.get(profile.entityName());
        if (builder == null) {
            throw new UnsupportedGeometryException(profile.entityName() + " extrusion is unsupported");
        }
        return builder.apply(profile);
    }

    private List<Point2> rectangleProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires x and y dimensions");
        }
        double halfX = profile.parameters().get(0) * 0.5;
        double halfY = profile.parameters().get(1) * 0.5;
        return List.of(
                new Point2(-halfX, -halfY),
                new Point2(halfX, -halfY),
                new Point2(halfX, halfY),
                new Point2(-halfX, halfY),
                new Point2(-halfX, -halfY)
        );
    }

    private List<Point2> circleProfile(StepProfileDef profile) {
        if (profile.parameters().isEmpty()) {
            throw new UnsupportedGeometryException("CIRCLE_PROFILE_DEF requires a radius");
        }
        double radius = profile.parameters().get(0);
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("CIRCLE_PROFILE_DEF radius must be positive");
        }
        Circle2 circle = new Circle2(new Point2(0.0, 0.0), new com.minicad.geometry2d.Direction2(1.0, 0.0), radius);
        return geometryOps.sampleCurve2(circle, 72);
    }

    private List<Point2> ellipseProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("ELLIPSE_PROFILE_DEF requires semi axes");
        }
        double semiAxis1 = profile.parameters().get(0);
        double semiAxis2 = profile.parameters().get(1);
        if (semiAxis1 <= 0.0 || semiAxis2 <= 0.0) {
            throw new UnsupportedGeometryException("ELLIPSE_PROFILE_DEF semi axes must be positive");
        }
        Ellipse2 ellipse = new Ellipse2(new Point2(0.0, 0.0), new com.minicad.geometry2d.Direction2(1.0, 0.0), semiAxis1, semiAxis2);
        return geometryOps.sampleCurve2(ellipse, 72);
    }

    private List<Point2> roundedRectangleProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 3) {
            throw new UnsupportedGeometryException("ROUNDED_RECTANGLE_PROFILE_DEF requires x, y and radius");
        }
        double width = profile.parameters().get(0);
        double height = profile.parameters().get(1);
        double radius = profile.parameters().get(2);
        if (width <= 0.0 || height <= 0.0 || radius <= 0.0) {
            throw new UnsupportedGeometryException("ROUNDED_RECTANGLE_PROFILE_DEF dimensions must be positive");
        }
        if (radius * 2.0 >= width || radius * 2.0 >= height) {
            throw new UnsupportedGeometryException("ROUNDED_RECTANGLE_PROFILE_DEF radius must be smaller than half dimensions");
        }
        List<Point2> points = new ArrayList<>();
        appendArc(points, new Point2(width * 0.5 - radius, height * 0.5 - radius), radius, 0.0, Math.PI * 0.5, 12, true);
        appendArc(points, new Point2(-width * 0.5 + radius, height * 0.5 - radius), radius, Math.PI * 0.5, Math.PI, 12, false);
        appendArc(points, new Point2(-width * 0.5 + radius, -height * 0.5 + radius), radius, Math.PI, Math.PI * 1.5, 12, false);
        appendArc(points, new Point2(width * 0.5 - radius, -height * 0.5 + radius), radius, Math.PI * 1.5, Math.PI * 2.0, 12, false);
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private void appendArc(List<Point2> points, Point2 center, double radius, double startAngle, double endAngle, int segments, boolean includeStart) {
        int startIndex = includeStart ? 0 : 1;
        for (int index = startIndex; index <= segments; index++) {
            double angle = startAngle + (endAngle - startAngle) * index / segments;
            points.add(new Point2(center.getX() + Math.cos(angle) * radius, center.getY() + Math.sin(angle) * radius));
        }
    }

    private ProfileLoops circularHollowProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("CIRCULAR_HOLLOW_PROFILE_DEF requires radius and wall thickness");
        }
        double outerRadius = profile.parameters().get(0);
        double wallThickness = profile.parameters().get(1);
        double innerRadius = outerRadius - wallThickness;
        if (outerRadius <= 0.0 || wallThickness <= 0.0 || innerRadius <= 0.0) {
            throw new UnsupportedGeometryException("CIRCULAR_HOLLOW_PROFILE_DEF dimensions must define a positive inner radius");
        }
        return new ProfileLoops(
                normalizeOuterLoop(circleProfile(new StepProfileDef(profile.id(), profile.profileType(), profile.profileName(), profile.getPosition(), profile.curves(), List.of(outerRadius), "CIRCLE_PROFILE_DEF"))),
                List.of(normalizeInnerLoop(circleProfile(new StepProfileDef(profile.id(), profile.profileType(), profile.profileName(), profile.getPosition(), profile.curves(), List.of(innerRadius), "CIRCLE_PROFILE_DEF"))))
        );
    }

    private ProfileLoops rectangleHollowProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException("RECTANGLE_HOLLOW_PROFILE_DEF requires xDim, yDim, wallThickness and innerRadius");
        }
        double xDim = profile.parameters().get(0);
        double yDim = profile.parameters().get(1);
        double wallThickness = profile.parameters().get(2);
        double innerRadius = profile.parameters().get(3);
        double innerXDim = xDim - 2.0 * wallThickness;
        double innerYDim = yDim - 2.0 * wallThickness;
        if (xDim <= 0.0 || yDim <= 0.0 || wallThickness <= 0.0 || innerXDim <= 0.0 || innerYDim <= 0.0) {
            throw new UnsupportedGeometryException("RECTANGLE_HOLLOW_PROFILE_DEF dimensions must define a positive inner rectangle");
        }
        double halfOuterX = xDim * 0.5;
        double halfOuterY = yDim * 0.5;
        double halfInnerX = innerXDim * 0.5;
        double halfInnerY = innerYDim * 0.5;
        List<Point2> outer = List.of(
                new Point2(-halfOuterX, -halfOuterY),
                new Point2(halfOuterX, -halfOuterY),
                new Point2(halfOuterX, halfOuterY),
                new Point2(-halfOuterX, halfOuterY),
                new Point2(-halfOuterX, -halfOuterY)
        );
        List<Point2> inner = List.of(
                new Point2(-halfInnerX, -halfInnerY),
                new Point2(-halfInnerX, halfInnerY),
                new Point2(halfInnerX, halfInnerY),
                new Point2(halfInnerX, -halfInnerY),
                new Point2(-halfInnerX, -halfInnerY)
        );
        return new ProfileLoops(normalizeOuterLoop(outer), List.of(normalizeInnerLoop(inner)));
    }

    private List<Point2> centeredCircleProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("CENTERED_CIRCLE_PROFILE_DEF requires radius and centerOffset");
        }
        double radius = profile.parameters().get(0);
        double centerOffset = profile.parameters().get(1);
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("CENTERED_CIRCLE_PROFILE_DEF radius must be positive");
        }
        Circle2 circle = new Circle2(new Point2(centerOffset, 0.0), new com.minicad.geometry2d.Direction2(1.0, 0.0), radius);
        return geometryOps.sampleCurve2(circle, 72);
    }

    private List<Point2> centreLineArcProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("CENTRE_LINE_ARC_PROFILE_DEF requires radius and angle");
        }
        double radius = profile.parameters().get(0);
        double angle = profile.parameters().get(1);
        if (radius <= 0.0 || angle <= 0.0) {
            throw new UnsupportedGeometryException("CENTRE_LINE_ARC_PROFILE_DEF radius and angle must be positive");
        }
        List<Point2> points = new ArrayList<>();
        int segments = Math.max(12, (int) (Math.abs(angle) / (Math.PI / 36.0)));
        double startAngle = -angle / 2.0;
        double endAngle = angle / 2.0;
        for (int i = 0; i <= segments; i++) {
            double a = startAngle + (endAngle - startAngle) * i / segments;
            points.add(new Point2(radius * Math.cos(a), radius * Math.sin(a)));
        }
        return List.copyOf(points);
    }

    private List<Point2> iShapeProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters (flangeWidth, webDepth, flangeThickness, webThickness)");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double filletRadius = profile.parameters().size() > 4 ? profile.parameters().get(4) : 0.0;
        double halfFlange = flangeWidth / 2.0;
        double halfWeb = webThickness / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(-halfWeb, halfDepth));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfWeb, halfDepth - filletRadius, filletRadius, Math.PI / 2, 0, 6);
        }
        points.add(new Point2(-halfWeb, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfWeb, -halfDepth + flangeThickness + filletRadius, filletRadius, Math.PI, Math.PI / 2, 6);
        }
        points.add(new Point2(-halfFlange, -halfDepth));
        points.add(new Point2(halfFlange, -halfDepth));
        if (filletRadius > 0.0) {
            appendFillet(points, halfWeb, -halfDepth + flangeThickness + filletRadius, filletRadius, Math.PI / 2, 0, 6);
        }
        points.add(new Point2(halfWeb, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfWeb, halfDepth - filletRadius, filletRadius, 0, -Math.PI / 2, 6);
        }
        points.add(new Point2(halfWeb, halfDepth));
        points.add(new Point2(halfFlange, halfDepth));
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private List<Point2> tShapeProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double filletRadius = profile.parameters().size() > 4 ? profile.parameters().get(4) : 0.0;
        double halfFlange = flangeWidth / 2.0;
        double halfWeb = webThickness / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfWeb, halfDepth - flangeThickness + filletRadius, filletRadius, Math.PI / 2, 0, 6);
        }
        points.add(new Point2(halfWeb, halfDepth - flangeThickness));
        points.add(new Point2(halfWeb, -halfDepth));
        points.add(new Point2(-halfWeb, -halfDepth));
        points.add(new Point2(-halfWeb, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfWeb, halfDepth - flangeThickness + filletRadius, filletRadius, Math.PI, Math.PI / 2, 6);
        }
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private List<Point2> lShapeProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 3) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 3 parameters");
        }
        double width = profile.parameters().get(0);
        double depth = profile.parameters().get(1);
        double thickness = profile.parameters().get(2);
        double filletRadius = profile.parameters().size() > 3 ? profile.parameters().get(3) : 0.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(0.0, 0.0));
        points.add(new Point2(width, 0.0));
        points.add(new Point2(width, thickness));
        if (filletRadius > 0.0) {
            appendFillet(points, thickness - filletRadius, thickness - filletRadius, filletRadius, 0, -Math.PI / 2, 6);
        } else {
            points.add(new Point2(thickness, thickness));
        }
        points.add(new Point2(thickness, depth));
        points.add(new Point2(0.0, depth));
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private List<Point2> uShapeProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double filletRadius = profile.parameters().size() > 4 ? profile.parameters().get(4) : 0.0;
        double halfFlange = flangeWidth / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(-halfFlange, -halfDepth));
        points.add(new Point2(halfFlange, -halfDepth));
        points.add(new Point2(halfFlange, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfFlange - webThickness - filletRadius, -halfDepth + flangeThickness + filletRadius, filletRadius, 0, -Math.PI / 2, 6);
        }
        points.add(new Point2(halfFlange - webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(halfFlange - webThickness, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfFlange - webThickness - filletRadius, halfDepth - flangeThickness - filletRadius, filletRadius, Math.PI / 2, Math.PI, 6);
        }
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfFlange + webThickness + filletRadius, halfDepth - flangeThickness - filletRadius, filletRadius, Math.PI, Math.PI * 1.5, 6);
        }
        points.add(new Point2(-halfFlange + webThickness, halfDepth - flangeThickness));
        points.add(new Point2(-halfFlange + webThickness, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfFlange + webThickness + filletRadius, -halfDepth + flangeThickness + filletRadius, filletRadius, Math.PI * 1.5, Math.PI * 2, 6);
        }
        points.add(new Point2(-halfFlange, -halfDepth + flangeThickness));
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private List<Point2> zShapeProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double halfFlange = flangeWidth / 2.0;
        double halfWeb = webThickness / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(halfWeb, halfDepth - flangeThickness));
        points.add(new Point2(halfWeb, -halfDepth + flangeThickness));
        points.add(new Point2(-halfFlange, -halfDepth + flangeThickness));
        points.add(new Point2(-halfFlange, -halfDepth));
        points.add(new Point2(halfFlange - webThickness, -halfDepth));
        points.add(new Point2(halfFlange - webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(-halfWeb, -halfDepth + flangeThickness));
        points.add(new Point2(-halfWeb, halfDepth - flangeThickness));
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private List<Point2> hatShapeProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double halfFlange = flangeWidth / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(halfFlange - webThickness, halfDepth - flangeThickness));
        points.add(new Point2(halfFlange - webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(halfFlange, -halfDepth + flangeThickness));
        points.add(new Point2(halfFlange, -halfDepth));
        points.add(new Point2(-halfFlange, -halfDepth));
        points.add(new Point2(-halfFlange, -halfDepth + flangeThickness));
        points.add(new Point2(-halfFlange + webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(-halfFlange + webThickness, halfDepth - flangeThickness));
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private List<Point2> flatBarProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires width and thickness");
        }
        double width = profile.parameters().get(0);
        double thickness = profile.parameters().get(1);
        double halfWidth = width / 2.0;
        double halfThickness = thickness / 2.0;
        return List.of(
                new Point2(-halfWidth, -halfThickness),
                new Point2(halfWidth, -halfThickness),
                new Point2(halfWidth, halfThickness),
                new Point2(-halfWidth, halfThickness),
                new Point2(-halfWidth, -halfThickness)
        );
    }

    private List<Point2> doveTailProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 3) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 3 parameters");
        }
        double width = profile.parameters().get(0);
        double depth = profile.parameters().get(1);
        double neckWidth = profile.parameters().size() > 3 ? profile.parameters().get(3) : width * 0.5;
        double halfWidth = width / 2.0;
        double halfNeck = neckWidth / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(-halfWidth, 0.0));
        points.add(new Point2(halfWidth, 0.0));
        points.add(new Point2(halfNeck, depth));
        points.add(new Point2(-halfNeck, depth));
        points.add(points.get(0));
        return List.copyOf(points);
    }

    private ProfileLoops buildArbitraryOpenProfile(StepProfileDef profile) {
        if (profile.curves().isEmpty()) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires a profile curve");
        }
        return new ProfileLoops(List.of(), List.of());
    }

    private ProfileLoops buildParameterizedProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 3) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 3 parameters");
        }
        double p1 = profile.parameters().get(0);
        double p2 = profile.parameters().get(1);
        double halfX = p1 / 2.0;
        double halfY = p2 / 2.0;
        List<Point2> rect = List.of(
                new Point2(-halfX, -halfY),
                new Point2(halfX, -halfY),
                new Point2(halfX, halfY),
                new Point2(-halfX, halfY)
        );
        return new ProfileLoops(rect, List.of());
    }

    private void appendFillet(List<Point2> points, double centerX, double centerY, double radius, double startAngle, double endAngle, int segments) {
        for (int i = 1; i <= segments; i++) {
            double angle = startAngle + (endAngle - startAngle) * i / segments;
            points.add(new Point2(centerX + radius * Math.cos(angle), centerY + radius * Math.sin(angle)));
        }
    }

    private List<Point2> arbitraryClosedProfile(StepProfileDef profile) {
        if (profile.curves().isEmpty()) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires a profile curve");
        }
        return arbitraryProfileCurve(profile.entityName(), profile.curves().get(0));
    }

    private ProfileLoops arbitraryProfileWithVoids(StepProfileDef profile) {
        if (profile.curves().isEmpty()) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires an outer profile curve");
        }
        List<Point2> outer = normalizeOuterLoop(arbitraryProfileCurve(profile.entityName(), profile.curves().get(0)));
        List<List<Point2>> inner = profile.curves().stream()
                .skip(1)
                .map(curve -> normalizeInnerLoop(arbitraryProfileCurve(profile.entityName(), curve)))
                .collect(Collectors.toList());
        return new ProfileLoops(outer, inner);
    }

    private List<Point2> arbitraryProfileCurve(String entityName, StepEntity curveEntity) {
        Curve2 curve2 = buildCurve2.apply(curveEntity);
        if (curve2 == null) {
            throw new UnsupportedGeometryException(entityName + " profile curve is not 2D");
        }
        return geometryOps.sampleCurve2(curve2, 72);
    }

    private List<Point2> normalizeOuterLoop(List<Point2> points) {
        List<Point2> normalized = geometryOps.normalizeClosedLoop2(points);
        if (Math.abs(signedArea2(normalized)) <= 1.0e-9) {
            throw new UnsupportedGeometryException("profile area must be non-zero");
        }
        if (signedArea2(normalized) < 0.0) {
            normalized = geometryOps.reverseClosedLoop2(normalized);
        }
        return List.copyOf(normalized.subList(0, normalized.size() - 1));
    }

    private List<Point2> normalizeInnerLoop(List<Point2> points) {
        List<Point2> normalized = geometryOps.normalizeClosedLoop2(points);
        if (Math.abs(signedArea2(normalized)) <= 1.0e-9) {
            throw new UnsupportedGeometryException("profile hole area must be non-zero");
        }
        if (signedArea2(normalized) > 0.0) {
            normalized = geometryOps.reverseClosedLoop2(normalized);
        }
        return List.copyOf(normalized.subList(0, normalized.size() - 1));
    }

    private static double signedArea2(List<Point2> points) {
        double area = 0.0;
        for (int index = 0; index < points.size() - 1; index++) {
            Point2 current = points.get(index);
            Point2 next = points.get(index + 1);
            area += current.getX() * next.getY() - next.getX() * current.getY();
        }
        return area * 0.5;
    }
}
