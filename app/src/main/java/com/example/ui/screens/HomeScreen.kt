package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlowButton

@Composable
fun HomeScreen(
    onNavigateToMathRiddles: () -> Unit,
    onNavigateToPdfScanner: () -> Unit,
    onShowComingSoon: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Custom dark background gradient with retro-future style
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF040406),
                        Color(0xFF0E0E14),
                        Color(0xFF030304)
                    )
                )
            )
            .drawBehind {
                // Background radial glows
                drawCircle(
                    color = Color(0xFFBD00FF).copy(alpha = 0.04f),
                    radius = size.width * 0.7f,
                    center = Offset(size.width * 0.8f, size.height * 0.2f)
                )
                drawCircle(
                    color = Color(0xFF00FFCC).copy(alpha = 0.03f),
                    radius = size.width * 0.8f,
                    center = Offset(size.width * 0.2f, size.height * 0.8f)
                )
            }
    ) {
        // TOP CONTROL BAR: ROW with App status on left and action top-right side icons on right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Status indicator "ONLINE MODE"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF14141A))
                    .border(1.dp, Color(0xFF00FFCC).copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00FFCC))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "STANDALONE",
                    color = Color(0xFF00FFCC),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Top-right side icons: Settings, Achievement, Leaderboard/Crown
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButtonWithCustomGlow(
                    icon = Icons.Default.Settings,
                    contentDescription = "Settings",
                    onClick = { onShowComingSoon("Settings") },
                    colorAccent = Color(0xFFCCCCCC)
                )
                IconButtonWithCustomGlow(
                    icon = Icons.Default.EmojiEvents,
                    contentDescription = "Achievements",
                    onClick = { onShowComingSoon("Achievements") },
                    colorAccent = Color(0xFFFFCC00)
                )
                IconButtonWithCustomGlow(
                    icon = Icons.Default.Leaderboard,
                    contentDescription = "Leaderboard",
                    onClick = { onShowComingSoon("Leaderboard") },
                    colorAccent = Color(0xFFBD00FF)
                )
            }
        }

        // MAIN CONTENT SECTION
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Title - "C Q&A"
            Box(contentAlignment = Alignment.Center) {
                // Background titles shadow text "MATH"
                Text(
                    text = "MATH PRACTICE",
                    color = Color(0xFF1B1B26),
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        letterSpacing = 8.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "C Q&A",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            fontSize = 48.sp,
                            letterSpacing = 4.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ver 0.0.1",
                        color = Color(0xFF00FFCC),
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            letterSpacing = 3.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Large MENU Buttons
            Column(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. MATH RIDDLES
                GlowButton(
                    text = "MATH RIDDLES",
                    onClick = onNavigateToMathRiddles,
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = Color(0xFF00FFCC),
                    borderColor = Color(0xFF00FFCC).copy(alpha = 0.3f),
                    contentColor = Color.White
                )

                // 2. Item of Questions (1 item question answer upload my PDF)
                GlowButton(
                    text = "ITEM OF QUESTIONS",
                    onClick = onNavigateToPdfScanner,
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = Color(0xFFBD00FF),
                    borderColor = Color(0xFFBD00FF).copy(alpha = 0.3f),
                    contentColor = Color.White
                )

                // 3. DAILY CHALLENGES (Coming Soon)
                GlowButton(
                    text = "DAILY CHALLENGES",
                    onClick = { onShowComingSoon("Daily Challenges") },
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = Color(0xFFFFCC00).copy(alpha = 0.5f),
                    borderColor = Color(0xFFFFCC00).copy(alpha = 0.15f),
                    contentColor = Color.Gray,
                    isGlowing = false
                )

                // 4. Other future (Coming Soon)
                GlowButton(
                    text = "OTHER FUTURE",
                    onClick = { onShowComingSoon("Future Features") },
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = Color(0xFFFFFFFF).copy(alpha = 0.15f),
                    borderColor = Color(0xFFFFFFFF).copy(alpha = 0.1f),
                    contentColor = Color.DarkGray,
                    isGlowing = false
                )
            }
        }

        // BOTTOM FOOTER SECTION: FOLLOW US + social icons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "FOLLOW US",
                color = Color.White.copy(alpha = 0.5f),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    letterSpacing = 4.sp
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialIconButton(
                    label = "IG",
                    onClick = { onShowComingSoon("Instagram: @C_QA") },
                    colorAccent = Color(0xFFFF0055)
                )
                SocialIconButton(
                    label = "X",
                    onClick = { onShowComingSoon("X/Twitter: @C_QA") },
                    colorAccent = Color(0xFF00D2FF)
                )
                SocialIconButton(
                    label = "TT",
                    onClick = { onShowComingSoon("TikTok: @C_QA") },
                    colorAccent = Color(0xFF00FF55)
                )
            }
        }
    }
}

@Composable
fun IconButtonWithCustomGlow(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    colorAccent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF14141D))
            .border(1.dp, colorAccent.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = colorAccent,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SocialIconButton(
    label: String,
    onClick: () -> Unit,
    colorAccent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF121217))
            .border(1.dp, colorAccent.copy(alpha = 0.25f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = colorAccent,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        )
    }
}
