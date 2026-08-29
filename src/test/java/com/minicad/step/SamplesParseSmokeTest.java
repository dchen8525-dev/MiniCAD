package com.minicad.step;

import com.minicad.helper.StepTextReader;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Regression guard: every real STEP file under {@code samples/} must parse, resolve,
 * and build into a CAD model without throwing. Catches parser / resolver / builder
 * regressions on production-shaped data (far closer to reality than unit tests alone).
 */
class SamplesParseSmokeTest {

    private static final Path SAMPLES_DIR = Paths.get("samples");

    @Test
    void allSamplesParseResolveAndBuildWithoutException() throws IOException {
        List<Path> files = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(SAMPLES_DIR)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".step") || name.endsWith(".stp");
                    })
                    .forEach(files::add);
        }
        assertFalse(files.isEmpty(), "expected sample STEP files under " + SAMPLES_DIR);

        List<String> failures = new ArrayList<>();
        for (Path file : files) {
            try {
                String text = StepTextReader.read(file);
                StepFile stepFile = StepParser.parse(text);
                if (stepFile.entities().isEmpty()) {
                    failures.add(file + ": parsed but produced zero entities");
                    continue;
                }
                var resolved = StepEntityResolver.resolveAll(stepFile);
                StepCadBuilder.fromResolved(resolved);
            } catch (Exception ex) {
                failures.add(file + ": " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
        }

        if (!failures.isEmpty()) {
            fail("Sample parsing failures (" + failures.size() + "/" + files.size() + "):\n"
                    + String.join("\n", failures));
        }
    }
}
