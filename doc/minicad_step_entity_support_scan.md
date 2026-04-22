# MiniCAD STEP 实体支持扫描表

> 基于 `doc/occt_step_support.md` 的实体清单，对仓库静态代码做交叉扫描，生成时间：2026-04-22。

## 判定口径

- 清单来源：`doc/occt_step_support.md` 中各 `2.x` 小节的“主要实体对象”。
- 扫描依据：`src/main/java/com/minicad/step/semantic/StepEntityResolver.java`、`StepCadBuilder.java`、`StepTopologyBuilder.java`、`StepShellBuilder.java`、`StepSolidBuilder.java`、`StepProfileBuilder.java`、`src/main/java/com/minicad/app/StepPreviewJsonExporter.java`、`StepMeshExporter.java`、`ProductMetadataExtractor.java`、`StepMetadataExtractor.java`，以及 `step.syntax` 下的 HEADER 解析类。
- `模型类/解析对象`：仓库中存在对应 `Step...` 类型或 HEADER 解析记录。
- `解析接入`：检出 resolver 注册/解析入口；HEADER 三项按 `step.syntax` 记为已接入。
- `语义构建`：检出进入 `semantic` builder 路径。
- `应用导出/提取`：检出进入预览导出、mesh 导出或元数据提取路径。
- `部分支持` 采用保守覆盖：即使代码路径存在，只要仓库说明明确标记为 correctness/completeness 未完成，仍记为部分支持。

## 总览

| 状态 | 数量 |
|---|---:|
| 已接入构建/导出 | 136 |
| 已注册语义解析 | 122 |
| 部分支持 | 9 |
| 仅模型类 | 1 |
| 未发现明确支持 | 24 |

## 分模块汇总

| 模块 | 实体数 | 已接入构建/导出 | 已注册语义解析 | 部分支持 | 仅模型类 | 未发现明确支持 |
|---|---:|---:|---:|---:|---:|---:|
| StepBasic | 15 | 13 | 2 | 0 | 0 | 0 |
| StepGeom | 35 | 35 | 0 | 0 | 0 | 0 |
| StepShape | 45 | 26 | 9 | 9 | 0 | 1 |
| StepRepr | 29 | 18 | 11 | 0 | 0 | 0 |
| StepVisual | 45 | 23 | 8 | 0 | 0 | 14 |
| StepDimTol | 36 | 8 | 25 | 0 | 0 | 3 |
| StepKinematics | 17 | 1 | 16 | 0 | 0 | 0 |
| StepFEA | 22 | 0 | 22 | 0 | 0 | 0 |
| StepElement | 10 | 0 | 10 | 0 | 0 | 0 |
| StepAP203 | 16 | 4 | 11 | 0 | 1 | 0 |
| StepAP214 | 23 | 10 | 7 | 0 | 0 | 6 |
| StepAP242 | 4 | 0 | 0 | 4 | 0 | 0 |
| StepAP209 | 1 | 0 | 1 | 0 | 0 | 0 |
| HeaderSection | 3 | 3 | 0 | 0 | 0 | 0 |

## 重点观察

- 几何与拓扑主干实体整体覆盖较高，`StepGeom` 和 `StepShape` 中大多数核心实体已进入构建或预览导出路径。
- 关系/属性/可视化/GD&T/运动学/FEA 类实体大量停留在“已注册语义解析”，说明仓库在“能读入语义对象”和“能参与几何构建/前端导出”之间有明显分层。
- 明确标记为部分支持的条目主要集中在布尔、半空间、细分曲面族，以及 AP242/PMI 关联实体。
- `HeaderSection` 的 `FILE_DESCRIPTION` / `FILE_NAME` / `FILE_SCHEMA` 已支持，但走 `step.syntax` 路径，不属于 `StepEntityResolver` 体系。

## 实体大表

| 来源模块 | 实体 | STEP 名 | 模型类/解析对象 | 解析接入 | 语义构建 | 应用导出/提取 | 判定 | 备注 |
|---|---|---|---|---|---|---|---|---|
| StepBasic | `Product` | `PRODUCT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `ProductDefinition` | `PRODUCT_DEFINITION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `ProductDefinitionFormation` | `PRODUCT_DEFINITION_FORMATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `ProductContext` | `PRODUCT_CONTEXT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `Organization` | `ORGANIZATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `Person` | `PERSON` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `PersonAndOrganization` | `PERSON_AND_ORGANIZATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `Approval` | `APPROVAL` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `ApprovalRole` | `APPROVAL_ROLE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `ApprovalStatus` | `APPROVAL_STATUS` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `UncertaintyMeasure` | `UNCERTAINTY_MEASURE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic | `MechanicalContext` | `MECHANICAL_CONTEXT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepBasic | `DesignContext` | `DESIGN_CONTEXT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepBasic、StepAP214 | `AppliedApprovalAssignment` | `APPLIED_APPROVAL_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepBasic、StepAP214 | `AppliedDateAssignment` | `APPLIED_DATE_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepGeom | `CartesianPoint` | `CARTESIAN_POINT` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Direction` | `DIRECTION` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Vector` | `VECTOR` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Line` | `LINE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Circle` | `CIRCLE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Ellipse` | `ELLIPSE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Hyperbola` | `HYPERBOLA` | ❌ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Parabola` | `PARABOLA` | ❌ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `BSplineCurve` | `B_SPLINE_CURVE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `BSplineSurface` | `B_SPLINE_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Plane` | `PLANE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `CylindricalSurface` | `CYLINDRICAL_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `ConicalSurface` | `CONICAL_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `SphericalSurface` | `SPHERICAL_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `ToroidalSurface` | `TOROIDAL_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `SurfaceOfRevolution` | `SURFACE_OF_REVOLUTION` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `SurfaceOfLinearExtrusion` | `SURFACE_OF_LINEAR_EXTRUSION` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `OffsetSurface` | `OFFSET_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `PCurve` | `PCURVE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `TrimmedCurve` | `TRIMMED_CURVE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `CompositeCurve` | `COMPOSITE_CURVE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `CompositeCurveOnSurface` | `COMPOSITE_CURVE_ON_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `SurfaceCurve` | `SURFACE_CURVE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Axis1Placement` | `AXIS1_PLACEMENT` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Axis2Placement2d` | `AXIS2_PLACEMENT_2D` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Axis2Placement3d` | `AXIS2_PLACEMENT_3D` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `CartesianTransformationOperator` | `CARTESIAN_TRANSFORMATION_OPERATOR` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `CartesianTransformationOperator2d` | `CARTESIAN_TRANSFORMATION_OPERATOR_2D` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `CartesianTransformationOperator3d` | `CARTESIAN_TRANSFORMATION_OPERATOR_3D` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `Point` | `POINT` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom | `RectangularTrimmedSurface` | `RECTANGULAR_TRIMMED_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom、StepVisual | `FillAreaStyle` | `FILL_AREA_STYLE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepGeom | `GeometricRepresentationItem` | `GEOMETRIC_REPRESENTATION_ITEM` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepGeom、StepShape | `EdgeCurve` | `EDGE_CURVE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepGeom、StepShape | `FaceSurface` | `FACE_SURFACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `VertexPoint` | `VERTEX_POINT` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `VertexLoop` | `VERTEX_LOOP` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `EdgeLoop` | `EDGE_LOOP` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `OrientedEdge` | `ORIENTED_EDGE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `PolyLoop` | `POLY_LOOP` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `AdvancedFace` | `ADVANCED_FACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `FaceBound` | `FACE_BOUND` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `FaceOuterBound` | `FACE_OUTER_BOUND` | ❌ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `ClosedShell` | `CLOSED_SHELL` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `OpenShell` | `OPEN_SHELL` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `ConnectedFaceSet` | `CONNECTED_FACE_SET` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `ManifoldSolidBrep` | `MANIFOLD_SOLID_BREP` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `BrepWithVoids` | `BREP_WITH_VOIDS` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `FacetedBrep` | `FACETED_BREP` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `FacetedBrepAndBrepWithVoids` | `FACETED_BREP_AND_BREP_WITH_VOIDS` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepShape | `ShellBasedSurfaceModel` | `SHELL_BASED_SURFACE_MODEL` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `GeometricSet` | `GEOMETRIC_SET` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `GeometricCurveSet` | `GEOMETRIC_CURVE_SET` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `TessellatedShell` | `TESSELLATED_SHELL` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `TessellatedSolid` | `TESSELLATED_SOLID` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `TriangulatedFace` | `TRIANGULATED_FACE` | ✅ | ✅ | ✅ | ✅ | 部分支持 | 细分/三角化族已接入解析与壳构建，但仓库说明仍属部分支持。 |
| StepShape | `ComplexTriangulatedFace` | `COMPLEX_TRIANGULATED_FACE` | ✅ | ✅ | ✅ | ✅ | 部分支持 | 细分/三角化族已接入解析与壳构建，但仓库说明仍属部分支持。 |
| StepShape | `TessellatedCoordinateSet` | `TESSELLATED_COORDINATE_SET` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `TessellatedFace` | `TESSELLATED_FACE` | ✅ | ✅ | ✅ | ✅ | 部分支持 | 细分/三角化族已接入解析与壳构建，但仓库说明仍属部分支持。 |
| StepShape | `HalfSpaceSolid` | `HALF_SPACE_SOLID` | ✅ | ✅ | ✅ | ✅ | 部分支持 | 半空间实体已解析并进入构建路径，但仓库说明仍属部分支持。 |
| StepShape | `SolidModel` | `SOLID_MODEL` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `BooleanResult` | `BOOLEAN_RESULT` | ✅ | ✅ | ✅ | ✅ | 部分支持 | 布尔构建已接入，但按仓库说明仍属 correctness/completeness 未完成的部分支持。 |
| StepShape | `OrientedClosedShell` | `ORIENTED_CLOSED_SHELL` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `OrientedOpenShell` | `ORIENTED_OPEN_SHELL` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `OrientedPath` | `ORIENTED_PATH` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `Path` | `PATH` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `Loop` | `LOOP` | ❌ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepShape | `Edge` | `EDGE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `Face` | `FACE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepShape | `Shell` | `SHELL` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `CsgShapeRepresentation` | `CSG_SHAPE_REPRESENTATION` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `BoundingBox` | `BOUNDING_BOX` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `FillAreaShapeUse` | `FILL_AREA_SHAPE_USE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape | `PointOnFace` | `POINT_ON_FACE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepShape、StepAP242 | `GeometricItemSpecificUsage` | `GEOMETRIC_ITEM_SPECIFIC_USAGE` | ✅ | ✅ | ❌ | ✅ | 部分支持 | AP242/PMI 关联实体已解析，但端到端 PMI 元数据/导出仍不完整。 |
| StepShape、StepAP242 | `ItemIdentifiedRepresentationUsage` | `ITEM_IDENTIFIED_REPRESENTATION_USAGE` | ✅ | ✅ | ❌ | ✅ | 部分支持 | AP242/PMI 关联实体已解析，但端到端 PMI 元数据/导出仍不完整。 |
| StepShape、StepAP242 | `IdAttribute` | `ID_ATTRIBUTE` | ✅ | ✅ | ❌ | ✅ | 部分支持 | AP242 标识属性已解析；更偏元数据关联，不属于几何构建主路径。 |
| StepShape、StepAP242 | `DraughtingModelItemAssociation` | `DRAUGHTING_MODEL_ITEM_ASSOCIATION` | ✅ | ✅ | ❌ | ✅ | 部分支持 | AP242/PMI 关联实体已解析，但端到端 PMI 元数据/导出仍不完整。 |
| StepRepr | `Representation` | `REPRESENTATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `ShapeRepresentation` | `SHAPE_REPRESENTATION` | ❌ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `ProductDefinitionShape` | `PRODUCT_DEFINITION_SHAPE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `ShapeDefinitionRepresentation` | `SHAPE_DEFINITION_REPRESENTATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `RepresentationContext` | `REPRESENTATION_CONTEXT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `RepresentationContext3d` | `REPRESENTATION_CONTEXT3D` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `GeometricRepresentationContext` | `GEOMETRIC_REPRESENTATION_CONTEXT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `PropertyDefinition` | `PROPERTY_DEFINITION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `PropertyDefinitionRepresentation` | `PROPERTY_DEFINITION_REPRESENTATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `ShapeAspect` | `SHAPE_ASPECT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `ShapeAspectRelationship` | `SHAPE_ASPECT_RELATIONSHIP` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `NextAssemblyUsageOccurrence` | `NEXT_ASSEMBLY_USAGE_OCCURRENCE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `UsageOccurrence` | `USAGE_OCCURRENCE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `ContextDependentShapeRepresentation` | `CONTEXT_DEPENDENT_SHAPE_REPRESENTATION` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepRepr | `RepresentationRelationship` | `REPRESENTATION_RELATIONSHIP` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `RepresentationMap` | `REPRESENTATION_MAP` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `MappedItem` | `MAPPED_ITEM` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepRepr | `ShapeRepresentationRelationship` | `SHAPE_REPRESENTATION_RELATIONSHIP` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `ShapeRepresentationTransformation` | `SHAPE_REPRESENTATION_TRANSFORMATION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `ProductDefinitionRelationship` | `PRODUCT_DEFINITION_RELATIONSHIP` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepRepr | `SpecificHigherUsageOccurrence` | `SPECIFIC_HIGHER_USAGE_OCCURRENCE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `AppliedAttributeClassification` | `APPLIED_ATTRIBUTE_CLASSIFICATION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `AttributeClassification` | `ATTRIBUTE_CLASSIFICATION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `ExternallyDefinedRepresentation` | `EXTERNALLY_DEFINED_REPRESENTATION` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `FeatureRelationship` | `FEATURE_RELATIONSHIP` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `StructuralAnalysisRepresentation` | `STRUCTURAL_ANALYSIS_REPRESENTATION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `StructuralAnalysisRepresentationParameters` | `STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `ValueReasonPair` | `VALUE_REASON_PAIR` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepRepr | `MeasureWithUnit` | `MEASURE_WITH_UNIT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `StyledItem` | `STYLED_ITEM` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `StyledRepresentationItem` | `STYLED_REPRESENTATION_ITEM` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `Colour` | `COLOUR` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `RgbColour` | `COLOUR_RGB` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `PreDefinedColour` | `PRE_DEFINED_COLOUR` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `DraughtingPreDefinedColour` | `DRAUGHTING_PRE_DEFINED_COLOUR` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `SurfaceStyleUsage` | `SURFACE_STYLE_USAGE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `SurfaceSideStyle` | `SURFACE_SIDE_STYLE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `SurfaceStyleFillArea` | `SURFACE_STYLE_FILL_AREA` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `SurfaceStyleRendering` | `SURFACE_STYLE_RENDERING` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `SurfaceStyleSegmentationCurve` | `SURFACE_STYLE_SEGMENTATION_CURVE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `CurveStyle` | `CURVE_STYLE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `CurveStyleFill` | `CURVE_STYLE_FILL` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `CurveStyleFont` | `CURVE_STYLE_FONT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `FillAreaStyleColour` | `FILL_AREA_STYLE_COLOUR` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `FillAreaStyleHatching` | `FILL_AREA_STYLE_HATCHING` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `FillAreaStyleTileCurveWithStyle` | `FILL_AREA_STYLE_TILE_CURVE_WITH_STYLE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `TileCurve` | `TILE_CURVE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `TileSymbol` | `TILE_SYMBOL` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `SymbolStyle` | `SYMBOL_STYLE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `MarkerStyle` | `MARKER_STYLE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `TextStyle` | `TEXT_STYLE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `TextLiteral` | `TEXT_LITERAL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `TextLiteralWithExtent` | `TEXT_LITERAL_WITH_EXTENT` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `AnnotationCurveOccurrence` | `ANNOTATION_CURVE_OCCURRENCE` | ✅ | ✅ | ✅ | ✅ | 已接入构建/导出 |  |
| StepVisual | `AnnotationFillAreaOccurrence` | `ANNOTATION_FILL_AREA_OCCURRENCE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `PresentationLayerAssignment` | `PRESENTATION_LAYER_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `PresentationLayerUsage` | `PRESENTATION_LAYER_USAGE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `PresentationLayerWithStyle` | `PRESENTATION_LAYER_WITH_STYLE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `LayeredItem` | `LAYERED_ITEM` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `PresentationStyleAssignment` | `PRESENTATION_STYLE_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `PresentationStyleByContext` | `PRESENTATION_STYLE_BY_CONTEXT` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `DraughtingModel` | `DRAUGHTING_MODEL` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `AnnotationOccurrence` | `ANNOTATION_OCCURRENCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `AnnotationOccurrenceRoleAssociation` | `ANNOTATION_OCCURRENCE_ROLE_ASSOCIATION` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `AnnotationPlane` | `ANNOTATION_PLANE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `AnnotationFillArea` | `ANNOTATION_FILL_AREA` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `AnnotationSymbol` | `ANNOTATION_SYMBOL` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `AnnotationText` | `ANNOTATION_TEXT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepVisual | `DrawingRevision` | `DRAWING_REVISION` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `ViewVolume` | `VIEW_VOLUME` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepVisual | `ViewingPlane` | `VIEWING_PLANE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `VisibleActionRequest` | `VISIBLE_ACTION_REQUEST` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepVisual | `ImageFile` | `IMAGE_FILE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepDimTol | `GeometricTolerance` | `GEOMETRIC_TOLERANCE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `FlatnessTolerance` | `FLATNESS_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `StraightnessTolerance` | `STRAIGHTNESS_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `RoundnessTolerance` | `ROUNDNESS_TOLERANCE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepDimTol | `CylindricityTolerance` | `CYLINDRICITY_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `ProfileOfLineTolerance` | `PROFILE_OF_LINE_TOLERANCE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepDimTol | `ProfileOfSurfaceTolerance` | `PROFILE_OF_SURFACE_TOLERANCE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepDimTol | `PositionTolerance` | `POSITION_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `PerpendicularityTolerance` | `PERPENDICULARITY_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `ParallelismTolerance` | `PARALLELISM_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `AngularityTolerance` | `ANGULARITY_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `ConcentricityTolerance` | `CONCENTRICITY_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `SymmetryTolerance` | `SYMMETRY_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `CircularRunoutTolerance` | `CIRCULAR_RUNOUT_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `TotalRunoutTolerance` | `TOTAL_RUNOUT_TOLERANCE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `DimensionalSize` | `DIMENSIONAL_SIZE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `DimensionalLocation` | `DIMENSIONAL_LOCATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `DimensionalLocationWithPath` | `DIMENSIONAL_LOCATION_WITH_PATH` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `AngularSize` | `ANGULAR_SIZE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `AngularLocation` | `ANGULAR_LOCATION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `Datum` | `DATUM` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `DatumFeature` | `DATUM_FEATURE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `DatumTarget` | `DATUM_TARGET` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `DatumReference` | `DATUM_REFERENCE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `DatumReferenceModifier` | `DATUM_REFERENCE_MODIFIER` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `DatumReferenceModifierWithSign` | `DATUM_REFERENCE_MODIFIER_WITH_SIGN` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `ToleranceZone` | `TOLERANCE_ZONE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `ToleranceZoneForm` | `TOLERANCE_ZONE_FORM` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `Modifier` | `MODIFIER` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `GeometricToleranceTarget` | `GEOMETRIC_TOLERANCE_TARGET` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `GeometricToleranceWithDatumReference` | `GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepDimTol | `CompositeGroupTolerance` | `COMPOSITE_GROUP_TOLERANCE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `RunoutZoneDefinition` | `RUNOUT_ZONE_DEFINITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `ToleranceValue` | `TOLERANCE_VALUE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `ToleranceZoneDefinition` | `TOLERANCE_ZONE_DEFINITION` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepDimTol | `QualifiedRepresentationItem` | `QUALIFIED_REPRESENTATION_ITEM` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `KinematicJoint` | `KINEMATIC_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `KinematicPair` | `KINEMATIC_PAIR` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `Mechanism` | `MECHANISM` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `Link` | `LINK` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `MotionConstraint` | `MOTION_CONSTRAINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `KinematicChain` | `KINEMATIC_CHAIN` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `KinematicModel` | `KINEMATIC_MODEL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `KinematicProperty` | `KINEMATIC_PROPERTY` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepKinematics | `RevoluteJoint` | `REVOLUTE_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `PrismaticJoint` | `PRISMATIC_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `CylindricalJoint` | `CYLINDRICAL_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `SphericalJoint` | `SPHERICAL_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `PlanarJoint` | `PLANAR_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `ScrewJoint` | `SCREW_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `GeneralJoint` | `GENERAL_JOINT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `DirectionSense` | `DIRECTION_SENSE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepKinematics | `JointValue` | `JOINT_VALUE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `FeaModel` | `FEA_MODEL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `Node` | `NODE` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `Element` | `ELEMENT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `Load` | `LOAD` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `BoundaryCondition` | `BOUNDARY_CONDITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `Material` | `MATERIAL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `StructuralAnalysisModel` | `STRUCTURAL_ANALYSIS_MODEL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `StressAnalysis` | `STRESS_ANALYSIS` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `BucklingAnalysis` | `BUCKLING_ANALYSIS` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `ModalAnalysis` | `MODAL_ANALYSIS` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `ThermalAnalysis` | `THERMAL_ANALYSIS` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `DisplacementBoundaryCondition` | `DISPLACEMENT_BOUNDARY_CONDITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `VelocityBoundaryCondition` | `VELOCITY_BOUNDARY_CONDITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `AccelerationBoundaryCondition` | `ACCELERATION_BOUNDARY_CONDITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `ForceBoundaryCondition` | `FORCE_BOUNDARY_CONDITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `PressureBoundaryCondition` | `PRESSURE_BOUNDARY_CONDITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `ThermalBoundaryCondition` | `THERMAL_BOUNDARY_CONDITION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `FeaLinearMaterial` | `FEA_LINEAR_MATERIAL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `FeaNonLinearMaterial` | `FEA_NON_LINEAR_MATERIAL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `FeaMassDensity` | `FEA_MASS_DENSITY` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `FeaYieldStress` | `FEA_YIELD_STRESS` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepFEA | `FeaUltimateStress` | `FEA_ULTIMATE_STRESS` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `VolumeElement` | `VOLUME_ELEMENT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `SurfaceElement` | `SURFACE_ELEMENT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `LineElement` | `LINE_ELEMENT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `MassElement` | `MASS_ELEMENT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `ConnectivityElement` | `CONNECTIVITY_ELEMENT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `ElementVolume` | `ELEMENT_VOLUME` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `NodeRepresentation` | `NODE_REPRESENTATION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `ElementGeometricDescription` | `ELEMENT_GEOMETRIC_DESCRIPTION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `UniformSurfaceElement` | `UNIFORM_SURFACE_ELEMENT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepElement | `UniformVolumeElement` | `UNIFORM_VOLUME_ELEMENT` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `CcDesignApproval` | `CC_DESIGN_APPROVAL` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `CcDesignCertification` | `CC_DESIGN_CERTIFICATION` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `CcDesignContract` | `CC_DESIGN_CONTRACT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `CcDesignDateAndTimeAssignment` | `CC_DESIGN_DATE_AND_TIME_ASSIGNMENT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `CcDesignPersonAndOrganizationAssignment` | `CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `CcDesignSecurityClassification` | `CC_DESIGN_SECURITY_CLASSIFICATION` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `Change` | `CHANGE` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `ChangeRequest` | `CHANGE_REQUEST` | ✅ | ❌ | ❌ | ❌ | 仅模型类 | 有解析对象/模型类，但未检出明确注册入口。 |
| StepAP203 | `StartRequest` | `START_REQUEST` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `StartWork` | `START_WORK` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `WorkItem` | `WORK_ITEM` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `MakeFromUsageOption` | `MAKE_FROM_USAGE_OPTION` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP203 | `SecurityClassification` | `SECURITY_CLASSIFICATION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP203 | `SecurityClassificationLevel` | `SECURITY_CLASSIFICATION_LEVEL` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP203 | `Effectivity` | `EFFECTIVITY` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP203 | `DateAndTime` | `DATE_AND_TIME` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `AppliedGroupAssignment` | `APPLIED_GROUP_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `AppliedSecurityClassificationAssignment` | `APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `AutoDesignApprovalAssignment` | `AUTO_DESIGN_APPROVAL_ASSIGNMENT` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepAP214 | `AutoDesignDateAssignment` | `AUTO_DESIGN_DATE_ASSIGNMENT` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepAP214 | `AutoDesignGroupAssignment` | `AUTO_DESIGN_GROUP_ASSIGNMENT` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepAP214 | `AutoDesignOrganizationAssignment` | `AUTO_DESIGN_ORGANIZATION_ASSIGNMENT` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepAP214 | `AutoDesignSecurityClassificationAssignment` | `AUTO_DESIGN_SECURITY_CLASSIFICATION_ASSIGNMENT` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepAP214 | `ExternallyDefinedClass` | `EXTERNALLY_DEFINED_CLASS` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP214 | `ExternallyDefinedGeneralProperty` | `EXTERNALLY_DEFINED_GENERAL_PROPERTY` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP214 | `DateAssignment` | `DATE_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `OrganizationAssignment` | `ORGANIZATION_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `SecurityClassificationAssignment` | `SECURITY_CLASSIFICATION_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `ApprovalAssignment` | `APPROVAL_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `DateAndTimeAssignment` | `DATE_AND_TIME_ASSIGNMENT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP214 | `PersonAndOrganizationAssignment` | `PERSON_AND_ORGANIZATION_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `PresentationView` | `PRESENTATION_VIEW` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP214 | `DrawingSheetRevision` | `DRAWING_SHEET_REVISION` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP214 | `GeneralPropertyDefinition` | `GENERAL_PROPERTY_DEFINITION` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP214 | `GroupAssignment` | `GROUP_ASSIGNMENT` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 |  |
| StepAP214 | `RequirementAssignment` | `REQUIREMENT_ASSIGNMENT` | ❌ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| StepAP214 | `RequirementSource` | `REQUIREMENT_SOURCE` | ❌ | ❌ | ❌ | ❌ | 未发现明确支持 |  |
| StepAP209 | `StructAnalysisModel` | `STRUCT_ANALYSIS_MODEL` | ✅ | ✅ | ❌ | ❌ | 已注册语义解析 | 更偏元数据/关系实体，未进入几何构建或预览导出主路径。 |
| HeaderSection | `FileDescription` | `FILE_DESCRIPTION` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 | HEADER 实体走 `step.syntax` 解析路径，不经过 `StepEntityResolver`。 |
| HeaderSection | `FileName` | `FILE_NAME` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 | HEADER 实体走 `step.syntax` 解析路径，不经过 `StepEntityResolver`。 |
| HeaderSection | `FileSchema` | `FILE_SCHEMA` | ✅ | ✅ | ❌ | ✅ | 已接入构建/导出 | HEADER 实体走 `step.syntax` 解析路径，不经过 `StepEntityResolver`。 |
