package com.minicad.step.syntax;

import com.minicad.common.StepParseException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepParserTest {

    @Test
    void shouldParseMinimalDataSection() {
        String step = """
                ISO-10303-21;
                HEADER;
                FILE_DESCRIPTION(('mini cad'),'1');
                ENDSEC;
                DATA;
                #10=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #11=DIRECTION('D0',(0.0,0.0,1.0));
                ENDSEC;
                END-ISO-10303-21;
                """;

        StepFile file = StepParser.parse(step);

        assertEquals(2, file.entities().size());
        assertEquals(1, file.headerEntries().size());
        assertEquals("FILE_DESCRIPTION", file.headerEntries().getFirst().name());
        assertEquals("CARTESIAN_POINT", file.entities().getFirst().name());
        assertEquals(10, file.entities().getFirst().id());
    }

    @Test
    void shouldKeepForwardReferencesAsRawReferences() {
        String step = """
                DATA;
                #20=EDGE_CURVE('E0',#30,#31,#40,.T.);
                #40=LINE('L0',#50,#60);
                ENDSEC;
                """;

        StepFile file = StepParser.parse(step);
        StepEntityInstance edgeCurve = file.entities().getFirst();

        assertEquals("EDGE_CURVE", edgeCurve.name());
        assertInstanceOf(StepValue.ReferenceValue.class, edgeCurve.parameters().get(1));
        assertEquals(40, ((StepValue.ReferenceValue) edgeCurve.parameters().get(3)).id());
    }

    @Test
    void shouldParseStringsEnumsOmittedValuesTypedValuesAndLists() {
        String step = """
                DATA;
                #1=EXAMPLE('A''B',$,*,.T.,LENGTH_MEASURE(1.0),(1.0,#2,'X'));
                ENDSEC;
                """;

        StepEntityInstance entity = StepParser.parse(step).entities().getFirst();

        assertEquals("A'B", ((StepValue.StringValue) entity.parameters().get(0)).value());
        assertInstanceOf(StepValue.OmittedValue.class, entity.parameters().get(1));
        assertInstanceOf(StepValue.NotProvidedValue.class, entity.parameters().get(2));
        assertEquals("T", ((StepValue.EnumValue) entity.parameters().get(3)).value());
        StepValue.TypedValue typedValue = assertInstanceOf(StepValue.TypedValue.class, entity.parameters().get(4));
        assertEquals("LENGTH_MEASURE", typedValue.typeName());
        StepValue.ListValue list = (StepValue.ListValue) entity.parameters().get(5);
        assertEquals(3, list.elements().size());
    }

    @Test
    void shouldNotTreatEndsecInsideStringAsSectionTerminator() {
        String step = """
                ISO-10303-21;
                HEADER;
                FILE_DESCRIPTION(('contains ENDSEC; in header'),'1');
                ENDSEC;
                DATA;
                #1=EXAMPLE('contains ENDSEC; in data');
                #2=EXAMPLE('still parsing');
                ENDSEC;
                END-ISO-10303-21;
                """;

        StepFile file = StepParser.parse(step);

        assertEquals(1, file.headerEntries().size());
        assertEquals(2, file.entities().size());
        assertEquals("contains ENDSEC; in data", ((StepValue.StringValue) file.entities().getFirst().parameters().getFirst()).value());
    }

    @Test
    void shouldRejectMissingSemicolon() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0))
                ENDSEC;
                """;

        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(step));

        assertTrue(exception.getMessage().startsWith("expected ';' after entity instance at position "));
    }

    @Test
    void shouldRejectIllegalStepSyntax() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0];
                ENDSEC;
                """;

        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(step));

        assertEquals("unexpected character ']' at position 42", exception.getMessage());
    }

    @Test
    void shouldParseComplexEntityInstance() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                ENDSEC;
                """;

        StepEntityInstance instance = StepParser.parse(step).entities().getFirst();

        assertTrue(instance.isComplex());
        assertEquals(2, instance.definitions().size());
        assertTrue(instance.hasDefinition("GEOMETRIC_REPRESENTATION_CONTEXT"));
        assertTrue(instance.hasDefinition("REPRESENTATION_CONTEXT"));
    }

    @Test
    void shouldCacheNormalizedDefinitionNamesForCaseInsensitiveQueries() {
        String step = """
                DATA;
                #1=(geometric_representation_context(3) representation_context('ID','MODEL'));
                ENDSEC;
                """;

        StepEntityInstance instance = StepParser.parse(step).entities().getFirst();

        assertEquals(List.of("GEOMETRIC_REPRESENTATION_CONTEXT", "REPRESENTATION_CONTEXT"), instance.normalizedDefinitionNames());
        assertTrue(instance.hasDefinition("representation_context"));
        assertSame(instance.definitions().getFirst(), instance.requireDefinition("GEOMETRIC_REPRESENTATION_CONTEXT"));
    }

    @Test
    void shouldIndexEntitiesById() {
        String step = """
                DATA;
                #1=A();
                #2=B(#1);
                ENDSEC;
                """;

        StepFile file = StepParser.parse(step);

        assertEquals(List.of(1, 2), file.entitiesById().keySet().stream().toList());
        assertEquals("B", file.entitiesById().get(2).name());
    }
}
