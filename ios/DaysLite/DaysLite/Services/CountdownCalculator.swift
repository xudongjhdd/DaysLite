import Foundation

struct CountdownCalculator: Sendable {
    let calendar: Calendar

    func displayDate(
        for event: CountdownEvent,
        today: LocalDate
    ) -> LocalDate {
        guard event.repeatYearly else {
            return event.targetDate
        }

        let thisYear = event.targetDate.withYear(today.year, calendar: calendar)
        if thisYear < today {
            return event.targetDate.withYear(today.year + 1, calendar: calendar)
        }
        return thisYear
    }

    func status(
        for event: CountdownEvent,
        today: LocalDate
    ) -> CountdownStatus {
        let target = displayDate(for: event, today: today)
        let days = dayDistance(from: today, to: target)
        if days == 0 {
            return .today
        }
        if days > 0 {
            return .future(days)
        }
        return .past(abs(days))
    }

    func isPast(
        _ event: CountdownEvent,
        today: LocalDate
    ) -> Bool {
        !event.repeatYearly && event.targetDate < today
    }

    private func dayDistance(from start: LocalDate, to end: LocalDate) -> Int {
        guard
            let startDate = start.date(in: calendar),
            let endDate = end.date(in: calendar)
        else {
            return 0
        }
        return calendar.dateComponents([.day], from: startDate, to: endDate).day ?? 0
    }
}
