package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.random.Random

private data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float,
    var rotation: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiView(
    modifier: Modifier = Modifier,
    active: Boolean
) {
    if (!active) return

    var particles by remember { mutableStateOf(emptyList<Particle>()) }

    val colors = listOf(
        Color(0xFF00FFCC), // Neo Cyan
        Color(0xFF0088FF), // Vivid Blue
        Color(0xFFBD00FF), // Violet Neon
        Color(0xFFFF0055), // Hot Salmon Pink
        Color(0xFFFFCC00), // Bright Gold
        Color(0xFF00FF55)  // Lime Green
    )

    LaunchedEffect(active) {
        if (active) {
            val random = Random(System.currentTimeMillis())
            // Initialize 60 particles
            particles = List(90) {
                Particle(
                    x = random.nextFloat() * 1000f, // Scale to screen later
                    y = -50f - random.nextFloat() * 300f,
                    vx = (random.nextFloat() - 0.5f) * 15f,
                    vy = random.nextFloat() * 20f + 10f,
                    color = colors[random.nextInt(colors.size)],
                    size = random.nextFloat() * 20f + 10f,
                    rotation = random.nextFloat() * 360f,
                    rotationSpeed = (random.nextFloat() - 0.5f) * 10f
                )
            }

            var lastTime = withFrameMillis { it }
            while (particles.isNotEmpty()) {
                withFrameMillis { frameTime ->
                    val deltaSec = (frameTime - lastTime) / 1000f
                    lastTime = frameTime

                    // Update physics
                    particles = particles.mapNotNull { particle ->
                        particle.x += particle.vx
                        particle.y += particle.vy
                        // Gravity acceleration
                        particle.vy += 0.35f
                        particle.rotation += particle.rotationSpeed

                        // If offscreen at bottom, remove it
                        if (particle.y > 2200f) null else particle
                    }
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val scaleXFactor = size.width / 1000f // Scaling coordinate system
        particles.forEach { p ->
            val finalX = p.x * scaleXFactor
            withTransform({
                rotate(p.rotation, pivot = Offset(finalX, p.y))
            }) {
                drawRect(
                    color = p.color,
                    topLeft = Offset(finalX - p.size / 2, p.y - p.size / 2),
                    size = Size(p.size, p.size)
                )
            }
        }
    }
}
