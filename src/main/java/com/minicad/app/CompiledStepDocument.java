package com.minicad.app;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;

import java.util.Map;
import java.util.Objects;

/**
 * Shared compiled STEP pipeline state that can be reused across exporters and diagnostics.
 *
 * <h2>Pipeline Architecture</h2>
 *
 * The STEP processing pipeline flows through five stages:
 *
 * <pre>
 * raw STEP text
 *   → StepParser.parse()          → StepFile (raw AST, syntax-only)
 *   → StepEntityResolver.resolveAll() → Map&lt;Integer, StepEntity&gt; (semantic model)
 *   → StepCadBuilder.fromResolved()   → StepCadBuilder (geometry/topology caches)
 *   → exporters (JSON / binary / GLB / OBJ)
 * </pre>
 *
 * <p><b>Stage 1 — Syntax parsing</b> ({@code StepTokenizer} → {@code StepParser}):
 * Reads ISO 10303 STEP exchange file text and produces a raw AST of
 * {@code StepEntityInstance} objects with typed parameter values. No semantic
 * interpretation occurs at this stage.
 *
 * <p><b>Stage 2 — Semantic resolution</b> ({@code StepEntityResolver}):
 * Maps raw AST nodes to 1214+ typed model classes organized into 26 sub-packages
 * (geometry, topology, product, annotation, manufacturing, tolerance, etc.).
 * Uses direct {@code Map.get()} dispatch on normalized entity names for O(1) lookup.
 * Resolved entities are stored in a {@code Map<Integer, StepEntity>} keyed by entity ID.
 *
 * <p><b>Stage 3 — CAD geometry/topology build</b> ({@code StepCadBuilder}):
 * Converts resolved STEP entities into internal geometry and topology types:
 * <ul>
 *   <li>Geometry: {@code Curve3} (13 types), {@code SurfaceGeometry} (16 types), 2D curves</li>
 *   <li>Topology: {@code Vertex}, {@code Edge}, {@code OrientedEdge}, {@code EdgeLoop},
 *       {@code FaceBound}, {@code Face}, {@code Shell}, {@code Solid}</li>
 *   <li>CSG/swept solids: Boolean results, extruded/revolved profiles, tessellated meshes</li>
 * </ul>
 * The builder maintains memoization caches (LinkedHashMaps) keyed by entity ID
 * to avoid redundant construction. Heavy helper classes have been extracted:
 * <ul>
 *   <li>{@code StepCadGeometryOps}: curve sampling, loop normalization, surface transformations</li>
 *   <li>{@code StepTrimResolver}: trim-value validation, parameter resolution, snap-to-curve</li>
 * </ul>
 *
 * <p><b>Stage 4 — Payload construction</b> (exporter-specific):
 * Builds a {@code PreviewPayload} containing triangulated face geometry, edge data,
 * PMI/annotation payloads, assembly graphs, metadata, and validation info.
 *
 * <p><b>Stage 5 — Encoding</b> (serializer-specific):
 * Serializes the payload into JSON, binary (MCPB), or GLB (glTF) format.
 *
 * <h2>Reuse Model</h2>
 *
 * {@code CompiledStepDocument} encapsulates stages 1–3. It is produced once via
 * {@link #compile(String)} and then reused across multiple export calls:
 * <ul>
 *   <li>{@code StepPreviewJsonExporter} uses compiled state for JSON/binary/GLB export</li>
 *   <li>{@code StepMeshExporter} uses compiled state for OBJ mesh export</li>
 *   <li>{@code StepBenchmarkApp} measures cold vs. compiled-path performance</li>
 * </ul>
 *
 * <p>Each {@code CompiledStepDocument} instance is immutable after construction.
 * The internal {@code StepCadBuilder} maintains mutable caches during build
 * operations, so compiled documents should not be shared across concurrent threads.
 *
 * @see com.minicad.app.StepPreviewJsonExporter
 * @see com.minicad.app.StepMeshExporter
 * @see com.minicad.step.semantic.StepCadBuilder
 * @see com.minicad.step.semantic.StepCadGeometryOps
 * @see com.minicad.step.semantic.StepTrimResolver
 */
record CompiledStepDocument(
        String stepText,
        StepFile stepFile,
        Map<Integer, StepEntity> resolved,
        StepCadBuilder builder
) {

    CompiledStepDocument {
        Objects.requireNonNull(stepText, "stepText");
        Objects.requireNonNull(stepFile, "stepFile");
        Objects.requireNonNull(resolved, "resolved");
        Objects.requireNonNull(builder, "builder");
    }

    static CompiledStepDocument compile(String stepText) {
        StepFile stepFile = StepParser.parse(stepText);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        return of(stepText, stepFile, resolved);
    }

    static CompiledStepDocument of(String stepText, StepFile stepFile, Map<Integer, StepEntity> resolved) {
        return new CompiledStepDocument(stepText, stepFile, resolved, StepCadBuilder.fromResolved(resolved));
    }
}
