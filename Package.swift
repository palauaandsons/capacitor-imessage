// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorImessage",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorImessage",
            targets: ["IMessagePlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "IMessagePlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/IMessagePlugin"),
        .testTarget(
            name: "IMessagePluginTests",
            dependencies: ["IMessagePlugin"],
            path: "ios/Tests/IMessagePluginTests")
    ]
)