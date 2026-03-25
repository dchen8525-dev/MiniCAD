package com.minicad.step.syntax;

import com.minicad.common.StepParseException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepParserTest {

    @Test
    void shouldParseMinimalDataSection() {
        String step = """
                ISO-10303-21;
                HEADER;
                ENDSEC;
                DATA;
                #10=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #11=DIRECTION('D0',(0.0,0.0,1.0));
                ENDSEC;
                END-ISO-10303-21;
                """;

        StepFile file = StepParser.parse(step);

        assertEquals(2, file.entities().size());
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
    void shouldParseStringsEnumsOmittedValuesAndLists() {
        String step = """
                DATA;
                #1=EXAMPLE('A''B',$,.T.,(1.0,#2,'X'));
                ENDSEC;
                """;

        StepEntityInstance entity = StepParser.parse(step).entities().getFirst();

        assertEquals("A'B", ((StepValue.StringValue) entity.parameters().get(0)).value());
        assertInstanceOf(StepValue.OmittedValue.class, entity.parameters().get(1));
        assertEquals("T", ((StepValue.EnumValue) entity.parameters().get(2)).value());
        StepValue.ListValue list = (StepValue.ListValue) entity.parameters().get(3);
        assertEquals(3, list.elements().size());
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

        assertEquals("unexpected character ']' at position 37", exception.getMessage());
    }

    @Test
    void shouldRejectUnsupportedTypedParameterSyntax() {
        String step = """
                DATA;
                #1=SHAPE_REPRESENTATION_RELATIONSHIP('A',B_SOMETHING(#2));
                ENDSEC;
                """;

        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(step));

        assertTrue(exception.getMessage().startsWith("typed parameters are unsupported at position "));
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
