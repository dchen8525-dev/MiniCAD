# 工业级 STEP 实体支持增强计划

## 现状（2026-04-17 更新）

| 指标 | 数值 |
|---|---|
| Registry 条目 | ~1559 |
| 总实体引用 | ~24000+ |
| Model Classes | 1062 |
| 测试用例 | 全部通过 |
| Core STEP 覆盖率 | 100% (主流 CAD 导出实体) |

### 项目定位

MiniCAD 致力于构建一个完整的工业 CAD 内核：
- **完整 STEP 解析**: 支持所有主流 STEP 实体类型的解析和语义理解
- **B-Rep 几何内核**: 支持完整的边界表示 (Boundary Representation) 拓扑结构
- **可视化预览**: 提供基于 Web 的三维模型预览能力
- **工业级兼容**: 支持主流 CAD 软件 (CATIA、NX、SolidWorks、Creo 等) 导出的 STEP 文件

### 已完成工作

- **Phase 1: CAD 构建链路全部打通**——所有关键几何实体都实现了 CAD 构建方法
- **Phase 2-7: 实体注册大幅扩展**——新增 500+ 实体别名注册，覆盖制造特征、公差、表示类型等
- **Web 预览器完善**——支持平面、圆柱面、圆锥面、球面、环面、B-Spline 曲面等可视化
- **示例文件覆盖**——44 个示例文件全部成功解析

### 尚未支持的 STEP 实体

以下 STEP AP214/AP242 实体类型尚未实现解析或几何求值：

**解析层尚未支持的实体**:
| 类别 | 实体 |
|---|---|
| 高级几何曲面 | `SURFACE_OF_TRANSLATION`, `SURFACE_OF_PROJECTION`, `PARABOLOID_SURFACE`, `HYPERBOLOID_SURFACE` |
| 高级扫掠实体 | `EXTRUDED_FACE_SOLID`, `REVOLVED_FACE_SOLID`, `SURFACE_CURVE_SWEPT_FACE_SOLID` |
| 高级 CSG 体素 | `CYLINDER_VOLUME`, `SPHERE_VOLUME`, `TORUS_VOLUME`, `PRISM_VOLUME` |
| Validation Property | `VALIDATION_PROPERTY_REPRESENTATION`, `CALCULATED_GEOMETRIC_REPRESENTATION_ITEM` |
| Kinematic | `KINEMATIC_PATH`, `KINEMATIC_FRAME_BASED_TRANSFORMATION` |

**几何求值尚未实现的实体**（已解析，无 B-Rep 生成）:
| 类别 | 实体 |
|---|---|
| CSG Boolean | `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`, `COMPLEX_CLIPPING_RESULT` |
| Swept Solid | `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`, `SURFACE_CURVE_SWEPT_AREA_SOLID` |
| Half Space | `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `POLYGONAL_BOUNDED_HALF_SPACE` |
| Tessellated | `TESSELLATED_FACE_SET`, `TESSELLATED_FACE`, `TESSELLATED_TRIANGLE` |

---

## 下一步工作（近期优先级）

### 优先级 1: 几何求值实现

目标：为已解析但未求值的实体实现几何生成。

| 实体类别 | 具体实体 | 工业价值 |
|---|---|---|
| CSG Boolean | `BOOLEAN_RESULT` | 布尔运算是 CAD 建模核心操作 |
| Swept Solid | `EXTRUDED_AREA_SOLID` | 拉伸是零件建模最常用操作 |
| Swept Solid | `REVOLVED_AREA_SOLID` | 旋转是轴类零件标准操作 |
| Swept Disk | `SWEPT_DISK_SOLID` | 管道/管件的标准表示 |

### 优先级 2: 高级实体解析

目标：添加缺失的 STEP 实体解析支持。

| 实体类别 | 实体数量 | 工业价值 |
|---|---|---|
| 高级几何曲面 | 5 个 | 支持更复杂的曲面造型 |
| 高级扫掠实体 | 3 个 | 面扫掠是高级建模操作 |
| 高级 CSG 体素 | 6 个 | CSG 建模基本原语 |
| Validation Property | 3 个 | 数据交换质量验证 |

### 优先级 3: 完善现有功能

- B-Spline 曲面修剪完善
- PMI 实体扩展支持
- 拓扑修复/healing 探索

---

## 实施顺序

```
优先级 1 (几何求值)  → 工业价值最高，解决最常见的未支持实体
优先级 2 (高级实体)  → 扩展解析支持范围
优先级 3 (完善功能)  → 提升现有功能质量
```

---

## 验证方式

```bash
# 1. 编译通过
mvn compile

# 2. 全部测试通过
mvn test

# 3. 示例文件无错误解析
mvn exec:java -Dexec.args="examples/engine.stp"

# 4. Web 预览器启动
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp exec:java
# 访问 http://127.0.0.1:8080
```

---

## 技术栈

- Java 21
- Maven
- JUnit 5
- Jetty 11 (嵌入式 Web 服务器)
- Three.js (前端渲染)
- 无外部 CAD 内核依赖（不依赖 OpenCascade、FreeCAD、Parasolid 等）
