# 测试覆盖体检（结构化映射）

> 生成时间：2026-08-27。本地无 Maven，无法跑 jacoco 拿到真实行覆盖率数字；
> 下表为**包级结构覆盖**（main `.java` 数量 vs test `.java` 数量），用于定位薄弱模块。
> 真正门禁是 `pom.xml` 的 jacoco `check`（BUNDLE 行覆盖 ≥ 0.70），需 `mvn test` 才能出数。

## 结构覆盖表

| 包 | main.java | test.java | 评估 |
|---|---:|---:|---|
| app | 7 | 17 | OK |
| geometry | 39 | 35 | OK |
| geometry2d | 16 | 17 | OK |
| helper | 9 | 2 | OK |
| step.semantic | 72 | 21 | OK（较薄） |
| step.syntax | 12 | 1 | OK |
| topology | 12 | 5 | 偏薄 |
| step | 0 | 1 | OK |
| common | 9 | 1 | OK（多为异常/常量） |
| test | 0 | 2 | OK |
| **step.model** | **1265** | **0** | 自动生成的 STEP 实体类；靠 `SamplesParseSmokeTest` 间接覆盖 |
| **export.json** | 23 | 0 | **高风险：手写导出逻辑零测试** |
| **export.glb** | 4 | 0 | 高风险 |
| **export.mesh** | 3 | 0 | 高风险 |
| **export** | 0 | 0 | — |
| **preview.payload** | 45 | 0 | **已补 1 个：`UvBoundsTest`** |
| **preview.sampling** | 11 | 0 | 高风险：三角化/采样核心 |
| **preview.builder** | 5 | 0 | 高风险 |
| **preview.mapper** | 5 | 0 | 高风险：UV 映射 |
| **preview.statistics** | 3 | 0 | — |
| **preview** | 0 | 0 | — |
| builder | 3 | 0 | 低风险（内部） |
| tool | 5 | 0 | 低风险（CLI 工具） |

## 怎么读这张表

- `step.model`（1265 个文件）是 STEP 模式**代码生成**出来的实体类，逐个单测不现实；它由
  `SamplesParseSmokeTest`（45 个真实 STEP 全量解析/构建）间接覆盖，风险可接受。
- 真正危险的是 **`export.*` 与 `preview.*`**：这是手写的生产逻辑（GLB/JSON 导出、预览网格、
  UV 映射、三角化），却 **0 测试**。`StepFacePayloadBuilder`（本轮刚补 WARN 的地方）就在
  `export.json` 里——此类生产路径没有任何单测兜底，回归只能靠肉眼。

## 建议的补测优先级

1. **preview.payload（45 个 DTO）** — 价值高、风险低。已从 `UvBounds` 起步（构造器/访问器/
   `uSpan`/`vSpan`/equals-hashCode）。后续同类：`UvPoint`、`PointPayload`、`BoundsPayload`、
   `VectorPayload`、`FacePayload`/`FaceSurfacePayload` 等纯值对象，照此模式批量补。
2. **preview.sampling + preview.builder + preview.mapper** — 预览核心算法（三角化、UV 映射）。
   建议先补纯函数式入口（如采样密度、边界框计算），再补需要构造简单曲面的集成断言。
3. **export.json / export.glb** — 导出管线。最稳妥的做法是复用 `SamplesParseSmokeTest` 的
   构建结果，挑 1–2 个简单 sample 走完整"模型→GLB/JSON"导出并断言产物非空/结构合法。
4. **topology** — 当前 5/12，补 1–2 个拓扑操作（如壳/面遍历、朝向）即可明显抬升该包覆盖。

## 配套工具

- `tools/build_test_classpath.py`：校验并归一化 `target/cp.txt`，**ghost 路径直接报错**
  （本次 13 个失败的根因就是手改 classpath 的 ghost 路径），并强制使用 `toolchain` 版
  servlet-api、排除不兼容的 `jakarta.servlet-api` 6.x。CI 可用 `--check` 模式。
- `src/test/java/com/minicad/step/SamplesParseSmokeTest.java`：全量样本回归守卫。

## 长期建议

本机目前没有 Maven，测试靠手拼 classpath + junit standalone。要真正满足 jacoco 70% 门禁并
获得真实覆盖率数字，最稳妥是**安装 Maven 并跑 `mvn test`**，让依赖解析与 classpath 成为
唯一真相源（届时 `target/cp.txt` 可废弃）。安装需访问 Maven Central（联网），请评估环境网络。
