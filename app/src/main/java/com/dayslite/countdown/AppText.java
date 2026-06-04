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

    String privacyIntro() {
        return language == AppLanguage.CHINESE
                ? "DaysLite 不会收集、传输、出售或共享个人数据。"
                : "DaysLite does not collect, transmit, sell, or share personal data.";
    }

    String privacyLocalData() {
        return language == AppLanguage.CHINESE
                ? "你创建的倒计时事件、日期、颜色和备注只会保存在你的设备本地，不会发送到 DaysLite 服务器或第三方。"
                : "Countdown events, dates, colors, and notes are stored locally on your device. They are not sent to DaysLite servers or shared with third parties.";
    }

    String privacyPermissions() {
        return language == AppLanguage.CHINESE
                ? "DaysLite 不需要账号登录、位置、联系人、照片、麦克风、相机或其他敏感权限。"
                : "DaysLite does not require account login, location access, contacts, photos, microphone, camera, or other sensitive permissions.";
    }

    String privacyDeletion() {
        return language == AppLanguage.CHINESE
                ? "你可以在应用内删除单个倒计时，也可以通过卸载 DaysLite 或在 Android 系统设置中清除应用存储来删除全部数据。"
                : "You can delete individual countdowns inside the app. You can also delete all app data by uninstalling DaysLite or clearing app storage from Android system settings.";
    }

    String privacySupport() {
        return language == AppLanguage.CHINESE
                ? "如需支持，请使用开发者提供的 GitHub 仓库联系方式。"
                : "For support, use the GitHub repository contact path provided by the developer.";
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
