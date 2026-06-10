package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ConfettiView
import com.example.ui.components.GlowButton
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun MathRiddlesScreen(
    onNavigateBack: () -> Unit,
    onShowComingSoon: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isHintDialogVisible by remember { mutableStateOf(false) }
    var isSuccessDialogVisible by remember { mutableStateOf(false) }
    var isGridDialogVisible by remember { mutableStateOf(false) }

    var userAnswerInput by remember { mutableStateOf("") }
    var isConfettiActive by remember { mutableStateOf(false) }

    // Screen error shake animation offset
    val shakeTranslationX = remember { Animatable(0f) }
    var isWrongAnswerFlag by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    // Error shake animation sequence
    LaunchedEffect(isWrongAnswerFlag) {
        if (isWrongAnswerFlag) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            shakeTranslationX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 350
                    -25f at 50 with LinearEasing
                    25f at 100 with LinearEasing
                    -15f at 150 with LinearEasing
                    15f at 200 with LinearEasing
                    -8f at 250 with LinearEasing
                    8f at 300 with LinearEasing
                }
            )
            isWrongAnswerFlag = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF030305),
                        Color(0xFF101018),
                        Color(0xFF020203)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TOP BAR SECTION
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1C1C24))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back to home",
                        tint = Color.White
                    )
                }

                // Level Label
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "LEVEL 1",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 3.sp
                        )
                    )
                    Text(
                        text = "MATH PRACTICE",
                        color = Color(0xFF00FFCC).copy(alpha = 0.5f),
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    )
                }

                // Grid Button on right
                IconButton(
                    onClick = { isGridDialogVisible = true },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1C1C24))
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = "Riddle Grid Levels",
                        tint = Color(0xFF00FFCC)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // MAIN PUZZLE CONTAINER CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.3f)
                    .padding(horizontal = 24.dp)
                    .offset { IntOffset(shakeTranslationX.value.roundToInt(), 0) }
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF13131B))
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF00FFCC).copy(alpha = if (isWrongAnswerFlag) 0.8f else 0.25f),
                                Color(0xFF0088FF).copy(alpha = if (isWrongAnswerFlag) 0.8f else 0.1f),
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .drawBehind {
                        // Card atmospheric overlay
                        drawCircle(
                            color = if (isWrongAnswerFlag) Color(0xFFFF0055).copy(alpha = 0.05f) else Color(0xFF00FFCC).copy(alpha = 0.03f),
                            radius = size.width * 0.4f,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Question instructions
                    Text(
                        text = "FILL IN THE MISSING NUMBER IN THE PATTERN",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    // Large Riddle sequence Text centered "4, 8, 16, ?"
                    Text(
                        text = "4, 8, 16, ?",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            fontSize = 42.sp,
                            letterSpacing = 2.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    // Sparkle line
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(2.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF00FFCC),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ANSWER INPUT KEYBOARD SYSTEM AT BOTTOM
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0A0A0E))
                    .border(1.dp, Color(0xFF1E1E28).copy(alpha = 0.8f))
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // TOP ROW: Answer Field, Hint Icon, Clear Button, ENTER Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Answer Display Field Container
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF14141E))
                            .border(1.dp, Color(0xFF00FFCC).copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (userAnswerInput.isEmpty()) {
                            Text(
                                text = "ENTER ANSWER...",
                                color = Color.White.copy(alpha = 0.25f),
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        } else {
                            Text(
                                text = userAnswerInput,
                                color = Color(0xFF00FFCC),
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    // HINT Button with Bulb icon
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1C132E))
                            .border(1.dp, Color(0xFFBD00FF).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable {
                                isHintDialogVisible = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Hint",
                            tint = Color(0xFFBD00FF),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // CLEAR / DELETE button
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF221115))
                            .border(1.dp, Color(0xFFFF0055).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (userAnswerInput.isNotEmpty()) {
                                    userAnswerInput = userAnswerInput.dropLast(1)
                                }
                            }
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "DEL",
                            color = Color(0xFFFF0055),
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    // ENTER button
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF052B1E))
                            .border(1.dp, Color(0xFF00FFCC).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                // Verification Logic
                                if (userAnswerInput == "32") {
                                    isConfettiActive = true
                                    isSuccessDialogVisible = true
                                } else {
                                    isWrongAnswerFlag = true
                                }
                            }
                            .padding(horizontal = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ENTER",
                            color = Color(0xFF00FFCC),
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // CUSTOM KEYBOARD ROW 1: 1 2 3 4 5
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        val number = (index + 1).toString()
                        CustomKeyField(
                            label = number,
                            onClick = {
                                if (userAnswerInput.length < 5) userAnswerInput += number
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // CUSTOM KEYBOARD ROW 2: 6 7 8 9 0
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        val number = if (index == 4) "0" else (index + 6).toString()
                        CustomKeyField(
                            label = number,
                            onClick = {
                                if (userAnswerInput.length < 5) userAnswerInput += number
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // CONFETTI SYSTEM LAYER
        ConfettiView(active = isConfettiActive)
    }

    // DIAGLOGS & SNACKBARS
    // 1. Success Answer Dialog
    if (isSuccessDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                isSuccessDialogVisible = false
                isConfettiActive = false
            },
            title = {
                Text(
                    text = "CORRECT ANSWER!",
                    color = Color(0xFF00FFCC),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Incredible! 32 is the correct term. The sequence doubles each step (4, 8, 16, 32).",
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    GlowButton(
                        text = "NEXT LEVEL COMING SOON",
                        onClick = {
                            isSuccessDialogVisible = false
                            isConfettiActive = false
                        },
                        glowColor = Color(0xFF00FFCC),
                        corners = 8.dp
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFF13131C),
            modifier = Modifier.border(1.dp, Color(0xFF00FFCC).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        )
    }

    // 2. Hint Dialog
    if (isHintDialogVisible) {
        AlertDialog(
            onDismissRequest = { isHintDialogVisible = false },
            title = {
                Text(
                    text = "HINT - LEVEL 1",
                    color = Color(0xFFBD00FF),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Pattern: Multiply by 2\n\n4 × 2 = 8\n8 × 2 = 16\n16 × 2 = ...?",
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = { isHintDialogVisible = false }) {
                    Text(
                        text = "UNDERSTOOD",
                        color = Color(0xFFBD00FF),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFF140F22),
            modifier = Modifier.border(1.dp, Color(0xFFBD00FF).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        )
    }

    // 3. Grid Level Dialog
    if (isGridDialogVisible) {
        AlertDialog(
            onDismissRequest = { isGridDialogVisible = false },
            title = {
                Text(
                    text = "RIDDLES GRID",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Practice Levels List",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Level 1 active
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1C1C28))
                                .border(1.dp, Color(0xFF00FFCC), RoundedCornerShape(8.dp))
                                .clickable { isGridDialogVisible = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("1", color = Color(0xFF00FFCC), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }

                        // Locked levels
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x501C1C28))
                                    .clickable { onShowComingSoon("Level ${index + 2}") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 2).toString(),
                                    color = Color.DarkGray,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { isGridDialogVisible = false }) {
                    Text("CLOSE", color = Color.White, fontFamily = FontFamily.Monospace)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFF121217),
            modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun CustomKeyField(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.9f else 1f, label = "keyScale")
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF13131A))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .border(1.dp, Color(0xFF1E1E28), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 1.sp
            )
        )
    }
}
