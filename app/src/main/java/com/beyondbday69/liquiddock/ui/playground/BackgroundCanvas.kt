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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
                    Text(
                        text = "Liquid Glass Dockbar",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (config.isDarkTheme) Color.White else Color(0xFF1A1C1E)
                    )
                    Text(
                        text = "Scroll items to watch real-time glass refraction & blur over moving content.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (config.isDarkTheme) Color.White.copy(0.70f) else Color(0xFF44474E)
                    )
                }
            }

            itemsIndexed(SampleCards) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (config.isDarkTheme) {
                            Color(0xFF1E2128).copy(alpha = 0.85f)
                        } else {
                            Color.White.copy(alpha = 0.90f)
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            Icon(
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
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (config.isDarkTheme) Color.White else Color(0xFF1A1C1E)
                            )
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (config.isDarkTheme) Color.White.copy(0.65f) else Color(0xFF6B7280)
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = item.color.copy(alpha = 0.85f)
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = if (config.isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun AnimatedMeshGradient() {
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2831855f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "phase",
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Rich animated gradient orbs
        val x1 = w * (0.3f + 0.2f * sin(phase))
        val y1 = h * (0.3f + 0.2f * cos(phase))
        val x2 = w * (0.7f + 0.2f * cos(phase * 0.8f))
        val y2 = h * (0.7f + 0.2f * sin(phase * 0.8f))
        val x3 = w * (0.5f + 0.3f * sin(phase * 1.2f))
        val y3 = h * (0.85f + 0.1f * cos(phase * 1.2f))

        drawRect(Color(0xFF0F141C))

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF8A2387).copy(alpha = 0.75f), Color.Transparent),
                center = Offset(x1, y1),
                radius = w * 0.65f
            ),
            center = Offset(x1, y1),
            radius = w * 0.65f
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFE94057).copy(alpha = 0.70f), Color.Transparent),
                center = Offset(x2, y2),
                radius = w * 0.60f
            ),
            center = Offset(x2, y2),
            radius = w * 0.60f
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFF27121).copy(alpha = 0.65f), Color.Transparent),
                center = Offset(x3, y3),
                radius = w * 0.70f
            ),
            center = Offset(x3, y3),
            radius = w * 0.70f
        )
    }
}

@Composable
private fun ColorGridBackdrop() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val colors = listOf(
            Color(0xFF1E3C72),
            Color(0xFF2A5298),
            Color(0xFF7B1FA2),
            Color(0xFF303F9F),
        )
        drawRect(
            brush = Brush.verticalGradient(colors)
        )
    }
}

@Composable
private fun NeonFlowBackdrop() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0A0E17),
                    Color(0xFF132238),
                    Color(0xFF0D324D),
                    Color(0xFF004D7A),
                )
            )
        )
    }
}
