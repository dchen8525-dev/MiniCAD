package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.StepEntity;

import java.util.Map;

/**
 * Shared id → entity lookup for the {@code StepCad*} builders.
 *
 * <p>Every builder in the family is constructed with the same resolved
 * {@code entitiesById} map, so the "missing entity" / "wrong type" guards were
 * copied verbatim into each of them. Keeping one copy here makes the two
 * exception messages impossible to drift apart.</p>
 *
 * <p>Bodies are lifted verbatim from {@link StepCadBuilder}; the delegating
 * entry points keep their original signatures so call sites are unaffected.</p>
 */
final class StepCadEntityLookup {

    private StepCadEntityLookup() {
    }

    static StepEntity requireExisting(Map<Integer, StepEntity> entitiesById, int id) {
        StepEntity entity = entitiesById.get(id);
        if (entity == null) {
            throw new StepResolutionException("missing resolved entity #" + id);
        }
        return entity;
    }

    static <T extends StepEntity> T require(
            Map<Integer, StepEntity> entitiesById, int id, Class<T> type, String expectedName) {
        StepEntity entity = requireExisting(entitiesById, id);
        if (!type.isInstance(entity)) {
            throw new StepResolutionException("entity #" + id + " is not a " + expectedName);
        }
        return type.cast(entity);
    }
}
