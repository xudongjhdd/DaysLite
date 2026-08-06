import SwiftUI

enum EditorSelection: Identifiable {
    case new
    case existing(UUID)

    var id: String {
        switch self {
        case .new:
            return "new"
        case .existing(let id):
            return id.uuidString
        }
    }
}

struct RootView: View {
    @Environment(AppModel.self) private var model
    @State private var editorSelection: EditorSelection?
    @State private var showsSettings = false

    private var text: AppText {
        AppText(language: model.language)
    }

    var body: some View {
        HomeView(
            onAdd: { editorSelection = .new },
            onEdit: { editorSelection = .existing($0) },
            onSettings: { showsSettings = true }
        )
        .sheet(item: $editorSelection) { selection in
            CountdownEditorView(event: event(for: selection))
        }
        .sheet(isPresented: $showsSettings) {
            NavigationStack {
                SettingsView()
            }
        }
        .alert(text.saveFailure, isPresented: storeErrorBinding) {
            Button(text.dismiss) {
                model.clearStoreError()
            }
        }
    }

    private var storeErrorBinding: Binding<Bool> {
        Binding(
            get: { model.storeError != nil },
            set: { isPresented in
                if !isPresented {
                    model.clearStoreError()
                }
            }
        )
    }

    private func event(for selection: EditorSelection) -> CountdownEvent? {
        switch selection {
        case .new:
            return nil
        case .existing(let id):
            return model.events.first { $0.id == id }
        }
    }
}
