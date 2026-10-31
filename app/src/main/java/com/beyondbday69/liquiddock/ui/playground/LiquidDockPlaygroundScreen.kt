package com.beyondbday69.liquiddock.ui.playground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beyondbday69.liquiddock.ui.components.LiquidToggle
import com.beyondbday69.liquiddock.ui.dock.AllPlaygroundTabs
import com.beyondbday69.liquiddock.ui.dock.BackgroundPreset
import com.beyondbday69.liquiddock.ui.dock.DockConfig
import com.beyondbday69.liquiddock.ui.dock.LiquidDockBar
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop

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
            Surface(
                shape = CircleShape,
                color = if (config.isDarkTheme) Color.Black.copy(0.6f) else Color.White.copy(0.85f),
                shadowElevation = 4.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (config.liquidGlassEnabled) Color(0xFF00E676) else Color(0xFFFF9100))
                    )
                    Text(
                        text = if (config.liquidGlassEnabled) "Liquid Glass: ON" else "Standard M3: OFF",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = CircleShape,
                    color = if (config.isDarkTheme) Color.Black.copy(0.6f) else Color.White.copy(0.85f),
                    shadowElevation = 4.dp,
                ) {
                    IconButton(onClick = { config = config.copy(isDarkTheme = !config.isDarkTheme) }) {
                        Icon(
                            imageVector = if (config.isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (config.isDarkTheme) Color.White else Color.Black
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = if (config.isDarkTheme) Color.Black.copy(0.6f) else Color.White.copy(0.85f),
                    shadowElevation = 4.dp,
                ) {
                    IconButton(onClick = { showInspector = !showInspector }) {
                        Icon(
                            imageVector = if (showInspector) Icons.Filled.Close else Icons.Filled.Tune,
                            contentDescription = "Tune Inspector",
                            tint = if (config.isDarkTheme) Color.White else Color.Black
                        )
                    }
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

        // Floating Bottom DockBar
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (config.isDarkTheme) Color(0xFF1E222B).copy(alpha = 0.96f) else Color.White.copy(alpha = 0.96f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                Text(
                    text = "Dock Playground Inspector",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (config.isDarkTheme) Color.White else Color.Black
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = if (config.isDarkTheme) Color.White else Color.Black
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
                    Text(
                        text = "Liquid Glass Effect",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "Authentic Kyant refraction shader vs solid M3",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (config.isDarkTheme) Color.White.copy(0.6f) else Color.Gray
                    )
                }
                LiquidToggle(
                    selected = config.liquidGlassEnabled,
                    onSelect = { onConfigChange(config.copy(liquidGlassEnabled = it)) },
                    backdrop = backdrop,
                    accentColor = Color(0xFF34C759)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Liquid Glass Toggle 2: Chromatic Aberration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = "Chromatic Dispersion",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "Prismatic lens color fringe effect",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (config.isDarkTheme) Color.White.copy(0.6f) else Color.Gray
                    )
                }
                LiquidToggle(
                    selected = config.chromaticAberration,
                    onSelect = { onConfigChange(config.copy(chromaticAberration = it)) },
                    backdrop = backdrop,
                    accentColor = Color(0xFF00A3FF)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Liquid Glass Toggle 3: Cut Corner Morph
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = "Cut-Corner Morph",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (config.isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "Dynamic 16dp -> 24dp settings corner morph",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (config.isDarkTheme) Color.White.copy(0.6f) else Color.Gray
                    )
                }
                LiquidToggle(
                    selected = config.showCutCornerMorph,
                    onSelect = { onConfigChange(config.copy(showCutCornerMorph = it)) },
                    backdrop = backdrop,
                    accentColor = Color(0xFFAF52DE)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Background preset
            Text(
                text = "Backdrop Scenario",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (config.isDarkTheme) Color.White else Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BackgroundPreset.values().forEach { preset ->
                    FilterChip(
                        selected = config.backgroundPreset == preset,
                        onClick = { onConfigChange(config.copy(backgroundPreset = preset)) },
                        label = { Text(preset.displayName, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Blur radius
            SliderControl(
                title = "Blur Radius: ${config.blurRadiusDp.toInt()} dp",
                value = config.blurRadiusDp,
                range = 0f..32f,
                isDark = config.isDarkTheme,
                onValueChange = { onConfigChange(config.copy(blurRadiusDp = it)) }
            )

            // Lens refraction
            SliderControl(
                title = "Lens Height / Refraction: ${config.lensHeightDp.toInt()} dp",
                value = config.lensHeightDp,
                range = 0f..28f,
                isDark = config.isDarkTheme,
                onValueChange = { onConfigChange(config.copy(lensHeightDp = it)) }
            )

            // Highlight alpha
            SliderControl(
                title = "Highlight Edge Specular: ${(config.highlightAlpha * 100).toInt()}%",
                value = config.highlightAlpha,
                range = 0f..1f,
                isDark = config.isDarkTheme,
                onValueChange = { onConfigChange(config.copy(highlightAlpha = it)) }
            )

            // Number of tabs
            Text(
                text = "Tab Count: ${config.tabCount} tabs",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (config.isDarkTheme) Color.White else Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(3, 4, 5).forEach { count ->
                    FilterChip(
                        selected = config.tabCount == count,
                        onClick = { onConfigChange(config.copy(tabCount = count)) },
                        label = { Text("$count Tabs") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderControl(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    isDark: Boolean,
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (isDark) Color.White else Color.Black
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
        )
    }
}
