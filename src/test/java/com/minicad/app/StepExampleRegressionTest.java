package com.minicad.app;

import com.minicad.helper.StepTextReader;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

class StepExampleRegressionTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("exampleStepFiles")
    void shouldParseEveryExampleStepFile(Path path) throws IOException {
        StepFile file = StepParser.parse(StepTextReader.read(path));

        assertFalse(file.entities().isEmpty(), () -> path + " should contain DATA entities");
    }

    static Stream<Path> exampleStepFiles() throws IOException {
        return Files.list(Path.of("samples"))
                .filter(Files::isRegularFile)
                .filter(StepExampleRegressionTest::isStepFile)
                .sorted(Comparator.comparing(path -> path.getFileName().toString()));
    }

    private static boolean isStepFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".step") || fileName.endsWith(".stp") || fileName.endsWith(".p21");
    }
}
