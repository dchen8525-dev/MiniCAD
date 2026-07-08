package com.minicad.geometry;

import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.builder.StepAssemblyGraphBuilder.AssemblyGraph;
import com.minicad.builder.StepAssemblyGraphBuilder.AssemblyNode;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for F04: Unit transform interaction.
 * Assembly transforms must apply after unit conversion consistently.
 */
class CategoryFAssemblyTransformTest {

    private static final String L = System.lineSeparator();

    private static String step(String... lines) {
        return String.join(L, lines);
    }

    // ===== F04: Unit transform interaction =====

    @Test
    void assemblyTransformWithMillimeterUnits() {
        // STEP file with millimeter units (most common CAD unit)
        // Transform should scale translation by 0.001 (mm to meters)
        String s = step(
            "DATA;",
            "#1=APPLICATION_CONTEXT('mechanical design');",
            "#2=PRODUCT_CONTEXT('','mechanical',#1);",
            "#3=PRODUCT('ASM','Assembly','',(#2));",
            "#4=PRODUCT('PART','Part','',(#2));",
            "#5=PRODUCT_DEFINITION_FORMATION('v1','',#3);",
            "#6=PRODUCT_DEFINITION_FORMATION('v2','',#4);",
            "#7=PRODUCT_DEFINITION_CONTEXT('d','r',#1);",
            "#8=PRODUCT_DEFINITION('d1','',#5,#7);",
            "#9=PRODUCT_DEFINITION('d2','',#6,#7);",
            "#10=PRODUCT_DEFINITION_SHAPE('','',#8);",
            "#11=PRODUCT_DEFINITION_SHAPE('','',#9);",
            "#12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) GLOBAL_UNIT_ASSIGNED_CONTEXT((#13,#14,#15)) REPRESENTATION_CONTEXT('ID','MODEL'));",
            "#13=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));",
            "#14=(NAMED_UNIT(*) SI_UNIT($,.RADIAN.));",
            "#15=(NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));",
            "#16=CARTESIAN_POINT('O',(0.0,0.0,0.0));",
            "#17=CARTESIAN_POINT('T',(1000.0,2000.0,500.0));",  // mm values
            "#18=DIRECTION('Z',(0.0,0.0,1.0));",
            "#19=DIRECTION('X',(1.0,0.0,0.0));",
            "#20=AXIS2_PLACEMENT_3D('A0',#16,#18,#19);",
            "#21=AXIS2_PLACEMENT_3D('A1',#17,#18,#19);",
            "#22=ITEM_DEFINED_TRANSFORMATION('t','',#20,#21);",
            "#23=SHAPE_REPRESENTATION('ASM_REP',(),#12);",
            "#24=SHAPE_REPRESENTATION('PART_REP',(),#12);",
            "#25=SHAPE_DEFINITION_REPRESENTATION(#10,#23);",
            "#26=SHAPE_DEFINITION_REPRESENTATION(#11,#24);",
            "#27=(REPRESENTATION_RELATIONSHIP('r','',#23,#24) REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));",
            "#28=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ','', '',#8,#9);",
            "#29=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#27,#28);",
            "ENDSEC;"
        );

        // Parse and resolve
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(s));

        // Extract unit scale from metadata (this is what we expect: mm -> m = 0.001)
        double scaleToMeters = 0.001;

        // Build assembly graph with unit scale
        AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved, scaleToMeters);

        assertEquals(2, graph.nodes().size());

        AssemblyNode part = graph.nodes().stream()
                .filter(n -> n.occurrenceId() != null)
                .findFirst().orElseThrow();

        double[] m = part.worldMatrix();

        // Translation should be scaled from mm to meters
        // 1000mm = 1m, 2000mm = 2m, 500mm = 0.5m
        assertEquals(1.0, m[3], 1.0e-9);   // 1000mm -> 1m
        assertEquals(2.0, m[7], 1.0e-9);   // 2000mm -> 2m
        assertEquals(0.5, m[11], 1.0e-9);  // 500mm -> 0.5m

        // Rotation part should NOT be scaled (it's normalized)
        assertEquals(1.0, m[0], 1.0e-9);  // identity rotation
        assertEquals(0.0, m[1], 1.0e-9);
        assertEquals(0.0, m[2], 1.0e-9);
    }

    @Test
    void assemblyTransformWithScaleFactorDirectly() {
        // Test unit scaling without complex STEP unit entities
        // This validates the core F04 logic: scale factor applied to translation
        String s = step(
            "DATA;",
            "#1=APPLICATION_CONTEXT('mechanical design');",
            "#2=PRODUCT_CONTEXT('','mechanical',#1);",
            "#3=PRODUCT('ASM','Assembly','',(#2));",
            "#4=PRODUCT('PART','Part','',(#2));",
            "#5=PRODUCT_DEFINITION_FORMATION('v1','',#3);",
            "#6=PRODUCT_DEFINITION_FORMATION('v2','',#4);",
            "#7=PRODUCT_DEFINITION_CONTEXT('d','r',#1);",
            "#8=PRODUCT_DEFINITION('d1','',#5,#7);",
            "#9=PRODUCT_DEFINITION('d2','',#6,#7);",
            "#10=PRODUCT_DEFINITION_SHAPE('','',#8);",
            "#11=PRODUCT_DEFINITION_SHAPE('','',#9);",
            "#12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));",
            "#13=CARTESIAN_POINT('O',(0.0,0.0,0.0));",
            "#14=CARTESIAN_POINT('T',(10.0,20.0,5.0));",  // values in some unit
            "#15=DIRECTION('Z',(0.0,0.0,1.0));",
            "#16=DIRECTION('X',(1.0,0.0,0.0));",
            "#17=AXIS2_PLACEMENT_3D('A0',#13,#15,#16);",
            "#18=AXIS2_PLACEMENT_3D('A1',#14,#15,#16);",
            "#19=ITEM_DEFINED_TRANSFORMATION('t','',#17,#18);",
            "#20=SHAPE_REPRESENTATION('ASM_REP',(),#12);",
            "#21=SHAPE_REPRESENTATION('PART_REP',(),#12);",
            "#22=SHAPE_DEFINITION_REPRESENTATION(#10,#20);",
            "#23=SHAPE_DEFINITION_REPRESENTATION(#11,#21);",
            "#24=(REPRESENTATION_RELATIONSHIP('r','',#20,#21) REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));",
            "#25=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ','', '',#8,#9);",
            "#26=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#24,#25);",
            "ENDSEC;"
        );

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(s));

        // Test various scale factors
        // 1. scale = 2.0 (doubles the translation)
        AssemblyGraph graph2x = StepAssemblyGraphBuilder.build(resolved, 2.0);
        AssemblyNode part2x = graph2x.nodes().stream()
                .filter(n -> n.occurrenceId() != null)
                .findFirst().orElseThrow();
        double[] m2x = part2x.worldMatrix();
        assertEquals(20.0, m2x[3], 1.0e-9);   // 10 * 2
        assertEquals(40.0, m2x[7], 1.0e-9);   // 20 * 2
        assertEquals(10.0, m2x[11], 1.0e-9);  // 5 * 2

        // 2. scale = 0.5 (halves the translation)
        AssemblyGraph graphHalf = StepAssemblyGraphBuilder.build(resolved, 0.5);
        AssemblyNode partHalf = graphHalf.nodes().stream()
                .filter(n -> n.occurrenceId() != null)
                .findFirst().orElseThrow();
        double[] mHalf = partHalf.worldMatrix();
        assertEquals(5.0, mHalf[3], 1.0e-9);   // 10 * 0.5
        assertEquals(10.0, mHalf[7], 1.0e-9);  // 20 * 0.5
        assertEquals(2.5, mHalf[11], 1.0e-9);  // 5 * 0.5

        // 3. scale = 0.0254 (inch to meter)
        AssemblyGraph graphInch = StepAssemblyGraphBuilder.build(resolved, 0.0254);
        AssemblyNode partInch = graphInch.nodes().stream()
                .filter(n -> n.occurrenceId() != null)
                .findFirst().orElseThrow();
        double[] mInch = partInch.worldMatrix();
        assertEquals(0.254, mInch[3], 1.0e-9);   // 10 * 0.0254
        assertEquals(0.508, mInch[7], 1.0e-9);   // 20 * 0.0254
        assertEquals(0.127, mInch[11], 1.0e-9);  // 5 * 0.0254
    }

    @Test
    void assemblyTransformWithDefaultMeterUnits() {
        // STEP file with default meter units (no explicit unit, or meter unit)
        // Transform should NOT scale (scale factor = 1.0)
        String s = step(
            "DATA;",
            "#1=APPLICATION_CONTEXT('mechanical design');",
            "#2=PRODUCT_CONTEXT('','mechanical',#1);",
            "#3=PRODUCT('ASM','Assembly','',(#2));",
            "#4=PRODUCT('PART','Part','',(#2));",
            "#5=PRODUCT_DEFINITION_FORMATION('v1','',#3);",
            "#6=PRODUCT_DEFINITION_FORMATION('v2','',#4);",
            "#7=PRODUCT_DEFINITION_CONTEXT('d','r',#1);",
            "#8=PRODUCT_DEFINITION('d1','',#5,#7);",
            "#9=PRODUCT_DEFINITION('d2','',#6,#7);",
            "#10=PRODUCT_DEFINITION_SHAPE('','',#8);",
            "#11=PRODUCT_DEFINITION_SHAPE('','',#9);",
            "#12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));",
            "#13=CARTESIAN_POINT('O',(0.0,0.0,0.0));",
            "#14=CARTESIAN_POINT('T',(1.0,2.0,0.5));",  // meter values
            "#15=DIRECTION('Z',(0.0,0.0,1.0));",
            "#16=DIRECTION('X',(1.0,0.0,0.0));",
            "#17=AXIS2_PLACEMENT_3D('A0',#13,#15,#16);",
            "#18=AXIS2_PLACEMENT_3D('A1',#14,#15,#16);",
            "#19=ITEM_DEFINED_TRANSFORMATION('t','',#17,#18);",
            "#20=SHAPE_REPRESENTATION('ASM_REP',(),#12);",
            "#21=SHAPE_REPRESENTATION('PART_REP',(),#12);",
            "#22=SHAPE_DEFINITION_REPRESENTATION(#10,#20);",
            "#23=SHAPE_DEFINITION_REPRESENTATION(#11,#21);",
            "#24=(REPRESENTATION_RELATIONSHIP('r','',#20,#21) REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));",
            "#25=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ','', '',#8,#9);",
            "#26=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#24,#25);",
            "ENDSEC;"
        );

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(s));
        double scaleToMeters = 1.0; // meters (default)

        AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved, scaleToMeters);

        assertEquals(2, graph.nodes().size());

        AssemblyNode part = graph.nodes().stream()
                .filter(n -> n.occurrenceId() != null)
                .findFirst().orElseThrow();

        double[] m = part.worldMatrix();

        // Translation should remain in meters (no scaling)
        assertEquals(1.0, m[3], 1.0e-9);
        assertEquals(2.0, m[7], 1.0e-9);
        assertEquals(0.5, m[11], 1.0e-9);
    }
}