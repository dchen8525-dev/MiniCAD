package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepRealWorldCorpusTest {

    private static final Path REAL_WORLD_DIR = Path.of("src", "test", "resources", "step", "realworld");

    @Test
    void realWorldCorpusDirectoryExists() {
        assertTrue(Files.isDirectory(REAL_WORLD_DIR));
        assertTrue(Files.isRegularFile(REAL_WORLD_DIR.resolve("README.md")));
        assertTrue(Files.isRegularFile(REAL_WORLD_DIR.resolve("corpus-manifest.tsv")));
        assertTrue(Files.isRegularFile(REAL_WORLD_DIR.resolve("local-only").resolve(".gitignore")));
    }

    @Test
    void compilesLocalRealWorldFixturesWhenPresent() throws IOException {
        List<Path> fixtures;
        try (var stream = Files.walk(REAL_WORLD_DIR)) {
            fixtures = stream
                    .filter(Files::isRegularFile)
                    .filter(StepRealWorldCorpusTest::isStepFixture)
                    .sorted()
                    .collect(Collectors.toList());
        }

        for (Path fixture : fixtures) {
            String text = StepTextReader.read(fixture);
            CompiledStepDocument compiled = CompiledStepDocument.compile(text);
            assertFalse(compiled.stepFile().entities().isEmpty(), fixture.toString());
            assertFalse(compiled.resolved().isEmpty(), fixture.toString());
            StepPreviewJsonExporter.export(compiled);
        }
    }

    private static boolean isStepFixture(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        return fileName.endsWith(".step") || fileName.endsWith(".stp") || fileName.endsWith(".p21");
    }
}
