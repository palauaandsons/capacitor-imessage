import Foundation

@objc public class IMessage: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
