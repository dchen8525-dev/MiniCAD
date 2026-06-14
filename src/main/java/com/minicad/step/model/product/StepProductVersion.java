package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PRODUCT_VERSION.
 * A product version entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionId version identifier
 * @param description version description
 * @param product relating product
 * @param versionContext version context information
 */
/**
 * Resolved PRODUCT_VERSION.
 * A product version entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionId version identifier
 * @param description version description
 * @param product relating product
 * @param versionContext version context information
 */
public final class StepProductVersion implements StepEntity {
    private final int id;
    private final String name;
    private final String versionId;
    private final String description;
    private final StepEntity product;
    private final StepEntity versionContext;

    public StepProductVersion(int id, String name, String versionId, String description, StepEntity product, StepEntity versionContext) {
        this.id = id;
        this.name = name;
        this.versionId = versionId;
        this.description = description;
        this.product = product;
        this.versionContext = versionContext;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getProduct() {
        return product;
    }

    public StepEntity getVersionContext() {
        return versionContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductVersion that = (StepProductVersion) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(versionId, that.versionId) && Objects.equals(description, that.description) && Objects.equals(product, that.product) && Objects.equals(versionContext, that.versionContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, versionId, description, product, versionContext);
    }

    @Override
    public String toString() {
        return "StepProductVersion{" + "id=" + id + "name=" + name + "versionId=" + versionId + "description=" + description + "product=" + product + "versionContext=" + versionContext + "}";
    }
}