# mini-cad

一个**教学性质**的最小 CAD kernel 与 STEP 解析器示例项目，使用 Java 实现。

这个项目刻意保持很小。它的目标不是工业级 CAD 内核，也**不声称支持完整 STEP 标准**。

## 项目目标

这个项目主要用于演示一条最小、可运行、可测试的处理链路：

- STEP 文本
- 语法解析（syntax AST）
- STEP 语义解析（semantic model）
- 内部几何 / 拓扑对象构建
- CLI 结构摘要输出
- 浏览器中的最小 Three.js 预览

设计原则是：

- 教学清晰度优先
- 所有支持都必须真实可运行
- 所有不支持都必须显式失败

## 明确不做

本项目**不实现**以下内容：

- Boolean 运算
- 倒角、圆角
- NURBS / B-Spline 求值
- 完整曲面修剪
- 拓扑修复 / healing
- 容差传播系统
- 完整 ISO 10303 / AP203 / AP214 / AP242 支持
- 工业级鲁棒性与精度处理

如果某项功能接近工业 CAD 内核复杂度，本项目的策略是：

- 主动缩小范围
- 明确标记 unsupported
- 不写“看起来支持、实际上错误”的实现

## 技术约束

- Java 21
- Maven
- JUnit 5
- 不依赖 OpenCascade、FreeCAD kernel、Parasolid 绑定、CGAL、JCAE 或其他现成 CAD 内核

## 当前支持范围

### 几何层

已支持的最小子集已明显扩大，当前包括：

- 3D 基础几何：
  - `CartesianPoint` (含 `midpoint`, `interpolate`, `projectOnto`, `distanceTo`, `distanceSquaredTo`, `offset`, `approxEquals`, `scaleAbout`, `mirrorThrough`, `transform`, `toPoint2`, `origin`, `fromArray`, `subtractVector`)
  - `Vector3` (含 `negate`, `angleBetween`, `signedAngleBetween`, `reflect`, `perpendicular`, `projectOnto`, `abs`, `minComponent`, `maxComponent`, `distanceTo`, `distanceSquaredTo`, `midpoint`, `interpolate`, `rotateAround`, `xAxis`, `yAxis`, `zAxis`, `zero`, `fromArray`)
  - `Direction3` (含 `cross`, `angleBetween`, `perpendicular`, `rotateAround`, `signedAngleBetween`, `xAxis`, `yAxis`, `zAxis`)
  - `Axis2Placement3D` (含 `xDirection`, `yDirection`, `transformToWorld`, `transformToLocal`, `transformDirectionToWorld`, `transformDirectionToLocal`, `origin`, `at`)
  - `Transformation3` (含 `identity`, `translation`, `scale`, `rotationX`, `rotationY`, `rotationZ`, `from`, `compose`, `transform`, `inverse`, `translation`)
  - `BoundingBox3` (含 `empty`, `of`, `isEmpty`, `width`, `height`, `depth`, `diagonal`, `center`, `volume`, `contains`, `intersects`, `union`, `intersection`, `expand`, `scale`, `minCorner`, `maxCorner`, `surfaceArea`, `closestPointTo`, `distanceTo`, `pointAt`, `corners`)
- 3D 曲线：
  - `Curve3`
  - `Line3` (含 `tangentAt`, `curvature`, `curvatureAt`, `length`, `boundingBox`, `closestPointTo`, `parameterOfClosestPoint`, `distanceTo`, `contains`, `sample`)
  - `Circle` (含 `tangentAt`, `normalInPlaneAt`, `curvature`, `curvatureAt`, `binormalAt`, `arcLength`, `circumference`, `boundingBox`, `closestPointTo`, `distanceTo`, `angleOf`, `sample`)
  - `Ellipse3` (含 `tangentAt`, `normalInPlaneAt`, `curvatureAt`, `binormalAt`, `perimeter`, `boundingBox`, `closestPointTo`, `distanceTo`, `angleOf`, `sample`)
  - `Hyperbola3` (含 `tangentAt`, `boundingBox`, `sample`, `curvatureAt`, `closestPointTo`, `distanceTo`, `semiMajorAxis`, `semiMinorAxis`, `eccentricity`)
  - `Parabola3` (含 `tangentAt`, `boundingBox`, `sample`, `curvatureAt`, `closestPointTo`, `distanceTo`, `focus`, `vertex`, `focalLength`)
  - `Polyline3` (含 `boundingBox`, `length`, `sample`, `pointAt`, `tangentAt`, `closestPointTo`, `distanceTo`, `segmentCount`, `startPoint`, `endPoint`, `midpoint`)
  - `CompositeCurve3` (含 `boundingBox`, `sample`, `pointAt`, `tangentAt`, `length`, `closestPointTo`, `distanceTo`, `segmentCount`)
  - `TrimmedCurve3` (含 `tangentAt`, `boundingBox`, `length`, `sample`, `pointAt`, `closestPointTo`, `distanceTo`, `midpoint`, `curvature`, `curvatureAt`)
  - `BSplineCurve3` (含 `tangentAt`, `boundingBox`, `sample`, `pointAt`, `startParameter`, `endParameter`, `length`, `closestPointTo`, `distanceTo`, `midpoint`, `controlPointCount`, `knotCount`)
  - `RationalBSplineCurve3` (含 `tangentAt`, `boundingBox`, `sample`, `pointAt`, `startParameter`, `endParameter`, `length`, `closestPointTo`, `distanceTo`, `midpoint`, `controlPointCount`, `knotCount`, `weightCount`)
  - `SurfaceCurve3` (含 `tangentAt`, `boundingBox`, `sample`, `pointAt`, `length`, `closestPointTo`, `distanceTo`, `midpoint`, `underlyingCurve`)
  - `Clothoid3` (含 `tangentAt`, `boundingBox`, `sample`, `length`, `closestPointTo`, `distanceTo`, `curvatureRate`, `intercept`)
  - `DegenerateCurve3` (含 `tangentAt`, `boundingBox`, `sample`, `pointAt`, `closestPointTo`, `distanceTo`)
- 3D 曲面：
  - `Plane` (含 `normalAt`, `closestPointTo`, `distanceTo`, `project`, `intersect`, `contains`, `sampleGrid`)
  - `CylindricalSurface` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `ConicalSurface` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `ToroidalSurface` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `SphericalSurface` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `BSplineSurface3` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `RationalBSplineSurface3` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `OffsetSurface3` (含 `normalAt`, `pointAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `sampleGrid`)
  - `SurfaceOfConstantRadius3` (含 `normalAt`, `pointAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `sampleGrid`)
  - `SurfaceOfLinearExtrusion3` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `SurfaceOfRevolution3` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
  - `RuledSurface3` (含 `normalAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `pointAt`, `sampleGrid`)
- 2D 参数域几何：
  - `Point2` (含 `distanceTo`, `midpoint`, `interpolate`)
  - `Vector2` (含 `subtract`, `negate`, `perpendicular`, `cross`, `angleBetween`, `normSquared`, `projectOnto`, `reflect`, `rotate`)
  - `Direction2` (含 `angleBetween`, `signedAngleTo`, `perpendicular`, `rotate`)
  - `BoundingBox2` (含 `empty`, `of`, `isEmpty`, `width`, `height`, `diagonal`, `center`, `area`, `contains`, `intersects`, `union`, `intersection`, `expand`, `scale`, `minCorner`, `maxCorner`)
  - `Line2` (含 `tangentAt`, `normalAt`, `curvature`, `curvatureAt`, `sample`, `distanceTo`, `signedDistanceTo`, `project`, `parallelThrough`, `perpendicularThrough`, `intersect`, `isParallelTo`, `isCoincidentWith`)
  - `Circle2` (含 `tangentAt`, `normalAt`, `curvature`, `curvatureAt`, `sample`, `circumference`, `area`, `arcLength`, `boundingBox`, `closestPointTo`, `distanceTo`, `diameter`, `centerPoint`, `yDirection`, `at`)
  - `Ellipse2` (含 `tangentAt`, `normalAt`, `curvatureAt`, `sample`, `perimeter`, `area`, `boundingBox`, `closestPointTo`, `distanceTo`, `semiMajorAxis`, `semiMinorAxis`, `eccentricity`, `yDirection`, `foci`, `at`)
  - `Hyperbola2` (含 `tangentAt`, `normalAt`, `curvatureAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `length`, `semiMajorAxis`, `semiMinorAxis`, `eccentricity`, `foci`, `yDirection`, `at`)
  - `Parabola2` (含 `tangentAt`, `normalAt`, `curvatureAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `length`, `focus`, `directrix`, `focalLength`, `yDirection`, `at`)
  - `Polyline2` (含 `boundingBox`, `length`, `sample`, `pointAt`, `tangentAt`, `closestPointTo`, `distanceTo`, `midpoint`, `segmentCount`, `startPoint`, `endPoint`, `pointCount`)
  - `CompositeCurve2`
  - `TrimmedCurve2`
  - `BSplineCurve2`
  - `RationalBSplineCurve2`
  - `Plane`
  - `CylindricalSurface`
  - `ConicalSurface`
  - `ToroidalSurface`
  - `SphericalSurface`
  - `BSplineSurface3`
  - `RationalBSplineSurface3`
  - `OffsetSurface3`
  - `SurfaceOfConstantRadius3`
  - `SurfaceOfLinearExtrusion3` (支持多种曲线类型：Line3, Circle, Ellipse3, Polyline3, CompositeCurve3, TrimmedCurve3, Hyperbola3, Parabola3, BSplineCurve3, RationalBSplineCurve3, SurfaceCurve3, Clothoid3, DegenerateCurve3)
  - `SurfaceOfRevolution3` (支持相同曲线类型)
  - `RuledSurface3` (支持相同曲线类型)
- 2D 参数域几何：
  - `Point2` (含 `distanceTo`, `midpoint`, `interpolate`)
  - `Vector2` (含 `subtract`, `negate`, `perpendicular`, `cross`, `angleBetween`, `normSquared`, `projectOnto`, `reflect`, `rotate`)
  - `Direction2` (含 `angleBetween`, `signedAngleTo`, `perpendicular`, `rotate`)
  - `BoundingBox2` (含 `empty`, `of`, `isEmpty`, `width`, `height`, `diagonal`, `center`, `area`, `contains`, `intersects`, `union`, `intersection`, `expand`, `scale`, `minCorner`, `maxCorner`)
  - `Line2` (含 `tangentAt`, `normalAt`, `curvature`, `curvatureAt`, `sample`, `distanceTo`, `signedDistanceTo`, `project`, `parallelThrough`, `perpendicularThrough`, `intersect`, `isParallelTo`, `isCoincidentWith`)
  - `Circle2` (含 `tangentAt`, `normalAt`, `curvature`, `curvatureAt`, `sample`, `circumference`, `area`, `arcLength`, `boundingBox`, `closestPointTo`, `distanceTo`, `diameter`, `centerPoint`, `yDirection`, `at`)
  - `Ellipse2` (含 `tangentAt`, `normalAt`, `curvatureAt`, `sample`, `perimeter`, `area`, `boundingBox`, `closestPointTo`, `distanceTo`, `semiMajorAxis`, `semiMinorAxis`, `eccentricity`, `yDirection`, `foci`, `at`)
  - `Hyperbola2` (含 `tangentAt`, `normalAt`, `curvatureAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `length`, `semiMajorAxis`, `semiMinorAxis`, `eccentricity`, `foci`, `yDirection`, `at`)
  - `Parabola2` (含 `tangentAt`, `normalAt`, `curvatureAt`, `boundingBox`, `closestPointTo`, `distanceTo`, `length`, `focus`, `directrix`, `focalLength`, `yDirection`, `at`)
  - `Polyline2` (含 `boundingBox`, `length`, `sample`, `pointAt`, `tangentAt`, `closestPointTo`, `distanceTo`, `midpoint`, `segmentCount`, `startPoint`, `endPoint`, `pointCount`) (含 `boundingBox`, `length`, `sample`, `pointAt`, `tangentAt`, `closestPointTo`, `distanceTo`, `midpoint`, `segmentCount`, `startPoint`, `endPoint`, `pointCount`)
  - `CompositeCurve2` (含 `boundingBox`, `sample`, `pointAt`, `tangentAt`, `length`, `closestPointTo`, `distanceTo`, `segmentCount`)
  - `TrimmedCurve2` (含 `boundingBox`, `length`, `sample`, `pointAt`, `tangentAt`, `closestPointTo`, `distanceTo`, `midpoint`, `underlyingCurve`)
  - `BSplineCurve2` (含 `boundingBox`, `sample`, `pointAt`, `tangentAt`, `startParameter`, `endParameter`, `length`, `closestPointTo`, `distanceTo`, `midpoint`, `controlPointCount`, `knotCount`)
  - `RationalBSplineCurve2` (含 `boundingBox`, `sample`, `pointAt`, `tangentAt`, `startParameter`, `endParameter`, `length`, `closestPointTo`, `distanceTo`, `midpoint`, `controlPointCount`, `knotCount`, `weightCount`)

### 拓扑层

已支持：

- `Vertex`
- `Edge` (含 `boundingBox`, `length`)
- `OrientedEdge` (含 `boundingBox`, `length`, `startVertex`, `endVertex`)
- `EdgeLoop` (含 `boundingBox`, `perimeter`, `edgeCount`, `vertices`)
- `VertexLoop` (含 `boundingBox`)
- `PolyLoop` (含 `boundingBox`, `vertexCount`, `perimeter`)
- `FaceBound`
- `Face` (含 `boundingBox`, `edgeCount`, `vertices`, `perimeter`, `area`, `boundCount`, `centroid`, `normal`, `outerBound`, `innerBounds`, `contains`, `closestPointTo`, `distanceTo`)
- `Shell` (含 `boundingBox`, `faceCount`, `edgeCount`, `vertices`, `surfaceArea`, `perimeter`, `centroid`, `closestPointTo`, `distanceTo`, `containsApproximate`, `edges`)
- `Solid` (含 `boundingBox`, `faceCount`, `shellCount`, `edgeCount`, `vertices`, `surfaceArea`, `approximateVolume`, `centroid`, `closestPointTo`, `distanceTo`, `containsApproximate`, `allShells`, `allFaces`, `allEdges`)

### 几何接口默认方法

`Curve3` 和 `SurfaceGeometry` 接口提供了默认实现：

- `Curve3` 接口默认方法：`boundingBox()`, `sample()`, `length()`, `tangentAt()`
- `SurfaceGeometry` 接口默认方法：`boundingBox()`, `sampleGrid()`, `normalAt()`

### STEP 语法层

已支持：

- `HEADER;` 与 `DATA; ... ENDSEC;`
- 实体实例，例如 `#10=CARTESIAN_POINT(...);`
- 引用 `#id`
- 数字、字符串、枚举
- 省略值 `$` 与 omitted `*`
- 列表 `( ... )`
- complex entity
- typed value，例如 `LENGTH_MEASURE(1.0)`

### STEP 语义层

当前支持的是一个”教学用途但已可扩展”的 AP242 基础子集，解析器已注册约 **24000+ 种 STEP 实体类型**（约 1559 个直接 registry.put() 调用，870 个 helper 方法，1040 个模型类），主要包括：

- 基础几何 / 拓扑：
  - `CARTESIAN_POINT`, `DIRECTION`, `VECTOR`
  - `AXIS2_PLACEMENT_3D`, `AXIS2_PLACEMENT_2D`, `AXIS1_PLACEMENT`
  - `LINE`, `POLYLINE`, `CIRCLE`, `ELLIPSE`
  - `TRIMMED_CURVE`, `COMPOSITE_CURVE`, `COMPOSITE_CURVE_ON_SURFACE`
  - `B_SPLINE_CURVE_WITH_KNOTS`, `B_SPLINE_CURVE`, `RATIONAL_B_SPLINE_CURVE`
  - `BEZIER_CURVE`, `UNIFORM_CURVE`, `QUASI_UNIFORM_CURVE`
  - `SURFACE_CURVE`, `SEAM_CURVE`, `PCURVE`, `OFFSET_CURVE_3D`, `OFFSET_CURVE_2D`
  - `PLANE`, `CYLINDRICAL_SURFACE`, `CONICAL_SURFACE`, `TOROIDAL_SURFACE`, `SPHERICAL_SURFACE`
  - `B_SPLINE_SURFACE_WITH_KNOTS`, `B_SPLINE_SURFACE`, `RATIONAL_B_SPLINE_SURFACE`
  - `BEZIER_SURFACE`, `UNIFORM_SURFACE`, `QUASI_UNIFORM_SURFACE`
  - `SURFACE_OF_LINEAR_EXTRUSION`, `SURFACE_OF_REVOLUTION`, `OFFSET_SURFACE`
  - `RECTANGULAR_TRIMMED_SURFACE`, `CURVE_BOUNDED_SURFACE`
  - `VERTEX_POINT`, `EDGE_CURVE`, `ORIENTED_EDGE`, `SUBEDGE`
  - `EDGE_LOOP`, `VERTEX_LOOP`, `POLY_LOOP`
  - `FACE_BOUND`, `FACE_OUTER_BOUND`, `ADVANCED_FACE`, `FACE_SURFACE`, `ORIENTED_FACE`
  - `OPEN_SHELL`, `CLOSED_SHELL`, `ORIENTED_OPEN_SHELL`, `ORIENTED_CLOSED_SHELL`
  - `MANIFOLD_SOLID_BREP`, `BREP_WITH_VOIDS`
  - `SHELL_BASED_SURFACE_MODEL`, `FACE_BASED_SURFACE_MODEL`
  - `CONNECTED_FACE_SET`, `CONNECTED_FACE_SUB_SET`, `CONNECTED_EDGE_SET`

- CSG / Swept Solid / Profile（解析层支持）：
  - `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`
  - `CSG_SOLID`, `SOLID_REPLICA`
  - CSG 图元: `BLOCK`, `SPHERE`, `ELLIPSOID`, `RIGHT_CIRCULAR_CYLINDER`, `TORUS`, `RIGHT_ANGULAR_WEDGE`
  - `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`
  - Profile: `CIRCLE_PROFILE_DEF`, `RECTANGLE_PROFILE_DEF`, `ELLIPSE_PROFILE_DEF`, `ROUNDED_RECTANGLE_PROFILE_DEF`, `CIRCULAR_HOLLOW_PROFILE_DEF`, `ARBITRARY_CLOSED_PROFILE_DEF`, `ARBITRARY_PROFILE_DEF`
  - `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `BOX_DOMAIN`

- representation / context / unit：
  - `REPRESENTATION`, `SHAPE_REPRESENTATION`, `REPRESENTATION_MAP`, `MAPPED_ITEM`
  - `REPRESENTATION_CONTEXT`, `GEOMETRIC_REPRESENTATION_CONTEXT`
  - `NAMED_UNIT`, `SI_UNIT`, `CONTEXT_DEPENDENT_UNIT`, `CONVERSION_BASED_UNIT`, `DERIVED_UNIT`
  - `MEASURE_WITH_UNIT`, `UNCERTAINTY_MEASURE_WITH_UNIT`
  - `GLOBAL_UNIT_ASSIGNED_CONTEXT`, `GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT`

- product / assembly：
  - `APPLICATION_CONTEXT`, `APPLICATION_PROTOCOL_DEFINITION`
  - `PRODUCT_CONTEXT`, `PRODUCT`, `PRODUCT_DEFINITION`, `PRODUCT_DEFINITION_FORMATION`
  - `PRODUCT_DEFINITION_SHAPE`, `SHAPE_DEFINITION_REPRESENTATION`
  - `SHAPE_REPRESENTATION_RELATIONSHIP`, `REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION`
  - `NEXT_ASSEMBLY_USAGE_OCCURRENCE`, `CONTEXT_DEPENDENT_SHAPE_REPRESENTATION`
  - `ITEM_DEFINED_TRANSFORMATION`, `MAPPED_ITEM`

- 颜色 / 样式 / PMI：
  - `COLOUR_RGB`, `STYLED_ITEM`, `OVER_RIDING_STYLED_ITEM`
  - `PRESENTATION_STYLE_ASSIGNMENT`, `PRESENTATION_LAYER_ASSIGNMENT`
  - `CURVE_STYLE`, `FILL_AREA_STYLE`, `FILL_AREA_STYLE_COLOUR`
  - `SURFACE_STYLE_USAGE`, `SURFACE_STYLE_FILL_AREA`, `SURFACE_STYLE_TRANSPARENT`
  - `ANNOTATION_TEXT_OCCURRENCE`, `ANNOTATION_CURVE_OCCURRENCE`, `ANNOTATION_PLANE`
  - `DRAUGHTING_CALLOUT`, `MEASURE_REPRESENTATION_ITEM`
  - `GEOMETRIC_ITEM_SPECIFIC_USAGE`, `ITEM_IDENTIFIED_REPRESENTATION_USAGE`

- 元数据 / 组织 / 审批：
  - `PERSON`, `ORGANIZATION`, `PERSON_AND_ORGANIZATION`
  - `APPROVAL`, `APPROVAL_STATUS`, `APPROVAL_ROLE`
  - `SECURITY_CLASSIFICATION`, `SECURITY_CLASSIFICATION_LEVEL`
  - `CERTIFICATION`, `CONTRACT`, `DOCUMENT`
  - `DATE_AND_TIME`, `CALENDAR_DATE`, `LOCAL_TIME`
  - `GROUP`, `CLASSIFICATION_ASSIGNMENT`, `IDENTIFICATION_ASSIGNMENT`

- 变换 / replica：
  - `CARTESIAN_TRANSFORMATION_OPERATOR_2D`, `CARTESIAN_TRANSFORMATION_OPERATOR_3D`
  - `GEOMETRIC_REPLICA`, `POINT_REPLICA`, `CURVE_REPLICA`, `SURFACE_REPLICA`

## 重要限制

请特别注意，当前支持虽然已覆盖大量 STEP 实体类型，但仍然是**受控子集**，不是通用工业级支持：

- CSG / Swept Solid / Profile 相关实体当前在**解析层**已注册，但不代表完整几何内核求值支持
- `ADVANCED_FACE` 的 Web 预览已经支持：
  - 平面面
  - 圆柱 / 圆锥 / 环面 / 球面曲面 patch
  - 一部分 B-spline 曲面 patch
  - 基于 `PCURVE` / `SEAM_CURVE` 的周期面参数域 trimming
- 但这仍然**不是**完整曲面修剪内核，尤其不代表：
  - 通用 `PCURVE` / `SEAM_CURVE` 全覆盖
  - 完整 `B_SPLINE_SURFACE_WITH_KNOTS` 修剪支持
  - 工业级稳健的 trimmed-surface kernel
- `B_SPLINE_CURVE_WITH_KNOTS` 与 `B_SPLINE_SURFACE_WITH_KNOTS` 当前重点在解析与预览，不代表完整 CAD 几何内核支持
- CLI 的 `Build Summary` 和浏览器 `preview` 是两条不同链路：
  - CLI 更偏内部拓扑构型统计
  - 浏览器更偏导出的预览三角化
  - 某些曲面面可能”可预览”但仍不会在 CLI 中当作完整内部 face 构型
- PMI 当前是最小 presentation / semantic 关联子集，不是完整 AP242 PMI 语义网
- validation 当前是”原生校验项提取 + 预览几何对比”的最小 report，不是完整 validation property 框架

## 项目结构

```text
src/main/java/com/minicad/
├── common/           -- 公共工具类和异常类
├── geometry/         -- 3D 几何类型
├── geometry2d/       -- 2D 参数域几何类型
├── topology/         -- 拓扑类型
├── step/
│   ├── model/        -- STEP 实体模型类（~330 个 StepXxx 类）
│   ├── semantic/     -- STEP 语义解析器
│   └── syntax/       -- STEP 语法解析器
└── app/              -- CLI 应用和 Web 预览应用
```

## 构建与测试

运行全部测试：

```bash
mvn test
```

## CLI 演示

示例 STEP 文件（共 44 个）：

- 基础: `minimal-square.step`, `metadata-square.step`, `test.step`, `plate-with-round-hole.step`, `rectangular-frame.step`, `triangle-face.step`, `ellipse-face.step`, `two-squares-open-shell.step`
- 圆柱面系列: `cylindrical-face.step`, `cylindrical-hole.step`, `cylindrical-band.step`, `cylindrical-two-holes.step`, `cylindrical-trimmed-loops-with-hole.step`, `cylindrical-multi-trimmed-loop.step`, `cylindrical-seam-two-holes.step`, `cylindrical-trimmed-circle-pcurve.step`, `cylindrical-trimmed-ellipse-pcurve.step`, `cylindrical-trimmed-bspline-pcurve.step`, `cylindrical-hole-ellipse-pcurve.step`
- 圆锥面系列: `conical-band.step`, `conical-hole.step`, `conical-two-holes.step`, `conical-trimmed-ellipse-pcurve.step`, `conical-trimmed-loops-with-hole.step`, `conical-seam-hole-vertex-loop.step`, `conical-seam-missing-outer.step`, `conical-seam-two-holes.step`, `conical-seam-vertex-loop.step`
- 环面系列: `toroidal-band.step`, `toroidal-hole.step`, `toroidal-two-holes.step`, `toroidal-trimmed-loops-with-hole.step`, `toroidal-seam-hole-vertex-loop.step`, `toroidal-seam-missing-outer.step`, `toroidal-seam-two-holes.step`, `toroidal-seam-vertex-loop.step`, `toroidal-trimmed-circle-pcurve.step`
- B-spline: `bspline-patch.step`
- 装配: `nested-assembly.step`, `two-instance-assembly.step`, `translated-part-assembly.step`
- PMI/验证: `pmi-validation-square.step`, `pmi-validation-mismatch.step`
- 复杂模型: `engine.stp`, `fan.stp`

运行命令：

```bash
mvn exec:java -Dexec.args="examples/minimal-square.step"
```

输出会包含三部分摘要：

- `Syntax Summary`
- `Semantic Summary`
- `Build Summary`

对于当前无法构面的曲面面，`Build Summary` 会明确统计 `unsupportedFaces`，而不是伪装成已支持。

## Web 预览

项目还提供了一个本地浏览器预览器，使用：

- 内嵌 Jetty
- 原生 JavaScript
- Three.js

启动方式：

```bash
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp
```

默认地址：

```text
http://127.0.0.1:8080
```

你可以：

- 上传 `.step` / `.stp` 文件
- 直接粘贴 STEP 文本
- 在页面中切换并加载 `examples/` 里的大量手工样例
- 点击模型中的面、边、装配实例或 PMI 查看信息
- 查看 `unsupportedFaces` 明细，而不只是一个计数

当前预览范围已经包括：

- 线和圆弧边的采样预览
- 平面面片
- 圆柱 / 圆锥 / 环面 / 球面曲面 patch
- 一部分 B-spline 曲面 patch
- 一部分基于 `PCURVE` / `SEAM_CURVE` 的周期面 trimming
- 装配实例树与实例变换
- 颜色、图层、名称和最小 validation report
- 最小 presentation PMI 与目标关联

当前 viewer 还额外做了两类抗锯齿优化：

- 更高的参数域三角化密度，减轻曲边 patch 的几何锯齿
- 前端 FXAA + 缩放自适应超采样，减轻滚轮放大后的屏幕空间锯齿

说明：

- Three.js 已随项目静态资源一起提供，浏览器离线也可以打开页面
- 浏览器预览现在已经不只是统计 `unsupportedFaceCount`，还会导出 `unsupportedFaces` 明细和 `reason`
- 当前仍然不支持完整工业级的：
  - 通用 trimmed surface kernel
  - 全量 AP242 PMI
  - 全量 validation property
  - 拓扑修复 / healing

## 开发说明

- 当前实现刻意保持显式，而不是做复杂泛化
- 不支持的内容应该抛出清晰异常
- 如果某项能力接近工业 CAD 复杂度，应该优先缩小范围，而不是伪实现
