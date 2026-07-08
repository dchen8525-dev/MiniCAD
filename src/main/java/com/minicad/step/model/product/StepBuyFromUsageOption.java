package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved BUY_FROM_USAGE_OPTION.
 */
/**
 * Resolved BUY_FROM_USAGE_OPTION.
 */
public final class StepBuyFromUsageOption implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity supplier;

    public StepBuyFromUsageOption(int id, String name, StepEntity supplier) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSupplier() {
        return supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBuyFromUsageOption that = (StepBuyFromUsageOption) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, supplier);
    }

    @Override
    public String toString() {
        return "StepBuyFromUsageOption{" + "id=" + id + "name=" + name + "supplier=" + supplier + "}";
    }
}
