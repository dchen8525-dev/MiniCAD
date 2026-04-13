package com.minicad.topology;

/**
 * Marker interface for topological loop subtypes.
 */
public sealed interface Loop permits EdgeLoop, VertexLoop, PolyLoop {
}
