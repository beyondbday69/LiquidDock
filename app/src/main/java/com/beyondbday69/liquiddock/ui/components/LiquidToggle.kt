package com.beyondbday69.liquiddock.ui.components

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow

@Composable
fun LiquidToggle(
    selected: Boolean,
    onSelect: (Boolean) -> Unit,
    backdrop: Backdrop?,
    modifier: Modifier = Modifier,
    accentColor: Color = if (!isSystemInDarkTheme()) Color(0xFF34C759) else Color(0xFF30D158),
    enabled: Boolean = true,
) {
    val isDark = isSystemInDarkTheme()
    val trackUnchecked = if (isDark) Color(0xFF3A3D44) else Color(0xFFE2E4E9)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val fraction by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.75f),
        label = "toggleFraction"
    )

    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 1.22f else 1.0f,
        animationSpec = spring(stiffness = 600f, dampingRatio = 0.6f),
        label = "togglePressScale"
    )

    val currentTrackColor by animateColorAsState(
        targetValue = lerp(trackUnchecked, accentColor, fraction),
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.75f),
        label = "toggleTrackColor"
    )

    val canUseGlass = backdrop != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // Switch container: 58dp wide x 32dp high
    Box(
        modifier = modifier
            .width(58.dp)
            .height(32.dp)
            .clip(CircleShape)
            .background(currentTrackColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
            ) { onSelect(!selected) },
        contentAlignment = Alignment.CenterStart
    ) {
        val travelDistance = 26.dp // 58dp - 32dp thumb
        val thumbOffset = travelDistance * fraction

        val thumbModifier = if (canUseGlass && backdrop != null) {
            Modifier
                .padding(start = 3.dp)
                .graphicsLayer {
                    translationX = thumbOffset.toPx()
                    scaleX = pressScale
                    scaleY = pressScale
                }
                .size(26.dp)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { CircleShape },
                    effects = {
                        vibrancy()
                        blur(6.dp.toPx())
                        lens(4.dp.toPx(), 8.dp.toPx(), chromaticAberration = true)
                    },
                    highlight = { Highlight.Default.copy(alpha = 0.75f) },
                    shadow = { Shadow(radius = 4.dp, color = Color.Black.copy(alpha = 0.20f)) },
                    innerShadow = { InnerShadow(radius = 3.dp, color = Color.White.copy(alpha = 0.35f)) },
                    onDrawSurface = {
                        drawRect(Color.White.copy(alpha = 0.88f))
                    }
                )
                .clip(CircleShape)
        } else {
            Modifier
                .padding(start = 3.dp)
                .graphicsLayer {
                    translationX = thumbOffset.toPx()
                    scaleX = pressScale
                    scaleY = pressScale
                }
                .size(26.dp)
                .shadow(elevation = 3.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Color.White)
        }

        Box(modifier = thumbModifier)
    }
}
