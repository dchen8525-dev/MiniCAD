# MiniCAD

面向工业应用的 CAD 内核与 STEP 文件解析器，使用 Java 21 实现。

## 项目定位

MiniCAD 致力于构建一个完整的工业 CAD 内核，提供 STEP (ISO 10303) 文件的完整解析能力，支持 AP214/AP242 协议的核心实体类型。项目的目标是：

- **完整 STEP 解析**: 支持所有主流 STEP 实体类型的解析和语义理解
- **B-Rep 几何内核**: 支持完整的边界表示 (Boundary Representation) 拓扑结构
- **可视化预览**: 提供基于 Web 的三维模型预览能力
- **工业级兼容**: 支持主流 CAD 软件 (CATIA、NX、SolidWorks、Creo 等) 导出的 STEP 文件

项目已实现：

- **1175 个 STEP 实体模型类** (`step/model`)
- **1324 种 STEP 实体类型注册**（`registry.put()` 调用次数）
- **完整的语法解析链**：STEP 文本 → syntax AST → semantic model → geometry/topology
- **Web 预览器**：基于 Three.js 的浏览器可视化，支持平面、圆柱面、圆锥面、球面、环面、B-Spline 曲面、有理 B-Spline 曲面等 16 种曲面类型

## 包说明

| 包路径 | 文件数 | 说明 |
|--------|--------|------|
| `com.minicad.common` | 8 | 公共异常类和工具类（`Epsilon`, `Preconditions`, `GeometryException`, `StepParseException`, `StepResolutionException`, `TopologyException`, `UnsupportedGeometryException`, `UnsupportedStepEntityException`） |
| `com.minicad.geometry` | 37 | 3D 几何类型：`Curve3` (13 种子类型)、`SurfaceGeometry` (16 种子类型) 及辅助类型 |
| `com.minicad.geometry2d` | 16 | 2D 参数域几何类型（曲线、包围盒等） |
| `com.minicad.topology` | 11 | B-Rep 拓扑类型（Vertex、Edge、Face、Shell、Solid 等） |
| `com.minicad.step.syntax` | 5 | STEP 语法解析器（Tokenizer、Parser、AST 模型） |
| `com.minicad.step.semantic` | 6 | STEP 语义解析器（EntityResolver、EntityFactory、CadBuilder 等） |
| `com.minicad.step.model` | 1175 | STEP 实体模型类，分 26 个子包（见下表） |
| `com.minicad.app` | 13 | 应用入口：CLI 解析器、Web 预览器、JSON 导出器、装配图构建器等 |

### `step.model` 子包明细

| 子包 | 文件数 | 说明 |
|------|--------|------|
| `workflow` | 199 | 工作流与流程管理实体 |
| `geometry` | 115 | 几何表示实体（曲线、曲面、变换、坐标系等） |
| `product` | 107 | 产品定义与装配结构 |
| `annotation` | 117 | 标注与 PMI（Product Manufacturing Information） |
| `manufacturing` | 117 | 制造工艺与加工特征 |
| `resource` | 67 | 资源管理实体 |
| `validation` | 50 | 验证与检查结果表示 |
| `action` | 49 | 动作与操作链定义 |
| `classification` | 28 | 分类与编码体系 |
| `config_mgmt` | 28 | 配置管理与版本控制 |
| `tolerance` | 32 | GD&T 几何尺寸与公差 |
| `fea` | 24 | 有限元分析实体 |
| `security` | 22 | 安全分类与访问控制 |
| `log_audit` | 23 | 日志与审计追踪 |
| `document` | 21 | 文档与文件引用 |
| `organization` | 19 | 组织与人员信息 |
| `unit` | 19 | 单位定义与转换 |
| `kinematic` | 26 | 运动学副与机构状态 |
| `approval` | 13 | 审批流程与状态 |
| `backup_recovery` | 14 | 备份与恢复管理 |
| `analysis` | 15 | 分析属性与结果 |
| `date_time` | 11 | 日期与时间表示 |
| `system` | 10 | 系统级通用实体 |
| `profile` | 7 | 截面轮廓定义 |
| `topology` | 31 | STEP 拓扑实体映射 |
| `base` | 11 | 基础抽象（`StepRepresentationItem`、`StepGeometricRepresentationItem` 等） |

## STEP 支持范围

### 国际标准对比

以下对比基于 ISO 10303 官方 EXPRESS  schema（AP242 Ed2 来源：[ISO STEP Module Repository](https://standards.iso.org/iso/10303/smrl/v8/tech/smrlv8.zip)，AP203 Ed2 同理；AP214 来源：[NIST STEP File Analyzer](https://github.com/usnistgov/SFA)）。

| 标准协议 | 标准实体数 | MiniCAD 支持 | 覆盖率 | 说明 |
|----------|-----------|-------------|--------|------|
| **AP203 Ed2** | 1006 | 578 | 57.5% | 机械设计核心协议 |
| **AP214** | 929 | 525 | 56.5% | 汽车机械设计协议 |
| **AP242 Ed2** | 2122 | 735 | 34.6% | 基于 3D 工程的完整协议 |

> **注意**：MiniCAD 注册表中 1324 次 `registry.put()` 调用对应 **1231 种不同的实体类型名称**。其中约 487 种是 MiniCAD 自定义的别名/扩展名（如 `ARBITRARY_CLOSED_PROFILE_DEF`、`B_SPLINE_CURVE_2D` 等），不对应任何 ISO 10303 标准实体。实际覆盖标准实体 **735 种**（以 AP242 Ed2 为基准）。

### 已支持的实体类型（按领域）

<details>
<summary><b>3D 几何/拓扑 — 已支持（65 种核心实体）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `ADVANCED_FACE` | ✅ 完全支持 |
| `AXIS2_PLACEMENT_2D` | ✅ 完全支持 |
| `AXIS2_PLACEMENT_3D` | ✅ 完全支持 |
| `BREP_WITH_VOIDS` | ✅ 完全支持 |
| `B_SPLINE_CURVE` | ✅ 完全支持 |
| `B_SPLINE_CURVE_WITH_KNOTS` | ✅ 完全支持 |
| `B_SPLINE_SURFACE` | ✅ 完全支持 |
| `B_SPLINE_SURFACE_WITH_KNOTS` | ✅ 完全支持 |
| `CARTESIAN_POINT` | ✅ 完全支持 |
| `CIRCLE` | ✅ 完全支持 |
| `CLOSED_SHELL` | ✅ 完全支持 |
| `CLOTHOID` | ✅ 完全支持 |
| `COMPOSITE_CURVE` | ✅ 完全支持 |
| `COMPOSITE_CURVE_ON_SURFACE` | ✅ 完全支持 |
| `COMPOSITE_CURVE_SEGMENT` | ✅ 完全支持 |
| `CONICAL_SURFACE` | ✅ 完全支持 |
| `CYLINDRICAL_SURFACE` | ✅ 完全支持 |
| `DIRECTION` | ✅ 完全支持 |
| `EDGE_CURVE` | ✅ 完全支持 |
| `EDGE_LOOP` | ✅ 完全支持 |
| `ELLIPSE` | ✅ 完全支持 |
| `FACETED_BREP` | ✅ 完全支持 |
| `FACE_BOUND` | ✅ 完全支持 |
| `HYPERBOLA` | ✅ 完全支持 |
| `LINE` | ✅ 完全支持 |
| `MANIFOLD_SOLID_BREP` | ✅ 完全支持 |
| `OFFSET_CURVE_2D` | ✅ 完全支持 |
| `OFFSET_CURVE_3D` | ✅ 完全支持 |
| `OFFSET_SURFACE` | ✅ 完全支持 |
| `OPEN_SHELL` | ✅ 完全支持 |
| `ORIENTED_CLOSED_SHELL` | ✅ 完全支持 |
| `ORIENTED_EDGE` | ✅ 完全支持 |
| `ORIENTED_OPEN_SHELL` | ✅ 完全支持 |
| `PARABOLA` | ✅ 完全支持 |
| `PCURVE` | ✅ 完全支持 |
| `PLANE` | ✅ 完全支持 |
| `POLYLINE` | ✅ 完全支持 |
| `RATIONAL_B_SPLINE_CURVE` | ✅ 完全支持 |
| `RATIONAL_B_SPLINE_SURFACE` | ✅ 完全支持 |
| `SEAM_CURVE` | ✅ 完全支持 |
| `SPHERICAL_SURFACE` | ✅ 完全支持 |
| `SURFACE_CURVE` | ✅ 完全支持 |
| `SURFACE_CURVE_SWEPT_AREA_SOLID` | ✅ 部分支持（B-Rep 生成） |
| `SURFACE_OF_LINEAR_EXTRUSION` | ✅ 完全支持 |
| `SURFACE_OF_REVOLUTION` | ✅ 完全支持 |
| `TOROIDAL_SURFACE` | ✅ 完全支持 |
| `TRIMMED_CURVE` | ✅ 完全支持 |
| `VECTOR` | ✅ 完全支持 |
| `VERTEX_POINT` | ✅ 完全支持 |
</details>

<details>
<summary><b>CSG/Swept Solid/Tessellated — 已支持（20 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `BOOLEAN_RESULT` | ✅ 半空间裁剪 |
| `BLOCK` | ✅ 完全支持 |
| `SPHERE` | ✅ 完全支持 |
| `ELLIPSOID` | ✅ 完全支持 |
| `TORUS` | ✅ 完全支持 |
| `EXTRUDED_AREA_SOLID` | ✅ 完全支持 |
| `REVOLVED_AREA_SOLID` | ✅ 完全支持 |
| `HALF_SPACE_SOLID` | ✅ 裁剪支持 |
| `BOXED_HALF_SPACE` | ✅ 裁剪支持 |
| `TESSELLATED_FACE_SET` | ✅ 三角网格 B-Rep |
| `TESSELLATED_FACE` | ✅ 三角网格 B-Rep |
| `TRIANGULATED_FACE` | ✅ 三角网格 B-Rep |
</details>

<details>
<summary><b>Product/Assembly — 已支持（12 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `PRODUCT` | ✅ 完全支持 |
| `PRODUCT_DEFINITION` | ✅ 完全支持 |
| `PRODUCT_DEFINITION_FORMATION` | ✅ 完全支持 |
| `PRODUCT_DEFINITION_SHAPE` | ✅ 完全支持 |
| `NEXT_ASSEMBLY_USAGE_OCCURRENCE` | ✅ 完全支持 |
| `CONTEXT_DEPENDENT_SHAPE_REPRESENTATION` | ✅ 完全支持 |
| `APPLICATION_CONTEXT` | ✅ 完全支持 |
| `REPRESENTATION` | ✅ 完全支持 |
| `SHAPE_REPRESENTATION` | ✅ 完全支持 |
| `REPRESENTATION_MAP` | ✅ 完全支持 |
| `MAPPED_ITEM` | ✅ 完全支持 |
| `ITEM_DEFINED_TRANSFORMATION` | ✅ 完全支持 |
</details>

<details>
<summary><b>PMI/Annotation — 已支持（10 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `ANNOTATION_FILL_AREA` | ✅ 预览支持 |
| `ANNOTATION_PLANE` | ✅ 完全支持 |
| `DRAUGHTING_CALLOUT` | ✅ 完全支持 |
| `DRAUGHTING_PRE_DEFINED_COLOUR` | ✅ 完全支持 |
| `COLOUR_RGB` | ✅ 完全支持 |
| `STYLED_ITEM` | ✅ 完全支持 |
| `PRESENTATION_STYLE_ASSIGNMENT` | ✅ 完全支持 |
| `PRESENTATION_LAYER_ASSIGNMENT` | ✅ 完全支持 |
| `TERMINATOR_SYMBOL` | ✅ 预览支持 |
| `MEASURE_REPRESENTATION_ITEM` | ✅ 完全支持 |
</details>

<details>
<summary><b>GD&T 公差 — 已支持（8 种）</b></summary>

| 标准实体名 | 支持状态 |
|-----------|---------|
| `GEOMETRIC_TOLERANCE` | ✅ 预览支持（含变体） |
| `DATUM_REFERENCE_MODIFIER` | ✅ 完全支持 |
| `RUNOUT_ZONE_DEFINITION_ORIENTATION` | ✅ 完全支持 |
| `FLATNESS_TOLERANCE` | ✅ 完全支持 |
| `CYLINDRICITY_TOLERANCE` | ✅ 完全支持 |
| `CIRCULAR_RUNOUT_TOLERANCE` | ✅ 完全支持 |
| `PERPENDICULARITY_TOLERANCE` | ✅ 完全支持 |
| `POSITION_TOLERANCE` | ✅ 完全支持 |
</details>

### 未支持的 AP242 Ed2 实体（按领域分类，1387 种）

<details>
<summary><b>3D 几何/拓扑 — 未支持（211 种）</b></summary>

主要包括：
- B-Spline 变体：`B_SPLINE_SURFACE_PATCH`, `B_SPLINE_SURFACE_STRIP`, `B_SPLINE_BASIS`, `B_SPLINE_FUNCTION`
- 边界曲线：`BOUNDARY_CURVE_OF_B_SPLINE_OR_RECTANGULAR_COMPOSITE_SURFACE`, `BOUNDED_PCURVE`, `BOUNDED_SURFACE_CURVE`
- 曲线变体：`B_SPLINE_CURVE_SEGMENT`, `REPARAMETRISED_B_SPLINE_CURVE_SEGMENT`, `INDEXED_POLY_CURVE`
- 曲面变体：`TWO_DIRECTIONAL_DERIVATIVE_SURFACE`, `TANGENTIAL_SURFACE`, `RECTANGULAR_COMPOSITE_SURFACE`, `BLENDED_SURFACE`
- 扫掠体：`GENERALIZED_SWEEP`, `SWEEPING_SURFACE`, `SWEEPING_SOLID`
- Voxel/体素：`BEZIER_VOLUME`
- 退化/奇异：`DEGENERATE_BSPLINE_CURVE`, `DEGENERATE_BSPLINE_SURFACE`
- 其他：`SURFACE_STYLE_FILL_AREA`, `FILL_AREA_STYLE_COLOUR`, `ANNOTATION_FILL_AREA_OCCURRENCE`
</details>

<details>
<summary><b>Product/Assembly — 未支持（98 种）</b></summary>

主要包括：
- 产品变体：`PRODUCT_DEFINITION_FOR_ASSOCIATIVE_DRAFTING`, `PRODUCT_RELATED_PRODUCT_CATEGORY`
- 装配：`ALL_PRODUCT_CONTEXT`, `PRODUCT_DEFINITION_SUBSTITUTION`
- 配置：`EFFECTIVITY`, `DATE_EFFECTIVITY`, `LOT_EFFECTIVITY`, `SERIAL_EFFECTIVITY`
- 溯源：`TRACEABILITY`
</details>

<details>
<summary><b>Kinematic 运动学 — 未支持（41 种）</b></summary>

主要包括：
- 运动副：`PLANAR_PAIR`, `SPHERICAL_PAIR`, `CYLINDRICAL_PAIR`, `SCREW_PAIR` 等
- 机构：`KINEMATIC_MODEL`, `KINEMATIC_JOINT`, `KINEMATIC_PAIR_INSTANCE`
- 运动学路径：`KINEMATIC_LINK_PATH`, `KINEMATIC_MECHANISM`
</details>

<details>
<summary><b>FEA 有限元 — 未支持（62 种）</b></summary>

主要包括：
- 单元：`FINITE_ELEMENT_GEOMETRIC_REAL`, `FINITE_ELEMENT_MODEL`, `FEA_MESH`
- 结果：`FEA_VOLUME_RESULT`, `FEA_NODAL_RESULT`
- 材料：`FEA_MATERIAL`, `FEA_MATERIAL_PROPERTY`
- 载荷/约束：`FEA_LOAD`, `FEA_BOUNDARY_CONDITION`
</details>

<details>
<summary><b>Tolerance/GD&T — 未支持（55 种）</b></summary>

主要包括：
- 公差变体：`TOLERANCE_VALUE`, `GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT`
- 基准：`DATUM_REFERENCE`, `DATUM_TARGET`, `DATUM_FEATURE_CALLOUT`
- 修饰符：`MODIFIED_GEOMETRIC_TOLERANCE`, `MAXIMUM_MATERIAL_REQUIREMENT`
</details>

<details>
<summary><b>Presentation/Style — 未支持（47 种）</b></summary>

主要包括：
- 文本样式：`TEXT_STYLE_WITH_BOX_CHARACTERISTICS`, `TEXT_STYLE_FONT_MODEL`
- 表面纹理：`SURFACE_TEXTURE`, `SURFACE_CONDITION`, `MACHINING_ALLOWED_VALUE`
- 颜色/图层：`FILL_AREA_STYLE`, `CURVE_STYLE`
</details>

<details>
<summary><b>A3M 等效性检测 — 未支持（59 种）</b></summary>

AP242 Ed2 新增的 3D 模型等效性检测实体（`A3M_*`, `A3MA_*`, `A3MS_*` 系列），用于模型比较和验证。
</details>

<details>
<summary><b>其他未支持领域汇总</b></summary>

| 领域 | 未支持数量 | 说明 |
|------|-----------|------|
| Unit/Measure | 26 | 各类单位定义（`VOLUME_UNIT`, `MASS_UNIT` 等） |
| Configuration | 18 | 配置管理（`CONFIGURATION_EFFECTIVITY` 等） |
| Date/Time | 12 | 日期时间实体 |
| Organization/Person | 10 | 组织人员管理 |
| Approval/Contract | 7 | 审批合同 |
| Profile | 14 | 截面轮廓定义 |
| Manufacturing | 5 | 制造工艺实体 |
| Workflow | 5 | 工作流实体 |
| Representation/Context | 6 | 表示上下文 |
| Resource | 8 | 资源管理 |
| Action | 18 | 动作实体 |
| Analysis | 4 | 分析属性 |
| Validation | 3 | 验证结果 |
| Document | 4 | 文档引用 |
| Security | 2 | 安全分类 |
| 3D 几何/拓扑（其他）| 663 | 其他几何拓扑实体 |
</details>

### 数据来源

- **AP242 Ed2**: ISO 10303-242:2020 EXPRESS schema (`mim_lf.exp`), 来源: [ISO STEP Module Repository](https://standards.iso.org/iso/10303/smrl/v8/tech/smrlv8.zip)
- **AP203 Ed2**: ISO 10303-203 EXPRESS schema (`mim_lf.exp`), 同上
- **AP214**: NIST STEP File Analyzer 实体列表, 来源: [usnistgov/SFA](https://github.com/usnistgov/SFA)

### 几何层

**3D 曲线** (`Curve3`，sealed interface，13 种)：
- `Line3`, `Circle`, `Ellipse3`, `Hyperbola3`, `Parabola3`, `Clothoid3`
- `Polyline3`, `CompositeCurve3`, `TrimmedCurve3`
- `BSplineCurve3`, `RationalBSplineCurve3`
- `SurfaceCurve3`, `DegenerateCurve3`

**3D 曲面** (`SurfaceGeometry`，sealed interface，16 种)：
- `Plane`, `CylindricalSurface`, `ConicalSurface`, `ToroidalSurface`, `SphericalSurface`
- `BSplineSurface3`, `RationalBSplineSurface3`
- `SurfaceOfLinearExtrusion3`, `SurfaceOfRevolution3`, `RuledSurface3`
- `OffsetSurface3`, `SurfaceOfConstantRadius3`
- `ParaboloidSurface`, `HyperboloidSurface`, `SurfaceOfTranslation3`, `SurfaceOfProjection3`

**2D 参数域几何** (16 种)：
- `Point2`, `Vector2`, `Direction2`, `BoundingBox2`
- `Line2`, `Circle2`, `Ellipse2`, `Hyperbola2`, `Parabola2`
- `Polyline2`, `CompositeCurve2`, `TrimmedCurve2`
- `BSplineCurve2`, `RationalBSplineCurve2`, `Curve2`, `DegenerateCurve2`

**基础几何类型**：
- `CartesianPoint`, `Vector3`, `Direction3`, `Axis2Placement3D`
- `Transformation3`, `BoundingBox3`

### 拓扑层

完整 B-Rep 拓扑支持：
- `Vertex`, `Edge`, `OrientedEdge`, `EdgeLoop`, `VertexLoop`, `PolyLoop`
- `FaceBound`, `Face`, `Shell`, `Solid`

### STEP 实体类型覆盖

**基础几何/拓扑** (核心)：
- `CARTESIAN_POINT`, `DIRECTION`, `VECTOR`
- `AXIS2_PLACEMENT_3D`, `AXIS2_PLACEMENT_2D`, `AXIS1_PLACEMENT`
- `LINE`, `POLYLINE`, `CIRCLE`, `ELLIPSE`, `HYPERBOLA`, `PARABOLA`
- `TRIMMED_CURVE`, `COMPOSITE_CURVE`, `COMPOSITE_CURVE_ON_SURFACE`
- `B_SPLINE_CURVE_WITH_KNOTS`, `RATIONAL_B_SPLINE_CURVE`, `BEZIER_CURVE`
- `SURFACE_CURVE`, `SEAM_CURVE`, `PCURVE`, `OFFSET_CURVE_3D`, `OFFSET_CURVE_2D`
- `PLANE`, `CYLINDRICAL_SURFACE`, `CONICAL_SURFACE`, `TOROIDAL_SURFACE`, `SPHERICAL_SURFACE`
- `B_SPLINE_SURFACE_WITH_KNOTS`, `RATIONAL_B_SPLINE_SURFACE`
- `SURFACE_OF_LINEAR_EXTRUSION`, `SURFACE_OF_REVOLUTION`, `OFFSET_SURFACE`
- `RECTANGULAR_TRIMMED_SURFACE`, `CURVE_BOUNDED_SURFACE`
- `VERTEX_POINT`, `EDGE_CURVE`, `ORIENTED_EDGE`, `SUBEDGE`
- `EDGE_LOOP`, `VERTEX_LOOP`, `POLY_LOOP`
- `FACE_BOUND`, `FACE_OUTER_BOUND`, `ADVANCED_FACE`, `FACE_SURFACE`, `ORIENTED_FACE`
- `OPEN_SHELL`, `CLOSED_SHELL`, `ORIENTED_OPEN_SHELL`, `ORIENTED_CLOSED_SHELL`
- `MANIFOLD_SOLID_BREP`, `BREP_WITH_VOIDS`
- `SHELL_BASED_SURFACE_MODEL`, `FACE_BASED_SURFACE_MODEL`

**CSG/Swept Solid/Profile/Tessellated**：
- `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`, `CSG_SOLID`, `SOLID_REPLICA`
- `BLOCK`, `SPHERE`, `ELLIPSOID`, `RIGHT_CIRCULAR_CYLINDER`, `TORUS`, `RIGHT_ANGULAR_WEDGE`
- `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`
- `SURFACE_CURVE_SWEPT_AREA_SOLID`
- `EXTRUDED_AREA_SOLID_TAPERED`, `REVOLVED_AREA_SOLID_TAPERED`
- `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `POLYGONAL_BOUNDED_HALF_SPACE`, `BOX_DOMAIN`
- `SWEPT_DISK_SOLID`
- `CIRCLE_PROFILE_DEF`, `RECTANGLE_PROFILE_DEF`, `ELLIPSE_PROFILE_DEF`
- `ARBITRARY_CLOSED_PROFILE_DEF`, `ARBITRARY_PROFILE_DEF`
- `EXTRUDED_FACE_SOLID`, `REVOLVED_FACE_SOLID`, `SWEPT_FACE_SOLID`
- `TESSELLATED_FACE_SET`, `TESSELLATED_FACE` → 三角网格 B-Rep
- `TRIANGULATED_FACE`, `COMPLEX_TRIANGULATED_FACE`, `CUBIC_BEZIER_TRIANGULATED_FACE` → 三角网格 B-Rep
- `CYLINDER_VOLUME`, `SPHERE_VOLUME`, `TORUS_VOLUME`, `PRISM_VOLUME`, `CSG_SOLID`

**Representation/Context/Unit**：
- `REPRESENTATION`, `SHAPE_REPRESENTATION`, `REPRESENTATION_MAP`, `MAPPED_ITEM`
- `REPRESENTATION_CONTEXT`, `GEOMETRIC_REPRESENTATION_CONTEXT`
- `NAMED_UNIT`, `SI_UNIT`, `CONTEXT_DEPENDENT_UNIT`, `CONVERSION_BASED_UNIT`, `DERIVED_UNIT`
- `MEASURE_WITH_UNIT`, `UNCERTAINTY_MEASURE_WITH_UNIT`

**Product/Assembly**：
- `APPLICATION_CONTEXT`, `APPLICATION_PROTOCOL_DEFINITION`
- `PRODUCT_CONTEXT`, `PRODUCT`, `PRODUCT_DEFINITION`, `PRODUCT_DEFINITION_FORMATION`
- `PRODUCT_DEFINITION_SHAPE`, `SHAPE_DEFINITION_REPRESENTATION`
- `NEXT_ASSEMBLY_USAGE_OCCURRENCE`, `CONTEXT_DEPENDENT_SHAPE_REPRESENTATION`
- `ITEM_DEFINED_TRANSFORMATION`, `MAPPED_ITEM`

**颜色/样式/PMI**：
- `COLOUR_RGB`, `STYLED_ITEM`, `OVER_RIDING_STYLED_ITEM`
- `PRESENTATION_STYLE_ASSIGNMENT`, `PRESENTATION_LAYER_ASSIGNMENT`
- `ANNOTATION_TEXT_OCCURRENCE`, `ANNOTATION_CURVE_OCCURRENCE`, `ANNOTATION_PLANE`
- `ANNOTATION_FILL_AREA`, `ANNOTATION_FILL_AREA_REGION`, `FILL_AREA_WITH_OUTLINE`
- `DRAUGHTING_CALLOUT`, `DRAUGHTING_PRE_DEFINED_COLOUR`, `PRE_DEFINED_COLOUR`
- `MEASURE_REPRESENTATION_ITEM`, `TERMINATOR_SYMBOL`, `DIMENSIONAL_EXPONENT`

**GD&T 公差**：
- `GEOMETRIC_TOLERANCE` (含最大公差、定义区域单位、非均匀区域变体)
- `DATUM_REFERENCE_MODIFIER`, `DATUM_REFERENCE_MODIFIER_WITH_VALUE`
- `RUNOUT_ZONE_DEFINITION_ORIENTATION`

**运动学**：
- 14 种运动副类型（`PRISMATIC_PAIR`, `REVOLUTE_PAIR` 等）
- `MECHANISM_STATE_REPRESENTATION`, `KINEMATIC_PATH`

**有限元/网格**：
- `VOLUME_3D_ELEMENT_REPRESENTATION`, `FEA_MATERIAL_PROPERTY_REPRESENTATION`
- `ELEMENT_VOLUME_2D`, `ELEMENT_VOLUME_3D`, `NODE_SET`, `ELEMENT_SET`

**元数据/组织/审批**：
- `PERSON`, `ORGANIZATION`, `PERSON_AND_ORGANIZATION`
- `APPROVAL`, `APPROVAL_STATUS`, `APPROVAL_ROLE`
- `SECURITY_CLASSIFICATION`, `CERTIFICATION`, `CONTRACT`, `DOCUMENT`
- `DATE_AND_TIME`, `CALENDAR_DATE`, `LOCAL_TIME`
- `GROUP`, `CLASSIFICATION_ASSIGNMENT`, `IDENTIFICATION_ASSIGNMENT`

## 项目结构

```
src/main/java/com/minicad/
├── common/           -- 公共异常类和工具类 (8 个)
├── geometry/         -- 3D 几何类型 (37 个)
│   ├── Curve3        -- 曲线密封接口 (13 种实现)
│   └── SurfaceGeometry -- 曲面密封接口 (16 种实现)
├── geometry2d/       -- 2D 参数域几何类型 (16 个)
├── topology/         -- B-Rep 拓扑类型 (11 个)
├── step/
│   ├── syntax/       -- STEP 语法解析器 (5 个)
│   ├── semantic/     -- STEP 语义解析器 (6 个)
│   └── model/        -- STEP 实体模型类 (1175 个, 26 个子包)
│       ├── base/         -- 基础抽象 (11)
│       ├── geometry/     -- 几何实体 (115)
│       ├── topology/     -- 拓扑实体 (31)
│       ├── product/      -- 产品/装配 (107)
│       ├── annotation/   -- 标注/PMI (117)
│       ├── manufacturing/-- 制造工艺 (117)
│       ├── tolerance/    -- GD&T 公差 (32)
│       ├── unit/         -- 单位定义 (19)
│       ├── kinematic/    -- 运动学 (26)
│       ├── fea/          -- 有限元 (24)
│       ├── workflow/     -- 工作流 (199)
│       ├── validation/   -- 验证 (50)
│       ├── action/       -- 动作 (49)
│       ├── classification/ -- 分类 (28)
│       ├── config_mgmt/  -- 配置管理 (28)
│       ├── security/     -- 安全 (22)
│       ├── resource/     -- 资源 (67)
│       ├── organization/ -- 组织 (19)
│       ├── date_time/    -- 日期时间 (11)
│       ├── document/     -- 文档 (21)
│       ├── approval/     -- 审批 (13)
│       ├── log_audit/    -- 日志审计 (23)
│       ├── backup_recovery/ -- 备份恢复 (14)
│       ├── analysis/     -- 分析 (15)
│       ├── profile/      -- 截面轮廓 (7)
│       └── system/       -- 系统级 (10)
└── app/              -- 应用入口 (13 个)

examples/             -- STEP 示例文件
```

## 构建与运行

```bash
mvn test                                    # 运行全部测试
mvn -q test                                 # 安静模式运行测试
mvn clean test                              # 清理后重新构建
mvn exec:java -Dexec.args="examples/minimal-square.step"  # CLI 解析 STEP 文件
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp exec:java  # 启动 Web 预览器
```

## Web 预览器

启动后访问 http://127.0.0.1:8080，支持：

- 上传 `.step` / `.stp` 文件
- 直接粘贴 STEP 文本
- 加载 `examples/` 目录中的示例文件
- 点击面、边、装配实例查看详细信息
- 查看颜色、图层、PMI 和 validation report

## 技术栈

- Java 21
- Maven
- JUnit 5
- Jetty 11 (嵌入式 Web 服务器)
- Three.js (前端渲染)
- 无外部 CAD 内核依赖 (不依赖 OpenCascade、FreeCAD、Parasolid 等)

## 当前限制

### 几何求值已知约束

- **CSG 实体 Solid-Solid Boolean**: `BOOLEAN_RESULT` 中两个有界实体的布尔运算（如球体减圆柱）需要网格布尔内核——未实现。仅实现了半空间裁剪（`HALF_SPACE_SOLID` / `BOXED_HALF_SPACE` 的差集/交集/并集）。
- **B-Spline 曲面修剪**: 修剪曲线在 B-Spline 曲面上使用 UV 投影；复杂多循环修剪可能产生伪影。
- **退化边**: 零长度边可能在拓扑构造期间失败。

### 尚未实现 B-Rep 生成的实体

以下 STEP AP214/AP242 实体类型已注册但无 B-Rep 生成：

**高级 PMI/公差**:
- `GEOMETRIC_TOLERANCE_RELATIONSHIP` - 几何公差关系
- `DATUM_SYSTEM` - 基准系统（多基准组合）

**Validation Property 框架**:
- `VALIDATION_RESULT_REPRESENTATION` - 验证结果表示

### 工业文件导入状态

| 文件 | 实体数 | solids | 未支持面数 | 备注 |
|------|--------|--------|-----------|------|
| `engine.stp` | 93829 | 31 | 0 | 顶点投影容差处理源数据 ~1mm 精度问题 |
| `fan.stp` | 41707 | 1 | 0 | - |

## 开发原则

- **工业级可靠性**: 所有支持必须真实可运行，经过实际 STEP 文件测试
- **显式失败**: 所有不支持必须显式失败（抛出 `UnsupportedStepEntityException` 或 `UnsupportedGeometryException`），不静默忽略
- **代码显式优于泛化框架**: 保持代码清晰可读，避免过度抽象
- **渐进式支持**: 优先支持最常见的 STEP 实体类型，逐步扩展支持范围

## 路线图

### 近期目标
- 完善 B-Spline 曲面修剪支持
- 实现基本的布尔运算几何求值
- 支持更多 PMI 实体类型

### 中期目标
- 实现 Swept Solid 的几何求值
- 支持完整的 validation property 框架
- 添加拓扑修复/healing 功能

### 远期目标
- 支持所有 STEP AP214/AP242 核心实体
- 实现完整的几何内核功能
- 支持 STEP 文件导出能力
