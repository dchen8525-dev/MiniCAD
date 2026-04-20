package com.minicad.app;

import com.minicad.app.StepAssemblyGraphBuilder.AssemblyGraph;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyNode;
import com.minicad.step.model.base.StepEntity;
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
}
