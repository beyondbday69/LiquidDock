package com.beyondbday69.liquiddock.ui.dock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow

data class PlaygroundTab(
    val title: String,
    val icon: ImageVector,
)

val AllPlaygroundTabs = listOf(
    PlaygroundTab("Home", Icons.Filled.Home),
    PlaygroundTab("Search", Icons.Filled.Search),
    PlaygroundTab("Library", Icons.Filled.LibraryMusic),
    PlaygroundTab("Mix", Icons.Filled.AutoAwesome),
    PlaygroundTab("Stats", Icons.Filled.Analytics),
)

@Composable
fun LiquidDockBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    isSettingsSelected: Boolean,
    onSettingsClick: () -> Unit,
    backdrop: LayerBackdrop?,
    config: DockConfig,
    modifier: Modifier = Modifier,
) {
    val isGlass = config.liquidGlassEnabled && backdrop != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU
    val isDarkTheme = config.isDarkTheme

    val activeTabs = remember(config.tabCount) {
        AllPlaygroundTabs.take(config.tabCount.coerceIn(3, 5))
    }

    val selectedItemWidth = when {
        activeTabs.size <= 3 -> 106.dp
        activeTabs.size == 4 -> 98.dp
        else -> 90.dp
    }

    Row(
        modifier = modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        // --- 1. Main Navigation Dock Pill (Left) ---
        val mainDockShape = RoundedCornerShape(
            topStart = 29.dp,
            bottomStart = 29.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp,
        )

        val mainDockModifier = if (isGlass) {
            Modifier
                .wrapContentWidth()
                .height(config.dockHeightDp.dp)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { mainDockShape },
                    effects = {
                        vibrancy()
                        blur(config.blurRadiusDp.dp.toPx())
                        lens(config.lensHeightDp.dp.toPx(), config.lensAmountDp.dp.toPx(), chromaticAberration = config.chromaticAberration)
                    },
                    highlight = { Highlight.Default.copy(alpha = config.highlightAlpha) },
                    shadow = {
                        Shadow(
                            radius = 16.dp,
                            color = Color.Black.copy(alpha = if (isDarkTheme) 0.35f else 0.12f),
                        )
                    },
                    innerShadow = {
                        InnerShadow(
                            radius = 8.dp,
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.25f),
                        )
                    },
                    onDrawSurface = {
                        val glassSurfaceColor = if (isDarkTheme) {
                            Color(0xFF141416).copy(alpha = 0.55f)
                        } else {
                            Color(0xFFFAFAFA).copy(alpha = 0.60f)
                        }
                        drawRect(glassSurfaceColor)
                    },
                )
                .clip(mainDockShape)
        } else {
            Modifier
                .wrapContentWidth()
                .height(config.dockHeightDp.dp)
                .clip(mainDockShape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        }

        Box(modifier = mainDockModifier) {
            Row(
                modifier = Modifier.padding(start = 8.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                activeTabs.forEachIndexed { index, tab ->
                    val isSelected = selectedIndex == index && !isSettingsSelected
                    LiquidDockItem(
                        tab = tab,
                        selected = isSelected,
                        selectedWidth = selectedItemWidth,
                        backdrop = backdrop,
                        isGlass = isGlass,
                        isDarkTheme = isDarkTheme,
                        selectedPillAlpha = config.selectedItemPillAlpha,
                        onSelect = { onTabSelected(index) },
                    )
                }
            }
        }

        // --- 2. Settings Button Pill (Right) ---
        val settingsInteractionSource = remember { MutableInteractionSource() }
        val isSettingsPressed by settingsInteractionSource.collectIsPressedAsState()
        val isSettingsHovered by settingsInteractionSource.collectIsHoveredAsState()

        val settingsLiftY by animateDpAsState(
            targetValue = when {
                isSettingsPressed -> (-2).dp
                isSettingsHovered -> (-6).dp
                isSettingsSelected -> (-2).dp
                else -> 0.dp
            },
            animationSpec = spring(stiffness = 500f, dampingRatio = 0.7f),
            label = "settingsLiftY",
        )

        val settingsScale by animateFloatAsState(
            targetValue = when {
                isSettingsPressed -> 0.94f
                isSettingsHovered -> 1.08f
                isSettingsSelected -> 1.03f
                else -> 1.0f
            },
            animationSpec = spring(stiffness = 600f, dampingRatio = 0.65f),
            label = "settingsScale",
        )

        val settingsUnselectedColor = if (isGlass) {
            if (isDarkTheme) Color.White.copy(alpha = 0.85f) else Color(0xFF1F1F1F).copy(alpha = 0.80f)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

        val settingsContentColor by animateColorAsState(
            targetValue = if (isSettingsSelected) MaterialTheme.colorScheme.onPrimaryContainer else settingsUnselectedColor,
            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
            label = "settingsContentColor",
        )

        val settingsSurfaceColor by animateColorAsState(
            targetValue = if (isSettingsSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
            label = "settingsSurfaceColor",
        )

        val settingsCutCorner by animateDpAsState(
            targetValue = if (isSettingsSelected && config.showCutCornerMorph) 24.dp else 16.dp,
            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
            label = "settingsCutCorner",
        )

        val settingsWidth by animateDpAsState(
            targetValue = if (isSettingsSelected) 62.dp else 54.dp,
            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
            label = "settingsWidth",
        )

        val gearRotation by animateFloatAsState(
            targetValue = if (isSettingsSelected) 90f else 0f,
            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
            label = "settingsGearRotation",
        )

        val gearScale by animateFloatAsState(
            targetValue = if (isSettingsSelected) 1.08f else 1.0f,
            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
            label = "settingsGearScale",
        )

        val settingsShape = RoundedCornerShape(
            topStart = settingsCutCorner,
            bottomStart = settingsCutCorner,
            topEnd = 29.dp,
            bottomEnd = 29.dp,
        )

        val glassSettingsTint = if (isSettingsSelected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = config.selectedItemPillAlpha)
        } else {
            if (isDarkTheme) Color(0xFF141416).copy(alpha = 0.55f) else Color(0xFFFAFAFA).copy(alpha = 0.60f)
        }

        val settingsModifier = if (isGlass) {
            Modifier
                .height(config.dockHeightDp.dp)
                .width(settingsWidth)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { settingsShape },
                    effects = {
                        vibrancy()
                        blur(config.blurRadiusDp.dp.toPx())
                        lens(config.lensHeightDp.dp.toPx(), config.lensAmountDp.dp.toPx(), chromaticAberration = config.chromaticAberration)
                    },
                    highlight = { Highlight.Default.copy(alpha = config.highlightAlpha) },
                    shadow = {
                        Shadow(
                            radius = 16.dp,
                            color = Color.Black.copy(alpha = if (isDarkTheme) 0.35f else 0.12f),
                        )
                    },
                    innerShadow = {
                        InnerShadow(
                            radius = 8.dp,
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.25f),
                        )
                    },
                    onDrawSurface = {
                        drawRect(glassSettingsTint)
                    },
                )
                .clip(settingsShape)
                .then(
                    if (isSettingsSelected) {
                        Modifier.border(
                            BorderStroke(0.5.dp, Color.White.copy(alpha = if (isDarkTheme) 0.30f else 0.50f)),
                            settingsShape,
                        )
                    } else {
                        Modifier
                    }
                )
        } else {
            Modifier
                .height(config.dockHeightDp.dp)
                .width(settingsWidth)
                .clip(settingsShape)
                .background(settingsSurfaceColor)
        }

        Box(
            modifier = settingsModifier
                .hoverable(interactionSource = settingsInteractionSource)
                .graphicsLayer {
                    translationY = settingsLiftY.toPx()
                    scaleX = settingsScale
                    scaleY = settingsScale
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = settingsInteractionSource,
                        indication = null,
                    ) { onSettingsClick() },
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(LocalContentColor provides settingsContentColor) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer {
                                rotationZ = gearRotation
                                scaleX = gearScale
                                scaleY = gearScale
                            },
                    )
                }
            }
        }
    }
}

@Composable
private fun LiquidDockItem(
    tab: PlaygroundTab,
    selected: Boolean,
    selectedWidth: Dp,
    backdrop: LayerBackdrop?,
    isGlass: Boolean,
    isDarkTheme: Boolean,
    selectedPillAlpha: Float,
    onSelect: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Hover lift & elevation: lifts up smoothly (-6dp) on hover or select
    val liftY by animateDpAsState(
        targetValue = when {
            isPressed -> (-2).dp
            isHovered -> (-6).dp
            selected -> (-2).dp
            else -> 0.dp
        },
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.7f),
        label = "dockItemLiftY",
    )

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.94f
            isHovered -> 1.08f
            selected -> 1.03f
            else -> 1.0f
        },
        animationSpec = spring(stiffness = 600f, dampingRatio = 0.65f),
        label = "dockItemScale",
    )

    val unselectedContentColor = if (isGlass) {
        if (isDarkTheme) Color.White.copy(alpha = 0.85f) else Color(0xFF1F1F1F).copy(alpha = 0.80f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else unselectedContentColor,
        animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
        label = "dockContentColor",
    )

    val pillColor = if (isGlass) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = selectedPillAlpha)
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    val pillBackgroundColor by animateColorAsState(
        targetValue = if (selected) pillColor else Color.Transparent,
        animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
        label = "dockPillBackgroundColor",
    )

    val itemWidth by animateDpAsState(
        targetValue = if (selected) selectedWidth else 44.dp,
        animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
        label = "dockItemWidth",
    )

    val itemShape = CircleShape

    // Selected item is an authentic Liquid Glass lens!
    val itemBaseModifier = if (isGlass && backdrop != null && selected) {
        Modifier
            .height(44.dp)
            .width(itemWidth)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { itemShape },
                effects = {
                    vibrancy()
                    blur(8.dp.toPx())
                    lens(10.dp.toPx(), 16.dp.toPx(), chromaticAberration = true)
                },
                highlight = { Highlight.Default.copy(alpha = 0.85f) },
                shadow = { Shadow(radius = 8.dp, color = Color.Black.copy(alpha = 0.25f)) },
                innerShadow = { InnerShadow(radius = 4.dp, color = Color.White.copy(alpha = 0.45f)) },
                onDrawSurface = {
                    drawRect(MaterialTheme.colorScheme.primaryContainer.copy(alpha = selectedPillAlpha))
                }
            )
            .clip(itemShape)
            .border(
                BorderStroke(0.5.dp, Color.White.copy(alpha = if (isDarkTheme) 0.50f else 0.70f)),
                itemShape
            )
    } else if (isGlass && isHovered && !selected) {
        Modifier
            .height(44.dp)
            .width(itemWidth)
            .clip(itemShape)
            .background(Color.White.copy(alpha = if (isDarkTheme) 0.12f else 0.20f))
            .border(
                BorderStroke(0.5.dp, Color.White.copy(alpha = if (isDarkTheme) 0.30f else 0.45f)),
                itemShape
            )
    } else {
        val itemBorder = if (isGlass && selected) {
            BorderStroke(0.5.dp, Color.White.copy(alpha = if (isDarkTheme) 0.35f else 0.50f))
        } else {
            null
        }
        Modifier
            .height(44.dp)
            .width(itemWidth)
            .clip(itemShape)
            .background(pillBackgroundColor)
            .then(
                if (itemBorder != null) {
                    Modifier.border(itemBorder, itemShape)
                } else {
                    Modifier
                }
            )
    }

    Box(
        modifier = itemBaseModifier
            .hoverable(interactionSource = interactionSource)
            .graphicsLayer {
                translationY = liftY.toPx()
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) { onSelect() },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.title,
                    modifier = Modifier.size(24.dp),
                )
            }
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f)) +
                        expandHorizontally(
                            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
                            expandFrom = Alignment.Start,
                            clip = true,
                        ),
                exit = fadeOut(animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f)) +
                        shrinkHorizontally(
                            animationSpec = spring(stiffness = 380f, dampingRatio = 0.8f),
                            shrinkTowards = Alignment.Start,
                            clip = true,
                        ),
            ) {
                Text(
                    text = tab.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}
