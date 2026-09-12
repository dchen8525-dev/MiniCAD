package com.minicad.topology;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.TrimmedCurve3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch behind {@link Edge#isClosedCurve(Curve3)},
 * which used to be an untested five-branch if-chain:
 *
 * <ol>
 *   <li>{@code Circle || Ellipse3} -- always closed;</li>
 *   <li>{@code BSplineCurve3} / {@code RationalBSplineCurve3} -- closed iff
 *       first and last control points coincide;</li>
 *   <li>{@code CompositeCurve3} -- closed iff its segments form a loop;</li>
 *   <li>{@code TrimmedCurve3} -- recurses on its basis curve;</li>
 *   <li>a {@code false} tail for Line3, Polyline3, SurfaceCurve3.</li>
 * </ol>
 *
 * The table order is read back from the host source (a private static field)
 * and compared with the frozen order file under src/test/resources. The
 * behaviour tests below are the first direct coverage this predicate ever had.
 */
class EdgeClosedCurveDispatchTableTest {

    private static final String HOST_SOURCE = "src/main/java/com/minicad/topology/Edge.java";

    @Test
    @DisplayName("isClosedCurve keeps the original branch order")
    void closedCurveTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes(
                Paths.get("src/test/resources/closed-curve-dispatch-order.txt"));
        List<String> actual = liveHandlerTypes("CLOSED_CURVE_RULES");

        assertEquals(expected.size(), actual.size(),
                "Dispatch table CLOSED_CURVE_RULES branch count changed. Expected "
                        + expected.size() + " branches from the original chain, found "
                        + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table CLOSED_CURVE_RULES order/types changed. The table is "
                        + "ordered data, not control flow: the first match wins, so "
                        + "reordering silently changes which handler runs.");
    }

    @Test
    @DisplayName("CLOSED_CURVE_RULES has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        List<String> types = liveHandlerTypes("CLOSED_CURVE_RULES");
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in CLOSED_CURVE_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * A table that nothing iterates is dead weight: the entry point must keep
     * dispatching through its table.
     */
    @Test
    @DisplayName("isClosedCurve dispatches through its table")
    void entryPointShouldIterateItsTable() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        assertTrue(text.contains("for (ClosedCurveRule rule : CLOSED_CURVE_RULES)"),
                "isClosedCurve must iterate CLOSED_CURVE_RULES.");
    }

    @Test
    @DisplayName("circle and ellipse are always closed")
    void analyticCurvesShouldBeClosed() {
        Axis2Placement3D position = new Axis2Placement3D(
                CartesianPoint.origin(), new Direction3(0.0, 0.0, 1.0), new Direction3(1.0, 0.0, 0.0));
        assertTrue(Edge.isClosedCurve(new Circle(position, 3.0)), "circle is closed");
        assertTrue(Edge.isClosedCurve(new Ellipse3(position, 3.0, 2.0)), "ellipse is closed");
    }

    @Test
    @DisplayName("b-splines are closed iff first and last control points coincide")
    void bsplinesShouldCloseOnCoincidentEndpoints() {
        List<CartesianPoint> closedPoints = List.of(
                new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0), new CartesianPoint(0, 0, 0));
        List<CartesianPoint> openPoints = List.of(
                new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0), new CartesianPoint(2, 0, 0));
        List<Integer> multiplicities = List.of(2, 2, 1);
        List<Double> knots = List.of(0.0, 1.0, 2.0);

        assertTrue(Edge.isClosedCurve(new BSplineCurve3(1, closedPoints, multiplicities, knots)),
                "b-spline with coincident end control points is closed");
        assertFalse(Edge.isClosedCurve(new BSplineCurve3(1, openPoints, multiplicities, knots)),
                "b-spline with distinct end control points is open");

        List<Double> weights = List.of(1.0, 1.0, 1.0);
        assertTrue(Edge.isClosedCurve(
                new RationalBSplineCurve3(1, closedPoints, weights, multiplicities, knots)),
                "rational b-spline with coincident end control points is closed");
        assertFalse(Edge.isClosedCurve(
                new RationalBSplineCurve3(1, openPoints, weights, multiplicities, knots)),
                "rational b-spline with distinct end control points is open");
    }

    @Test
    @DisplayName("composite curves are closed iff sampled ends of first/last segment coincide")
    void compositesShouldCloseOnLoopedSegments() {
        // Circle.sample(2) starts and ends at angle 0, so a one-circle composite
        // has coincident first-start / last-end sample points: closed.
        Axis2Placement3D position = new Axis2Placement3D(
                CartesianPoint.origin(), new Direction3(0.0, 0.0, 1.0), new Direction3(1.0, 0.0, 0.0));
        assertTrue(Edge.isClosedCurve(new CompositeCurve3(List.of(new Circle(position, 3.0)))),
                "a composite whose first/last segment samples coincide is closed");

        // Line3.sample(2) walks a fixed +/-10 world-unit window of the infinite
        // line, so its first/last samples never coincide: conservatively open.
        Line3 line = new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0));
        assertFalse(Edge.isClosedCurve(new CompositeCurve3(List.of(line))),
                "a line segment composite is conservatively not closed");
    }

    @Test
    @DisplayName("trimmed curves inherit the closedness of their basis curve")
    void trimmedCurvesShouldInheritBasisClosedness() {
        Axis2Placement3D position = new Axis2Placement3D(
                CartesianPoint.origin(), new Direction3(0.0, 0.0, 1.0), new Direction3(1.0, 0.0, 0.0));
        Circle circle = new Circle(position, 3.0);
        Line3 line = new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0));

        assertTrue(Edge.isClosedCurve(new TrimmedCurve3(circle, 0.0, 2.0 * Math.PI, true)),
                "trimming a closed curve stays closed");
        assertFalse(Edge.isClosedCurve(new TrimmedCurve3(line, 0.0, 1.0, true)),
                "trimming an open curve stays open");
    }

    @Test
    @DisplayName("line, polyline-like and null-family curves are not closed")
    void tailTypesShouldNotBeClosed() {
        assertFalse(Edge.isClosedCurve(new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0))),
                "line is not closed");
    }

    private static List<String> frozenTypes(Path frozenOrder) throws IOException {
        if (!Files.exists(frozenOrder)) {
            fail("Missing frozen dispatch order at " + frozenOrder.toAbsolutePath());
        }
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(frozenOrder, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                types.add(trimmed);
            }
        }
        return types;
    }

    private static List<String> liveHandlerTypes(String tableField) throws Exception {
        if (!Files.exists(Paths.get(HOST_SOURCE))) {
            fail("Cannot read " + HOST_SOURCE + " to verify the dispatch table order.");
        }
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        int field = text.indexOf(tableField + " = List.of(");
        if (field < 0) {
            fail("Cannot find " + tableField + " in " + HOST_SOURCE);
        }
        // The table is assigned as `NAME = List.of(entry, entry, ...)`. Count the
        // `List.of(` opener's own paren as depth 1 so the matching `)` is the
        // List.of closer -- not the first entry's closing paren.
        int listOf = text.indexOf("List.of(", field);
        int paren = listOf + "List.of".length();
        int depth = 1;
        int close = -1;
        for (int i = paren + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    close = i;
                    break;
                }
            }
        }
        if (close < 0) {
            fail("Unbalanced parentheses around " + tableField + " in " + HOST_SOURCE);
        }
        String body = text.substring(listOf, close);
        List<String> types = new ArrayList<>();
        Matcher matcher = Pattern.compile("([A-Za-z0-9_]+)\\.class").matcher(body);
        while (matcher.find()) {
            types.add(matcher.group(1));
        }
        if (types.isEmpty()) {
            fail("No X.class entries found in " + tableField + " at " + HOST_SOURCE);
        }
        return types;
    }
}
