package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0xFF00FFCC),
    backgroundColor: Color = Color(0xFF14141A),
    borderColor: Color = Color(0xFF00FFCC).copy(alpha = 0.5f),
    contentColor: Color = Color.White,
    fontSize: Float = 16f,
    corners: Dp = 12.dp,
    fontWeight: FontWeight = FontWeight.Bold,
    isGlowing: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.94f else 1.0f, label = "buttonScale")
    val haptic = LocalHapticFeedback.current

    val finalGlowColor = if (isPressed) glowColor else glowColor.copy(alpha = 0.7f)

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(corners))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No default ripple to keep the pure neon glow look
                onClick = onClick
            )
            .drawBehind {
                if (isGlowing) {
                    drawRoundRect(
                        color = finalGlowColor.copy(alpha = 0.12f),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(corners.toPx(), corners.toPx())
                    )
                }
            }
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor,
                        if (isPressed) finalGlowColor else finalGlowColor.copy(alpha = 0.3f),
                        borderColor
                    )
                ),
                shape = RoundedCornerShape(corners)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = fontWeight,
                fontSize = fontSize.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}
