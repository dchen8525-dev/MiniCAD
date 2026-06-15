package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;

import java.util.List;
import java.util.Objects;

/**
 * Minimal solid wrapping a closed shell.
 *
 * @param outerShell outer closed shell
 * @param voidShells inner closed void shells
 */
/**
 * Minimal solid wrapping a closed shell.
 *
 * @param outerShell outer closed shell
 * @param voidShells inner closed void shells
 */
public final class Solid {
    private final Shell outerShell;
    private final List<Shell> voidShells;

    public Solid(Shell outerShell, List<Shell> voidShells) {
        this.outerShell = outerShell;
        this.voidShells = voidShells == null ? null : java.util.List.copyOf(voidShells);
    }

    public Solid(Shell outerShell) {
        this(outerShell, List.of());
    }

    public Shell getOuterShell() {
        return outerShell;
    }

    public List<Shell> getVoidShells() {
        return voidShells;
    }

    // Record-style accessors
    public Shell outerShell() { return getOuterShell(); }
    public List<Shell> voidShells() { return getVoidShells(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solid that = (Solid) o;
        return Objects.equals(outerShell, that.outerShell) && Objects.equals(voidShells, that.voidShells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outerShell, voidShells);
    }

    @Override
    public String toString() {
        return "Solid{" + "outerShell=" + outerShell + "voidShells=" + voidShells + "}";
    }
}
