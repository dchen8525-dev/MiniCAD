# MiniCAD STEP AP242 Ed2 100% 覆盖计划

## Context

当前 MiniCAD 覆盖 AP242 Ed2 标准 735/2122 实体（34.6%），缺失 1387 个实体。
数据来源：ISO 官方 EXPRESS schema（[ISO SMRL v8](https://standards.iso.org/iso/10303/smrl/v8/tech/smrlv8.zip)）。

**关键发现**：
- 1387 个缺失实体全部需要新建 `StepXxx.java` 记录类（无现有匹配）
- 约 246 个实体可复用已有 resolver 模式，1141 个需要全新 resolver 方法
- 大部分缺失实体是 EXPRESS SUBTYPE（属性从父类继承）
- 147 个表达式/函数实体构成独立的表达式语言，需要特殊处理

## 工作分 6 个阶段，按优先级递减

### Phase 1: 核心几何与拓扑（~180 实体，B-Rep 生成）

**目标**：让真实 STEP 文件的几何/拓扑能正确解析和渲染

| 子领域 | 数量 | 说明 | B-Rep |
|--------|------|------|-------|
| B-Spline 曲线变体 | 25 | B_SPLINE_CURVE_SEGMENT, POLY_CURVE, BEZIER_VOLUME 等 | 部分 |
| B-Spline 曲面变体 | 20 | B_SPLINE_SURFACE_PATCH, STRIP, KNOT_LOCATOR 等 | 部分 |
| 高级拓扑 | 40 | 复杂壳、拓扑检查、边/面关系 | 部分 |
| 扫掠曲面 | 10 | SWEEPED_SURFACE, SWEEPING_SURFACE 等 | 是 |
| 扫掠体 | 15 | CSG_SOLID_2D, ELLIPSOID_VOLUME, 锥体等 | 是 |
| 截面轮廓 | 15 | CIRCULAR_CLOSED_PROFILE, RECTANGULAR_CLOSED_PROFILE 等 | 是 |
| 加工特征实体 | 50 | HOLE, BOSS, SLOT, POCKET, CHAMFER 等 | 是 |
| 矩阵类型 | 5 | 各种矩阵定义 | 否 |

**关键文件**：
- `src/main/java/com/minicad/step/model/geometry/` — 新建 Step 记录
- `src/main/java/com/minicad/step/model/topology/` — 新建 Step 记录
- `src/main/java/com/minicad/geometry/` — 可能需要新 domain class
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java` — 新增 resolver 方法
- `src/main/java/com/minicad/step/semantic/StepCadBuilder.java` — 新增 build 方法
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java` — 新增导出支持

### Phase 2: PMI 标注与公差（~150 实体，无需 B-Rep）

**目标**：支持制造标注、尺寸公差、几何公差的解析和预览

| 子领域 | 数量 | 说明 |
|--------|------|------|
| 具体公差类型 | 35 | ANGULARITY_TOLERANCE, FLATNESS_TOLERANCE 等 |
| 尺寸标注 | 30 | ANGULAR_DIMENSION, DIAMETER_DIMENSION 等 |
| PMI 注释 | 25 | ANNOTATION_OCCURRENCE 等 |
| 样式/颜色 | 50 | 曲线样式、填充样式、颜色、字体等 |
| 文本 | 10 | 复合文本、标注文本等 |

**关键文件**：
- `src/main/java/com/minicad/step/model/tolerance/` — 新建记录
- `src/main/java/com/minicad/step/model/annotation/` — 新建记录
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java` — 批量注册（扩展现有 `registerGeometricToleranceAliases` 等）
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java` — PMI 预览

### Phase 3: 产品与装配结构（~120 实体，无需 B-Rep）

**目标**：支持装配层次结构、产品定义、配置管理

| 子领域 | 数量 | 说明 |
|--------|------|------|
| 产品定义链 | 30 | PRODUCT 变体、PRODUCT_DEFINITION 变体 |
| 装配组件 | 40 | ASSEMBLY_COMPONENT, ASSEMBLY_JOINT 等 |
| 配置管理 | 25 | EFFECTIVITY, CHANGE 等 |
| 表示与映射 | 15 | REPRESENTATION 变体、REPRESENTATION_MAP 等 |
| 几何约束 | 10 | 装配约束、几何约束 |

**关键文件**：
- `src/main/java/com/minicad/step/model/product/` — 新建记录
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java` — 批量注册
- `src/main/java/com/minicad/app/StepAssemblyGraphBuilder.java` — 装配图构建

### Phase 4: 元数据与领域扩展（~300 实体，纯解析）

**目标**：覆盖各业务领域的元数据实体

| 子领域 | 数量 | 说明 |
|--------|------|------|
| 运动学扩展 | 50 | 额外运动副类型、机构分析 |
| 元数据 | 30 | 分组、分类、标识 |
| 单位扩展 | 25 | SI 单位变体、换算单位 |
| 时间 | 15 | 日期时间赋值、时间区间 |
| 组织 | 12 | 项目、地址 |
| 动作 | 20 | 动作变体 |
| 文档 | 5 | 文档标识 |
| 审批 | 3 | 审批关系 |
| 资源 | 10 | 属性、分析 |
| 验证 | 3 | 验证实体 |
| 工作流 | 5 | 工艺计划 |
| 制造 | 5 | 增材制造 |
| 材料 | 5 | 材料指定 |
| 数据结构 | 15 | 集合、聚合 |
| A3M 等效性 | 40 | AP242 Ed2 专属 |
| 质量检查 | 5 | 几何质量标记 |

**关键文件**：
- 各 `step/model/` 子包 — 新建记录
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java` — 批量注册
- 可能需新增 `step/model/a3m/` 等子包

### Phase 5: 表达式语言（~150 实体，特殊处理）

**目标**：解析 EXPRESS 表达式语言（函数、变量、运算符）

**策略**：先做 AST 透传（Opaque Pass-Through），不做求值引擎
- 每个表达式实体映射为 StepXxx 记录，保存操作数引用
- 不做求值，仅保留结构
- 足够应对工业 STEP 文件中的参数化约束

**关键文件**：
- `src/main/java/com/minicad/step/model/expression/` — 新建子包（~50 个记录类）
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java` — 表达式解析方法
- `src/main/java/com/minicad/step/semantic/ExpressionOps.java` — 可选，未来求值引擎

### Phase 6: 其他与清理（~487 实体，纯解析）

**目标**：扫尾剩余实体

主要是各种关系、分配、特殊用途实体。大部分是 3-6 个参数的透传。

## 自动化方案

编写 Python 脚本从 EXPRESS schema 批量生成代码：

```
generate_from_schema.py
  输入: ap242e2_schema.exp (49943 行)
  输出:
    - StepXxx.java 记录类 (每个实体 5-15 行)
    - resolveXxx() 方法 (每个实体 5-10 行)
    - registry.put("XXX", ...) 注册行
```

**自动化范围**：
- Phase 1 中不需要 B-Rep 的实体（约 40%）→ 完全自动生成
- Phase 2-4 的纯解析实体（约 570 个）→ 完全自动生成
- Phase 5 的表达式实体 → 部分生成（需手动设计 AST 结构）
- Phase 1 中需要 B-Rep 的实体 → 需手动编写 build 方法

**预计效果**：自动化可覆盖约 70% 的工作量（~970/1387 实体）

## 执行顺序

```
Phase 1（几何拓扑）     ← 最高优先级，直接影响 STEP 文件渲染
  ↓
Phase 2（PMI/公差）     ← 制造场景关键
  ↓
Phase 3（装配结构）     ← 装配体支持
  ↓
Phase 4（元数据）       ← 批量生成，快速覆盖
  ↓
Phase 5（表达式）       ← 特殊处理，可延后
  ↓
Phase 6（扫尾）         ← 自动化批量处理
```

## 验证方式

每个阶段完成后：
1. `mvn test` — 确保所有测试通过
2. 用 `engine.stp`（93829 实体）验证解析无错误
3. 用 `fan.stp`（41707 实体）验证解析无错误
4. 统计覆盖率：`已注册实体数 / 2122`

## 关键参考

- AP242 Ed2 schema: ISO STEP Module Repository (`mim_lf.exp`)
- 已注册实体列表: `StepEntityResolver.java` 中的 `registry.put()` 调用
- 现有 resolver 模式: 674 个方法，15 个批量注册辅助方法
- 现有 model 记录: 1172 个 `StepXxx.java` 文件
