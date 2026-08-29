package com.minicad.step.semantic;

import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepEntity;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression tests that port the unique assertions of the old standalone
 * ReproOptional harness (target/repro/ReproOptional.java) into the JUnit suite.
 *
 * <p>They verify the ISO 10303-42 OPTIONAL-direction defaults added by the
 * GeometryResolver fix (B): an omitted axis/ref-direction is accepted and
 * filled with the spec default instead of rejecting a valid STEP file.
 */
class PlacementDefaultsTest {

    @Test
    void axis2Placement2dOmittingRefDirectionDefaultsToPlusX() {
        String step =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('P0',(1.0,2.0,0.0));\n"
            + "#2=AXIS2_PLACEMENT_2D('A2',#1,$);\n"
            + "ENDSEC;";

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepAxis2Placement2D placement = assertInstanceOf(StepAxis2Placement2D.class, resolved.get(2));
        assertTrue(placement.getRefDirection() != null);
        assertEquals(List.of(1.0, 0.0), placement.getRefDirection().getDirectionRatios());
        // Location must be preserved untouched.
        assertEquals(1.0, placement.getLocation().coordinates().get(0), 1e-12);
        assertEquals(2.0, placement.getLocation().coordinates().get(1), 1e-12);
    }

    @Test
    void matrixForOmittedDirections3dIsIdentityRotationPlusTranslation() {
        String step =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));\n"
            + "#2=AXIS2_PLACEMENT_3D('AX',#1,$,$);\n"
            + "ENDSEC;";

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepAxis2Placement3D placement = assertInstanceOf(StepAxis2Placement3D.class, resolved.get(2));
        assertTrue(placement.getAxis() != null);
        assertTrue(placement.getRefDirection() != null);

        double[] m = StepAssemblyGraphBuilder.matrixForPlacement(placement);
        // Column-major 4x4: rotation columns should be the identity basis and the
        // translation column should carry the origin (1,2,3).
        assertEquals(1.0, m[0], 1e-12);
        assertEquals(1.0, m[5], 1e-12);
        assertEquals(1.0, m[10], 1e-12);
        // Off-diagonal rotation terms ~ 0.
        assertEquals(0.0, m[1], 1e-12);
        assertEquals(0.0, m[2], 1e-12);
        assertEquals(0.0, m[6], 1e-12);
        // Translation = location.
        assertEquals(1.0, m[3], 1e-12);
        assertEquals(2.0, m[7], 1e-12);
        assertEquals(3.0, m[11], 1e-12);
    }
}
