package com.minicad.geometry;

import com.minicad.app.CompiledStepDocument;
import com.minicad.app.StepAssemblyGraphBuilder;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyGraph;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyNode;

import com.minicad.common.GeometryException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryFAssemblyTransformTest {

    private static final String L = System.lineSeparator();

    private static String step(String... lines) {
        return String.join(L, lines);
    }

    private static StepCadBuilder builder(String stepData) {
        return StepCadBuilder.fromResolved(StepEntityResolver.resolveAll(StepParser.parse(stepData)));
    }

    private static double dot3(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static double norm3(double[] v) {
        return Math.sqrt(dot3(v, v));
    }

    // ===== F01: MAPPED_ITEM transform correctness =====

    @Test
    void nestedAssemblyTransformsComposeCorrectly() {
        String s = step(
            "DATA;",
            "#1=APPLICATION_CONTEXT('mechanical design');",
            "#2=PRODUCT_CONTEXT('','mechanical',#1);",
            "#3=PRODUCT('ROOT','Root','',(#2));",
            "#4=PRODUCT('SUB','SubAsm','',(#2));",
            "#5=PRODUCT('PART','Leaf','',(#2));",
            "#6=PRODUCT_DEFINITION_FORMATION('v1','',#3);",
            "#7=PRODUCT_DEFINITION_FORMATION('v2','',#4);",
            "#8=PRODUCT_DEFINITION_FORMATION('v3','',#5);",
            "#9=PRODUCT_DEFINITION_CONTEXT('d','r',#1);",
            "#10=PRODUCT_DEFINITION('d1','',#6,#9);",
            "#11=PRODUCT_DEFINITION('d2','',#7,#9);",
            "#12=PRODUCT_DEFINITION('d3','',#8,#9);",
            "#13=PRODUCT_DEFINITION_SHAPE('s1','',#10);",
            "#14=PRODUCT_DEFINITION_SHAPE('s2','',#11);",
            "#15=PRODUCT_DEFINITION_SHAPE('s3','',#12);",
            "#16=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));",
            "#17=CARTESIAN_POINT('O',(0.0,0.0,0.0));",
            "#18=CARTESIAN_POINT('T1',(10.0,0.0,0.0));",
            "#19=CARTESIAN_POINT('T2',(0.0,5.0,0.0));",
            "#20=DIRECTION('Z',(0.0,0.0,1.0));",
            "#21=DIRECTION('X',(1.0,0.0,0.0));",
            "#22=AXIS2_PLACEMENT_3D('A0',#17,#20,#21);",
            "#23=AXIS2_PLACEMENT_3D('A1',#18,#20,#21);",
            "#24=AXIS2_PLACEMENT_3D('A2',#19,#20,#21);",
            "#25=ITEM_DEFINED_TRANSFORMATION('t1','',#22,#23);",
            "#26=ITEM_DEFINED_TRANSFORMATION('t2','',#22,#24);",
            "#27=SHAPE_REPRESENTATION('ROOT_REP',(),#16);",
            "#28=SHAPE_REPRESENTATION('SUB_REP',(),#16);",
            "#29=SHAPE_REPRESENTATION('PART_REP',(),#16);",
            "#30=SHAPE_DEFINITION_REPRESENTATION(#13,#27);",
            "#31=SHAPE_DEFINITION_REPRESENTATION(#14,#28);",
            "#32=SHAPE_DEFINITION_REPRESENTATION(#15,#29);",
            "#33=(REPRESENTATION_RELATIONSHIP('r1','',#27,#28) REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#25));",
            "#34=(REPRESENTATION_RELATIONSHIP('r2','',#28,#29) REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#26));",
            "#35=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-sub','', '',#10,#11);",
            "#36=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-part','', '',#11,#12);",
            "#37=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#33,#35);",
            "#38=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#34,#36);",
            "ENDSEC;"
        );
        AssemblyGraph graph = StepAssemblyGraphBuilder.build(
                StepEntityResolver.resolveAll(StepParser.parse(s)));
        assertEquals(3, graph.nodes().size());
        AssemblyNode leaf = graph.nodes().stream()
                .filter(n -> Integer.valueOf(36).equals(n.occurrenceId()))
                .findFirst().orElseThrow();
        assertEquals(10.0, leaf.worldMatrix()[3], 1.0e-9);
        assertEquals(5.0, leaf.worldMatrix()[7], 1.0e-9);
        assertEquals(0.0, leaf.worldMatrix()[11], 1.0e-9);
    }

    // F01: rotation + translation combined
    @Test
    void rotationAndTranslationCombinedInAssemblyTransform() {
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
            "#14=CARTESIAN_POINT('T',(10.0,20.0,0.0));",
            "#15=DIRECTION('Z',(0.0,0.0,1.0));",
            "#16=DIRECTION('X',(1.0,0.0,0.0));",
            "#17=DIRECTION('Y',(0.0,1.0,0.0));",
            "#18=AXIS2_PLACEMENT_3D('A0',#13,#15,#16);",
            "#19=AXIS2_PLACEMENT_3D('A1',#14,#15,#17);",
            "#20=ITEM_DEFINED_TRANSFORMATION('t','',#18,#19);",
            "#21=SHAPE_REPRESENTATION('ASM_REP',(),#12);",
            "#22=SHAPE_REPRESENTATION('PART_REP',(),#12);",
            "#23=SHAPE_DEFINITION_REPRESENTATION(#10,#21);",
            "#24=SHAPE_DEFINITION_REPRESENTATION(#11,#22);",
            "#25=(REPRESENTATION_RELATIONSHIP('r','',#21,#22) REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#20));",
            "#26=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ','', '',#8,#9);",
            "#27=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#25,#26);",
            "ENDSEC;"
        );
        AssemblyGraph graph = StepAssemblyGraphBuilder.build(
                StepEntityResolver.resolveAll(StepParser.parse(s)));
        assertEquals(2, graph.nodes().size());
        AssemblyNode part = graph.nodes().stream()
                .filter(n -> n.occurrenceId() != null)
                .findFirst().orElseThrow();
        double[] m = part.worldMatrix();
        assertEquals(0.0, m[0], 1.0e-9);
        assertEquals(-1.0, m[1], 1.0e-9);
        assertEquals(0.0, m[2], 1.0e-9);
        assertEquals(1.0, m[4], 1.0e-9);
        assertEquals(0.0, m[5], 1.0e-9);
        assertEquals(10.0, m[3], 1.0e-9);
        assertEquals(20.0, m[7], 1.0e-9);
    }
}
