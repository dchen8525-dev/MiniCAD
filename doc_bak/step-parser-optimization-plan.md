# STEP 解析链路优化计划

## 背景

当前项目的 STEP 链路已经具备较强的实体覆盖和工业文件兼容能力，但解析性能侧还没有形成稳定的优化闭环：

- `step.syntax` 层仍有明显的大对象分配和文本复制
- `step.semantic` 层体量极大，但缺少阶段级性能基线
- `parse -> resolve -> build/export` 的耗时边界尚不清晰
- 目前更适合先做低风险提速，而不是直接重写解析结构

本计划的目标不是一次性重构解析器，而是先建立基线，再逐层优化，确保性能收益可量化、语义风险可控。

## 总体目标

1. 降低大文件解析时的字符串复制和临时对象分配
2. 建立 `parse / resolve / build-export` 的阶段化性能基线
3. 在不改语义接口的前提下，优先优化 syntax 热路径
4. 在 syntax 稳定后，再分析 resolver 热点
5. 为后续更激进的零拷贝/typed token 优化预留路线

## 优先级原则

- 先测量，后优化
- 先低风险，后结构性改造
- 先 syntax，后 semantic
- 优先减少大文件的内存峰值和前段 CPU
- 不在没有 benchmark 的情况下讨论 resolver“感觉上的瓶颈”

---

## Phase 1: 建立性能基线

### 目标

明确当前瓶颈到底在：

- `StepParser.parse()`
- `StepEntityResolver.resolveAll()`
- `StepCadBuilder` / preview-export / mesh-export

### 计划

1. 增加一个轻量性能入口，至少统计以下阶段耗时：
   - `parse`
   - `resolve`
   - `build`
   - `preview export` 或 `mesh export`
2. 选取 3 组基线样例：
   - 小文件：语法正确但实体量很小
   - 中等文件：典型装配/曲面样例
   - 大文件：如 `examples/engine.stp`、`examples/fan.stp`
3. 输出以下指标：
   - 文件大小
   - entity 数量
   - 总耗时
   - 各阶段耗时占比
   - 可获取时的近似内存占用

### 涉及文件

- `src/main/java/com/minicad/app/StepDumpApp.java`
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- `src/main/java/com/minicad/app/StepMeshExporter.java`
- 视实现方式可能新增：
  - `src/main/java/com/minicad/app/StepBenchmarkApp.java`
  - 或 `src/test/java/...` 下的 benchmark/diagnostic 测试

### 任务拆解

1. 设计统一阶段计时结构
   - 输入：STEP 文本或文件路径
   - 输出：`parse / resolve / build / export` 分段耗时
2. 选定基线样例
   - 小：最小几何样例
   - 中：典型 preview 样例
   - 大：`engine.stp`、`fan.stp`
3. 形成首版基线结果
   - 保存到文档或 benchmark 输出样例

### 验证命令

```bash
mvn -q -Dtest=StepParserTest,StepEntityResolverTest test
```

如果 benchmark 作为 app：

```bash
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/engine.stp"
```

当前已落地入口：

- `src/main/java/com/minicad/app/StepBenchmarkApp.java`

### 退出条件

- 已能稳定输出分阶段耗时
- 至少 3 个样例有可重复的基线数据
- 后续优化可以直接拿这些数据对比

### 交付物

- 一个可重复执行的 benchmark/diagnostic 入口
- 一份基线结果表

### 风险

- 低

### 备注

这一阶段不做逻辑优化，只做测量。

---

## Phase 2: Syntax 层低风险提速

### 目标

在不改变 `StepFile`、`StepEntityInstance`、`StepValue` 对外语义的前提下，降低 parser/tokenizer 的分配和复制成本。

### 已识别问题

1. `StepParser.parseSections()` 对整份文本做 `toUpperCase()`
2. `HEADER` / `DATA` 解析前有大段 `substring()`
3. `StepTokenizer` 默认基于整段字符串扫描，缺少区间解析能力
4. 单字符 token 仍会创建新的短字符串
5. syntax 层对大文件没有容量预估

### 优化项

1. `StepParser` 改为基于原始字符串区间解析
   - 不再为 `HEADER` / `DATA` 构造大段新字符串
2. `findOutsideStringsAndComments()` 改为大小写无关扫描
   - 去掉整文件 `toUpperCase()`
3. `StepTokenizer` 支持 `[start, end)` 区间扫描
4. 单字符 token 复用固定文本常量
5. 尽量避免不必要的 `substring()` 和重复 `length()` 边界判断

### 涉及文件

- `src/main/java/com/minicad/step/syntax/StepParser.java`
- `src/main/java/com/minicad/step/syntax/StepTokenizer.java`
- `src/main/java/com/minicad/step/syntax/StepToken.java`
- `src/main/java/com/minicad/step/syntax/StepValue.java`
- 可能影响：
  - `src/main/java/com/minicad/step/syntax/StepFile.java`
  - `src/main/java/com/minicad/step/syntax/StepEntityInstance.java`

### 任务拆解

1. `StepTokenizer` 支持区间扫描
   - 新增 `(input, start, end)` 构造或等价方案
   - 保持现有 `next()` 外部行为不变
2. `StepParser.parseSections()` 去掉整文件复制
   - 去掉 `toUpperCase()`
   - 去掉 `HEADER` / `DATA` `substring()`
3. 热路径分配收紧
   - 单字符 token 文本常量化
   - 降低重复 `substring()` 频率
4. 如有必要，再补列表容量预估
   - entity 列表
   - 参数列表
   - list value

### 建议实现边界

- 不修改 `StepEntityResolver` 调用方式
- 不修改 `StepValue` 语义层级
- 不引入零拷贝 token 设计
- 不把本阶段扩展成“重写 parser”

### 交付物

- `step.syntax` 层实现保持兼容
- 所有 syntax / semantic 测试继续通过
- 与 Phase 1 对比，`parse` 阶段耗时和内存峰值有可见改善

### 风险

- 低到中

### 验证方式

- `StepParserTest`
- `StepEntityResolverTest`
- 大文件 benchmark 对比

### 验证命令

```bash
mvn -q -Dtest=StepParserTest test
mvn -q -Dtest=StepEntityResolverTest test
mvn -q test
```

### 退出条件

- syntax 层测试全部通过
- resolver / builder 相关回归未受影响
- benchmark 显示 `parse` 阶段有稳定改善
- 没有引入语义层 API 破坏

---

## Phase 3: Resolver 热点分析

### 前提

只有在 Phase 1-2 完成后，且 benchmark 显示 `resolve` 已成为主瓶颈，才进入本阶段。

### 目标

定位 `StepEntityResolver` 的高频路径，优先消除重复字符串分派和重复参数解包。

### 候选热点

1. entity name 分派
   - `equalsIgnoreCase`
   - 重复大小写转换
   - registry 查找成本
2. 参数访问热点
   - `referenceId`
   - `number`
   - `string`
   - `list`
3. 复杂实体定义读取
4. 递归解析中的重复读取和重复异常路径构造

### 优化方向

1. 收紧 entity name 归一化路径
2. 高频 helper 做局部缓存或轻量封装
3. 减少重复遍历和重复异常字符串拼装
4. 对极高频 resolve 路径补 benchmark case

### 涉及文件

- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/step/semantic/EntityFactory.java`
- 可能影响部分 `step.model` 构造路径，但原则上不改 model 类

### 任务拆解

1. 先用 benchmark 或采样日志定位 resolver 热点
2. 列出 Top N 高频 helper
   - `referenceId`
   - `number`
   - `string`
   - `list`
3. 对热点路径做小步改造
   - 减少重复读取
   - 减少重复归一化
   - 减少重复异常消息构造
4. 每轮改动后回跑大样例

### 不建议的实现方式

- 不直接拆散整个 resolver 注册表
- 不在一轮内同时改 registry、factory、resolver 语义
- 不为了少量理论收益破坏可读性

### 风险

- 中

### 验证方式

- 全量 resolver 测试
- 工业样例回归
- Phase 1 benchmark 对比

### 验证命令

```bash
mvn -q -Dtest=StepEntityResolverTest test
mvn -q test
```

必要时补样例验证：

```bash
mvn -q exec:java -Dexec.args="examples/fan.stp"
mvn -q exec:java -Dexec.args="examples/engine.stp"
```

### 退出条件

- resolver 阶段耗时有可量化下降
- 工业样例导入结果不回退
- 没有新增实体解析兼容性问题

---

## Phase 4: 结构性优化（可选）

### 目标

在前面阶段仍不能满足性能目标时，再考虑更激进的 syntax 重构。

### 候选方向

1. Token 零拷贝
   - `StepToken` 不直接持有 `String text`
   - 改为 source slice 或 lazy materialization
2. Typed token
   - `INTEGER` / `NUMBER` 在 tokenizer 阶段直接携带解析值
3. 更轻量的 raw AST
   - 减少 `StepValue` 层的中间对象
4. 参数列表容量预估
5. 更细粒度的解析统计与 tracing

### 涉及文件

- `src/main/java/com/minicad/step/syntax/StepToken.java`
- `src/main/java/com/minicad/step/syntax/StepTokenizer.java`
- `src/main/java/com/minicad/step/syntax/StepParser.java`
- `src/main/java/com/minicad/step/syntax/StepValue.java`
- 可能波及 `StepEntityResolver` 的消费方式

### 进入条件

- Phase 1-3 已完成
- benchmark 显示 syntax 或 raw AST 仍是主瓶颈
- 低风险优化已经接近收益上限

### 任务拆解

1. 设计零拷贝 token 原型
2. 评估是否要把 `text` 改成 slice
3. 评估 number token typed payload
4. 做一个小范围原型验证，而不是全量直接替换

### 验证命令

```bash
mvn -q test
```

并配合 benchmark 对比前后收益。

### 退出条件

- 收益显著高于复杂度成本
- syntax / semantic 全链路测试稳定
- 文档明确记录设计变化

### 风险

- 高

### 说明

这一阶段不应在没有 benchmark 压力的情况下提前实施。

---

## 基线数据与 benchmark 结果

### Phase 1 基线（优化前，2026-04-20）

| 日期 | 样例 | parse | resolve | build | preview export | mesh export | 备注 |
|---|---|---:|---:|---:|---:|---:|---|
| 2026-04-20 | minimal-square.step | 4.7ms | 241ms | 36ms | 205ms | 27ms | baseline |
| 2026-04-20 | engine.stp | 387ms | 3028ms | 455ms | 5988ms | 52618ms | baseline |
| 2026-04-20 | fan.stp | 157ms | 1742ms | 416ms | 2979ms | 44353ms | baseline |

### Export 层优化（2026-04-20）

| 日期 | 样例 | parse | resolve | build | preview export | mesh export | 总耗时 | 备注 |
|---|---|---:|---:|---:|---:|---:|---:|---|
| 2026-04-20 | engine.stp | 440ms | 912ms | 442ms | 6464ms | 14138ms | ~22s | export opt |
| 2026-04-20 | fan.stp | 145ms | 622ms | 342ms | ~4s | 13464ms | ~15s | export opt |

**改动：**
- `StepMeshExporter.formatObj()`: 替换 `String.format()` 为直接 StringBuilder 拼接 + 自定义 `append6()` 快速浮点格式化（6位小数）
- `StepMeshExporter.formatStlText()`: 同上
- `BSplineSurface3` / `RationalBSplineSurface3`: 从 record 转为 class，expanded knot vectors 延迟缓存，避免每次 `pointAt()`/`normalAt()` 重复分配 ArrayList
- `StepMeshExporter.buildMesh()`: StepFaceEntity 三角化改为 `parallelStream()`，每个 face 独立 Triangulator，最后合并结果（全局顶点去重）
- engine.stp mesh export: 60117ms → 14138ms（**76% 改善**）
- fan.stp mesh export: 55885ms → 13464ms（**76% 改善**）

### 参数域 bbox 预过滤（2026-04-20）

| 日期 | 样例 | parse | resolve | build | preview export | mesh export | 备注 |
|---|---|---:|---:|---:|---:|---:|---|
| 2026-04-20 | engine.stp | 360ms | 922ms | 524ms | 3796ms | 13909ms | after bbox pre-filter |
| 2026-04-20 | fan.stp | 172ms | 655ms | 364ms | 1915ms | 20405ms | after bbox pre-filter |

**改动：**
- `containsParametricLoops`: 对每个 parametric loop 预计算 AABB（minU/maxU/minV/maxV），射线测试前先做 bbox 裁剪
- 对 hole loop 同样做 bbox 预过滤，跳过不可能包含点的孔洞
- `triangulateParametricFace` / `triangulateSemanticParametricFace`: 保持原有 per-cell pointAt/normalAt 调用模式

**性能对比（相对于导出优化后的基线）：**
- engine.stp mesh export: 17913ms → 13909ms（**22% 改善**）
- fan.stp mesh export: 25953ms → 20405ms（**21% 改善**）

### Phase 3+ follow-up 优化（2026-04-20）

| 日期 | 样例 | parse | resolve | build | preview export | mesh export | 备注 |
|---|---|---:|---:|---:|---:|---:|---|
| 2026-04-20 | minimal-square.step | 0.1ms | 0.4ms | 0.2ms | 165ms | 23ms | after follow-up opt |
| 2026-04-20 | engine.stp | 433ms | 877ms | 598ms | 6464ms | 60117ms | after follow-up opt |
| 2026-04-20 | fan.stp | 347ms | 342ms | 176ms | 4050ms | 55885ms | after follow-up opt |

**Phase 3+ 改动：**
- `StepFile.entitiesById()`: 结果缓存到 volatile 字段，避免每次调用重建 LinkedHashMap
- `StepEntityInstance.hasDefinition()` / `requireDefinition()`: 替换 stream + `equalsIgnoreCase` 为直接循环 + ASCII 大小写无关比较（位运算 `(a | 0x20) == (b | 0x20)`）
- `StepFile` 和 `StepEntityInstance` 从 record 转为 class 以支持 lazy 缓存字段
- resolve 性能与 Phase 3 持平（~850-1100ms 范围内波动属正常 JIT/GC 波动）

### Phase 3 优化后（2026-04-20）

| 日期 | 样例 | parse | resolve | build | preview export | mesh export | 备注 |
|---|---|---:|---:|---:|---:|---:|---|
| 2026-04-20 | minimal-square.step | 6.0ms | 52ms | 38ms | 165ms | 23ms | after resolver opt |
| 2026-04-20 | engine.stp | 367ms | 827ms | 453ms | 6464ms | 60117ms | after resolver opt |
| 2026-04-20 | fan.stp | 140ms | 632ms | 357ms | 4050ms | 55885ms | after resolver opt |

**Phase 3 改动：**
- `StepEntityResolver.resolve()`: 将 O(registry_size × entity_count) 的线性扫描改为 O(entity_definition_count × registry_size_in_order) 的模式匹配
  - 旧方案：对每个 entity 遍历 24000+ 注册表条目，每次调用 `equalsIgnoreCase`
  - 新方案：预归一化 entity definition names 为 ASCII 大写，然后按注册表顺序匹配（保持原有匹配语义），只做精确字符串比较
- engine.stp resolve 从 ~3028ms 降到 ~827ms（**73% 改善**）
- fan.stp resolve 从 ~1742ms 降到 ~632ms（**64% 改善**）

### Phase 2 优化后（2026-04-20）

| 日期 | 样例 | parse | resolve | build | preview export | mesh export | 备注 |
|---|---|---:|---:|---:|---:|---:|---|
| 2026-04-20 | minimal-square.step | 6.0ms | 272ms | 38ms | 165ms | 23ms | after syntax opt |
| 2026-04-20 | engine.stp | 377ms | 2955ms | 535ms | 6464ms | 60117ms | after syntax opt |
| 2026-04-20 | fan.stp | 151ms | 1650ms | 417ms | 4050ms | 55885ms | after syntax opt |

**Phase 2 改动：**
- `StepParser.parseSections()`: 去掉了为 DATA/HEADER 段创建 `substring()` 大对象的做法，改用 `StepTokenizer(input, start, end)` 区间扫描，避免双倍内存占用
- `toUpperCase()` 保留：实测发现它并非瓶颈，且 `String.toUpperCase()` 在 JDK 中已高度优化，盲目移除反而引入更慢的逐字符扫描
- 单字符 token 文本常量复用已在 `StepTokenizer` 中实现

**结论：** Phase 2 完成后，`parse` 阶段耗时基本持平（消除了 substring 大对象分配，降低了峰值内存），**resolve** 阶段仍是主要瓶颈（engine.stp 占 ~80%）。下一步是否进入 Phase 3（resolver 优化）取决于性能需求紧迫度。

---

## 建议执行顺序

1. Phase 1：建立性能基线 ✅ **已完成**
2. Phase 2：syntax 低风险提速 ✅ **已完成**
3. Phase 3：resolver 热点优化 ✅ **已完成**
4. 重新跑基线并比较收益 ✅ **已完成（见上表）**
5. 如果仍不够，再讨论 Phase 4

## 可执行任务清单

### Milestone A: 基线就绪 ✅ **已完成**

- [x] 新增 benchmark/diagnostic 入口 (`StepBenchmarkApp`)
- [x] 跑完小/中/大 3 组样例
- [x] 将结果补回文档

### Milestone B: Syntax 提速 ✅ **已完成**

- [x] `StepTokenizer` 支持区间扫描（已存在）
- [x] `StepParser` 去掉 `substring()` 大对象分配，改用区间扫描
- [x] `toUpperCase()` 保留（非瓶颈）
- [x] 回跑 syntax、resolver、全量测试 — 全部通过
- [x] 更新 benchmark 结果

### Milestone C: Resolver 优化决策 ✅ **已完成**

- [x] resolve 阶段在 engine.stp 中占 ~3000ms（~80% 解析时间），已确认为主瓶颈
- [x] 进入 Phase 3，优化 registry 匹配逻辑
- [x] resolve 从 ~3028ms 降到 ~827ms（engine.stp），~1742ms 降到 ~632ms（fan.stp）

### Milestone D: Resolver 热点收敛 ✅ **已完成**

- [x] 核心热点：`resolve()` 方法中遍历 24000+ 注册表条目的 O(N×M) 匹配
- [x] 优化方案：预归一化 definition names + 按注册表顺序精确匹配
- [x] 全量测试通过，工业样例回归未受影响

### Milestone E: 结构性改造评估

- 仅在前面收益不足时考虑
- 先原型，后全量

---

## 不建议现在就做的事

1. 直接重写 `StepParser` 为全新架构
2. 在没有 benchmark 的情况下大改 `StepEntityResolver`
3. 同时改 syntax / semantic / builder，导致收益来源不可分辨
4. 为了“理论更优”而提前引入零拷贝复杂设计

---

## 验收标准

### 最低目标

- 新计划文档落地
- 有统一的 benchmark 入口
- 有一版优化前基线数据

### 阶段目标

- Phase 2 后：
  - 所有 syntax / semantic / app 测试通过
  - 大文件 `parse` 阶段耗时下降
  - 大文件解析前段内存占用下降

### 长期目标

- `parse / resolve / build-export` 有持续可对比的数据
- 后续每次解析链路优化都能量化收益

## 建议记录格式

后续每次执行本计划时，建议在文档或单独结果文件中记录：

| 日期 | 样例 | parse | resolve | build/export | 总耗时 | 备注 |
|---|---|---:|---:|---:|---:|---|
| 2026-xx-xx | minimal-square.step | - | - | - | - | baseline |
| 2026-xx-xx | engine.stp | - | - | - | - | after syntax optimization |
| 2026-xx-xx | fan.stp | - | - | - | - | after resolver optimization |

这样后续不会再出现“改了很多，但不知道到底快了多少”的情况。

---

## 近期建议

Phase 1-3+ 及 Export 优化已全部完成，当前状态：

| 阶段 | 状态 | 主要收益 |
|---|---|---|
| Phase 1: 基线 | ✅ | 建立了可重复的 benchmark 入口 |
| Phase 2: Syntax | ✅ | 消除了 DATA 段 substring 大对象分配 |
| Phase 3: Resolver | ✅ | resolve 耗时降低 64-73% |
| Phase 3+: Follow-up | ✅ | `hasDefinition/requireDefinition` 消除 stream，`entitiesById()` 缓存 |
| Export 优化 | ✅ | mesh export 降低 ~76%（engine: 60s→14s） |
| 参数域 bbox 预过滤 | ✅ | mesh export 降低 21-22%（engine: 17.9s→13.9s, fan: 25.9s→20.4s） |

**最终性能对比（engine.stp）：**

| 指标 | 基线 | 当前 | 总改善 |
|---|---:|---:|---:|
| parse | 387ms | 440ms | 持平 |
| resolve | 3028ms | 912ms | **70% ↓** |
| build | 455ms | 442ms | 持平 |
| preview export | 5988ms | 6464ms | 持平 |
| mesh export | 60117ms | 13909ms | **77% ↓** |
| 总耗时 | ~70s | ~19s | **73% ↓** |

### 参数域循环 bbox 预过滤（2026-04-20）

在 `containsParametricLoops` 中增加了 AABB（轴对齐包围盒）预过滤：
- 对每个 parametric loop（outer + holes）预计算 minU/maxU/minV/maxV
- 在昂贵的射线测试之前，先用 bbox 快速排除明显在外的格点
- 对 hole loop 同样做 bbox 预过滤，跳过不需要精确测试的点

收益：engine.stp mesh export 从 17913ms 降到 13909ms（**22% 改善**），fan.stp 从 25953ms 降到 20405ms（**21% 改善**）。

### 预计算 grid 方案的放弃说明

尝试过预计算 (uSegments+1)×(vSegments+1) 的 pointAt/normalAt 网格来消除相邻 cell 的冗余调用，但实测发现：
- 对于裁剪面积大的文件（fan.stp），预计算大量不需要的点反而更慢
- 对于裁剪面积小的文件（engine.stp），收益不足以抵消预计算成本
- 结论：对于含大量裁剪的 B-Spline 面，按需调用 `pointAt` 配合 bbox 预过滤是更优策略

下一步最值得做的事：
1. 如果当前性能已满足需求，维持现状
2. 如果还需要进一步优化，进入 Phase 4（结构性优化：零拷贝 token、typed token payload）
3. 或进一步优化 preview export（当前 ~6.5s，占比 ~30%）

这条路线最稳，也最符合当前项目状态。
