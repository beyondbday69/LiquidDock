package com.beyondbday69.liquiddock.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalIsDarkTheme = staticCompositionLocalOf { true }

/**
 * Pure Liquid Glass Theme (100% Free of Material 3).
 */
@Composable
fun LiquidDockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIsDarkTheme provides darkTheme,
        content = content
    )
}
