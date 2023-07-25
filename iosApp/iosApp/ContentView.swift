import SwiftUI
import shared

struct ContentView: View {
	var body: some View {
        ComposeView()
            .ignoresSafeArea(.all, edges: .all)
	}
}

struct ComposeView: UIViewControllerRepresentable {

    init() {

    }

    func makeUIViewController(context _: Context) -> UIViewController {
        return DeckBoxUiViewControllerKt.DeckBoxUiViewController()
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}


struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
