package com.dayslite.countdown;

class AppText {
    private final AppLanguage language;

    AppText(AppLanguage language) {
        this.language = language;
    }

    String settings() {
        return language == AppLanguage.CHINESE ? "设置" : "Settings";
    }

    String addCountdown() {
        return language == AppLanguage.CHINESE ? "添加倒计时" : "Add countdown";
    }

    String addCountdownAction() {
        return language == AppLanguage.CHINESE ? "+ 添加倒计时" : "+ Add countdown";
    }

    String noCountdownsYet() {
        return language == AppLanguage.CHINESE ? "还没有倒计时" : "No countdowns yet";
    }

    String emptyHelper() {
        return language == AppLanguage.CHINESE ? "添加一个值得期待的日子。" : "Add a date worth looking forward to.";
    }

    String addCountdownTitle() {
        return language == AppLanguage.CHINESE ? "添加倒计时" : "Add countdown";
    }

    String editCountdownTitle() {
        return language == AppLanguage.CHINESE ? "编辑倒计时" : "Edit countdown";
    }

    String eventName() {
        return language == AppLanguage.CHINESE ? "事件名称" : "Event name";
    }

    String eventNameHint() {
        return language == AppLanguage.CHINESE ? "旅行、生日、考试..." : "Trip, birthday, exam...";
    }

    String targetDate() {
        return language == AppLanguage.CHINESE ? "目标日期" : "Target date";
    }

    String color() {
        return language == AppLanguage.CHINESE ? "颜色" : "Color";
    }

    String note() {
        return language == AppLanguage.CHINESE ? "备注" : "Note";
    }

    String repeatYearly() {
        return language == AppLanguage.CHINESE ? "每年重复" : "Repeat yearly";
    }

    String repeatYearlyDetail() {
        return language == AppLanguage.CHINESE ? "生日、纪念日等每年自动计算下一次日期" : "Automatically uses the next yearly occurrence";
    }

    String yearlyBadge() {
        return language == AppLanguage.CHINESE ? "每年" : "Yearly";
    }

    String optional() {
        return language == AppLanguage.CHINESE ? "可选" : "Optional";
    }

    String save() {
        return language == AppLanguage.CHINESE ? "保存" : "Save";
    }

    String delete() {
        return language == AppLanguage.CHINESE ? "删除" : "Delete";
    }

    String back() {
        return language == AppLanguage.CHINESE ? "返回" : "Back";
    }

    String settingsTitle() {
        return settings();
    }

    String privacyPolicy() {
        return language == AppLanguage.CHINESE ? "隐私政策" : "Privacy Policy";
    }

    String privacyDetail() {
        return language == AppLanguage.CHINESE ? "DaysLite 如何处理数据" : "How DaysLite handles data";
    }

    String localStorage() {
        return language == AppLanguage.CHINESE ? "本地存储" : "Local storage";
    }

    String localStorageDetail() {
        return language == AppLanguage.CHINESE ? "倒计时只保存在此设备上" : "Countdowns are saved on this device only";
    }

    String language() {
        return language == AppLanguage.CHINESE ? "语言" : "Language";
    }

    String languageDetail() {
        return language == AppLanguage.CHINESE ? "当前：中文，点击切换到 English" : "Current: English, tap to switch to 中文";
    }

    String version() {
        return language == AppLanguage.CHINESE ? "版本" : "Version";
    }

    String privacyLoadError() {
        return language == AppLanguage.CHINESE
                ? "无法加载隐私政策，请检查网络连接后重试。"
                : "Couldn't load the privacy policy. Please check your connection and try again.";
    }

    String titleRequired() {
        return language == AppLanguage.CHINESE ? "请输入名称" : "Name required";
    }

    String deleteCountdownQuestion() {
        return language == AppLanguage.CHINESE ? "删除倒计时？" : "Delete countdown?";
    }

    String deleteCountdownMessage() {
        return language == AppLanguage.CHINESE ? "这会从此设备移除该倒计时。" : "This removes the countdown from this device.";
    }

    String cancel() {
        return language == AppLanguage.CHINESE ? "取消" : "Cancel";
    }
}
