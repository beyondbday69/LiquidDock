package com.beyondbday69.liquiddock.ui.playground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beyondbday69.liquiddock.ui.components.GlassCard
import com.beyondbday69.liquiddock.ui.components.GlassChip
import com.beyondbday69.liquiddock.ui.components.GlassIcon
import com.beyondbday69.liquiddock.ui.components.GlassIconButton
import com.beyondbday69.liquiddock.ui.components.GlassText
import com.beyondbday69.liquiddock.ui.components.LiquidSlider
import com.beyondbday69.liquiddock.ui.components.LiquidToggle
import com.beyondbday69.liquiddock.ui.dock.BackgroundPreset
import com.beyondbday69.liquiddock.ui.dock.DockConfig
import com.beyondbday69.liquiddock.ui.dock.LiquidDockBar
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop

/**
 * Pure Authentic Liquid Glass Playground (100% Free of Material 3).
 */
@Composable
fun LiquidDockPlaygroundScreen(
    backdrop: LayerBackdrop,
    modifier: Modifier = Modifier,
) {
    var config by remember { mutableStateOf(DockConfig()) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isSettingsSelected by remember { mutableStateOf(false) }
    var showInspector by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // Source backdrop layer that the floating glass dock samples from
        Box(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
        ) {
            BackgroundCanvas(
                config = config,
                contentPadding = PaddingValues(top = 80.dp, bottom = 120.dp),
            )
        }

        // Top Glass Control Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val headerBg = if (config.isDarkTheme) Color.Black.copy(0.60f) else Color.White.copy(0.85f)
            val headerBorder = Color.White.copy(alpha = if (config.isDarkTheme) 0.20f else 0.50f)

            // Status Pill
            Box(
                modifier = Modifier
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(headerBg)
                    .border(BorderStroke(0.5.dp, headerBorder), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (config.liquidGlassEnabled) Color(0xFF00E676) else Color(0xFFFF9100))
                    )
                    GlassText(
                        text = if (config.liquidGlassEnabled) "Liquid Glass: ACTIVE" else "Liquid Glass: OFF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                }
            }

            // Theme & Inspector Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassIconButton(
                    onClick = { config = config.copy(isDarkTheme = !config.isDarkTheme) },
                    shape = CircleShape,
                    containerColor = headerBg,
                    modifier = Modifier
                        .shadow(4.dp, CircleShape)
                        .border(BorderStroke(0.5.dp, headerBorder), CircleShape)
                ) {
                    GlassIcon(
                        imageVector = if (config.isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = if (config.isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                GlassIconButton(
                    onClick = { showInspector = !showInspector },
                    shape = CircleShape,
                    containerColor = headerBg,
                    modifier = Modifier
                        .shadow(4.dp, CircleShape)
                        .border(BorderStroke(0.5.dp, headerBorder), CircleShape)
                ) {
                    GlassIcon(
                        imageVector = if (showInspector) Icons.Filled.Close else Icons.Filled.Tune,
                        contentDescription = "Tune Inspector",
                        tint = if (config.isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Tweak Inspector Overlay
        AnimatedVisibility(
            visible = showInspector,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
        ) {
            InspectorPanel(
                config = config,
                backdrop = backdrop,
                onConfigChange = { config = it },
                onClose = { showInspector = false },
            )
        }

        // Floating Bottom Liquid DockBar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            LiquidDockBar(
                selectedIndex = selectedIndex,
                onTabSelected = {
                    selectedIndex = it
                    isSettingsSelected = false
                },
                isSettingsSelected = isSettingsSelected,
                onSettingsClick = {
                    isSettingsSelected = !isSettingsSelected
                },
                backdrop = backdrop,
                config = config,
            )
        }
    }
}

@Composable
private fun InspectorPanel(
    config: DockConfig,
    backdrop: LayerBackdrop,
    onConfigChange: (DockConfig) -> Unit,
    onClose: () -> Unit,
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        isDarkTheme = config.isDarkTheme,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassText(
                    text = "Liquid Glass Inspector",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (config.isDarkTheme) Color.White else Color.Black
                )
                GlassIconButton(
                    onClick = onClose,
                    shape = CircleShape,
                ) {
                    GlassIcon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = if (config.isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Liquid Glass Toggle 1: Enable Liquid Glass
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    GlassText(
                        text = "Liquid Glass Shader",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                    GlassText(
                        text = "Authentic Kyant chromatic lens refraction",
                        fontSize = 12.sp,
                        color = if (config.isDarkTheme) Color.White.copy(0.6f) else Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                LiquidToggle(
                    selected = config.liquidGlassEnabled,
                    onSelect = { onConfigChange(config.copy(liquidGlassEnabled = it)) },
                    backdrop = backdrop,
                    accentColor = Color(0xFF34C759)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Liquid Glass Toggle 2: Chromatic Aberration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    GlassText(
                        text = "Chromatic Dispersion",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                    GlassText(
                        text = "Prismatic optical lens color fringe",
                        fontSize = 12.sp,
                        color = if (config.isDarkTheme) Color.White.copy(0.6f) else Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                LiquidToggle(
                    selected = config.chromaticAberration,
                    onSelect = { onConfigChange(config.copy(chromaticAberration = it)) },
                    backdrop = backdrop,
                    accentColor = Color(0xFF00A3FF)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Liquid Glass Toggle 3: Cut Corner Morph
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    GlassText(
                        text = "Cut-Corner Morph",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                    GlassText(
                        text = "Fluid 16dp -> 24dp settings corner morph",
                        fontSize = 12.sp,
                        color = if (config.isDarkTheme) Color.White.copy(0.6f) else Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                LiquidToggle(
                    selected = config.showCutCornerMorph,
                    onSelect = { onConfigChange(config.copy(showCutCornerMorph = it)) },
                    backdrop = backdrop,
                    accentColor = Color(0xFFAF52DE)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Background preset
            GlassText(
                text = "Backdrop Scenario",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (config.isDarkTheme) Color.White else Color.Black
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BackgroundPreset.values().forEach { preset ->
                    GlassChip(
                        selected = config.backgroundPreset == preset,
                        onClick = { onConfigChange(config.copy(backgroundPreset = preset)) },
                        label = preset.displayName,
                        isDarkTheme = config.isDarkTheme
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Blur radius
            SliderSection(
                title = "Blur Radius: ${config.blurRadiusDp.toInt()} dp",
                value = config.blurRadiusDp,
                range = 0f..32f,
                isDarkTheme = config.isDarkTheme,
                onValueChange = { onConfigChange(config.copy(blurRadiusDp = it)) }
            )

            // Lens refraction
            SliderSection(
                title = "Lens Height / Refraction: ${config.lensHeightDp.toInt()} dp",
                value = config.lensHeightDp,
                range = 0f..28f,
                isDarkTheme = config.isDarkTheme,
                onValueChange = { onConfigChange(config.copy(lensHeightDp = it)) }
            )

            // Highlight alpha
            SliderSection(
                title = "Specular Highlight: ${(config.highlightAlpha * 100).toInt()}%",
                value = config.highlightAlpha,
                range = 0f..1f,
                isDarkTheme = config.isDarkTheme,
                onValueChange = { onConfigChange(config.copy(highlightAlpha = it)) }
            )

            // Number of tabs
            Spacer(modifier = Modifier.height(6.dp))
            GlassText(
                text = "Tab Count: ${config.tabCount} tabs",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (config.isDarkTheme) Color.White else Color.Black
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(3, 4, 5).forEach { count ->
                    GlassChip(
                        selected = config.tabCount == count,
                        onClick = { onConfigChange(config.copy(tabCount = count)) },
                        label = "$count Tabs",
                        isDarkTheme = config.isDarkTheme
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderSection(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    isDarkTheme: Boolean,
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        GlassText(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDarkTheme) Color.White else Color.Black
        )
        LiquidSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            isDarkTheme = isDarkTheme,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
