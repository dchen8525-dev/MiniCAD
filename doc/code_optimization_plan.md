# MiniCAD 代码优化规划

> 生成日期：2026-04-28。本文面向代码质量、解析性能、可维护性和验证闭环；STEP 实体能力补齐仍以现有实体缺口/实施顺序文档为主。

## 当前基线

- `src/main/java` 当前约 98k 行代码，复杂度主要集中在 STEP 解析/构建/预览导出链路。
- 最大文件：
  - `StepPreviewJsonExporter.java`：约 18.5k 行，混合了预览构建、参数曲面采样、PMI、JSON/Binary/GLB 序列化。
  - `StepEntityResolver.java`：约 17.2k 行，混合了注册表、实体解析、通用参数读取、类型兼容判断。
  - `StepCadBuilder.java`：约 6.7k 行，承担 2D/3D 几何、拓扑、solid、CSG/swept/tessellated 构建。
- 已有测试覆盖语法解析、resolver、builder、preview/export、viewer 静态资源等主路径；优化必须保持 `mvn test` 通过。

## 本轮已落地

- 语法层 section 扫描改为跳过字符串/注释后的大小写无关匹配，避免为大 STEP 文本额外创建完整大写副本。
- `StepFile.entitiesById()` 改为不可变索引，并按实体数量预估 `LinkedHashMap` 容量。
- `StepEntityInstance.name()` 改为构造期缓存，避免复杂实体名重复拼接。
- 拓扑预览面的通用曲面采样密度抽成 `TOPOLOGY_SURFACE_GRID_SEGMENTS`，并从 `32x32` 降到 `16x16`，降低 `engine.stp` / `fan.stp` 这类大模型 GLB 导出的内存峰值。

## 优化目标

- 降低大 STEP 文件解析时的临时内存与重复扫描成本。
- 缩小核心大类单文件职责，降低后续实体补齐和预览修复的回归风险。
- 固化不可变数据边界，避免缓存对象被外部调用者修改。
- 给性能和正确性改动补可复现测试，优先小步提交。

## 优先级路线

### P0：低风险解析与缓存优化

- 避免对完整 STEP 文本做全量大小写复制；section 定位应在跳过字符串和注释的同时做大小写无关匹配。
- `StepFile.entitiesById()` 返回不可变索引，防止解析缓存被外部修改。
- 对高频派生值做构造期缓存，例如复杂实体组合名，避免反复 stream/reduce 分配。
- 清理明显死代码，保持语法层轻量。

完成标准：

- 语法层新增字符串/注释中出现 `HEADER;`、`DATA;`、`ENDSEC;` 的回归测试。
- `StepParserTest` 通过，随后跑全量 `mvn test`。

### P1：拆分预览导出职责

- 将 `StepPreviewJsonExporter` 拆成明确子组件：
  - `PreviewPayloadBuilder`：从 resolved/builder 构建 `PreviewPayload`。
  - `PreviewSurfaceSampler`：曲面参数化、采样、triangulation。
  - `PreviewEdgeSampler`：边/曲线采样和 loose edge 逻辑。
  - `PreviewPmiBuilder`：PMI 目标、leader、annotation payload。
  - `PreviewSerializers`：JSON、binary、GLB 输出。
- 拆分必须保持 payload record 不破坏前端契约，先迁移纯函数，再迁移有状态依赖。

完成标准：

- 每次拆分后 `StepPreviewJsonExporterTest`、`StepMeshExporterTest`、`StepViewerStaticResourcesTest` 通过。
- 不改变导出 JSON 字段名和现有 example 渲染统计。

### P2：拆分语义解析注册与参数读取

- 将 `StepEntityResolver` 中的通用参数读取工具抽到 `StepParameterReader`。
- 将 `createRegistry()` 拆成领域注册器，例如 geometry/topology/product/annotation/tolerance/fea。
- 保留注册顺序语义，复杂实体选择逻辑必须有测试锁定。

完成标准：

- resolver 注册数量和关键实体解析行为不变。
- 复杂实体、多定义继承、forward reference、missing reference 测试保持通过。

### P3：Builder 缓存与构建边界收敛

- 将 `StepCadBuilder` 的 curve/surface/topology/solid 构建入口拆到已有 helper builder 中，主类保留门面和缓存协调。
- 对 CSG、swept、tessellated 这类部分支持功能，先补限制条件和错误信息测试，再优化几何正确性。
- 对重复采样逻辑建立统一的采样预算和退化几何处理策略。

完成标准：

- `unsupportedFaceCount`、mesh face/edge counts、solid/shell 构建测试稳定。
- 不引入 silently wrong；无法构建时给出可解释错误。

## 验证策略

- 快速回归：`mvn -q -Dtest=StepParserTest test`
- STEP 语义回归：`mvn -q -Dtest=StepEntityResolverTest,StepCadBuilderTest test`
- 导出回归：`mvn -q -Dtest=StepPreviewJsonExporterTest,StepMeshExporterTest test`
- 全量回归：`mvn test`

## 风险控制

- 大文件拆分只移动代码，不同时改行为。
- 性能优化必须配套行为测试；如没有基准测试，至少补稳定样例和边界输入。
- 优先不可变与纯函数改造，推迟跨模块接口调整。
- 遇到导出 payload 结构变更，必须同步前端静态资源和测试。
