# LiquidDock 🌊

An interactive Android sandbox and playground application built for experimenting with and tuning the **Liquid Glass Floating Dockbar** powered by Kyant Liquid Glass (`io.github.kyant0:backdrop`).

---

## ✨ Features

- **Toggleable Liquid Glass**: Switch seamlessly between authentic GPU-accelerated refraction shaders (`drawBackdrop` with blur, vibrancy, lens refraction, highlights, and inner shadows) and standard Material 3 surfaces.
- **Fluid Spring Physics**: Coordinated 0-jitter item expansion, tactile press responses, and dynamic cut-corner morphs.
- **Interactive Tweak Inspector**:
  - Live Blur Radius slider (0 – 32 dp)
  - Lens Refraction & Height slider (0 – 28 dp)
  - Specular Highlight slider (0% – 100%)
  - Dynamic Tab Count (3, 4, or 5 tabs with adaptive pill sizing)
  - Light & Dark Theme toggles
- **Multiple Test Backdrops**:
  - Animated multi-color fluid mesh gradients
  - Rich colorful artwork collage
  - Deep neon glow visualizer
  - Real-time scrollable cards list to observe dynamic magnification and refraction as elements scroll under the dock.

---

## 📦 Download APK

You can download the pre-built `.apk` directly from:
- **[GitHub Releases](https://github.com/beyondbday69/LiquidDock/releases)**
- **[GitHub Actions Artifacts](https://github.com/beyondbday69/LiquidDock/actions)**

---

## 🛠️ Tech Stack

- **Jetpack Compose** & **Material 3 Expressive**
- **io.github.kyant0:backdrop** (2.0.1) & **io.github.kyant0:shapes** (1.2.1)
- **Kotlin 2.4.20** & **Android Gradle Plugin 9.2.1**
- Minimum SDK: Android 8.0 (API 26) • Target SDK: Android 15 (API 36)

---

## 🚀 Building Locally

```bash
./gradlew assembleDebug
# Output APK: app/build/outputs/apk/debug/app-debug.apk
```
