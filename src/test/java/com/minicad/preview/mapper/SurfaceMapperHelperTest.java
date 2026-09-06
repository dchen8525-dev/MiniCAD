package com.minicad.preview.mapper;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import com.minicad.preview.payload.UvPoint;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Runtime behaviour tests for the SURFACE_MAPPER_RULES dispatch in
 * SurfaceMapperHelper.mapperForSurface, exercised through real STEP surfaces
 * resolved into a real StepCadBuilder.
 *
 * Fixture: AXIS2_PLACEMENT_3D #4 at (1,2,3) with axis +Z and reference +X,
 * so the PLANE #5 mapper spans u along +X and v along +Y from the origin.
 * CYLINDRICAL_SURFACE #6 sits on the same placement with radius 3.
 * ORIENTED_SURFACE #7 flips PLANE #5, so its mapper negates the normal.
 */
class SurfaceMapperHelperTest {

    private static final String STEP =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));\n"
            + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
            + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
            + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
            + "#5=PLANE('PL',#4);\n"
            + "#6=CYLINDRICAL_SURFACE('CY',#4,3.0);\n"
            + "#7=ORIENTED_SURFACE('OS',#5,.F.);\n"
            + "ENDSEC;\n";

    private static StepCadBuilder builder;
    private static StepEntity plane;
    private static StepEntity cylinder;
    private static StepOrientedSurface orientedSurface;

    @BeforeAll
    static void setUp() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(STEP));
        builder = StepCadBuilder.fromResolved(resolved);
        plane = resolved.get(5);
        cylinder = resolved.get(6);
        orientedSurface = (StepOrientedSurface) resolved.get(7);
    }

    private static void assertVector(Vector3 vector, double x, double y, double z) {
        assertEquals(x, vector.getX(), 1.0e-9);
        assertEquals(y, vector.getY(), 1.0e-9);
        assertEquals(z, vector.getZ(), 1.0e-9);
    }

    @Test
    @DisplayName("PLANE mapper projects and unprojects through its placement axes")
    void planeMapperRoundTrip() {
        ParametricSurfaceMapper mapper = SurfaceMapperHelper.mapperForSurface(plane, builder);
        assertNotNull(mapper);
        CartesianPoint point = mapper.pointAt(2.0, 3.0);
        assertEquals(3.0, point.getX(), 1.0e-9);
        assertEquals(5.0, point.getY(), 1.0e-9);
        assertEquals(3.0, point.getZ(), 1.0e-9);
        UvPoint uv = mapper.project(point, null);
        assertEquals(2.0, uv.u(), 1.0e-9);
        assertEquals(3.0, uv.v(), 1.0e-9);
        assertVector(mapper.normalAt(0.0, 0.0), 0.0, 0.0, 1.0);
    }

    @Test
    @DisplayName("CYLINDRICAL_SURFACE mapper is u-periodic and places v along the axis")
    void cylindricalMapper() {
        ParametricSurfaceMapper mapper = SurfaceMapperHelper.mapperForSurface(cylinder, builder);
        assertNotNull(mapper);
        assertEquals(Math.PI * 2.0, mapper.uPeriod(), 1.0e-9);
        CartesianPoint point = mapper.pointAt(0.0, 1.0);
        assertEquals(4.0, point.getX(), 1.0e-9);
        assertEquals(2.0, point.getY(), 1.0e-9);
        assertEquals(4.0, point.getZ(), 1.0e-9);
        Vector3 normal = mapper.normalAt(0.0, 1.0);
        assertEquals(1.0, normal.norm(), 1.0e-9);
    }

    @Test
    @DisplayName("ORIENTED_SURFACE with orientation .F. negates the base mapper normal")
    void orientedSurfaceNegatesNormal() {
        ParametricSurfaceMapper base = SurfaceMapperHelper.mapperForSurface(orientedSurface.surfaceElement(), builder);
        ParametricSurfaceMapper mapper = SurfaceMapperHelper.mapperForSurface(orientedSurface, builder);
        assertNotNull(mapper);
        Vector3 baseNormal = base.normalAt(0.5, 0.5);
        Vector3 normal = mapper.normalAt(0.5, 0.5);
        assertVector(normal, -baseNormal.getX(), -baseNormal.getY(), -baseNormal.getZ());
        assertEquals(base.pointAt(0.5, 0.5).getX(), mapper.pointAt(0.5, 0.5).getX(), 1.0e-9);
    }

    @Test
    @DisplayName("a surface type with no rule maps to null")
    void unknownSurfaceMapsToNull() {
        StepEntity unknown = new StepEntity() {
            @Override
            public int getId() {
                return -1;
            }

            @Override
            public String getName() {
                return "";
            }
        };
        assertNull(SurfaceMapperHelper.mapperForSurface(unknown, builder));
    }
}
