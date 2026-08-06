import SwiftUI

struct HomeView: View {
    @Environment(AppModel.self) private var model

    let onAdd: () -> Void
    let onEdit: (UUID) -> Void
    let onSettings: () -> Void

    private var text: AppText {
        AppText(language: model.language)
    }

    private var today: LocalDate {
        LocalDate(Date())
    }

    var body: some View {
        NavigationStack {
            ZStack {
                Theme.background.ignoresSafeArea()

                ScrollView {
                    LazyVStack(alignment: .leading, spacing: 16) {
                        header

                        if model.events.isEmpty {
                            emptyState
                        } else {
                            ForEach(model.sortedEvents(today: today)) { event in
                                CountdownCardView(event: event, today: today, text: text) {
                                    onEdit(event.id)
                                }
                            }

                            addButton
                        }
                    }
                    .padding(.horizontal, 20)
                    .padding(.top, 16)
                    .padding(.bottom, 36)
                }
            }
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button(action: onSettings) {
                        Image(systemName: "gearshape")
                            .font(.body.weight(.semibold))
                    }
                    .accessibilityLabel(text.settings)
                    .accessibilityIdentifier("home.settings")
                }
            }
            .toolbarBackground(Theme.background, for: .navigationBar)
        }
    }

    private var header: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(text.appName)
                .font(.system(size: 34, weight: .bold, design: .rounded))
                .foregroundStyle(Theme.textPrimary)
                .accessibilityIdentifier("home.title")

            Text(model.events.isEmpty ? text.tagline : text.summary(upcoming: model.upcomingCount(today: today)))
                .font(.subheadline)
                .foregroundStyle(Theme.textSecondary)
                .accessibilityIdentifier("home.summary")
        }
    }

    private var emptyState: some View {
        VStack(spacing: 18) {
            Image(systemName: "calendar.badge.plus")
                .font(.system(size: 42, weight: .medium))
                .foregroundStyle(Theme.primary)

            VStack(spacing: 6) {
                Text(text.noCountdownsYet)
                    .font(.title3.weight(.bold))
                    .foregroundStyle(Theme.textPrimary)
                Text(text.emptyHelper)
                    .font(.subheadline)
                    .foregroundStyle(Theme.textSecondary)
                    .multilineTextAlignment(.center)
            }

            addButton
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 24)
        .padding(.vertical, 36)
        .background(Theme.card, in: RoundedRectangle(cornerRadius: 24, style: .continuous))
        .overlay {
            RoundedRectangle(cornerRadius: 24, style: .continuous)
                .stroke(Theme.border, lineWidth: 1)
        }
        .shadow(color: Theme.shadow, radius: 14, y: 6)
        .padding(.top, 8)
        .accessibilityIdentifier("home.empty")
    }

    private var addButton: some View {
        Button(action: onAdd) {
            Label(text.addCountdown, systemImage: "plus")
                .font(.headline)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 14)
                .foregroundStyle(.white)
                .background(Theme.primary, in: RoundedRectangle(cornerRadius: 14, style: .continuous))
        }
        .buttonStyle(.plain)
        .accessibilityIdentifier("home.add")
    }
}
