package com.beyondbday69.liquiddock.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * Pure Liquid Glass Slider with tactile elastic thumb and luminous glass track.
 * 100% Free of Material 3.
 */
@Composable
fun LiquidSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    isDarkTheme: Boolean = true,
    accentColor: Color = Color(0xFF007AFF),
    modifier: Modifier = Modifier,
) {
    var isDragging by remember { mutableStateOf(false) }

    val thumbScale by animateFloatAsState(
        targetValue = if (isDragging) 1.25f else 1.0f,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.65f),
        label = "thumbScale",
    )

    val span = (valueRange.endInclusive - valueRange.start).coerceAtLeast(0.0001f)
    val fraction = ((value - valueRange.start) / span).coerceIn(0f, 1f)

    val trackHeight = 8.dp
    val thumbSize = 24.dp

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .pointerInput(valueRange, span) {
                detectTapGestures { offset ->
                    val newFraction = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                    val newValue = valueRange.start + newFraction * span
                    onValueChange(newValue)
                }
            }
            .pointerInput(valueRange, span) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newFraction = (fraction + dragAmount.x / size.width.toFloat()).coerceIn(0f, 1f)
                        val newValue = valueRange.start + newFraction * span
                        onValueChange(newValue)
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val density = LocalDensity.current
        val totalWidthPx = constraints.maxWidth.toFloat()
        val thumbRadiusPx = with(density) { (thumbSize / 2).toPx() }
        val effectiveWidthPx = (totalWidthPx - 2 * thumbRadiusPx).coerceAtLeast(1f)
        val thumbOffsetDp = with(density) { (fraction * effectiveWidthPx).toDp() }

        // Track Background
        val trackBg = if (isDarkTheme) {
            Color.White.copy(alpha = 0.15f)
        } else {
            Color.Black.copy(alpha = 0.10f)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(CircleShape)
                .background(trackBg)
                .border(
                    BorderStroke(0.5.dp, Color.White.copy(alpha = if (isDarkTheme) 0.15f else 0.30f)),
                    CircleShape
                )
        ) {
            // Filled Active Track
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(trackHeight)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                accentColor.copy(alpha = 0.75f),
                                accentColor
                            )
                        )
                    )
            )
        }

        // Floating Liquid Glass Thumb
        val thumbShape = CircleShape
        val thumbColor = if (isDarkTheme) {
            Color.White.copy(alpha = 0.95f)
        } else {
            Color.White
        }

        Box(
            modifier = Modifier
                .offset(x = thumbOffsetDp)
                .size(thumbSize)
                .graphicsLayer {
                    scaleX = thumbScale
                    scaleY = thumbScale
                }
                .shadow(
                    elevation = if (isDragging) 8.dp else 4.dp,
                    shape = thumbShape,
                    ambientColor = Color.Black.copy(alpha = 0.25f),
                    spotColor = Color.Black.copy(alpha = 0.35f),
                )
                .clip(thumbShape)
                .background(thumbColor)
                .border(
                    BorderStroke(1.dp, Color.White),
                    thumbShape
                )
        )
    }
}
