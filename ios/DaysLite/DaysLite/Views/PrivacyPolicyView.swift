import SwiftUI

struct PrivacyPolicyView: View {
    let language: AppLanguage

    private var text: AppText {
        AppText(language: language)
    }

    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 14) {
                ForEach(Array(document.blocks.enumerated()), id: \.offset) { _, block in
                    switch block {
                    case .heading(let level, let text):
                        Text(text)
                            .font(level == 1 ? .title2.bold() : .headline)
                            .padding(.top, level == 1 ? 0 : 8)
                    case .paragraph(let text):
                        Text(text)
                            .font(.body)
                            .lineSpacing(4)
                    }
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

    private var document: PrivacyPolicyDocument {
        PrivacyPolicyDocument(markdown: policySource)
    }
}

struct PrivacyPolicyDocument: Equatable {
    enum Block: Equatable {
        case heading(level: Int, text: String)
        case paragraph(String)
    }

    let blocks: [Block]

    init(markdown: String) {
        blocks = markdown
            .components(separatedBy: "\n\n")
            .compactMap { rawBlock in
                let content = rawBlock.trimmingCharacters(in: .whitespacesAndNewlines)
                guard !content.isEmpty else { return nil }

                if content.hasPrefix("## ") {
                    return .heading(level: 2, text: String(content.dropFirst(3)))
                }
                if content.hasPrefix("# ") {
                    return .heading(level: 1, text: String(content.dropFirst(2)))
                }

                return .paragraph(content.replacingOccurrences(of: "\n", with: " "))
            }
    }
}
