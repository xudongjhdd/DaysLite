import Foundation

struct LocalDate: Codable, Hashable, Comparable, Sendable {
    let year: Int
    let month: Int
    let day: Int

    init(year: Int, month: Int, day: Int) {
        self.year = year
        self.month = month
        self.day = day
    }

    init(_ date: Date, calendar: Calendar = .autoupdatingCurrent) {
        let components = calendar.dateComponents([.year, .month, .day], from: date)
        self.init(
            year: components.year ?? 1970,
            month: components.month ?? 1,
            day: components.day ?? 1
        )
    }

    static func < (lhs: LocalDate, rhs: LocalDate) -> Bool {
        (lhs.year, lhs.month, lhs.day) < (rhs.year, rhs.month, rhs.day)
    }

    func date(in calendar: Calendar = .autoupdatingCurrent) -> Date? {
        calendar.date(from: DateComponents(year: year, month: month, day: day))
    }

    func adding(
        days: Int,
        calendar: Calendar = .autoupdatingCurrent
    ) -> LocalDate? {
        guard
            let date = date(in: calendar),
            let result = calendar.date(byAdding: .day, value: days, to: date)
        else {
            return nil
        }
        return LocalDate(result, calendar: calendar)
    }

    func withYear(
        _ year: Int,
        calendar: Calendar = Calendar(identifier: .gregorian)
    ) -> LocalDate {
        let requested = DateComponents(year: year, month: month, day: day)
        if let date = calendar.date(from: requested) {
            let result = calendar.dateComponents([.year, .month, .day], from: date)
            if result.year == year, result.month == month, result.day == day {
                return LocalDate(year: year, month: month, day: day)
            }
        }
        return LocalDate(year: year, month: month, day: 28)
    }
}
