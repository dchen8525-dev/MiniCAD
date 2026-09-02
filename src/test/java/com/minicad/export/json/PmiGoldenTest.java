package com.minicad.export.json;

import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.helper.StepTextReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Behavioural golden test over the STEP corpus in samples/.
 *
 * The PMI target builder dispatches on entity type through very long
 * instanceof chains (collectSemanticTargets ~1670 lines,
 * appendSemanticDefinitionTargets ~815 lines). Converting those to a dispatch
 * table is a purely mechanical refactor, so the only thing that can go wrong is
 * a change in behaviour: a branch that stops matching, a different match order,
 * or a handler wired to the wrong type.
 *
 * This test pins the exported preview JSON of every sample. If a refactor
 * changes PMI target collection in any way, the digest changes and the test
 * fails before the change can reach main.
 *
 * The preview export includes the PMI payloads produced by
 * StepPmiPayloadBuilder -> StepPmiTargetBuilder, so it covers the dispatch
 * chains end to end.
 *
 * Golden file: src/test/resources/pmi-golden.txt
 * Regenerate deliberately by deleting that file and re-running this test.
 */
class PmiGoldenTest {

    private static final Path SAMPLES_DIR = Paths.get("samples");
    private static final Path GOLDEN_FILE = Paths.get("src/test/resources/pmi-golden.txt");

    @Test
    @DisplayName("Preview export must be deterministic before it can be pinned")
    void exportShouldBeDeterministic() throws Exception {
        List<Path> samples = sampleFiles();
        if (samples.isEmpty()) {
            fail("No STEP samples found under " + SAMPLES_DIR.toAbsolutePath());
        }
        // Determinism is a precondition for pinning digests: a hash of a
        // non-deterministic output would fail for reasons unrelated to the refactor.
        for (Path sample : samples.subList(0, Math.min(6, samples.size()))) {
            String first = export(sample);
            String second = export(sample);
            assertEquals(
                    digest(first),
                    digest(second),
                    "Preview export is not deterministic for " + sample.getFileName()
                            + " - digests cannot be used as a golden baseline"
            );
        }
    }

    @Test
    @DisplayName("PMI target collection over the sample corpus matches the golden snapshot")
    void pmiTargetsShouldMatchGoldenSnapshot() throws Exception {
        Map<String, String> digests = new TreeMap<>();
        for (Path sample : sampleFiles()) {
            digests.put(sample.getFileName().toString(), digest(export(sample)));
        }

        String rendered = render(digests);

        if (!Files.exists(GOLDEN_FILE)) {
            Files.createDirectories(GOLDEN_FILE.getParent());
            Files.writeString(GOLDEN_FILE, rendered, StandardCharsets.UTF_8);
            System.out.println("Wrote new golden baseline for " + digests.size()
                    + " samples to " + GOLDEN_FILE);
            return;
        }

        // Normalise CRLF: the baseline is rendered with '\n', but git may check the
        // file out with platform line endings. That must not look like a regression.
        String expected = Files.readString(GOLDEN_FILE, StandardCharsets.UTF_8).replace("\r\n", "\n");
        assertEquals(expected, rendered,
                "PMI target collection changed. If the change is intentional, delete "
                        + GOLDEN_FILE + " and re-run to record a new baseline.");
    }

    private static String export(Path sample) throws IOException {
        // STEP files are frequently ISO-8859-1 rather than UTF-8, so read them
        // through the project's decoding helper instead of assuming UTF-8.
        return StepPreviewJsonExporter.export(StepTextReader.read(sample));
    }

    /** Smallest samples first so a regression surfaces quickly. */
    private static List<Path> sampleFiles() throws IOException {
        if (!Files.isDirectory(SAMPLES_DIR)) {
            return List.of();
        }
        List<Path> files = new ArrayList<>();
        try (Stream<Path> stream = Files.list(SAMPLES_DIR)) {
            stream.filter(p -> {
                String n = p.getFileName().toString().toLowerCase();
                return n.endsWith(".step") || n.endsWith(".stp");
            }).forEach(files::add);
        }
        files.sort(Comparator.comparingLong(PmiGoldenTest::sizeOf).thenComparing(p -> p.getFileName().toString()));
        return files;
    }

    private static long sizeOf(Path p) {
        try {
            return Files.size(p);
        } catch (IOException e) {
            return Long.MAX_VALUE;
        }
    }

    private static String render(Map<String, String> digests) {
        StringBuilder sb = new StringBuilder();
        sb.append("# PMI preview-export golden digests (SHA-256 of StepPreviewJsonExporter.export)\n");
        sb.append("# Regenerate by deleting this file and re-running PmiGoldenTest.\n");
        for (Map.Entry<String, String> e : digests.entrySet()) {
            sb.append(e.getKey()).append("  ").append(e.getValue()).append('\n');
        }
        return sb.toString();
    }

    private static String digest(String text) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(Character.forDigit((b >> 4) & 0xf, 16)).append(Character.forDigit(b & 0xf, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }
}
