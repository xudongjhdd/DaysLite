import Foundation
import Observation

@MainActor
@Observable
final class AppModel {
    private let store: CountdownStore
    private let now: () -> Date

    private(set) var events: [CountdownEvent]
    private(set) var language: AppLanguage
    private(set) var storeError: StoreError?

    init(
        store: CountdownStore = CountdownStore(),
        now: @escaping () -> Date = Date.init
    ) {
        self.store = store
        self.now = now
        events = store.loadEvents()
        language = store.loadLanguage()
    }

    @discardableResult
    func add(
        title: String,
        targetDate: LocalDate,
        note: String,
        colorHex: String,
        repeatYearly: Bool
    ) -> CountdownEvent {
        let timestamp = now()
        let event = CountdownEvent(
            id: UUID(),
            title: title,
            targetDate: targetDate,
            note: note,
            colorHex: colorHex,
            repeatYearly: repeatYearly,
            createdAt: timestamp,
            updatedAt: timestamp
        )
        events.append(event)
        persistEvents()
        return event
    }

    func update(
        id: UUID,
        title: String,
        targetDate: LocalDate,
        note: String,
        colorHex: String,
        repeatYearly: Bool
    ) {
        guard let index = events.firstIndex(where: { $0.id == id }) else {
            return
        }

        events[index].title = title
        events[index].targetDate = targetDate
        events[index].note = note
        events[index].colorHex = colorHex
        events[index].repeatYearly = repeatYearly
        events[index].updatedAt = now()
        persistEvents()
    }

    func delete(id: UUID) {
        events.removeAll { $0.id == id }
        persistEvents()
    }

    func toggleLanguage() {
        language.toggle()
        store.saveLanguage(language)
    }

    private func persistEvents() {
        switch store.saveEvents(events) {
        case .success:
            storeError = nil
        case .failure(let error):
            storeError = error
        }
    }
}
