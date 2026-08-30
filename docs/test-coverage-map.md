# 测试覆盖体检（结构化映射）

> 生成：2026-08-27（结构估算）。2026-08-29 首次校准为 jacoco 实测；2026-08-30 再校准。
> 门禁：`pom.xml` jacoco `check`——手写代码口径（排除 `step.model` 1265 个 codegen 类、
> `tool` 包 5 个开发期 codegen CLI、ANTLR 生成的 6 个语法类）BUNDLE 行覆盖 ≥ 0.55，
> 绑定在 `test` 阶段，`mvn test` 即强制；报告在 `target/site/jacoco/`。
> 工具链：JDK 17+（本地 26 验证），字节码目标 17。

## 包级行覆盖（jacoco 实测，2026-08-30 口径校准前）

| 包 | 行覆盖 | 评估 |
|---|---:|---|
| common | 86.2% | OK |
| geometry2d | 81.9% | OK（真 De Boor 落地后） |
| geometry | 80.8% | OK（Cox-de Boor 表格式已锁独立参考测试） |
| app | 78.2% | OK |
| preview.statistics | 78.7% | OK |
| builder | 77.6% | OK |
| topology | 78.2% | OK |
| export.json | 71.7% | samples 全量 JSON 契约测试兜底 |
| helper | 72.7% | 靠间接覆盖 |
| export.mesh | 63.9% | 间接覆盖为主 |
| preview.mapper | 63.1% | 已补 `PreviewUvCoordsTest` |
| preview.payload | 55.2% | 45 个 DTO 仅 UvBounds 有专属测试 |
| step.syntax | 58.1% | 含 ANTLR 生成物（已入排除口径） |
| export.glb | 52.2% | 零专属测试文件，靠 app 层间接 |
| step.semantic | 50.0% | **绝对缺口最大**（~10,200 missed 行） |
| preview.sampling | 33.9% | 纯函数多，**最好爬的池** |
| preview.builder | 24.8% | `PreviewPipelineTest` 已锁主入口（0→13%） |
| preview.sampling(Evaluator) | 5% | `PreviewCurveEvaluator` 主入口已测，深处待补 |
| tool | 0% | codegen CLI，已从口径剔除 |

## 补测优先级（2026-08-30 更新）

1. **preview.sampling**（纯函数，参数化测试性价比最高）。
2. **step.semantic 大类分支**：样例驱动（照 `PreviewPipelineTest` 模式）。
3. **export.glb**：samples 循环 + GLB 头/长度断言。
4. preview.payload 其余 44 个 DTO：只测不变量。

## 已完成的里程碑

- 2026-08-29：preview 四包纯函数测试 + SamplesParseSmokeTest + jacoco 0.50→0.55
- 2026-08-30：PreviewPipelineTest（三兄弟 0-4% → 5-15%）、2D B-spline 真 De Boor、
  批 C 死子图删除（StepCadSurfaceBuilder 1847→153）、StepEntityResolver 助手提取（6484→5909+760）
- 工具链：maven wrapper（3.9.16）、JDK 17+ 字节码、Jetty 12.0.15、CodeQL 恢复

## 工具链备忘

- ANTLR 生成物与手写 `StepAntlrBridge` 同包——jacoco 按类名逐个排除（见 pom 注释）。
- `tools/analyze_surface_builder_closure.sh`：方法闭包分析器（批 C 已执行完毕，留档防复发）。
