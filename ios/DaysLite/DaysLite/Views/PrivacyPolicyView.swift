import SwiftUI

struct PrivacyPolicyView: View {
    let language: AppLanguage

    private var text: AppText {
        AppText(language: language)
    }

    var body: some View {
        ScrollView {
            Group {
                if let attributedPolicy {
                    Text(attributedPolicy)
                } else {
                    Text(policySource)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .foregroundStyle(Theme.textPrimary)
            .padding(20)
            .textSelection(.enabled)
            .accessibilityIdentifier("privacy.content")
        }
        .background(Theme.background)
        .navigationTitle(text.privacyPolicy)
        .navigationBarTitleDisplayMode(.inline)
    }

    private var policySource: String {
        let resource = language == .chinese ? "privacy-policy.zh-Hans" : "privacy-policy.en"
        guard
            let url = Bundle.main.url(forResource: resource, withExtension: "md"),
            let source = try? String(contentsOf: url, encoding: .utf8)
        else {
            return text.privacyLoadError
        }
        return source
    }

    private var attributedPolicy: AttributedString? {
        try? AttributedString(markdown: policySource)
    }
}
