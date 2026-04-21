# MiniCAD STEP AP242 Ed2 覆盖推进计划

## 目标

这份计划用于推进 MiniCAD 对 AP242 Ed2 的覆盖，但当前仓库已经明显超出旧版文档描述的支持范围，因此后续工作不能再基于“735/2122、缺失 1387”这一类静态数字直接排期。

后续一律按以下三层口径区分“覆盖”：

1. `schema coverage`
   `mim_lf.exp` 中的标准实体，MiniCAD 能识别并分派到 resolver。
2. `semantic coverage`
   实体不仅已注册，而且能稳定 resolve 成内部 `StepEntity` 模型。
3. `runtime coverage`
   实体在真实文件中可进一步参与装配、PMI、预览导出或 B-Rep 构建，而不是仅停留在透传解析。

计划目标不是机械追求 `registry.put()` 数量，而是优先提升真实工业文件的可导入性和可预览性，同时用脚本持续收敛标准实体差集。

## 当前现状

当前仓库已经具备以下基础，不应再按”待支持”重复排期：

- `StepEntityResolver` 已覆盖大批几何、拓扑、装配、PMI、验证、运动学、FEA 和样式相关实体。
- 多类实体已通过 alias/helper 批量注册，而不是”一实体一 resolver”。
- `StepAssemblyGraphBuilder` 已存在，装配图并非空白能力。
- `StepPreviewJsonExporter` 已具备大模型文件验证路径。
- `examples/engine.stp` 当前测试基线为 `93829` 实体。
- `examples/fan.stp` 当前测试基线为 `41905` 实体。

### Phase 0 基线数据（由 `CoverageAnalyzer` 生成）

| 指标 | 数值 |
|------|------|
| AP242 Ed2 标准实体 | 2122 |
| Resolver 注册总数 | 2085 |
| 注册的标准实体 | 860 |
| MiniCAD 别名（非标准） | 1225 |
| StepXxx 模型类 | 1172 |
| Schema coverage | 40.5% |
| Semantic coverage | 19.5% |
| 已注册且有模型类 | 414 |
| 已注册 alias 覆盖 | 125 |
| 已注册但无模型类 | 446 |
| 未注册（schema gap） | 1262 |

详细报告见 `doc/generated/ap242-gap-report.md`。

当前 `step/model` 已存在多个按领域拆分的子包：

- `action`
- `analysis`
- `annotation`
- `approval`
- `classification`
- `config_mgmt`
- `date_time`
- `document`
- `fea`
- `geometry`
- `kinematic`
- `manufacturing`
- `organization`
- `product`
- `profile`
- `resource`
- `tolerance`
- `topology`
- `unit`
- `validation`
- `workflow`

因此，这份计划的第一原则是：

- 先生成“标准 schema 差集”
- 再决定是否需要新增 model 类、复用现有 model、扩展 alias 注册，还是补运行时几何能力

## 基线重建

旧计划的问题不在于方向，而在于基线已过期。后续所有数字必须由脚本生成，不再手工维护。

### 必做产物

新增一个覆盖统计脚本，输入 AP242 Ed2 `mim_lf.exp`，输出以下 4 份结果：

1. 标准实体全集
2. 当前 resolver 已注册的标准实体集合
3. 当前已有 `StepXxx` 模型集合
4. 差集分类报告

差集分类至少分成以下几类：

- `unregistered`: schema 中存在，但 resolver 未注册
- `registered-no-runtime`: 已注册但没有 B-Rep/PMI/装配导出能力
- `alias-covered`: 通过通用 resolver 或 alias 已覆盖，不需要新增独立类
- `runtime-gap`: resolve 成功，但真实文件导出或构建仍缺能力

### 建议输出文件

- `doc/generated/ap242-entities.txt`
- `doc/generated/ap242-registered-standard-entities.txt`
- `doc/generated/ap242-model-classes.txt`
- `doc/generated/ap242-gap-report.md`

### 判定规则

- 覆盖率分母固定为 AP242 Ed2 `mim_lf.exp` 中的标准实体名集合
- MiniCAD 自定义别名不计入分母
- 一个实体是否“已支持”，以它能否落到稳定 resolver 为准
- 一个实体是否“已完成”，取决于所在阶段目标，不默认要求 B-Rep

## 分阶段执行

### Phase 0: 基线与自动化

目标：让后续计划建立在可复算数据上。

交付：

- 生成 AP242 Ed2 标准实体清单
- 生成 resolver 注册清单
- 生成 model 类清单
- 生成差集报告和分阶段候选列表
- 在文档中固定统计口径

涉及文件：

- `tools/` 或 `scripts/` 下新增覆盖统计脚本
- `doc/generated/` 下新增报告产物
- `doc/coverage-plan.md`

完成标准：

- 任意一次代码变更后都能重新生成差集
- 文档中的数字全部来自脚本输出

### Phase 1: 真实文件优先的几何与 B-Rep 缺口

目标：优先解决会阻断真实 STEP 文件导入、装配浏览、GLB 导出和网格预览的实体。

优先级：

- 高优先级几何/拓扑实体
- 已注册但未进入 B-Rep 构建的实体
- 已 resolve 但会导致 `unsupportedFaceCount`、空壳、空 solid、几何退化的实体

这阶段不再笼统写”扫掠体、曲面、特征全部待做”，而是以差集报告中的真实缺口为准。像 `EXTRUDED_AREA_SOLID`、`REVOLVED_AREA_SOLID`、`SURFACE_OF_TRANSLATION`、`SURFACE_OF_PROJECTION`、`PARABOLOID_SURFACE`、`HYPERBOLOID_SURFACE` 这类当前已经注册的实体，不应再作为”未支持解析”统计。

#### Phase 1 实际评估结果

对全部 45 个 example 文件执行 `StepDumpApp` 验证后：

| 文件 | solids | unsupported faces | 状态 |
|------|--------|-------------------|------|
| engine.stp | 31 | 0 | 已稳定 |
| fan.stp | 1 | 0 | 已稳定 |
| nested-assembly.step | 1 | 0 | 已稳定 |
| two-instance-assembly.step | 1 | 0 | 已稳定 |
| translated-part-assembly.step | 1 | 0 | 已稳定 |
| test.step (AP214) | 1 | 0 | 已稳定 |
| 其余 38 个几何/PMI 样例 | 0-1 | 0-12 | 多数已稳定 |

**结论：工业文件（engine.stp、fan.stp）的几何链路已经完整，不存在阻断导入的缺口。**

对 1262 个未注册标准实体逐一扫描 engine.stp 和 fan.stp 内容后，发现**没有任何一个未注册实体出现在真实工业文件中**。这些实体属于 AP242 的扩展特性（A3M 等效检验、隐式曲线、局部细化样条、加法制造等），不属于当前需要优先处理的目标。

#### 已知可改进项

1. **顶点-曲线容差问题** (`Edge.java` `liesOnCurve`)：
   - 合成测试文件中部分圆锥/环面 seam 边的顶点不在曲线上（容差 0.01 不够）
   - 这是合成文件的几何精度问题，不影响 engine.stp / fan.stp
   - 可后续考虑自适应容差（基于模型尺寸和 UNCERTAINTY_MEASURE_WITH_UNIT）

2. **conical/toroidal 测试文件** 中 `unsupportedFaces` 来源：
   - `conical-hole.step`: 5 faces, `edge_vertex_off_curve`
   - `conical-two-holes.step`: 12 faces
   - `toroidal-two-holes.step`: 10 faces
   - 这些都是 seam 曲线在锥面/环面顶点处的退化边问题

涉及文件：

- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`
- `src/main/java/com/minicad/geometry/`
- `src/main/java/com/minicad/topology/`
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- 对应测试

完成标准：

- `engine.stp`、`fan.stp`、`nested-assembly.step`、相关几何样例可稳定导入 — **已完成**
- 新增能力有回归测试 — **已有 1430 个测试通过**
- 文档明确区分”已解析”和”已生成 B-Rep” — **本节已说明**

### Phase 2: PMI、验证与表示层补齐

目标：补齐制造标注、公差、验证属性、表示关系和样式导出的缺口。

重点对象：

- 几何公差变体
- 尺寸与注释表示
- `VALIDATION_PROPERTY_REPRESENTATION` 一类验证相关对象
- presentation/style/text/font/fill/curve style 家族

策略：

- 优先复用现有 alias 注册模式
- 对纯语义透传实体，优先保证 resolve 稳定
- 仅在预览确有消费路径时扩展 exporter

涉及文件：

- `src/main/java/com/minicad/step/model/tolerance/`
- `src/main/java/com/minicad/step/model/annotation/`
- `src/main/java/com/minicad/step/model/validation/`
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

完成标准：

- PMI/验证样例可稳定解析
- 纯语义实体不会因缺 exporter 而阻断整个文件
- 文档标明哪些 PMI 仅解析，哪些已预览

### Phase 3: 产品结构、装配与配置管理缺口

目标：补齐当前装配链路中尚未覆盖的标准实体，而不是重复建设已存在的装配图能力。

重点对象：

- 产品定义与关系变体
- effectivity / configuration management
- 表示映射和上下文依赖关系
- 装配约束和几何关系

策略：

- 以 `StepAssemblyGraphBuilder` 已有能力为基础扩展
- 优先修补“resolve 成功但未进入装配图”的差集
- 避免为单个标准名重复创建等价模型

涉及文件：

- `src/main/java/com/minicad/step/model/product/`
- `src/main/java/com/minicad/step/model/config_mgmt/`
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/app/StepAssemblyGraphBuilder.java`
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

完成标准：

- 典型多实例装配样例可稳定形成装配树
- 变换、引用关系、effectivity 不再导致装配丢失

### Phase 4: 纯解析领域批量收敛

目标：用生成器收敛不影响运行时几何的标准实体缺口。

候选领域：

- `organization`
- `approval`
- `document`
- `date_time`
- `workflow`
- `resource`
- `classification`
- `unit`
- `analysis`
- `manufacturing`
- `kinematic`
- `fea`

策略：

- 先尝试复用现有通用模型或基类
- 能 alias 的不强制生成独立类
- 仅在 schema 属性结构确有区别时新增专用 `StepXxx`

这阶段的目标是提高 `schema coverage` 和 `semantic coverage`，不是追求运行时几何能力。

### Phase 5: 表达式与参数化语言

目标：对 EXPRESS 表达式相关实体建立稳定的结构化表示。

当前原则：

- 先做 AST/引用结构透传
- 不在本阶段实现求值引擎
- 不因为缺少求值而阻断文件整体解析

注意事项：

- 这里不能简单按“一个标准实体对应一个新 record”规划
- 部分表达式实体更适合归入统一 AST 层次，而不是生成大量彼此重复的记录类

涉及文件：

- `src/main/java/com/minicad/step/model/`
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- 可选新增表达式专用子包或抽象层

### Phase 6: 收尾与口径维护

目标：让覆盖工作可持续，而不是写完一次文档后再次过期。

交付：

- 覆盖统计脚本纳入日常使用
- 文档引用脚本输出，而不是手填数字
- 为新增领域补最小验证样例
- 定期清理“已完成但仍在待办列表”的条目

## 自动化原则

自动化应该生成的是“候选实现”，不是未经筛选的大量样板代码。

生成器职责：

- 解析 schema 实体名、继承关系、参数列表
- 给出推荐实现方式
- 区分“新建模型类”“复用现有模型”“alias 注册”“仅需 runtime 补齐”

不建议默认生成：

- 所有 `resolveXxx()` 方法
- 所有 `StepXxx.java` 记录类
- 所有 `registry.put()` 行

原因：

- 当前仓库已经广泛使用通用 resolver 和批量 alias 注册
- 直接铺开生成会放大冗余类型和维护成本
- 对许多 SUBTYPE 来说，运行时能力缺口比“是否缺一个 record 类”更关键

## 验证方式

每个阶段完成后至少执行：

1. `mvn test`
2. `mvn -q -Dtest=StepEntityResolverTest test`
3. `mvn -q -Dtest=StepPreviewJsonExporterTest test`
4. 用 `examples/engine.stp` 验证解析与导出稳定，当前实体基线为 `93829`
5. 用 `examples/fan.stp` 验证解析与导出稳定，当前实体基线为 `41905`
6. 用针对该阶段的最小样例验证新增实体

当阶段目标涉及几何导出时，还应记录：

- `solidCount`
- `unsupportedFaceCount`
- 是否出现空 mesh、空 assembly node、空 representation

## 近期执行顺序

建议按以下顺序推进：

1. Phase 0，先把差集报告做出来
2. Phase 1，处理真实文件仍有影响的几何/B-Rep 缺口
3. Phase 2，补齐 PMI、验证和表示层
4. Phase 3，收敛装配与配置管理缺口
5. Phase 4，批量提升纯解析覆盖
6. Phase 5，最后处理表达式语言

## 关键参考

- AP242 Ed2 schema: ISO STEP Module Repository `mim_lf.exp`
- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/app/StepAssemblyGraphBuilder.java`
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- `src/test/java/com/minicad/step/semantic/StepEntityResolverTest.java`
- `src/test/java/com/minicad/app/StepPreviewJsonExporterTest.java`

## 维护约定

更新这份计划时，必须同时满足：

- 不把已注册实体重新写回“未支持列表”
- 不混淆“已注册”“已 resolve”“已导出/已构建 B-Rep”三种状态
- 所有数字都能通过脚本或测试基线回溯
- 示例文件实体数使用当前测试断言值，而不是手工估计值
