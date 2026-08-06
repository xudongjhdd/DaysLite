# DaysLite 1.0.0 App Store 上架检查清单

更新时间：2026-08-06

## A. 账号与法律（用户本人）

- [x] 已用个人身份申请 Apple Developer Program 并支付 99 美元。
- [x] 会员状态已从“待处理”变为有效（用户已确认）。
- [x] 已确认签名团队为个人团队，证书持有人是本人。
- [x] 已记录 Team ID `XAQW5BWUB6`；会员到期日仍需核对（不要记录密码或验证码）。
- [ ] 已在 App Store Connect 接受最新协议。
- [ ] 已确认商店显示的个人开发者名称符合预期。

## B. App 与代码

- [x] 原生 SwiftUI 工程可构建，最低 iOS 17。
- [x] Bundle ID 配置为 `com.dayslite.countdown.ios`。
- [x] 版本 `1.0.0`、构建号 `1`。
- [x] 新建、编辑、删除、每年重复、颜色和备注已实现。
- [x] English / 简体中文切换和持久化已实现。
- [x] 无账号、广告、分析、跟踪、后端和第三方 SDK。
- [x] 本地存储、隐私政策、深色模式和辅助功能已验证。
- [x] 单元测试 26 项通过。
- [x] 1024×1024、无 alpha 的 App 图标已生成。
- [x] `PrivacyInfo.xcprivacy` 已声明 UserDefaults 必需原因 `CA92.1`。
- [x] 已用个人 Team 完成自动签名和真机 Debug 构建。
- [x] 真机安装并完成核心流程验收。

## C. App Store Connect 记录

- [x] 显式 App ID / Bundle ID `com.dayslite.countdown.ios` 已注册。
- [x] 已创建 `DaysLite: Simple Countdown` App 记录，平台为 iOS，Apple App ID 为 `6798545461`。
- [x] SKU 已设为唯一内部值 `DAYSLITE-IOS-001`。
- [ ] Primary Language、分类和版本信息已填写。
- [ ] Support URL 可匿名访问。
- [ ] Privacy Policy URL 可匿名访问。
- [ ] 年龄分级问卷已按实际功能完成。
- [ ] App Privacy 已按当前版本选择“不收集数据”。
- [ ] 内容版权、出口合规和其他必填问卷已由账号持有人确认。

## D. 商店素材

- [x] 英文与简体中文商店文案草稿已完成。
- [x] 审核备注草稿已完成。
- [x] 开发阶段截图已保存用于功能证据。
- [ ] 已按 Apple 提交时的官方尺寸生成英文最终截图。
- [ ] 已按 Apple 提交时的官方尺寸生成简体中文最终截图。
- [ ] 所有截图无个人信息、键盘、调试文字、裁切或错误状态。
- [ ] 商店名称、描述和截图只展示当前构建已有功能。

## E. 构建与 TestFlight

- [x] Release 配置在通用 iOS 设备目标构建成功。
- [x] Archive 的 Bundle ID、版本、构建号和 Team 正确。
- [ ] Organizer Validate App 通过。
- [x] 构建 1 已上传并完成 Apple 处理。
- [ ] TestFlight 出口合规信息已完成。
- [ ] 内部 TestFlight 真机验收通过。
- [ ] 如产生新构建，构建号已递增且商店选择的是最终构建。

## F. 审核与发布

- [ ] 版本页已选择最终构建。
- [ ] App Review 联系方式和审核备注已填写。
- [ ] 已完成 Add for Review。
- [ ] 账号持有人已最终核对并 Submit for Review。
- [ ] 已记录审核状态变化及任何沟通。
- [ ] 审核通过后已手动发布。
- [ ] 已从公开 App Store 安装并完成发布后验收。
- [ ] 已记录 App Store 链接、发布日期和完整流程复盘。
