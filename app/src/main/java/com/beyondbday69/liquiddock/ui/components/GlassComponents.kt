package com.beyondbday69.liquiddock.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pure Liquid Glass UI Components (100% Free of Material 3).
 * Inspired by authentic Apple Liquid Glass and Kyant's glassmorphism shaders.
 */

@Composable
fun GlassText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = letterSpacing,
            textAlign = textAlign ?: TextAlign.Start,
        ),
        maxLines = maxLines,
        overflow = overflow,
        softWrap = softWrap,
    )
}

@Composable
fun GlassIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    Image(
        imageVector = imageVector,
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(tint),
        modifier = modifier,
    )
}

@Composable
fun GlassIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    containerColor: Color = Color.Transparent,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = if (isPressed) 0.90f else 1.0f

    Box(
        modifier = modifier
            .size(40.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(shape)
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    isDarkTheme: Boolean = true,
    borderAlpha: Float = if (isDarkTheme) 0.25f else 0.50f,
    content: @Composable () -> Unit,
) {
    val backgroundColor = if (isDarkTheme) {
        Color(0xFF16181D).copy(alpha = 0.88f)
    } else {
        Color(0xFFFFFFFF).copy(alpha = 0.92f)
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = if (isDarkTheme) 0.35f else 0.08f),
                spotColor = Color.Black.copy(alpha = if (isDarkTheme) 0.35f else 0.12f),
            )
            .clip(shape)
            .background(backgroundColor)
            .border(
                BorderStroke(0.5.dp, Color.White.copy(alpha = borderAlpha)),
                shape,
            ),
    ) {
        content()
    }
}

@Composable
fun GlassChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    isDarkTheme: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val targetBgColor = when {
        selected -> if (isDarkTheme) Color.White.copy(alpha = 0.24f) else Color(0xFF007AFF).copy(alpha = 0.18f)
        else -> if (isDarkTheme) Color.White.copy(alpha = 0.06f) else Color.Black.copy(alpha = 0.05f)
    }

    val targetTextColor = when {
        selected -> if (isDarkTheme) Color.White else Color(0xFF007AFF)
        else -> if (isDarkTheme) Color.White.copy(alpha = 0.70f) else Color(0xFF1C1C1E).copy(alpha = 0.75f)
    }

    val targetBorderColor = when {
        selected -> if (isDarkTheme) Color.White.copy(alpha = 0.55f) else Color(0xFF007AFF).copy(alpha = 0.40f)
        else -> if (isDarkTheme) Color.White.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.08f)
    }

    val bgColor by animateColorAsState(targetBgColor, animationSpec = spring(stiffness = 500f, dampingRatio = 0.8f))
    val textColor by animateColorAsState(targetTextColor, animationSpec = spring(stiffness = 500f, dampingRatio = 0.8f))
    val borderColor by animateColorAsState(targetBorderColor, animationSpec = spring(stiffness = 500f, dampingRatio = 0.8f))

    val shape = CircleShape

    Box(
        modifier = modifier
            .clip(shape)
            .background(bgColor)
            .border(BorderStroke(0.5.dp, borderColor), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        GlassText(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        )
    }
}
