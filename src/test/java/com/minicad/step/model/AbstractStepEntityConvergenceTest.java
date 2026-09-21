package com.minicad.step.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Pins the extraction of the value contract into {@link AbstractStepEntity}.
 *
 * <p>Every concrete entity in this package used to hand-write {@code id}/{@code name} plus the same
 * three value algorithms. 1223 of them now inherit those from the base and declare only their
 * {@code components()} list. This test holds the extraction in place from four directions.
 *
 * <ul>
 *   <li><b>Nothing restates the triple.</b> Every entity that extends the base must not declare
 *       {@code getId}, {@code getName}, {@code equals}, {@code hashCode} or {@code toString}, must
 *       still declare exactly one {@code components()}, and must not still declare the two moved
 *       fields. The count of such entities is asserted exactly, so a new entity cannot quietly opt
 *       out - and any entity outside the base that still writes {@code equals} has to be on the
 *       documented legacy list, which is itself asserted to be exactly the nineteen expected.</li>
 *   <li><b>The retired order is preserved.</b> The single most dangerous part of the fold was
 *       reordering a component: the order is what {@code toString} prints and what {@code hashCode}
 *       folds. The digest below was computed from the retired bodies themselves, read out of
 *       {@code HEAD} before the fold ran, and is re-derived here from the folded
 *       {@code components()} maps - so a reordering anywhere in the package changes it.</li>
 *   <li><b>No field was left out.</b> For every entity, the labels of {@code components()} are the
 *       entity's declared fields, in declaration order. That is the one mistake the fold could
 *       still make (a field added to a constructor but not to the list), and the classes are read
 *       from source rather than reflectively so that a trailing comment on a field declaration -
 *       which is what made one entity lose a component during the fold - cannot hide anything.</li>
 *   <li><b>Behaviour.</b> Reflective round-trips over all of them: two instances built from equal
 *       arguments are equal with equal hashes, instances differing only in the instance id are not
 *       equal, and {@code toString} still renders every component.</li>
 * </ul>
 */
class AbstractStepEntityConvergenceTest {

    private static final String PKG = "com.minicad.step.model";

    /**
     * Entities that keep their own value bodies for now. Eighteen key {@code equals}/{@code hashCode}
     * on the instance id alone and ignore their other fields - a contract they have had since the
     * initial import, shared by no coherent group (two of the twenty-two {@code *Relationship}
     * entities, two of the twenty-unit family, and the whole small A3m/DataEquivalence and
     * GenericExpression groups), which is why they read as residue of an older generator pass rather
     * than a decision. The nineteenth, {@code StepFacetedBrepAndBrepWithVoids}, is a value entity but
     * prints its components with a comma separator. Neither contract can be folded into the base
     * without either changing behaviour or adding a branch to it, and changing what {@code equals}
     * means for eighteen public types is a decision to be taken deliberately, not as a side effect of
     * a deduplication pass.
     */
    private static final Set<String> LEGACY_CONTRACT = Set.of(
            "StepA3mEquivalenceAccuracyAssociation",
            "StepA3mEquivalenceCriterion",
            "StepA3mInspectedModelAndInspectionResultRelationship",
            "StepA3maEquivalenceInspectionResult",
            "StepA3msEquivalenceInspectionResult",
            "StepBinaryGenericExpression",
            "StepDataEquivalenceAssessmentSpecification",
            "StepDataEquivalenceInspectionCriterionReportItem",
            "StepDataEquivalenceInspectionInstanceReportItem",
            "StepDataEquivalenceInspectionRequirement",
            "StepDataEquivalenceReportRequest",
            "StepExternallyDefinedConversionBasedUnit",
            "StepFacetedBrepAndBrepWithVoids",
            "StepGenericEntity",
            "StepMultipleArityGenericExpression",
            "StepNonAgreedUnitUsage",
            "StepRepresentationItemRelationship",
            "StepSimpleGenericExpression",
            "StepUnaryGenericExpression");

    private static final int FOLDED_ENTITIES = 1223;

    /**
     * How many folded entities derive their label instead of storing one - a date and a time rendered
     * as one string, a person's full name built from family and given name, and so on. They have no
     * {@code name} field, so their value contract never saw the label and the fold does not touch
     * their {@code getName()}; the app-level resolver tests cover the values these derivations return.
     */
    private static final int DERIVED_LABEL_ENTITIES = 30;

    /** sha256 over "Entity:label,label,..." for the 1223 entities, taken from HEAD before the fold. */
    private static final String RETIRED_ORDER_DIGEST =
            "0f394c9006b001dd8bc9e2fbe2355ba3048352464b1f46bce69b81de020e42bb";

    private static final Pattern COMPONENT = Pattern.compile("state\\.put\\(\"([A-Za-z0-9_]+)\",");
    private static final Pattern COMPONENTS_DECL =
            Pattern.compile("protected Map<String, Object> components\\(\\) \\{");
    private static final Pattern FIELD =
            Pattern.compile("^    private final [A-Za-z0-9_<>,.\\[\\] ]+? ([A-Za-z0-9_]+);", Pattern.MULTILINE);

    // ------------------------------------------------------------------ the triple lives once

    @Test
    void everyFoldedEntityHandsTheTripleToTheBase() throws Exception {
        List<String> folded = new ArrayList<>();
        List<String> derivedLabels = new ArrayList<>();
        for (Path source : sources()) {
            String name = fileName(source);
            if (!name.startsWith("Step")) {
                continue;
            }
            String code = strip(Files.readString(source, StandardCharsets.UTF_8));
            if (!code.contains("extends AbstractStepEntity")) {
                continue;
            }
            folded.add(name);
            for (String signature : List.of(
                    "public final int getId() {", "public int getId() {",
                    "public boolean equals(Object o)", "public int hashCode()", "public String toString()")) {
                assertFalse(code.contains(signature),
                        name + " must not restate " + signature + " once the base owns it");
            }
            assertFalse(code.contains("private final int id;"),
                    name + " must not redeclare id - the base owns it");
            assertFalse(code.contains("private final String name;"),
                    name + " must not redeclare name - the base owns it");
            assertEquals(1, COMPONENTS_DECL.matcher(code).results().count(),
                    name + " must declare exactly one components()");

            // getName() is the one accessor the base leaves open, and only for an entity that has no
            // label field to return: it derives the label from its other components instead.
            if (code.contains("public String getName()")) {
                assertFalse(components(code, name).contains("name"),
                        name + " stores a label and must not restate getName()");
                derivedLabels.add(name);
            }
        }
        assertEquals(FOLDED_ENTITIES, folded.size(),
                "the set of entities that delegate the value contract to the base changed");
        assertEquals(DERIVED_LABEL_ENTITIES, derivedLabels.size(),
                "the set of entities with a derived label changed: " + derivedLabels);
    }

    @Test
    void noEntityOutsideTheBaseStillWritesItsOwnValueTriple() throws Exception {
        Set<String> legacy = new java.util.TreeSet<>();
        for (Path source : sources()) {
            String name = fileName(source);
            if (!name.startsWith("Step")) {
                continue;
            }
            String code = strip(Files.readString(source, StandardCharsets.UTF_8));
            if (!code.contains("public boolean equals(Object o)")) {
                continue;
            }
            if (code.contains("extends AbstractStepEntity") || code.contains("extends AbstractStepControlPoint")) {
                continue;
            }
            legacy.add(name);
        }
        assertEquals(LEGACY_CONTRACT, legacy,
                "the entities that keep their own equals changed - an entity opted out of the base, "
                        + "or one of the legacy contracts was folded or removed");
    }

    // ------------------------------------------------------------------ the order is preserved

    @Test
    void theComponentOrderIsTheOneTheRetiredBodiesUsed() throws Exception {
        List<String> lines = new ArrayList<>();
        for (Path source : sources()) {
            String name = fileName(source);
            if (LEGACY_CONTRACT.contains(name)) {
                continue;
            }
            String code = strip(Files.readString(source, StandardCharsets.UTF_8));
            if (!code.contains("extends AbstractStepEntity")) {
                continue;
            }
            lines.add(name + ":" + String.join(",", components(code, name)));
        }
        lines.sort(null);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha.digest((String.join("\n", lines) + "\n").getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : digest) {
            hex.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
        }
        assertEquals(RETIRED_ORDER_DIGEST, hex.toString(),
                "a component was added, dropped or reordered: the order is what toString prints and "
                        + "what hashCode folds, so it must stay exactly what the retired bodies used");
    }

    @Test
    void everyDeclaredFieldIsAComponentInDeclarationOrder() throws Exception {
        int checked = 0;
        for (Path source : sources()) {
            String name = fileName(source);
            if (LEGACY_CONTRACT.contains(name)) {
                continue;
            }
            String code = strip(Files.readString(source, StandardCharsets.UTF_8));
            if (!code.contains("extends AbstractStepEntity")) {
                continue;
            }
            List<String> declared = new ArrayList<>();
            Matcher m = FIELD.matcher(code);
            while (m.find()) {
                declared.add(m.group(1));
            }
            List<String> components = components(code, name);
            assertEquals("id", components.get(0), name + " must list id first");
            // id and name moved into the base, so they are no longer declared here - but they are
            // still components, and `name` sits wherever the entity used to declare it. The digest
            // test pins those positions; this one pins that no *own* field was left out.
            List<String> own = new ArrayList<>(components);
            assertTrue(own.remove("id"), name + " must carry id as a component");
            own.remove("name");
            assertEquals(declared, own,
                    name + ": components() must list every declared field, in declaration order");
            checked++;
        }
        assertEquals(FOLDED_ENTITIES, checked, "not every folded entity was inspected");
    }

    // ------------------------------------------------------------------ and it still behaves

    @Test
    void theValueContractStillBehaves() throws Exception {
        int checked = 0;
        int arrayBacked = 0;
        for (Path source : sources()) {
            String name = fileName(source);
            if (LEGACY_CONTRACT.contains(name) || !name.startsWith("Step")) {
                continue;
            }
            String code = strip(Files.readString(source, StandardCharsets.UTF_8));
            if (!code.contains("extends AbstractStepEntity")) {
                continue;
            }
            Class<?> type = Class.forName(PKG + "." + name);
            Constructor<?> primary = primaryConstructor(type);
            assertTrue(Modifier.isFinal(type.getModifiers()), name + " must stay final");

            Object[] args = arguments(primary.getParameterTypes(), 0);
            AbstractStepEntity one = (AbstractStepEntity) primary.newInstance(args);
            AbstractStepEntity same = (AbstractStepEntity) primary.newInstance(arguments(primary.getParameterTypes(), 0));

            assertEquals(one, one, name + " must be equal to itself");
            assertNotEquals(one, null, name + " must not be equal to null");

            Map<String, Object> components = one.components();
            assertFalse(components.isEmpty(), name + " exposes no components");
            for (Map.Entry<String, Object> component : components.entrySet()) {
                assertTrue(one.toString().contains(component.getKey() + "="),
                        name + " toString() dropped the " + component.getKey() + " label");
            }
            assertEquals(name + "{", one.toString().substring(0, name.length() + 1),
                    name + " toString() must be prefixed with its own simple name");
            assertTrue(one.toString().endsWith("}"), name + " toString() must end with a brace");

            boolean arrayComponent = components.values().stream()
                    .anyMatch(v -> v != null && v.getClass().isArray());
            if (arrayComponent) {
                // Arrays keep identity equality - in the retired bodies too, because they went through
                // Objects.equals as well. Two instances built from equal arguments therefore stay two
                // instances; the fold neither caused nor changed this.
                arrayBacked++;
                assertNotEquals(one, same, name + " compares its array component by identity");
            } else {
                assertEquals(one, same, name + " must be equal to an instance built from the same arguments");
                assertEquals(one.hashCode(), same.hashCode(), name + " must hash equal instances equally");
                assertEquals(one.toString(), same.toString(), name + " must print equal instances equally");
            }

            // instances that differ only in the instance id must stay distinct
            Object[] other = arguments(primary.getParameterTypes(), 0);
            other[0] = ((Number) other[0]).intValue() + 1;
            AbstractStepEntity different = (AbstractStepEntity) primary.newInstance(other);
            assertNotEquals(one, different, name + " must separate instances with different ids");
            assertNotEquals(one.hashCode(), different.hashCode(),
                    name + " must hash instances with different ids differently");

            // every constructor argument must reach all three algorithms - this is what fails when a
            // component is left out of components(), or when equals/hashCode/toString stops folding
            // the whole list
            Class<?>[] types = primary.getParameterTypes();
            for (int i = 0; i < types.length; i++) {
                if (Objects.equals(fixture(types[i], 0), fixture(types[i], 1))) {
                    continue;
                }
                Object[] varied = arguments(types, 0);
                varied[i] = fixture(types[i], 1);
                AbstractStepEntity changed = (AbstractStepEntity) primary.newInstance(varied);
                assertNotEquals(one, changed,
                        name + " must see a difference in constructor parameter " + i);
                assertNotEquals(one.hashCode(), changed.hashCode(),
                        name + " must fold constructor parameter " + i + " into hashCode");
                assertNotEquals(one.toString(), changed.toString(),
                        name + " must print constructor parameter " + i + " into toString");
            }
            checked++;
        }
        assertEquals(FOLDED_ENTITIES, checked, "not every folded entity could be round-tripped");
        assertEquals(1, arrayBacked, "the set of entities holding an array component changed");
    }

    // ------------------------------------------------------------------ helpers

    private static List<String> components(String code, String name) {
        Matcher m = COMPONENT.matcher(code);
        List<String> labels = new ArrayList<>();
        while (m.find()) {
            labels.add(m.group(1));
        }
        assertFalse(labels.isEmpty(), name + " declares no components");
        return labels;
    }

    private static Constructor<?> primaryConstructor(Class<?> type) {
        Constructor<?> best = null;
        for (Constructor<?> ctor : type.getConstructors()) {
            if (best == null || ctor.getParameterCount() > best.getParameterCount()) {
                best = ctor;
            }
        }
        assertTrue(best != null && best.getParameterCount() > 0, type.getSimpleName() + " has no constructor");
        return best;
    }

    /** Deterministic values for every parameter type this package uses; variant 1 differs from 0. */
    private static Object[] arguments(Class<?>[] types, int variant) {
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            args[i] = fixture(types[i], variant);
        }
        return args;
    }

    private static Object fixture(Class<?> type, int variant) {
        if (type == int.class || type == Integer.class) {
            return 7 + variant;
        }
        if (type == long.class || type == Long.class) {
            return 7L + variant;
        }
        if (type == double.class || type == Double.class) {
            return 1.5 + variant;
        }
        if (type == float.class || type == Float.class) {
            return 1.5f + variant;
        }
        if (type == boolean.class || type == Boolean.class) {
            return variant == 0;
        }
        if (type == char.class || type == Character.class) {
            return (char) ('a' + variant);
        }
        if (type == String.class) {
            return "s" + variant;
        }
        if (type == Object.class) {
            return "s" + variant;
        }
        if (type == List.class) {
            return List.of("s" + variant);
        }
        if (type == Set.class) {
            return Set.of("s" + variant);
        }
        if (type == Map.class) {
            return Map.of("s", "s" + variant);
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants[Math.min(variant, constants.length - 1)];
        }
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }
        return null;
    }

    private static List<Path> sources() throws IOException {
        Path root = Path.of("src", "main", "java", "com", "minicad", "step", "model");
        assertTrue(Files.isDirectory(root), "the model package must exist");
        try (Stream<Path> stream = Files.list(root)) {
            return stream.filter(p -> p.getFileName().toString().endsWith(".java")).sorted().toList();
        }
    }

    private static String fileName(Path source) {
        return source.getFileName().toString().replace(".java", "");
    }

    private static String strip(String code) {
        return code.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\n]*", "");
    }
}
