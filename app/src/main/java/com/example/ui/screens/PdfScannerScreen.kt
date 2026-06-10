package com.example.ui.screens

import kotlin.math.roundToInt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GKQuestion
import com.example.model.sampleGKQuestions
import com.example.ui.components.GlowButton
import kotlinx.coroutines.delay

@Composable
fun PdfScannerScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isScanningMode by remember { mutableStateOf(false) }
    var isScannedCompleted by remember { mutableStateOf(false) }
    var scanProgress by remember { mutableStateOf(0f) }
    var scanStatusMessage by remember { mutableStateOf("READY TO EXTRACT") }

    // Active Q&A practicing states
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userSelectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isCorrectSelected by remember { mutableStateOf(false) }
    var playerScore by remember { mutableStateOf(0) }
    var finishedGKQuestions by remember { mutableStateOf(setOf<Int>()) }

    val haptic = LocalHapticFeedback.current

    val currentQuestion = sampleGKQuestions[currentQuestionIndex]

    // Scan Simulation Animation Effects
    LaunchedEffect(isScanningMode) {
        if (isScanningMode) {
            scanStatusMessage = "ESTABLISHING PDF LINK..."
            delay(600)
            scanStatusMessage = "SCANNING OCR CHUNKS..."
            while (scanProgress < 1.0f) {
                scanProgress += 0.05f
                delay(80)
                if (scanProgress > 0.40f && scanProgress < 0.70f) {
                    scanStatusMessage = "PARSING BENGALI CHARACTERS..."
                } else if (scanProgress >= 0.70f) {
                    scanStatusMessage = "GENERATING INTERACTIVE Q&A..."
                }
            }
            scanStatusMessage = "SUCCESSFULLY IMPORTED 100 Q&A!"
            delay(1200)
            isScanningMode = false
            isScannedCompleted = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF040206),
                        Color(0xFF0C0712),
                        Color(0xFF020104)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF161022))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Header Labels
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "C Q&A PDF EXAMINER",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 1.5.sp
                        )
                    )
                    Text(
                        text = "WEST BENGAL G.K. INTERACTIVE",
                        color = Color(0xFFBD00FF).copy(alpha = 0.6f),
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.size(48.dp))
            }

            if (!isScannedCompleted && !isScanningMode) {
                // SCREEN 1: UPLOADED PDF LAUNCH STATE
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Document Icon with Neon pulsing halo
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF140F24).copy(alpha = 0.6f))
                            .border(1.5.dp, Color(0xFFBD00FF).copy(alpha = 0.4f), CircleShape)
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "PDF document logo",
                            tint = Color(0xFFBD00FF),
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "UPLOADED PDF DETECTED",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "West_Bengal_GK_100_QnA.pdf",
                        color = Color(0xFF00FFCC),
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "This file contains 100 Bengali GK multiple-choice questions regarding West Bengal. Click below to simulate a high-speed scanner extraction and convert them into an interactive exam.",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(44.dp))

                    GlowButton(
                        text = "EXTRACT & SCAN PDF",
                        onClick = { isScanningMode = true },
                        glowColor = Color(0xFFBD00FF),
                        borderColor = Color(0xFFBD00FF).copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (isScanningMode) {
                // SCREEN 2: SCANNING LASER AND PROGRESS BAR SIMULATOR
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0F0B1E))
                            .border(1.dp, Color(0xFFBD00FF).copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Drawing moving scanning laser bar
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cached,
                                contentDescription = "Active scanning",
                                tint = Color(0xFFBD00FF),
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = scanStatusMessage,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // Progress indicators
                    LinearProgressIndicator(
                        progress = { scanProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = Color(0xFFBD00FF),
                        trackColor = Color(0xFF18122C)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${(scanProgress * 100).roundToInt()}% COMPLETE",
                        color = Color(0xFF00FFCC),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            } else {
                // SCREEN 3: INTERACTIVE PRACTICE QUIZ SYSTEM
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    // Quiz Progress and Score Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF130E20))
                            .border(1.dp, Color(0xFFBD00FF).copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "QUESTION PROGRESS:  ${currentQuestionIndex + 1} / ${sampleGKQuestions.size}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (currentQuestionIndex + 1).toFloat() / sampleGKQuestions.size },
                                color = Color(0xFFBD00FF),
                                trackColor = Color(0xFF1F1B2C),
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(4.dp)
                                    .clip(CircleShape)
                            )
                        }

                        // Score Counter
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "SCORE",
                                color = Color(0xFF00FFCC),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$playerScore PTS",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // GK Question Text Display Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF0B0715))
                            .border(1.dp, Color(0xFFBD00FF).copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                            .padding(20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column {
                            Text(
                                text = "BENGALI GK QUESTION",
                                color = Color(0xFFBD00FF).copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = currentQuestion.question,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 26.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // OPTIONS SELECTOR BUTTONS (A, B, C, D)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.8f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        currentQuestion.options.forEachIndexed { optIndex, optText ->
                            val isChosen = userSelectedAnswerIndex == optIndex
                            val isCorrect = currentQuestion.correctOptionIndex == optIndex

                            val rowColor by animateColorAsState(
                                targetValue = when {
                                    userSelectedAnswerIndex == null -> Color(0xFF13111C)
                                    isChosen && isCorrect -> Color(0xFF0A311D) // Match Green
                                    isChosen && !isCorrect -> Color(0xFF381016) // Match Red
                                    isCorrect -> Color(0xFF0A311D) // Auto reveal correct option
                                    else -> Color(0xFF13111C).copy(alpha = 0.4f)
                                },
                                label = "rowColor"
                            )

                            val borderGlowColor by animateColorAsState(
                                targetValue = when {
                                    userSelectedAnswerIndex == null -> Color(0xFF1E1E28).copy(alpha = 0.6f)
                                    isChosen && isCorrect -> Color(0xFF00FF55)
                                    isChosen && !isCorrect -> Color(0xFFFF0055)
                                    isCorrect -> Color(0xFF00FF55).copy(alpha = 0.6f)
                                    else -> Color(0xFF1E1E28).copy(alpha = 0.2f)
                                },
                                label = "rowBorder"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(rowColor)
                                    .border(1.dp, borderGlowColor, RoundedCornerShape(12.dp))
                                    .clickable(enabled = userSelectedAnswerIndex == null) {
                                        userSelectedAnswerIndex = optIndex
                                        val correct =
                                            (optIndex == currentQuestion.correctOptionIndex)
                                        isCorrectSelected = correct
                                        if (correct) {
                                            playerScore += 10
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        } else {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                        finishedGKQuestions =
                                            finishedGKQuestions + currentQuestion.id
                                    }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = optText,
                                    color = if (userSelectedAnswerIndex == null) Color.White else Color.White.copy(alpha = 0.9f),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )

                                // Trailing status ticks
                                if (userSelectedAnswerIndex != null) {
                                    if (isCorrect) {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "Correct",
                                            tint = Color(0xFF00FF55),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else if (isChosen) {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "Wrong",
                                            tint = Color(0xFFFF0055),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // QUESTION DETAILED EXPLANATION BOTTOM DRAWER
                    AnimatedVisibility(visible = userSelectedAnswerIndex != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1A1A24))
                                .border(1.dp, Color(0xFF908D9A).copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "EXPLANATION / বিশ্লেষণ",
                                color = Color(0xFF00FFCC),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQuestion.explanation,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // BOTTOM ACTION BAR: PREVIOUS & NEXT CONTROLS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Question Button
                        GlowButton(
                            text = "PREV",
                            onClick = {
                                if (currentQuestionIndex > 0) {
                                    currentQuestionIndex--
                                    userSelectedAnswerIndex = null
                                }
                            },
                            corners = 8.dp,
                            fontSize = 11f,
                            glowColor = Color.DarkGray,
                            borderColor = Color.Gray.copy(alpha = 0.15f),
                            backgroundColor = Color(0xFF121216),
                            isGlowing = false
                        )

                        // Reset button
                        IconButton(
                            onClick = {
                                currentQuestionIndex = 0
                                userSelectedAnswerIndex = null
                                playerScore = 0
                                finishedGKQuestions = emptySet()
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF22121E))
                                .border(1.dp, Color(0xFFFF0055).copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cached,
                                contentDescription = "Reset exam score",
                                tint = Color(0xFFFF0055)
                            )
                        }

                        // Next Question Button
                        GlowButton(
                            text = if (currentQuestionIndex < sampleGKQuestions.size - 1) "NEXT" else "COMPLETED",
                            onClick = {
                                if (currentQuestionIndex < sampleGKQuestions.size - 1) {
                                    currentQuestionIndex++
                                    userSelectedAnswerIndex = null
                                } else {
                                    // Loop back or reset
                                    currentQuestionIndex = 0
                                    userSelectedAnswerIndex = null
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            },
                            corners = 8.dp,
                            fontSize = 11f,
                            glowColor = Color(0xFF00FFCC),
                            borderColor = Color(0xFF00FFCC).copy(alpha = 0.2f),
                            backgroundColor = Color(0xFF071F19)
                        )
                    }
                }
            }
        }
    }
}
