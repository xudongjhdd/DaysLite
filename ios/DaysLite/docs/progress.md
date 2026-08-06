# DaysLite iOS 开发与上架进度

更新时间：2026-08-06

## 状态说明

- `未开始`：尚未进入该阶段。
- `进行中`：正在执行且没有外部阻塞。
- `阻塞`：需要等待用户、Apple 或其他外部条件。
- `已完成`：已提供并核对完成证据。

## 当前摘要

- 当前阶段：App 图标、隐私清单与商店材料
- 总体状态：进行中
- 当前阻塞：Apple Developer Program 付款正在处理，Apple 页面提示最长可能需要 48 小时
- 下一步：生成正式 App 图标、隐私清单和 App Store 上架文档

## 里程碑

| 阶段 | 状态 | 完成时间 | 负责人 | 验证证据 | 阻塞项 / 下一步 |
| --- | --- | --- | --- | --- | --- |
| 产品目标与范围确认 | 已完成 | 2026-08-06 | 用户 / Codex | 对话中确认 SwiftUI、iOS 17、本地存储、个人发布和学习目标 | 无 |
| 技术路线确认 | 已完成 | 2026-08-06 | 用户 / Codex | 选择独立原生 SwiftUI 工程 | 无 |
| 设计规格 | 已完成 | 2026-08-06 | 用户 / Codex | `docs/superpowers/specs/2026-08-06-dayslite-ios-design.md`；用户回复“规格通过” | 无 |
| Apple Developer Program 激活 | 阻塞 |  | 用户 / Apple | 已支付 99 美元；账户显示“待处理” | 等待最长 48 小时后检查 Active、Team ID 和确认邮件 |
| 实施计划 | 已完成 | 2026-08-06 | Codex | `docs/superpowers/plans/2026-08-06-dayslite-ios.md`；已完成规格覆盖、占位符和类型一致性自审 | 当前任务内按计划执行 |
| iOS 工程创建 | 已完成 | 2026-08-06 | Codex | Xcode 26.5 可列出 `DaysLite` scheme；iPhone 17 Pro / iOS 26.5 冒烟测试 1 项通过 | 无 |
| 核心功能实现 | 已完成 | 2026-08-06 | Codex | 完整测试 26 项通过；首页、编辑、设置、双语隐私和辅助功能均经模拟器核对 | 进入商店资源和发布验证 |
| 自动化测试与模拟器验收 | 未开始 |  | Codex |  | 等待功能实现 |
| 真机签名与测试 | 未开始 |  | 用户 / Codex |  | 需要会员激活及真机连接 |
| Release Archive | 未开始 |  | Codex |  | 需要签名配置 |
| TestFlight | 未开始 |  | 用户 / Codex |  | 需要会员激活和 App Store Connect 记录 |
| 商店资料与隐私申报 | 未开始 |  | 用户 / Codex |  | 等待构建和截图 |
| App Review | 未开始 |  | 用户 / Apple |  | 等待提交材料完整 |
| App Store 发布 | 未开始 |  | 用户 / Apple |  | 等待审核通过 |
| 发布流程复盘 | 未开始 |  | 用户 / Codex |  | 发布后更新手册 |

## 决策记录

| 日期 | 决策 | 原因 |
| --- | --- | --- |
| 2026-08-05 | 最低支持 iOS 17 | 兼顾现代 SwiftUI 能力和设备覆盖 |
| 2026-08-05 | 使用 iOS 原生交互而非复制 Android 控件 | 符合 iPhone 使用习惯和审核预期 |
| 2026-08-05 | Android 与 iOS 数据相互独立 | 保持首版轻量、离线和无账号定位 |
| 2026-08-05 | 采用独立 SwiftUI 工程 | 无第三方依赖，便于学习和维护 |
| 2026-08-06 | 以个人开发者身份发布 | 用户选择个人 Apple Developer Program |
| 2026-08-06 | 同时交付可复用的发布手册 | 本项目的主要目的包括学习完整上架流程 |

## 外部事项

### Apple Developer Program

- 已完成个人会员 99 美元付款。
- Apple Developer 账户当前显示 `Snow xu（待处理）`。
- Apple 页面提示购买处理最多可能需要 48 小时。
- 激活后需要记录会员状态、角色、续费日期和 Team ID 是否可见；不要在本文件记录账号密码、付款信息或证件号码。

## 工作记录

### 2026-08-05

- 盘点现有 Android DaysLite 的功能、数据模型、隐私政策和 Google Play 材料。
- 确认采用原生 SwiftUI、最低 iOS 17 和 iOS 原生交互。
- 确认不进行 Android 数据同步或迁移。
- 用户退出无法控制的旧组织开发者团队。
- 用户选择以个人身份注册 Apple Developer Program。

### 2026-08-06

- 用户完成 99 美元会员付款，Apple 正在处理注册。
- 明确项目的学习目标：跑通真实 App Store 上架流程并沉淀可复用经验。
- 用户确认架构、功能、数据、测试、上架和学习记录设计。
- 写入完整设计规格和初始进度记录。
- 用户通过书面设计规格，并确认 iOS 代码继续放在当前 GitHub 仓库的 `ios/` 目录。
- 编写测试驱动的详细实施与上架计划。
- 用户选择在当前任务中执行实施计划。
- Android 基线 `./gradlew test` 通过。
- 创建原生 SwiftUI Xcode 工程；在 iPhone 17 Pro / iOS 26.5 模拟器完成 RED/GREEN 冒烟测试。
- 以 TDD 实现 `LocalDate`、`CountdownEvent` 和 `CountdownCalculator`；8 项日期与年度重复测试通过。
- 以 TDD 实现本地 JSON 存储、逐条坏数据恢复、旧字段兼容、语言保存和 `AppModel` 即时持久化；7 项针对性测试通过。
- 建立完整的集中式中英文 `AppText` 文案与确定性日期/天数格式；完整 iOS 测试 19 项通过。
- 完成 SwiftUI 首页、空状态、排序后的倒计时卡片、年度标记、过期弱化、设置与新增导航入口；完整测试 21 项通过。
- 模拟器视觉证据：`StoreAssets/screenshots/development/home-empty.png`、`StoreAssets/screenshots/development/home-populated.png`（均为 1206×2622）。
- 完成新增/编辑表单、日期选择、六种事件颜色、备注、年度重复、空标题校验和删除确认；完整测试 25 项通过。
- 模拟器手工验收新增保存、修改后取消、删除取消、删除确认、键盘收起和重启持久化，结果符合预期。
- 完成设置页、English/简体中文 Picker、版本信息和随 App 打包的双语 Markdown 隐私政策；语言切换立即生效并跨重启保存。
- 更新公开隐私政策，分别准确说明 iOS 卸载删除数据和 Android 清除应用存储，不再把 Android 操作描述为 iOS 能力。
- 设置与隐私视觉证据：`StoreAssets/screenshots/development/settings-en.png`、`privacy-en.png`、`settings-zh-Hans.png`、`privacy-zh-Hans.png`。
- 完成 VoiceOver 语义树检查、Extra Extra Large、Accessibility XXXL 和深色模式验收；发现并修复固定浅色隐私页在深色模式下的白字问题。
- 完整 iOS 测试 26 项通过。
