package com.beyondbday69.liquiddock.ui.components

import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import com.kyant.shapes.Capsule
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Authentic Liquid Glass Slider from Kyant Backdrop:
 * https://kyant.gitbook.io/backdrop/tutorials/glass-slider
 *
 * Fully interactive: tap anywhere or drag smoothly across the entire track.
 * Refracts both the underlying content backdrop and the track layer backdrop,
 * with elastic squash deformation, velocity tracking, and chromatic lens dispersion.
 */
@Composable
fun LiquidSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF007AFF),
    isDarkTheme: Boolean = true,
) {
    val isLightTheme = !isDarkTheme
    val trackBgColor = if (isLightTheme) {
        Color(0xFF787878).copy(alpha = 0.22f)
    } else {
        Color(0xFF787880).copy(alpha = 0.36f)
    }

    val span = (valueRange.endInclusive - valueRange.start).coerceAtLeast(0.0001f)
    val fraction = ((value - valueRange.start) / span).coerceIn(0f, 1f)

    var isPressed by remember { mutableStateOf(false) }
    var dragVelocity by remember { mutableFloatStateOf(0f) }

    val pressProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = spring(stiffness = 600f, dampingRatio = 0.65f),
        label = "sliderPressProgress",
    )

    val thumbScale by animateFloatAsState(
        targetValue = if (isPressed) 1.30f else 1.0f,
        animationSpec = spring(stiffness = 600f, dampingRatio = 0.65f),
        label = "sliderThumbScale",
    )

    val velocityAnim = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val trackBackdrop = rememberLayerBackdrop()
    val canUseGlass = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    val thumbWidth = 40.dp
    val thumbHeight = 24.dp

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .pointerInput(valueRange, span) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val widthPx = size.width.toFloat()
                    val thumbWidthPx = thumbWidth.toPx()
                    val travelPx = (widthPx - thumbWidthPx).coerceAtLeast(1f)

                    // Touch down: snap thumb to finger position immediately
                    val initialOffset = down.position.x - (thumbWidthPx / 2f)
                    val initialFraction = (initialOffset / travelPx).coerceIn(0f, 1f)
                    onValueChange(valueRange.start + initialFraction * span)
                    isPressed = true
                    down.consume()

                    val pointerId = down.id
                    var lastX = down.position.x
                    var lastTime = System.currentTimeMillis()

                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == pointerId } ?: break
                        if (change.changedToUpIgnoreConsumed()) {
                            change.consume()
                            break
                        }
                        if (change.pressed) {
                            val now = System.currentTimeMillis()
                            val dt = (now - lastTime).coerceAtLeast(1)
                            val deltaX = change.position.x - lastX

                            val instantVel = (deltaX / dt.toFloat()) * 10f
                            dragVelocity = instantVel.coerceIn(-20f, 20f)
                            lastX = change.position.x
                            lastTime = now

                            val currentOffset = change.position.x - (thumbWidthPx / 2f)
                            val currentFraction = (currentOffset / travelPx).coerceIn(0f, 1f)
                            onValueChange(valueRange.start + currentFraction * span)
                            change.consume()
                        }
                    }

                    isPressed = false
                    dragVelocity = 0f
                    coroutineScope.launch {
                        velocityAnim.animateTo(0f, spring(stiffness = 400f, dampingRatio = 0.7f))
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val density = LocalDensity.current
        val totalWidthPx = constraints.maxWidth.toFloat()
        val thumbWidthPx = with(density) { thumbWidth.toPx() }
        val travelDistancePx = (totalWidthPx - thumbWidthPx).coerceAtLeast(0f)
        val thumbOffsetPx = fraction * travelDistancePx

        // --- Track with Layer Backdrop ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .layerBackdrop(trackBackdrop)
        ) {
            // Unfilled Base Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(Capsule())
                    .background(trackBgColor)
            )

            // Filled Accent Track
            val filledWidthDp = with(density) {
                (thumbOffsetPx + (thumbWidthPx / 2f)).toDp()
            }
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .size(width = filledWidthDp, height = 6.dp)
                    .clip(Capsule())
                    .background(accentColor)
            )
        }

        // --- Authentic Liquid Glass Thumb ---
        val velocitySquashX = 1f + (dragVelocity * 0.015f).coerceIn(-0.25f, 0.25f)
        val velocitySquashY = 1f - (dragVelocity * 0.008f).coerceIn(-0.25f, 0.25f)

        val thumbBaseModifier = if (canUseGlass) {
            Modifier
                .offset { IntOffset(thumbOffsetPx.roundToInt(), 0) }
                .graphicsLayer {
                    scaleX = thumbScale * velocitySquashX
                    scaleY = thumbScale * velocitySquashY
                }
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(
                        backdrop,
                        rememberBackdrop(trackBackdrop) { drawBackdrop ->
                            val scaleX = lerp(2f / 3f, 1f, pressProgress)
                            val scaleY = lerp(0f, 1f, pressProgress)
                            scale(scaleX, scaleY) {
                                drawBackdrop()
                            }
                        }
                    ),
                    shape = { Capsule() },
                    effects = {
                        blur(8.dp.toPx() * (1f - pressProgress * 0.4f))
                        lens(
                            10.dp.toPx() * pressProgress,
                            14.dp.toPx() * pressProgress,
                            chromaticAberration = true
                        )
                    },
                    highlight = {
                        Highlight.Ambient.copy(
                            width = Highlight.Ambient.width / 1.5f,
                            blurRadius = Highlight.Ambient.blurRadius / 1.5f,
                            alpha = (pressProgress * 0.8f).coerceIn(0.2f, 1.0f)
                        )
                    },
                    shadow = {
                        Shadow(
                            radius = 6.dp,
                            color = Color.Black.copy(alpha = 0.20f)
                        )
                    },
                    innerShadow = {
                        InnerShadow(
                            radius = 4.dp * pressProgress,
                            alpha = pressProgress
                        )
                    },
                    onDrawSurface = {
                        drawRect(
                            Color.White.copy(alpha = (1f - pressProgress * 0.65f).coerceIn(0.20f, 0.95f))
                        )
                    }
                )
                .clip(Capsule())
                .border(
                    BorderStroke(
                        0.5.dp,
                        Color.White.copy(alpha = if (isDarkTheme) 0.65f else 0.85f)
                    ),
                    Capsule()
                )
                .size(thumbWidth, thumbHeight)
        } else {
            Modifier
                .offset { IntOffset(thumbOffsetPx.roundToInt(), 0) }
                .graphicsLayer {
                    scaleX = thumbScale * velocitySquashX
                    scaleY = thumbScale * velocitySquashY
                }
                .shadow(
                    elevation = if (isPressed) 6.dp else 2.dp,
                    shape = Capsule(),
                    ambientColor = Color.Black.copy(alpha = 0.20f),
                    spotColor = Color.Black.copy(alpha = 0.25f),
                )
                .clip(Capsule())
                .background(Color.White)
                .border(BorderStroke(0.5.dp, Color.White), Capsule())
                .size(thumbWidth, thumbHeight)
        }

        Box(modifier = thumbBaseModifier)
    }
}
