package com.minicad.export.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Guards the export against JVM-run-dependent JSON key order.
 *
 * Map.of(...) returns a JDK immutable map whose iteration order is randomised
 * on every JVM run (java.util.ImmutableCollections#SALT32L). Serialising one
 * directly produces a different but equivalent document each run, which makes
 * any digest pinned over the export - such as PmiGoldenTest - flaky for reasons
 * that have nothing to do with the code under test.
 *
 * PreviewSerializers.appendJsonValue neutralises that by sorting the entries of
 * immutable maps. This test pins the behaviour so the safety net cannot be
 * removed silently.
 */
class PreviewJsonDeterminismTest {

    @Test
    @DisplayName("Immutable maps serialise in a JVM-run-independent key order")
    void immutableMapsSerializeInStableKeyOrder() {
        StringBuilder sb = new StringBuilder();
        PreviewSerializers.appendJsonValue(sb, Map.of("c", 3, "a", 1, "b", 2));
        assertEquals("{\"a\":1,\"b\":2,\"c\":3}", sb.toString(),
                "Map.of() must be sorted before it is written, otherwise the exported "
                        + "JSON changes key order on every JVM run and breaks golden digests");
    }

    @Test
    @DisplayName("Insertion-ordered maps keep their intended field order")
    void insertionOrderedMapsKeepTheirOrder() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("stepId", 7);
        map.put("type", "circle");

        StringBuilder sb = new StringBuilder();
        PreviewSerializers.appendJsonValue(sb, map);
        assertEquals("{\"stepId\":7,\"type\":\"circle\"}", sb.toString(),
                "LinkedHashMap field order must survive serialization untouched");
    }
}
