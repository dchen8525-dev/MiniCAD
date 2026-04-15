package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Axis2Placement3DTest {

    @Test
    void shouldBuildOrthogonalDirections() {
        Axis2Placement3D placement = new Axis2Placement3D(
                new CartesianPoint(0.0, 0.0, 0.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                Direction3.from(new Vector3(1.0, 1.0, 0.0))
        );

        assertEquals(0.0, placement.xDirection().dot(placement.axis()), 1.0e-12);
        assertEquals(0.0, placement.yDirection().dot(placement.axis()), 1.0e-12);
    }

    @Test
    void shouldRejectParallelAxisAndReferenceDirection() {
        GeometryException exception = assertThrows(
                GeometryException.class,
                () -> new Axis2Placement3D(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                        Direction3.from(new Vector3(0.0, 0.0, -1.0))
                )
        );

        assertEquals("axis and refDirection must not be parallel", exception.getMessage());
    }

    @Test
    void originPlacement() {
        Axis2Placement3D origin = Axis2Placement3D.origin();
        assertEquals(new CartesianPoint(0, 0, 0), origin.location());
        assertEquals(new Direction3(0, 0, 1), origin.axis());
        assertEquals(new Direction3(1, 0, 0), origin.refDirection());
    }

    @Test
    void atOrigin() {
        CartesianPoint point = new CartesianPoint(10, 20, 30);
        Axis2Placement3D placement = Axis2Placement3D.at(point);
        assertEquals(point, placement.location());
        assertEquals(new Direction3(0, 0, 1), placement.axis());
        assertEquals(new Direction3(1, 0, 0), placement.refDirection());
    }

    @Test
    void transformToWorldIdentity() {
        Axis2Placement3D origin = Axis2Placement3D.origin();
        CartesianPoint localPoint = new CartesianPoint(5, 10, 15);
        CartesianPoint worldPoint = origin.transformToWorld(localPoint);
        assertEquals(5.0, worldPoint.x(), 0.001);
        assertEquals(10.0, worldPoint.y(), 0.001);
        assertEquals(15.0, worldPoint.z(), 0.001);
    }

    @Test
    void transformToWorldTranslated() {
        Axis2Placement3D placement = Axis2Placement3D.at(new CartesianPoint(10, 20, 30));
        CartesianPoint localPoint = new CartesianPoint(5, 10, 15);
        CartesianPoint worldPoint = placement.transformToWorld(localPoint);
        assertEquals(15.0, worldPoint.x(), 0.001);
        assertEquals(30.0, worldPoint.y(), 0.001);
        assertEquals(45.0, worldPoint.z(), 0.001);
    }

    @Test
    void transformToLocalTranslated() {
        Axis2Placement3D placement = Axis2Placement3D.at(new CartesianPoint(10, 20, 30));
        CartesianPoint worldPoint = new CartesianPoint(15, 30, 45);
        CartesianPoint localPoint = placement.transformToLocal(worldPoint);
        assertEquals(5.0, localPoint.x(), 0.001);
        assertEquals(10.0, localPoint.y(), 0.001);
        assertEquals(15.0, localPoint.z(), 0.001);
    }

    @Test
    void roundTripTransformation() {
        Axis2Placement3D placement = new Axis2Placement3D(
            new CartesianPoint(10, 20, 30),
            new Direction3(0, 1, 0),
            new Direction3(1, 0, 0)
        );
        CartesianPoint original = new CartesianPoint(5, 10, 15);
        CartesianPoint world = placement.transformToWorld(original);
        CartesianPoint back = placement.transformToLocal(world);
        assertEquals(original.x(), back.x(), 0.1);
        assertEquals(original.y(), back.y(), 0.1);
        assertEquals(original.z(), back.z(), 0.1);
    }
}
