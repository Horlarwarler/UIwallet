import SwiftUI
import composeApp
import ASN1Decoder

@main
struct iOSApp: App {
    

    init() {
        IosInitKoinKt.iosInitKoin(component: IosApplicationComponent(
            cryptographicOperation: IosCryptographicOperation(),
            keyPairGenerator: IosKeyPairGenerator()))
     
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
