package com.minicad.preview.statistics;

import com.minicad.export.json.PreviewSerializers;
import com.minicad.export.json.StepBoundsAccumulator;
import com.minicad.helper.ValidationReportHelper;
import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.PointPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence of the bounding box accumulator onto
 * {@link BoundsAccumulator}.
 *
 * <p>The accumulator used to exist twice, in two different shapes: as this
 * top-level class with package-private members, which nothing referenced, and as
 * a {@code public static final} nested class inside
 * {@code export.json.PreviewSerializers}, which is the copy every caller used.
 * The two were byte-for-byte identical apart from the six accessors the nested
 * copy carried and never had called. No existing test could see the pair: the
 * method scanner groups by name and skips nothing, but the shared name resolves
 * to two types, so it treats the occurrences as ordinary references, and
 * {@code scan_dead_methods} answers a question about methods, not types.</p>
 *
 * <p>Nested copies are the shape that hides best, so the guard is mostly about
 * placement rather than arithmetic. It pins:</p>
 * <ul>
 *   <li>exactly one declaration of the type in main sources, and it is this file;</li>
 *   <li>the type is top level - {@code PreviewSerializers} declares no nested
 *       {@code BoundsAccumulator} and is not its declaring class;</li>
 *   <li>the consumers name the type directly: {@code helper.ValidationReportHelper}
 *       takes it as a parameter without reaching through the serializer, and
 *       {@code StepBoundsAccumulator} no longer wraps the copy in a static;</li>
 *   <li>the six accessors are public while the fields are private, which is what
 *       the exporter needs once the type moved out of its package - before the
 *       move it read {@code bounds.minX} straight off the field;</li>
 *   <li>the behaviour survives the move: an empty accumulator still collapses to
 *       the zero box at the origin, and {@code copy()} still yields an independent
 *       accumulator.</li>
 * </ul>
 */
class BoundsAccumulatorConvergenceTest {

    private static final String ACCUMULATOR = "src/main/java/com/minicad/preview/statistics/BoundsAccumulator.java";

    private static final String SERIALIZERS = "src/main/java/com/minicad/export/json/PreviewSerializers.java";

    @Test
    @DisplayName("the accumulator is declared exactly once, in preview.statistics")
    void theAccumulatorShouldBeDeclaredOnce() throws IOException {
        Pattern declaration = Pattern.compile("class\\s+BoundsAccumulator\\b");
        List<String> homes = new ArrayList<>();
        for (Path file : mainSources()) {
            Matcher matcher = declaration.matcher(read(file));
            while (matcher.find()) {
                homes.add(unix(file.toString()));
            }
        }
        assertEquals(List.of(ACCUMULATOR), homes,
                "a second declaration of BoundsAccumulator is free to drift from the shared one, and "
                        + "the dead nested copy inside PreviewSerializers is exactly how the last one "
                        + "survived two convergence passes unnoticed");
    }

    @Test
    @DisplayName("the type is top level, not nested inside the serializer")
    void theAccumulatorShouldNotBeNestedInTheSerializer() throws IOException {
        assertNull(BoundsAccumulator.class.getDeclaringClass(),
                "the accumulator is a value type shared by preview, export and helper code; the "
                        + "nested copy forced helper.ValidationReportHelper to import a serializer to "
                        + "name it");

        for (Class<?> nested : PreviewSerializers.class.getDeclaredClasses()) {
            assertFalse(nested.getSimpleName().equals("BoundsAccumulator"),
                    "PreviewSerializers declared its own BoundsAccumulator again: the nested copy was "
                            + "a verbatim clone of the top-level one and half of its members were dead");
        }

        assertFalse(read(Paths.get(SERIALIZERS)).contains("class BoundsAccumulator"),
                "the serializer must not re-declare the accumulator; the folded copy is what made "
                        + "--dead and the duplicate scanner both report nothing here");
    }

    @Test
    @DisplayName("consumers name the type directly instead of reaching through the serializer")
    void consumersShouldNameTheTypeDirectly() throws Exception {
        Method includePmi = ValidationReportHelper.class
                .getDeclaredMethod("includePmi", BoundsAccumulator.class, List.class);
        assertEquals(BoundsAccumulator.class, includePmi.getParameterTypes()[0],
                "helper code must resolve the accumulator to the value type, not to a serializer member");

        for (Method method : StepBoundsAccumulator.class.getDeclaredMethods()) {
            assertFalse(method.getName().equals("copyBounds"),
                    "StepBoundsAccumulator.copyBounds was a static wrapper around a field copy; the "
                            + "copy is a property of the accumulator (BoundsAccumulator.copy) and a "
                            + "wrapper can only drift from it");
        }

        List<String> stale = new ArrayList<>();
        for (Path file : mainSources()) {
            if (read(file).contains("PreviewSerializers.BoundsAccumulator")) {
                stale.add(unix(file.toString()));
            }
        }
        assertEquals(List.of(), stale,
                "these files still qualify the accumulator through PreviewSerializers: the nested "
                        + "class is gone, so the reference cannot compile and would be a leftover of "
                        + "a half-applied fold");
    }

    @Test
    @DisplayName("the fields are private and the six accessors are public")
    void theAccessorsShouldCarryTheExportPath() throws Exception {
        assertTrue(Modifier.isPublic(BoundsAccumulator.class.getModifiers()), "the type is shared across packages");
        assertTrue(Modifier.isFinal(BoundsAccumulator.class.getModifiers()),
                "the accumulator is a mutable box, not a subclassing point");

        for (Field field : BoundsAccumulator.class.getDeclaredFields()) {
            assertTrue(Modifier.isPrivate(field.getModifiers()),
                    "field " + field.getName() + " is reachable: the accumulator left the exporter's "
                            + "package, so package-private fields silently became unusable and the "
                            + "public accessors are the only supported way in");
        }

        for (String accessor : List.of("minX", "minY", "minZ", "maxX", "maxY", "maxZ")) {
            Method getter = BoundsAccumulator.class.getDeclaredMethod(accessor);
            assertTrue(Modifier.isPublic(getter.getModifiers()), accessor + "() must stay public");
            assertEquals(double.class, getter.getReturnType(), accessor + "() must return a primitive");
        }
    }

    @Test
    @DisplayName("an empty accumulator collapses to the zero box at the origin")
    void anEmptyAccumulatorShouldCollapseToTheOrigin() {
        BoundsAccumulator bounds = new BoundsAccumulator();
        assertTrue(bounds.isEmpty(), "a fresh accumulator holds no point");

        // The accessors expose the sentinels, the collapse happens on the way out.
        // This is why every reader guards on isEmpty() first - StepPreviewJsonExporter
        // subtracts max from min only under that guard, and an unguarded read here
        // would put Infinity into the exported JSON.
        assertEquals(Double.POSITIVE_INFINITY, bounds.minX());
        assertEquals(Double.NEGATIVE_INFINITY, bounds.maxX());

        assertEquals(new PointPayload(0.0, 0.0, 0.0), bounds.toPayload().min());
        assertEquals(new PointPayload(0.0, 0.0, 0.0), bounds.toPayload().max());

        BoundsAccumulator copy = bounds.copy();
        assertTrue(copy.isEmpty(), "copying an empty accumulator must not leak the sentinel bounds");
        assertEquals(new PointPayload(0.0, 0.0, 0.0), copy.toPayload().min());
    }

    @Test
    @DisplayName("include grows the box and the accessors report it")
    void includingPointsShouldGrowTheBox() {
        BoundsAccumulator bounds = new BoundsAccumulator();
        bounds.include(new PointPayload(1.0, 2.0, 3.0));
        bounds.include(new PointPayload(-4.0, 0.5, 6.0));
        bounds.include(new PointPayload(0.0, -7.0, 3.0));

        assertFalse(bounds.isEmpty());
        assertEquals(-4.0, bounds.minX(), 0.0);
        assertEquals(-7.0, bounds.minY(), 0.0);
        assertEquals(3.0, bounds.minZ(), 0.0);
        assertEquals(1.0, bounds.maxX(), 0.0);
        assertEquals(2.0, bounds.maxY(), 0.0);
        assertEquals(6.0, bounds.maxZ(), 0.0);

        BoundsPayload payload = bounds.toPayload();
        assertEquals(new PointPayload(-4.0, -7.0, 3.0), payload.min());
        assertEquals(new PointPayload(1.0, 2.0, 6.0), payload.max());
    }

    @Test
    @DisplayName("copy is independent of the accumulator it came from")
    void copyShouldBeIndependent() {
        BoundsAccumulator bounds = new BoundsAccumulator();
        bounds.include(new PointPayload(1.0, 2.0, 3.0));
        bounds.include(new PointPayload(4.0, 5.0, 6.0));

        BoundsAccumulator copy = bounds.copy();
        assertNotSame(bounds, copy);
        assertEquals(bounds.minX(), copy.minX(), 0.0);
        assertEquals(bounds.maxZ(), copy.maxZ(), 0.0);

        copy.include(new PointPayload(-10.0, -10.0, -10.0));
        assertEquals(1.0, bounds.minX(), 0.0,
                "the copy shares no state with its source; StepPreviewJsonExporter copies the geometry "
                        + "bounds before folding PMI points in, and that step must not move the geometry box");

        // The folded copyBounds fed min and max back through include; the payload
        // round-trip has to reproduce the same box.
        BoundsAccumulator roundTrip = new BoundsAccumulator();
        StepBoundsAccumulator.includeBounds(roundTrip, bounds.toPayload());
        assertEquals(bounds.minX(), roundTrip.minX(), 0.0);
        assertEquals(bounds.maxY(), roundTrip.maxY(), 0.0);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private static List<Path> mainSources() throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get("src/main/java"))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static String unix(String path) {
        return path.replace('\\', '/');
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
