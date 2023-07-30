import SwiftUI
import shared

class AppDelegate: UIResponder, UIApplicationDelegate {
    lazy var applicationComponent: MergedIosApplicationComponent = createApplicationComponent(
        appDelegate: self
    )

    func application(
        _: UIApplication,
        didFinishLaunchingWithOptions _: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
//        if !(FirebaseOptions.defaultOptions()?.apiKey?.isEmpty ?? true) {
//            FirebaseApp.configure()
//        }
//        applicationComponent.initializers.initialize()
        return true
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

	var body: some Scene {
		WindowGroup {
			let uiComponent = createHomeUiControllerComponent(
                applicationComponent: delegate.applicationComponent
            )
            ContentView(component: uiComponent)
		}
	}
}

private func createApplicationComponent(
    appDelegate: AppDelegate
) -> MergedIosApplicationComponent {
    return IosApplicationComponent.companion.createMergedIosApplicationComponent()
}

private func createHomeUiControllerComponent(
    applicationComponent: MergedIosApplicationComponent
) -> HomeUiControllerComponent {
    return applicationComponent.createHomeUiControllerComponent()
}
