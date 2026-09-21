package com.minicad.step.model;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Guards the shared core of the control-point curve and surface entities.
 *
 * <p>Twenty resolved entities - the B-spline, Bezier, uniform, quasi-uniform and piecewise-Bezier
 * curves and surfaces, plus the converter's 2D and rational variants - each declared, null-checked,
 * copied and exposed the same core state themselves. On the curve side that is {@code id, name,
 * degree, controlPoints, curveForm}; on the surface side {@code id, name, uDegree, vDegree,
 * controlPoints, surfaceForm}. The declarations were byte for byte identical, including the
 * {@code List.copyOf} guard and the {@code toString}/{@code equals}/{@code hashCode} bodies.
 *
 * <p>{@link AbstractStepControlPointCurve} and {@link AbstractStepControlPointSurface} now own that
 * half once. The important part is what the change does <em>not</em> do: it does not make the twenty
 * entities one family. Every dispatch table and every {@code instanceof} chain in the tree keys on
 * the concrete entity class, so a core type that anything branched on would silently widen a branch
 * (a rational curve matching a plain-curve rule) - the exact failure mode that forced the same
 * convergence in {@code geometry} to use composition instead of inheritance. The cores are therefore
 * only ever named in their own declaration and in the twenty {@code extends} clauses, and this pins
 * that:
 *
 * <ul>
 *   <li><b>Who extends what.</b> The set of files naming each core after {@code extends} is exactly
 *       the expected twelve / eight, and outside the core's own file no other file names it except
 *       to extend it - so no {@code instanceof}, no {@code .class} key and no cast can exist.</li>
 *   <li><b>The shared half is gone from the subclasses.</b> No entity declares a core field or a
 *       core accessor (getter or record-style alias) any more, and every one of them still is a
 *       {@code StepEntity} and still {@code final}.</li>
 *   <li><b>The value contract is written once, in the cores.</b> {@code equals}, {@code hashCode}
 *       and {@code toString} were three more algorithms written out per entity. They are now final
 *       on the cores and derive from one {@code components()} map per entity: this pins that no
 *       entity restates them, that the component list is the retired order, that it covers every
 *       field the entity declares, and - via {@link #RETIRED_VALUES} - that the pair
 *       {@code hashCode|toString} still equals what the deleted bodies produced.</li>
 *   <li><b>Behaviour.</b> Reflective round-trips over all twenty: every core accessor returns what
 *       the constructor was given, the control-point list is copied and immutable, {@code null} stays
 *       {@code null}, {@code toString} still prints every core field in order, and {@code equals} /
 *       {@code hashCode} still separate instances that differ in a single core field.</li>
 * </ul>
 */
class ControlPointEntityCoreConvergenceTest {

    private static final String PKG = "com.minicad.step.model.";

    private static final List<String> CURVES = List.of(
            "StepBSplineCurve",
            "StepBSplineCurve2D",
            "StepBSplineCurveWithKnots",
            "StepBSplineCurveWithKnotsAndBreakpoints",
            "StepBezierCurve",
            "StepPiecewiseBezierCurve",
            "StepQuasiUniformCurve",
            "StepQuasiUniformCurve2D",
            "StepRationalBSplineCurve",
            "StepRationalBSplineCurve2D",
            "StepUniformCurve",
            "StepUniformCurve2D");

    private static final List<String> SURFACES = List.of(
            "StepBSplineSurface",
            "StepBSplineSurfaceWithKnots",
            "StepBSplineSurfaceWithKnotsAndBreakpoints",
            "StepBezierSurface",
            "StepPiecewiseBezierSurface",
            "StepQuasiUniformSurface",
            "StepRationalBSplineSurface",
            "StepUniformSurface");

    private static final String CURVE_CORE = "AbstractStepControlPointCurve";
    private static final String SURFACE_CORE = "AbstractStepControlPointSurface";

    /** Core fields, in the order the core constructor takes them. */
    private static final List<String> CURVE_CORE_FIELDS =
            List.of("id", "name", "degree", "controlPoints", "curveForm");
    private static final List<String> SURFACE_CORE_FIELDS =
            List.of("id", "name", "uDegree", "vDegree", "controlPoints", "surfaceForm");

    /** Core accessors: the getter plus the record-style alias of each core field. */
    private static final List<String> CORE_METHODS = List.of(
            "getId", "getName", "getDegree", "getControlPoints", "getCurveForm",
            "id", "name", "degree", "controlPoints", "curveForm",
            "getUDegree", "getVDegree", "getSurfaceForm",
            "uDegree", "vDegree", "surfaceForm");

    /**
     * Construction order of every entity's public constructor, as constructor-parameter labels. The
     * labels are this test's own vocabulary ({@code grid} is the surface control-point grid); they
     * only have to line up with {@link #typeOf} and with the fixture below.
     */
    private static final Map<String, String> CTOR_ORDER = Map.ofEntries(
            entry("StepBSplineCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepBSplineCurve2D", "id,name,degree,controlPoints,curveForm"),
            entry("StepBSplineCurveWithKnots",
                    "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect,"
                            + "knotMultiplicities,knots,knotSpec"),
            entry("StepBSplineCurveWithKnotsAndBreakpoints",
                    "id,name,degree,controlPoints,knotMultiplicities,knots,breakpoints,curveForm,"
                            + "closedCurve,selfIntersect"),
            entry("StepBezierCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepPiecewiseBezierCurve",
                    "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepQuasiUniformCurve",
                    "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepQuasiUniformCurve2D", "id,name,degree,controlPoints,curveForm"),
            entry("StepRationalBSplineCurve",
                    "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect,weightsData,"
                            + "knotMultiplicities,knots,knotSpec"),
            entry("StepRationalBSplineCurve2D", "id,name,degree,controlPoints,weights,curveForm"),
            entry("StepUniformCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepUniformCurve2D", "id,name,degree,controlPoints,curveForm"),
            entry("StepBSplineSurface", "id,name,uDegree,vDegree,grid,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepBSplineSurfaceWithKnots",
                    "id,name,uDegree,vDegree,grid,surfaceForm,uClosed,vClosed,selfIntersect,"
                            + "uMultiplicities,vMultiplicities,uKnots,vKnots,knotSpec"),
            entry("StepBSplineSurfaceWithKnotsAndBreakpoints",
                    "id,name,uDegree,vDegree,grid,uKnotMultiplicities,vKnotMultiplicities,uKnots,vKnots,"
                            + "uBreakpoints,vBreakpoints,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepBezierSurface", "id,name,uDegree,vDegree,grid,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepPiecewiseBezierSurface",
                    "id,name,uDegree,vDegree,grid,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepQuasiUniformSurface",
                    "id,name,uDegree,vDegree,grid,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepRationalBSplineSurface",
                    "id,name,uDegree,vDegree,grid,surfaceForm,uClosed,vClosed,selfIntersect,weightsData,"
                            + "uMultiplicities,vMultiplicities,uKnots,vKnots,knotSpec"),
            entry("StepUniformSurface", "id,name,uDegree,vDegree,grid,surfaceForm,uClosed,vClosed,selfIntersect"));

    // ------------------------------------------------------------------ structure

    @Test
    void theTwentyEntitiesExtendTheCoreAndNothingElseDoes() throws IOException {
        List<String> all = new ArrayList<>();
        all.addAll(CURVES);
        all.addAll(SURFACES);
        assertEquals(20, all.size(), "the family is exactly twenty entities");
        assertEquals(20, all.stream().distinct().count(), "no entity is listed twice");

        TreeSet<String> curveExtenders = sourceExtenders(CURVE_CORE);
        assertEquals(new TreeSet<>(CURVES), curveExtenders, "who extends the curve core");
        TreeSet<String> surfaceExtenders = sourceExtenders(SURFACE_CORE);
        assertEquals(new TreeSet<>(SURFACES), surfaceExtenders, "who extends the surface core");

        // Outside the core's own file - where it names itself in its declaration, its constructor
        // and its self-cast - the name may appear exactly once per subclass and only inside an
        // `extends` clause. Any other mention is a use, and a use is a widened branch.
        assertNamedOnlyToExtend(CURVE_CORE, curveExtenders);
        assertNamedOnlyToExtend(SURFACE_CORE, surfaceExtenders);
    }

    @Test
    void theCoresAreAbstractAndAssignableToStepEntity() throws Exception {
        for (String core : List.of(CURVE_CORE, SURFACE_CORE)) {
            Class<?> type = Class.forName(PKG + core);
            assertTrue(Modifier.isAbstract(type.getModifiers()), core + " must be abstract");
            assertTrue(StepEntity.class.isAssignableFrom(type), core + " must be a StepEntity");
            List<Constructor<?>> ctors = List.of(type.getDeclaredConstructors());
            assertEquals(1, ctors.size(), core + " must have exactly one constructor");
            assertFalse(Modifier.isPublic(ctors.get(0).getModifiers()),
                    core + " must not be constructible from outside the package");
        }
        assertNull(Class.forName(PKG + CURVE_CORE).getSuperclass().getSuperclass(),
                "curve core must sit directly under Object");
    }

    @Test
    void noEntityDeclaresTheSharedHalfAnyMore() throws Exception {
        for (String name : everyEntity()) {
            Class<?> type = Class.forName(PKG + name);
            List<String> coreFields = CURVES.contains(name) ? CURVE_CORE_FIELDS : SURFACE_CORE_FIELDS;
            Class<?> core = CURVES.contains(name) ? Class.forName(PKG + CURVE_CORE)
                    : Class.forName(PKG + SURFACE_CORE);

            assertEquals(core, type.getSuperclass(), name + " must extend its core directly");
            assertTrue(Modifier.isFinal(type.getModifiers()), name + " must stay final - no new family");
            assertTrue(StepEntity.class.isAssignableFrom(type), name + " must stay a StepEntity");

            for (Field field : type.getDeclaredFields()) {
                assertFalse(coreFields.contains(field.getName()),
                        name + " re-declares the core field " + field.getName());
            }
            for (Method method : type.getDeclaredMethods()) {
                if (method.isSynthetic() || method.getName().startsWith("$")) {
                    continue;
                }
                assertFalse(CORE_METHODS.contains(method.getName()),
                        name + " re-declares the core accessor " + method.getName());
            }
        }
    }

    // ------------------------------------------------------------------ behaviour

    @Test
    void everyCoreAccessorReturnsWhatTheConstructorWasGiven() throws Exception {
        for (String name : everyEntity()) {
            Map<String, Object> values = fixture(name);
            Object entity = construct(name, values, false);
            for (String field : coreFieldsOf(name)) {
                assertEquals(coreArgument(values, name, field), coreValue(entity, field),
                        name + "." + getterOf(field) + "()");
            }
            assertEquals(7, ((StepEntity) entity).id(), name + " inherited id() default");
            assertEquals("entity-7", ((StepEntity) entity).name(), name + " inherited name() default");
        }
    }

    @Test
    void theControlPointListIsCopiedAndNullStaysNull() throws Exception {
        for (String name : everyEntity()) {
            Map<String, Object> values = fixture(name);
            Object entity = construct(name, values, false);
            @SuppressWarnings("unchecked")
            List<Object> handedIn = (List<Object>) coreArgument(values, name, "controlPoints");
            List<?> before = new ArrayList<>(handedIn);
            assertEquals(before, coreValue(entity, "controlPoints"), name + " must copy its control points");
            handedIn.clear();
            assertEquals(before, coreValue(entity, "controlPoints"),
                    name + " must not alias the caller's list");
            @SuppressWarnings("unchecked")
            List<Object> exposed = (List<Object>) coreValue(entity, "controlPoints");
            assertThrows(UnsupportedOperationException.class, () -> exposed.add(new Object()),
                    name + " must expose an immutable control-point list");

            Object withNull = construct(name, fixture(name), true);
            assertNull(coreValue(withNull, "controlPoints"), name + " must keep a null control-point list");
        }
    }

    @Test
    void equalsHashCodeAndToStringStillSeeEveryCoreField() throws Exception {
        for (String name : everyEntity()) {
            Map<String, Object> values = fixture(name);
            Object first = construct(name, values, false);
            Object same = construct(name, fixture(name), false);
            assertEquals(first, same, name + " equal fixtures must stay equal");
            assertEquals(first.hashCode(), same.hashCode(), name + " equal fixtures must hash alike");
            assertEquals(first, first, name + " must stay reflexive");
            assertNotEquals(first, "not-an-entity", name + " must not equal a foreign object");

            Object secondId = construct(name, "id", 8);
            assertNotEquals(first, secondId, name + " must notice a changed id");
            Object secondDegree = construct(name, CURVES.contains(name) ? "degree" : "uDegree", 9);
            assertNotEquals(first, secondDegree, name + " must notice a changed degree");
            Object secondForm = construct(name, CURVES.contains(name) ? "curveForm" : "surfaceForm", "OTHER");
            assertNotEquals(first, secondForm, name + " must notice a changed form");

            String text = first.toString();
            assertTrue(text.startsWith(name + "{"), name + ".toString() must open with the class name");
            for (String field : coreFieldsOf(name)) {
                assertTrue(text.contains(field + "=" + coreArgument(values, name, field)),
                        name + ".toString() must print " + field);
            }
        }
    }

    @Test
    void entitiesOfDifferentKindsNeverCompareEqual() throws Exception {
        Object curve = construct("StepUniformCurve", fixture("StepUniformCurve"), false);
        Object surface = construct("StepUniformSurface", fixture("StepUniformSurface"), false);
        assertNotEquals(curve, surface, "a curve and a surface must not compare equal");
        assertNotEquals(curve.hashCode(), surface.hashCode(), "and must not collide here");
    }

    private static final Map<String, String> COMPONENT_ORDER = Map.ofEntries(
            entry("StepBSplineCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepBSplineCurve2D", "id,name,degree,controlPoints,curveForm"),
            entry("StepBSplineCurveWithKnots", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect,knotMultiplicities,knots,knotSpec"),
            entry("StepBSplineCurveWithKnotsAndBreakpoints", "id,name,degree,controlPoints,knotMultiplicities,knots,breakpoints,curveForm,closedCurve,selfIntersect"),
            entry("StepBezierCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepPiecewiseBezierCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepQuasiUniformCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepQuasiUniformCurve2D", "id,name,degree,controlPoints,curveForm"),
            entry("StepRationalBSplineCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect,weightsData,knotMultiplicities,knots,knotSpec"),
            entry("StepRationalBSplineCurve2D", "id,name,degree,controlPoints,weights,curveForm"),
            entry("StepUniformCurve", "id,name,degree,controlPoints,curveForm,closedCurve,selfIntersect"),
            entry("StepUniformCurve2D", "id,name,degree,controlPoints,curveForm"),
            entry("StepBSplineSurface", "id,name,uDegree,vDegree,controlPoints,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepBSplineSurfaceWithKnots", "id,name,uDegree,vDegree,controlPoints,surfaceForm,uClosed,vClosed,selfIntersect,uMultiplicities,vMultiplicities,uKnots,vKnots,knotSpec"),
            entry("StepBSplineSurfaceWithKnotsAndBreakpoints", "id,name,uDegree,vDegree,controlPoints,uKnotMultiplicities,vKnotMultiplicities,uKnots,vKnots,uBreakpoints,vBreakpoints,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepBezierSurface", "id,name,uDegree,vDegree,controlPoints,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepPiecewiseBezierSurface", "id,name,uDegree,vDegree,controlPoints,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepQuasiUniformSurface", "id,name,uDegree,vDegree,controlPoints,surfaceForm,uClosed,vClosed,selfIntersect"),
            entry("StepRationalBSplineSurface", "id,name,uDegree,vDegree,controlPoints,surfaceForm,uClosed,vClosed,selfIntersect,weightsData,uMultiplicities,vMultiplicities,uKnots,vKnots,knotSpec"),
            entry("StepUniformSurface", "id,name,uDegree,vDegree,controlPoints,surfaceForm,uClosed,vClosed,selfIntersect")
            );

    private static final Map<String, String> RETIRED_VALUES = Map.ofEntries(
            entry("StepBSplineCurve", "-959215823|StepBSplineCurve{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7closedCurve=trueselfIntersect=false}"),
            entry("StepBSplineCurve2D", "615760907|StepBSplineCurve2D{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7}"),
            entry("StepBSplineCurveWithKnots", "-1822652325|StepBSplineCurveWithKnots{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7closedCurve=trueselfIntersect=falseknotMultiplicities=[1, 2, 3]knots=[0.0, 0.25, 1.0]knotSpec=knot-spec-7}"),
            entry("StepBSplineCurveWithKnotsAndBreakpoints", "1773734642|StepBSplineCurveWithKnotsAndBreakpoints{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]knotMultiplicities=[1, 2, 3]knots=[0.0, 0.25, 1.0]breakpoints=[0.0625]curveForm=curve-form-7closedCurve=trueselfIntersect=false}"),
            entry("StepBezierCurve", "-959215823|StepBezierCurve{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7closedCurve=trueselfIntersect=false}"),
            entry("StepPiecewiseBezierCurve", "-959215823|StepPiecewiseBezierCurve{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7closedCurve=trueselfIntersect=false}"),
            entry("StepQuasiUniformCurve", "-959215823|StepQuasiUniformCurve{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7closedCurve=trueselfIntersect=false}"),
            entry("StepQuasiUniformCurve2D", "615760907|StepQuasiUniformCurve2D{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7}"),
            entry("StepRationalBSplineCurve", "-1972638946|StepRationalBSplineCurve{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7closedCurve=trueselfIntersect=falseweightsData=[1.5, 2.5, 3.5]knotMultiplicities=[1, 2, 3]knots=[0.0, 0.25, 1.0]knotSpec=knot-spec-7}"),
            entry("StepRationalBSplineCurve2D", "-1721257908|StepRationalBSplineCurve2D{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]weights=[4.5, 5.5]curveForm=curve-form-7}"),
            entry("StepUniformCurve", "-959215823|StepUniformCurve{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7closedCurve=trueselfIntersect=false}"),
            entry("StepUniformCurve2D", "615760907|StepUniformCurve2D{id=7name=entity-7degree=3controlPoints=[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}]curveForm=curve-form-7}"),
            entry("StepBSplineSurface", "853998297|StepBSplineSurface{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=false}"),
            entry("StepBSplineSurfaceWithKnots", "-2136757839|StepBSplineSurfaceWithKnots{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=falseuMultiplicities=[11, 12]vMultiplicities=[13, 14]uKnots=[0.0, 0.5]vKnots=[0.125, 0.875]knotSpec=knot-spec-7}"),
            entry("StepBSplineSurfaceWithKnotsAndBreakpoints", "-1565146791|StepBSplineSurfaceWithKnotsAndBreakpoints{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]uKnotMultiplicities=[5, 6]vKnotMultiplicities=[7, 8]uKnots=[0.0, 0.5]vKnots=[0.125, 0.875]uBreakpoints=[0.25]vBreakpoints=[0.75]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=false}"),
            entry("StepBezierSurface", "853998297|StepBezierSurface{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=false}"),
            entry("StepPiecewiseBezierSurface", "853998297|StepPiecewiseBezierSurface{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=false}"),
            entry("StepQuasiUniformSurface", "853998297|StepQuasiUniformSurface{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=false}"),
            entry("StepRationalBSplineSurface", "1663232004|StepRationalBSplineSurface{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=falseweightsData=[1.5, 2.5, 3.5]uMultiplicities=[11, 12]vMultiplicities=[13, 14]uKnots=[0.0, 0.5]vKnots=[0.125, 0.875]knotSpec=knot-spec-7}"),
            entry("StepUniformSurface", "853998297|StepUniformSurface{id=7name=entity-7uDegree=2vDegree=4controlPoints=[[StepCartesianPoint{id=91name=cpt-acoordinates=[1.0, 2.0, 3.0]}], [StepCartesianPoint{id=92name=cpt-bcoordinates=[-4.0, 5.0, 6.0]}]]surfaceForm=surface-form-7uClosed=truevClosed=falseselfIntersect=false}")
            );

    // ------------------------------------------------------------------ value contract

    @Test
    void theValueContractIsWrittenOnceInTheCoresAndSealedThere() throws Exception {
        for (String core : List.of(CURVE_CORE, SURFACE_CORE)) {
            Class<?> type = Class.forName(PKG + core);
            for (String name : List.of("equals", "hashCode", "toString")) {
                Method method = name.equals("equals")
                        ? type.getDeclaredMethod("equals", Object.class)
                        : type.getDeclaredMethod(name);
                assertTrue(Modifier.isFinal(method.getModifiers()),
                        core + "." + name + " must be final - the entities may not restate it");
            }
            assertTrue(Modifier.isAbstract(type.getDeclaredMethod("components").getModifiers()),
                    core + ".components must be abstract - every entity declares its own state");
        }
        for (String name : everyEntity()) {
            Class<?> type = Class.forName(PKG + name);
            for (Method method : type.getDeclaredMethods()) {
                assertFalse(List.of("equals", "hashCode", "toString").contains(method.getName()),
                        name + " must not restate " + method.getName() + "; declare components() instead");
            }
        }
    }

    @Test
    void everyComponentIsCapturedInTheRetiredOrder() throws Exception {
        for (String name : everyEntity()) {
            Object entity = construct(name, fixture(name), false);
            assertEquals(List.of(COMPONENT_ORDER.get(name).split(",")), componentKeys(entity),
                    name + " component order");
        }
    }

    /**
     * The invariant that the retired bodies could not enforce: a new field added to the constructor
     * but forgotten in one of the three algorithms. Deriving all three from one map makes the
     * omission a compile error instead, and this pins it from the outside.
     */
    @Test
    void noOwnFieldIsLeftOutOfTheComponentList() throws Exception {
        for (String name : everyEntity()) {
            Class<?> type = Class.forName(PKG + name);
            List<String> keys = componentKeys(construct(name, fixture(name), false));
            List<String> ownFields = new ArrayList<>();
            for (Field field : type.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    ownFields.add(field.getName());
                }
            }
            for (String field : ownFields) {
                assertTrue(keys.contains(field), name + " field " + field + " is missing from components()");
            }
            // The 2D entities have no state of their own, so the component list is exactly the core
            // fields - the total is what proves nothing was dropped *and* nothing was listed twice.
            assertEquals(coreFieldsOf(name).size() + ownFields.size(), keys.size(),
                    name + " must list every core field once plus exactly its own " + ownFields);
        }
    }

    /**
     * The oracle: the exact {@code hashCode} and {@code toString} the retired hand-written bodies
     * produced, captured before they were deleted. Reordering a component moves both, so this is
     * what makes the "same order" claim in the cores checkable rather than asserted in prose.
     */
    @Test
    void theValueContractStillProducesWhatTheRetiredBodiesDid() throws Exception {
        for (String name : everyEntity()) {
            Object entity = construct(name, fixture(name), false);
            assertEquals(RETIRED_VALUES.get(name), entity.hashCode() + "|" + entity.toString(),
                    name + " must hash and print exactly as before");
        }
    }

    @SuppressWarnings("unchecked")
    private static List<String> componentKeys(Object entity) throws Exception {
        Method components = entity.getClass().getDeclaredMethod("components");
        components.setAccessible(true);
        return new ArrayList<>(((Map<String, Object>) components.invoke(entity)).keySet());
    }

    // ------------------------------------------------------------------ helpers

    private static List<String> everyEntity() {
        List<String> all = new ArrayList<>();
        all.addAll(CURVES);
        all.addAll(SURFACES);
        return all;
    }

    private static List<String> coreFieldsOf(String entity) {
        return CURVES.contains(entity) ? CURVE_CORE_FIELDS : SURFACE_CORE_FIELDS;
    }

    private static Class<?> typeOf(String label) {
        switch (label) {
            case "id":
            case "degree":
            case "uDegree":
            case "vDegree":
                return int.class;
            case "name":
            case "curveForm":
            case "surfaceForm":
            case "knotSpec":
                return String.class;
            case "closedCurve":
            case "selfIntersect":
            case "uClosed":
            case "vClosed":
                return boolean.class;
            default:
                return List.class;
        }
    }

    private static Map<String, Object> fixture(String entity) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", 7);
        values.put("name", "entity-7");
        values.put("degree", 3);
        values.put("uDegree", 2);
        values.put("vDegree", 4);
        values.put("curveForm", "curve-form-7");
        values.put("surfaceForm", "surface-form-7");
        values.put("closedCurve", true);
        values.put("selfIntersect", false);
        values.put("uClosed", true);
        values.put("vClosed", false);
        values.put("weightsData", List.of(1.5, 2.5, 3.5));
        values.put("weights", List.of(4.5, 5.5));
        values.put("knotMultiplicities", List.of(1, 2, 3));
        values.put("knots", List.of(0.0, 0.25, 1.0));
        values.put("knotSpec", "knot-spec-7");
        values.put("uMultiplicities", List.of(11, 12));
        values.put("vMultiplicities", List.of(13, 14));
        values.put("uKnots", List.of(0.0, 0.5));
        values.put("vKnots", List.of(0.125, 0.875));
        values.put("breakpoints", List.of(0.0625));
        values.put("uBreakpoints", List.of(0.25));
        values.put("vBreakpoints", List.of(0.75));
        values.put("uKnotMultiplicities", List.of(5, 6));
        values.put("vKnotMultiplicities", List.of(7, 8));
        List<StepCartesianPoint> row = List.of(new StepCartesianPoint(91, "cpt-a", List.of(1.0, 2.0, 3.0)));
        List<StepCartesianPoint> row2 = List.of(new StepCartesianPoint(92, "cpt-b", List.of(-4.0, 5.0, 6.0)));
        values.put("controlPoints", new ArrayList<>(row));
        values.put("grid", new ArrayList<>(List.of(row, row2)));
        return values;
    }

    /**
     * The fixture value that the {@code controlPoints} core field was built from: a surface takes
     * the control-point grid, a curve takes the single row.
     */
    private static Object coreArgument(Map<String, Object> values, String entity, String field) {
        if (field.equals("controlPoints") && !CURVES.contains(entity)) {
            return values.get("grid");
        }
        return values.get(field);
    }

    private static Object construct(String entity, Map<String, Object> values, boolean nullControlPoints)
            throws Exception {
        List<String> order = List.of(CTOR_ORDER.get(entity).split(","));
        Class<?> type = Class.forName(PKG + entity);
        Constructor<?> ctor = type.getDeclaredConstructors()[0];
        assertEquals(order.size(), ctor.getParameterCount(), entity + " constructor arity");
        Object[] args = new Object[order.size()];
        for (int i = 0; i < order.size(); i++) {
            String label = order.get(i);
            assertEquals(typeOf(label), ctor.getParameterTypes()[i],
                    entity + " constructor parameter " + i + " (" + label + ") type");
            if (nullControlPoints && (label.equals("controlPoints") || label.equals("grid"))) {
                args[i] = null;
            } else {
                args[i] = values.get(label);
            }
            assertTrue(args[i] != null || nullControlPoints, entity + " fixture misses " + label);
        }
        return ctor.newInstance(args);
    }

    private static Object construct(String entity, String changedLabel, Object changedValue) throws Exception {
        Map<String, Object> values = fixture(entity);
        values.put(changedLabel, changedValue);
        return construct(entity, values, false);
    }

    private static Object coreValue(Object entity, String field) throws Exception {
        Method getter = entity.getClass().getMethod(getterOf(field));
        return getter.invoke(entity);
    }

    private static String getterOf(String field) {
        String suffix = field.substring(0, 1).toUpperCase() + field.substring(1);
        return switch (field) {
            case "id" -> "getId";
            case "name" -> "getName";
            default -> "get" + suffix;
        };
    }

    private static TreeSet<String> sourceExtenders(String core) throws IOException {
        TreeSet<String> found = new TreeSet<>();
        for (Path source : mainSources()) {
            String code = stripComments(Files.readString(source, StandardCharsets.UTF_8));
            if (code.contains("extends " + core + " {")
                    || code.contains("extends " + core + " implements ")) {
                found.add(source.getFileName().toString().replace(".java", ""));
            }
        }
        return found;
    }

    private static void assertNamedOnlyToExtend(String core, TreeSet<String> extenders) throws IOException {
        for (Path source : mainSources()) {
            String file = source.getFileName().toString();
            if (file.equals(core + ".java")) {
                continue;
            }
            String code = stripComments(Files.readString(source, StandardCharsets.UTF_8));
            assertEquals(0, count(code.replace("extends " + core, "extends @"), core),
                    file + " names " + core + " outside an extends clause; that is a widened branch");
            assertEquals(extenders.contains(file.replace(".java", "")) ? 1 : 0, count(code, core),
                    file + " may name " + core + " only to extend it");
        }
    }

    private static List<Path> mainSources() throws IOException {
        Path root = Path.of("src", "main", "java");
        assertTrue(Files.isDirectory(root), "src/main/java must exist");
        try (Stream<Path> stream = Files.walk(root)) {
            return stream.filter(p -> p.toString().endsWith(".java")).sorted().toList();
        }
    }

    /**
     * Removes line and block comments while keeping string literals, so a name mentioned in prose
     * cannot pass a "nobody uses it" assertion and a name inside a string cannot fake one.
     */
    private static String stripComments(String source) {
        StringBuilder out = new StringBuilder(source.length());
        int i = 0;
        while (i < source.length()) {
            char c = source.charAt(i);
            if (c == '"' || c == '\'') {
                int end = i + 1;
                while (end < source.length() && source.charAt(end) != c) {
                    end += source.charAt(end) == '\\' ? 2 : 1;
                }
                out.append(source, i, Math.min(end + 1, source.length()));
                i = end + 1;
            } else if (c == '/' && i + 1 < source.length() && source.charAt(i + 1) == '/') {
                while (i < source.length() && source.charAt(i) != '\n') {
                    i++;
                }
            } else if (c == '/' && i + 1 < source.length() && source.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < source.length() && !(source.charAt(i) == '*' && source.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString();
    }

    private static int count(String haystack, String needle) {
        int total = 0;
        int at = haystack.indexOf(needle);
        while (at >= 0) {
            total++;
            at = haystack.indexOf(needle, at + needle.length());
        }
        return total;
    }
}
