package com.beyondbday69.liquiddock.ui.playground

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beyondbday69.liquiddock.ui.components.GlassCard
import com.beyondbday69.liquiddock.ui.components.GlassIcon
import com.beyondbday69.liquiddock.ui.components.GlassIconButton
import com.beyondbday69.liquiddock.ui.components.GlassText
import com.beyondbday69.liquiddock.ui.dock.BackgroundPreset
import com.beyondbday69.liquiddock.ui.dock.DockConfig
import kotlin.math.cos
import kotlin.math.sin

data class PlaygroundCardItem(
    val title: String,
    val subtitle: String,
    val color: Color,
)

val SampleCards = listOf(
    PlaygroundCardItem("Starboy", "The Weeknd • Daft Punk", Color(0xFFE53935)),
    PlaygroundCardItem("Blinding Lights", "The Weeknd • After Hours", Color(0xFFD81B60)),
    PlaygroundCardItem("Levitating", "Dua Lipa • Future Nostalgia", Color(0xFF8E24AA)),
    PlaygroundCardItem("Midnight City", "M83 • Hurry Up, We're Dreaming", Color(0xFF3949AB)),
    PlaygroundCardItem("Save Your Tears", "The Weeknd • After Hours", Color(0xFF1E88E5)),
    PlaygroundCardItem("Get Lucky", "Daft Punk • Random Access Memories", Color(0xFF00ACC1)),
    PlaygroundCardItem("Instant Crush", "Daft Punk • Julian Casablancas", Color(0xFF43A047)),
    PlaygroundCardItem("Something About Us", "Daft Punk • Discovery", Color(0xFFFB8C00)),
    PlaygroundCardItem("One More Time", "Daft Punk • Discovery", Color(0xFFF4511E)),
    PlaygroundCardItem("Harder, Better, Faster", "Daft Punk • Discovery", Color(0xFF6D4C41)),
    PlaygroundCardItem("Around The World", "Daft Punk • Homework", Color(0xFF546E7A)),
    PlaygroundCardItem("Technologic", "Daft Punk • Human After All", Color(0xFF00897B)),
    PlaygroundCardItem("Lose Yourself to Dance", "Daft Punk • Pharrell Williams", Color(0xFF7CB342)),
    PlaygroundCardItem("Aerodynamic", "Daft Punk • Discovery", Color(0xFFC0CA33)),
    PlaygroundCardItem("Da Funk", "Daft Punk • Homework", Color(0xFFFDD835)),
)

@Composable
fun BackgroundCanvas(
    config: DockConfig,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (config.backgroundPreset) {
            BackgroundPreset.MESH_GRADIENT -> AnimatedMeshGradient()
            BackgroundPreset.ALBUM_CARDS -> ColorGridBackdrop()
            BackgroundPreset.NEON_FLOW -> NeonFlowBackdrop()
            BackgroundPreset.CLEAN_MINIMAL -> Box(
                Modifier
                    .fillMaxSize()
                    .background(if (config.isDarkTheme) Color(0xFF101216) else Color(0xFFF7F8FA))
            )
        }

        // Scrollable content that slides directly underneath the floating liquid dock!
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    GlassText(
                        text = "Liquid Glass Dockbar",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (config.isDarkTheme) Color.White else Color(0xFF1A1C1E)
                    )
                    GlassText(
                        text = "Scroll items to watch real-time glass refraction & blur over moving content.",
                        fontSize = 14.sp,
                        color = if (config.isDarkTheme) Color.White.copy(0.70f) else Color(0xFF44474E),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            itemsIndexed(SampleCards) { _, item ->
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(18.dp),
                    isDarkTheme = config.isDarkTheme,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(item.color),
                            contentAlignment = Alignment.Center,
                        ) {
                            GlassIcon(
                                imageVector = Icons.Filled.Album,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 14.dp)
                        ) {
                            GlassText(
                                text = item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (config.isDarkTheme) Color.White else Color(0xFF1A1C1E)
                            )
                            GlassText(
                                text = item.subtitle,
                                fontSize = 13.sp,
                                color = if (config.isDarkTheme) Color.White.copy(0.65f) else Color(0xFF6B7280),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        GlassIconButton(onClick = {}) {
                            GlassIcon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = item.color.copy(alpha = 0.85f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        GlassIconButton(onClick = {}) {
                            GlassIcon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = if (config.isDarkTheme) Color.White else Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedMeshGradient() {
    val transition = rememberInfiniteTransition(label = "meshAnim")
    val phase1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Restart),
        label = "phase1"
    )
    val phase2 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Restart),
        label = "phase2"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep vibrant mesh background
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0F0C29),
                    Color(0xFF302B63),
                    Color(0xFF24243E),
                )
            )
        )

        val center1 = Offset(
            w * 0.3f + cos(phase1.toDouble()).toFloat() * (w * 0.25f),
            h * 0.25f + sin(phase1.toDouble()).toFloat() * (h * 0.15f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFF2A6D).copy(alpha = 0.65f), Color.Transparent),
                center = center1,
                radius = w * 0.65f,
            ),
            center = center1,
            radius = w * 0.65f
        )

        val center2 = Offset(
            w * 0.7f + sin(phase2.toDouble()).toFloat() * (w * 0.25f),
            h * 0.65f + cos(phase2.toDouble()).toFloat() * (h * 0.2f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF05D9E8).copy(alpha = 0.60f), Color.Transparent),
                center = center2,
                radius = w * 0.7f,
            ),
            center = center2,
            radius = w * 0.7f
        )

        val center3 = Offset(
            w * 0.5f - cos((phase1 + phase2).toDouble() * 0.5).toFloat() * (w * 0.2f),
            h * 0.85f - sin((phase1 + phase2).toDouble() * 0.5).toFloat() * (h * 0.15f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF7B2CBF).copy(alpha = 0.70f), Color.Transparent),
                center = center3,
                radius = w * 0.6f,
            ),
            center = center3,
            radius = w * 0.6f
        )
    }
}

@Composable
private fun ColorGridBackdrop() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF141E30),
                    Color(0xFF243B55),
                    Color(0xFF141E30),
                )
            )
        )
    }
}

@Composable
private fun NeonFlowBackdrop() {
    val transition = rememberInfiniteTransition(label = "neonAnim")
    val sweepOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing), RepeatMode.Reverse),
        label = "sweep"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF6A11CB),
                    Color(0xFF2575FC),
                    Color(0xFF00F2FE),
                    Color(0xFF4FACFE),
                    Color(0xFF6A11CB),
                ),
                center = Offset(size.width / 2f + sweepOffset * 0.1f, size.height / 2f)
            )
        )
    }
}
