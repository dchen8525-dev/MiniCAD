package com.minicad.step.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Shared core of the resolved STEP entities in this package.
 *
 * <p>Each concrete entity used to declare, assign and expose its own {@code id} and {@code name},
 * and then hand-write the same three value algorithms over its whole field list: {@code equals}
 * chaining one {@code Objects.equals} per field, {@code hashCode} folding the same fields into
 * {@code Objects.hash}, and {@code toString} printing the same pairs. That is three bodies per
 * entity, about three and a half thousand of them across the package, all of them the same
 * algorithm with a different field list. This type owns the algorithms once; an entity keeps only
 * the one thing that is actually its own - the ordered list of its components.
 *
 * <p>{@link #components()} is that list: label to value, in declaration order, {@code id} first and
 * {@code name} second when present. The order is load-bearing twice over - it is what
 * {@code toString} prints and what {@code hashCode} folds - so it is pinned per entity against the
 * field order the retired bodies used (see {@code AbstractStepEntityConvergenceTest}). Adding a
 * field to a constructor without adding it here is therefore the one mistake that stays possible,
 * and the same test fails loudly when it happens.
 *
 * <p>The three algorithms are {@code final}: an entity that restates one is reintroducing the
 * duplication this type exists to remove.
 *
 * <p>The two control-point cores, {@link AbstractStepControlPointCurve} and
 * {@link AbstractStepControlPointSurface}, extend this type too. They were where the same contract
 * had been written a second and a third time - byte-identical copies of the three algorithms and of
 * the component cache, one per core - so they now carry only their geometry and inherit the rest
 * from here.
 *
 * <p>Not every entity in the package extends this type yet. The classes that key equality on the
 * instance id alone, and the one whose {@code toString} uses a different separator, still carry
 * their own bodies; they are listed, with the reason, in the convergence test.
 */
public abstract class AbstractStepEntity implements StepEntity {
    private final int id;
    private final String name;

    protected AbstractStepEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public final int getId() {
        return id;
    }

    /**
     * The stored STEP label.
     *
     * <p>Not final on purpose: a handful of entities have no label field at all and derive one from
     * their other components - a calendar date and a local time rendered as one string, a person's
     * full name from family and given name. Those keep their own derivation. What they must not do
     * is read the field this method returns, because they do not have one; and an entity that does
     * have a label must not restate this method, which the convergence test enforces.
     *
     * @return the label, or the empty string when the entity has none
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * This entity's state, as the value contract sees it: label to value, in the order the retired
     * hand-written {@code toString} printed the pairs and the order the retired {@code hashCode}
     * folded them.
     *
     * @return an ordered map of component label to value
     */
    protected abstract Map<String, Object> components();

    private Map<String, Object> componentCache;

    private Map<String, Object> componentMap() {
        if (componentCache == null) {
            // The entity is immutable, so the map is built at most once and then answers every
            // equals/hashCode/toString call - which is cheaper than the boxing the retired bodies
            // did on each call.
            componentCache = components();
        }
        return componentCache;
    }

    private List<Object> componentValues() {
        return new ArrayList<>(componentMap().values());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return componentValues().equals(((AbstractStepEntity) o).componentValues());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(componentValues().toArray());
    }

    @Override
    public final String toString() {
        StringBuilder text = new StringBuilder(getClass().getSimpleName()).append('{');
        componentMap().forEach((label, value) -> text.append(label).append('=').append(value));
        return text.append('}').toString();
    }
}
