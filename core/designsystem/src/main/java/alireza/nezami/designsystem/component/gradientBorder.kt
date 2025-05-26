package alireza.nezami.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


fun Modifier.gradientBorder(
        width: Dp, gradient: Brush, shape: Shape = RoundedCornerShape(0.dp)
) = this.drawWithContent {
    drawContent()

    val strokeWidth = width.toPx()
    val rect = Rect(
        offset = Offset(strokeWidth / 2, strokeWidth / 2), size = Size(
            width = size.width - strokeWidth, height = size.height - strokeWidth
        )
    )

    drawRoundRect(
        brush = gradient,
        topLeft = rect.topLeft,
        size = rect.size,
        style = Stroke(width = strokeWidth),
        cornerRadius = when (shape) {
            is RoundedCornerShape -> androidx.compose.ui.geometry.CornerRadius(
                shape.topStart.toPx(size, this)
            )

            else -> androidx.compose.ui.geometry.CornerRadius.Zero
        }
    )
}

fun Modifier.gradientBorderAlt(
        width: Dp, gradient: Brush, shape: Shape = RoundedCornerShape(0.dp)
) = this.border(
    width = width, brush = gradient, shape = shape
)

object GradientColorProvider {

    private val beautifulGradients = listOf(
        listOf(Color(0xFFFF6B6B), Color(0xFFFFE66D)),
        listOf(Color(0xFFFF8A80), Color(0xFFFF80AB)),
        listOf(Color(0xFFFFAB91), Color(0xFFFFCC02)),

        listOf(Color(0xFF4FC3F7), Color(0xFF29B6F6)),
        listOf(Color(0xFF81D4FA), Color(0xFF4DD0E1)),
        listOf(Color(0xFF26C6DA), Color(0xFF00BCD4)),

        listOf(Color(0xFFBA68C8), Color(0xFF9C27B0)),
        listOf(Color(0xFFCE93D8), Color(0xFFAB47BC)),
        listOf(Color(0xFF8E24AA), Color(0xFF7B1FA2)),

        listOf(Color(0xFF66BB6A), Color(0xFF4CAF50)),
        listOf(Color(0xFF81C784), Color(0xFF66BB6A)),
        listOf(Color(0xFF26A69A), Color(0xFF00897B)),

        listOf(Color(0xFFFF5722), Color(0xFFFF9800)),
        listOf(Color(0xFFFF7043), Color(0xFFFFAB40)),
        listOf(Color(0xFFE91E63), Color(0xFFFF5722)),

        listOf(Color(0xFF42A5F5), Color(0xFF1E88E5)),
        listOf(Color(0xFF5C6BC0), Color(0xFF3F51B5)),
        listOf(Color(0xFF7E57C2), Color(0xFF673AB7)),

        listOf(Color(0xFFFFA726), Color(0xFFFF7043)),
        listOf(Color(0xFFFFCA28), Color(0xFFFFA000)),
        listOf(Color(0xFFEF5350), Color(0xFFE53935)),

        listOf(Color(0xFF8BC34A), Color(0xFF689F38)),
        listOf(Color(0xFFCDDC39), Color(0xFFAFBB42)),
        listOf(Color(0xFF00E676), Color(0xFF00C853)),

        listOf(Color(0xFF90A4AE), Color(0xFF607D8B)),
        listOf(Color(0xFFBCAAA4), Color(0xFF8D6E63)),
        listOf(Color(0xFFA1887F), Color(0xFF795548)),

        listOf(Color(0xFFE040FB), Color(0xFFAB47BC)),
        listOf(Color(0xFF40C4FF), Color(0xFF2196F3)),
        listOf(Color(0xFF64FFDA), Color(0xFF1DE9B6)),

        listOf(Color(0xFFF8BBD9), Color(0xFFF48FB1)),
        listOf(Color(0xFFB39DDB), Color(0xFF9575CD)),
        listOf(Color(0xFF90CAF9), Color(0xFF64B5F6)),

        listOf(Color(0xFF00E5FF), Color(0xFF00B0FF)),
        listOf(Color(0xFF76FF03), Color(0xFF64DD17)),
        listOf(Color(0xFFFF1744), Color(0xFFD50000)),
    )

    fun getRandomGradient(): Brush {
        val colors = beautifulGradients.random()
        return Brush.linearGradient(
            colors = colors,
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    }

    fun getRandomDiagonalGradient(): Brush {
        val colors = beautifulGradients.random()
        return Brush.linearGradient(
            colors = colors,
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    }

    fun getRandomHorizontalGradient(): Brush {
        val colors = beautifulGradients.random()
        return Brush.horizontalGradient(colors = colors)
    }

    fun getRandomVerticalGradient(): Brush {
        val colors = beautifulGradients.random()
        return Brush.verticalGradient(colors = colors)
    }

    fun getRandomRadialGradient(): Brush {
        val colors = beautifulGradients.random()
        return Brush.radialGradient(colors = colors)
    }

    fun getRandomGradientByType(type: GradientType): Brush {
        val gradientRange = when (type) {
            GradientType.SUNSET -> 0..2
            GradientType.OCEAN -> 3..5
            GradientType.PURPLE -> 6..8
            GradientType.GREEN -> 9..11
            GradientType.FIRE -> 12..14
            GradientType.COOL -> 15..17
            GradientType.WARM -> 18..20
            GradientType.NATURE -> 21..23
            GradientType.ELEGANT -> 24..26
            GradientType.VIBRANT -> 27..29
            GradientType.PASTEL -> 30..32
            GradientType.NEON -> 33..35
        }

        val colors = beautifulGradients[gradientRange.random()]
        return Brush.linearGradient(
            colors = colors,
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    }

    fun getAllGradients(): List<Brush> {
        return beautifulGradients.map { colors ->
            Brush.linearGradient(
                colors = colors,
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
            )
        }
    }
}

enum class GradientType {
    SUNSET, OCEAN, PURPLE, GREEN, FIRE, COOL, WARM, NATURE, ELEGANT, VIBRANT, PASTEL, NEON
}

fun List<Color>.toLinearGradient(): Brush {
    return Brush.linearGradient(
        colors = this,
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
}

fun List<Color>.toHorizontalGradient(): Brush {
    return Brush.horizontalGradient(colors = this)
}

fun List<Color>.toVerticalGradient(): Brush {
    return Brush.verticalGradient(colors = this)
}

fun List<Color>.toRadialGradient(): Brush {
    return Brush.radialGradient(colors = this)
}