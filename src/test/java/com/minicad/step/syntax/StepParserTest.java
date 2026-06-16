package com.minicad.step.syntax;

import com.minicad.common.StepParseException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepParserTest {

    @Test
    void shouldParseMinimalDataSection() {
        String step = 
        "ISO-10303-21;\n"
        + "HEADER;\n"
        + "FILE_DESCRIPTION(('mini cad'),'1');\n"
        + "ENDSEC;\n"
        + "DATA;\n"
        + "#10=CARTESIAN_POINT('P0',(1.0,2.0,3.0));\n"
        + "#11=DIRECTION('D0',(0.0,0.0,1.0));\n"
        + "ENDSEC;\n"
        + "END-ISO-10303-21;";

        StepFile file = StepParser.parse(step);

        assertEquals(2, file.entities().size());
        assertEquals(1, file.headerEntries().size());
        assertEquals("FILE_DESCRIPTION", file.headerEntries().get(0).getName());
        assertEquals("CARTESIAN_POINT", file.entities().get(0).name());
        assertEquals(10, file.entities().get(0).id());
    }

    @Test
    void shouldExposeTypedAp214AndAp242HeaderMetadata() {
        String step = 
        "ISO-10303-21;\n"
        + "HEADER;\n"
        + "FILE_DESCRIPTION(('AP214/AP242 metadata'),'2;1');\n"
        + "FILE_NAME('assembly.step','2026-06-10T12:00:00',('Alice','Bob'),('MiniCAD Org'),'MiniCAD 0.1','MiniCAD Exporter','');\n"
        + "FILE_SCHEMA(('AUTOMOTIVE_DESIGN','AP242_MANAGED_MODEL_BASED_3D_ENGINEERING_MIM_LF'));\n"
        + "ENDSEC;\n"
        + "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "ENDSEC;\n"
        + "END-ISO-10303-21;";

        StepFile file = StepParser.parse(step);
        StepFileName fileName = file.fileName().orElseThrow();
        StepFileSchema schema = file.fileSchema().orElseThrow();

        assertEquals("assembly.step", fileName.getName());
        assertEquals("2026-06-10T12:00:00", fileName.getTimeStamp());
        assertEquals(List.of("Alice", "Bob"), fileName.getAuthor());
        assertEquals(List.of("MiniCAD Org"), fileName.getOrganization());
        assertEquals("MiniCAD 0.1", fileName.getPreprocessorVersion());
        assertEquals("MiniCAD Exporter", fileName.getOriginatingSystem());
        assertEquals(List.of("AUTOMOTIVE_DESIGN", "AP242_MANAGED_MODEL_BASED_3D_ENGINEERING_MIM_LF"), schema.getSchemaNames());
        assertEquals(schema.getSchemaNames(), file.schemaNames());
        assertThrows(UnsupportedOperationException.class, () -> fileName.getAuthor().add("Mallory"));
        assertThrows(UnsupportedOperationException.class, () -> schema.getSchemaNames().clear());
    }

    @Test
    void shouldKeepForwardReferencesAsRawReferences() {
        String step = 
        "DATA;\n"
        + "#20=EDGE_CURVE('E0',#30,#31,#40,.T.);\n"
        + "#40=LINE('L0',#50,#60);\n"
        + "ENDSEC;";

        StepFile file = StepParser.parse(step);
        StepEntityInstance edgeCurve = file.entities().get(0);

        assertEquals("EDGE_CURVE", edgeCurve.name());
        assertInstanceOf(StepValue.ReferenceValue.class, edgeCurve.parameters().get(1));
        assertEquals(40, ((StepValue.ReferenceValue) edgeCurve.parameters().get(3)).getId());
    }

    @Test
    void shouldParseStringsEnumsOmittedValuesTypedValuesAndLists() {
        String step = 
        "DATA;\n"
        + "#1=EXAMPLE('A''B',$,*,.T.,LENGTH_MEASURE(1.0),(1.0,#2,'X'));\n"
        + "ENDSEC;";

        StepEntityInstance entity = StepParser.parse(step).entities().get(0);

        assertEquals("A'B", ((StepValue.StringValue) entity.parameters().get(0)).getValue());
        assertInstanceOf(StepValue.OmittedValue.class, entity.parameters().get(1));
        assertInstanceOf(StepValue.NotProvidedValue.class, entity.parameters().get(2));
        assertEquals("T", ((StepValue.EnumValue) entity.parameters().get(3)).getValue());
        StepValue.TypedValue typedValue = assertInstanceOf(StepValue.TypedValue.class, entity.parameters().get(4));
        assertEquals("LENGTH_MEASURE", typedValue.getTypeName());
        assertInstanceOf(StepValue.NumberValue.class, typedValue.getValue());
        StepValue.ListValue list = (StepValue.ListValue) entity.parameters().get(5);
        assertEquals(3, list.getElements().size());
    }

    @Test
    void shouldParseTypedValueParameterLists() {
        String step = 
        "DATA;\n"
        + "#1=EXAMPLE(AP242_SELECT(#2,'name',.T.));\n"
        + "ENDSEC;";

        StepEntityInstance entity = StepParser.parse(step).entities().get(0);

        StepValue.TypedValue typedValue = assertInstanceOf(StepValue.TypedValue.class, entity.parameters().get(0));
        assertEquals("AP242_SELECT", typedValue.getTypeName());
        StepValue.ListValue payload = assertInstanceOf(StepValue.ListValue.class, typedValue.getValue());
        assertEquals(3, payload.getElements().size());
    }

    @Test
    void shouldTreatCommentsAsWhitespaceBetweenParameterTokens() {
        String step = 
        "DATA;\n"
        + "/* before entity */\n"
        + "#1=EXAMPLE(\n"
        + "    /* string */ 'part',\n"
        + "    /* enum */ .DONE.,\n"
        + "    /* typed select */ AP242_SELECT(/* ref */ #2, /* name */ 'selected', /* flag */ .T.),\n"
        + "    /* list */ (1.0, /* omitted */ $, /* not provided */ *)\n"
        + ");\n"
        + "ENDSEC;";

        StepEntityInstance entity = StepParser.parse(step).entities().get(0);

        assertEquals("part", ((StepValue.StringValue) entity.parameters().get(0)).getValue());
        assertEquals("DONE", ((StepValue.EnumValue) entity.parameters().get(1)).getValue());
        StepValue.TypedValue typedValue = assertInstanceOf(StepValue.TypedValue.class, entity.parameters().get(2));
        assertEquals("AP242_SELECT", typedValue.getTypeName());
        StepValue.ListValue typedPayload = assertInstanceOf(StepValue.ListValue.class, typedValue.getValue());
        assertEquals(3, typedPayload.getElements().size());
        StepValue.ListValue list = assertInstanceOf(StepValue.ListValue.class, entity.parameters().get(3));
        assertInstanceOf(StepValue.OmittedValue.class, list.getElements().get(1));
        assertInstanceOf(StepValue.NotProvidedValue.class, list.getElements().get(2));
    }

    @Test
    void shouldDecodeStepStringEscapes() {
        String step = 
        "DATA;\n"
        + "#1=EXAMPLE('A''B','\\S\\D','\\P\\A\\S\\|','\\X\\E9','\\X2\\4F60597D\\X0\\','\\X2\\65E5672C\\X0\\','\\X4\\0001F600\\X0\\');\n"
        + "ENDSEC;";

        StepEntityInstance entity = StepParser.parse(step).entities().get(0);

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
        StepParseException invalidHex = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE('\\X\\ZZ');\n"
        + "ENDSEC;"
        ));
        assertTrue(invalidHex.getMessage().startsWith("malformed \\X\\ string escape at position "));

        StepParseException unterminatedLong = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE('\\X2\\4F60');\n"
        + "ENDSEC;"
        ));
        assertTrue(unterminatedLong.getMessage().startsWith("malformed long string escape at position "));
    }

    @Test
    void shouldPreserveOriginalNumberLiteral() {
        String step = 
        "DATA;\n"
        + "#1=EXAMPLE(1.25E-3);\n"
        + "ENDSEC;"
        ));

        StepValue.NumberValue number = assertInstanceOf(
                StepValue.NumberValue.class,
                StepParser.parse(step).entities().get(0).parameters().get(0));

        assertEquals(0.00125, number.getValue());
        assertEquals("1.25E-3", number.getRaw());
    }

    @Test
    void shouldRejectNonFiniteNumbers() {
        StepParseException hugeExponent = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE(1E9999);\n"
        + "ENDSEC;"
        ));
        assertEquals("non-finite number '1E9999' at position 17", hugeExponent.getMessage());

        StepParseException nan = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE(NaN);\n"
        + "ENDSEC;"
        ));
        assertEquals("invalid number 'NaN' at position 17", nan.getMessage());

        StepParseException infinity = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE(Infinity);\n"
        + "ENDSEC;"
        ));
        assertEquals("invalid number 'Infinity' at position 17", infinity.getMessage());
    }

    @Test
    void shouldRejectUnsupportedEntityIds() {
        StepParseException zero = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#0=EXAMPLE();\n"
        + "ENDSEC;"
        ));
        assertTrue(zero.getMessage().startsWith("entity id '#0' must be positive at position "));

        StepParseException negative = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#-1=EXAMPLE();\n"
        + "ENDSEC;"
        ));
        assertTrue(negative.getMessage().startsWith("entity id '#-1' must be positive at position "));

        StepParseException explicitPlus = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#+1=EXAMPLE();\n"
        + "ENDSEC;"
        ));
        assertTrue(explicitPlus.getMessage().startsWith("invalid entity id '#+1' at position "));

        StepParseException overflow = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#2147483648=EXAMPLE();\n"
        + "ENDSEC;"
        ));
        assertTrue(overflow.getMessage().startsWith(
                "entity id '#2147483648' exceeds supported maximum #2147483647 at position "));
    }

    @Test
    void shouldRejectUnsupportedReferenceIds() {
        StepParseException zero = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE(#0);\n"
        + "ENDSEC;"
        ));
        assertTrue(zero.getMessage().startsWith("referenced entity id '#0' must be positive at position "));

        StepParseException overflow = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE(#2147483648);\n"
        + "ENDSEC;"
        ));
        assertTrue(overflow.getMessage().startsWith(
                "referenced entity id '#2147483648' exceeds supported maximum #2147483647 at position "));
    }

    @Test
    void shouldNotTreatEndsecInsideStringAsSectionTerminator() {
        String step = 
        "ISO-10303-21;\n"
        + "HEADER;\n"
        + "FILE_DESCRIPTION(('contains ENDSEC; in header'),'1');\n"
        + "ENDSEC;\n"
        + "DATA;\n"
        + "#1=EXAMPLE('contains ENDSEC; in data');\n"
        + "#2=EXAMPLE('still parsing');\n"
        + "ENDSEC;\n"
        + "END-ISO-10303-21;";

        StepFile file = StepParser.parse(step);

        assertEquals(1, file.headerEntries().size());
        assertEquals(2, file.entities().size());
        assertEquals("contains ENDSEC; in data", ((StepValue.StringValue) file.entities().get(0).parameters().get(0)).getValue());
    }

    @Test
    void shouldFindSectionsOutsideStringsAndComments() {
        String step = 
        "ISO-10303-21;\n"
        + "/* HEADER; DATA; ENDSEC; */\n"
        + "HEADER;\n"
        + "FILE_DESCRIPTION(('mentions DATA; and ENDSEC; before real data'),'1');\n"
        + "ENDSEC;\n"
        + "/* DATA; */\n"
        + "DATA;\n"
        + "#1=EXAMPLE('payload');\n"
        + "ENDSEC;\n"
        + "END-ISO-10303-21;";

        StepFile file = StepParser.parse(step);

        assertEquals(1, file.headerEntries().size());
        assertEquals(1, file.entities().size());
        assertEquals("EXAMPLE", file.entities().get(0).name());
    }

    @Test
    void shouldNotMatchSectionKeywordsInsideLongerWords() {
        StepParseException noData = assertThrows(StepParseException.class, () -> StepParser.parse(
        "ISO-10303-21;\n"
        + "SOMEDATA;\n"
        + "#1=EXAMPLE('payload');\n"
        + "ENDSEC;\n"
        + "END-ISO-10303-21;"
        ));
        assertEquals("missing DATA section", noData.getMessage());

        String valid = 
        "ISO-10303-21;\n"
        + "DATA;\n"
        + "#1=EXAMPLE('SOMEENDSEC; text');\n"
        + "ENDSEC;\n"
        + "END-ISO-10303-21;"
        ));

        StepFile file = StepParser.parse(valid);

        assertEquals(1, file.entities().size());
        assertEquals("SOMEENDSEC; text", ((StepValue.StringValue) file.entities().get(0).parameters().get(0)).getValue());
    }

    @Test
    void shouldRejectMissingSemicolon() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0))\n"
        + "ENDSEC;";

        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(step));

        assertTrue(exception.getMessage().startsWith("expected ';' after entity instance at position "));
    }

    @Test
    void shouldRejectMissingDataEndsec() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE();"

        assertEquals("missing ENDSEC for DATA section", exception.getMessage());
    }

    @Test
    void shouldRejectUnterminatedString() {
        StepParseException inData = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=EXAMPLE('unterminated);\n"
        + "ENDSEC;"
        ));
        assertTrue(inData.getMessage().startsWith("unterminated string at position "));

        StepParseException beforeData = assertThrows(StepParseException.class, () -> StepParser.parse(
        "ISO-10303-21;\n"
        + "'unterminated\n"
        + "DATA;\n"
        + "#1=EXAMPLE();\n"
        + "ENDSEC;"
        ));
        assertTrue(beforeData.getMessage().startsWith("unterminated string at position "));
    }

    @Test
    void shouldRejectUnterminatedComment() {
        StepParseException beforeData = assertThrows(StepParseException.class, () -> StepParser.parse(
        "ISO-10303-21;\n"
        + "/* unterminated\n"
        + "DATA;\n"
        + "#1=EXAMPLE();\n"
        + "ENDSEC;"
        ));
        assertTrue(beforeData.getMessage().startsWith("unterminated comment at position "));

        StepParseException inData = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "/* unterminated\n"
        + "#1=EXAMPLE();\n"
        + "ENDSEC;"
        ));
        assertTrue(inData.getMessage().startsWith("unterminated comment at position "));
    }

    @Test
    void shouldRejectIllegalStepSyntax() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0];\n"
        + "ENDSEC;"
        ));

        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(step));

        assertEquals("unexpected character ']' at position 42", exception.getMessage());
    }

    @Test
    void shouldParseComplexEntityInstance() {
        String step = 
        "DATA;\n"
        + "#1=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "ENDSEC;"
        ));

        StepEntityInstance instance = StepParser.parse(step).entities().get(0);

        assertTrue(instance.isComplex());
        assertEquals(2, instance.definitions().size());
        assertTrue(instance.hasDefinition("GEOMETRIC_REPRESENTATION_CONTEXT"));
        assertTrue(instance.hasDefinition("REPRESENTATION_CONTEXT"));
    }

    @Test
    void shouldReportEofInsideComplexEntityWithOpeningPosition() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=(GEOMETRIC_REPRESENTATION_CONTEXT(3)\n"
        + "ENDSEC;"
        ));

        assertEquals("unterminated complex entity opened at position 9", exception.getMessage());
    }

    @Test
    void shouldRejectMultipleDataSectionsExplicitly() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=A();\n"
        + "ENDSEC;\n"
        + "DATA;\n"
        + "#2=B();\n"
        + "ENDSEC;"
        ));

        assertEquals("multiple DATA sections are not supported", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicateEntityIdsDuringParse() {
        StepParseException exception = assertThrows(StepParseException.class, () -> StepParser.parse(
        "DATA;\n"
        + "#1=A();\n"
        + "#2=B();\n"
        + "#1=C();\n"
        + "ENDSEC;"
        ));

        assertTrue(exception.getMessage().startsWith("duplicate entity id #1 at position "));
        assertTrue(exception.getMessage().contains("; first declared at position "));
    }

    @Test
    void shouldCacheNormalizedDefinitionNamesForCaseInsensitiveQueries() {
        String step = 
        "DATA;\n"
        + "#1=(geometric_representation_context(3) representation_context('ID','MODEL'));\n"
        + "ENDSEC;"
        ));

        StepEntityInstance instance = StepParser.parse(step).entities().get(0);

        assertEquals(List.of("GEOMETRIC_REPRESENTATION_CONTEXT", "REPRESENTATION_CONTEXT"), instance.normalizedDefinitionNames());
        assertTrue(instance.hasDefinition("representation_context"));
        assertSame(instance.definitions().get(0), instance.requireDefinition("GEOMETRIC_REPRESENTATION_CONTEXT"));
    }

    @Test
    void shouldIndexEntitiesById() {
        String step = 
        "DATA;\n"
        + "#1=A();\n"
        + "#2=B(#1);\n"
        + "ENDSEC;";

        StepFile file = StepParser.parse(step);

        assertEquals(List.of(1, 2), file.entitiesById().keySet().stream().collect(Collectors.toList()));
        assertEquals("B", file.entitiesById().get(2).name());
    }

    @Test
    void shouldExposeImmutableEntityIndex() {
        String step = 
        "DATA;\n"
        + "#1=A();\n"
        + "ENDSEC;";

        StepFile file = StepParser.parse(step);

        assertThrows(UnsupportedOperationException.class, () -> file.entitiesById().clear());
    }

    @Test
    void parserFuzzShouldNotHangOrThrowUnexpectedExceptions() {
        Random random = new Random(0x51E9C0DEL);

        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int i = 0; i < 250; i++) {
                String step = "DATA;\n" + randomStepFragment(random) + "\nENDSEC;\n";
                try {
                    StepParser.parse(step);
                } catch (StepParseException expected) {
                    assertTrue(expected.getMessage() != null && !expected.getMessage().isBlank());
                }
            }
        });
    }

    private static String stringParameter(StepEntityInstance entity, int index) {
        return assertInstanceOf(StepValue.StringValue.class, entity.parameters().get(index)).getValue();
    }

    private static String randomStepFragment(Random random) {
        String[] tokens = {
                "#", "=", ";", "(", ")", ",", "$", "*",
                "A", "B", "CARTESIAN_POINT", "DIRECTION", "AP242_SELECT",
                "1", "2", "3", "0.0", "-4.5", "1E3", ".T.", ".FALSE.",
                "'name'", "'A''B'", "#1", "#2", "#2147483648",
                "/* comment */"
        };
        StringBuilder fragment = new StringBuilder();
        int tokenCount = 1 + random.nextInt(48);
        for (int i = 0; i < tokenCount; i++) {
            if (i > 0 && random.nextBoolean()) {
                fragment.append(' ');
            }
            fragment.append(tokens[random.nextInt(tokens.length)]);
        }
        return fragment.toString();
    }
}
