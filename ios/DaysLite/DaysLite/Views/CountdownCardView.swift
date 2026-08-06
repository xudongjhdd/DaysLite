import SwiftUI

struct CountdownCardView: View {
    let event: CountdownEvent
    let today: LocalDate
    let text: AppText
    let onTap: () -> Void

    private var calculator: CountdownCalculator {
        CountdownCalculator(calendar: .autoupdatingCurrent)
    }

    private var isPast: Bool {
        calculator.isPast(event, today: today)
    }

    var body: some View {
        Button(action: onTap) {
            VStack(alignment: .leading, spacing: 14) {
                HStack(alignment: .top, spacing: 12) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(Color(hex: event.colorHex))
                        .frame(width: 6, height: 48)

                    VStack(alignment: .leading, spacing: 5) {
                        Text(event.title)
                            .font(.headline)
                            .foregroundStyle(Theme.textPrimary)
                            .lineLimit(2)

                        Text(text.dateLabel(calculator.displayDate(for: event, today: today)))
                            .font(.subheadline)
                            .foregroundStyle(Theme.textSecondary)
                    }

                    Spacer(minLength: 8)

                    Text(text.daysLabel(for: calculator.status(for: event, today: today)))
                        .font(.subheadline.weight(.semibold))
                        .foregroundStyle(isPast ? Theme.textSecondary : Color(hex: event.colorHex))
                        .multilineTextAlignment(.trailing)
                }

                if event.repeatYearly || !event.note.isEmpty {
                    HStack(spacing: 8) {
                        if event.repeatYearly {
                            Text(text.yearlyBadge)
                                .font(.caption.weight(.semibold))
                                .padding(.horizontal, 9)
                                .padding(.vertical, 4)
                                .background(Color(hex: event.colorHex).opacity(0.12), in: Capsule())
                                .foregroundStyle(Color(hex: event.colorHex))
                        }

                        if !event.note.isEmpty {
                            Text(event.note)
                                .font(.footnote)
                                .foregroundStyle(Theme.textSecondary)
                                .lineLimit(2)
                        }
                    }
                }
            }
            .padding(18)
            .background(Theme.card, in: RoundedRectangle(cornerRadius: 20, style: .continuous))
            .overlay {
                RoundedRectangle(cornerRadius: 20, style: .continuous)
                    .stroke(Theme.border, lineWidth: 1)
            }
            .shadow(color: Theme.shadow, radius: 12, y: 5)
            .opacity(isPast ? 0.68 : 1)
        }
        .buttonStyle(.plain)
        .accessibilityElement(children: .combine)
        .accessibilityHint(text.language == .chinese ? "双击编辑倒计时" : "Double-tap to edit countdown")
        .accessibilityIdentifier("event.\(event.id.uuidString.uppercased())")
    }
}
