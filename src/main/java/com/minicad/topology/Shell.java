package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;

import java.util.List;
import java.util.Objects;

/**
 * Minimal shell made of planar faces.
 *
 * @param faces faces of the shell
 * @param closed whether the shell is declared closed
 */
/**
 * Minimal shell made of planar faces.
 *
 * @param faces faces of the shell
 * @param closed whether the shell is declared closed
 */
public final class Shell {
    private final List<Face> faces;
    private final boolean closed;

    public Shell(List<Face> faces, boolean closed) {
        this.faces = faces == null ? null : java.util.List.copyOf(faces);
        this.closed = closed;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public boolean isClosed() {
        return closed;
    }

    // Record-style accessors
    public List<Face> faces() { return getFaces(); }
    public boolean closed() { return isClosed(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shell that = (Shell) o;
        return Objects.equals(faces, that.faces) && closed == that.closed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(faces, closed);
    }

    @Override
    public String toString() {
        return "Shell{" + "faces=" + faces + "closed=" + closed + "}";
    }
}
