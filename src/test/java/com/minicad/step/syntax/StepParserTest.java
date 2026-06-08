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
    void shouldDecodeStepStringEscapes() {
        String step = """
                DATA;
                #1=EXAMPLE('A''B','\\S\\D','\\P\\A\\S\\|','\\X\\E9','\\X2\\4F60597D\\X0\\','\\X2\\65E5672C\\X0\\','\\X4\\0001F600\\X0\\');
                ENDSEC;
                """;

        StepEntityInstance entity = StepParser.parse(step).entities().getFirst();

        assertEquals("A'B", stringParameter(entity, 0));
        assertEquals("\u00C4", stringParameter(entity, 1));
        assertEquals("\u00FC", stringParameter(entity, 2));
        assertEquals("\u00E9", stringParameter(entity, 3));
        assertEquals("\u4F60\u597D", stringParameter(entity, 4));
        assertEquals("\u65E5\u672C", stringParameter(entity, 5));
        assertEquals("\uD83D\uDE00", stringParameter(entity, 6));
    }

    @Test
    void shouldRejectMalformedStepStringEscapes() {
        StepParseException invalidHex = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE('\\X\\ZZ');
                ENDSEC;
                """));
        assertTrue(invalidHex.getMessage().startsWith("malformed \\X\\ string escape at position "));

        StepParseException unterminatedLong = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE('\\X2\\4F60');
                ENDSEC;
                """));
        assertTrue(unterminatedLong.getMessage().startsWith("malformed long string escape at position "));
    }

    @Test
    void shouldPreserveOriginalNumberLiteral() {
        String step = """
                DATA;
                #1=EXAMPLE(1.25E-3);
                ENDSEC;
                """;

        StepValue.NumberValue number = assertInstanceOf(
                StepValue.NumberValue.class,
                StepParser.parse(step).entities().getFirst().parameters().getFirst());

        assertEquals(0.00125, number.value());
        assertEquals("1.25E-3", number.raw());
    }

    @Test
    void shouldRejectNonFiniteNumbers() {
        StepParseException hugeExponent = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE(1E9999);
                ENDSEC;
                """));
        assertEquals("non-finite number '1E9999' at position 17", hugeExponent.getMessage());

        StepParseException nan = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE(NaN);
                ENDSEC;
                """));
        assertEquals("invalid number 'NaN' at position 17", nan.getMessage());

        StepParseException infinity = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE(Infinity);
                ENDSEC;
                """));
        assertEquals("invalid number 'Infinity' at position 17", infinity.getMessage());
    }

    @Test
    void shouldRejectUnsupportedEntityIds() {
        StepParseException zero = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #0=EXAMPLE();
                ENDSEC;
                """));
        assertTrue(zero.getMessage().startsWith("entity id '#0' must be positive at position "));

        StepParseException negative = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #-1=EXAMPLE();
                ENDSEC;
                """));
        assertTrue(negative.getMessage().startsWith("entity id '#-1' must be positive at position "));

        StepParseException explicitPlus = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #+1=EXAMPLE();
                ENDSEC;
                """));
        assertTrue(explicitPlus.getMessage().startsWith("invalid entity id '#+1' at position "));

        StepParseException overflow = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #2147483648=EXAMPLE();
                ENDSEC;
                """));
        assertTrue(overflow.getMessage().startsWith(
                "entity id '#2147483648' exceeds supported maximum #2147483647 at position "));
    }

    @Test
    void shouldRejectUnsupportedReferenceIds() {
        StepParseException zero = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE(#0);
                ENDSEC;
                """));
        assertTrue(zero.getMessage().startsWith("referenced entity id '#0' must be positive at position "));

        StepParseException overflow = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE(#2147483648);
                ENDSEC;
                """));
        assertTrue(overflow.getMessage().startsWith(
                "referenced entity id '#2147483648' exceeds supported maximum #2147483647 at position "));
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
    void shouldFindSectionsOutsideStringsAndComments() {
        String step = """
                ISO-10303-21;
                /* HEADER; DATA; ENDSEC; */
                HEADER;
                FILE_DESCRIPTION(('mentions DATA; and ENDSEC; before real data'),'1');
                ENDSEC;
                /* DATA; */
                DATA;
                #1=EXAMPLE('payload');
                ENDSEC;
                END-ISO-10303-21;
                """;

        StepFile file = StepParser.parse(step);

        assertEquals(1, file.headerEntries().size());
        assertEquals(1, file.entities().size());
        assertEquals("EXAMPLE", file.entities().getFirst().name());
    }

    @Test
    void shouldNotMatchSectionKeywordsInsideLongerWords() {
        StepParseException noData = assertThrows(StepParseException.class, () -> StepParser.parse("""
                ISO-10303-21;
                SOMEDATA;
                #1=EXAMPLE('payload');
                ENDSEC;
                END-ISO-10303-21;
                """));
        assertEquals("missing DATA section", noData.getMessage());

        String valid = """
                ISO-10303-21;
                DATA;
                #1=EXAMPLE('SOMEENDSEC; text');
                ENDSEC;
                END-ISO-10303-21;
                """;

        StepFile file = StepParser.parse(valid);

        assertEquals(1, file.entities().size());
        assertEquals("SOMEENDSEC; text", ((StepValue.StringValue) file.entities().getFirst().parameters().getFirst()).value());
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
    void shouldRejectMissingDataEndsec() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE();
                """));

        assertEquals("missing ENDSEC for DATA section", exception.getMessage());
    }

    @Test
    void shouldRejectUnterminatedString() {
        StepParseException inData = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=EXAMPLE('unterminated);
                ENDSEC;
                """));
        assertTrue(inData.getMessage().startsWith("unterminated string at position "));

        StepParseException beforeData = assertThrows(StepParseException.class, () -> StepParser.parse("""
                ISO-10303-21;
                'unterminated
                DATA;
                #1=EXAMPLE();
                ENDSEC;
                """));
        assertTrue(beforeData.getMessage().startsWith("unterminated string at position "));
    }

    @Test
    void shouldRejectUnterminatedComment() {
        StepParseException beforeData = assertThrows(StepParseException.class, () -> StepParser.parse("""
                ISO-10303-21;
                /* unterminated
                DATA;
                #1=EXAMPLE();
                ENDSEC;
                """));
        assertTrue(beforeData.getMessage().startsWith("unterminated comment at position "));

        StepParseException inData = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                /* unterminated
                #1=EXAMPLE();
                ENDSEC;
                """));
        assertTrue(inData.getMessage().startsWith("unterminated comment at position "));
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
    void shouldReportEofInsideComplexEntityWithOpeningPosition() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                ENDSEC;
                """));

        assertEquals("unterminated complex entity opened at position 9", exception.getMessage());
    }

    @Test
    void shouldRejectMultipleDataSectionsExplicitly() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=A();
                ENDSEC;
                DATA;
                #2=B();
                ENDSEC;
                """));

        assertEquals("multiple DATA sections are not supported", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicateEntityIdsDuringParse() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse("""
                DATA;
                #1=A();
                #2=B();
                #1=C();
                ENDSEC;
                """));

        assertTrue(exception.getMessage().startsWith("duplicate entity id #1 at position "));
        assertTrue(exception.getMessage().contains("; first declared at position "));
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

    @Test
    void shouldExposeImmutableEntityIndex() {
        String step = """
                DATA;
                #1=A();
                ENDSEC;
                """;

        StepFile file = StepParser.parse(step);

        assertThrows(UnsupportedOperationException.class, () -> file.entitiesById().clear());
    }

    private static String stringParameter(StepEntityInstance entity, int index) {
        return assertInstanceOf(StepValue.StringValue.class, entity.parameters().get(index)).value();
    }
}
