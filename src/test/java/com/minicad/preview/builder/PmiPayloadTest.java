package com.minicad.preview.builder;

import com.minicad.preview.payload.PointPayload;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PmiPayloadTest {

    private static PointPayload pp(double x, double y, double z) {
        return new PointPayload(x, y, z);
    }

    private static PmiTargetPayload target(int id) {
        return new PmiTargetPayload(id, "SHAPE", "name" + id, Arrays.asList("i1"),
                "REL", 11, "USAGE", 12, "DEF", 13);
    }

    private static PmiPayload sample() {
        List<PointPayload> leader = Arrays.asList(pp(0, 0, 0), pp(1, 1, 1));
        List<Integer> targetIds = Arrays.asList(1, 2);
        List<PmiTargetPayload> targets = Arrays.asList(target(10), target(20));
        return new PmiPayload("pmi1", "text1", pp(5, 5, 5), leader, targetIds, targets);
    }

    @Test
    void gettersReturnValues() {
        PmiPayload p = sample();
        assertEquals("pmi1", p.getName());
        assertEquals("text1", p.getText());
        assertEquals("pmi1", p.name());
        assertEquals("text1", p.text());
        assertEquals(pp(5, 5, 5), p.getPosition());
        assertEquals(pp(5, 5, 5), p.position());
        assertEquals(2, p.getLeader().size());
        assertEquals(2, p.leader().size());
        assertEquals(Arrays.asList(1, 2), p.getTargetIds());
        assertEquals(Arrays.asList(1, 2), p.targetIds());
        assertEquals(2, p.getTargets().size());
        assertEquals(2, p.targets().size());
    }

    @Test
    void listsAreDefensiveCopies() {
        List<PointPayload> leader = new ArrayList<>(Arrays.asList(pp(0, 0, 0)));
        List<Integer> targetIds = new ArrayList<>(Arrays.asList(1));
        List<PmiTargetPayload> targets = new ArrayList<>(Arrays.asList(target(10)));
        PmiPayload p = new PmiPayload("a", "b", pp(0, 0, 0), leader, targetIds, targets);
        // mutating the originals must not affect the payload
        leader.clear();
        targetIds.clear();
        targets.clear();
        assertEquals(1, p.getLeader().size());
        assertEquals(1, p.getTargetIds().size());
        assertEquals(1, p.getTargets().size());
    }

    @Test
    void equalsAndHashCode() {
        PmiPayload a = sample();
        PmiPayload b = sample();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        PmiPayload different = new PmiPayload("OTHER", "text1", pp(5, 5, 5),
                Arrays.asList(pp(0, 0, 0), pp(1, 1, 1)), Arrays.asList(1, 2),
                Arrays.asList(target(10), target(20)));
        assertNotEquals(a, different);

        assertFalse(a.equals(null));
        assertFalse(a.equals("not a pmi"));
        assertFalse(a.equals(new Object()));
    }

    @Test
    void toStringContainsClassName() {
        assertTrue(sample().toString().contains("PmiPayload"));
    }

    // ---- PmiTargetPayload ----

    @Test
    void targetGettersReturnValues() {
        PmiTargetPayload t = target(10);
        assertEquals(10, t.getId());
        assertEquals(10, t.id());
        assertEquals("SHAPE", t.getType());
        assertEquals("SHAPE", t.type());
        assertEquals("name10", t.getName());
        assertEquals(Arrays.asList("i1"), t.getInstanceIds());
        assertEquals("REL", t.getViaRelationshipType());
        assertEquals(11, t.getViaRelationshipId());
        assertEquals("USAGE", t.getViaUsageType());
        assertEquals(12, t.getViaUsageId());
        assertEquals("DEF", t.getViaDefinitionType());
        assertEquals(13, t.getViaDefinitionId());
    }

    @Test
    void targetListsAreDefensiveCopies() {
        List<String> instanceIds = new ArrayList<>(Arrays.asList("a"));
        PmiTargetPayload t = new PmiTargetPayload(1, "T", "n", instanceIds,
                "R", 1, "U", 2, "D", 3);
        instanceIds.clear();
        assertEquals(1, t.getInstanceIds().size());
    }

    @Test
    void targetEqualsAndHashCode() {
        PmiTargetPayload a = target(10);
        PmiTargetPayload b = target(10);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        PmiTargetPayload different = new PmiTargetPayload(99, "SHAPE", "name10", Arrays.asList("i1"),
                "REL", 11, "USAGE", 12, "DEF", 13);
        assertNotEquals(a, different);

        assertFalse(a.equals(null));
        assertFalse(a.equals("x"));
    }

    @Test
    void targetToStringContainsClassName() {
        assertNotNull(target(10).toString());
        assertTrue(target(10).toString().contains("PmiTargetPayload"));
    }
}
