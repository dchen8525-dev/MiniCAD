package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StepTextReaderTest {

    @Test
    void shouldReadUtf8StepFiles() throws IOException {
        Path file = Files.createTempFile("minicad-utf8", ".step");
        String expected = """
                DATA;
                #1=EXAMPLE('UTF-8 name');
                ENDSEC;
                """;
        Files.writeString(file, expected, StandardCharsets.UTF_8);

        String text = StepTextReader.read(file);

        assertEquals(expected, text);
        assertEquals(expected, StepTextReader.read(expected.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void shouldFallbackToGb18030ForLocalizedCadExports() throws IOException {
        Path file = Files.createTempFile("minicad-gb18030", ".step");
        String expected = """
                DATA;
                #1=EXAMPLE('齿轮');
                ENDSEC;
                """;
        Files.write(file, expected.getBytes(Charset.forName("GB18030")));

        String text = StepTextReader.read(file);

        assertEquals(expected, text);
        assertEquals(expected, StepTextReader.read(expected.getBytes(Charset.forName("GB18030"))));
    }

    @Test
    void shouldFallbackToLatin1WhenUtf8AndGb18030Fail() throws IOException {
        Path file = Files.createTempFile("minicad-latin1", ".step");
        String expected = """
                DATA;
                #1=EXAMPLE('café');
                ENDSEC;
                """;
        Files.write(file, expected.getBytes(StandardCharsets.ISO_8859_1));

        String text = StepTextReader.read(file);

        assertEquals(expected, text);
        assertEquals(expected, StepTextReader.read(expected.getBytes(StandardCharsets.ISO_8859_1)));
    }
}
