struct AppText: Sendable {
    let language: AppLanguage

    var appName: String { "DaysLite" }
    var settings: String { localized("设置", "Settings") }
    var addCountdown: String { localized("添加倒计时", "Add countdown") }
    var addCountdownAction: String { localized("+ 添加倒计时", "+ Add countdown") }
    var noCountdownsYet: String { localized("还没有倒计时", "No countdowns yet") }
    var emptyHelper: String { localized("添加一个值得期待的日子。", "Add a date worth looking forward to.") }
    var tagline: String { localized("记录重要日子的轻量倒计时", "A simple countdown app for important days") }

    var addCountdownTitle: String { addCountdown }
    var editCountdownTitle: String { localized("编辑倒计时", "Edit countdown") }
    var eventName: String { localized("事件名称", "Event name") }
    var eventNameHint: String { localized("旅行、生日、考试…", "Trip, birthday, exam…") }
    var targetDate: String { localized("目标日期", "Target date") }
    var color: String { localized("颜色", "Color") }
    var note: String { localized("备注", "Note") }
    var repeatYearly: String { localized("每年重复", "Repeat yearly") }
    var repeatYearlyDetail: String {
        localized("生日、纪念日等每年自动计算下一次日期", "Automatically uses the next yearly occurrence")
    }
    var yearlyBadge: String { localized("每年", "Yearly") }
    var optional: String { localized("可选", "Optional") }
    var save: String { localized("保存", "Save") }
    var delete: String { localized("删除", "Delete") }
    var back: String { localized("返回", "Back") }
    var cancel: String { localized("取消", "Cancel") }

    var settingsTitle: String { settings }
    var privacyPolicy: String { localized("隐私政策", "Privacy Policy") }
    var privacyDetail: String { localized("DaysLite 如何处理数据", "How DaysLite handles data") }
    var localStorage: String { localized("本地存储", "Local storage") }
    var localStorageDetail: String {
        localized("倒计时只保存在此设备上", "Countdowns are saved on this device only")
    }
    var languageLabel: String { localized("语言", "Language") }
    var languageDetail: String {
        localized("当前：中文，点击切换到 English", "Current: English, tap to switch to 中文")
    }
    var version: String { localized("版本", "Version") }
    var privacyLoadError: String {
        localized(
            "无法加载隐私政策，请检查网络连接后重试。",
            "Couldn't load the privacy policy. Please check your connection and try again."
        )
    }

    var titleRequired: String { localized("请输入名称", "Name required") }
    var deleteCountdownQuestion: String { localized("删除倒计时？", "Delete countdown?") }
    var deleteCountdownMessage: String {
        localized("这会从此设备移除该倒计时。", "This removes the countdown from this device.")
    }
    var saveFailure: String {
        localized("无法保存更改，请重试。", "Couldn't save your changes. Please try again.")
    }
    var dismiss: String { localized("好", "OK") }

    func daysLabel(for status: CountdownStatus) -> String {
        switch status {
        case .today:
            return localized("今天", "Today")
        case .future(let days):
            if language == .chinese {
                return "还剩 \(days) 天"
            }
            return "\(days) \(days == 1 ? "day" : "days") left"
        case .past(let days):
            if language == .chinese {
                return "已过 \(days) 天"
            }
            return "\(days) \(days == 1 ? "day" : "days") ago"
        }
    }

    func dateLabel(_ date: LocalDate) -> String {
        if language == .chinese {
            return "\(date.year)年\(date.month)月\(date.day)日"
        }

        let months = [
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ]
        let month = (1...12).contains(date.month) ? months[date.month - 1] : ""
        return "\(month) \(date.day), \(date.year)"
    }

    func summary(upcoming: Int) -> String {
        if language == .chinese {
            return "\(upcoming) 个即将到来的日子"
        }
        return "\(upcoming) upcoming \(upcoming == 1 ? "day" : "days")"
    }

    private func localized(_ chinese: String, _ english: String) -> String {
        language == .chinese ? chinese : english
    }
}
