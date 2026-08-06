#!/usr/bin/env swift

import CoreGraphics
import Foundation
import ImageIO
import UniformTypeIdentifiers

guard CommandLine.arguments.count == 2 else {
    FileHandle.standardError.write(Data("Usage: generate-app-icon.swift OUTPUT.png\n".utf8))
    exit(2)
}

let size = 1024
let colorSpace = CGColorSpaceCreateDeviceRGB()
guard let context = CGContext(
    data: nil,
    width: size,
    height: size,
    bitsPerComponent: 8,
    bytesPerRow: size * 4,
    space: colorSpace,
    bitmapInfo: CGImageAlphaInfo.noneSkipLast.rawValue
) else {
    fatalError("Could not create RGB drawing context")
}

func rgb(_ red: Int, _ green: Int, _ blue: Int) -> CGColor {
    CGColor(
        colorSpace: colorSpace,
        components: [CGFloat(red) / 255, CGFloat(green) / 255, CGFloat(blue) / 255, 1]
    )!
}

func fillRoundedRect(_ rect: CGRect, radius: CGFloat, color: CGColor) {
    context.setFillColor(color)
    context.addPath(CGPath(roundedRect: rect, cornerWidth: radius, cornerHeight: radius, transform: nil))
    context.fillPath()
}

let blue = rgb(37, 99, 235)
let paleBlue = rgb(219, 234, 254)
let white = rgb(255, 255, 255)

context.setFillColor(blue)
context.fill(CGRect(x: 0, y: 0, width: size, height: size))

let calendar = CGRect(x: 190, y: 180, width: 644, height: 650)
fillRoundedRect(calendar, radius: 86, color: white)

context.setFillColor(paleBlue)
context.fill(CGRect(x: calendar.minX, y: 596, width: calendar.width, height: 138))

fillRoundedRect(CGRect(x: 328, y: 754, width: 76, height: 142), radius: 30, color: white)
fillRoundedRect(CGRect(x: 620, y: 754, width: 76, height: 142), radius: 30, color: white)

// A bold, geometric “7” keeps the countdown mark legible at small icon sizes.
context.setFillColor(blue)
context.beginPath()
context.move(to: CGPoint(x: 382, y: 516))
context.addLine(to: CGPoint(x: 674, y: 516))
context.addLine(to: CGPoint(x: 674, y: 448))
context.addLine(to: CGPoint(x: 526, y: 244))
context.addLine(to: CGPoint(x: 422, y: 244))
context.addLine(to: CGPoint(x: 570, y: 448))
context.addLine(to: CGPoint(x: 382, y: 448))
context.closePath()
context.fillPath()

guard let image = context.makeImage() else {
    fatalError("Could not create icon image")
}

let outputURL = URL(fileURLWithPath: CommandLine.arguments[1])
guard let destination = CGImageDestinationCreateWithURL(
    outputURL as CFURL,
    UTType.png.identifier as CFString,
    1,
    nil
) else {
    fatalError("Could not create PNG destination")
}

CGImageDestinationAddImage(destination, image, nil)
guard CGImageDestinationFinalize(destination) else {
    fatalError("Could not write PNG")
}
