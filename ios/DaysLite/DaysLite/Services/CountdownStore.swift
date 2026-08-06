import Foundation

protocol KeyValueStoring: AnyObject {
    func data(forKey defaultName: String) -> Data?
    func string(forKey defaultName: String) -> String?
    func set(_ value: Any?, forKey defaultName: String)
}

extension UserDefaults: KeyValueStoring {}

enum StoreError: Error, Equatable {
    case encodingFailed
    case writeVerificationFailed
}

final class CountdownStore {
    private enum Key {
        static let events = "events"
        static let language = "language"
    }

    private let storage: any KeyValueStoring
    private let encoder = JSONEncoder()
    private let decoder = JSONDecoder()

    convenience init(defaults: UserDefaults = .standard) {
        self.init(storage: defaults)
    }

    init(storage: any KeyValueStoring) {
        self.storage = storage
    }

    func loadEvents() -> [CountdownEvent] {
        guard
            let data = storage.data(forKey: Key.events),
            let array = try? JSONSerialization.jsonObject(with: data) as? [Any]
        else {
            return []
        }

        return array.compactMap { value in
            guard
                let entry = try? JSONSerialization.data(withJSONObject: value),
                let event = try? decoder.decode(CountdownEvent.self, from: entry)
            else {
                return nil
            }
            return event
        }
    }

    @discardableResult
    func saveEvents(_ events: [CountdownEvent]) -> Result<Void, StoreError> {
        guard let data = try? encoder.encode(events) else {
            return .failure(.encodingFailed)
        }

        storage.set(data, forKey: Key.events)
        guard storage.data(forKey: Key.events) == data else {
            return .failure(.writeVerificationFailed)
        }
        return .success(())
    }

    func loadLanguage() -> AppLanguage {
        guard
            let rawValue = storage.string(forKey: Key.language),
            let language = AppLanguage(rawValue: rawValue)
        else {
            return .english
        }
        return language
    }

    func saveLanguage(_ language: AppLanguage) {
        storage.set(language.rawValue, forKey: Key.language)
    }
}
