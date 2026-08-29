# 测试覆盖体检（结构化映射）

> 生成：2026-08-27（结构估算）。2026-08-29 校准：Maven 已可用，下表改为 jacoco 实测行覆盖。
> 门禁：`pom.xml` jacoco `check`——手写代码口径（排除 `step.model` 1265 个 codegen 类）
> BUNDLE 行覆盖 ≥ 0.55，绑定在 `test` 阶段，`mvn test` 即强制；报告在 `target/site/jacoco/`。
> 基线：2026-08-29 `mvn test`，2047 个测试全绿，手写代码整体行覆盖 **56.5%**。

## 包级行覆盖（jacoco 实测，2026-08-29）

| 包 | 行覆盖 | 评估 |
|---|---:|---|
| common | 87.9% | OK |
| geometry2d | 81.6% | OK |
| topology | 80.6% | OK |
| geometry | 80.5% | OK |
| preview.statistics | 78.7% | OK |
| app | 78.1% | OK |
| builder | 77.6% | OK |
| helper | 73.5% | OK（靠间接覆盖） |
| export.json | 71.2% | 已有 samples 全量 JSON 导出契约测试兜底 |
| export.mesh | 63.3% | 间接覆盖为主（app 层 OBJ/STL 断言） |
| preview.mapper | 63.1% | 已补 `PreviewUvCoordsTest` |
| preview.payload | 55.0% | 45 个 DTO 仅 `UvBounds` 有专属测试 |
| step.syntax | 55.2% | 含 ANTLR 生成解析器（约 3.5% 分母），口径待排除 |
| export.glb | 52.2% | **零专属测试文件**，靠 app 层 GLB 字节断言间接 |
| step.semantic | 47.6% | **绝对缺口最大**（11,222 missed 行） |
| preview.sampling | 29.8% | 已补 6 个纯函数测试，管线级仍缺 |
| preview.builder | 15.9% | 仅 `mergeGeometry`/`reverseFacePayload` 两个纯函数被测 |
| tool | 0.0% | codegen CLI 工具，零引用零测试 |

## 补测优先级（2026-08-29 更新）

1. **P0 — preview 管线三个核心类**：`PreviewCurveEvaluator`（1,693 行，**0%**）、
   `PreviewFaceBuilder`（1,866 行，约 4%）、`PreviewGeometryCollector`（约 1%）。
   注意 `PreviewCurveEvaluator` 与 `StepPreviewJsonExporter` 存在 verbatim 复制耦合
   （`sampleLooseCurvePoints`），先解耦再补测试才划算。
2. **P1 — export.glb**：4 个类零专属测试。复用 samples 循环，断言 GLB 头/长度/非空即可。
3. **P1 — step.semantic 大类**：`StepCadBuilder`（4,397 行）、`StepEntityResolver`
   （6,455 行）、`StepCadSurfaceBuilder`、`GeometryResolver`、`AnalysisResolver`
   （308 行仅 3 行被覆盖）。用手写 STEP 文本 fixture 定向打分支；真实世界分支依赖
   `src/test/resources/step/realworld/local-only/`（设计上不入库，放入语料后自动生效，
   但 CI 上恒为空——注意假绿风险，语料为空时应 `assumeTrue` 跳过）。
4. **P2 — export.json payload builders 与 preview.payload 其余 44 个 DTO**：
   只测不变量（span、defensive copy、equals 契约），勿照抄逐 getter 的低价值断言。
5. **P3 — tool 包（0%）**：补冒烟测试，或从 jacoco 口径剔除（与 `step.model` 同理）。

## 已完成的补测（本轮，2026-08-29）

- preview.sampling / builder / mapper / payload / statistics 纯函数单测（9 个文件）
- `export.json/StepPreviewJsonExporterTest`：全部 samples 走 JSON 导出契约断言
- `geometry/BSplineCurveFullConsistencyTest`
- `step/SamplesParseSmokeTest`：45 个 STEP 全量 parse→resolve→build 回归
- `step/model/StepModelEntityAccessorTest`：1265 个生成类反射冒烟
- 同轮删除 4 个零调用死类（约 1142 行）与 `StepCurveMetadataHelper` 死类

## 工具链（2026-08-29 更新）

- Maven 已可用（wrapper 分发包 + JDK 11），并已提交 **maven wrapper**（`./mvnw` /
  `mvnw.cmd`，钉 3.9.16）——依赖解析与 classpath 以 Maven 为唯一真相源。
- `tools/build_test_classpath.py` 与 `target/cp.txt` 已退役（原为无 Maven 环境的手拼
  classpath 方案，ghost 路径防御不再需要）。
- `lib/` 的 junit-platform-console-standalone jar 已删除，测试统一走 surefire。
- ANTLR 生成物位于 `target/generated-sources/antlr4/com/minicad/step/syntax/`，
  与手写代码同包计数——建议后续把 `StepAntlr*` 纳入 jacoco 排除口径（约 +3.5%）。
