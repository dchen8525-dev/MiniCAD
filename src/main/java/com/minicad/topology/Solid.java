package com.minicad.topology;

import com.minicad.common.TopologyException;

import java.util.List;

/**
 * Minimal solid wrapping a closed shell.
 *
 * @param outerShell outer closed shell
 * @param voidShells inner closed void shells
 */
public record Solid(Shell outerShell, List<Shell> voidShells) {

    /**
     * Creates a solid from a closed shell.
     */
    public Solid {
        if (outerShell == null) {
            throw new TopologyException("outerShell must not be null");
        }
        if (!outerShell.closed()) {
            throw new TopologyException("solid requires a closed shell");
        }
        if (voidShells == null) {
            throw new TopologyException("voidShells must not be null");
        }
        voidShells = List.copyOf(voidShells);
        for (Shell voidShell : voidShells) {
            if (voidShell == null) {
                throw new TopologyException("voidShells must not contain null");
            }
            if (!voidShell.closed()) {
                throw new TopologyException("solid void shells must be closed");
            }
        }
    }

    public Solid(Shell outerShell) {
        this(outerShell, List.of());
    }
}
