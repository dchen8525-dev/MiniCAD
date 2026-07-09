package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DATUM_SYSTEM_REFERENCE.
 * A datum system reference entity.
 *
 * @param id STEP instance id
 * @param name datum system name
 * @param datumSystem the datum system being referenced
 * @param precedenceLevel precedence level in the datum system
 */
/**
 * Resolved DATUM_SYSTEM_REFERENCE.
 * A datum system reference entity.
 *
 * @param id STEP instance id
 * @param name datum system name
 * @param datumSystem the datum system being referenced
 * @param precedenceLevel precedence level in the datum system
 */
public final class StepDatumSystemReference implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity datumSystem;
    private final int precedenceLevel;

    public StepDatumSystemReference(int id, String name, StepEntity datumSystem, int precedenceLevel) {
        this.id = id;
        this.name = name;
        this.datumSystem = datumSystem;
        this.precedenceLevel = precedenceLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getDatumSystem() {
        return datumSystem;
    }

    public int getPrecedenceLevel() {
        return precedenceLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumSystemReference that = (StepDatumSystemReference) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(datumSystem, that.datumSystem) && precedenceLevel == that.precedenceLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, datumSystem, precedenceLevel);
    }

    @Override
    public String toString() {
        return "StepDatumSystemReference{" + "id=" + id + "name=" + name + "datumSystem=" + datumSystem + "precedenceLevel=" + precedenceLevel + "}";
    }
}