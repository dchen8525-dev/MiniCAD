package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Transformation3 class.
 */
class Transformation3Test {

    @Test
    void identityTransformation() {
        Transformation3 identity = Transformation3.identity();
        CartesianPoint point = new CartesianPoint(5, 10, 15);
        CartesianPoint transformed = identity.transform(point);
        assertEquals(point.x(), transformed.x(), 0.001);
        assertEquals(point.y(), transformed.y(), 0.001);
        assertEquals(point.z(), transformed.z(), 0.001);
    }

    @Test
    void translationTransformation() {
        Transformation3 translation = Transformation3.translation(10, 20, 30);
        CartesianPoint point = new CartesianPoint(5, 10, 15);
        CartesianPoint transformed = translation.transform(point);
        assertEquals(15.0, transformed.x(), 0.001);
        assertEquals(30.0, transformed.y(), 0.001);
        assertEquals(45.0, transformed.z(), 0.001);
    }

    @Test
    void scaleTransformation() {
        Transformation3 scale = Transformation3.scale(2, 3, 4);
        CartesianPoint point = new CartesianPoint(5, 10, 15);
        CartesianPoint transformed = scale.transform(point);
        assertEquals(10.0, transformed.x(), 0.001);
        assertEquals(30.0, transformed.y(), 0.001);
        assertEquals(60.0, transformed.z(), 0.001);
    }

    @Test
    void uniformScaleTransformation() {
        Transformation3 scale = Transformation3.scale(2);
        CartesianPoint point = new CartesianPoint(5, 10, 15);
        CartesianPoint transformed = scale.transform(point);
        assertEquals(10.0, transformed.x(), 0.001);
        assertEquals(20.0, transformed.y(), 0.001);
        assertEquals(30.0, transformed.z(), 0.001);
    }

    @Test
    void rotationZTransformation() {
        Transformation3 rotation = Transformation3.rotationZ(Math.PI / 2);
        CartesianPoint point = new CartesianPoint(1, 0, 0);
        CartesianPoint transformed = rotation.transform(point);
        assertEquals(0.0, transformed.x(), 0.001);
        assertEquals(1.0, transformed.y(), 0.001);
        assertEquals(0.0, transformed.z(), 0.001);
    }

    @Test
    void rotationXTransformation() {
        Transformation3 rotation = Transformation3.rotationX(Math.PI / 2);
        CartesianPoint point = new CartesianPoint(0, 1, 0);
        CartesianPoint transformed = rotation.transform(point);
        assertEquals(0.0, transformed.x(), 0.001);
        assertEquals(0.0, transformed.y(), 0.001);
        assertEquals(1.0, transformed.z(), 0.001);
    }

    @Test
    void rotationYTransformation() {
        Transformation3 rotation = Transformation3.rotationY(Math.PI / 2);
        CartesianPoint point = new CartesianPoint(1, 0, 0);
        CartesianPoint transformed = rotation.transform(point);
        assertEquals(0.0, transformed.x(), 0.001);
        assertEquals(0.0, transformed.y(), 0.001);
        assertEquals(-1.0, transformed.z(), 0.001);
    }

    @Test
    void composeTransformations() {
        Transformation3 t1 = Transformation3.translation(10, 0, 0);
        Transformation3 t2 = Transformation3.translation(0, 20, 0);
        Transformation3 composed = t1.compose(t2);
        CartesianPoint point = new CartesianPoint(0, 0, 0);
        CartesianPoint transformed = composed.transform(point);
        assertEquals(10.0, transformed.x(), 0.001);
        assertEquals(20.0, transformed.y(), 0.001);
        assertEquals(0.0, transformed.z(), 0.001);
    }

    @Test
    void composeScaleAndTranslation() {
        Transformation3 scale = Transformation3.scale(2);
        Transformation3 translation = Transformation3.translation(10, 0, 0);
        Transformation3 composed = scale.compose(translation);
        // scale.compose(translation) applies translation first, then scale
        // For point (5, 0, 0): translate to (15, 0, 0), then scale to (30, 0, 0)
        CartesianPoint point = new CartesianPoint(5, 0, 0);
        CartesianPoint transformed = composed.transform(point);
        assertEquals(30.0, transformed.x(), 0.001);
    }

    @Test
    void composeTranslationAndScale() {
        Transformation3 translation = Transformation3.translation(10, 0, 0);
        Transformation3 scale = Transformation3.scale(2);
        Transformation3 composed = translation.compose(scale);
        // translation.compose(scale) applies scale first, then translate
        // For point (5, 0, 0): scale to (10, 0, 0), then translate to (20, 0, 0)
        CartesianPoint point = new CartesianPoint(5, 0, 0);
        CartesianPoint transformed = composed.transform(point);
        assertEquals(20.0, transformed.x(), 0.001);
    }

    @Test
    void inverseTranslation() {
        Transformation3 translation = Transformation3.translation(10, 20, 30);
        Transformation3 inverse = translation.inverse();
        CartesianPoint point = new CartesianPoint(15, 30, 45);
        CartesianPoint back = inverse.transform(point);
        assertEquals(5.0, back.x(), 0.001);
        assertEquals(10.0, back.y(), 0.001);
        assertEquals(15.0, back.z(), 0.001);
    }

    @Test
    void inverseRotation() {
        Transformation3 rotation = Transformation3.rotationZ(Math.PI / 4);
        Transformation3 inverse = rotation.inverse();
        CartesianPoint point = new CartesianPoint(1, 1, 0);
        CartesianPoint rotated = rotation.transform(point);
        CartesianPoint back = inverse.transform(rotated);
        assertEquals(point.x(), back.x(), 0.001);
        assertEquals(point.y(), back.y(), 0.001);
        assertEquals(point.z(), back.z(), 0.001);
    }

    @Test
    void fromAxis2Placement3D() {
        Axis2Placement3D placement = Axis2Placement3D.at(new CartesianPoint(10, 20, 30));
        Transformation3 transformation = Transformation3.from(placement);
        CartesianPoint point = new CartesianPoint(5, 10, 15);
        CartesianPoint transformed = transformation.transform(point);
        assertEquals(15.0, transformed.x(), 0.001);
        assertEquals(30.0, transformed.y(), 0.001);
        assertEquals(45.0, transformed.z(), 0.001);
    }

    @Test
    void transformVector() {
        Transformation3 scale = Transformation3.scale(2);
        Vector3 vector = new Vector3(5, 10, 15);
        Vector3 transformed = scale.transform(vector);
        assertEquals(10.0, transformed.x(), 0.001);
        assertEquals(20.0, transformed.y(), 0.001);
        assertEquals(30.0, transformed.z(), 0.001);
    }

    @Test
    void transformDirection() {
        Transformation3 rotation = Transformation3.rotationZ(Math.PI / 2);
        Direction3 direction = new Direction3(1, 0, 0);
        Direction3 transformed = rotation.transform(direction);
        assertEquals(0.0, transformed.x(), 0.001);
        assertEquals(1.0, transformed.y(), 0.001);
        assertEquals(0.0, transformed.z(), 0.001);
    }

    @Test
    void translationComponent() {
        Transformation3 translation = Transformation3.translation(10, 20, 30);
        Vector3 t = translation.translation();
        assertEquals(10.0, t.x(), 0.001);
        assertEquals(20.0, t.y(), 0.001);
        assertEquals(30.0, t.z(), 0.001);
    }
}