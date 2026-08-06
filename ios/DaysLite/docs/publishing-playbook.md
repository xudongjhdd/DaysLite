# iOS / App Store 发布操作手册

本手册把 DaysLite 1.0.0 的真实发布流程整理成以后项目可以复用的顺序。Apple 的页面和规则会变化，每次提交都要重新核对文中链接。

标记说明：`[用户]` 表示必须由账号持有人确认的法律、身份或发布操作；`[技术]` 表示可以由开发过程准备或协助验证的操作。

## 1. 激活开发者会员

1. `[用户]` 等待 Apple Developer Program 从“待处理”变成有效会员，并保留 Apple 的确认邮件。
2. `[用户]` 登录 developer.apple.com/account，确认显示的是自己的个人会员，而不是之前退出的组织。
3. `[用户]` 记录 Team ID、Account Holder 姓名、会员到期日；不要把密码、验证码、付款或证件信息提交到 Git。
4. `[用户]` 在 App Store Connect 接受待处理协议。创建 App 记录前，Account Holder 需要接受最新协议。

Team ID 只用于本地签名配置和进度核对，不是 App 的 Bundle ID。目前使用占位状态：`<TEAM_ID — 等待会员激活后填写>`。

## 2. 配置 Xcode 身份与签名

1. `[用户]` Xcode → Settings → Accounts，添加完成付款的 Apple Account。
2. `[技术]` 打开 `DaysLite.xcodeproj`，选择 DaysLite target → Signing & Capabilities。
3. `[用户]` Team 选择自己的个人团队；保持 Automatically manage signing 开启。
4. `[技术]` 核对 Bundle ID 为 `com.dayslite.countdown.ios`，版本 `1.0.0`，构建号 `1`。
5. `[技术]` 连接一台 iPhone，完成一次真机 Debug 构建和核心流程验收。

如果 Bundle ID 已被占用，不要随意在多个页面分别修改。先确定新的唯一 ID，再同步修改 Xcode、开发者标识符和 App Store Connect 记录。

## 3. 创建 App Store Connect 记录

App Store Connect 必须先有 App 记录，上传的构建才能与之关联。参考 Apple 的 [Add a new app](https://developer.apple.com/help/app-store-connect/create-an-app-record/add-a-new-app/)。

1. `[用户]` App Store Connect → Apps → `+` → New App。
2. `[用户]` Platform 选择 iOS；Name 填 `DaysLite`；Primary Language 建议 English (U.S.)。
3. `[用户]` Bundle ID 选择 `com.dayslite.countdown.ios` 对应的显式标识符。
4. `[用户]` SKU 使用内部唯一值，例如 `DAYSLITE-IOS-001`；SKU 不展示给用户，创建后谨慎对待。
5. `[用户]` User Access 选择 Full Access（个人账号通常只有本人）。

## 4. 填写版本资料

1. `[技术]` 从 `StoreAssets/app-store-listing.md`复制英文和简体中文名称、副标题、描述、关键词和更新说明。
2. `[用户]` 确认 Support URL、Privacy Policy URL 能从公网匿名打开。
3. `[用户]` 完成年龄分级、内容版权、出口合规等问卷，必须按实际构建回答。
4. `[用户]` App Privacy 选择“不收集数据”，前提是提交版本仍然没有账号、广告、分析、跟踪、后端或第三方收集 SDK。
5. `[技术]` 按 `StoreAssets/screenshots/README.md` 生成并逐张核对最终截图。

## 5. 创建 Release Archive 并上传

1. `[技术]` 将运行目标选择为 Any iOS Device (arm64)，使用 Release 配置。
2. `[技术]` Product → Archive；在 Organizer 中确认版本、构建号、Bundle ID 和签名团队。
3. `[技术]` 先 Validate App，再 Distribute App → App Store Connect → Upload。
4. `[用户]` 在上传对话框中确认签名和合规问题；不要共享 Apple Account 验证码。
5. `[技术]` 上传后等待 Apple 处理。构建不会立即出现，处理完成后才可在版本页或 TestFlight 选择。参考 [Upload builds](https://developer.apple.com/help/app-store-connect/manage-builds/upload-builds/)。

每次上传必须增加构建号，例如 1、2、3；营销版本在 1.0.0 审核周期内可以保持不变。

## 6. TestFlight 验收

1. `[用户]` 在 TestFlight 选择已处理的构建，补齐出口合规信息。
2. `[用户]` 先加入内部测试员；如需外部测试，提交 TestFlight Beta App Review。
3. `[技术]` 在真实 iPhone 上覆盖新建、编辑、删除、每年重复、语言切换、深色模式、重启持久化和卸载数据删除。
4. `[技术]` 将发现的问题修复后增加构建号，重新 Archive、上传和验收。

## 7. 提交 App Review

Apple 当前流程是：在版本页选择构建 → Add for Review → 在草稿提交中检查项目 → Submit for Review。参考 [Submit an app](https://developer.apple.com/help/app-store-connect/manage-submissions-to-app-review/submit-an-app/)。

1. `[用户]` 在 1.0.0 版本页选择最终构建。
2. `[技术]` 粘贴 `StoreAssets/review-notes.md` 的英文审核说明。
3. `[用户]` 核对每个必填项、隐私回答、截图和出口合规回答。
4. `[用户]` Add for Review，然后在草稿提交页 Submit for Review。
5. `[用户]` 关注 App Review 消息和状态；状态含义见 [App and submission statuses](https://developer.apple.com/help/app-store-connect/reference/app-information/app-and-submission-statuses/)。

首个学习项目建议选择“审核通过后手动发布”，便于完整观察 Ready for Distribution 前后的状态变化。

## 8. 审核通过与发布后

1. `[用户]` 审核通过后手动发布，并记录发布时间和商店链接。
2. `[技术]` 从商店真实下载安装，验证启动、新建、编辑、语言和隐私政策链接。
3. `[技术]` 检查崩溃、审核消息和用户反馈，但不要为了统计而临时加入未评估的分析 SDK。
4. `[用户/技术]` 更新 `progress.md`，记录实际 Team 激活、Archive、TestFlight、审核和发布证据。
5. `[技术]` 为下个项目复制本手册，但重新评估 Bundle ID、隐私、年龄分级、截图尺寸、SDK 和 Apple 最新规则。

完整流程概览：[App Store Connect workflow](https://developer.apple.com/help/app-store-connect/get-started/app-store-connect-workflow)。
