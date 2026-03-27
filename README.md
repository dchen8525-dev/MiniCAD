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
  - `CartesianPoint`
  - `Vector3`
  - `Direction3`
  - `Axis2Placement3D`
- 3D 曲线：
  - `Curve3`
  - `Line3`
  - `Circle`
  - `Ellipse3`
  - `TrimmedCurve3`
  - `SurfaceCurve3`
  - `BSplineCurve3`
- 3D 曲面：
  - `Plane`
  - `CylindricalSurface`
  - `ConicalSurface`
  - `ToroidalSurface`
  - `BSplineSurface3`
- 2D 参数域几何：
  - `Point2`
  - `Vector2`
  - `Direction2`
  - `Line2`
  - `Circle2`
  - `Ellipse2`
  - `TrimmedCurve2`
  - `BSplineCurve2`

### 拓扑层

已支持：

- `Vertex`
- `Edge`
- `OrientedEdge`
- `EdgeLoop`
- `VertexLoop`
- `FaceBound`
- `Face`
- `Shell`
- `Solid`

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

当前支持的是一个“教学用途但已可扩展”的 AP242 基础子集，主要包括：

- 基础几何 / 拓扑：
  - `CARTESIAN_POINT`
  - `DIRECTION`
  - `VECTOR`
  - `AXIS2_PLACEMENT_3D`
  - `AXIS2_PLACEMENT_2D`
  - `LINE`
  - `CIRCLE`
  - `ELLIPSE`
  - `TRIMMED_CURVE`
  - `B_SPLINE_CURVE_WITH_KNOTS`
  - `SURFACE_CURVE`
  - `SEAM_CURVE`
  - `PCURVE`
  - `PLANE`
  - `CYLINDRICAL_SURFACE`
  - `CONICAL_SURFACE`
  - `TOROIDAL_SURFACE`
  - `B_SPLINE_SURFACE_WITH_KNOTS`
  - `VERTEX_POINT`
  - `EDGE_CURVE`
  - `ORIENTED_EDGE`
  - `EDGE_LOOP`
  - `VERTEX_LOOP`
  - `FACE_BOUND`
  - `FACE_OUTER_BOUND`
  - `FACE_SURFACE`
  - `ORIENTED_FACE`
  - `ADVANCED_FACE`
  - `OPEN_SHELL`
  - `CLOSED_SHELL`
  - `MANIFOLD_SOLID_BREP`
- representation / context / unit：
  - `REPRESENTATION`
  - `SHAPE_REPRESENTATION`
  - `REPRESENTATION_CONTEXT`
  - `GEOMETRIC_REPRESENTATION_CONTEXT`
  - `NAMED_UNIT`
  - `SI_UNIT`
  - `MEASURE_WITH_UNIT`
  - `UNCERTAINTY_MEASURE_WITH_UNIT`
  - `GLOBAL_UNIT_ASSIGNED_CONTEXT`
  - `GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT`
- product / assembly：
  - `APPLICATION_CONTEXT`
  - `PRODUCT_CONTEXT`
  - `PRODUCT`
  - `PRODUCT_DEFINITION_FORMATION`
  - `PRODUCT_DEFINITION_CONTEXT`
  - `PRODUCT_DEFINITION`
  - `PRODUCT_DEFINITION_SHAPE`
  - `SHAPE_DEFINITION_REPRESENTATION`
  - `SHAPE_REPRESENTATION_RELATIONSHIP`
  - `NEXT_ASSEMBLY_USAGE_OCCURRENCE`
  - `CONTEXT_DEPENDENT_SHAPE_REPRESENTATION`
  - `ITEM_DEFINED_TRANSFORMATION`
  - `REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION`
- metadata / presentation / PMI 最小子集：
  - `COLOUR_RGB`
  - `STYLED_ITEM`
  - `PRESENTATION_STYLE_ASSIGNMENT`
  - `PRESENTATION_LAYER_ASSIGNMENT`
  - `ANNOTATION_TEXT_OCCURRENCE`
  - `DRAUGHTING_CALLOUT`
  - `MEASURE_REPRESENTATION_ITEM`
  - `GEOMETRIC_ITEM_SPECIFIC_USAGE`

## 重要限制

请特别注意，当前支持虽然比最初丰富很多，但仍然是**受控子集**，不是通用工业级支持：

- `AXIS2_PLACEMENT_3D` / `AXIS2_PLACEMENT_2D` 仍按当前仓库需要的最小形式使用
- `ADVANCED_FACE` 的 Web 预览已经支持：
  - 平面面
  - 一部分圆柱 / 圆锥 / 环面 / B-spline 曲面 patch
  - 基于 `PCURVE` / `SEAM_CURVE` 的周期面参数域 trimming
- 但这仍然**不是**完整曲面修剪内核，尤其不代表：
  - 通用 `PCURVE` / `SEAM_CURVE` 全覆盖
  - 完整 `B_SPLINE_SURFACE_WITH_KNOTS` 修剪支持
  - 工业级稳健的 trimmed-surface kernel
- `B_SPLINE_CURVE_WITH_KNOTS` 与 `B_SPLINE_SURFACE_WITH_KNOTS` 当前重点在解析与预览，不代表完整 CAD 几何内核支持
- CLI 的 `Build Summary` 和浏览器 `preview` 是两条不同链路：
  - CLI 更偏内部拓扑构型统计
  - 浏览器更偏导出的预览三角化
  - 某些曲面面可能“可预览”但仍不会在 CLI 中当作完整内部 face 构型
- PMI 当前是最小 presentation / semantic 关联子集，不是完整 AP242 PMI 语义网
- validation 当前是“原生校验项提取 + 预览几何对比”的最小 report，不是完整 validation property 框架

## 项目结构

```text
src/main/java/com/minicad/common
src/main/java/com/minicad/geometry
src/main/java/com/minicad/topology
src/main/java/com/minicad/step/syntax
src/main/java/com/minicad/step/semantic
src/main/java/com/minicad/step/model
src/main/java/com/minicad/app
```

## 构建与测试

运行全部测试：

```bash
mvn test
```

## CLI 演示

示例 STEP 文件：

- [examples/minimal-square.step](/root/work/MiniCAD/examples/minimal-square.step)
- [examples/plate-with-round-hole.step](/root/work/MiniCAD/examples/plate-with-round-hole.step)

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
- 一部分圆柱 / 圆锥 / 环面曲面 patch
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
