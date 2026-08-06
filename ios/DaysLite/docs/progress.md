# DaysLite iOS 开发与上架进度

更新时间：2026-08-06

## 状态说明

- `未开始`：尚未进入该阶段。
- `进行中`：正在执行且没有外部阻塞。
- `阻塞`：需要等待用户、Apple 或其他外部条件。
- `已完成`：已提供并核对完成证据。

## 当前摘要

- 当前阶段：商店资料与隐私申报
- 总体状态：进行中（内部 TestFlight 真机验收已完成）
- 当前阻塞：正式提交前需完成商店截图、在线元数据及合规问卷；欧盟分发前仍需如实完成交易商状态申报
- 下一步：填写 App Store 版本资料、隐私与年龄分级，并上传最终商店截图

## 里程碑

| 阶段 | 状态 | 完成时间 | 负责人 | 验证证据 | 阻塞项 / 下一步 |
| --- | --- | --- | --- | --- | --- |
| 产品目标与范围确认 | 已完成 | 2026-08-06 | 用户 / Codex | 对话中确认 SwiftUI、iOS 17、本地存储、个人发布和学习目标 | 无 |
| 技术路线确认 | 已完成 | 2026-08-06 | 用户 / Codex | 选择独立原生 SwiftUI 工程 | 无 |
| 设计规格 | 已完成 | 2026-08-06 | 用户 / Codex | `docs/superpowers/specs/2026-08-06-dayslite-ios-design.md`；用户回复“规格通过” | 无 |
| Apple Developer Program 激活 | 已完成 | 2026-08-06 | 用户 / Apple | 用户确认会员已激活 | 核对个人 Team、Team ID、角色和续费日期 |
| 实施计划 | 已完成 | 2026-08-06 | Codex | `docs/superpowers/plans/2026-08-06-dayslite-ios.md`；已完成规格覆盖、占位符和类型一致性自审 | 当前任务内按计划执行 |
| iOS 工程创建 | 已完成 | 2026-08-06 | Codex | Xcode 26.5 可列出 `DaysLite` scheme；iPhone 17 Pro / iOS 26.5 冒烟测试 1 项通过 | 无 |
| 核心功能实现 | 已完成 | 2026-08-06 | Codex | 完整测试 26 项通过；首页、编辑、设置、双语隐私和辅助功能均经模拟器核对 | 进入商店资源和发布验证 |
| 自动化测试与模拟器验收 | 已完成 | 2026-08-06 | Codex | 功能测试 26 项、Release 模拟器构建和多轮模拟器手工验收已通过 | 无 |
| 真机签名与测试 | 已完成 | 2026-08-06 | 用户 / Codex | Team `XAQW5BWUB6` 自动签名；DaysLite 已在 iPhone 15 Pro 完成安装、启动和用户核心流程验收 | 无 |
| App ID 与 App Store Connect 记录 | 已完成 | 2026-08-06 | 用户 / Codex | 显式 Bundle ID `com.dayslite.countdown.ios`；商店名 `DaysLite: Simple Countdown`；Apple App ID `6798545461`；iOS 1.0 状态为“准备提交” | 无 |
| Release Archive | 已完成 | 2026-08-06 | Codex | `xcodebuild archive` 成功；App Store Connect 本地导出成功；版本 1.0.0 (1)、arm64、Team `XAQW5BWUB6`、Cloud Managed Apple Distribution、`get-task-allow=false` | 上传前取得用户确认 |
| TestFlight | 已完成 | 2026-08-06 | 用户 / Codex | `DaysLite Internal` 群组包含构建 1；用户确认已通过 iPhone TestFlight 成功安装并完成真机验收 | 无 |
| 商店资料与隐私申报 | 进行中 |  | 用户 / Codex | App 图标、隐私清单、双语文案、审核备注、发布手册和检查清单已完成 | 最终商店截图及 App Store Connect 在线申报需会员激活 |
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
- 用户于 2026-08-06 确认 Apple Developer Program 会员已经激活。
- 下一步需要在 Developer 账户和 Xcode 中核对个人 Team、Team ID、Account Holder 角色和续费日期；不要在本文件记录账号密码、付款信息或证件号码。

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
- 生成确定性的 1024×1024 无透明通道 App 图标，并将生成脚本纳入版本管理。
- 添加 Apple 隐私清单：不跟踪、不收集数据，仅以 `CA92.1` 说明 App 自身使用 UserDefaults。
- 完成英文/简体中文 App Store 文案、审核备注和最终截图采集矩阵；开发截图明确不作为最终商店图使用。
- 完成可复用的 App Store 发布操作手册和逐项检查清单，区分用户本人操作、技术操作和当前会员激活阻塞。
- 校验 App 图标为 1024×1024 且无 alpha；`PrivacyInfo.xcprivacy` 通过 `plutil` 语法检查。
- 完整 iOS 单元测试再次运行：26 项全部通过，0 失败。
- Release 模拟器构建成功，并核对产物包含 App 图标、双语隐私政策与隐私清单。
- 当前执行环境访问 GitHub 时出现 SSL 网络错误，因此 Support URL 与公开 Privacy Policy URL 仍保留在提交前人工在线核对清单中。
- 用户确认 Apple Developer Program 已激活，账号阶段从等待处理转入个人 Team、签名和 App Store Connect 配置阶段。
- 在 Xcode 创建新的 `Apple Development: Dong Xu` 证书，并确认本机钥匙串持有对应私钥；个人 Team ID 为 `XAQW5BWUB6`。
- 将 DaysLite Debug/Release 配置接入个人 Team 自动签名；自动登记 iPhone 并生成开发描述文件。
- DaysLite 真机 Debug 构建签名成功并安装到 iPhone 15 Pro；安装后设备连接变为 unavailable，首次启动和人工验收待重新连接后继续。
- iPhone 恢复连接后，DaysLite 通过 `devicectl` 启动成功，并确认应用进程正在真机运行；签名、设备注册、安装和首次启动链路已跑通。
- 用户确认在 iPhone 15 Pro 上完成新增、编辑、删除、语言切换和重启持久化等核心流程验收，结果正常。
- 在 Apple Developer 注册显式 App ID `com.dayslite.countdown.ios`。
- App Store Connect 中原名称 `DaysLite` 已被占用；经用户确认采用商店名 `DaysLite: Simple Countdown`，代码和设备桌面显示名保持 `DaysLite`。
- 创建 App Store Connect iOS App 记录成功：SKU `DAYSLITE-IOS-001`，Apple App ID `6798545461`，版本 1.0 状态为“准备提交”。
- App Store Connect 提示欧盟分发前必须提供交易商状态；该法律身份申报保留给账号持有人如实完成。
- 生成 Release Archive 成功：Bundle ID `com.dayslite.countdown.ios`、版本 `1.0.0`、构建号 `1`、Team `XAQW5BWUB6`、arm64、最低 iOS 17。
- 本地 App Store Connect 导出成功；Apple 自动使用 Cloud Managed Apple Distribution 证书与 Store Provisioning Profile 重新签名，导出包 `get-task-allow=false`、`beta-reports-active=true`。
- 导出的 IPA 包含 App 图标、`PrivacyInfo.xcprivacy`、英文/简体中文隐私政策和符号文件，已具备上传 TestFlight 条件。
- 经用户确认，将版本 `1.0.0` 构建 `1` 上传 App Store Connect；Xcode 返回 `Upload succeeded`。
- Apple 处理完成后，构建 1 已出现在 TestFlight，当前状态为“缺少出口合规证明”。
- 对 Swift 源码和归档二进制进行加密能力核对：未发现 CryptoKit、CommonCrypto、SecKey、网络请求、WebView、自定义加密或第三方框架；建议在问卷中选择“不属于上述任意一种算法”。
- 出口合规声明已生效；TestFlight 构建 1 状态变为“准备提交”，有效期 90 天，可进入内部测试配置。
- 创建内部测试群组 `DaysLite Internal`，关闭不可修改的自动分发选项，以保留后续构建的手动控制。
- 将版本 `1.0.0` 构建 `1` 加入群组，状态为“准备测试”，并邀请账号持有人 `xudongboy@gmail.com`；App Store Connect 显示 1 个测试员、1 个构建版本、状态“已邀请”。
- 用户确认已通过 iPhone TestFlight 成功安装 DaysLite，并要求将“内部 TestFlight 真机验收”标记完成；TestFlight 分发与验收里程碑完成。
