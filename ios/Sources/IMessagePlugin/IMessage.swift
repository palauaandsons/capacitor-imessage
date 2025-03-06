import Foundation
import MessageUI
import UIKit
import Capacitor

@objc(IMessage)
public class IMessage: NSObject, MFMessageComposeViewControllerDelegate {

    private var call: CAPPluginCall?

    @objc public func sendMessage(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            if !MFMessageComposeViewController.canSendText() {
                call.reject("Messaging is not available on this device")
                return
            }

            let messageVC = MFMessageComposeViewController()
            messageVC.messageComposeDelegate = self
            self.call = call

            // Get text and image URL from the call
            let text = call.getString("text") ?? ""
            let imageUrl = call.getString("imageUrl")

            messageVC.body = text

            // Attach the image if a valid URL is provided
            if let imageUrl = imageUrl, let url = URL(string: imageUrl),
               let imageData = try? Data(contentsOf: url) {
                
                let fileName = "image.jpg"
                let mimeType = "image/jpeg"
                messageVC.addAttachmentData(imageData, typeIdentifier: mimeType, filename: fileName)
            }

            guard let viewController = UIApplication.shared.connectedScenes
                .compactMap({ ($0 as? UIWindowScene)?.windows.first?.rootViewController })
                .first else {
                call.reject("Failed to get root view controller")
                return
            }

            viewController.present(messageVC, animated: true, completion: nil)
            call.resolve()
        }
    }

    public func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
        controller.dismiss(animated: true, completion: nil)

        switch result {
        case .sent:
            call?.resolve(["status": "sent"])
        case .cancelled:
            call?.resolve(["status": "cancelled"])
        default:
            call?.reject("Failed to send message")
        }
    }
}
