package com.minicad.step.semantic;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis1Placement;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Point2;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.model.core.base.StepFaceEntity;
import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.geometry.StepVector;
import com.minicad.step.model.product.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.product.StepExtrudedFaceSolid;
import com.minicad.step.model.product.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.product.StepRevolvedFaceSolid;
import com.minicad.step.model.product.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.product.StepSweptAreaSolid;
import com.minicad.step.model.product.StepSweptDiskSolid;
import com.minicad.step.model.product.StepSweptFaceSolid;
import com.minicad.step.model.profile_analysis.profile.StepAreaProfile;
import com.minicad.step.model.profile_analysis.profile.StepCenteredCircleProfileDef;
import com.minicad.step.model.profile_analysis.profile.StepCentreLineArcProfileDef;
import com.minicad.step.model.profile_analysis.profile.StepGeneralizedAreaProfile;
import com.minicad.step.model.profile_analysis.profile.StepProfileDef;
import com.minicad.step.model.profile_analysis.profile.StepRectangleHollowProfileDef;
import com.minicad.step.model.profile_analysis.profile.StepSweptProfileAreaOutline;
import com.minicad.step.model.topology.StepAdvancedFace;
import com.minicad.step.model.topology.StepFaceSurface;
import com.minicad.step.model.topology.StepOrientedFace;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds swept area solid geometry from STEP entities.
 * Handles extrusion and revolution operations from profile definitions.
 * Package-private class that delegates to StepCadBuilder for shared functionality.
 */
final class StepCadSweptBuilder {

    private final StepCadBuilder builder;
    private final StepProfileBuilder profileBuilder;

    StepCadSweptBuilder(StepCadBuilder builder, StepProfileBuilder profileBuilder) {
        this.builder = builder;
        this.profileBuilder = profileBuilder;
    }

    // ========================================================================
    // Swept Face Solid builders
    // ========================================================================

    Solid buildExtrudedFaceSolid(StepExtrudedFaceSolid extrudedFace) {
        StepEntity faceGeometry = extrudedFace.sweptFace();
        if (!(faceGeometry instanceof StepFaceEntity)) {
            throw new UnsupportedGeometryException("EXTRUDED_FACE_SOLID swept_face must be a face entity");
        }
        StepFaceEntity stepFace = (StepFaceEntity) faceGeometry;
        SurfaceGeometry surface = builder.buildSupportedFaceGeometry(faceGeometry(stepFace), "EXTRUDED_FACE_SOLID");
        List<CartesianPoint> localProfilePoints = sampleFaceBoundary(surface, 72);
        if (localProfilePoints.isEmpty()) {
            throw new UnsupportedGeometryException("EXTRUDED_FACE_SOLID could not extract boundary points");
        }
        if (!(extrudedFace.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("EXTRUDED_FACE_SOLID position must be an AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement = (StepAxis2Placement3D) extrudedFace.getPosition();
        Axis2Placement3D solidPlacement = builder.buildPlacement(placement.id());
        double depth = extrudedFace.depth() != null ? extrudedFace.depth() : 1.0;
        if (depth <= 0.0) {
            throw new UnsupportedGeometryException("EXTRUDED_FACE_SOLID requires positive depth");
        }

        Direction3 localDirection = buildExtrusionDirection(extrudedFace.getDirection(), "EXTRUDED_FACE_SOLID");
        Direction3 worldDirection = solidPlacement.transformDirectionToWorld(localDirection);
        List<CartesianPoint> profilePoints = localProfilePoints.stream()
                .map(solidPlacement::transformToWorld)
                .collect(Collectors.toList());
        return buildExtrudedProfile(profilePoints, worldDirection, depth);
    }

    Solid buildRevolvedFaceSolid(StepRevolvedFaceSolid revolvedFace) {
        StepEntity faceGeometry = revolvedFace.sweptFace();
        if (!(faceGeometry instanceof StepFaceEntity)) {
            throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID swept_face must be a face entity");
        }
        StepFaceEntity stepFace = (StepFaceEntity) faceGeometry;
        SurfaceGeometry surface = builder.buildSupportedFaceGeometry(faceGeometry(stepFace), "REVOLVED_FACE_SOLID");
        List<CartesianPoint> localProfilePoints = sampleFaceBoundary(surface, 72);
        if (localProfilePoints.isEmpty()) {
            throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID could not extract boundary points");
        }
        if (!(revolvedFace.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID position must be an AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement = (StepAxis2Placement3D) revolvedFace.getPosition();
        Axis2Placement3D solidPlacement = builder.buildPlacement(placement.id());
        double angle = revolvedFace.angle() != null ? revolvedFace.angle() : 2 * Math.PI;
        if (Math.abs(angle) <= 1.0e-9) {
            throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID requires a non-zero revolution angle");
        }
        if (Math.abs(angle) > Math.PI * 2.0 + 1.0e-9) {
            throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID revolution angle must not exceed 2*PI");
        }

        Axis1Placement revolutionAxis = buildRevolutionAxis(revolvedFace.getAxis(), solidPlacement, "REVOLVED_FACE_SOLID");
        List<CartesianPoint> profilePoints = localProfilePoints.stream()
                .map(solidPlacement::transformToWorld)
                .collect(Collectors.toList());
        for (CartesianPoint point : profilePoints) {
            if (distanceToAxis(point, revolutionAxis) <= 1.0e-9) {
                throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID profile must not intersect the revolution axis");
            }
        }
        CartesianPoint axisOrigin = revolutionAxis.getLocation();
        Vector3 axis = revolutionAxis.getAxis().asVector();
        return buildRevolvedProfile(profilePoints, axisOrigin, axis, angle);
    }

    Solid buildSweptFaceSolid(StepSweptFaceSolid sweptFace) {
        // Sweep a face along a trajectory curve
        StepEntity faceGeometry = sweptFace.sweptFace();
        if (!(faceGeometry instanceof StepFaceEntity)) {
            throw new UnsupportedGeometryException("SWEPT_FACE_SOLID swept_face must be a face entity");
        }
        StepFaceEntity stepFace = (StepFaceEntity) faceGeometry;
        SurfaceGeometry surface = builder.buildSupportedFaceGeometry(stepFace, "SWEPT_FACE_SOLID");
        List<CartesianPoint> profilePoints = sampleFaceBoundary(surface, 72);
        if (profilePoints.isEmpty()) {
            throw new UnsupportedGeometryException("SWEPT_FACE_SOLID could not extract boundary points");
        }
        // Build trajectory curve and sample
        Curve3 trajectory = builder.buildCurve3(sweptFace.trajectory());
        List<Curve3Sample> samples = sampleCurve3WithTangent(trajectory, 48);
        return buildSweptProfileAlongCurve(profilePoints, samples);
    }

    // ========================================================================
    // Swept Area Solid builders
    // ========================================================================

    Solid buildSweptAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        if ("EXTRUDED_AREA_SOLID".equals(sweptAreaSolid.entityName())) {
            return buildExtrudedAreaSolid(sweptAreaSolid);
        }
        if ("REVOLVED_AREA_SOLID".equals(sweptAreaSolid.entityName())) {
            return buildRevolvedAreaSolid(sweptAreaSolid);
        }
        throw new UnsupportedGeometryException(sweptAreaSolid.entityName() + " construction is unsupported");
    }

    Solid buildSweptDiskSolid(StepSweptDiskSolid sweptDiskSolid) {
        Curve3 sweptCurve = builder.buildCurve3(sweptDiskSolid.getSweptCurve());
        double radius = sweptDiskSolid.getRadius();
        Double innerRadius = sweptDiskSolid.innerRadius();
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("SWEPT_DISK_SOLID radius must be positive");
        }
        boolean isTube = innerRadius != null && innerRadius > 0.0;
        if (isTube && innerRadius >= radius) {
            throw new UnsupportedGeometryException("SWEPT_DISK_SOLID inner radius must be less than outer radius");
        }
        // Sample the swept curve to get positions and tangent directions
        int curveSegments = 48;
        int ringSegments = 24;
        List<Curve3Sample> samples = sampleCurve3WithTangent(sweptCurve, curveSegments);
        List<List<CartesianPoint>> outerRings = new ArrayList<>();
        List<List<CartesianPoint>> innerRings = new ArrayList<>();
        for (Curve3Sample sample : samples) {
            CircularFrame frame = circularFrameAtPoint(sample.point(), sample.tangent());
            List<CartesianPoint> outerRing = sampleCircle3(sample.point(), frame.getX(), frame.getY(), radius, ringSegments);
            outerRings.add(outerRing);
            if (isTube) {
                List<CartesianPoint> innerRing = sampleCircle3(sample.point(), frame.getX(), frame.getY(), innerRadius, ringSegments);
                innerRings.add(innerRing);
            }
        }
        List<Face> faces = new ArrayList<>();
        // End caps
        if (!isTube) {
            // Solid disk: cap both ends
            faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(outerRings.get(0)), samples.get(0).tangent().reverse()));
            faces.add(builder.faceFromPolyLoop(builder.closeLoop3(outerRings.get(outerRings.size() - 1)), samples.get(samples.size() - 1).tangent()));
        } else {
            // Tube: ring-shaped caps
            faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(outerRings.get(0)), samples.get(0).tangent().reverse()));
            faces.add(builder.faceFromPolyLoop(builder.closeLoop3(innerRings.get(0)), samples.get(0).tangent()));
            faces.add(builder.faceFromPolyLoop(builder.closeLoop3(outerRings.get(outerRings.size() - 1)), samples.get(samples.size() - 1).tangent()));
            faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(innerRings.get(innerRings.size() - 1)), samples.get(samples.size() - 1).tangent().reverse()));
        }
        // Lateral faces connecting adjacent rings
        for (int ringIndex = 0; ringIndex < outerRings.size() - 1; ringIndex++) {
            List<CartesianPoint> currentOuter = outerRings.get(ringIndex);
            List<CartesianPoint> nextOuter = outerRings.get(ringIndex + 1);
            for (int segIndex = 0; segIndex < ringSegments; segIndex++) {
                CartesianPoint a = currentOuter.get(segIndex);
                CartesianPoint b = currentOuter.get((segIndex + 1) % ringSegments);
                CartesianPoint c = nextOuter.get((segIndex + 1) % ringSegments);
                CartesianPoint d = nextOuter.get(segIndex);
                faces.add(builder.faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
            }
            if (isTube) {
                List<CartesianPoint> currentInner = innerRings.get(ringIndex);
                List<CartesianPoint> nextInner = innerRings.get(ringIndex + 1);
                for (int segIndex = 0; segIndex < ringSegments; segIndex++) {
                    CartesianPoint a = currentInner.get(segIndex);
                    CartesianPoint b = nextInner.get(segIndex);
                    CartesianPoint c = nextInner.get((segIndex + 1) % ringSegments);
                    CartesianPoint d = currentInner.get((segIndex + 1) % ringSegments);
                    faces.add(builder.faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
                }
            }
        }
        return new Solid(new Shell(faces, true));
    }

    Solid buildExtrudedAreaSolidTapered(StepExtrudedAreaSolidTapered tapered) {
        // Get the profile from sweptArea (which is StepEntity, need to cast to StepProfileDef)
        StepProfileDef profileDef = asProfileDef(tapered.sweptArea());
        StepProfileBuilder.ProfileLoops baseProfile = profileBuilder.buildAreaProfileLoops(profileDef);
        Vector3 direction = builder.buildDirection(tapered.getDirection().id()).asVector();
        double depth = tapered.depth();
        double taperAngle = tapered.taperAngle();
        if (depth <= 0.0) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID_TAPERED depth must be positive");
        }
        // Calculate scale factor at top based on taper angle
        // Taper angle is the angle of the taper relative to the extrusion direction
        double topScale = 1.0 - depth * Math.tan(Math.abs(taperAngle));
        if (topScale <= 0.0) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID_TAPERED taper angle too large, top profile would be zero or negative");
        }
        // Build scaled top profile
        List<Point2> baseOuter = baseProfile.getOuter();
        List<Point2> topOuter = scaleProfile(baseOuter, topScale);
        // Build 3D geometry
        List<CartesianPoint> bottom3D = baseOuter.stream()
                .map(p -> new CartesianPoint(p.getX(), p.getY(), 0.0))
                .collect(Collectors.toList());
        List<CartesianPoint> top3D = topOuter.stream()
                .map(p -> new CartesianPoint(p.getX() * topScale, p.getY() * topScale, depth))
                .collect(Collectors.toList());
        // Close loops
        bottom3D = builder.closeLoop3(bottom3D);
        top3D = builder.closeLoop3(top3D);
        List<Face> faces = new ArrayList<>();
        Direction3 extrusionDir = Direction3.from(direction);
        // Bottom face
        faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(bottom3D), extrusionDir.reverse()));
        // Top face
        faces.add(builder.faceFromPolyLoop(top3D, extrusionDir));
        // Side faces connecting bottom to top
        for (int i = 0; i < baseOuter.size(); i++) {
            CartesianPoint a = bottom3D.get(i);
            CartesianPoint b = bottom3D.get((i + 1) % baseOuter.size());
            CartesianPoint c = top3D.get((i + 1) % top3D.size());
            CartesianPoint d = top3D.get(i);
            faces.add(builder.faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
        }
        return new Solid(new Shell(faces, true));
    }

    Solid buildRevolvedAreaSolidTapered(StepRevolvedAreaSolidTapered tapered) {
        StepProfileDef profileDef = asProfileDef(tapered.sweptArea());
        StepProfileBuilder.ProfileLoops baseProfile = profileBuilder.buildAreaProfileLoops(profileDef);
        Axis1Placement axis = builder.buildAxis1Placement(tapered.getAxis().id());
        double angle = tapered.angle();
        double taperAngle = tapered.taperAngle();
        if (angle <= 0.0 || angle > Math.PI * 2.0 + 1e-9) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID_TAPERED angle must be positive and <= 2*PI");
        }
        int segments = Math.max(12, (int) (angle / (Math.PI / 36.0)));
        // Build rings at different heights with taper scaling
        List<Face> faces = new ArrayList<>();
        List<Point2> profileOuter = baseProfile.getOuter();
        CircularFrame frame = circularFrame(axis.getAxis());
        for (int seg = 0; seg < segments; seg++) {
            double currentAngle = angle * seg / segments;
            double nextAngle = angle * (seg + 1) / segments;
            double currentScale = 1.0 - Math.tan(Math.abs(taperAngle)) * (angle * seg / segments / angle);
            double nextScale = 1.0 - Math.tan(Math.abs(taperAngle)) * (angle * (seg + 1) / segments / angle);
            if (currentScale <= 0.0 || nextScale <= 0.0) {
                throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID_TAPERED taper produces zero or negative profile");
            }
            // Build profile at current and next angle
            List<CartesianPoint> currentRing = revolveProfileAtAngle(profileOuter, axis, frame, currentAngle, currentScale);
            List<CartesianPoint> nextRing = revolveProfileAtAngle(profileOuter, axis, frame, nextAngle, nextScale);
            // Connect rings with faces
            for (int i = 0; i < profileOuter.size(); i++) {
                CartesianPoint a = currentRing.get(i);
                CartesianPoint b = currentRing.get((i + 1) % currentRing.size());
                CartesianPoint c = nextRing.get((i + 1) % nextRing.size());
                CartesianPoint d = nextRing.get(i);
                faces.add(builder.faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
            }
        }
        // End caps
        List<CartesianPoint> startRing = revolveProfileAtAngle(profileOuter, axis, frame, 0.0, 1.0);
        List<CartesianPoint> endRing = revolveProfileAtAngle(profileOuter, axis, frame, angle, 1.0 - Math.tan(Math.abs(taperAngle)));
        faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(startRing), frame.radialAtAngle(0.0).reverse()));
        faces.add(builder.faceFromPolyLoop(builder.closeLoop3(endRing), frame.radialAtAngle(angle)));
        return new Solid(new Shell(faces, true));
    }

    Solid buildSurfaceCurveSweptAreaSolid(StepSurfaceCurveSweptAreaSolid swept) {
        StepProfileDef profileDef = asProfileDef(swept.sweptArea());
        StepProfileBuilder.ProfileLoops profile = profileBuilder.buildAreaProfileLoops(profileDef);
        Curve3 trajectory = builder.buildCurve3(swept.trajectory());
        double startPoint = swept.startPoint();
        double endPoint = swept.endPoint();
        // Sample trajectory curve
        int segments = 48;
        List<Curve3Sample> samples = sampleCurve3WithTangent(trajectory, segments);
        // Adjust to start/end points
        int startIndex = (int) (startPoint * samples.size());
        int endIndex = (int) (endPoint * samples.size());
        if (startIndex < 0) startIndex = 0;
        if (endIndex > samples.size()) endIndex = samples.size();
        List<Curve3Sample> usedSamples = samples.subList(startIndex, endIndex);
        List<Face> faces = new ArrayList<>();
        List<List<CartesianPoint>> rings = new ArrayList<>();
        for (Curve3Sample sample : usedSamples) {
            // Build profile at this trajectory point
            List<CartesianPoint> ring = placeProfileAtPoint(profile.getOuter(), sample.getPoint(), sample.getTangent());
            rings.add(builder.closeLoop3(ring));
        }
        // End caps
        if (!rings.isEmpty()) {
            faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(rings.get(0)), usedSamples.get(0).tangent().reverse()));
            faces.add(builder.faceFromPolyLoop(builder.closeLoop3(rings.get(rings.size() - 1)), usedSamples.get(usedSamples.size() - 1).tangent()));
        }
        // Side faces
        for (int ringIndex = 0; ringIndex < rings.size() - 1; ringIndex++) {
            List<CartesianPoint> current = rings.get(ringIndex);
            List<CartesianPoint> next = rings.get(ringIndex + 1);
            for (int seg = 0; seg < current.size(); seg++) {
                CartesianPoint a = current.get(seg);
                CartesianPoint b = current.get((seg + 1) % current.size());
                CartesianPoint c = next.get((seg + 1) % next.size());
                CartesianPoint d = next.get(seg);
                faces.add(builder.faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    // ========================================================================
    // Private swept area solid builders
    // ========================================================================

    private Solid buildExtrudedAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        StepProfileBuilder.ProfileLoops profileLoops = profileBuilder.buildAreaProfileLoops(sweptAreaSolid.sweptArea());
        List<Point2> profile = profileLoops.getOuter();
        if (profile.size() < 3) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID requires at least 3 profile points");
        }
        Axis2Placement3D solidPlacement = builder.buildPlacement(sweptAreaSolid.getPosition().id());
        if (!(sweptAreaSolid.sweepReference() instanceof StepDirection)) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID extrusion direction must be a DIRECTION");
        }
        StepDirection direction = (StepDirection) sweptAreaSolid.sweepReference();
        Vector3 extrusion = builder.buildDirection(direction.id()).asVector().scale(sweptAreaSolid.parameter());
        if (extrusion.isZero()) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID requires a non-zero extrusion depth");
        }

        List<CartesianPoint> bottom = profile.stream()
                .map(point -> mapProfilePoint(sweptAreaSolid.sweptArea(), solidPlacement, point))
                .collect(Collectors.toList());
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(extrusion)).collect(Collectors.toList());
        List<List<CartesianPoint>> innerBottomLoops = profileLoops.inner().stream()
                .map(loop -> loop.stream()
                        .map(point -> mapProfilePoint(sweptAreaSolid.sweptArea(), solidPlacement, point))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<List<CartesianPoint>> innerTopLoops = innerBottomLoops.stream()
                .map(loop -> loop.stream().map(point -> point.add(extrusion)).collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<Face> faces = new ArrayList<>();
        Direction3 axis = Direction3.from(extrusion);
        faces.add(builder.faceFromProfileLoops(builder.reverseClosedLoop3(bottom), builder.reverseClosedLoops3(innerBottomLoops), axis.reverse()));
        faces.add(builder.faceFromProfileLoops(builder.closeLoop3(top), builder.closeLoops3(innerTopLoops), axis));
        for (int index = 0; index < bottom.size(); index++) {
            CartesianPoint startBottom = bottom.get(index);
            CartesianPoint endBottom = bottom.get((index + 1) % bottom.size());
            CartesianPoint endTop = top.get((index + 1) % top.size());
            CartesianPoint startTop = top.get(index);
            Vector3 edge = endBottom.subtract(startBottom);
            Direction3 normal = Direction3.from(edge.cross(extrusion));
            faces.add(builder.faceFromPolyLoop(
                    List.of(startBottom, endBottom, endTop, startTop, startBottom),
                    normal
            ));
        }
        for (int loopIndex = 0; loopIndex < innerBottomLoops.size(); loopIndex++) {
            List<CartesianPoint> innerBottom = innerBottomLoops.get(loopIndex);
            List<CartesianPoint> innerTop = innerTopLoops.get(loopIndex);
            for (int index = 0; index < innerBottom.size(); index++) {
                CartesianPoint startBottom = innerBottom.get(index);
                CartesianPoint endBottom = innerBottom.get((index + 1) % innerBottom.size());
                CartesianPoint endTop = innerTop.get((index + 1) % innerTop.size());
                CartesianPoint startTop = innerTop.get(index);
                Direction3 normal = quadNormal(startBottom, startTop, endTop, endBottom);
                faces.add(builder.faceFromPolyLoop(
                        List.of(startBottom, startTop, endTop, endBottom, startBottom),
                        normal
                ));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRevolvedAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        StepProfileBuilder.ProfileLoops profileLoops = profileBuilder.buildAreaProfileLoops(sweptAreaSolid.sweptArea());
        List<List<Point2>> profileRings = new ArrayList<>();
        profileRings.add(profileLoops.getOuter());
        profileRings.addAll(profileLoops.inner());
        if (profileLoops.getOuter().size() < 3) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID requires at least 3 profile points");
        }
        if (!(sweptAreaSolid.sweepReference() instanceof StepAxis1Placement)) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID axis must be an AXIS1_PLACEMENT");
        }
        StepAxis1Placement axisPlacement = (StepAxis1Placement) sweptAreaSolid.sweepReference();
        double angle = sweptAreaSolid.parameter();
        if (Math.abs(angle) <= 1.0e-9) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID requires a non-zero revolution angle");
        }
        if (Math.abs(angle) > Math.PI * 2.0 + 1.0e-9) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID revolution angle must not exceed 2*PI");
        }

        Axis2Placement3D solidPlacement = builder.buildPlacement(sweptAreaSolid.getPosition().id());
        Axis1Placement revolutionAxis = builder.buildAxis1Placement(axisPlacement.id());
        List<List<CartesianPoint>> sectionRings = profileRings.stream()
                .map(loop -> loop.stream()
                        .map(point -> mapProfilePoint(sweptAreaSolid.sweptArea(), solidPlacement, point))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        for (List<CartesianPoint> ring : sectionRings) {
            for (CartesianPoint point : ring) {
                if (distanceToAxis(point, revolutionAxis) <= 1.0e-9) {
                    throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID profile must not intersect the revolution axis");
                }
            }
        }

        int stepCount = Math.max(1, (int) Math.ceil(Math.abs(angle) / (Math.PI / 16.0)));
        boolean closedRevolution = Math.abs(Math.abs(angle) - Math.PI * 2.0) <= 1.0e-9;
        int sectionCount = closedRevolution ? stepCount : stepCount + 1;
        List<List<List<CartesianPoint>>> revolvedRings = sectionRings.stream()
                .map(ring -> {
                    List<List<CartesianPoint>> sections = new ArrayList<>(sectionCount);
                    for (int step = 0; step < sectionCount; step++) {
                        double sectionAngle = angle * step / stepCount;
                        sections.add(ring.stream()
                                .map(point -> rotateAroundAxis(point, revolutionAxis, sectionAngle))
                                .collect(Collectors.toList()));
                    }
                    return List.copyOf(sections);
                })
                .collect(Collectors.toList());

        List<Face> faces = new ArrayList<>();
        if (!closedRevolution) {
            List<CartesianPoint> outerStart = sectionRings.get(0);
            List<List<CartesianPoint>> innerStart = sectionRings.subList(1, sectionRings.size());
            List<CartesianPoint> outerEnd = revolvedRings.get(0).get(revolvedRings.get(0).size() - 1);
            List<List<CartesianPoint>> innerEnd = revolvedRings.stream()
                    .skip(1)
                    .map(ring -> ring.get(ring.size() - 1))
                    .collect(Collectors.toList());
            Vector3 startSweep = sweepDirectionAtSection(outerStart, revolutionAxis, angle >= 0.0);
            Vector3 endSweep = sweepDirectionAtSection(outerEnd, revolutionAxis, angle >= 0.0);
            faces.add(builder.faceFromProfileLoops(
                    builder.closeLoop3(outerStart),
                    builder.closeLoops3(innerStart),
                    polygonNormal(outerStart, startSweep.scale(-1.0))
            ));
            faces.add(builder.faceFromProfileLoops(
                    builder.reverseClosedLoop3(outerEnd),
                    builder.reverseClosedLoops3(innerEnd),
                    polygonNormal(outerEnd, endSweep)
            ));
        }
        for (List<List<CartesianPoint>> ringSections : revolvedRings) {
            int ringSize = ringSections.get(0).size();
            for (int sectionIndex = 0; sectionIndex < stepCount; sectionIndex++) {
                List<CartesianPoint> current = ringSections.get(sectionIndex);
                List<CartesianPoint> next = ringSections.get((sectionIndex + 1) % ringSections.size());
                for (int pointIndex = 0; pointIndex < ringSize; pointIndex++) {
                    CartesianPoint a = current.get(pointIndex);
                    CartesianPoint b = current.get((pointIndex + 1) % ringSize);
                    CartesianPoint c = next.get((pointIndex + 1) % ringSize);
                    CartesianPoint d = next.get(pointIndex);
                    faces.add(builder.faceFromPolyLoop(
                            List.of(a, b, c, d, a),
                            quadNormal(a, b, c, d)
                    ));
                }
            }
        }
        return new Solid(new Shell(faces, true));
    }

    // ========================================================================
    // Profile building methods
    // ========================================================================

    Solid buildExtrudedProfile(List<CartesianPoint> profile, Direction3 direction, double depth) {
        List<Face> faces = new ArrayList<>();
        Vector3 dirVec = direction.asVector();
        List<CartesianPoint> top = profile.stream()
                .map(p -> new CartesianPoint(
                        p.getX() + dirVec.getX() * depth,
                        p.getY() + dirVec.getY() * depth,
                        p.getZ() + dirVec.getZ() * depth))
                .collect(Collectors.toList());
        faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(profile), direction.reverse()));
        faces.add(builder.faceFromPolyLoop(top, direction));
        for (int i = 0; i < profile.size(); i++) {
            int next = (i + 1) % profile.size();
            faces.add(builder.faceFromPolyLoop(
                    List.of(profile.get(i), profile.get(next), top.get(next), top.get(i), profile.get(i)),
                    quadNormal(profile.get(i), profile.get(next), top.get(next), top.get(i))));
        }
        return new Solid(new Shell(faces, true));
    }

    Solid buildRevolvedProfile(List<CartesianPoint> profile, CartesianPoint axisOrigin,
                                        Vector3 axis, double angle) {
        int sections = Math.max(1, (int) Math.ceil(Math.abs(angle) / (Math.PI / 16.0)));
        boolean closedRevolution = Math.abs(Math.abs(angle) - Math.PI * 2.0) <= 1.0e-9;
        int sectionCount = closedRevolution ? sections : sections + 1;
        List<Face> faces = new ArrayList<>();
        Axis1Placement revolutionAxis = new Axis1Placement(axisOrigin, Direction3.from(axis));
        List<List<CartesianPoint>> rings = new ArrayList<>();
        for (int i = 0; i < sectionCount; i++) {
            double theta = angle * i / sections;
            List<CartesianPoint> ring = new ArrayList<>();
            for (CartesianPoint p : profile) {
                ring.add(rotateAroundAxis(p, revolutionAxis, theta));
            }
            rings.add(ring);
        }
        if (!closedRevolution) {
            Vector3 startSweep = sweepDirectionAtSection(rings.get(0), revolutionAxis, angle >= 0.0);
            Vector3 endSweep = sweepDirectionAtSection(rings.get(rings.size() - 1), revolutionAxis, angle >= 0.0);
            faces.add(builder.faceFromPolyLoop(
                    builder.closeLoop3(rings.get(0)),
                    polygonNormal(rings.get(0), startSweep.scale(-1.0))
            ));
            faces.add(builder.faceFromPolyLoop(
                    builder.reverseClosedLoop3(rings.get(rings.size() - 1)),
                    polygonNormal(rings.get(rings.size() - 1), endSweep)
            ));
        }
        for (int r = 0; r < rings.size() - 1; r++) {
            List<CartesianPoint> cur = rings.get(r);
            List<CartesianPoint> nxt = rings.get(r + 1);
            for (int i = 0; i < cur.size(); i++) {
                int next = (i + 1) % cur.size();
                faces.add(builder.faceFromPolyLoop(
                        List.of(cur.get(i), cur.get(next), nxt.get(next), nxt.get(i), cur.get(i)),
                        quadNormal(cur.get(i), cur.get(next), nxt.get(next), nxt.get(i))));
            }
        }
        if (closedRevolution) {
            List<CartesianPoint> cur = rings.get(rings.size() - 1);
            List<CartesianPoint> nxt = rings.get(0);
            for (int i = 0; i < cur.size(); i++) {
                int next = (i + 1) % cur.size();
                faces.add(builder.faceFromPolyLoop(
                        List.of(cur.get(i), cur.get(next), nxt.get(next), nxt.get(i), cur.get(i)),
                        quadNormal(cur.get(i), cur.get(next), nxt.get(next), nxt.get(i))));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    Solid buildSweptProfileAlongCurve(List<CartesianPoint> profile, List<Curve3Sample> samples) {
        if (samples.isEmpty()) {
            throw new UnsupportedGeometryException("trajectory curve has no samples");
        }
        List<Face> faces = new ArrayList<>();
        List<List<CartesianPoint>> rings = new ArrayList<>();
        for (Curve3Sample sample : samples) {
            rings.add(placeProfilePoints(profile, sample.point(), sample.tangent()));
        }
        // End caps
        if (!rings.isEmpty()) {
            faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(rings.get(0)), samples.get(0).tangent().reverse()));
            faces.add(builder.faceFromPolyLoop(builder.closeLoop3(rings.get(rings.size() - 1)), samples.get(samples.size() - 1).tangent()));
        }
        // Side faces
        for (int r = 0; r < rings.size() - 1; r++) {
            List<CartesianPoint> cur = rings.get(r);
            List<CartesianPoint> nxt = rings.get(r + 1);
            for (int i = 0; i < cur.size(); i++) {
                int next = (i + 1) % cur.size();
                faces.add(builder.faceFromPolyLoop(
                        List.of(cur.get(i), cur.get(next), nxt.get(next), nxt.get(i)),
                        samples.get(r).tangent()));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private List<CartesianPoint> placeProfilePoints(List<CartesianPoint> profile, CartesianPoint point, Direction3 tangent) {
        CircularFrame frame = circularFrame(tangent);
        Vector3 fx = frame.getX();
        Vector3 fy = frame.getY();
        Vector3 fz = frame.getZ().asVector();
        CartesianPoint first = profile.get(0);
        return profile.stream()
                .map(p -> point.add(fx.scale(p.getX() - first.getX())
                        .add(fy.scale(p.getY() - first.getY()))
                        .add(fz.scale(p.getZ() - first.getZ()))))
                .collect(Collectors.toList());
    }

    // ========================================================================
    // Profile conversion and helper methods
    // ========================================================================

    private StepProfileDef asProfileDef(StepEntity entity) {
        if (entity instanceof StepProfileDef) {
            StepProfileDef profileDef = (StepProfileDef) entity;
            return profileDef;
        }
        // Dedicated profile types with their own model classes
        if (entity instanceof StepCenteredCircleProfileDef) {
            return new StepProfileDef(
                entity.id(), "AREA", "", null, List.of(),
                List.of(((StepCenteredCircleProfileDef) entity).getRadius(),
                        ((StepCenteredCircleProfileDef) entity).centerOffset()),
                "CENTERED_CIRCLE_PROFILE_DEF");
        }
        if (entity instanceof StepCentreLineArcProfileDef) {
            return new StepProfileDef(
                entity.id(), "AREA", "", null, List.of(),
                List.of(((StepCentreLineArcProfileDef) entity).getRadius(),
                        ((StepCentreLineArcProfileDef) entity).angle()),
                "CENTRE_LINE_ARC_PROFILE_DEF");
        }
        if (entity instanceof StepRectangleHollowProfileDef) {
            StepRectangleHollowProfileDef def = (StepRectangleHollowProfileDef) entity;
            return new StepProfileDef(
                entity.id(), "AREA", "", null, List.of(),
                List.of(def.xDim(), def.yDim(), def.wallThickness(), def.innerRadius()),
                "RECTANGLE_HOLLOW_PROFILE_DEF");
        }
        // Profile wrappers that delegate to inner profileDef
        if (entity instanceof StepAreaProfile) {
            StepAreaProfile areaProfile = (StepAreaProfile) entity;
            return asProfileDef(areaProfile.profileDef());
        }
        if (entity instanceof StepGeneralizedAreaProfile) {
            StepGeneralizedAreaProfile generalizedProfile = (StepGeneralizedAreaProfile) entity;
            return asProfileDef(generalizedProfile.profileDef());
        }
        if (entity instanceof StepSweptProfileAreaOutline) {
            StepSweptProfileAreaOutline outline = (StepSweptProfileAreaOutline) entity;
            return asProfileDef(outline.profileDef());
        }
        throw new UnsupportedGeometryException("swept area must be a profile definition");
    }

    private List<Point2> scaleProfile(List<Point2> profile, double scale) {
        return profile.stream()
                .map(p -> new Point2(p.getX() * scale, p.getY() * scale))
                .collect(Collectors.toList());
    }

    private List<CartesianPoint> revolveProfileAtAngle(List<Point2> profile, Axis1Placement axis, CircularFrame frame, double angle, double scale) {
        Direction3 radial = frame.radialAtAngle(angle);
        return profile.stream()
                .map(p -> axis.getLocation().add(
                        radial.asVector().scale(p.getX() * scale)
                                .add(frame.getZ().asVector().scale(p.getY()))))
                .collect(Collectors.toList());
    }

    private List<CartesianPoint> placeProfileAtPoint(List<Point2> profile, CartesianPoint point, Direction3 tangent) {
        CircularFrame frame = circularFrame(tangent);
        return profile.stream()
                .map(p -> point.add(frame.getX().scale(p.getX()).add(frame.getY().scale(p.getY()))))
                .collect(Collectors.toList());
    }

    private CartesianPoint mapProfilePoint(StepProfileDef profile, Axis2Placement3D solidPlacement, Point2 point) {
        Point2 local = point;
        if (profile.getPosition() instanceof com.minicad.step.model.geometry.StepAxis2Placement2D) {
            com.minicad.step.model.geometry.StepAxis2Placement2D placement2D = (com.minicad.step.model.geometry.StepAxis2Placement2D) profile.getPosition();
            Point2 origin2 = builder.buildPoint2(placement2D.getLocation().id());
            com.minicad.geometry2d.Direction2 x2 = builder.buildDirection2(placement2D.getRefDirection().id());
            com.minicad.geometry2d.Direction2 y2 = new com.minicad.geometry2d.Direction2(-x2.getY(), x2.getX());
            local = origin2.add(x2.asVector().scale(point.getX())).add(y2.asVector().scale(point.getY()));
        }
        Vector3 alongX = solidPlacement.xDirection().asVector().scale(local.getX());
        Vector3 alongY = solidPlacement.yDirection().asVector().scale(local.getY());
        return solidPlacement.getLocation().add(alongX.add(alongY));
    }

    // ========================================================================
    // Geometry sampling and face boundary methods
    // ========================================================================

    private List<CartesianPoint> sampleFaceBoundary(SurfaceGeometry surface, int samples) {
        // For simple surfaces, sample the bounding box edges
        // This is a simplified approach - real implementation would need face bounds
        return sampleSurfaceBoundary(surface, samples);
    }

    private List<CartesianPoint> sampleSurfaceBoundary(SurfaceGeometry surface, int samples) {
        // Simplified boundary sampling - for planar surfaces, use bounding box
        if (surface instanceof Plane) {
            double bb = 10.0; // Default bounding box
            return List.of(
                    new CartesianPoint(-bb, -bb, 0),
                    new CartesianPoint(bb, -bb, 0),
                    new CartesianPoint(bb, bb, 0),
                    new CartesianPoint(-bb, bb, 0));
        }
        if (surface instanceof CylindricalSurface) {
            CylindricalSurface cyl = (CylindricalSurface) surface;
            return builder.buildCircleRing(cyl.getRadius(), samples);
        }
        // Generic fallback
        return List.of();
    }

    private List<CartesianPoint> sampleCircle3(
            CartesianPoint center,
            Vector3 xAxis,
            Vector3 yAxis,
            double radius,
            int segments
    ) {
        List<CartesianPoint> points = new ArrayList<>(segments);
        for (int index = 0; index < segments; index++) {
            double angle = Math.PI * 2.0 * index / segments;
            Vector3 offset = xAxis.scale(Math.cos(angle) * radius).add(yAxis.scale(Math.sin(angle) * radius));
            points.add(center.add(offset));
        }
        return List.copyOf(points);
    }

    private List<Curve3Sample> sampleCurve3WithTangent(Curve3 curve, int segments) {
        List<CartesianPoint> points = builder.sampleCurve3(curve, segments);
        List<Curve3Sample> samples = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            CartesianPoint current = points.get(i);
            CartesianPoint next = points.get((i + 1) % points.size());
            Vector3 tangent = new Vector3(next.getX() - current.getX(), next.getY() - current.getY(), next.getZ() - current.getZ()).normalize().asVector();
            samples.add(new Curve3Sample(current, Direction3.from(tangent)));
        }
        return samples;
    }

    // ========================================================================
    // Direction and axis building helpers
    // ========================================================================

    private Direction3 buildExtrusionDirection(StepEntity entity, String context) {
        if (entity instanceof StepDirection) {
            StepDirection stepDirection = (StepDirection) entity;
            return builder.buildDirection(stepDirection.id());
        }
        if (entity instanceof StepVector) {
            StepVector stepVector = (StepVector) entity;
            return builder.buildDirection(stepVector.isOrientation().id());
        }
        throw new UnsupportedGeometryException(context + " direction must be a DIRECTION or VECTOR");
    }

    private Axis1Placement buildRevolutionAxis(StepEntity entity, Axis2Placement3D solidPlacement, String context) {
        CartesianPoint location;
        Direction3 axis;
        if (entity instanceof StepAxis1Placement) {
            StepAxis1Placement axisPlacement = (StepAxis1Placement) entity;
            Axis1Placement localAxis = builder.buildAxis1Placement(axisPlacement.id());
            location = solidPlacement.transformToWorld(localAxis.getLocation());
            axis = solidPlacement.transformDirectionToWorld(localAxis.getAxis());
        } else if (entity instanceof StepDirection) {
            StepDirection stepDirection = (StepDirection) entity;
            location = solidPlacement.getLocation();
            axis = solidPlacement.transformDirectionToWorld(builder.buildDirection(stepDirection.id()));
        } else if (entity instanceof StepVector) {
            StepVector stepVector = (StepVector) entity;
            location = solidPlacement.getLocation();
            axis = solidPlacement.transformDirectionToWorld(builder.buildDirection(stepVector.isOrientation().id()));
        } else {
            throw new UnsupportedGeometryException(context + " axis must be an AXIS1_PLACEMENT, DIRECTION or VECTOR");
        }
        return new Axis1Placement(location, axis);
    }

    // ========================================================================
    // Rotation and axis geometry helpers
    // ========================================================================

    private CartesianPoint rotateAroundAxis(CartesianPoint point, Axis1Placement axis, double angle) {
        Vector3 axisVector = axis.getAxis().asVector();
        Vector3 relative = point.subtract(axis.getLocation());
        Vector3 parallel = axisVector.scale(relative.dot(axisVector));
        Vector3 radial = relative.subtract(parallel);
        Vector3 rotatedRadial = radial.scale(Math.cos(angle))
                .add(axisVector.cross(radial).scale(Math.sin(angle)));
        return axis.getLocation().add(parallel.add(rotatedRadial));
    }

    private double distanceToAxis(CartesianPoint point, Axis1Placement axis) {
        Vector3 relative = point.subtract(axis.getLocation());
        Vector3 parallel = axis.getAxis().asVector().scale(relative.dot(axis.getAxis().asVector()));
        return relative.subtract(parallel).norm();
    }

    private Vector3 sweepDirectionAtSection(List<CartesianPoint> section, Axis1Placement axis, boolean positiveAngle) {
        CartesianPoint sample = section.get(0);
        Vector3 radial = radialFromAxis(sample, axis);
        Vector3 tangent = axis.getAxis().asVector().cross(radial);
        if (tangent.isZero()) {
            tangent = axis.getAxis().asVector();
        }
        return positiveAngle ? tangent : tangent.scale(-1.0);
    }

    private Vector3 radialFromAxis(CartesianPoint point, Axis1Placement axis) {
        Vector3 relative = point.subtract(axis.getLocation());
        Vector3 parallel = axis.getAxis().asVector().scale(relative.dot(axis.getAxis().asVector()));
        return relative.subtract(parallel);
    }

    // ========================================================================
    // Normal calculation helpers
    // ========================================================================

    private Direction3 polygonNormal(List<CartesianPoint> points, Vector3 fallback) {
        Vector3 normal = new Vector3(0.0, 0.0, 0.0);
        for (int index = 0; index < points.size(); index++) {
            CartesianPoint current = points.get(index);
            CartesianPoint next = points.get((index + 1) % points.size());
            normal = normal.add(new Vector3(
                    (current.getY() - next.getY()) * (current.getZ() + next.getZ()),
                    (current.getZ() - next.getZ()) * (current.getX() + next.getX()),
                    (current.getX() - next.getX()) * (current.getY() + next.getY())
            ));
        }
        if (normal.isZero()) {
            normal = fallback;
        }
        if (normal.isZero()) {
            throw new UnsupportedGeometryException("revolved face normal is degenerate");
        }
        if (!fallback.isZero() && normal.dot(fallback) < 0.0) {
            normal = normal.scale(-1.0);
        }
        return Direction3.from(normal.normalize());
    }

    private Direction3 quadNormal(CartesianPoint a, CartesianPoint b, CartesianPoint c, CartesianPoint d) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.isZero()) {
            normal = c.subtract(a).cross(d.subtract(a));
        }
        if (normal.isZero()) {
            throw new UnsupportedGeometryException("revolved side face is degenerate");
        }
        return Direction3.from(normal.normalize());
    }

    // ========================================================================
    // Circular frame helpers
    // ========================================================================

    private CircularFrame circularFrame(Direction3 axis) {
        Vector3 z = axis.asVector();
        Vector3 reference = Math.abs(z.getZ()) < 0.9 ? new Vector3(0.0, 0.0, 1.0) : new Vector3(1.0, 0.0, 0.0);
        Vector3 x = z.cross(reference);
        if (x.isZero()) {
            reference = new Vector3(0.0, 1.0, 0.0);
            x = z.cross(reference);
        }
        x = x.normalize().asVector();
        Vector3 y = z.cross(x).normalize().asVector();
        return new CircularFrame(x, y);
    }

    private CircularFrame circularFrameAtPoint(CartesianPoint point, Direction3 tangent) {
        return circularFrame(tangent);
    }

    // ========================================================================
    // Face geometry helper
    // ========================================================================

    static StepEntity faceGeometry(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            return faceGeometry(orientedFace.faceElement());
        }
        throw new UnsupportedGeometryException("unsupported face subtype");
    }

    // ========================================================================
    // Inner classes
    // ========================================================================

    static class Curve3Sample {
        private final CartesianPoint point;
        private final Direction3 tangent;

        Curve3Sample(CartesianPoint point, Direction3 tangent) {
            this.point = point;
            this.tangent = tangent;
        }

        CartesianPoint point() { return point; }
        Direction3 tangent() { return tangent; }
        CartesianPoint getPoint() { return point; }
        Direction3 getTangent() { return tangent; }
    }

    private static class CircularFrame {
        private final Vector3 x;
        private final Vector3 y;

        CircularFrame(Vector3 x, Vector3 y) {
            this.x = x;
            this.y = y;
        }

        Vector3 x() { return x; }
        Vector3 y() { return y; }
        Vector3 getX() { return x; }
        Vector3 getY() { return y; }

        Direction3 radialAtAngle(double angle) {
            return Direction3.from(x.scale(Math.cos(angle)).add(y.scale(Math.sin(angle))));
        }
        Direction3 z() {
            return Direction3.from(x.cross(y));
        }
        Vector3 getZ() {
            return x.cross(y);
        }
    }
}