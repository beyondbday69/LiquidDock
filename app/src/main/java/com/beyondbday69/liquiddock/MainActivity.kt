package com.beyondbday69.liquiddock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import com.beyondbday69.liquiddock.ui.playground.LiquidDockPlaygroundScreen
import com.beyondbday69.liquiddock.ui.theme.LiquidDockTheme
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backdrop = rememberLayerBackdrop {
                drawRect(Color.Black)
                drawContent()
            }
            LiquidDockTheme {
                LiquidDockPlaygroundScreen(backdrop = backdrop)
            }
        }
    }
}
