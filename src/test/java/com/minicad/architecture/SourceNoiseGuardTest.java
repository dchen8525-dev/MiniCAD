package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Guards the removal of three kinds of text noise that the entity generator left behind: the same
 * javadoc block written twice in a row, an import of a class from the file's own package, and a
 * duplicated import line.
 *
 * <p>The noise was large - 1243 files carried a doubled javadoc (11598 lines), 1235 carried 2421
 * same-package imports of which {@code StepEntity.java} alone held 1097, and four files repeated 33
 * import lines - and none of it could move a byte of code. That is why the cleanup is asserted
 * rather than claimed: every touched file kept its comment-, import- and blank-stripped text
 * byte-identical, so the whole change is a pure deletion.
 *
 * <p>A one-off bulk edit without a guard is a bulk edit that comes back the next time the generator
 * runs, so the three rules are pinned here as counting invariants over the whole Java tree rather
 * than as a list of the files that happened to be dirty. Every offender is collected before the
 * assertion, so a regression names each file instead of only the first one.
 */
class SourceNoiseGuardTest {

    private static final List<Path> ROOTS = List.of(Path.of("src", "main", "java"), Path.of("src", "test", "java"));

    /** Two identical javadoc blocks back to back; the backreference is the whole point. */
    private static final Pattern DOUBLED_JAVADOC =
            Pattern.compile("(/\\*\\*(?:\\n \\*[^\\n]*)*\\n \\*/\\n)\\1");

    private static final Pattern PACKAGE = Pattern.compile("^package ([\\w.]+);", Pattern.MULTILINE);

    @Test
    void noSourceFileRepeatsItsOwnText() throws IOException {
        List<String> doubledJavadoc = new ArrayList<>();
        List<String> ownPackageImports = new ArrayList<>();
        List<String> repeatedImports = new ArrayList<>();
        int files = 0;

        for (Path source : sources()) {
            files++;
            String text = Files.readString(source, StandardCharsets.UTF_8).replace("\r\n", "\n");
            String where = source.toString().replace('\\', '/');

            Matcher doubled = DOUBLED_JAVADOC.matcher(text);
            while (doubled.find()) {
                doubledJavadoc.add(where + " repeats a " + doubled.group(1).split("\n").length
                        + "-line javadoc block");
            }

            Matcher packageLine = PACKAGE.matcher(text);
            assertTrue(packageLine.find(), where + " has no package declaration");
            String own = "import " + packageLine.group(1) + ".";
            Set<String> seen = new HashSet<>();
            int lineNumber = 0;
            for (String line : text.split("\n")) {
                lineNumber++;
                if (!line.startsWith("import ")) {
                    continue;
                }
                if (line.startsWith(own)) {
                    String imported = line.substring(own.length()).replace(";", "");
                    if (!imported.isEmpty() && !imported.contains(".")) {
                        ownPackageImports.add(where + ":" + lineNumber + " imports its own package: " + line);
                    }
                }
                if (!seen.add(line)) {
                    repeatedImports.add(where + ":" + lineNumber + " repeats an import: " + line);
                }
            }
        }

        assertTrue(files > 1000, "the tree should still hold the generated entity layer, found " + files);
        assertEquals(List.of(), doubledJavadoc, "no file may write the same javadoc twice");
        assertEquals(List.of(), ownPackageImports, "no file may import from its own package");
        assertEquals(List.of(), repeatedImports, "no file may list the same import twice");
    }

    @Test
    void theFilesThatCarriedTheNoiseAreClean() throws IOException {
        Set<String> all = new TreeSet<>(sources().stream().map(p -> p.toString().replace('\\', '/')).toList());
        for (String known : List.of(
                "src/main/java/com/minicad/step/model/StepEntity.java",
                "src/main/java/com/minicad/step/model/StepBSplineCurve.java",
                "src/main/java/com/minicad/step/model/StepThreadFeature.java",
                "src/main/java/com/minicad/app/StepDumpApp.java")) {
            assertTrue(all.contains(known), known + " should still exist");
        }

        String entityInterface = read("src/main/java/com/minicad/step/model/StepEntity.java");
        assertEquals(25, entityInterface.split("\n").length,
                "StepEntity.java is the marker interface and nothing else (it was 1124 lines)");
        assertTrue(entityInterface.contains("default int id() { return getId(); }"),
                "the record-style defaults stay on the interface");
        assertEquals(0, entityInterface.lines().filter(L -> L.startsWith("import ")).count(),
                "StepEntity.java needs no import at all");

        String dumpApp = read("src/main/java/com/minicad/app/StepDumpApp.java");
        assertEquals(1, dumpApp.lines().filter(L -> L.equals("import com.minicad.step.model.*;")).count(),
                "StepDumpApp.java listed that wildcard import twice");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    private static List<Path> sources() throws IOException {
        List<Path> found = new ArrayList<>();
        for (Path root : ROOTS) {
            assertTrue(Files.isDirectory(root), root + " must exist");
            try (Stream<Path> stream = Files.walk(root)) {
                stream.filter(p -> p.toString().endsWith(".java")).sorted().forEach(found::add);
            }
        }
        return found;
    }
}
