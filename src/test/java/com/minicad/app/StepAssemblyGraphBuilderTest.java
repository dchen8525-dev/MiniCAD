package com.minicad.app;

import com.minicad.app.StepAssemblyGraphBuilder.AssemblyGraph;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyNode;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepAssemblyGraphBuilderTest {

    @Test
    void shouldBuildNestedAssemblyGraphWithAccumulatedTransforms() throws IOException {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                StepParser.parse(Files.readString(Path.of("examples/nested-assembly.step")))
        );

        AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved);

        assertEquals(3, graph.nodes().size());
        assertEquals(3, graph.representations().size());

        AssemblyNode root = graph.nodes().stream()
                .filter(node -> node.parentId() == null)
                .findFirst()
                .orElseThrow();
        AssemblyNode subAssembly = graph.nodes().stream()
                .filter(node -> Integer.valueOf(69).equals(node.occurrenceId()))
                .findFirst()
                .orElseThrow();
        AssemblyNode part = graph.nodes().stream()
                .filter(node -> Integer.valueOf(70).equals(node.occurrenceId()))
                .findFirst()
                .orElseThrow();

        assertEquals("Root Assembly", root.label());
        assertEquals(List.of(61), root.representationIds());
        assertEquals(root.id(), subAssembly.parentId());
        assertEquals(List.of(62), subAssembly.representationIds());
        assertEquals(List.of(63), part.representationIds());
        assertEquals(10.0, subAssembly.localMatrix()[3]);
        assertEquals(10.0, part.worldMatrix()[3]);
        assertEquals(4.0, part.worldMatrix()[7]);
        assertNotNull(graph.representations().stream().filter(rep -> rep.representationId() == 63).findFirst().orElse(null));
    }

    @Test
    void shouldBuildMultiplePartInstancesWithRotationAndTranslation() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                StepParser.parse("""
                        DATA;
                        #1=APPLICATION_CONTEXT('mechanical design');
                        #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                        #3=PRODUCT('ASM','Assembly','',(#2));
                        #4=PRODUCT('PART','Repeated Part','',(#2));
                        #5=PRODUCT_DEFINITION_FORMATION('asm-v1','',#3);
                        #6=PRODUCT_DEFINITION_FORMATION('part-v1','',#4);
                        #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                        #8=PRODUCT_DEFINITION('asm-def','assembly',#5,#7);
                        #9=PRODUCT_DEFINITION('part-def','part',#6,#7);
                        #10=PRODUCT_DEFINITION_SHAPE('asm-shape','',#8);
                        #11=PRODUCT_DEFINITION_SHAPE('part-shape','',#9);
                        #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                        #13=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                        #14=CARTESIAN_POINT('T1',(2.0,0.0,0.0));
                        #15=CARTESIAN_POINT('T2',(5.0,0.0,0.0));
                        #16=DIRECTION('DZ',(0.0,0.0,1.0));
                        #17=DIRECTION('DX',(1.0,0.0,0.0));
                        #18=DIRECTION('DY',(0.0,1.0,0.0));
                        #19=AXIS2_PLACEMENT_3D('AX0',#13,#16,#17);
                        #20=AXIS2_PLACEMENT_3D('MOVE',#14,#16,#17);
                        #21=AXIS2_PLACEMENT_3D('ROT90_MOVE',#15,#16,#18);
                        #22=ITEM_DEFINED_TRANSFORMATION('move','translate x',#19,#20);
                        #23=ITEM_DEFINED_TRANSFORMATION('rotate-move','rotate z and translate',#19,#21);
                        #24=SHAPE_REPRESENTATION('ASM_REP',(),#12);
                        #25=SHAPE_REPRESENTATION('PART_REP',(),#12);
                        #26=SHAPE_DEFINITION_REPRESENTATION(#10,#24);
                        #27=SHAPE_DEFINITION_REPRESENTATION(#11,#25);
                        #28=(REPRESENTATION_RELATIONSHIP('rr1','first instance',#24,#25)
                             REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));
                        #29=(REPRESENTATION_RELATIONSHIP('rr2','second instance',#24,#25)
                             REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#23));
                        #30=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','first part','',#8,#9);
                        #31=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-2','second part','',#8,#9);
                        #32=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#28,#30);
                        #33=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#29,#31);
                        ENDSEC;
                        """)
        );

        AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved);

        assertEquals(3, graph.nodes().size());
        assertEquals(2, graph.representations().size());

        AssemblyNode first = graph.nodes().stream()
                .filter(node -> Integer.valueOf(30).equals(node.occurrenceId()))
                .findFirst()
                .orElseThrow();
        AssemblyNode second = graph.nodes().stream()
                .filter(node -> Integer.valueOf(31).equals(node.occurrenceId()))
                .findFirst()
                .orElseThrow();

        assertEquals(List.of(25), first.representationIds());
        assertEquals(List.of(25), second.representationIds());
        assertEquals(2.0, first.worldMatrix()[3], 1.0e-9);
        assertEquals(5.0, second.worldMatrix()[3], 1.0e-9);
        assertEquals(0.0, second.worldMatrix()[0], 1.0e-9);
        assertEquals(-1.0, second.worldMatrix()[1], 1.0e-9);
        assertEquals(1.0, second.worldMatrix()[4], 1.0e-9);
        assertEquals(0.0, second.worldMatrix()[5], 1.0e-9);
    }

    @Test
    void shouldRejectParallelPlacementAxisAndReferenceDirection() {
        StepAxis2Placement3D placement = placement(
                List.of(0.0, 0.0, 0.0),
                List.of(0.0, 0.0, 1.0),
                List.of(0.0, 0.0, 2.0));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> StepAssemblyGraphBuilder.matrixForPlacement(placement));

        assertEquals("AXIS2_PLACEMENT_3D #1 axis and refDirection must not be parallel", ex.getMessage());
    }

    @Test
    void shouldOrthogonalizeSkewPlacementReferenceDirection() {
        StepAxis2Placement3D placement = placement(
                List.of(1.0, 2.0, 3.0),
                List.of(0.0, 0.0, 1.0),
                List.of(1.0, 1.0, 0.25));

        double[] matrix = StepAssemblyGraphBuilder.matrixForPlacement(placement);

        assertEquals(1.0, matrix[3], 1.0e-9);
        assertEquals(2.0, matrix[7], 1.0e-9);
        assertEquals(3.0, matrix[11], 1.0e-9);
        assertEquals(0.0, dot(column(matrix, 0), column(matrix, 1)), 1.0e-9);
        assertEquals(0.0, dot(column(matrix, 0), column(matrix, 2)), 1.0e-9);
        assertEquals(0.0, dot(column(matrix, 1), column(matrix, 2)), 1.0e-9);
        assertEquals(1.0, length(column(matrix, 0)), 1.0e-9);
        assertEquals(1.0, length(column(matrix, 1)), 1.0e-9);
        assertEquals(1.0, length(column(matrix, 2)), 1.0e-9);
    }

    private static StepAxis2Placement3D placement(List<Double> origin, List<Double> axis, List<Double> refDirection) {
        return new StepAxis2Placement3D(
                1,
                "P",
                new StepCartesianPoint(2, "O", origin),
                new StepDirection(3, "Z", axis),
                new StepDirection(4, "X", refDirection));
    }

    private static double[] column(double[] matrix, int column) {
        return new double[]{matrix[column], matrix[4 + column], matrix[8 + column]};
    }

    private static double dot(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static double length(double[] vector) {
        return Math.sqrt(dot(vector, vector));
    }
}
