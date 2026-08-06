import SwiftUI

struct CountdownEditorView: View {
    @Environment(AppModel.self) private var model
    @Environment(\.dismiss) private var dismiss

    private let eventID: UUID?
    @State private var draft: CountdownDraft
    @State private var showsTitleError = false
    @State private var showsDeleteConfirmation = false
    @FocusState private var focusedField: Field?

    private enum Field {
        case title
        case note
    }

    init(event: CountdownEvent?, today: LocalDate = LocalDate(Date())) {
        eventID = event?.id
        _draft = State(
            initialValue: event.map(CountdownDraft.init(event:))
                ?? .new(today: today, calendar: .autoupdatingCurrent)
        )
    }

    private var text: AppText {
        AppText(language: model.language)
    }

    var body: some View {
        NavigationStack {
            Form {
                Section {
                    TextField(text.eventNameHint, text: $draft.title)
                        .textInputAutocapitalization(.sentences)
                        .submitLabel(.done)
                        .focused($focusedField, equals: .title)
                        .accessibilityLabel(text.eventName)
                        .accessibilityIdentifier("editor.title")

                    if showsTitleError {
                        Text(text.titleRequired)
                            .font(.footnote)
                            .foregroundStyle(.red)
                    }
                } header: {
                    Text(text.eventName)
                }

                Section(text.targetDate) {
                    DatePicker(
                        text.targetDate,
                        selection: dateBinding,
                        displayedComponents: .date
                    )
                    .datePickerStyle(.graphical)
                    .accessibilityIdentifier("editor.date")
                }

                Section(text.color) {
                    ScrollView(.horizontal) {
                        HStack(spacing: 18) {
                            ForEach(CountdownDraft.approvedColors, id: \.self) { colorHex in
                                Button {
                                    draft.colorHex = colorHex
                                } label: {
                                    ZStack {
                                        Circle()
                                            .fill(Color(hex: colorHex))
                                            .frame(width: 40, height: 40)

                                        if draft.colorHex == colorHex {
                                            Image(systemName: "checkmark")
                                                .font(.body.weight(.bold))
                                                .foregroundStyle(.white)
                                        }
                                    }
                                }
                                .buttonStyle(.plain)
                                .accessibilityLabel(colorAccessibilityLabel(colorHex))
                                .accessibilityValue(draft.colorHex == colorHex ? selectedLabel : "")
                                .accessibilityIdentifier("editor.color.\(colorHex.dropFirst())")
                            }
                        }
                        .padding(.vertical, 4)
                    }
                    .scrollIndicators(.hidden)
                }

                Section {
                    TextEditor(text: $draft.note)
                        .frame(minHeight: 90)
                        .focused($focusedField, equals: .note)
                        .accessibilityLabel(text.note)
                        .accessibilityIdentifier("editor.note")
                } header: {
                    Text("\(text.note) · \(text.optional)")
                }

                Section {
                    Toggle(isOn: $draft.repeatYearly) {
                        VStack(alignment: .leading, spacing: 3) {
                            Text(text.repeatYearly)
                            Text(text.repeatYearlyDetail)
                                .font(.footnote)
                                .foregroundStyle(.secondary)
                        }
                    }
                    .accessibilityIdentifier("editor.yearly")
                }

                if eventID != nil {
                    Section {
                        Button(text.delete, role: .destructive) {
                            showsDeleteConfirmation = true
                        }
                        .frame(maxWidth: .infinity)
                        .accessibilityIdentifier("editor.delete")
                    }
                }
            }
            .scrollDismissesKeyboard(.interactively)
            .navigationTitle(eventID == nil ? text.addCountdownTitle : text.editCountdownTitle)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(text.cancel) {
                        dismiss()
                    }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button(text.save, action: save)
                        .fontWeight(.semibold)
                        .accessibilityIdentifier("editor.save")
                }
                ToolbarItemGroup(placement: .keyboard) {
                    Spacer()
                    Button("Done") {
                        focusedField = nil
                    }
                }
            }
            .confirmationDialog(
                text.deleteCountdownQuestion,
                isPresented: $showsDeleteConfirmation,
                titleVisibility: .visible
            ) {
                Button(text.delete, role: .destructive) {
                    deleteEvent()
                }
                Button(text.cancel, role: .cancel) {}
            } message: {
                Text(text.deleteCountdownMessage)
            }
        }
    }

    private var dateBinding: Binding<Date> {
        Binding(
            get: { draft.targetDate.date() ?? Date() },
            set: { draft.targetDate = LocalDate($0) }
        )
    }

    private var selectedLabel: String {
        model.language == .chinese ? "已选择" : "Selected"
    }

    private func colorAccessibilityLabel(_ colorHex: String) -> String {
        let names: [String: (String, String)] = [
            "#2563EB": ("蓝色", "Blue"),
            "#10B981": ("绿色", "Green"),
            "#F97316": ("橙色", "Orange"),
            "#EC4899": ("粉色", "Pink"),
            "#7C3AED": ("紫色", "Purple"),
            "#475569": ("灰色", "Gray")
        ]
        let name = names[colorHex] ?? ("颜色", "Color")
        return model.language == .chinese ? name.0 : name.1
    }

    private func save() {
        draft.title = draft.title.trimmingCharacters(in: .whitespacesAndNewlines)
        draft.note = draft.note.trimmingCharacters(in: .whitespacesAndNewlines)

        guard draft.isValid else {
            showsTitleError = true
            focusedField = .title
            return
        }

        if let eventID {
            model.update(
                id: eventID,
                title: draft.title,
                targetDate: draft.targetDate,
                note: draft.note,
                colorHex: draft.colorHex,
                repeatYearly: draft.repeatYearly
            )
        } else {
            model.add(
                title: draft.title,
                targetDate: draft.targetDate,
                note: draft.note,
                colorHex: draft.colorHex,
                repeatYearly: draft.repeatYearly
            )
        }
        dismiss()
    }

    private func deleteEvent() {
        guard let eventID else { return }
        model.delete(id: eventID)
        dismiss()
    }
}
