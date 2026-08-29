package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.PolyLoop;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for tessellating geometric primitives (spheres, tori, boxes).
 * Provides methods to generate triangulated Face lists for basic shapes.
 *
 * <p>Extracted from {@link StepCadBuilder} to improve code organization and maintainability.</p>
 *
 * @since 1.0
 */
public class StepPrimitiveTessellator {

    /**
     * Tessellates a sphere centered at origin with given radius.
     * Generates a mesh of quadrilateral and triangular faces.
     *
     * @param radius sphere radius
     * @param latSteps latitude divisions (vertical)
     * @param lonSteps longitude divisions (horizontal)
     * @return list of faces approximating the sphere surface
     */
    public static List<Face> tessellateSphere(double radius, int latSteps, int lonSteps) {
        List<Face> faces = new ArrayList<>();
        for (int i = 0; i < latSteps; i++) {
            double phi1 = Math.PI * i / latSteps;
            double phi2 = Math.PI * (i + 1) / latSteps;
            for (int j = 0; j < lonSteps; j++) {
                double theta1 = 2 * Math.PI * j / lonSteps;
                double theta2 = 2 * Math.PI * (j + 1) / lonSteps;
                CartesianPoint p00 = spherePoint(radius, phi1, theta1);
                CartesianPoint p10 = spherePoint(radius, phi2, theta1);
                CartesianPoint p11 = spherePoint(radius, phi2, theta2);
                CartesianPoint p01 = spherePoint(radius, phi1, theta2);
                CartesianPoint midPoint = spherePoint(radius, (phi1 + phi2) / 2, (theta1 + theta2) / 2);
                Vector3 normal = new Vector3(midPoint.getX(), midPoint.getY(), midPoint.getZ());
                if (i == 0) {
                    faces.add(faceFromPolyLoop(List.of(p00, p10, p11), Direction3.from(normal)));
                } else if (i == latSteps - 1) {
                    faces.add(faceFromPolyLoop(List.of(p00, p01, p10), Direction3.from(normal)));
                } else {
                    faces.add(faceFromPolyLoop(List.of(p00, p10, p11, p01), Direction3.from(normal)));
                }
            }
        }
        return faces;
    }

    /**
     * Tessellates a torus centered at origin.
     *
     * @param majorR major radius (center of tube to center of torus)
     * @param minorR minor radius (radius of tube)
     * @param majorSteps divisions around the major circumference
     * @param minorSteps divisions around the minor circumference
     * @return list of quadrilateral faces approximating the torus surface
     */
    public static List<Face> tessellateTorus(double majorR, double minorR, int majorSteps, int minorSteps) {
        List<Face> faces = new ArrayList<>();
        double[][][] points = new double[majorSteps + 1][minorSteps + 1][];
        for (int i = 0; i <= majorSteps; i++) {
            double theta = 2 * Math.PI * i / majorSteps;
            for (int j = 0; j <= minorSteps; j++) {
                double phi = 2 * Math.PI * j / minorSteps;
                points[i][j] = new double[]{
                        (majorR + minorR * Math.cos(phi)) * Math.cos(theta),
                        (majorR + minorR * Math.cos(phi)) * Math.sin(theta),
                        minorR * Math.sin(phi)
                };
            }
        }
        for (int i = 0; i < majorSteps; i++) {
            for (int j = 0; j < minorSteps; j++) {
                CartesianPoint p00 = pt(points[i][j]);
                CartesianPoint p10 = pt(points[i + 1][j]);
                CartesianPoint p11 = pt(points[i + 1][j + 1]);
                CartesianPoint p01 = pt(points[i][j + 1]);
                faces.add(faceFromPolyLoop(List.of(p00, p10, p11, p01), Direction3.from(new Vector3(0, 0, 1))));
            }
        }
        return faces;
    }

    /**
     * Tessellates a sphere at a specific center location.
     *
     * @param center sphere center point
     * @param radius sphere radius
     * @param latSteps latitude divisions
     * @param lonSteps longitude divisions
     * @return list of faces approximating the sphere surface
     */
    public static List<Face> tessellateSphereAt(CartesianPoint center, double radius, int latSteps, int lonSteps) {
        List<Face> faces = new ArrayList<>();
        for (int i = 0; i < latSteps; i++) {
            double phi1 = Math.PI * i / latSteps;
            double phi2 = Math.PI * (i + 1) / latSteps;
            for (int j = 0; j < lonSteps; j++) {
                double theta1 = 2 * Math.PI * j / lonSteps;
                double theta2 = 2 * Math.PI * (j + 1) / lonSteps;
                CartesianPoint p00 = spherePointAt(center, radius, phi1, theta1);
                CartesianPoint p10 = spherePointAt(center, radius, phi2, theta1);
                CartesianPoint p11 = spherePointAt(center, radius, phi2, theta2);
                CartesianPoint p01 = spherePointAt(center, radius, phi1, theta2);
                CartesianPoint midPoint = spherePointAt(center, radius, (phi1 + phi2) / 2, (theta1 + theta2) / 2);
                Vector3 normal = new Vector3(midPoint.getX() - center.getX(), midPoint.getY() - center.getY(), midPoint.getZ() - center.getZ());
                if (i == 0) {
                    faces.add(faceFromPolyLoop(List.of(p00, p10, p11), Direction3.from(normal)));
                } else if (i == latSteps - 1) {
                    faces.add(faceFromPolyLoop(List.of(p00, p01, p10), Direction3.from(normal)));
                } else {
                    faces.add(faceFromPolyLoop(List.of(p00, p10, p11, p01), Direction3.from(normal)));
                }
            }
        }
        return faces;
    }

    /**
     * Tessellates a torus at a specific placement.
     *
     * @param placement placement defining position and orientation
     * @param majorR major radius
     * @param minorR minor radius
     * @param majorSteps divisions around major circumference
     * @param minorSteps divisions around minor circumference
     * @return list of faces approximating the torus surface
     */
    public static List<Face> tessellateTorusAt(Axis2Placement3D placement, double majorR, double minorR, int majorSteps, int minorSteps) {
        List<Face> faces = new ArrayList<>();
        double[][][] points = new double[majorSteps + 1][minorSteps + 1][];
        for (int i = 0; i <= majorSteps; i++) {
            double theta = 2 * Math.PI * i / majorSteps;
            for (int j = 0; j <= minorSteps; j++) {
                double phi = 2 * Math.PI * j / minorSteps;
                points[i][j] = new double[]{
                        (majorR + minorR * Math.cos(phi)) * Math.cos(theta),
                        (majorR + minorR * Math.cos(phi)) * Math.sin(theta),
                        minorR * Math.sin(phi)
                };
            }
        }
        for (int i = 0; i < majorSteps; i++) {
            for (int j = 0; j < minorSteps; j++) {
                CartesianPoint p00 = placement.transformToWorld(pt(points[i][j]));
                CartesianPoint p10 = placement.transformToWorld(pt(points[i + 1][j]));
                CartesianPoint p11 = placement.transformToWorld(pt(points[i + 1][j + 1]));
                CartesianPoint p01 = placement.transformToWorld(pt(points[i][j + 1]));
                CartesianPoint mid = placement.transformToWorld(new CartesianPoint(
                        (points[i][j][0] + points[i + 1][j][0] + points[i + 1][j + 1][0] + points[i][j + 1][0]) / 4,
                        (points[i][j][1] + points[i + 1][j][1] + points[i + 1][j + 1][1] + points[i][j + 1][1]) / 4,
                        (points[i][j][2] + points[i + 1][j][2] + points[i + 1][j + 1][2] + points[i][j + 1][2]) / 4));
                Vector3 normal = new Vector3(mid.getX() - placement.getLocation().getX(), mid.getY() - placement.getLocation().getY(), mid.getZ() - placement.getLocation().getZ());
                faces.add(faceFromPolyLoop(List.of(p00, p10, p11, p01), Direction3.from(normal)));
            }
        }
        return faces;
    }

    /**
     * Builds the six faces of a rectangular box from bottom and top point lists.
     *
     * @param bottom four corner points of bottom face (counterclockwise when viewed from top)
     * @param top four corner points of top face (counterclockwise when viewed from top)
     * @return six faces forming a closed box
     */
    public static List<Face> buildBoxFaces(List<CartesianPoint> bottom, List<CartesianPoint> top) {
        List<Face> faces = new ArrayList<>();
        Direction3 up = Direction3.from(new Vector3(0, 0, 1));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), up.reverse()));
        faces.add(faceFromPolyLoop(top, up));
        Direction3 right = Direction3.from(new Vector3(1, 0, 0));
        Direction3 forward = Direction3.from(new Vector3(0, 1, 0));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), top.get(0), top.get(3), bottom.get(3))), right));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(1), bottom.get(2), top.get(2), top.get(1))), forward));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(2), bottom.get(3), top.get(3), top.get(2))), right.reverse()));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), bottom.get(1), top.get(1), top.get(0))), forward.reverse()));
        return faces;
    }

    /**
     * Creates a face from a polygonal loop of points with given normal direction.
     *
     * @param points vertices of the polygon
     * @param normal normal direction of the face
     * @return a planar face with the polygon as outer boundary
     */
    public static Face faceFromPolyLoop(List<CartesianPoint> points, Direction3 normal) {
        Plane plane = new Plane(points.get(0), normal);
        return new Face(plane, List.of(FaceBound.outer(new PolyLoop(points), true)), true);
    }

    // ===== Helper methods =====

    private static CartesianPoint spherePoint(double radius, double phi, double theta) {
        return new CartesianPoint(
                radius * Math.sin(phi) * Math.cos(theta),
                radius * Math.sin(phi) * Math.sin(theta),
                radius * Math.cos(phi));
    }

    private static CartesianPoint spherePointAt(CartesianPoint center, double radius, double phi, double theta) {
        return new CartesianPoint(
                center.getX() + radius * Math.sin(phi) * Math.cos(theta),
                center.getY() + radius * Math.sin(phi) * Math.sin(theta),
                center.getZ() + radius * Math.cos(phi));
    }

    private static CartesianPoint pt(double[] coords) {
        return new CartesianPoint(coords[0], coords[1], coords[2]);
    }

    /**
     * Reverses a closed loop of 3 points (used for face orientation).
     */
    private static List<CartesianPoint> reverseClosedLoop3(List<CartesianPoint> points) {
        // For a closed loop of 3+ points, reverse order
        List<CartesianPoint> reversed = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            reversed.add(points.get(i));
        }
        return reversed;
    }

    /**
     * Rotates a point around an axis using Rodrigues' rotation formula.
     *
     * @param p point to rotate
     * @param origin origin point on the rotation axis
     * @param axis rotation axis direction
     * @param angle rotation angle in radians
     * @return rotated point
     */
    public static CartesianPoint rotatePointAroundAxis(CartesianPoint p, CartesianPoint origin,
                                                        Vector3 axis, double angle) {
        // Rodrigues' rotation formula
        double dx = p.getX() - origin.getX();
        double dy = p.getY() - origin.getY();
        double dz = p.getZ() - origin.getZ();
        Vector3 v = new Vector3(dx, dy, dz);
        Vector3 k = axis.normalize().asVector();

        // v_rot = v*cos(θ) + (k×v)*sin(θ) + k*(k·v)*(1-cos(θ))
        Vector3 kCrossV = k.cross(v);
        double kDotV = k.dot(v);
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        Vector3 rotated = v.scale(cosAngle)
                .add(kCrossV.scale(sinAngle))
                .add(k.scale(kDotV * (1 - cosAngle)));

        return new CartesianPoint(
                origin.getX() + rotated.getX(),
                origin.getY() + rotated.getY(),
                origin.getZ() + rotated.getZ());
    }
}
