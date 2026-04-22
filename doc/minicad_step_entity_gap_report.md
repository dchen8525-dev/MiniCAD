# MiniCAD STEP 实体支持缺口与问题清单

> 基于 `doc/minicad_step_entity_support_scan.md` 反向整理的缺口 backlog，生成时间：2026-04-22。

## 使用说明

- 这张表只列“支持不足或有问题”的实体，不重复列出已接入构建/导出的 136 个实体。
- `P0`：建议优先处理的核心几何/拓扑/GD&T 缺口。
- `P1`：已经有一定基础，但还没形成端到端支持，或缺口会明显影响 AP242/显示/表达能力。
- `P2`：主要是元数据、关系、运动学、FEA 等解析后未继续向 builder/exporter 落地。

## 汇总

| 维度 | 数量 |
|---|---:|
| P0 | 9 |
| P1 | 72 |
| P2 | 75 |
| 仅解析未落地 | 122 |
| 部分支持 | 9 |
| 未发现明确支持 | 24 |
| 仅模型类 | 1 |

## 建议先做的批次

- 第一批：`BOOLEAN_RESULT`、`HALF_SPACE_SOLID`、`TESSELLATED_FACE`、`TRIANGULATED_FACE`、`COMPLEX_TRIANGULATED_FACE`、`FACETED_BREP_AND_BREP_WITH_VOIDS`。
- 第二批：`ROUNDNESS_TOLERANCE`、`PROFILE_OF_LINE_TOLERANCE`、`PROFILE_OF_SURFACE_TOLERANCE`，以及整组 `StepDimTol` 中“已解析但未导出”的公差实体。
- 第三批：`StepVisual` 中完全缺失或只停留在解析层的表现/标注实体。
- 第四批：`StepRepr`、`StepKinematics`、`StepFEA`、`StepElement` 中仅解析未落地的关系/分析实体。

## 缺口大表

| 优先级 | 模块 | 实体 | STEP 名 | 当前状态 | 问题类型 | 当前缺口/说明 | 建议动作 |
|---|---|---|---|---|---|---|---|
| P0 | StepDimTol | `ProfileOfLineTolerance` | `PROFILE_OF_LINE_TOLERANCE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P0 | StepDimTol | `ProfileOfSurfaceTolerance` | `PROFILE_OF_SURFACE_TOLERANCE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P0 | StepDimTol | `RoundnessTolerance` | `ROUNDNESS_TOLERANCE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P0 | StepShape | `BooleanResult` | `BOOLEAN_RESULT` | 部分支持 | 已接入但 correctness/completeness 不足 | 布尔构建已接入，但按仓库说明仍属 correctness/completeness 未完成的部分支持。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P0 | StepShape | `ComplexTriangulatedFace` | `COMPLEX_TRIANGULATED_FACE` | 部分支持 | 已接入但 correctness/completeness 不足 | 细分/三角化族已接入解析与壳构建，但仓库说明仍属部分支持。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P0 | StepShape | `FacetedBrepAndBrepWithVoids` | `FACETED_BREP_AND_BREP_WITH_VOIDS` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P0 | StepShape | `HalfSpaceSolid` | `HALF_SPACE_SOLID` | 部分支持 | 已接入但 correctness/completeness 不足 | 半空间实体已解析并进入构建路径，但仓库说明仍属部分支持。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P0 | StepShape | `TessellatedFace` | `TESSELLATED_FACE` | 部分支持 | 已接入但 correctness/completeness 不足 | 细分/三角化族已接入解析与壳构建，但仓库说明仍属部分支持。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P0 | StepShape | `TriangulatedFace` | `TRIANGULATED_FACE` | 部分支持 | 已接入但 correctness/completeness 不足 | 细分/三角化族已接入解析与壳构建，但仓库说明仍属部分支持。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P1 | StepAP203 | `ChangeRequest` | `CHANGE_REQUEST` | 仅模型类 | 有模型类但未接入解析 | 有解析对象/模型类，但未检出明确注册入口。 | 补 `StepEntityResolver` 注册和解析入口，再决定是否进入 builder/exporter。 |
| P1 | StepDimTol | `AngularLocation` | `ANGULAR_LOCATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `AngularSize` | `ANGULAR_SIZE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `AngularityTolerance` | `ANGULARITY_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `CircularRunoutTolerance` | `CIRCULAR_RUNOUT_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `CompositeGroupTolerance` | `COMPOSITE_GROUP_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `ConcentricityTolerance` | `CONCENTRICITY_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `CylindricityTolerance` | `CYLINDRICITY_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `DatumReference` | `DATUM_REFERENCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `DatumReferenceModifier` | `DATUM_REFERENCE_MODIFIER` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `DatumReferenceModifierWithSign` | `DATUM_REFERENCE_MODIFIER_WITH_SIGN` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `DimensionalLocationWithPath` | `DIMENSIONAL_LOCATION_WITH_PATH` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `DimensionalSize` | `DIMENSIONAL_SIZE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `FlatnessTolerance` | `FLATNESS_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `GeometricToleranceTarget` | `GEOMETRIC_TOLERANCE_TARGET` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `Modifier` | `MODIFIER` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `ParallelismTolerance` | `PARALLELISM_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `PerpendicularityTolerance` | `PERPENDICULARITY_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `PositionTolerance` | `POSITION_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `QualifiedRepresentationItem` | `QUALIFIED_REPRESENTATION_ITEM` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `RunoutZoneDefinition` | `RUNOUT_ZONE_DEFINITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `StraightnessTolerance` | `STRAIGHTNESS_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `SymmetryTolerance` | `SYMMETRY_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `ToleranceValue` | `TOLERANCE_VALUE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `ToleranceZoneDefinition` | `TOLERANCE_ZONE_DEFINITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepDimTol | `TotalRunoutTolerance` | `TOTAL_RUNOUT_TOLERANCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `AppliedAttributeClassification` | `APPLIED_ATTRIBUTE_CLASSIFICATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `AttributeClassification` | `ATTRIBUTE_CLASSIFICATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `ExternallyDefinedRepresentation` | `EXTERNALLY_DEFINED_REPRESENTATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `FeatureRelationship` | `FEATURE_RELATIONSHIP` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `RepresentationContext3d` | `REPRESENTATION_CONTEXT3D` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `ShapeRepresentationTransformation` | `SHAPE_REPRESENTATION_TRANSFORMATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `SpecificHigherUsageOccurrence` | `SPECIFIC_HIGHER_USAGE_OCCURRENCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `StructuralAnalysisRepresentation` | `STRUCTURAL_ANALYSIS_REPRESENTATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `StructuralAnalysisRepresentationParameters` | `STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `UsageOccurrence` | `USAGE_OCCURRENCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepRepr | `ValueReasonPair` | `VALUE_REASON_PAIR` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `BoundingBox` | `BOUNDING_BOX` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `CsgShapeRepresentation` | `CSG_SHAPE_REPRESENTATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `FacetedBrep` | `FACETED_BREP` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `FillAreaShapeUse` | `FILL_AREA_SHAPE_USE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `PointOnFace` | `POINT_ON_FACE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `Shell` | `SHELL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `TessellatedCoordinateSet` | `TESSELLATED_COORDINATE_SET` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `TessellatedShell` | `TESSELLATED_SHELL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape | `TessellatedSolid` | `TESSELLATED_SOLID` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepShape、StepAP242 | `DraughtingModelItemAssociation` | `DRAUGHTING_MODEL_ITEM_ASSOCIATION` | 部分支持 | 已接入但 correctness/completeness 不足 | AP242/PMI 关联实体已解析，但端到端 PMI 元数据/导出仍不完整。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P1 | StepShape、StepAP242 | `GeometricItemSpecificUsage` | `GEOMETRIC_ITEM_SPECIFIC_USAGE` | 部分支持 | 已接入但 correctness/completeness 不足 | AP242/PMI 关联实体已解析，但端到端 PMI 元数据/导出仍不完整。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P1 | StepShape、StepAP242 | `IdAttribute` | `ID_ATTRIBUTE` | 部分支持 | 已接入但 correctness/completeness 不足 | AP242 标识属性已解析；更偏元数据关联，不属于几何构建主路径。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P1 | StepShape、StepAP242 | `ItemIdentifiedRepresentationUsage` | `ITEM_IDENTIFIED_REPRESENTATION_USAGE` | 部分支持 | 已接入但 correctness/completeness 不足 | AP242/PMI 关联实体已解析，但端到端 PMI 元数据/导出仍不完整。 | 补测试样例，明确限制，再完善 builder/exporter 的几何正确性。 |
| P1 | StepVisual | `AnnotationOccurrence` | `ANNOTATION_OCCURRENCE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `AnnotationOccurrenceRoleAssociation` | `ANNOTATION_OCCURRENCE_ROLE_ASSOCIATION` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `CurveStyleFill` | `CURVE_STYLE_FILL` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `CurveStyleFont` | `CURVE_STYLE_FONT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `DraughtingModel` | `DRAUGHTING_MODEL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `DrawingRevision` | `DRAWING_REVISION` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `FillAreaStyleHatching` | `FILL_AREA_STYLE_HATCHING` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `FillAreaStyleTileCurveWithStyle` | `FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `ImageFile` | `IMAGE_FILE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `LayeredItem` | `LAYERED_ITEM` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `MarkerStyle` | `MARKER_STYLE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `PresentationLayerUsage` | `PRESENTATION_LAYER_USAGE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `PresentationLayerWithStyle` | `PRESENTATION_LAYER_WITH_STYLE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `PresentationStyleByContext` | `PRESENTATION_STYLE_BY_CONTEXT` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `StyledRepresentationItem` | `STYLED_REPRESENTATION_ITEM` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `TextLiteral` | `TEXT_LITERAL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `TextLiteralWithExtent` | `TEXT_LITERAL_WITH_EXTENT` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `TileCurve` | `TILE_CURVE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `TileSymbol` | `TILE_SYMBOL` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `ViewVolume` | `VIEW_VOLUME` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P1 | StepVisual | `ViewingPlane` | `VIEWING_PLANE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P1 | StepVisual | `VisibleActionRequest` | `VISIBLE_ACTION_REQUEST` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P2 | StepAP203 | `CcDesignApproval` | `CC_DESIGN_APPROVAL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `CcDesignCertification` | `CC_DESIGN_CERTIFICATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `CcDesignContract` | `CC_DESIGN_CONTRACT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `CcDesignDateAndTimeAssignment` | `CC_DESIGN_DATE_AND_TIME_ASSIGNMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `CcDesignPersonAndOrganizationAssignment` | `CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `CcDesignSecurityClassification` | `CC_DESIGN_SECURITY_CLASSIFICATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `Change` | `CHANGE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `MakeFromUsageOption` | `MAKE_FROM_USAGE_OPTION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `StartRequest` | `START_REQUEST` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `StartWork` | `START_WORK` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP203 | `WorkItem` | `WORK_ITEM` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP209 | `StructAnalysisModel` | `STRUCT_ANALYSIS_MODEL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `AutoDesignApprovalAssignment` | `AUTO_DESIGN_APPROVAL_ASSIGNMENT` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P2 | StepAP214 | `AutoDesignDateAssignment` | `AUTO_DESIGN_DATE_ASSIGNMENT` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P2 | StepAP214 | `AutoDesignGroupAssignment` | `AUTO_DESIGN_GROUP_ASSIGNMENT` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P2 | StepAP214 | `AutoDesignOrganizationAssignment` | `AUTO_DESIGN_ORGANIZATION_ASSIGNMENT` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P2 | StepAP214 | `AutoDesignSecurityClassificationAssignment` | `AUTO_DESIGN_SECURITY_CLASSIFICATION_ASSIGNMENT` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P2 | StepAP214 | `DateAndTimeAssignment` | `DATE_AND_TIME_ASSIGNMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `DrawingSheetRevision` | `DRAWING_SHEET_REVISION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `ExternallyDefinedClass` | `EXTERNALLY_DEFINED_CLASS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `ExternallyDefinedGeneralProperty` | `EXTERNALLY_DEFINED_GENERAL_PROPERTY` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `GeneralPropertyDefinition` | `GENERAL_PROPERTY_DEFINITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `PresentationView` | `PRESENTATION_VIEW` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `RequirementAssignment` | `REQUIREMENT_ASSIGNMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepAP214 | `RequirementSource` | `REQUIREMENT_SOURCE` | 未发现明确支持 | 未检出明确支持 | 未进入完整端到端链路。 | 先确认是否需要支持；若需要，补模型/解析/构建/导出中的缺失链路。 |
| P2 | StepBasic | `DesignContext` | `DESIGN_CONTEXT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepBasic | `MechanicalContext` | `MECHANICAL_CONTEXT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `ConnectivityElement` | `CONNECTIVITY_ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `ElementGeometricDescription` | `ELEMENT_GEOMETRIC_DESCRIPTION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `ElementVolume` | `ELEMENT_VOLUME` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `LineElement` | `LINE_ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `MassElement` | `MASS_ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `NodeRepresentation` | `NODE_REPRESENTATION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `SurfaceElement` | `SURFACE_ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `UniformSurfaceElement` | `UNIFORM_SURFACE_ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `UniformVolumeElement` | `UNIFORM_VOLUME_ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepElement | `VolumeElement` | `VOLUME_ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `AccelerationBoundaryCondition` | `ACCELERATION_BOUNDARY_CONDITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `BoundaryCondition` | `BOUNDARY_CONDITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `BucklingAnalysis` | `BUCKLING_ANALYSIS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `DisplacementBoundaryCondition` | `DISPLACEMENT_BOUNDARY_CONDITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `Element` | `ELEMENT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `FeaLinearMaterial` | `FEA_LINEAR_MATERIAL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `FeaMassDensity` | `FEA_MASS_DENSITY` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `FeaModel` | `FEA_MODEL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `FeaNonLinearMaterial` | `FEA_NON_LINEAR_MATERIAL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `FeaUltimateStress` | `FEA_ULTIMATE_STRESS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `FeaYieldStress` | `FEA_YIELD_STRESS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `ForceBoundaryCondition` | `FORCE_BOUNDARY_CONDITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `Load` | `LOAD` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `Material` | `MATERIAL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `ModalAnalysis` | `MODAL_ANALYSIS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `Node` | `NODE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `PressureBoundaryCondition` | `PRESSURE_BOUNDARY_CONDITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `StressAnalysis` | `STRESS_ANALYSIS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `StructuralAnalysisModel` | `STRUCTURAL_ANALYSIS_MODEL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `ThermalAnalysis` | `THERMAL_ANALYSIS` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `ThermalBoundaryCondition` | `THERMAL_BOUNDARY_CONDITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepFEA | `VelocityBoundaryCondition` | `VELOCITY_BOUNDARY_CONDITION` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `CylindricalJoint` | `CYLINDRICAL_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `DirectionSense` | `DIRECTION_SENSE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `GeneralJoint` | `GENERAL_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `JointValue` | `JOINT_VALUE` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `KinematicChain` | `KINEMATIC_CHAIN` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `KinematicJoint` | `KINEMATIC_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `KinematicModel` | `KINEMATIC_MODEL` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `KinematicPair` | `KINEMATIC_PAIR` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `Link` | `LINK` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `Mechanism` | `MECHANISM` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `MotionConstraint` | `MOTION_CONSTRAINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `PlanarJoint` | `PLANAR_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `PrismaticJoint` | `PRISMATIC_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `RevoluteJoint` | `REVOLUTE_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `ScrewJoint` | `SCREW_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
| P2 | StepKinematics | `SphericalJoint` | `SPHERICAL_JOINT` | 已注册语义解析 | 已能解析，但未进入构建/导出主路径 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 | 如果目标是端到端支持，补 semantic builder 和 app/exporter；若仅需读元数据，可维持现状。 |
