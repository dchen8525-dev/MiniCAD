package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Minimal offset surface wrapper around another supported surface geometry.
 * An offset surface is parallel to the basis surface at a constant distance.
 *
 * @param basisSurface wrapped basis surface
 * @param distance offset distance (positive offsets along normal direction)
 */
public record OffsetSurface3(SurfaceGeometry basisSurface, double distance) implements SurfaceGeometry {

    public OffsetSurface3 {
        Preconditions.requireNonNull(basisSurface, "basisSurface");
        Preconditions.requireFinite(distance, "distance");
    }

    /**
     * Evaluates a point on the offset surface by computing the basis surface point
     * and offsetting along the surface normal.
     *
     * @param u parameter along U direction
     * @param v parameter along V direction
     * @return point on the offset surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint basisPoint = getBasisPointAt(u, v);
        Vector3 normal = getNormalAt(u, v);
        return basisPoint.add(normal.scale(distance));
    }

    /**
     * Gets a point on the basis surface at the given parameters.
     */
    private CartesianPoint getBasisPointAt(double u, double v) {
        if (basisSurface instanceof CylindricalSurface cylinder) {
            return cylinder.pointAt(u, v);
        } else if (basisSurface instanceof SphericalSurface sphere) {
            return sphere.pointAt(u, v);
        } else if (basisSurface instanceof ToroidalSurface torus) {
            return torus.pointAt(u, v);
        } else if (basisSurface instanceof ConicalSurface cone) {
            return cone.pointAt(u, v);
        } else if (basisSurface instanceof BSplineSurface3 bspline) {
            return bspline.pointAt(u, v);
        } else if (basisSurface instanceof RationalBSplineSurface3 rational) {
            return rational.pointAt(u, v);
        } else if (basisSurface instanceof SurfaceOfLinearExtrusion3 extrusion) {
            return extrusion.pointAt(u, v);
        } else if (basisSurface instanceof SurfaceOfRevolution3 revolution) {
            return revolution.pointAt(u, v);
        } else if (basisSurface instanceof RuledSurface3 ruled) {
            return ruled.pointAt(u, v);
        } else if (basisSurface instanceof Plane plane) {
            Vector3 n = plane.normal().asVector();
            Vector3 xDir = n.cross(new Vector3(1, 0, 0)).normalize().asVector();
            if (xDir.norm() < 0.01) xDir = n.cross(new Vector3(0, 1, 0)).normalize().asVector();
            Vector3 yDir = n.cross(xDir).normalize().asVector();
            Vector3 offset = xDir.scale(u).add(yDir.scale(v));
            return plane.origin().add(offset);
        } else if (basisSurface instanceof OffsetSurface3 offset) {
            return offset.pointAt(u, v);
        } else if (basisSurface instanceof SurfaceOfConstantRadius3 constant) {
            return constant.pointAt(u, v);
        } else if (basisSurface instanceof ParaboloidSurface paraboloid) {
            return paraboloid.pointAt(u, v);
        } else if (basisSurface instanceof HyperboloidSurface hyperboloid) {
            return hyperboloid.pointAt(u, v);
        } else if (basisSurface instanceof SurfaceOfTranslation3 translation) {
            return translation.pointAt(u, v);
        } else if (basisSurface instanceof SurfaceOfProjection3 projection) {
            return projection.pointAt(u, v);
        }
        return new CartesianPoint(0, 0, 0);
    }

    /**
     * Computes the normal at the given parameters on the basis surface.
     */
    private Vector3 getNormalAt(double u, double v) {
        if (basisSurface instanceof CylindricalSurface cylinder) {
            return getCylinderNormal(cylinder, u);
        } else if (basisSurface instanceof SphericalSurface sphere) {
            return getSphereNormal(sphere, u, v);
        } else if (basisSurface instanceof ToroidalSurface torus) {
            return getTorusNormal(torus, u, v);
        } else if (basisSurface instanceof ConicalSurface cone) {
            return getConeNormal(cone, u);
        } else if (basisSurface instanceof BSplineSurface3 bspline) {
            return bspline.normalAt(u, v);
        } else if (basisSurface instanceof RationalBSplineSurface3 rational) {
            return rational.normalAt(u, v);
        } else if (basisSurface instanceof Plane plane) {
            return plane.normal().asVector();
        } else if (basisSurface instanceof SurfaceOfLinearExtrusion3 extrusion) {
            return getExtrusionNormal(extrusion, u);
        } else if (basisSurface instanceof SurfaceOfRevolution3 revolution) {
            return getRevolutionNormal(revolution, u, v);
        } else if (basisSurface instanceof RuledSurface3 ruled) {
            return getRuledNormal(ruled, u, v);
        } else if (basisSurface instanceof OffsetSurface3 offset) {
            return offset.normalAt(u, v);
        } else if (basisSurface instanceof SurfaceOfConstantRadius3 constant) {
            return constant.normalAt(u, v);
        } else if (basisSurface instanceof ParaboloidSurface paraboloid) {
            return paraboloid.normalAt(u, v);
        } else if (basisSurface instanceof HyperboloidSurface hyperboloid) {
            return hyperboloid.normalAt(u, v);
        } else if (basisSurface instanceof SurfaceOfTranslation3 translation) {
            return translation.normalAt(u, v);
        } else if (basisSurface instanceof SurfaceOfProjection3 projection) {
            return projection.normalAt(u, v);
        }
        // Default fallback
        return new Vector3(0, 0, 1);
    }

    private static Vector3 getCylinderNormal(CylindricalSurface cylinder, double angle) {
        Vector3 radial = cylinder.position().xDirection().asVector().scale(Math.cos(angle))
                .add(cylinder.position().yDirection().asVector().scale(Math.sin(angle)));
        if (radial.norm() <= Epsilon.EPS) {
            return cylinder.position().xDirection().asVector();
        }
        return radial.normalize().asVector();
    }

    private static Vector3 getSphereNormal(SphericalSurface sphere, double theta, double phi) {
        double x = Math.sin(phi) * Math.cos(theta);
        double y = Math.sin(phi) * Math.sin(theta);
        double z = Math.cos(phi);
        Vector3 localNormal = new Vector3(x, y, z);
        Vector3 worldNormal = sphere.position().xDirection().asVector().scale(localNormal.x())
                .add(sphere.position().yDirection().asVector().scale(localNormal.y()))
                .add(sphere.position().axis().asVector().scale(localNormal.z()));
        if (worldNormal.norm() <= Epsilon.EPS) {
            return sphere.position().axis().asVector();
        }
        return worldNormal.normalize().asVector();
    }

    private static Vector3 getTorusNormal(ToroidalSurface torus, double theta, double phi) {
        // Normal points outward from tube center
        double nx = Math.cos(phi) * Math.cos(theta);
        double ny = Math.cos(phi) * Math.sin(theta);
        double nz = Math.sin(phi);
        Vector3 localNormal = new Vector3(nx, ny, nz);
        Vector3 worldNormal = torus.position().xDirection().asVector().scale(localNormal.x())
                .add(torus.position().yDirection().asVector().scale(localNormal.y()))
                .add(torus.position().axis().asVector().scale(localNormal.z()));
        if (worldNormal.norm() <= Epsilon.EPS) {
            return torus.position().axis().asVector();
        }
        return worldNormal.normalize().asVector();
    }

    private static Vector3 getConeNormal(ConicalSurface cone, double angle) {
        double semiAngle = cone.semiAngle();
        // Normal is perpendicular to cone surface
        double radialX = Math.cos(angle);
        double radialY = Math.sin(angle);
        double axial = -Math.tan(semiAngle);
        Vector3 localNormal = new Vector3(radialX, radialY, axial);
        Vector3 worldNormal = cone.position().xDirection().asVector().scale(localNormal.x())
                .add(cone.position().yDirection().asVector().scale(localNormal.y()))
                .add(cone.position().axis().asVector().scale(localNormal.z()));
        if (worldNormal.norm() <= Epsilon.EPS) {
            return cone.position().axis().asVector();
        }
        return worldNormal.normalize().asVector();
    }

    private static Vector3 getExtrusionNormal(SurfaceOfLinearExtrusion3 extrusion, double curveParam) {
        // For extrusion, normal is perpendicular to swept curve and extrusion direction
        Vector3 extrusionDir = extrusion.extrusionVector().normalize().asVector();
        // Approximate tangent by small parameter change
        double eps = 0.001;
        CartesianPoint p1 = extrusion.pointAt(Math.max(0, curveParam - eps), 0);
        CartesianPoint p2 = extrusion.pointAt(Math.min(1, curveParam + eps), 0);
        Vector3 tangent = p2.subtract(p1);
        if (tangent.norm() <= Epsilon.EPS) {
            return extrusionDir.cross(new Vector3(1, 0, 0)).normalize().asVector();
        }
        Vector3 normal = tangent.cross(extrusionDir);
        if (normal.norm() <= Epsilon.EPS) {
            return extrusionDir.cross(new Vector3(1, 0, 0)).normalize().asVector();
        }
        return normal.normalize().asVector();
    }

    private static Vector3 getRevolutionNormal(SurfaceOfRevolution3 revolution, double curveParam, double angle) {
        // For revolution, compute point and derive normal from radial direction
        CartesianPoint point = revolution.pointAt(curveParam, angle);
        Vector3 rel = point.subtract(revolution.axisOrigin());
        Vector3 axisVec = revolution.axisDirection().asVector();
        double axialComponent = rel.dot(axisVec);
        Vector3 axialPart = axisVec.scale(axialComponent);
        Vector3 radialPart = rel.subtract(axialPart);
        if (radialPart.norm() <= Epsilon.EPS) {
            return axisVec;
        }
        return radialPart.normalize().asVector();
    }

    private static Vector3 getRuledNormal(RuledSurface3 ruled, double u, double v) {
        // Approximate normal using partial derivatives
        double eps = 0.001;
        CartesianPoint pu = ruled.pointAt(Math.max(0, u - eps), v);
        CartesianPoint pu2 = ruled.pointAt(Math.min(1, u + eps), v);
        CartesianPoint pv = ruled.pointAt(u, Math.max(0, v - eps));
        CartesianPoint pv2 = ruled.pointAt(u, Math.min(1, v + eps));
        Vector3 tangentU = pu2.subtract(pu);
        Vector3 tangentV = pv2.subtract(pv);
        if (tangentU.norm() <= Epsilon.EPS || tangentV.norm() <= Epsilon.EPS) {
            return new Vector3(0, 0, 1);
        }
        Vector3 normal = tangentU.cross(tangentV);
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0, 0, 1);
        }
        return normal.normalize().asVector();
    }

    /**
     * Samples the offset surface by computing basis surface points and offsetting along normals.
     *
     * @param uSegments number of segments along U direction
     * @param vSegments number of segments along V direction
     * @return grid of offset points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> basisGrid = getBasisSampleGrid(uSegments, vSegments);
        if (basisGrid.isEmpty()) {
            return java.util.List.of();
        }
        java.util.List<java.util.List<CartesianPoint>> offsetGrid = new java.util.ArrayList<>(basisGrid.size());
        for (int i = 0; i < basisGrid.size(); i++) {
            java.util.List<CartesianPoint> basisRow = basisGrid.get(i);
            java.util.List<CartesianPoint> offsetRow = new java.util.ArrayList<>(basisRow.size());
            for (int j = 0; j < basisRow.size(); j++) {
                double u = (double) i / Math.max(1, basisGrid.size() - 1);
                double v = (double) j / Math.max(1, basisRow.size() - 1);
                CartesianPoint basisPoint = basisRow.get(j);
                Vector3 normal = getNormalAt(u, v);
                offsetRow.add(basisPoint.add(normal.scale(distance)));
            }
            offsetGrid.add(java.util.List.copyOf(offsetRow));
        }
        return java.util.List.copyOf(offsetGrid);
    }

    /**
     * Gets sample grid from basis surface.
     */
    private java.util.List<java.util.List<CartesianPoint>> getBasisSampleGrid(int uSegments, int vSegments) {
        if (basisSurface instanceof CylindricalSurface cylinder) {
            return cylinder.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof SphericalSurface sphere) {
            return sphere.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof ToroidalSurface torus) {
            return torus.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof ConicalSurface cone) {
            return cone.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof BSplineSurface3 bspline) {
            return bspline.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof RationalBSplineSurface3 rational) {
            return rational.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof SurfaceOfLinearExtrusion3 extrusion) {
            return extrusion.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof SurfaceOfRevolution3 revolution) {
            return revolution.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof RuledSurface3 ruled) {
            return ruled.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof Plane plane) {
            return plane.sampleGrid(1.0, 1.0, uSegments, vSegments);
        } else if (basisSurface instanceof OffsetSurface3 offset) {
            return offset.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof SurfaceOfConstantRadius3 constant) {
            return constant.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof ParaboloidSurface paraboloid) {
            return paraboloid.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof HyperboloidSurface hyperboloid) {
            return hyperboloid.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof SurfaceOfTranslation3 translation) {
            return translation.sampleGrid(uSegments, vSegments);
        } else if (basisSurface instanceof SurfaceOfProjection3 projection) {
            return projection.sampleGrid(uSegments, vSegments);
        }
        return java.util.List.of();
    }

    /**
     * Returns the bounding box for the offset surface.
     * The offset surface extends the basis surface bounding box by the offset distance.
     *
     * @return bounding box enclosing the offset surface
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 basisBox = getBasisBoundingBox();
        // Expand by offset distance in all directions
        return basisBox.expand(Math.abs(distance));
    }

    @Override
    public Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        Vector3 basisNormal = getNormalAt(u, v);
        if (basisNormal.norm() <= Epsilon.EPS) {
            return new Vector3(0, 0, 1);
        }
        return basisNormal.normalize().asVector();
    }

    /**
     * Gets bounding box from basis surface.
     */
    private BoundingBox3 getBasisBoundingBox() {
        if (basisSurface instanceof CylindricalSurface cylinder) {
            return cylinder.boundingBox(-1.0, 1.0);
        } else if (basisSurface instanceof SphericalSurface sphere) {
            return sphere.boundingBox();
        } else if (basisSurface instanceof ToroidalSurface torus) {
            return torus.boundingBox();
        } else if (basisSurface instanceof ConicalSurface cone) {
            return cone.boundingBox(-1.0, 1.0);
        } else if (basisSurface instanceof BSplineSurface3 bspline) {
            return bspline.boundingBox();
        } else if (basisSurface instanceof RationalBSplineSurface3 rational) {
            return rational.boundingBox();
        }
        // For other surfaces, sample and compute bounding box
        BoundingBox3 box = BoundingBox3.empty();
        java.util.List<java.util.List<CartesianPoint>> grid = getBasisSampleGrid(32, 32);
        for (java.util.List<CartesianPoint> row : grid) {
            for (CartesianPoint point : row) {
                box = box.union(point);
            }
        }
        return box;
    }

    /**
     * Returns the closest point on the offset surface to a given point.
     * Uses sampling approach for offset surfaces.
     *
     * @param point target point
     * @return approximate closest point on the offset surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        // Sample at multiple resolutions to find closest point
        for (int resolution : new int[]{16, 32, 64}) {
            java.util.List<java.util.List<CartesianPoint>> grid = sampleGrid(resolution, resolution);
            for (java.util.List<CartesianPoint> row : grid) {
                for (CartesianPoint sample : row) {
                    double dist = point.distanceTo(sample);
                    if (dist < minDistance) {
                        minDistance = dist;
                        closest = sample;
                    }
                }
            }
        }
        return closest != null ? closest : pointAt(0, 0);
    }

    /**
     * Returns the shortest distance from a point to the offset surface.
     *
     * @param point target point
     * @return approximate shortest distance to the offset surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }
}