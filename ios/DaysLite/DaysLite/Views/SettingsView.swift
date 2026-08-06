import SwiftUI

struct SettingsView: View {
    @Environment(AppModel.self) private var model
    @Environment(\.dismiss) private var dismiss

    private var text: AppText {
        AppText(language: model.language)
    }

    private var version: String {
        Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? "1.0.0"
    }

    var body: some View {
        List {
            Section {
                NavigationLink {
                    PrivacyPolicyView(language: model.language)
                } label: {
                    SettingsRow(
                        systemImage: "hand.raised.fill",
                        title: text.privacyPolicy,
                        detail: text.privacyDetail
                    )
                }
                .accessibilityIdentifier("settings.privacy")

                SettingsRow(
                    systemImage: "iphone",
                    title: text.localStorage,
                    detail: text.localStorageDetail
                )
                .accessibilityIdentifier("settings.storage")
            }

            Section {
                Picker(text.languageLabel, selection: languageBinding) {
                    Text("English").tag(AppLanguage.english)
                    Text("简体中文").tag(AppLanguage.chinese)
                }
                .pickerStyle(.navigationLink)
                .accessibilityIdentifier("settings.language")
            } header: {
                Text(text.languageLabel)
            } footer: {
                Text(text.languageDetail)
            }

            Section {
                LabeledContent(text.version, value: version)
                    .accessibilityIdentifier("settings.version")
            }
        }
        .navigationTitle(text.settingsTitle)
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .confirmationAction) {
                Button(text.dismiss) {
                    dismiss()
                }
                .accessibilityIdentifier("settings.done")
            }
        }
    }

    private var languageBinding: Binding<AppLanguage> {
        Binding(
            get: { model.language },
            set: { model.setLanguage($0) }
        )
    }
}

private struct SettingsRow: View {
    let systemImage: String
    let title: String
    let detail: String

    var body: some View {
        HStack(alignment: .top, spacing: 14) {
            Image(systemName: systemImage)
                .frame(width: 24)
                .foregroundStyle(Theme.primary)
                .accessibilityHidden(true)

            VStack(alignment: .leading, spacing: 3) {
                Text(title)
                    .foregroundStyle(.primary)
                Text(detail)
                    .font(.footnote)
                    .foregroundStyle(.secondary)
            }
        }
        .padding(.vertical, 3)
    }
}
