package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Minimal surface of constant radius representation.
 * A surface swept by maintaining a constant radius along a path.
 * This can represent pipe-like surfaces with constant cross-section radius.
 *
 * @param sweptSurface the base surface being swept
 * @param radius the constant radius
 */
public record SurfaceOfConstantRadius3(SurfaceGeometry sweptSurface, double radius) implements SurfaceGeometry {

    public SurfaceOfConstantRadius3 {
        Preconditions.requireNonNull(sweptSurface, "sweptSurface");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= 0.0) {
            throw new IllegalArgumentException("radius must be positive");
        }
    }

    /**
     * Evaluates a point on the surface of constant radius.
     * The point is computed by offsetting from the swept surface along its normal.
     *
     * @param u parameter along U direction
     * @param v parameter along V direction
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint basisPoint = getBasisPointAt(u, v);
        Vector3 normal = getNormalAt(u, v);
        return basisPoint.add(normal.scale(radius));
    }

    /**
     * Gets a point on the swept surface at the given parameters.
     */
    private CartesianPoint getBasisPointAt(double u, double v) {
        if (sweptSurface instanceof CylindricalSurface cylinder) {
            return cylinder.pointAt(u, v);
        } else if (sweptSurface instanceof SphericalSurface sphere) {
            return sphere.pointAt(u, v);
        } else if (sweptSurface instanceof ToroidalSurface torus) {
            return torus.pointAt(u, v);
        } else if (sweptSurface instanceof ConicalSurface cone) {
            return cone.pointAt(u, v);
        } else if (sweptSurface instanceof BSplineSurface3 bspline) {
            return bspline.pointAt(u, v);
        } else if (sweptSurface instanceof RationalBSplineSurface3 rational) {
            return rational.pointAt(u, v);
        } else if (sweptSurface instanceof SurfaceOfLinearExtrusion3 extrusion) {
            return extrusion.pointAt(u, v);
        } else if (sweptSurface instanceof SurfaceOfRevolution3 revolution) {
            return revolution.pointAt(u, v);
        } else if (sweptSurface instanceof RuledSurface3 ruled) {
            return ruled.pointAt(u, v);
        } else if (sweptSurface instanceof OffsetSurface3 offset) {
            return offset.pointAt(u, v);
        } else if (sweptSurface instanceof SurfaceOfConstantRadius3 constant) {
            return constant.pointAt(u, v);
        }
        return new CartesianPoint(0, 0, 0);
    }

    /**
     * Computes the normal at the given parameters on the swept surface.
     */
    private Vector3 getNormalAt(double u, double v) {
        if (sweptSurface instanceof CylindricalSurface cylinder) {
            return getCylinderNormal(cylinder, u);
        } else if (sweptSurface instanceof SphericalSurface sphere) {
            return getSphereNormal(sphere, u, v);
        } else if (sweptSurface instanceof ToroidalSurface torus) {
            return getTorusNormal(torus, u, v);
        } else if (sweptSurface instanceof ConicalSurface cone) {
            return getConeNormal(cone, u);
        } else if (sweptSurface instanceof BSplineSurface3 bspline) {
            return bspline.normalAt(u, v);
        } else if (sweptSurface instanceof RationalBSplineSurface3 rational) {
            return rational.normalAt(u, v);
        } else if (sweptSurface instanceof Plane plane) {
            return plane.normal().asVector();
        } else if (sweptSurface instanceof SurfaceOfLinearExtrusion3 extrusion) {
            return getExtrusionNormal(extrusion, u);
        } else if (sweptSurface instanceof SurfaceOfRevolution3 revolution) {
            return getRevolutionNormal(revolution, u, v);
        } else if (sweptSurface instanceof RuledSurface3 ruled) {
            return getRuledNormal(ruled, u, v);
        }
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
        Vector3 extrusionDir = extrusion.extrusionVector().normalize().asVector();
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
     * Samples the surface by computing swept surface points and offsetting along normals.
     *
     * @param uSegments number of segments along U direction
     * @param vSegments number of segments along V direction
     * @return grid of sampled points
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
                offsetRow.add(basisPoint.add(normal.scale(radius)));
            }
            offsetGrid.add(java.util.List.copyOf(offsetRow));
        }
        return java.util.List.copyOf(offsetGrid);
    }

    /**
     * Gets sample grid from swept surface.
     */
    private java.util.List<java.util.List<CartesianPoint>> getBasisSampleGrid(int uSegments, int vSegments) {
        if (sweptSurface instanceof CylindricalSurface cylinder) {
            return cylinder.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof SphericalSurface sphere) {
            return sphere.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof ToroidalSurface torus) {
            return torus.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof ConicalSurface cone) {
            return cone.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof BSplineSurface3 bspline) {
            return bspline.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof RationalBSplineSurface3 rational) {
            return rational.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof SurfaceOfLinearExtrusion3 extrusion) {
            return extrusion.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof SurfaceOfRevolution3 revolution) {
            return revolution.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof RuledSurface3 ruled) {
            return ruled.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof Plane plane) {
            return plane.sampleGrid(1.0, 1.0, uSegments, vSegments);
        } else if (sweptSurface instanceof OffsetSurface3 offset) {
            return offset.sampleGrid(uSegments, vSegments);
        } else if (sweptSurface instanceof SurfaceOfConstantRadius3 constant) {
            return constant.sampleGrid(uSegments, vSegments);
        }
        return java.util.List.of();
    }

    /**
     * Returns the bounding box for the surface of constant radius.
     * The surface extends the swept surface bounding box by the radius.
     *
     * @return bounding box enclosing the surface
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 basisBox = getBasisBoundingBox();
        // Expand by radius in all directions
        return basisBox.expand(radius);
    }

    /**
     * Gets bounding box from swept surface.
     */
    private BoundingBox3 getBasisBoundingBox() {
        if (sweptSurface instanceof CylindricalSurface cylinder) {
            return cylinder.boundingBox(-1.0, 1.0);
        } else if (sweptSurface instanceof SphericalSurface sphere) {
            return sphere.boundingBox();
        } else if (sweptSurface instanceof ToroidalSurface torus) {
            return torus.boundingBox();
        } else if (sweptSurface instanceof ConicalSurface cone) {
            return cone.boundingBox(-1.0, 1.0);
        } else if (sweptSurface instanceof BSplineSurface3 bspline) {
            return bspline.boundingBox();
        } else if (sweptSurface instanceof RationalBSplineSurface3 rational) {
            return rational.boundingBox();
        } else if (sweptSurface instanceof SurfaceOfLinearExtrusion3 extrusion) {
            return extrusion.boundingBox();
        } else if (sweptSurface instanceof SurfaceOfRevolution3 revolution) {
            return revolution.boundingBox();
        } else if (sweptSurface instanceof RuledSurface3 ruled) {
            return ruled.boundingBox();
        } else if (sweptSurface instanceof OffsetSurface3 offset) {
            return offset.boundingBox();
        }
        // For other surfaces, sample and compute
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
     * Returns the closest point on the surface to a given point.
     * Uses sampling approach for surfaces of constant radius.
     *
     * @param point target point
     * @return approximate closest point on the surface
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
     * Returns the shortest distance from a point to the surface.
     *
     * @param point target point
     * @return approximate shortest distance to the surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }
}