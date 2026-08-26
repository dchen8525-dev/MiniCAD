package com.minicad.step.semantic;
import com.minicad.step.model.StepBlockVolume;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis1Placement;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepBoxDomain;
import com.minicad.step.model.StepAdvancedBrep;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepFacetedBrepAndBrepWithVoids;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgPrimitive3D;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepCsgVolume;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepFacettedBrep;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds CSG (Constructive Solid Geometry) and Boolean operation solids from STEP entities.
 * Package-private class that delegates to StepCadBuilder for shared functionality.
 */
final class StepCadBooleanBuilder {

    private final StepCadBuilder builder;
    private final Map<Integer, StepEntity> entitiesById;

    StepCadBooleanBuilder(StepCadBuilder builder, Map<Integer, StepEntity> entitiesById) {
        this.builder = builder;
        this.entitiesById = entitiesById;
    }

    /**
     * Builds a solid from a CSG_VOLUME.
     *
     * @param csgVolume CSG volume entity
     * @return built solid
     */
    Solid buildCsgVolumeSolid(StepCsgVolume csgVolume) {
        // CSG_VOLUME has a treeRoot that may be a boolean result, primitive, or replica
        return buildBooleanOperandSolid(csgVolume.treeRoot());
    }

    /**
     * Builds a solid from a CSG primitive (BLOCK, SPHERE, CYLINDER, TORUS, etc.).
     *
     * @param csgPrimitive CSG primitive entity
     * @return built solid
     */
    Solid buildCsgPrimitive(StepCsgPrimitive csgPrimitive) {
        String entityName = csgPrimitive.entityName();
        switch (entityName) {
            case "BLOCK":
                return buildBlockPrimitive(csgPrimitive);
            case "SPHERE":
                return buildSpherePrimitive(csgPrimitive);
            case "ELLIPSOID":
                return buildEllipsoidPrimitive(csgPrimitive);
            case "RIGHT_CIRCULAR_CYLINDER":
                return buildRightCircularCylinderPrimitive(csgPrimitive);
            case "TORUS":
                return buildTorusPrimitive(csgPrimitive);
            case "RIGHT_ANGULAR_WEDGE":
                return buildRightAngularWedgePrimitive(csgPrimitive);
            case "RIGHT_CIRCULAR_CONE":
                return buildRightCircularConePrimitive(csgPrimitive);
            default:
                throw new UnsupportedGeometryException(entityName + " construction is unsupported");
        }
    }

    private Solid buildBlockPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("BLOCK position must be an AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement = (StepAxis2Placement3D) csgPrimitive.getPosition();
        if (csgPrimitive.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("BLOCK requires x, y and z dimensions");
        }
        Axis2Placement3D blockPlacement = builder.buildPlacement(placement.id());
        double x = csgPrimitive.dimensions().get(0);
        double y = csgPrimitive.dimensions().get(1);
        double z = csgPrimitive.dimensions().get(2);
        if (x <= 0.0 || y <= 0.0 || z <= 0.0) {
            throw new UnsupportedGeometryException("BLOCK dimensions must be positive");
        }
        CartesianPoint origin = blockPlacement.getLocation();
        Vector3 alongX = blockPlacement.xDirection().asVector().scale(x);
        Vector3 alongY = blockPlacement.yDirection().asVector().scale(y);
        Vector3 alongZ = blockPlacement.getAxis().asVector().scale(z);
        List<CartesianPoint> bottom = List.of(
                origin,
                origin.add(alongX),
                origin.add(alongX.add(alongY)),
                origin.add(alongY)
        );
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(alongZ)).collect(Collectors.toList());

        List<Face> faces = new ArrayList<>();
        faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(bottom), blockPlacement.getAxis().reverse()));
        faces.add(builder.faceFromPolyLoop(builder.closeLoop3(top), blockPlacement.getAxis()));
        for (int index = 0; index < bottom.size(); index++) {
            CartesianPoint startBottom = bottom.get(index);
            CartesianPoint endBottom = bottom.get((index + 1) % bottom.size());
            CartesianPoint endTop = top.get((index + 1) % top.size());
            CartesianPoint startTop = top.get(index);
            faces.add(builder.faceFromPolyLoop(
                    List.of(startBottom, endBottom, endTop, startTop, startBottom),
                    quadNormal(startBottom, endBottom, endTop, startTop)
            ));
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildSpherePrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("SPHERE position must be an AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement = (StepAxis2Placement3D) csgPrimitive.getPosition();
        if (csgPrimitive.dimensions().isEmpty()) {
            throw new UnsupportedGeometryException("SPHERE requires a radius");
        }
        double radius = csgPrimitive.dimensions().get(0);
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("SPHERE radius must be positive");
        }
        return buildEllipsoidLike(builder.buildPlacement(placement.id()), radius, radius, radius, 24, 12);
    }

    private Solid buildEllipsoidPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("ELLIPSOID position must be an AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement = (StepAxis2Placement3D) csgPrimitive.getPosition();
        if (csgPrimitive.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("ELLIPSOID requires three semi axes");
        }
        double rx = csgPrimitive.dimensions().get(0);
        double ry = csgPrimitive.dimensions().get(1);
        double rz = csgPrimitive.dimensions().get(2);
        if (rx <= 0.0 || ry <= 0.0 || rz <= 0.0) {
            throw new UnsupportedGeometryException("ELLIPSOID semi axes must be positive");
        }
        return buildEllipsoidLike(builder.buildPlacement(placement.id()), rx, ry, rz, 24, 12);
    }

    private Solid buildRightCircularCylinderPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.getPosition() instanceof StepAxis1Placement)) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CYLINDER position must be an AXIS1_PLACEMENT");
        }
        StepAxis1Placement placement = (StepAxis1Placement) csgPrimitive.getPosition();
        if (csgPrimitive.dimensions().size() < 2) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CYLINDER requires height and radius");
        }
        double height = csgPrimitive.dimensions().get(0);
        double radius = csgPrimitive.dimensions().get(1);
        if (height <= 0.0 || radius <= 0.0) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CYLINDER dimensions must be positive");
        }
        Axis1Placement axis = builder.buildAxis1Placement(placement.id());
        CircularFrame frame = circularFrame(axis.getAxis());
        Vector3 alongAxis = axis.getAxis().asVector().scale(height);
        List<CartesianPoint> bottom = sampleCircle3(axis.getLocation(), frame.getX(), frame.getY(), radius, 48);
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(alongAxis)).collect(Collectors.toList());
        List<Face> faces = new ArrayList<>();
        faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(bottom), axis.getAxis().reverse()));
        faces.add(builder.faceFromPolyLoop(builder.closeLoop3(top), axis.getAxis()));
        for (int index = 0; index < bottom.size(); index++) {
            CartesianPoint a = bottom.get(index);
            CartesianPoint b = bottom.get((index + 1) % bottom.size());
            CartesianPoint c = top.get((index + 1) % top.size());
            CartesianPoint d = top.get(index);
            faces.add(builder.faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildTorusPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.getPosition() instanceof StepAxis1Placement)) {
            throw new UnsupportedGeometryException("TORUS position must be an AXIS1_PLACEMENT");
        }
        StepAxis1Placement placement = (StepAxis1Placement) csgPrimitive.getPosition();
        if (csgPrimitive.dimensions().size() < 2) {
            throw new UnsupportedGeometryException("TORUS requires major and minor radii");
        }
        double majorRadius = csgPrimitive.dimensions().get(0);
        double minorRadius = csgPrimitive.dimensions().get(1);
        if (majorRadius <= 0.0 || minorRadius <= 0.0 || majorRadius <= minorRadius) {
            throw new UnsupportedGeometryException("TORUS radii must satisfy major > minor > 0");
        }
        Axis1Placement axis = builder.buildAxis1Placement(placement.id());
        CircularFrame frame = circularFrame(axis.getAxis());
        int uSegments = 32;
        int vSegments = 16;
        List<List<CartesianPoint>> grid = new ArrayList<>(uSegments);
        for (int uIndex = 0; uIndex < uSegments; uIndex++) {
            double u = Math.PI * 2.0 * uIndex / uSegments;
            Vector3 radial = frame.getX().scale(Math.cos(u)).add(frame.getY().scale(Math.sin(u)));
            Vector3 tangent = frame.getX().scale(-Math.sin(u)).add(frame.getY().scale(Math.cos(u)));
            List<CartesianPoint> ring = new ArrayList<>(vSegments);
            for (int vIndex = 0; vIndex < vSegments; vIndex++) {
                double v = Math.PI * 2.0 * vIndex / vSegments;
                Vector3 offset = radial.scale(majorRadius + Math.cos(v) * minorRadius)
                        .add(axis.getAxis().asVector().scale(Math.sin(v) * minorRadius));
                ring.add(axis.getLocation().add(offset));
            }
            grid.add(List.copyOf(ring));
        }
        List<Face> faces = new ArrayList<>();
        for (int uIndex = 0; uIndex < uSegments; uIndex++) {
            List<CartesianPoint> current = grid.get(uIndex);
            List<CartesianPoint> next = grid.get((uIndex + 1) % uSegments);
            for (int vIndex = 0; vIndex < vSegments; vIndex++) {
                CartesianPoint a = current.get(vIndex);
                CartesianPoint b = current.get((vIndex + 1) % vSegments);
                CartesianPoint c = next.get((vIndex + 1) % vSegments);
                CartesianPoint d = next.get(vIndex);
                addTriangleFace(faces, a, b, c, outwardApproximation(a, axis.getLocation()));
                addTriangleFace(faces, a, c, d, outwardApproximation(d, axis.getLocation()));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRightAngularWedgePrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("RIGHT_ANGULAR_WEDGE position must be an AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement = (StepAxis2Placement3D) csgPrimitive.getPosition();
        if (csgPrimitive.dimensions().size() < 4) {
            throw new UnsupportedGeometryException("RIGHT_ANGULAR_WEDGE requires x, y, z and ltx dimensions");
        }
        double x = csgPrimitive.dimensions().get(0);
        double y = csgPrimitive.dimensions().get(1);
        double z = csgPrimitive.dimensions().get(2);
        double ltx = csgPrimitive.dimensions().get(3);
        if (x <= 0.0 || y <= 0.0 || z <= 0.0 || ltx <= 0.0 || ltx > x) {
            throw new UnsupportedGeometryException("RIGHT_ANGULAR_WEDGE dimensions must satisfy x,y,z,ltx > 0 and ltx <= x");
        }
        Axis2Placement3D wedgePlacement = builder.buildPlacement(placement.id());
        CartesianPoint a = pointOnPlacement(wedgePlacement, 0.0, 0.0, 0.0);
        CartesianPoint b = pointOnPlacement(wedgePlacement, x, 0.0, 0.0);
        CartesianPoint c = pointOnPlacement(wedgePlacement, x, y, 0.0);
        CartesianPoint d = pointOnPlacement(wedgePlacement, 0.0, y, 0.0);
        CartesianPoint e = pointOnPlacement(wedgePlacement, 0.0, 0.0, z);
        CartesianPoint f = pointOnPlacement(wedgePlacement, x, 0.0, z);
        CartesianPoint g = pointOnPlacement(wedgePlacement, ltx, y, z);
        CartesianPoint h = pointOnPlacement(wedgePlacement, 0.0, y, z);
        List<Face> faces = new ArrayList<>();
        faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(List.of(a, b, c, d)), wedgePlacement.getAxis().reverse()));
        faces.add(builder.faceFromPolyLoop(builder.closeLoop3(List.of(e, f, g, h)), wedgePlacement.getAxis()));
        faces.add(builder.faceFromPolyLoop(List.of(a, b, f, e, a), quadNormal(a, b, f, e)));
        faces.add(builder.faceFromPolyLoop(List.of(d, h, g, c, d), quadNormal(d, h, g, c)));
        faces.add(builder.faceFromPolyLoop(List.of(a, d, h, e, a), quadNormal(a, d, h, e)));
        addTriangleFace(faces, b, c, g, quadNormal(b, c, g, f).asVector());
        addTriangleFace(faces, b, g, f, quadNormal(b, c, g, f).asVector());
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRightCircularConePrimitive(StepCsgPrimitive csgPrimitive) {
        // Accept either AXIS1_PLACEMENT or AXIS2_PLACEMENT_3D
        Axis1Placement axis;
        if (csgPrimitive.getPosition() instanceof StepAxis1Placement) {
            StepAxis1Placement placement = (StepAxis1Placement) csgPrimitive.getPosition();
            axis = builder.buildAxis1Placement(placement.id());
        } else if (csgPrimitive.getPosition() instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement = (StepAxis2Placement3D) csgPrimitive.getPosition();
            // Use the z-axis direction from AXIS2_PLACEMENT_3D as the cone axis
            Axis2Placement3D pl = builder.buildPlacement(placement.id());
            axis = new Axis1Placement(pl.getLocation(), pl.getAxis());
        } else {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CONE position must be an AXIS1_PLACEMENT or AXIS2_PLACEMENT_3D");
        }
        if (csgPrimitive.dimensions().size() < 2) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CONE requires height and radius");
        }
        double height = csgPrimitive.dimensions().get(0);
        double radius = csgPrimitive.dimensions().get(1);
        if (height <= 0.0 || radius <= 0.0) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CONE dimensions must be positive");
        }
        CircularFrame frame = circularFrame(axis.getAxis());
        Vector3 alongAxis = axis.getAxis().asVector().scale(height);
        CartesianPoint apex = axis.getLocation().add(alongAxis);
        List<CartesianPoint> base = sampleCircle3(axis.getLocation(), frame.getX(), frame.getY(), radius, 48);
        List<Face> faces = new ArrayList<>();
        // Base face
        faces.add(builder.faceFromPolyLoop(builder.reverseClosedLoop3(base), axis.getAxis().reverse()));
        // Lateral faces as triangles connecting base to apex
        for (int index = 0; index < base.size(); index++) {
            CartesianPoint a = base.get(index);
            CartesianPoint b = base.get((index + 1) % base.size());
            Vector3 midVector = new Vector3(
                    (a.getX() + b.getX()) / 2.0 - apex.getX(),
                    (a.getY() + b.getY()) / 2.0 - apex.getY(),
                    (a.getZ() + b.getZ()) / 2.0 - apex.getZ()
            );
            Vector3 edgeVector = new Vector3(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
            Vector3 normal = edgeVector.cross(midVector).normalize().asVector();
            addTriangleFace(faces, a, b, apex, normal);
        }
        return new Solid(new Shell(faces, true));
    }

    /**
     * Builds a solid from a Boolean result operation.
     *
     * @param operator the Boolean operator (DIFFERENCE, INTERSECTION, UNION)
     * @param first first operand entity
     * @param second second operand entity
     * @return built solid
     */
    Solid buildBooleanResult(String operator, StepEntity first, StepEntity second) {
        String normalizedOperator = operator == null ? "" : operator.replace(".", "").trim().toUpperCase();
        switch (normalizedOperator) {
            case "DIFFERENCE": {
                HalfSpaceOperand halfSpace = asHalfSpaceOperand(second);
                if (halfSpace != null) {
                    return clipSolidWithHalfSpace(buildBooleanOperandSolid(first), halfSpace, false);
                }
                throw new UnsupportedGeometryException(
                        "BOOLEAN_RESULT difference requires a HALF_SPACE_SOLID, BOXED_HALF_SPACE, or POLYGONAL_BOUNDED_HALF_SPACE as second operand");
            }
            case "INTERSECTION": {
                HalfSpaceOperand halfSpace = asHalfSpaceOperand(second);
                if (halfSpace != null) {
                    return clipSolidWithHalfSpace(buildBooleanOperandSolid(first), halfSpace, true);
                }
                halfSpace = asHalfSpaceOperand(first);
                if (halfSpace != null) {
                    return clipSolidWithHalfSpace(buildBooleanOperandSolid(second), halfSpace, true);
                }
                throw new UnsupportedGeometryException(
                        "BOOLEAN_RESULT intersection requires one operand to be a HALF_SPACE_SOLID, BOXED_HALF_SPACE, or POLYGONAL_BOUNDED_HALF_SPACE");
            }
            case "UNION": {
                // UNION with half-space: extend solid into half-space region
                // This is the inverse of DIFFERENCE with half-space
                HalfSpaceOperand halfSpace = asHalfSpaceOperand(second);
                if (halfSpace != null) {
                    return unionWithHalfSpace(buildBooleanOperandSolid(first), halfSpace);
                }
                halfSpace = asHalfSpaceOperand(first);
                if (halfSpace != null) {
                    return unionWithHalfSpace(buildBooleanOperandSolid(second), halfSpace);
                }
                throw new UnsupportedGeometryException(
                        "BOOLEAN_RESULT union requires one operand to be a HALF_SPACE_SOLID, BOXED_HALF_SPACE, or POLYGONAL_BOUNDED_HALF_SPACE; solid-solid union is not supported");
            }
            default:
                throw new UnsupportedGeometryException("BOOLEAN_RESULT operator " + normalizedOperator + " is unsupported");
        }
    }

    /** Extracted half-space parameters shared by StepHalfSpaceSolid and StepPolygonalBoundedHalfSpace. */
    private static final class HalfSpaceOperand {
        private final StepEntity surface;
        private final boolean agreementFlag;
        private final StepEntity enclosure;
        private final String entityName;

        HalfSpaceOperand(StepEntity surface, boolean agreementFlag, StepEntity enclosure, String entityName) {
            this.surface = surface;
            this.agreementFlag = agreementFlag;
            this.enclosure = enclosure;
            this.entityName = entityName;
        }

        StepEntity surface() { return surface; }
        boolean agreementFlag() { return agreementFlag; }
        StepEntity enclosure() { return enclosure; }
        String entityName() { return entityName; }
    }

    private HalfSpaceOperand asHalfSpaceOperand(StepEntity operand) {
        if (operand instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid hs = (StepHalfSpaceSolid) operand;
            return new HalfSpaceOperand(hs.baseSurface(), hs.agreementFlag(), hs.enclosure(), hs.entityName());
        }
        if (operand instanceof StepPolygonalBoundedHalfSpace) {
            StepPolygonalBoundedHalfSpace hs = (StepPolygonalBoundedHalfSpace) operand;
            return new HalfSpaceOperand(hs.basisSurface(), hs.sameSense(), null, "POLYGONAL_BOUNDED_HALF_SPACE");
        }
        return null;
    }

    /**
     * Builds a solid from a Boolean operand entity.
     *
     * @param operand the operand entity
     * @return built solid
     */
    Solid buildBooleanOperandSolid(StepEntity operand) {
        if (operand instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solidBrep = (StepManifoldSolidBrep) operand;
            return builder.buildSolid(solidBrep.id());
        }
        if (operand instanceof StepFacettedBrep) {
            StepFacettedBrep facettedBrep = (StepFacettedBrep) operand;
            return builder.buildSolid(facettedBrep.id());
        }
        if (operand instanceof StepBrepWithVoids) {
            StepBrepWithVoids brepWithVoids = (StepBrepWithVoids) operand;
            return builder.buildSolid(brepWithVoids.id());
        }
        if (operand instanceof StepFacetedBrepAndBrepWithVoids) {
            StepFacetedBrepAndBrepWithVoids facetedBrepWithVoids = (StepFacetedBrepAndBrepWithVoids) operand;
            return builder.buildSolid(facetedBrepWithVoids.id());
        }
        if (operand instanceof StepBlockVolume) {
            return builder.buildSolid(operand.id());
        }
        if (operand instanceof StepNonManifoldSolidBrep) {
            StepNonManifoldSolidBrep nonManifold = (StepNonManifoldSolidBrep) operand;
            return builder.buildSolid(nonManifold.id());
        }
        if (operand instanceof StepAdvancedBrep) {
            StepAdvancedBrep advancedBrep = (StepAdvancedBrep) operand;
            return builder.buildSolid(advancedBrep.id());
        }
        if (operand instanceof StepCsgPrimitive) {
            StepCsgPrimitive csgPrimitive = (StepCsgPrimitive) operand;
            return buildCsgPrimitive(csgPrimitive);
        }
        if (operand instanceof StepCsgPrimitive3D) {
            StepCsgPrimitive3D csg3D = (StepCsgPrimitive3D) operand;
            // CSG_PRIMITIVE_3D is a reference wrapper; build solid from the position entity
            StepEntity actual = entitiesById.get(csg3D.getPosition().id());
            if (actual != null && actual instanceof StepCsgPrimitive) {
                StepCsgPrimitive primitive = (StepCsgPrimitive) actual;
                return buildCsgPrimitive(primitive);
            }
            throw new UnsupportedGeometryException("CSG_PRIMITIVE_3D #" + csg3D.id() + " position must reference a CSG primitive");
        }
        if (operand instanceof StepCsgVolume) {
            StepCsgVolume csgVolume = (StepCsgVolume) operand;
            return buildCsgVolumeSolid(csgVolume);
        }
        if (operand instanceof StepCsgSolid) {
            StepCsgSolid csgSolid = (StepCsgSolid) operand;
            return buildBooleanOperandSolid(csgSolid.treeRootExpression());
        }
        if (operand instanceof StepSolidReplica) {
            StepSolidReplica solidReplica = (StepSolidReplica) operand;
            return builder.buildSolid(solidReplica.id());
        }
        if (operand instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) operand;
            return builder.buildSweptAreaSolid(sweptAreaSolid);
        }
        if (operand instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid sweptDiskSolid = (StepSweptDiskSolid) operand;
            return builder.buildSweptDiskSolid(sweptDiskSolid);
        }
        if (operand instanceof StepExtrudedAreaSolidTapered) {
            StepExtrudedAreaSolidTapered taperedExtrusion = (StepExtrudedAreaSolidTapered) operand;
            return builder.buildExtrudedAreaSolidTapered(taperedExtrusion);
        }
        if (operand instanceof StepRevolvedAreaSolidTapered) {
            StepRevolvedAreaSolidTapered taperedRevolution = (StepRevolvedAreaSolidTapered) operand;
            return builder.buildRevolvedAreaSolidTapered(taperedRevolution);
        }
        if (operand instanceof StepSurfaceCurveSweptAreaSolid) {
            StepSurfaceCurveSweptAreaSolid surfaceCurveSweep = (StepSurfaceCurveSweptAreaSolid) operand;
            return builder.buildSurfaceCurveSweptAreaSolid(surfaceCurveSweep);
        }
        if (operand instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult clippingResult = (StepBooleanClippingResult) operand;
            return buildBooleanResult(clippingResult.operator(), clippingResult.firstOperand(), clippingResult.secondOperand());
        }
        if (operand instanceof StepBooleanResult) {
            StepBooleanResult booleanResult = (StepBooleanResult) operand;
            return buildBooleanResult(booleanResult.operator(), booleanResult.firstOperand(), booleanResult.secondOperand());
        }
        if (operand instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid halfSpace = (StepHalfSpaceSolid) operand;
            return builder.buildHalfSpaceSolid(halfSpace);
        }
        if (operand instanceof StepPolygonalBoundedHalfSpace) {
            StepPolygonalBoundedHalfSpace polyHalfSpace = (StepPolygonalBoundedHalfSpace) operand;
            return builder.buildPolygonalBoundedHalfSpace(polyHalfSpace);
        }
        if (operand instanceof StepSolidModel) {
            StepSolidModel solidModel = (StepSolidModel) operand;
            // SolidModel is the abstract base type; check for concrete subtype at same ID.
            StepEntity actual = entitiesById.get(solidModel.id());
            if (actual != null && actual != solidModel && builder.canBuildAsSolid(actual)) {
                return builder.buildSolid(solidModel.id());
            }
            throw new com.minicad.common.StepResolutionException("entity #" + solidModel.id() + " is an abstract SOLID_MODEL with no concrete subtype");
        }
        if (operand instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) operand;
            return buildBooleanOperandSolid(mappedItem.mappingTarget());
        }
        throw new UnsupportedGeometryException("boolean operand " + StepCadBuilder.stepEntityTypeName(operand) + " is unsupported");
    }

    private Solid clipSolidWithHalfSpace(Solid solid, HalfSpaceOperand halfSpace, boolean keepAgreementSide) {
        Plane plane = builder.buildSupportedPlaneGeometry(halfSpace.surface(), halfSpace.entityName());
        if (plane == null) {
            throw new UnsupportedGeometryException(halfSpace.entityName() + " requires PLANE geometry");
        }
        boolean keepPositive = keepAgreementSide ? halfSpace.agreementFlag() : !halfSpace.agreementFlag();
        Solid clipped = clipSolidWithPlane(solid, plane, keepPositive, "BOOLEAN_RESULT clipping");
        if (halfSpace.enclosure() == null) {
            return clipped;
        }
        if (!(halfSpace.enclosure() instanceof StepBoxDomain)) {
            throw new UnsupportedGeometryException(
                    halfSpace.entityName() + " construction with "
                            + StepCadBuilder.stepEntityTypeName(halfSpace.enclosure()) + " enclosure is unsupported");
        }
        StepBoxDomain boxDomain = (StepBoxDomain) halfSpace.enclosure();
        return clipSolidWithBoxDomain(clipped, boxDomain, "BOOLEAN_RESULT clipping");
    }

    private Solid unionWithHalfSpace(Solid solid, HalfSpaceOperand halfSpace) {
        Plane plane = builder.buildSupportedPlaneGeometry(halfSpace.surface(), halfSpace.entityName());
        if (plane == null) {
            throw new UnsupportedGeometryException(halfSpace.entityName() + " requires PLANE geometry");
        }
        // Union with half-space: extend solid into half-space agreement side
        // This creates a new solid that includes both the original solid and the half-space region
        // For bounded half-space (BOXED_HALF_SPACE), union creates solid + box portion on agreement side
        if (halfSpace.enclosure() == null) {
            // Unbounded half-space union would create infinite geometry - not supported
            throw new UnsupportedGeometryException(
                    "BOOLEAN_RESULT union with unbounded " + halfSpace.entityName() + " is not supported: would create infinite geometry");
        }
        if (!(halfSpace.enclosure() instanceof StepBoxDomain)) {
            throw new UnsupportedGeometryException(
                    halfSpace.entityName() + " union with "
                            + StepCadBuilder.stepEntityTypeName(halfSpace.enclosure()) + " enclosure is unsupported");
        }
        StepBoxDomain boxDomain = (StepBoxDomain) halfSpace.enclosure();
        // Build box domain geometry and merge with solid
        Solid boxSolid = buildBoxDomainSolid(boxDomain);
        // Clip box to half-space agreement side
        boolean keepAgreementSide = halfSpace.agreementFlag();
        Solid halfSpaceBox = clipSolidWithPlane(boxSolid, plane, keepAgreementSide, "UNION half-space box");
        // Merge solids: union of solid and halfSpaceBox
        return mergeSolids(solid, halfSpaceBox);
    }

    private Solid buildBoxDomainSolid(StepBoxDomain boxDomain) {
        CartesianPoint min = builder.buildPoint(boxDomain.corner().id());
        if (boxDomain.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("BOX_DOMAIN requires x, y and z dimensions");
        }
        double dx = boxDomain.dimensions().get(0);
        double dy = boxDomain.dimensions().get(1);
        double dz = boxDomain.dimensions().get(2);
        if (dx <= 0.0 || dy <= 0.0 || dz <= 0.0) {
            throw new UnsupportedGeometryException("BOX_DOMAIN dimensions must be positive");
        }
        CartesianPoint max = new CartesianPoint(min.getX() + dx, min.getY() + dy, min.getZ() + dz);
        // Build box from 6 faces
        List<Face> faces = new ArrayList<>();
        faces.add(builder.faceFromPolyLoop(List.of(
                new CartesianPoint(min.getX(), min.getY(), min.getZ()),
                new CartesianPoint(max.getX(), min.getY(), min.getZ()),
                new CartesianPoint(max.getX(), max.getY(), min.getZ()),
                new CartesianPoint(min.getX(), max.getY(), min.getZ()),
                new CartesianPoint(min.getX(), min.getY(), min.getZ())
        ), new Direction3(0.0, 0.0, -1.0)));
        faces.add(builder.faceFromPolyLoop(List.of(
                new CartesianPoint(min.getX(), min.getY(), max.getZ()),
                new CartesianPoint(min.getX(), max.getY(), max.getZ()),
                new CartesianPoint(max.getX(), max.getY(), max.getZ()),
                new CartesianPoint(max.getX(), min.getY(), max.getZ()),
                new CartesianPoint(min.getX(), min.getY(), max.getZ())
        ), new Direction3(0.0, 0.0, 1.0)));
        faces.add(builder.faceFromPolyLoop(List.of(
                new CartesianPoint(min.getX(), min.getY(), min.getZ()),
                new CartesianPoint(min.getX(), max.getY(), min.getZ()),
                new CartesianPoint(min.getX(), max.getY(), max.getZ()),
                new CartesianPoint(min.getX(), min.getY(), max.getZ()),
                new CartesianPoint(min.getX(), min.getY(), min.getZ())
        ), new Direction3(-1.0, 0.0, 0.0)));
        faces.add(builder.faceFromPolyLoop(List.of(
                new CartesianPoint(max.getX(), min.getY(), min.getZ()),
                new CartesianPoint(max.getX(), min.getY(), max.getZ()),
                new CartesianPoint(max.getX(), max.getY(), max.getZ()),
                new CartesianPoint(max.getX(), max.getY(), min.getZ()),
                new CartesianPoint(max.getX(), min.getY(), min.getZ())
        ), new Direction3(1.0, 0.0, 0.0)));
        faces.add(builder.faceFromPolyLoop(List.of(
                new CartesianPoint(min.getX(), min.getY(), min.getZ()),
                new CartesianPoint(min.getX(), min.getY(), max.getZ()),
                new CartesianPoint(max.getX(), min.getY(), max.getZ()),
                new CartesianPoint(max.getX(), min.getY(), min.getZ()),
                new CartesianPoint(min.getX(), min.getY(), min.getZ())
        ), new Direction3(0.0, -1.0, 0.0)));
        faces.add(builder.faceFromPolyLoop(List.of(
                new CartesianPoint(min.getX(), max.getY(), min.getZ()),
                new CartesianPoint(max.getX(), max.getY(), min.getZ()),
                new CartesianPoint(max.getX(), max.getY(), max.getZ()),
                new CartesianPoint(min.getX(), max.getY(), max.getZ()),
                new CartesianPoint(min.getX(), max.getY(), min.getZ())
        ), new Direction3(0.0, 1.0, 0.0)));
        return new Solid(new Shell(faces, true));
    }

    private Solid mergeSolids(Solid first, Solid second) {
        // Simple merge: combine all faces from both solids
        // This works when solids don't overlap or share boundaries
        List<Face> mergedFaces = new ArrayList<>();
        mergedFaces.addAll(first.getOuterShell().getFaces());
        mergedFaces.addAll(second.getOuterShell().getFaces());
        return new Solid(new Shell(mergedFaces, true));
    }

    private Solid clipSolidWithBoxDomain(Solid solid, StepBoxDomain boxDomain, String context) {
        CartesianPoint min = builder.buildPoint(boxDomain.corner().id());
        if (boxDomain.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("BOX_DOMAIN requires x, y and z dimensions");
        }
        double dx = boxDomain.dimensions().get(0);
        double dy = boxDomain.dimensions().get(1);
        double dz = boxDomain.dimensions().get(2);
        if (dx <= 0.0 || dy <= 0.0 || dz <= 0.0) {
            throw new UnsupportedGeometryException("BOX_DOMAIN dimensions must be positive");
        }
        CartesianPoint max = new CartesianPoint(min.getX() + dx, min.getY() + dy, min.getZ() + dz);
        Solid clipped = solid;
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(min, 1.0, 0.0, 0.0), true, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(max, 1.0, 0.0, 0.0), false, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(min, 0.0, 1.0, 0.0), true, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(max, 0.0, 1.0, 0.0), false, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(min, 0.0, 0.0, 1.0), true, context);
        return clipSolidWithPlane(clipped, axisAlignedPlane(max, 0.0, 0.0, 1.0), false, context);
    }

    private Plane axisAlignedPlane(CartesianPoint origin, double x, double y, double z) {
        return new Plane(origin, Direction3.from(new Vector3(x, y, z)));
    }

    private Solid clipSolidWithPlane(Solid solid, Plane plane, boolean keepPositive, String context) {
        List<Face> clippedFaces = new ArrayList<>();
        List<CartesianPoint> capPoints = new ArrayList<>();
        for (Face face : solid.getOuterShell().getFaces()) {
            List<CartesianPoint> polygon = outerLoopPoints(face);
            List<CartesianPoint> clipped = clipPolygonWithPlane(polygon, plane, keepPositive, capPoints);
            if (clipped.size() >= 3) {
                Plane facePlane = requirePlaneSurface(face, context);
                clippedFaces.add(builder.faceFromPolyLoop(builder.closeLoop3(clipped), polygonNormal(clipped, facePlane.getNormal().asVector())));
            }
        }
        List<CartesianPoint> capLoop = buildCapLoop(capPoints, plane);
        if (capLoop.size() >= 3) {
            Direction3 capNormal = keepPositive ? plane.getNormal().reverse() : plane.getNormal();
            clippedFaces.add(builder.faceFromPolyLoop(builder.closeLoop3(capLoop), capNormal));
        }
        if (clippedFaces.isEmpty()) {
            throw new UnsupportedGeometryException(context + " removed the entire solid");
        }
        return new Solid(new Shell(clippedFaces, true));
    }

    private List<CartesianPoint> outerLoopPoints(Face face) {
        for (FaceBound bound : face.getBounds()) {
            if (!bound.isOuter()) {
                continue;
            }
            if (bound.getLoop() instanceof PolyLoop) {
                PolyLoop polyLoop = (PolyLoop) bound.getLoop();
                List<CartesianPoint> points = polyLoop.getPoints();
                return stripClosedPoint(points);
            }
            if (bound.getLoop() instanceof com.minicad.topology.EdgeLoop) {
                com.minicad.topology.EdgeLoop edgeLoop = (com.minicad.topology.EdgeLoop) bound.getLoop();
                List<CartesianPoint> points = new ArrayList<>(edgeLoop.edges().size());
                for (com.minicad.topology.OrientedEdge edge : edgeLoop.edges()) {
                    points.add(edge.startVertex().point());
                }
                return points;
            }
        }
        throw new UnsupportedGeometryException("boolean clipping requires a polygonal outer loop");
    }

    private Plane requirePlaneSurface(Face face, String context) {
        if (face.getSurface() instanceof Plane) {
            Plane plane = (Plane) face.getSurface();
            return plane;
        }
        throw new UnsupportedGeometryException(context + " requires planar topology faces");
    }

    private List<CartesianPoint> stripClosedPoint(List<CartesianPoint> points) {
        if (points.size() >= 2 && points.get(0).distanceTo(points.get(points.size() - 1)) <= 1.0e-9) {
            return List.copyOf(points.subList(0, points.size() - 1));
        }
        return List.copyOf(points);
    }

    private List<CartesianPoint> clipPolygonWithPlane(
            List<CartesianPoint> polygon,
            Plane plane,
            boolean keepPositive,
            List<CartesianPoint> capPoints
    ) {
        List<CartesianPoint> output = new ArrayList<>();
        for (int index = 0; index < polygon.size(); index++) {
            CartesianPoint current = polygon.get(index);
            CartesianPoint next = polygon.get((index + 1) % polygon.size());
            double currentDistance = signedDistanceForHalfSpace(current, plane, keepPositive);
            double nextDistance = signedDistanceForHalfSpace(next, plane, keepPositive);
            boolean currentInside = currentDistance >= -1.0e-9;
            boolean nextInside = nextDistance >= -1.0e-9;
            if (currentInside && nextInside) {
                addDistinctPoint(output, next);
            } else if (currentInside) {
                CartesianPoint intersection = interpolatePlaneIntersection(current, next, currentDistance, nextDistance);
                addDistinctPoint(output, intersection);
                addDistinctPoint(capPoints, intersection);
            } else if (nextInside) {
                CartesianPoint intersection = interpolatePlaneIntersection(current, next, currentDistance, nextDistance);
                addDistinctPoint(output, intersection);
                addDistinctPoint(output, next);
                addDistinctPoint(capPoints, intersection);
            }
        }
        if (!output.isEmpty() && output.get(0).distanceTo(output.get(output.size() - 1)) <= 1.0e-9) {
            output.remove(output.size() - 1);
        }
        return List.copyOf(output);
    }

    private double signedDistanceForHalfSpace(CartesianPoint point, Plane plane, boolean keepPositive) {
        double distance = plane.signedDistanceTo(point);
        return keepPositive ? distance : -distance;
    }

    private CartesianPoint interpolatePlaneIntersection(
            CartesianPoint start,
            CartesianPoint end,
            double startDistance,
            double endDistance
    ) {
        double t = startDistance / (startDistance - endDistance);
        Vector3 edge = end.subtract(start);
        return start.add(edge.scale(t));
    }

    private void addDistinctPoint(List<CartesianPoint> points, CartesianPoint candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).distanceTo(candidate) > 1.0e-9) {
            points.add(candidate);
        }
    }

    private List<CartesianPoint> buildCapLoop(List<CartesianPoint> capPoints, Plane plane) {
        List<CartesianPoint> unique = uniquePoints(capPoints);
        if (unique.size() < 3) {
            return List.of();
        }
        CartesianPoint centroid = averagePoint(unique);
        Vector3 xAxis = planeBasis(plane.getNormal());
        Vector3 yAxis = plane.getNormal().asVector().cross(xAxis);
        unique.sort((left, right) -> {
            double leftAngle = Math.atan2(left.subtract(centroid).dot(yAxis), left.subtract(centroid).dot(xAxis));
            double rightAngle = Math.atan2(right.subtract(centroid).dot(yAxis), right.subtract(centroid).dot(xAxis));
            return Double.compare(leftAngle, rightAngle);
        });
        return List.copyOf(unique);
    }

    private List<CartesianPoint> uniquePoints(List<CartesianPoint> points) {
        List<CartesianPoint> unique = new ArrayList<>();
        for (CartesianPoint point : points) {
            boolean duplicate = false;
            for (CartesianPoint existing : unique) {
                if (existing.distanceTo(point) <= 1.0e-9) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                unique.add(point);
            }
        }
        return unique;
    }

    private CartesianPoint averagePoint(List<CartesianPoint> points) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (CartesianPoint point : points) {
            x += point.getX();
            y += point.getY();
            z += point.getZ();
        }
        double scale = 1.0 / points.size();
        return new CartesianPoint(x * scale, y * scale, z * scale);
    }

    private Vector3 planeBasis(Direction3 normal) {
        Vector3 reference = Math.abs(normal.getZ()) < 0.9 ? new Vector3(0.0, 0.0, 1.0) : new Vector3(1.0, 0.0, 0.0);
        Vector3 xAxis = normal.asVector().cross(reference);
        if (xAxis.isZero()) {
            xAxis = normal.asVector().cross(new Vector3(0.0, 1.0, 0.0));
        }
        return xAxis.normalize().asVector();
    }

    // Primitive builder helper methods

    private Solid buildEllipsoidLike(
            Axis2Placement3D placement,
            double rx,
            double ry,
            double rz,
            int uSegments,
            int vSegments
    ) {
        List<Face> faces = new ArrayList<>();
        List<List<CartesianPoint>> rings = new ArrayList<>(vSegments - 1);
        for (int vIndex = 1; vIndex < vSegments; vIndex++) {
            double phi = Math.PI * vIndex / vSegments;
            double sinPhi = Math.sin(phi);
            double cosPhi = Math.cos(phi);
            List<CartesianPoint> ring = new ArrayList<>(uSegments);
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                double theta = Math.PI * 2.0 * uIndex / uSegments;
                ring.add(pointOnPlacement(
                        placement,
                        Math.cos(theta) * sinPhi * rx,
                        Math.sin(theta) * sinPhi * ry,
                        cosPhi * rz
                ));
            }
            rings.add(List.copyOf(ring));
        }
        CartesianPoint north = pointOnPlacement(placement, 0.0, 0.0, rz);
        CartesianPoint south = pointOnPlacement(placement, 0.0, 0.0, -rz);
        if (!rings.isEmpty()) {
            List<CartesianPoint> firstRing = rings.get(0);
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                CartesianPoint b = firstRing.get(uIndex);
                CartesianPoint c = firstRing.get((uIndex + 1) % uSegments);
                addTriangleFace(faces, north, b, c, north.subtract(placement.getLocation()));
            }
            for (int ringIndex = 0; ringIndex < rings.size() - 1; ringIndex++) {
                List<CartesianPoint> lower = rings.get(ringIndex);
                List<CartesianPoint> upper = rings.get(ringIndex + 1);
                for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                    CartesianPoint a = lower.get(uIndex);
                    CartesianPoint b = lower.get((uIndex + 1) % uSegments);
                    CartesianPoint c = upper.get((uIndex + 1) % uSegments);
                    CartesianPoint d = upper.get(uIndex);
                    addTriangleFace(faces, a, b, c, outwardApproximation(a, placement.getLocation()));
                    addTriangleFace(faces, a, c, d, outwardApproximation(d, placement.getLocation()));
                }
            }
            List<CartesianPoint> lastRing = rings.get(rings.size() - 1);
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                CartesianPoint a = lastRing.get(uIndex);
                CartesianPoint b = lastRing.get((uIndex + 1) % uSegments);
                addTriangleFace(faces, a, south, b, south.subtract(placement.getLocation()));
            }
        }
        return new Solid(new Shell(faces, true));
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

    private CartesianPoint pointOnPlacement(Axis2Placement3D placement, double x, double y, double z) {
        return placement.getLocation()
                .add(placement.xDirection().asVector().scale(x))
                .add(placement.yDirection().asVector().scale(y))
                .add(placement.getAxis().asVector().scale(z));
    }

    private void addTriangleFace(
            List<Face> faces,
            CartesianPoint a,
            CartesianPoint b,
            CartesianPoint c,
            Vector3 fallback
    ) {
        faces.add(builder.faceFromPolyLoop(
                List.of(a, b, c, a),
                polygonNormal(List.of(a, b, c), fallback)
        ));
    }

    private Vector3 outwardApproximation(CartesianPoint point, CartesianPoint center) {
        Vector3 vector = point.subtract(center);
        return vector.isZero() ? new Vector3(0.0, 0.0, 1.0) : vector;
    }

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

    /**
     * Represents a circular frame (local coordinate system for circles/tubes).
     */
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