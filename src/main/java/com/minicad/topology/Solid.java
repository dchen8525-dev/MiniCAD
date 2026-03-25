package com.minicad.topology;

import com.minicad.common.TopologyException;

/**
 * Minimal solid wrapping a closed shell.
 *
 * @param outerShell outer closed shell
 */
public record Solid(Shell outerShell) {

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
    }
}
