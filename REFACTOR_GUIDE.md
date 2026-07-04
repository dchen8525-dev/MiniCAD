# StepPreviewJsonExporter.java 重构完成指南

## 当前状态
- **原文件行数**: 18094行  
- **备份文件**: `StepPreviewJsonExporter.java.backup` ✓ 已创建
- **已提取文件**: `PreviewCurveEvaluator.java` (1734行) ✓ 已包含所需方法

## 问题诊断
多次自动替换尝试失败，原因：
- 方法定义与调用点混合在一起
- sed替换会影响方法定义行，导致语法错误
- 文件太大（18000+行），精确控制困难

## 建议方案：使用IDE重构工具

### 推荐：IntelliJ IDEA重构步骤

**步骤1：添加导入**
```java
// 在imports部分添加
import com.minicad.app.PreviewCurveEvaluator;
```

**步骤2：使用IDE的"Safe Delete"功能**
1. 将光标放在`curveEvaluator`方法定义上（约6259行）
2. 右键 → Refactor → Safe Delete
3. IDE会查找所有调用点，选择"Delegate to PreviewCurveEvaluator.curveEvaluator"
4. 确认删除

**步骤3：重复Safe Delete**
对以下方法执行相同操作：
- `sampledCurveEvaluator` (约6500行)
- `closestParameter` (约6523行)  
- `radialComponent` (约6645行)
- `fallbackNormal` (约6650行)
- `unwrapPeriodic` (约6659行)
- `CurveEvaluator`接口 (约18028行)

**步骤4：验证**
```bash
mvn compile -DskipTests
mvn test
```

### 方法2：手动查找替换（精确控制）

如果IDE不可用，使用文本编辑器的查找替换：

**查找替换模式（注意顺序）：**

1. **替换类型声明**
   ```
   查找: CurveEvaluator directrix
   替换: PreviewCurveEvaluator.CurveEvaluator directrix
   条件: 不在方法定义行（不以"private static"开头）
   ```

2. **替换方法调用**
   ```
   查找: curveEvaluator(
   替换: PreviewCurveEvaluator.curveEvaluator(
   条件: 不在方法定义行
   ```

3. **类似处理其他方法**：
   - sampledCurveEvaluator → PreviewCurveEvaluator.sampledCurveEvaluator
   - unwrapPeriodic → PreviewCurveEvaluator.unwrapPeriodic
   - closestParameter → PreviewCurveEvaluator.closestParameter
   - radialComponent → PreviewCurveEvaluator.radialComponent
   - fallbackNormal → PreviewCurveEvaluator.fallbackNormal

4. **删除方法定义**（确认所有调用替换后）：
   - 删除curveEvaluator方法（约240行）
   - 删除sampledCurveEvaluator方法（约24行）
   - 删除closestParameter方法（约38行）
   - 删除radialComponent方法（约5行）
   - 删除fallbackNormal方法（约9行）
   - 删除unwrapPeriodic方法（约13行）
   - 删除CurveEvaluator接口（约28行）

## 需要修改的具体位置（部分示例）

| 行号范围 | 原代码 | 修改后 | 类型 |
|---------|--------|--------|------|
| 6162 | `CurveEvaluator directrix = curveEvaluator(...)` | `PreviewCurveEvaluator.CurveEvaluator directrix = PreviewCurveEvaluator.curveEvaluator(...)` | 调用 |
| 6198 | `CurveEvaluator directrix = curveEvaluator(...)` | `PreviewCurveEvaluator.CurveEvaluator directrix = PreviewCurveEvaluator.curveEvaluator(...)` | 调用 |
| 4402-4425 | `unwrapPeriodic(...)` | `PreviewCurveEvaluator.unwrapPeriodic(...)` | 调用 |
| 6259-6498 | `private static CurveEvaluator curveEvaluator(...)` | **删除整个方法** | 定义 |
| 6500-6522 | `private static CurveEvaluator sampledCurveEvaluator(...)` | **删除整个方法** | 定义 |
| 18028-18055 | `private interface CurveEvaluator {...}` | **删除整个接口** | 定义 |

## 预期结果

修改完成后：
- 文件行数：约17737行（删除约357行）
- 减少代码重复
- **注意**：文件仍将超过2000行，需要进一步拆分其他模块

## 下一步建议

完成此次修改后，继续处理其他提取文件：
- PreviewPmiBuilder.java (1673行)
- PreviewEdgeSampler.java (733行)
- PreviewSurfaceSampler.java (279行)

这些文件也有类似的重复问题。

