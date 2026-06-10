package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MathRiddlesScreen
import com.example.ui.screens.PdfScannerScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

enum class ActiveScreen {
    HOME,
    MATH_RIDDLES,
    PDF_SCANNER
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainAppContainer() {
    var activeScreen by remember { mutableStateOf(ActiveScreen.HOME) }
    var comingSoonItemName by remember { mutableStateOf<String?>(null) }

    // Auto-dismiss soon alert duration timer
    LaunchedEffect(comingSoonItemName) {
        if (comingSoonItemName != null) {
            delay(2000)
            comingSoonItemName = null
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF030305)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen router with smooth horizontal transitions
            AnimatedContent(
                targetState = activeScreen,
                transitionSpec = {
                    if (targetState == ActiveScreen.HOME) {
                        // Slide back left
                        (slideInHorizontally(animationSpec = tween(350)) { width -> -width } + fadeIn(animationSpec = tween(200)))
                            .with(slideOutHorizontally(animationSpec = tween(350)) { width -> width } + fadeOut(animationSpec = tween(200)))
                    } else {
                        // Slide forward right
                        (slideInHorizontally(animationSpec = tween(350)) { width -> width } + fadeIn(animationSpec = tween(200)))
                            .with(slideOutHorizontally(animationSpec = tween(350)) { width -> -width } + fadeOut(animationSpec = tween(200)))
                    }
                },
                label = "screenCycle"
            ) { targetState ->
                when (targetState) {
                    ActiveScreen.HOME -> {
                        HomeScreen(
                            onNavigateToMathRiddles = { activeScreen = ActiveScreen.MATH_RIDDLES },
                            onNavigateToPdfScanner = { activeScreen = ActiveScreen.PDF_SCANNER },
                            onShowComingSoon = { comingSoonItemName = it }
                        )
                    }
                    ActiveScreen.MATH_RIDDLES -> {
                        MathRiddlesScreen(
                            onNavigateBack = { activeScreen = ActiveScreen.HOME },
                            onShowComingSoon = { comingSoonItemName = it }
                        )
                    }
                    ActiveScreen.PDF_SCANNER -> {
                        PdfScannerScreen(
                            onNavigateBack = { activeScreen = ActiveScreen.HOME }
                        )
                    }
                }
            }

            // NEON "COMING SOON" SLIDE-DOWN HEADS-UP NOTIFICATION ALERT HUD overlay
            AnimatedVisibility(
                visible = comingSoonItemName != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF140F11))
                            .border(1.dp, Color(0xFFFF0055).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alert logo",
                            tint = Color(0xFFFF0055),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "COMING SOON: ${comingSoonItemName?.uppercase()}",
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
