# MiniCAD 大文件重构实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将MiniCAD项目中超过2000行的3个最大文件拆分成<2000行的小文件，提高代码可维护性。

**Architecture:** 渐进式重构策略，从简单到复杂依次拆分StepCadBuilder(7429行)、StepEntityResolver(13324行)、StepPreviewJsonExporter(14973行)。每个文件按功能模块拆分，保持单一职责原则。

**Tech Stack:** Java 11, JUnit Jupiter, Maven, STEP文件处理

---

## 文件结构

### Phase 1: StepCadBuilder拆分 (8个文件)

| 文件 | 职责 | 预估行数 |
|-----|------|---------|
| `StepCadBuilder.java` | 主入口，公共API + 缓存管理 | ~1500 |
| `StepCadGeometryBuilder.java` | Point, Direction, Vector, Placement构建 | ~1500 |
| `StepCadCurveBuilder.java` | 2D/3D曲线构建（Line, Circle, Ellipse, BSpline等） | ~1000 |
| `StepCadSurfaceBuilder.java` | 曲面构建（Plane, Cylindrical, Conical等） | ~1000 |
| `StepCadTopologyBuilder.java` | 拓扑构建（Vertex, Edge, Face, Shell, Solid） | ~500 |
| `StepCadSolidBuilder.java` | 实体构建（Extruded, Revolved, Box等） | ~800 |
| `StepCadTransformOps.java` | 变换操作（Point3, Direction3, Curve3变换） | ~500 |
| `StepCadSamplingUtils.java` | 采样工具（Fresnel, 曲线采样） | ~500 |

### Phase 2: StepEntityResolver拆分 (3个文件)

| 文件 | 职责 | 预估行数 |
|-----|------|---------|
| `StepEntityResolver.java` | 主入口，resolveAll + resolve核心逻辑 | ~2000 |
| `StepResolverUtils.java` | 参数提取工具 | ~1500 |
| `StepEntityValidator.java` | 实体类型判断和验证 | ~1000 |

### Phase 3: StepPreviewJsonExporter拆分 (9个文件)

| 文件 | 职责 | 预估行数 |
|-----|------|---------|
| `StepPreviewJsonExporter.java` | 主入口，export/exportBinary/exportGlb | ~800 |
| `PreviewPayloadBuilderV2.java` | Payload构建逻辑 | ~1200 |
| `PreviewFaceBuilderV2.java` | Face处理和曲面采样 | ~2000 |
| `PreviewEdgeBuilderV2.java` | Edge处理和曲线采样 | ~1200 |
| `PreviewPmiBuilder.java` | PMI数据提取和构建 | ~1500 |
| `PreviewAssemblyBuilder.java` | 装配体层级构建 | ~1000 |
| `PreviewTargetCollector.java` | Target收集逻辑 | ~2000 |
| `PreviewSamplingUtilsV2.java` | 几何采样辅助工具 | ~1000 |
| `PreviewPayloadTypesV2.java` | Payload类型定义 | ~500 |

---

## Phase 1: StepCadBuilder拆分

### Task 1.1: 创建StepCadGeometryBuilder骨架

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepCadGeometryBuilder.java`
- Test: `src/test/java/com/minicad/step/semantic/StepCadGeometryBuilderTest.java`

- [ ] **Step 1: 创建StepCadGeometryBuilder.java骨架**

```java
package com.minicad.step.semantic;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.geometry.StepVector;
import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import java.util.Map;

/**
 * Geometry builder for StepCadBuilder.
 * Responsible for building Point, Direction, Vector, Placement objects.
 */
class StepCadGeometryBuilder {
    private final Map<Integer, CartesianPoint> points;
    private final Map<Integer, Direction3> directions;
    private final Map<Integer, Vector3> vectors;
    private final Map<Integer, Axis2Placement3D> placements;

    StepCadGeometryBuilder(
            Map<Integer, CartesianPoint> points,
            Map<Integer, Direction3> directions,
            Map<Integer, Vector3> vectors,
            Map<Integer, Axis2Placement3D> placements) {
        this.points = points;
        this.directions = directions;
        this.vectors = vectors;
        this.placements = placements;
    }

    CartesianPoint buildPoint(StepCartesianPoint stepPoint) {
        // Implementation moved from StepCadBuilder
        throw new UnsupportedOperationException("TODO: move from StepCadBuilder");
    }

    Direction3 buildDirection(StepDirection stepDirection) {
        // Implementation moved from StepCadBuilder
        throw new UnsupportedOperationException("TODO: move from StepCadBuilder");
    }

    Vector3 buildVector(StepVector stepVector) {
        // Implementation moved from StepCadBuilder
        throw new UnsupportedOperationException("TODO: move from StepCadBuilder");
    }

    Axis2Placement3D buildPlacement(StepAxis2Placement3D stepPlacement) {
        // Implementation moved from StepCadBuilder
        throw new UnsupportedOperationException("TODO: move from StepCadBuilder");
    }

    Axis2Placement3D buildAxis1Placement(StepAxis1Placement stepPlacement) {
        // Implementation moved from StepCadBuilder
        throw new UnsupportedOperationException("TODO: move from StepCadBuilder");
    }
}
```

- [ ] **Step 2: 创建测试骨架**

```java
package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class StepCadGeometryBuilderTest {
    private Map<Integer, CartesianPoint> points;
    private Map<Integer, Direction3> directions;
    private Map<Integer, Vector3> vectors;
    private Map<Integer, Axis2Placement3D> placements;
    private StepCadGeometryBuilder builder;

    @BeforeEach
    void setUp() {
        points = new HashMap<>();
        directions = new HashMap<>();
        vectors = new HashMap<>();
        placements = new HashMap<>();
        builder = new StepCadGeometryBuilder(points, directions, vectors, placements);
    }

    @Test
    void testBuildPoint() {
        // Will be implemented after moving code
        assertNotNull(builder);
    }
}
```

- [ ] **Step 3: 运行测试确认骨架正确**

Run: `mvn test -Dtest=StepCadGeometryBuilderTest`
Expected: PASS (test passes with placeholder)

- [ ] **Step 4: 提交骨架**

```bash
git add src/main/java/com/minicad/step/semantic/StepCadGeometryBuilder.java
git add src/test/java/com/minicad/step/semantic/StepCadGeometryBuilderTest.java
git commit -m "feat(refactor): add StepCadGeometryBuilder skeleton for Phase 1"
```

---

### Task 1.2: 从StepCadBuilder移动buildPoint方法

**Files:**
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java:行号待定`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadGeometryBuilder.java`

- [ ] **Step 1: 在StepCadBuilder中找到buildPoint方法**

Run: `grep -n "buildPoint" src/main/java/com/minicad/step/semantic/StepCadBuilder.java | head -20`

Expected: 找到buildPoint方法定义和所有相关调用

- [ ] **Step 2: 复制buildPoint实现到StepCadGeometryBuilder**

从StepCadBuilder.java复制完整的buildPoint方法实现，替换StepCadGeometryBuilder中的placeholder：

```java
// 在StepCadGeometryBuilder.java中，替换buildPoint方法：
CartesianPoint buildPoint(StepCartesianPoint stepPoint) {
    // 从StepCadBuilder.java复制完整实现
    // 包括参数解析、坐标提取、缓存逻辑等
    // 实际代码将在执行时从原文件复制
}
```

- [ ] **Step 3: 在StepCadBuilder中委托调用**

修改StepCadBuilder.java的buildPoint方法，改为委托调用：

```java
// StepCadBuilder.java中修改：
private StepCadGeometryBuilder geometryBuilder;

// 在构造函数或初始化中：
geometryBuilder = new StepCadGeometryBuilder(points, directions, vectors, placements);

// 修改buildPoint方法：
public CartesianPoint buildPoint(StepCartesianPoint stepPoint) {
    return geometryBuilder.buildPoint(stepPoint);
}
```

- [ ] **Step 4: 移动相关import**

如果buildPoint使用了特定的import，从StepCadBuilder移动到StepCadGeometryBuilder：

```java
// 检查并移动必要的import语句
```

- [ ] **Step 5: 运行测试确认功能正常**

Run: `mvn test -Dtest=StepCadBuilderTest,StepCadGeometryBuilderTest`

Expected: PASS - 所有相关测试通过

- [ ] **Step 6: 提交buildPoint迁移**

```bash
git add src/main/java/com/minicad/step/semantic/StepCadGeometryBuilder.java
git add src/main/java/com/minicad/step/semantic/StepCadBuilder.java
git commit -m "refactor(phase1): move buildPoint from StepCadBuilder to StepCadGeometryBuilder"
```

---

### Task 1.3: 从StepCadBuilder移动buildDirection方法

**Files:**
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadGeometryBuilder.java`

- [ ] **Step 1: 找到buildDirection方法**

Run: `grep -n "buildDirection" src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

Expected: 找到方法定义

- [ ] **Step 2: 复制buildDirection实现**

将完整实现复制到StepCadGeometryBuilder，替换placeholder

- [ ] **Step 3: 在StepCadBuilder中委托调用**

```java
public Direction3 buildDirection(StepDirection stepDirection) {
    return geometryBuilder.buildDirection(stepDirection);
}
```

- [ ] **Step 4: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 5: 提交**

```bash
git commit -am "refactor(phase1): move buildDirection to StepCadGeometryBuilder"
```

---

### Task 1.4: 从StepCadBuilder移动buildVector方法

**Files:**
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadGeometryBuilder.java`

- [ ] **Step 1: 找到buildVector方法并复制实现**

Run: `grep -n "buildVector" src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 2: 委托调用**

```java
public Vector3 buildVector(StepVector stepVector) {
    return geometryBuilder.buildVector(stepVector);
}
```

- [ ] **Step 3: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 4: 提交**

```bash
git commit -am "refactor(phase1): move buildVector to StepCadGeometryBuilder"
```

---

### Task 1.5: 从StepCadBuilder移动buildPlacement系列方法

**Files:**
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadGeometryBuilder.java`

- [ ] **Step 1: 找到所有Placement相关方法**

Run: `grep -n "buildPlacement\|buildAxis1Placement\|buildAxis2" src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

Expected: 找到所有Placement构建方法

- [ ] **Step 2: 批量移动所有Placement方法**

包括：
- buildPlacement (Axis2Placement3D)
- buildAxis1Placement
- buildAxis2Placement2D (如果有)

复制所有实现到StepCadGeometryBuilder

- [ ] **Step 3: 委托调用**

```java
public Axis2Placement3D buildPlacement(StepAxis2Placement3D stepPlacement) {
    return geometryBuilder.buildPlacement(stepPlacement);
}

public Axis2Placement3D buildAxis1Placement(StepAxis1Placement stepPlacement) {
    return geometryBuilder.buildAxis1Placement(stepPlacement);
}
```

- [ ] **Step 4: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 5: 提交**

```bash
git commit -am "refactor(phase1): move all Placement methods to StepCadGeometryBuilder"
```

---

### Task 1.6: 创建StepCadCurveBuilder并移动2D曲线方法

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java`
- Test: `src/test/java/com/minicad/step/semantic/StepCadCurveBuilderTest.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 1: 创建StepCadCurveBuilder骨架**

```java
package com.minicad.step.semantic;

import com.minicad.geometry2d.*;
import com.minicad.geometry.Curve3;
import java.util.Map;

/**
 * Curve builder for StepCadBuilder.
 * Responsible for building 2D and 3D curves.
 */
class StepCadCurveBuilder {
    private final Map<Integer, Curve2> curves2d;
    private final Map<Integer, Curve3> curves3d;
    private final StepCadGeometryBuilder geometryBuilder;

    StepCadCurveBuilder(
            Map<Integer, Curve2> curves2d,
            Map<Integer, Curve3> curves3d,
            StepCadGeometryBuilder geometryBuilder) {
        this.curves2d = curves2d;
        this.curves3d = curves3d;
        this.geometryBuilder = geometryBuilder;
    }

    // 2D curves
    Line2 buildLine2(StepLine2D stepLine) { throw new UnsupportedOperationException(); }
    Circle2 buildCircle2(StepCircle2D stepCircle) { throw new UnsupportedOperationException(); }
    Ellipse2 buildEllipse2(StepEllipse2D stepEllipse) { throw new UnsupportedOperationException(); }
    BSplineCurve2 buildBSplineCurve2(StepBSplineCurve2D stepCurve) { throw new UnsupportedOperationException(); }
    Polyline2 buildPolyline2(StepPolyline2D stepPolyline) { throw new UnsupportedOperationException(); }

    // 3D curves
    Line3 buildLine(StepLine stepLine) { throw new UnsupportedOperationException(); }
    Circle buildCircle(StepCircle stepCircle) { throw new UnsupportedOperationException(); }
    Ellipse3 buildEllipse(StepEllipse stepEllipse) { throw new UnsupportedOperationException(); }
    BSplineCurve3 buildBSplineCurve(StepBSplineCurve stepCurve) { throw new UnsupportedOperationException(); }
    TrimmedCurve3 buildTrimmedCurve(StepTrimmedCurve stepCurve) { throw new UnsupportedOperationException(); }
}
```

- [ ] **Step 2: 创建测试骨架**

```java
package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class StepCadCurveBuilderTest {
    private StepCadCurveBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new StepCadCurveBuilder(new HashMap<>(), new HashMap<>(), null);
    }

    @Test
    void testBuilderNotNull() {
        assertNotNull(builder);
    }
}
```

- [ ] **Step 3: 找到所有2D曲线方法**

Run: `grep -n "buildLine2\|buildCircle2\|buildEllipse2\|buildBSplineCurve2\|buildPolyline2" src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 4: 批量移动所有2D曲线方法**

复制所有2D曲线构建方法的完整实现

- [ ] **Step 5: 在StepCadBuilder中委托调用**

```java
private StepCadCurveBuilder curveBuilder;

// 初始化
curveBuilder = new StepCadCurveBuilder(curves2d, curves3d, geometryBuilder);

// 委托
public Line2 buildLine2(StepLine2D stepLine) {
    return curveBuilder.buildLine2(stepLine);
}
// ... 其他2D曲线方法
```

- [ ] **Step 6: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest,StepCadCurveBuilderTest`

Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java
git add src/test/java/com/minicad/step/semantic/StepCadCurveBuilderTest.java
git commit -am "refactor(phase1): create StepCadCurveBuilder and move 2D curve methods"
```

---

### Task 1.7: 从StepCadBuilder移动3D曲线方法

**Files:**
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java`

- [ ] **Step 1: 找到所有3D曲线方法**

Run: `grep -n "buildLine\|buildCircle[^2]\|buildEllipse[^2]\|buildBSplineCurve[^2]\|buildTrimmedCurve\|buildCompositeCurve" src/main/java/com/minicad/step/semantic/StepCadBuilder.java | grep -v "2D"`

Expected: 找到所有3D曲线构建方法

- [ ] **Step 2: 批量移动所有3D曲线方法**

包括：
- buildLine (Line3)
- buildCircle (Circle)
- buildEllipse (Ellipse3)
- buildBSplineCurve (BSplineCurve3)
- buildRationalBSplineCurve (RationalBSplineCurve3)
- buildTrimmedCurve (TrimmedCurve3)
- buildCompositeCurve (CompositeCurve3)
- buildPolyline (Polyline3)

复制所有实现到StepCadCurveBuilder

- [ ] **Step 3: 在StepCadBuilder中委托调用**

```java
public Line3 buildLine(StepLine stepLine) {
    return curveBuilder.buildLine(stepLine);
}
// ... 其他3D曲线方法
```

- [ ] **Step 4: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 5: 提交**

```bash
git commit -am "refactor(phase1): move all 3D curve methods to StepCadCurveBuilder"
```

---

### Task 1.8: 创建StepCadSurfaceBuilder并移动曲面方法

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepCadSurfaceBuilder.java`
- Test: `src/test/java/com/minicad/step/semantic/StepCadSurfaceBuilderTest.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 1: 创建StepCadSurfaceBuilder骨架**

```java
package com.minicad.step.semantic;

import com.minicad.geometry.*;
import java.util.Map;

/**
 * Surface builder for StepCadBuilder.
 * Responsible for building surface geometries.
 */
class StepCadSurfaceBuilder {
    private final Map<Integer, SurfaceGeometry> surfaces;
    private final StepCadGeometryBuilder geometryBuilder;
    private final StepCadCurveBuilder curveBuilder;

    StepCadSurfaceBuilder(
            Map<Integer, SurfaceGeometry> surfaces,
            StepCadGeometryBuilder geometryBuilder,
            StepCadCurveBuilder curveBuilder) {
        this.surfaces = surfaces;
        this.geometryBuilder = geometryBuilder;
        this.curveBuilder = curveBuilder;
    }

    Plane buildPlane(StepPlane stepPlane) { throw new UnsupportedOperationException(); }
    CylindricalSurface buildCylindricalSurface(StepCylindricalSurface stepSurface) { throw new UnsupportedOperationException(); }
    ConicalSurface buildConicalSurface(StepConicalSurface stepSurface) { throw new UnsupportedOperationException(); }
    SphericalSurface buildSphericalSurface(StepSphericalSurface stepSurface) { throw new UnsupportedOperationException(); }
    ToroidalSurface buildToroidalSurface(StepToroidalSurface stepSurface) { throw new UnsupportedOperationException(); }
    BSplineSurface3 buildBSplineSurface(StepBSplineSurface stepSurface) { throw new UnsupportedOperationException(); }
    RationalBSplineSurface3 buildRationalBSplineSurface(StepRationalBSplineSurface stepSurface) { throw new UnsupportedOperationException(); }
    RuledSurface3 buildRuledSurface(StepRuledSurface stepSurface) { throw new UnsupportedOperationException(); }
    SurfaceOfRevolution3 buildSurfaceOfRevolution(StepSurfaceOfRevolution stepSurface) { throw new UnsupportedOperationException(); }
    SurfaceOfLinearExtrusion3 buildSurfaceOfLinearExtrusion(StepSurfaceOfLinearExtrusion stepSurface) { throw new UnsupportedOperationException(); }
}
```

- [ ] **Step 2: 创建测试骨架**

```java
package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StepCadSurfaceBuilderTest {
    @Test
    void testBuilderNotNull() {
        assertNotNull(new StepCadSurfaceBuilder(new HashMap<>(), null, null));
    }
}
```

- [ ] **Step 3: 找到所有曲面构建方法**

Run: `grep -n "buildPlane\|build.*Surface" src/main/java/com/minicad/step/semantic/StepCadBuilder.java | grep -v "Face"`

Expected: 找到所有Surface构建方法（不包括Face相关）

- [ ] **Step 4: 批量移动所有曲面方法**

复制所有曲面构建方法的完整实现到StepCadSurfaceBuilder

- [ ] **Step 5: 在StepCadBuilder中委托调用**

```java
private StepCadSurfaceBuilder surfaceBuilder;

// 初始化
surfaceBuilder = new StepCadSurfaceBuilder(surfaces, geometryBuilder, curveBuilder);

// 委托
public Plane buildPlane(StepPlane stepPlane) {
    return surfaceBuilder.buildPlane(stepPlane);
}
// ... 其他曲面方法
```

- [ ] **Step 6: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add src/main/java/com/minicad/step/semantic/StepCadSurfaceBuilder.java
git add src/test/java/com/minicad/step/semantic/StepCadSurfaceBuilderTest.java
git commit -am "refactor(phase1): create StepCadSurfaceBuilder and move surface methods"
```

---

### Task 1.9: 创建StepCadTopologyBuilder并移动拓扑方法

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepCadTopologyBuilder.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 1: 检查是否已存在StepCadTopologyBuilder**

Run: `ls src/main/java/com/minicad/step/semantic/StepCadTopologyBuilder.java`

如果存在，跳过创建，直接修改现有文件

- [ ] **Step 2: 创建或修改StepCadTopologyBuilder**

如果不存在，创建骨架：

```java
package com.minicad.step.semantic;

import com.minicad.topology.*;
import java.util.Map;

/**
 * Topology builder for StepCadBuilder.
 * Responsible for building Vertex, Edge, Face, Shell, Solid topology.
 */
class StepCadTopologyBuilder {
    private final Map<Integer, Vertex> vertices;
    private final Map<Integer, Edge> edges;
    private final Map<Integer, Face> faces;
    private final Map<Integer, Shell> shells;
    private final Map<Integer, Solid> solids;
    private final StepCadGeometryBuilder geometryBuilder;
    private final StepCadCurveBuilder curveBuilder;
    private final StepCadSurfaceBuilder surfaceBuilder;

    // Constructor and topology building methods
    Vertex buildVertex(StepVertex stepVertex) { throw new UnsupportedOperationException(); }
    Edge buildEdge(StepEdgeCurve stepEdge) { throw new UnsupportedOperationException(); }
    OrientedEdge buildOrientedEdge(StepOrientedEdge stepEdge) { throw new UnsupportedOperationException(); }
    EdgeLoop buildEdgeLoop(StepEdgeLoop stepLoop) { throw new UnsupportedOperationException(); }
    Face buildFace(StepAdvancedFace stepFace) { throw new UnsupportedOperationException(); }
    Shell buildShell(StepClosedShell stepShell) { throw new UnsupportedOperationException(); }
    Solid buildSolid(StepManifoldSolidBrep stepSolid) { throw new UnsupportedOperationException(); }
}
```

- [ ] **Step 3: 移动拓扑构建方法**

复制所有拓扑相关方法的完整实现

- [ ] **Step 4: 在StepCadBuilder中委托调用**

```java
private StepCadTopologyBuilder topologyBuilder;

// 初始化
topologyBuilder = new StepCadTopologyBuilder(vertices, edges, faces, shells, solids,
                                              geometryBuilder, curveBuilder, surfaceBuilder);

// 委托所有拓扑方法
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git commit -am "refactor(phase1): create StepCadTopologyBuilder and move topology methods"
```

---

### Task 1.10: 创建StepCadSolidBuilder并移动实体构建方法

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepCadSolidBuilder.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 1: 检查是否已存在StepCadSolidBuilder**

Run: `ls src/main/java/com/minicad/step/semantic/StepCadSolidBuilder.java`

- [ ] **Step 2: 创建或修改StepCadSolidBuilder**

```java
package com.minicad.step.semantic;

import com.minicad.geometry.*;
import com.minicad.topology.Solid;
import java.util.Map;

/**
 * Solid builder for StepCadBuilder.
 * Responsible for building Extruded, Revolved, Box, Sphere, Cylinder solids.
 */
class StepCadSolidBuilder {
    private final StepCadGeometryBuilder geometryBuilder;
    private final StepCadCurveBuilder curveBuilder;
    private final StepCadSurfaceBuilder surfaceBuilder;
    private final StepCadTopologyBuilder topologyBuilder;

    // Solid building methods
    Solid buildExtrudedAreaSolid(StepExtrudedAreaSolid stepSolid) { throw new UnsupportedOperationException(); }
    Solid buildRevolvedAreaSolid(StepRevolvedAreaSolid stepSolid) { throw new UnsupportedOperationException(); }
    Solid buildBox(StepBlockVolume stepBox) { throw new UnsupportedOperationException(); }
    Solid buildSphere(StepSphereVolume stepSphere) { throw new UnsupportedOperationException(); }
    Solid buildCylinder(StepCylinderVolume stepCylinder) { throw new UnsupportedOperationException(); }
    Solid buildTorus(StepTorusVolume stepTorus) { throw new UnsupportedOperationException(); }
    Solid buildPrism(StepPrismVolume stepPrism) { throw new UnsupportedOperationException(); }
}
```

- [ ] **Step 3: 移动所有Solid构建方法**

找到并移动：
- buildExtrudedAreaSolid
- buildRevolvedAreaSolid
- buildExtrudedFaceSolid
- buildRevolvedFaceSolid
- buildBox / buildBlockVolume
- buildSphere / buildSphereVolume
- buildCylinder / buildCylinderVolume
- buildTorus / buildTorusVolume
- buildPrism

复制完整实现

- [ ] **Step 4: 在StepCadBuilder中委托调用**

```java
private StepCadSolidBuilder solidBuilder;

// 初始化
solidBuilder = new StepCadSolidBuilder(geometryBuilder, curveBuilder, surfaceBuilder, topologyBuilder);

// 委托
public Solid buildExtrudedAreaSolid(StepExtrudedAreaSolid stepSolid) {
    return solidBuilder.buildExtrudedAreaSolid(stepSolid);
}
// ... 其他方法
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git commit -am "refactor(phase1): create StepCadSolidBuilder and move solid building methods"
```

---

### Task 1.11: 创建StepCadTransformOps并移动变换方法

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepCadTransformOps.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 1: 创建StepCadTransformOps**

```java
package com.minicad.step.semantic;

import com.minicad.geometry.*;
import com.minicad.geometry2d.*;

/**
 * Transform operations for StepCadBuilder.
 * Responsible for transforming points, directions, curves, surfaces.
 */
class StepCadTransformOps {
    private final Axis2Placement3D transform;

    StepCadTransformOps(Axis2Placement3D transform) {
        this.transform = transform;
    }

    CartesianPoint transformPoint3(CartesianPoint point) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }

    Direction3 transformDirection3(Direction3 direction) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }

    Curve3 transformCurve3(Curve3 curve) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }

    SurfaceGeometry transformSurfaceGeometry(SurfaceGeometry surface) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }

    // 2D transforms if needed
    Point2 transformPoint2(Point2 point) { throw new UnsupportedOperationException(); }
    Direction2 transformDirection2(Direction2 direction) { throw new UnsupportedOperationException(); }
    Curve2 transformCurve2(Curve2 curve) { throw new UnsupportedOperationException(); }
}
```

- [ ] **Step 2: 找到所有变换方法**

Run: `grep -n "transformPoint\|transformDirection\|transformCurve\|transformSurface" src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 3: 移动所有变换方法实现**

复制完整实现

- [ ] **Step 4: 在StepCadBuilder中委托**

```java
public CartesianPoint transformPoint3(CartesianPoint point, Axis2Placement3D transform) {
    return new StepCadTransformOps(transform).transformPoint3(point);
}
// ... 其他方法
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/step/semantic/StepCadTransformOps.java
git commit -am "refactor(phase1): create StepCadTransformOps and move transform methods"
```

---

### Task 1.12: 创建StepCadSamplingUtils并移动采样方法

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepCadSamplingUtils.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 1: 创建StepCadSamplingUtils**

```java
package com.minicad.step.semantic;

import com.minicad.geometry.*;

/**
 * Sampling utilities for StepCadBuilder.
 * Responsible for Fresnel functions and curve sampling.
 */
class StepCadSamplingUtils {
    // Fresnel integrals for Clothoid
    static double fresnelC(double t) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }

    static double fresnelS(double t) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }

    // Curve sampling
    static List<CartesianPoint> sampleCurve3(Curve3 curve, int numSamples) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }

    static List<Point2> sampleCurve2(Curve2 curve, int numSamples) {
        // Implementation from StepCadBuilder
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 找到所有采样和辅助方法**

Run: `grep -n "fresnel\|sampleCurve\|reverseList\|interpolate" src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 3: 移动所有采样和辅助方法**

包括：
- fresnelC, fresnelS (Fresnel积分)
- sampleCurve3, sampleCurve2 (曲线采样)
- reverseList (列表反转)
- interpolate (插值)
- 其他辅助工具方法

复制完整实现

- [ ] **Step 4: 在StepCadBuilder中调用**

```java
// 将调用改为静态调用
double c = StepCadSamplingUtils.fresnelC(t);
double s = StepCadSamplingUtils.fresnelS(t);
List<CartesianPoint> samples = StepCadSamplingUtils.sampleCurve3(curve, 100);
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepCadBuilderTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/step/semantic/StepCadSamplingUtils.java
git commit -am "refactor(phase1): create StepCadSamplingUtils and move sampling utilities"
```

---

### Task 1.13: 清理StepCadBuilder主类

**Files:**
- Modify: `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

- [ ] **Step 1: 检查StepCadBuilder剩余行数**

Run: `wc -l src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

Expected: 应该接近1500行目标

- [ ] **Step 2: 清理已迁移方法的import**

检查并移除不再需要的import语句

- [ ] **Step 3: 整理缓存字段和初始化**

确保所有Builder实例正确初始化：

```java
public class StepCadBuilder {
    // 缓存字段（保留）
    private final Map<Integer, CartesianPoint> points = new HashMap<>();
    private final Map<Integer, Direction3> directions = new HashMap<>();
    // ... 其他缓存

    // Builder实例
    private StepCadGeometryBuilder geometryBuilder;
    private StepCadCurveBuilder curveBuilder;
    private StepCadSurfaceBuilder surfaceBuilder;
    private StepCadTopologyBuilder topologyBuilder;
    private StepCadSolidBuilder solidBuilder;

    // 初始化方法
    private void initializeBuilders() {
        geometryBuilder = new StepCadGeometryBuilder(points, directions, vectors, placements);
        curveBuilder = new StepCadCurveBuilder(curves2d, curves3d, geometryBuilder);
        surfaceBuilder = new StepCadSurfaceBuilder(surfaces, geometryBuilder, curveBuilder);
        topologyBuilder = new StepCadTopologyBuilder(vertices, edges, faces, shells, solids,
                                                      geometryBuilder, curveBuilder, surfaceBuilder);
        solidBuilder = new StepCadSolidBuilder(geometryBuilder, curveBuilder, surfaceBuilder, topologyBuilder);
    }

    // 公共方法（委托调用）
    public CartesianPoint buildPoint(StepCartesianPoint stepPoint) {
        return geometryBuilder.buildPoint(stepPoint);
    }
    // ... 其他委托方法
}
```

- [ ] **Step 4: 最终行数检查**

Run: `wc -l src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

目标: < 2000行

- [ ] **Step 5: 运行完整测试套件**

Run: `mvn -B clean test`

Expected: PASS - 所有测试通过

- [ ] **Step 6: 黄金文件测试**

Run: `mvn exec:java -Dexec.args="examples/minimal-square.step"`

Expected: 输出与之前一致

- [ ] **Step 7: 提交Phase 1完成**

```bash
git tag phase1-stepcadbuilder-refactoring-complete
git commit -am "refactor(phase1): complete StepCadBuilder refactoring - all methods distributed to builders"
```

---

## Phase 2: StepEntityResolver拆分

### Task 2.1: 创建StepResolverUtils骨架

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepResolverUtils.java`
- Test: `src/test/java/com/minicad/step/semantic/StepResolverUtilsTest.java`

- [ ] **Step 1: 创建StepResolverUtils骨架**

```java
package com.minicad.step.semantic;

import com.minicad.step.syntax.*;
import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Map;

/**
 * Resolver utilities for StepEntityResolver.
 * Responsible for parameter extraction, type conversion, and reference resolution.
 */
class StepResolverUtils {
    // Parameter extraction
    static StepValue getParameter(StepEntityInstance instance, int index) {
        List<StepValue> params = instance.getParameters();
        if (index < 0 || index >= params.size()) {
            throw new StepResolutionException("Parameter index out of bounds");
        }
        return params.get(index);
    }

    static String getString(StepValue value) {
        if (value instanceof StepStringValue) {
            return ((StepStringValue) value).getValue();
        }
        throw new StepResolutionException("Expected string value");
    }

    static Integer getInteger(StepValue value) {
        if (value instanceof StepIntegerValue) {
            return ((StepIntegerValue) value).getValue();
        }
        throw new StepResolutionException("Expected integer value");
    }

    static Double getReal(StepValue value) {
        if (value instanceof StepRealValue) {
            return ((StepRealValue) value).getValue();
        }
        throw new StepResolutionException("Expected real value");
    }

    static StepEntity resolveReference(StepValue value, Map<Integer, StepEntity> entities) {
        if (value instanceof StepReferenceValue) {
            int id = ((StepReferenceValue) value).getId();
            StepEntity entity = entities.get(id);
            if (entity == null) {
                throw new StepResolutionException("Unresolved reference: #" + id);
            }
            return entity;
        }
        throw new StepResolutionException("Expected reference value");
    }

    // Default value handling
    static boolean isOmitted(StepValue value) {
        return value instanceof StepOmittedValue;
    }

    static boolean isNotProvided(StepValue value) {
        return value instanceof StepNotProvidedValue;
    }

    // Type conversion helpers
    static List<Double> toDoubleList(List<StepValue> values) {
        // Implementation
        throw new UnsupportedOperationException();
    }

    static List<Integer> toIntegerList(List<StepValue> values) {
        // Implementation
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 创建测试骨架**

```java
package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StepResolverUtilsTest {
    @Test
    void testGetString() {
        // Test implementation
    }

    @Test
    void testGetInteger() {
        // Test implementation
    }

    @Test
    void testGetReal() {
        // Test implementation
    }
}
```

- [ ] **Step 3: 找到参数提取方法**

Run: `grep -n "getParameter\|getString\|getInteger\|getReal\|resolveReference" src/main/java/com/minicad/step/semantic/StepEntityResolver.java`

- [ ] **Step 4: 移动所有参数提取方法**

复制完整实现

- [ ] **Step 5: 在StepEntityResolver中使用**

```java
// 改为静态调用
StepValue param = StepResolverUtils.getParameter(instance, 0);
String name = StepResolverUtils.getString(param);
Integer id = StepResolverUtils.getInteger(StepResolverUtils.getParameter(instance, 1));
```

- [ ] **Step 6: 运行测试**

Run: `mvn test -Dtest=StepEntityResolverTest,StepResolverUtilsTest`

Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add src/main/java/com/minicad/step/semantic/StepResolverUtils.java
git add src/test/java/com/minicad/step/semantic/StepResolverUtilsTest.java
git commit -am "refactor(phase2): create StepResolverUtils and move parameter extraction methods"
```

---

### Task 2.2: 创建StepEntityValidator并移动验证方法

**Files:**
- Create: `src/main/java/com/minicad/step/semantic/StepEntityValidator.java`
- Modify: `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`

- [ ] **Step 1: 创建StepEntityValidator骨架**

```java
package com.minicad.step.semantic;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.topology.*;
import com.minicad.step.model.product.*;

/**
 * Entity validator for StepEntityResolver.
 * Responsible for entity type checking and validation.
 */
class StepEntityValidator {
    // Shell entity checks
    static boolean isShellEntity(StepEntity entity) {
        return entity instanceof StepClosedShell
            || entity instanceof StepOpenShell
            || entity instanceof StepAdvancedFace
            || entity instanceof StepConnectedFaceSet;
    }

    // Boolean operand checks
    static boolean isBooleanOperandEntity(StepEntity entity) {
        return entity instanceof StepSolidModel
            || entity instanceof StepHalfSpaceSolid
            || entity instanceof StepCsgSolid
            || entity instanceof StepBooleanResult;
    }

    // Supported entity checks
    static boolean isSupportedGeometryEntity(StepEntity entity) {
        // Implementation from StepEntityResolver
        throw new UnsupportedOperationException();
    }

    static boolean isSupportedTopologyEntity(StepEntity entity) {
        // Implementation from StepEntityResolver
        throw new UnsupportedOperationException();
    }

    static boolean isSupportedProductEntity(StepEntity entity) {
        // Implementation from StepEntityResolver
        throw new UnsupportedOperationException();
    }

    // Entity completeness validation
    static void validateEntityCompleteness(StepEntity entity) {
        // Implementation from StepEntityResolver
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 找到所有验证方法**

Run: `grep -n "isShellEntity\|isBoolean\|isSupported\|validate" src/main/java/com/minicad/step/semantic/StepEntityResolver.java`

- [ ] **Step 3: 移动所有验证方法**

复制完整实现

- [ ] **Step 4: 在StepEntityResolver中使用**

```java
// 改为静态调用
if (StepEntityValidator.isShellEntity(entity)) {
    // ...
}
if (StepEntityValidator.isBooleanOperandEntity(entity)) {
    // ...
}
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepEntityResolverTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/step/semantic/StepEntityValidator.java
git commit -am "refactor(phase2): create StepEntityValidator and move validation methods"
```

---

### Task 2.3: 清理StepEntityResolver主类

**Files:**
- Modify: `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`

- [ ] **Step 1: 检查剩余行数**

Run: `wc -l src/main/java/com/minicad/step/semantic/StepEntityResolver.java`

Expected: 应该接近2000行目标

- [ ] **Step 2: 清理import**

移除已迁移方法相关的import

- [ ] **Step 3: 整理resolve方法**

确保resolveAll和resolve方法保持简洁：

```java
public class StepEntityResolver {
    public static Map<Integer, StepEntity> resolveAll(StepFile file) {
        Map<Integer, StepEntity> entities = new HashMap<>();
        // Core resolve logic
        for (StepEntityInstance instance : file.getDataSection().getEntities()) {
            StepEntity entity = resolve(instance, entities);
            entities.put(instance.getId(), entity);
        }
        return entities;
    }

    private static StepEntity resolve(StepEntityInstance instance, Map<Integer, StepEntity> entities) {
        EntityFactory factory = resolveFactory(instance);
        return factory.create(instance, entities);
    }

    static EntityFactory resolveFactory(StepEntityInstance instance) {
        String typeName = instance.getTypeName();
        // Factory lookup logic (may delegate to registry)
        return StepEntityRegistry.getFactory(typeName);
    }
}
```

- [ ] **Step 4: 最终行数检查**

Run: `wc -l src/main/java/com/minicad/step/semantic/StepEntityResolver.java`

目标: < 2000行

- [ ] **Step 5: 运行完整测试**

Run: `mvn -B clean test`

Expected: PASS

- [ ] **Step 6: 黄金文件测试**

Run: `mvn exec:java -Dexec.args="examples/engine.stp"`

Expected: 输出与之前一致

- [ ] **Step 7: 提交Phase 2完成**

```bash
git tag phase2-stepentityresolver-refactoring-complete
git commit -am "refactor(phase2): complete StepEntityResolver refactoring - utilities and validators extracted"
```

---

## Phase 3: StepPreviewJsonExporter拆分

### Task 3.1: 创建PreviewPayloadBuilderV2骨架

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewPayloadBuilderV2.java`
- Test: `src/test/java/com/minicad/app/PreviewPayloadBuilderV2Test.java`

- [ ] **Step 1: 创建PreviewPayloadBuilderV2骨架**

```java
package com.minicad.app;

import com.minicad.step.model.base.StepEntity;
import java.util.Map;
import java.util.List;

/**
 * Payload builder for StepPreviewJsonExporter.
 * Responsible for building preview payload structures.
 */
class PreviewPayloadBuilderV2 {
    private final Map<Integer, StepEntity> entities;

    PreviewPayloadBuilderV2(Map<Integer, StepEntity> entities) {
        this.entities = entities;
    }

    PreviewPayload buildPayload(StepSolidModel solid) {
        // Implementation from StepPreviewJsonExporter
        throw new UnsupportedOperationException();
    }

    ValidationPayload buildValidationPayload(List<MiniCadIssue> issues) {
        // Implementation from StepPreviewJsonExporter
        throw new UnsupportedOperationException();
    }

    GeometrySummary buildGeometrySummary(StepSolidModel solid) {
        // Implementation from StepPreviewJsonExporter
        throw new UnsupportedOperationException();
    }

    // Payload assembly helpers
    private void assembleFaces(PreviewPayload payload, Shell shell) {
        throw new UnsupportedOperationException();
    }

    private void assembleEdges(PreviewPayload payload, Shell shell) {
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 创建测试骨架**

```java
package com.minicad.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PreviewPayloadBuilderV2Test {
    @Test
    void testBuildPayload() {
        // Test implementation
    }
}
```

- [ ] **Step 3: 找到Payload构建方法**

Run: `grep -n "buildPayload\|buildValidationPayload\|buildGeometrySummary" src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 4: 移动所有Payload构建方法**

复制完整实现（约1200行）

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest,PreviewPayloadBuilderV2Test`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewPayloadBuilderV2.java
git commit -am "refactor(phase3): create PreviewPayloadBuilderV2 and move payload building methods"
```

---

### Task 3.2: 创建PreviewFaceBuilderV2并移动Face处理方法

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewFaceBuilderV2.java`
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 创建PreviewFaceBuilderV2骨架**

```java
package com.minicad.app;

import com.minicad.geometry.*;
import com.minicad.topology.Face;

/**
 * Face builder for StepPreviewJsonExporter.
 * Responsible for building face payloads and surface sampling.
 */
class PreviewFaceBuilderV2 {
    private final PreviewSamplingUtilsV2 samplingUtils;

    PreviewFaceBuilderV2(PreviewSamplingUtilsV2 samplingUtils) {
        this.samplingUtils = samplingUtils;
    }

    // Face payload methods
    FacePayload toPlanarFacePayload(Face face, Plane plane) {
        throw new UnsupportedOperationException();
    }

    FacePayload toCylindricalFacePayload(Face face, CylindricalSurface surface) {
        throw new UnsupportedOperationException();
    }

    FacePayload toConicalFacePayload(Face face, ConicalSurface surface) {
        throw new UnsupportedOperationException();
    }

    FacePayload toSphericalFacePayload(Face face, SphericalSurface surface) {
        throw new UnsupportedOperationException();
    }

    FacePayload toToroidalFacePayload(Face face, ToroidalSurface surface) {
        throw new UnsupportedOperationException();
    }

    FacePayload toBSplineSurfaceFacePayload(Face face, BSplineSurface3 surface) {
        throw new UnsupportedOperationException();
    }

    // Triangulation methods
    TriangulatedFacePayload triangulateParametricFace(Face face, SurfaceGeometry surface) {
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 找到所有Face处理方法**

Run: `grep -n "to.*FacePayload\|triangulate" src/main/java/com/minicad/app/StepPreviewJsonExporter.java | grep -i face`

Expected: 找到约40个方法

- [ ] **Step 3: 批量移动所有Face处理方法**

复制完整实现（约2000行）

- [ ] **Step 4: 在StepPreviewJsonExporter中委托**

```java
private PreviewFaceBuilderV2 faceBuilder;

// 初始化
faceBuilder = new PreviewFaceBuilderV2(samplingUtils);

// 委托调用
FacePayload payload = faceBuilder.toCylindricalFacePayload(face, surface);
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewFaceBuilderV2.java
git commit -am "refactor(phase3): create PreviewFaceBuilderV2 and move face processing methods"
```

---

### Task 3.3: 创建PreviewEdgeBuilderV2并移动Edge处理方法

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewEdgeBuilderV2.java`
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 创建PreviewEdgeBuilderV2骨架**

```java
package com.minicad.app;

import com.minicad.geometry.Curve3;
import com.minicad.topology.Edge;

/**
 * Edge builder for StepPreviewJsonExporter.
 * Responsible for building edge payloads and curve sampling.
 */
class PreviewEdgeBuilderV2 {
    private final PreviewSamplingUtilsV2 samplingUtils;

    EdgePayload buildTopologyEdgePayload(Edge edge) {
        throw new UnsupportedOperationException();
    }

    EdgePayload sampledCurvePayload(Curve3 curve, int samples) {
        throw new UnsupportedOperationException();
    }

    EdgePayload edgeCurvePayload(Edge edge) {
        throw new UnsupportedOperationException();
    }

    List<Point3> sampleEdge(Edge edge, int samples) {
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 找到所有Edge处理方法**

Run: `grep -n "buildTopologyEdgePayload\|sampledCurvePayload\|edgeCurvePayload\|sampleEdge" src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 3: 批量移动所有Edge处理方法**

复制完整实现（约1200行）

- [ ] **Step 4: 在StepPreviewJsonExporter中委托**

```java
private PreviewEdgeBuilderV2 edgeBuilder;

EdgePayload payload = edgeBuilder.buildTopologyEdgePayload(edge);
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewEdgeBuilderV2.java
git commit -am "refactor(phase3): create PreviewEdgeBuilderV2 and move edge processing methods"
```

---

### Task 3.4: 创建PreviewPmiBuilder并移动PMI处理方法

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewPmiBuilder.java`
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 创建PreviewPmiBuilder骨架**

```java
package com.minicad.app;

import com.minicad.step.model.annotation.*;
import com.minicad.step.model.tolerance.*;
import java.util.List;

/**
 * PMI builder for StepPreviewJsonExporter.
 * Responsible for building PMI (Product Manufacturing Information) payloads.
 */
class PreviewPmiBuilder {
    private final Map<Integer, StepEntity> entities;

    List<PmiPayload> buildPmiPayloads(StepProductDefinition productDef) {
        throw new UnsupportedOperationException();
    }

    void appendDraughtingAnnotationPmi(List<PmiPayload> payloads, StepAnnotationOccurrence annotation) {
        throw new UnsupportedOperationException();
    }

    PmiPayload toPmiPayload(StepGeometricTolerance tolerance) {
        throw new UnsupportedOperationException();
    }

    PmiPayload toPmiPayload(StepDimensionalSize dimension) {
        throw new UnsupportedOperationException();
    }

    // Point extraction from annotations
    Point3 pointFromAnnotationCurve(StepAnnotationCurveOccurrence annotation) {
        throw new UnsupportedOperationException();
    }

    Point3 pointFromAnnotationPoint(StepAnnotationPointOccurrence annotation) {
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 找到所有PMI处理方法**

Run: `grep -n "buildPmiPayloads\|appendDraughting\|toPmiPayload\|pointFromAnnotation" src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

Expected: 找到约60个方法

- [ ] **Step 3: 批量移动所有PMI处理方法**

复制完整实现（约1500行）

- [ ] **Step 4: 在StepPreviewJsonExporter中委托**

```java
private PreviewPmiBuilder pmiBuilder;

List<PmiPayload> pmiPayloads = pmiBuilder.buildPmiPayloads(productDef);
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewPmiBuilder.java
git commit -am "refactor(phase3): create PreviewPmiBuilder and move PMI processing methods"
```

---

### Task 3.5: 创建PreviewAssemblyBuilder并移动装配体处理方法

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewAssemblyBuilder.java`
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 创建PreviewAssemblyBuilder骨架**

```java
package com.minicad.app;

import com.minicad.step.model.product.*;
import java.util.Map;
import java.util.List;

/**
 * Assembly builder for StepPreviewJsonExporter.
 * Responsible for building assembly hierarchy data.
 */
class PreviewAssemblyBuilder {
    private final Map<Integer, StepEntity> entities;

    AssemblyData buildAssemblyData(StepProductDefinition productDef) {
        throw new UnsupportedOperationException();
    }

    List<ShapeRepresentation> collectLinkedShapeRepresentations(StepProductDefinition productDef) {
        throw new UnsupportedOperationException();
    }

    AssemblyNode buildAssemblyNode(StepAssemblyComponentRelationship relationship) {
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 2: 找到所有装配体处理方法**

Run: `grep -n "buildAssemblyData\|collectLinked\|AssemblyNode" src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 3: 批量移动所有装配体处理方法**

复制完整实现（约1000行）

- [ ] **Step 4: 在StepPreviewJsonExporter中委托**

```java
private PreviewAssemblyBuilder assemblyBuilder;

AssemblyData assemblyData = assemblyBuilder.buildAssemblyData(productDef);
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewAssemblyBuilder.java
git commit -am "refactor(phase3): create PreviewAssemblyBuilder and move assembly processing methods"
```

---

### Task 3.6: 创建PreviewTargetCollector并移动Target收集方法

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewTargetCollector.java`
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 创建PreviewTargetCollector骨架**

```java
package com.minicad.app;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Map;

/**
 * Target collector for StepPreviewJsonExporter.
 * Responsible for collecting semantic targets for various entity types.
 */
class PreviewTargetCollector {
    private final Map<Integer, StepEntity> entities;

    List<SemanticTarget> collectSemanticTargets(StepProductDefinition productDef) {
        throw new UnsupportedOperationException();
    }

    List<SemanticTarget> collectTargetsForGeometry(StepGeometricRepresentationItem geometry) {
        throw new UnsupportedOperationException();
    }

    List<SemanticTarget> collectTargetsForTopology(StepTopologicalRepresentationItem topology) {
        throw new UnsupportedOperationException();
    }

    void appendGeometryTargets(List<SemanticTarget> targets, StepEntity entity) {
        throw new UnsupportedOperationException();
    }

    void appendTopologyTargets(List<SemanticTarget> targets, StepEntity entity) {
        throw new UnsupportedOperationException();
    }

    // ... 约90个append*Targets方法
}
```

- [ ] **Step 2: 找到所有Target收集方法**

Run: `grep -n "collectSemanticTargets\|collectTargetsFor\|append.*Targets" src/main/java/com/minicad/app/StepPreviewJsonExporter.java | wc -l`

Expected: 约90个方法

- [ ] **Step 3: 批量移动所有Target收集方法**

这是最复杂的部分，需要仔细移动约2000行代码

复制完整实现

- [ ] **Step 4: 在StepPreviewJsonExporter中委托**

```java
private PreviewTargetCollector targetCollector;

List<SemanticTarget> targets = targetCollector.collectSemanticTargets(productDef);
```

- [ ] **Step 5: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest`

Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewTargetCollector.java
git commit -am "refactor(phase3): create PreviewTargetCollector and move target collection methods"
```

---

### Task 3.7: 创建PreviewSamplingUtilsV2并移动采样方法

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewSamplingUtilsV2.java`
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 检查是否已存在PreviewSamplingUtils**

Run: `ls src/main/java/com/minicad/app/PreviewSamplingUtils.java`

如果存在，检查内容并决定是否整合或创建V2

- [ ] **Step 2: 创建PreviewSamplingUtilsV2**

```java
package com.minicad.app;

import com.minicad.geometry.*;
import com.minicad.topology.*;

/**
 * Sampling utilities for preview builders.
 * Responsible for surface and curve sampling.
 */
class PreviewSamplingUtilsV2 {
    // Surface sampling
    static List<Point3> sampleTopologySurfaceGrid(Face face, int uSamples, int vSamples) {
        throw new UnsupportedOperationException();
    }

    static List<Point3> sampleSurfaceGrid(SurfaceGeometry surface, int uSamples, int vSamples) {
        throw new UnsupportedOperationException();
    }

    static GridData sampleParametricSurface(SurfaceGeometry surface, Bounds bounds) {
        throw new UnsupportedOperationException();
    }

    // Edge sampling
    static List<Point3> sampleParametricOrientedEdge(OrientedEdge edge, int samples) {
        throw new UnsupportedOperationException();
    }

    static List<Point2> sampleLoop(EdgeLoop loop, int samples) {
        throw new UnsupportedOperationException();
    }

    // Curve sampling on surface
    static List<Point3> sampleCurveOnSurface(SurfaceCurve3 curve, int samples) {
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 3: 找到所有采样方法**

Run: `grep -n "sampleTopologySurfaceGrid\|sampleSurfaceGrid\|sampleParametric\|sampleLoop" src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 4: 批量移动所有采样方法**

复制完整实现（约1000行）

- [ ] **Step 5: 在各个Builder中使用**

```java
// PreviewFaceBuilderV2, PreviewEdgeBuilderV2中调用
List<Point3> samples = PreviewSamplingUtilsV2.sampleSurfaceGrid(surface, 20, 20);
```

- [ ] **Step 6: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest`

Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewSamplingUtilsV2.java
git commit -am "refactor(phase3): create PreviewSamplingUtilsV2 and move sampling methods"
```

---

### Task 3.8: 创建PreviewPayloadTypesV2并移动类型定义

**Files:**
- Create: `src/main/java/com/minicad/app/PreviewPayloadTypesV2.java`
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 检查是否已存在PreviewPayloadTypes**

Run: `ls src/main/java/com/minicad/app/PreviewPayloadTypes.java`

- [ ] **Step 2: 创建PreviewPayloadTypesV2**

如果PreviewPayloadTypes已存在且内容相似，考虑整合或创建V2

```java
package com.minicad.app;

import java.util.List;

/**
 * Payload type definitions for preview exporters.
 */
class PreviewPayloadTypesV2 {
    // Main payload types
    static class PreviewPayload {
        List<FacePayload> faces;
        List<EdgePayload> edges;
        List<VertexPayload> vertices;
        GeometrySummary geometrySummary;
        AssemblyData assembly;
        List<PmiPayload> pmi;
    }

    static class FacePayload {
        String type;
        List<Point3> vertices;
        List<Integer> triangles;
        // ... 其他字段
    }

    static class EdgePayload {
        String type;
        List<Point3> points;
        // ... 其他字段
    }

    static class VertexPayload {
        Point3 position;
    }

    static class GeometrySummary {
        int faceCount;
        int edgeCount;
        int vertexCount;
        BoundingBox3 boundingBox;
    }

    static class AssemblyData {
        List<AssemblyNode> nodes;
        String rootName;
    }

    static class PmiPayload {
        String type;
        String text;
        Point3 position;
    }

    static class SemanticTarget {
        int entityId;
        String entityType;
        String description;
    }

    // ... 其他辅助类型
}
```

- [ ] **Step 3: 找到所有类型定义**

检查StepPreviewJsonExporter中的内部类和类型定义

- [ ] **Step 4: 移动所有类型定义**

复制所有payload类型定义（约500行）

- [ ] **Step 5: 更新各个Builder的引用**

```java
// 各Builder中使用PreviewPayloadTypesV2的类型
PreviewPayloadTypesV2.FacePayload facePayload = new PreviewPayloadTypesV2.FacePayload();
```

- [ ] **Step 6: 运行测试**

Run: `mvn test -Dtest=StepPreviewJsonExporterTest`

Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add src/main/java/com/minicad/app/PreviewPayloadTypesV2.java
git commit -am "refactor(phase3): create PreviewPayloadTypesV2 and move type definitions"
```

---

### Task 3.9: 清理StepPreviewJsonExporter主类

**Files:**
- Modify: `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

- [ ] **Step 1: 检查剩余行数**

Run: `wc -l src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

Expected: 应该接近800行目标

- [ ] **Step 2: 清理import**

移除已迁移方法相关的import

- [ ] **Step 3: 整理主流程**

```java
public class StepPreviewJsonExporter {
    // Builder实例
    private PreviewPayloadBuilderV2 payloadBuilder;
    private PreviewFaceBuilderV2 faceBuilder;
    private PreviewEdgeBuilderV2 edgeBuilder;
    private PreviewPmiBuilder pmiBuilder;
    private PreviewAssemblyBuilder assemblyBuilder;
    private PreviewTargetCollector targetCollector;
    private PreviewSamplingUtilsV2 samplingUtils;

    // 公共API
    public static String export(String stepText) {
        StepFile file = StepParser.parse(stepText);
        Map<Integer, StepEntity> entities = StepEntityResolver.resolveAll(file);
        StepCadBuilder cadBuilder = new StepCadBuilder(entities);
        StepSolidModel solid = cadBuilder.buildSolid();

        return new StepPreviewJsonExporter(entities).buildJson(solid);
    }

    public static byte[] exportBinary(String stepText) {
        // ... 实现
    }

    public static byte[] exportGlb(String stepText) {
        // ... 实现
    }

    // 主流程编排
    private String buildJson(StepSolidModel solid) {
        PreviewPayload payload = payloadBuilder.buildPayload(solid);
        List<PmiPayload> pmi = pmiBuilder.buildPmiPayloads(productDef);
        AssemblyData assembly = assemblyBuilder.buildAssemblyData(productDef);
        List<SemanticTarget> targets = targetCollector.collectSemanticTargets(productDef);

        // 组装最终JSON
        return toJson(payload, pmi, assembly, targets);
    }
}
```

- [ ] **Step 4: 最终行数检查**

Run: `wc -l src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

目标: < 2000行

- [ ] **Step 5: 运行完整测试**

Run: `mvn -B clean test`

Expected: PASS - 所有测试通过

- [ ] **Step 6: 黄金文件测试（关键文件）**

Run: 对关键example文件测试：

```bash
mvn exec:java -Dexec.args="examples/minimal-square.step"
mvn exec:java -Dexec.args="examples/plate-with-round-hole.step"
mvn exec:java -Dexec.args="examples/engine.stp"
mvn exec:java -Dexec.args="examples/bspline-patch.step"
mvn exec:java -Dexec.args="examples/conical-hole.step"
```

Expected: 所有输出与之前一致

- [ ] **Step 7: 黄金文件测试（完整套件）**

Run: `find examples -name "*.step" -o -name "*.stp" | xargs -I {} mvn exec:java -Dexec.args="{}"`

Expected: 所有文件成功处理

- [ ] **Step 8: 提交Phase 3完成**

```bash
git tag phase3-steppreviewjsonexporter-refactoring-complete
git commit -am "refactor(phase3): complete StepPreviewJsonExporter refactoring - all builders and utilities extracted"
```

---

## 最终验证

### Task 4.1: 行数验证

- [ ] **Step 1: 验证所有目标文件行数**

Run: `find src/main/java -name "*.java" -exec wc -l {} + | awk '$1 > 2000'`

Expected: 无文件超过2000行

- [ ] **Step 2: 验证新文件行数**

Run: 检查所有新创建的文件行数：

```bash
wc -l src/main/java/com/minicad/step/semantic/StepCad*.java
wc -l src/main/java/com/minicad/step/semantic/StepResolver*.java
wc -l src/main/java/com/minicad/step/semantic/StepEntity*.java
wc -l src/main/java/com/minicad/app/Preview*.java
```

Expected: 所有新文件 < 2000行

- [ ] **Step 3: 提交最终验证**

```bash
git add docs/superpowers/plans/2026-07-04-large-file-refactoring-implementation.md
git commit -m "docs: add complete refactoring implementation plan"
git tag refactoring-complete-all-phases
```

---

### Task 4.2: 测试覆盖率验证

- [ ] **Step 1: 运行完整测试套件**

Run: `mvn -B clean test`

Expected: 所有测试通过，无编译错误

- [ ] **Step 2: 检查新增测试文件**

Run: `find src/test/java -name "*Builder*Test.java" -o -name "*Resolver*Test.java" -o -name "*Preview*Test.java"`

Expected: 所有新测试文件存在

- [ ] **Step 3: 黄金文件对比**

如果有黄金文件对比工具，运行对比验证

- [ ] **Step 4: 最终提交**

```bash
git push origin main --tags
```

---

## 成功标准

- ✅ StepCadBuilder.java < 2000行
- ✅ StepEntityResolver.java < 2000行
- ✅ StepPreviewJsonExporter.java < 2000行
- ✅ 所有新创建文件 < 2000行
- ✅ 所有单元测试通过
- ✅ 黄金文件测试输出一致
- ✅ 无编译错误
- ✅ 代码职责清晰，易于维护

---

**计划状态**: 完成
**下一步**: 选择执行方式（Subagent-Driven 或 Inline Execution）