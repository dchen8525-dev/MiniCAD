package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pins the lookup behaviour and the two guard messages that the {@code StepCad*}
 * builders now share through {@link StepCadEntityLookup} instead of carrying a
 * copy each.
 */
class StepCadEntityLookupTest {

    private static Map<Integer, StepEntity> entities() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        entitiesById.put(1, new StepCartesianPoint(1, "origin", Arrays.asList(1.0, 2.0, 3.0)));
        return entitiesById;
    }

    @Test
    void returnsTheStoredEntity() {
        Map<Integer, StepEntity> entitiesById = entities();
        assertSame(entitiesById.get(1), StepCadEntityLookup.requireExisting(entitiesById, 1));
    }

    @Test
    void missingEntityKeepsTheOriginalMessage() {
        StepResolutionException thrown = assertThrows(StepResolutionException.class,
                () -> StepCadEntityLookup.requireExisting(entities(), 99));
        assertEquals("missing resolved entity #99", thrown.getMessage());
    }

    @Test
    void requireCastsWhenTheTypeMatches() {
        StepEntity entity = StepCadEntityLookup.require(entities(), 1, StepCartesianPoint.class, "CARTESIAN_POINT");
        assertEquals(StepCartesianPoint.class, entity.getClass());
    }

    @Test
    void wrongTypeKeepsTheOriginalMessage() {
        StepResolutionException thrown = assertThrows(StepResolutionException.class,
                () -> StepCadEntityLookup.require(entities(), 1, StepDirection.class, "DIRECTION"));
        assertEquals("entity #1 is not a DIRECTION", thrown.getMessage());
    }

    @Test
    void requireReportsAMissingEntityAsMissing() {
        StepResolutionException thrown = assertThrows(StepResolutionException.class,
                () -> StepCadEntityLookup.require(entities(), 99, StepCartesianPoint.class, "CARTESIAN_POINT"));
        assertEquals("missing resolved entity #99", thrown.getMessage());
    }
}
