package com.beyondbday69.liquiddock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beyondbday69.liquiddock.ui.playground.LiquidDockPlaygroundScreen
import com.beyondbday69.liquiddock.ui.theme.LiquidDockTheme
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

class MainActivity : ComponentActivity() {
    private var crashLog by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            crashLog = "${throwable.javaClass.simpleName}: ${throwable.message}\n\n" +
                    throwable.stackTrace.take(15).joinToString("\n") { "  at $it" }
        }

        enableEdgeToEdge()
        setContent {
            LiquidDockTheme {
                if (crashLog != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1E1E1E))
                            .padding(24.dp)
                    ) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            BasicText(
                                text = "Application Error",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF5252)
                                )
                            )
                            BasicText(
                                text = crashLog ?: "",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                ),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                } else {
                    val backdrop = rememberLayerBackdrop {
                        drawRect(Color.Black)
                        drawContent()
                    }
                    LiquidDockPlaygroundScreen(backdrop = backdrop)
                }
            }
        }
    }
}
