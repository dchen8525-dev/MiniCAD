package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MAKE_FROM_USAGE_OPTION.
 * A manufacturing usage option.
 */
/**
 * Resolved MAKE_FROM_USAGE_OPTION.
 * A manufacturing usage option.
 */
public final class StepMakeFromUsageOption implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity usage;

    public StepMakeFromUsageOption(int id, String name, String description, StepEntity usage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getUsage() {
        return usage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMakeFromUsageOption that = (StepMakeFromUsageOption) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(usage, that.usage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, usage);
    }

    @Override
    public String toString() {
        return "StepMakeFromUsageOption{" + "id=" + id + "name=" + name + "description=" + description + "usage=" + usage + "}";
    }
}
