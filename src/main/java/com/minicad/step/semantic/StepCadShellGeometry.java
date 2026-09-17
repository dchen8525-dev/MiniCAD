package com.minicad.step.semantic;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared shell construction helpers for the {@code StepCad*} builders.
 *
 * <p>{@link StepCadBooleanBuilder} and {@link StepCadSweptBuilder} each grew a
 * private copy of the same vocabulary for sweeping a profile into a shell: the
 * circular frame, its per-point variant, the ring sampler and the polygon / quad
 * normal. The bodies are lifted verbatim, wording included -- both copies already
 * agreed on the "revolved ..." degenerate messages -- and the delegating entry
 * points in the two builders keep their original signatures, so no call site had
 * to move.</p>
 *
 * <p>{@link StepCadBuilder} carried a third cluster of these (plus the ellipsoid
 * sweep that consumed them) but nothing called into it, so that cluster was
 * dropped rather than converged.</p>
 */
final class StepCadShellGeometry {

    private StepCadShellGeometry() {
    }

    /** Represents a circular frame (local coordinate system for circles/tubes). */
    static final class CircularFrame {
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

    static CircularFrame circularFrame(Direction3 axis) {
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

    static CircularFrame circularFrameAtPoint(CartesianPoint point, Direction3 tangent) {
        return circularFrame(tangent);
    }

    static List<CartesianPoint> sampleCircle3(
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

    static Direction3 polygonNormal(List<CartesianPoint> points, Vector3 fallback) {
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

    static Direction3 quadNormal(CartesianPoint a, CartesianPoint b, CartesianPoint c, CartesianPoint d) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.isZero()) {
            normal = c.subtract(a).cross(d.subtract(a));
        }
        if (normal.isZero()) {
            throw new UnsupportedGeometryException("revolved side face is degenerate");
        }
        return Direction3.from(normal.normalize());
    }
}
