import Foundation
import Capacitor

@objc(IMessagePlugin)
public class IMessagePlugin: CAPPlugin, CAPBridgedPlugin {

    public let identifier = "IMessagePlugin"
    public let jsName = "IMessage"

    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "sendMessage", returnType: CAPPluginReturnPromise)
    ]

    private let implementation = IMessage()

    @objc func sendMessage(_ call: CAPPluginCall) {
        implementation.sendMessage(call)
    }
}
