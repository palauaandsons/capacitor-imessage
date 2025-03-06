import Foundation
import MessageUI
import UIKit
import Capacitor

@objc(IMessage)
public class IMessage: NSObject, MFMessageComposeViewControllerDelegate {

    private var activeCall: CAPPluginCall?
    
    @objc public func isMessagingAvailable(_ call: CAPPluginCall) {
        let available = MFMessageComposeViewController.canSendText()
        call.resolve(["available": available])
    }

    @objc public func sendMessage(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            if !MFMessageComposeViewController.canSendText() {
                call.reject("Messaging is not available on this device")
                return
            }

            let messageVC = MFMessageComposeViewController()
            messageVC.messageComposeDelegate = self
            self.activeCall = call

            // Get text and image URL from the call
            let text = call.getString("text") ?? ""
            let imageUrl = call.getString("imageUrl")

            messageVC.body = text

            // Attach the image if a valid URL is provided
            if let imageUrl = imageUrl, let url = URL(string: imageUrl),
               let imageData = try? Data(contentsOf: url) {
                messageVC.addAttachmentData(imageData, typeIdentifier: "image/jpeg", filename: "image.jpg")
            }

            guard let viewController = UIApplication.shared.connectedScenes
                .compactMap({ ($0 as? UIWindowScene)?.windows.first?.rootViewController })
                .first else {
                call.reject("Failed to get root view controller")
                return
            }

            viewController.present(messageVC, animated: true)
        }
    }

    // Handle user interaction result
    public func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
        controller.dismiss(animated: true, completion: nil)

        guard let pluginCall = self.activeCall else { return }

        switch result {
        case .sent:
            pluginCall.resolve(["status": "sent"])
        case .cancelled:
            pluginCall.resolve(["status": "cancelled"])
        default:
            pluginCall.reject("Failed to send message")
        }

        self.activeCall = nil
    }
}
