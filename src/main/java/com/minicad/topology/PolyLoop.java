package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.CartesianPoint;

import java.util.List;

/**
 * Loop represented directly by polygon vertices.
 *
 * @param points polygon vertices in order
 */
public record PolyLoop(List<CartesianPoint> points) implements Loop {

    public PolyLoop {
        if (points == null) {
            throw new TopologyException("poly loop points must not be null");
        }
        points = List.copyOf(points);
        if (points.size() < 3) {
            throw new TopologyException("poly loop must contain at least three points");
        }
        for (CartesianPoint point : points) {
            if (point == null) {
                throw new TopologyException("poly loop points must not contain null");
            }
        }
    }
}
