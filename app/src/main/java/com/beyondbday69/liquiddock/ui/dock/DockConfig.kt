package com.beyondbday69.liquiddock.ui.dock

enum class BackgroundPreset(val displayName: String) {
    MESH_GRADIENT("Animated Mesh"),
    ALBUM_CARDS("Artwork Cards"),
    NEON_FLOW("Neon Glow"),
    CLEAN_MINIMAL("Clean Surface")
}

data class DockConfig(
    val liquidGlassEnabled: Boolean = true,
    val blurRadiusDp: Float = 16f,
    val lensHeightDp: Float = 14f,
    val lensAmountDp: Float = 20f,
    val highlightAlpha: Float = 0.45f,
    val dockHeightDp: Float = 58f,
    val isDarkTheme: Boolean = true,
    val backgroundPreset: BackgroundPreset = BackgroundPreset.MESH_GRADIENT,
    val tabCount: Int = 4, // 3, 4, or 5 tabs
    val selectedItemPillAlpha: Float = 0.72f,
    val showCutCornerMorph: Boolean = true,
    val chromaticAberration: Boolean = true,
    val specularGlow: Boolean = true,
)
