# java-cad-kernel

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

已支持：

- `CartesianPoint`
- `Vector3`
- `Direction3`
- `Axis2Placement3D`
- `Curve3`
- `Line3`
- `Plane`
- `Circle`
- `CylindricalSurface`

### 拓扑层

已支持：

- `Vertex`
- `Edge`
- `OrientedEdge`
- `EdgeLoop`
- `FaceBound`
- `Face`
- `Shell`
- `Solid`

### STEP 语法层

已支持：

- `DATA; ... ENDSEC;`
- 实体实例，例如 `#10=CARTESIAN_POINT(...);`
- 引用 `#id`
- 数字
- 字符串
- 枚举值，例如 `.T.` / `.F.`
- 省略值 `$`
- 列表 `( ... )`

### STEP 语义层

当前支持以下最小实体子集：

- `CARTESIAN_POINT`
- `DIRECTION`
- `VECTOR`
- `AXIS2_PLACEMENT_3D`
- `LINE`
- `PLANE`
- `CIRCLE`
- `CYLINDRICAL_SURFACE`
- `VERTEX_POINT`
- `EDGE_CURVE`
- `ORIENTED_EDGE`
- `EDGE_LOOP`
- `FACE_BOUND`
- `FACE_OUTER_BOUND`
- `ADVANCED_FACE`
- `OPEN_SHELL`
- `CLOSED_SHELL`
- `MANIFOLD_SOLID_BREP`

## 重要限制

请特别注意，当前支持是**最小实现**，不是通用支持：

- `AXIS2_PLACEMENT_3D` 目前要求显式提供 `axis` 和 `ref_direction`
- `ADVANCED_FACE` 语义层目前支持 `PLANE` 和 `CYLINDRICAL_SURFACE`
- 内部拓扑构建目前支持由 `LINE` 和 `CIRCLE` 支撑的 `EDGE_CURVE`
- `CIRCLE` 目前支持：
  - STEP 语义解析
  - 内部几何对象构建
  - 作为拓扑边进行构建
- `CYLINDRICAL_SURFACE` 目前支持：
  - STEP 语义解析
  - 内部几何对象构建
  - 作为 `ADVANCED_FACE` 的语义几何被识别
  - 但**不支持** `ADVANCED_FACE` 构面与浏览器预览
- `ORIENTED_EDGE` 当前只支持 `$,$,#edge,.T./.F.` 这种最小形式
- parser 的错误位置目前是相对于提取出的 `DATA` 段，不是整文件绝对位置

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
- 在页面中切换并加载 `minimal-square` 与 `plate-with-round-hole` 示例
- 点击模型中的面或边查看基础选中信息

当前预览范围：

- 直线和圆弧 `EDGE_CURVE` 线框
- 平面 `ADVANCED_FACE` 面片
- 基于当前支持范围的 shell / solid 结构统计
- 对暂不支持渲染的面返回 `unsupportedFaceCount`

说明：

- Three.js 已随项目静态资源一起提供，浏览器离线也可以打开页面
- `CYLINDRICAL_SURFACE` 当前只做到：
  - 语义解析
  - 内部几何对象构建
  - 在 CLI / Web 统计中识别并计入 unsupported face
- 当前仍然不支持圆柱/圆锥曲面的构面或网格化、NURBS，以及复杂工业级修剪面

## 开发说明

- 当前实现刻意保持显式，而不是做复杂泛化
- 不支持的内容应该抛出清晰异常
- 如果某项能力接近工业 CAD 复杂度，应该优先缩小范围，而不是伪实现
