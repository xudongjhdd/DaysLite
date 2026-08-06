import SwiftUI

enum Theme {
    static let background = Color(
        red: 248.0 / 255.0,
        green: 250.0 / 255.0,
        blue: 252.0 / 255.0
    )
    static let textPrimary = Color(
        red: 15.0 / 255.0,
        green: 23.0 / 255.0,
        blue: 42.0 / 255.0
    )
    static let textSecondary = Color(
        red: 100.0 / 255.0,
        green: 116.0 / 255.0,
        blue: 139.0 / 255.0
    )
    static let primary = Color(
        red: 37.0 / 255.0,
        green: 99.0 / 255.0,
        blue: 235.0 / 255.0
    )
    static let card = Color.white
    static let border = Color.black.opacity(0.06)
    static let shadow = Color.black.opacity(0.06)
    static let eventColors = ["#2563EB", "#10B981", "#F59E0B", "#EC4899", "#8B5CF6", "#64748B"]
}

extension Color {
    init(hex: String) {
        let raw = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        let value = UInt64(raw, radix: 16) ?? 0x2563EB
        let red = Double((value >> 16) & 0xFF) / 255
        let green = Double((value >> 8) & 0xFF) / 255
        let blue = Double(value & 0xFF) / 255
        self.init(red: red, green: green, blue: blue)
    }
}
